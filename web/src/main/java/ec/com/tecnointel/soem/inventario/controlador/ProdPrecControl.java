package ec.com.tecnointel.soem.inventario.controlador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.inventario.listaInt.ProdDimmListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdPrecListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProductoListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdDimm;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrup;
import ec.com.tecnointel.soem.inventario.modelo.ProdPrec;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.inventario.registroInt.ProdPrecRegisInt;
import ec.com.tecnointel.soem.parametro.listaInt.PrecioListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Precio;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.listaInt.RolPrecListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolSucuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ProdPrecControl extends PaginaControl implements Serializable {

	private Integer productoId;
	private String paginaRuta;
	
	private ProdPrec prodPrec;
	
	private Parametro paraActualPrecioTodasSucu;

	private List<ProdPrec> prodPrecs;
	private List<Producto> productos;
	private List<Precio> precios;
	
	private Set<Sucursal> sucursals;
	private Set<Precio> preciosSet;

	private List<Parametro> parametros;
	
	@Inject
	ProdPrecRegisInt prodPrecRegis;

	@Inject
	ProdPrecListaInt prodPrecLista;

	@Inject
	PrecioListaInt precioLista;
	
	@Inject
	ProductoListaInt productoLista;
	
	@Inject
	ProdDimmListaInt prodDimmLista;
	
	@Inject
	RolSucuListaInt rolSucuLista;

	@Inject
	RolPrecListaInt rolPrecLista;
	
	private static final long serialVersionUID = -4443019646959117463L;

	@PostConstruct
	public void cargar() {
		
		Producto producto = new Producto();
		prodPrec = new ProdPrec();
		
		ProdGrup prodGrup = new ProdGrup();
		
		this.parametros = new ArrayList<>();
		
//		Esta busqueda va aqui para poder seleccionar el tipo predeterminado
//		ya que aqui se esta haciendo la asignacion de las variables
		List<Parametro> parametros = this.buscarProdGrupTipos();
		
		String[] tipoGrupPred;
		
//		Se recorre la lista y se busca el valor predeterminado PT = Predeterminado true; PF = Predeterminado False
//		Tambien se quita de la descripcion del parametro el valor que determina cual es el predeterminado
		for (Parametro parametro : parametros) {
			tipoGrupPred = parametro.getDescri().split(";");
			if (tipoGrupPred[1].equals("PVN") || tipoGrupPred[1].equals("PCV")) {
				prodGrup.setTipo(tipoGrupPred[0]);
			}
			parametro.setDescri(tipoGrupPred[0]);
			this.parametros.add(parametro);
		}
		
		producto.setProdGrup(prodGrup);
		producto.setEstado(true);
		
//		Se coloca en estado true como predeterminado
//		Se tendria que poner un campo para poder seleccionar entre true y false
		prodPrec.setProducto(producto);
		prodPrec.setPrecio(new Precio());

//		Estas variables se utilizan para la pantalla rolPrecList
		this.sucursals = new HashSet<>();
		this.preciosSet = new HashSet<>();
		
//		Este metodo se utiliza para la pantalla rolPrecList
		List<RolSucu> rolSucus =  this.buscarRolSucus();
		for (RolSucu rolSucu : rolSucus) {
			sucursals.add(rolSucu.getSucursal());
		}
		
//		Este metodo se utiliza para la pantalla rolPrecList 
		List<RolPrec> rolPrecs = this.buscarRolPrecs();
		for (RolPrec rolPrecFiltro : rolPrecs) {
			this.preciosSet.add(rolPrecFiltro.getPrecio());
		}
		
		this.paraActualPrecioTodasSucu = this.buscarParametro(5080);
	}

//	public void buscar() {
//
//		prodPrec.setSucursal(this.getVariablesSesion().getSucursal());
//
//		try {
//			
//			prodPrecLista.ordenar("descri");
//			this.prodPrecs = prodPrecLista.buscar(prodPrec, this.pagina, variablesSesion.getFilasPagina());
//			this.numeroReg = prodPrecs.size();
//			this.contadorReg = prodPrecLista.contarRegistros(prodPrec);
//		} catch (Exception e) {
//			FacesContext.getCurrentInstance().addMessage(null,
//					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
//			e.printStackTrace();
//		}
//	}
	
//	Este metodo se llama desde la pagina
	public void buscar() {

		try {
			
			prodPrecLista.ordenar("descri");
			this.prodPrecs = prodPrecLista.buscar(sucursals, preciosSet, prodPrec, this.pagina, variablesSesion.getFilasPagina());
			this.numeroReg = prodPrecs.size();
			this.contadorReg = prodPrecLista.contarRegistros(sucursals, preciosSet, prodPrec);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}
	
	public List<ProdPrec> buscarProdPrecs(Set<Sucursal> sucursals, Set<Precio> precios, ProdPrec prodPrec, Integer pagina, Integer filas) {
		
		List<ProdPrec> prodPrecs = new ArrayList<>();
		
		try {
			
			prodPrecLista.ordenar("descri");
			prodPrecs = prodPrecLista.buscar(sucursals, precios, prodPrec, pagina, filas);
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar precios con sucursales"));
			e.printStackTrace();
		}
		return prodPrecs;
	}


	public List<RolPrec> buscarRolPrecs(){
		
		RolPrec rolPrec = new RolPrec();
		
		List<RolSucu> rolSucus = new ArrayList<>();
		List<RolPrec> rolPrecs = new ArrayList<>();
		
		Set<Sucursal> sucursals = new HashSet<>();
		Set<Integer> rols = new HashSet<>();
		
		rolSucus = this.buscarRolSucus();
		
		for (RolSucu rolSucu : rolSucus) {
			rols.add(rolSucu.getRol().getRolId());
			sucursals.add(rolSucu.getSucursal());
		}
		
		List<Integer> rols1 = new ArrayList<>(rols);
		
		rolPrec.setAcceso(true);
		for (Sucursal sucursal : sucursals) {
			try {
				rolPrecs.addAll(rolPrecLista.buscar(sucursal, rols1, rolPrec));
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepción - Error al buscar rol-precio"));
				e.printStackTrace();
			}
		}
				
		return rolPrecs;				
	}

	public List<RolSucu> buscarRolSucus() {
		
		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");
		
		List<RolSucu> rolSucus = new ArrayList<>();
		
		try {
			rolSucus = rolSucuLista.buscar(persUsuaSesion.getRolPersUsuas());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar sucursales del rol"));
			e.printStackTrace();
		}
			
		return rolSucus;
	}
	
	public Parametro buscarParametro(Integer parametroId) {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, parametroId);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepción - Error al buscar parámetro " + parametroId));
			e.printStackTrace();
		}

		return parametro;

	}
	
	public List<Parametro> buscarProdGrupTipos() {
		
		Parametro parametro = new Parametro();
		
		List<Parametro> parametros = new ArrayList<>();
		
		parametro.setCodigo("Inventario-GrupoTipo");
		parametro.setEstado(true);
	
		try {
			parametros = parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepción- Error al buscar parametro Inventario-GrupoTipo"));
			e.printStackTrace();
		}
		
		return parametros;
	}
	
	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}

		this.getProdPrec().getProducto().setProductoId(productoId);
		this.buscarProductos();
		this.buscarPrecios();
		
		if (this.id == null) {
			this.prodPrec = new ProdPrec();
			this.prodPrec.setFactor(new BigDecimal(1));
			this.prodPrec.setUtilid(new BigDecimal(0));
			
		} else {

			try {
				this.prodPrec = prodPrecRegis.buscarPorId(ProdPrec.class, this.getId());
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
				
		this.calcularPrecioConImpu();

		try {
			if (this.id == null) {
				
				this.prodPrec.setSucursal(this.getVariablesSesion().getSucursal());

				Object id = prodPrecRegis.insertar(prodPrec);
				this.id = (Integer) id;
			} else {
				
				if (this.paraActualPrecioTodasSucu.getDescri().equals("true")) {
					
					List<ProdPrec> prodPrecs =  new ArrayList<ProdPrec>();
					Set<Precio> precios = new HashSet<Precio>();

					precios.add(this.prodPrec.getPrecio());
					
					prodPrecs = this.buscarProdPrecs(this.sucursals, precios, this.prodPrec, null, null);
					
					for (ProdPrec prodPrec : prodPrecs) {
						
//						TODO: Error no permite tener dos precios (presentaciones) iguales
//						con factor o precio diferente
//						Al modificar datos de todas las sucursales no se puede modificar 
//						el campo setPrecio
//						prodPrec.setPrecio(this.prodPrec.getPrecio());
						prodPrec.setFactor(this.prodPrec.getFactor());
						prodPrec.setUtilid(this.prodPrec.getUtilid());
						prodPrec.setPrecioSinImpu(this.prodPrec.getPrecioSinImpu());
						prodPrec.setPrecioConImpu(this.prodPrec.getPrecioConImpu());

						prodPrecRegis.modificar(prodPrec);
					}
				} else {
					prodPrecRegis.modificar(prodPrec);					
				}
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
			return "/ppsj/inventario/prodPrec/registra?faces-redirect=true&productoId=" + this.getProductoId() + "&paginaRuta=" + this.getPaginaRuta();			
		} else {
			return "/ppsj/inventario/prodPrec/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta(); 
		}

	}
		
	public String modificar() {
		if (this.getProductoId() != null){
			return "/ppsj/inventario/prodPrec/registra?faces-redirect=true&prodPrecId=" + this.getId() + "&productoId=" + this.getProductoId() + "&paginaRuta=" + this.getPaginaRuta();	
		} else {
			return "/ppsj/inventario/prodPrec/registra?faces-redirect=true&prodPrecId=" + this.getId() + "&paginaRuta=" + this.getPaginaRuta();
		}
	}

//	Esta pagina se abre desde productos (explorar) y desde prodPRecList entonces
//	Tambien deberia ser asi para poder navergar y para pasar el parametro productoId
//	return "/ppsj/inventario/prodPrec/registra?faces-redirect=true&prodPrecId=" + this.getId() + "&productoId=" + this.getProductoId() + "&paginaRuta=" + this.getPaginaRuta();
//	Pero se dejo solamente como esta abajo porque en la pagina explorar no se llena el combo de productos
//	y si se abre esta pagina desde productos (explorar) ahi no existe la opcion explorar solo modificar
	public String explorar() {
		return "explora?faces-redirect=true&prodPrecId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			ProdPrec prodPrec = prodPrecRegis.buscarPorId(ProdPrec.class, this.getId());
			prodPrecRegis.eliminar(prodPrec);

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

	public List<ProdPrec> buscarTodo() {

		List<ProdPrec> prodPrecs = new ArrayList<>();

		try {
			prodPrecs = prodPrecLista.buscarTodo("prodPrecId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return prodPrecs;
	}
	
	public void buscarPrecios() {

		this.getProdPrec().getPrecio().setEstado(true);
		
		try {
			this.precios = precioLista.buscar(this.getProdPrec().getPrecio(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Precio - Error al buscar precios"));
			e.printStackTrace();
		}
	}
	
	public void buscarProductos() {

		this.getProdPrec().getProducto().setProdGrup(new ProdGrup());
		this.getProdPrec().getProducto().setEstado(true);
		
		try {
			productoLista.ordenar("descri");
			this.productos = productoLista.buscar(this.getProdPrec().getProducto(), null, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Productos - Error al buscar productos"));
			e.printStackTrace();
		}
	}
	
	public void calcularPrecioConImpu(){
		
		BigDecimal porcenImpu = new BigDecimal(0);
		BigDecimal inversImpu  = new BigDecimal(0); // De 12;1.12 de 14;1.14
		
		ProdDimm prodDimmBuscar = new ProdDimm();
		List<ProdDimm> prodDimms = new ArrayList<>();
		
		prodDimmBuscar.setProducto(this.getProdPrec().getProducto());
		prodDimmBuscar.setDimm(new Dimm());
		
		try {
			
//			Suma todos los impuestos cargados al producto para poder desglosar
			prodDimms = this.prodDimmLista.buscar(prodDimmBuscar, null);
			for (ProdDimm prodDimm : prodDimms) {
				
//				No toma en cuenta las retenciones que son factor -1, ni los impuestos por ICE
				if (prodDimm.getDimm().getFactor() == 1 && !prodDimm.getDimm().getTablaRefe().equals("ReteICE")) {
					porcenImpu = porcenImpu.add(prodDimm.getDimm().getPorcen());
				}
			}
			
			inversImpu = porcenImpu.divide(new BigDecimal(100)).add(new BigDecimal(1));
			
			this.getProdPrec().setPrecioSinImpu(this.getProdPrec().getPrecioConImpu().divide(inversImpu,6,RoundingMode.HALF_UP));
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Dimm - Error al buscar impuestos"));
			e.printStackTrace();
		}
	}
	

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public ProdPrec getProdPrec() {
		return prodPrec;
	}

	public void setProdPrec(ProdPrec prodPrec) {
		this.prodPrec = prodPrec;
	}

	public List<ProdPrec> getProdPrecs() {
		return prodPrecs;
	}

	public void setProdPrecs(List<ProdPrec> prodPrecs) {
		this.prodPrecs = prodPrecs;
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

	public List<Precio> getPrecios() {
		return precios;
	}

	public void setPrecgas(List<Precio> precios) {
		this.precios = precios;
	}

	public String getPaginaRuta() {
		return paginaRuta;
	}

	public void setPaginaRuta(String paginaRuta) {
		this.paginaRuta = paginaRuta;
	}

	public List<Parametro> getParametros() {
		return parametros;
	}

	public void setParametros(List<Parametro> parametros) {
		this.parametros = parametros;
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

