package ec.com.tecnointel.soem.parametro.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.DimmListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.DocuEgreListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.DocuTranListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.DocuEgre;
import ec.com.tecnointel.soem.parametro.modelo.DocuTran;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.registroInt.DocuEgreRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.DocumentoRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class DocuEgreControl extends PaginaControl implements Serializable {

	private DocuEgre docuEgre;

	List<DocuEgre> docuEgres;
	List<DocuTran> docuTrans;
	List<Dimm> dimms;
	List<Dimm> dimmAmbiens;
	List<Dimm> dimmTipoEmiss;
	List<Dimm> dimmTipoComps;
	
	private List<Parametro> tipoDocuElecs;

	@Inject
	DocuEgreRegisInt docuEgreRegis;

	@Inject
	DocuEgreListaInt docuEgreLista;

	@Inject
	DimmListaInt dimmLista;

	@Inject
	DocuTranListaInt docuTranLista;
	
	@Inject
	DocumentoRegisInt documentoRegis;

	private static final long serialVersionUID = -8423504260258017223L;
	
	@PostConstruct
	public void cargar() {
		docuEgre = new DocuEgre();
		docuEgre.setDocumento(new Documento());
		docuEgre.getDocumento().setEstado(true);
	}

	public void buscar() {

		try {
			
			docuEgreLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.docuEgres = docuEgreLista.buscar(this.getDocuEgre(), this.pagina);
			this.numeroReg = docuEgres.size();
			this.contadorReg = docuEgreLista.contarRegistros(docuEgre);
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
		this.buscarDimm();
//		this.buscarDimmTipoComps();
		this.buscarDimmAmbiens();
		this.buscarDimmTipoEmiss();
		this.buscarDocuElecs();

		if (this.id == null) {
			this.docuEgre = new DocuEgre();
			this.docuEgre.setDocumento(new Documento());
			this.docuEgre.getDocumento().setCopias((short) 1);
			this.docuEgre.getDocumento().setFormat("F");
//			this.docuEgre.setForma2("F2");
			this.docuEgre.setImprimDire(false);
			this.docuEgre.setImpresDesc("I");
			this.docuEgre.getDocumento().setEstado(true);
		} else {

			try {
				this.docuEgre = docuEgreRegis.buscarPorId(DocuEgre.class, this.getId());

				if (this.docuEgre.getDocumento().getDocumento() != null) {
//					Inicializa Proxy documento
					Documento documento = new Documento();
					documento = this.documentoRegis.buscarPorId(Documento.class, this.docuEgre.getDocumento().getDocumento().getDocumentoId());
					this.docuEgre.getDocumento().setDocumento(documento);
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
				Object id = docuEgreRegis.insertar(docuEgre);
				this.id = (Integer) id;
			} else {
				
				docuEgreRegis.modificar(docuEgre);
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
			DocuEgre docuEgre = docuEgreRegis.buscarPorId(DocuEgre.class, this.getId());
			docuEgreRegis.eliminar(docuEgre);

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

	public List<DocuEgre> buscarTodo() {

		List<DocuEgre> docuEgres = new ArrayList<DocuEgre>();

		try {
			docuEgres = docuEgreLista.buscarTodo("documentoId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return docuEgres;
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
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al buscar tipo de comprobante dimms"));
			e.printStackTrace();
		}
	}
	
//	public void buscarDimmTipoComps() {
//		Dimm dimmDesde = new Dimm();
//		Dimm dimmHasta = new Dimm();
//		
//		dimmDesde.setTablaRefe("Tabla4");
//		dimmDesde.setEstado(true);
//		try {
//			dimmTipoComps = dimmLista.buscar(dimmDesde, dimmHasta, null); 
//		} catch (Exception e) {
//			FacesContext.getCurrentInstance().addMessage(null,
//					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepción - Error al buscar tipo de comprobante para anexo transaccional"));
//			e.printStackTrace();
//		}
//	}
	
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

	public void buscarDocuElecs(){
		
		Parametro parametro = new Parametro();

		parametro.setCodigo("Egreso-FactElecTipoDocu");
		parametro.setEstado(true);
		
		try {
			tipoDocuElecs = parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
							"Excepcion - Error al buscar tipo de documento electrónico"));
			e.printStackTrace();
		}
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public DocuEgre getDocuEgre() {
		return docuEgre;
	}

	public void setDocuEgre(DocuEgre docuEgre) {
		this.docuEgre = docuEgre;
	}

	public List<DocuEgre> getDocuEgres() {
		return docuEgres;
	}

	public void setDocuEgres(List<DocuEgre> docuEgres) {
		this.docuEgres = docuEgres;
	}

	public List<Dimm> getDimms() {
		return dimms;
	}

	public void setDimms(List<Dimm> dimms) {
		this.dimms = dimms;
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

	public List<Parametro> getTipoDocuElecs() {
		return tipoDocuElecs;
	}

	public void setTipoDocuElecs(List<Parametro> tipoDocuElecs) {
		this.tipoDocuElecs = tipoDocuElecs;
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
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}