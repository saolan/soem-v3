package ec.com.saolan.soem.asistencia.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.DepaContListaInt;
import ec.com.saolan.soem.asistencia.modelo.DepaCont;
import ec.com.saolan.soem.asistencia.registroInt.DepaContRegisInt;
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
public class DepaContControl extends PaginaControl implements Serializable {

	private DepaCont depaCont;

	private List<DepaCont> depaConts;
	private List<Parametro> parameEstados;

	@Inject
	DepaContRegisInt depaContRegis;

	@Inject
	DepaContListaInt depaContLista;

	private static final long serialVersionUID = 2000697934065428184L;

	@PostConstruct
	public void cargar() {

		this.depaCont = new DepaCont();
		this.depaCont.setEstado("Activo");
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
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
							"Ex - Error al buscar Parametro-EstadoRegistro"));
			e.printStackTrace();
		}

		return parametros;
	}

	public String eliminar() {

		try {
			DepaCont depaCont = depaContRegis.buscarPorId(DepaCont.class, this.getId());
			depaContRegis.eliminar(depaCont);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
							"Ex - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().getExternalContext().getFlash()
				.setKeepMessages(true);
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null,
						"Registro eliminado"));

		return "lista?faces-redirect=true";
	}

	public String explorar() {
		return "explora?faces-redirect=true&depaContId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = depaContRegis.insertar(depaCont);
				this.id = (Integer) id;
			} else {
				depaContRegis.modificar(depaCont);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
							"Ex - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().getExternalContext().getFlash()
				.setKeepMessages(true);
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null,
						"Registro grabado"));

		return "explora?faces-redirect=true&depaContId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&depaContId=" + this.getId();
	}

	public void preCargarLista() {

		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			depaContLista.filasPagina(variablesSesion.getFilasPagina());

			this.depaConts = depaContLista.buscar(this.depaCont, this.pagina);
			this.numeroReg = depaConts.size();
			this.contadorReg = depaContLista.contarRegistros(this.depaCont);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
							"Ex - Error al buscar datos"));
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
			this.depaCont = new DepaCont();
			this.depaCont.setEstado("Activo");
		} else {

			try {
				this.depaCont = depaContRegis.buscarPorId(DepaCont.class, this.getId());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
								"Ex - Error al buscar Id"));
				e.printStackTrace();
			}
		}
	}

	// <<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>
	// <<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>
	// <<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>

	public DepaCont getDepaCont() {
		return depaCont;
	}

	public void setDepaCont(DepaCont depaCont) {
		this.depaCont = depaCont;
	}

	public List<DepaCont> getDepaConts() {
		return depaConts;
	}

	public void setDepaConts(List<DepaCont> depaConts) {
		this.depaConts = depaConts;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}