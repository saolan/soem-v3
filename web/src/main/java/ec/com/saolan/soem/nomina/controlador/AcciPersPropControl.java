package ec.com.saolan.soem.nomina.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.nomina.listaInt.AcciPersPropListaInt;
import ec.com.saolan.soem.nomina.modelo.AcciPersProp;
import ec.com.saolan.soem.nomina.registroInt.AcciPersPropRegisInt;
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
public class AcciPersPropControl extends PaginaControl implements Serializable {

	private AcciPersProp acciPersProp;

	private List<AcciPersProp> acciPersProps;
	private List<Parametro> parameEstados;

	@Inject
	AcciPersPropRegisInt acciPersPropRegis;

	@Inject
	AcciPersPropListaInt acciPersPropLista;

	private static final long serialVersionUID = 4750923270163205933L;

	@PostConstruct
	public void cargar() {

		this.acciPersProp = new AcciPersProp();
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
			AcciPersProp acciPersProp = acciPersPropRegis.buscarPorId(AcciPersProp.class, this.getId());
			acciPersPropRegis.eliminar(acciPersProp);

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
		return "explora?faces-redirect=true&acciPersPropId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = acciPersPropRegis.insertar(acciPersProp);
				this.id = (Integer) id;
			} else {
				acciPersPropRegis.modificar(acciPersProp);
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

		return "explora?faces-redirect=true&acciPersPropId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&acciPersPropId=" + this.getId();
	}

	public void preCargarLista() {

//		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			acciPersPropLista.filasPagina(variablesSesion.getFilasPagina());

			this.acciPersProps = acciPersPropLista.buscar(this.acciPersProp, this.pagina);
			this.numeroReg = acciPersProps.size();
			this.contadorReg = acciPersPropLista.contarRegistros(this.acciPersProp);
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
			this.acciPersProp = new AcciPersProp();
		} else {

			try {
				this.acciPersProp = acciPersPropRegis.buscarPorId(AcciPersProp.class, this.getId());
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

	public AcciPersProp getAcciPersProp() {
		return acciPersProp;
	}

	public void setAcciPersProp(AcciPersProp acciPersProp) {
		this.acciPersProp = acciPersProp;
	}

	public List<AcciPersProp> getAcciPersProps() {
		return acciPersProps;
	}

	public void setAcciPersProps(List<AcciPersProp> acciPersProps) {
		this.acciPersProps = acciPersProps;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}