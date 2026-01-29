package ec.com.tecnointel.soem.parametro.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.DocuMoviEgreListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.DocuTranListaInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuMoviEgre;
import ec.com.tecnointel.soem.parametro.modelo.DocuTran;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.registroInt.DocuMoviEgreRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.DocumentoRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class DocuMoviEgreControl extends PaginaControl implements Serializable {

	private DocuMoviEgre docuMoviEgre;

	private List<DocuMoviEgre> docuMoviEgres;
	private List<DocuTran> docuTrans;
	
	private List<Parametro> parameTipos;

	@Inject
	DocuMoviEgreRegisInt docuMoviEgreRegis;

	@Inject
	DocuMoviEgreListaInt docuMoviEgreLista;

	@Inject
	DocuTranListaInt docuTranLista;
	
	@Inject
	DocumentoRegisInt documentoRegis;

	private static final long serialVersionUID = -2689207049032376403L;

	@PostConstruct
	public void cargar() {

		docuMoviEgre = new DocuMoviEgre();
		docuMoviEgre.setDocumento(new Documento());
		docuMoviEgre.getDocumento().setEstado(true);

	}

	public void buscar() {

		try {
			
			docuMoviEgreLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.docuMoviEgres = docuMoviEgreLista.buscar(docuMoviEgre,
					this.pagina);
			this.numeroReg = docuMoviEgres.size();
			this.contadorReg = docuMoviEgreLista.contarRegistros(docuMoviEgre);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
							"Excepcion - Error al buscar datos"));
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
			this.docuMoviEgre = new DocuMoviEgre();
			this.docuMoviEgre.setDocumento(new Documento());
			this.docuMoviEgre.getDocumento().setCopias((short) 1);
			this.docuMoviEgre.getDocumento().setEstado(true);
		} else {

			try {
				this.docuMoviEgre = docuMoviEgreRegis.buscarPorId(DocuMoviEgre.class, this.getId());
				
				if (this.docuMoviEgre.getDocumento().getDocumento() != null) {
//					Inicializa Proxy documento
					Documento documento = new Documento();
					documento = this.documentoRegis.buscarPorId(Documento.class, this.docuMoviEgre.getDocumento().getDocumento().getDocumentoId());
					this.docuMoviEgre.getDocumento().setDocumento(documento);
//					Fin Inicializa Proxy documento				
				}

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
								"Excepcion - Error al buscar Id"));
				e.printStackTrace();
			}
		}
	}

	public String grabar() {

		// Los mensajes tienen RequestScope y se borran al navegar a otro pagina
		// Con este comando se guarda los mensages de confirmaci?n en navegacion
		// entre paginas
		FacesContext.getCurrentInstance().getExternalContext().getFlash()
				.setKeepMessages(true);

		try {
			if (this.id == null) {
				Object id = docuMoviEgreRegis.insertar(docuMoviEgre);
				this.id = (Integer) id;
			} else {
				docuMoviEgreRegis.modificar(docuMoviEgre);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
							"Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null,
						"Registro grabado"));

		return "lista?faces-redirect=true";
	}

	public String modificar() {
		return "registra?faces-redirect=true&documentoId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&documentoId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash()
				.setKeepMessages(true);

		try {
			DocuMoviEgre docuMoviEgre = docuMoviEgreRegis.buscarPorId(DocuMoviEgre.class, this
					.getId());
			docuMoviEgreRegis.eliminar(docuMoviEgre);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
							"Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null,
						"Registro eliminado"));

		return "lista?faces-redirect=true";
	}

	public List<DocuMoviEgre> buscarTodo() {

		List<DocuMoviEgre> docuMoviEgres = new ArrayList<>();

		try {
			docuMoviEgres = docuMoviEgreLista.buscarTodo("documentoId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
							"Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return docuMoviEgres;
	}
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public DocuMoviEgre getDocuMoviEgre() {
		return docuMoviEgre;
	}

	public void setDocuMoviEgre(DocuMoviEgre docuMoviEgre) {
		this.docuMoviEgre = docuMoviEgre;
	}

	public List<DocuMoviEgre> getDocuMoviEgres() {
		return docuMoviEgres;
	}

	public void setDocuMoviEgres(List<DocuMoviEgre> docuMoviEgres) {
		this.docuMoviEgres = docuMoviEgres;
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