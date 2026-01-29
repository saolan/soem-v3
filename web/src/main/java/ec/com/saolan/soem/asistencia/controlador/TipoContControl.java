package ec.com.saolan.soem.asistencia.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.TipoContListaInt;
import ec.com.saolan.soem.asistencia.modelo.TipoCont;
import ec.com.saolan.soem.asistencia.registroInt.TipoContRegisInt;
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
public class TipoContControl extends PaginaControl implements Serializable {

	private TipoCont tipoCont;

	private List<TipoCont> tipoConts;
	private List<Parametro> parameEstados;

	@Inject
	TipoContRegisInt tipoContRegis;

	@Inject
	TipoContListaInt tipoContLista;

	private static final long serialVersionUID = -680385706620917566L;

	@PostConstruct
	public void cargar() {

		this.tipoCont = new TipoCont();
		this.tipoCont.setEstado("Activo");
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
			TipoCont tipoCont = tipoContRegis.buscarPorId(TipoCont.class, this.getId());
			tipoContRegis.eliminar(tipoCont);

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
		return "explora?faces-redirect=true&tipoContId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = tipoContRegis.insertar(tipoCont);
				this.id = (Integer) id;
			} else {
				tipoContRegis.modificar(tipoCont);
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

		return "explora?faces-redirect=true&tipoContId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&tipoContId=" + this.getId();
	}

	public void preCargarLista() {

		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			tipoContLista.filasPagina(variablesSesion.getFilasPagina());

			this.tipoConts = tipoContLista.buscar(this.tipoCont, this.pagina);
			this.numeroReg = tipoConts.size();
			this.contadorReg = tipoContLista.contarRegistros(this.tipoCont);
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
			this.tipoCont = new TipoCont();
			this.tipoCont.setEstado("Activo");
		} else {

			try {
				this.tipoCont = tipoContRegis.buscarPorId(TipoCont.class, this.getId());
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

	public TipoCont getTipoCont() {
		return tipoCont;
	}

	public void setTipoCont(TipoCont tipoCont) {
		this.tipoCont = tipoCont;
	}

	public List<TipoCont> getTipoConts() {
		return tipoConts;
	}

	public void setTipoConts(List<TipoCont> tipoConts) {
		this.tipoConts = tipoConts;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}