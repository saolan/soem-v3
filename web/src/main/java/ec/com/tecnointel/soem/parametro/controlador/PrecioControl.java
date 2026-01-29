package ec.com.tecnointel.soem.parametro.controlador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.inventario.listaInt.ProductoListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrup;
import ec.com.tecnointel.soem.inventario.modelo.ProdPrec;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.inventario.registroInt.ProdPrecRegisInt;
import ec.com.tecnointel.soem.parametro.listaInt.PrecioListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.SucuPrecListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.SucursalListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Precio;
import ec.com.tecnointel.soem.parametro.modelo.SucuPrec;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.registroInt.PrecioRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class PrecioControl extends PaginaControl implements Serializable {

	private String ordenColumna;
	
	private Precio precio;

	private List<Precio> precios;
	private List<SucuPrec> sucuPrecs;

	@Inject
	PrecioRegisInt precioRegis;

	@Inject
	PrecioListaInt precioLista;
	
	@Inject
	ProductoListaInt productoLista;
	
	@Inject
	ProdPrecRegisInt prodPrecRegis;
	
	@Inject
	SucuPrecListaInt sucuPrecLista;
	
	@Inject
	SucursalListaInt sucursalLista;

	private static final long serialVersionUID = 5250017346714051942L;

	@PostConstruct
	public void cargar() {

		precio = new Precio();
		precio.setEstado(true);
		
		if (variablesSesion.isActivarImagen()) {
			ordenColumna = "codigo";
		} else {
			ordenColumna = "descri";
		}

	}

	public void buscar() {

		try {
			
			precioLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.precios = precioLista.buscar(precio, this.pagina);
			this.numeroReg = precios.size();
			this.contadorReg = precioLista.contarRegistros(precio);
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
//			this.precio = new Precio();
			this.precio.setFactor(new BigDecimal(1));
			this.precio.setEstado(true);
		} else {

			try {
				this.precio = precioRegis.buscarPorId(Precio.class, this.getId());
				this.buscarSucuPrecs(precio);
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
		
		List<ProdPrec> prodPrecs = new ArrayList<>();

		try {
			if (this.id == null) {
				
				Object id = precioRegis.insertar(precio);
				this.id = (Integer) id;

//				Crea los precios de acuerdo a getAlcanc() si es cero afecta a todos los productos
//				sino no crea listas y se agrega al producto manualmente
				if (this.getPrecio().getAlcanc().equals(0)) {
					prodPrecs = this.insertarProdPrecs(precio);
					this.insertar(prodPrecs);
				}
				
			} else {
				
				precioRegis.modificar(precio);
				
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "explora?faces-redirect=true&precioId=" + this.getId();
	}
	
	public List<ProdPrec> insertarProdPrecs(Precio precio){
		
		Producto producto = new Producto();
//		Precio precio = new Precio();
		
		List<Producto> productos = new ArrayList<>();
		List<ProdPrec> prodPrecs = new ArrayList<>();
		List<Sucursal> sucursals = new ArrayList<>();
		
		try {
			sucursals = sucursalLista.buscar(new Sucursal(), null);
		} catch (Exception e1) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar sucursales"));
			e1.printStackTrace();
		}
		
		producto.setProdGrup(new ProdGrup());
		producto.setEstado(true);
		
		try {
			
			productoLista.ordenar(ordenColumna);
			productoLista.filasPagina(0);
			
			productos = this.productoLista.buscar(producto, null, null);
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar productos"));
			e.printStackTrace();
		}
		
		
		for (Sucursal sucursal : sucursals) {
			
//			precio.setPrecioId(precioId);
			
			for (Producto productoInsertar : productos) {
				
				ProdPrec prodPrec = new ProdPrec();
				
				prodPrec.setPrecio(precio);
				prodPrec.setProducto(productoInsertar);
				prodPrec.setSucursal(sucursal);
				prodPrec.setFactor(precio.getFactor());
				prodPrec.setUtilid(new BigDecimal(0));
				prodPrec.setPrecioSinImpu(new BigDecimal(0));
				prodPrec.setPrecioConImpu(new BigDecimal(0));
				
				prodPrecs.add(prodPrec);
				
			}			
		}

		return prodPrecs;
	}
	
	public void insertar(List<ProdPrec> prodPRecs) {
		
		for (ProdPrec prodPrec : prodPRecs) {
			
			try {
				prodPrecRegis.insertar(prodPrec);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al insertar Productos - Precios" + prodPrec.getProducto().getDescri() ));
				e.printStackTrace();
			}
			
		}
	}

	public String modificar() {
		return "registra?faces-redirect=true&precioId=" + this.getId();
	}
	
	public String explorar() {
		return "explora?faces-redirect=true&precioId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			Precio precio = precioRegis.buscarPorId(Precio.class, this.getId());
			precioRegis.eliminar(precio);

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

	public List<Precio> buscarTodo() {

		List<Precio> precios = new ArrayList<>();

		try {
			precios = precioLista.buscarTodo("descri");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return precios;
	}
	
	public void buscarSucuPrecs(Precio precio) {

		SucuPrec sucuPrec = new SucuPrec();
		sucuPrec.setPrecio(precio);
		sucuPrec.setSucursal(new Sucursal());

		try {
			this.sucuPrecs = sucuPrecLista.buscar(sucuPrec, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar precios"));
			e.printStackTrace();
		}
	}
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public Precio getPrecio() {
		return precio;
	}

	public void setPrecio(Precio precio) {
		this.precio = precio;
	}

	public List<Precio> getPrecios() {
		return precios;
	}

	public void setPrecios(List<Precio> precios) {
		this.precios = precios;
	}

	public List<SucuPrec> getSucuPrecs() {
		return sucuPrecs;
	}

	public void setSucuPrecs(List<SucuPrec> sucuPrecs) {
		this.sucuPrecs = sucuPrecs;
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