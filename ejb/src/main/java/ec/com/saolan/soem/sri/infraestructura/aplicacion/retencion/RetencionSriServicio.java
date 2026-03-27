package ec.com.saolan.soem.sri.infraestructura.aplicacion.retencion;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import ec.com.saolan.soem.compartido.cache.ParametroDocuElectronicoCache;
import ec.com.saolan.soem.sri.infraestructura.unmarshaller.RetencionSriUnmarshaller;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.ComprobanteRetencion;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.DocSustento;
import ec.com.tecnointel.soem.ingreso.modelo.ReteDeta;
import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import ec.com.tecnointel.soem.serWebClientSri.autorizacion.RespuestaComprobante;
import ec.com.tecnointel.soem.serWebClientSri.general.AutorizacionDTO;
import ec.com.tecnointel.soem.serWebSri.registroInt.AutorizacionComprobantesWsInt;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class RetencionSriServicio implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(RetencionSriServicio.class.getName());

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final String AMBIENTE_PRODUCCION = "2";
	private static final String NOMBRE_SERVICIO = "AutorizacionComprobantesOffline";
	private static final String ESTADO_PROCESADO = "PR";
	private static final String ESTADO_DOCUMENTO_ELECTRONICO_AUTORIZADO = "AUTORIZADO";
	private static final Map<String, String> CODIGO_IMPUESTO = Map.of("1", "Renta", "2", "Iva", "6", "ISD");

	@Inject
	AutorizacionComprobantesWsInt autorizacionComprobantes;

	@Inject
	RetencionSriUnmarshaller unmarshaller;

	@Inject
	ParametroDocuElectronicoCache parametroDocuElectronicoCache;

	public Retencion descargarRetencionSri(String claveAcce) {

		Retencion retencion = null;

		ComprobanteRetencion comprobanteRetencion = new ComprobanteRetencion();

		String xmlRetencion = null;

		try {

			RespuestaComprobante respuestaComprobante = autorizacionComprobantes.autorizarComprobante(
					parametroDocuElectronicoCache.getProxyIp().getDescri(),
					parametroDocuElectronicoCache.getProxyPuerto().getDescri(), AMBIENTE_PRODUCCION,
					parametroDocuElectronicoCache.getUrlProduccion().getDescri(),
					parametroDocuElectronicoCache.getUrlPruebas().getDescri(), NOMBRE_SERVICIO, claveAcce);

			if (respuestaComprobante.getAutorizaciones().getAutorizacion().isEmpty()) {
				return null;
			}

			AutorizacionDTO autorizacionDTO = autorizacionComprobantes.obtenerEstadoAutorizacion(respuestaComprobante);
			xmlRetencion = cargarXmlRetencion(claveAcce, respuestaComprobante);

			// 2. Unmarshall XML → objeto Java
			comprobanteRetencion = unmarshaller.unmarshall(xmlRetencion);

			// 3. Mapear a entidades de dominio
			retencion = mapearRetencion(comprobanteRetencion, autorizacionDTO);

		} catch (

		Exception e) {
			LOGGER.log(Level.SEVERE, "Error: Retencion del SRI no se ha procesado", e);
			e.printStackTrace();
		}

		return retencion;
	}

	public String cargarXmlRetencion(String claveAcce, RespuestaComprobante respuestaComprobante) {
		return respuestaComprobante.getAutorizaciones().getAutorizacion().stream().findFirst()
				.orElseThrow(() -> new RuntimeException("Sin autorización para clave: " + claveAcce)).getComprobante(); // aquí
	}

	public Retencion mapearRetencion(ComprobanteRetencion comprobanteRetencion, AutorizacionDTO autorizacionDTO) {
		Retencion retencion = mapearCabecera(comprobanteRetencion, autorizacionDTO);
		retencion.setReteDetas(mapearReteDetas(comprobanteRetencion, retencion));
		return retencion;
	}

	private Retencion mapearCabecera(ComprobanteRetencion comprobanteRetencion, AutorizacionDTO autorizacionDTO) {

		Retencion retencion = new Retencion();

		retencion.setFechaRegi(LocalDate.now());
		retencion.setFechaHoraRegi(LocalDateTime.now());
		retencion.setFechaEmis(
				LocalDate.parse(comprobanteRetencion.getInfoCompRetencion().getFechaEmision(), DATE_TIME_FORMATTER));
		retencion.setFechaHoraEmis(
				LocalDate.parse(comprobanteRetencion.getInfoCompRetencion().getFechaEmision(), DATE_TIME_FORMATTER)
						.atStartOfDay());
		retencion.setFechaAuto(autorizacionDTO.getAutorizacion().getFechaAutorizacion().toGregorianCalendar()
				.toZonedDateTime().toLocalDateTime());
		retencion.setSerie1(comprobanteRetencion.getInfoTributaria().getEstab());
		retencion.setSerie2(comprobanteRetencion.getInfoTributaria().getPtoEmi());
		retencion.setNumero(Integer.valueOf(comprobanteRetencion.getInfoTributaria().getSecuencial()));
		retencion.setClaveAcce(comprobanteRetencion.getInfoTributaria().getClaveAcceso());
		retencion.setAutori(comprobanteRetencion.getInfoTributaria().getClaveAcceso());

		retencion.setDocumeElec(true);
		retencion.setEstado(ESTADO_PROCESADO);
		retencion.setEstadoDocuElec(ESTADO_DOCUMENTO_ELECTRONICO_AUTORIZADO);

		return retencion;
	}

	private Set<ReteDeta> mapearReteDetas(ComprobanteRetencion comprobanteRetencion, Retencion retencion) {
		Set<ReteDeta> reteDetas = new HashSet<ReteDeta>();

		for (DocSustento docSustentoRete : comprobanteRetencion.getDocsSustento().getDocSustento()) {
			for (ec.com.tecnointel.soem.documeElec.modelo.retencion.Retencion reteDetaInfo : docSustentoRete
					.getRetenciones().getRetencion()) {

				String tipoImpuesto = definirTipoImpuesto(reteDetaInfo.getCodigo());

				ReteDeta reteDeta = new ReteDeta();
				reteDeta.setRetencion(retencion);
				reteDeta.setEjerciFisc(retencion.getFechaEmis());
				reteDeta.setImpues(tipoImpuesto);
				reteDeta.setCodigoImpu(reteDetaInfo.getCodigoRetencion());
				reteDeta.setPorcen(reteDetaInfo.getPorcentajeRetener());
				reteDeta.setBase(reteDetaInfo.getBaseImponible());

				reteDetas.add(reteDeta);
			}
		}
		return reteDetas;
	}

	private String definirTipoImpuesto(String codigo) {
		String nombre = CODIGO_IMPUESTO.get(codigo);
		if (nombre == null) {
			LOGGER.warning(() -> "Código de impuesto no reconocido: " + codigo);
			return "Desconocido";
		}
		return nombre;
	}
}
