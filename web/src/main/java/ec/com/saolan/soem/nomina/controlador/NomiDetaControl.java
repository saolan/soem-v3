package ec.com.saolan.soem.nomina.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.nomina.listaInt.NomiDetaListaInt;
import ec.com.saolan.soem.nomina.modelo.NomiDeta;
import ec.com.saolan.soem.nomina.registroInt.NomiDetaRegisInt;
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
public class NomiDetaControl extends PaginaControl implements Serializable {

	private NomiDeta nomiMovi;

	private List<NomiDeta> nomiMovis;
	private List<Parametro> parameEstados;

	@Inject
	NomiDetaRegisInt nomiMoviRegis;

	@Inject
	NomiDetaListaInt nomiMoviLista;

	private static final long serialVersionUID = -9217054586819044040L;

	@PostConstruct
	public void cargar() {

		this.nomiMovi = new NomiDeta();
		this.nomiMovi.setEstado("Activo");
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
			NomiDeta nomiMovi = nomiMoviRegis.buscarPorId(NomiDeta.class, this.getId());
			nomiMoviRegis.eliminar(nomiMovi);

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
		return "explora?faces-redirect=true&nomiMoviId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = nomiMoviRegis.insertar(nomiMovi);
				this.id = (Integer) id;
			} else {
				nomiMoviRegis.modificar(nomiMovi);
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

		return "explora?faces-redirect=true&nomiMoviId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&nomiMoviId=" + this.getId();
	}

	public void preCargarLista() {

		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			nomiMoviLista.filasPagina(variablesSesion.getFilasPagina());

			this.nomiMovis = nomiMoviLista.buscar(this.nomiMovi, this.pagina);
			this.numeroReg = nomiMovis.size();
			this.contadorReg = nomiMoviLista.contarRegistros(this.nomiMovi);
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
			this.nomiMovi = new NomiDeta();
			this.nomiMovi.setEstado("Activo");
		} else {

			try {
				this.nomiMovi = nomiMoviRegis.buscarPorId(NomiDeta.class, this.getId());
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

	public NomiDeta getNomiMovi() {
		return nomiMovi;
	}

	public void setNomiMovi(NomiDeta nomiMovi) {
		this.nomiMovi = nomiMovi;
	}

	public List<NomiDeta> getNomiMovis() {
		return nomiMovis;
	}

	public void setNomiMovis(List<NomiDeta> nomiMovis) {
		this.nomiMovis = nomiMovis;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}