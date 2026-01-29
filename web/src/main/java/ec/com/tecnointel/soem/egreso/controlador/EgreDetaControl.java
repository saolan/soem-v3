package ec.com.tecnointel.soem.egreso.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.egreso.listaInt.EgreDetaListaInt;
import ec.com.tecnointel.soem.egreso.modelo.EgreDeta;
import ec.com.tecnointel.soem.egreso.registroInt.EgreDetaRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class EgreDetaControl extends PaginaControl implements Serializable {

	private EgreDeta egreDeta;

	List<EgreDeta> egreDetas;

	@Inject
	EgreDetaRegisInt egreDetaRegis;

	@Inject
	EgreDetaListaInt egreDetaLista;

	private static final long serialVersionUID = -6962770887912170637L;

	@PostConstruct
	public void cargar() {

		egreDeta = new EgreDeta();

	}

	public void buscar() {

		try {
			
			egreDetaLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.egreDetas = egreDetaLista.buscar(egreDeta, this.pagina);
			this.numeroReg = egreDetas.size();
			this.contadorReg = egreDetaLista.contarRegistros(egreDeta);
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
			this.egreDeta = new EgreDeta();
		} else {

			try {
				this.egreDeta = egreDetaRegis.buscarPorId(EgreDeta.class, this.getId());
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
				Object id = egreDetaRegis.insertar(egreDeta);
				this.id = (Integer) id;
			} else {
				egreDetaRegis.modificar(egreDeta);
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
		return "registra?faces-redirect=true&egreDetaId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&egreDetaId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			EgreDeta egreDeta = egreDetaRegis.buscarPorId(EgreDeta.class, this.getId());
			egreDetaRegis.eliminar(egreDeta);

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

	public List<EgreDeta> buscarTodo() {

		List<EgreDeta> egreDetas = new ArrayList<>();

		try {
			egreDetas = egreDetaLista.buscarTodo("egreDetaId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return egreDetas;
	}
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public EgreDeta getEgreDeta() {
		return egreDeta;
	}

	public void setEgreDeta(EgreDeta egreDeta) {
		this.egreDeta = egreDeta;
	}

	public List<EgreDeta> getEgreDetas() {
		return egreDetas;
	}

	public void setEgreDetas(List<EgreDeta> egreDetas) {
		this.egreDetas = egreDetas;
	}
}