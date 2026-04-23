package ec.com.saolan.soem.sri.infraestructura.aplicacion.compartido;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import ec.com.saolan.soem.compartido.cache.ParametroDocuElectronicoCache;
import ec.com.saolan.soem.compartido.excepcion.InfraestructuraExcepcion;
import ec.com.saolan.soem.compartido.excepcion.IntegracionExcepcion;
import ec.com.saolan.soem.compartido.excepcion.ValidacionNegocioExcepcion;
import ec.com.tecnointel.soem.serWebClientSri.autorizacion.RespuestaComprobante;
import ec.com.tecnointel.soem.serWebClientSri.general.AutorizacionDTO;
import ec.com.tecnointel.soem.serWebSri.registroInt.AutorizacionComprobantesWsInt;
import jakarta.inject.Inject;

public abstract class ImportarDocumeElecSriServicio<T, D> implements ImportarDocumeElecSri<T>, Serializable {

	private static final long serialVersionUID = 1L;

	protected static final Logger LOGGER = Logger.getLogger(ImportarDocumeElecSriServicio.class.getName());

	protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	protected static final String AMBIENTE_PRODUCCION = "2";
	protected static final String NOMBRE_SERVICIO = "AutorizacionComprobantesOffline";
	protected static final String ESTADO_PROCESADO = "PR";
	protected static final String ESTADO_DOCUMENTO_ELECTRONICO_AUTORIZADO = "AUTORIZADO";
	protected static final Map<String, String> CODIGO_IMPUESTO = Map.of("1", "Renta", "2", "Iva", "6", "ISD");

	protected static final String CODIGO_FACTURA = "01";
	protected static final String CODIGO_RETENCION = "07";

	@Inject
	protected AutorizacionComprobantesWsInt autorizacionComprobantes;

	@Inject
	protected ParametroDocuElectronicoCache parametroDocuElectronicoCache;

	/**
	 * Unmarshaller específico del tipo de comprobante.
	 */
	protected abstract DocumentoSriUnmarshaller<D> getUnmarshaller();

	/**
	 * Convierte el XML unmarshalleado + autorización en la entidad de dominio.
	 */
	protected abstract T mapearDocumento(D comprobante, AutorizacionDTO autorizacionDTO,
			ImportarDocumeElecSriParametros importarDocumeElecSriParametros);

	protected abstract String codigoDocumentoEsperado();

	/**
	 * Parametros: String claveAcceso. con la que se descargara el documento desde
	 * el SRI ImportarDocumeElecSriParametros importarDocumeElecSriParametros.
	 * Parametros adicionales que se ppueden usar en cualquier implementacion
	 * especifica por ejemplo al descargar facturas
	 */
	@Override
	public T importar(String claveAcceso, ImportarDocumeElecSriParametros importarDocumeElecSriParametros) {
		try {
			RespuestaComprobante respuesta = autorizacionComprobantes.autorizarComprobante(
					parametroDocuElectronicoCache.getProxyIp().getDescri(),
					parametroDocuElectronicoCache.getProxyPuerto().getDescri(), AMBIENTE_PRODUCCION,
					parametroDocuElectronicoCache.getUrlProduccion().getDescri(),
					parametroDocuElectronicoCache.getUrlPruebas().getDescri(), NOMBRE_SERVICIO, claveAcceso);

			if (respuesta.getAutorizaciones().getAutorizacion().isEmpty()) {
				return null;
			}

			AutorizacionDTO autorizacionDTO = autorizacionComprobantes.obtenerEstadoAutorizacion(respuesta);
			String xml = cargarXmlDocumento(claveAcceso, respuesta);
			validarTipoDocumento(claveAcceso);
			D comprobante = getUnmarshaller().unmarshallDocumento(xml);

			return mapearDocumento(comprobante, autorizacionDTO, importarDocumeElecSriParametros);
		} catch (ValidacionNegocioExcepcion e) {
			throw e;
		} catch (IntegracionExcepcion | InfraestructuraExcepcion e) {
			throw e;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error al descargar documento SRI clave: " + claveAcceso, e);
			return null;
		}
	}

	protected void validarTipoDocumento(String claveAcceso) {
		String codigoDocumento = obtenerCodigoDocumento(claveAcceso);

		if (!codigoDocumentoEsperado().equals(codigoDocumento)) {
			throw new ValidacionNegocioExcepcion(
					"La clave de acceso " + claveAcceso + " corresponde a un documento código " + codigoDocumento
							+ " y no al codigo " + codigoDocumentoEsperado() + ".");
		}
	}

	protected String obtenerCodigoDocumento(String claveAcceso) {
		if (claveAcceso == null || claveAcceso.isBlank()) {
			throw new IntegracionExcepcion("La clave de acceso es obligatoria");
		}

		if (claveAcceso.length() < 10) {
			throw new IntegracionExcepcion("La clave de acceso no tiene una longitud válida");
		}

		return claveAcceso.substring(8, 10);
	}

	protected String cargarXmlDocumento(String claveAcceso, RespuestaComprobante respuestaComprobante) {
		return respuestaComprobante.getAutorizaciones().getAutorizacion().stream().findFirst().orElseThrow(
				() -> new IntegracionExcepcion("No existe autorización para la clave de acceso: " + claveAcceso))
				.getComprobante();
	}
}
