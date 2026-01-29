package ec.com.saolan.soem.nomina.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.nomina.listaInt.AcciPersListaInt;
import ec.com.saolan.soem.nomina.modelo.AcciPers;
import ec.com.saolan.soem.nomina.registroInt.AcciPersRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class AcciPersControl extends PaginaControl implements Serializable {

	private AcciPers acciPers;

	private List<AcciPers> acciPerss;
	private List<Parametro> parameEstados;

	@Inject
	AcciPersRegisInt acciPersRegis;

	@Inject
	AcciPersListaInt acciPersLista;

	private static final long serialVersionUID = -4742127218771120328L;

	@PostConstruct
	public void cargar() {

		this.acciPers = new AcciPers();
		this.acciPers.setEstado("Activo");
	}

	public List<Parametro> agregarEstado(List<Parametro> parameEstados) {

		Parametro parametro = new Parametro();
		parametro.setDescri("Todo");
		parametro.setEstado(true);

		parameEstados.add(parametro);

		return parameEstados;
	}

	public List<Parametro> buscarAgregarParameEstados() {

		List<Parametro> parameEstados = this.buscarParameEstados();

		Parametro parametro = new Parametro();
		parametro.setDescri("Todo");
		parametro.setEstado(true);

		parameEstados.add(parametro);

		return parameEstados;
	}

	public List<Parametro> buscarParameEstados() {

		Parametro parametro = new Parametro();
		parametro.setCodigo("Parametro-EstadoRegistro");
		parametro.setEstado(true);

		List<Parametro> parametros = new ArrayList<>();

		try {
			parametros = parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Ex - Error al buscar Parametro-EstadoRegistro"));
			e.printStackTrace();
		}

		return parametros;
	}

	public String eliminar() {

		try {
			AcciPers acciPers = acciPersRegis.buscarPorId(AcciPers.class, this.getId());
			acciPersRegis.eliminar(acciPers);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Ex - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		return "lista?faces-redirect=true";
	}

	public String explorar() {
		return "explora?faces-redirect=true&acciPersId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = acciPersRegis.insertar(acciPers);
				this.id = (Integer) id;
			} else {
				acciPersRegis.modificar(acciPers);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Ex - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "explora?faces-redirect=true&acciPersId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&acciPersId=" + this.getId();
	}

	public void preCargarLista() {

		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			acciPersLista.filasPagina(variablesSesion.getFilasPagina());

			this.acciPerss = acciPersLista.buscar(this.acciPers, this.pagina);
			this.numeroReg = acciPerss.size();
			this.contadorReg = acciPersLista.contarRegistros(this.acciPers);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Ex - Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public void preCargarRegExp() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}

		this.parameEstados = this.buscarParameEstados();

		if (this.id == null) {
			this.acciPers = new AcciPers();
			this.acciPers.setEstado("Activo");
		} else {

			try {
				this.acciPers = acciPersRegis.buscarPorId(AcciPers.class, this.getId());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Ex - Error al buscar Id"));
				e.printStackTrace();
			}
		}
	}

	// <<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>
	// <<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>
	// <<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>

	public AcciPers getAcciPers() {
		return acciPers;
	}

	public void setAcciPers(AcciPers acciPers) {
		this.acciPers = acciPers;
	}

	public List<AcciPers> getAcciPerss() {
		return acciPerss;
	}

	public void setAcciPerss(List<AcciPers> acciPerss) {
		this.acciPerss = acciPerss;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}