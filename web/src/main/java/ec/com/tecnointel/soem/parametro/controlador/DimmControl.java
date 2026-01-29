package ec.com.tecnointel.soem.parametro.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuen;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.DimmListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.DimmPlanCuenListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.DimmPlanCuen;
import ec.com.tecnointel.soem.parametro.registroInt.DimmRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class DimmControl extends PaginaControl implements Serializable {

	private Dimm dimm;

	private List<Dimm> dimms;
	private List<DimmPlanCuen> dimmPlanCuens;

	@Inject
	DimmRegisInt dimmRegis;

	@Inject
	DimmListaInt dimmLista;

	@Inject
	DimmPlanCuenListaInt dimmPlanCuenLista;

	private static final long serialVersionUID = 5614826900697338826L;

	@PostConstruct
	public void cargar() {

		dimm = new Dimm();
		dimm.setEstado(true);

	}

	public void buscar() {
		
		Dimm dimmHasta = new Dimm();
		
		try {
			
			dimmLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.dimms = dimmLista.buscar(dimm, dimmHasta, this.pagina);
			this.numeroReg = dimms.size();
			this.contadorReg = dimmLista.contarRegistros(dimm, dimm);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}
	
	public void buscarDimmPlanCuens(Dimm dimm){

		DimmPlanCuen dimmPlanCuen = new DimmPlanCuen();
		dimmPlanCuen.setDimm(dimm);
		dimmPlanCuen.setPlanCuen(new PlanCuen());
				
		try {
			this.dimmPlanCuens = dimmPlanCuenLista.buscar(dimmPlanCuen, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Dimm Plan Cuenta"));
			e.printStackTrace();
		}
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}

		if (this.id == null) {
			this.dimm = new Dimm();
			this.dimm.setEstado(true);
		} else {

			try {
				this.dimm = dimmRegis.buscarPorId(Dimm.class, this.getId());
				this.buscarDimmPlanCuens(dimm);
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
				Object id = dimmRegis.insertar(dimm);
				this.id = (Integer) id;
			} else {
				dimmRegis.modificar(dimm);
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
		return "registra?faces-redirect=true&dimmId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&dimmId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			Dimm dimm = dimmRegis.buscarPorId(Dimm.class, this.getId());
			dimmRegis.eliminar(dimm);

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

	public List<Dimm> buscarTodo() {

		List<Dimm> dimms = new ArrayList<>();

		try {
			dimms = dimmLista.buscarTodo("descri");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return dimms;
	}
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public Dimm getDimm() {
		return dimm;
	}

	public void setDimm(Dimm dimm) {
		this.dimm = dimm;
	}

	public List<Dimm> getDimms() {
		return dimms;
	}

	public void setDimms(List<Dimm> dimms) {
		this.dimms = dimms;
	}

	public List<DimmPlanCuen> getDimmPlanCuens() {
		return dimmPlanCuens;
	}

	public void setDimmPlanCuens(List<DimmPlanCuen> dimmPlanCuens) {
		this.dimmPlanCuens = dimmPlanCuens;
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