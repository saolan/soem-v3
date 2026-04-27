package ec.com.saolan.soem.sri.aplicacion.importacion.retencion;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import ec.com.saolan.soem.compartido.cache.ParametroConexionSriCache;
import ec.com.saolan.soem.sri.infraestructura.importacion.ImportarDocumeElecSriParametros;
import ec.com.saolan.soem.sri.infraestructura.importacion.ImportarDocumeElecSriServicio;
import ec.com.saolan.soem.sri.infraestructura.unmarshaller.DocumentoSriUnmarshaller;
import ec.com.saolan.soem.sri.infraestructura.unmarshaller.RetencionSriUnmarshaller;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.ComprobanteRetencion;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.DocSustento;
import ec.com.tecnointel.soem.ingreso.modelo.ReteDeta;
import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import ec.com.tecnointel.soem.serWebClientSri.general.AutorizacionDTO;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class ImportarRetencionSriServicio extends ImportarDocumeElecSriServicio<Retencion, ComprobanteRetencion>
		implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	RetencionSriUnmarshaller retencionSriUnmarshaller;

	@Inject
	ParametroConexionSriCache parametroDocuElectronicoCache;

	@Override
	protected DocumentoSriUnmarshaller<ComprobanteRetencion> getUnmarshaller() {
		return retencionSriUnmarshaller;
	}

	/*
	 * En este caso recive esta clase (importarDocumeElecSriParametros) vacia, no es
	 * necesario ningun parametro
	 * 
	 */
	@Override
	protected Retencion mapearDocumento(ComprobanteRetencion comprobanteRetencion, AutorizacionDTO autorizacionDTO,
			ImportarDocumeElecSriParametros importarDocumeElecSriParametros) {
		return mapearRetencion(comprobanteRetencion, autorizacionDTO, importarDocumeElecSriParametros);
	}

	@Override
	protected String codigoDocumentoEsperado() {
		return CODIGO_RETENCION;
	}
	
//	LLama al metodo descargar de la clase abstracta
	public Retencion importarRetencionSri(String claveAcceso,
			ImportarDocumeElecSriParametros importarDocumeElecSriParametros) {
		return importar(claveAcceso, importarDocumeElecSriParametros);
	}

	public Retencion mapearRetencion(ComprobanteRetencion comprobanteRetencion, AutorizacionDTO autorizacionDTO,
			ImportarDocumeElecSriParametros importarDocumeElecSriParametros) {
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
				reteDeta.setReteDetaTotal(reteDetaInfo.getValorRetenido());

				reteDetas.add(reteDeta);
			}
		}
		return reteDetas;
	}

	private String definirTipoImpuesto(String codigo) {
		String nombre = CODIGO_IMPUESTO.get(codigo);
		if (nombre == null) {
			LOGGER.log(Level.WARNING, "Código de impuesto no mapeado: {0}", codigo);
			return "Desconocido";
		}
		return nombre;
	}
}
