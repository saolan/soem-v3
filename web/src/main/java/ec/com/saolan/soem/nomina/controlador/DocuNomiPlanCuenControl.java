package ec.com.saolan.soem.nomina.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.nomina.listaInt.DocuNomiPlanCuenListaInt;
import ec.com.saolan.soem.nomina.modelo.DocuNomiPlanCuen;
import ec.com.saolan.soem.nomina.registroInt.DocuNomiPlanCuenRegisInt;
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
public class DocuNomiPlanCuenControl extends PaginaControl implements Serializable {

	private DocuNomiPlanCuen docuNomiPlanCuen;

	private List<DocuNomiPlanCuen> docuNomiPlanCuens;
	private List<Parametro> parameEstados;

	@Inject
	DocuNomiPlanCuenRegisInt docuNomiPlanCuenRegis;

	@Inject
	DocuNomiPlanCuenListaInt docuNomiPlanCuenLista;

	private static final long serialVersionUID = -2578982896394577636L;

	@PostConstruct
	public void cargar() {

		this.docuNomiPlanCuen = new DocuNomiPlanCuen();
		// this.docuNomiPlanCuen.setEstado("Activo");
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
			DocuNomiPlanCuen docuNomiPlanCuen = docuNomiPlanCuenRegis.buscarPorId(DocuNomiPlanCuen.class, this.getId());
			docuNomiPlanCuenRegis.eliminar(docuNomiPlanCuen);

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
		return "explora?faces-redirect=true&docuNomiPlanCuenId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = docuNomiPlanCuenRegis.insertar(docuNomiPlanCuen);
				this.id = (Integer) id;
			} else {
				docuNomiPlanCuenRegis.modificar(docuNomiPlanCuen);
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

		return "explora?faces-redirect=true&docuNomiPlanCuenId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&docuNomiPlanCuenId=" + this.getId();
	}

	public void preCargarLista() {

//		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			docuNomiPlanCuenLista.filasPagina(variablesSesion.getFilasPagina());

			this.docuNomiPlanCuens = docuNomiPlanCuenLista.buscar(this.docuNomiPlanCuen, this.pagina);
			this.numeroReg = docuNomiPlanCuens.size();
			this.contadorReg = docuNomiPlanCuenLista.contarRegistros(this.docuNomiPlanCuen);
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
			this.docuNomiPlanCuen = new DocuNomiPlanCuen();
			// this.docuNomiPlanCuen.setEstado("Activo");
		} else {

			try {
				this.docuNomiPlanCuen = docuNomiPlanCuenRegis.buscarPorId(DocuNomiPlanCuen.class, this.getId());
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

	public DocuNomiPlanCuen getDocuNomiPlanCuen() {
		return docuNomiPlanCuen;
	}

	public void setDocuNomiPlanCuen(DocuNomiPlanCuen docuNomiPlanCuen) {
		this.docuNomiPlanCuen = docuNomiPlanCuen;
	}

	public List<DocuNomiPlanCuen> getDocuNomiPlanCuens() {
		return docuNomiPlanCuens;
	}

	public void setDocuNomiPlanCuens(List<DocuNomiPlanCuen> docuNomiPlanCuens) {
		this.docuNomiPlanCuens = docuNomiPlanCuens;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}