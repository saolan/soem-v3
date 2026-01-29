package ec.com.tecnointel.soem.contabilidad.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.contabilidad.listaInt.TranDetaListaInt;
import ec.com.tecnointel.soem.contabilidad.modelo.TranDeta;
import ec.com.tecnointel.soem.contabilidad.modelo.Transaccion;
import ec.com.tecnointel.soem.contabilidad.registroInt.TranDetaRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class TranDetaControl extends PaginaControl implements Serializable {

	private TranDeta tranDeta;

	List<TranDeta> tranDetas;

	@Inject
	TranDetaRegisInt tranDetaRegis;

	@Inject
	TranDetaListaInt tranDetaLista;

	private static final long serialVersionUID = -3207183181470045015L;

	@PostConstruct
	public void cargar() {

		tranDeta = new TranDeta();
		this.tranDeta.setTransaccion(new Transaccion());

	}

	public void buscar() {

		try {
			
			tranDetaLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.tranDetas = tranDetaLista.buscar(tranDeta, this.pagina);
			this.numeroReg = tranDetas.size();
			this.contadorReg = tranDetaLista.contarRegistros(tranDeta);
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
			this.tranDeta = new TranDeta();
		} else {

			try {
				this.tranDeta = tranDetaRegis.buscarPorId(TranDeta.class, this.getId());
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
				Object id = tranDetaRegis.insertar(tranDeta);
				this.id = (Integer) id;
			} else {
				tranDetaRegis.modificar(tranDeta);
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
		return "registra?faces-redirect=true&tranDetaId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&tranDetaId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			TranDeta tranDeta = tranDetaRegis.buscarPorId(TranDeta.class, this.getId());
			tranDetaRegis.eliminar(tranDeta);

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

	public List<TranDeta> buscarTodo() {

		List<TranDeta> tranDetas = new ArrayList<>();

		try {
			tranDetas = tranDetaLista.buscarTodo("tranDetaId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return tranDetas;
	}
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public TranDeta getTranDeta() {
		return tranDeta;
	}

	public void setTranDeta(TranDeta tranDeta) {
		this.tranDeta = tranDeta;
	}

	public List<TranDeta> getTranDetas() {
		return tranDetas;
	}

	public void setTranDetas(List<TranDeta> tranDetas) {
		this.tranDetas = tranDetas;
	}
}