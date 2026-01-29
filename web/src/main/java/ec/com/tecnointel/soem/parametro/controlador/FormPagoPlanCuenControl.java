package ec.com.tecnointel.soem.parametro.controlador;

import java.io.Serializable;
import java.util.List;

import ec.com.tecnointel.soem.contabilidad.listaInt.PlanCuenListaInt;
import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuen;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.FormPagoListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.FormPagoPlanCuenListaInt;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.parametro.modelo.FormPagoPlanCuen;
import ec.com.tecnointel.soem.parametro.registroInt.FormPagoPlanCuenRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class FormPagoPlanCuenControl extends PaginaControl implements Serializable {

	private Integer formPagoId;
	private String paginaRuta;

	private FormPagoPlanCuen formPagoPlanCuen;

	private List<FormPagoPlanCuen> formPagoPlanCuens;
	private List<FormPago> formPagos;
	private List<PlanCuen> planCuens;

	@Inject
	FormPagoPlanCuenRegisInt formPagoPlanCuenRegis;

	@Inject
	FormPagoPlanCuenListaInt formPagoPlanCuenLista;

	@Inject
	FormPagoListaInt formPagoLista;

	@Inject
	PlanCuenListaInt planCuenLista;

	private static final long serialVersionUID = 5388774736047062904L;

	@PostConstruct
	public void cargar() {
		formPagoPlanCuen = new FormPagoPlanCuen();
		formPagoPlanCuen.setFormPago(new FormPago());
		formPagoPlanCuen.setPlanCuen(new PlanCuen());
	}

	public void buscar() {

		try {

			formPagoPlanCuenLista.filasPagina(variablesSesion.getFilasPagina());

			this.formPagoPlanCuens = formPagoPlanCuenLista.buscar(formPagoPlanCuen, this.pagina);
			this.numeroReg = formPagoPlanCuens.size();
			this.contadorReg = formPagoPlanCuenLista.contarRegistros(formPagoPlanCuen);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public void buscarFormPagos() {

		FormPago formPago = new FormPago();
		formPago.setFormPagoId(formPagoId);
		formPago.setEstado(true);
		
		try {
			this.formPagos = formPagoLista.buscar(formPago, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Producto Tipo - Error al buscar tipo de productos"));
			e.printStackTrace();
		}
	}

	public void buscarPlanCuens() {

		this.getFormPagoPlanCuen().getPlanCuen().setEstado(true);
		this.getFormPagoPlanCuen().getPlanCuen().setDetall(true);

		try {
			this.planCuens = planCuenLista.buscar(this.getFormPagoPlanCuen().getPlanCuen(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Plan Cuenta - Error al buscar plan de cuentas"));
			e.printStackTrace();
		}
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}

		this.buscarFormPagos();
		this.buscarPlanCuens();

		if (this.id == null) {
			this.formPagoPlanCuen = new FormPagoPlanCuen();
		} else {

			try {
				this.formPagoPlanCuen = formPagoPlanCuenRegis.buscarPorId(FormPagoPlanCuen.class, this.getId());
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
				Object id = formPagoPlanCuenRegis.insertar(formPagoPlanCuen);
				this.id = (Integer) id;
			} else {
				formPagoPlanCuenRegis.modificar(formPagoPlanCuen);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		if (this.getFormPagoId() != null) {
			return paginaRuta + "?faces-redirect=true&formPagoId=" + this.getFormPagoId();
		} else {
			return paginaRuta + "?faces-redirect=true";
		}

	}

	public String nuevo() {

		if (this.getFormPagoId() != null) {
			return "/ppsj/parametro/formPagoPlanCuen/registra?faces-redirect=true&formPagoId=" + this.getFormPagoId()
					+ "&paginaRuta=" + this.getPaginaRuta();
		} else {
			return "/ppsj/parametro/formPagoPlanCuen/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta();
		}

	}

	public String modificar() {
		if (this.getFormPagoId() != null) {
			return "/ppsj/parametro/formPagoPlanCuen/registra?faces-redirect=true&fppcId=" + this.getId()
					+ "&formPagoId=" + this.getFormPagoId() + "&paginaRuta=" + this.getPaginaRuta();
		} else {
			return "/ppsj/parametro/formPagoPlanCuen/registra?faces-redirect=true&fppcId=" + this.getId()
					+ "&paginaRuta=" + this.getPaginaRuta();
		}
	}

	public String explorar() {
		return "explora?faces-redirect=true&fppcId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			FormPagoPlanCuen formPagoPlanCuen = formPagoPlanCuenRegis.buscarPorId(FormPagoPlanCuen.class, this.getId());
			formPagoPlanCuenRegis.eliminar(formPagoPlanCuen);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		if (this.getFormPagoId() != null) {
			return paginaRuta + "?faces-redirect=true&formPagoId=" + this.getFormPagoId();
		} else {
			return paginaRuta + "?faces-redirect=true";
		}

	}

	public String cancelar() {

		if (this.getFormPagoId() != null) {
			return paginaRuta + "?faces-redirect=true&formPagoId=" + this.getFormPagoId();
		} else {
			return paginaRuta + "?faces-redirect=true";
		}
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public FormPagoPlanCuen getFormPagoPlanCuen() {
		return formPagoPlanCuen;
	}

	public void setFormPagoPlanCuen(FormPagoPlanCuen formPagoPlanCuen) {
		this.formPagoPlanCuen = formPagoPlanCuen;
	}

	public List<FormPagoPlanCuen> getFormPagoPlanCuens() {
		return formPagoPlanCuens;
	}

	public void setFormPagoPlanCuens(List<FormPagoPlanCuen> formPagoPlanCuens) {
		this.formPagoPlanCuens = formPagoPlanCuens;
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

	public Integer getFormPagoId() {
		return formPagoId;
	}

	public void setFormPagoId(Integer formPagoId) {
		this.formPagoId = formPagoId;
	}

	public List<FormPago> getFormPagos() {
		return formPagos;
	}

	public void setFormPagos(List<FormPago> formPagos) {
		this.formPagos = formPagos;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}
