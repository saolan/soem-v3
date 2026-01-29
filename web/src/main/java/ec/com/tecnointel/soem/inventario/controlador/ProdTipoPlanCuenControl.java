package ec.com.tecnointel.soem.inventario.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.contabilidad.listaInt.PlanCuenListaInt;
import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuen;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.inventario.listaInt.ProdTipoListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdTipoPlanCuenListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdTipo;
import ec.com.tecnointel.soem.inventario.modelo.ProdTipoPlanCuen;
import ec.com.tecnointel.soem.inventario.registroInt.ProdTipoPlanCuenRegisInt;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ProdTipoPlanCuenControl extends PaginaControl implements Serializable {

	private Integer prodTipoId;
	private String paginaRuta;
	
	private ProdTipoPlanCuen prodTipoPlanCuen;

	private List<ProdTipoPlanCuen> prodTipoPlanCuens;
	private List<ProdTipo> prodTipos;
	private List<PlanCuen> planCuens;
	
	private List<Parametro> parametros;
	
	@Inject
	ProdTipoPlanCuenRegisInt prodTipoPlanCuenRegis;

	@Inject
	ProdTipoPlanCuenListaInt prodTipoPlanCuenLista;
	
	@Inject
	ProdTipoListaInt prodTipoLista;
	
	@Inject
	PlanCuenListaInt planCuenLista;

	private static final long serialVersionUID = -924981737245773782L;

	@PostConstruct
	public void cargar() {
		prodTipoPlanCuen = new ProdTipoPlanCuen();
		prodTipoPlanCuen.setProdTipo(new ProdTipo());
		prodTipoPlanCuen.setPlanCuen(new PlanCuen());
		
		this.parametros = this.buscarTipoTrans();
	}

	public void buscar() {

		try {
			
			prodTipoPlanCuenLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.prodTipoPlanCuens = prodTipoPlanCuenLista.buscar(prodTipoPlanCuen, this.pagina);
			this.numeroReg = prodTipoPlanCuens.size();
			this.contadorReg = prodTipoPlanCuenLista.contarRegistros(prodTipoPlanCuen);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public void buscarProdTipos() {
		
//		Se coloca el prodTipoId para buscar el registro que viene de la pagina anterior
//		y que se despliegue en el combo de tipo de producto, si no busca todos y 
//		se tendria que seleccionar el tipo de producto que se necesite 
		
		ProdTipo prodTipo = new ProdTipo();
		prodTipo.setProdTipoId(prodTipoId);
		prodTipo.setEstado(true);
		
		try {
			this.prodTipos = prodTipoLista.buscar(prodTipo, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Producto Tipo - Error al buscar tipo de productos"));
			e.printStackTrace();
		}
	}

	public void buscarPlanCuens() {
	
		PlanCuen planCuen = new PlanCuen();
		planCuen.setEstado(true);
		planCuen.setDetall(true);
		
		try {
			this.planCuens = planCuenLista.buscar(planCuen, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Plan Cuenta - Error al buscar plan de cuentas"));
			e.printStackTrace();
		}
	}

	public List<Parametro> buscarTipoTrans() {
		
		Parametro parametro = new Parametro();
		
		List<Parametro> parametros = new ArrayList<>();
		
		parametro.setCodigo("Contabilidad-TipoTran");
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
		if (facesContext.isPostback()) {
			return;
		}
		
		this.buscarProdTipos();
		this.buscarPlanCuens();

		if (this.id == null) {
			this.prodTipoPlanCuen = new ProdTipoPlanCuen();
		} else {

			try {
				this.prodTipoPlanCuen = prodTipoPlanCuenRegis.buscarPorId(ProdTipoPlanCuen.class, this.getId());
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
				Object id = prodTipoPlanCuenRegis.insertar(prodTipoPlanCuen);
				this.id = (Integer) id;
			} else {
				prodTipoPlanCuenRegis.modificar(prodTipoPlanCuen);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		if (this.getProdTipoId() != null){
			return paginaRuta + "?faces-redirect=true&prodTipoId=" + this.getProdTipoId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public String nuevo() {
		
		if (this.getProdTipoId() != null){
			return "/ppsj/inventario/prodTipoPlanCuen/registra?faces-redirect=true&prodTipoId=" + this.getProdTipoId() + "&paginaRuta=" + this.getPaginaRuta();			
		} else {
			return "/ppsj/inventario/prodTipoPlanCuen/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta(); 
		}

	}
	
	public String modificar() {
		if (this.getProdTipoId() != null){
			return "/ppsj/inventario/prodTipoPlanCuen/registra?faces-redirect=true&ptpcId=" + this.getId() + "&prodTipoId=" + this.getProdTipoId() + "&paginaRuta=" + this.getPaginaRuta();	
		} else {
			return "/ppsj/inventario/prodTipoPlanCuen/registra?faces-redirect=true&ptpcId=" + this.getId() + "&paginaRuta=" + this.getPaginaRuta();	
		}		
	}

	public String explorar() {
		return "explora?faces-redirect=true&ptpcId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			ProdTipoPlanCuen prodTipoPlanCuen = prodTipoPlanCuenRegis.buscarPorId(ProdTipoPlanCuen.class, this.getId());
			prodTipoPlanCuenRegis.eliminar(prodTipoPlanCuen);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		if (this.getProdTipoId() != null){
			return paginaRuta + "?faces-redirect=true&prodTipoId=" + this.getProdTipoId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public String cancelar(){
		
		if (this.getProdTipoId() != null){
			return paginaRuta + "?faces-redirect=true&prodTipoId=" + this.getProdTipoId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}


	}
	
	public List<ProdTipoPlanCuen> buscarTodo() {

		List<ProdTipoPlanCuen> prodTipoPlanCuens = new ArrayList<>();

		try {
			prodTipoPlanCuens = prodTipoPlanCuenLista.buscarTodo("prodTipoPlanCuenId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return prodTipoPlanCuens;
	}
	
	
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public ProdTipoPlanCuen getProdTipoPlanCuen() {
		return prodTipoPlanCuen;
	}

	public void setProdTipoPlanCuen(ProdTipoPlanCuen prodTipoPlanCuen) {
		this.prodTipoPlanCuen = prodTipoPlanCuen;
	}

	public List<ProdTipoPlanCuen> getProdTipoPlanCuens() {
		return prodTipoPlanCuens;
	}

	public void setProdTipoPlanCuens(List<ProdTipoPlanCuen> prodTipoPlanCuens) {
		this.prodTipoPlanCuens = prodTipoPlanCuens;
	}

	public Integer getProdTipoId() {
		return prodTipoId;
	}

	public void setProdTipoId(Integer prodTipoId) {
		this.prodTipoId = prodTipoId;
	}

	public List<ProdTipo> getProdTipos() {
		return prodTipos;
	}

	public void setProdTipos(List<ProdTipo> prodTipos) {
		this.prodTipos = prodTipos;
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

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}

