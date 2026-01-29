package ec.com.tecnointel.soem.parametro.controlador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.caja.listaInt.CajaMoviListaInt;
import ec.com.tecnointel.soem.caja.modelo.Caja;
import ec.com.tecnointel.soem.caja.modelo.CajaMovi;
import ec.com.tecnointel.soem.caja.modelo.PersCaje;
import ec.com.tecnointel.soem.firmaElec.modelo.CertEmis;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.ingreso.listaInt.IngresoListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.modelo.PersProv;
import ec.com.tecnointel.soem.inventario.listaInt.ProdBodeListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdCostListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdPrecListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProductoListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdBode;
import ec.com.tecnointel.soem.inventario.modelo.ProdCost;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrup;
import ec.com.tecnointel.soem.inventario.modelo.ProdPrec;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.inventario.registroInt.ProdBodeRegisInt;
import ec.com.tecnointel.soem.inventario.registroInt.ProdCostRegisInt;
import ec.com.tecnointel.soem.inventario.registroInt.ProdPrecRegisInt;
import ec.com.tecnointel.soem.parametro.listaInt.BodegaListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.PrecioListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.SucuCertEmisListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.SucursalListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Bodega;
import ec.com.tecnointel.soem.parametro.modelo.DocuCaja;
import ec.com.tecnointel.soem.parametro.modelo.DocuIngr;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.modelo.Precio;
import ec.com.tecnointel.soem.parametro.modelo.SucuCertEmis;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.registroInt.SucursalRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class SucursalControl extends PaginaControl implements Serializable {

	private Sucursal sucursal;

	List<Sucursal> sucursals;
	List<SucuCertEmis> sucuCertEmiss;

	@Inject
	SucursalRegisInt sucursalRegis;

	@Inject
	SucursalListaInt sucursaLista;
		
	@Inject
	ProductoListaInt productoLista;
	
	@Inject
	BodegaListaInt bodegaLista;

	@Inject
	ProdBodeListaInt prodBodeLista;
	
	@Inject
	ProdBodeRegisInt prodBodeRegis;

	@Inject
	ProdCostListaInt prodCostLista;

	@Inject
	ProdCostRegisInt prodCostRegis;
	
	@Inject
	PrecioListaInt precioLista;
	
	@Inject
	ProdPrecListaInt prodPrecLista;
	
	@Inject
	ProdPrecRegisInt prodPrecRegis;
	
	@Inject
	SucuCertEmisListaInt sucuCertEmisLista;

	@Inject
	CajaMoviListaInt cajaMoviLista;
	
	@Inject
	IngresoListaInt ingresoLista;
	
	private static final long serialVersionUID = 498782560260386931L;

	@PostConstruct
	public void cargar() {

		sucursal = new Sucursal();
		sucuCertEmiss = new ArrayList<SucuCertEmis>();

	}

	public void buscar() {

		try {
			
			sucursaLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.sucursals = sucursaLista.buscar(sucursal, this.pagina);
			this.numeroReg = sucursals.size();
			this.contadorReg = sucursaLista.contarRegistros(sucursal);
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
			this.sucursal = new Sucursal();
			sucursal.setEstado(true);
		} else {

			try {
				this.sucursal = sucursalRegis.buscarPorId(Sucursal.class, this.getId());
				
				SucuCertEmis sucuCertEmis = new SucuCertEmis(sucursal, new CertEmis());
				this.buscarSucuCertEmis(sucuCertEmis);
				
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar certificado"));
				e.printStackTrace();
			}
		}
	}

	public String grabar() {

		List<Producto> productos = new ArrayList<>();
		
		// Los mensajes tienen RequestScope y se borran al navegar a otro pagina
		// Con este comando se guarda los mensages de confirmaci�n en navegacion
		// entre paginas
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			if (this.id == null) {
				
//				Este campo se elimino de la pantalla
				sucursal.setLogoti("logo");
				
				Object id = sucursalRegis.insertar(sucursal);
				this.id = (Integer) id;
				
				productos = this.buscarProducto();
				
				this.insertarProdPrec(this.id, productos);
				this.insertarProdBode(this.id, productos);
				this.insertarProdCost(this.id, productos);
				
			} else {
				sucursalRegis.modificar(sucursal);
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
	
	public List<Producto> buscarProducto(){
		
		Producto producto = new Producto();
		List<Producto> productos = new ArrayList<>();
		
		producto.setProdGrup(new ProdGrup());
		producto.setEstado(true);
		try {
			
			productoLista.ordenar("descri");
			productos = productoLista.buscar(producto, null, 0);
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar productos"));
			e.printStackTrace();
		}
		
		return productos;
		
	}
	
	public void insertarProdPrec(Integer sucursalId, List<Producto> productos) {
		
		Sucursal sucursal = new Sucursal();
		Precio precio = new Precio();
		
		sucursal.setSucursalId(sucursalId);
		precio.setEstado(true);
		
		List<Precio> precios = new ArrayList<>();
		try {
			precios = precioLista.buscar(precio, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar precios"));
			e.printStackTrace();
		}
				
		for (Precio precioInsertar : precios) {
			
			if (precioInsertar.getAlcanc().equals(0)) {
				
				for (Producto productoInsertar : productos) {
					ProdPrec prodPrec = new ProdPrec();
					prodPrec.setProducto(productoInsertar);
					prodPrec.setPrecio(precioInsertar);
					prodPrec.setSucursal(sucursal);
					prodPrec.setFactor(precioInsertar.getFactor());
					prodPrec.setUtilid(new BigDecimal(0));
					prodPrec.setPrecioConImpu(new BigDecimal(0));
					prodPrec.setPrecioSinImpu(new BigDecimal(0));
					
					try {
						prodPrecRegis.insertar(prodPrec);
					} catch (Exception e) {
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar precios - " + precioInsertar.getDescri()));
						e.printStackTrace();
					}
				}
			}
		}
	}
		
	public void insertarProdCost(Integer sucursalId, List<Producto> productos) {
		
		Sucursal sucursal = new Sucursal();
		sucursal.setSucursalId(sucursalId);
		
		for (Producto productoInsertar : productos) {
			
			ProdCost prodCost = new ProdCost();
			prodCost.setProducto(productoInsertar);
			prodCost.setSucursal(sucursal);
			prodCost.setCostoInic(new BigDecimal(0));
			prodCost.setCostoActu(new BigDecimal(0));
			prodCost.setCostoUlti(new BigDecimal(0));
			prodCost.setCostoCont(new BigDecimal(0));
				
			try {
				prodCostRegis.insertar(prodCost);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar costos"));
				e.printStackTrace();
			}			
		}
	}	

	public void insertarProdBode(Integer sucursalId, List<Producto> productos) {
		
		Sucursal sucursal = new Sucursal();
		Bodega bodega = new Bodega();
		
		sucursal.setSucursalId(sucursalId);
		bodega.setEstado(true);
		
		List<Bodega> bodegas = new ArrayList<>();
		try {
			bodegas = bodegaLista.buscar(bodega, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar bodegas"));
			e.printStackTrace();
		}
		
		for (Bodega bodegaInsertar : bodegas) {
			
			for (Producto productoInsertar : productos) {
				
				ProdBode prodBode = new ProdBode();
				prodBode.setProducto(productoInsertar);
				prodBode.setBodega(bodegaInsertar);
				prodBode.setSucursal(sucursal);
				prodBode.setCantidInic(new BigDecimal(0));
				prodBode.setCantidActu(new BigDecimal(0));
				prodBode.setCantidCont(new BigDecimal(0));
				prodBode.setMaximo(new BigDecimal(0));
				prodBode.setMinimo(new BigDecimal(0));
				
				try {
					prodBodeRegis.insertar(prodBode);
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar Bodegas - " + bodegaInsertar.getDescri()));
					e.printStackTrace();
				}
			}
		}
	}
	
	public String modificar() {
		return "registra?faces-redirect=true&sucursalId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&sucursalId=" + this.getId();
	}

	public String eliminar() {

		List<CajaMovi> cajaMovis = new ArrayList<>();
		List<Ingreso> ingresos = new ArrayList<>();
		
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		

		try {
			
			Sucursal sucursal = sucursalRegis.buscarPorId(Sucursal.class, this.getId());
			
			if (sucursal.getSucursalId().equals(variablesSesion.getSucursal().getSucursalId())){
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Error - No se puede eliminar sucursal en sesión"));
				return null;
			}
			
			cajaMovis = this.buscarCajaMovi(sucursal);
			
			if (cajaMovis.size() != 0 || !cajaMovis.isEmpty()){
				
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Error - No se puede eliminar sucursal, existen movimientos de caja"));
				return null;
				
			}
			
			ingresos = this.buscarIngresos(sucursal);
			
			if (ingresos.size() != 0 || !ingresos.isEmpty()){
				
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Error - No se puede eliminar sucursal, existen ingresos"));
				
				return null;
				
			}

			this.eliminarProdPrec(sucursal);
			this.eliminarProdBode(sucursal);
			this.eliminarProdCost(sucursal);
			
			sucursalRegis.eliminar(sucursal);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado."));

		return "lista?faces-redirect=true";
	}

	public List<CajaMovi> buscarCajaMovi(Sucursal sucursal){
		
		CajaMovi cajaMovi = new CajaMovi();
		PersCaje persCaje = new PersCaje();
		DocuCaja docuCaja = new DocuCaja();
		
		List<CajaMovi> cajaMovis = new ArrayList<>();
		
		persCaje.setPersona(new Persona());
		docuCaja.setDocumento(new Documento());
		
		cajaMovi.setSucursal(sucursal);
		cajaMovi.setCaja(new Caja());
		cajaMovi.setDocuCaja(docuCaja);
		cajaMovi.setPersCaje(persCaje);
		
		try {
			
			cajaMovis = cajaMoviLista.buscar(cajaMovi, null);
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al buscar movimientos de caja"));
			e.printStackTrace();
		}
		
		return cajaMovis;
		
	}
	
	public List<Ingreso> buscarIngresos(Sucursal sucursal){
		
		Ingreso ingreso = new Ingreso();
		PersProv persProv = new PersProv();
		DocuIngr docuIngr = new DocuIngr();
		
		List<Ingreso> ingresos = new ArrayList<>();
		
		persProv.setPersona(new Persona());
		docuIngr.setDocumento(new Documento());
		
		ingreso.setSucursal(sucursal);
		ingreso.setDocuIngr(docuIngr);
		ingreso.setPersProv(persProv);
		
		try {
			ingresos = ingresoLista.buscar(ingreso, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al buscar ingresos"));
			e.printStackTrace();
		}
		
		return ingresos;
		
	}
	
	public void eliminarProdPrec(Sucursal sucursal){
		
		ProdPrec prodPrec = new ProdPrec();
		Producto producto = new Producto();
		
		List<ProdPrec> prodPrecs = new ArrayList<>();
		
		producto.setEstado(true);
		prodPrec.setSucursal(sucursal);
		prodPrec.setPrecio(new Precio());
		prodPrec.setProducto(producto);
		
		try {
			
			prodPrecLista.ordenar("descri");
			prodPrecs = this.prodPrecLista.buscar(prodPrec, null, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al buscar precios de productos"));
			e.printStackTrace();
		}
		
		for (ProdPrec prodPrecEliminar : prodPrecs) {
			try {
				prodPrecRegis.eliminar(prodPrecEliminar);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar precio " + prodPrecEliminar.getProducto().getDescri()));
				e.printStackTrace();
			}
		}
	}
	
	public void eliminarProdBode(Sucursal sucursal){
		
		ProdBode prodBode = new ProdBode();
		
		List<ProdBode> prodBodes = new ArrayList<>();
		
		prodBode.setSucursal(sucursal);
		prodBode.setBodega(new Bodega());
		prodBode.setProducto(new Producto());
		
		try {
			prodBodes = this.prodBodeLista.buscar(prodBode, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al buscar bodegas de productos"));
			e.printStackTrace();
		}
		
		for (ProdBode prodBodeEliminar : prodBodes) {
			try {
				prodBodeRegis.eliminar(prodBodeEliminar);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar bodegas " + prodBodeEliminar.getProducto().getDescri()));
				e.printStackTrace();
			}
		}
	}

	public void eliminarProdCost(Sucursal sucursal){
		
		ProdCost prodCost = new ProdCost();
		
		List<ProdCost> prodCosts = new ArrayList<>();
		
		prodCost.setSucursal(sucursal);
		prodCost.setProducto(new Producto());
		
		try {
			prodCosts = this.prodCostLista.buscar(prodCost, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al buscar costos de productos"));
			e.printStackTrace();
		}
		
		for (ProdCost prodCostEliminar : prodCosts) {
			try {
				prodCostRegis.eliminar(prodCostEliminar);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar costos " + prodCostEliminar.getProducto().getDescri()));
				e.printStackTrace();
			}
		}
	}
		
	public void buscarSucuCertEmis(SucuCertEmis sucuCertEmis){
				
		try {
			this.sucuCertEmiss = sucuCertEmisLista.buscar(sucuCertEmis, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar certificados"));

			e.printStackTrace();
		}

	}
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public List<Sucursal> getSucursals() {
		return sucursals;
	}

	public void setSucursals(List<Sucursal> sucursals) {
		this.sucursals = sucursals;
	}

	public List<SucuCertEmis> getSucuCertEmiss() {
		return sucuCertEmiss;
	}

	public void setSucuCertEmiss(List<SucuCertEmis> sucuCertEmiss) {
		this.sucuCertEmiss = sucuCertEmiss;
	}
}