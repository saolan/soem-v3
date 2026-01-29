package ec.com.tecnointel.soem.documeElec.tareas;

import java.io.File;
import java.time.LocalDateTime;

import javax.sql.DataSource;
import javax.xml.datatype.XMLGregorianCalendar;

import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.egreso.registroInt.EgresoRegisInt;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import ec.com.tecnointel.soem.ingreso.registroInt.IngresoRegisInt;
import ec.com.tecnointel.soem.ingreso.registroInt.RetencionRegisInt;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.registroInt.DimmRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.ParametroRegisInt;
import ec.com.tecnointel.soem.serWebClientSri.autorizacion.RespuestaComprobante;
import ec.com.tecnointel.soem.serWebClientSri.general.AutorizacionDTO;
import ec.com.tecnointel.soem.serWebSri.registroInt.AutorizacionComprobantesWsInt;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;

//@Dependent
public class TareaAutoriDocu implements Runnable {

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

	private String rutaAutorizados;
	private String rutaFirmados;

//	Guarda una entidad egreso o retencion
	private Object object;

	@Inject
	EgresoRegisInt egresoRegis;
	
	@Inject
	IngresoRegisInt ingresoRegis;

	@Inject
	RetencionRegisInt retencionRegis;

	@Inject
	ParametroRegisInt parametroRegis;

	@Inject
	DimmRegisInt dimmRegis;

	@Inject
	AutorizacionComprobantesWsInt autorizacionComprobantes;

	@Resource(mappedName = "java:jboss/datasources/soemDS")
	private DataSource dataSource;

	@Override
	public void run() {

		RespuestaComprobante respuestaComprobante;
		AutorizacionDTO autorizacionDTO;

		respuestaComprobante = this.verificarAutorizacion();

		if (!respuestaComprobante.getAutorizaciones().getAutorizacion().isEmpty()) {

			autorizacionDTO = this.obtenerEstadoAutorizacion(respuestaComprobante);

//			TODO: Actualizar gregorian calentar a LocalDate o LocalDateTime 
			XMLGregorianCalendar xmlDate = autorizacionDTO.getAutorizacion().getFechaAutorizacion();
//			Date fechaAutorizacion = xmlDate.toGregorianCalendar().getTime();
			LocalDateTime fechaAutorizacion = xmlDate.toGregorianCalendar().toZonedDateTime().toLocalDateTime();

			try {
				// Crea el archivo con la autorizacion y copia a la carpeta autorizados
				autorizacionComprobantes.validarRespuestaAutorizacion(autorizacionDTO, claveAcceso, rutaAutorizados);

			} catch (Exception e) {

				// "Error al consultar autorización Documento: " + egreso.getNumero() +
				// autorizacionDTO.getMensaje()));
				e.printStackTrace();

			}

			if (this.object instanceof Egreso) {

				try {
					Egreso egreso = (Egreso) object;

					egreso.setFechaAuto(fechaAutorizacion);
					egreso.setMotivoRech(autorizacionDTO.getMensaje());
//					Modifica estado y grabar documento
					egreso.setEstadoDocuElec(autorizacionDTO.getAutorizacion().getEstado());
					egresoRegis.modificar(egreso);

					File file = new File(rutaFirmados + claveAcceso + ".xml");
					file.delete();

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (this.object instanceof Retencion) {

				try {

					Retencion retencion = (Retencion) object;

					retencion.setFechaAuto(fechaAutorizacion);
					retencion.setMotivoRech(autorizacionDTO.getMensaje());

//					Modifica estado y grabar documento
					retencion.setEstadoDocuElec(autorizacionDTO.getAutorizacion().getEstado());
					retencionRegis.modificar(retencion);

					File file = new File(rutaFirmados + claveAcceso + ".xml");
					file.delete();

				} catch (Exception e) {
					e.printStackTrace();
				}
				
			} else if (this.object instanceof Ingreso) {

				try {

					Ingreso ingreso = (Ingreso) object;

					ingreso.setFechaAuto(fechaAutorizacion);
					ingreso.setMotivoRech(autorizacionDTO.getMensaje());

//					Modifica estado y grabar documento
					ingreso.setEstadoDocuElec(autorizacionDTO.getAutorizacion().getEstado());
					ingresoRegis.modificar(ingreso);

					File file = new File(rutaFirmados + claveAcceso + ".xml");
					file.delete();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	public RespuestaComprobante verificarAutorizacion() {

		RespuestaComprobante respuestaComprobante = new RespuestaComprobante();

		try {
			respuestaComprobante = autorizacionComprobantes.autorizarComprobante(proxyIp, proxyPuerto, ambiente,
					urlProduccion, urlPruebas, nombreServicio, claveAcceso);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return respuestaComprobante;

	}

	public AutorizacionDTO obtenerEstadoAutorizacion(RespuestaComprobante respuestaComprobante) {

		AutorizacionDTO autorizacionDTO = null;

		try {
			autorizacionDTO = autorizacionComprobantes.obtenerEstadoAutorizacion(respuestaComprobante);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return autorizacionDTO;
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

	public String getRutaAutorizados() {
		return rutaAutorizados;
	}

	public void setRutaAutorizados(String rutaAutorizados) {
		this.rutaAutorizados = rutaAutorizados;
	}

	public String getRutaFirmados() {
		return rutaFirmados;
	}

	public void setRutaFirmados(String rutaFirmados) {
		this.rutaFirmados = rutaFirmados;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}
