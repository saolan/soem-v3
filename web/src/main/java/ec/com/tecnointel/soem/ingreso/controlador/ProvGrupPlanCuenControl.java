package ec.com.tecnointel.soem.ingreso.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.contabilidad.listaInt.PlanCuenListaInt;
import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuen;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.ingreso.listaInt.ProvGrupListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.ProvGrupPlanCuenListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.ProvGrup;
import ec.com.tecnointel.soem.ingreso.modelo.ProvGrupPlanCuen;
import ec.com.tecnointel.soem.ingreso.registroInt.ProvGrupPlanCuenRegisInt;
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
public class ProvGrupPlanCuenControl extends PaginaControl implements Serializable {

	private Integer provGrupId;
	private String paginaRuta;
	
	private ProvGrupPlanCuen provGrupPlanCuen;
	
	private List<Parametro> parametros;

	private List<ProvGrupPlanCuen> provGrupPlanCuens;
	private List<ProvGrup> provGrups;
	private List<PlanCuen> planCuens;
	private List<Documento> documentos;

	@Inject
	ProvGrupPlanCuenRegisInt provGrupPlanCuenRegis;

	@Inject
	ProvGrupPlanCuenListaInt provGrupPlanCuenLista;
	
	@Inject
	ProvGrupListaInt provGrupLista;
	
	@Inject
	PlanCuenListaInt planCuenLista;

	@Inject
	DocumentoListaInt documentoLista;

	private static final long serialVersionUID = 1962814642891938047L;

	@PostConstruct
	public void cargar() {
		provGrupPlanCuen = new ProvGrupPlanCuen();
		provGrupPlanCuen.setProvGrup(new ProvGrup());
		provGrupPlanCuen.setPlanCuen(new PlanCuen());
		
		this.parametros = this.buscarTipoTrans();
	}

	public void buscar() {

		try {
			
			provGrupPlanCuenLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.provGrupPlanCuens = provGrupPlanCuenLista.buscar(provGrupPlanCuen, this.pagina);
			this.numeroReg = provGrupPlanCuens.size();
			this.contadorReg = provGrupPlanCuenLista.contarRegistros(provGrupPlanCuen);
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

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}
		
		this.buscarProvGrups();
		this.buscarPlanCuens();
		
		Documento documento = new Documento();
		documentos = buscarDocumentos(documento);

		if (this.id == null) {
			this.provGrupPlanCuen = new ProvGrupPlanCuen();
		} else {

			try {
				this.provGrupPlanCuen = provGrupPlanCuenRegis.buscarPorId(ProvGrupPlanCuen.class, this.getId());
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
				Object id = provGrupPlanCuenRegis.insertar(provGrupPlanCuen);
				this.id = (Integer) id;
			} else {
				provGrupPlanCuenRegis.modificar(provGrupPlanCuen);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		if (this.getProvGrupId() != null){
			return paginaRuta + "?faces-redirect=true&provGrupId=" + this.getProvGrupId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public String nuevo() {
		
		if (this.getProvGrupId() != null){
			return "/ppsj/ingreso/provGrupPlanCuen/registra?faces-redirect=true&provGrupId=" + this.getProvGrupId() + "&paginaRuta=" + this.getPaginaRuta();			
		} else {
			return "/ppsj/ingreso/provGrupPlanCuen/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta(); 
		}

	}
	
	public String modificar() {
		if (this.getProvGrupId() != null){
			return "/ppsj/ingreso/provGrupPlanCuen/registra?faces-redirect=true&pgpcId=" + this.getId() + "&provGrupId=" + this.getProvGrupId() + "&paginaRuta=" + this.getPaginaRuta();	
		} else {
			return "/ppsj/ingreso/provGrupPlanCuen/registra?faces-redirect=true&pgpcId=" + this.getId() + "&paginaRuta=" + this.getPaginaRuta();
		}		
	}

	public String explorar() {
		return "explora?faces-redirect=true&pgpcId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			ProvGrupPlanCuen provGrupPlanCuen = provGrupPlanCuenRegis.buscarPorId(ProvGrupPlanCuen.class, this.getId());
			provGrupPlanCuenRegis.eliminar(provGrupPlanCuen);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		if (this.getProvGrupId() != null){
			return paginaRuta + "?faces-redirect=true&provGrupId=" + this.getProvGrupId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public String cancelar(){
		
		if (this.getProvGrupId() != null){
			return paginaRuta + "?faces-redirect=true&provGrupId=" + this.getProvGrupId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}

	public List<ProvGrupPlanCuen> buscarTodo() {

		List<ProvGrupPlanCuen> provGrupPlanCuens = new ArrayList<>();

		try {
			provGrupPlanCuens = provGrupPlanCuenLista.buscarTodo("provGrupPlanCuenId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return provGrupPlanCuens;
	}
	
	public void buscarProvGrups() {

		ProvGrup provGrup = new ProvGrup();
		provGrup.setProvGrupId(provGrupId);
		provGrup.setEstado(true);
		
		try {
			this.provGrups = provGrupLista.buscar(provGrup, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Bodega - Error al buscar grupo de proveedores"));
			e.printStackTrace();
		}
	}
	
	public void buscarPlanCuens() {

		this.getProvGrupPlanCuen().getPlanCuen().setEstado(true);
		this.getProvGrupPlanCuen().getPlanCuen().setDetall(true);
		
		try {
			this.planCuens = planCuenLista.buscar(this.getProvGrupPlanCuen().getPlanCuen(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Bodega - Error al buscar plan de cuentas"));
			e.printStackTrace();
		}
	}
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public ProvGrupPlanCuen getProvGrupPlanCuen() {
		return provGrupPlanCuen;
	}

	public void setProvGrupPlanCuen(ProvGrupPlanCuen provGrupPlanCuen) {
		this.provGrupPlanCuen = provGrupPlanCuen;
	}

	public List<ProvGrupPlanCuen> getProvGrupPlanCuens() {
		return provGrupPlanCuens;
	}

	public void setProvGrupPlanCuens(List<ProvGrupPlanCuen> provGrupPlanCuens) {
		this.provGrupPlanCuens = provGrupPlanCuens;
	}

	public Integer getProvGrupId() {
		return provGrupId;
	}

	public void setProvGrupId(Integer provGrupId) {
		this.provGrupId = provGrupId;
	}

	public List<ProvGrup> getProvGrups() {
		return provGrups;
	}

	public void setProvGrups(List<ProvGrup> provGrups) {
		this.provGrups = provGrups;
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

