package ec.com.tecnointel.soem.seguridad.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.seguridad.listaInt.MenuListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.MenuNiveListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.Menu;
import ec.com.tecnointel.soem.seguridad.modelo.MenuNive;
import ec.com.tecnointel.soem.seguridad.registroInt.MenuRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class MenuControl extends PaginaControl implements Serializable {

	private Menu menu;

	List<Menu> menus;
	List<MenuNive> menuNives;

	@Inject
	MenuRegisInt menuRegis;

	@Inject
	MenuListaInt menuLista;
	
	@Inject
	MenuNiveListaInt menuNiveLista;

	private static final long serialVersionUID = -3381455931600461066L;

	@PostConstruct
	public void cargar() {

		menu = new Menu();
		menu.setEstado(true);

	}

	public void buscar() {

		try {
			
			menuLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.menus = menuLista.buscar(menu, this.pagina);
			this.numeroReg = menus.size();
			this.contadorReg = menuLista.contarRegistros(menu);
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
		
		this.buscarMenuNives();

		if (this.id == null) {
			this.menu = new Menu();
			this.menu.setEstado(true);
		} else {

			try {
				this.menu = menuRegis.buscarPorId(Menu.class, this.getId());
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
				Object id = menuRegis.insertar(menu);
				this.id = (Integer) id;
			} else {
				menuRegis.modificar(menu);
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
		return "registra?faces-redirect=true&menuId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&menuId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			Menu secuencial = menuRegis.buscarPorId(Menu.class, this.getId());
			menuRegis.eliminar(secuencial);

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

	public List<Menu> buscarTodo() {

		List<Menu> menus = new ArrayList<>();

		try {
			menus = menuLista.buscarTodo("descri");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return menus;
	}
	
	public void buscarMenuNives(){
		
		this.getMenu().setMenuNive(new MenuNive());
		
		try {
			this.menuNives = menuNiveLista.buscar(this.getMenu().getMenuNive(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar niveles de menú"));
			e.printStackTrace();
		}
	}
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	public List<MenuNive> getMenuNives() {
		return menuNives;
	}

	public void setMenuNives(List<MenuNive> menuNives) {
		this.menuNives = menuNives;
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