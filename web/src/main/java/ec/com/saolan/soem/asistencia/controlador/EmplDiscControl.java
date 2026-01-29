package ec.com.saolan.soem.asistencia.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.EmplDiscListaInt;
import ec.com.saolan.soem.asistencia.modelo.EmplDisc;
import ec.com.saolan.soem.asistencia.registroInt.EmplDiscRegisInt;
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
public class EmplDiscControl extends PaginaControl implements Serializable {

	private EmplDisc emplDisc;

	private List<EmplDisc> emplDiscs;
	private List<Parametro> parameEstados;

	@Inject
	EmplDiscRegisInt emplDiscRegis;

	@Inject
	EmplDiscListaInt emplDiscLista;

	private static final long serialVersionUID = 2212332369667398603L;

	@PostConstruct
	public void cargar() {

		this.emplDisc = new EmplDisc();
		// this.emplDisc.setEstado("Activo");
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
			EmplDisc emplDisc = emplDiscRegis.buscarPorId(EmplDisc.class, this.getId());
			emplDiscRegis.eliminar(emplDisc);

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
		return "explora?faces-redirect=true&emplDiscId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = emplDiscRegis.insertar(emplDisc);
				this.id = (Integer) id;
			} else {
				emplDiscRegis.modificar(emplDisc);
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

		return "explora?faces-redirect=true&emplDiscId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&emplDiscId=" + this.getId();
	}

	public void preCargarLista() {

//		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			emplDiscLista.filasPagina(variablesSesion.getFilasPagina());

			this.emplDiscs = emplDiscLista.buscar(this.emplDisc, this.pagina);
			this.numeroReg = emplDiscs.size();
			this.contadorReg = emplDiscLista.contarRegistros(this.emplDisc);
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
			this.emplDisc = new EmplDisc();
			// this.emplDisc.setEstado("Activo");
		} else {

			try {
				this.emplDisc = emplDiscRegis.buscarPorId(EmplDisc.class, this.getId());
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

	public EmplDisc getEmplDisc() {
		return emplDisc;
	}

	public void setEmplDisc(EmplDisc emplDisc) {
		this.emplDisc = emplDisc;
	}

	public List<EmplDisc> getEmplDiscs() {
		return emplDiscs;
	}

	public void setEmplDiscs(List<EmplDisc> emplDiscs) {
		this.emplDiscs = emplDiscs;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}