package ec.com.saolan.soem.nomina.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.nomina.listaInt.AcciPersAutoListaInt;
import ec.com.saolan.soem.nomina.modelo.AcciPersAuto;
import ec.com.saolan.soem.nomina.registroInt.AcciPersAutoRegisInt;
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
public class AcciPersAutoControl extends PaginaControl implements Serializable {

	private AcciPersAuto acciPersAuto;

	private List<AcciPersAuto> acciPersAutos;
	private List<Parametro> parameEstados;

	@Inject
	AcciPersAutoRegisInt acciPersAutoRegis;

	@Inject
	AcciPersAutoListaInt acciPersAutoLista;

	private static final long serialVersionUID = -4881067224105261081L;

	@PostConstruct
	public void cargar() {

		this.acciPersAuto = new AcciPersAuto();
		this.acciPersAuto.setEstado("Activo");
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
			AcciPersAuto acciPersAuto = acciPersAutoRegis.buscarPorId(AcciPersAuto.class, this.getId());
			acciPersAutoRegis.eliminar(acciPersAuto);

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
		return "explora?faces-redirect=true&acciPersAutoId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = acciPersAutoRegis.insertar(acciPersAuto);
				this.id = (Integer) id;
			} else {
				acciPersAutoRegis.modificar(acciPersAuto);
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

		return "explora?faces-redirect=true&acciPersAutoId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&acciPersAutoId=" + this.getId();
	}

	public void preCargarLista() {

		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			acciPersAutoLista.filasPagina(variablesSesion.getFilasPagina());

			this.acciPersAutos = acciPersAutoLista.buscar(this.acciPersAuto, this.pagina);
			this.numeroReg = acciPersAutos.size();
			this.contadorReg = acciPersAutoLista.contarRegistros(this.acciPersAuto);
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
			this.acciPersAuto = new AcciPersAuto();
			this.acciPersAuto.setEstado("Activo");
		} else {

			try {
				this.acciPersAuto = acciPersAutoRegis.buscarPorId(AcciPersAuto.class, this.getId());
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

	public AcciPersAuto getAcciPersAuto() {
		return acciPersAuto;
	}

	public void setAcciPersAuto(AcciPersAuto acciPersAuto) {
		this.acciPersAuto = acciPersAuto;
	}

	public List<AcciPersAuto> getAcciPersAutos() {
		return acciPersAutos;
	}

	public void setAcciPersAutos(List<AcciPersAuto> acciPersAutos) {
		this.acciPersAutos = acciPersAutos;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}