package ec.com.tecnointel.soem.egreso.controlador;

import java.io.Serializable;
import java.util.List;

import ec.com.tecnointel.soem.egreso.listaInt.EgreNotaListaInt;
import ec.com.tecnointel.soem.egreso.modelo.EgreNota;
import ec.com.tecnointel.soem.egreso.registroInt.EgreNotaRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class EgreNotaControl extends PaginaControl implements Serializable {

	private static final long serialVersionUID = 6362485674322665421L;

	private EgreNota egreNota;

	List<EgreNota> egreNotas;

	@Inject
	EgreNotaRegisInt egreNotaRegis;

	@Inject
	EgreNotaListaInt egreNotaLista;

	@PostConstruct
	public void cargar() {
		egreNota = new EgreNota();
	}

	public void buscar() {

		try {
			
			egreNotaLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.egreNotas = egreNotaLista.buscar(egreNota, this.pagina);
			this.numeroReg = egreNotas.size();
			this.contadorReg = egreNotaLista.contarRegistros(egreNota);
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
			this.egreNota = new EgreNota();
		} else {

			try {
				this.egreNota = egreNotaRegis.buscarPorId(EgreNota.class, this.getId());
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
				Object id = egreNotaRegis.insertar(egreNota);
				this.id = (Integer) id;
			} else {
				egreNotaRegis.modificar(egreNota);
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
		return "registra?faces-redirect=true&egreNotaId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&egreNotaId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			EgreNota egreNota = egreNotaRegis.buscarPorId(EgreNota.class, this.getId());
			egreNotaRegis.eliminar(egreNota);

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

	public EgreNota getEgreNota() {
		return egreNota;
	}

	public void setEgreNota(EgreNota egreNota) {
		this.egreNota = egreNota;
	}

	public List<EgreNota> getEgreNotas() {
		return egreNotas;
	}

	public void setEgreNotas(List<EgreNota> egreNotas) {
		this.egreNotas = egreNotas;
	}

}