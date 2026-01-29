package ec.com.tecnointel.soem.documeElec.tareas;

/**
 Esta clase es de prueba
 se intento enviar los docuemtos electronicos en periodos de tiempo
 se puede usar para consultar los documentos pendientes de autorizar
 */

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.egreso.listaInt.EgresoListaInt;
import ec.com.tecnointel.soem.egreso.modelo.EgreNota;
import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.egreso.registroInt.EgresoRegisInt;
import ec.com.tecnointel.soem.ingreso.registroInt.RetencionRegisInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuEgre;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.serWebSri.registroInt.EnvioComprobantesWsInt;
import jakarta.inject.Inject;

//@Dependent
public class TareaEnviarHoraFija implements Runnable {
	
//	@PersistenceContext
//	private EntityManager entityManager;

//	private String detalleProceso = null;
	
	private File archivoFirmado;
	private Sucursal sucursal;
	private String codDoc;
	private String claveAcceso;
	
	private String proxyIp;
	private String proxyPuerto;
	private String ambiente;
	private String urlProduccion;
	private String urlPruebas;
	private String nombreServicio;
	
	private Object object;
		
	@Inject
	EgresoRegisInt egresoRegis;

	@Inject
	EgresoListaInt egresoLista;
	
	@Inject
	RetencionRegisInt retencionRegis;

	@Inject
	EnvioComprobantesWsInt envioComprobantesWs;

	@Override
	public void run() {
		
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Evento disparado...");
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Evento disparado...");
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Evento disparado...");
	
//		LocalDateTime fechaHoraDesde = restarMinutosAFechaActual(23040); // 4320
//		LocalDateTime fechaHoraHasta = restarMinutosAFechaActual(15);
		
		LocalDate fechaDesde = LocalDate.now();
		LocalDate fechaHasta = LocalDate.now();

		Set<Integer> sucursals = new HashSet<>();
		Egreso egresoBuscar = new Egreso();
		
		DocuEgre docuEgre = new DocuEgre();
		docuEgre.setDocumeElec("Ninguno");
		
		egresoBuscar.setDocuEgre(docuEgre);
		egresoBuscar.setEstado("PR");
		egresoBuscar.setEstadoDocuElec("AUTORIZADO");
		
		List<Egreso> egresos = new ArrayList<Egreso>();
		try {
			egresos = egresoLista.buscar2(egresoBuscar, fechaDesde, fechaHasta, null, sucursals);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int contador = 0;

		for (Egreso egreso : egresos) {
			contador ++;
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<< Egreso id " + egreso.getEgresoId() + " " + egreso.getEstado() + " " + egreso.getEstadoDocuElec() + " " + egreso.getDocuEgre().getDocumeElec());
			Set<EgreNota>  egreNotas = egreso.getEgreNotas();
			System.out.println("Notasssssssssssssssssssssssssssssssssss " + egreNotas.size());
		}
		
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<< Contador " + contador);
	}
	
	public static LocalDateTime restarMinutosAFechaActual(long minutos) {
        // Obtener la fecha y hora actual
        LocalDateTime localDateTime = LocalDateTime.now();

        LocalDateTime fechaHoraEmisHasta = localDateTime.minusMinutes(minutos);
        
        return fechaHoraEmisHasta;
	}
//	@Override
//	public void run() {
//					
//		RespuestaSolicitud respuestaSolicitud = new RespuestaSolicitud();
//		
//		try {
//			respuestaSolicitud = envioComprobantesWs.obtenerRespuestaEnvio1(archivoFirmado, sucursal.getRuc(), codDoc,
//					claveAcceso,
//					envioComprobantesWs.devuelveUrlWs(proxyIp, proxyPuerto, ambiente, urlProduccion, urlPruebas,
//							nombreServicio));
//		} catch (Exception e) {
//						
//			e.printStackTrace();
//		}
//		
//		if (respuestaSolicitud.getEstado().equals("DEVUELTA")) {
//			
////			Obtiene motivos de devolucion
//			try {
//				this.detalleProceso  = envioComprobantesWs.obtenerMensajeRespuesta(respuestaSolicitud);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		} 
//
//		if (this.object instanceof Egreso) {
//			
//			try {
//				Egreso egreso = (Egreso) object;
//		
////				Poner motivo rechazo en caso que exista
//				if (this.detalleProceso != null) {
//					egreso.setMotivoRech(this.detalleProceso);
//				}
//				
////				Modificar estado y grabar documento
//				egreso.setEstadoDocuElec(respuestaSolicitud.getEstado());
//				egresoRegis.modificar(egreso);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			
//		} else if (this.object instanceof Retencion) {
//			
//			try {
//				
//				Retencion retencion = (Retencion) object;
//
////				Poner motivo rechazo en caso que exista
//				if (this.detalleProceso != null) {
//					retencion.setMotivoRech(this.detalleProceso);
//				}
//				
////				Modificar estado y grabar documento
//				retencion.setEstadoDocuElec(respuestaSolicitud.getEstado());
//				retencionRegis.modificar(retencion);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}

	public File getArchivoFirmado() {
		return archivoFirmado;
	}

	public void setArchivoFirmado(File archivoFirmado) {
		this.archivoFirmado = archivoFirmado;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public String getCodDoc() {
		return codDoc;
	}

	public void setCodDoc(String codDoc) {
		this.codDoc = codDoc;
	}

	public String getClaveAcceso() {
		return claveAcceso;
	}

	public void setClaveAcceso(String claveAcceso) {
		this.claveAcceso = claveAcceso;
	}

	public String getProxyIp() {
		return proxyIp;
	}

	public void setProxyIp(String proxyIp) {
		this.proxyIp = proxyIp;
	}

	public String getProxyPuerto() {
		return proxyPuerto;
	}

	public void setProxyPuerto(String proxyPuerto) {
		this.proxyPuerto = proxyPuerto;
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public String getUrlProduccion() {
		return urlProduccion;
	}

	public void setUrlProduccion(String urlProduccion) {
		this.urlProduccion = urlProduccion;
	}

	public String getUrlPruebas() {
		return urlPruebas;
	}

	public void setUrlPruebas(String urlPruebas) {
		this.urlPruebas = urlPruebas;
	}

	public String getNombreServicio() {
		return nombreServicio;
	}

	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

//	public Integer getEgresoId() {
//		return documentoId;
//	}
//
//	public void setEgresoId(Integer egresoId) {
//		this.documentoId = egresoId;
//	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}
