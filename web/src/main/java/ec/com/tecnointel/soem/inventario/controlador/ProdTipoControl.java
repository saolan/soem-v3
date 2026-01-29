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
import ec.com.tecnointel.soem.inventario.registroInt.ProdTipoRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ProdTipoControl extends PaginaControl implements Serializable {

	private ProdTipo prodTipo;

	List<ProdTipo> prodTipos;
	List<PlanCuen> planCuens;
	List<ProdTipoPlanCuen> prodTipoPlanCuens;

	@Inject
	ProdTipoRegisInt prodTipoRegis;

	@Inject
	ProdTipoListaInt prodTipoLista;
	
	@Inject
	PlanCuenListaInt planCuenLista;
	
	@Inject
	ProdTipoPlanCuenListaInt prodTipoPlanCuenLista;


	private static final long serialVersionUID = -8302858644609636941L;

	@PostConstruct
	public void cargar() {

		prodTipo = new ProdTipo();
		prodTipo.setEstado(true);

	}

	public void buscar() {

		try {
			
			prodTipoLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.prodTipos = prodTipoLista.buscar(prodTipo, this.pagina);
			this.numeroReg = prodTipos.size();
			this.contadorReg = prodTipoLista.contarRegistros(prodTipo);
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
			this.prodTipo = new ProdTipo();
			this.prodTipo.setEstado(true);
		} else {

			try {
				this.prodTipo = prodTipoRegis.buscarPorId(ProdTipo.class, this.getId());
				this.buscarProdTipoPlanCuens(prodTipo);
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
				Object id = prodTipoRegis.insertar(prodTipo);
				this.id = (Integer) id;
			} else {
				prodTipoRegis.modificar(prodTipo);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "explora?faces-redirect=true&prodTipoId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&prodTipoId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&prodTipoId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			ProdTipo prodTipo = prodTipoRegis.buscarPorId(ProdTipo.class, this.getId());
			prodTipoRegis.eliminar(prodTipo);

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

	public List<ProdTipo> buscarTodo() {

		List<ProdTipo> prodTipos = new ArrayList<>();

		try {
			prodTipos = prodTipoLista.buscarTodo("prodTipoId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return prodTipos;
	}
	
	public void buscarPlanCuens(){
		
		PlanCuen planCuen = new PlanCuen();
		
		try {
			this.planCuens = planCuenLista.buscar(planCuen, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar plan cuenta"));

			e.printStackTrace();
		}

	}
	
	public void buscarProdTipoPlanCuens(ProdTipo prodTipo){

		ProdTipoPlanCuen prodTipoPlanCuen = new ProdTipoPlanCuen();
		prodTipoPlanCuen.setProdTipo(prodTipo);
		prodTipoPlanCuen.setPlanCuen(new PlanCuen());
				
		try {
			this.prodTipoPlanCuens = prodTipoPlanCuenLista.buscar(prodTipoPlanCuen, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Producto Tipo Plan Cuenta"));
			e.printStackTrace();
		}
	}	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public ProdTipo getProdTipo() {
		return prodTipo;
	}

	public void setProdTipo(ProdTipo prodTipo) {
		this.prodTipo = prodTipo;
	}

	public List<ProdTipo> getProdTipos() {
		return prodTipos;
	}

	public void setProdTipos(List<ProdTipo> prodTipos) {
		this.prodTipos = prodTipos;
	}

	public List<ProdTipoPlanCuen> getProdTipoPlanCuens() {
		return prodTipoPlanCuens;
	}

	public void setProdTipoPlanCuens(List<ProdTipoPlanCuen> prodTipoPlanCuens) {
		this.prodTipoPlanCuens = prodTipoPlanCuens;
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