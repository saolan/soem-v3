package ec.com.saolan.soem.asistencia.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.PersEmplListaInt;
import ec.com.saolan.soem.asistencia.modelo.PersEmpl;
import ec.com.saolan.soem.asistencia.registroInt.PersEmplRegisInt;
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
public class PersEmplControl extends PaginaControl implements Serializable {

	private PersEmpl persEmpl;

	private List<PersEmpl> persEmpls;
	private List<Parametro> parameEstados;

	@Inject
	PersEmplRegisInt persEmplRegis;

	@Inject
	PersEmplListaInt persEmplLista;

	private static final long serialVersionUID = 7429741145962396397L;

	@PostConstruct
	public void cargar() {

		this.persEmpl = new PersEmpl();
		this.persEmpl.setEstado("Activo");
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
			PersEmpl persEmpl = persEmplRegis.buscarPorId(PersEmpl.class, this.getId());
			persEmplRegis.eliminar(persEmpl);

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
		return "explora?faces-redirect=true&persEmplId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = persEmplRegis.insertar(persEmpl);
				this.id = (Integer) id;
			} else {
				persEmplRegis.modificar(persEmpl);
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

		return "explora?faces-redirect=true&persEmplId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&persEmplId=" + this.getId();
	}

	public void preCargarLista() {

		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			persEmplLista.filasPagina(variablesSesion.getFilasPagina());

			this.persEmpls = persEmplLista.buscar(this.persEmpl, this.pagina);
			this.numeroReg = persEmpls.size();
			this.contadorReg = persEmplLista.contarRegistros(this.persEmpl);
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
			this.persEmpl = new PersEmpl();
			this.persEmpl.setEstado("Activo");
		} else {

			try {
				this.persEmpl = persEmplRegis.buscarPorId(PersEmpl.class, this.getId());
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

	public PersEmpl getPersEmpl() {
		return persEmpl;
	}

	public void setPersEmpl(PersEmpl persEmpl) {
		this.persEmpl = persEmpl;
	}

	public List<PersEmpl> getPersEmpls() {
		return persEmpls;
	}

	public void setPersEmpls(List<PersEmpl> persEmpls) {
		this.persEmpls = persEmpls;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}