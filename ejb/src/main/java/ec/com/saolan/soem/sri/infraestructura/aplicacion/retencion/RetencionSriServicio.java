package ec.com.saolan.soem.sri.infraestructura.aplicacion.retencion;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import ec.com.saolan.soem.sri.infraestructura.unmarshaller.RetencionSriUnmarshaller;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.ComprobanteRetencion;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.DocSustento;
import ec.com.tecnointel.soem.ingreso.modelo.ReteDeta;
import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.registroInt.ParametroRegisInt;
import ec.com.tecnointel.soem.serWebClientSri.autorizacion.RespuestaComprobante;
import ec.com.tecnointel.soem.serWebClientSri.general.AutorizacionDTO;
import ec.com.tecnointel.soem.serWebSri.registroInt.AutorizacionComprobantesWsInt;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class RetencionSriServicio implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(RetencionSriServicio.class.getName());

	@Inject
	AutorizacionComprobantesWsInt autorizacionComprobantes;

	@Inject
	RetencionSriUnmarshaller unmarshaller;

	@Inject
	ParametroRegisInt parametroRegis;

	Parametro parametroFilasPagina = new Parametro();
	Parametro parametroProxyIp = new Parametro();
	Parametro parametroProxyPuerto = new Parametro();
	Parametro parametroUrlProduccion = new Parametro();
	Parametro parametroUrlPruebas = new Parametro();
	Parametro parametroRutaDescargados = new Parametro();

	public void cargarParametros() throws Exception {
		parametroFilasPagina = parametroRegis.buscarPorId(Parametro.class, 6100);
		parametroProxyIp = parametroRegis.buscarPorId(Parametro.class, 3211);
		parametroProxyPuerto = parametroRegis.buscarPorId(Parametro.class, 3212);
		parametroUrlProduccion = parametroRegis.buscarPorId(Parametro.class, 3220);
		parametroUrlPruebas = parametroRegis.buscarPorId(Parametro.class, 3221);
		parametroRutaDescargados = parametroRegis.buscarPorId(Parametro.class, 4251);
	}

	public Retencion descargarRetencionSri(String claveAcce) {

		Retencion retencion = null;

		ComprobanteRetencion comprobanteRetencion = new ComprobanteRetencion();

		String xmlRetencion = null;

		try {
			cargarParametros();

			RespuestaComprobante respuestaComprobante = autorizacionComprobantes.autorizarComprobante(parametroProxyIp.getDescri(),
					parametroProxyPuerto.getDescri(), "2", parametroUrlProduccion.getDescri(),
					parametroUrlPruebas.getDescri(), "AutorizacionComprobantesOffline", claveAcce);

			if (respuestaComprobante.getAutorizaciones().getAutorizacion().isEmpty()) {
				return null;
			}

			AutorizacionDTO autorizacionDTO = autorizacionComprobantes.obtenerEstadoAutorizacion(respuestaComprobante);
			xmlRetencion = cargarXmlRetencion(claveAcce, respuestaComprobante);

			// 2. Unmarshall XML → objeto Java
			comprobanteRetencion = unmarshaller.unmarshall(xmlRetencion);

			// 3. Mapear a entidades de dominio
			retencion = construirRetencion(comprobanteRetencion, autorizacionDTO);

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

	public Retencion construirRetencion(ComprobanteRetencion comprobanteRetencion, AutorizacionDTO autorizacionDTO) {

		Retencion retencion = new Retencion();

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		retencion.setFechaRegi(LocalDate.now());
		retencion.setFechaHoraRegi(LocalDateTime.now());
		retencion.setFechaEmis(
				LocalDate.parse(comprobanteRetencion.getInfoCompRetencion().getFechaEmision(), dateTimeFormatter));
		retencion.setFechaHoraEmis(
				LocalDate.parse(comprobanteRetencion.getInfoCompRetencion().getFechaEmision(), dateTimeFormatter)
						.atStartOfDay());
		retencion.setFechaAuto(autorizacionDTO.getAutorizacion().getFechaAutorizacion().toGregorianCalendar()
				.toZonedDateTime().toLocalDateTime());
		retencion.setSerie1(comprobanteRetencion.getInfoTributaria().getEstab());
		retencion.setSerie2(comprobanteRetencion.getInfoTributaria().getPtoEmi());
		retencion.setNumero(Integer.valueOf(comprobanteRetencion.getInfoTributaria().getSecuencial()));
		retencion.setClaveAcce(comprobanteRetencion.getInfoTributaria().getClaveAcceso());
		retencion.setAutori(comprobanteRetencion.getInfoTributaria().getClaveAcceso());

		retencion.setDocumeElec(true);
		retencion.setEstado("PR");
		retencion.setEstadoDocuElec("AUTORIZADO");

		comprobanteRetencion.getDocsSustento().getDocSustento();

		Set<ReteDeta> reteDetas = new HashSet<ReteDeta>();

		for (DocSustento docSustentoRete : comprobanteRetencion.getDocsSustento().getDocSustento()) {
			for (ec.com.tecnointel.soem.documeElec.modelo.retencion.Retencion reteDetaInfo : docSustentoRete
					.getRetenciones().getRetencion()) {

				ReteDeta reteDeta = new ReteDeta();

				reteDeta.setRetencion(retencion);

				reteDeta.setEjerciFisc(retencion.getFechaEmis());
				if (reteDetaInfo.getCodigo().equals("1")) {
					reteDeta.setImpues("Renta");
				} else if (reteDetaInfo.getCodigo().equals("2")) {
					reteDeta.setImpues("Iva");
				} else if (reteDetaInfo.getCodigo().equals("6")) {
					reteDeta.setImpues("ISD");
				} else {
					System.out.println("Codigo no disponible");
				}

				reteDeta.setCodigoImpu(reteDetaInfo.getCodigoRetencion());
				reteDeta.setPorcen(reteDetaInfo.getPorcentajeRetener());
				reteDeta.setBase(reteDetaInfo.getBaseImponible());

				reteDetas.add(reteDeta);
			}
		}

		retencion.setReteDetas(reteDetas);

		return retencion;
	}
}
