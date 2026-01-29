package ec.com.tecnointel.soem.seguridad.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.SucursalListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.listaInt.RolListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolSucuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import ec.com.tecnointel.soem.seguridad.registroInt.RolSucuRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class RolSucuControl extends PaginaControl implements Serializable {

	private Integer rolId;
	private String paginaRuta;
	
	private RolSucu rolSucu;

	private List<RolSucu> RolSucus;
	private List<Rol> rols;
	private List<Sucursal> sucursals;

	@Inject
	RolSucuRegisInt rolSucuRegis;

	@Inject
	RolSucuListaInt rolSucuLista;
	
	@Inject
	RolListaInt rolLista;

	@Inject
	SucursalListaInt sucursalLista;
	
	private static final long serialVersionUID = 4604390788304972491L;

	@PostConstruct
	public void cargar() {
		rolSucu = new RolSucu();
		rolSucu.setRol(new Rol());
		rolSucu.setSucursal(new Sucursal());
	}

	public void buscar() {

		try {
			
			rolSucuLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.RolSucus = rolSucuLista.buscar(rolSucu, this.pagina);
			this.numeroReg = RolSucus.size();
			this.contadorReg = rolSucuLista.contarRegistros(rolSucu);
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
		
		this.getRolSucu().getRol().setRolId(rolId);
		this.buscarRols();
		this.buscarSucursals();


		if (this.id == null) {
			this.rolSucu = new RolSucu();
			this.rolSucu.setAcceso(true);
		} else {

			try {
				this.rolSucu = rolSucuRegis.buscarPorId(RolSucu.class, this.getId());
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
				Object id = rolSucuRegis.insertar(rolSucu);
				this.id = (Integer) id;
			} else {
				rolSucuRegis.modificar(rolSucu);
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
			return "/ppsj/seguridad/rolSucu/registra?faces-redirect=true&rolId=" + this.getRolId() + "&paginaRuta=" + this.getPaginaRuta();			
		} else {
			return "/ppsj/seguridad/rolSucu/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta(); 
		}

	}
	
	public String modificar() {
		if (this.getRolId() != null){
			return "/ppsj/seguridad/rolSucu/registra?faces-redirect=true&rolSucuId=" + this.getId() + "&rolId=" + this.getRolId() + "&paginaRuta=" + this.getPaginaRuta();	
		} else {
			return "/ppsj/seguridad/rolSucu/registra?faces-redirect=true&rolSucuId=" + this.getId() + "&paginaRuta=" + this.getPaginaRuta();
		}
	}

	public String explorar() {
		return "explora?faces-redirect=true&rolSucuId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			RolSucu rolSucu = rolSucuRegis.buscarPorId(RolSucu.class, this.getId());
			rolSucuRegis.eliminar(rolSucu);

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
	
	public List<RolSucu> buscarTodo() {

		List<RolSucu> rolSucus = new ArrayList<>();

		try {
			rolSucus = rolSucuLista.buscarTodo("rolSucuId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return rolSucus;
	}
	
	public void buscarRols() {

		this.getRolSucu().getRol().setEstado(true);
		
		try {
			this.rols = rolLista.buscar(this.getRolSucu().getRol(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion Buscar Rol - Error al buscar roles"));
			e.printStackTrace();
		}
	}
	
	public void buscarSucursals() {

		try {
			this.sucursals = sucursalLista.buscar(this.getRolSucu().getSucursal(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar sucursales"));
			e.printStackTrace();
		}
	}
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public RolSucu getRolSucu() {
		return rolSucu;
	}

	public void setRolSucu(RolSucu rolSucu) {
		this.rolSucu = rolSucu;
	}

	public List<RolSucu> getRolSucus() {
		return RolSucus;
	}

	public void setRolSucus(List<RolSucu> rolSucus) {
		RolSucus = rolSucus;
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

	public List<Sucursal> getSucursals() {
		return sucursals;
	}

	public void setSucursals(List<Sucursal> sucursals) {
		this.sucursals = sucursals;
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