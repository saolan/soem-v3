package ec.com.saolan.soem.asistencia.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.FeriMapaListaInt;
import ec.com.saolan.soem.asistencia.modelo.FeriMapa;
import ec.com.saolan.soem.asistencia.registroInt.FeriMapaRegisInt;
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
public class MapaFeriControl extends PaginaControl implements Serializable {

	private FeriMapa mapaFeri;

	private List<FeriMapa> mapaFeris;
	private List<Parametro> parameEstados;

	@Inject
	FeriMapaRegisInt mapaFeriRegis;

	@Inject
	FeriMapaListaInt mapaFeriLista;

	private static final long serialVersionUID = -6906723896413514107L;

	@PostConstruct
	public void cargar() {

		this.mapaFeri = new FeriMapa();
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
			FeriMapa mapaFeri = mapaFeriRegis.buscarPorId(FeriMapa.class, this.getId());
			mapaFeriRegis.eliminar(mapaFeri);

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
		return "explora?faces-redirect=true&mapaFeriId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = mapaFeriRegis.insertar(mapaFeri);
				this.id = (Integer) id;
			} else {
				mapaFeriRegis.modificar(mapaFeri);
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

		return "explora?faces-redirect=true&mapaFeriId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&mapaFeriId=" + this.getId();
	}

	public void preCargarLista() {

//		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			mapaFeriLista.filasPagina(variablesSesion.getFilasPagina());

			this.mapaFeris = mapaFeriLista.buscar(this.mapaFeri, this.pagina);
			this.numeroReg = mapaFeris.size();
			this.contadorReg = mapaFeriLista.contarRegistros(this.mapaFeri);
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
			this.mapaFeri = new FeriMapa();
		} else {

			try {
				this.mapaFeri = mapaFeriRegis.buscarPorId(FeriMapa.class, this.getId());
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

	public FeriMapa getMapaFeri() {
		return mapaFeri;
	}

	public void setMapaFeri(FeriMapa mapaFeri) {
		this.mapaFeri = mapaFeri;
	}

	public List<FeriMapa> getMapaFeris() {
		return mapaFeris;
	}

	public void setMapaFeris(List<FeriMapa> mapaFeris) {
		this.mapaFeris = mapaFeris;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}