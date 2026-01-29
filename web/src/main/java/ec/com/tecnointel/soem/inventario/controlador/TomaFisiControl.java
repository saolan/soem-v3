package ec.com.tecnointel.soem.inventario.controlador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDeta;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDetaPrec;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.modelo.PersProv;
import ec.com.tecnointel.soem.ingreso.registroInt.IngrDetaRegisInt;
import ec.com.tecnointel.soem.ingreso.registroInt.IngresoRegisInt;
import ec.com.tecnointel.soem.ingreso.registroInt.PersProvRegisInt;
import ec.com.tecnointel.soem.inventario.listaInt.KardTotaViewListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdBodeListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdCostListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdPrecListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.TomaFisiDetaListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.TomaFisiListaInt;
import ec.com.tecnointel.soem.inventario.modelo.KardTotaView;
import ec.com.tecnointel.soem.inventario.modelo.ProdBode;
import ec.com.tecnointel.soem.inventario.modelo.ProdCost;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrup;
import ec.com.tecnointel.soem.inventario.modelo.ProdPrec;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.inventario.modelo.TomaFisi;
import ec.com.tecnointel.soem.inventario.modelo.TomaFisiDeta;
import ec.com.tecnointel.soem.inventario.registroInt.ProductoRegisInt;
import ec.com.tecnointel.soem.inventario.registroInt.TomaFisiDetaRegisInt;
import ec.com.tecnointel.soem.inventario.registroInt.TomaFisiRegisInt;
import ec.com.tecnointel.soem.parametro.listaInt.BodegaListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.DocuIngrListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.SucursalListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Bodega;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.DocuIngr;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Precio;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.registroInt.DocumentoRegisInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolPrecListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolDocu;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class TomaFisiControl extends PaginaControl implements Serializable {

	private Integer productoId;
	private int numeroRegProd;
	private long contadorRegProd;
	private Integer paginaProd;
	private int numeroTomaFisiDeta;
	private long contadorTomaFisiDeta;
	private Integer paginaTomaFisiDeta;
	private String tipo;
	private String notaIngreso;
	private String notaEgreso;
	
	private String ordenColumna;

	private TomaFisi tomaFisi;
	private TomaFisiDeta tomaFisiDeta;
	private TomaFisiDeta tomaFisiDetaSele;
	private ProdPrec prodPrec;
	private ProdPrec prodPrecSele;
	private Precio precioPred;
	private DocuIngr docuIngrIngreso;
	private DocuIngr docuIngrEgreso;
	private PersUsua persUsuaSesion;
	private Ingreso ingreso;
	private Ingreso egreso;

	private List<String> tipos;
	private List<Sucursal> sucursals;
	private List<Bodega> bodegas;
	private List<ProdPrec> prodPrecs;
	private List<TomaFisi> tomaFisis;
	private List<TomaFisiDeta> tomaFisiDetas;
	private List<TomaFisiDeta> tomaFisiDetaEliminados;
	private List<Parametro> parametroEstados;
	private List<DocuIngr> docuIngrIngresos;
	private List<DocuIngr> docuIngrEgresos;
	private List<IngrDeta> ingrDetaIngresos;
	private List<IngrDeta> ingrDetaEgresos;

	@Inject
	TomaFisiRegisInt tomaFisiRegis;

	@Inject
	TomaFisiListaInt tomaFisiLista;

	@Inject
	TomaFisiDetaRegisInt tomaFisiDetaRegis;

	@Inject
	TomaFisiDetaListaInt tomaFisiDetaLista;

	@Inject
	SucursalListaInt sucursalLista;

	@Inject
	BodegaListaInt bodegaLista;

	@Inject
	ProdPrecListaInt prodPrecLista;

	@Inject
	ProdCostListaInt prodCostLista;

	@Inject
	ProdBodeListaInt prodBodeLista;

	@Inject
	RolPrecListaInt rolPrecLista;

	@Inject
	KardTotaViewListaInt kardTotaViewLista;

	@Inject
	DocuIngrListaInt docuIngrLista;

	@Inject
	PersProvRegisInt persProvRegis;

	@Inject
	IngresoRegisInt ingresoRegis;

	@Inject
	IngrDetaRegisInt ingrDetaRegis;

	@Inject
	DocumentoRegisInt documentoRegis;
	
	@Inject
	ProductoRegisInt productoRegis;

	private static final long serialVersionUID = -4527085267992432096L;

	@PostConstruct
	public void cargar() {

		this.persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");

		this.tomaFisi = new TomaFisi();
		this.tomaFisiDeta = new TomaFisiDeta();

		ProdGrup prodGrup = new ProdGrup();
		prodGrup.setTipo("Mercaderia");

		Producto producto = new Producto();
		producto.setProdGrup(prodGrup);

		this.prodPrec = new ProdPrec();
		this.prodPrec.setProducto(producto);

		this.ingrDetaIngresos = new ArrayList<IngrDeta>();
		this.ingrDetaEgresos = new ArrayList<IngrDeta>();
		this.tomaFisis = new ArrayList<TomaFisi>();
		this.tomaFisiDetas = new ArrayList<TomaFisiDeta>();
		this.tomaFisiDetaEliminados = new ArrayList<TomaFisiDeta>();
		this.sucursals = new ArrayList<Sucursal>();
		this.bodegas = new ArrayList<Bodega>();
		this.prodPrecs = new ArrayList<ProdPrec>();
		this.parametroEstados = new ArrayList<Parametro>();
		this.docuIngrIngresos = new ArrayList<DocuIngr>();
		this.docuIngrEgresos = new ArrayList<DocuIngr>();

		Sucursal sucursalBuscar = new Sucursal();
		this.sucursals = this.buscarSucursals(sucursalBuscar);

		Bodega bodegaBuscar = new Bodega();
		bodegaBuscar.setEstado(true);
		this.bodegas = this.buscarBodegas(bodegaBuscar);

		Parametro parametroBuscar = new Parametro();
		parametroBuscar.setCodigo("Parametro-Estado");
		parametroBuscar.setEstado(true);
		this.parametroEstados = this.buscarParametros(parametroBuscar);

		this.tipos = new ArrayList<String>();
		this.tipos.add("Parcial");
		this.tipos.add("Total");

//		this.tomaFisi.setFechaRegi(new Date());
		this.tomaFisi.setFechaRegi(LocalDate.now());
		
		this.tomaFisi.setEstado("GR");
		
		if (variablesSesion.isActivarImagen()) {
			ordenColumna = "codigo";
		} else {
			ordenColumna = "descri";
		}
	}

	public void agregarProducto() {
	
			TomaFisiDeta tomaFisiDeta = new TomaFisiDeta();
	
			TomaFisiDeta tomaFisiDetaBuscar = new TomaFisiDeta();
			TomaFisi tomaFisiBuscar = new TomaFisi();
	
			tomaFisiBuscar.setSucursal(this.tomaFisi.getSucursal());
			tomaFisiBuscar.setNumero(this.tomaFisi.getNumero());
	
			tomaFisiDetaBuscar.setTomaFisi(tomaFisiBuscar);
			tomaFisiDetaBuscar.setProducto(prodPrecSele.getProducto());
	
			List<TomaFisiDeta> tomaFisiDetas = this.buscarTomaFisiDetas(tomaFisiDetaBuscar);
	
			if (tomaFisiDetas.size() != 0) {
				
				for (TomaFisiDeta tomaFisiDetaInfo : tomaFisiDetas) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Ya se ha ingresado este producto en el documento con número de control " + tomaFisiDetaInfo.getTomaFisi().getTomaFisiId()));
				}
				
				return;
			}
	
			this.prodPrecSele.getProducto().setProdGrup(this.getProdPrec().getProducto().getProdGrup());
			
			tomaFisiDeta.setTomaFisi(this.tomaFisi);
			tomaFisiDeta.setProducto(prodPrecSele.getProducto());
	//		Se le pone el costo del producto seleccionado y no del kardex porque en el kardex
	//		si la cantidad es cero el costo le va a poner cero, pero puede ser que al hacer la toma física
	//		exista algún producto entonces no sabria que costo es
			tomaFisiDeta.setCostoActu(prodPrecSele.getProducto().getProdCost().getCostoActu());
			tomaFisiDeta.setCantidActu(prodPrecSele.getProducto().getStockActual());
			tomaFisiDeta.setCostoCont(prodPrecSele.getProducto().getProdCost().getCostoActu());
	//		tomaFisiDeta.setCantidCont(prodPrecSele.getProducto().getStockActual());
			tomaFisiDeta.setCantidCont(new BigDecimal(0));
	
			this.tomaFisiDetas.add(0, tomaFisiDeta);
	
			this.grabarTomaFisiDeta(tomaFisiDeta);
		}

	public void descargar() {

		Parametro parametro = new Parametro();
		String nombreReporte = "tomaFisiFormato1";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		String rutaJrxml;
		String rutaReporteCompilado = "\\jasperReportes\\inventario\\";

		try {

			Integer tomaFisiId = this.tomaFisi.getTomaFisiId();

			if (tomaFisiId == null) {
				parametrosJasper.put("tomaFisiId", 0);
			} else {
				parametrosJasper.put("tomaFisiId", tomaFisiId);
			}

			parametro = parametroRegis.buscarPorId(Parametro.class, 5000);
			rutaJrxml = parametro.getDescri();

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper, rutaJrxml, rutaReporteCompilado,
					formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al descargar documento"));
			e.printStackTrace();
		}
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

	public List<DocuIngr> buscarDocuIngrs() {

		Documento documento = new Documento();
		DocuIngr docuIngr = new DocuIngr();

		List<DocuIngr> docuIngrs = new ArrayList<DocuIngr>();

		documento.setEstado(true);
		docuIngr.setDocumento(documento);

		try {
			docuIngrs = docuIngrLista.buscar(docuIngr, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar documentos de ajuste"));
			e.printStackTrace();
		}

		return docuIngrs;
	}

	public List<KardTotaView> buscarKardTotaViews(KardTotaView kardTotaView) {

		List<KardTotaView> kardTotaViews = new ArrayList<KardTotaView>();

		try {
			kardTotaViews = kardTotaViewLista.buscar(kardTotaView, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepción al buscar stock - KardTotalView"));
			e.printStackTrace();
		}

		return kardTotaViews;

	}

	public List<Parametro> buscarParametros(Parametro parametro) {

		List<Parametro> parametros = new ArrayList<Parametro>();
		try {
			parametros = parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepción al buscar parametro estado de documentos"));
			e.printStackTrace();
		}
		return parametros;
	}

//	public PersProv buscarPersProv() {
//	
//		PersProv persProv = new PersProv();
//				
//		try {
//			persProv = this.persProvRegis.buscarPorId(1);
//		} catch (Exception e) {
//			FacesContext.getCurrentInstance().addMessage(null,
//					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepción al buscar proveedor"));
//			e.printStackTrace();
//		}
//		
//		return persProv;
//	}

	public Precio buscarPrecioPred() {

		Precio precioPred = new Precio();
		RolPrec rolPrec = new RolPrec();

		List<RolPrec> rolPrecs = new ArrayList<>();
		List<Rol> rols = new ArrayList<>();

		Set<RolPersUsua> rolPersUsuas = persUsuaSesion.getRolPersUsuas();
		for (RolPersUsua rolPersUsua : rolPersUsuas) {
			rols.add(rolPersUsua.getRol());
		}

		for (Rol rol : rols) {
			rolPrec.setRol(rol);
			rolPrec.setPrecio(new Precio());
			rolPrec.setSucursal(this.getVariablesSesion().getSucursal());

			try {
				rolPrecs.addAll(this.rolPrecLista.buscar(rolPrec, null));
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al buscar precios de roles"));
				e.printStackTrace();
			}
		}

		for (RolPrec rolPrec2 : rolPrecs) {
			if (rolPrec2.getPredet()) {
				precioPred = rolPrec2.getPrecio();
			}
		}

		return precioPred;

	}

	public List<ProdBode> buscarProdBodes(ProdBode prodBode) {
	
		List<ProdBode> prodBodes = new ArrayList<ProdBode>();
	
		try {
			prodBodes = this.prodBodeLista.buscar(prodBode, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion al buscar producto por bodega"));
			e.printStackTrace();
		}
	
		return prodBodes;
	}

	public void buscarProdPrecs(Integer paginador) {

		if (paginador == 0) {
			this.paginaProd = 0;
		}
		if (this.prodPrec.getProducto().getCodigoBarra() != null) {
			this.prodPrec.getProducto().setCodigo(null);
			this.prodPrec.getProducto().setDescri(null);
		}

		this.prodPrec.setSucursal(this.getVariablesSesion().getSucursal());
		this.prodPrec.setPrecio(this.precioPred);
		this.prodPrec.getProducto().setEstado(true);

		try {

			this.prodPrecs = prodPrecLista.buscar(this.prodPrec, this.paginaProd, variablesSesion.getFilasPagina());
			this.numeroRegProd = prodPrecs.size();
			this.contadorRegProd = prodPrecLista.contarRegistros(this.prodPrec);

//			Asignar el costo a la lista de productos
			for (ProdPrec prodPrec : this.prodPrecs) {

				ProdCost prodCostBuscar = new ProdCost();
				List<ProdCost> prodCosts = new ArrayList<ProdCost>();

				prodCostBuscar.setSucursal(this.getVariablesSesion().getSucursal());
				prodCostBuscar.setProducto(prodPrec.getProducto());

				try {
//					Asigna el costo a cada elemento de la lista de prodPrec
					prodCosts = this.prodCostLista.buscar(prodCostBuscar, null);
					for (ProdCost prodCost : prodCosts) {
						prodPrec.getProducto().setProdCost(prodCost);
					}
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
							null, "Excepcion - Error al asignar costo en: " + prodPrec.getProducto().getDescri()));
					e.printStackTrace();
				}

				KardTotaView kardTotaView = new KardTotaView();
				kardTotaView.setSucursalId(variablesSesion.getSucursal().getSucursalId());
				kardTotaView.setBodegaId(1);
				kardTotaView.setProductoId(prodPrec.getProducto().getProductoId());

				List<KardTotaView> kardTotaViews = this.buscarKardTotaViews(kardTotaView);

				if (kardTotaViews.size() == 1) {
					prodPrec.getProducto().setStockActual(kardTotaViews.get(0).getCantidSald());
				} else if (kardTotaViews.size() == 0) {
					prodPrec.getProducto().setStockActual(new BigDecimal(0));
				}
			}

			if (this.prodPrec.getProducto().getCodigoBarra() != null && this.prodPrecs.size() == 1) {

				this.prodPrecSele = this.prodPrecs.get(0);
				this.agregarProducto();

				this.prodPrec.getProducto().setCodigoBarra(null);
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar productos"));
			e.printStackTrace();
		}

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

	public void buscarTomaFisis() {

		try {

			tomaFisiLista.filasPagina(variablesSesion.getFilasPagina());

			this.tomaFisis = tomaFisiLista.buscar(tomaFisi, this.pagina);
			this.numeroReg = this.tomaFisis.size();
			this.contadorReg = tomaFisiLista.contarRegistros(this.tomaFisi);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar toma física"));
			e.printStackTrace();
		}
	}

	public List<TomaFisiDeta> buscarTomaFisiDetas(TomaFisiDeta tomaFisiDeta) {

		List<TomaFisiDeta> tomaFisiDetas = new ArrayList<TomaFisiDeta>();

		try {
			tomaFisiDetas = tomaFisiDetaLista.buscar(tomaFisiDeta, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar detalle toma física"));
			e.printStackTrace();
		}

		return tomaFisiDetas;
	}

	public Ingreso crearIngreso(DocuIngr docuIngr) {

		Ingreso ingreso = new Ingreso();
		PersProv persProv = new PersProv();
		Dimm dimm = new Dimm();
		Bodega bodega = new Bodega();

		bodega.setBodegaId(1);
		persProv.setPersonaId(1);
		dimm.setDimmId(6050);

		ingreso.setAutori("0000000000");
		ingreso.setBodega(bodega);
		ingreso.setClaveAcce("0000000000");
		ingreso.setDescue(new BigDecimal(0));
		ingreso.setDiasPlaz((short) 1);
		ingreso.setDimm(dimm);
		ingreso.setDocuIngr(docuIngr);
		ingreso.setEstado("GR");
		ingreso.setEstadoDocuElec("NO ENVIADO");
		ingreso.setFechaEmis(LocalDate.now());
		ingreso.setFechaRegi(LocalDate.now());
		ingreso.setFechaHoraEmis(LocalDateTime.now());
		ingreso.setFechaHoraRegi(LocalDateTime.now());
		// El numero se pone al grabar el documento
		// ingreso.setNumero(numero);
		ingreso.setNumeroCuot((short) 1);
		ingreso.setNumeroGuia(0);
		ingreso.setNumeroRete(0);
		ingreso.setPersProv(persProv);
		ingreso.setSerie1("000");
		ingreso.setSerie2("000");
		ingreso.setSucursal(this.getVariablesSesion().getSucursal());
		ingreso.setTotal(new BigDecimal(0));

		return ingreso;
	}

	public void crearIngresoEgreso() {

		if (this.ingrDetaIngresos.size() != 0) {

			this.ingreso = this.crearIngreso(this.docuIngrIngreso);
		}

		if (this.ingrDetaEgresos.size() != 0) {

			this.egreso = this.crearIngreso(this.docuIngrEgreso);
		}
	}

	public IngrDeta crearIngrDeta(TomaFisiDeta tomaFisiDeta, KardTotaView kardTotaView, BigDecimal CantidAjus,
			BigDecimal factor) {

		IngrDeta ingrDeta = new IngrDeta();

		ingrDeta.setCantid(CantidAjus);
		ingrDeta.setCosto(kardTotaView.getCosto());
		ingrDeta.setCostoNeto(kardTotaView.getCosto());
		ingrDeta.setDescue(new BigDecimal(0));
		ingrDeta.setDescueValo(new BigDecimal(0));
		ingrDeta.setFactor(factor);
		ingrDeta.setFechaRegi(LocalDate.now());
		ingrDeta.setFechaHoraRegi(LocalDateTime.now());
		ingrDeta.setPrecio(this.precioPred);
		ingrDeta.setProducto(tomaFisiDeta.getProducto());
		ingrDeta.setPromoc(new BigDecimal(0));
		ingrDeta.setTotal(ingrDeta.getCantid().multiply(ingrDeta.getCostoNeto()));

		return ingrDeta;
	}

	public Set<IngrDetaPrec> crearIngrDetaPrec(IngrDeta ingrDeta) {

		ProdPrec prodPrec = new ProdPrec();
		
		ProdGrup prodGrup = new ProdGrup();
		prodGrup.setTipo("Mercaderia");
		
		ingrDeta.getProducto().setProdGrup(prodGrup);

		// List<ProdPrec> prodPrecsFiltro = new ArrayList<ProdPrec>();
		List<ProdPrec> prodPrecs = new ArrayList<ProdPrec>();
		Set<IngrDetaPrec> ingrDetaPrecs = new HashSet<IngrDetaPrec>();

		prodPrec.setSucursal(this.getVariablesSesion().getSucursal());
		prodPrec.setPrecio(new Precio());
		prodPrec.setProducto(ingrDeta.getProducto());

		prodPrecs = this.buscarProdPrecs(prodPrec, null, null, "codigo");

		try {
			prodPrecs = prodPrecLista.filtrarProdPrec(prodPrecs, persUsuaSesion, variablesSesion.getRolPrecs(), variablesSesion.getSucursal());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al filtrar precios de productos"));
			e.printStackTrace();
		}

		for (ProdPrec prodPrecCopiar : prodPrecs) {

			IngrDetaPrec ingrDetaPrec = new IngrDetaPrec();
			ingrDetaPrec.setIngrDeta(ingrDeta);
			ingrDetaPrec.setPrecio(prodPrecCopiar.getPrecio());
			ingrDetaPrec.setFactor(prodPrecCopiar.getFactor());
			ingrDetaPrec.setPrecioConImpu(prodPrecCopiar.getPrecioConImpu());
			ingrDetaPrec.setPrecioSinImpu(prodPrecCopiar.getPrecioSinImpu());
			ingrDetaPrec.setUtilid(prodPrecCopiar.getUtilid());

			ingrDetaPrecs.add(ingrDetaPrec);
		}

		return ingrDetaPrecs;
	}
	
	public List<ProdPrec> filtrarProdPrecs(List<ProdPrec> prodPrecs, PersUsua persUsua, List<RolPrec> rolPrecs, Sucursal sucursal) throws Exception {
		return this.prodPrecLista.filtrarProdPrec(prodPrecs, persUsua, rolPrecs, sucursal);
	}

	public List<ProdPrec> buscarProdPrecs(ProdPrec prodPrec, Integer pagina, Integer filas, String ordenColumna) {

		List<ProdPrec> prodPrecs = new ArrayList<>();

		try {
			prodPrecs = prodPrecLista.buscar(prodPrec, pagina, filas);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar precios"));
			e.printStackTrace();
		}
		return prodPrecs;
	}
	
	public void crearIngrDetas() {

		for (TomaFisiDeta tomaFisiDeta : this.tomaFisiDetas) {

			BigDecimal cantidSald = new BigDecimal(0);
//			Se envia como parametro para crear ingrDeta
			KardTotaView kardTotaView = new KardTotaView();
			
			KardTotaView kardTotaViewBuscar = new KardTotaView();
			kardTotaViewBuscar.setSucursalId(variablesSesion.getSucursal().getSucursalId());
			kardTotaViewBuscar.setBodegaId(1);
			kardTotaViewBuscar.setProductoId(tomaFisiDeta.getProducto().getProductoId());

			List<KardTotaView> kardTotaViews = this.buscarKardTotaViews(kardTotaViewBuscar);

			if (kardTotaViews.size() == 1) {
				cantidSald = kardTotaViews.get(0).getCantidSald();
				kardTotaView = kardTotaViews.get(0);
			} else {
//				Si no encuentra un kardex es un producto nuevo
//				entonces se le pone el costo de la toma fisica
				kardTotaView.setCosto(tomaFisiDeta.getCostoCont());
			}

			// Determina la cantidad que se graba en el ingreso o el egreso de bodeg
			BigDecimal cantidAjus = tomaFisiDeta.getCantidCont().subtract(cantidSald).abs();

			if (tomaFisiDeta.getCantidCont().compareTo(cantidSald) < 0) {
				
				Set<IngrDetaPrec> ingrDetaPrecs = new HashSet<IngrDetaPrec>();
				
				IngrDeta ingrDeta = this.crearIngrDeta(tomaFisiDeta, kardTotaView, cantidAjus,
						new BigDecimal(1));
				this.ingrDetaEgresos.add(ingrDeta);
				
				// Crea ingrDetaPrec para poder ver los precios en el ingreso y egreso Erotik
				ingrDetaPrecs = this.crearIngrDetaPrec(ingrDeta);
				ingrDeta.setIngrDetaPrecs(ingrDetaPrecs);

			} else if (tomaFisiDeta.getCantidCont().compareTo(cantidSald) > 0) {

				Set<IngrDetaPrec> ingrDetaPrecs = new HashSet<IngrDetaPrec>();
				// Se le pone siempre el costo del conteo para crear el ingreso de bodega
				kardTotaView.setCosto(tomaFisiDeta.getCostoCont());
				
				IngrDeta ingrDeta = this.crearIngrDeta(tomaFisiDeta, kardTotaView, cantidAjus,
						new BigDecimal(1));
				this.ingrDetaIngresos.add(ingrDeta);

				// Crea ingrDetaPrec para poder ver los precios en el ingreso y egreso Erotik
				ingrDetaPrecs = this.crearIngrDetaPrec(ingrDeta);
				ingrDeta.setIngrDetaPrecs(ingrDetaPrecs);

			}
		}
	}

	public List<ProdBode> crearProdBodeSinTFs(List<TomaFisiDeta> tomaFisiDetas) {
	
		List<ProdBode> prodBodeConTFs = new ArrayList<ProdBode>();
		Bodega bodega = new Bodega();
		bodega.setBodegaId(1);
	
		for (TomaFisiDeta tomaFisiDeta : tomaFisiDetas) {
			ProdBode prodBode = new ProdBode();
	
			prodBode.setBodega(bodega);
			prodBode.setSucursal(this.tomaFisi.getSucursal());
			prodBode.setProducto(tomaFisiDeta.getProducto());
	
			prodBodeConTFs.add(prodBode);
		}
	
		List<ProdBode> prodBodeSinTFs = new ArrayList<ProdBode>();
		List<ProdBode> prodBodes = new ArrayList<ProdBode>();
		ProdBode prodBodeBuscar = new ProdBode();
	
		prodBodeBuscar.setBodega(bodega);
		prodBodeBuscar.setProducto(new Producto());
		prodBodeBuscar.setSucursal(this.tomaFisi.getSucursal());
	
		prodBodes = this.buscarProdBodes(prodBodeBuscar);
	
		for (ProdBode prodBode : prodBodes) {
			
			Producto producto = this.buscarProductoPorId(prodBode.getProducto());
			
			if (producto.getProdGrup().getTipo().equals("Mercaderia")) {
				
				if (!prodBodeConTFs.contains(prodBode)) {
					
					prodBode.getProducto().setProdGrup(producto.getProdGrup());					
					prodBodeSinTFs.add(prodBode);
				}
			}
		}
		
		return prodBodeSinTFs;
	}
	
	public Producto buscarProductoPorId(Producto productoBuscar) {
		
		Producto producto = new Producto();
		
		try {
			producto = this.productoRegis.buscarPorId(Producto.class, productoBuscar.getProductoId());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Error al buscar producto"));
		}
		
		return producto;
	}

	public void crearPSTF() {
	
			if (this.tomaFisiDetas.size() != 0) {
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, null, "No se puede crear toma física en un documento con datos"));
				return;
			}
			
			TomaFisiDeta tomaFisiDetaBuscar = new TomaFisiDeta();
			TomaFisi tomaFisiBuscar = new TomaFisi();
	
	//		Buscar todos los detalles de la toma fisica en el caso que se haga en varios docuemntos
	//		toma como referencia el numero que el usuario ingresa
			tomaFisiBuscar.setSucursal(this.tomaFisi.getSucursal());
			tomaFisiBuscar.setNumero(this.tomaFisi.getNumero());
			tomaFisiDetaBuscar.setTomaFisi(tomaFisiBuscar);
			List<TomaFisiDeta> tomaFisiDetas = this.buscarTomaFisiDetas(tomaFisiDetaBuscar);
	
	//		Crea una lista de todos los productos que aun no estan en la lista anterior,
	//		es decir los que no estan en ningún documento de toma física
			List<ProdBode> prodBodeSinTFs = this.crearProdBodeSinTFs(tomaFisiDetas);
	
	//		Con la lista anterior crea los detalles de la toma física donde se incluye
	//		todos los productos que no estan en ningún documento de toma física 
			this.tomaFisiDetas.clear();
			this.tomaFisiDetas = this.crearTomaFisiDetas(prodBodeSinTFs, this.tomaFisi);
	
			for (TomaFisiDeta tomaFisiDeta : this.tomaFisiDetas) {
				this.grabarTomaFisiDeta(tomaFisiDeta);
			}
		}

	public List<TomaFisiDeta> crearTomaFisiDetas(List<ProdBode> prodBodeSinTFs, TomaFisi tomaFisi) {
	
			List<TomaFisiDeta> tomaFisiDetas = new ArrayList<TomaFisiDeta>();
	
			for (ProdBode prodBode : prodBodeSinTFs) {
	
				TomaFisiDeta tomaFisiDeta = new TomaFisiDeta();
	
				ProdCost prodCostBuscar = new ProdCost();
				List<ProdCost> prodCosts = new ArrayList<ProdCost>();
					
				prodCostBuscar.setSucursal(tomaFisi.getSucursal());
				prodCostBuscar.setProducto(prodBode.getProducto());
	
				try {
	//				Asigna el costo en tomaFisiDeta
					prodCosts = this.prodCostLista.buscar(prodCostBuscar, null);
					for (ProdCost prodCost : prodCosts) {
						prodPrec.getProducto().setProdCost(prodCost);
						tomaFisiDeta.setCostoActu(prodCost.getCostoActu());
						tomaFisiDeta.setCostoCont(prodCost.getCostoActu());
					}
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
							"Excepcion - Error al asignar costo en: " + prodPrec.getProducto().getDescri()));
					e.printStackTrace();
				}
	
				KardTotaView kardTotaView = new KardTotaView();
				kardTotaView.setSucursalId(tomaFisi.getSucursal().getSucursalId());
				kardTotaView.setBodegaId(1);
				kardTotaView.setProductoId(prodBode.getProducto().getProductoId());
	
				List<KardTotaView> kardTotaViews = this.buscarKardTotaViews(kardTotaView);
	
	//			Asigna la cantidad en tomaFisiDeta
				if (kardTotaViews.size() == 1) {
					tomaFisiDeta.setCantidActu(kardTotaViews.get(0).getCantidSald());
				} else if (kardTotaViews.size() == 0) {
					tomaFisiDeta.setCantidActu(new BigDecimal(0));
				}
	
				tomaFisiDeta.setTomaFisi(tomaFisi);
				tomaFisiDeta.setProducto(prodBode.getProducto());
				tomaFisiDeta.setCantidCont(new BigDecimal(0));
	
				tomaFisiDetas.add(tomaFisiDeta);
			}
			return tomaFisiDetas;
		}

	public String eliminar() {

		try {
			this.eliminarTomaFisiDetas();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
					"Excepción al eliminar detalle de toma física"));
			e.printStackTrace();
		}

		try {
			this.eliminarTomaFisi();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepción al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		return "lista?faces-redirect=true";
	}

	public void eliminarTomaFisi() throws Exception {

		TomaFisi tomaFisi = tomaFisiRegis.buscarPorId(TomaFisi.class, this.getId());
		tomaFisiRegis.eliminar(tomaFisi);
	}

	public void eliminarTomaFisiDeta() {
	
		try {
	
			tomaFisiDetaRegis.eliminar(this.tomaFisiDetaSele);
			this.tomaFisiDetas.remove(this.tomaFisiDetaSele);
	
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion al eliminar detalle de toma física"));
			e.printStackTrace();
		}
	}

	public void eliminarTomaFisiDetas() throws Exception {

		for (TomaFisiDeta tomaFisiDeta : this.tomaFisiDetas) {
			tomaFisiDetaRegis.eliminar(tomaFisiDeta);
		}
	}

//	public void eliminarTomaFisiDeta() {
//
//		this.tomaFisiDetas.remove(this.tomaFisiDetaSele);
//
//		try {
//
//			if (tomaFisiDetaSele.getTomaFisiDetaId() != null) {
//
//				this.tomaFisiDetaEliminados.add(this.tomaFisiDetaSele);
//
//			}
//
//		} catch (Exception e) {
//			FacesContext.getCurrentInstance().addMessage(null,
//					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al eliminar detalle toma física"));
//			e.printStackTrace();
//
//		}
//	}

	public String explorar() {
		return "explora?faces-redirect=true&tomaFisiId=" + this.getId();
	}

	public List<DocuIngr> filtrarDocuIngrs(List<DocuIngr> docuIngrs, PersUsua persUsua, List<RolDocu> rolDocus) {

		List<DocuIngr> docuIngrFiltrados = new ArrayList<DocuIngr>();

		try {
			docuIngrFiltrados = docuIngrLista.filtrarDocuIngrs(docuIngrs, persUsua, rolDocus);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al filtrar documentos de ajuste"));
			e.printStackTrace();
		}
		return docuIngrFiltrados;
	}

	// Primero se graba para poder llenar la lista de productos, ya que
//	sino no se sabe de que sucursal se va a realizar la toma fisica
	public String grabar() {

		try {
			if (this.id == null) {

				Bodega bodega = new Bodega();
				bodega.setBodegaId(1);

				tomaFisi.setBodega(bodega);
				tomaFisi.setSucursal(this.getVariablesSesion().getSucursal());

				Object id = tomaFisiRegis.insertar(tomaFisi);
				this.id = (Integer) id;

			} else {

				tomaFisiRegis.modificar(tomaFisi);

			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

//		Para que se quede en la misma pagina
		return null;
	}

	public void grabarIngreso(Ingreso ingreso) {
	
			ingreso.setNumero(ingreso.getDocuIngr().getDocumento().getNumero() + 1);
	
	//		Actualiza el numero secuencial del documento
			ingreso.getDocuIngr().getDocumento().setNumero(ingreso.getNumero());
			try {
				documentoRegis.modificar(ingreso.getDocuIngr().getDocumento());
	
				try {
					this.ingresoRegis.insertar(ingreso);
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
							"Excepcion grabar ajuste de inventario - Ingreso o Egreso"));
					e.printStackTrace();
				}
	
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion actualizar numero en documento"));
				e.printStackTrace();
			}
		}

	public void grabarIngrDeta(IngrDeta ingrDeta) {
	
		try {
			this.ingrDetaRegis.insertar(ingrDeta);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion grabar detalle de ajuste de inventario - Ingreso o Egreso"));
		}
	}

	public void grabarTomaFisiDeta(TomaFisiDeta tomaFisiDeta) {

		if (tomaFisiDeta.getTomaFisiDetaId() == null) {
			try {
				tomaFisiDetaRegis.insertar(tomaFisiDeta);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion al grabar detalle de toma física"));
				e.printStackTrace();
			}
		}
	}

	public void modificarCelda(CellEditEvent<?> event) {

		DataTable dataTable = (DataTable) event.getSource();
		TomaFisiDeta tomaFisiDeta = (TomaFisiDeta) dataTable.getRowData();

		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		if (newValue != null && !newValue.equals(oldValue)) {

			this.modificarTomaFisiDeta(tomaFisiDeta);

		}
	}

	public void modificarTomaFisiDeta(TomaFisiDeta tomaFisiDeta) {
		try {
			tomaFisiDetaRegis.modificar(tomaFisiDeta);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion al modificar detalle de toma física"));
			e.printStackTrace();
		}
	}

	public String modificar() {
		return "registra?faces-redirect=true&tomaFisiId=" + this.getId();
	}

	public String procesar() {
		
		StringBuilder refere = new StringBuilder();
		
//		Crea ingrDetaIngresos, ingrDetaEgresos
		this.crearIngrDetas();

		if (this.ingrDetaIngresos.size() != 0) {
			
			this.ingreso = this.crearIngreso(this.docuIngrIngreso);
			
			if (this.notaIngreso == null) {
				this.ingreso.setNota("Ajuste inventario por toma Física");	
			} else {
				this.ingreso.setNota(this.notaIngreso);
			}
			
			this.grabarIngreso(this.ingreso);
			
			for (IngrDeta ingrDetaIngreso : ingrDetaIngresos) {
				ingrDetaIngreso.setIngreso(this.ingreso);
				this.grabarIngrDeta(ingrDetaIngreso);
			}

			refere.append("Ingreso ");
			refere.append(this.ingreso.getNumero().toString());
			
			FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Se han generado el ingreso de inventario No. " + this.ingreso.getNumero()));
		}

		if (this.ingrDetaEgresos.size() != 0) {
			
			this.egreso = this.crearIngreso(this.docuIngrEgreso);
			
			if (this.notaEgreso == null) {
				this.egreso.setNota("Ajuste inventario por toma Física");	
			} else {
				this.egreso.setNota(this.notaEgreso);
			}

			this.grabarIngreso(this.egreso);
			
			for (IngrDeta ingrDetaEgreso : ingrDetaEgresos) {
				ingrDetaEgreso.setIngreso(this.egreso);
				this.grabarIngrDeta(ingrDetaEgreso);
			}
			
			refere.append("- Egreso ");
			refere.append(this.egreso.getNumero().toString());
			
			FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Se han generado el egreso de inventario No. " + this.egreso.getNumero()));
		}
				
		this.tomaFisi.setRefere(refere.toString());
		this.tomaFisi.setEstado("PR");
		this.grabar();

		return "/ppsj/inventario/tomaFisi/lista.xhtml?faces-redirect=true";
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback()) {
			return;
		}

		List<DocuIngr> docuIngrs = this.buscarDocuIngrs();
		List<DocuIngr> docuIngrFiltrados = this.filtrarDocuIngrs(docuIngrs, persUsuaSesion,
				variablesSesion.getRolDocus());

		for (DocuIngr docuIngr : docuIngrFiltrados) {
			if (docuIngr.isAfectaCost() && docuIngr.getDocumento().getFactor() == 1) {
				this.docuIngrIngresos.add(docuIngr);
			} else if (docuIngr.isAfectaCost() && docuIngr.getDocumento().getFactor() == -1) {
				this.docuIngrEgresos.add(docuIngr);
			}
		}

		this.precioPred = this.buscarPrecioPred();
		this.buscarProdPrecs(0);

		if (this.id == null) {

			this.tomaFisi = new TomaFisi();

			this.tomaFisi.setFechaRegi(LocalDate.now());
			this.tomaFisi.setFechaProc(LocalDate.now());
			this.tomaFisi.setFechaHoraRegi(LocalDateTime.now());
			this.tomaFisi.setFechaHoraProc(LocalDateTime.now());
			this.tomaFisi.setEstado("GR");

		} else {

			try {
				this.tomaFisi = tomaFisiRegis.buscarPorId(TomaFisi.class, this.getId());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion al buscar toma fisica id"));
				e.printStackTrace();
			}

			TomaFisiDeta tomaFisiDetaBuscar = new TomaFisiDeta();
			TomaFisi tomaFisiBuscar = new TomaFisi();

			tomaFisiBuscar.setTomaFisiId(this.tomaFisi.getTomaFisiId());
			tomaFisiDetaBuscar.setTomaFisi(tomaFisiBuscar);

			this.tomaFisiDetas = this.buscarTomaFisiDetas(tomaFisiDetaBuscar);
		}
	}

	public int getFilasProductos() {
		return variablesSesion.getFilasProductosIngreso();
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

	public int getNumeroTomaFisiDeta() {
		return numeroTomaFisiDeta;
	}

	public void setNumeroTomaFisiDeta(int numeroTomaFisiDeta) {
		this.numeroTomaFisiDeta = numeroTomaFisiDeta;
	}

	public long getContadorTomaFisiDeta() {
		return contadorTomaFisiDeta;
	}

	public void setContadorTomaFisiDeta(long contadorTomaFisiDeta) {
		this.contadorTomaFisiDeta = contadorTomaFisiDeta;
	}

	public Integer getPaginaTomaFisiDeta() {
		return paginaTomaFisiDeta;
	}

	public void setPaginaTomaFisiDeta(Integer paginaTomaFisiDeta) {
		this.paginaTomaFisiDeta = paginaTomaFisiDeta;
	}

	public TomaFisi getTomaFisi() {
		return tomaFisi;
	}

	public void setTomaFisi(TomaFisi tomaFisi) {
		this.tomaFisi = tomaFisi;
	}

	public TomaFisiDeta getTomaFisiDeta() {
		return tomaFisiDeta;
	}

	public void setTomaFisiDeta(TomaFisiDeta tomaFisiDeta) {
		this.tomaFisiDeta = tomaFisiDeta;
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

	public List<TomaFisi> getTomaFisis() {
		return tomaFisis;
	}

	public void setTomaFisis(List<TomaFisi> tomaFisis) {
		this.tomaFisis = tomaFisis;
	}

	public List<TomaFisiDeta> getTomaFisiDetas() {
		return tomaFisiDetas;
	}

	public void setTomaFisiDetas(List<TomaFisiDeta> tomaFisiDetas) {
		this.tomaFisiDetas = tomaFisiDetas;
	}

	public List<TomaFisiDeta> getTomaFisiDetaEliminados() {
		return tomaFisiDetaEliminados;
	}

	public void setTomaFisiDetaEliminados(List<TomaFisiDeta> tomaFisiDetaEliminados) {
		this.tomaFisiDetaEliminados = tomaFisiDetaEliminados;
	}

	public List<Parametro> getParametroEstados() {
		return parametroEstados;
	}

	public void setParametroEstados(List<Parametro> parametroEstados) {
		this.parametroEstados = parametroEstados;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public List<String> getTipos() {
		return tipos;
	}

	public void setTipos(List<String> tipos) {
		this.tipos = tipos;
	}

	public TomaFisiDeta getTomaFisiDetaSele() {
		return tomaFisiDetaSele;
	}

	public void setTomaFisiDetaSele(TomaFisiDeta tomaFisiDetaSele) {
		this.tomaFisiDetaSele = tomaFisiDetaSele;
	}

	public Integer getProductoId() {
		return productoId;
	}

	public void setProductoId(Integer productoId) {
		this.productoId = productoId;
	}

	public ProdPrec getProdPrec() {
		return prodPrec;
	}

	public void setProdPrec(ProdPrec prodPrec) {
		this.prodPrec = prodPrec;
	}

	public ProdPrec getProdPrecSele() {
		return prodPrecSele;
	}

	public void setProdPrecSele(ProdPrec prodPrecSele) {
		this.prodPrecSele = prodPrecSele;
	}

	public List<ProdPrec> getProdPrecs() {
		return prodPrecs;
	}

	public void setProdPrecs(List<ProdPrec> prodPrecs) {
		this.prodPrecs = prodPrecs;
	}

	public DocuIngr getDocuIngrIngreso() {
		return docuIngrIngreso;
	}

	public void setDocuIngrIngreso(DocuIngr docuIngrIngreso) {
		this.docuIngrIngreso = docuIngrIngreso;
	}

	public DocuIngr getDocuIngrEgreso() {
		return docuIngrEgreso;
	}

	public void setDocuIngrEgreso(DocuIngr docuIngrEgreso) {
		this.docuIngrEgreso = docuIngrEgreso;
	}

	public List<DocuIngr> getDocuIngrIngresos() {
		return docuIngrIngresos;
	}

	public void setDocuIngrIngresos(List<DocuIngr> docuIngrIngresos) {
		this.docuIngrIngresos = docuIngrIngresos;
	}

	public List<DocuIngr> getDocuIngrEgresos() {
		return docuIngrEgresos;
	}

	public void setDocuIngrEgresos(List<DocuIngr> docuIngrEgresos) {
		this.docuIngrEgresos = docuIngrEgresos;
	}

	public String getNotaIngreso() {
		return notaIngreso;
	}

	public void setNotaIngreso(String notaIngreso) {
		this.notaIngreso = notaIngreso;
	}

	public String getNotaEgreso() {
		return notaEgreso;
	}

	public void setNotaEgreso(String notaEgreso) {
		this.notaEgreso = notaEgreso;
	}

	public String getOrdenColumna() {
		return ordenColumna;
	}

	public void setOrdenColumna(String ordenColumna) {
		this.ordenColumna = ordenColumna;
	}
}
