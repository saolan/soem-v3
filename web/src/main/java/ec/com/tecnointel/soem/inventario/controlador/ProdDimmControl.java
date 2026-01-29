package ec.com.tecnointel.soem.inventario.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.inventario.listaInt.ProdDimmListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProductoListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdDimm;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrup;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.inventario.registroInt.ProdDimmRegisInt;
import ec.com.tecnointel.soem.parametro.listaInt.DimmListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ProdDimmControl extends PaginaControl implements Serializable {

	private Integer productoId;
	private String paginaRuta;
	private String listarIvaRete;
	
	private ProdDimm prodDimm;
	
	private List<ProdDimm> prodDimms;
	private List<Producto> productos;
	private List<Dimm> dimms;
	
	@Inject
	ProdDimmRegisInt prodDimmRegis;

	@Inject
	ProdDimmListaInt prodDimmLista;

	@Inject
	ProductoListaInt productoLista; 

	@Inject
	DimmListaInt dimmLista;

	private static final long serialVersionUID = -5505555784292930621L;

	@PostConstruct
	public void cargar() {
		prodDimm = new ProdDimm();
		prodDimm.setProducto(new Producto());
		prodDimm.setDimm(new Dimm());
		
		dimms = new ArrayList<Dimm>();
	}

	public void buscar() {

		try {
			
			prodDimmLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.prodDimms = prodDimmLista.buscar(prodDimm, this.pagina);
			this.numeroReg = prodDimms.size();
			this.contadorReg = prodDimmLista.contarRegistros(prodDimm);
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

		this.getProdDimm().getProducto().setProductoId(productoId);
		this.buscarProductos();
		
//		Recupera retencion renta, retencion iva, impuestos dependiendo de lo que selecciono en la pantalla explorar
//		se separa de esta manera sino en el listado de seleccion sale mezclado iva, retenciones, ice
//		el valor de la variable listarIvaRete viene desde la pagina
		if (this.listarIvaRete.equals("iva")) {
			this.dimms.addAll(this.buscarDimms("Tabla12"));
			this.dimms.addAll(this.buscarDimms("ReteICE"));
			this.dimms.addAll(this.buscarDimms("IRBPNR"));
		} else if (this.listarIvaRete.equals("reteRent")) {
			this.dimms.addAll(this.buscarDimms("Tabla3"));
		} else if (this.listarIvaRete.equals("reteIva")) {
			this.dimms.addAll(this.buscarDimms("Tabla11"));
		}

		if (this.id == null) {
			this.prodDimm = new ProdDimm();
		} else {

			try {
				this.prodDimm = prodDimmRegis.buscarPorId(ProdDimm.class, this.getId());
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
				
				Object id = prodDimmRegis.insertar(prodDimm);
				this.id = (Integer) id;
			} else {
				prodDimmRegis.modificar(prodDimm);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

//		return "explora?faces-redirect=true&prodDimmId=" + this.getId();
		if (this.getProductoId() != null){
			return paginaRuta + "?faces-redirect=true&productoId=" + this.getProductoId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public String nuevo() {
		
		if (this.getProductoId() != null){
			return "/ppsj/inventario/prodDimm/registra?faces-redirect=true&productoId=" + this.getProductoId() + "&paginaRuta=" + this.getPaginaRuta() + "&listarIvaRete=" + this.getListarIvaRete();			
		} else {
			return "/ppsj/inventario/prodDimm/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta() + "&listarIvaRete=" + this.getListarIvaRete(); 
		}

	}
	
	public String modificar() {
		if (this.getProductoId() != null){
			return "/ppsj/inventario/prodDimm/registra?faces-redirect=true&prodDimmId=" + this.getId() + "&productoId=" + this.getProductoId() + "&paginaRuta=" + this.getPaginaRuta() + "&listarIvaRete=" + this.getListarIvaRete();	
		} else {
			return "/ppsj/inventario/prodDimm/registra?faces-redirect=true&prodDimmId=" + this.getId() + "&paginaRuta=" + this.getPaginaRuta() + "&listarIvaRete=" + this.getListarIvaRete();	
		}
	}
	
	public String explorar() {
		return "explora?faces-redirect=true&prodDimmId=" + this.getId();
	}
	
	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			ProdDimm prodDimm = prodDimmRegis.buscarPorId(ProdDimm.class, this.getId());
			prodDimmRegis.eliminar(prodDimm);

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
	
	public void buscarProductos() {
		
		this.getProdDimm().getProducto().setProdGrup(new ProdGrup());
		this.getProdDimm().getProducto().setEstado(true);
		
		try {
			this.productos = productoLista.buscar(this.getProdDimm().getProducto(), null, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar producto - Error al buscar productos"));
			e.printStackTrace();
		}
	}
	
	public List<Dimm> buscarDimms(String tablaRefe) {
		
		Dimm dimmDesde = new Dimm();

		dimmDesde.setTablaRefe(tablaRefe);
		dimmDesde.setEstado(true);
		
		List<Dimm> dimms = new ArrayList<>();
		
		try {
			dimms = dimmLista.buscar(dimmDesde, dimmDesde, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Dimm - Error al buscar dimm"));
			e.printStackTrace();
		}
		
		return dimms;
	}
		
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public ProdDimm getProdDimm() {
		return prodDimm;
	}

	public void setProdDimm(ProdDimm prodDimm) {
		this.prodDimm = prodDimm;
	}

	public List<ProdDimm> getProdDimms() {
		return prodDimms;
	}

	public void setProdDimms(List<ProdDimm> prodDimms) {
		this.prodDimms = prodDimms;
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

	public List<Dimm> getDimms() {
		return dimms;
	}

	public void setDimms(List<Dimm> dimms) {
		this.dimms = dimms;
	}

	public String getPaginaRuta() {
		return paginaRuta;
	}

	public void setPaginaRuta(String paginaRuta) {
		this.paginaRuta = paginaRuta;
	}

	public String getListarIvaRete() {
		return listarIvaRete;
	}

	public void setListarIvaRete(String listarIvaRete) {
		this.listarIvaRete = listarIvaRete;
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

