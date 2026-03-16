package ec.com.saolan.soem.sri.infraestructura.aplicacion.retencion;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import ec.com.saolan.soem.sri.infraestructura.cliente.SriClienteWs;
import ec.com.saolan.soem.sri.infraestructura.unmarshaller.RetencionSriUnmarshaller;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.ComprobanteRetencion;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.DocSustento;
import ec.com.tecnointel.soem.ingreso.modelo.ReteDeta;
import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import ec.com.tecnointel.soem.serWebClientSri.autorizacion.RespuestaComprobante;
import ec.com.tecnointel.soem.serWebClientSri.general.AutorizacionDTO;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@Stateless
public class RetencionSriServicio implements IRetencionSriServicio, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	SriClienteWs sriClienteWs;

	@Inject
	RetencionSriUnmarshaller unmarshaller;

	@Override
	@Transactional
	public Retencion procesarRetencionSri(String claveAcceso) {

		Retencion retencion = new Retencion();

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		ComprobanteRetencion comprobanteRetencion = new ComprobanteRetencion();

		String xmlRetencion = null;

		try {

			sriClienteWs.cargarParametros();

			RespuestaComprobante respuestaComprobante = sriClienteWs.verificarAutorizacion(claveAcceso);

			if (respuestaComprobante.getAutorizaciones().getAutorizacion().isEmpty()) {
				return retencion;
			}

			AutorizacionDTO autorizacionDTO = sriClienteWs.obtenerEstadoAutorizacion(respuestaComprobante);

			xmlRetencion = respuestaComprobante.getAutorizaciones().getAutorizacion().stream().findFirst()
					.orElseThrow(() -> new RuntimeException("Sin autorización para clave: " + claveAcceso))
					.getComprobante(); // aquí viene el XML crudo

			// 2. Unmarshall XML → objeto Java
			comprobanteRetencion = unmarshaller.unmarshall(xmlRetencion);

			// 3. Mapear a entidades de dominio
//			Retencion retencion = mapper.toRetencion(retencionXml);
//			List<ReteDeta> detalles = mapper.toReteDeta(retencionXml, retencion);

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
			
			// 4. Persistir
//			retencionRepo.save(retencion);
//			reteDetaRepo.saveAll(detalles);
		} catch (

		Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		return comprobanteRetencion;
		return retencion;
	}

}
