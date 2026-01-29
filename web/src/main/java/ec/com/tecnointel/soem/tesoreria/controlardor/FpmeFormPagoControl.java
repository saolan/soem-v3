package ec.com.tecnointel.soem.tesoreria.controlardor;

import java.io.Serializable;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.tesoreria.listaInt.FpmeFormPagoListaInt;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviEgre;
import ec.com.tecnointel.soem.tesoreria.modelo.FpmeFormPago;
import ec.com.tecnointel.soem.tesoreria.registroInt.FormPagoMoviEgreRegisInt;
import ec.com.tecnointel.soem.tesoreria.registroInt.FpmeFormPagoRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class FpmeFormPagoControl extends PaginaControl implements Serializable {

	private FpmeFormPago fpmeFormPago;

	List<FpmeFormPago> fpmeFormPagos;

	private static final long serialVersionUID = -7338527583746121299L;

	@Inject
	FpmeFormPagoRegisInt fpmeFormPagoRegis;

	@Inject
	FpmeFormPagoListaInt fpmeFormPagoLista;

	@Inject
	FormPagoMoviEgreRegisInt FormPagoMoviEgreRegis;

	@PostConstruct
	public void cargar() {

		this.fpmeFormPago = new FpmeFormPago();
		this.fpmeFormPago.setFormPagoMoviEgre(new FormPagoMoviEgre());
		this.fpmeFormPago.getFormPagoMoviEgre().setPersona(new Persona());
	}

	public void buscar() {

		try {

			fpmeFormPagoLista.filasPagina(variablesSesion.getFilasPagina());

			this.fpmeFormPagos = this.fpmeFormPagoLista.buscar(fpmeFormPago, this.pagina);

			for (FpmeFormPago fpmeFormPago : fpmeFormPagos) {

//				FormPagoMoviEgre formPagoMoviEgre = new FormPagoMoviEgre();
//
//				formPagoMoviEgre = this.buscarFormPagoMoviEgrePorId(fpmeFormPago.getFormPagoMoviEgre().getFpmeId());
//				fpmeFormPago.setFormPagoMoviEgre(formPagoMoviEgre);

				fpmeFormPago.setFormPagoMoviEgre(
						this.buscarFormPagoMoviEgrePorId(fpmeFormPago.getFormPagoMoviEgre().getFpmeId()));

			}

			this.numeroReg = fpmeFormPagos.size();
			this.contadorReg = fpmeFormPagoLista.contarRegistros(fpmeFormPago);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar cobros"));
			e.printStackTrace();
		}
	}

	public FormPagoMoviEgre buscarFormPagoMoviEgrePorId(Integer fpmeId) {

		FormPagoMoviEgre formPagoMoviEgre = new FormPagoMoviEgre();

		try {

			formPagoMoviEgre = this.FormPagoMoviEgreRegis.buscarPorId(FormPagoMoviEgre.class, fpmeId);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar cabecera del cobro FPME"));
			e.printStackTrace();
		}
		return formPagoMoviEgre;
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}

		if (this.id == null) {
			this.fpmeFormPago = new FpmeFormPago();
		} else {

			try {
				this.fpmeFormPago = fpmeFormPagoRegis.buscarPorId(FpmeFormPago.class, this.getId());
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
				Object id = fpmeFormPagoRegis.insertar(fpmeFormPago);
				// return "explora?faces-redirect=true&cateId=" + id;
				this.id = (Integer) id;
			} else {
				fpmeFormPagoRegis.modificar(fpmeFormPago);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "explora?faces-redirect=true&cateId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&cateId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&cateId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			FpmeFormPago fpmeFormPago = fpmeFormPagoRegis.buscarPorId(FpmeFormPago.class, this.getId());
			fpmeFormPagoRegis.eliminar(fpmeFormPago);

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

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public FpmeFormPago getFpmeFormPago() {
		return fpmeFormPago;
	}

	public void setFpmeFormPago(FpmeFormPago fpmeFormPago) {
		this.fpmeFormPago = fpmeFormPago;
	}

	public List<FpmeFormPago> getFpmeFormPagos() {
		return fpmeFormPagos;
	}

	public void setFpmeFormPagos(List<FpmeFormPago> fpmeFormPagos) {
		this.fpmeFormPagos = fpmeFormPagos;
	}
}