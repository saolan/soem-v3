package ec.com.tecnointel.soem.contabilidad.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.contabilidad.listaInt.PlanCuenListaInt;
import ec.com.tecnointel.soem.contabilidad.listaInt.PlanCuenNiveListaInt;
import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuen;
import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuenNive;
import ec.com.tecnointel.soem.contabilidad.registroInt.PlanCuenRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class PlanCuenControl extends PaginaControl implements Serializable {

	private PlanCuen planCuen;

	List<PlanCuen> planCuens;
	List<PlanCuen> planCuenSupes;
	List<PlanCuenNive> planCuenNives;

	@Inject
	PlanCuenRegisInt planCuenRegis;

	@Inject
	PlanCuenListaInt planCuenLista;
	
	@Inject
	PlanCuenNiveListaInt planCuenNiveLista;

	private static final long serialVersionUID = 3714647041954520630L;

	@PostConstruct
	public void cargar() {

		planCuen = new PlanCuen();
		planCuen.setDetall(null);
		planCuen.setEstado(true);

	}

	public void buscar() {

		try {
			
			planCuenLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.planCuens = planCuenLista.buscar(planCuen, this.pagina);
			this.numeroReg = planCuens.size();
			this.contadorReg = planCuenLista.contarRegistros(planCuen);
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
		
		this.planCuenNives = this.buscarPlanCuenNives(new PlanCuenNive());
		
		PlanCuen planCuenBuscar = new PlanCuen();
		planCuenBuscar.setDetall(false);
		planCuenBuscar.setEstado(true);
		
		this.planCuenSupes = this.buscarPlanCuen(planCuenBuscar);
		
		if (this.id == null) {
			this.planCuen = new PlanCuen();
			this.planCuen.setEstado(true);
		} else {

			try {
				this.planCuen = planCuenRegis.buscarPorId(PlanCuen.class, this.getId());
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
				Object id = planCuenRegis.insertar(planCuen);
				this.id = (Integer) id;
			} else {
				planCuenRegis.modificar(planCuen);
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
		return "registra?faces-redirect=true&planCuenId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&planCuenId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			PlanCuen planCuen = planCuenRegis.buscarPorId(PlanCuen.class, this.getId());
			planCuenRegis.eliminar(planCuen);

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

	public List<PlanCuen> buscarTodo() {

		List<PlanCuen> planCuens = new ArrayList<>();

		try {
			planCuens = planCuenLista.buscarTodo("descri");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return planCuens;
	}
	
	public List<PlanCuenNive> buscarPlanCuenNives(PlanCuenNive planCuenNiveBuscar) {
		
		List<PlanCuenNive> planCuenNives = new ArrayList<>();
		
		try {
			planCuenNives = planCuenNiveLista.buscar(planCuenNiveBuscar, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar niveles del plan de cuentas"));
			e.printStackTrace();
		}
		
		return planCuenNives;

	}
	
	public List<PlanCuen> buscarPlanCuen(PlanCuen planCuenBuscar){
				
		List<PlanCuen> planCuens = new ArrayList<>();
		
		try {
			planCuens = planCuenLista.buscar(planCuenBuscar, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar plan de cuentas"));
			e.printStackTrace();
		}
		
		return planCuens;
	}
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public PlanCuen getPlanCuen() {
		return planCuen;
	}

	public void setPlanCuen(PlanCuen planCuen) {
		this.planCuen = planCuen;
	}

	public List<PlanCuen> getPlanCuens() {
		return planCuens;
	}

	public void setPlanCuens(List<PlanCuen> planCuens) {
		this.planCuens = planCuens;
	}

	public List<PlanCuenNive> getPlanCuenNives() {
		return planCuenNives;
	}

	public void setPlanCuenNives(List<PlanCuenNive> planCuenNives) {
		this.planCuenNives = planCuenNives;
	}

	public List<PlanCuen> getPlanCuenSupes() {
		return planCuenSupes;
	}

	public void setPlanCuenSupes(List<PlanCuen> planCuenSupes) {
		this.planCuenSupes = planCuenSupes;
	}
}