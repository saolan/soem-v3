package ec.com.tecnointel.soem.seguridad.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.seguridad.listaInt.PersUsuaListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolPersUsuaListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.seguridad.registroInt.RolPersUsuaRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class RolPersUsuaControl extends PaginaControl implements Serializable {

	private Integer personaId;
	private RolPersUsua rolPersUsua;
	private String paginaRuta;

	private List<RolPersUsua> rolPersUsuas;
	private List<Rol> rols;
	private List<PersUsua> persUsuas;

	@Inject
	RolPersUsuaRegisInt rolPersUsuaRegis;

	@Inject
	RolPersUsuaListaInt rolPersUsuaLista;

	@Inject
	RolListaInt rolLista;

	@Inject
	PersUsuaListaInt persUsuaLista;

	private static final long serialVersionUID = 7969116096169899756L;

	@PostConstruct
	public void cargar() {
		
		PersUsua persUsua = new PersUsua();
		persUsua.setPersona(new Persona());
		
		rolPersUsua = new RolPersUsua();
		rolPersUsua.setRol(new Rol());
		rolPersUsua.setPersUsua(persUsua);
		
		this.rolPermiso = variablesSesion.getRolPermiso();
	}

	public void buscar() {

		try {
			
			rolPersUsuaLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.rolPersUsuas = rolPersUsuaLista.buscar(rolPersUsua, this.pagina);
			this.numeroReg = rolPersUsuas.size();
			this.contadorReg = rolPersUsuaLista.contarRegistros(rolPersUsua);
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

		this.getRolPersUsua().getPersUsua().setPersonaId(personaId);
		this.buscarPersUsuas();
		this.buscarRols();
		
		if (this.id == null) {
			this.rolPersUsua = new RolPersUsua();
			
		} else {

			try {
				this.rolPersUsua = rolPersUsuaRegis.buscarPorId(RolPersUsua.class, this.getId());
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
				Object id = rolPersUsuaRegis.insertar(rolPersUsua);
				this.id = (Integer) id;
			} else {
				rolPersUsuaRegis.modificar(rolPersUsua);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		if (this.getPersonaId() != null){
			return paginaRuta + "?faces-redirect=true&persUsuaId=" + this.getPersonaId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public String nuevo() {
		
		if (this.getPersonaId() != null){
			return "/ppsj/seguridad/rolPersUsua/registra?faces-redirect=true&personaId=" + this.getPersonaId() + "&paginaRuta=" + this.getPaginaRuta();			
		} else {
			return "/ppsj/seguridad/rolPersUsua/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta(); 
		}

	}
	
	public String modificar() {
		if (this.getPersonaId() != null){ 
			return "/ppsj/seguridad/rolPersUsua/registra?faces-redirect=true&rolPersUsuaId=" + this.getId() + "&personaId=" + this.getPersonaId() + "&paginaRuta=" + this.getPaginaRuta();
		} else {
			return "/ppsj/seguridad/rolPersUsua/registra?faces-redirect=true&rolPersUsuaId=" + this.getId() + "&paginaRuta=" + this.getPaginaRuta();
		}
	}

	public String explorar() {
		return "explora?faces-redirect=true&rolPersUsuaId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			RolPersUsua rolPersUsua = rolPersUsuaRegis.buscarPorId(RolPersUsua.class, this.getId());
			rolPersUsuaRegis.eliminar(rolPersUsua);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		if (this.getPersonaId() != null){
			return paginaRuta + "?faces-redirect=true&persUsuaId=" + this.getPersonaId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}
	}
	
	public String cancelar(){
		
		if (this.getPersonaId() != null){
			return paginaRuta + "?faces-redirect=true&persUsuaId=" + this.getPersonaId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}
	}
	
	public List<RolPersUsua> buscarTodo() {

		List<RolPersUsua> rolPersUsuas = new ArrayList<>();

		try {
			rolPersUsuas = rolPersUsuaLista.buscarTodo("rolPersUsuaId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return rolPersUsuas;
	}
	
	public void buscarRols() {

		this.getRolPersUsua().getRol().setEstado(true);
		
		try {
			this.rols = rolLista.buscar(this.getRolPersUsua().getRol(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion Buscar Rol - Error al buscar roles"));
			e.printStackTrace();
		}
	}
	
	public void buscarPersUsuas() {

		this.getRolPersUsua().getPersUsua().setEstado(true);
		
		try {
			this.persUsuas = persUsuaLista.buscar(this.getRolPersUsua().getPersUsua(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion Buscar Usuario - Error al buscar usuarios"));
			e.printStackTrace();
		}
	}
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public RolPersUsua getRolPersUsua() {
		return rolPersUsua;
	}

	public void setRolPersUsua(RolPersUsua rolPersUsua) {
		this.rolPersUsua = rolPersUsua;
	}

	public List<RolPersUsua> getRolPersUsuas() {
		return rolPersUsuas;
	}

	public void setRolPersUsuas(List<RolPersUsua> rolPersUsuas) {
		this.rolPersUsuas = rolPersUsuas;
	}

	public List<Rol> getRoles() {
		return rols;
	}

	public void setRoles(List<Rol> roles) {
		rols = roles;
	}

	public List<PersUsua> getPersUsuas() {
		return persUsuas;
	}

	public void setPersUsuas(List<PersUsua> persUsuas) {
		this.persUsuas = persUsuas;
	}

	public Integer getPersonaId() {
		return personaId;
	}

	public void setPersonaId(Integer personaId) {
		this.personaId = personaId;
	}

	public List<Rol> getRols() {
		return rols;
	}

	public void setRols(List<Rol> rols) {
		this.rols = rols;
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