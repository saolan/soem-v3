package ec.com.tecnointel.soem.ingreso.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuen;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.ingreso.listaInt.ProvGrupListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.ProvGrupPlanCuenListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.ProvGrup;
import ec.com.tecnointel.soem.ingreso.modelo.ProvGrupPlanCuen;
import ec.com.tecnointel.soem.ingreso.registroInt.ProvGrupRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ProvGrupControl extends PaginaControl implements Serializable {

	private ProvGrup provGrup;

	List<ProvGrup> provGrups;
	private List<ProvGrupPlanCuen> provGrupPlanCuens;

	@Inject
	ProvGrupRegisInt provGrupRegis;

	@Inject
	ProvGrupListaInt provGrupLista;
	
	@Inject
	ProvGrupPlanCuenListaInt provGrupPlanCuenLista;

	private static final long serialVersionUID = 619880623224871493L;

	@PostConstruct
	public void cargar() {

		provGrup = new ProvGrup();
		provGrup.setEstado(true);

	}

	public void buscar() {

		try {
			
			provGrupLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.provGrups = provGrupLista.buscar(provGrup, this.pagina);
			this.numeroReg = provGrups.size();
			this.contadorReg = provGrupLista.contarRegistros(provGrup);
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
			this.provGrup = new ProvGrup();
			this.provGrup.setEstado(true);
		} else {

			try {
				this.provGrup = provGrupRegis.buscarPorId(ProvGrup.class, this.getId());
				this.buscarProvGrupPlanCuens(provGrup);
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
				Object id = provGrupRegis.insertar(provGrup);
				this.id = (Integer) id;
			} else {
				provGrupRegis.modificar(provGrup);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "explora?faces-redirect=true&provGrupId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&provGrupId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&provGrupId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			ProvGrup provGrup = provGrupRegis.buscarPorId(ProvGrup.class, this.getId());
			provGrupRegis.eliminar(provGrup);

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

	public List<ProvGrup> buscarTodo() {

		List<ProvGrup> provGrups = new ArrayList<>();

		try {
			provGrups = provGrupLista.buscarTodo("provGrupId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return provGrups;
	}
	
	public void buscarProvGrupPlanCuens(ProvGrup provGrup){

		ProvGrupPlanCuen provGrupPlanCuen = new ProvGrupPlanCuen();
		provGrupPlanCuen.setProvGrup(provGrup);
		provGrupPlanCuen.setPlanCuen(new PlanCuen());
		
		try {
			this.provGrupPlanCuens = provGrupPlanCuenLista.buscar(provGrupPlanCuen, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Porveedor Grupo Plan Cuenta"));
			e.printStackTrace();
		}
	}
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public ProvGrup getProvGrup() {
		return provGrup;
	}

	public void setProvGrup(ProvGrup provGrup) {
		this.provGrup = provGrup;
	}

	public List<ProvGrup> getProvGrups() {
		return provGrups;
	}

	public void setProvGrups(List<ProvGrup> provGrups) {
		this.provGrups = provGrups;
	}

	public List<ProvGrupPlanCuen> getProvGrupPlanCuens() {
		return provGrupPlanCuens;
	}

	public void setProvGrupPlanCuens(List<ProvGrupPlanCuen> provGrupPlanCuens) {
		this.provGrupPlanCuens = provGrupPlanCuens;
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