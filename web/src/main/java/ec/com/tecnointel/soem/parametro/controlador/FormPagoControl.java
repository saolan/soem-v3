package ec.com.tecnointel.soem.parametro.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.contabilidad.listaInt.PlanCuenListaInt;
import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuen;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.DimmListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.FormPagoListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.FormPagoPlanCuenListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.parametro.modelo.FormPagoPlanCuen;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.registroInt.FormPagoRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class FormPagoControl extends PaginaControl implements Serializable {

	private FormPago formPago;

	private List<Parametro> parameFPModulos;
	private List<FormPago> formPagos;
	private List<PlanCuen> planCuens;
	private List<Dimm> dimms;
	private List<FormPagoPlanCuen> formPagoPlanCuens;
	private List<Parametro> parameFPTipos;

	@Inject
	FormPagoRegisInt formPagoRegis;

	@Inject
	FormPagoListaInt formPagoLista;
	
	@Inject
	PlanCuenListaInt planCuenLista;
	
	@Inject
	DimmListaInt dimmLista;

	@Inject
	FormPagoPlanCuenListaInt formPagoPlanCuenLista;

	private static final long serialVersionUID = 4468338363438817615L;

	@PostConstruct
	public void cargar() {

		formPago = new FormPago();
//		formPago.setTipo("FP");
		formPago.setEstado(true);

	}

	public void buscar() {

		try {
			
			formPagoLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.formPagos = formPagoLista.buscar(formPago, this.pagina);
			this.numeroReg = formPagos.size();
			this.contadorReg = formPagoLista.contarRegistros(formPago);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}
	
	public void buscarFormaPagos() {
		Dimm dimmDesde = new Dimm();
		Dimm dimmHasta = new Dimm();
	
		dimmDesde.setTablaRefe("Tabla13");
		dimmDesde.setEstado(true);
	
		try {
			this.dimms = dimmLista.buscar(dimmDesde, dimmHasta, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar dimm"));
			e.printStackTrace();
	
		}
	}

	public void buscarModulos() {
	
		Parametro parametro = new Parametro();
		parametro.setCodigo("Parametro-FPModulo");
		parametro.setEstado(true);
	
		try {
			this.parameFPModulos = parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar dimm"));
			e.printStackTrace();
	
		}
	}

	public void buscarPlanCuens(){
		
		PlanCuen planCuen = new PlanCuen();
		planCuen.setEstado(true);
		
		try {
			this.planCuens = planCuenLista.buscar(planCuen, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar niveles de menú"));
			e.printStackTrace();
		}
	}

	public void buscarTipos() {
		
		Parametro parametro = new Parametro();
		parametro.setCodigo("Parametro-FPTipo");
		parametro.setEstado(true);
	
		try {
			this.parameFPTipos = parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Parametro-FPTipo"));
			e.printStackTrace();
	
		}
	}

	public void buscarFormPagoPlanCuens(FormPago formPago){

		FormPagoPlanCuen formPagoPlanCuen = new FormPagoPlanCuen();
		formPagoPlanCuen.setFormPago(formPago);
		formPagoPlanCuen.setPlanCuen(new PlanCuen());
				
		try {
			this.formPagoPlanCuens = formPagoPlanCuenLista.buscar(formPagoPlanCuen, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Forma Pago Plan Cuenta"));
			e.printStackTrace();
		}
	}	

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() ) {
			return;
		}

		this.buscarModulos();
		this.buscarTipos();
		this.buscarPlanCuens();
		this.buscarFormaPagos();
		
		if (this.id == null) {
			this.formPago.setTipo("FP");
			this.formPago.setEstado(true);
		} else {

			try {
				this.formPago = formPagoRegis.buscarPorId(FormPago.class, this.getId());
				this.buscarFormPagoPlanCuens(formPago);
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
				Object id = formPagoRegis.insertar(formPago);
				this.id = (Integer) id;
			} else {
				formPagoRegis.modificar(formPago);
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
		return "registra?faces-redirect=true&formPagoId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&formPagoId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			FormPago formPago = formPagoRegis.buscarPorId(FormPago.class, this.getId());
			formPagoRegis.eliminar(formPago);

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

	public List<FormPago> buscarTodo() {

		List<FormPago> formPagos = new ArrayList<>();

		try {
			formPagos = formPagoLista.buscarTodo("descri");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return formPagos;
	}
		
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public FormPago getFormPago() {
		return formPago;
	}

	public void setFormPago(FormPago formPago) {
		this.formPago = formPago;
	}

	public List<FormPago> getFormPagos() {
		return formPagos;
	}

	public void setFormPagos(List<FormPago> formPagos) {
		this.formPagos = formPagos;
	}

	public List<PlanCuen> getPlanCuens() {
		return planCuens;
	}

	public void setPlanCuens(List<PlanCuen> planCuens) {
		this.planCuens = planCuens;
	}

	public List<Dimm> getDimms() {
		return dimms;
	}

	public void setDimms(List<Dimm> dimms) {
		this.dimms = dimms;
	}

	public List<FormPagoPlanCuen> getFormPagoPlanCuens() {
		return formPagoPlanCuens;
	}

	public void setFormPagoPlanCuens(List<FormPagoPlanCuen> formPagoPlanCuens) {
		this.formPagoPlanCuens = formPagoPlanCuens;
	}

	public List<Parametro> getParameFPModulos() {
		return parameFPModulos;
	}

	public void setParameFPModulos(List<Parametro> parameFPModulos) {
		this.parameFPModulos = parameFPModulos;
	}

	public List<Parametro> getParameFPTipos() {
		return parameFPTipos;
	}

	public void setParameFPTipos(List<Parametro> parameFPTipos) {
		this.parameFPTipos = parameFPTipos;
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