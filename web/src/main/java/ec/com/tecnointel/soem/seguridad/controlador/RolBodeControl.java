package ec.com.tecnointel.soem.seguridad.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.BodegaListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Bodega;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.listaInt.RolBodeListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolSucuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolBode;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import ec.com.tecnointel.soem.seguridad.registroInt.RolBodeRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class RolBodeControl extends PaginaControl implements Serializable {

	private Integer rolId;
	private String paginaRuta;
	
	private RolBode rolBode;

	private List<RolBode> rolBodes;
	private List<Rol> rols;
	private List<Bodega> bodegas;
	private Set<Sucursal> sucursals;

	@Inject
	RolBodeRegisInt rolBodeRegis;

	@Inject
	RolBodeListaInt rolBodeLista;

	@Inject
	RolListaInt rolLista;

	@Inject
	BodegaListaInt bodegaLista;
		
	@Inject
	RolSucuListaInt rolSucuLista;
	
	private static final long serialVersionUID = -2535180422795238662L;

	@PostConstruct
	public void cargar() {
		rolBode = new RolBode();
		rolBode.setSucursal(new Sucursal());
		rolBode.setRol(new Rol());
		rolBode.setBodega(new Bodega());
		
		sucursals = new HashSet<>();
	}

	public void buscar() {
				
		try {
			
			rolBodeLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.rolBodes = rolBodeLista.buscar(this.rolBode, this.pagina);
			this.numeroReg = rolBodes.size();
			this.contadorReg = rolBodeLista.contarRegistros(rolBode);

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

		this.getRolBode().getRol().setRolId(rolId);
		this.buscarRols();
		this.buscarBodegas();
		this.buscarRolSucus();

		if (this.id == null) {
			this.rolBode = new RolBode();
			this.rolBode.setAcceso(true);
		} else {

			try {
				this.rolBode = rolBodeRegis.buscarPorId(RolBode.class, this.getId());
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

				Object id = rolBodeRegis.insertar(rolBode);
				this.id = (Integer) id;
			} else {
				rolBodeRegis.modificar(rolBode);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		if (this.getRolId() != null){
			return paginaRuta + "?faces-redirect=true&rolId=" + this.getRolId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public String nuevo() {
		
		if (this.getRolId() != null){
			return "/ppsj/seguridad/rolBode/registra?faces-redirect=true&rolId=" + this.getRolId() + "&paginaRuta=" + this.getPaginaRuta();			
		} else {
			return "/ppsj/seguridad/rolBode/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta(); 
		}

	}

	public String modificar() {
		if (this.getRolId() != null){
			return "/ppsj/seguridad/rolBode/registra?faces-redirect=true&rolBodeId=" + this.getId() + "&rolId=" + this.getRolId() + "&paginaRuta=" + this.getPaginaRuta();
		} else {
			return "/ppsj/seguridad/rolBode/registra?faces-redirect=true&rolBodeId=" + this.getId() + "&paginaRuta=" + this.getPaginaRuta();
		}
	}

	public String explorar() {
		return "explora?faces-redirect=true&rolBodeId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			RolBode rolBode = rolBodeRegis.buscarPorId(RolBode.class, this.getId());
			rolBodeRegis.eliminar(rolBode);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		if (this.getRolId() != null){
			return paginaRuta + "?faces-redirect=true&rolId=" + this.getRolId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public String cancelar(){
		
		if (this.getRolId() != null){
			return paginaRuta + "?faces-redirect=true&rolId=" + this.getRolId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public List<RolBode> buscarTodo() {

		List<RolBode> rolBodes = new ArrayList<>();

		try {
			rolBodes = rolBodeLista.buscarTodo("rolBodeId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return rolBodes;
	}

	public void buscarRols() {

		this.getRolBode().getRol().setEstado(true);
		
		try {
			this.rols = rolLista.buscar(this.getRolBode().getRol(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion Buscar Rol - Error al buscar roles"));
			e.printStackTrace();
		}
	}

	public void buscarBodegas() {

		this.getRolBode().getBodega().setEstado(true);
		
		try {
			this.bodegas = bodegaLista.buscar(this.getRolBode().getBodega(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Bodega - Error al buscar bodegas"));
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
			if (rolSucu.getAcceso() == true){
				this.sucursals.add(rolSucu.getSucursal());
			}
		}

	}	

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public RolBode getRolBode() {
		return rolBode;
	}

	public void setRolBode(RolBode rolBode) {
		this.rolBode = rolBode;
	}

	public List<RolBode> getRolBodes() {
		return rolBodes;
	}

	public void setRolBodes(List<RolBode> rolBodes) {
		this.rolBodes = rolBodes;
	}

	public List<Rol> getRols() {
		return rols;
	}

	public void setRols(List<Rol> rols) {
		this.rols = rols;
	}

	public List<Bodega> getBodegas() {
		return bodegas;
	}

	public void setBodegas(List<Bodega> bodegas) {
		this.bodegas = bodegas;
	}

	public Integer getRolId() {
		return rolId;
	}

	public void setRolId(Integer rolId) {
		this.rolId = rolId;
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

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}