package ec.com.tecnointel.soem.parametro.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.DimmListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.DocuCajaListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.DocuTranListaInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuCaja;
import ec.com.tecnointel.soem.parametro.modelo.DocuTran;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.registroInt.DocuCajaRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.DocumentoRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class DocuCajaControl extends PaginaControl implements Serializable {

	private DocuCaja docuCaja;

	List<DocuCaja> docuCajas;
	List<DocuTran> docuTrans;

	@Inject
	DocuCajaRegisInt docuCajaRegis;

	@Inject
	DocuCajaListaInt docuCajaLista;

	@Inject
	DimmListaInt dimmLista;

	@Inject
	DocuTranListaInt docuTranLista;
	
	@Inject
	DocumentoRegisInt documentoRegis;

	private static final long serialVersionUID = 5920987155275748332L;

	@PostConstruct
	public void cargar() {
		docuCaja = new DocuCaja();
		docuCaja.setDocumento(new Documento());
		docuCaja.getDocumento().setEstado(true);
	}

	public void buscar() {

		try {
			
			docuCajaLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.docuCajas = docuCajaLista.buscar(this.getDocuCaja(), this.pagina);
			this.numeroReg = docuCajas.size();
			this.contadorReg = docuCajaLista.contarRegistros(docuCaja);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}
	
	public List<DocuTran> buscarDocuTrans() {
		
		Documento documento = new Documento();
		documento.setEstado(true);
		
		DocuTran docuTran = new DocuTran();
		docuTran.setDocumento(documento);
		
		List<DocuTran> docuTrans = new ArrayList<>();
		
		try {
			docuTrans = docuTranLista.buscar(docuTran, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar documentos transaccion"));
			e.printStackTrace();
		}
		
		return docuTrans;
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}
		
		this.docuTrans = this.buscarDocuTrans();

		if (this.id == null) {
			this.docuCaja = new DocuCaja();
			this.docuCaja.setDocumento(new Documento());
			this.docuCaja.getDocumento().setCopias((short) 1);
			this.docuCaja.getDocumento().setEstado(true);
		} else {

			try {
				this.docuCaja = docuCajaRegis.buscarPorId(DocuCaja.class, this.getId());
				
				if (this.docuCaja.getDocumento().getDocumento() != null) {
//					Inicializa Proxy documento
					Documento documento = new Documento();
					documento = this.documentoRegis.buscarPorId(Documento.class, this.docuCaja.getDocumento().getDocumento().getDocumentoId());
					this.docuCaja.getDocumento().setDocumento(documento);
//					Fin Inicializa Proxy documento				
				}

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
				Object id = docuCajaRegis.insertar(docuCaja);
				this.id = (Integer) id;
			} else {
				
				docuCajaRegis.modificar(docuCaja);
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
			DocuCaja docuCaja = docuCajaRegis.buscarPorId(DocuCaja.class, this.getId());
			docuCajaRegis.eliminar(docuCaja);

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

	public List<DocuCaja> buscarTodo() {

		List<DocuCaja> docuCajas = new ArrayList<DocuCaja>();

		try {
			docuCajas = docuCajaLista.buscarTodo("documentoId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return docuCajas;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public DocuCaja getDocuCaja() {
		return docuCaja;
	}

	public void setDocuCaja(DocuCaja docuCaja) {
		this.docuCaja = docuCaja;
	}

	public List<DocuCaja> getDocuCajas() {
		return docuCajas;
	}

	public void setDocuCajas(List<DocuCaja> docuCajas) {
		this.docuCajas = docuCajas;
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