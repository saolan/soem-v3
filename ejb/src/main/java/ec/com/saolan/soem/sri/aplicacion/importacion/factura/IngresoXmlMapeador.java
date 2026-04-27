package ec.com.saolan.soem.sri.aplicacion.importacion.factura;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import ec.com.saolan.soem.compartido.excepcion.InfraestructuraExcepcion;
import ec.com.saolan.soem.compartido.excepcion.IntegracionExcepcion;
import ec.com.saolan.soem.compartido.excepcion.ValidacionNegocioExcepcion;
import ec.com.saolan.soem.sri.infraestructura.importacion.ImportarDocumeElecSriParametros;
import ec.com.tecnointel.soem.documeElec.modelo.InfoTributaria;
import ec.com.tecnointel.soem.documeElec.modelo.factura.Factura;
import ec.com.tecnointel.soem.documeElec.modelo.factura.Factura.Detalles.Detalle;
import ec.com.tecnointel.soem.ingreso.listaInt.PersProvDimmListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.PersProvListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.ProvGrupListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDeta;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDetaImpu;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDetaPrec;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDimm;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.modelo.PersProv;
import ec.com.tecnointel.soem.ingreso.modelo.PersProvDimm;
import ec.com.tecnointel.soem.ingreso.modelo.ProvGrup;
import ec.com.tecnointel.soem.ingreso.registroInt.PersProvRegisInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdCostListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdDimmListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdPrecListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdCost;
import ec.com.tecnointel.soem.inventario.modelo.ProdDimm;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrup;
import ec.com.tecnointel.soem.inventario.modelo.ProdPrec;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.inventario.registroInt.ProductoRegisInt;
import ec.com.tecnointel.soem.parametro.modelo.Bodega;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.modelo.Precio;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.listaInt.RolPrecListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import ec.com.tecnointel.soem.serWebClientSri.general.AutorizacionDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;

@ApplicationScoped
public class IngresoXmlMapeador implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(IngresoXmlMapeador.class.getName());
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	@Inject
	PersProvListaInt persProvLista;

	@Inject
	ProvGrupListaInt provGrupLista;

	@Inject
	PersProvDimmListaInt persProvDimmLista;

	@Inject
	RolPrecListaInt rolPrecLista;

	@Inject
	ProdPrecListaInt prodPrecLista;

	@Inject
	ProdDimmListaInt prodDimmLista;

	@Inject
	ProductoRegisInt productoRegis;

	@Inject
	ProdCostListaInt prodCostLista;

	@Inject
	PersProvRegisInt persProvRegis;

	/**
	 * Convierte una factura SRI en una entidad Ingreso. Esta clase concentra toda
	 * la lógica reutilizable de mapeo.
	 */
	public Ingreso mapearFactura(Factura factura, AutorizacionDTO autorizacionDTO,
			ImportarDocumeElecSriParametros importarDocumeElecSriParametros) {

		Ingreso ingreso = new Ingreso();

		try {
			ingreso = mapearCabecera(factura, importarDocumeElecSriParametros);
		} catch (ValidacionNegocioExcepcion e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			throw e;
		} catch (InfraestructuraExcepcion | IntegracionExcepcion e) {
//			Se loguea donde el error nace, No en cada capa por donde pasa
//			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error al importar datos en cabecera de factura", e);
			throw new IntegracionExcepcion("Error al importar datos en cabecera de factura", e);
		}

		try {
			ingreso.setIngrDetas(mapearIngrDetas(factura, ingreso, importarDocumeElecSriParametros));
		} catch (ValidacionNegocioExcepcion e) {
			LOGGER.log(Level.WARNING, e.getMessage());
			throw e;
		} catch (InfraestructuraExcepcion | IntegracionExcepcion e) {
//			Se loguea donde el error nace, No en cada capa por donde pasa
//			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error al importar datos en detalle de factura", e);
			throw new IntegracionExcepcion("Error al importar datos en detalle de factura", e);
		}

		if (ingreso.getPersProv().getPersonaId() == null) {
			insertarPersProv(ingreso.getPersProv());
		}

		return ingreso;
	}

	private Ingreso mapearCabecera(Factura factura, ImportarDocumeElecSriParametros importarDocumeElecSriParametros) {

		Ingreso ingreso = new Ingreso();
		ingreso.setSucursal(importarDocumeElecSriParametros.getSucursal());
		ingreso.setBodega(new Bodega(1));
		ingreso.setFechaEmis(LocalDate.parse(factura.getInfoFactura().getFechaEmision(), DATE_TIME_FORMATTER));
		ingreso.setFechaRegi(LocalDate.now());
		ingreso.setFechaHoraEmis(ingreso.getFechaEmis().atTime(LocalTime.now()));
		ingreso.setFechaHoraRegi(LocalDateTime.now());
		ingreso.setSerie1(factura.getInfoTributaria().getEstab());
		ingreso.setSerie2(factura.getInfoTributaria().getPtoEmi());
		ingreso.setNumero(Integer.valueOf(factura.getInfoTributaria().getSecuencial()));
		ingreso.setClaveAcce(factura.getInfoTributaria().getClaveAcceso());
		ingreso.setAutori(factura.getInfoTributaria().getClaveAcceso());
//		No se puede hacer esta asignacion porque el descuento viene en valor no en porventaje
//		ingreso.setDescue(factura.getInfoFactura().getTotalDescuento());
//		se coloca 0 para que el usuario ingrese el descuento de forma manual
		ingreso.setDescue(BigDecimal.ZERO);
		ingreso.setNumeroCuot((short) 1);
		ingreso.setDiasPlaz((short) 30);
		ingreso.setNumeroGuia(0);
		ingreso.setNumeroRete(0);
		ingreso.setTotal(BigDecimal.ZERO);
		ingreso.setNota("Compra");
		ingreso.setEstado("GR");
		ingreso.setEstadoDocuElec("NO ENVIADO");

//		Estos dos campos vienen desde el controlador		
//		ingreso.setDocuIngr(getDocuIngr());
//		ingreso.setDimm(getDimm());

		PersProv persProv = new PersProv();
		try {
			persProv = cargarPersProv(factura.getInfoTributaria(), importarDocumeElecSriParametros.getCorreo());
			ingreso.setPersProv(persProv);
		} catch (InfraestructuraExcepcion e) {
			throw e;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error al cargar proveedor o crear proveedor", e);
			throw new InfraestructuraExcepcion("Error al cargar proveedor o crear proveedor", e);
		}

		try {
			ingreso.setIngrDimms(crearIngrDimm(ingreso));
		} catch (InfraestructuraExcepcion e) {
			throw e;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error al cargar impuestos y retenciones del proveedor", e);
			throw new InfraestructuraExcepcion("Error al cargar impuestos y retenciones del proveedor", e);
		}

		return ingreso;
	}

	private Set<IngrDeta> mapearIngrDetas(Factura factura, Ingreso ingreso,
			ImportarDocumeElecSriParametros importarDocumeElecSriParametros) {

		RolPrec rolPrecPredet = buscarRolPrecPredet(ingreso, importarDocumeElecSriParametros);

		validarProductosNoRegistrados(ingreso, rolPrecPredet, factura);

		List<IngrDeta> ingrDetas = cargarIngrDetas(ingreso, rolPrecPredet, factura);

		for (IngrDeta ingrDeta : ingrDetas) {

			ingrDeta.setIngrDetaPrecs(
					crearIngrDetaPrec(ingrDeta, ingreso, importarDocumeElecSriParametros.getPersUsua()));
			ingrDeta.setIngrDetaImpus(crearIngrDetaImpuRete(ingrDeta));
			ingrDeta.getProducto().setProdCost(buscarProdCost(ingreso, ingrDeta));
			ingrDeta.setIngreso(ingreso);
		}

		return new HashSet<>(ingrDetas);
	}

	public ProdCost buscarProdCost(Ingreso ingreso, IngrDeta ingrDeta) {

		ProdCost prodCost = new ProdCost();

		List<ProdCost> prodCosts = new ArrayList<ProdCost>();

//		Asignar el costo a la lista de productos
		ProdCost prodCostFiltro = new ProdCost();
		prodCostFiltro.setSucursal(ingreso.getSucursal());
		prodCostFiltro.setProducto(ingrDeta.getProducto());

//		Asigna el costo a cada elemento de la lista de prodPrec
		try {
			prodCosts = prodCostLista.buscar(prodCostFiltro, null);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error al buscar costos de productos", e);
			throw new InfraestructuraExcepcion("Error al buscar costos de productos", e);
		}

		for (ProdCost prodCostRecorrer : prodCosts) {
			prodCost = prodCostRecorrer;
		}

		return prodCost;
	}

//	Buscar en RolPrec el precio predeterminado que tiene el rol para crear ingrDeta
	public RolPrec buscarRolPrecPredet(Ingreso ingreso,
			ImportarDocumeElecSriParametros importarDocumeElecSriParametros) {

		RolPrec rolPrecPredet = new RolPrec();

		RolPrec rolPrec = new RolPrec(new Precio(), null, ingreso.getSucursal(), null, null);
		Set<RolPersUsua> rolPersUsuas = importarDocumeElecSriParametros.getPersUsua().getRolPersUsuas();

		try {
			rolPrecPredet = rolPrecLista.buscarPrecioPred(rolPrec, rolPersUsuas);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error al buscar precio predeterminado", e);
			throw new InfraestructuraExcepcion("Error al buscar precio predeterminado", e);
		}

		return rolPrecPredet;
	}

	private void validarProductosNoRegistrados(Ingreso ingreso, RolPrec rolPrecPrec, Factura factura) {

		List<IngrDeta> ingrDetas = revisarIngrDetas(ingreso, rolPrecPrec, factura);

		if (!ingrDetas.isEmpty()) {
			LOGGER.log(Level.WARNING, "Factura importada con productos no registrados. Cantidad: {0}",
					ingrDetas.size());
			throw new ValidacionNegocioExcepcion("Existen " + ingrDetas.size()
					+ " productos no registrados. Cree los productos nuevos y cargue nuevamente el documento.");
		}
	}

	public PersProv cargarPersProv(InfoTributaria infoTributaria, String correo) {

		PersProv persProvFiltro = new PersProv(new Persona(infoTributaria.getRuc(), null, null, true), true);
		PersProv persProv = new PersProv(new Persona(), true);

		List<PersProv> persProvs = new ArrayList<PersProv>();

		persProvLista.filasPagina(1);
		try {
			persProvs = persProvLista.buscar(persProvFiltro, null);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error al buscar proveedores", e);
			throw new InfraestructuraExcepcion("Error al buscar proveedores", e);
		}

		if (persProvs.isEmpty()) {
			persProv = crearPersProv(infoTributaria, correo);
		} else {
			persProv = persProvs.get(0);
		}

		return persProv;
	}

	public PersProv crearPersProv(InfoTributaria infoTributaria, String correo) {

		PersProv persProv = new PersProv(
				new Persona(infoTributaria.getRuc(), infoTributaria.getRazonSocial(), null, true), true);

		persProv.getPersona().setDirecc(infoTributaria.getDirMatriz());
		persProv.setProvGrup(buscarProvGrup());
		persProv.setDimm(seleccionarDimm(infoTributaria.getRuc()));
		persProv.getPersona().setCorreo(correo);
		persProv.setAutori("0");
		persProv.setFechaAuto(LocalDate.now());
		persProv.setExonerIva(false);
		persProv.setRetienRent(false);
		persProv.setRetienIva(false);
		persProv.setNatura("01");
		persProv.setParteRela(false);

		return persProv;
	}

	public ProvGrup buscarProvGrup() {

		ProvGrup provGrup = new ProvGrup();
		provGrup.setEstado(true);

		List<ProvGrup> provGrups = new ArrayList<>();

		try {
			provGrups = provGrupLista.buscar(provGrup, null);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error al buscar grupos de proveedores", e);
			throw new InfraestructuraExcepcion("Error al buscar grupos de proveedores", e);
		}

		if (provGrups.isEmpty()) {
			throw new InfraestructuraExcepcion("No existen grupos de proveedores");
		}

		return provGrups.get(0);
	}

	public Dimm seleccionarDimm(String cedulaRuc) {

		Dimm dimm = new Dimm();

		if (cedulaRuc.length() == 10) {
			dimm.setDimmId(2010);
		} else {
			dimm.setDimmId(2000);
		}

		return dimm;
	}

	public Set<IngrDimm> crearIngrDimm(Ingreso ingreso) {

		try {

			Set<IngrDimm> ingrDimms = new HashSet<>();

			PersProvDimm persProvDimmFiltro = new PersProvDimm();
			persProvDimmFiltro.setPersProv(ingreso.getPersProv());
			persProvDimmFiltro.setDimm(new Dimm());
			List<PersProvDimm> persProvDimms = persProvDimmLista.buscar(persProvDimmFiltro, null);

			for (PersProvDimm persProvDimm : persProvDimms) {

				IngrDimm ingrDimm = new IngrDimm();

				ingrDimm.setIngreso(ingreso);
				ingrDimm.setDimm(persProvDimm.getDimm());
				ingrDimm.setTipo(persProvDimm.getTipo());

				ingrDimms.add(ingrDimm);
			}
			return ingrDimms;

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error al buscar impuestos y retenciones del proveedor - PersProvDimm", e);
			throw new InfraestructuraExcepcion("Error al buscar impuestos y retenciones del proveedor - PersProvDimm",
					e);
		}
	}

//	Revisa si todos los productos del documento consten en el sistema
//	La lista que devuelve es la de los productos que no existen en el sistema
	public List<IngrDeta> revisarIngrDetas(Ingreso ingreso, RolPrec rolPrecPred, Factura factura) {

		List<IngrDeta> ingrDetas = new ArrayList<>();

		for (Detalle detalle : factura.getDetalles().getDetalle()) {

			ProdGrup prodGrup = new ProdGrup(null, "Todo", true, false, false, false, true);
			Producto producto = new Producto(prodGrup, null, detalle.getCodigoPrincipal(), null, true);
			ProdPrec prodPrecFiltro = new ProdPrec(ingreso.getSucursal(), rolPrecPred.getPrecio(), producto);
			List<ProdPrec> prodPrecs = buscarProdPrecs(prodPrecFiltro);

			if (prodPrecs.isEmpty()) {

				ProdPrec prodPrecNuevo = new ProdPrec(null, null, producto);
//				Se coloca el codigo de barra ya que la busqueda se hace por codigo
//				y por lo tanto como se esta creando un nuevo prodPrec no tiene esta valor
				prodPrecNuevo.getProducto().setCodigoBarra(detalle.getCodigoPrincipal());

				IngrDeta ingrDeta = new IngrDeta(ingreso, prodPrecNuevo.getProducto(), prodPrecNuevo.getPrecio(),
						ingreso.getFechaRegi(), ingreso.getFechaEmis().atTime(LocalTime.now()), detalle.getCantidad(),
						prodPrecNuevo.getFactor(), detalle.getPrecioUnitario(), detalle.getPrecioUnitario(),
						BigDecimal.ZERO, detalle.getDescuento(), BigDecimal.ZERO, prodPrecNuevo.getPrecioConImpu());
				ingrDetas.add(ingrDeta);
			}
		}

		return ingrDetas;
	}

	public List<IngrDeta> cargarIngrDetas(Ingreso ingreso, RolPrec rolPrecPred, Factura factura) {

		List<IngrDeta> ingrDetas = new ArrayList<>();

		for (Detalle detalle : factura.getDetalles().getDetalle()) {

			ProdGrup prodGrup = new ProdGrup(null, "Todo", true, false, false, false, true);
			Producto producto = new Producto(prodGrup, null, detalle.getCodigoPrincipal(), null, true);
			ProdPrec prodPrecFiltro = new ProdPrec(ingreso.getSucursal(), rolPrecPred.getPrecio(), producto);
			List<ProdPrec> prodPrecs = buscarProdPrecs(prodPrecFiltro);

			if (!prodPrecs.isEmpty()) {
//				Cuando se busca el código del producto puede ser que haya mas de uno, 
//				ya que la busqueda se hace por codigo no por codigo de barra y el codigo admite duplicados
//				entonces varios productos pueden tener el mismo codigo.  
//				Se recorre la lista de productos y se añaden todos a la lista, el usuario tendra que eliminar
//				los productos que no pertenezcan al documento
				for (ProdPrec prodPrec : prodPrecs) {

//					Controlar que pasa su el producto tiene codigos duplicados
//					if (prodPrecs.size() > 1) {}

					BigDecimal porcentajeDescuento = calcularIngrDetaDescue(detalle);
					BigDecimal costoNeto = calcularIngrDetaCostoNeto(detalle);

					IngrDeta ingrDeta = new IngrDeta(ingreso, prodPrec.getProducto(), prodPrec.getPrecio(),
							ingreso.getFechaRegi(), ingreso.getFechaEmis().atTime(LocalTime.now()),
							detalle.getCantidad(), prodPrec.getFactor(), detalle.getPrecioUnitario(), costoNeto,
							BigDecimal.ZERO, porcentajeDescuento, BigDecimal.ZERO, prodPrec.getPrecioConImpu());

					ingrDetas.add(ingrDeta);
				}
			}
		}
		return ingrDetas;
	}

	public static BigDecimal calcularIngrDetaDescue(Detalle detalle) {

		if (detalle.getCantidad().signum() <= 0 || detalle.getPrecioUnitario().signum() <= 0
				|| detalle.getDescuento().signum() <= 0) {
			return BigDecimal.ZERO;
		}

		BigDecimal subtotal = detalle.getCantidad().multiply(detalle.getPrecioUnitario());
		BigDecimal porcentaje = detalle.getDescuento().multiply(BigDecimal.valueOf(100)).divide(subtotal, 2,
				RoundingMode.HALF_UP);
		return porcentaje;
	}

	public static BigDecimal calcularIngrDetaCostoNeto(Detalle detalle) {

		if (detalle.getCantidad().signum() <= 0) {
			return detalle.getPrecioUnitario();
		}

		BigDecimal subtotal = detalle.getCantidad().multiply(detalle.getPrecioUnitario())
				.subtract(detalle.getDescuento());

		if (subtotal.signum() <= 0) {
			return detalle.getPrecioUnitario();
		}

		return subtotal.divide(detalle.getCantidad(), 6, RoundingMode.HALF_UP);
	}

	public List<ProdPrec> buscarProdPrecs(ProdPrec prodPrec) {

		List<ProdPrec> prodPrecs = new ArrayList<>();

		try {
			prodPrecs = prodPrecLista.buscar(prodPrec, null, null);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error al buscar precios de producto", e);
			throw new InfraestructuraExcepcion("Error al buscar precios de producto", e);
		}

		return prodPrecs;
	}

	public Set<IngrDetaPrec> crearIngrDetaPrec(IngrDeta ingrDeta, Ingreso ingreso, PersUsua persUsua) {

		Set<IngrDetaPrec> ingrDetaPrecs = new HashSet<>();

		ProdGrup prodGrup = new ProdGrup(null, "Todo", true, false, false, false, true);
		Producto producto = new Producto(prodGrup, null, ingrDeta.getProducto().getCodigo(), null, true);
		ProdPrec prodPrec = new ProdPrec(ingreso.getSucursal(), new Precio(), producto);
		List<ProdPrec> prodPrecs = buscarProdPrecs(prodPrec);

		try {
			prodPrecs = prodPrecLista.filtrarProdPrec(prodPrecs, persUsua, buscarRolPrec(), ingreso.getSucursal());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error al filtrar precios de productos", e);
			throw new InfraestructuraExcepcion("Error al filtrar precios de productos", e);
		}

		for (ProdPrec prodPrecRecorrer : prodPrecs) {
			ingrDetaPrecs.add(crearIngrDetaPrecDesdeProdPrec(ingrDeta, prodPrecRecorrer));
		}

		return ingrDetaPrecs;
	}

	private IngrDetaPrec crearIngrDetaPrecDesdeProdPrec(IngrDeta ingrDeta, ProdPrec prodPrec) {

		IngrDetaPrec ingrDetaPrec = new IngrDetaPrec();
		ingrDetaPrec.setIngrDeta(ingrDeta);
		ingrDetaPrec.setPrecio(prodPrec.getPrecio());
		ingrDetaPrec.setFactor(prodPrec.getFactor());
		ingrDetaPrec.setPrecioConImpu(prodPrec.getPrecioConImpu());
		ingrDetaPrec.setPrecioSinImpu(prodPrec.getPrecioSinImpu());
		ingrDetaPrec.setUtilid(prodPrec.getUtilid());
		return ingrDetaPrec;
	}

	public List<RolPrec> buscarRolPrec() {

		List<RolPrec> rolPrecs = new ArrayList<RolPrec>();

		RolPrec rolPrecFiltro = new RolPrec(new Precio(), new Rol(), new Sucursal(), null, true);

		try {
			rolPrecs = rolPrecLista.buscar(rolPrecFiltro, null);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error al buscar precios del rol", e);
			throw new InfraestructuraExcepcion("Error al buscar precios del rol", e);
		}
		return rolPrecs;
	}

	private Set<IngrDetaImpu> crearIngrDetaImpuRete(IngrDeta ingrDeta) {

		Set<IngrDetaImpu> ingrDetaImpus = new HashSet<>();
		ingrDetaImpus = this.crearImpuestos("IMPUE", ingrDeta);

//		Buscar Iva del producto, si tiene iva se crea ingrDetaImpu sino no se crea ingrDetaImpu
		ProdDimm prodDimmBuscar = new ProdDimm();
		prodDimmBuscar.setDimm(new Dimm());
		prodDimmBuscar.setProducto(ingrDeta.getProducto());

		List<ProdDimm> prodDimms = new ArrayList<ProdDimm>();
		try {
			prodDimms = prodDimmLista.buscar(prodDimmBuscar, null);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error al crear impuestos de productos - ProdDimm", e);
			throw new InfraestructuraExcepcion("Error al crear impuestos de productos - ProdDimm", e);
		}

		ProdDimm prodDimmIva = new ProdDimm();

		for (ProdDimm prodDimmRecorrer : prodDimms) {
			if (prodDimmRecorrer.getDimm().getTablaRefe().equals("Tabla12")) {
				prodDimmIva = prodDimmRecorrer;
			}
		}

//		TODO: arreglar esto no graba IngrDimm pero si ingrDetaImpu y calcula retenciones
		try {

//			Buscar el Producto para relacionar con prodGrup y poder saber si es Mercaderia o Gasto-Servicio
			Producto producto = productoRegis.buscarPorId(Producto.class, ingrDeta.getProducto().getProductoId());

//			Crea Retenciones dependiendo de si los productos son Mercaderia o Gasto-Servicio
//			y de acuerdo a eso selecciona la lista de impuesto que va a crear
			if (producto.getProdGrup().getTipo().equals("Mercaderia")) {

				for (IngrDimm ingrDimm : ingrDeta.getIngreso().getIngrDimms()) {

					if (ingrDimm.getTipo().equals("RENTA_BIEN")) {
						ingrDetaImpus.add(this.crearRetenciones(ingrDeta, ingrDimm));
					} else if (ingrDimm.getTipo().equals("IVA_BIEN")) {
						if (prodDimmIva.getDimm().getPorcen().compareTo(BigDecimal.ZERO) > 0) {
							ingrDetaImpus.add(this.crearRetenciones(ingrDeta, ingrDimm));
						}
					}
				}

			} else {
//				El producto es un gasto
				for (IngrDimm ingrDimm : ingrDeta.getIngreso().getIngrDimms()) {

					if (ingrDimm.getTipo().equals("RENTA_SERV")) {
						ingrDetaImpus.add(this.crearRetenciones(ingrDeta, ingrDimm));
					} else if (ingrDimm.getTipo().equals("IVA_SERV")) {
						if (prodDimmIva.getDimm().getPorcen().compareTo(BigDecimal.ZERO) > 0) {
							ingrDetaImpus.add(this.crearRetenciones(ingrDeta, ingrDimm));
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error al buscar ID del producto", e);
			throw new InfraestructuraExcepcion("Error al buscar ID del producto", e);
		}

		return ingrDetaImpus;
	}

	public Set<IngrDetaImpu> crearImpuestos(String ingrDetaTipoImpu, IngrDeta ingrDeta) {

		Set<IngrDetaImpu> ingrDetaImpus = new HashSet<>();

		List<ProdDimm> prodDimms = new ArrayList<ProdDimm>();

		ProdDimm prodDimmFiltro = new ProdDimm();
		prodDimmFiltro.setDimm(new Dimm());
		prodDimmFiltro.setProducto(ingrDeta.getProducto());

		try {
			prodDimms = prodDimmLista.buscar(prodDimmFiltro, null);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error al buscar impuestos del producto - ProdDimm", e);
			throw new InfraestructuraExcepcion("Error al buscar impuestos del producto - ProdDimm", e);
		}

		for (ProdDimm prodDimm : prodDimms) {

			IngrDetaImpu ingrDetaImpu = this.crearIngrDetaImpuDesdeDimm(ingrDeta, prodDimm.getDimm());

			if (ingrDetaTipoImpu.equals("IMPUE")) {

				if (prodDimm.getDimm().getDimmId() >= 13000 && prodDimm.getDimm().getDimmId() <= 13099) {
					ingrDetaImpu.setTipo("IVA");
				} else if (prodDimm.getDimm().getDimmId() >= 13100 && prodDimm.getDimm().getDimmId() <= 13199) {
					ingrDetaImpu.setTipo("OTR");
				} else if (prodDimm.getDimm().getDimmId() >= 11000 && prodDimm.getDimm().getDimmId() <= 11100) {
//					El ultimo código 11100 es el ice de fundas plasticas
					ingrDetaImpu.setTipo("ICE");
				} else if (prodDimm.getDimm().getDimmId() >= 11800 && prodDimm.getDimm().getDimmId() <= 11900) {
					ingrDetaImpu.setTipo("IRBPNR");
				}
			}

			if (ingrDetaImpu.getTipo() != null) {
				ingrDetaImpus.add(ingrDetaImpu);
			}
		}

		return ingrDetaImpus;
	}

	public IngrDetaImpu crearIngrDetaImpuDesdeDimm(IngrDeta ingrDeta, Dimm dimm) {

		IngrDetaImpu ingrDetaImpu = new IngrDetaImpu();
		ingrDetaImpu.setIngrDeta(ingrDeta);
		ingrDetaImpu.setDimm(dimm);
		ingrDetaImpu.setDescri(dimm.getDescri());
		ingrDetaImpu.setCodigo(dimm.getCodigo());
		ingrDetaImpu.setPorcen(dimm.getPorcen());
		ingrDetaImpu.setFactor(dimm.getFactor());
		ingrDetaImpu.setTipo(null);

		return ingrDetaImpu;
	}

	public IngrDetaImpu crearRetenciones(IngrDeta ingrDeta, IngrDimm ingrDimm) {

		IngrDetaImpu ingrDetaImpu = this.crearIngrDetaImpuDesdeDimm(ingrDeta, ingrDimm.getDimm());

		if (ingrDimm.getTipo().equals("RENTA_BIEN") || ingrDimm.getTipo().equals("RENTA_SERV")) {
			ingrDetaImpu.setTipo("RR");
		} else if (ingrDimm.getTipo().equals("IVA_BIEN") || ingrDimm.getTipo().equals("IVA_SERV")) {
			ingrDetaImpu.setTipo("RI");
		}

		return ingrDetaImpu;
	}

	public Integer insertarPersProv(PersProv persProv) {
		Object id = null;
		try {
			id = persProvRegis.insertar(persProv);
		} catch (ConstraintViolationException e) {
			LOGGER.log(Level.SEVERE, "Error al grabar proveedor, verifique datos duplicados o restricciones", e);
			throw new InfraestructuraExcepcion("Error al grabar proveedor, verifique datos duplicados o restricciones",
					e);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error inesperado al grabar proveedor", e);
			throw new InfraestructuraExcepcion("Error inesperado al grabar proveedor", e);
		}

		return (Integer) id;
	}
}