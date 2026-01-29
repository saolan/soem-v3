package ec.com.tecnointel.soem.seguridad.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.seguridad.listaInt.MenuListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolMenuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.Menu;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolMenu;
import ec.com.tecnointel.soem.seguridad.registroInt.RolMenuRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class RolMenuControl extends PaginaControl implements Serializable {

	private RolMenu rolMenu;

	List<RolMenu> rolMenus;
	List<Rol> rols;
	List<Menu> menus;

	@Inject
	RolMenuRegisInt rolMenuRegis;

	@Inject
	RolMenuListaInt RolMenuLista;
	
	@Inject
	RolListaInt rolLista;

	@Inject
	MenuListaInt menuLista;


	private static final long serialVersionUID = 1868480449515488480L;

	@PostConstruct
	public void cargar() {
		rolMenu = new RolMenu();
		rolMenu.setRol(new Rol());
		rolMenu.setMenu(new Menu());
		rolMenu.setAcceso(true);
	}

	public void buscar() {

		try {
			
			RolMenuLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.rolMenus = RolMenuLista.buscar(rolMenu, this.pagina);
			this.numeroReg = rolMenus.size();
			this.contadorReg = RolMenuLista.contarRegistros(rolMenu);
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

		this.buscarRols();
		this.buscarMenus();

		if (this.id == null) {
			this.rolMenu = new RolMenu();
			this.rolMenu.setAcceso(true);
		} else {

			try {
				this.rolMenu = rolMenuRegis.buscarPorId(RolMenu.class, this.getId());
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
				Object id = rolMenuRegis.insertar(rolMenu);
				this.id = (Integer) id;
			} else {
				rolMenuRegis.modificar(rolMenu);
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
		return "registra?faces-redirect=true&rolMenuId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&rolMenuId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			RolMenu rolMenu = rolMenuRegis.buscarPorId(RolMenu.class, this.getId());
			rolMenuRegis.eliminar(rolMenu);

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

	public List<RolMenu> buscarTodo() {

		List<RolMenu> rolMenus = new ArrayList<>();

		try {
			rolMenus = RolMenuLista.buscarTodo("rolMenuId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return rolMenus;
	}
	
	public void buscarRols() {

		this.getRolMenu().getRol().setEstado(true);
		
		try {
			this.rols = rolLista.buscar(this.getRolMenu().getRol(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion Buscar Rol - Error al buscar roles"));
			e.printStackTrace();
		}
	}
	
	public void buscarMenus() {

		this.getRolMenu().getMenu().setEstado(true);
		
		try {
			this.menus = menuLista.buscar(this.getRolMenu().getMenu(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion Buscar Menu - Error al buscar Menús"));
			e.printStackTrace();
		}
	}
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public RolMenu getRolMenu() {
		return rolMenu;
	}

	public void setRolMenu(RolMenu rolMenu) {
		this.rolMenu = rolMenu;
	}

	public List<RolMenu> getRolMenus() {
		return rolMenus;
	}

	public void setRolMenus(List<RolMenu> rolMenus) {
		this.rolMenus = rolMenus;
	}

	public List<Rol> getRols() {
		return rols;
	}

	public void setRols(List<Rol> rols) {
		this.rols = rols;
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
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