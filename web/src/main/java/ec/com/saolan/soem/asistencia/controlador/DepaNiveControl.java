package ec.com.saolan.soem.asistencia.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.DepaNiveListaInt;
import ec.com.saolan.soem.asistencia.modelo.DepaNive;
import ec.com.saolan.soem.asistencia.registroInt.DepaNiveRegisInt;
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
public class DepaNiveControl extends PaginaControl implements Serializable {

	private DepaNive depaNive;

	private List<DepaNive> depaNives;
	private List<Parametro> parameEstados;

	@Inject
	DepaNiveRegisInt depaNiveRegis;

	@Inject
	DepaNiveListaInt depaNiveLista;

	private static final long serialVersionUID = 3512713862649053466L;

	@PostConstruct
	public void cargar() {

		this.depaNive = new DepaNive();
		// this.depaNive.setEstado("Activo");
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
			DepaNive depaNive = depaNiveRegis.buscarPorId(DepaNive.class, this.getId());
			depaNiveRegis.eliminar(depaNive);

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
		return "explora?faces-redirect=true&depaNiveId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = depaNiveRegis.insertar(depaNive);
				this.id = (Integer) id;
			} else {
				depaNiveRegis.modificar(depaNive);
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

		return "explora?faces-redirect=true&depaNiveId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&depaNiveId=" + this.getId();
	}

	public void preCargarLista() {

//		Se activa o se desactiva depandiendo si la tabla tiene o no el campo estado
//		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			depaNiveLista.filasPagina(variablesSesion.getFilasPagina());

			this.depaNives = depaNiveLista.buscar(this.depaNive, this.pagina);
			this.numeroReg = depaNives.size();
			this.contadorReg = depaNiveLista.contarRegistros(this.depaNive);
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

//		Se activa o se desactiva dependiendo si la tabla tiene o no el campo estado
//		this.parameEstados = this.buscarParameEstados();
		
		if (this.id == null) {
			this.depaNive = new DepaNive();
			// this.depaNive.setEstado("Activo");
		} else {

			try {
				this.depaNive = depaNiveRegis.buscarPorId(DepaNive.class, this.getId());
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

	public DepaNive getDepaNive() {
		return depaNive;
	}

	public void setDepaNive(DepaNive depaNive) {
		this.depaNive = depaNive;
	}

	public List<DepaNive> getDepaNives() {
		return depaNives;
	}

	public void setDepaNives(List<DepaNive> depaNives) {
		this.depaNives = depaNives;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}