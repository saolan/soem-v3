package ec.com.tecnointel.soem.ingreso.controlador;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.ingreso.listaInt.RetencionListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import ec.com.tecnointel.soem.ingreso.registroInt.RetencionRegisInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuIngr;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class RetencionControl extends PaginaControl implements Serializable {

	private Retencion retencion;

	List<Retencion> retencions;

	@Inject
	RetencionRegisInt retencionRegis;

	@Inject
	RetencionListaInt retencionLista;

	private static final long serialVersionUID = 8528557941931803207L;

	@PostConstruct
	public void cargar() {

		Ingreso ingreso = new Ingreso();
		ingreso.setDocuIngr(new DocuIngr());
		retencion = new Retencion();
		retencion.setIngreso(ingreso);

//		Date fecha = Util.cambiarFormatoFecha(new Date(), "dd-MM-yyyy");		
//		retencion.setFechaEmis(fecha);
		retencion.setFechaEmis(LocalDate.now());
		retencion.setFechaRegi(LocalDate.now());
	}

	public void buscar() {

		try {
			
			retencionLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.retencions = retencionLista.buscar(retencion, this.pagina);
			this.numeroReg = retencions.size();
			this.contadorReg = retencionLista.contarRegistros(retencion);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}

		if (this.id == null) {
			this.retencion = new Retencion();
		} else {

			try {
				this.retencion = retencionRegis.buscarPorId(Retencion.class, this.getId());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
				e.printStackTrace();
			}
		}
	}

	public String grabar() {

		// Los mensajes tienen RequestScope y se borran al navegar a otro pagina
		// Con este comando se guarda los mensages de confirmaci�n en navegacion
		// entre paginas
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			if (this.id == null) {
				Object id = retencionRegis.insertar(retencion);
				this.id = (Integer) id;
			} else {
				retencionRegis.modificar(retencion);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "lista?faces-redirect=true";
	}

	public String modificar() {
		return "registra?faces-redirect=true&retencionId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&retencionId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			Retencion retencion = retencionRegis.buscarPorId(Retencion.class, this.getId());
			retencionRegis.eliminar(retencion);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		return "lista?faces-redirect=true";
	}

	public List<Retencion> buscarTodo() {

		List<Retencion> retenciones = new ArrayList<>();

		try {
			retenciones = retencionLista.buscarTodo("retencionId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return retenciones;
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

	public List<Retencion> getRetencions() {
		return retencions;
	}

	public void setRetencions(List<Retencion> retencions) {
		this.retencions = retencions;
	}


	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}