package ec.com.tecnointel.soem.seguridad.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.PrecioListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Precio;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.listaInt.RolListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolPrecListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolSucuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import ec.com.tecnointel.soem.seguridad.registroInt.RolPrecRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class RolPrecControl extends PaginaControl implements Serializable {

	private Integer rolId;
	private String paginaRuta;
	
	private RolPrec rolPrec;

	private List<RolPrec> rolPrecs;
	private List<Rol> rols;
	private List<Precio> precios;
	private Set<Sucursal> sucursals;

	@Inject
	RolPrecRegisInt rolPrecRegis;

	@Inject
	RolPrecListaInt rolPrecLista;
	
	@Inject
	RolListaInt rolLista;
	
	@Inject
	PrecioListaInt precioLista;
	
	@Inject
	RolSucuListaInt rolSucuLista;
	
	private static final long serialVersionUID = 3571724872199810129L;

	@PostConstruct
	public void cargar() {
		
		rolPrec = new RolPrec();
		rolPrec.setSucursal(new Sucursal());
		rolPrec.setRol(new Rol());
		rolPrec.setPrecio(new Precio());
		
		sucursals = new HashSet<>();
	}

	public void buscar() {
		
		try {
			
			rolPrecLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.rolPrecs = rolPrecLista.buscar(rolPrec, this.pagina);
			this.numeroReg = rolPrecs.size();
			this.contadorReg = rolPrecLista.contarRegistros(rolPrec);
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

		this.getRolPrec().getRol().setRolId(rolId);
		this.buscarRols();
		this.buscarPrecios();
		this.buscarRolSucus(); // Busqueda en RolSucu y filtra sucursales de cada rol

		if (this.id == null) {
			this.rolPrec = new RolPrec();
			this.rolPrec.setAcceso(true);
		} else {

			try {
				this.rolPrec = rolPrecRegis.buscarPorId(RolPrec.class, this.getId());
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
				
				Object id = rolPrecRegis.insertar(rolPrec);
				this.id = (Integer) id;
			} else {
				rolPrecRegis.modificar(rolPrec);
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
			return "/ppsj/seguridad/rolPrec/registra?faces-redirect=true&rolId=" + this.getRolId() + "&paginaRuta=" + this.getPaginaRuta();			
		} else {
			return "/ppsj/seguridad/rolPrec/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta(); 
		}

	}
	
	public String modificar() {
		if (this.getRolId() != null){
			return "/ppsj/seguridad/rolPrec/registra?faces-redirect=true&rolPrecId=" + this.getId() + "&rolId=" + this.getRolId() + "&paginaRuta=" + this.getPaginaRuta();
		} else {
			return "/ppsj/seguridad/rolPrec/registra?faces-redirect=true&rolPrecId=" + this.getId() + "&paginaRuta=" + this.getPaginaRuta();
		}
	}
	
	public String explorar() {
		return "explora?faces-redirect=true&rolPrecId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			RolPrec rolPrec = rolPrecRegis.buscarPorId(RolPrec.class, this.getId());
			rolPrecRegis.eliminar(rolPrec);

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

	public List<RolPrec> buscarTodo() {

		List<RolPrec> rolPrecs = new ArrayList<>();

		try {
			rolPrecs = rolPrecLista.buscarTodo("rolPrecId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return rolPrecs;
	}
	
	public void buscarRols() {

		this.getRolPrec().getRol().setEstado(true);
		
		try {
			this.rols = rolLista.buscar(this.getRolPrec().getRol(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion Buscar Rol - Error al buscar roles"));
			e.printStackTrace();
		}
	}
	
	public void buscarPrecios() {

		this.getRolPrec().getPrecio().setEstado(true);
		
		try {
			this.precios = precioLista.buscar(this.getRolPrec().getPrecio(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Bodega - Error al buscar precios"));
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



	public List<RolPrec> getRolPrecs() {
		return rolPrecs;
	}

	public void setRolPrecs(List<RolPrec> rolPrecs) {
		this.rolPrecs = rolPrecs;
	}

	public Integer getRolId() {
		return rolId;
	}

	public void setRolId(Integer rolId) {
		this.rolId = rolId;
	}

	public List<Rol> getRols() {
		return rols;
	}

	public void setRols(List<Rol> rols) {
		this.rols = rols;
	}

	public List<Precio> getPrecios() {
		return precios;
	}

	public void setPrecios(List<Precio> precios) {
		this.precios = precios;
	}

	public String getPaginaRuta() {
		return paginaRuta;
	}

	public void setPaginaRuta(String paginaRuta) {
		this.paginaRuta = paginaRuta;
	}

	public RolPrec getRolPrec() {
		return rolPrec;
	}

	public void setRolPrec(RolPrec rolPrec) {
		this.rolPrec = rolPrec;
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