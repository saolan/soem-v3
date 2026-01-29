package ec.com.saolan.soem.asistencia.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.EmplCargVacaListaInt;
import ec.com.saolan.soem.asistencia.modelo.EmplCargVaca;
import ec.com.saolan.soem.asistencia.registroInt.EmplCargVacaRegisInt;
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
public class EmplCargVacaControl extends PaginaControl implements Serializable {

	private EmplCargVaca emplCargVaca;

	private List<EmplCargVaca> emplCargVacas;
	private List<Parametro> parameEstados;

	@Inject
	EmplCargVacaRegisInt emplCargVacaRegis;

	@Inject
	EmplCargVacaListaInt emplCargVacaLista;

	private static final long serialVersionUID = 4605151385992827491L;

	@PostConstruct
	public void cargar() {

		this.emplCargVaca = new EmplCargVaca();
		// this.emplCargVaca.setEstado("Activo");
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
			EmplCargVaca emplCargVaca = emplCargVacaRegis.buscarPorId(EmplCargVaca.class, this.getId());
			emplCargVacaRegis.eliminar(emplCargVaca);

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
		return "explora?faces-redirect=true&emplCargVacaId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = emplCargVacaRegis.insertar(emplCargVaca);
				this.id = (Integer) id;
			} else {
				emplCargVacaRegis.modificar(emplCargVaca);
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

		return "explora?faces-redirect=true&emplCargVacaId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&emplCargVacaId=" + this.getId();
	}

	public void preCargarLista() {

//		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			emplCargVacaLista.filasPagina(variablesSesion.getFilasPagina());

			this.emplCargVacas = emplCargVacaLista.buscar(this.emplCargVaca, this.pagina);
			this.numeroReg = emplCargVacas.size();
			this.contadorReg = emplCargVacaLista.contarRegistros(this.emplCargVaca);
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
			this.emplCargVaca = new EmplCargVaca();
			// this.emplCargVaca.setEstado("Activo");
		} else {

			try {
				this.emplCargVaca = emplCargVacaRegis.buscarPorId(EmplCargVaca.class, this.getId());
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

	public EmplCargVaca getEmplCargVaca() {
		return emplCargVaca;
	}

	public void setEmplCargVaca(EmplCargVaca emplCargVaca) {
		this.emplCargVaca = emplCargVaca;
	}

	public List<EmplCargVaca> getEmplCargVacas() {
		return emplCargVacas;
	}

	public void setEmplCargVacas(List<EmplCargVaca> emplCargVacas) {
		this.emplCargVacas = emplCargVacas;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}