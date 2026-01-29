package ec.com.tecnointel.soem.firmaElec.controlador;

import java.io.Serializable;
import java.util.List;

import ec.com.tecnointel.soem.firmaElec.listaInt.CertEmisListaInt;
import ec.com.tecnointel.soem.firmaElec.modelo.CertEmis;
import ec.com.tecnointel.soem.firmaElec.registroInt.CertEmisRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class CertEmisControl extends PaginaControl implements Serializable {

	private CertEmis certEmis;

	private List<CertEmis> certEmiss;

	@Inject
	CertEmisRegisInt certEmisRegis;

	@Inject
	CertEmisListaInt certEmisLista;

	private static final long serialVersionUID = -470125811775844862L;

	@PostConstruct
	public void cargar() {

		certEmis = new CertEmis();
		certEmis.setEstado(true);

		this.rolPermiso = variablesSesion.getRolPermiso();
	}

	public void buscar() {

		try {
			
			certEmisLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.certEmiss = certEmisLista.buscar(certEmis, this.pagina);
			this.numeroReg = certEmiss.size();
			this.contadorReg = certEmisLista.contarRegistros(certEmis);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar datos certificados"));
			e.printStackTrace();
		}
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}

		if (this.id == null) {
			this.certEmis = new CertEmis();
			this.certEmis.setEstado(true);
		} else {

			try {
				this.certEmis = certEmisRegis.buscarPorId(CertEmis.class, this.getId());
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
				Object id = certEmisRegis.insertar(certEmis);
				this.id = (Integer) id;
			} else {
				certEmisRegis.modificar(certEmis);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "explora?faces-redirect=true&certEmisId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&certEmisId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&certEmisId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			CertEmis certEmis = certEmisRegis.buscarPorId(CertEmis.class, this.getId());
			certEmisRegis.eliminar(certEmis);

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

	public CertEmis getCertEmis() {
		return certEmis;
	}

	public void setCertEmis(CertEmis certEmis) {
		this.certEmis = certEmis;
	}

	public List<CertEmis> getCertEmiss() {
		return certEmiss;
	}

	public void setCertEmiss(List<CertEmis> certEmiss) {
		this.certEmiss = certEmiss;
	}

}