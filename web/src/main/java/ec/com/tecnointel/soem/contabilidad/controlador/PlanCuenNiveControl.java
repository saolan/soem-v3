package ec.com.tecnointel.soem.contabilidad.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.contabilidad.listaInt.PlanCuenNiveListaInt;
import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuenNive;
import ec.com.tecnointel.soem.contabilidad.registroInt.PlanCuenNiveRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class PlanCuenNiveControl extends PaginaControl implements Serializable {

	private PlanCuenNive planCuenNive;

	List<PlanCuenNive> planCuenNives;

	@Inject
	PlanCuenNiveRegisInt planCuenNiveRegis;

	@Inject
	PlanCuenNiveListaInt planCuenNiveLista;

	private static final long serialVersionUID = 2249089312906923715L;

	@PostConstruct
	public void cargar() {

		planCuenNive = new PlanCuenNive();

	}

	public void buscar() {

		try {
			
			planCuenNiveLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.planCuenNives = planCuenNiveLista.buscar(planCuenNive, this.pagina);
			this.numeroReg = planCuenNives.size();
			this.contadorReg = planCuenNiveLista.contarRegistros(planCuenNive);
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
			this.planCuenNive = new PlanCuenNive();
		} else {

			try {
				this.planCuenNive = planCuenNiveRegis.buscarPorId(PlanCuenNive.class, this.getId());
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
				Object id = planCuenNiveRegis.insertar(planCuenNive);
				this.id = (Integer) id;
			} else {
				planCuenNiveRegis.modificar(planCuenNive);
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
		return "registra?faces-redirect=true&planCuenNiveId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&planCuenNiveId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			PlanCuenNive planCuenNive = planCuenNiveRegis.buscarPorId(PlanCuenNive.class, this.getId());
			planCuenNiveRegis.eliminar(planCuenNive);

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

	public List<PlanCuenNive> buscarTodo() {

		List<PlanCuenNive> planCuenNives = new ArrayList<>();

		try {
			planCuenNives = planCuenNiveLista.buscarTodo("planCuenNiveId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return planCuenNives;
	}
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public PlanCuenNive getPlanCuenNive() {
		return planCuenNive;
	}

	public void setPlanCuenNive(PlanCuenNive planCuenNive) {
		this.planCuenNive = planCuenNive;
	}

	public List<PlanCuenNive> getPlanCuenNives() {
		return planCuenNives;
	}

	public void setPlanCuenNives(List<PlanCuenNive> planCuenNives) {
		this.planCuenNives = planCuenNives;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// Estas 3 Lineas llaman al metodo cuando se abra la pagina es similar a
	// @PostConstructor
	// <f:metadata>
	// <f:event type="preRenderView"
	// listener='#{categoriaControlador.buscarTodo}' />
	// </f:metadata>

	// public void buscarTodo() {
	// try {
	// this.categorias =
	// categoriaConsultaInterface.categoriaConsultar(this.getCategoria());
	// } catch (Exception e) {
	// // TODOs Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

}