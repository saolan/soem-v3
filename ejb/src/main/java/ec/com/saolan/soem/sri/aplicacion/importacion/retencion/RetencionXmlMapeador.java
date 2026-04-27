package ec.com.saolan.soem.sri.aplicacion.importacion.retencion;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import ec.com.tecnointel.soem.documeElec.modelo.retencion.ComprobanteRetencion;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.DocSustento;
import ec.com.tecnointel.soem.ingreso.modelo.ReteDeta;
import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import ec.com.tecnointel.soem.serWebClientSri.general.AutorizacionDTO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RetencionXmlMapeador implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(RetencionXmlMapeador.class.getName());
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final Map<String, String> CODIGO_IMPUESTO = Map.of("1", "Renta", "2", "Iva", "6", "ISD");
	private static final String ESTADO_PROCESADO = "PR";
	private static final String ESTADO_DOCUMENTO_ELECTRONICO_AUTORIZADO = "AUTORIZADO";

	public Retencion mapearRetencion(ComprobanteRetencion comprobanteRetencion, AutorizacionDTO autorizacionDTO) {
		Retencion retencion = mapearCabecera(comprobanteRetencion, autorizacionDTO);
		retencion.setReteDetas(mapearReteDetas(comprobanteRetencion, retencion));
		return retencion;
	}

	private Retencion mapearCabecera(ComprobanteRetencion comprobanteRetencion, AutorizacionDTO autorizacionDTO) {

		Retencion retencion = new Retencion();

		LocalDate fechaEmision = LocalDate.parse(comprobanteRetencion.getInfoCompRetencion().getFechaEmision(),
				DATE_TIME_FORMATTER);

		retencion.setFechaRegi(LocalDate.now());
		retencion.setFechaHoraRegi(LocalDateTime.now());
		retencion.setFechaEmis(fechaEmision);
		retencion.setFechaHoraEmis(fechaEmision.atStartOfDay());
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
		Set<ReteDeta> reteDetas = new HashSet<>();

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
