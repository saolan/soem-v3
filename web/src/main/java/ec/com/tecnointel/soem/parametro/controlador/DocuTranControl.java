package ec.com.tecnointel.soem.parametro.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.DimmListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.DocuTranListaInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuTran;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.registroInt.DocuTranRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class DocuTranControl extends PaginaControl implements Serializable {

	private DocuTran docuTran;

	List<DocuTran> docuTrans;

	@Inject
	DocuTranRegisInt docuTranRegis;

	@Inject
	DocuTranListaInt docuTranLista;

	@Inject
	DimmListaInt dimmLista;

	private static final long serialVersionUID = -4788086629949937887L;

	@PostConstruct
	public void cargar() {
		docuTran = new DocuTran();
		docuTran.setDocumento(new Documento());
		docuTran.getDocumento().setEstado(true);
	}

	public void buscar() {

		try {
			
			docuTranLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.docuTrans = docuTranLista.buscar(this.getDocuTran(), this.pagina);
			this.numeroReg = docuTrans.size();
			this.contadorReg = docuTranLista.contarRegistros(docuTran);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}

		if (this.id == null) {
			this.docuTran = new DocuTran();
			this.docuTran.setDocumento(new Documento());
			this.docuTran.getDocumento().setCopias((short) 1);
			this.docuTran.getDocumento().setEstado(true);
		} else {

			try {
				this.docuTran = docuTranRegis.buscarPorId(DocuTran.class, this.getId());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
				e.printStackTrace();
			}
		}
	}

	public String grabar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			if (this.id == null) {
				
				this.docuTran.getDocumento().setFactor((short) 0);
				this.docuTran.getDocumento().setContab(true);
				
				Object id = docuTranRegis.insertar(docuTran);
				this.id = (Integer) id;
			} else {
				
				docuTranRegis.modificar(docuTran);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "lista?faces-redirect=true";
	}

	public String modificar() {
		return "registra?faces-redirect=true&documentoId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&documentoId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			DocuTran docuTran = docuTranRegis.buscarPorId(DocuTran.class, this.getId());
			docuTranRegis.eliminar(docuTran);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		return "lista?faces-redirect=true";
	}

	public List<DocuTran> buscarTodo() {

		List<DocuTran> docuTrans = new ArrayList<DocuTran>();

		try {
			docuTrans = docuTranLista.buscarTodo("documentoId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return docuTrans;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public DocuTran getDocuTran() {
		return docuTran;
	}

	public void setDocuTran(DocuTran docuTran) {
		this.docuTran = docuTran;
	}

	public List<DocuTran> getDocuTrans() {
		return docuTrans;
	}

	public void setDocuTrans(List<DocuTran> docuTrans) {
		this.docuTrans = docuTrans;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}