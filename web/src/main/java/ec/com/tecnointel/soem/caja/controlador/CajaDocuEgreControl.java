package ec.com.tecnointel.soem.caja.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.caja.listaInt.CajaDocuEgreListaInt;
import ec.com.tecnointel.soem.caja.listaInt.CajaListaInt;
import ec.com.tecnointel.soem.caja.listaInt.SaliArchListaInt;
import ec.com.tecnointel.soem.caja.modelo.Caja;
import ec.com.tecnointel.soem.caja.modelo.CajaDocuEgre;
import ec.com.tecnointel.soem.caja.modelo.SaliArch;
import ec.com.tecnointel.soem.caja.registroInt.CajaDocuEgreRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.DocuEgreListaInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuEgre;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.listaInt.RolSucuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class CajaDocuEgreControl extends PaginaControl implements Serializable {

	private Integer cajaId;
	private String paginaRuta;
	
	private CajaDocuEgre cajaDocuEgre;

	private Set<Sucursal> sucursals;
	private List<CajaDocuEgre> cajaDocuEgres;
	private List<Caja> cajas;
	private List<DocuEgre> docuEgres;
	private List<SaliArch> saliArchs;
	
	@Inject
	CajaDocuEgreRegisInt cajaDocuEgreRegis;

	@Inject
	CajaDocuEgreListaInt cajaDocuEgreLista;
	
	@Inject
	CajaListaInt cajaLista;

	@Inject
	DocuEgreListaInt docuEgreLista;
	
	@Inject
	RolSucuListaInt rolSucuLista;

	@Inject
	SaliArchListaInt saliArchLista;

	private static final long serialVersionUID = -4752936781174478449L;

	@PostConstruct
	public void cargar() {

		cajaDocuEgre = new CajaDocuEgre();
		cajaDocuEgre.setCaja(new Caja());
		cajaDocuEgre.setDocuEgre(new DocuEgre());
		
		sucursals = new HashSet<>();
		
		this.buscarRolSucus();

	}

	public void buscar() {

		try {
			cajaDocuEgreLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.cajaDocuEgres = cajaDocuEgreLista.buscar(cajaDocuEgre, this.pagina);
			this.numeroReg = cajaDocuEgres.size();
			this.contadorReg = cajaDocuEgreLista.contarRegistros(cajaDocuEgre);
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
		
		this.getCajaDocuEgre().getCaja().setCajaId(cajaId);
		this.buscarCajas();
		this.buscarDocuEgres();

		if (this.id == null) {
			this.cajaDocuEgre = new CajaDocuEgre();
		} else {

			try {
				this.cajaDocuEgre = cajaDocuEgreRegis.buscarPorId(CajaDocuEgre.class, this.getId());
				this.buscarSaliArchs(this.cajaDocuEgre);
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
				Object id = cajaDocuEgreRegis.insertar(cajaDocuEgre);
				this.id = (Integer) id;
			} else {
				cajaDocuEgreRegis.modificar(cajaDocuEgre);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		if (this.getCajaId() != null){
			return paginaRuta + "?faces-redirect=true&cajaId=" + this.getCajaId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public String nuevo() {
		
		if (this.getCajaId() != null){
			return "/ppsj/caja/cajaDocuEgre/registra?faces-redirect=true&cajaId=" + this.getCajaId() + "&paginaRuta=" + this.getPaginaRuta();
			
		} else {
			return "/ppsj/caja/cajaDocuEgre/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta(); 
		}

	}
	
	public String modificar() {
		if (this.getCajaId() != null){
			return "/ppsj/caja/cajaDocuEgre/registra?faces-redirect=true&cajaDocuEgreId=" + this.getId() + "&cajaId=" + this.getCajaId() + "&paginaRuta=" + this.getPaginaRuta();
		} else {
			return "/ppsj/caja/cajaDocuEgre/registra?faces-redirect=true&cajaDocuEgreId=" + this.getId() + "&paginaRuta=" + this.getPaginaRuta();	
		}
	}
		
	public String explorar() {
		return "explora?faces-redirect=true&cajaDocuEgreId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			CajaDocuEgre cajaDocuEgre = cajaDocuEgreRegis.buscarPorId(CajaDocuEgre.class, this.getId());
			cajaDocuEgreRegis.eliminar(cajaDocuEgre);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		if (this.getCajaId() != null){
			return paginaRuta + "?faces-redirect=true&cajaId=" + this.getCajaId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public String cancelar(){
		
		if (this.getCajaId() != null){
			return paginaRuta + "?faces-redirect=true&cajaId=" + this.getCajaId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public List<CajaDocuEgre> buscarTodo() {

		List<CajaDocuEgre> cajaDocuEgres = new ArrayList<>();

		try {
			cajaDocuEgres = cajaDocuEgreLista.buscarTodo("cajaDocuEgreId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return cajaDocuEgres;
	}
	
	public void buscarCajas() {

		this.getCajaDocuEgre().getCaja().setEstado(true);
		
		try {
			this.cajas = cajaLista.buscar(this.getCajaDocuEgre().getCaja(), null, this.sucursals);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Bodega - Error al buscar cajas"));
			e.printStackTrace();
		}
	}
	
	public void buscarDocuEgres() {

		DocuEgre docuEgre = new DocuEgre();
		docuEgre.setDocumento(new Documento());
		this.getCajaDocuEgre().setDocuEgre(docuEgre);
		
		this.getCajaDocuEgre().getDocuEgre().getDocumento().setEstado(true);
		
		try {
			this.docuEgres = docuEgreLista.buscar(this.getCajaDocuEgre().getDocuEgre(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Bodega - Error al buscar documento egreso"));
			e.printStackTrace();
		}
	}
	
	
	public void buscarRolSucus() {
		
		List<RolSucu> rolSucus = new ArrayList<>();
		
		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");
		
		try {
			rolSucus = rolSucuLista.buscar(persUsuaSesion.getRolPersUsuas());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar sucursales del rol"));
			e.printStackTrace();
		}
			
		for (RolSucu rolSucu : rolSucus) {
			this.sucursals.add(rolSucu.getSucursal());
		}
	}
	
	public void buscarSaliArchs(CajaDocuEgre cajaDocuEgre){

		SaliArch saliArch = new SaliArch();
		saliArch.setCajaDocuEgre(cajaDocuEgre);
		
		try {
			this.saliArchs = saliArchLista.buscar(saliArch, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Caja - Documento Egreso"));
			e.printStackTrace();
		}
	}
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public CajaDocuEgre getCajaDocuEgre() {
		return cajaDocuEgre;
	}

	public void setCajaDocuEgre(CajaDocuEgre cajaDocuEgre) {
		this.cajaDocuEgre = cajaDocuEgre;
	}

	public List<CajaDocuEgre> getCajaDocuEgres() {
		return cajaDocuEgres;
	}

	public void setCajaDocuEgres(List<CajaDocuEgre> cajaDocuEgres) {
		this.cajaDocuEgres = cajaDocuEgres;
	}

	public Integer getCajaId() {
		return cajaId;
	}

	public void setCajaId(Integer cajaId) {
		this.cajaId = cajaId;
	}

	public List<Caja> getCajas() {
		return cajas;
	}

	public void setCajas(List<Caja> cajas) {
		this.cajas = cajas;
	}

	public List<DocuEgre> getDocuEgres() {
		return docuEgres;
	}

	public void setDocuEgres(List<DocuEgre> docuEgres) {
		this.docuEgres = docuEgres;
	}

	public String getPaginaRuta() {
		return paginaRuta;
	}

	public void setPaginaRuta(String paginaRuta) {
		this.paginaRuta = paginaRuta;
	}

	public Set<Sucursal> getSucursals() {
		return sucursals;
	}

	public void setSucursals(Set<Sucursal> sucursals) {
		this.sucursals = sucursals;
	}

	public List<SaliArch> getSaliArchs() {
		return saliArchs;
	}

	public void setSaliArchs(List<SaliArch> saliArchs) {
		this.saliArchs = saliArchs;
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
