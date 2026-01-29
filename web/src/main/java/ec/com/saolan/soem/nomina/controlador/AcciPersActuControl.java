package ec.com.saolan.soem.nomina.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.nomina.listaInt.AcciPersActuListaInt;
import ec.com.saolan.soem.nomina.modelo.AcciPersActu;
import ec.com.saolan.soem.nomina.registroInt.AcciPersActuRegisInt;
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
public class AcciPersActuControl extends PaginaControl implements Serializable {

	private AcciPersActu acciPersActu;

	private List<AcciPersActu> acciPersActus;
	private List<Parametro> parameEstados;

	@Inject
	AcciPersActuRegisInt acciPersActuRegis;

	@Inject
	AcciPersActuListaInt acciPersActuLista;

	private static final long serialVersionUID = 6695235894791325857L;

	@PostConstruct
	public void cargar() {

		this.acciPersActu = new AcciPersActu();
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
			AcciPersActu acciPersActu = acciPersActuRegis.buscarPorId(AcciPersActu.class, this.getId());
			acciPersActuRegis.eliminar(acciPersActu);

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
		return "explora?faces-redirect=true&acciPersActuId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = acciPersActuRegis.insertar(acciPersActu);
				this.id = (Integer) id;
			} else {
				acciPersActuRegis.modificar(acciPersActu);
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

		return "explora?faces-redirect=true&acciPersActuId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&acciPersActuId=" + this.getId();
	}

	public void preCargarLista() {

//		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			acciPersActuLista.filasPagina(variablesSesion.getFilasPagina());

			this.acciPersActus = acciPersActuLista.buscar(this.acciPersActu, this.pagina);
			this.numeroReg = acciPersActus.size();
			this.contadorReg = acciPersActuLista.contarRegistros(this.acciPersActu);
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
			this.acciPersActu = new AcciPersActu();
		} else {

			try {
				this.acciPersActu = acciPersActuRegis.buscarPorId(AcciPersActu.class, this.getId());
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

	public AcciPersActu getAcciPersActu() {
		return acciPersActu;
	}

	public void setAcciPersActu(AcciPersActu acciPersActu) {
		this.acciPersActu = acciPersActu;
	}

	public List<AcciPersActu> getAcciPersActus() {
		return acciPersActus;
	}

	public void setAcciPersActus(List<AcciPersActu> acciPersActus) {
		this.acciPersActus = acciPersActus;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}