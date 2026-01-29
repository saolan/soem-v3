package ec.com.tecnointel.soem.inventario.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.inventario.listaInt.ProdGrupListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProductoListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrup;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.parametro.listaInt.BodegaListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.PrecioListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.SucursalListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Bodega;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Precio;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class RecaPrecControl extends PaginaControl implements Serializable {

	private static final long serialVersionUID = 9183832648167498738L;
	
	private int numeroRegProd;
	private long contadorRegProd;
	private Integer paginaProd;

	private Producto productoBuscar;

	private List<Sucursal> sucursals;
	private List<Bodega> bodegas;
	private List<ProdGrup> prodGrups;
	private List<Precio> precios;
	private List<Producto> productos;
	private List<Parametro> parametroEstados;
	
//	TODO: Faltan lista de los seleccionados 
	
	@Inject
	SucursalListaInt sucursalLista;

	@Inject
	BodegaListaInt bodegaLista;

	@Inject
	ProdGrupListaInt prodGrupLista;
	
	@Inject
	PrecioListaInt precioLista;

	@Inject
	ProductoListaInt productoLista;

	@PostConstruct
	public void cargar() {

		ProdGrup prodGrup = new ProdGrup();
		this.productoBuscar = new Producto();
		
		prodGrup.setTipo("Todo");
		this.productoBuscar.setProdGrup(prodGrup);
		this.productoBuscar.setControStoc("Controla");
		this.productoBuscar.setEstado(true);
		
		this.sucursals = new ArrayList<Sucursal>();
		this.bodegas = new ArrayList<Bodega>();
		this.prodGrups = new ArrayList<ProdGrup>();
		this.productos = new ArrayList<Producto>();
		this.precios = new ArrayList<Precio>();
		
		parametroEstados = new ArrayList<Parametro>();
		
		Sucursal sucursalBuscar = new Sucursal();
		this.sucursals = this.buscarSucursals(sucursalBuscar);

		Bodega bodegaBuscar = new Bodega();
		bodegaBuscar.setEstado(true);
		this.bodegas = this.buscarBodegas(bodegaBuscar);

		ProdGrup prodGrupBuscar = new ProdGrup();
		prodGrupBuscar.setEstado(true);
		this.prodGrups = this.buscarProdGrups(prodGrupBuscar);
		
		Precio precioBuscar = new Precio();
		precioBuscar.setEstado(true);
		this.precios = this.buscarPrecios(precioBuscar);

		Parametro parametroBuscar = new Parametro();
		parametroBuscar.setCodigo("Parametro-Estado");
		parametroBuscar.setEstado(true);
		this.parametroEstados = this.buscarParametros(parametroBuscar);

	}

	public List<Bodega> buscarBodegas(Bodega bodega) {

		List<Bodega> bodegas = new ArrayList<Bodega>();

		try {
			bodegas = bodegaLista.buscar(bodega, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar bodegas"));
			e.printStackTrace();
		}

		return bodegas;
	}
	
	public List<Parametro> buscarParametros(Parametro parametro) {
				
		List<Parametro> parametros = new ArrayList<Parametro>();
		try {
			parametros = parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepción al buscar parametro estado de documentos"));
			e.printStackTrace();
		}
		return parametros;
	}

	public void buscarProductos(Integer paginador) {

		if (paginador == 0) {
			this.paginaProd = 0;
		}
		if (this.productoBuscar.getCodigoBarra() != null) {
			this.productoBuscar.setCodigo(null);
			this.productoBuscar.setDescri(null);
		}

		try {
			
			productoBuscar.setEstado(true);
			
			productoLista.ordenar("descri");
			productoLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.productos = productoLista.buscar(productoBuscar, this.paginaProd, variablesSesion.getFilasPagina());
			this.numeroReg = productos.size();
			this.contadorReg = productoLista.contarRegistros(productoBuscar);
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public List<Precio> buscarPrecios(Precio precio) {

		List<Precio> precios = new ArrayList<Precio>();

		try {
			precios = precioLista.buscar(precio, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar grupos de productos"));
			e.printStackTrace();
		}

		return precios;
	}

	public List<ProdGrup> buscarProdGrups(ProdGrup prodGrup) {

		List<ProdGrup> prodGrups = new ArrayList<ProdGrup>();

		try {
			prodGrups = prodGrupLista.buscar(prodGrup, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar grupos de productos"));
			e.printStackTrace();
		}

		return prodGrups;
	}

	public List<Sucursal> buscarSucursals(Sucursal sucursal) {

		List<Sucursal> sucursals = new ArrayList<Sucursal>();

		try {
			sucursals = sucursalLista.buscar(sucursal, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar sucursales"));
			e.printStackTrace();
		}

		return sucursals;
	}

	public String grabar() {
		return "";
	}

	public int getNumeroRegProd() {
		return numeroRegProd;
	}

	public void setNumeroRegProd(int numeroRegProd) {
		this.numeroRegProd = numeroRegProd;
	}

	public long getContadorRegProd() {
		return contadorRegProd;
	}

	public void setContadorRegProd(long contadorRegProd) {
		this.contadorRegProd = contadorRegProd;
	}

	public Integer getPaginaProd() {
		return paginaProd;
	}

	public void setPaginaProd(Integer paginaProd) {
		this.paginaProd = paginaProd;
	}

	public Producto getProductoBuscar() {
		return productoBuscar;
	}

	public void setProductoBuscar(Producto productoBuscar) {
		this.productoBuscar = productoBuscar;
	}

	public List<Sucursal> getSucursals() {
		return sucursals;
	}

	public void setSucursals(List<Sucursal> sucursals) {
		this.sucursals = sucursals;
	}

	public List<Bodega> getBodegas() {
		return bodegas;
	}

	public void setBodegas(List<Bodega> bodegas) {
		this.bodegas = bodegas;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	public List<Parametro> getParametroEstados() {
		return parametroEstados;
	}

	public void setParametroEstados(List<Parametro> parametroEstados) {
		this.parametroEstados = parametroEstados;
	}

	public List<ProdGrup> getProdGrups() {
		return prodGrups;
	}

	public void setProdGrups(List<ProdGrup> prodGrups) {
		this.prodGrups = prodGrups;
	}

	public List<Precio> getPrecios() {
		return precios;
	}

	public void setPrecios(List<Precio> precios) {
		this.precios = precios;
	}
}
