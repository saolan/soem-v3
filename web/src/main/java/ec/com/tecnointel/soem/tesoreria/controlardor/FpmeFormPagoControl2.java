package ec.com.tecnointel.soem.tesoreria.controlardor;

import java.io.Serializable;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.tesoreria.listaInt.FpmeFormPagoListaInt;
import ec.com.tecnointel.soem.tesoreria.modelo.FpmeFormPago;
import ec.com.tecnointel.soem.tesoreria.registroInt.FpmeFormPagoRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class FpmeFormPagoControl2 extends PaginaControl implements Serializable {

	private FpmeFormPago fpmeFormPago;

	List<FpmeFormPago> fpmeFormPagos;

	private static final long serialVersionUID = -7338527583746121299L;

	@Inject
	FpmeFormPagoRegisInt fpmeFormPagoRegis;

	@Inject
	FpmeFormPagoListaInt fpmeFormPagoLista;

	@PostConstruct
	public void cargar() {

		fpmeFormPago = new FpmeFormPago();

	}

	public void buscar() {

		try {
			this.fpmeFormPagos = fpmeFormPagoLista.buscar(fpmeFormPago, this.pagina);
			this.numeroReg = fpmeFormPagos.size();
			this.contadorReg = fpmeFormPagoLista.contarRegistros(fpmeFormPago);
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
	// listener='#{fpmeFormPagoControlador.buscarTodo}' />
	// </f:metadata>

	// public void buscarTodo() {
	// try {
	// this.fpmeFormPagos =
	// fpmeFormPagoConsultaInterface.fpmeFormPagoConsultar(this.getFpmeFormPago());
	// } catch (Exception e) {
	// // TODOs Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

}