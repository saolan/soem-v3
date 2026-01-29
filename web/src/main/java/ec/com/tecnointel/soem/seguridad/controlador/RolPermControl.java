package ec.com.tecnointel.soem.seguridad.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.seguridad.listaInt.PermisoListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolPermListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.Permiso;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolPerm;
import ec.com.tecnointel.soem.seguridad.registroInt.RolPermRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class RolPermControl extends PaginaControl implements Serializable {

	private Integer rolId;
	private String paginaRuta;
	
	private RolPerm rolPerm;

	private List<RolPerm> rolPerms;
	private List<Rol> rols;
	private List<Permiso> permisos;

	@Inject
	RolPermRegisInt rolPermRegis;

	@Inject
	RolPermListaInt rolPermLista;
	
	@Inject
	RolListaInt rolLista;

	@Inject
	PermisoListaInt permisoLista;
	
	private static final long serialVersionUID = -4119729040171670307L;

	@PostConstruct
	public void cargar() {
		rolPerm = new RolPerm();
		rolPerm.setRol(new Rol());
		rolPerm.setPermiso(new Permiso());
	}

	public void buscar() {

		try {
			
			rolPermLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.rolPerms = rolPermLista.buscar(rolPerm, this.pagina);
			this.numeroReg = rolPerms.size();
			this.contadorReg = rolPermLista.contarRegistros(rolPerm);
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
		
		this.getRolPerm().getRol().setRolId(rolId);
		this.buscarRols();
		this.buscarPermisos();


		if (this.id == null) {
			this.rolPerm = new RolPerm();
			this.rolPerm.setAcceso(true);
		} else {

			try {
				this.rolPerm = rolPermRegis.buscarPorId(RolPerm.class, this.getId());
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
				Object id = rolPermRegis.insertar(rolPerm);
				this.id = (Integer) id;
			} else {
				rolPermRegis.modificar(rolPerm);
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
			return "/ppsj/seguridad/rolPerm/registra?faces-redirect=true&rolId=" + this.getRolId() + "&paginaRuta=" + this.getPaginaRuta();			
		} else {
			return "/ppsj/seguridad/rolPerm/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta(); 
		}

	}
	
	public String modificar() {
		if (this.getRolId() != null){
			return "/ppsj/seguridad/rolPerm/registra?faces-redirect=true&rolPermId=" + this.getId() + "&rolId=" + this.getRolId() + "&paginaRuta="  + this.getPaginaRuta();
		} else {
			return "/ppsj/seguridad/rolPerm/registra?faces-redirect=true&rolPermId=" + this.getId() + "&paginaRuta=" + this.getPaginaRuta();
		}
	}

	public String explorar() {
		return "explora?faces-redirect=true&rolPermId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			RolPerm rolPerm = rolPermRegis.buscarPorId(RolPerm.class, this.getId());
			rolPermRegis.eliminar(rolPerm);

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

	public List<RolPerm> buscarTodo() {

		List<RolPerm> rolPerms = new ArrayList<>();

		try {
			rolPerms = rolPermLista.buscarTodo("rolPermId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return rolPerms;
	}
	
	public void buscarRols() {

		this.getRolPerm().getRol().setEstado(true);
		
		try {
			this.rols = rolLista.buscar(this.getRolPerm().getRol(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion Buscar Rol - Error al buscar roles"));
			e.printStackTrace();
		}
	}
	
	public void buscarPermisos() {
		
		try {
			this.permisos = permisoLista.buscar(this.getRolPerm().getPermiso(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Bodega - Error al buscar permisos"));
			e.printStackTrace();
		}
	}
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public RolPerm getRolPerm() {
		return rolPerm;
	}

	public void setRolPerm(RolPerm rolPerm) {
		this.rolPerm = rolPerm;
	}

	public List<RolPerm> getRolPerms() {
		return rolPerms;
	}

	public void setRolPerms(List<RolPerm> rolPerms) {
		this.rolPerms = rolPerms;
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

	public List<Permiso> getPermisos() {
		return permisos;
	}

	public void setPermisos(List<Permiso> permisos) {
		this.permisos = permisos;
	}

	public String getPaginaRuta() {
		return paginaRuta;
	}

	public void setPaginaRuta(String paginaRuta) {
		this.paginaRuta = paginaRuta;
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