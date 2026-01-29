package ec.com.tecnointel.soem.seguridad.controlador;

import java.io.Serializable;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.registroInt.PersonaRegisInt;
import ec.com.tecnointel.soem.seguridad.listaInt.PersUsuaListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolPermListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolPersUsuaListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.seguridad.registroInt.PersUsuaRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class PersUsuaControl extends PaginaControl implements Serializable {

	private Integer personaId;
	private String paginaRuta;
	
	private PersUsua persUsua;
	
	List<PersUsua> persUsuarios;
	List<RolPersUsua> rolPersUsuas;
	
	@Inject
	PersUsuaRegisInt persUsuaRegis;

	@Inject
	PersUsuaListaInt persUsuaLista;

	@Inject
	RolPersUsuaListaInt rolPersUsuaLista;
	
	@Inject
	RolPermListaInt rolPermLista;
	
	@Inject
	PersonaRegisInt personaRegis;
	
	private static final long serialVersionUID = 4613482617128967486L;
	
	@PostConstruct
	public void cargar() {

		persUsua = new PersUsua();
		persUsua.setPersona(new Persona());
		persUsua.setEstado(true);
		
	}

	public void buscar() {

		try {
			
			persUsuaLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.persUsuarios = persUsuaLista.buscar(persUsua, this.pagina);
			this.numeroReg = persUsuarios.size();
			this.contadorReg = persUsuaLista.contarRegistros(persUsua);
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

		if (this.id == null) {
			this.persUsua = new PersUsua();
			this.persUsua.setPersona(new Persona());
			this.persUsua.setEstado(true);
			
			if (this.personaId != null) {
				
				Persona persona = new Persona();
				try {
					persona =  personaRegis.buscarPorId(Persona.class, this.getPersonaId());
					this.persUsua.setPersona(persona);
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al persona"));
					e.printStackTrace();
				}
			}
			
		} else {
			
				try {
					this.persUsua = persUsuaRegis.buscarPorId(PersUsua.class, this.getId());
					this.buscarRolPersUsuas(persUsua);
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
					e.printStackTrace();
				}
				
		}
	}

//	Se llama solo desde personaExplora para traer parametro personaId
//	public String nuevo() {
//		return "/ppsj/seguridad/persUsua/registra?faces-redirect=true&personaId=" + this.getPersonaId();
//	}

	public String grabar() {

		String campoTexto;
		
		campoTexto = this.persUsua.getPersona().getCedulaRuc().trim();
		this.persUsua.getPersona().setCedulaRuc(campoTexto);

		campoTexto = this.persUsua.getPersona().getApelli().trim();
		this.persUsua.getPersona().setApelli(campoTexto);

		if (this.persUsua.getPersona().getNombre() != null) {
			campoTexto = this.persUsua.getPersona().getNombre().trim();
			this.persUsua.getPersona().setNombre(campoTexto);
		}
		
		try {
			if (this.id == null) {
				this.persUsua.getPersona().setEstado(true);
				Object id = persUsuaRegis.insertar(persUsua);
				this.id = (Integer) id;
			} else {
				persUsuaRegis.modificar(persUsua);
			}
		} catch (RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error cédula o Ruc ya existe"));
			e.printStackTrace();
			return null;			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

//		return "explora?faces-redirect=true&persUsuaId=" + this.getId();
		
		if (this.getPersonaId() != null){
			return paginaRuta + "?faces-redirect=true&personaId=" + this.getPersonaId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}		
	}
	
	public String nuevo() {
		
		if (this.getPersonaId() != null){
			
			return "/ppsj/seguridad/persUsua/registra?faces-redirect=true&personaId=" + this.getPersonaId() + "&paginaRuta=" + this.getPaginaRuta();			
		} else {
			return "/ppsj/seguridad/persUsua/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta(); 
		}

	}	
	
	public String modificar() {
		if (this.getPersonaId() != null){
			return "/ppsj/seguridad/persUsua/registra?faces-redirect=true&persUsuaId=" + this.getId() + "&personaId=" + this.getPersonaId() + "&paginaRuta=" + this.getPaginaRuta();	
		} else {
			return "/ppsj/seguridad/persUsua/registra?faces-redirect=true&persUsuaId=" + this.getId() + "&paginaRuta=" + this.getPaginaRuta();	
		}
	}	
	
//	public String modificar() {
//		return "/ppsj/seguridad/persUsua/registra?faces-redirect=true&persUsuaId=" + this.getId();
//	}

	public String explorar() {
		return "explora?faces-redirect=true&persUsuaId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			PersUsua persUsua = persUsuaRegis.buscarPorId(PersUsua.class, this.getId());
			persUsuaRegis.eliminar(persUsua);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

//		return "lista?faces-redirect=true";
		if (this.getPersonaId() != null){
			return paginaRuta + "?faces-redirect=true&personaId=" + this.getPersonaId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}
	}
	
	public String cancelar(){
		
		if (this.getPersonaId() != null){
			return paginaRuta + "?faces-redirect=true&personaId=" + this.getPersonaId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}

	public void buscarRolPersUsuas(PersUsua persUsua){
		
		RolPersUsua rolPersUsua = new RolPersUsua();
		rolPersUsua.setPersUsua(persUsua);
		rolPersUsua.setRol(new Rol());
		
		try {
			this.rolPersUsuas = rolPersUsuaLista.buscar(rolPersUsua, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Roles Usuarios"));

			e.printStackTrace();
		}

	}
		
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public PersUsua getPersUsua() {
		return persUsua;
	}

	public void setPersUsua(PersUsua persUsua) {
		this.persUsua = persUsua;
	}

	public List<PersUsua> getPersUsuarios() {
		return persUsuarios;
	}

	public void setPersUsuarios(List<PersUsua> persUsuarios) {
		this.persUsuarios = persUsuarios;
	}

	public List<RolPersUsua> getRolPersUsuas() {
		return rolPersUsuas;
	}

	public void setRolPersUsuas(List<RolPersUsua> rolPersUsuas) {
		this.rolPersUsuas = rolPersUsuas;
	}

	public Integer getPersonaId() {
		return personaId;
	}

	public void setPersonaId(Integer personaId) {
		this.personaId = personaId;
	}

	public String getPaginaRuta() {
		return paginaRuta;
	}

	public void setPaginaRuta(String paginaRuta) {
		this.paginaRuta = paginaRuta;
	}
}