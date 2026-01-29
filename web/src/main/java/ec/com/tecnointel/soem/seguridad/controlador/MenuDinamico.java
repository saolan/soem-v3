package ec.com.tecnointel.soem.seguridad.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import ec.com.tecnointel.soem.seguridad.listaInt.MenuListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.PersUsuaListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolMenuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.Menu;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolMenu;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@SessionScoped
public class MenuDinamico implements Serializable {
	
	@Inject
	RolMenuListaInt rolMenuLista;

	@Inject
	PersUsuaListaInt persUsuaLista;

	@Inject
	MenuListaInt menuList;
		
	private MenuModel menuModel;

	DefaultSubMenu defaultSubMenuNivel1;

	DefaultSubMenu defaultSubMenuNivel2;

	DefaultMenuItem defaultMenuItem;

	private static final long serialVersionUID = -4129445500837522809L;

	@PostConstruct
	public void init() {
		
		menuModel = new DefaultMenuModel();
		this.crearMenu();

	}

	public void crearMenu() {

//		Se trae el contexto (soem-web) para poder concatenar a la url del defaultMenuItem
//		ya que con la version 8 de primefaces la propiedad .url no registra este contexto
//		Se puede usar .outcome que si registra este contexto, en vez de .url y esta variable ya no haria falta
//		pero si por algún motivo un defaultMenuItem no existiere o estuviese mal escrito no abre ningún menu
//		y parece que el sistema se ha bloquedado. 
//		Por esta razón se mantiene la opción .url y se aumenta el contexto ya que asi la única pagina
//		que no se abre es la que no exite o esta mal escrita
		String contextPath =  FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		
//		Se puede cargar de esta manera pero se procedio de cargar persUsua de manera individual
//		VariablesSesion variablesSesion = (VariablesSesion) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
//				.get("variablesSesion"); 
		
		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");
		
		try {

// 			Recuperando una lista con los roles a los que pertenece el usuario
//			Set<RolPersUsua> rolPersUsuas = persUsua.getRolPersUsuas();
			Set<RolPersUsua> rolPersUsuas = persUsuaSesion.getRolPersUsuas();

			List<Rol> roles = new ArrayList<Rol>();
			roles.clear();

			for (RolPersUsua rolPersUsua : rolPersUsuas) {
				roles.add(rolPersUsua.getRol());
			}

			RolMenu rolMenu = new RolMenu();
			List<RolMenu> rolMenus = new ArrayList<RolMenu>();

			for (Rol rol : roles) {
				rolMenu.setAcceso(true);
				rolMenus.addAll(rolMenuLista.buscar(rolMenu, rol));
			}

			List<Menu> menus = new ArrayList<Menu>();

			for (RolMenu rolMenu1 : rolMenus) {
				menus.addAll(menuList.buscar(rolMenu1.getMenu(), null));
			}

//			// Ordenar lista Por ID del menu
			Collections.sort(menus, new Comparator() {
				@Override
				public int compare(Object menu1, Object menu2) {
					return (((Menu) menu1).getMenuId()).compareTo((((Menu) menu2).getMenuId()));
				}
			});

			// Eliminar Duplicados de la lista para no generar menus repetidos
			Set<Menu> linkedMenu = new LinkedHashSet<Menu>();
			linkedMenu.addAll(menus);
			menus.clear();
			menus.addAll(linkedMenu);

			for (Menu menu : menus) {

				if (!menu.isDetall() && menu.getMenu() == null) {

					defaultSubMenuNivel1 = DefaultSubMenu.builder()
							.label(menu.getDescri())
			                .build();

					menuModel.getElements().add(defaultSubMenuNivel1);

				} else if (!menu.isDetall() && menu.getMenu() != null) {

					defaultSubMenuNivel2 = DefaultSubMenu.builder()
							.label(menu.getDescri())
			                .build();

					defaultSubMenuNivel1.getElements().add(defaultSubMenuNivel2);

				} else if (menu.isDetall()) {
					
					defaultMenuItem = DefaultMenuItem.builder()
							.value(menu.getDescri())
							.url(contextPath + menu.getPaginaRuta())
							.build();
										
					defaultSubMenuNivel2.getElements().add(defaultMenuItem);
				}
			}
			
			defaultMenuItem = DefaultMenuItem.builder()
					.value("Acerca de")
					.url(contextPath+"/acercaDe.xhtml")
	                .build();

			menuModel.getElements().add(defaultMenuItem);
			
		} catch (Exception e1) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Error al crear menu. Por favor intente nuevamente"));
			e1.printStackTrace();
		}

		menuModel.generateUniqueIds();
	}
	
//	<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//	<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//	<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public MenuModel getModel() {
		return menuModel;
	}
}
