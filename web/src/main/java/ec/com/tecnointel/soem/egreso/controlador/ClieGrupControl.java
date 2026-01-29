package ec.com.tecnointel.soem.egreso.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuen;
import ec.com.tecnointel.soem.egreso.listaInt.ClieGrupListaInt;
import ec.com.tecnointel.soem.egreso.listaInt.ClieGrupPlanCuenListaInt;
import ec.com.tecnointel.soem.egreso.modelo.ClieGrup;
import ec.com.tecnointel.soem.egreso.modelo.ClieGrupPlanCuen;
import ec.com.tecnointel.soem.egreso.registroInt.ClieGrupRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ClieGrupControl extends PaginaControl implements Serializable {

	private ClieGrup clieGrup;

	List<ClieGrup> clieGrups;
	private List<ClieGrupPlanCuen> clieGrupPlanCuens;

	@Inject
	ClieGrupRegisInt clieGrupRegis;

	@Inject
	ClieGrupListaInt clieGrupLista;
	
	@Inject
	ClieGrupPlanCuenListaInt clieGrupPlanCuenLista;
	
	private static final long serialVersionUID = -4644950956343215519L;

	@PostConstruct
	public void cargar() {

		clieGrup = new ClieGrup();
		clieGrup.setEstado(true);

	}

	public void buscar() {

		try {
			
			clieGrupLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.clieGrups = clieGrupLista.buscar(clieGrup, this.pagina);
			this.numeroReg = clieGrups.size();
			this.contadorReg = clieGrupLista.contarRegistros(clieGrup);
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
			this.clieGrup = new ClieGrup();
			this.clieGrup.setEstado(true);
		} else {

			try {
				this.clieGrup = clieGrupRegis.buscarPorId(ClieGrup.class, this.getId());
				this.buscarClieGrupPlanCuens(clieGrup);
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
				Object id = clieGrupRegis.insertar(clieGrup);
				this.id = (Integer) id;
			} else {
				clieGrupRegis.modificar(clieGrup);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "explora?faces-redirect=true&clieGrupId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&clieGrupId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&clieGrupId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			ClieGrup clieGrup = clieGrupRegis.buscarPorId(ClieGrup.class, this.getId());
			clieGrupRegis.eliminar(clieGrup);

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

	public List<ClieGrup> buscarTodo() {

		List<ClieGrup> clieGrups = new ArrayList<>();

		try {
			clieGrups = clieGrupLista.buscarTodo("clieGrupId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return clieGrups;
	}
	
	public void buscarClieGrupPlanCuens(ClieGrup clieGrup){

		ClieGrupPlanCuen clieGrupPlanCuen = new ClieGrupPlanCuen();
		clieGrupPlanCuen.setClieGrup(clieGrup);
		clieGrupPlanCuen.setPlanCuen(new PlanCuen());
		
		try {
			this.clieGrupPlanCuens = clieGrupPlanCuenLista.buscar(clieGrupPlanCuen, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Cliente Grupo Plan Cuenta"));
			e.printStackTrace();
		}
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public ClieGrup getClieGrup() {
		return clieGrup;
	}

	public void setClieGrup(ClieGrup clieGrup) {
		this.clieGrup = clieGrup;
	}

	public List<ClieGrup> getClieGrups() {
		return clieGrups;
	}

	public void setClieGrups(List<ClieGrup> clieGrups) {
		this.clieGrups = clieGrups;
	}

	public List<ClieGrupPlanCuen> getClieGrupPlanCuens() {
		return clieGrupPlanCuens;
	}

	public void setClieGrupPlanCuens(List<ClieGrupPlanCuen> clieGrupPlanCuens) {
		this.clieGrupPlanCuens = clieGrupPlanCuens;
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