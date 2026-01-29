package ec.com.tecnointel.soem.ingreso.controlador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.primefaces.event.CellEditEvent;

import ec.com.tecnointel.soem.documeElec.tareas.ManejadorTareaEnviarCorreoDocu;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.ingreso.listaInt.ReteDetaListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.RetencionListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.modelo.ReteDeta;
import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import ec.com.tecnointel.soem.ingreso.registroInt.ReteDetaRegisInt;
import ec.com.tecnointel.soem.ingreso.registroInt.RetencionRegisInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuIngr;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.registroInt.DocuIngrRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Conversation;
import jakarta.enterprise.context.ConversationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ConversationScoped
public class RetencControl extends PaginaControl implements Serializable {

	Integer ingresoId;
	BigDecimal retencionTotal;
	
	Retencion retencion;
	ReteDeta reteDetaSele;
	
	List<ReteDeta> reteDetas;
	List<ReteDeta> reteDetaEliminados;
	
	@Inject
	RetencionListaInt retencionLista;
	
	@Inject
	RetencionRegisInt retencionRegis;
	
	@Inject
	ReteDetaListaInt reteDetaLista;
	
	@Inject
	ReteDetaRegisInt reteDetaRegis;
	
	@Inject
	DocuIngrRegisInt docuIngrRegis;
		
	@Inject
	ManejadorTareaEnviarCorreoDocu manejadorTareaEnviarCorreoDocu;
	
	@Inject
	private Conversation conversation;

	private static final long serialVersionUID = 7104215349128020613L;
	
	@PostConstruct
	public void cargar() {
		
		this.retencionTotal = new BigDecimal(0);
		
		this.reteDetas = new ArrayList<>();	
		this.reteDetaEliminados = new ArrayList<>();
		
	}

	public void conversationBegin() {
		if (conversation.isTransient()) {
			conversation.setTimeout(1800000L);
			conversation.begin();
		}
	}

	public void conversationEnd() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
	}
	
	
	public void recuperar() {
		
		DocuIngr docuIngr = new DocuIngr();
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}
		
		this.conversationBegin();
		
		if (this.id == null) {
			
			this.retencion = new Retencion();
			this.retencion.setIngreso(new Ingreso());
			this.retencion.setFechaEmis(LocalDate.now());
			this.retencion.setFechaRegi(LocalDate.now());
			this.retencion.setFechaHoraEmis(LocalDateTime.now());
			this.retencion.setFechaHoraRegi(LocalDateTime.now());
			this.retencion.setEstado("PR");

		} else {

			try {
				
				this.retencion = retencionRegis.buscarPorId(Retencion.class, this.getId());
				docuIngr = this.buscarDocuIngr(retencion);
				this.retencion.getIngreso().setDocuIngr(docuIngr);
				this.buscarReteDetas();
				
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar retencion Id"));
				e.printStackTrace();
			}
		}
	}
	
	public void buscarReteDetas(){

		
		if (FacesContext.getCurrentInstance().isPostback()) {
			return; // Skip postback requests.
		}
		
		ReteDeta reteDeta = new ReteDeta();
		reteDeta.setRetencion(new Retencion());
		
		reteDeta.getRetencion().setRetencionId(this.getId());
		
		try {
			
			this.reteDetas = reteDetaLista.buscar(reteDeta, null);
			this.calcularTotal();
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar detalle de retención"));
			e.printStackTrace();
		}
		
	}	
	
	public DocuIngr buscarDocuIngr(Retencion retencion) {
		
		DocuIngr docuIngr = new DocuIngr();
		
		try {
			docuIngr = docuIngrRegis.buscarPorId(DocuIngr.class, retencion.getIngreso().getDocuIngr().getDocumentoId());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar tipo documento Ingreso para retención"));
			e.printStackTrace();
		}
		
		return docuIngr;

	}
	
	public String grabar() {
		
		this.conversationEnd();
		
		try {
			
//			Graba cabecera
			if (this.id == null) {
 
				Object id = retencionRegis.insertar(retencion);
				this.id = (Integer) id;
				
			} else {
				
				retencionRegis.modificar(retencion);
				
			}
			
//			Graba Detalles
			for (ReteDeta reteDeta : this.reteDetas) {
								
//				Se coloca la entidad retencion en reteDeta ya que al buscar reteDetas no buscar retencion
//				no existe graph en la busqueda
				reteDeta.setRetencion(this.retencion);
				
				if (reteDeta.getReteDetaId() == null){
					reteDetaRegis.insertar(reteDeta);
				} else {
					reteDetaRegis.modificar(reteDeta);
				}
			}
			
//			Elimina reteDeta
			for (ReteDeta reteDeta : this.reteDetaEliminados) {
				reteDetaRegis.eliminar(reteDeta);
			} 

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar retencion"));
			e.printStackTrace();
			
			return "null";
			
		}
		
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Documento grabado"));
		
//		return "/ppsj/ingreso/retencion/explora.xhtml?faces-redirect=true";
		return "explora?faces-redirect=true&retencionId=" + this.getId();
		
	}
	
	public String eliminar() {
		
		this.conversationEnd();
		
		try {
						
//			Eliminar IngrDeta y por cascada IngrDetaImpu
			this.eliminarReteDetas();			
			retencionRegis.eliminar(this.getRetencion());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al eliminar retención"));
			e.printStackTrace();
		}
		
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Documento eliminado"));

		return "/ppsj/ingreso/retencion/lista.xhtml?faces-redirect=true";
	}
	
	public String explorar() {
		return "/ppsj/ingreso/retenc/explora?faces-redirect=true&retencionId=" + this.getId();
	}
	
	public void eliminarReteDetas() {
		
		for (ReteDeta reteDeta : this.reteDetas) {
			try {
				
				reteDetaRegis.eliminar(reteDeta);
				
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al eliminar detalle de retención"));
				e.printStackTrace();
			}
		}
	}	
	
	public String modificar() {

		return "/ppsj/ingreso/retenc/registra?faces-redirect=true&retencionId=" + this.getId();

	}
	
	public void agregarReteDeta() {
		
		ReteDeta reteDeta = new ReteDeta();
		
		reteDeta.setEjerciFisc(this.retencion.getFechaEmis());
		reteDeta.setBase(new BigDecimal(0));
		reteDeta.setPorcen(new BigDecimal(0));
		
		this.reteDetas.add(reteDeta);
		
	}

//	Carga una nueva lista de productos que se tienen que eliminar
//	Se elimina al grabar
	public void eliminarReteDeta() {
		
		this.reteDetas.remove(this.reteDetaSele);
		
		try {
			
			if (reteDetaSele.getReteDetaId() != null) {
				
				reteDetaEliminados.add(reteDetaSele);
				
			}
			
			this.calcularTotal();
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al eliminar detalle documento"));
			e.printStackTrace();

		}
	}
	
	public void modificarCelda(CellEditEvent<?> event) {
		
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		if (newValue != null && !newValue.equals(oldValue)) {
			this.calcularTotal();
		}
	}
		
	public void calcularTotal(){
		
		BigDecimal total = new BigDecimal(0);
		
		for (ReteDeta reteDeta : reteDetas) {
		
			reteDeta.setReteDetaTotal(reteDeta.getBase().multiply(reteDeta.getPorcen()).divide(new BigDecimal(100)));
			total = total.add(reteDeta.getReteDetaTotal());
		
		}
		
		retencionTotal = total;

	}	
	
	public void descargar(){
		
		if (id == null){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Error - Por favor primero grabe o procese el documento para poder descargarlo"));
			return;
		}

		Parametro parametro = new Parametro();
		// Nombre que esta en archivo fuente en jasperReport
		String nombreReporte = "";
		
		Map<String, Object> parametrosJasper = new HashMap<String, Object>();
				
		try {
			
			parametro = parametroRegis.buscarPorId(Parametro.class, 104007);
			nombreReporte = parametro.getDescri();
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar formato retención"));
			e.printStackTrace();
		}

//		Se coloca aqui la ruta para poder tener los reportes en diferentes y varias carpetas
//		Sino se podria poner en la clase GenerarJasperReport
		String rutaJrxml; // = "C:\\tecnoIntel\\soem\\jasperReportes\\ingreso\\";
//		En esta ruta se compila el reporte dentro del servidor de aplicaciones dentro del archivo .EAR .WEB
		String rutaReporteCompilado = "\\jasperReportes\\ingreso\\";

		try {

			Integer retencionId = this.retencion.getRetencionId();
			
			if (retencionId == null) {
				parametrosJasper.put("retencionId",0);
			} else {
				parametrosJasper.put("retencionId",retencionId);
			}
			
			parametro = parametroRegis.buscarPorId(Parametro.class, 4000);
			rutaJrxml = parametro.getDescri();

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper, rutaJrxml, rutaReporteCompilado , formatoReporte);
					
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al descargar documento"));
			e.printStackTrace();
		}
	}
	
	public void descargarRide(){
		
		if (id == null){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Error - Por favor primero grabe o procese el documento para poder descargarlo"));
			return;
		}

		Parametro parametro = new Parametro();
		// Nombre que esta en archivo fuente en jasperReport
		String nombreReporte = "rideRetencion";
		
		Map<String, Object> parametrosJasper = new HashMap<String, Object>();
				
//		try {
//			
//			parametro = parametroRegis.buscarPorId(104007);
//			nombreReporte = parametro.getDescri();
//			
//		} catch (Exception e) {
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
//					"Excepcion - Error al buscar formato retención"));
//			e.printStackTrace();
//		}

//		Se coloca aqui la ruta para poder tener los reportes en diferentes y varias carpetas
//		Sino se podria poner en la clase GenerarJasperReport
		String rutaJrxml; // = "C:\\tecnoIntel\\soem\\jasperReportes\\ingreso\\";
//		En esta ruta se compila el reporte dentro del servidor de aplicaciones dentro del archivo .EAR .WEB
		String rutaReporteCompilado = "\\jasperReportes\\ingreso\\";

		try {

			Integer retencionId = this.retencion.getRetencionId();
			
			if (retencionId == null) {
				parametrosJasper.put("retencionId",0);
			} else {
				parametrosJasper.put("retencionId",retencionId);
			}
			
			parametro = parametroRegis.buscarPorId(Parametro.class, 4000);
			rutaJrxml = parametro.getDescri();

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper, rutaJrxml, rutaReporteCompilado , formatoReporte);
					
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al descargar documento"));
			e.printStackTrace();
		}
	}

	public void reenviarCorreo() {
		
		if (this.retencion.getIngreso().getPersProv().getPersona().getCorreo() != null 
				&& this.retencion.getIngreso().getDocuIngr().getDocumento().isEnviaCorreo()) {
				this.ejecutarTareaEnviarCorreoDocu(this.retencion.getIngreso().getPersProv().getPersona().getCorreo(), this.retencion.getClaveAcce());	
		}
	}
	
	public void ejecutarTareaEnviarCorreoDocu(String destinatario, String claveAcce) {
		
		// if (destinatario != null){
		// destintariosFinal.append(destinatarios);
		//// TODO; ojo cuando no hay uno de los dos no va la coma
		// destintariosFinal.append(",");
		// }

		// Parametro 100250 configurar si se desea enviar los documentos electronicos a
		// otras direcciones
		// if (variablesSesion.getDireccionesAdicionales() != null){
		// destintariosFinal.append(variablesSesion.getDireccionesAdicionales());
		// }
		
		try {

			manejadorTareaEnviarCorreoDocu.ejecutarTareaEnviarCorreoDocu(destinatario, claveAcce);
			
		} catch (ExecutionException | InterruptedException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Documento no re-enviado por correo al proveedor " + destinatario));
			e.printStackTrace();
		}
	}

	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	
	public Retencion getRetencion() {
		return retencion;
	}

	public void setRetencion(Retencion retencion) {
		this.retencion = retencion;
	}

	public List<ReteDeta> getReteDetas() {
		return reteDetas;
	}

	public void setReteDetas(List<ReteDeta> reteDetas) {
		this.reteDetas = reteDetas;
	}

	public ReteDeta getReteDetaSele() {
		return reteDetaSele;
	}

	public void setReteDetaSele(ReteDeta reteDetaSele) {
		this.reteDetaSele = reteDetaSele;
	}

	public BigDecimal getRetencionTotal() {
		return retencionTotal;
	}

	public void setRetencionTotal(BigDecimal retencionTotal) {
		this.retencionTotal = retencionTotal;
	}
}
