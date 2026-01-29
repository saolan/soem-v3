package ec.com.tecnointel.soem.contabilidad.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import ec.com.tecnointel.soem.contabilidad.listaInt.PlanCuenListaInt;
import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuen;
import ec.com.tecnointel.soem.contabilidad.registroInt.PlanCuenRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.TranPlanDetaListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.TranPlanListaInt;
import ec.com.tecnointel.soem.parametro.modelo.TranPlan;
import ec.com.tecnointel.soem.parametro.modelo.TranPlanDeta;
import ec.com.tecnointel.soem.parametro.registroInt.TranPlanDetaRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class TranPlanDetaControl extends PaginaControl implements Serializable {

	private long contadorRegPlanCuen;
	private int numeroRegPlanCuen;
	
	private Integer tranPlanId;
	private Integer paginaPlanCuen;
	
	private String paginaRuta;
	
	private TranPlanDeta tranPlanDeta;

//	private PlanCuen planCuen;
	private PlanCuen planCuenBuscar;
	
	private List<TranPlanDeta> tranPlanDetas;
	private List<TranPlan> tranPlans;
	private List<PlanCuen> planCuens;
	
	@Inject
	TranPlanDetaRegisInt tranPlanDetaRegis;

	@Inject
	TranPlanDetaListaInt tranPlanDetaLista;
	
	@Inject
	TranPlanListaInt tranPlanLista;
	
	@Inject
	PlanCuenListaInt planCuenLista;

	@Inject
	PlanCuenRegisInt planCuenRegis;
	
	private static final long serialVersionUID = -1620853379603379983L;

	@PostConstruct
	public void cargar() {
		
		tranPlanDeta = new TranPlanDeta();
		tranPlanDeta.setTranPlan(new TranPlan());
		
//		planCuen = new PlanCuen();
		planCuenBuscar = new PlanCuen();
	}

	public void buscar() {

		try {
			
			tranPlanDetaLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.tranPlanDetas = tranPlanDetaLista.buscar(tranPlanDeta, this.pagina);
			this.numeroReg = tranPlanDetas.size();
			this.contadorReg = tranPlanDetaLista.contarRegistros(tranPlanDeta);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}
	
	public List<PlanCuen> buscarPlanCuens(Integer paginador) {

		if (paginador == 0) {
			this.paginaPlanCuen = 0;
		}

		if (this.planCuenBuscar.getCodigo() != null) {
			this.planCuenBuscar.setDescri(null);
		}

		List<PlanCuen> planCuens = new ArrayList<>();
		this.planCuenBuscar.setDetall(true);
		this.planCuenBuscar.setEstado(true);

		try {

			planCuenLista.filasPagina(variablesSesion.getFilasPagina());
			planCuens = this.planCuenLista.buscar(this.planCuenBuscar, this.paginaPlanCuen);

			this.numeroRegPlanCuen = planCuens.size();
			this.contadorRegPlanCuen = planCuenLista.contarRegistros(planCuenBuscar);

			this.planCuens = planCuens;

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Plan de Cuentas"));
			e.printStackTrace();
		}

		return planCuens;
	}
	
	public void buscarTranPlans() {

		this.getTranPlanDeta().getTranPlan().setEstado(true);
		
		try {
			this.tranPlans = tranPlanLista.buscar(this.getTranPlanDeta().getTranPlan(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Error al buscar transacción plantilla"));
			e.printStackTrace();
		}
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() ) {
			return;
		}

		PlanCuen planCuen = new PlanCuen();
		
		this.getTranPlanDeta().getTranPlan().setTranPlanId(tranPlanId);
		this.buscarTranPlans();

		if (this.id == null) {
			
			this.tranPlanDeta = new TranPlanDeta();
			this.tranPlanDeta.setFactor((short) 1);
			
		} else {

			try {
								
				this.tranPlanDeta = tranPlanDetaRegis.buscarPorId(TranPlanDeta.class, this.getId());
				planCuen = planCuenRegis.buscarPorId(PlanCuen.class, tranPlanDeta.getPlanCuenId());
				this.tranPlanDeta.setPlanCuen(planCuen);
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
				
				this.tranPlanDeta.setPlanCuenId(this.tranPlanDeta.getPlanCuen().getPlanCuenId());
				Object id = tranPlanDetaRegis.insertar(tranPlanDeta);
				this.id = (Integer) id;
				
			} else {
				tranPlanDetaRegis.modificar(tranPlanDeta);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		if (this.getTranPlanId() != null){
			return paginaRuta + "?faces-redirect=true&tranPlanId=" + this.getTranPlanId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public String nuevo() {
		
		if (this.getTranPlanId() != null){
			return "/ppsj/contabilidad/tranPlanDeta/registra?faces-redirect=true&tranPlanId=" + this.getTranPlanId() + "&paginaRuta=" + this.getPaginaRuta();			
		} else {
			return "/ppsj/contabilidad/tranPlanDeta/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta(); 
		}

	}
	
	public String modificar() {
		if (this.getTranPlanId() != null){
			return "/ppsj/contabilidad/tranPlanDeta/registra?faces-redirect=true&tranPlanDetaId=" + this.getId() + "&tranPlanId=" + this.getTranPlanId() + "&paginaRuta=" + this.getPaginaRuta();
		} else {
			return "/ppsj/contabilidad/tranPlanDeta/registra?faces-redirect=true&tranPlanDetaId=" + this.getId() + "&paginaRuta=" + this.getPaginaRuta();
		}
	}

	public String explorar() {
		return "explora?faces-redirect=true&tranPlanDetaId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			TranPlanDeta tranplanDeta = tranPlanDetaRegis.buscarPorId(TranPlanDeta.class, this.getId());
			tranPlanDetaRegis.eliminar(tranplanDeta);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		if (this.getTranPlanId() != null){
			return paginaRuta + "?faces-redirect=true&tranPlanId=" + this.getTranPlanId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public String cancelar(){
		
		if (this.getTranPlanId() != null){
			return paginaRuta + "?faces-redirect=true&tranPlanId=" + this.getTranPlanId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}
	}

	public void onRowSelect(SelectEvent<?> event) {

		this.tranPlanDeta.setPlanCuen((PlanCuen) event.getObject());

		PrimeFaces.current().executeScript("PF('seleccionCuenta').hide();");
	}
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public String getPaginaRuta() {
		return paginaRuta;
	}

	public void setPaginaRuta(String paginaRuta) {
		this.paginaRuta = paginaRuta;
	}

	public Integer getTranPlanId() {
		return tranPlanId;
	}

	public void setTranPlanId(Integer tranPlanId) {
		this.tranPlanId = tranPlanId;
	}

	public TranPlanDeta getTranPlanDeta() {
		return tranPlanDeta;
	}

	public void setTranPlanDeta(TranPlanDeta tranPlanDeta) {
		this.tranPlanDeta = tranPlanDeta;
	}

	public List<TranPlanDeta> getTranPlanDetas() {
		return tranPlanDetas;
	}

	public void setTranPlanDetas(List<TranPlanDeta> tranPlanDetas) {
		this.tranPlanDetas = tranPlanDetas;
	}

	public List<TranPlan> getTranPlans() {
		return tranPlans;
	}

	public void setTranPlans(List<TranPlan> tranPlans) {
		this.tranPlans = tranPlans;
	}

	public Integer getPaginaPlanCuen() {
		return paginaPlanCuen;
	}

	public void setPaginaPlanCuen(Integer paginaPlanCuen) {
		this.paginaPlanCuen = paginaPlanCuen;
	}

	public PlanCuen getPlanCuenBuscar() {
		return planCuenBuscar;
	}

	public void setPlanCuenBuscar(PlanCuen planCuenBuscar) {
		this.planCuenBuscar = planCuenBuscar;
	}

	public long getContadorRegPlanCuen() {
		return contadorRegPlanCuen;
	}

	public void setContadorRegPlanCuen(long contadorRegPlanCuen) {
		this.contadorRegPlanCuen = contadorRegPlanCuen;
	}

	public int getNumeroRegPlanCuen() {
		return numeroRegPlanCuen;
	}

	public void setNumeroRegPlanCuen(int numeroRegPlanCuen) {
		this.numeroRegPlanCuen = numeroRegPlanCuen;
	}

	public List<PlanCuen> getPlanCuens() {
		return planCuens;
	}

	public void setPlanCuens(List<PlanCuen> planCuens) {
		this.planCuens = planCuens;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}