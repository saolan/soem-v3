package ec.com.tecnointel.soem.ingreso.controlador;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.file.UploadedFile;

import ec.com.saolan.soem.compartido.excepcion.InfraestructuraExcepcion;
import ec.com.saolan.soem.compartido.excepcion.IntegracionExcepcion;
import ec.com.saolan.soem.compartido.excepcion.ValidacionNegocioExcepcion;
import ec.com.saolan.soem.sri.aplicacion.importacion.factura.ImportarFacturaArchivoServicio;
import ec.com.saolan.soem.sri.aplicacion.importacion.factura.ImportarFacturaSriServicio;
import ec.com.saolan.soem.sri.infraestructura.importacion.ImportarDocumeElecSriParametros;
import ec.com.tecnointel.soem.contabilidad.modelo.Transaccion;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionCompraInt;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionFpmiInt;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionGestionInt;
import ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.LiquidacionCompra;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.ComprobanteRetencion;
import ec.com.tecnointel.soem.documeElec.registroInt.DocumeElecRegisInt;
import ec.com.tecnointel.soem.documeElec.tareas.ManejadorTareaAutoriDocu;
import ec.com.tecnointel.soem.documeElec.tareas.ManejadorTareaEnviarCorreoDocu;
import ec.com.tecnointel.soem.documeElec.tareas.ManejadorTareaEnviarDocu;
import ec.com.tecnointel.soem.egreso.modelo.EgreDeta;
import ec.com.tecnointel.soem.firmaElec.registroInt.FirmarArchivoInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.general.util.IdenSistema;
import ec.com.tecnointel.soem.ingreso.listaInt.ProvGrupPlanCuenListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDeta;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDetaImpu;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDetaPrec;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDimm;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.modelo.PersProv;
import ec.com.tecnointel.soem.ingreso.modelo.PersProvDimm;
import ec.com.tecnointel.soem.ingreso.modelo.ProvGrup;
import ec.com.tecnointel.soem.ingreso.modelo.ProvGrupPlanCuen;
import ec.com.tecnointel.soem.ingreso.modelo.ReteDeta;
import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import ec.com.tecnointel.soem.ingreso.modelo.TotalDocu;
import ec.com.tecnointel.soem.ingreso.registroInt.CompraInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdGrupPlanCuenListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdPrecListaInt;
import ec.com.tecnointel.soem.inventario.modelo.KardTotaView;
import ec.com.tecnointel.soem.inventario.modelo.Kardex;
import ec.com.tecnointel.soem.inventario.modelo.ProdBode;
import ec.com.tecnointel.soem.inventario.modelo.ProdCost;
import ec.com.tecnointel.soem.inventario.modelo.ProdDimm;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrup;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrupPlanCuen;
import ec.com.tecnointel.soem.inventario.modelo.ProdPrec;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.parametro.controlador.PersonaException;
import ec.com.tecnointel.soem.parametro.listaInt.DimmPlanCuenListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.FormPagoPlanCuenListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.TranPlanListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Bodega;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.DocuIngr;
import ec.com.tecnointel.soem.parametro.modelo.DocuMoviIngr;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.parametro.modelo.FormPagoPlanCuen;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.modelo.Precio;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.modelo.TranPlan;
import ec.com.tecnointel.soem.parametro.registroInt.DimmRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.DocumentoRegisInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolDocu;
import ec.com.tecnointel.soem.seguridad.modelo.RolFormPago;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import ec.com.tecnointel.soem.serWebClientSri.recepcion.RespuestaSolicitud;
import ec.com.tecnointel.soem.serWebSri.registroInt.AutorizacionComprobantesWsInt;
import ec.com.tecnointel.soem.serWebSri.registroInt.EnvioComprobantesWsInt;
import ec.com.tecnointel.soem.tesoreria.modelo.Cxp;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import ec.com.tecnointel.soem.tesoreria.modelo.PagoDeta;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.Conversation;
import jakarta.enterprise.context.ConversationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

@Named
@ConversationScoped
public class CompraControl extends PaginaControl implements Serializable {

	private Integer ingresoId;
	private Integer productoId;

	private int factor;
	private int numeroRegProv;
	private Integer paginaProv;
	private long contadorRegProv;

	private long contadorRegCxp;
	private int numeroRegCxp;
	private Integer paginaCxp = 0;

	BigDecimal descue = new BigDecimal(0);
	private BigDecimal saldoProveedor = new BigDecimal(0);

	private BigDecimal totalDocumento = new BigDecimal(0);
	private BigDecimal totalPrecioConImpu = new BigDecimal(0);

	private String ordenColumna;

	private String detalleProceso;
	private String estadoDocuElecRete;

	private LocalDate fechaPago;

	private Ingreso ingreso;
	private IngrDeta ingrDetaSele;
	// private Producto producto;
	private ProdPrec prodPrec;
	// private Precio precio;
	private ProdPrec prodPrecSele;
	private Precio precioPred;
	private Dimm dimmIva;

	private IngrDetaPrec ingrDetaPrecSele;

	private PersUsua persUsuaSesion;
	private PersProv persProv;
	private PersProv persProvRegis;

	private FormPagoMoviIngr formPagoMoviIngr;

	private RolSucu rolSucuConvertir;
	private DocuIngr docuIngrConvertir;

	private Cxp cxpBuscar;
	private Cxp cxpSele;

	List<Parametro> parametros;

	private Parametro paraUtilidadActualizaPrecio;
	private Parametro paraActualPrecioTodasSucu;

	private List<Ingreso> ingresos;
	private List<ProdPrec> prodPrecs;
	private List<ProdPrec> prodPrecDialogos;
	private List<ProdCost> prodCostDialogos;
	private List<Precio> precios;
	private List<IngrDeta> ingrDetaDataTable;
	private List<IngrDeta> ingrDetaEliminados;
	private List<DocuIngr> docuIngrs;
	private List<PersProv> persProvs;
	private List<Dimm> dimms;
	private List<Dimm> dimmRetes;
	private List<RolDocu> rolDocus;
	private List<KardTotaView> kardTotaViews;
	private List<DocuMoviIngr> docuMoviIngrs;
	private List<FormPago> formPagos;

	private List<RolSucu> rolSucuConvertirs;

	private List<Cxp> cxps;

	private List<DocuMoviIngr> docuMoviIngrTmps;
	private List<FormPago> formPagoTmps;

	private List<ProvGrup> provGrups;
	private List<Dimm> dimmTipoIdens;
	private List<Dimm> dimmTipoIdenProvs;

	private List<IngrDetaPrec> ingrDetaPrecOrdens;

	private Set<Integer> sucursals;
	private Retencion retencion;
	private Set<ReteDeta> reteDetas;

	// TreeMap<String, BigDecimal> totalIngr = new TreeMap<String, BigDecimal>();
	List<TotalDocu> totalDocus = new ArrayList<>();

	@Inject
	private Conversation conversation;

	@Inject
	CompraInt compra;

	// Se injecto solamente para enviar como parametro en tareaCrearRideFactura
	@Inject
	DimmRegisInt dimmRegis;

	@Inject
	DocumeElecRegisInt documeElectRegis;

	@Inject
	EnvioComprobantesWsInt envioComprobantesWs;

	@Inject
	AutorizacionComprobantesWsInt autorizacionComprobantes;

	@Inject
	ManejadorTareaEnviarDocu manejadorTareaEnvioDocu;

	@Inject
	ManejadorTareaAutoriDocu manejadorTareaAutoriDocu;

	@Inject
	ManejadorTareaEnviarCorreoDocu manejadorTareaEnviarCorreoDocu;

	@Inject
	IdenSistema idenSistema;

	@Inject
	ProdGrupPlanCuenListaInt prodGrupPlanCuenLista;

	@Inject
	ProvGrupPlanCuenListaInt provGrupPlanCuenLista;

	@Inject
	DimmPlanCuenListaInt dimmPlanCuenLista;

	@Inject
	DocumentoRegisInt documentoRegis;

	@Inject
	FormPagoPlanCuenListaInt formPagoPlanCuenLista;

	@Inject
	TransaccionGestionInt transaccionGestion;

	@Inject
	TranPlanListaInt tranPlanLista;

	@Inject
	TransaccionCompraInt transaccionCompra;

	@Inject
	TransaccionFpmiInt transaccionFpmi;

	@Resource(mappedName = "java:jboss/datasources/soemDS")
	private DataSource dataSource;

	@Inject
	FirmarArchivoInt firmarArchivo;

	private static final long serialVersionUID = 5176912413722064550L;

	@PostConstruct
	public void cargar() {

		ProdGrup prodGrup = new ProdGrup();

		this.ingreso = new Ingreso();
		this.ingreso.setNumeroGuia(0);
		this.ingreso.setNumeroRete(0);

		this.prodPrec = new ProdPrec();

		this.parametros = new ArrayList<>();

		// Esta busqueda va aqui para poder seleccionar el tipo predeterminado
		// ya que aqui se esta haciendo la asignacion de las variables
		Parametro parametroBuscar = new Parametro();
		parametroBuscar.setCodigo("Inventario-GrupoTipo");
		parametroBuscar.setEstado(true);

		List<Parametro> paraGrupTipos = this.buscarParametros(parametroBuscar);

		String[] tipoGrupPred;

		// Se recorre la lista y se busca el valor predeterminado
		// PCM = Predeterminado en compras; PVN = Predeterminado en ventas; PCV =
		// Predetermindado en compras y ventas; PFL = Predeterminado false
		// Tambien se quita de la descripcion del parametro el valor que determina cual
		// es el predeterminado
		for (Parametro parametro : paraGrupTipos) {
			tipoGrupPred = parametro.getDescri().split(";");
			if (tipoGrupPred[1].equals("PCM") || tipoGrupPred[1].equals("PCV")) {
				prodGrup.setTipo(tipoGrupPred[0]);
			}
			parametro.setDescri(tipoGrupPred[0]);
			this.parametros.add(parametro);
		}

		this.paraUtilidadActualizaPrecio = this.buscarParametro(4150);
		this.paraActualPrecioTodasSucu = this.buscarParametro(4151);

		Producto producto = new Producto();
		producto.setProdGrup(prodGrup);

		this.prodPrec.setProducto(producto);

		this.ingrDetaEliminados = new ArrayList<>();

		// Buscar el registro que contenga el valor del IVA
		// Para el calculo en caso que exista Retencion Iva
		this.dimmIva = this.buscarIva();

		rolDocus = variablesSesion.getRolDocus();
		rolPermiso = variablesSesion.getRolPermiso();

		this.persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");

		this.persProv = new PersProv();
		persProv.setPersona(new Persona());

		this.sucursals = new HashSet<>();
		this.buscarRolSucus();

		try {
			buscarFormatoReportes();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepción - Error al buscar formato reportes"));
			e.printStackTrace();
		}

		if (variablesSesion.isActivarImagen()) {
			ordenColumna = "codigo";
		} else {
			ordenColumna = "descri";
		}

		this.precioPred = this.buscarPrecioPred();

		// Busqueda de CXP
		this.cxpBuscar = new Cxp();
		PersProv persProv = new PersProv();
		persProv.setPersona(new Persona());
		Ingreso ingreso = new Ingreso();
		ingreso.setPersProv(persProv);

		cxpBuscar.setIngreso(ingreso);
		cxpBuscar.setEstado(false);

		this.cxpSele = new Cxp();

		this.cxps = new ArrayList<>();

		this.docuMoviIngrTmps = new ArrayList<>();

		this.dimmRetes = new ArrayList<>();

//		Carga las leyendas que estan en parametros para poner en 
//		el xml de los documentos electronicos 
		documeElectRegis.leyenda1(variablesSesion.getLeyenda1());
		documeElectRegis.leyenda2(variablesSesion.getLeyenda2());
		documeElectRegis.msgInfoAdicional(variablesSesion.getMsgInfoAdicional());
	}

	public void ordenarIngrDetaPrec() {

		List<IngrDetaPrec> ingrDetaPrecs = new ArrayList<IngrDetaPrec>(this.ingrDetaSele.getIngrDetaPrecs());

		Collections.sort(ingrDetaPrecs, new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				return new String(((IngrDetaPrec) o1).getPrecio().getCodigo())
						.compareTo(new String(((IngrDetaPrec) o2).getPrecio().getCodigo()));
			}
		});

		this.ingrDetaPrecOrdens = new ArrayList<IngrDetaPrec>();
		this.ingrDetaPrecOrdens.addAll(ingrDetaPrecs);
	}

//	Muestra los datos del producto en el dialogo de de modificar precios ingrDetaPrec
	public void productoExplorarDesdeIngrDeta() {

		ordenarIngrDetaPrec();

//		Muestra inventario
		buscarKardTotaViews();

//		Muestra Costos
		ProdCost prodCostBuscar = new ProdCost();

		prodCostBuscar.setSucursal(this.variablesSesion.getSucursal());
		prodCostBuscar.setProducto(new Producto());

		prodCostBuscar.getProducto().setProductoId(productoId);

		prodCostDialogos = this.buscarProdCosts(prodCostBuscar);
	}

	public void conversationBegin() {
		if (conversation.isTransient()) {
			conversation.setTimeout(7200000L);
			conversation.begin();
		}
	}

	public void conversationEnd() {
		if (!conversation.isTransient()) {
			conversation.end();
		}
	}

	public void seleccionarProveedor() {

		this.saldoProveedor();

//		Limpia datos anteriores en caso de que el usario cambie de proveedor
//		y haya estado ingresando una nota de credito
		this.cxpSele = new Cxp();
		this.ingreso.setIngreso(null);
		this.cxps.clear();

//		Asigna el cliente que selecciona el usuario al cliente de busqueda
//		para buscar las Cxc del cliente en caso que sea nota de credito
		this.cxpBuscar.getIngreso().setPersProv(this.ingreso.getPersProv());

		this.buscarCopiarPersProvDimm();
		this.crearIngrDetaImpuRetens();

	}

	public List<Cxp> buscarCxps() {

		List<PagoDeta> pagoDetas = new ArrayList<>();

		try {

			this.cxps = compra.buscarCxps(this.cxpBuscar, this.paginaCxp, variablesSesion.getFilasPagina());
			this.numeroRegCxp = cxps.size();
			this.contadorRegCxp = compra.contarCxps(this.cxpBuscar);

			for (Cxp cxpSaldo : cxps) {

				BigDecimal totalAbono = new BigDecimal(0);

				pagoDetas = this.buscarPagoDetas(cxpSaldo);

				for (PagoDeta pagoDetaCxp : pagoDetas) {
					totalAbono = totalAbono.add(pagoDetaCxp.getTotal());
				}

				cxpSaldo.setAbono(totalAbono);
				cxpSaldo.setSaldo(cxpSaldo.getTotal().subtract(totalAbono));
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}

		return cxps;
	}

	public List<PagoDeta> buscarPagoDetas(Cxp cxp) throws Exception {

		List<PagoDeta> pagoDetas = new ArrayList<>();

		PagoDeta pagoDeta = new PagoDeta();

		Cxp cxpBuscar = new Cxp();
		cxpBuscar.setCxpId(cxp.getCxpId());
		Ingreso ingreso = new Ingreso();
		cxpBuscar.setIngreso(ingreso);

		FormPagoMoviIngr formPagoMoviIngrPagoDeta = new FormPagoMoviIngr();
		Persona persona = new Persona();
		FormPago formPago = new FormPago();
		formPagoMoviIngrPagoDeta.setPersona(persona);
		formPagoMoviIngrPagoDeta.setFormPago(formPago);

		pagoDeta.setFormPagoMoviIngr(formPagoMoviIngrPagoDeta);
		pagoDeta.setCxp(cxpBuscar);

		try {
			pagoDetas = compra.buscarPagoDetas(pagoDeta);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar detalle de pagos"));
			e.printStackTrace();
		}

		return pagoDetas;
	}

	// Recupera el tipo de documento para generar transaccion
	public Documento buscarPorIdDocumento(Integer documentoId) throws Exception {
		return this.documentoRegis.buscarPorId(Documento.class, documentoId);
	}

	public List<ProdGrupPlanCuen> buscarProdgpc(ProdGrupPlanCuen pgpc) throws Exception {
		return this.prodGrupPlanCuenLista.buscar(pgpc, null);
	}

	public List<ProvGrupPlanCuen> buscarPgpc(ProvGrupPlanCuen pgpc) throws Exception {
		return this.provGrupPlanCuenLista.buscar(pgpc, null);
	}

	public List<FormPagoPlanCuen> buscarFppc(FormPagoPlanCuen fppc) throws Exception {
		return this.formPagoPlanCuenLista.buscar(fppc, null);
	}

	public List<TranPlan> buscarTranPlans() {

		TranPlan tranPlan = new TranPlan();

		List<TranPlan> tranPlans = new ArrayList<>();

		tranPlan.setModulo("PAGOS");
		tranPlan.setEstado(true);

		try {
			tranPlans = this.tranPlanLista.buscar(tranPlan, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar transacción Plantilla"));
			e.printStackTrace();
		}

		return tranPlans;
	}

	public Integer contabilizarCompra(Ingreso ingreso) {

		Integer transaccionId = 0;

		try {

//			Modifica la nota del ingreso para pasar eso a la nota de la tansaccion
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

			ingreso.setNota(ingreso.getNota() + " " + ingreso.getSerie1() + "-" + ingreso.getSerie2() + "-"
					+ ingreso.getNumero() + " " + ingreso.getPersProv().getPersona().getApelli() + " "
					+ ingreso.getFechaEmis().format(dateTimeFormatter));

//			Revisar que la nota no tenga mas de 255 caracateres
			if (ingreso.getNota().length() > 255) {
				ingreso.setNota(ingreso.getNota().substring(0, 254));
			}

			// Si cumple estas condiciones es una compra
			if (ingreso.getDocuIngr().getDocumento().getFactor() == 1 && !ingreso.getDocuIngr().isAfectaCost()) {
				transaccionId = transaccionCompra.contabilizarCompra(ingreso);
			}

			// Si cumple estas condiciones es una nota de credito
			if (ingreso.getDocuIngr().getDocumento().getFactor() == -1 && !this.ingreso.getDocuIngr().isAfectaCost()) {

//				Si el saldo de la cxp es menor que el total del documento se carga contra cxp
//				caso contrario se carga contra anticipo
				if (this.cxpSele.getSaldo().compareTo(this.formPagoMoviIngr.getTotal()) >= 0) {
					transaccionId = transaccionCompra.contabilizarNotaCredito(ingreso, formPagoMoviIngr, "CXP-CXC");
				} else {
					transaccionId = transaccionCompra.contabilizarNotaCredito(ingreso, formPagoMoviIngr, "ANTICIPO");
				}
			}

			// Si cumple estas condiciones es un ingreso de bodega
			if (ingreso.getDocuIngr().getDocumento().getFactor() == 1 && this.ingreso.getDocuIngr().isAfectaCost()) {
				transaccionId = transaccionCompra.contabilizarIngreso(ingreso);
			}

			// Si cumple estas condiciones es un egreso de bodega
			if (ingreso.getDocuIngr().getDocumento().getFactor() == -1 && this.ingreso.getDocuIngr().isAfectaCost()) {
				transaccionId = transaccionCompra.contabilizarIngreso(ingreso);
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, null, "Transacción contable no se ha generado"));
			e.printStackTrace();
		}

		try {

			// Coloca transaccionId en el ingreso
			if (transaccionId != 0) {

				Transaccion transaccion = new Transaccion();
				transaccion.setTransaccionId(transaccionId);

				ingreso.setTransaccion(transaccion);
				try {

					this.compra.modificar(ingreso);

				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
							null, "Excepcion - No se ha registrado el Id de la transaccion en compra"));
					e.printStackTrace();
					return null;
				}

				// Coloca transaccionId en el formPagoMoviIng en caso que sea nota de credito,
				// si es factura este proceso se realiza en contabilizarPago
				// si en la N/C se selecciono una Cxp (cxpSele) se crea un abono si no se
				// selecciono se crea un anticipo
				if (ingreso.getDocuIngr().getDocumento().getFactor() == -1 && !ingreso.getDocuIngr().isAfectaCost()) {

					transaccion.setTransaccionId(transaccionId);

					this.formPagoMoviIngr.setTransaccion(transaccion);
					try {
						this.compra.modificarFpmi(this.formPagoMoviIngr);
					} catch (Exception e) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
								null, "Excepcion - No se ha registrado el Id de la transaccion en pago"));
						e.printStackTrace();
						return null;
					}
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, null, "Transacción contable no se ha generado"));
			e.printStackTrace();
		}

		return transaccionId;
	}

	public Integer contabilizarPago(FormPagoMoviIngr formPagoMoviIngr) {

		Integer transaccionId = 0;

		try {

//			Modifica la nota del ingreso para pasar eso a la nota de la tansaccion
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

//			Revisar que la nota no tenga valores nulos
			if (formPagoMoviIngr.getNota() == null) {
				formPagoMoviIngr.setNota(formPagoMoviIngr.getDocuMoviIngr().getDocumento().getDescri() + " "
						+ formPagoMoviIngr.getFormPago().getDescri() + " " + formPagoMoviIngr.getNumero() + " "
						+ formPagoMoviIngr.getPersona().getApelli() + " Ref:" + formPagoMoviIngr.getRefere() + " "
						+ formPagoMoviIngr.getFecha().format(dateTimeFormatter));
			} else {
				formPagoMoviIngr.setNota(formPagoMoviIngr.getNota() + " "
						+ formPagoMoviIngr.getDocuMoviIngr().getDocumento().getDescri() + " "
						+ formPagoMoviIngr.getFormPago().getDescri() + " " + formPagoMoviIngr.getNumero() + " "
						+ formPagoMoviIngr.getPersona().getApelli() + " Ref:" + formPagoMoviIngr.getRefere() + " "
						+ formPagoMoviIngr.getFecha().format(dateTimeFormatter));
			}

//			Revisar que la nota no tenga mas de 255 caracateres
			if (formPagoMoviIngr.getNota().length() > 255) {
				formPagoMoviIngr.setNota(formPagoMoviIngr.getNota().substring(0, 254));
			}

			transaccionId = transaccionFpmi.contabilizarPago(formPagoMoviIngr);

			if (transaccionId != 0) {

				Transaccion transaccion = new Transaccion();
				transaccion.setTransaccionId(transaccionId);

				this.formPagoMoviIngr.setTransaccion(transaccion);
				try {
					this.compra.modificarFpmi(this.formPagoMoviIngr);
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
							null, "Excepcion - No se ha registrado el Id de la transaccion en pago"));
					e.printStackTrace();
					return null;
				}
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, null,
					"Transacción contable por el pago del documento no se ha generado"));
			e.printStackTrace();
		}

		return transaccionId;
	}

	public void calcularTotalIngres() {

		BigDecimal totalBrutoCabe = new BigDecimal(0);
		BigDecimal totalBrutoDeta = new BigDecimal(0);
		BigDecimal descueCabe = new BigDecimal(0);
		BigDecimal descueDeta = new BigDecimal(0);
		BigDecimal descueTotal = new BigDecimal(0);
		BigDecimal totalNetoDeta = new BigDecimal(0);
		BigDecimal totalNetoDetaTmp = new BigDecimal(0);
		BigDecimal impuestoDeta = new BigDecimal(0);
		BigDecimal impuestoDetaAcum = new BigDecimal(0);
		BigDecimal totalDeta = new BigDecimal(0);
		BigDecimal totalCabe = new BigDecimal(0);
		BigDecimal descueCabePorc = new BigDecimal(0);
		BigDecimal ivaIceIva = new BigDecimal(0);
		BigDecimal ivaIceRete = new BigDecimal(0);

		// Se inicializa esta variable ya que sino guarda el total anterior y acumula
		// demasiado
		this.totalPrecioConImpu = BigDecimal.ZERO;

		// Se recupera el descuento que tiene en la cabecera para poder realizar los
		// calculos respectivos
		if (this.getIngreso().getDescue() != null) {
			descueCabePorc = this.getIngreso().getDescue();
		}

		this.totalDocus.clear();

//		Para controlar esto se crea un producto con solo impuesto ICE y un Producto solo
//		con impuesto IRBPNR y en este for se calcula el iva del ice y se le acumula en el
//		iva generado en la factura
		for (IngrDeta ingrDeta : ingrDetaDataTable) {

			if (this.getIngreso().getDocuIngr().isGeneraImpu()) {

				for (IngrDetaImpu ingrDetaImpu : ingrDeta.getIngrDetaImpus()) {

//					Calcula el iva del ICE
					if (ingrDetaImpu.getTipo().equals("ICE")) {

						ivaIceIva = ingrDeta.getCosto().multiply(this.dimmIva.getPorcen()).divide(new BigDecimal(100),
								6, RoundingMode.HALF_UP);
						ivaIceIva = ivaIceIva.setScale(6, RoundingMode.HALF_UP);
						ivaIceRete = ingrDeta.getCosto().multiply(this.dimmIva.getPorcen()).divide(new BigDecimal(100),
								6, RoundingMode.HALF_UP);
						ivaIceRete = ivaIceRete.setScale(6, RoundingMode.HALF_UP);
						break;
					}
//					Fin Calcula el iva del ICE
				}
			}
		}

		for (IngrDeta ingrDeta : ingrDetaDataTable) {

			impuestoDetaAcum = new BigDecimal(0);

			totalBrutoDeta = ingrDeta.getCantid().multiply(ingrDeta.getCosto());
			descueDeta = totalBrutoDeta.multiply(ingrDeta.getDescue()).divide(new BigDecimal(100));
			totalNetoDeta = totalBrutoDeta.subtract(descueDeta).subtract(ingrDeta.getDescueValo());
			ingrDeta.setTotal(totalNetoDeta);

			// if (ingrDeta.getIngreso() != null) {
			descueCabe = totalNetoDeta.multiply(descueCabePorc).divide(new BigDecimal(100));
			totalNetoDeta = totalNetoDeta.subtract(descueCabe);
			// }
			// Se grabo en un campo aparte para poder utilizarlo para el calculo del costo
			// promedio ya que si se coloca descuento kardTotaView agrupa por descuento
			// y duplica la fila
			ingrDeta.setCostoNeto(totalNetoDeta.divide(ingrDeta.getCantid(), 6, RoundingMode.HALF_UP));

			// Se guarda en una variable temporal para el calculo de retenciones de Iva
			totalNetoDetaTmp = totalNetoDeta;

			// Calcula impuestos de acuerdo al tipo de documento seleccionado
			if (this.getIngreso().getDocuIngr().isGeneraImpu()) {

				// Recorrido de impuestos y retenciones grabados en cada producto
				for (IngrDetaImpu ingrDetaImpu : ingrDeta.getIngrDetaImpus()) {

					BigDecimal factor = new BigDecimal(ingrDetaImpu.getFactor());

					// No calcula retenciones en caso de ser nota de credito y si el factor de
					// impuesto es menor que 1
					if (this.ingreso.getDocuIngr().getDocumento().getFactor() <= 0
							&& factor.compareTo(BigDecimal.ZERO) <= 0) {
						continue;
					}

					// En caso de retencion Iva se cambia la base del calculo
					// la variable totalNetoDeta temporalmente almacena el valor del IVA
					// El valor "RI" se graba cuando se crea ingreDetaImpu (crearIngrDetaImpu())
					if (ingrDetaImpu.getTipo().equals("RI")) {

						ProdDimm prodDimmBuscar = new ProdDimm(ingrDeta.getProducto(), new Dimm());
						ProdDimm prodDimmIva = buscarProdDimmIva(prodDimmBuscar);
						// Se cambia el valor del totalNetoDeta por el valor del IVA
//						totalNetoDeta = totalNetoDeta.multiply(this.dimmIva.getPorcen()).divide(new BigDecimal(100));
						totalNetoDeta = totalNetoDeta.multiply(prodDimmIva.getDimm().getPorcen())
								.divide(new BigDecimal(100));

//						Se acumula iva del ice al subtotal Retencion Iva
						totalNetoDeta = totalNetoDeta.add(ivaIceRete);
//						Si no se coloca esta variable en cero acumula el ice para todos los productos en la factura 
						ivaIceRete = BigDecimal.ZERO;
						// totalNetoDeta = totalNetoDeta.setScale(2, RoundingMode.HALF_UP);

					} else {
						// Se regresa el valor del totalLNetoDeta a su valor original
						// Para que no afecte en el proximo calculo
						totalNetoDeta = totalNetoDetaTmp;
					}

					totalNetoDeta = totalNetoDeta.setScale(6, RoundingMode.HALF_UP);

//					Se hace esto para que no calcule impuesto por ICE, ya que solo se necesita calcular 
//					el iva sobre el valor ingresado en costo
					if (ingrDetaImpu.getTipo().equals("ICE")) {
						impuestoDeta = BigDecimal.ZERO;
					} else if (ingrDetaImpu.getTipo().equals("IVA")) {
						impuestoDeta = (totalNetoDeta.multiply(ingrDetaImpu.getPorcen()).divide(new BigDecimal(100)));
//						Se acumula iva del ice al Iva						
						impuestoDeta = impuestoDeta.add(ivaIceIva);
//						Si no se coloca esta variable en cero acumula el ice para todos los productos en la factura						
						ivaIceIva = BigDecimal.ZERO;
					} else {
						impuestoDeta = (totalNetoDeta.multiply(ingrDetaImpu.getPorcen()).divide(new BigDecimal(100)));
					}
//					Fin

					impuestoDeta = impuestoDeta.multiply(factor);
					impuestoDeta = impuestoDeta.setScale(6, RoundingMode.HALF_UP);

					// Acumula impuesto solo para calculo del total del documento
					// cuando un producto esta grabado con mas de un impuesto
					impuestoDetaAcum = impuestoDetaAcum.add(impuestoDeta);

					// Crear Key para treeMap para separar y acumular impuestos y subtotal de cada
					// impuesto
					String claveImpuesto[] = ingrDetaImpu.getDescri().split(";");
					String claveSubtotal = "Subtotal " + claveImpuesto[1];
					// String Clave = "Subtotal " + ingrDetaImpu.getDescri();

					// Si Key No exite almacena impuestos y retenciones de acuerdo a lo que tenga
					// grabado el producto
					// Si Key existe acumula impuestos y retenciones de acuerdo a lo que tenga
					// grabado el producto
					TotalDocu totalDocu1 = new TotalDocu();
					totalDocu1.setDescri(claveSubtotal);
					int indice1 = this.totalDocus.indexOf(totalDocu1);

					TotalDocu totalDocu2 = new TotalDocu();
					totalDocu2.setDescri(claveImpuesto[1]);
					int indice2 = this.totalDocus.indexOf(totalDocu2);

					if (indice1 == -1) {

						totalDocu1.setCodigo(ingrDetaImpu.getCodigo());
						totalDocu1.setDescri(claveSubtotal);
						totalDocu1.setPorcen(ingrDetaImpu.getPorcen());
						totalDocu1.setValor(totalNetoDeta);
						totalDocu1.setTipoImpu(ingrDetaImpu.getTipo());
						this.totalDocus.add(totalDocu1);

						// TotalDocu totalDocu2 = new TotalDocu();
						// totalDocu.setCodigo(ingrDetaImpu.getCodigo());
						totalDocu2.setDescri(claveImpuesto[1]);
						// totalDocu.setPorcen(ingrDetaImpu.getPorcen());
						totalDocu2.setValor(impuestoDeta);
						this.totalDocus.add(totalDocu2);

					} else {

						BigDecimal total = new BigDecimal(0);
						TotalDocu totalDocu3 = this.totalDocus.get(indice1);
						total = totalDocu3.getValor();
						totalDocu3.setValor(total.add(totalNetoDeta));

						BigDecimal total1 = new BigDecimal(0);
						TotalDocu totalDocu4 = this.totalDocus.get(indice2);
						total1 = totalDocu4.getValor();
						totalDocu4.setValor(total1.add(impuestoDeta));
					}
				}
			}

			// Se regresa el valor del totalNetoDeta a su valor original
			// Para que no afecte en el proximo calculo
			totalNetoDeta = totalNetoDetaTmp;

			totalDeta = totalNetoDeta.add(impuestoDetaAcum);
			totalBrutoCabe = totalBrutoCabe.add(totalBrutoDeta);
			descueTotal = descueTotal.add(descueDeta).add(descueCabe).add(ingrDeta.getDescueValo());
			totalCabe = totalCabe.add(totalDeta);

			// Calcular total precio (Erotik)
			ingrDeta.setTotalPrecioConImpu(
					(ingrDeta.getCantid().multiply(ingrDeta.getPrecioConImpu())).setScale(6, RoundingMode.HALF_UP));
			this.totalPrecioConImpu = this.totalPrecioConImpu.add(ingrDeta.getTotalPrecioConImpu());
		}

		TotalDocu totalDocu = new TotalDocu();
		totalDocu.setDescri("Total Bruto");
		totalDocu.setValor(totalBrutoCabe.setScale(6, RoundingMode.HALF_UP));
		totalDocus.add(totalDocu);

		totalDocu = new TotalDocu();
		totalDocu.setDescri("Total Descuento");
		totalDocu.setValor(descueTotal.setScale(6, RoundingMode.HALF_UP));
		totalDocus.add(totalDocu);

		totalDocu = new TotalDocu();
		totalDocu.setDescri("Total Documento");
		totalDocu.setValor(totalCabe.setScale(6, RoundingMode.HALF_UP));
		totalDocus.add(totalDocu);
	}

//	Buscar Iva del producto
	public ProdDimm buscarProdDimmIva(ProdDimm prodDimm) {

		ProdDimm prodDimmIva = new ProdDimm();

		List<ProdDimm> prodDimms = this.buscarProdDimms(prodDimm);

		for (ProdDimm prodDimmRecorrer : prodDimms) {
			if (prodDimmRecorrer.getDimm().getTablaRefe().equals("Tabla12")) {
				prodDimmIva = prodDimmRecorrer;
			}
		}

		return prodDimmIva;
	}

	public void calcularDiasPlaz() {

		this.ingreso.setDiasPlaz((short) ChronoUnit.DAYS.between(this.ingreso.getFechaEmis(), this.fechaPago));
	}

	public void calcularFechaPago() {

		this.fechaPago = this.ingreso.getFechaEmis().plusDays(this.ingreso.getDiasPlaz());

	}

//	tambien bloquea los campos para que no se modifique la informacion si la forma de pago
//	es diferente de credito, esto se usa para sacar el reporte de las facturas por forma de pago
	public void alCambiarFormPago() {

		if (this.formPagoMoviIngr.getFormPago().getTipo().equals("CR")) {
			this.ingreso.setDiasPlaz((short) 30);
			this.calcularFechaPago();
		} else {
			this.ingreso.setDiasPlaz((short) 0);
			this.fechaPago = this.ingreso.getFechaRegi();
		}
	}

	// Esto se usa para poner como fecha de registro la fecha de emision
	// dependiendo de los permisos del usuario por motivos contables y para el
	// reporte del ATS
	public void copiarFecha() {
		if (variablesSesion.getRolPermiso().get(4350)) {
			this.ingreso.setFechaRegi(this.ingreso.getFechaEmis());
		}
	}

	public void buscarDimmDocu() {
		try {
			Dimm dimm = this.compra.buscarDimmPorId(this.ingreso.getDocuIngr().getDocumentoId());
			this.ingreso.getDocuIngr().setDimm(dimm);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error buscar dimm del documento"));
			e.printStackTrace();
		}
	}

	public void iniciarCompraCabe() {

//		Se coloca esta metodo aqui para que al ingresar un nuevo proveedor se actualize la lista

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback()) {
			return;
		}

		this.buscarPersProvs(0);

		this.conversationBegin();

		DocuIngr docuIngrPred = new DocuIngr();
		List<DocuIngr> docuIngrs = this.buscarDocuIngrs();

		persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");

		formPagoMoviIngr = new FormPagoMoviIngr();

		this.buscarSustentos();

		Dimm dimm = new Dimm();

		dimm.setTablaRefe("Tabla3");
		dimm.setEstado(true);
		this.dimmRetes.addAll(this.buscarDimms(dimm));

		dimm.setTablaRefe("Tabla11");
		this.dimmRetes.addAll(this.buscarDimms(dimm));

		try {
			this.docuIngrs = compra.filtrarDocuIngrs(docuIngrs, persUsuaSesion, variablesSesion.getRolDocus());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al filtrar documentos ingreso"));
			e.printStackTrace();
		}

		if (this.ingresoId != null) {

			PersProv persProv = new PersProv();

			this.buscarIngreso();

//			Se asigna el tipo de identificación del proveedor (DIMM - Cedula o Ruc) 
//			para generar el documento electronico, porque al recuperar el ingreso 
//			solo se busca hasta el provedor y no se busca DIMM
			try {
				persProv = this.compra.buscarPersProvPorId(this.ingreso.getPersProv());
				this.ingreso.getPersProv().setDimm(persProv.getDimm());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Proveedor"));
				e.printStackTrace();
			}

			this.buscarDimmDocu();

			this.factor = this.getIngreso().getDocuIngr().getDocumento().getFactor();

			if (this.ingreso.getEstado().equals("GR")) {

				this.calcularFechaPago();

			}

//			Buscar IngrDimm cuando el documento este procesado
//			ya que si esta grabado se carga del proveedor
//			y puede ser diferente ya que los valores del proveedor son solo de referencia
			IngrDimm ingrDimm = new IngrDimm();
			ingrDimm.setIngreso(this.ingreso);
			ingrDimm.setDimm(new Dimm());
			this.ingrDimms = this.buscarIngrDimms(ingrDimm, null);

//			Buscar retencion para saber el estado y en el caso que sea electronica
//			y ya esta autorizada no se pueda desprocesar
			Retencion retencionBuscar = new Retencion();
			List<Retencion> retencions = new ArrayList<>();
			retencionBuscar.setIngreso(this.ingreso);

			try {
				retencions = this.compra.buscarRetencions(retencionBuscar);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Error al buscar retención"));
				e.printStackTrace();
			}

			if (retencions.size() == 0) {
//				Se coloca este estado si no hay retencion
//				para que permita desprocesar
				estadoDocuElecRete = "NO AUTORIZADO";
			} else if (retencions.size() >= 1) {
				if (retencions.get(0).isDocumeElec() && retencions.get(0).getEstadoDocuElec().equals("AUTORIZADO")) {
					estadoDocuElecRete = "AUTORIZADO";
				} else if (retencions.get(0).isDocumeElec()
						&& !retencions.get(0).getEstadoDocuElec().equals("AUTORIZADO")) {
					estadoDocuElecRete = "NO AUTORIZADO";
				} else if (!retencions.get(0).isDocumeElec()) {
//					Se coloca este estado si la retencion no es electronica
//					para que permita desprocesar
					estadoDocuElecRete = "AUTORIZADO";
				}
				estadoDocuElecRete = retencions.get(0).getEstadoDocuElec();
			}
			// Fin buscar retención

		} else {
			ingreso.setFechaEmis(LocalDate.now());
			ingreso.setFechaRegi(LocalDate.now());
			ingreso.setDescue(new BigDecimal(0));
			ingreso.setNumeroCuot((short) 1);
			ingreso.setSerie1("0");
			ingreso.setSerie2("0");
//			ingreso.setAutori("0");
//			ingreso.setClaveAcce("0");
//			Se diseño con el fin de crear cuotas por 30, 15, 7 o cualquier numero de dìas
//			Se elimina de la pagina para generar solamente cuotas mensuales
			ingreso.setDiasPlaz((short) 30);
			ingreso.setEstado("GR");

//			Seleccionar el documento predetermido en rolDocu
			for (DocuIngr docuIngr : this.docuIngrs) {
				for (RolDocu rolDocu : rolDocus) {
					if (docuIngr.getDocumentoId().equals(rolDocu.getDocumento().getDocumentoId())
							&& rolDocu.getPredet() == true) {
						docuIngrPred = docuIngr;
					}
				}
			}

			this.ingreso.setDocuIngr(docuIngrPred);
//			Coloca al valor de la variable factor para habilitar o inhabilitar botones
			this.factor = docuIngrPred.getDocumento().getFactor();

			this.valoresCabecera();

			this.calcularFechaPago();
		}

//		Buscar Documentos de pago para luego filtar de acuerdo a permisos 
//		y de acuerdo a que documento se seleccione
		try {
			docuMoviIngrTmps = compra.buscarDocuMoviIngrs();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error buscar documentos forma pago Ingreso - Tmp"));
			e.printStackTrace();
		}

		try {
			formPagoTmps = compra.buscarFormPago();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error buscar formas de pago - Tmp"));
			e.printStackTrace();
		}
	}

	public void iniciarCompraDeta() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return; // Skip postback requests.
		}

		this.buscarProdPrecs(0);

		this.ingrDetaDataTable = new ArrayList<IngrDeta>();
		List<IngrDeta> ingreDetas = new ArrayList<>();

		IngrDeta ingrDetaBuscar = new IngrDeta();
		Ingreso ingreso = new Ingreso();
		ingrDetaBuscar.setIngreso(ingreso);

		if (this.ingresoId != null) {

			ingrDetaBuscar.getIngreso().setIngresoId(this.ingresoId);

			try {

				ingreDetas = compra.buscarIngrDetas(ingrDetaBuscar, null);
				for (IngrDeta ingrDeta : ingreDetas) {

					List<IngrDetaImpu> ingrDetaImpus = new ArrayList<>();
					List<IngrDetaPrec> ingrDetaPrecs = new ArrayList<>();

					IngrDetaImpu ingrDetaImpuBuscar = new IngrDetaImpu();
					IngrDetaPrec ingrDetaPrec = new IngrDetaPrec();

//					Si el ingreso esta grabado solo carga el iva porque aun no estan grabados impuestos ni retenciones
//					si ya esta procesado carga los impuestos que ya estan grabados
					if (this.ingreso.getEstado().equals("PR")) {
						ingrDetaImpuBuscar.setIngrDeta(ingrDeta);
						ingrDetaImpus = compra.buscarIngrDetaImpus(ingrDetaImpuBuscar);
						Set<IngrDetaImpu> ingrDetaImpus2 = new HashSet<IngrDetaImpu>(ingrDetaImpus);
						ingrDeta.setIngrDetaImpus(ingrDetaImpus2);
					}

//					Para saber si el producto lleva o no lleva impuestos
//					este valor se muestra en la pantalla
					this.mostrarIva(ingrDetaBuscar);

//					Se asigna prodCost al producto porque despues se lo utiliza 
//					para actualizar costos y precios 
					ProdCost prodCostBuscar = new ProdCost();
					List<ProdCost> prodCosts = new ArrayList<ProdCost>();

					prodCostBuscar.setSucursal(this.variablesSesion.getSucursal());
					prodCostBuscar.setProducto(ingrDeta.getProducto());

					prodCosts = this.buscarProdCosts(prodCostBuscar);
					for (ProdCost prodCost : prodCosts) {
						ingrDeta.getProducto().setProdCost(prodCost);
					}

					ingrDetaPrec.setIngrDeta(ingrDeta);
					ingrDetaPrecs = compra.buscarIngrDetaPrecs(ingrDetaPrec);
					Set<IngrDetaPrec> ingrDetaPrec2 = new HashSet<IngrDetaPrec>(ingrDetaPrecs);
					ingrDeta.setIngrDetaPrecs(ingrDetaPrec2);

					// Buscar Precio de Venta (Erotik)
					for (IngrDetaPrec ingrDetaPrecVenta : ingrDetaPrecs) {
						if (ingrDetaPrecVenta.getPrecio().equals(ingrDeta.getPrecio())) {
							ingrDeta.setPrecioConImpu(ingrDetaPrecVenta.getPrecioConImpu());
						}
					}

					// Envia ingrDeta para colocar los datos de máximo, mínimo y stock actual
					this.agregarControlMaxMin(ingrDeta);
					// Se pinta las advertencias de máximo y mínimo
					this.controlarMaxMin(ingrDeta);

					this.ingrDetaDataTable.add(0, ingrDeta);
				}

//				Ingresa aca para generar los impuestos y retenciones solamente cuando el documento esta grabado
//				ya que si esta procesado los impuestos y retenciones ya estan grabados en la base
//				el metodo crearIngrDetaImpuRetens() se ejecuta cuando se selecciona el proveedor
				if (this.ingreso.getEstado().equals("GR")) {

					this.buscarCopiarPersProvDimm();
					this.crearIngrDetaImpuRetens();
				}

				this.calcularTotalIngres();

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al buscar detalle documento"));
				e.printStackTrace();
			}
		}
	}

	public void iniciarProveedorRegisDialogo() {

		this.persProvRegis = new PersProv();
		this.persProvRegis.setPersona(new Persona());
//		this.persClieRegis.setClieGrup(new ClieGrup());
//		this.persClieRegis.setDimm(new Dimm());

		this.buscarDimmTipoIden();
		this.buscarDimmTipoIdenProvs();
		this.buscarProvGrups();

		if (this.persProvRegis.getPersonaId() == null) {

			this.persProvRegis.setAutori("0");
			this.persProvRegis.setFechaAuto(LocalDate.now());
			this.persProvRegis.setExonerIva(false);
			this.persProvRegis.setRetienRent(true);
			this.persProvRegis.setRetienIva(true);
			this.persProvRegis.setEstado(true);
		}

	}

	// Elimina el registro que se haya seleccionado para cxpSele
	// En caso que ya no se quiera que la nota de credito afecte a ninguna CXP
	public void elimnarCxp() {
		this.cxpSele = new Cxp();
		this.ingreso.setIngreso(this.cxpSele.getIngreso());
	}

	// <<<<<<<<<<<<<<<<<<<< BOTONES >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< BOTONES >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< BOTONES >>>>>>>>>>>>>>>>>>>>

	// Grabar: solamente graba el documentos
	// Procesar: Grabar genera CXP, contabilidad
	// Anular: Elimina ingrDeta, Cxp con sus respectivos pagos, contabilidad,
	// retencion

	// Si se necesita modificar un ingreso se puede:
	// 1. Seleccionar la opcion grabar para modificar el ingreso y modificar
	// manualmente CXP, PAGOS, CONTABILIDAD, RETENCION
	// 2. Anular el documento y volver a procesar y si tiene pagos ingresar
	// manualmente
	// ya que al anular se eliminan todos los movimientos de esta ingreso

	public String grabar() {

		String estadoDocu;
		String navegar = null;

//		Si el usuario solo graba en este campo se pone nulo ya que en el caso que sea 
//		nota de credito se tiene que volver a seleccionar a que documento se va a afectar
//		y esto se grabara al momento de procesar el documento
		this.ingreso.setIngreso(null);

		estadoDocu = this.grabarIngreso("GR");

		if (estadoDocu == "grabado") {

			this.conversationEnd();

			navegar = "/ppsj/ingreso/ingreso/lista.xhtml?faces-redirect=true";

			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Documento grabado No. " + this.ingreso.getNumero()));
		}

		return navegar;
	}

	public String grabarPersProv() {

		String campoTexto;
		String mensaje = "Error al insertar o modificar proveedor";

		campoTexto = this.persProvRegis.getPersona().getCedulaRuc().trim();
		this.persProvRegis.getPersona().setCedulaRuc(campoTexto);

		campoTexto = this.persProvRegis.getPersona().getApelli().trim();
		this.persProvRegis.getPersona().setApelli(campoTexto);

		if (this.persProvRegis.getPersona().getNombre() != null) {
			campoTexto = this.persProvRegis.getPersona().getNombre().trim();
			this.persProvRegis.getPersona().setNombre(campoTexto);
		}

		try {

			if (persProvRegis.getDimm().getDimmId() == 2010
					&& persProvRegis.getPersona().getCedulaRuc().length() != 10) {
				throw new PersonaException("Revisar número de cédula o tipo de identificación");
			}

			if (persProvRegis.getDimm().getDimmId() == 2000
					&& persProvRegis.getPersona().getCedulaRuc().length() != 13) {
				throw new PersonaException("Revisar número de ruc o tipo de identificación");
			}

			if (this.persProvRegis.getPersonaId() == null) {

				Integer persProvId = this.compra.insertarPersProv(persProvRegis);
				if (persProvId != null) {
					this.getPersProv().setPersonaId(persProvId);
					this.ingreso.setPersProv(this.persProvRegis);
					mensaje = "Proveedor Registrado";
				}
			} else {
				this.compra.modificarPersProv(persProvRegis);
				this.ingreso.setPersProv(this.persProvRegis);
				mensaje = "Proveedor Actualizado";
			}

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, mensaje));

		} catch (RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepción - Error cédula o Ruc ya existe"));
			e.printStackTrace();
		} catch (PersonaException pe) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					pe.getMessage() + " .Error al grabar o actualizar proveedor"));
			pe.printStackTrace();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepción - Error al grabar o actualizar proveedor"));
			e.printStackTrace();
		}

		return null;
	}

	// Procesa y actualiza todos los movimientos adicionales: Kardex, Cxp, Costos,
	// Precios, Impuestos y Retenciones
	// Ya que al grabar solamente de graba la cabecera y el detalle
	public String pagar() {

		Integer retencionId = null;

		String estadoDocu = null;

		String estado = this.estadoActual();

		if (estado.equals("AN")) {

			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, "No se puede pagar un documento anulado"));

			return "/ppsj/ingreso/ingreso/lista.xhtml?faces-redirect=true";
		}

		if (estado.equals("GR")) {

			estadoDocu = this.grabarIngreso("PR");

			if (estadoDocu == "grabado") {

				this.conversationEnd();

				if (this.getIngreso().getDocuIngr().getDocumento().getFactor() != 0) {
					this.crearKardex();
				}

				// Grabar retenciones de la cabecera de la factura
				this.grabarIngrDimm();

				// Actualiza costos, precios, genera impuestos y retenciones
				this.actualizarProdCostPrecImpu();

				// Crea cxc y formPagoMoviEgre porque esta pagando directamente desde el modulo
				// de Compras
				// La otra forma de pagar es por el modulo de CXP
				if (this.getIngreso().getDocuIngr().isGeneraCxp()) {

					// Procesa todos los movimientos cuando el tipo de forma de pago "FP" o "AN". La
					// foma de pago "NC"
					// no deberia aperecer ya que es una compra y "NC" aparece solo cuando el
					// documento es nota N/C
					if (this.formPagoMoviIngr.getFormPago().getTipo().equals("FP")
							|| this.formPagoMoviIngr.getFormPago().getTipo().equals("AN")) {

						Integer cxpId = this.crearCxp(true);

						Integer fpmiId = this.insertarFormPagoMoviIngr();

						FormPagoMoviIngr formPagoMoviIngr = new FormPagoMoviIngr();
						Cxp cxp = new Cxp();

						formPagoMoviIngr.setFpmiId(fpmiId);
						cxp.setCxpId(cxpId);

						this.insertarPagoDeta(formPagoMoviIngr, cxp);

					} else if (this.formPagoMoviIngr.getFormPago().getTipo().equals("CR")) {

						this.crearCxp(false);
					}
				} else {
					// Si se cumplen estas dos condiciones es una nota de credito
					if (this.ingreso.getDocuIngr().getDocumento().getFactor() == -1
							&& !this.ingreso.getDocuIngr().isAfectaCost()) {

						// Si se ha seleccionado una CXP osea cxpSele.getCxpId() es difere de null hace
						// movimientos en el modulo de CXP,
						// caso contraio solo hace movimientos en el modulo de contabilidad
						// if (this.cxpSele.getCxpId() != null) {
						// Como se esta procesando una nota de credito todas las formas de pago tiene
						// que generar abono a una cxp
						// Entonces si se selecciono una CXP (cxpSele) se hace un abono caso contrario
						// se hace un anticipo

						if (this.cxpSele.getSaldo().setScale(2, RoundingMode.HALF_UP)
								.compareTo(this.formPagoMoviIngr.getTotal().setScale(2, RoundingMode.HALF_UP)) >= 0) {

							// Se asigna de la lista que selecciono el usuario
							Integer cxpId = this.cxpSele.getCxpId();

							Integer fpmiId = this.insertarFormPagoMoviIngr();

							FormPagoMoviIngr formPagoMoviIngr = new FormPagoMoviIngr();
							Cxp cxp = new Cxp();

							formPagoMoviIngr.setFpmiId(fpmiId);

							cxp.setCxpId(cxpId);

							this.insertarPagoDeta(formPagoMoviIngr, cxp);

							// Modificar estado de cxp. En esta comparacion puede ser que cxcSele.getTotal
							// sea menor que fpmi.getTotal porque en nota de credito no se toman en cuenta
							// las retenciones, entonces el total de puede ser 100 y el total de cxpSele 90,
							// en este caso el saldo de cxp quedaria negativo
							if (this.cxpSele.getSaldo().setScale(2, RoundingMode.HALF_UP).compareTo(
									this.formPagoMoviIngr.getTotal().setScale(2, RoundingMode.HALF_UP)) <= 0) {
								try {

									this.cxpSele.setEstado(true);
									this.compra.modificarCxp(cxpSele);

								} catch (Exception e) {
									e.printStackTrace();
									FacesContext.getCurrentInstance().addMessage(null,
											new FacesMessage(FacesMessage.SEVERITY_WARN, null,
													"No se ha modificado el estado de cuenta por pagar"));
								}
							}
						} else {
							// TODO: insertar como anticipo por la diferencia entre cxc y pago en caso de
							// devolución con retenciones
							// Integer fpmiId = this.insertarFormPagoMoviIngr();

							if (this.formPagoMoviIngr.getNota() == null || this.formPagoMoviIngr.getNota().isEmpty()) {
								this.formPagoMoviIngr.setNota(
										"Anticipo generado por nota de crédito No. " + this.ingreso.getNumero());
							}

							this.insertarFormPagoMoviIngr();

						}
					}
				}

//				Generar Liquidacion de compra electronica
				if (this.getIngreso().getDocuIngr().getDocumeElec().equals("Liquidacion")) {

					if (this.variablesSesion.getFactElecCompraAuto().equals("Activar")) {

//						if (this.validarUrl() == true) {

//							if (this.validarConexion() == true) {

						this.procesarComprobanteElectronicoLiquidacion();

//								Esto ya se hace al autorizar el documento 
//								String estadoDocuElec = this.procesarComprobanteElectronicoLiquidacion();
//								this.ingreso = this.buscarPorId(retencionId);
//								this.ingreso.setMotivoRech(this.detalleProceso);
//								this.ingreso.setEstadoDocuElec(estadoDocuElec);
//								try {
//									compra.modificarRetencion(this.ingreso);
//								} catch (Exception e) {
//									FacesContext.getCurrentInstance().addMessage(null,
//											new FacesMessage(FacesMessage.SEVERITY_INFO, null,
//													"Error al cambiar estado del documento"));
//									e.printStackTrace();
//								}
//							}
//						}
					}
				}
//				Fin Generar Liquidacion de compras

				// Crear la retencion en caso que sea manual o electronica
				// Si el tipoRete es Ninguna crea la retencion manual y vuelve a preguntar
				// para realizar la parte electronica
				if (!this.getIngreso().getDocuIngr().getTipoRete().equals("Ninguna")) {

					// Se buscar la retención en el caso que el documento se haya desprocesado
					// para usar el mismo número de retención generado. Mas para el caso de
					// retención electrónica
					// Si se necesita usar otra retención se tiene que anular la compra
					Retencion retencionBuscar = new Retencion();
					List<Retencion> retencions = new ArrayList<>();
					retencionBuscar.setIngreso(ingreso);

					try {
						retencions = this.compra.buscarRetencions(retencionBuscar);
					} catch (Exception e) {
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Error al buscar retención"));
						e.printStackTrace();
					}
					// Fin buscar retención

					if (retencions.size() == 0) {
						this.retencion = this.crearRetencion();
					} else if (retencions.size() == 1) {
						this.retencion = retencions.get(0);
					} else if (retencions.size() > 1) {
						this.retencion = retencions.get(0);
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
								null,
								"Se ha encontrado mas de una retención. Los valores se aplicaran a la primera encontrada"));
					}

					this.reteDetas = this.crearReteDetas(retencion);

					if (this.reteDetas.size() != 0) {

						retencion.setReteDetas(reteDetas);

						// Insertar o modificar depende si encontro retencion o no
						// Insertar seria una compra nueva, modificar una compra reprocesada
						// retencionId es para volver a buscar en caso que sea retencion electronica
						if (retencions.size() == 0) {

							// Graba el nuevo secuencial de retencion en parametro ya que al crearRetencion
							// se asigno dicho numero
							Parametro parametro = new Parametro();
							parametro = this.buscarParametro(104004);
							parametro.setDescri(Integer.toString(retencion.getNumero()));
							this.grabarParametro(parametro);

							retencionId = this.insertarRetencion(retencion);
						} else {
							try {

								retencionId = retencion.getRetencionId();
								retencion.setEstado("PR");
								this.compra.modificarRetencion(retencion);

							} catch (Exception e) {
								FacesContext.getCurrentInstance().addMessage(null,
										new FacesMessage(FacesMessage.SEVERITY_INFO, null,
												"Error al modificar - reprocesar retención "));
								e.printStackTrace();
							}
						}

						if (this.getIngreso().getDocuIngr().getTipoRete().equals("Electronica")) {
//							Si el parametro es true envia la factura electrónica
//							si es false quiere decir que el envio es manual y 
//							se debe enviar desde la pagina reprocesar
							if (this.variablesSesion.getFactElecCompraAuto().equals("Activar")) {

//								if (this.validarUrl() == true) {

//									if (this.validarConexion() == true) {

								String estadoDocuElec = this.procesarComprobanteElectronico();

								this.retencion = this.buscarRetencionPorId(retencionId);
								this.retencion.setMotivoRech(this.detalleProceso);
								this.retencion.setEstadoDocuElec(estadoDocuElec);

								try {

									compra.modificarRetencion(this.retencion);

								} catch (Exception e) {
									FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
											FacesMessage.SEVERITY_INFO, null, "Error al cambiar estado del documento"));
									e.printStackTrace();
								}

								// this.crearRideRetencionPdf(retencionId);

//									} else {
//										FacesContext.getCurrentInstance().addMessage(null,
//												new FacesMessage(FacesMessage.SEVERITY_INFO, null,
//														"No existe conexión con el Sri, documento no enviado"));
//									}
//								} else {
//									FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
//											FacesMessage.SEVERITY_INFO, null, "Url no esta configurado correctamente"));
//								}
							}
						}
					} // fin if (this.reteDetas.size() != 0)
				}

				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
						"Documento procesado No. " + this.ingreso.getNumero()));
			}
		}

//		Se contabiliza por separado compras y pagos en el caso que se quiera contabilizar
//		la compra pero no el pago o el pago pero no la compra

//		Contabiliza Compra
		if (this.getIngreso().getDocuIngr().getDocumento().isContab()) {

			this.contabilizarCompra(this.ingreso);

		}

//		Contabiliza Pago
//		la compra pero no el pago o el pago pero no la compra
//		Si el documento Genera CXP y si el factor == 1
//		Si el documento no generaCxp se entiende que es una nota de credito, ingreso
//		o egreso de bodega. Las notas de credito generan abonos, pero el abono se
//		resta del modulo CXP y solamente se contabiliza la nota de credito,
//		no el pago, como en el caso de que sea una compra que se esta pagando con cheque
		if (this.ingreso.getDocuIngr().isGeneraCxp()
				&& this.getIngreso().getDocuIngr().getDocumento().getFactor() == 1) {

//			Deberia entrar aca si el documento es diferente de nota de credito
//		 	entonces pregunta si la forma de pago procesa el credito, entonces se
//		 	entiende que de acuerdo a la forma de pago se selecciono una 
//		 	diferente de credito y por lo tanto se procede a contabilizar 
//		 	el pago que se esta haciendo directamente en este modulo (Compra)
//		 	Si la forma de pago no procesara el credito se contabiliza solamente la
//		 	compra como tal

//			Si el documento esta marcado para contabilizar se procede a generar
//			la transaccion
			if (this.formPagoMoviIngr.getDocuMoviIngr().getDocumento().isContab()) {

				if (this.formPagoMoviIngr.getFormPago().getTipo().equals("FP")
						|| this.formPagoMoviIngr.getFormPago().getTipo().equals("AN")) {

					this.contabilizarPago(formPagoMoviIngr);
				}
			}
		}

		return estadoDocu;
	}

	// Se ejecuta al dar click sobre algun dato en la lista de cxp (cxpList)
	// Se le colaca para actualizar en numero de documento que se va a modificar del
	// outputLabel id="cxpSele"
	// Se coloca el ingreso que se esta modificando con en caso de ser nota de
	// credito y con esto
	// se graba en la base en el campo ingreso_supe_id
	public void seleccionarCxp() {
		this.ingreso.setIngreso(this.cxpSele.getIngreso());
	}

	public String grabarIngreso(String estado) {

		String estadoDocu;

		// Validar Licencia
		if (!idenSistema.getEstadoLicencia().equals("activada")) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Licencia no activada"));

			Long registros = this.contarIngresos();
			if (registros >= 10) {

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"El periodo de prueba ha finalizado. Por favor comuniquese con su distribuidor para obtener su licencia"));

				return null;
			}
		}
		// Fin Validar Licencia

		try {

			ingreso.setTotal(this.totalDocumento);

			ingreso.setFechaHoraEmis(ingreso.getFechaEmis().atTime(LocalTime.now()));
			ingreso.setFechaHoraRegi(ingreso.getFechaRegi().atTime(LocalTime.now()));

			ingreso.setEstado(estado);

			// Graba cabecera
			if (ingresoId == null) {

				// Se coloca la sucursal en el caso de que se este convirtiendo documento
				Sucursal sucursal = new Sucursal();
				if (this.rolSucuConvertir == null) {
					sucursal = this.variablesSesion.getSucursal();
				} else {
					sucursal = this.rolSucuConvertir.getSucursal();
				}
				// Fin Se coloca la sucursal en el caso de que se este convirtiendo documento

				ingreso.setSucursal(sucursal);
				Bodega bodega = new Bodega();
				bodega.setBodegaId(1);
				ingreso.setBodega(bodega);

//				Genera numeración automatica si es ingreso o egreso
				if (this.ingreso.getDocuIngr().isAfectaCost()) {

					this.ingreso.setNumero(this.ingreso.getDocuIngr().getDocumento().getNumero() + 1);

//					Actualiza el numero secuencial del documento
					this.ingreso.getDocuIngr().getDocumento().setNumero(this.ingreso.getNumero());
					this.compra.modificarDocumento(this.ingreso.getDocuIngr().getDocumento());

					ingreso.setClaveAcce(ingreso.getAutori());
				}

//				Si es liquidacion en compras generar la clave de acceso
				if (this.getIngreso().getDocuIngr().getDocumeElec().equals("Liquidacion")) {

//					Genera numeración automatica si liquidación
					this.ingreso.setNumero(this.ingreso.getDocuIngr().getDocumento().getNumero() + 1);

//					Actualiza el numero secuencial del documento
					this.ingreso.getDocuIngr().getDocumento().setNumero(this.ingreso.getNumero());
					this.compra.modificarDocumento(this.ingreso.getDocuIngr().getDocumento());
//					Fin Genera numeración automatica si liquidación

					try (Formatter formatter = new Formatter()) {
						String numero = formatter.format("%09d", ingreso.getNumero()).toString();

						String claveAcceso = documeElectRegis.generarClaveAcceso(
								this.getVariablesSesion().getSucursal(), this.ingreso.getFechaRegi(),
								this.getIngreso().getDocuIngr().getDimm().getCodigo(), ingreso.getSerie1(),
								ingreso.getSerie2(), numero, this.getIngreso().getDocuIngr().getAmbien(),
								this.getIngreso().getDocuIngr().getTipoEmis(), variablesSesion.getCodiClav());
						// Fin Generar clave acceso

						ingreso.setClaveAcce(claveAcceso);
						ingreso.setAutori(claveAcceso);
					}

				} else {

//					Si es cualquier otro documento que no sea liquidacion Ej: Factura-Compra
					ingreso.setClaveAcce(ingreso.getAutori());
				}

				this.ingreso.setLeyen1(variablesSesion.getLeyenda1());
				this.ingreso.setLeyen2(variablesSesion.getLeyenda2());
				this.ingreso.setMsgInfoAdic(variablesSesion.getMsgInfoAdicional());

				this.ingreso.setEstadoDocuElec("NO ENVIADO");
				compra.grabar(ingreso);

			} else {
				compra.modificar(ingreso);
			}

			// Graba Detalles
			for (IngrDeta ingrDeta : this.ingrDetaDataTable) {

				// Se coloca la entidad ingreso en ingrDeta ya que al buscar ingrDetas no buscar
				// ingreso no existe graph en la busqueda
				ingrDeta.setIngreso(this.ingreso);

				if (ingrDeta.getIngrDetaId() == null) {
					compra.grabarIngrDeta(ingrDeta);
				} else {
					compra.modificarIngrDeta(ingrDeta);
				}
			}

			// Elimina ingreDeta usa CascadeDelete para eliminar ingrDetaImpu
			for (IngrDeta ingrDeta : this.ingrDetaEliminados) {
				// Se busca nuevamente por Id para que el registro vuela a relacionarse con
				// IngrDetaImpu
				// sino borra todos los IngrDetaImpu de todas las facturas
				IngrDeta ingrDetaEliminar = new IngrDeta();
				ingrDetaEliminar = compra.buscarIngrDetaPorId(ingrDeta);
				compra.eliminarIngrDeta(ingrDetaEliminar);
			}

			estadoDocu = "grabado";

		} catch (Exception e) {
			estadoDocu = null;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar documento"));
			e.printStackTrace();
		}

		return estadoDocu;
	}

	public Long contarIngresos() {

		Long totalIngresos = null;

		Ingreso ingreso = new Ingreso();
		PersProv persProv = new PersProv();
		persProv.setPersona(new Persona());

		DocuIngr docuIngr = new DocuIngr();
		docuIngr.setDocumento(new Documento());

		ingreso.setSucursal(new Sucursal());

		ingreso.setPersProv(persProv);
		ingreso.setDocuIngr(docuIngr);

		try {
			totalIngresos = compra.contarIngresos(ingreso);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al contar ingresos"));
			e.printStackTrace();
		}

		return totalIngresos;
	}

	public String validarAnular() {

		String mensaje = "validado";

		Ingreso ingreso = new Ingreso();
		List<Ingreso> ingresos = new ArrayList<>();

		PersProv persProv = new PersProv();
		persProv.setPersona(new Persona());

		DocuIngr docuIngr = new DocuIngr();
		docuIngr.setDocumento(new Documento());

		ingreso.setSucursal(new Sucursal());
		ingreso.setPersProv(persProv);
		ingreso.setDocuIngr(docuIngr);

		ingreso.setIngreso(new Ingreso());
		ingreso.getIngreso().setIngresoId(this.ingreso.getIngresoId());

		try {
			ingresos = this.compra.buscarIngresos(ingreso);

			if (ingresos.size() == 0) {
				mensaje = "validado";
			}

			// Recorre la lista para verificar que el docuemnto que encontro este anulado
			// caso contrario no debe dejar anular o deaprocesar el docuemento
			for (Ingreso ingreso2 : ingresos) {
				if (ingreso2.getEstado().equals("AN")) {
					mensaje = "validado";
				} else {
					mensaje = "Error no se puede anular o desprocesar - Existe Nota de Crédito para el documento";
					return mensaje;
				}
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar ingreso - ingresoSupe "));
			e.printStackTrace();
		}

		PagoDeta pagoDeta = new PagoDeta();
		pagoDeta.setIngresoId(this.ingreso.getIngresoId());

		List<Object[]> objPagoDetas = new ArrayList<Object[]>();
		try {
			objPagoDetas = compra.buscarPagosPorIngresoId(pagoDeta);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Error al buscar pagos del documento - EgresoId"));
			e.printStackTrace();
		}

		if (!objPagoDetas.isEmpty()) {
			try {
				objPagoDetas = compra.buscarPagosPorFpmeId(objPagoDetas, this.ingreso);

				if (objPagoDetas.isEmpty()) {
					mensaje = "validado";
				} else {
					mensaje = "Error no se puede anular o desprocesar - Documento tiene pagos combinados con otros documentos";
				}
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Error al buscar pagos del documento - FmeFpId"));
				e.printStackTrace();
			}
		}

		return mensaje;
	}

	public String anular() {

		this.conversationEnd();

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		String mensajeValidacion = this.validarAnular();

		if (!mensajeValidacion.equals("validado")) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, mensajeValidacion));
			return "/ppsj/ingreso/ingreso/lista.xhtml?faces-redirect=true";
		}

		try {
			// Se elimina la transaccion de la COMPRA
			if (this.ingreso.getTransaccion() != null) {
				this.transaccionGestion.anularTransaccion(this.ingreso.getTransaccion().getTransaccionId());

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
						"Transacción anulada No. " + this.ingreso.getTransaccion().getNumero()));
			}

			this.anularRetencion("AN");

			// Elimina Cxp solamente si el documento es factura o nota de credito es decir
			// afectaCost = false
			// si afectaCost = true debe ser un ingreso o egreso entonces no se ha generado
			// CXP y no pasa a eliminar
			if (!this.ingreso.getDocuIngr().isAfectaCost()) {

				FormPagoMoviIngr fpmi = new FormPagoMoviIngr();
				List<FormPagoMoviIngr> fpmis = new ArrayList<>();

				fpmi.setPersona(new Persona());
				fpmi.setIngresoId(this.ingreso.getIngresoId());

				fpmis = this.buscarFpmis(fpmi);

//				Anula FPMI en el caso que sea una NC que creo un anticipo
//				Se crea anticipo cuando el valor de la NC es Mayor que el saldo de la CXP
				if (fpmis.size() != 0) {

//					Si encontro fpmis anula transaccion, fpmi, cxp, pagoDeta ya que es un documento pagado, 
//					abonado o anticipo desde el modulo de compra ya que tiene grabado fpmi.ingresoId
					for (FormPagoMoviIngr formPagoMoviIngr : fpmis) {

//						Si es NC que genero un ANTICIPO se anula solamente fpmi ya que no existe cxp ni pagos						
						if (formPagoMoviIngr.getDocuMoviIngr().getTipo().equals("ANTICIPO")) {

//							Anula la transaccion del pago						
							if (formPagoMoviIngr.getTransaccion() != null) {
								this.transaccionGestion
										.anularTransaccion(formPagoMoviIngr.getTransaccion().getTransaccionId());

								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
										FacesMessage.SEVERITY_INFO, null,
										"Transacción anulada No. " + formPagoMoviIngr.getTransaccion().getNumero()));
							}

							this.anularFpmi(formPagoMoviIngr);

						} else {
//							caso contrario esta metodo elimina todo
							this.eliminarCxp();
						}
					}
				} else {
//					caso contrario como no existe fpme quiere decir que es una factura a credito
//					o abonada en el modulo de cxc entonces todo de borra en eliminarCxc()
					this.eliminarCxp();
				}
			}

//			Verifica si esta procesado ya que si esta solo grabado todavia no se genera el kardex
//			y si por alguna razón se agraga un producto y se anula se borra todos los datos del kardex
			if (this.ingreso.getEstado().equals("PR")) {
				this.eliminarKardex();
			}

			this.eliminarIngrDimm();

			// Eliminar IngrDeta y por cascada IngrDetaImpu
			this.compra.anularIngreso(ingreso);
			this.ingrDetaDataTable.clear();
			this.ingrDetaEliminados.clear();

			this.grabarIngreso("AN");

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar anular ingreso"));
			e.printStackTrace();
		}

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
				"Documento anulado No. " + this.ingreso.getNumero()));

		return "/ppsj/ingreso/ingreso/lista.xhtml?faces-redirect=true";
	}

	// Desprocesar
	public String anular2() {

		this.conversationEnd();

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		String mensajeValidacion = this.validarAnular();

		if (!mensajeValidacion.equals("validado")) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, mensajeValidacion));
			return "/ppsj/ingreso/ingreso/lista.xhtml?faces-redirect=true";
		}

		try {

			// Se elimina la transaccion de la COMPRA
			if (this.ingreso.getTransaccion() != null) {
				this.transaccionGestion.anularTransaccion(this.ingreso.getTransaccion().getTransaccionId());

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
						"Transacción anulada No. " + this.ingreso.getTransaccion().getNumero()));
			}

			this.anularRetencion("GR");

			// Elimina Cxp solamente si el documento es factura o nota de credito es decir
			// afectaCost = false
			// si afectaCost = true debe ser un ingreso o egreso entonces no se ha generado
			// CXP y no pasa a eliminar
			if (!this.ingreso.getDocuIngr().isAfectaCost()) {
				FormPagoMoviIngr fpmi = new FormPagoMoviIngr();
				List<FormPagoMoviIngr> fpmis = new ArrayList<>();

				fpmi.setPersona(new Persona());
				fpmi.setIngresoId(this.ingreso.getIngresoId());

				fpmis = this.buscarFpmis(fpmi);

//				Anula FPMI en el caso que sea una NC que creo un anticipo
//				Se crea anticipo cuando el valor de la NC es Mayor que el saldo de la CXP
				if (fpmis.size() != 0) {

//					Si encontro fpmis anula transaccion, fpmi, cxp, pagoDeta ya que es un documento pagado, 
//					abonado o anticipo desde el modulo de ventas ya que tiene grabado fpmi.ingresoId
					for (FormPagoMoviIngr formPagoMoviIngr : fpmis) {

//						Si es NC que genero un ANTICIPO se anula solamente fpmi ya que no existe cxp ni pagos						
						if (formPagoMoviIngr.getDocuMoviIngr().getTipo().equals("ANTICIPO")) {

//							Anula la transaccion del pago						
							if (formPagoMoviIngr.getTransaccion() != null) {
								this.transaccionGestion
										.anularTransaccion(formPagoMoviIngr.getTransaccion().getTransaccionId());

								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
										FacesMessage.SEVERITY_INFO, null,
										"Transacción anulada No. " + formPagoMoviIngr.getTransaccion().getNumero()));
							}

							this.anularFpmi(formPagoMoviIngr);
						} else {
//							caso contrario esta metodo elimina todo
							this.eliminarCxp();
						}
					}
				} else {
//					caso contrario como no existe fpme quiere decir que es una factura a credito
//					o abonada en el modulo de cxc entonces todo de borra en eliminarCxc()
					this.eliminarCxp();
				}
			}

//			Verifica si esta procesado ya que si esta solo grabado todavia no se genera el kardex
//			y si por alguna razón se agraga un producto y se anula se borra todos los datos del kardex
			if (this.ingreso.getEstado().equals("PR")) {
				this.eliminarKardex();
			}

			this.eliminarIngrDetaImpu();
			this.eliminarIngrDimm();

			this.ingrDetaDataTable.clear();
			this.ingrDetaEliminados.clear();
			this.ingreso.setIngreso(null);

			this.grabarIngreso("GR");

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al desprocesar ingreso"));
			e.printStackTrace();
		}

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
				"Documento desprocesado No. " + this.ingreso.getNumero()));

		return "/ppsj/ingreso/ingreso/lista.xhtml?faces-redirect=true";
	}

	public void eliminarIngrDetaImpu() {

		for (IngrDeta ingrDeta : this.ingrDetaDataTable) {

			IngrDetaImpu ingrDetaImpu = new IngrDetaImpu();
			Set<IngrDetaImpu> ingrDetaImpus = new HashSet<>();

			ingrDetaImpu.setIngrDeta(ingrDeta);
			ingrDetaImpus = ingrDeta.getIngrDetaImpus();

			try {

				for (IngrDetaImpu ingrDetaImpuEliminar : ingrDetaImpus) {
					this.compra.eliminarIngrDetaImpu(ingrDetaImpuEliminar);
				}

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al desprocesar ingreso - eliminar detalle impuestos"));
				e.printStackTrace();
			}
		}
	}

	public void anularRetencion(String estado) {

		// No cambia el estado de la retención porque al volver a procesar se vuelve a
		// generar la retención
		Retencion retencionBuscar = new Retencion();
		retencionBuscar.setIngreso(ingreso);

		List<Retencion> retencions = new ArrayList<>();

		try {
			retencions = compra.buscarRetencions(retencionBuscar);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar retención"));
			e.printStackTrace();
		}

		try {
			this.compra.anularRetencion(retencions);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al anular retención"));
			e.printStackTrace();
		}

		try {

			for (Retencion retencionModificar : retencions) {
				retencionModificar.setEstadoDocuElec("NO ENVIADO");
				retencionModificar.setEstado(estado);
				this.compra.modificarRetencion(retencionModificar);
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error cambiar estado de retención"));
			e.printStackTrace();
		}
	}

	public void eliminarCxp() {

		Cxp cxpBuscar = new Cxp();

		// Coloca el ingreso si es factura o el documento modificado si es nota de
		// credito
		// para poder realizar la busqueda de cxc
		if (this.ingreso.getDocuIngr().isGeneraCxp()) {
			cxpBuscar.setIngreso(ingreso);
		} else {
			cxpBuscar.setIngreso(ingreso.getIngreso());
		}

		try {

			List<Cxp> cxps = new ArrayList<>();
			cxps = compra.buscarCxps(cxpBuscar, null, variablesSesion.getFilasPagina());

			for (Cxp cxp : cxps) {

				this.eliminarPagoDeta(cxp);

				if (this.ingreso.getDocuIngr().isGeneraCxp()) {
					this.compra.eliminarCxp(cxp);
				} else {
					// Cambia el estado de la Cxp en caso que haya estado true
					cxp.setEstado(false);
					this.compra.modificarCxp(cxp);
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al eliminar cuentas por pagar"));
			e.printStackTrace();
		}
	}

	// Elimina pagoDeta, coloca estado anulado en FPMI y anula transaccion contable
	public void eliminarPagoDeta(Cxp cxp) {
		PagoDeta pagoDetaBuscar = new PagoDeta();

		cxp.setIngreso(cxp.getIngreso());
		pagoDetaBuscar.setCxp(cxp);

		FormPago formPago = new FormPago();
		FormPagoMoviIngr formPagoMoviIngr = new FormPagoMoviIngr();
		formPagoMoviIngr.setPersona(cxp.getIngreso().getPersProv().getPersona());
		formPagoMoviIngr.setFormPago(formPago);
		pagoDetaBuscar.setFormPagoMoviIngr(formPagoMoviIngr);

		List<PagoDeta> pagoDetas = new ArrayList<>();

		pagoDetaBuscar.setCxp(cxp);

		try {
			pagoDetas = this.compra.buscarPagoDetas(pagoDetaBuscar);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar detalle de pagos"));
			e.printStackTrace();
		}

		for (PagoDeta pagoDetaEliminar : pagoDetas) {

			FormPagoMoviIngr fpmi = new FormPagoMoviIngr();
//			formPagoMoviIngr2 = pagoDetaEliminar.getFormPagoMoviIngr();

			try {
				// Compara los ingresoId para deteminar a que ingreso pertenece el pago
				// y borrar solamente los abonos del documento que se esta procesando
				// si es compra borra todo si es NC borra solo el abono que se hizo con la NC
				if (pagoDetaEliminar.getIngresoId().equals(this.ingreso.getIngresoId())) {

					this.compra.eliminarPagoDeta(pagoDetaEliminar);

//					Buscar fpmi para que encuentre transaccion y poder anular
					fpmi = compra.buscarFpmisPorId(pagoDetaEliminar.getFormPagoMoviIngr().getFpmiId());

					this.anularFpmi(fpmi);

//					Anula la transaccion del pago						
					if (fpmi.getTransaccion() != null) {
						this.transaccionGestion.anularTransaccion(fpmi.getTransaccion().getTransaccionId());

						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
								null, "Transacción anulada No. " + fpmi.getTransaccion().getNumero()));
					}
				}
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al eliminar detalle de pagos"));
				e.printStackTrace();
			}
		}
	}

	public void anularFpmi(FormPagoMoviIngr fpmi) {

//		Buscar y eliminar en caso que el documento haya generado un anticipo
//		Las notas de credito que se hacen contra un documento ya pagado hacen anticipos
//		por lo tanto no tiene Cxc ni cobrDeta, entonces solo anula FPME
		String nota;

		if (fpmi.getNota() != null) {
			nota = fpmi.getNota() + " " + "Anulado o desprocesado desde el modulo compras";
		} else {
			nota = "Anulado o desprocesado desde el modulo compras";
		}

		fpmi.setNota(nota);
//		formPagoMoviEgre2.setTotal(new BigDecimal(0));
		fpmi.setEstado("AN");
//		this.venta.eliminarFpme(formPagoMoviEgre2);
		try {
			this.compra.modificarFpmi(fpmi);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al modificar FPME"));
			e.printStackTrace();
		}
	}

	public void eliminarKardex() {

		List<Kardex> kardexs = new ArrayList<>();

		try {

			for (IngrDeta ingrDeta : this.ingrDetaDataTable) {

				Kardex kardex = new Kardex();
				kardex.setEgreDeta(new EgreDeta());
				kardex.setIngrDeta(ingrDeta);

				kardexs.addAll(compra.buscarKardexs(kardex));
			}

			for (Kardex kardex : kardexs) {
				compra.eliminarKardex(kardex);
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar o eliminar kardex"));
			e.printStackTrace();
		}

	}

	// <<<<<<<<<<<<<<<<<<<< DETALLE DOCUMENTO >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< DETALLE DOCUMENTO >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< DETALLE DOCUMENTO >>>>>>>>>>>>>>>>>>>>

	public void agregarProductoPrev() {
		this.agregarProducto();
//		this.crearIngrDetaImpuRetens();
//		this.calcularTotalIngres(); Se paso a this.crearIngrDetaImpuRetens();
		this.calcularTotalIngres();
	}

	private void crearIngrDetaImpuRete(IngrDeta ingrDeta) {

		Set<IngrDetaImpu> ingrDetaImpus = new HashSet<>();
		ingrDetaImpus = this.crearImpuestos("IMPUE", ingrDeta);

//		Buscar Iva del producto, si tiene iva se crea ingrDetaImpu sino no se crea ingrDetaImpu
		ProdDimm prodDimmBuscar = new ProdDimm();
		prodDimmBuscar.setDimm(new Dimm());
		prodDimmBuscar.setProducto(ingrDeta.getProducto());
		List<ProdDimm> prodDimms = this.buscarProdDimms(prodDimmBuscar);

		ProdDimm prodDimmIva = new ProdDimm();

		for (ProdDimm prodDimmRecorrer : prodDimms) {
			if (prodDimmRecorrer.getDimm().getTablaRefe().equals("Tabla12")) {

				prodDimmIva = prodDimmRecorrer;
			}
		}

		try {

//			Buscar el Producto para relacionar con prodGrup y poder saber si es Mercaderia o Gasto-Servicio
			Producto producto = this.compra.buscarProductoPorId(ingrDeta.getProducto().getProductoId());

//			Crea Retenciones dependiendo de si los productos son Mercaderia o Gasto-Servicio
//			y de acuerdo a eso selecciona la lista de impuesto que va a crear
			if (producto.getProdGrup().getTipo().equals("Mercaderia")) {

				for (IngrDimm ingrDimm : this.ingrDimms) {

					if (ingrDimm.getTipo().equals("RENTA_BIEN")) {
						ingrDetaImpus.add(this.crearRetenciones(ingrDeta, ingrDimm));
					} else if (ingrDimm.getTipo().equals("IVA_BIEN")) {
//						if (prodDimmIva.getDimm().getDimmId().equals(13030)) {
						if (prodDimmIva.getDimm().getPorcen().compareTo(BigDecimal.ZERO) > 0) {
							ingrDetaImpus.add(this.crearRetenciones(ingrDeta, ingrDimm));
						}
					}
				}

			} else {
//				El producto es un gasto
				for (IngrDimm ingrDimm : this.ingrDimms) {

					if (ingrDimm.getTipo().equals("RENTA_SERV")) {
						ingrDetaImpus.add(this.crearRetenciones(ingrDeta, ingrDimm));
					} else if (ingrDimm.getTipo().equals("IVA_SERV")) {
//						if (prodDimmIva.getDimm().getDimmId().equals(13030)) {
						if (prodDimmIva.getDimm().getPorcen().compareTo(BigDecimal.ZERO) > 0) {
							ingrDetaImpus.add(this.crearRetenciones(ingrDeta, ingrDimm));
						}
					}
				}
			}

			ingrDeta.setIngrDetaImpus(ingrDetaImpus);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Error al buscar id del producto para determinar a que tipo pertenece Mercaderia-Gasto y generar ingrDetaImpu"));
			e.printStackTrace();
		}
	}

	public void agregarProducto() {

		BigDecimal cantidSald;
		BigDecimal cantidEgre = new BigDecimal(0);

		List<KardTotaView> kardTotaViews = new ArrayList<>();
		Set<IngrDetaPrec> ingrDetaPrecs = new HashSet<IngrDetaPrec>();

		this.prodPrecSele.getProducto().setProdGrup(this.getProdPrec().getProducto().getProdGrup());

		// Si el documento se esta modificando y estado del documento no es igual a
		// grabado osea esta PR o AN
		// no puede agregar mas productos ya que si agrega y anula el egreso se borra
		// toda la tabla Kardex
		if (this.ingresoId != null && !this.ingreso.getEstado().equals("GR")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"No se puede agregar productos en documentos procesados o anulados"));
			return;
		}

//		Agrega precio de venta al publico como referencia (Erotik)
//		ingrDeta.setPrecioConImpu(prodPrecSele.getPrecioConImpu());
		IngrDeta ingrDeta = new IngrDeta(ingreso, prodPrecSele.getProducto(), prodPrecSele.getPrecio(),
				ingreso.getFechaRegi(), ingreso.getFechaEmis().atTime(LocalTime.now()), new BigDecimal(1),
				prodPrecSele.getFactor(), new BigDecimal(0), new BigDecimal(0), BigDecimal.ZERO, new BigDecimal(0),
				BigDecimal.ZERO, prodPrecSele.getPrecioConImpu());

//		Buscar costo actual - promedio , cantidad actual
		KardTotaView kardTotaView = new KardTotaView();

		kardTotaView.setSucursalId(variablesSesion.getSucursal().getSucursalId());
		kardTotaView.setBodegaId(1);
		kardTotaView.setProductoId(prodPrecSele.getProducto().getProductoId());

		kardTotaViews = this.buscarKardTotaViews(kardTotaView);

		// Si es ingreso o egreso de bodega poner el costo promedio no el ultimo costo
		// porque eso modificaria el costo del producto si es egreso incluso bloquea el
		// campo para que no se pueda modificar
		if (this.ingreso.getDocuIngr().isAfectaCost()) {
//			ingrDeta.setCosto(prodCost.getCostoActu());
			ingrDeta.setCosto(ingrDeta.getProducto().getProdCost().getCostoActu());
		} else {
//			ingrDeta.setCosto(prodCost.getCostoUlti());
			ingrDeta.setCosto(ingrDeta.getProducto().getProdCost().getCostoUlti());
		}

//		Valida en el caso que no exista ningun movimiento de ingreso
//		es decir que directamente se va registrar egresos, 
//		por lo tanto el producto no tiene cantidad ni costo
		if (kardTotaViews.size() != 0) {
//			costo = kardTotaViews.get(0).getCosto().divide(this.prodPrecSele.getFactor());
//			costo = kardTotaViews.get(0).getC();
			cantidSald = kardTotaViews.get(0).getCantidSald();
		} else {
//			costo = new BigDecimal(0);
			cantidSald = new BigDecimal(0);
		}

// 		Controla inventario solamente si el documento es egreso (factor < 0)
//		Se hace un poco diferente a la venta porque en venta se acumula la cantidad
//		en una sola linea y en compra se puede agregar el mismo producto mas de una vez
		if (this.factor < 0) {

//			Recorre la lista de productos agregados para saber si el que se esta pasando ya esta en la lista
//			y sumar la cantida de la venta = cantidadVenta, para luego controlar el inventario
//			en el caso que se vaya a pasar el producto mas de una vez
			for (IngrDeta ingrDetaBuscar : this.ingrDetaDataTable) {

				if (prodPrecSele.getProducto().getProductoId().equals(ingrDetaBuscar.getProducto().getProductoId())) {

					cantidEgre = cantidEgre.add(ingrDetaBuscar.getCantid());
				}
			}

			cantidEgre = cantidEgre.add(ingrDeta.getCantid());

			if (cantidSald.compareTo(cantidEgre.multiply(ingrDeta.getFactor())) == -1) {

				if (prodPrecSele.getProducto().getControStoc().equals("Controla")) {

					PrimeFaces.current().ajax().update("compraForm:controlStock");
					PrimeFaces.current().executeScript("PF('controlStockDialogo').show();");

					return;
				}
			}
		}

//		Crea un nuevo dimm para poder almacenar y mostra el porcentaje del iva 
//		al agregar productos esto se hace en el metodo calcularTotalIngres
		ingrDeta.getProducto().setDimm(new Dimm());

		ProdPrec prodPrec = new ProdPrec(this.getVariablesSesion().getSucursal(), new Precio(), ingrDeta.getProducto());
		ingrDetaPrecs = this.crearIngrDetaPrec(ingrDeta, prodPrec);
		ingrDeta.setIngrDetaPrecs(ingrDetaPrecs);

//		Se crea impuestos y retenciones del producto que se esta agregando
		this.crearIngrDetaImpuRete(ingrDeta);

//		Para saber si el producto lleva o no lleva impuestos
//		este valor se muestra en la pantalla
		this.mostrarIva(ingrDeta);

		this.ingrDetaDataTable.add(0, ingrDeta);

		// Envia ingrDeta para colocar los datos de máximo, mínimo y stock actual
		this.agregarControlMaxMin(ingrDeta);
		// Se pinta las advertencias de máximo y mínimo
		this.controlarMaxMin(ingrDeta);
	}

//	Para saber si el producto lleva o no lleva impuestos
//	este valor se muestra en la pantalla
	public void mostrarIva(IngrDeta ingrDeta) {
		for (IngrDetaImpu ingrDetaImpu : ingrDeta.getIngrDetaImpus()) {
//			Coloca el porcentaje del iva para mostrar en pantalla
			if (ingrDetaImpu.getTipo().equals("IVA")) {
				ingrDeta.getProducto().setDimm(new Dimm());
				ingrDeta.getProducto().getDimm().setPorcen(ingrDetaImpu.getPorcen());
			}
		}
	}

	public void agregarControlMaxMin(IngrDeta ingrDeta) {

		Bodega bodega = new Bodega();
		bodega.setBodegaId(1);

		ProdBode prodBodeBuscar = new ProdBode();
		prodBodeBuscar.setSucursal(this.variablesSesion.getSucursal());
		prodBodeBuscar.setBodega(bodega);
		prodBodeBuscar.setProducto(ingrDeta.getProducto());

		List<ProdBode> prodBodes = this.buscarProdBode(prodBodeBuscar);

		KardTotaView kardTotaViewBuscar = new KardTotaView();
		kardTotaViewBuscar.setSucursalId(this.variablesSesion.getSucursal().getSucursalId());
		kardTotaViewBuscar.setBodegaId(1);
		kardTotaViewBuscar.setProductoId(ingrDeta.getProducto().getProductoId());

		List<KardTotaView> kardTotaViews = this.buscarKardTotaViews(kardTotaViewBuscar);

		if (prodBodes.size() != 0) {
			ProdBode prodBode = prodBodes.get(0);
			ingrDeta.getProducto().setMaximo(prodBode.getMaximo());
			ingrDeta.getProducto().setMinimo(prodBode.getMinimo());
		} else {
			ingrDeta.getProducto().setMaximo(new BigDecimal(0));
			ingrDeta.getProducto().setMinimo(new BigDecimal(0));
		}

		if (kardTotaViews.size() != 0) {
			KardTotaView kardTotaView = kardTotaViews.get(0);
			ingrDeta.getProducto().setStockActual(kardTotaView.getCantidSald());
		} else {
			ingrDeta.getProducto().setStockActual(new BigDecimal(0));
		}
	}

	public void controlarMaxMin(IngrDeta ingrDeta) {

		BigDecimal cantidadTotal = new BigDecimal(0);

		// Suma o resta del saldo actual dependiendo si el documento es compra, ingreso
		// o N/C, egreso
		if (this.ingreso.getDocuIngr().getDocumento().getFactor() >= 0) {
			cantidadTotal = ingrDeta.getProducto().getStockActual().add(ingrDeta.getCantid());
		} else {
			cantidadTotal = ingrDeta.getProducto().getStockActual().subtract(ingrDeta.getCantid());
		}

		// int maximo = kardTotaView.getCantidSald().compareTo(prodBode.getMaximo());
		if (ingrDeta.getProducto().getMaximo().compareTo(new BigDecimal(0)) != 0
				&& cantidadTotal.compareTo(ingrDeta.getProducto().getMaximo()) >= 0) {
			ingrDeta.getProducto().setControlMaxMin("maximo");
		} else {
			ingrDeta.getProducto().setControlMaxMin(null);
		}

		// int minimo = kardTotaView.getCantidSald().compareTo(prodBode.getMinimo());
		if (ingrDeta.getProducto().getControlMaxMin() == null) {
			if (ingrDeta.getProducto().getMinimo().compareTo(new BigDecimal(0)) != 0
					&& cantidadTotal.compareTo(ingrDeta.getProducto().getMinimo()) <= 0) {
				ingrDeta.getProducto().setControlMaxMin("minimo");
			} else {
				ingrDeta.getProducto().setControlMaxMin("null");
			}
		}
	}

	public List<ProdBode> buscarProdBode(ProdBode prodBode) {

		List<ProdBode> prodBodes = new ArrayList<>();

		try {
			prodBodes = compra.buscarProdBodes(prodBode);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Ex - Error al buscar Producto-Bodega"));
			e.printStackTrace();
		}
		return prodBodes;
	}

	public ProdBode recorrerProdBodes(List<ProdBode> ProdBodes) {

		ProdBode prodBode = new ProdBode();

		for (ProdBode prodBodeBuscar : ProdBodes) {
			prodBode = prodBodeBuscar;
		}

		ProdBodes.get(0);

		return prodBode;
	}

//	Reemplaza a modificarCelda
	public void modificarDatos(IngrDeta ingrDeta) {

		this.descue = new BigDecimal(0);
		BigDecimal cantidEgre = new BigDecimal(0);
		BigDecimal cantidSald = new BigDecimal(0);

		Producto producto = new Producto();

		List<KardTotaView> kardTotaViews = new ArrayList<>();

		this.controlarMaxMin(ingrDeta);

		if (ingrDeta.getDescueValo().compareTo(ingrDeta.getTotal()) > 0) {
			ingrDeta.setDescueValo(new BigDecimal(0));
		}

		producto = ingrDeta.getProducto();

		if (this.factor < 0) {

			KardTotaView kardTotaView = new KardTotaView();

			kardTotaView.setSucursalId(variablesSesion.getSucursal().getSucursalId());
			kardTotaView.setBodegaId(1);
			kardTotaView.setProductoId(producto.getProductoId());

			kardTotaViews = this.buscarKardTotaViews(kardTotaView);

			if (kardTotaViews.size() != 0) {
				cantidSald = kardTotaViews.get(0).getCantidSald();
			} else {
				cantidSald = new BigDecimal(0);
			}

			if (kardTotaViews.size() != 0) {

//					Recorre la lista de productos agregados para saber si el que se esta pasando ya esta en la lista
//					y sumar la cantida de la venta = cantidadVenta, para luego controlar el inventario
//					en el caso que se vaya a pasar el producto mas de una vez
				for (IngrDeta ingrDetaBuscar : this.ingrDetaDataTable) {

					if (ingrDeta.getProducto().getProductoId().equals(ingrDetaBuscar.getProducto().getProductoId())) {

						cantidEgre = cantidEgre.add(ingrDetaBuscar.getCantid());
					}
				}

				if (cantidSald.compareTo((BigDecimal) (cantidEgre.multiply(ingrDeta.getFactor()))) == -1) {

					if (producto.getControStoc().equals("Controla")) {

						ingrDeta.setCantid((new BigDecimal(1)));

						PrimeFaces.current().ajax().update("compraForm:controlStock");
						PrimeFaces.current().executeScript("PF('controlStockDialogo').show();");
					}
				}
			} else {

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
						"Información - Producto no tiene movimientos - kardTotaView"));
			}
		}

		this.calcularTotalIngres();
	}

	@Deprecated
	public void modificarCelda(CellEditEvent<?> event) {

		DataTable dataTable = (DataTable) event.getSource();

		this.descue = new BigDecimal(0);
		BigDecimal cantidEgre = new BigDecimal(0);
		BigDecimal cantidSald = new BigDecimal(0);

		Producto producto = new Producto();
		IngrDeta ingrDeta = (IngrDeta) dataTable.getRowData();

		List<KardTotaView> kardTotaViews = new ArrayList<>();

		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		this.controlarMaxMin(ingrDeta);

		if (newValue != null) {

			if (ingrDeta.getDescueValo().compareTo(ingrDeta.getTotal()) > 0) {
				ingrDeta.setDescueValo(new BigDecimal(0));
			}

			producto = ingrDeta.getProducto();

			if (this.factor < 0) {

				KardTotaView kardTotaView = new KardTotaView();

				kardTotaView.setSucursalId(variablesSesion.getSucursal().getSucursalId());
				kardTotaView.setBodegaId(1);
				kardTotaView.setProductoId(producto.getProductoId());

				kardTotaViews = this.buscarKardTotaViews(kardTotaView);

				if (kardTotaViews.size() != 0) {
					cantidSald = kardTotaViews.get(0).getCantidSald();
				} else {
					cantidSald = new BigDecimal(0);
				}

				if (kardTotaViews.size() != 0) {

//					Recorre la lista de productos agregados para saber si el que se esta pasando ya esta en la lista
//					y sumar la cantida de la venta = cantidadVenta, para luego controlar el inventario
//					en el caso que se vaya a pasar el producto mas de una vez
					for (IngrDeta ingrDetaBuscar : this.ingrDetaDataTable) {

						if (prodPrecSele.getProducto().getProductoId()
								.equals(ingrDetaBuscar.getProducto().getProductoId())) {

							cantidEgre = cantidEgre.add(ingrDetaBuscar.getCantid());
						}
					}

					if (cantidSald.compareTo((BigDecimal) (cantidEgre.multiply(ingrDeta.getFactor()))) == -1) {

						if (producto.getControStoc().equals("Controla")) {

							ingrDeta.setCantid((BigDecimal) oldValue);

							PrimeFaces.current().ajax().update("compraForm:controlStock");
							PrimeFaces.current().executeScript("PF('controlStockDialogo').show();");
						}
					}
				} else {

					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							null, "Información - Producto no tiene movimientos - kardTotaView"));
				}
			}

		}

		this.calcularTotalIngres();
	}

//	Se ejecuta al seleccionar el tipo de impuesto que tiene 
//	en la cabecera de la factura (ingrDimm)
	public void seleccionarTipoImpuRete() {

		this.crearIngrDetaImpuRetens();
		this.calcularTotalIngres();

	}

//	Se ejecuta al seleccionar el tipo de impuesto que tiene 
//	en la cabecera de la factura (ingrDimm)
	public void seleccionarDimmImpuRete() {

		this.crearIngrDetaImpuRetens();
		this.calcularTotalIngres();

	}

	public String modificarIngrDetas() {

		return "/ppsj/ingreso/compra/compraDeta?faces-redirect=true&ingresoId=" + this.getIngresoId();

	}

	// Este metodo llena la lista de los productos que se van a agregar al documento
	public void buscarProdPrecs(Integer paginador) {

		if (paginador == 0) {
			this.pagina = 0;
		}
		if (this.prodPrec.getProducto().getCodigoBarra() != null) {
			this.prodPrec.getProducto().setCodigo(null);
			this.prodPrec.getProducto().setDescri(null);
		}

		prodPrec.setSucursal(this.getVariablesSesion().getSucursal());
		prodPrec.setPrecio(this.precioPred);
		prodPrec.setProducto(this.prodPrec.getProducto());

		this.prodPrec.getProducto().getProdGrup().setModuloComp(true);
		this.prodPrec.getProducto().setEstado(true);

		try {

			this.prodPrecs = this.buscarProdPrecs(prodPrec, this.pagina, variablesSesion.getFilasProductosEgreso(),
					this.ordenColumna);
			this.numeroReg = compra.tamanioProdPrecs();
			this.contadorReg = compra.contarProdPrecs(prodPrec);

//			Asignar el costo a la lista de productos
			for (ProdPrec prodPrec : this.prodPrecs) {

				ProdCost prodCostBuscar = new ProdCost();
				List<ProdCost> prodCosts = new ArrayList<ProdCost>();

				prodCostBuscar.setSucursal(this.variablesSesion.getSucursal());
				prodCostBuscar.setProducto(prodPrec.getProducto());

//				Asigna el costo a cada elemento de la lista de prodPrec
				prodCosts = this.buscarProdCosts(prodCostBuscar);
				for (ProdCost prodCost : prodCosts) {
					prodPrec.getProducto().setProdCost(prodCost);
				}
			}

//			Controla cuando se esta pasado el producto por el campo codigoBarra para agregar 
//			automaticamente el producto a la lista de compras
			if (this.prodPrec.getProducto().getCodigoBarra() != null && this.prodPrecs.size() == 1) {

				this.prodPrecSele = this.prodPrecs.get(0);

//				revisar si el metodo calcularTotalIngres() va aqui o cambiarlo al metodo agregarProducto()
				this.agregarProducto();
				this.calcularTotalIngres();

				this.prodPrec.getProducto().setCodigoBarra(null);
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar productos"));
			e.printStackTrace();
		}
	}

//	Esta busqueda se usa para poder asignar prodCost a la lista de productos (prodPrecs) 
//	y a la lista de detalle de productos (egreDetaDataTable)
	public List<ProdCost> buscarProdCosts(ProdCost prodCost) {

		List<ProdCost> prodCosts = new ArrayList<ProdCost>();

		try {
			prodCosts = this.compra.buscarProdCosts(prodCost);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar costos de productos"));
			e.printStackTrace();
		}

		return prodCosts;
	}

	// Carga una nueva lista de productos que se tienen que eliminar
	// Se elimina al grabarIngreso
	public void eliminarIngrDeta() {

		this.ingrDetaDataTable.remove(ingrDetaSele);

		try {

			if (ingrDetaSele.getIngrDetaId() != null) {

				ingrDetaEliminados.add(ingrDetaSele);

			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al eliminar detalle documento"));
			e.printStackTrace();

		}

		this.calcularTotalIngres();
	}

	public void buscarDimmTipoIden() {
		Dimm dimmDesde = new Dimm();
		Dimm dimmHasta = new Dimm();

		dimmDesde.setTablaRefe("Tabla2p");
		dimmDesde.setEstado(true);
		try {
			this.dimmTipoIdens = compra.buscarDimms(dimmDesde, dimmHasta);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
					"Excepcion - Error al buscar tipo de identificación"));
			e.printStackTrace();
		}
	}

	public void buscarDimmTipoIdenProvs() {
		Dimm dimmDesde = new Dimm();
		Dimm dimmHasta = new Dimm();

		dimmDesde.setTablaRefe("Tabla14");
		dimmDesde.setEstado(true);
		try {
			this.dimmTipoIdenProvs = compra.buscarDimms(dimmDesde, dimmHasta);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
					"Excepcion - Error al buscar tipo identificación cliente"));
			e.printStackTrace();
		}
	}

	public void buscarProvGrups() {

		ProvGrup provGrup = new ProvGrup();
		provGrup.setEstado(true);

		try {
			provGrups = compra.buscarProvGrups(provGrup, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al buscar grupo clientes"));
			e.printStackTrace();
		}
	}

	public List<ProdPrec> buscarProdPrecs(Set<Sucursal> sucursals, Set<Precio> precios, ProdPrec prodPrec,
			String ordenColumna) {

		List<ProdPrec> prodPrecs = new ArrayList<>();

		try {
			prodPrecs = compra.buscarProdPrecs(sucursals, precios, prodPrec, null, ordenColumna);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar precios con sucursales"));
			e.printStackTrace();
		}
		return prodPrecs;
	}

	public List<ProdPrec> buscarProdPrecs(ProdPrec prodPrec, Integer pagina, Integer filas, String ordenColumna) {

		List<ProdPrec> prodPrecs = new ArrayList<>();

		try {
			prodPrecs = compra.buscarProdPrecs(prodPrec, pagina, filas, ordenColumna);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar precios"));
			e.printStackTrace();
		}
		return prodPrecs;
	}

	// <<<<<<<<<<<<<<<<<<<< CABECERA DOCUMENTO >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< CABECERA DOCUMENTO >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< CABECERA DOCUMENTO >>>>>>>>>>>>>>>>>>>>

	public String modificarIngreso() {

		return "compraCabe?faces-redirect=true&ingresoId=" + this.getIngresoId();
	}

	public Dimm buscarIva() {

		Dimm dimmIva = new Dimm();

		try {

			dimmIva = compra.buscarDimmPorId(variablesSesion.getDimmIdIvaActual());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Dimm IVA"));
			e.printStackTrace();
		}

		return dimmIva;
	}

//	Se ejecuta al seleccionar el proveedor, al recuperar una factura grabada
//	al modificar los impuesto de ingrDimm, para porder calcular
//	retenciones de acuerdo a parametros de retencion renta o iva
//	Asigna nuevamente los impuestos seleccionados a todos los productos de la factura
//	si se cambio algun impuesto directamente en el detalle de la factura (ingrDetaImpu)
//	se pierde esta cambio y se tiene que asignar nuevamente
	public void crearIngrDetaImpuRetens() {

//		Set<IngrDetaImpu> ingrDetaImpus = new HashSet<>();

		for (IngrDeta ingrDeta : this.ingrDetaDataTable) {

			this.crearIngrDetaImpuRete(ingrDeta);

//			Para saber si el producto lleva o no lleva impuestos
//			este valor se muestra en la pantalla
			this.mostrarIva(ingrDeta);
		}

		this.calcularTotalIngres();

		if (this.ingreso.getPersProv().getPersonaId() != null) {
			this.saldoProveedor();
		}
	}

	// Crear retenciones de acuerdo a ingrDimm
	// El iva se genera al agregar el producto las retenciones se genera al
	// selecionar el proveedor
//	public Set<IngrDetaImpu> crearIngrDetaImpu(IngrDeta ingrDeta, List<IngrDimm> ingrDimms) {
	public IngrDetaImpu crearRetenciones(IngrDeta ingrDeta, IngrDimm ingrDimm) {

		IngrDetaImpu ingrDetaImpu = this.crearIngrDetaImpu(ingrDeta, ingrDimm.getDimm());

		if (ingrDimm.getTipo().equals("RENTA_BIEN") || ingrDimm.getTipo().equals("RENTA_SERV")) {
			ingrDetaImpu.setTipo("RR");
		} else if (ingrDimm.getTipo().equals("IVA_BIEN") || ingrDimm.getTipo().equals("IVA_SERV")) {
			ingrDetaImpu.setTipo("RI");
		}

		return ingrDetaImpu;
	}

	// Se busca Impuestos en el producto
	public Set<IngrDetaImpu> crearImpuestos(String ingrDetaTipoImpu, IngrDeta ingrDeta) {

		ProdDimm prodDimm = new ProdDimm();
		prodDimm.setDimm(new Dimm());

		List<ProdDimm> prodDimms = new ArrayList<ProdDimm>();
		Set<IngrDetaImpu> ingrDetaImpus = new HashSet<IngrDetaImpu>();

		prodDimm.setProducto(ingrDeta.getProducto());

		try {

			prodDimms = compra.buscarProdDimms(prodDimm);

			for (ProdDimm prodDimm2 : prodDimms) {

				IngrDetaImpu ingrDetaImpu = this.crearIngrDetaImpu(ingrDeta, prodDimm2.getDimm());

				if (ingrDetaTipoImpu.equals("IMPUE")) {

					if (prodDimm2.getDimm().getDimmId() >= 13000 && prodDimm2.getDimm().getDimmId() <= 13099) {
						ingrDetaImpu.setTipo("IVA");
					} else if (prodDimm2.getDimm().getDimmId() >= 13100 && prodDimm2.getDimm().getDimmId() <= 13199) {
						ingrDetaImpu.setTipo("OTR");
					} else if (prodDimm2.getDimm().getDimmId() >= 11000 && prodDimm2.getDimm().getDimmId() <= 11100) {
//						El ultimo código 11100 es el ice de fundas plasticas
						ingrDetaImpu.setTipo("ICE");
					} else if (prodDimm2.getDimm().getDimmId() >= 11800 && prodDimm2.getDimm().getDimmId() <= 11900) {
						ingrDetaImpu.setTipo("IRBPNR");
					}

				}

				if (ingrDetaImpu.getTipo() != null) {
					ingrDetaImpus.add(ingrDetaImpu);
				}
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al crear impuestos y retenciones"));
			e.printStackTrace();
		}

		return ingrDetaImpus;
	}

	public void buscarIngreso() {
		try {
			this.ingreso = compra.buscarIngresoPorId(ingresoId);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar ingreso"));
			e.printStackTrace();
		}
	}

	public List<DocuIngr> buscarDocuIngrs() {

		Documento documento = new Documento();
		documento.setEstado(true);

		DocuIngr docuIngr = new DocuIngr();
		docuIngr.setDocumento(documento);

		List<DocuIngr> docuIngrs = new ArrayList<>();

		try {
			docuIngrs = compra.buscarDocuIngrs(docuIngr);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar documentos ingreso"));
			e.printStackTrace();
		}

		return docuIngrs;
	}

	public void buscarPersProvs(Integer paginador) {

		if (paginador == 0) {
			this.paginaProv = 0;
		}

		if (this.persProv.getPersona().getCedulaRuc() != null) {
			this.persProv.getPersona().setApelli(null);
			this.persProv.getPersona().setNombre(null);
		}

		try {
			this.persProv.setEstado(true);

			this.persProvs = compra.buscarPersProvs(this.persProv, this.paginaProv, variablesSesion.getFilasPagina());
			this.numeroRegProv = persProvs.size();
			this.contadorRegProv = compra.contarRegistrosProv(persProv);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar proveedores"));
			e.printStackTrace();
		}
	}

	public void buscarSustentos() {
		Dimm dimmDesde = new Dimm();
		dimmDesde.setEstado(true);

		dimmDesde.setTablaRefe("Tabla5");

		try {
			this.dimms = compra.buscarSustentos(dimmDesde, dimmDesde);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Sustento"));
			e.printStackTrace();

		}
	}

	// <<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES >>>>>>>>>>>>>>>>>>>>

	// Se utiliza para comunicarse con la pagina y habilitar o inhabilitar botones
	public String estadoActual() {
		return this.getIngreso().getEstado();
	}

	// Este metodo no se usa
	// Se utilizaba para grabar el kardex al momento de grabar el ingreso
	// Ahora se graba el kardex al procesar el ingreso
	public void grabarKardex(IngrDeta ingrDeta) {

		Kardex kardex = new Kardex();
		kardex.setProducto(ingrDeta.getProducto());
		kardex.setDocumento(ingrDeta.getIngreso().getDocuIngr().getDocumento());
		kardex.setIngrDeta(ingrDeta);
		kardex.setEgreDeta(null);
		kardex.setDocumeFech(ingrDeta.getIngreso().getFechaRegi());

		try {
			compra.grabarKardex(kardex);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al crear kardex"));
			e.printStackTrace();

		}

	}

	// Este Procedimiento se utiliza para consultar el inventario
	// al hacer click sobre el icono (lupa) que esta en la pagina
	public List<KardTotaView> buscarKardTotaViews() {

		List<Integer> sucursals = new ArrayList<>(this.sucursals);

		KardTotaView kardTotaView = new KardTotaView();

		kardTotaView.setBodegaId(1);
		kardTotaView.setProductoId(this.productoId);

		try {
			this.kardTotaViews = compra.buscarKardTotaViews(sucursals, kardTotaView);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion KardTotaView - Error al buscar saldos de producto"));
			e.printStackTrace();
		}

		return this.kardTotaViews;
	}

//	Muestra los datos del producto en el dialogo de seleccionar producto - prodPrec
	public void productoExplorarDesdeProdPrec() {

//		Muestra inventario
		buscarKardTotaViews();

//		Muestra precios
		ProdPrec prodPrec = new ProdPrec(variablesSesion.getSucursal(), new Precio(), new Producto());
		prodPrec.getProducto().setProdGrup(new ProdGrup());
		prodPrec.getPrecio().setEstado(true);

		prodPrec.getProducto().setProductoId(productoId);
		prodPrec.getProducto().setEstado(true);

		prodPrecDialogos = buscarProdPrecs(prodPrec);

//		Muestra Costos
		ProdCost prodCostBuscar = new ProdCost();

		prodCostBuscar.setSucursal(this.variablesSesion.getSucursal());
		prodCostBuscar.setProducto(new Producto());

		prodCostBuscar.getProducto().setProductoId(productoId);

		prodCostDialogos = this.buscarProdCosts(prodCostBuscar);
	}

	// Este metodo se utiliza para buscar los costos y luego actualizarlos en todas
	// las sucursales
	public List<KardTotaView> buscarKardTotaViews(KardTotaView kardTotaView, List<Integer> sucursals) {

		try {
			this.kardTotaViews = compra.buscarKardTotaViews(sucursals, kardTotaView);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion KardTotaView - Error al buscar saldos de producto"));
			e.printStackTrace();
		}

		return this.kardTotaViews;
	}

	// Esta busqueda se usa para controlar el maximo y el minimo
	public List<KardTotaView> buscarKardTotaViews(KardTotaView kardTotaView) {

		List<KardTotaView> kardTotaViews = new ArrayList<>();

		try {
			kardTotaViews = this.compra.buscarKardTotaViews(kardTotaView);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Ex - Error al buscar kardex"));
			e.printStackTrace();
		}

		return kardTotaViews;
	}

	// Se ejecuta al abrir el dialogo del pago
	public void cargarFormPagoMoviIngr() {

		DocuMoviIngr docuMoviIngrPred = null;

		// List<DocuMoviIngr> docuMoviIngrTmps = new ArrayList<>();

		this.docuMoviIngrs = new ArrayList<>();
		this.formPagos = new ArrayList<>();

		// Trae el valor del total para utilizarlo en la comparación si es ota de
		// credito y para grabar este total en formPagoMoviIngr
		TotalDocu totalDocu = new TotalDocu();
		totalDocu.setDescri("Total Documento");
		int indice = this.totalDocus.indexOf(totalDocu);

		try {
			// Realiza filtro de acuerdo a los permisos del rol
			this.docuMoviIngrs = compra.filtrarDocuMoviIngrs(this.docuMoviIngrTmps, this.persUsuaSesion, rolDocus);

			// Limpia y asigna las lista nuevamente para realizar mas abajo el segundo
			// filtro de acuerdo al docuEgre seleccionado
			docuMoviIngrTmps.clear();
			docuMoviIngrTmps.addAll(this.docuMoviIngrs);
			this.docuMoviIngrs.clear();

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al filtar documentos de pago"));
			e.printStackTrace();
		}

		try {
			docuMoviIngrPred = compra.buscarDocuMoviIngrPred(this.docuMoviIngrTmps, rolDocus);
			this.formPagoMoviIngr.setDocuMoviIngr(docuMoviIngrPred);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar documento predeterminado"));
			e.printStackTrace();
		}

		// Se le coloca otro try catch para mostrar el mensaje de error respectivo a la
		// comparación con el bigdecimal
		// this.cxpSele.getSaldo().compareTo(new BigDecimal(0)) ya que cxpSele puede ser
		// null
		try {
			for (DocuMoviIngr docuMoviIngr : docuMoviIngrTmps) {

				// Recorrer y filtrar los documentos dependiendo si el documento es factura o
				// N/C
				// Determina si es factura
				if (this.ingreso.getDocuIngr().isGeneraCxp()
						&& this.ingreso.getDocuIngr().getDocumento().getFactor() == 1
						&& !this.ingreso.getDocuIngr().isAfectaCost()) {

					if (docuMoviIngr.getTipo().equals("PAGO-COBRO")) {
						this.docuMoviIngrs.add(docuMoviIngr);
					}
				}

				// Determina si es N/C
				if (!this.ingreso.getDocuIngr().isGeneraCxp()
						&& this.ingreso.getDocuIngr().getDocumento().getFactor() == -1
						&& !this.ingreso.getDocuIngr().isAfectaCost()) {
					// if (this.cxpSele.getCxpId() != null &&
					// docuMoviIngr.getTipo().equals("PAGO-COBRO")) {
					// Se hace este redondeo para poder tener el valor exacto a dos decimales tanto
					// del saldo como del documento
					// porque puede ser cxcSaldo = 54.9989 y totalDocu 54.999952, entonces no
					// fucuncionaria correctamente
					BigDecimal cxpSaldo = this.cxpSele.getSaldo().setScale(2, RoundingMode.HALF_UP);
					BigDecimal totalDocumento = totalDocus.get(indice).getValor().setScale(2, RoundingMode.HALF_UP);
					if (cxpSaldo.compareTo(totalDocumento) >= 0 && docuMoviIngr.getTipo().equals("PAGO-COBRO")) {
						this.docuMoviIngrs.add(docuMoviIngr);
					}

					// if (this.cxpSele.getCxpId() == null &&
					// docuMoviIngr.getTipo().equals("ANTICIPO")) {
					if (cxpSaldo.compareTo(totalDocumento) < 0 && docuMoviIngr.getTipo().equals("ANTICIPO")) {
						this.docuMoviIngrs.add(docuMoviIngr);
					}
				}

				// Es un ingreso o egreso de bodega
				if (!this.ingreso.getDocuIngr().isGeneraCxp() && this.ingreso.getDocuIngr().isAfectaCost()) {

					if (docuMoviIngr.getTipo().equals("PAGO-COBRO")) {
						this.docuMoviIngrs.add(docuMoviIngr);
					}
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Error no ha seleccionado documento que modifique nota de crédito"));
			e.printStackTrace();
		}
		// Esta linea se paso al metodo filtrarFormPago() ya que el filtro se raliza de
		// acuerdo al
		// documento (docuMoviEgre) seleccionado en formPagoMoviEgre
		// this.formPagoMoviIngr.setFormPago(formPagoPred);

		TranPlan tranPlan = new TranPlan();
		List<TranPlan> tranPlans = this.buscarTranPlans();

		tranPlan = this.recorrerTranPlans(tranPlans);

		this.formPagoMoviIngr.setTranPlanId(tranPlan.getTranPlanId());

		this.formPagoMoviIngr.setSucursal(this.getVariablesSesion().getSucursal());
		this.formPagoMoviIngr.setPersona(this.getIngreso().getPersProv().getPersona());
		this.formPagoMoviIngr.setFecha(this.getIngreso().getFechaRegi());
		this.formPagoMoviIngr.setTotal(totalDocus.get(indice).getValor());
		this.formPagoMoviIngr.setEstado("PR");

		this.filtrarFormPago();

		// Se asigna estos valores en caso que la forma predeterminada sea diferente de
		// credito
		// y sea factura
		if (this.ingreso.getDocuIngr().isGeneraCxp() && this.ingreso.getDocuIngr().getDocumento().getFactor() == 1
				&& !this.ingreso.getDocuIngr().isAfectaCost()) {
			if (!this.formPagoMoviIngr.getFormPago().getTipo().equals("CR")) {
				this.ingreso.setDiasPlaz((short) 0);
				this.fechaPago = this.ingreso.getFechaRegi();
			}
		}
	}

	public void filtrarFormPago() {

		TotalDocu totalDocu = new TotalDocu();
		totalDocu.setDescri("Total Documento");
		int indice = this.totalDocus.indexOf(totalDocu);

		FormPago formPagoPred = new FormPago();

		List<RolFormPago> rolFormPagos = variablesSesion.getRolFormPagos();

		try {

			this.formPagos.clear();
			// Realiza el primer filtro de acuerdo al los permisos del Rol
			this.formPagoTmps = compra.filtrarFormPagos(this.formPagoTmps, persUsuaSesion,
					variablesSesion.getRolFormPagos());

			// Realilza el segundo filtro de acuerdo al docuIngr y al docuMoviIngr
			// seleccionado
			for (FormPago formPago : this.formPagoTmps) {

				// Filtra forma de pago de acuerdo a documento y a seleccion de cxpSele
				// Si cumple estas condiciones es una compra, añadir todas las formas de pago
				// excepto nota de credito (NC)
				if (this.ingreso.getDocuIngr().getDocumento().getFactor() == 1
						&& !this.ingreso.getDocuIngr().isAfectaCost()) {

					if (!formPago.getTipo().equals("NC")) {
						this.formPagos.add(formPago);
					}
				}

				// Si cumple estas condiciones es una nota de credito
				if (this.ingreso.getDocuIngr().getDocumento().getFactor() == -1
						&& !this.ingreso.getDocuIngr().isAfectaCost()) {

					// Determinar la lista de formas de pago dependiendo si selecciono o no una
					// cxpSele
					// Si no selecciono el documento que se despliegue sera anticipo y se deplegaran
					// todas las formas de pago de tipo FP
					// Se hace este redondeo para poder tener el valor exacto a dos decimales tanto
					// del saldo como del documento
					// porque puede ser cxcSaldo = 54.9989 y totalDocu 54.999952, entonces no
					// fucuncionaria correctamente
					BigDecimal cxpSaldo = this.cxpSele.getSaldo().setScale(2, RoundingMode.HALF_UP);
					BigDecimal totalDocumento = totalDocus.get(indice).getValor().setScale(2, RoundingMode.HALF_UP);
					if (cxpSaldo.compareTo(totalDocumento) >= 0) {
						// Si selecciono el documento que se despliegue sera Pago-retiro y se deplegara
						// solamente la forma de pago de tipo NC
						if (formPago.getTipo().equals("NC")) {
							this.formPagos.add(formPago);
							formPagoPred = formPago;
						}
					} else {
						if (formPago.getTipo().equals("FP")) {
							this.formPagos.add(formPago);
						}
					}
				}

				// Es un ingreso o egreso de bodega
				if (!this.ingreso.getDocuIngr().isGeneraCxp() && this.ingreso.getDocuIngr().isAfectaCost()) {

					if (formPago.getTipo().equals("CR")) {
						this.formPagos.add(formPago);
					}
				}
			}

		} catch (Exception e) {
			// Cae en esta excepción cuando no se ha seleccionado una cxcSele ya que cxcSele
			// es nulo y no puede realizar la comparación
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error buscar forma de pago"));
			e.printStackTrace();
		}

		// Seleccionar la forma de pago predetermida en rolFormPago
		for (FormPago formPago : formPagos) {
			for (RolFormPago rolFormPago : rolFormPagos) {
				if (formPago.getFormPagoId() == rolFormPago.getFormPago().getFormPagoId()
						&& rolFormPago.getPredet() == true) {
					formPagoPred = formPago;
				}
			}
		}

		// Coloca la forma de pago predeterminada en formPagoMoviEgre
		// Los demas atributos de formPagoMoviEgre se cargan en el metodo
		// cargarFormPagoMoviIngr
		this.formPagoMoviIngr.setFormPago(formPagoPred);

	}

	public void crearKardex() {

		Bodega bodega = new Bodega();
		bodega.setBodegaId(1);

		for (IngrDeta ingrDeta : this.ingrDetaDataTable) {

			Kardex kardex = new Kardex(ingrDeta.getIngreso().getSucursal(), bodega,
					ingrDeta.getIngreso().getDocuIngr().getDocumento(), ingrDeta.getProducto(), ingrDeta, null,
					ingreso.getNumero(), ingrDeta.getIngreso().getFechaRegi(),
					ingrDeta.getIngreso().getFechaHoraRegi());

			try {
				compra.grabarKardex(kardex);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al crear kardex"));
				e.printStackTrace();

			}
		}
	}

	public Integer crearCxp(Boolean estado) {

		Integer id = null;

		Cxp cxp = new Cxp();
		List<Cxp> cxps = new ArrayList<>();

		cxp.setIngreso(this.ingreso);

		try {
			cxps = compra.buscarCxps(cxp, null, variablesSesion.getFilasPagina());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar cuentas por pagar"));
			e.printStackTrace();
		}

		if (cxps.isEmpty()) {

			try {

				TotalDocu totalDocu = new TotalDocu();
				totalDocu.setDescri("Total Documento");
				int indice = this.totalDocus.indexOf(totalDocu);

				// cxps = this.compra.generarCxp(this.ingreso, totalIngr.get("Total Documento"),
				// estado);
				cxps = this.compra.generarCxp(this.ingreso, totalDocus.get(indice).getValor(), estado);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al generar cuentas por pagar"));
				e.printStackTrace();
			}

			try {
				id = compra.insertarCxp(cxps);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al insertar cuentas por pagar"));
				e.printStackTrace();
			}
		}
		return id;
	}

	public void insertarCxp(List<Cxp> cxps) {
		try {
			compra.insertarCxp(cxps);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al insertar cuentas por pagar"));
			e.printStackTrace();

		}
	}

	public void actualizarProdCostPrecImpu() {

		int factorInventario;
		boolean actualizarCostoUlti = false;
		boolean actualizarPrecios = false;

		ProdPrec prodPrec = new ProdPrec();

		List<ProdCost> prodCostsModifi = new ArrayList<>();

		Set<IngrDetaImpu> ingrDetaImpus = new HashSet<>();

		List<ProdPrec> prodPrecs = new ArrayList<>();
		List<ProdPrec> prodPrecsModifi = new ArrayList<>();

		factorInventario = this.ingreso.getDocuIngr().getDocumento().getFactor();
		if (factorInventario == 1) {
			actualizarCostoUlti = variablesSesion.getRolPermiso().get(4201);
			actualizarPrecios = variablesSesion.getRolPermiso().get(4202);
		} else if (factorInventario == 0) {
			actualizarCostoUlti = variablesSesion.getRolPermiso().get(4251);
			actualizarPrecios = variablesSesion.getRolPermiso().get(4252);
		} else if (factorInventario == -1) {
			actualizarCostoUlti = variablesSesion.getRolPermiso().get(4301);
			actualizarPrecios = variablesSesion.getRolPermiso().get(4302);
		}

		for (IngrDeta ingrDeta : ingrDetaDataTable) {

			// Actualiza Costos
			if (actualizarCostoUlti == true) {

				ingrDeta.getProducto().getProdCost().setCostoUlti(
						ingrDeta.getCosto().divide(ingrDeta.getPrecio().getFactor(), 6, RoundingMode.HALF_UP));

				KardTotaView kardTotaViewBuscar = new KardTotaView();

				kardTotaViewBuscar.setBodegaId(1);
				kardTotaViewBuscar.setProductoId(ingrDeta.getProducto().getProductoId());
				kardTotaViewBuscar.setSucursalId(this.variablesSesion.getSucursal().getSucursalId());

				// Buscar y graba el costo promedio
				List<KardTotaView> karTotaViews = this.buscarKardTotaViews(kardTotaViewBuscar);
				for (KardTotaView kardTotaView : karTotaViews) {
//					prodCostActualizar.setCostoActu(kardTotaView.getCosto());
//					prodCostActualizar.setCostoCont(kardTotaView.getCosto());

					ingrDeta.getProducto().getProdCost().setCostoActu(kardTotaView.getCosto());
					ingrDeta.getProducto().getProdCost().setCostoCont(kardTotaView.getCosto());
				}

				prodCostsModifi.add(ingrDeta.getProducto().getProdCost());
			}
			// Fin Actualiza Costos

			// Actualiza Precios
			if (actualizarPrecios == true) {

				// Parametro de busqueda para prodPrec
				Set<Sucursal> sucursals = new HashSet<>();
				Set<Precio> precios = new HashSet<>();

				if (this.paraActualPrecioTodasSucu.getDescri().equals("true")) {
					// Se recorre this.sucursals que es un set de enteros y se cargo anteriomente
					// con los Ids de las sucursales para pasar como parametro de busqueda
					for (Integer sucursalId : this.sucursals) {
						Sucursal sucursal = new Sucursal();
						sucursal.setSucursalId(sucursalId);
						sucursals.add(sucursal);
					}
				} else {
					Sucursal sucursal = new Sucursal();
					sucursal.setSucursalId(this.ingreso.getSucursal().getSucursalId());
					sucursals.add(sucursal);
				}

//				Actualizar los precios en la tabla prodPrec
//				si paraUtilidadActualizaPrecio es false copia lo que este en ingrDetaPrec
//				si es verdadero hace el calculo de los precios de acuerdo a las utilidades
				if (this.paraUtilidadActualizaPrecio.getDescri().equals("false")) {

					for (IngrDetaPrec ingrDetaPrec : ingrDeta.getIngrDetaPrecs()) {

						prodPrecs.clear();
						precios.clear();

						precios.add(ingrDetaPrec.getPrecio());

//						prodPrec.setSucursal(ingrDeta.getIngreso().getSucursal());
//						prodPrec.setPrecio(ingrDetaPrec.getPrecio());
						prodPrec.setProducto(ingrDeta.getProducto());
						prodPrec.getProducto().setProdGrup(new ProdGrup());

//						prodPrecs = this.buscarProdPrecs(prodPrec, null, null, this.ordenColumna);
						prodPrecs = this.buscarProdPrecs(sucursals, precios, prodPrec, this.ordenColumna);

						for (ProdPrec prodPrecActualizar : prodPrecs) {

//							Estos dos campos aqui en la compra estan bloqueados
//							Se debe modificar en el producto, en el producto no se puede modificar
//							el campo setPrecio, pero se puede eliminar y volver a crear
//							prodPrecActualizar.setPrecio(ingrDetaPrec.getPrecio());
//							prodPrecActualizar.setFactor(ingrDetaPrec.getFactor());
							prodPrecActualizar.setUtilid(ingrDetaPrec.getUtilid());
							prodPrecActualizar.setPrecioSinImpu(ingrDetaPrec.getPrecioSinImpu());
							prodPrecActualizar.setPrecioConImpu(ingrDetaPrec.getPrecioConImpu());

							prodPrecsModifi.add(prodPrecActualizar);
						}
					}

				} else {

					this.calcularPrecio(ingrDeta);

					for (IngrDetaPrec ingrDetaPrec : ingrDeta.getIngrDetaPrecs()) {

						prodPrecs.clear();

						precios.add(ingrDetaPrec.getPrecio());

//						prodPrec.setSucursal(ingrDeta.getIngreso().getSucursal());
//						prodPrec.setPrecio(ingrDetaPrec.getPrecio());
						prodPrec.setProducto(ingrDeta.getProducto());
						prodPrec.getProducto().setProdGrup(new ProdGrup());

//						prodPrecs = this.buscarProdPrecs(prodPrec, null, null, this.ordenColumna);
						prodPrecs = this.buscarProdPrecs(sucursals, precios, prodPrec, this.ordenColumna);

						for (ProdPrec prodPrecActualizar : prodPrecs) {

							prodPrecActualizar.setUtilid(ingrDetaPrec.getUtilid());
							prodPrecActualizar.setPrecioSinImpu(ingrDetaPrec.getPrecioSinImpu());
							prodPrecActualizar.setPrecioConImpu(ingrDetaPrec.getPrecioConImpu());

							prodPrecsModifi.add(prodPrecActualizar);
						}
					}
				}
			}
			// Fin Actualiza Precios

			// Crear ingrDetaImpu
			ingrDetaImpus.addAll(ingrDeta.getIngrDetaImpus());
			// Fin Crear ingrDetaImpu

		}

		try {
			compra.actualizarProdCost(prodCostsModifi);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al actualizar costos"));
			e.printStackTrace();
		}

		try {
			compra.actualizarProdPrec(prodPrecsModifi);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al actualizar precios"));
			e.printStackTrace();
		}

		for (IngrDetaImpu ingrDetaImpuInsertar : ingrDetaImpus) {

			try {
				compra.insertarIngrDetaImpu(ingrDetaImpuInsertar);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error no se creo detalle de impuestos y retenciones"));
				e.printStackTrace();
			}
		}
	}

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
				rolPrecs.addAll(compra.buscarRolPrecios(rolPrec));
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

	public void alCambiarDocuIngr() {

		this.valoresCabecera();
		this.factor = this.getIngreso().getDocuIngr().getDocumento().getFactor();

		// Limpia datos anteriores en caso de que el usario cambie de proveedor
		// y haya estado ingresando una nota de credito
		this.cxpSele = new Cxp();
		this.ingreso.setIngreso(null);
		this.cxps.clear();

		// RequestContext context = RequestContext.getCurrentInstance();
		// context.addCallbackParam("factor", this.factor);
		// context.execute("hide('pagar')");
		// context.execute("PF('pagar').hide();");
		// context.update("pagar");
		// context.execute("alert('Hola')");
		this.calcularTotalIngres();
	}

	public List<ProdDimm> buscarProdDimms(ProdDimm prodDimm) {
		List<ProdDimm> prodDimms = new ArrayList<>();
		try {
			prodDimms = compra.buscarProdDimms(prodDimm);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar producto e impuestos"));
			e.printStackTrace();
		}
		return prodDimms;
	}

	// Selecciona el valor de iva del producto
	// Con Iva 13030; sin iva 13040
	public ProdDimm seleccionarProdDimm(List<ProdDimm> prodDimms) {

		ProdDimm prodDimmIva = new ProdDimm();

		for (ProdDimm prodDimm : prodDimms) {
//			if (prodDimm.getDimm().getDimmId() == 13030 || prodDimm.getDimm().getDimmId() == 13040) {
			if (prodDimm.getDimm().getTablaRefe().equals("Tabla12")) {
				prodDimmIva = prodDimm;
			}
		}

		return prodDimmIva;
	}

	// Sirve para inventario
	public void seleccionarPrecio() {

		ProdPrec prodPrecBuscar = new ProdPrec();
		ProdPrec prodPrec = new ProdPrec();

		List<ProdPrec> prodPrecs = new ArrayList<>();

		// ingrDetaSele.setPrecio(precio);
		ingrDetaSele.setPrecio(ingrDetaPrecSele.getPrecio());

		prodPrecBuscar.setSucursal(variablesSesion.getSucursal());
		prodPrecBuscar.setPrecio(ingrDetaPrecSele.getPrecio());
		prodPrecBuscar.setProducto(ingrDetaSele.getProducto());

		prodPrecs = this.buscarProdPrecs(prodPrecBuscar, null, null, this.ordenColumna);

		for (ProdPrec prodPrecSeleccionar : prodPrecs) {
			prodPrec = prodPrecSeleccionar;
		}

		ingrDetaSele.setFactor(prodPrec.getFactor());
		ingrDetaSele.setPrecioConImpu(prodPrec.getPrecioConImpu());

		this.calcularTotalIngres();

	}

	// Numero de filas en cada pagina
	public int getFilasProductos() {
		return variablesSesion.getFilasProductosIngreso();
	}

	public TranPlan recorrerTranPlans(List<TranPlan> tranplans) {

		TranPlan tranPlanCxp = new TranPlan();

		for (TranPlan tranPlan : tranplans) {
			if (tranPlan.getTipo().equals("CXP")) {
				tranPlanCxp = tranPlan;
				break;
			}
		}

		return tranPlanCxp;
	}

	public void calcularUtilidad() {

		BigDecimal descueCabePorc = new BigDecimal(0);

		if (this.getIngreso().getDescue() != null) {
			descueCabePorc = this.getIngreso().getDescue();
		}

		for (IngrDetaPrec ingrDetaPrec : this.ingrDetaSele.getIngrDetaPrecs()) {

			BigDecimal costo = new BigDecimal(0);
			BigDecimal descuento = new BigDecimal(0);
			BigDecimal impuestoInverso = new BigDecimal(0);

			ProdDimm prodDimm = new ProdDimm();
			List<ProdDimm> prodDimms = new ArrayList<>();

			prodDimm.setDimm(new Dimm());
			prodDimm.setProducto(this.ingrDetaSele.getProducto());
			prodDimms = this.buscarProdDimms(prodDimm);
			prodDimm = this.seleccionarProdDimm(prodDimms);

			costo = this.ingrDetaSele.getCosto().multiply(ingrDetaPrec.getFactor());

			// Descuento Cabecera
			descuento = costo.multiply(descueCabePorc).divide(new BigDecimal(100), 6, RoundingMode.HALF_UP);
			costo = costo.subtract(descuento);

			// Descuento Detalle
			descuento = costo.multiply(this.ingrDetaSele.getDescue()).divide(new BigDecimal(100), 6,
					RoundingMode.HALF_UP);
			costo = costo.subtract(descuento);

			impuestoInverso = (prodDimm.getDimm().getPorcen().divide(new BigDecimal(100), 6, RoundingMode.HALF_UP))
					.add(new BigDecimal(1));

			ingrDetaPrec
					.setPrecioSinImpu(ingrDetaPrec.getPrecioConImpu().divide(impuestoInverso, 6, RoundingMode.HALF_UP));

			if (costo.compareTo(BigDecimal.ZERO) == 0) {
				ingrDetaPrec.setUtilid(new BigDecimal(100));
			} else {
				ingrDetaPrec.setUtilid(((ingrDetaPrec.getPrecioSinImpu().divide(costo, 4, RoundingMode.HALF_UP))
						.subtract(new BigDecimal(1))).multiply(new BigDecimal(100)));
			}
		}
	}

//	Este metodo se llama desde la pagina
//	llama al otro metodo this.calcularPrecio(this.ingrDetaSele) ya que al procesar
//	tambien se usa ek metodo this.calcularPrecio(this.ingrDetaSele)
	public void calcularPrecio() {

		this.calcularPrecio(this.ingrDetaSele);

	}

	public void calcularPrecio(IngrDeta ingrDeta) {

		BigDecimal descueCabePorc = new BigDecimal(0);

		if (this.getIngreso().getDescue() != null) {
			descueCabePorc = this.getIngreso().getDescue();
		}

		for (IngrDetaPrec ingrDetaPrec : ingrDeta.getIngrDetaPrecs()) {

			BigDecimal costo = new BigDecimal(0);
			BigDecimal descuento = new BigDecimal(0);
			BigDecimal utilidad = new BigDecimal(0);
			BigDecimal impuesto = new BigDecimal(0);

			ProdDimm prodDimm = new ProdDimm();
			List<ProdDimm> prodDimms = new ArrayList<>();

			prodDimm.setDimm(new Dimm());
			prodDimm.setProducto(ingrDeta.getProducto());
			prodDimms = this.buscarProdDimms(prodDimm);
			prodDimm = this.seleccionarProdDimm(prodDimms);

			// costo =
			// ingrDetaPrec.getIngrDeta().getCosto().divide(ingrDetaPrec.getFactor(), 6,
			// RoundingMode.HALF_UP);
			costo = ingrDeta.getCosto().multiply(ingrDetaPrec.getFactor());

			// Descuento Cabecera
			descuento = costo.multiply(descueCabePorc).divide(new BigDecimal(100), 6, RoundingMode.HALF_UP);
			costo = costo.subtract(descuento);

			// Descuento Detalle
			descuento = costo.multiply(ingrDeta.getDescue()).divide(new BigDecimal(100), 6, RoundingMode.HALF_UP);

			costo = costo.subtract(descuento);

			// utilidad = costo.multiply(ingrDetaPrec.getUtilid().divide(new
			// BigDecimal(100))).multiply(ingrDetaPrec.getFactor());
			utilidad = costo.multiply(ingrDetaPrec.getUtilid().divide(new BigDecimal(100)));
			// ingrDetaPrec.setPrecioSinImpu(costo.multiply(ingrDetaPrec.getFactor()).add(utilidad));
			ingrDetaPrec.setPrecioSinImpu(costo.add(utilidad));

			impuesto = ingrDetaPrec.getPrecioSinImpu()
					.multiply(prodDimm.getDimm().getPorcen().divide(new BigDecimal(100), 6, RoundingMode.HALF_UP));
			ingrDetaPrec.setPrecioConImpu(ingrDetaPrec.getPrecioSinImpu().add(impuesto));
		}
	}

	public void buscarRolSucus() {

		List<RolSucu> rolSucus = new ArrayList<>();

		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");

		try {
			rolSucus = compra.buscarRolSucus(persUsuaSesion.getRolPersUsuas());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar sucursales del rol"));
			e.printStackTrace();
		}

		for (RolSucu rolSucu : rolSucus) {
			if (rolSucu.getAcceso() == true) {
				this.sucursals.add(rolSucu.getSucursal().getSucursalId());
			}
		}
	}

	// Se llama desde la pagina CompraCabe con el boton generarRetencion
	// Se usa para generar la retención manualmente
	// No entra nunca aca ya que todas se generan automaticamente
	public String generarRetencion() {

		Integer retencionId;

		this.conversationEnd();

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		String estado = this.estadoActual();

		if (estado.equals("PR")) {

			// Crear la retencion en caso que sea manual o electronica
			// Si el tipoRete es Ninguna crea la retencion manual y vuelve a preguntar
			// para realizar la parte electronica
			if (!this.getIngreso().getDocuIngr().getTipoRete().equals("Ninguna")) {

				this.retencion = this.crearRetencion();
				this.reteDetas = this.crearReteDetas(retencion);

				retencion.setReteDetas(reteDetas);
				retencionId = this.insertarRetencion(retencion);

				if (this.getIngreso().getDocuIngr().getTipoRete().equals("Electronica")) {

					if (this.validarUrl() == true) {

						if (this.validarConexion() == true) {

							String estadoDocuElec = this.procesarComprobanteElectronico();

							this.retencion = this.buscarRetencionPorId(retencionId);
							this.retencion.setMotivoRech(this.detalleProceso);
							this.retencion.setEstadoDocuElec(estadoDocuElec);

							try {

								compra.modificarRetencion(this.retencion);

							} catch (Exception e) {

								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
										FacesMessage.SEVERITY_INFO, null, "Error al cambiar estado del documento"));
								e.printStackTrace();
							}

						} else {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_INFO, null,
											"No existe conexión con el Sri, documento no enviado"));
						}
					} else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
								null, "Url no esta configurado correctamente"));
					}
				}
			}

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Retención generada"));

		} else {

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Retención no generada. Documento no esta procesado"));

		}

		return "/ppsj/ingreso/ingreso/lista.xhtml?faces-redirect=true";
	}

	public Retencion crearRetencion() {

		String numeracionAutomatica;

		Retencion retencion = new Retencion();
		Parametro parametro = new Parametro();

		parametro = this.buscarParametro(104001);
		numeracionAutomatica = parametro.getDescri();

		retencion.setIngreso(this.ingreso);

		parametro = this.buscarParametro(104002);
		retencion.setSerie1(parametro.getDescri());

		parametro = this.buscarParametro(104003);
		retencion.setSerie2(parametro.getDescri());

		retencion.setFechaEmis(this.ingreso.getFechaRegi());
		retencion.setFechaRegi(this.ingreso.getFechaRegi());
		retencion.setFechaHoraEmis(this.ingreso.getFechaHoraRegi());
		retencion.setFechaHoraRegi(this.ingreso.getFechaHoraRegi());
		retencion.setEstado("PR");
		retencion.setEstadoDocuElec("NO ENVIADO");
		retencion.setLeyen1(variablesSesion.getLeyenda1());
		retencion.setLeyen2(variablesSesion.getLeyenda2());
		retencion.setMsgInfoAdic(variablesSesion.getMsgInfoAdicional());

		if (numeracionAutomatica.equals("true")) {

			Integer numero = this.buscarNumeroRete();
			retencion.setNumero(numero);

		} else {
			retencion.setNumero(this.getIngreso().getNumeroRete());
		}

		if (this.getIngreso().getDocuIngr().getTipoRete().equals("Manual")) {

			parametro = this.buscarParametro(104005);

			retencion.setAutori(parametro.getDescri());
			retencion.setClaveAcce(retencion.getAutori());

			parametro = this.buscarParametro(104006);
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

			retencion.setFechaAuto(LocalDateTime.parse(parametro.getDescri() + " 00:00", dateTimeFormatter));
			retencion.setDocumeElec(false);

		} else {

			try (Formatter formatter = new Formatter()) {
				String numero = formatter.format("%09d", retencion.getNumero()).toString();

				String claveAcce = documeElectRegis.generarClaveAcceso(this.getVariablesSesion().getSucursal(),
						this.ingreso.getFechaRegi(), this.getIngreso().getDocuIngr().getCodigoTipoCompRete(),
						retencion.getSerie1(), retencion.getSerie2(), numero,
						this.getIngreso().getDocuIngr().getAmbien(), this.getIngreso().getDocuIngr().getTipoEmis(),
						variablesSesion.getCodiClav());
				// Fin Generar clave acceso

				retencion.setAutori(claveAcce);
				retencion.setClaveAcce(claveAcce);
				retencion.setDocumeElec(true);
			}
		}

		return retencion;
	}

	public Set<ReteDeta> crearReteDetas(Retencion retencion) {

		Set<ReteDeta> reteDetas = new HashSet<>();

		for (TotalDocu totalDocu : this.totalDocus) {

			if (totalDocu.getDescri().contains("Subtotal Retencion")) {

				String impuesto[];

				impuesto = totalDocu.getDescri().split(" ");

				ReteDeta reteDeta = new ReteDeta();
				reteDeta.setRetencion(retencion);
				reteDeta.setEjerciFisc(this.ingreso.getFechaRegi());
				reteDeta.setImpues(impuesto[2]);
				reteDeta.setCodigoImpu(totalDocu.getCodigo());
				reteDeta.setBase(totalDocu.getValor());
				reteDeta.setPorcen(totalDocu.getPorcen());

				reteDetas.add(reteDeta);
			}
		}

		return reteDetas;
	}

	public Retencion buscarRetencionPorId(Integer retencionId) {

		Retencion retencion = new Retencion();

		try {
			retencion = compra.buscarRetencionPorId(retencionId);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar retención"));
			e.printStackTrace();
		}

		return retencion;
	}

	public Integer buscarNumeroRete() {

		Parametro parametro = new Parametro();

		parametro = this.buscarParametro(104004);

		return Integer.parseInt(parametro.getDescri()) + 1;
		// Integer numero = Integer.parseInt(parametro.getDescri()) + 1;

		// parametro.setDescri(Integer.toString(numero));
		// this.grabarParametro(parametro);

		// return numero;

	}

	public List<Parametro> buscarParametros(Parametro parametro) {

		List<Parametro> parametros = new ArrayList<>();

		try {
			parametros = parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepción- Error al buscar parametro Inventario-GrupoTipo"));
			e.printStackTrace();
		}

		return parametros;
	}

	public void grabarParametro(Parametro parametro) {

		try {
			parametroRegis.modificar(parametro);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepción - Error grabar número de retención"));
			e.printStackTrace();
		}
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

	public List<FormPagoMoviIngr> buscarFpmis(FormPagoMoviIngr fpmi) {

		List<FormPagoMoviIngr> fpmis = new ArrayList<>();

		try {
			fpmis = this.compra.buscarFpmis(fpmi);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar pagos FPME"));
			e.printStackTrace();
		}

		return fpmis;

	}

	public Integer insertarRetencion(Retencion retencion) {

		Integer retencionId = null;

		try {

			retencionId = this.compra.insertarRetencion(retencion);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al crear retención"));
			e.printStackTrace();
		}

		return retencionId;
	}

	public Integer insertarFormPagoMoviIngr() {

		Integer id = null;

		try {

//			Graba Fecha y Hora
			formPagoMoviIngr.setFechaHora(this.formPagoMoviIngr.getFecha().atTime(LocalTime.now()));

			// Grabar el número de cobro
			formPagoMoviIngr.setNumero(this.formPagoMoviIngr.getDocuMoviIngr().getDocumento().getNumero() + 1);

			// Actualizar secuencial
			this.formPagoMoviIngr.getDocuMoviIngr().getDocumento().setNumero(this.formPagoMoviIngr.getNumero());
			compra.modificarDocumento(this.getFormPagoMoviIngr().getDocuMoviIngr().getDocumento());
			// Fin Actualizar secuencial

			this.getFormPagoMoviIngr().setIngresoId(this.ingreso.getIngresoId());
			id = compra.insertarFpmi(this.getFormPagoMoviIngr());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al insertar forma de pago"));
			e.printStackTrace();
		}

		return id;

	}

	public void insertarPagoDeta(FormPagoMoviIngr formPagoMoviIngr, Cxp cxp) {

		PagoDeta pagoDeta = new PagoDeta();

		pagoDeta.setFormPagoMoviIngr(formPagoMoviIngr);
		;
		pagoDeta.setCxp(cxp);
		pagoDeta.setIngresoId(this.ingreso.getIngresoId()); // Se registra como referencia para poder eliminar el abono
															// en caso de NC
		pagoDeta.setTotal(this.formPagoMoviIngr.getTotal());

		try {
			compra.insertarPagoDeta(pagoDeta);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error crear detalle de Pago"));
			e.printStackTrace();

		}
	}

	// <<<<<<<<<<<<<<<<<<<< DESCARGAR DOCUMENTO - IMPRESION >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< DESCARGAR DOCUMENTO - IMPRESION >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< DESCARGAR DOCUMENTO - IMPRESION >>>>>>>>>>>>>>>>>>>>

	public void descargar() {

		if (ingresoId == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Error - Por favor primero grabe o procese el documento para poder descargarlo"));
			return;
		}
		// Nombre que esta en archivo fuente en jasperReport
		String nombreReporte = this.getIngreso().getDocuIngr().getDocumento().getFormat();

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		Parametro parametro = new Parametro();
//		Dimm dimm = new Dimm();

		// Se coloca aqui la ruta para poder tener los reportes en diferentes y varias
		// carpetas
		// Sino se podria poner en la clase GenerarJasperReport
		String rutaJrxml; // = "C:\\tecnoIntel\\soem\\jasperReportes\\ingreso\\";
		// En esta ruta se compila el reporte dentro del servidor de aplicaciones dentro
		// del archivo .EAR .WEB
		String rutaReporteCompilado = "\\jasperReportes\\ingreso\\";

		try {

			Integer ingresoId = this.ingreso.getIngresoId();

			if (ingresoId == null) {
				parametrosJasper.put("ingresoId", 0);
			} else {
				parametrosJasper.put("ingresoId", ingresoId);
			}

			parametro = parametroRegis.buscarPorId(Parametro.class, 4000);
			rutaJrxml = parametro.getDescri();

//			Buscar variables iva para poder separar los subtotales e imprimir en la factura los valores por separado
//			dimm = compra.buscarDimmPorId(13030);
//			parametrosJasper.put("ingrDetaImpuDescri12", dimm.getDescri().trim());
//	 
//			dimm = compra.buscarDimmPorId(13040);
//			parametrosJasper.put("ingrDetaImpuDescri0", dimm.getDescri().trim());

//			Envia como parametro el porcentaje predeterminado actual
			parametrosJasper.put("dimmIvaPorcen", dimmIva.getPorcen());
			parametrosJasper.put("rutaJrxml", rutaJrxml);

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper, rutaJrxml, rutaReporteCompilado,
					formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al descargar documento"));
			e.printStackTrace();
		}
	}

	public String procesarDialogo() {

		String navegar = null;
		String estadoDocu = null;

//		Primefaces 5
//		RequestContext context = RequestContext.getCurrentInstance();

		estadoDocu = this.pagar();

		if (estadoDocu == "grabado") {
			navegar = "/ppsj/ingreso/ingreso/lista.xhtml?faces-redirect=true";
		}

		PrimeFaces.current().ajax().addCallbackParam("procesar", true);

		return navegar;

	}

	public void ejecutarTareaEnviarCorreoDocu(String destinatario, String claveAcce) {

		// if (destinatario != null){
		// destintariosFinal.append(destinatarios);
		// ojo cuando no hay uno de los dos no va la coma
		// destintariosFinal.append(",");
		// }

		// Parametro 100250 configurar si se desea enviar los documentos electronicos a
		// otras direcciones
		// if (variablesSesion.getDireccionesAdicionales() != null){
		// destintariosFinal.append(variablesSesion.getDireccionesAdicionales());
		// }

		try {

			manejadorTareaEnviarCorreoDocu.ejecutarTareaEnviarCorreoDocu(destinatario, claveAcce);

		} catch (ExecutionException | InterruptedException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Documento no enviado por correo al proveedor " + destinatario));
			e.printStackTrace();
		}
	}

	public String convertir() {

		String estadoDocu;
		String navegar = null;

		if (this.docuIngrConvertir == null || this.rolSucuConvertir == null) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Seleccione sucursal y documento de destino"));
			return null;
		}

		// Para que pueda entrar al metodo grabar sino entra a modificar
		// Se coloca null en los ids de las entidades
		this.ingresoId = null;

		for (IngrDeta ingrDeta : this.ingrDetaDataTable) {
			ingrDeta.setIngrDetaId(null);

			for (IngrDetaPrec ingrDetaPrec : ingrDeta.getIngrDetaPrecs()) {
				ingrDetaPrec.setIngrDetaPrecId(null);
				ingrDetaPrec.setIngrDeta(ingrDeta);
			}
		}

		// El new Egreso(this.egreso) duplica el egreso actual;
		Ingreso ingresoDuplicado = new Ingreso(this.ingreso);

		ingresoDuplicado.setFechaRegi(LocalDate.now());
		ingresoDuplicado.setFechaEmis(LocalDate.now());
		ingresoDuplicado.setFechaHoraRegi(LocalDateTime.now());
		ingresoDuplicado.setFechaHoraEmis(LocalDateTime.now());

		// Coloca nuevo documento y nueva sucursal
		ingresoDuplicado.setDocuIngr(this.docuIngrConvertir);
		ingresoDuplicado.setSucursal(this.rolSucuConvertir.getSucursal());

		this.ingreso = ingresoDuplicado;

		estadoDocu = this.grabarIngreso("GR");

		if (estadoDocu == "grabado") {

			this.conversationEnd();

			navegar = "/ppsj/ingreso/ingreso/lista.xhtml?faces-redirect=true";

			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Documento grabado No.:" + this.ingreso.getNumero()));
		}

		return navegar;
	}

	public List<RolSucu> obtenerRolSucuUnicosPorSucursal(List<RolSucu> listaRolSucu) {

		Set<Integer> sucursalIdsVistos = ConcurrentHashMap.newKeySet();

		return listaRolSucu.stream().filter(rs -> rs.getSucursal() != null)
				.filter(rs -> sucursalIdsVistos.add(rs.getSucursal().getSucursalId())) // filtra duplicados
				.collect(Collectors.toList());
	}

	public void cargarDatosConvertir() {

		// La lista de documentos se usa la lista inicial
		// this.buscarSucurals();

		try {

			PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
					.get("persUsua");

			// Buscar el acceso del rol a cada una de las sucursales
			// Se llena esta lista pero en el caso que un usuario tenga dos roles
			// se va a duplicar la sucursal, se tendría que sacar en otra lista aparte solo
			// las sucursales
			// rolSucuConvertirs = compra.buscarRolSucus(persUsuaSesion.getRolPersUsuas());

			List<RolSucu> rolSucus = compra.buscarRolSucus(persUsuaSesion.getRolPersUsuas());
			// PARA NO HACER ESTO SE PUEDE AUMETNAR EN LA CONSULTA UN GROUPBY
			rolSucuConvertirs = obtenerRolSucuUnicosPorSucursal(rolSucus);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar sucursales del usuario: " + persUsuaSesion.getPersona().getApelli()));
			e.printStackTrace();
		}

	}

	public void saldoProveedor() {

		BigDecimal sumaCxp = new BigDecimal(0);
		BigDecimal sumaPagoDeta = new BigDecimal(0);

		sumaCxp = this.sumarCxp();
		sumaPagoDeta = this.sumarPagoDeta();

		if (sumaCxp == null) {
			sumaCxp = new BigDecimal(0);
		}

		if (sumaPagoDeta == null) {
			sumaPagoDeta = new BigDecimal(0);
		}

		this.saldoProveedor = sumaCxp.subtract(sumaPagoDeta);
	}

	public BigDecimal sumarCxp() {

		BigDecimal sumaCxp = new BigDecimal(0);

		PersProv persProv = new PersProv();
		persProv.setPersona(this.ingreso.getPersProv().getPersona());
		// persProv.setPersonaId(1001);

		Cxp cxp = new Cxp();
		Ingreso ingreso = new Ingreso();
		ingreso.setPersProv(persProv);
		cxp.setIngreso(ingreso);

		try {

			sumaCxp = compra.sumarCxp(cxp);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al sumar cuentas por pagar"));
			e.printStackTrace();
		}

		return sumaCxp;
	}

	public BigDecimal sumarPagoDeta() {

		BigDecimal sumaPagoDeta = new BigDecimal(0);

		PersProv persProv = new PersProv();
		Ingreso ingreso = new Ingreso();
		Cxp cxp = new Cxp();
		PagoDeta pagoDeta = new PagoDeta();

		persProv.setPersona(this.ingreso.getPersProv().getPersona());
		ingreso.setPersProv(persProv);
		cxp.setIngreso(ingreso);
		pagoDeta.setCxp(cxp);

		try {
			sumaPagoDeta = this.compra.sumarPagoDeta(pagoDeta);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al sumar pagos"));
			e.printStackTrace();
		}

		return sumaPagoDeta;
	}

	public void valoresCabecera() {

		if (this.ingreso.getDocuIngr().getDocumento().getFactor() == -1) {
			if (this.ingreso.getDocuIngr().isAfectaCost()) {
				ingreso.setSerie1("000");
				ingreso.setSerie2("000");
				ingreso.setAutori("0000000000");
				ingreso.setDimm(this.dimms.get(0));
			} else {
				ingreso.setSerie1(null);
				ingreso.setSerie2(null);
				ingreso.setAutori(null);
				ingreso.setDimm(null);
			}
		} else if (this.ingreso.getDocuIngr().getDocumento().getFactor() == 1) {
			if (this.ingreso.getDocuIngr().isAfectaCost()) {
				ingreso.setSerie1("000");
				ingreso.setSerie2("000");
				ingreso.setAutori("0000000000");
				ingreso.setDimm(this.dimms.get(0));
			} else {

				if (this.getIngreso().getDocuIngr().getDocumeElec().equals("Liquidacion")) {
//					Se usa estos parametros para almacenar los numeros de serie
//					de la liquidacion y de la retencion
					Parametro parametro = new Parametro();

					parametro = this.buscarParametro(104002);
					ingreso.setSerie1(parametro.getDescri());

					parametro = this.buscarParametro(104003);
					ingreso.setSerie2(parametro.getDescri());

					ingreso.setAutori("0000000000");
					ingreso.setDimm(null);
				} else {
					ingreso.setSerie1(null);
					ingreso.setSerie2(null);
					ingreso.setAutori(null);
					ingreso.setDimm(null);
				}
			}
		} else if (this.ingreso.getDocuIngr().getDocumento().getFactor() == 0) {
			ingreso.setSerie1("000");
			ingreso.setSerie2("000");
			ingreso.setAutori("0000000000");
		}

		ingreso.setNota(this.ingreso.getDocuIngr().getDocumento().getDescri());
	}

	private IngrDimm ingrDimmSele = new IngrDimm();
	private List<IngrDimm> ingrDimms = new ArrayList<>();

	public void crearFilaIngrDimm() {

		IngrDimm ingrDimm = new IngrDimm();

		ingrDimm.setIngreso(this.ingreso);
		ingrDimm.setDimm(new Dimm());

		this.ingrDimms.add(ingrDimm);

	}

	public void eliminarFilaIngrDimm() {

		this.ingrDimms.remove(this.ingrDimmSele);

		this.crearIngrDetaImpuRetens();
		this.calcularTotalIngres();

	}

	public List<PersProvDimm> buscarPersProvDimms(PersProvDimm persProvDimm, Integer pagina) {
		List<PersProvDimm> persProvDimms = new ArrayList<>();

		try {
			persProvDimms = this.compra.buscarPersProvDimms(persProvDimm, pagina);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar reteenciones del proveedor"));
			e.printStackTrace();
		}

		return persProvDimms;
	}

	public List<IngrDimm> copiarPersProvDimmAIngrDimm(List<PersProvDimm> persProvDimms) {

		List<IngrDimm> ingrDimms = new ArrayList<>();

		for (PersProvDimm persProvDimm : persProvDimms) {

			IngrDimm ingrDimm = new IngrDimm();

			ingrDimm.setIngreso(this.ingreso);
			ingrDimm.setDimm(persProvDimm.getDimm());
			ingrDimm.setTipo(persProvDimm.getTipo());

			ingrDimms.add(ingrDimm);

		}

		return ingrDimms;
	}

	public void buscarCopiarPersProvDimm() {

		PersProvDimm persProvDimm = new PersProvDimm();
		persProvDimm.setPersProv(this.ingreso.getPersProv());
		persProvDimm.setDimm(new Dimm());

		List<PersProvDimm> persProvDimms = this.buscarPersProvDimms(persProvDimm, null);
		this.ingrDimms = this.copiarPersProvDimmAIngrDimm(persProvDimms);

	}

	public List<Dimm> buscarDimms(Dimm dimmDesde) {

		Dimm dimmHasta = new Dimm();

		List<Dimm> dimms = new ArrayList<>();
		try {
			dimms = compra.buscarDimms(dimmDesde, dimmHasta);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Error al buscar retenciones"));
			e.printStackTrace();
		}

		return dimms;
	}

	public void grabarIngrDimm() {

		for (IngrDimm ingrDimm : this.ingrDimms) {
			try {
				ingrDimm.setIngreso(this.ingreso);
				this.compra.insertarIngrDimm(ingrDimm);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
						"Error al grabar retenciones en ingreso (IngrDimm)"));
				e.printStackTrace();
			}
		}
	}

	public List<IngrDimm> buscarIngrDimms(IngrDimm ingrDimm, Integer pagina) {

		List<IngrDimm> ingrDimms = new ArrayList<>();

		try {

			ingrDimms = compra.buscarIngrDimms(ingrDimm, pagina);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Error al buscar retenciones de ingreso (IngrDimm)"));
			e.printStackTrace();
		}

		return ingrDimms;
	}

	public void eliminarIngrDimm() {

		for (IngrDimm ingrDimm : this.ingrDimms) {
			try {
				this.compra.eliminarIngrDimm(ingrDimm);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
						"Error al eliminar retenciones de ingreso (IngrDimm)"));
				e.printStackTrace();
			}
		}
	}

	public RolSucu getRolSucuConvertir() {
		return rolSucuConvertir;
	}

	public void setRolSucuConvertir(RolSucu rolSucuConvertir) {
		this.rolSucuConvertir = rolSucuConvertir;
	}

	public DocuIngr getDocuIngrConvertir() {
		return docuIngrConvertir;
	}

	public void setDocuIngrConvertir(DocuIngr docuIngrConvertir) {
		this.docuIngrConvertir = docuIngrConvertir;
	}

	// <<<<<<<<<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>>>>>>>>

	public int getFilasProveedores() {
		return variablesSesion.getFilasPagina();
	}

	public Integer getProductoId() {
		return productoId;
	}

	public void setProductoId(Integer productoId) {
		this.productoId = productoId;
	}

	public List<DocuIngr> getDocuIngrs() {
		return docuIngrs;
	}

	public void setDocuIngrs(List<DocuIngr> docuIngrs) {
		this.docuIngrs = docuIngrs;
	}

	public List<PersProv> getPersProvs() {
		return persProvs;
	}

	public void setPersProvs(List<PersProv> persProvs) {
		this.persProvs = persProvs;
	}

	public List<Dimm> getDimms() {
		return dimms;
	}

	public void setDimms(List<Dimm> dimms) {
		this.dimms = dimms;
	}

	public Ingreso getIngreso() {
		return ingreso;
	}

	public void setIngreso(Ingreso ingreso) {
		this.ingreso = ingreso;
	}

	public Integer getIngresoId() {
		return ingresoId;
	}

	public void setIngresoId(Integer ingresoId) {
		this.ingresoId = ingresoId;
	}

	public List<IngrDeta> getIngrDetaDataTable() {
		return ingrDetaDataTable;
	}

	public void setIngrDetaDataTable(List<IngrDeta> ingrDetaDataTable) {
		this.ingrDetaDataTable = ingrDetaDataTable;
	}

	public Conversation getConversation() {
		return conversation;
	}

	public IngrDeta getIngrDetaSele() {
		return ingrDetaSele;
	}

	public void setIngrDetaSele(IngrDeta ingrDetaSele) {
		this.ingrDetaSele = ingrDetaSele;
	}

	public List<Ingreso> getIngresos() {
		return ingresos;
	}

	public void setIngresos(List<Ingreso> ingresos) {
		this.ingresos = ingresos;
	}

	public List<RolDocu> getRolDocus() {
		return rolDocus;
	}

	public void setRolDocus(List<RolDocu> rolDocus) {
		this.rolDocus = rolDocus;
	}

	public int getFactor() {
		return factor;
	}

	public void setFactor(int factor) {
		this.factor = factor;
	}

	public List<Precio> getPrecios() {
		return precios;
	}

	public void setPrecios(List<Precio> precios) {
		this.precios = precios;
	}

	public List<KardTotaView> getKardTotaViews() {
		return kardTotaViews;
	}

	public void setKardTotaViews(List<KardTotaView> kardTotaViews) {
		this.kardTotaViews = kardTotaViews;
	}

	public List<TotalDocu> getTotalDocus() {
		return totalDocus;
	}

	public void setTotalDocus(List<TotalDocu> totalDocus) {
		this.totalDocus = totalDocus;
	}

	public FormPagoMoviIngr getFormPagoMoviIngr() {
		return formPagoMoviIngr;
	}

	public void setFormPagoMoviIngr(FormPagoMoviIngr formPagoMoviIngr) {
		this.formPagoMoviIngr = formPagoMoviIngr;
	}

	public List<DocuMoviIngr> getDocuMoviIngrs() {
		return docuMoviIngrs;
	}

	public void setDocuMoviIngrs(List<DocuMoviIngr> docuMoviIngrs) {
		this.docuMoviIngrs = docuMoviIngrs;
	}

	public List<FormPago> getFormPagos() {
		return formPagos;
	}

	public void setFormPagos(List<FormPago> formPagos) {
		this.formPagos = formPagos;
	}

	public boolean isActivarIngrDetaCodigoBarra() {
		return variablesSesion.isActivarIngrDetaCodigoBarra();
	}

	public boolean isActivarIngrDetaDescri() {
		return variablesSesion.isActivarIngrDetaDescri();
	}

	public boolean isActivarIngrDetaPrecio() {
		return variablesSesion.isActivarIngrDetaPrecio();
	}

	public boolean isActivarIngrDetaCostoUlti() {
		return variablesSesion.isActivarIngrDetaCostoUlti();
	}

	public boolean isActivarIngrDetaCostoProm() {
		return variablesSesion.isActivarIngrDetaCostoProm();
	}

	// <<<<<<<<<<<<<<<<<<<< NOTAS >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< NOTAS >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< NOTAS >>>>>>>>>>>>>>>>>>>>

//	private ComprobanteRetencion.InfoAdicional generarInformacionAdicionalComprobanteRetencion() {
//
//		// Dirección, Correo son los que estan en el sistema de facturacion del SRI
//		// Se podria aumentar telefono
//		ComprobanteRetencion.InfoAdicional infoAdicional = new ComprobanteRetencion.InfoAdicional();
//
//		if ((this.getIngreso().getPersProv().getPersona().getDirecc() != null)
//				&& (!this.getIngreso().getPersProv().getPersona().getDirecc().isEmpty())) {
//
//			ComprobanteRetencion.InfoAdicional.CampoAdicional campoAdicional = new ComprobanteRetencion.InfoAdicional.CampoAdicional();
//			campoAdicional.setNombre("Direccion");
//			campoAdicional.setValue(this.getIngreso().getPersProv().getPersona().getDirecc());
//			infoAdicional.getCampoAdicional().add(campoAdicional);
//
//		}
//
//		if ((this.getIngreso().getPersProv().getPersona().getCorreo() != null)
//				&& (!this.getIngreso().getPersProv().getPersona().getCorreo().isEmpty())) {
//
//			ComprobanteRetencion.InfoAdicional.CampoAdicional campoAdicional = new ComprobanteRetencion.InfoAdicional.CampoAdicional();
//			campoAdicional.setNombre("Email");
//			campoAdicional.setValue(this.getIngreso().getPersProv().getPersona().getCorreo());
//			infoAdicional.getCampoAdicional().add(campoAdicional);
//		}
//
//		return infoAdicional;
//	}
//
//	public InfoTributaria cargarInfoTributaria() {
//
//		// String nombreComercial = variablesSesion.getSucursal().getDescri();
//		String fechaEmision;
//		String numero = null;
//		String claveAcce = null;
//
//		Dimm dimm = new Dimm();
//		InfoTributaria infoTributaria = new InfoTributaria();
//
//		// Busca codigo de retencion 07
//		try {
//			dimm = compra.buscarDimmPorId(5060);
//		} catch (Exception e) {
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
//					"Excepcion - Error al buscar codigo de retención"));
//			e.printStackTrace();
//		}
//
//		fechaEmision = Util.cambiarFormatoFechaString(this.getIngreso().getFechaRegi(), "ddMMyyyy");
//
//		Formatter formatter = new Formatter();
//		numero = formatter.format("%09d", this.retencion.getNumero()).toString();
//
//		claveAcce = documeElectRegis.generarClaveAcceso(this.getVariablesSesion().getSucursal(), fechaEmision,
//				dimm.getCodigo(), this.retencion.getSerie1(), this.retencion.getSerie2(), numero,
//				this.getIngreso().getDocuIngr().getAmbien(), this.getIngreso().getDocuIngr().getTipoEmis(),
//				variablesSesion.getCodiClav());
//
//		infoTributaria = documeElectRegis.cargarInfoTributaria(this.getVariablesSesion().getSucursal(),
//				dimm.getCodigo(), this.getIngreso().getSerie1(), this.getIngreso().getSerie2(), numero,
//				this.ingreso.getDocuIngr().getAmbien(), this.ingreso.getDocuIngr().getTipoEmis(), claveAcce);
//
//		return infoTributaria;
//	}
//
//	public InfoCompRetencion cargarInfoComprobanteRetencion() {
//
//		String apellidoNombre;
//
//		InfoCompRetencion infoComprobanteRetencion = new InfoCompRetencion();
//
//		if ((this.getVariablesSesion().getSucursal().getDireccEsta() != null)
//				&& (!this.variablesSesion.getSucursal().getDireccEsta().isEmpty())) {
//			// Direccion de la sucursal no de la matriz la dirección de la matriz va en
//			// infoTributaria
//			infoComprobanteRetencion.setDirEstablecimiento(this.getVariablesSesion().getSucursal().getDireccEsta());
//		}
//
//		if ((this.getVariablesSesion().getSucursal().getContriEspe() != null)
//				&& (!this.getVariablesSesion().getSucursal().getContriEspe().isEmpty())) {
//			infoComprobanteRetencion.setContribuyenteEspecial(this.getVariablesSesion().getSucursal().getContriEspe());
//		}
//
//		if (this.getVariablesSesion().getSucursal().isObligaCont()) {
//			infoComprobanteRetencion.setObligadoContabilidad("SI");
//		} else {
//			infoComprobanteRetencion.setObligadoContabilidad("NO");
//		}
//
//		if (this.getIngreso().getPersProv().getPersona().getNombre() != null) {
//			apellidoNombre = this.getIngreso().getPersProv().getPersona().getApelli() + " "
//					+ this.getIngreso().getPersProv().getPersona().getNombre();
//		} else {
//			apellidoNombre = this.getIngreso().getPersProv().getPersona().getApelli();
//		}
//
//		String fechaEmision = Util.cambiarFormatoFechaString(this.getIngreso().getFechaRegi(), "dd/MM/yyyy");
//		String periodoFiscal = Util.cambiarFormatoFechaString(this.getIngreso().getFechaRegi(), "MM/yyyy");
//
//		infoComprobanteRetencion
//				.setTipoIdentificacionSujetoRetenido(this.getIngreso().getPersProv().getDimm().getCodigo());
//		infoComprobanteRetencion.setRazonSocialSujetoRetenido(apellidoNombre);
//		infoComprobanteRetencion
//				.setIdentificacionSujetoRetenido(this.ingreso.getPersProv().getPersona().getCedulaRuc());
//		infoComprobanteRetencion.setPeriodoFiscal(periodoFiscal);
//		infoComprobanteRetencion.setFechaEmision(fechaEmision);
//
//		return infoComprobanteRetencion;
//	}

//	public Impuestos cargarImpuestos() {
//
//		String codigo = null;
//
//		Impuestos impuestos = new Impuestos();
//
//		Formatter formatter = new Formatter();
//		String numero = formatter.format("%09d", this.getIngreso().getNumero()).toString();
//		String numDocSustento = this.ingreso.getSerie1() + this.ingreso.getSerie2() + numero;
//
//		String fechaEmision = Util.cambiarFormatoFechaString(this.getIngreso().getFechaEmis(), "dd/MM/yyyy");
//
//		for (ReteDeta reteDeta : this.reteDetas) {
//
//			Impuesto impuesto = new Impuesto();
//
//			if (reteDeta.getImpues().equals("Iva")) {
//				codigo = variablesSesion.getCodigoReteIva();
//			} else if (reteDeta.getImpues().equals("Renta")) {
//				codigo = variablesSesion.getCodigoReteRenta();
//			} else if (reteDeta.getImpues().equals("Isd")) {
//				codigo = variablesSesion.getCodigoReteIsd();
//			}
//
//			impuesto.setCodigo(codigo);
//			impuesto.setCodigoRetencion(reteDeta.getCodigoImpu());
//			impuesto.setPorcentajeRetener(reteDeta.getPorcen().setScale(2, RoundingMode.HALF_UP));
//			impuesto.setBaseImponible(reteDeta.getBase().setScale(6, RoundingMode.HALF_UP));
//			impuesto.setValorRetenido(reteDeta.getPorcen().multiply(reteDeta.getBase().divide(new BigDecimal(100)))
//					.setScale(6, RoundingMode.HALF_UP));
//			impuesto.setCodDocSustento(this.ingreso.getDocuIngr().getDimm().getCodigo());
//			impuesto.setNumDocSustento(numDocSustento);
//			impuesto.setFechaEmisionDocSustento(fechaEmision);
//
//			impuestos.getImpuesto().add(impuesto);
//		}
//
//		return impuestos;
//
//	}

	public String procesarComprobanteElectronico() {

		String rutaGenerados = null;
		// NO ENVIADO es el estado que se coloca de acuedo al SRI en el caso de que solo
		// se grabe la factura
		// o en el caso de se intento enviar pero ni siquiera se creo el archivo xml
		// (NoCreado)
		String estadoProceso = "NO ENVIADO";

		estadoProceso = this.crearArchivoRetencionXml();

		if (estadoProceso == "GENERADO") {

			// Representa el nombre del archivo con se graba para firmar
			// String nombreArchivo = this.egreso.getSerie1() + this.egreso.getSerie2() +
			// Util.llenarCeros(this.egreso.getNumero(), "%09d");

			String nombreArchivo = this.retencion.getClaveAcce() + ".xml";
			String claveAcceso = retencion.getClaveAcce();

			rutaGenerados = this.variablesSesion.getRutaGenerados() + nombreArchivo;

			try {

				firmarArchivo.signBes(this.ingreso.getSucursal().getSucuCertEmis(), nombreArchivo,
						this.variablesSesion.getRutaGenerados(), this.variablesSesion.getRutaFirmados());

				estadoProceso = "FIRMADO";

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "El documento no se ha firmado"));
				e.printStackTrace();
			}

			if (estadoProceso == "FIRMADO") {

				// Borra archivo de la carpeta generados
				new File(rutaGenerados).delete();

				File archivoFirmado = new File(this.variablesSesion.getRutaFirmados() + nombreArchivo);

				// Este proceso envia el documento en un proceso diferente
				// esperando el tiempo determinado en parametros ID = 3200
				this.ejecutarTareaEnviar(archivoFirmado, this.retencion,
						this.ingreso.getDocuIngr().getCodigoTipoCompRete(), this.retencion.getClaveAcce());
				this.ejecutarTareaAutorizar(archivoFirmado, this.retencion,
						this.ingreso.getDocuIngr().getCodigoTipoCompRete(), this.retencion.getClaveAcce());
				this.ejecurarTareaCrearRideRetencion(this.retencion);

				if (this.ingreso.getPersProv().getPersona().getCorreo() != null
						&& this.ingreso.getDocuIngr().getDocumento().isEnviaCorreo()) {
					this.ejecutarTareaEnviarCorreoDocu(this.ingreso.getPersProv().getPersona().getCorreo(),
							claveAcceso);
				}

			} else {

				// Mover es mensaje a otra variable para poder grabar en la base de datos en
				// motivo_rech
				// cambiar el estadoProceso a GENERADO para colocar en estado_docu_elec
				this.detalleProceso = estadoProceso;

				estadoProceso = "GENERADO";

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error al firmar", estadoProceso));
			}
		}

		return estadoProceso;
	}

	public String procesarComprobanteElectronicoLiquidacion() {

		String rutaGenerados = null;
		// NO ENVIADO es el estado que se coloca de acuedo al SRI en el caso de que solo
		// se grabe la factura
		// o en el caso de se intento enviar pero ni siquiera se creo el archivo xml
		// (NoCreado)
		String estadoProceso = "NO ENVIADO";

		estadoProceso = this.crearArchivoLiquidacionXml();

		if (estadoProceso == "GENERADO") {

			// Representa el nombre del archivo con se graba para firmar
			// String nombreArchivo = this.egreso.getSerie1() + this.egreso.getSerie2() +
			// Util.llenarCeros(this.egreso.getNumero(), "%09d");

			String nombreArchivo = this.ingreso.getClaveAcce() + ".xml";
			String claveAcceso = ingreso.getClaveAcce();

			rutaGenerados = this.variablesSesion.getRutaGenerados() + nombreArchivo;

			try {

				firmarArchivo.signBes(this.ingreso.getSucursal().getSucuCertEmis(), nombreArchivo,
						this.variablesSesion.getRutaGenerados(), this.variablesSesion.getRutaFirmados());
				estadoProceso = "FIRMADO";

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "El documento no se ha firmado"));
				e.printStackTrace();
			}

			if (estadoProceso == "FIRMADO") {

				// Borra archivo de la carpeta generados
				new File(rutaGenerados).delete();

				File archivoFirmado = new File(this.variablesSesion.getRutaFirmados() + nombreArchivo);

				// Este proceso envia el documento en un proceso diferente
				// esperando el tiempo determinado en parametros ID = 3200
				this.ejecutarTareaEnviar(archivoFirmado, this.ingreso, this.ingreso.getDocuIngr().getDimm().getCodigo(),
						ingreso.getClaveAcce());
				this.ejecutarTareaAutorizar(archivoFirmado, this.ingreso,
						this.ingreso.getDocuIngr().getDimm().getCodigo(), this.ingreso.getClaveAcce());
				this.ejecurarTareaCrearRideRetencion(this.ingreso);

				if (this.ingreso.getPersProv().getPersona().getCorreo() != null
						&& this.ingreso.getDocuIngr().getDocumento().isEnviaCorreo()) {
					this.ejecutarTareaEnviarCorreoDocu(this.ingreso.getPersProv().getPersona().getCorreo(),
							claveAcceso);
				}

			} else {

				// Mover es mensaje a otra variable para poder grabar en la base de datos en
				// motivo_rech
				// cambiar el estadoProceso a GENERADO para colocar en estado_docu_elec
				this.detalleProceso = estadoProceso;

				estadoProceso = "GENERADO";

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error al firmar", estadoProceso));
			}
		}

		return estadoProceso;
	}

	public String enviarArchivo(File archivoFirmado, Sucursal sucursal, String codDoc, String claveAcceso) {

		RespuestaSolicitud respuestaSolicitud = new RespuestaSolicitud();

		try {
			respuestaSolicitud = envioComprobantesWs.obtenerRespuestaEnvio1(archivoFirmado, sucursal.getRuc(), codDoc,
					claveAcceso,
					envioComprobantesWs.devuelveUrlWs(variablesSesion.getProxyIp(), variablesSesion.getProxyPuerto(),
							this.ingreso.getDocuIngr().getAmbien(), variablesSesion.getUrlProduccion(),
							variablesSesion.getUrlPruebas(), "RecepcionComprobantesOffline"));
		} catch (Exception e) {

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - No se creo Servicio Web, Comprobante no enviado"));

			e.printStackTrace();
		}

		if (respuestaSolicitud.getEstado().equals("DEVUELTA")) {

			try {

				this.detalleProceso = envioComprobantesWs.obtenerMensajeRespuesta(respuestaSolicitud);

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al buscar motivo rechazo por parte del SRI"));
				e.printStackTrace();
			}
		}

		return respuestaSolicitud.getEstado();
	}

	// public void autorizarArchivo(File archivoFirmado, Sucursal sucursal, String
	// codDoc, String claveAcceso) {
	//
	// RespuestaComprobante respuestaComprobante;
	// AutorizacionDTO autorizacionDTO;
	//
	// respuestaComprobante = this.verificarAutorizacion();
	//
	// if (!respuestaComprobante.getAutorizaciones().getAutorizacion().isEmpty()) {
	//
	// autorizacionDTO = this.obtenerEstadoAutorizacion(respuestaComprobante);
	//
	// try {
	//
	// // Crea el archivo con la autorizacion y copia a la carpeta autorizados
	// autorizacionComprobantes.validarRespuestaAutorizacion(autorizacionDTO,
	// claveAcceso, "C:\\salan\\soem\\comprobantes\\autorizados\\");
	//
	// } catch (Exception e) {
	//
	// // "Error al consultar autorización Documento: " + egreso.getNumero() +
	// // autorizacionDTO.getMensaje()));
	// e.printStackTrace();
	//
	// }
	//
	//
	// try {
	//
	// Retencion retencion = new Retencion();
	//
	//// Buscar egreso para actualizar estado
	// retencion = retencionRegis.buscarPorId(this.retencion.getRetencionId());
	//
	// retencion.setMotivoRech(autorizacionDTO.getMensaje());
	//
	//// Modificar estado y grabar documento
	// retencion.setEstadoDocuElec(autorizacionDTO.getAutorizacion().getEstado());
	// retencionRegis.modificar(retencion);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// File file = new File("C:\\salan\\soem\\comprobantes\\firmados\\" +
	// claveAcceso + ".xml");
	// file.delete();
	//
	// }
	// }

//	public RespuestaComprobante verificarAutorizacion() {
//
//		RespuestaComprobante respuestaComprobante = new RespuestaComprobante();
//
//		try {
//
//			respuestaComprobante = autorizacionComprobantes.autorizarComprobante(variablesSesion.getProxyIp(),
//					variablesSesion.getProxyPuerto(), this.ingreso.getDocuIngr().getAmbien(),
//					variablesSesion.getUrlProduccion(), variablesSesion.getUrlPruebas(),
//					"AutorizacionComprobantesOffline", this.retencion.getClaveAcce());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return respuestaComprobante;
//
//	}

//	public AutorizacionDTO obtenerEstadoAutorizacion(RespuestaComprobante respuestaComprobante) {
//
//		AutorizacionDTO autorizacionDTO = null;
//
//		try {
//			autorizacionDTO = autorizacionComprobantes.obtenerEstadoAutorizacion(respuestaComprobante);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return autorizacionDTO;
//	}

	public boolean validarUrl() {

		Boolean urlValido = false;
		try {

			urlValido = envioComprobantesWs.validarUrl(variablesSesion.getProxyIp(), variablesSesion.getProxyPuerto(),
					this.ingreso.getDocuIngr().getAmbien(), variablesSesion.getUrlProduccion(),
					variablesSesion.getUrlPruebas(), "RecepcionComprobantesOffline",
					variablesSesion.getRutaCertificadoSerWebSri());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Url no esta configurado correctamente"));
			e.printStackTrace();
		}

		return urlValido;

	}

	public boolean validarConexion() {

		boolean existeConexion = false;

		try {

			existeConexion = envioComprobantesWs.existeConexion(variablesSesion.getProxyIp(),
					variablesSesion.getProxyPuerto(), this.ingreso.getDocuIngr().getAmbien(),
					variablesSesion.getUrlProduccion(), variablesSesion.getUrlPruebas(), "RecepcionComprobantesOffline",
					variablesSesion.getRutaCertificadoSerWebSri());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"No existe conexión con el Sri, El documento no se enviara"));
			e.printStackTrace();
		}

		return existeConexion;
	}

	public String crearArchivoRetencionXml() {

		String estado = "NO ENVIADO";

		Object object = null;

		try {

			object = documeElectRegis.generarComprobanteElectronico(this.ingreso, this.ingrDetaDataTable,
					this.formPagoMoviIngr, this.retencion, variablesSesion.getCodigoReteIva(),
					variablesSesion.getCodigoReteRenta(), variablesSesion.getCodigoReteIsd(),
					variablesSesion.getCodigoIva(), variablesSesion.getCodigoIce(), variablesSesion.getCodigoIrbpnr());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepción - Error al generar comprobante"));
			e.printStackTrace();
		}

		try {

			if ((object instanceof ComprobanteRetencion)) {
				documeElectRegis.marshalRetencion((ComprobanteRetencion) object,
						this.variablesSesion.getRutaGenerados());
			}

			estado = "GENERADO";

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - No se genero el comprobante XML"));
			e.printStackTrace();
			return "NO ENVIADO";
		}

		return estado;
	}

	public String crearArchivoLiquidacionXml() {

		String estado = "NO ENVIADO";

		Object object = null;

		try {

			object = documeElectRegis.generarComprobanteElectronicoLiquidacion(this.ingreso, this.ingrDetaDataTable,
					this.formPagoMoviIngr, variablesSesion.getCodigoReteIva(), variablesSesion.getCodigoReteRenta(),
					variablesSesion.getCodigoReteIsd(), variablesSesion.getCodigoIva(), variablesSesion.getCodigoIce(),
					variablesSesion.getCodigoIrbpnr());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepción - Error al generar comprobante - Liquidación de Compra"));
			e.printStackTrace();
		}

		try {

			if ((object instanceof LiquidacionCompra)) {
				documeElectRegis.marshalLiquidacion((LiquidacionCompra) object,
						this.variablesSesion.getRutaGenerados());
			}

			estado = "GENERADO";

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - No se genero el comprobante XML"));
			e.printStackTrace();
			return "NO ENVIADO";
		}

		return estado;
	}

	public void ejecutarTareaEnviar(File archivoFirmado, Object object, String codigoDocu, String claveAcce) {

		try {
			manejadorTareaEnvioDocu.ejecutarTareaEnviarDocu(object, archivoFirmado, this.ingreso.getSucursal(),
					codigoDocu, claveAcce, variablesSesion.getProxyIp(), variablesSesion.getProxyPuerto(),
					this.ingreso.getDocuIngr().getAmbien(), variablesSesion.getUrlProduccion(),
					variablesSesion.getUrlPruebas(), "RecepcionComprobantesOffline");

		} catch (ExecutionException | InterruptedException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Documento no enviado al SRI"));
			e.printStackTrace();
		}
	}

	public void ejecutarTareaAutorizar(File archivoFirmado, Object object, String codigoDocu, String claveAcce) {

		try {
			manejadorTareaAutoriDocu.ejecutarTareaAutoriDocu(object, archivoFirmado, this.ingreso.getSucursal(),
					codigoDocu, claveAcce, variablesSesion.getProxyIp(), variablesSesion.getProxyPuerto(),
					this.ingreso.getDocuIngr().getAmbien(), variablesSesion.getUrlProduccion(),
					variablesSesion.getUrlPruebas(), "AutorizacionComprobantesOffline");

		} catch (ExecutionException | InterruptedException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Documento no autorizado por el SRI"));
			e.printStackTrace();
		}
	}

	public void ejecurarTareaCrearRideRetencion(Object object) {

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

		Parametro parametro;

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3203);
			Integer tiempoEspera = Integer.parseInt(parametro.getDescri());

			TimerTask timerTask = new TareaCrearRideRetencionPdf(object, this.parametroRegis, this.dimmRegis,
					this.dataSource, this.variablesSesion, request);

			Timer timer = new Timer(true);

			timer.schedule(timerTask, tiempoEspera * 1000);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error el crear RideRetencion.pdf o buscar parametro 3203"));
			e.printStackTrace();
		}
	}

//	public void ejecurarTareaCrearLiquidacion() {
//
//		Object object = new Object();
//
//		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
//
//		Parametro parametro;
//
//		if (this.getIngreso().getDocuIngr().getDocumeElec().equals("Liquidacion")) {
//			object = this.ingreso;
//		}
//
//		if (!this.getIngreso().getDocuIngr().getTipoRete().equals("Ninguna")) {
//			object = this.retencion;
//		}
//
//		try {
//			parametro = parametroRegis.buscarPorId(3203);
//			Integer tiempoEspera = Integer.parseInt(parametro.getDescri());
//
//			TimerTask timerTask = new TareaCrearRideRetencionPdf(object, this.parametroRegis, this.dataSource,
//					this.variablesSesion, request);
//
//			Timer timer = new Timer(true);
//
//			timer.schedule(timerTask, tiempoEspera * 1000);
//
//		} catch (Exception e) {
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
//					"Excepcion - Error el crear RideRetencion.pdf o buscar parametro 3203"));
//			e.printStackTrace();
//		}
//
//	}

	public List<RolSucu> getRolSucuConvertirs() {
		return rolSucuConvertirs;
	}

	public void setRolSucuConvertirs(List<RolSucu> rolSucuConvertirs) {
		this.rolSucuConvertirs = rolSucuConvertirs;
	}

	public BigDecimal getSaldoProveedor() {
		return saldoProveedor;
	}

	public void setSaldoProveedor(BigDecimal saldoProveedor) {
		this.saldoProveedor = saldoProveedor;
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

	public List<Cxp> getCxps() {
		return cxps;
	}

	public void setCxps(List<Cxp> cxps) {
		this.cxps = cxps;
	}

	public Cxp getCxpBuscar() {
		return cxpBuscar;
	}

	public void setCxpBuscar(Cxp cxpBuscar) {
		this.cxpBuscar = cxpBuscar;
	}

	public long getContadorRegCxp() {
		return contadorRegCxp;
	}

	public void setContadorRegCxp(long contadorRegCxp) {
		this.contadorRegCxp = contadorRegCxp;
	}

	public int getNumeroRegCxp() {
		return numeroRegCxp;
	}

	public void setNumeroRegCxp(int numeroRegCxp) {
		this.numeroRegCxp = numeroRegCxp;
	}

	public Integer getPaginaCxp() {
		return paginaCxp;
	}

	public void setPaginaCxp(Integer paginaCxp) {
		this.paginaCxp = paginaCxp;
	}

	public Cxp getCxpSele() {
		return cxpSele;
	}

	public void setCxpSele(Cxp cxpSele) {
		this.cxpSele = cxpSele;
	}

	public LocalDate getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(LocalDate fechaPago) {
		this.fechaPago = fechaPago;
	}

	public List<Parametro> getParametros() {
		return parametros;
	}

	public void setParametros(List<Parametro> parametros) {
		this.parametros = parametros;
	}

	public String getEstadoDocuElecRete() {
		return estadoDocuElecRete;
	}

	public void setEstadoDocuElecRete(String estadoDocuElecRete) {
		this.estadoDocuElecRete = estadoDocuElecRete;
	}

	public BigDecimal getTotalDocumento() {

		this.totalDocumento = BigDecimal.ZERO;

//		El total del documento de totalDocus esta restado las retenciones
//		Aqui se recorre y se suma estas retenciones al total documento
//		por eso se pregunta si valor es menor que 0 ya que las retenciones estan con saldo negativo
//		entonces de estos negativos se suma su valor absoluto
		for (TotalDocu totalDocu : this.totalDocus) {
			if (totalDocu.getDescri().toLowerCase().equals("total documento")
					|| totalDocu.getValor().compareTo(BigDecimal.ZERO) < 0) {

//				this.totalDocumento = this.totalDocumento.add(totalDocu.getValor().setScale(6, RoundingMode.HALF_UP));
				this.totalDocumento = this.totalDocumento.add(totalDocu.getValor().abs());
			}
		}

		return totalDocumento;
	}

	public void setTotalDocumento(BigDecimal totalDocumento) {
		this.totalDocumento = totalDocumento;
	}

	public PersProv getPersProv() {
		return persProv;
	}

	public void setPersProv(PersProv persProv) {
		this.persProv = persProv;
	}

	public int getNumeroRegProv() {
		return numeroRegProv;
	}

	public void setNumeroRegProv(int numeroRegProv) {
		this.numeroRegProv = numeroRegProv;
	}

	public Integer getPaginaProv() {
		return paginaProv;
	}

	public void setPaginaProv(Integer paginaProv) {
		this.paginaProv = paginaProv;
	}

	public long getContadorRegProv() {
		return contadorRegProv;
	}

	public void setContadorRegProv(long contadorRegProv) {
		this.contadorRegProv = contadorRegProv;
	}

	public PersProv getPersProvRegis() {
		return persProvRegis;
	}

	public void setPersProvRegis(PersProv persProvRegis) {
		this.persProvRegis = persProvRegis;
	}

	public List<ProvGrup> getProvGrups() {
		return provGrups;
	}

	public void setProvGrups(List<ProvGrup> provGrups) {
		this.provGrups = provGrups;
	}

	public List<Dimm> getDimmTipoIdens() {
		return dimmTipoIdens;
	}

	public void setDimmTipoIdens(List<Dimm> dimmTipoIdens) {
		this.dimmTipoIdens = dimmTipoIdens;
	}

	public List<Dimm> getDimmTipoIdenProvs() {
		return dimmTipoIdenProvs;
	}

	public void setDimmTipoIdenProvs(List<Dimm> dimmTipoIdenProvs) {
		this.dimmTipoIdenProvs = dimmTipoIdenProvs;
	}

	public List<IngrDetaPrec> getIngrDetaPrecOrdens() {
		return ingrDetaPrecOrdens;
	}

	public void setIngrDetaPrecOrdens(List<IngrDetaPrec> ingrDetaPrecOrdens) {
		this.ingrDetaPrecOrdens = ingrDetaPrecOrdens;
	}

	public IngrDetaPrec getIngrDetaPrecSele() {
		return ingrDetaPrecSele;
	}

	public void setIngrDetaPrecSele(IngrDetaPrec ingrDetaPrecSele) {
		this.ingrDetaPrecSele = ingrDetaPrecSele;
	}

	public BigDecimal getTotalPrecioConImpu() {
		return totalPrecioConImpu;
	}

	public void setTotalPrecioConImpu(BigDecimal totalPrecioConImpu) {
		this.totalPrecioConImpu = totalPrecioConImpu;
	}

	public IngrDimm getIngrDimmSele() {
		return ingrDimmSele;
	}

	public void setIngrDimmSele(IngrDimm ingrDimmSele) {
		this.ingrDimmSele = ingrDimmSele;
	}

	public List<IngrDimm> getIngrDimms() {
		return ingrDimms;
	}

	public void setIngrDimms(List<IngrDimm> ingrDimms) {
		this.ingrDimms = ingrDimms;
	}

	public List<Dimm> getDimmRetes() {
		return dimmRetes;
	}

	public void setDimmRetes(List<Dimm> dimmRetes) {
		this.dimmRetes = dimmRetes;
	}

	public Set<IngrDetaImpu> getIngrDetaImpus() {
		return ingrDetaImpus;
	}

	public void setIngrDetaImpus(Set<IngrDetaImpu> ingrDetaImpus) {
		this.ingrDetaImpus = ingrDetaImpus;
	}

	public IngrDetaImpu getIngrDetaImpuSele() {
		return ingrDetaImpuSele;
	}

	public void setIngrDetaImpuSele(IngrDetaImpu ingrDetaImpuSele) {
		this.ingrDetaImpuSele = ingrDetaImpuSele;
	}

	public List<ProdPrec> getProdPrecDialogos() {
		return prodPrecDialogos;
	}

	public void setProdPrecDialogos(List<ProdPrec> prodPrecDialogos) {
		this.prodPrecDialogos = prodPrecDialogos;
	}

	public List<ProdCost> getProdCostDialogos() {
		return prodCostDialogos;
	}

	public void setProdCostDialogos(List<ProdCost> prodCostDialogos) {
		this.prodCostDialogos = prodCostDialogos;
	}

	private IngrDetaImpu ingrDetaImpuSele;
	private List<IngrDetaImpu> ingrDetaImpusCopia;
	private Set<IngrDetaImpu> ingrDetaImpus;

	public void mostrarIngrDetaImpus() {

		this.ingrDetaImpuSele = new IngrDetaImpu();

		this.ingrDetaImpusCopia = new ArrayList<>();
		this.ingrDetaImpus = new HashSet<>();

		IngrDetaImpu ingrDetaImpuBorrar = new IngrDetaImpu();

//		Saca un respaldo de la lista en caso que se haya hecho algun cambio y quiera cancelar
//		Tambien para volver poner el iva que se borra en este metodo
		this.copiarIngrDetaImpu(ingrDetaSele.getIngrDetaImpus());

		this.ingrDetaImpus = ingrDetaSele.getIngrDetaImpus();

//		Borrar el Iva para que no aparezca en la lista
		for (IngrDetaImpu ingrDetaImpu : ingrDetaImpus) {
			if (ingrDetaImpu.getTipo().equals("IVA")) {
				ingrDetaImpuBorrar = ingrDetaImpu;
			}
		}

		this.ingrDetaImpus.remove(ingrDetaImpuBorrar);
	}

	public void copiarIngrDetaImpu(Set<IngrDetaImpu> ingrDetaImpus) {
		for (IngrDetaImpu ingrDetaImpu : ingrDetaImpus) {
			ingrDetaImpusCopia.add(ingrDetaImpu);
		}

		ingrDetaImpusCopia = Collections.unmodifiableList(ingrDetaImpusCopia);
	}

	public void insertarIngrDetaImpu() {

		IngrDetaImpu ingrDetaImpu = new IngrDetaImpu();
		ingrDetaImpu.setIngrDeta(this.ingrDetaSele);

		this.ingrDetaImpus.add(ingrDetaImpu);
	}

	public void borrarIngrDetaImpu() {
		this.borrarIngrDetaImpu(this.ingrDetaImpuSele);
	}

	public void borrarIngrDetaImpu(IngrDetaImpu ingrDetaImpu) {
		this.ingrDetaImpus.remove(ingrDetaImpu);
	}

	public void cancelarDialogoIngrDetaImpu() {

		this.ingrDetaImpus.clear();

		Set<IngrDetaImpu> ingrDetaImpus = new HashSet<>();

		for (IngrDetaImpu ingrDetaImpu : ingrDetaImpusCopia) {
			ingrDetaImpus.add(ingrDetaImpu);
		}

		this.ingrDetaSele.setIngrDetaImpus(ingrDetaImpus);
	}

	public void aceptarDialogoIngrDetaImpu() {

		for (IngrDetaImpu ingrDetaImpu : this.ingrDetaImpus) {

			Dimm dimmBuscar = new Dimm();
			dimmBuscar.setCodigo(ingrDetaImpu.getCodigo());

			Dimm dimm = this.buscarDimmPorCodigo(dimmBuscar);

			ingrDetaImpu.setDescri(dimm.getDescri());
//			ingrDetaImpu.setCodigo(dimm.getCodigo());
			ingrDetaImpu.setPorcen(dimm.getPorcen());
			ingrDetaImpu.setFactor(dimm.getFactor());
		}

//		Volver a poner el iva que se borro al mostrar la lista
		for (IngrDetaImpu ingrDetaImpu : this.ingrDetaImpusCopia) {
			if (ingrDetaImpu.getTipo().equals("IVA")) {
				this.ingrDetaImpus.add(ingrDetaImpu);
			}
		}

		this.calcularTotalIngres();
	}

	public Dimm buscarDimmPorCodigo(Dimm dimmBuscar) {

		Dimm dimm = new Dimm();

		for (Dimm dimmRecorrer : this.dimmRetes) {
			if (dimmBuscar.getCodigo().equals(dimmRecorrer.getCodigo())) {
				dimm = dimmRecorrer;
			}
		}
		return dimm;
	}

	public IngrDetaImpu crearIngrDetaImpu(IngrDeta ingrDeta, Dimm dimm) {

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

//	Aqui comienza cargar compra

	@Inject
	ProdPrecListaInt prodPrecLista;

	private String claveAcce;
	private String persProvCorreo;
	private boolean cargarDeta = true;

	public List<ProdPrec> buscarProdPrecs(ProdPrec prodPrec) {

		List<ProdPrec> prodPrecs = new ArrayList<>();

		try {

			prodPrecs = prodPrecLista.buscar(prodPrec, null, null);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar productos"));
			e.printStackTrace();
		}

		return prodPrecs;
	}

	public Set<IngrDetaPrec> crearIngrDetaPrec(IngrDeta ingrDeta, ProdPrec prodPrec) {

		List<ProdPrec> prodPrecs = new ArrayList<ProdPrec>();
		Set<IngrDetaPrec> ingrDetaPrecs = new HashSet<IngrDetaPrec>();

		prodPrecs = buscarProdPrecs(prodPrec);

		try {

			prodPrecs = prodPrecLista.filtrarProdPrec(prodPrecs, variablesSesion.getPersUsua(),
					variablesSesion.getRolPrecs(), variablesSesion.getSucursal());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al filtrar precios de productos"));
			e.printStackTrace();
		}

		prodPrecs.forEach((ProdPrec prodPrecCopiar) -> {

			IngrDetaPrec ingrDetaPrec = new IngrDetaPrec();
			ingrDetaPrec.setIngrDeta(ingrDeta);
			ingrDetaPrec.setPrecio(prodPrecCopiar.getPrecio());
			ingrDetaPrec.setFactor(prodPrecCopiar.getFactor());
			ingrDetaPrec.setPrecioConImpu(prodPrecCopiar.getPrecioConImpu());
			ingrDetaPrec.setPrecioSinImpu(prodPrecCopiar.getPrecioSinImpu());
			ingrDetaPrec.setUtilid(prodPrecCopiar.getUtilid());

			ingrDetaPrecs.add(ingrDetaPrec);

		});

		return ingrDetaPrecs;
	}

	public void alAbrirCargarCompra() {

		persProvCorreo = null;
		claveAcce = null;
	}

	@Inject
	ImportarFacturaSriServicio importarFacturaSriServicio;

	@Inject
	ImportarFacturaArchivoServicio importarFacturaArchivoServicio;

	public void cargarXmlDesdeSri() {

		try {

			ImportarDocumeElecSriParametros importarDocumeElecSriParametros = new ImportarDocumeElecSriParametros(
					variablesSesion.getSucursal(), variablesSesion.getPersUsua(), persProvCorreo);

			Ingreso ingresoBuscado = importarFacturaSriServicio.importarFacturaSri(claveAcce,
					importarDocumeElecSriParametros);

			if (ingresoBuscado == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, null,
						"No se encontró la factura en el SRI o no está autorizado - Clave: " + claveAcce));

				return;
			}

			ingresoBuscado.setDocuIngr(ingreso.getDocuIngr());
			ingreso = ingresoBuscado;
			ingrDetaDataTable = new ArrayList<>(ingreso.getIngrDetas());
//			Se coloca este clear porque la clase Retencion viene con un set de reteDetas y 
//			tiene cascade en persist entonces al grabar sale error porque intenta grabar nuevamente
//			con el clear deja el set vacio y grabar el list sin errores
//			Se hace esto porque aqui se ve la retencion en pantalla, mientras que en compra no se ve
//			la retencion entonces graba con el set de reteDeta
//			La IA aconseja si el elemento es visual utilizar list e lugar de set
			ingreso.getIngrDetas().clear();

			calcularTotalIngres();

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Documento cargado desde SRI, revisar y procesar..."));
		} catch (ValidacionNegocioExcepcion e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, null, e.getMessage()));
		} catch (IntegracionExcepcion e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
					"Ocurrió un error al importar el documento electrónico del SRI."));
		} catch (InfraestructuraExcepcion e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
					"Ocurrió un error interno al procesar la información."));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
					"Ocurrió un error inesperado al cargar el documento desde el SRI."));
		}
	}

	public String getClaveAcce() {
		return claveAcce;
	}

	public void setClaveAcce(String claveAcce) {
		this.claveAcce = claveAcce;
	}

	public String getPersProvCorreo() {
		return persProvCorreo;
	}

	public void setPersProvCorreo(String persProvCorreo) {
		this.persProvCorreo = persProvCorreo;
	}

	public boolean isCargarDeta() {
		return cargarDeta;
	}

	public void setCargarDeta(boolean cargarDeta) {
		this.cargarDeta = cargarDeta;
	}

	private UploadedFile uploadedFile;

	public void cargarXmlDesdeArchivo() {

		try {

			subirArchivo();

			ImportarDocumeElecSriParametros importarDocumeElecSriParametros = new ImportarDocumeElecSriParametros(
					variablesSesion.getSucursal(), variablesSesion.getPersUsua(), persProvCorreo);

			Ingreso ingresoBuscado = importarFacturaArchivoServicio.importarXmlDesdeArchivo(claveAcce,
					importarDocumeElecSriParametros);

			if (ingresoBuscado == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, null,
						"No se encontró la factura en el SRI o no está autorizado - Clave: " + claveAcce));

				return;
			}

			ingresoBuscado.setDocuIngr(ingreso.getDocuIngr());
			ingreso = ingresoBuscado;
			ingrDetaDataTable = new ArrayList<>(ingreso.getIngrDetas());
//			Se coloca este clear porque la clase Retencion viene con un set de reteDetas y 
//			tiene cascade en persist entonces al grabar sale error porque intenta grabar nuevamente
//			con el clear deja el set vacio y grabar el list sin errores
//			Se hace esto porque aqui se ve la retencion en pantalla, mientras que en compra no se ve
//			la retencion entonces graba con el set de reteDeta
//			La IA aconseja si el elemento es visual utilizar list e lugar de set
			ingreso.getIngrDetas().clear();

			calcularTotalIngres();

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Documento cargado desde SRI, revisar y procesar..."));
		} catch (ValidacionNegocioExcepcion e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, null, e.getMessage()));
		} catch (IntegracionExcepcion e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
					"Ocurrió un error al importar el documento electrónico del SRI."));
		} catch (InfraestructuraExcepcion e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
					"Ocurrió un error interno al procesar la información."));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
					"Ocurrió un error inesperado al cargar el documento desde el SRI."));
		}
	}

	public void subirArchivo() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		String nombreArchivo;
		String rutaComprobantesDescargados;

		Parametro parametro = new Parametro();

		byte[] bytes = null;
		BufferedOutputStream bufferedOutputStream = null;
		UploadedFile uploadedFile = getUploadedFile();

		parametro = buscarParametro(4251);

		if (parametro.getParametroId() == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepción - Error al buscar ruta de archivos descargados"));
			return;
		}

		rutaComprobantesDescargados = parametro.getDescri();

		if (uploadedFile != null) {

			nombreArchivo = claveAcce;
			bytes = uploadedFile.getContent();

			try {

				bufferedOutputStream = new BufferedOutputStream(
						new FileOutputStream(new File(rutaComprobantesDescargados + nombreArchivo + ".xml")));
				bufferedOutputStream.write(bytes);
				bufferedOutputStream.close();

			} catch (FileNotFoundException e) {

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error crear Stream"));
				e.printStackTrace();
				return;

			} catch (IOException e) {

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al escribir archivo"));
				e.printStackTrace();
			}
		}

		if (this.getUploadedFile() != null) {

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Archivo cargado:  " + this.getUploadedFile().getFileName()));
		}

	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	private boolean existeRetencionNumeroAutorizacion;

	public void validarIngresoNumeroAutorizacion() {

		FacesContext context = FacesContext.getCurrentInstance();

		existeRetencionNumeroAutorizacion = false;

		if (claveAcce == null || claveAcce.isBlank()) {
			return;
		}

		boolean existe = compra.existeNumeroAutorizacion(claveAcce);

		if (existe) {
			existeRetencionNumeroAutorizacion = true;
			UIComponent component = UIComponent.getCurrentComponent(context);

			if (component instanceof UIInput input) {
				input.setValid(false);
			}

			context.addMessage(component.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
					"El número de autorización ya esta registrado en el sistema"));
		}
	}

	public boolean isExisteRetencionNumeroAutorizacion() {
		return existeRetencionNumeroAutorizacion;
	}

	public void setExisteRetencionNumeroAutorizacion(boolean existeRetencionNumeroAutorizacion) {
		this.existeRetencionNumeroAutorizacion = existeRetencionNumeroAutorizacion;
	}
}
