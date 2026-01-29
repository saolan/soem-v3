package ec.com.tecnointel.soem.contabilidad.controlador;

import java.io.Serializable;
import java.util.List;

import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuen;
import ec.com.tecnointel.soem.contabilidad.registroInt.PlanCuenRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.TranPlanDetaListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.TranPlanListaInt;
import ec.com.tecnointel.soem.parametro.modelo.TranPlan;
import ec.com.tecnointel.soem.parametro.modelo.TranPlanDeta;
import ec.com.tecnointel.soem.parametro.registroInt.TranPlanRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class TranPlanControl extends PaginaControl implements Serializable {

	private TranPlan tranPlan;

	private List<TranPlan> tranPlans;
	private List<TranPlanDeta> tranPlanDetas;

	@Inject
	TranPlanRegisInt tranPlanRegis;

	@Inject
	TranPlanListaInt tranPlanLista;

	@Inject
	TranPlanDetaListaInt tranPlanDetaLista;

	@Inject
	PlanCuenRegisInt planCuenRegis;

	private static final long serialVersionUID = 469821836826453018L;

	@PostConstruct
	public void cargar() {

		tranPlan = new TranPlan();
		tranPlan.setEstado(true);

	}

	public void buscar() {

		try {

			tranPlanLista.filasPagina(variablesSesion.getFilasPagina());

			this.tranPlans = tranPlanLista.buscar(tranPlan, this.pagina);
			this.numeroReg = tranPlans.size();
			this.contadorReg = tranPlanLista.contarRegistros(tranPlan);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}
	
	public void buscarTranPlanDetas(TranPlan tranPlan){
		
		TranPlanDeta tranPlanDetaBuscar = new TranPlanDeta();
		tranPlanDetaBuscar.setTranPlan(tranPlan);
		
		try {
			this.tranPlanDetas = tranPlanDetaLista.buscar(tranPlanDetaBuscar, null);
			
			for (TranPlanDeta tranPlanDeta : tranPlanDetas) {
				PlanCuen planCuen = new PlanCuen();
				planCuen = planCuenRegis.buscarPorId(PlanCuen.class, tranPlanDeta.getPlanCuenId());
				tranPlanDeta.setPlanCuen(planCuen);   
			}
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar plantilla detalles"));

			e.printStackTrace();
		}
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}

		if (this.id == null) {
			this.tranPlan = new TranPlan();
			this.tranPlan.setEstado(true);
		} else {

			try {
				this.tranPlan = tranPlanRegis.buscarPorId(TranPlan.class, this.getId());
				this.buscarTranPlanDetas(tranPlan);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
				e.printStackTrace();
			}
		}
	}

	public String grabar() {

		// Los mensajes tienen RequestScope y se borran al navegar a otro pagina
		// Con este comando se guarda los mensages de confirmaci?n en navegacion
		// entre paginas
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			if (this.id == null) {
				Object id = tranPlanRegis.insertar(tranPlan);
				this.id = (Integer) id;
			} else {
				tranPlanRegis.modificar(tranPlan);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepci�n - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "explora?faces-redirect=true&tranPlanId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&tranPlanId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&tranPlanId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			TranPlan tranPlan = tranPlanRegis.buscarPorId(TranPlan.class, this.getId());
			tranPlanRegis.eliminar(tranPlan);

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

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public TranPlan getTranPlan() {
		return tranPlan;
	}

	public void setTranPlan(TranPlan tranPlan) {
		this.tranPlan = tranPlan;
	}

	public List<TranPlan> getTranPlans() {
		return tranPlans;
	}

	public void setTranPlans(List<TranPlan> tranPlans) {
		this.tranPlans = tranPlans;
	}

	public List<TranPlanDeta> getTranPlanDetas() {
		return tranPlanDetas;
	}

	public void setTranPlanDetas(List<TranPlanDeta> tranPlanDetas) {
		this.tranPlanDetas = tranPlanDetas;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}