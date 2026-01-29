package ec.com.tecnointel.soem.inventario.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.inventario.listaInt.ProdSubpListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProductoListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrup;
import ec.com.tecnointel.soem.inventario.modelo.ProdSubp;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.inventario.registroInt.ProdSubpRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ProdSubpControl extends PaginaControl implements Serializable {

	private Integer productoId;
	private String paginaRuta;

	private ProdSubp prodSubp;

	private List<ProdSubp> prodSubps;
	private List<Producto> productos;
	private List<Producto> subProductos;

	private static final long serialVersionUID = 8056105833423445273L;

	@Inject
	ProdSubpRegisInt prodSubpRegis;

	@Inject
	ProdSubpListaInt prodSubpLista;

	@Inject
	ProductoListaInt productoLista;

	@PostConstruct
	public void cargar() {

		prodSubp = new ProdSubp();

		prodSubp.setProducto(new Producto());
		prodSubp.setSubProducto(new Producto());
		
	}
	
	public String cancelar(){
		
		if (this.getProductoId() != null){
			return paginaRuta + "?faces-redirect=true&productoId=" + this.getProductoId();	
		} else {
			return paginaRuta +  "?faces-redirect=true";	
		}

	}

	public void buscar() {

		try {
			this.prodSubps = prodSubpLista.buscar(prodSubp, this.pagina);
			this.numeroReg = prodSubps.size();
			this.contadorReg = prodSubpLista.contarRegistros(prodSubp);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}


	public List<Producto> buscarProductos(Producto producto) {

		List<Producto> productos = new ArrayList<Producto>();
		
		try {
			productoLista.ordenar("descri");
			productos = productoLista.buscar(producto, null, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Productos - Error al buscar productos"));
			e.printStackTrace();
		}
		
		return productos;
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}

//		Se le asigna productoId para que busque solamente el producto que se va a explorar
//		y en la combo no aparezcan todos los productos 
		Producto productoBuscar = new Producto();
		productoBuscar.setProdGrup(new ProdGrup());
		productoBuscar.setProductoId(productoId);
		productoBuscar.setEstado(true);
		this.productos = this.buscarProductos(productoBuscar);

		Producto subProductoBuscar = new Producto();
		subProductoBuscar.setProdGrup(new ProdGrup());
		subProductoBuscar.setEstado(true);
		
		this.subProductos = this.buscarProductos(subProductoBuscar); 
		
		if (this.id == null) {
			this.prodSubp = new ProdSubp();
		} else {

			try {
				this.prodSubp = prodSubpRegis.buscarPorId(ProdSubp.class, this.getId());
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
				Object id = prodSubpRegis.insertar(prodSubp);
				this.id = (Integer) id;
			} else {
				prodSubpRegis.modificar(prodSubp);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));


		return paginaRuta + "?faces-redirect=true&productoId=" + this.getProductoId();			
	}

	public String modificar() {
		
		if (this.getProductoId() != null){
			return "/ppsj/inventario/prodSubp/registra?faces-redirect=true&prodSubpId=" + this.getId() + "&productoId=" + this.getProductoId() + "&paginaRuta=" + this.getPaginaRuta();	
		} else {
			return "/ppsj/inventario/prodSubp/registra?faces-redirect=true&prodSubpId=" + this.getId() + "&paginaRuta=" + this.getPaginaRuta();
		}
	}

	public String explorar() {
		return "explora?faces-redirect=true&productoId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			ProdSubp prodSubp = prodSubpRegis.buscarPorId(ProdSubp.class, this.getId());
			prodSubpRegis.eliminar(prodSubp);

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
	
	public String nuevo() {
		
		if (this.getProductoId() != null){
			return "/ppsj/inventario/prodSubp/registra?faces-redirect=true&productoId=" + this.getProductoId() + "&paginaRuta=" + this.getPaginaRuta();			
		} else {
			return "/ppsj/inventario/prodSubp/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta(); 
		}
	}


	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public ProdSubp getProdSubp() {
		return prodSubp;
	}

	public void setProdSubp(ProdSubp prodSubp) {
		this.prodSubp = prodSubp;
	}

	public List<ProdSubp> getProdSubps() {
		return prodSubps;
	}

	public void setProdSubps(List<ProdSubp> prodSubps) {
		this.prodSubps = prodSubps;
	}

	public Integer getProductoId() {
		return productoId;
	}

	public void setProductoId(Integer productoId) {
		this.productoId = productoId;
	}

	public String getPaginaRuta() {
		return paginaRuta;
	}

	public void setPaginaRuta(String paginaRuta) {
		this.paginaRuta = paginaRuta;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	public ProductoListaInt getProductoLista() {
		return productoLista;
	}

	public void setProductoLista(ProductoListaInt productoLista) {
		this.productoLista = productoLista;
	}

	public List<Producto> getSubProductos() {
		return subProductos;
	}

	public void setSubProductos(List<Producto> subProductos) {
		this.subProductos = subProductos;
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
	// listener='#{prodSubpControlador.buscarTodo}' />
	// </f:metadata>

	// public void buscarTodo() {
	// try {
	// this.prodSubps =
	// prodSubpConsultaInterface.prodSubpConsultar(this.getProdSubp());
	// } catch (Exception e) {
	// // TODOs Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

}