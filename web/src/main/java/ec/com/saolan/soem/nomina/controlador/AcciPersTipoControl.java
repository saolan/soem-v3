package ec.com.saolan.soem.nomina.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.nomina.listaInt.AcciPersTipoListaInt;
import ec.com.saolan.soem.nomina.modelo.AcciPersTipo;
import ec.com.saolan.soem.nomina.registroInt.AcciPersTipoRegisInt;
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
public class AcciPersTipoControl extends PaginaControl implements Serializable {

	private AcciPersTipo acciPersTipo;

	private List<AcciPersTipo> acciPersTipos;
	private List<Parametro> parameEstados;

	@Inject
	AcciPersTipoRegisInt acciPersTipoRegis;

	@Inject
	AcciPersTipoListaInt acciPersTipoLista;

	private static final long serialVersionUID = 8703916593081726486L;

	@PostConstruct
	public void cargar() {

		this.acciPersTipo = new AcciPersTipo();
		this.acciPersTipo.setEstado("Activo");
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
			AcciPersTipo acciPersTipo = acciPersTipoRegis.buscarPorId(AcciPersTipo.class, this.getId());
			acciPersTipoRegis.eliminar(acciPersTipo);

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
		return "explora?faces-redirect=true&acciPersTipoId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = acciPersTipoRegis.insertar(acciPersTipo);
				this.id = (Integer) id;
			} else {
				acciPersTipoRegis.modificar(acciPersTipo);
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

		return "explora?faces-redirect=true&acciPersTipoId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&acciPersTipoId=" + this.getId();
	}

	public void preCargarLista() {

		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			acciPersTipoLista.filasPagina(variablesSesion.getFilasPagina());

			this.acciPersTipos = acciPersTipoLista.buscar(this.acciPersTipo, this.pagina);
			this.numeroReg = acciPersTipos.size();
			this.contadorReg = acciPersTipoLista.contarRegistros(this.acciPersTipo);
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
			this.acciPersTipo = new AcciPersTipo();
			this.acciPersTipo.setEstado("Activo");
		} else {

			try {
				this.acciPersTipo = acciPersTipoRegis.buscarPorId(AcciPersTipo.class, this.getId());
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

	public AcciPersTipo getAcciPersTipo() {
		return acciPersTipo;
	}

	public void setAcciPersTipo(AcciPersTipo acciPersTipo) {
		this.acciPersTipo = acciPersTipo;
	}

	public List<AcciPersTipo> getAcciPersTipos() {
		return acciPersTipos;
	}

	public void setAcciPersTipos(List<AcciPersTipo> acciPersTipos) {
		this.acciPersTipos = acciPersTipos;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}