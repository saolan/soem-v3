package ec.com.tecnointel.soem.caja.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.caja.listaInt.PersCajeListaInt;
import ec.com.tecnointel.soem.caja.modelo.PersCaje;
import ec.com.tecnointel.soem.caja.registroInt.PersCajeRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.registroInt.PersonaRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class PersCajeControl extends PaginaControl implements Serializable {

	private Integer personaId;
	private String paginaRuta;
	
	private PersCaje persCaje;
	List<PersCaje> persCajes;

	@Inject
	PersCajeRegisInt persCajeRegis;

	@Inject
	PersCajeListaInt persCajeLista;
	
	@Inject
	PersonaRegisInt personaRegis;


	private static final long serialVersionUID = -6265838761281994084L;

	@PostConstruct
	public void cargar() {

		persCaje = new PersCaje();
		persCaje.setPersona(new Persona());
		persCaje.setEstado(true);
		
		this.rolPermiso = variablesSesion.getRolPermiso();

	}

	public void buscar() {

		try {
			
			persCajeLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.persCajes = persCajeLista.buscar(persCaje, this.pagina);
			this.numeroReg = persCajes.size();
			this.contadorReg = persCajeLista.contarRegistros(persCaje);
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
			this.persCaje = new PersCaje();
			this.persCaje.setPersona(new Persona());
			this.persCaje.setEstado(true);
			
			if (this.personaId != null) {
				
				Persona persona = new Persona();
				try {
					persona =  personaRegis.buscarPorId(Persona.class, this.getPersonaId());
					this.persCaje.setPersona(persona);
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al persona"));
					e.printStackTrace();
				}
			}
			
		} else {

			try {
				this.persCaje = persCajeRegis.buscarPorId(PersCaje.class, this.getId());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
				e.printStackTrace();
			}
		}
	}
	
	public String grabar() {

		try {
			if (this.id == null) {
				this.persCaje.getPersona().setEstado(true);
				Object id = persCajeRegis.insertar(persCaje);
				this.id = (Integer) id;
			} else {
				persCajeRegis.modificar(persCaje);
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

		if (this.getPersonaId() != null){
			return paginaRuta + "?faces-redirect=true&personaId=" + this.getPersonaId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}

	public String nuevo() {
		
		if (this.getPersonaId() != null){
			return "/ppsj/caja/persCaje/registra?faces-redirect=true&personaId=" + this.getPersonaId() + "&paginaRuta=" + this.getPaginaRuta();			
		} else {
			return "/ppsj/caja/persCaje/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta(); 
		}

	}
	
	public String modificar() {
		if (this.getPersonaId() != null){
			return "/ppsj/caja/persCaje/registra?faces-redirect=true&persCajeId=" + this.getId() + "&personaId=" + this.getPersonaId() + "&paginaRuta=" + this.getPaginaRuta();	
		} else {
			return "/ppsj/caja/persCaje/registra?faces-redirect=true&persCajeId=" + this.getId() + "&paginaRuta=" + this.getPaginaRuta();	
		}
	}
	
	public String explorar() {
		return "explora?faces-redirect=true&persCajeId=" + this.getId(); 
	}

//	Se llama solo desde personaExplora para traer parametro personaId
//	public String nuevo() {
//		return "/ppsj/caja/persCaje/registra?faces-redirect=true&personaId=" + this.getPersonaId();
//	}
		
//	public String modificar() {
//		return "/ppsj/caja/persCaje/registra?faces-redirect=true&persCajeId=" + this.getId();
//	}


	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			PersCaje persCaje = persCajeRegis.buscarPorId(PersCaje.class, this.getId());
			persCajeRegis.eliminar(persCaje);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

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
	
	public List<PersCaje> buscarTodo() {

		List<PersCaje> persCajes = new ArrayList<>();

		try {
			persCajes = persCajeLista.buscarTodo("persCajeId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return persCajes;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public PersCaje getPersCaje() {
		return persCaje;
	}

	public void setPersCaje(PersCaje persCaje) {
		this.persCaje = persCaje;
	}

	public List<PersCaje> getPersCajes() {
		return persCajes;
	}

	public void setPersCajes(List<PersCaje> persCajes) {
		this.persCajes = persCajes;
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

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}