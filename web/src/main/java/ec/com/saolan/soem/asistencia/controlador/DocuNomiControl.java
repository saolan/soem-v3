package ec.com.saolan.soem.asistencia.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.DocuNomiListaInt;
import ec.com.saolan.soem.asistencia.registroInt.DocuNomiRegisInt;
import ec.com.saolan.soem.nomina.modelo.DocuNomi;
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
public class DocuNomiControl extends PaginaControl implements Serializable {

	private DocuNomi docuNomi;

	private List<DocuNomi> docuNomis;
	private List<Parametro> parameEstados;

	@Inject
	DocuNomiRegisInt docuNomiRegis;

	@Inject
	DocuNomiListaInt docuNomiLista;

	private static final long serialVersionUID = -6767075087158223180L;

	@PostConstruct
	public void cargar() {

		this.docuNomi = new DocuNomi();
		this.docuNomi.setEstado("Activo");
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
			DocuNomi docuNomi = docuNomiRegis.buscarPorId(DocuNomi.class, this.getId());
			docuNomiRegis.eliminar(docuNomi);

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
		return "explora?faces-redirect=true&docuNomiId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = docuNomiRegis.insertar(docuNomi);
				this.id = (Integer) id;
			} else {
				docuNomiRegis.modificar(docuNomi);
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

		return "explora?faces-redirect=true&docuNomiId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&docuNomiId=" + this.getId();
	}

	public void preCargarLista() {

		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			docuNomiLista.filasPagina(variablesSesion.getFilasPagina());

			this.docuNomis = docuNomiLista.buscar(this.docuNomi, this.pagina);
			this.numeroReg = docuNomis.size();
			this.contadorReg = docuNomiLista.contarRegistros(this.docuNomi);
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
			this.docuNomi = new DocuNomi();
			this.docuNomi.setEstado("Activo");
		} else {

			try {
				this.docuNomi = docuNomiRegis.buscarPorId(DocuNomi.class, this.getId());
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

	public DocuNomi getDocuNomi() {
		return docuNomi;
	}

	public void setDocuNomi(DocuNomi docuNomi) {
		this.docuNomi = docuNomi;
	}

	public List<DocuNomi> getDocuNomis() {
		return docuNomis;
	}

	public void setDocuNomis(List<DocuNomi> docuNomis) {
		this.docuNomis = docuNomis;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}