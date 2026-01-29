package ec.com.tecnointel.soem.documeElec.tareas;

import java.io.File;

import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.egreso.registroInt.EgresoRegisInt;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import ec.com.tecnointel.soem.ingreso.registroInt.IngresoRegisInt;
import ec.com.tecnointel.soem.ingreso.registroInt.RetencionRegisInt;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.serWebClientSri.recepcion.RespuestaSolicitud;
import ec.com.tecnointel.soem.serWebSri.registroInt.EnvioComprobantesWsInt;
import jakarta.inject.Inject;

//@Dependent
public class TareaEnviarDocu implements Runnable {

//	@PersistenceContext
//	private EntityManager entityManager;

	private String detalleProceso = null;

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
	RetencionRegisInt retencionRegis;

	@Inject
	IngresoRegisInt ingresoRegis;

	@Inject
	EnvioComprobantesWsInt envioComprobantesWs;

	@Override
	public void run() {

		RespuestaSolicitud respuestaSolicitud = new RespuestaSolicitud();

		try {
			respuestaSolicitud = envioComprobantesWs.obtenerRespuestaEnvio1(archivoFirmado, sucursal.getRuc(), codDoc,
					claveAcceso, envioComprobantesWs.devuelveUrlWs(proxyIp, proxyPuerto, ambiente, urlProduccion,
							urlPruebas, nombreServicio));
		} catch (Exception e) {

			e.printStackTrace();
		}

		if (respuestaSolicitud.getEstado().equals("DEVUELTA")) {

//			Obtiene motivos de devolucion
			try {
				this.detalleProceso = envioComprobantesWs.obtenerMensajeRespuesta(respuestaSolicitud);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (this.object instanceof Egreso) {

			try {
				Egreso egreso = (Egreso) object;

//				Poner motivo rechazo en caso que exista
				if (this.detalleProceso != null) {
					egreso.setMotivoRech(this.detalleProceso.trim());
				}

//				Modificar estado y grabar documento
				egreso.setEstadoDocuElec(respuestaSolicitud.getEstado());
				egresoRegis.modificar(egreso);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (this.object instanceof Retencion) {

			try {

				Retencion retencion = (Retencion) object;

//				Poner motivo rechazo en caso que exista
				if (this.detalleProceso != null) {
					retencion.setMotivoRech(this.detalleProceso.trim());
				}

//				Modificar estado y grabar documento
				retencion.setEstadoDocuElec(respuestaSolicitud.getEstado());
				retencionRegis.modificar(retencion);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (this.object instanceof Ingreso) {

			try {

				Ingreso ingreso = (Ingreso) object;

//				Poner motivo rechazo en caso que exista
				if (this.detalleProceso != null) {
					ingreso.setMotivoRech(this.detalleProceso.trim());
				}

//				Modificar estado y grabar documento
				ingreso.setEstadoDocuElec(respuestaSolicitud.getEstado());
				ingresoRegis.modificar(ingreso);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

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
