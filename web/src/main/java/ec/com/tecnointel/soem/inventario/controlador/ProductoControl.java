package ec.com.tecnointel.soem.inventario.controlador;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.primefaces.model.file.UploadedFile;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.general.util.IdenSistema;
import ec.com.tecnointel.soem.general.util.Util;
import ec.com.tecnointel.soem.inventario.listaInt.KardexListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdBodeListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdCostListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdDimmListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdGrupListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdPrecListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdSubpListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdTipoListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProductoListaInt;
import ec.com.tecnointel.soem.inventario.modelo.Kardex;
import ec.com.tecnointel.soem.inventario.modelo.ProdBode;
import ec.com.tecnointel.soem.inventario.modelo.ProdComp;
import ec.com.tecnointel.soem.inventario.modelo.ProdCost;
import ec.com.tecnointel.soem.inventario.modelo.ProdDimm;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrup;
import ec.com.tecnointel.soem.inventario.modelo.ProdPrec;
import ec.com.tecnointel.soem.inventario.modelo.ProdSubp;
import ec.com.tecnointel.soem.inventario.modelo.ProdTipo;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.inventario.registroInt.ProdBodeRegisInt;
import ec.com.tecnointel.soem.inventario.registroInt.ProdCostRegisInt;
import ec.com.tecnointel.soem.inventario.registroInt.ProdDimmRegisInt;
import ec.com.tecnointel.soem.inventario.registroInt.ProdPrecRegisInt;
import ec.com.tecnointel.soem.inventario.registroInt.ProductoRegisInt;
import ec.com.tecnointel.soem.parametro.listaInt.BodegaListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.PrecioListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.SucursalListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Bodega;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Precio;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.listaInt.RolBodeListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolPrecListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolSucuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolBode;
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
public class ProductoControl extends PaginaControl implements Serializable {

	private Producto producto;

	private String ordenColumna;

	List<Producto> productos;
	List<ProdTipo> prodTipos;
	List<ProdGrup> prodGrups;
	List<ProdBode> prodBodes;
	List<ProdCost> prodCosts;
	List<ProdDimm> prodDimms;
	List<ProdSubp> prodSubps;
	List<ProdDimm> prodDimmIvas;
	List<ProdDimm> prodDimmReteRents;
	List<ProdDimm> prodDimmReteIvas;
	List<ProdPrec> prodPrecs;
	List<ProdComp> prodComps;
	List<Parametro> parametros;

	private UploadedFile uploadedFile;

	@Inject
	ProductoRegisInt productoRegis;

	@Inject
	ProductoListaInt productoLista;

	@Inject
	ProdTipoListaInt prodTipoLista;

	@Inject
	ProdGrupListaInt prodGrupLista;

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
	ProdDimmListaInt prodDimmLista;

	@Inject
	ProdDimmRegisInt prodDimmRegis;

	@Inject
	ProdSubpListaInt prodSubpLista;

	@Inject
	PrecioListaInt precioLista;

	@Inject
	ProdPrecListaInt prodPrecLista;

	@Inject
	ProdPrecRegisInt prodPrecRegis;

	@Inject
	RolPrecListaInt rolPrecLista;

	@Inject
	RolBodeListaInt rolBodeLista;

	@Inject
	RolSucuListaInt rolSucuLista;

	@Inject
	SucursalListaInt sucursalLista;

	@Inject
	KardexListaInt kardexLista;

	@Inject
	IdenSistema idenSistema;

	private static final long serialVersionUID = 46546512690478023L;

	@PostConstruct
	public void cargar() {

		producto = new Producto();
		ProdGrup prodGrup = new ProdGrup();

		this.parametros = new ArrayList<>();

//		Esta busqueda va aqui para poder seleccionar el tipo predeterminado
//		ya que aqui se esta haciendo la asignacion de las variables
		List<Parametro> parametros = this.buscarProdGrupTipos();

		String[] tipoGrupPred;

//		Se recorre la lista y se quita de la descripcion del parametro el valor que determina cual es el predeterminado
		for (Parametro parametro : parametros) {
			tipoGrupPred = parametro.getDescri().split(";");
			parametro.setDescri(tipoGrupPred[0]);
			this.parametros.add(parametro);
		}

		prodGrup.setTipo("Todo");

		producto.setProdGrup(prodGrup);
		producto.setControStoc("Controla");
		producto.setEstado(true);

		prodPrecs = new ArrayList<>();
		prodBodes = new ArrayList<>();
		prodComps = new ArrayList<>();

		prodDimmIvas = new ArrayList<>();
		prodDimmReteRents = new ArrayList<>();
		prodDimmReteIvas = new ArrayList<>();

		if (variablesSesion.isActivarImagen()) {
			ordenColumna = "codigo";
		} else {
			ordenColumna = "descri";
		}
	}

	public void buscar() {

		try {

			productoLista.ordenar(ordenColumna);
			productoLista.filasPagina(variablesSesion.getFilasPagina());

			this.productos = productoLista.buscar(producto, this.pagina, variablesSesion.getFilasPagina());
			this.numeroReg = productos.size();
			this.contadorReg = productoLista.contarRegistros(producto);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}

//	public void buscarKardex(Producto producto){
//		
//		Kardex kardex = new Kardex();
//		kardex.setProducto(producto);
//		
//		List<Kardex> kardexs = new ArrayList<>();
//		
//		try {
//			kardexs = kardexLista.buscar(kardex);
//		} catch (Exception e) {
//			FacesContext.getCurrentInstance().addMessage(null,
//					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar grupos de productos"));
//	
//			e.printStackTrace();
//		}
//	
//	}

	public void buscarProdBode(Producto producto) {

		RolBode rolBode = new RolBode();

		Set<Sucursal> sucursals = new HashSet<>();
		List<Integer> rols = new ArrayList<Integer>();
		List<RolBode> rolBodes = new ArrayList<>();

		List<RolSucu> rolSucus = new ArrayList<>();

		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");

		try {
			// Buscar el acceso del rol a cada una de las sucursales
			rolSucus = rolSucuLista.buscar(persUsuaSesion.getRolPersUsuas());
			// Crear una lista de sucursales eliminando los repetidos
			for (RolSucu rolSucu : rolSucus) {
				sucursals.add(rolSucu.getSucursal());
				rols.add(rolSucu.getRol().getRolId());
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar roles por sucursal"));
			e.printStackTrace();
		}

		for (Sucursal sucursal : sucursals) {

			List<Integer> bodegas = new ArrayList<Integer>();

			// Buscar acceso del rol a las diferetes sucursales y precios
			rolBode.setAcceso(true);
			try {
				rolBodes = rolBodeLista.buscar(sucursal, rols, rolBode);
			} catch (Exception e1) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al buscar roles del usuario"));
				e1.printStackTrace();
			}
			// Crear listas para enviar como parametros de busqueda en prodPrec
			// ya que la busqueda se realiza con la clausula IN
			for (RolBode rolPrecSesion : rolBodes) {
				bodegas.add(rolPrecSesion.getBodega().getBodegaId());
			}

			try {
				this.prodBodes.addAll(prodBodeLista.buscar(sucursal, bodegas, producto));
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al buscar bodegas del producto"));
				e.printStackTrace();
			}
		}
	}

	public void buscarProdComps(Producto producto) {

		RolPrec rolPrec = new RolPrec();

		Set<Sucursal> sucursals = new HashSet<>();
		List<Integer> rols = new ArrayList<Integer>();
		List<RolPrec> rolPrecs = new ArrayList<>();

		List<RolSucu> rolSucus = new ArrayList<>();

		List<Object[]> prodComps;

		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");

		try {
			// Buscar el acceso del rol a cada una de las sucursales
			rolSucus = rolSucuLista.buscar(persUsuaSesion.getRolPersUsuas());
			// Crear una lista de sucursales eliminando los repetidos
			for (RolSucu rolSucu : rolSucus) {
				sucursals.add(rolSucu.getSucursal());
				rols.add(rolSucu.getRol().getRolId());
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar roles por sucursal"));
			e.printStackTrace();
		}

		for (Sucursal sucursal : sucursals) {

			List<Integer> precios = new ArrayList<Integer>();

			// Buscar acceso del rol a las diferentes sucursales y precios
			rolPrec.setAcceso(true);
			try {
				rolPrecs = rolPrecLista.buscar(sucursal, rols, rolPrec);
			} catch (Exception e1) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al buscar roles del usuario"));
				e1.printStackTrace();
			}
			// Crear listas para enviar como parametros de busqueda en prodPrec
			// ya que la busqueda se realiza con la clausula IN
			for (RolPrec rolPrecSesion : rolPrecs) {
				precios.add(rolPrecSesion.getPrecio().getPrecioId());
			}

			try {
				prodComps = prodPrecLista.buscarProdComps(producto, rolPrecs);

				for (Object[] object : prodComps) {

					ProdComp prodComp = new ProdComp((String) object[0], (String) object[1], (String) object[2],
							new java.sql.Date(((java.sql.Date) object[3]).getTime()).toLocalDate(),
							(Integer) object[4], (BigDecimal) object[5], (BigDecimal) object[6], (BigDecimal) object[7],
							(BigDecimal) object[8], (BigDecimal) object[9], (BigDecimal) object[10],
							(BigDecimal) object[11]);

					this.prodComps.add(prodComp);

				}
				// this.prodPrecs.addAll(prodPrecLista.buscar(sucursal, precios, producto));
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al buscar precios del producto"));
				e.printStackTrace();
			}
		}
	}

	public void buscarProdCosts(Producto producto) {

		List<Integer> sucursals = new ArrayList<Integer>();
		List<Integer> rols = new ArrayList<Integer>();

		List<RolSucu> rolSucus = new ArrayList<>();

		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");

		try {
			// Buscar el acceso del rol a cada una de las sucursales
			rolSucus = rolSucuLista.buscar(persUsuaSesion.getRolPersUsuas());
			// Recorre la lista para obtener los Ids de la sucursal y enviar como paramtro
			// al buscar prodCost
			for (RolSucu rolSucu : rolSucus) {
				sucursals.add(rolSucu.getSucursal().getSucursalId());
				rols.add(rolSucu.getRol().getRolId());
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar roles por sucursal"));
			e.printStackTrace();
		}

		try {
			this.prodCosts = prodCostLista.buscar(sucursals, producto);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Costos"));
			e.printStackTrace();
		}

	}

	public void buscarProdDimms(Producto producto) {

		ProdDimm prodDimm = new ProdDimm();
		prodDimm.setProducto(producto);
		prodDimm.setDimm(new Dimm());

		try {
			this.prodDimms = prodDimmLista.buscar(prodDimm, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Dimms"));
			e.printStackTrace();
		}
	}

	public void buscarProdGrupos() {

		ProdGrup prodGrup = new ProdGrup();
		prodGrup.setDetall(true);
		prodGrup.setEstado(true);

		try {
			this.prodGrups = prodGrupLista.buscar(prodGrup, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar grupos de productos"));

			e.printStackTrace();
		}

	}

	public List<ProdSubp> buscarProdSubps(ProdSubp prodSubp) {

		List<ProdSubp> prodSubps = new ArrayList<ProdSubp>();
		try {

			prodSubpLista.filasPagina(variablesSesion.getFilasPagina());

			prodSubps = prodSubpLista.buscar(prodSubp, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar SubProductos"));
			e.printStackTrace();
		}

		return prodSubps;
	}

	public List<Parametro> buscarProdGrupTipos() {

		Parametro parametro = new Parametro();

		List<Parametro> parametros = new ArrayList<>();

		parametro.setCodigo("Inventario-GrupoTipo");
		parametro.setEstado(true);

		try {
			parametros = parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepción- Error al buscar parametro Inventario-GrupoTipo"));
			e.printStackTrace();
		}

		return parametros;
	}

	public void buscarProdPrecs(Producto producto) {

		RolPrec rolPrec = new RolPrec();

		Set<Sucursal> sucursals = new HashSet<>();
		List<Integer> rols = new ArrayList<Integer>();
		List<RolPrec> rolPrecs = new ArrayList<>();

		List<RolSucu> rolSucus = new ArrayList<>();

		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");

		try {
			// Buscar el acceso del rol a cada una de las sucursales
			rolSucus = rolSucuLista.buscar(persUsuaSesion.getRolPersUsuas());
			// Crear una lista de sucursales eliminando los repetidos
			for (RolSucu rolSucu : rolSucus) {
				sucursals.add(rolSucu.getSucursal());
				rols.add(rolSucu.getRol().getRolId());
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar roles por sucursal"));
			e.printStackTrace();
		}

		for (Sucursal sucursal : sucursals) {

			List<Integer> precios = new ArrayList<Integer>();

			// Buscar acceso del rol a las diferetes sucursales y precios
			rolPrec.setAcceso(true);
			try {
				rolPrecs = rolPrecLista.buscar(sucursal, rols, rolPrec);
			} catch (Exception e1) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al buscar roles del usuario"));
				e1.printStackTrace();
			}
			// Crear listas para enviar como parametros de busqueda en prodPrec
			// ya que la busqueda se realiza con la clausula IN
			for (RolPrec rolPrecSesion : rolPrecs) {
				precios.add(rolPrecSesion.getPrecio().getPrecioId());
			}

			try {
				this.prodPrecs.addAll(prodPrecLista.buscar(sucursal, precios, producto));
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al buscar precios del producto"));
				e.printStackTrace();
			}
		}
	}

	public void buscarProdTipos() {

		ProdTipo prodTipo = new ProdTipo();
		prodTipo.setEstado(true);
		this.getProducto().setProdTipo(prodTipo);

		try {
			this.prodTipos = prodTipoLista.buscar(this.getProducto().getProdTipo(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar tipos de productos"));

			e.printStackTrace();
		}
	}

	public Parametro buscarRutaImagenInventario() {

		Parametro parametro = null;

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 5020);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion 5020 - Error al buscar ruta imagenes inventario"));
			e.printStackTrace();
		}

		return parametro;

	}

	public Long contarProductos(boolean estado) {

		Long totalProductos = null;

		Producto producto = new Producto(new ProdGrup(), null, null, null, estado);

		try {
			totalProductos = productoLista.contarRegistros(producto);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al contar productos"));
			e.printStackTrace();
		}
		return totalProductos;
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {

			Producto producto = productoRegis.buscarPorId(Producto.class, this.getId());

			Kardex kardex = new Kardex();
			kardex.setProducto(producto);
			List<Kardex> kardexs = kardexLista.buscar(kardex);

			if (kardexs.size() == 0) {

//				Estas listas se llenaron al recuperar()
				for (ProdPrec prodPrec : this.prodPrecs) {
					this.prodPrecRegis.eliminar(prodPrec);
				}

				for (ProdCost prodCost : this.prodCosts) {
					this.prodCostRegis.eliminar(prodCost);
				}

				for (ProdBode prodBode : this.prodBodes) {
					this.prodBodeRegis.eliminar(prodBode);
				}

				for (ProdDimm prodDimm : this.prodDimms) {
					this.prodDimmRegis.eliminar(prodDimm);
				}
			}

			productoRegis.eliminar(producto);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Excepción", "No se puede eliminar producto puede tener movimientos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		return "lista?faces-redirect=true";

	}

	public String explorar() {
		return "explora?faces-redirect=true&productoId=" + this.getId();
	}

//	Separa en listas diferentes el iva e ice y las retenciones para poder ver por separado en las paginas
	public void filtrarProdDimms(List<ProdDimm> prodDimms) {

		for (ProdDimm prodDimm : prodDimms) {
			if (prodDimm.getDimm().getTablaRefe().toLowerCase().equals("tabla12")
					|| prodDimm.getDimm().getTablaRefe().toLowerCase().equals("reteice")
					|| prodDimm.getDimm().getTablaRefe().toLowerCase().equals("irbpnr")) {
				this.prodDimmIvas.add(prodDimm);
			} else if (prodDimm.getDimm().getTablaRefe().toLowerCase().equals("tabla3")) {
				this.prodDimmReteRents.add(prodDimm);
			} else if (prodDimm.getDimm().getTablaRefe().toLowerCase().equals("tabla11")) {
				this.prodDimmReteIvas.add(prodDimm);
			}
		}
	}

	public String grabar() {

		// Los mensajes tienen RequestScope y se borran al navegar a otro pagina
		// Con este comando se guarda los mensages de confirmaci�n en navegacion
		// entre paginas
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		// Validar Licencia
		if (!idenSistema.getEstadoLicencia().equals("activada")) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Licencia no activada"));

			Long registros = this.contarProductos(true);
			registros = registros + this.contarProductos(false);

			if (registros >= 10) {

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"El periodo de prueba ha finalizado. Por favor comuniquese con su distribuidor para obtener su licencia"));

				return null;
			}
		}
		// Fin Validar Licencia

		Sucursal sucursal = new Sucursal();

		List<Sucursal> sucursales = new ArrayList<>();

		try {

			sucursales = sucursalLista.buscar(sucursal, null);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar sucursales"));
			e.printStackTrace();
		}

		try {

			String descri = this.producto.getDescri().trim();
			this.producto.setDescri(descri);

			if (this.id == null) {

				// Graba siempre el tipo de producto
				ProdTipo prodTipo = new ProdTipo();
				prodTipo.setProdTipoId(1);
				this.producto.setProdTipo(prodTipo);
				// Fin Graba siempre el tipo de producto

				Object id = productoRegis.insertar(producto);
				this.id = (Integer) id;

				this.insertarProdDimm(this.id);

				for (Sucursal sucursalInsertar : sucursales) {
					this.insertarProdPrec(this.id, sucursalInsertar);
					this.insertarProdBode(this.id, sucursalInsertar);
					this.insertarProdCost(this.id, sucursalInsertar);
				}

			} else {

				productoRegis.modificar(producto);

			}
		} catch (RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error código barra ya existe"));
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "explora?faces-redirect=true&productoId=" + this.getId();
	}

	public void insertarProdPrec(Integer productoId, Sucursal sucursal) {

		Producto producto = new Producto();
		Precio precio = new Precio();

		producto.setProductoId(productoId);

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
				ProdPrec prodPrec = new ProdPrec();
				prodPrec.setProducto(producto);
				prodPrec.setPrecio(precioInsertar);
				prodPrec.setSucursal(sucursal);
				prodPrec.setFactor(precioInsertar.getFactor());
				prodPrec.setUtilid(new BigDecimal(0));
				prodPrec.setPrecioConImpu(new BigDecimal(0));
				prodPrec.setPrecioSinImpu(new BigDecimal(0));

				try {
					prodPrecRegis.insertar(prodPrec);
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
							null, "Excepcion - Error al grabar precios - " + precioInsertar.getDescri()));
					e.printStackTrace();
				}
			}
		}
	}

	public void insertarProdDimm(Integer productoId) {

		Parametro parametroBuscar = new Parametro();
		parametroBuscar.setCodigo("Inventario-ImpuestoPred");
		parametroBuscar.setEstado(true);

		List<Parametro> parametroDimms = new ArrayList<>();
		try {
			parametroDimms = parametroLista.buscar(parametroBuscar, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Buscar Impuestos y Retenciones predeterminados"));
			e.printStackTrace();

		}

		for (Parametro parametro : parametroDimms) {
			Integer dimmId;

			ProdDimm prodDimm = new ProdDimm();
			Producto producto = new Producto();
			Dimm dimm = new Dimm();

			dimmId = Util.convertirStringANumero(parametro.getDescri());

			producto.setProductoId(productoId);
			dimm.setDimmId(dimmId);

			prodDimm.setProducto(producto);
			prodDimm.setDimm(dimm);

			try {
				prodDimmRegis.insertar(prodDimm);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al grabar Impuestos - Retenciones"));
				e.printStackTrace();
			}

		}
	}

	public void insertarProdCost(Integer productoId, Sucursal sucursal) {

		Producto producto = new Producto();

		producto.setProductoId(productoId);

		ProdCost prodCost = new ProdCost();
		prodCost.setProducto(producto);
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

	public void insertarProdBode(Integer productoId, Sucursal sucursal) {

		Producto producto = new Producto();
		Bodega bodega = new Bodega();

		producto.setProductoId(productoId);

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

			ProdBode prodBode = new ProdBode();
			prodBode.setProducto(producto);
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
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al grabar Bodegas - " + bodegaInsertar.getDescri()));
				e.printStackTrace();
			}
		}
	}

	public String modificar() {
		return "registra?faces-redirect=true&productoId=" + this.getId();
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}

		// this.prepararImagen();

		this.buscarProdTipos();
		this.buscarProdGrupos();

		if (this.id == null) {
			this.producto.setEstado(true);
			this.producto.setFechaCrea(LocalDate.now());
		} else {

			try {
				this.producto = productoRegis.buscarPorId(Producto.class, this.getId());

//					Se deberia hacer asi para poder hacer el codigo reutilizable
				ProdSubp ProdSubpBuscar = new ProdSubp();
				ProdSubpBuscar.setProducto(this.producto);
				this.prodSubps = this.buscarProdSubps(ProdSubpBuscar);

//					Hace lo mismo de arriba pero envia como parametro un producto
//					no es buena practica ya que el por ejemplo el codigo de la sucursal
//					se pone abajo y con eso no se podria hacer reutilizable el codigo
				this.buscarProdBode(producto);
				this.buscarProdCosts(producto);
				this.buscarProdDimms(producto);
				this.filtrarProdDimms(prodDimms);
				this.buscarProdPrecs(producto);
				this.buscarProdComps(producto);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
				e.printStackTrace();
			}
		}
	}

	public String subirImagen() {

		String nombreArchivo;
		String rutaImagen;

		Parametro parametro = new Parametro();

		byte[] bytes = null;
		BufferedOutputStream bufferedOutputStream = null;
		UploadedFile uploadedFile = getUploadedFile();

		parametro = this.buscarRutaImagenInventario();

		if (parametro == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepción - Error al buscar ruta imagenes inventario"));
			return null;
		}

		rutaImagen = parametro.getDescri();

		if (uploadedFile != null) {

			nombreArchivo = this.id.toString();
			bytes = uploadedFile.getContent();

			try {

				bufferedOutputStream = new BufferedOutputStream(
						new FileOutputStream(new File(rutaImagen + nombreArchivo + ".png")));
				bufferedOutputStream.write(bytes);
				bufferedOutputStream.close();

			} catch (FileNotFoundException e) {

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error crear Stream"));
				e.printStackTrace();

			} catch (IOException e) {

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al escribir archivo"));
				e.printStackTrace();

			}

		}

		if (this.getUploadedFile() != null) {

			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Archivo cargado:  " + this.getUploadedFile().getFileName()));
		}

		return "lista?faces-redirect=true";
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	public List<ProdTipo> getProdTipos() {
		return prodTipos;
	}

	public void setProdTipos(List<ProdTipo> prodTipos) {
		this.prodTipos = prodTipos;
	}

	public List<ProdGrup> getProdGrups() {
		return prodGrups;
	}

	public void setProdGrups(List<ProdGrup> prodGrups) {
		this.prodGrups = prodGrups;
	}

	public List<ProdCost> getProdCosts() {
		return prodCosts;
	}

	public void setProdCosts(List<ProdCost> prodCosts) {
		this.prodCosts = prodCosts;
	}

	public List<ProdPrec> getProdPrecs() {
		return prodPrecs;
	}

	public List<ProdDimm> getProdDimmIvas() {
		return prodDimmIvas;
	}

	public void setProdDimmIvas(List<ProdDimm> prodDimmIvas) {
		this.prodDimmIvas = prodDimmIvas;
	}

	public void setProdPrecs(List<ProdPrec> prodPrecs) {
		this.prodPrecs = prodPrecs;
	}

	public List<ProdBode> getProdBodes() {
		return prodBodes;
	}

	public void setProdBodes(List<ProdBode> prodBodes) {
		this.prodBodes = prodBodes;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public List<ProdComp> getProdComps() {
		return prodComps;
	}

	public void setProdComps(List<ProdComp> prodComps) {
		this.prodComps = prodComps;
	}

	public List<Parametro> getParametros() {
		return parametros;
	}

	public void setParametros(List<Parametro> parametros) {
		this.parametros = parametros;
	}

	public List<ProdDimm> getProdDimms() {
		return prodDimms;
	}

	public void setProdDimms(List<ProdDimm> prodDimms) {
		this.prodDimms = prodDimms;
	}

	public List<ProdDimm> getProdDimmReteRents() {
		return prodDimmReteRents;
	}

	public void setProdDimmReteRents(List<ProdDimm> prodDimmReteRents) {
		this.prodDimmReteRents = prodDimmReteRents;
	}

	public List<ProdDimm> getProdDimmReteIvas() {
		return prodDimmReteIvas;
	}

	public void setProdDimmReteIvas(List<ProdDimm> prodDimmReteIvas) {
		this.prodDimmReteIvas = prodDimmReteIvas;
	}

	public List<ProdSubp> getProdSubps() {
		return prodSubps;
	}

	public void setProdSubps(List<ProdSubp> prodSubps) {
		this.prodSubps = prodSubps;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}