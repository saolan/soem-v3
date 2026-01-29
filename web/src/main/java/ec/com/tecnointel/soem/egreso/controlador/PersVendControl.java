package ec.com.tecnointel.soem.egreso.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.egreso.listaInt.PersVendListaInt;
import ec.com.tecnointel.soem.egreso.modelo.PersVend;
import ec.com.tecnointel.soem.egreso.registroInt.PersVendRegisInt;
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
public class PersVendControl extends PaginaControl implements Serializable {

	private Integer personaId;
	private String paginaRuta;
	
	private PersVend persVend;
	List<PersVend> persVends;

	@Inject
	PersVendRegisInt persVendRegis;

	@Inject
	PersVendListaInt persVendLista;
	
	@Inject
	PersonaRegisInt personaRegis;
	
	private static final long serialVersionUID = -854309081783265230L;

	@PostConstruct
	public void cargar() {

		persVend = new PersVend();
		persVend.setPersona(new Persona());
		persVend.setEstado(true);
		
		this.rolPermiso = variablesSesion.getRolPermiso();

	}

	public void buscar() {

		try {
			
			persVendLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.persVends = persVendLista.buscar(persVend, this.pagina);
			this.numeroReg = persVends.size();
			this.contadorReg = persVendLista.contarRegistros(persVend);
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
			this.persVend = new PersVend();
			this.persVend.setPersona(new Persona());
			this.persVend.setEstado(true);
			
			if (this.personaId != null) {
				
				Persona persona = new Persona();
				try {
					persona =  personaRegis.buscarPorId(Persona.class, this.getPersonaId());
					this.persVend.setPersona(persona);
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al persona"));
					e.printStackTrace();
				}
			}

		} else {

			try {
				this.persVend = persVendRegis.buscarPorId(PersVend.class, this.getId());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
				e.printStackTrace();
			}
		}
	}

	public String grabar() {

		String campoTexto;
		
		campoTexto = this.persVend.getPersona().getCedulaRuc().trim();
		this.persVend.getPersona().setCedulaRuc(campoTexto);

		campoTexto = this.persVend.getPersona().getApelli().trim();
		this.persVend.getPersona().setApelli(campoTexto);

		if (this.persVend.getPersona().getNombre() != null) {
			campoTexto = this.persVend.getPersona().getNombre().trim();
			this.persVend.getPersona().setNombre(campoTexto);
		}
		
		try {
			if (this.id == null) {
				this.persVend.getPersona().setEstado(true);
				Object id = persVendRegis.insertar(persVend);
				this.id = (Integer) id;
			} else {
				persVendRegis.modificar(persVend);
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
			return "/ppsj/egreso/persVend/registra?faces-redirect=true&personaId=" + this.getPersonaId() + "&paginaRuta=" + this.getPaginaRuta();			
		} else {
			return "/ppsj/egreso/persVend/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta(); 
		}

	}
	
	public String modificar() {
		if (this.getPersonaId() != null){
			return "/ppsj/egreso/persVend/registra?faces-redirect=true&persVendId=" + this.getId() + "&personaId=" + this.getPersonaId() + "&paginaRuta=" + this.getPaginaRuta();	
		} else {
			return "/ppsj/egreso/persVend/registra?faces-redirect=true&persVendId=" + this.getId() + "&paginaRuta=" + this.getPaginaRuta();	
		}
	}
	
//	Se llama solo desde personaExplora para traer parametro personaId
//	public String nuevo() {
//		return "/ppsj/egreso/persVend/registra?faces-redirect=true&personaId=" + this.getPersonaId();
//	}

//	public String modificar() {
//		return "/ppsj/egreso/persVend/registra?faces-redirect=true&persVendId=" + this.getId();
//	}

	public String explorar() {
		return "explora?faces-redirect=true&persVendId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			PersVend persVend = persVendRegis.buscarPorId(PersVend.class, this.getId());
			persVendRegis.eliminar(persVend);

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

	public List<PersVend> buscarTodo() {

		List<PersVend> persVends = new ArrayList<>();

		try {
			persVends = persVendLista.buscarTodo("persVendId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return persVends;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	public PersVend getPersVend() {
		return persVend;
	}

	public void setPersVend(PersVend persVend) {
		this.persVend = persVend;
	}

	public List<PersVend> getPersVends() {
		return persVends;
	}

	public void setPersVends(List<PersVend> persVends) {
		this.persVends = persVends;
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