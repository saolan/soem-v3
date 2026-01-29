package ec.com.tecnointel.soem.inventario.controlador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.inventario.listaInt.ProdBodeListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProductoListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdBode;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrup;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.inventario.registroInt.ProdBodeRegisInt;
import ec.com.tecnointel.soem.parametro.listaInt.BodegaListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Bodega;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ProdBodeControl extends PaginaControl implements Serializable {

	private Integer productoId;
	private String paginaRuta;
	
	private ProdBode prodBode;

	private List<ProdBode> prodBodes;
	private List<Producto> productos;
	private List<Bodega> bodegas;

	@Inject
	ProdBodeRegisInt prodBodeRegis;

	@Inject
	ProdBodeListaInt prodBodeLista;

	@Inject
	BodegaListaInt bodegaLista;
	
	@Inject
	ProductoListaInt productoLista; 

	private static final long serialVersionUID = 6032879060368195003L;

	@PostConstruct
	public void cargar() {
		prodBode = new ProdBode();
		prodBode.setProducto(new Producto());
		prodBode.setBodega(new Bodega());
	}

	public void buscar() {

//		Sucursal sucursal = variablesSesion.getSucursal();
		prodBode.setSucursal(this.getVariablesSesion().getSucursal());

		try {
			
			prodBodeLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.prodBodes = prodBodeLista.buscar(prodBode, this.pagina);
			this.numeroReg = prodBodes.size();
			this.contadorReg = prodBodeLista.contarRegistros(prodBode);
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

		this.getProdBode().getProducto().setProductoId(productoId);
		this.buscarProductos();
		this.buscarBodegas();
		
		if (this.id == null) {
			this.prodBode = new ProdBode();
			this.prodBode.setMaximo(new BigDecimal(0));
			this.prodBode.setMinimo(new BigDecimal(0));
		} else {

			try {
				this.prodBode = prodBodeRegis.buscarPorId(ProdBode.class, this.getId());
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
				this.prodBode.setSucursal(this.getVariablesSesion().getSucursal());
				this.prodBode.setCantidInic(new BigDecimal(0));
				this.prodBode.setCantidActu(new BigDecimal(0));

				Object id = prodBodeRegis.insertar(prodBode);
				this.id = (Integer) id;
			} else {
				prodBodeRegis.modificar(prodBode);
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
			return "/ppsj/inventario/prodBode/registra?faces-redirect=true&productoId=" + this.getProductoId() + "&paginaRuta=" + this.getPaginaRuta();			
		} else {
			return "/ppsj/inventario/prodBode/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta(); 
		}

	}

	
	public String modificar() {
		if (this.getProductoId() != null){
			return "/ppsj/inventario/prodBode/registra?faces-redirect=true&prodBodeId=" + this.getId() + "&productoId=" + this.getProductoId() + "&paginaRuta=" + this.getPaginaRuta();	
		} else {
			return "/ppsj/inventario/prodBode/registra?faces-redirect=true&prodBodeId=" + this.getId() + "&paginaRuta=" + this.getPaginaRuta();
		}
	}

	public String explorar() {
		return "explora?faces-redirect=true&prodBodeId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			ProdBode prodBode = prodBodeRegis.buscarPorId(ProdBode.class, this.getId());
			prodBodeRegis.eliminar(prodBode);

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
			return paginaRuta + "?faces-redirect=true";			
		}

	}

	public List<ProdBode> buscarTodo() {

		List<ProdBode> prodBodes = new ArrayList<>();

		try {
			prodBodes = prodBodeLista.buscarTodo("prodBodeId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return prodBodes;
	}
	
	public void buscarBodegas() {

		this.getProdBode().getBodega().setEstado(true);
		
		try {
			this.bodegas = bodegaLista.buscar(this.getProdBode().getBodega(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Bodega - Error al buscar bodegas"));
			e.printStackTrace();
		}
	}
	
	public void buscarProductos() {

		this.getProdBode().getProducto().setProdGrup(new ProdGrup());
		this.getProdBode().getProducto().setEstado(true);
		
		try {
			this.productos = productoLista.buscar(this.getProdBode().getProducto(), null, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Producto - Error al buscar productos"));
			e.printStackTrace();
		}
	}
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public ProdBode getProdBode() {
		return prodBode;
	}

	public void setProdBode(ProdBode prodBode) {
		this.prodBode = prodBode;
	}

	public List<ProdBode> getProdBodes() {
		return prodBodes;
	}

	public void setProdBodes(List<ProdBode> prodBodes) {
		this.prodBodes = prodBodes;
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

	public List<Bodega> getBodegas() {
		return bodegas;
	}

	public void setBodegas(List<Bodega> bodegas) {
		this.bodegas = bodegas;
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