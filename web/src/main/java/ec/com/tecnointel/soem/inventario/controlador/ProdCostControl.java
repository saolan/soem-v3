package ec.com.tecnointel.soem.inventario.controlador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.inventario.listaInt.ProdCostListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProductoListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdCost;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrup;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.inventario.registroInt.ProdCostRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ProdCostControl extends PaginaControl implements Serializable {

	private Integer productoId;
	private String paginaRuta;
	
	private ProdCost prodCost;

	private List<ProdCost> prodCosts;
	private List<Producto> productos;

	@Inject
	ProdCostRegisInt prodCostRegis;

	@Inject
	ProdCostListaInt prodCostLista;
	
	@Inject
	ProductoListaInt productoLista; 

//	@Inject
//	VariablesSesionOld variablesSesion;

	private static final long serialVersionUID = -1922050570898338906L;

	@PostConstruct
	public void cargar() {
		prodCost = new ProdCost();
		prodCost.setProducto(new Producto());
	}

	public void buscar() {

//		Sucursal sucursal = variablesSesion.getSucursal();
		prodCost.setSucursal(this.getVariablesSesion().getSucursal());

		try {
			
			prodCostLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.prodCosts = prodCostLista.buscar(prodCost, this.pagina);
			this.numeroReg = prodCosts.size();
			this.contadorReg = prodCostLista.contarRegistros(prodCost);
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

		this.getProdCost().getProducto().setProductoId(productoId);
		this.buscarProductos();
		
		if (this.id == null) {
			this.prodCost = new ProdCost();

		} else {

			try {
				this.prodCost = prodCostRegis.buscarPorId(ProdCost.class, this.getId());
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
				
//				Sucursal sucursal = variablesSesion.getSucursal();
				this.prodCost.setSucursal(this.getVariablesSesion().getSucursal());
				this.prodCost.setCostoInic(new BigDecimal(0));
				this.prodCost.setCostoActu(new BigDecimal(0));

				Object id = prodCostRegis.insertar(prodCost);
				this.id = (Integer) id;
			} else {
				prodCostRegis.modificar(prodCost);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		if (this.getProductoId() != null){
			return paginaRuta + "?faces-redirect=true&productoId=" + this.getProductoId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public String nuevo() {
		
		if (this.getProductoId() != null){
			return "/ppsj/inventario/prodCost/registra?faces-redirect=true&productoId=" + this.getProductoId() + "&paginaRuta=" + this.getPaginaRuta();			
		} else {
			return "/ppsj/inventario/prodCost/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta(); 
		}

	}

	public String modificar() {
		if (this.getProductoId() != null){
			return "/ppsj/inventario/prodCost/registra?faces-redirect=true&prodCostId=" + this.getId() + "&productoId=" + this.getProductoId() + "&paginaRuta=" + this.getPaginaRuta();	
		} else {
			return "/ppsj/inventario/prodCost/registra?faces-redirect=true&prodCostId=" + this.getId() + "&paginaRuta=" + this.getPaginaRuta();
		}
	}

	public String explorar() {
		return "explora?faces-redirect=true&prodCostId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			ProdCost prodCost = prodCostRegis.buscarPorId(ProdCost.class, this.getId());
			prodCostRegis.eliminar(prodCost);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		if (this.getProductoId() != null){
			return paginaRuta + "?faces-redirect=true&productoId=" + this.getProductoId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public String cancelar(){
		
		if (this.getProductoId() != null){
			return paginaRuta + "?faces-redirect=true&productoId=" + this.getProductoId();	
		} else {
			return paginaRuta +  "?faces-redirect=true";	
		}

	}
	
	public List<ProdCost> buscarTodo() {

		List<ProdCost> prodCosts = new ArrayList<>();

		try {
			prodCosts = prodCostLista.buscarTodo("prodCostId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return prodCosts;
	}
	
	public void buscarProductos() {

		this.getProdCost().getProducto().setProdGrup(new ProdGrup());
		this.getProdCost().getProducto().setEstado(true);
		
		try {
			this.productos = productoLista.buscar(this.getProdCost().getProducto(), null, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar producto - Error al buscar productos"));
			e.printStackTrace();
		}
	}
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public ProdCost getProdCost() {
		return prodCost;
	}

	public void setProdCost(ProdCost prodCost) {
		this.prodCost = prodCost;
	}

	public List<ProdCost> getProdCosts() {
		return prodCosts;
	}

	public void setProdCosts(List<ProdCost> prodCosts) {
		this.prodCosts = prodCosts;
	}

	public Integer getProductoId() {
		return productoId;
	}

	public void setProductoId(Integer productoId) {
		this.productoId = productoId;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
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

