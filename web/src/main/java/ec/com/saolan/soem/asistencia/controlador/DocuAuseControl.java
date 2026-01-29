package ec.com.saolan.soem.asistencia.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.DocuAuseListaInt;
import ec.com.saolan.soem.asistencia.modelo.DocuAuse;
import ec.com.saolan.soem.asistencia.registroInt.DocuAuseRegisInt;
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
public class DocuAuseControl extends PaginaControl implements Serializable {

	private DocuAuse docuAuse;

	private List<DocuAuse> docuAuses;
	private List<Parametro> parameEstados;

	@Inject
	DocuAuseRegisInt docuAuseRegis;

	@Inject
	DocuAuseListaInt docuAuseLista;

	private static final long serialVersionUID = 587772436262634743L;

	@PostConstruct
	public void cargar() {

		this.docuAuse = new DocuAuse();
		this.docuAuse.setEstado("Activo");
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
			DocuAuse docuAuse = docuAuseRegis.buscarPorId(DocuAuse.class, this.getId());
			docuAuseRegis.eliminar(docuAuse);

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
		return "explora?faces-redirect=true&docuAuseId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = docuAuseRegis.insertar(docuAuse);
				this.id = (Integer) id;
			} else {
				docuAuseRegis.modificar(docuAuse);
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

		return "explora?faces-redirect=true&docuAuseId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&docuAuseId=" + this.getId();
	}

	public void preCargarLista() {

		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			docuAuseLista.filasPagina(variablesSesion.getFilasPagina());

			this.docuAuses = docuAuseLista.buscar(this.docuAuse, this.pagina);
			this.numeroReg = docuAuses.size();
			this.contadorReg = docuAuseLista.contarRegistros(this.docuAuse);
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
			this.docuAuse = new DocuAuse();
			this.docuAuse.setEstado("Activo");
		} else {

			try {
				this.docuAuse = docuAuseRegis.buscarPorId(DocuAuse.class, this.getId());
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

	public DocuAuse getDocuAuse() {
		return docuAuse;
	}

	public void setDocuAuse(DocuAuse docuAuse) {
		this.docuAuse = docuAuse;
	}

	public List<DocuAuse> getDocuAuses() {
		return docuAuses;
	}

	public void setDocuAuses(List<DocuAuse> docuAuses) {
		this.docuAuses = docuAuses;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}