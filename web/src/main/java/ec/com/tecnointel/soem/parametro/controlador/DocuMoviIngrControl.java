package ec.com.tecnointel.soem.parametro.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.DocuMoviIngrListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.DocuTranListaInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuMoviIngr;
import ec.com.tecnointel.soem.parametro.modelo.DocuTran;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.registroInt.DocuMoviIngrRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.DocumentoRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class DocuMoviIngrControl extends PaginaControl implements Serializable {

	private DocuMoviIngr docuMoviIngr;

	private List<DocuMoviIngr> docuMoviIngrs;
	private List<DocuTran> docuTrans;
	
	private List<Parametro> parameTipos;

	@Inject
	DocuMoviIngrRegisInt docuMoviIngrRegis;

	@Inject
	DocuMoviIngrListaInt docuMoviIngrLista;
	
	@Inject
	DocuTranListaInt docuTranLista;
	
	@Inject
	DocumentoRegisInt documentoRegis;

	private static final long serialVersionUID = 8054153991428317673L;

	@PostConstruct
	public void cargar() {

		docuMoviIngr = new DocuMoviIngr();
		docuMoviIngr.setDocumento(new Documento());
		docuMoviIngr.getDocumento().setEstado(true);
	}

	public void buscar() {

		try {
			
			docuMoviIngrLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.docuMoviIngrs = docuMoviIngrLista.buscar(docuMoviIngr, this.pagina);
			this.numeroReg = docuMoviIngrs.size();
			this.contadorReg = docuMoviIngrLista.contarRegistros(docuMoviIngr);
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
	
	public List<Parametro> buscarTipos() {
		
		Parametro parametro = new Parametro();
		parametro.setCodigo("Parametro-CxpCxcTipoDocu");
		parametro.setEstado(true);
		
		List<Parametro> parameTipos = new ArrayList<>();
	
		try {
			parameTipos = parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Parametro-CxpCxcTipo"));
			e.printStackTrace();
	
		}
		
		return parameTipos;
		
	}
		
	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}

		this.docuTrans = this.buscarDocuTrans();
		this.parameTipos = this.buscarTipos();
		
		if (this.id == null) {
			this.docuMoviIngr = new DocuMoviIngr();
			this.docuMoviIngr.setDocumento(new Documento());
			this.docuMoviIngr.getDocumento().setCopias((short) 1);
			this.docuMoviIngr.getDocumento().setEstado(true);
			
		} else {

			try {
				this.docuMoviIngr = docuMoviIngrRegis.buscarPorId(DocuMoviIngr.class, this.getId());
				
				if (this.docuMoviIngr.getDocumento().getDocumento() != null) {
//					Inicializa Proxy documento
					Documento documento = new Documento();
					documento = this.documentoRegis.buscarPorId(Documento.class, this.docuMoviIngr.getDocumento().getDocumento().getDocumentoId());
					this.docuMoviIngr.getDocumento().setDocumento(documento);
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

		// Los mensajes tienen RequestScope y se borran al navegar a otro pagina
		// Con este comando se guarda los mensages de confirmaci�n en navegacion
		// entre paginas
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			if (this.id == null) {
				Object id = docuMoviIngrRegis.insertar(docuMoviIngr);
				this.id = (Integer) id;
			} else {
				docuMoviIngrRegis.modificar(docuMoviIngr);
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
			DocuMoviIngr docuMoviIngr = docuMoviIngrRegis.buscarPorId(DocuMoviIngr.class, this.getId());
			docuMoviIngrRegis.eliminar(docuMoviIngr);

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

	public List<DocuMoviIngr> buscarTodo() {

		List<DocuMoviIngr> docuMoviIngrs = new ArrayList<>();

		try {
			docuMoviIngrs = docuMoviIngrLista.buscarTodo("documentoId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return docuMoviIngrs;
	}
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public DocuMoviIngr getDocuMoviIngr() {
		return docuMoviIngr;
	}

	public void setDocuMoviIngr(DocuMoviIngr docuMoviIngr) {
		this.docuMoviIngr = docuMoviIngr;
	}

	public List<DocuMoviIngr> getDocuMoviIngrs() {
		return docuMoviIngrs;
	}

	public void setDocuMoviIngrs(List<DocuMoviIngr> docuMoviIngrs) {
		this.docuMoviIngrs = docuMoviIngrs;
	}

	public List<DocuTran> getDocuTrans() {
		return docuTrans;
	}

	public void setDocuTrans(List<DocuTran> docuTrans) {
		this.docuTrans = docuTrans;
	}

	public List<Parametro> getParameTipos() {
		return parameTipos;
	}

	public void setParameTipos(List<Parametro> parameTipos) {
		this.parameTipos = parameTipos;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// Estas 3 Lineas llaman al metodo cuando se abra la pagina es similar a
	// @PostConstructor
	// <f:metadata>
	// <f:event type="preRenderView"
	// listener='#{categoriaControlador.buscarTodo}' />
	// </f:metadata>

	// public void buscarTodo() {
	// try {
	// this.categorias =
	// categoriaConsultaInterface.categoriaConsultar(this.getCategoria());
	// } catch (Exception e) {
	// // TODOs Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

}