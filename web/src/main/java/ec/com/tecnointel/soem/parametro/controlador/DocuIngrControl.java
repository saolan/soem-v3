package ec.com.tecnointel.soem.parametro.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.DimmListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.DocuIngrListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.DocuTranListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.DocuIngr;
import ec.com.tecnointel.soem.parametro.modelo.DocuTran;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.registroInt.DocuIngrRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.DocumentoRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class DocuIngrControl extends PaginaControl implements Serializable {

	private DocuIngr docuIngr;

	private List<Parametro> tipoRetes;
	
	List<DocuIngr> docuIngrs;
	List<DocuTran> docuTrans;
	List<Dimm> dimms;
	List<Dimm> dimmAmbiens;
	List<Dimm> dimmTipoEmiss;
	List<Dimm> dimmTipoComps;

	private List<Parametro> tipoDocuElecs;
	
	@Inject
	DocuIngrRegisInt docuIngrRegis;

	@Inject
	DocuIngrListaInt docuIngrLista;

	@Inject
	DimmListaInt dimmLista;

	@Inject
	DocuTranListaInt docuTranLista;
	
	@Inject
	DocumentoRegisInt documentoRegis;

	private static final long serialVersionUID = 5570038637152345174L;

	@PostConstruct
	public void cargar() {
		docuIngr = new DocuIngr();
		docuIngr.setDocumento(new Documento());
		docuIngr.getDocumento().setEstado(true);
	}

	public void buscar() {

		try {
			
			docuIngrLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.docuIngrs = docuIngrLista.buscar(this.getDocuIngr(), this.pagina);
			this.numeroReg = docuIngrs.size();
			this.contadorReg = docuIngrLista.contarRegistros(docuIngr);
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
	
	public List<Parametro> buscarTipoDocuElecs(Parametro parametro) {
			
		List<Parametro> parametros = new ArrayList<>();
		
		try {
			parametros = parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
							"Excepcion - Error al buscar tipo de documento electrónico"));
			e.printStackTrace();
		}
		return parametros;
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}

		this.docuTrans = this.buscarDocuTrans();
		this.buscarDimm();
		this.buscarDimmAmbiens();
		this.buscarDimmTipoEmiss();
		this.buscarTipoRetes();
		
		Parametro parametro = new Parametro();

		parametro.setCodigo("Egreso-FactElecTipoDocu");
		parametro.setEstado(true);

		this.tipoDocuElecs = this.buscarTipoDocuElecs(parametro);

		if (this.id == null) {
			this.docuIngr = new DocuIngr();
			this.docuIngr.setDocumento(new Documento());
			this.docuIngr.getDocumento().setCopias((short) 1);
			this.docuIngr.setDocumeElec("Ninguno");
			this.docuIngr.getDocumento().setEstado(true);
		} else {

			try {
				this.docuIngr = docuIngrRegis.buscarPorId(DocuIngr.class, this.getId());
				
				if (this.docuIngr.getDocumento().getDocumento() != null) {
//					Inicializa Proxy documento
					Documento documento = new Documento();
					documento = this.documentoRegis.buscarPorId(Documento.class, this.docuIngr.getDocumento().getDocumento().getDocumentoId());
					this.docuIngr.getDocumento().setDocumento(documento);
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
				Object id = docuIngrRegis.insertar(docuIngr);
				this.id = (Integer) id;
			} else {
				
				docuIngrRegis.modificar(docuIngr);
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
			DocuIngr docuIngr = docuIngrRegis.buscarPorId(DocuIngr.class, this.getId());
			docuIngrRegis.eliminar(docuIngr);

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

	public List<DocuIngr> buscarTodo() {

		List<DocuIngr> docuIngrs = new ArrayList<DocuIngr>();

		try {
			docuIngrs = docuIngrLista.buscarTodo("documentoId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return docuIngrs;
	}

	public void buscarDimm() {
		Dimm dimmDesde = new Dimm();
		Dimm dimmHasta = new Dimm();
		
		dimmDesde.setTablaRefe("Tabla4");
		dimmDesde.setEstado(true);
		try {
			dimms = dimmLista.buscar(dimmDesde, dimmHasta, null);
			dimmTipoComps = dimms;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al buscar dimm"));
			e.printStackTrace();
		}
	}

	public void buscarDimmAmbiens() {
		Dimm dimmDesde = new Dimm();
		Dimm dimmHasta = new Dimm();
		
		dimmDesde.setTablaRefe("DocuElecT2");
		dimmDesde.setEstado(true);
		try {
			dimmAmbiens = dimmLista.buscar(dimmDesde, dimmHasta, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al buscar tipo de ambiente"));
			e.printStackTrace();
		}
	}

	public void buscarDimmTipoEmiss() {
		Dimm dimmDesde = new Dimm();
		Dimm dimmHasta = new Dimm();
		
		dimmDesde.setTablaRefe("DocuElecT1");
		dimmDesde.setEstado(true);
		try {
			dimmTipoEmiss = dimmLista.buscar(dimmDesde, dimmHasta, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al buscar tipo de emisión"));
			e.printStackTrace();
		}
	}
	
	public void buscarTipoRetes(){
		
		Parametro parametro = new Parametro();

		parametro.setCodigo("Ingreso-TipoRete");
		parametro.setEstado(true);
		
		try {
			tipoRetes = parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
							"Excepcion - Error al buscar tipo de retención"));
			e.printStackTrace();
		}

	}
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public DocuIngr getDocuIngr() {
		return docuIngr;
	}

	public void setDocuIngr(DocuIngr docuIngr) {
		this.docuIngr = docuIngr;
	}

	public List<DocuIngr> getDocuIngrs() {
		return docuIngrs;
	}

	public void setDocuIngrs(List<DocuIngr> docuIngrs) {
		this.docuIngrs = docuIngrs;
	}

	public List<Dimm> getDimms() {
		return dimms;
	}

	public void setDimms(List<Dimm> dimms) {
		this.dimms = dimms;
	}

	public List<Parametro> getTipoRetes() {
		return tipoRetes;
	}

	public void setTipoRetes(List<Parametro> tipoRetes) {
		this.tipoRetes = tipoRetes;
	}

	public List<Dimm> getDimmAmbiens() {
		return dimmAmbiens;
	}

	public void setDimmAmbiens(List<Dimm> dimmAmbiens) {
		this.dimmAmbiens = dimmAmbiens;
	}

	public List<Dimm> getDimmTipoEmiss() {
		return dimmTipoEmiss;
	}

	public void setDimmTipoEmiss(List<Dimm> dimmTipoEmiss) {
		this.dimmTipoEmiss = dimmTipoEmiss;
	}

	public List<DocuTran> getDocuTrans() {
		return docuTrans;
	}

	public void setDocuTrans(List<DocuTran> docuTrans) {
		this.docuTrans = docuTrans;
	}

	public List<Dimm> getDimmTipoComps() {
		return dimmTipoComps;
	}

	public void setDimmTipoComps(List<Dimm> dimmTipoComps) {
		this.dimmTipoComps = dimmTipoComps;
	}

	public List<Parametro> getTipoDocuElecs() {
		return tipoDocuElecs;
	}

	public void setTipoDocuElecs(List<Parametro> tipoDocuElecs) {
		this.tipoDocuElecs = tipoDocuElecs;
	}

	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}