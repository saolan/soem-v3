package ec.com.saolan.soem.asistencia.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.EmplTituListaInt;
import ec.com.saolan.soem.asistencia.modelo.EmplTitu;
import ec.com.saolan.soem.asistencia.registroInt.EmplTituRegisInt;
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
public class EmplTituControl extends PaginaControl implements Serializable {

	private EmplTitu emplTitu;

	private List<EmplTitu> emplTitus;
	private List<Parametro> parameEstados;

	@Inject
	EmplTituRegisInt emplTituRegis;

	@Inject
	EmplTituListaInt emplTituLista;

	private static final long serialVersionUID = -583233078314744079L;

	@PostConstruct
	public void cargar() {

		this.emplTitu = new EmplTitu();
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
			EmplTitu emplTitu = emplTituRegis.buscarPorId(EmplTitu.class, this.getId());
			emplTituRegis.eliminar(emplTitu);

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
		return "explora?faces-redirect=true&emplTituId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = emplTituRegis.insertar(emplTitu);
				this.id = (Integer) id;
			} else {
				emplTituRegis.modificar(emplTitu);
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

		return "explora?faces-redirect=true&emplTituId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&emplTituId=" + this.getId();
	}

	public void preCargarLista() {

//		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			emplTituLista.filasPagina(variablesSesion.getFilasPagina());

			this.emplTitus = emplTituLista.buscar(this.emplTitu, this.pagina);
			this.numeroReg = emplTitus.size();
			this.contadorReg = emplTituLista.contarRegistros(this.emplTitu);
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

//		this.parameEstados = this.buscarParameEstados();

		if (this.id == null) {
			this.emplTitu = new EmplTitu();
		} else {

			try {
				this.emplTitu = emplTituRegis.buscarPorId(EmplTitu.class, this.getId());
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

	public EmplTitu getEmplTitu() {
		return emplTitu;
	}

	public void setEmplTitu(EmplTitu emplTitu) {
		this.emplTitu = emplTitu;
	}

	public List<EmplTitu> getEmplTitus() {
		return emplTitus;
	}

	public void setEmplTitus(List<EmplTitu> emplTitus) {
		this.emplTitus = emplTitus;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}