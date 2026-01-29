package ec.com.tecnointel.soem.egreso.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.contabilidad.listaInt.PlanCuenListaInt;
import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuen;
import ec.com.tecnointel.soem.egreso.listaInt.ClieGrupListaInt;
import ec.com.tecnointel.soem.egreso.listaInt.ClieGrupPlanCuenListaInt;
import ec.com.tecnointel.soem.egreso.modelo.ClieGrup;
import ec.com.tecnointel.soem.egreso.modelo.ClieGrupPlanCuen;
import ec.com.tecnointel.soem.egreso.registroInt.ClieGrupPlanCuenRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.DocumentoListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ClieGrupPlanCuenControl extends PaginaControl implements Serializable {

	private Integer clieGrupId;
	private String paginaRuta;
	
	private ClieGrupPlanCuen clieGrupPlanCuen;
	
	private List<Parametro> parametros;

	private List<ClieGrupPlanCuen> clieGrupPlanCuens;
	private List<ClieGrup> clieGrups;
	private List<PlanCuen> planCuens;
	private List<Documento> documentos;

	@Inject
	ClieGrupPlanCuenRegisInt clieGrupPlanCuenRegis;

	@Inject
	ClieGrupPlanCuenListaInt clieGrupPlanCuenLista;
	
	@Inject
	ClieGrupListaInt clieGrupLista;
	
	@Inject
	PlanCuenListaInt planCuenLista;

	@Inject
	DocumentoListaInt documentoLista;

	private static final long serialVersionUID = -1050767778352640143L;

	@PostConstruct
	public void cargar() {
		clieGrupPlanCuen = new ClieGrupPlanCuen();
		clieGrupPlanCuen.setClieGrup(new ClieGrup());
		clieGrupPlanCuen.setPlanCuen(new PlanCuen());
		
		this.parametros = this.buscarTipoTrans();
	}

	public void buscar() {

		try {
			
			clieGrupPlanCuenLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.clieGrupPlanCuens = clieGrupPlanCuenLista.buscar(clieGrupPlanCuen, this.pagina);
			this.numeroReg = clieGrupPlanCuens.size();
			this.contadorReg = clieGrupPlanCuenLista.contarRegistros(clieGrupPlanCuen);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}
	
	public List<Documento> buscarDocumentos(Documento documento) {
		
		List<Documento> documentos = new ArrayList<>();
		
		try {
			documentos = documentoLista.buscar(documento, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepción al buscar documentos"));
			e.printStackTrace();
		}
		return documentos;
	}
	
	public List<Parametro> buscarTipoTrans() {

		Parametro parametro = new Parametro();

		List<Parametro> parametros = new ArrayList<>();

		parametro.setCodigo("Contabilidad-TipoTranCart");
		parametro.setEstado(true);

		try {
			parametros = this.parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Plan Cuenta - Error al buscar plan de cuentas"));
			e.printStackTrace();
		}

		return parametros;
	}

	public void buscarClieGrups() {
	
		ClieGrup clieGrup = new ClieGrup();
		clieGrup.setClieGrupId(clieGrupId);
		clieGrup.setEstado(true);
		
		try {
			this.clieGrups = clieGrupLista.buscar(clieGrup, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Bodega - Error al buscar grupo de clientes"));
			e.printStackTrace();
		}
	}

	public void buscarPlanCuens() {
	
		this.getClieGrupPlanCuen().getPlanCuen().setEstado(true);
		this.getClieGrupPlanCuen().getPlanCuen().setDetall(true);
		
		try {
			this.planCuens = planCuenLista.buscar(this.getClieGrupPlanCuen().getPlanCuen(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Bodega - Error al buscar plan de cuentas"));
			e.printStackTrace();
		}
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback()) {
			return;
		}
		
		this.buscarClieGrups();
		this.buscarPlanCuens();
		
		Documento documento = new Documento();
		documentos = buscarDocumentos(documento);

		if (this.id == null) {
			this.clieGrupPlanCuen = new ClieGrupPlanCuen();
		} else {

			try {
				this.clieGrupPlanCuen = clieGrupPlanCuenRegis.buscarPorId(ClieGrupPlanCuen.class, this.getId());
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
				Object id = clieGrupPlanCuenRegis.insertar(clieGrupPlanCuen);
				this.id = (Integer) id;
			} else {
				clieGrupPlanCuenRegis.modificar(clieGrupPlanCuen);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		if (this.getClieGrupId() != null){
			return paginaRuta + "?faces-redirect=true&clieGrupId=" + this.getClieGrupId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public String nuevo() {
		
		if (this.getClieGrupId() != null){
			return "/ppsj/egreso/clieGrupPlanCuen/registra?faces-redirect=true&clieGrupId=" + this.getClieGrupId() + "&paginaRuta=" + this.getPaginaRuta();			
		} else {
			return "/ppsj/egreso/clieGrupPlanCuen/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta(); 
		}

	}

	public String modificar() {
		if (this.getClieGrupId() != null){
			return "/ppsj/egreso/clieGrupPlanCuen/registra?faces-redirect=true&cgpcId=" + this.getId() + "&clieGrupId=" + this.getClieGrupId() + "&paginaRuta=" + this.getPaginaRuta();	
		} else {
			return "/ppsj/egreso/clieGrupPlanCuen/registra?faces-redirect=true&cgpcId=" + this.getId() + "&paginaRuta=" + this.getPaginaRuta();
		}		
	}

	public String explorar() {
		return "explora?faces-redirect=true&cgpcId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			ClieGrupPlanCuen clieGrupPlanCuen = clieGrupPlanCuenRegis.buscarPorId(ClieGrupPlanCuen.class, this.getId());
			clieGrupPlanCuenRegis.eliminar(clieGrupPlanCuen);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		if (this.getClieGrupId() != null){
			return paginaRuta + "?faces-redirect=true&clieGrupId=" + this.getClieGrupId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}
	}

	public String cancelar(){
		
		if (this.getClieGrupId() != null){
			return paginaRuta + "?faces-redirect=true&clieGrupId=" + this.getClieGrupId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}
	}

	public List<ClieGrupPlanCuen> buscarTodo() {

		List<ClieGrupPlanCuen> clieGrupPlanCuens = new ArrayList<>();

		try {
			clieGrupPlanCuens = clieGrupPlanCuenLista.buscarTodo("clieGrupPlanCuenId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return clieGrupPlanCuens;
	}
	
	
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public ClieGrupPlanCuen getClieGrupPlanCuen() {
		return clieGrupPlanCuen;
	}

	public void setClieGrupPlanCuen(ClieGrupPlanCuen clieGrupPlanCuen) {
		this.clieGrupPlanCuen = clieGrupPlanCuen;
	}

	public List<ClieGrupPlanCuen> getClieGrupPlanCuens() {
		return clieGrupPlanCuens;
	}

	public void setClieGrupPlanCuens(List<ClieGrupPlanCuen> clieGrupPlanCuens) {
		this.clieGrupPlanCuens = clieGrupPlanCuens;
	}

	public Integer getClieGrupId() {
		return clieGrupId;
	}

	public void setClieGrupId(Integer clieGrupId) {
		this.clieGrupId = clieGrupId;
	}

	public List<ClieGrup> getClieGrups() {
		return clieGrups;
	}

	public void setClieGrups(List<ClieGrup> clieGrups) {
		this.clieGrups = clieGrups;
	}

	public List<PlanCuen> getPlanCuens() {
		return planCuens;
	}

	public void setPlanCuens(List<PlanCuen> planCuens) {
		this.planCuens = planCuens;
	}

	public String getPaginaRuta() {
		return paginaRuta;
	}

	public void setPaginaRuta(String paginaRuta) {
		this.paginaRuta = paginaRuta;
	}

	public List<Parametro> getParametros() {
		return parametros;
	}

	public void setParametros(List<Parametro> parametros) {
		this.parametros = parametros;
	}

	public List<Documento> getDocumentos() {
		return documentos;
	}

	public void setDocumentos(List<Documento> documentos) {
		this.documentos = documentos;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}