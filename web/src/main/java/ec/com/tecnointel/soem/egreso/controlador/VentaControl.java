package ec.com.tecnointel.soem.egreso.controlador;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
import javax.sql.DataSource;

import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

import ec.com.tecnointel.soem.caja.modelo.Caja;
import ec.com.tecnointel.soem.caja.modelo.CajaDocuEgre;
import ec.com.tecnointel.soem.caja.modelo.CajaMovi;
import ec.com.tecnointel.soem.caja.modelo.PersCaje;
import ec.com.tecnointel.soem.caja.modelo.SaliArch;
import ec.com.tecnointel.soem.contabilidad.modelo.Transaccion;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionFpmeInt;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionGestionInt;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionVentaInt;
import ec.com.tecnointel.soem.documeElec.modelo.factura.Factura;
import ec.com.tecnointel.soem.documeElec.modelo.guia.GuiaRemision;
import ec.com.tecnointel.soem.documeElec.modelo.notaCredito.NotaCredito;
import ec.com.tecnointel.soem.documeElec.modelo.notaDebito.NotaDebito;
import ec.com.tecnointel.soem.documeElec.registroInt.DocumeElecRegisInt;
import ec.com.tecnointel.soem.documeElec.tareas.ManejadorTareaAutoriDocu;
import ec.com.tecnointel.soem.documeElec.tareas.ManejadorTareaEnviarCorreoDocu;
import ec.com.tecnointel.soem.documeElec.tareas.ManejadorTareaEnviarDocu;
import ec.com.tecnointel.soem.egreso.listaInt.EgreNotaListaInt;
import ec.com.tecnointel.soem.egreso.listaInt.EgreTranListaInt;
import ec.com.tecnointel.soem.egreso.modelo.ClieGrup;
import ec.com.tecnointel.soem.egreso.modelo.EgreDeta;
import ec.com.tecnointel.soem.egreso.modelo.EgreDetaImpu;
import ec.com.tecnointel.soem.egreso.modelo.EgreNota;
import ec.com.tecnointel.soem.egreso.modelo.EgreTran;
import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.egreso.modelo.PersClie;
import ec.com.tecnointel.soem.egreso.modelo.PersCobr;
import ec.com.tecnointel.soem.egreso.modelo.PersVend;
import ec.com.tecnointel.soem.egreso.modelo.TotalDocu;
import ec.com.tecnointel.soem.egreso.registroInt.EgreNotaRegisInt;
import ec.com.tecnointel.soem.egreso.registroInt.EgreTranRegisInt;
import ec.com.tecnointel.soem.egreso.registroInt.VentaInt;
import ec.com.tecnointel.soem.firmaElec.registroInt.FirmarArchivoInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.general.util.IdenSistema;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDeta;
import ec.com.tecnointel.soem.inventario.modelo.KardTotaView;
import ec.com.tecnointel.soem.inventario.modelo.Kardex;
import ec.com.tecnointel.soem.inventario.modelo.ProdBode;
import ec.com.tecnointel.soem.inventario.modelo.ProdDimm;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrup;
import ec.com.tecnointel.soem.inventario.modelo.ProdPrec;
import ec.com.tecnointel.soem.inventario.modelo.ProdSubp;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.parametro.controlador.PersonaException;
import ec.com.tecnointel.soem.parametro.listaInt.TranPlanListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Bodega;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.DocuCaja;
import ec.com.tecnointel.soem.parametro.modelo.DocuEgre;
import ec.com.tecnointel.soem.parametro.modelo.DocuMoviEgre;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.parametro.modelo.Mesa;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.modelo.Precio;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.modelo.TranPlan;
import ec.com.tecnointel.soem.parametro.registroInt.DimmRegisInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolDocu;
import ec.com.tecnointel.soem.seguridad.modelo.RolFormPago;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import ec.com.tecnointel.soem.seguridad.registroInt.RolDocuRegisInt;
import ec.com.tecnointel.soem.serWebSri.registroInt.EnvioComprobantesWsInt;
import ec.com.tecnointel.soem.tesoreria.listaInt.FormPagoMoviIngrListaInt;
import ec.com.tecnointel.soem.tesoreria.modelo.CobrDeta;
import ec.com.tecnointel.soem.tesoreria.modelo.Cxc;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviEgre;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import ec.com.tecnointel.soem.tesoreria.modelo.FpmeFormPago;
import ec.com.tecnointel.soem.tesoreria.registroInt.FormPagoMoviIngrRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.Conversation;
import jakarta.enterprise.context.ConversationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;

/**
 * @author Drosan
 * 
 * 
 *         Proceso para descargar Variable que permite descargar el documento:
 *         idDescargar 1. Finalizar Conversacion 2. navegar a ventaDeta 3. pasar
 *         idDescargar como variable de session 4. ejecutar descargar() De
 *         acuerdo a un javascript que esta en ventaDeta este metodo llama a
 *         windows.load y ejecuta con un boton oculto el metodo descargar()
 * 
 */
@Named
@ConversationScoped
public class VentaControl extends PaginaControl implements Serializable {

	private int factor;
	private int numeroRegClie;
	private long contadorRegClie;
	private Integer paginaClie;
	private Integer egresoId;
	private Integer productoId;
	private Integer origenPrecio;
	private Integer idDescargar;

	private long contadorRegCxc;
	private int numeroRegCxc;
	private Integer paginaCxc = 0;

//	BigDecimal descue = new BigDecimal(0);
	private BigDecimal cambio = new BigDecimal(0);
	private BigDecimal saldoCliente = new BigDecimal(0);
	private BigDecimal saldoDisponible = new BigDecimal(0);

	private BigDecimal totalDocumento = new BigDecimal(0);

	private String ordenColumna;

	private String detalleProceso;
	private String ventaDetaAlCargar;
	private String ventaDetaAlSeleProd;
	private String[] digiCodigoPrecio;

	private Boolean grabarImprimirEstado;
//	private Date fechaCobro;

	private long contadorDocuNoAuto = 0;

	private Egreso egreso;
	private EgreDeta egreDetaSele;
	private ProdPrec prodPrec;
	private ProdPrec prodPrecSele;
	private Precio precioPred;
	private PersUsua persUsuaSesion;
	private PersClie persClie;
	private PersClie persClieRegis;
	private FormPagoMoviEgre formPagoMoviEgre;
	private FpmeFormPago fpmeFormPagoSele;
	private FormPago formPagoPred;
	private RolSucu rolSucuConvertir;
	private DocuEgre docuEgreConvertir;
	private CajaMovi cajaMoviConvertir;
	private CajaMovi cajaMoviAbrioSesion;
	private Cxc cxcBuscar;
	private Cxc cxcSele;
	private Dimm dimmIva;
	private Parametro parametroActivarBalanza;
	private EgreNota egreNotaSele = new EgreNota();
	private Producto productoDialogo;

	private List<EgreNota> egreNotas;
	private Set<Integer> sucursals;
	private List<Egreso> egresos;
	private List<Precio> precios;
	private List<EgreDeta> egreDetaDataTable;
	private List<EgreDeta> egreDetaEliminados;
	private List<DocuEgre> docuEgres;
	private List<PersClie> persClies;
	private List<PersVend> persVends;
	private List<Dimm> dimms;
	private List<ProdPrec> prodPrecs;
	private List<ProdPrec> prodPrecSeles;
	private List<RolDocu> rolDocus;
	private List<FormPago> formPagos;
	private List<PersCobr> persCobrs;
	private List<DocuMoviEgre> docuMoviEgres;
	private List<KardTotaView> kardTotaViews;

	private List<RolSucu> rolSucuConvertirs;
	private List<CajaMovi> cajaMoviConvertirs;
	private List<Mesa> mesas;

	private List<Cxc> cxcs;

	private List<DocuMoviEgre> docuMoviEgreTmps;
	private List<FormPago> formPagoTmps;

	private List<ClieGrup> clieGrups;
	private List<Dimm> dimmTipoIdens;
	private List<Dimm> dimmTipoIdenClies;

	private List<Parametro> parametros;

	private List<FpmeFormPago> fpmeFormPagos;

//	TreeMap<String, BigDecimal> totalEgre = new TreeMap<String, BigDecimal>();
	List<TotalDocu> totalDocus = new ArrayList<>();

	@Inject
	private Conversation conversation;

	@Inject
	VentaInt venta;

	// Se injecto solamente para enviar como parametro en tareaCrearRideFactura
	@Inject
	DimmRegisInt dimmRegis;

	@Inject
	DocumeElecRegisInt documeElectRegis;

	@Inject
	EnvioComprobantesWsInt envioComprobantesWs;

	@Inject
	ManejadorTareaEnviarDocu manejadorTareaEnvioDocu;

	@Inject
	ManejadorTareaAutoriDocu manejadorTareaAutoriDocu;

	@Inject
	ManejadorTareaEnviarCorreoDocu manejadorTareaEnviarCorreoDocu;

	@Inject
	IdenSistema idenSistema;

	@Inject
	TranPlanListaInt tranPlanLista;

	@Inject
	TransaccionVentaInt transaccionVenta;

	@Inject
	TransaccionFpmeInt transaccionFpme;

	@Inject
	EgreTranRegisInt egreTranRegis;

	@Inject
	EgreTranListaInt egreTranLista;

	@Inject
	TransaccionGestionInt transaccionGestion;

	@Inject
	FormPagoMoviIngrListaInt formPagoMoviIngrLista;

	@Inject
	FormPagoMoviIngrRegisInt formPagoMoviIngrRegis;

	@Inject
	EgreNotaListaInt egreNotaLista;

	@Inject
	EgreNotaRegisInt egreNotaRegis;

	@Inject
	RolDocuRegisInt rolDocuRegis;

	@Inject
	FirmarArchivoInt firmarArchivo;

	@Resource(mappedName = "java:jboss/datasources/soemDS")
	private DataSource dataSource;

	private static final long serialVersionUID = -4518611523915812275L;

	@PostConstruct
	public void cargar() {

		this.paginaClie = 0;
		this.idDescargar = 0;
		this.origenPrecio = 1;

		this.parametros = new ArrayList<>();

		this.fpmeFormPagos = new ArrayList<FpmeFormPago>();

		this.ventaDetaAlCargar = variablesSesion.getVentaDetaAlCargar();
//		Se une estos dos parametros para controlar la navegación en la pagina Ej:
//		VentaDetaAlSeleProd1 = ventaForm: VentaDetaAlSeleProd2 = buscarProdCodigoBarra Seria: 'ventaForm:buscarProdCodigoBarra'
//		VentaDetaAlSeleProd1 = ventaForm:egresoDetaList:0: VentaDetaAlSeleProd1 = cantidInput Seria: ventaForm:egresoDetaList:0:cantidInput
//		El cero:0 es la referencia a la fila, por eso esta variable esta en dos partes para poder unir si fuera el caso con 
//		un munero diferente de cero ya que el cero:0 siempre hace referencia a la fila 1  
		this.ventaDetaAlSeleProd = variablesSesion.getVentaDetaAlSeleProd1()
				+ variablesSesion.getVentaDetaAlSeleProd2();
//		"ventaForm:egresoDetaList:0:" + 

		this.persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");

		this.egreso = new Egreso();
		this.egreDetaSele = new EgreDeta();

		this.precioPred = new Precio();
		this.precioPred = this.buscarPrecioPred();

		ProdGrup prodGrup = new ProdGrup();
		prodGrup.setModuloVent(true);

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

		Producto producto = new Producto();
		producto.setProdGrup(prodGrup);
		producto.setEstado(true);

		this.prodPrec = new ProdPrec(this.getVariablesSesion().getSucursal(), this.precioPred, producto);

		this.persClie = new PersClie();
		this.persClie.setPersona(new Persona());

		this.egreDetaEliminados = new ArrayList<>();

		rolDocus = variablesSesion.getRolDocus();
		rolPermiso = variablesSesion.getRolPermiso();

		if (variablesSesion.isActivarImagen()) {
			ordenColumna = "codigo";
		} else {
			ordenColumna = "descri";
		}

		this.sucursals = new HashSet<>();
		this.buscarRolSucus();

		this.rolSucuConvertirs = new ArrayList<>();

		try {
			buscarFormatoReportes();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar formato reportes"));
			e.printStackTrace();
		}

//		Busqueda de CXC
		this.cxcBuscar = new Cxc();
		PersClie persClie = new PersClie();
		persClie.setPersona(new Persona());
		Egreso egreso = new Egreso();
		egreso.setPersClie(persClie);

		cxcBuscar.setEgreso(egreso);
		cxcBuscar.setEstado(false);

		this.cxcSele = new Cxc();

		this.cxcs = new ArrayList<>();

		this.docuMoviEgreTmps = new ArrayList<>();

		try {
			this.dimmIva = this.venta.buscarDimmPorId(variablesSesion.getDimmIdIvaActual());
			documeElectRegis.registrarDimmIva(dimmIva);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Ex - No se encuentra Dimm Iva"));
			e.printStackTrace();
		}

//		Carga las leyendas que estan en parametros para poner en 
//		el xml de los documentos electronicos 
		documeElectRegis.leyenda1(variablesSesion.getLeyenda1());
		documeElectRegis.leyenda2(variablesSesion.getLeyenda2());
		documeElectRegis.msgInfoAdicional(variablesSesion.getMsgInfoAdicional());

		this.cajaMoviAbrioSesion = this.buscarSesionVentas();
		if (cajaMoviAbrioSesion.getCajaMoviId() == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, "No ha iniciado una sesión de ventas"));
		}

//		Notas facturación electrónica - infoAdicional
		this.egreNotas = new ArrayList<>();

//		Cargar Datos Balanza
		try {

			parametroActivarBalanza = parametroRegis.buscarPorId(Parametro.class, 3135);
			Parametro parametroBalanzaDigiCodigoPrecio = parametroRegis.buscarPorId(Parametro.class, 3136);
			digiCodigoPrecio = parametroBalanzaDigiCodigoPrecio.getDescri().split(";");

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar parametro 3135 - 3136"));
			e.printStackTrace();
		}
//		Fin Cargar Datos Balanza

	}

	public void crearFilaFpmeFormPago() {

		BigDecimal totalReciFpmeFp = new BigDecimal(0);

		FpmeFormPago fpmeFormPago = new FpmeFormPago();

//		Implementar esto en una proxima actualizacion, 
//		quita las formas de pago ya seleccionadas
//		for (FpmeFormPago fpmeFormPagoRecorrer : this.fpmeFormPagos) {
//			this.formPagos.remove(fpmeFormPagoRecorrer.getFormPago());
//		}

		if (this.fpmeFormPagos.size() == 0 || this.fpmeFormPagos.isEmpty()) {

			fpmeFormPago.setFormPagoMoviEgre(this.formPagoMoviEgre);
			fpmeFormPago.setFormPago(formPagoPred);
			fpmeFormPago.setFecha(this.egreso.getFechaEmis());
			fpmeFormPago.setFechaHora(this.egreso.getFechaEmis().atTime(LocalTime.now()));
			fpmeFormPago.setDiasPlaz((short) 0);
			fpmeFormPago.setTotalReci(this.getTotalDocumento());

		} else {

			fpmeFormPago.setFormPagoMoviEgre(this.formPagoMoviEgre);
			fpmeFormPago.setFormPago(new FormPago());
			fpmeFormPago.setFecha(this.egreso.getFechaEmis());
			fpmeFormPago.setFechaHora(this.egreso.getFechaEmis().atTime(LocalTime.now()));
			fpmeFormPago.setDiasPlaz((short) 0);

			totalReciFpmeFp = this.calcularCambio();
			fpmeFormPago.setTotalReci(totalReciFpmeFp);

		}

//		Se pone siempre el total porque cuando se añade una fila nueva
//		el saldo o cambio se calcula automaticamente, por lo tanto
//		la suma de fpmeFormPago.totalReci siempre va a ser el total del documento
		this.formPagoMoviEgre.setTotalReci(this.getFormPagoMoviEgre().getTotal());
		this.fpmeFormPagos.add(fpmeFormPago);

//		Siempre que se agraga una fila en cambio debe ser cero
//		ya que el total en cada fila aparece automaticamene
		this.cambio = BigDecimal.ZERO;
	}

	public void eliminarFilaFpmeFormPago() {

		this.fpmeFormPagos.remove(this.fpmeFormPagoSele);

		if (this.fpmeFormPagos.size() == 0 || this.fpmeFormPagos.isEmpty()) {
			this.formPagoMoviEgre.setTotalReci(BigDecimal.ZERO);
			this.setCambio(BigDecimal.ZERO);
		} else {
			this.cambio = this.calcularCambio();
		}
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

	public void buscarDimmTipoIden() {
		Dimm dimmDesde = new Dimm();
		Dimm dimmHasta = new Dimm();

		dimmDesde.setTablaRefe("Tabla2c");
		dimmDesde.setEstado(true);
		try {
			this.dimmTipoIdens = venta.buscarDimms(dimmDesde, dimmHasta);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
					"Excepcion - Error al buscar tipo de identificación"));
			e.printStackTrace();
		}
	}

	public void buscarDimmTipoIdenClies() {
		Dimm dimmDesde = new Dimm();
		Dimm dimmHasta = new Dimm();

		dimmDesde.setTablaRefe("Tabla14");
		dimmDesde.setEstado(true);
		try {
			this.dimmTipoIdenClies = venta.buscarDimms(dimmDesde, dimmHasta);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
					"Excepcion - Error al buscar tipo identificación cliente"));
			e.printStackTrace();
		}
	}

	public void buscarClieGrups() {

		ClieGrup clieGrup = new ClieGrup();
		clieGrup.setEstado(true);

		try {
			clieGrups = venta.buscarClieGrups(clieGrup, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al buscar grupo clientes"));
			e.printStackTrace();
		}
	}

	public void seleccionarDimm() {

		for (Dimm dimm : this.dimmTipoIdens) {

			if (dimm.getDimmId().equals(2030) && this.getPersClieRegis().getPersona().getCedulaRuc().length() == 13) {
				this.getPersClieRegis().setDimm(dimm);
			} else if (dimm.getDimmId().equals(2040)
					&& this.getPersClieRegis().getPersona().getCedulaRuc().length() == 10) {
				this.getPersClieRegis().setDimm(dimm);
			}
		}
	}

	public String grabarPersClie() {

		String campoTexto;
		String mensaje = "Error al insertar o modificar cliente";

		campoTexto = this.persClieRegis.getPersona().getCedulaRuc().trim();
		this.persClieRegis.getPersona().setCedulaRuc(campoTexto);

		campoTexto = this.persClieRegis.getPersona().getApelli().trim();
		this.persClieRegis.getPersona().setApelli(campoTexto);

		if (this.persClieRegis.getPersona().getNombre() != null) {
			campoTexto = this.persClieRegis.getPersona().getNombre().trim();
			this.persClieRegis.getPersona().setNombre(campoTexto);
		}

		try {

			if (persClieRegis.getDimm().getDimmId() == 2040
					&& persClieRegis.getPersona().getCedulaRuc().length() != 10) {
				throw new PersonaException("Revisar número de cédula o tipo de identificación");
			}

			if (persClieRegis.getDimm().getDimmId() == 2030
					&& persClieRegis.getPersona().getCedulaRuc().length() != 13) {
				throw new PersonaException("Revisar número de ruc o tipo de identificación");
			}

			if (this.persClieRegis.getPersonaId() == null) {

				Integer persClieId = this.venta.insertarPersCLie(persClieRegis);
				if (persClieId != null) {
					this.getPersClie().setPersonaId(persClieId);
					this.egreso.setPersClie(this.persClieRegis);
					mensaje = "Cliente Registrado";
				}
			} else {
				this.venta.modificarPersClie(persClieRegis);
				this.egreso.setPersClie(this.persClieRegis);
				mensaje = "Cliente Actualizado";
			}

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, mensaje));

		} catch (RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepción - Error cédula o Ruc ya existe"));
			e.printStackTrace();
		} catch (PersonaException pe) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					pe.getMessage() + " .Error al grabar o actualizar cliente"));
			pe.printStackTrace();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepción - Error al grabar o actualizar cliente"));
			e.printStackTrace();
		}

		return null;
	}

	public void iniciarClienteRegisDialogo() {

		this.persClieRegis = new PersClie();
		this.persClieRegis.setPersona(new Persona());
//		this.persClieRegis.setClieGrup(new ClieGrup());
//		this.persClieRegis.setDimm(new Dimm());

		this.buscarDimmTipoIden();
		this.buscarDimmTipoIdenClies();
		this.buscarClieGrups();

		if (this.persClieRegis.getPersonaId() == null) {

			this.persClieRegis.setExonerIva(false);
			this.persClieRegis.setRetienRent(true);
			this.persClieRegis.setRetienIva(true);
			this.persClieRegis.setCorteCred((short) 0);
			this.persClieRegis.setCupo(new BigDecimal(0));
			this.persClieRegis.setDescueMaxi(new BigDecimal(0));
			this.persClieRegis.setEstado(true);

//			if (this.personaId != null) {

//				Persona persona = new Persona();
//				try {
//					persona =  personaRegis.buscarPorId(this.getPersonaId());
//					this.persClie.setPersona(persona);
//				} catch (Exception e) {
//					FacesContext.getCurrentInstance().addMessage(null,
//							new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al persona"));
//					e.printStackTrace();
//				}
//			}
		}
//		} else {
//
//			try {
//				this.persClie = persClieRegis.buscarPorId(this.getId());
//			} catch (Exception e) {
//				FacesContext.getCurrentInstance().addMessage(null,
//						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
//				e.printStackTrace();
//			}
//		}
	}

	public List<Cxc> buscarCxcs() {

		List<CobrDeta> cobrDetas = new ArrayList<>();

		try {

			this.cxcs = venta.buscarCxcs(this.cxcBuscar, this.paginaCxc, variablesSesion.getFilasPagina());
			this.numeroRegCxc = cxcs.size();
			this.contadorRegCxc = venta.contarCxcs(this.cxcBuscar);

			for (Cxc cxcSaldo : cxcs) {

				BigDecimal totalAbono = new BigDecimal(0);

				cobrDetas = this.buscarCobrDetas(cxcSaldo);

				for (CobrDeta cobrDetaCxc : cobrDetas) {
					totalAbono = totalAbono.add(cobrDetaCxc.getTotal());
				}

				cxcSaldo.setAbono(totalAbono);
				cxcSaldo.setSaldo(cxcSaldo.getTotal().subtract(totalAbono));
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}

		return cxcs;
	}

	public List<CobrDeta> buscarCobrDetas(Cxc cxc) throws Exception {

		List<CobrDeta> cobrDetas = new ArrayList<>();

		CobrDeta cobrDeta = new CobrDeta();

		Cxc cxcBuscar = new Cxc();
		cxcBuscar.setCxcId(cxc.getCxcId());
		Egreso egreso = new Egreso();
		cxcBuscar.setEgreso(egreso);

		cobrDeta.setCxc(cxcBuscar);

		try {
			cobrDetas = venta.buscarCobrDetas(cobrDeta);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar detalle de cobros"));
			e.printStackTrace();
		}

		return cobrDetas;
	}

	public void iniciarVentaCabe() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback()) {
			return;
		}

		buscarPersClies(0);
		conversationBegin();

		DocuEgre docuEgrePred = new DocuEgre();
		List<DocuEgre> docuEgres = this.buscarDocuEgres();

		persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");

		formPagoMoviEgre = new FormPagoMoviEgre();

		try {
			this.docuEgres = venta.filtrarDocuEgres(docuEgres, persUsuaSesion, variablesSesion.getRolDocus());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al filtrar documentos egreso"));
			e.printStackTrace();
		}

		this.buscarPersVends();
		this.mesas = this.buscarMesas();

		if (this.egresoId != null) {

			this.buscarEgreso(egresoId);
			this.factor = this.getEgreso().getDocuEgre().getDocumento().getFactor();

			if (this.egreso.getEstado().equals("GR")) {
				this.calcularFechaCobro();
			}

			this.activarBotonAnular();

		} else {
			egreso.setFechaEmis(LocalDate.now());
			egreso.setSerie1("0");
			egreso.setSerie2("0");
			egreso.setNumero(0);
			egreso.setAutori("0");
			egreso.setClaveAcce("0");
			egreso.setDescue(new BigDecimal(0));
			egreso.setNumeroCuot((short) 1);
			// Se diseño con el fin de crear cuotas por 30, 15, 7 o cualquier numero de dias
			// Se elimina de la pagina para generar solamente cuotas mensuales
			egreso.setDiasPlaz((short) 0);
			egreso.setEstado("GR");

			List<Rol> roles = new ArrayList<>();
			Set<RolPersUsua> rolPersUsuas = persUsuaSesion.getRolPersUsuas();
			for (RolPersUsua rolPersUsua : rolPersUsuas) {
				roles.add(rolPersUsua.getRol());
			}

			// Seleccionar el documento predetermido en rolDocu
			for (Rol rol : roles) {
				for (DocuEgre docuEgre : this.docuEgres) {
					for (RolDocu rolDocu : rolDocus) {
						if (docuEgre.getDocumentoId().equals(rolDocu.getDocumento().getDocumentoId())
								&& rol.equals(rolDocu.getRol()) && rolDocu.getPredet() == true) {
							docuEgrePred = docuEgre;
							break;
						}
					}
				}
			}
			this.egreso.setDocuEgre(docuEgrePred);
			// Fin seleccionar el documento predetermido en rolDocu

			// Coloca al valor de la variable factor para habilitar o inhabilitar botones
			// Y para verificar el control de inventario segun el documento que seleccione
			this.factor = docuEgrePred.getDocumento().getFactor();

//			Controla acticavion de boton grabar-imprimir
			this.grabarImprimirEstado = grabarImprVerificarEstado();
//			Seleccionar cliente y vendedor predeterminado
			this.egreso.setPersClie(variablesSesion.getPersClie());
			this.egreso.setPersVend(variablesSesion.getPersVend());

//			Se asigna el descuento del cliente a la cabecera del documento
			if (this.egreso.getPersClie().getDescueMaxi().compareTo(BigDecimal.ZERO) != 0) {
				this.egreso.setDescue(this.egreso.getPersClie().getDescueMaxi());
			}

			if (this.egreso.getDocuEgre().getDocumento().getFactor() == -1) {
				egreso.setNota("Venta");
			} else if (this.egreso.getDocuEgre().getDocumento().getFactor() == 1) {
				egreso.setNota("Nota de Crédito");
			} else if (this.egreso.getDocuEgre().getDocumento().getFactor() == 0) {
				egreso.setNota("Proforma");
			}

			this.calcularFechaCobro();
		}

//		Asigna el cliente que este seleccionado cuando se abre ventaCabe
//		para buscar las Cxc del cliente en caso que sea nota de credito
		this.cxcBuscar.getEgreso().setPersClie(this.egreso.getPersClie());

//		Buscar Documentos de pago para luego filtar de acuerdo a permisos 
//		y de acuerdo a que documento se seleccione
		try {
			docuMoviEgreTmps = venta.buscarDocuMoviEgres();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error buscar documentos forma pago egreso - Tmp"));
			e.printStackTrace();
		}

		try {
			formPagoTmps = venta.buscarFormPago();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error buscar formas de pago - Tmp"));
			e.printStackTrace();
		}

		if (cajaMoviAbrioSesion.getCajaMoviId() != null) {
			consultarEgresosNoAutorizados();
		}

	}

//	Buscar y asigna impuestos
//	Buscar y asigna SubProductos en egreDetas (que es un set<> de egreDeta) 
//	en caso que sea una factura ya grabada o procesada
//	parametro egreDeta es una busqueda recursiva
	public void iniciarVentaDeta() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return; // Skip postback requests.
		}

//		if (cajaMoviAbrioSesion.getCajaMoviId() == null) {
//			return;
//		}

		this.buscarProdPrecs(0);

		this.idDescargar = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("idDescargar");
		if (this.idDescargar == null) {
			this.idDescargar = 0;
		}

		this.egreDetaDataTable = new ArrayList<EgreDeta>();
		List<EgreDeta> egreDetas = new ArrayList<>();

//		EgreDeta egreDeta = new EgreDeta();
//		egreDeta.setEgreDeta(new EgreDeta());
//		
//		Egreso egreso = new Egreso();
//		egreDeta.setEgreso(egreso);

		EgreDeta egreDeta = new EgreDeta(new Egreso(), new EgreDeta());

		if (this.egresoId != null) {

			egreDeta.getEgreso().setEgresoId(this.egresoId);

			try {

//				Buscar EgreDeta sin buscar subproductos (egreDetaSupeId = null) 
				egreDetas = venta.buscarEgreDetas(egreDeta, null);
				for (EgreDeta egreDeta2 : egreDetas) {

//					Busca y asigna los SubProductos que esten ya grabados en egreDeta (egreDetaSupeId = egreDeta2) 
//					ya no debe buscar los subproductos de prodSubp esto con el proposito de implementar
//					la eliminación de subproductos al hacer la factura
					EgreDeta egreDetaSubProductoBuscar = new EgreDeta(egreso, egreDeta2);

//					List<EgreDeta> egreDetaTmpSubProductos = this.buscarEgreDetaSubProductos(egreDetaSubProductoBuscar);
					List<EgreDeta> egreDetaTmpSubProductos = venta.buscarEgreDetas(egreDetaSubProductoBuscar, null);

					Set<EgreDeta> egreDetaSubProductos = new HashSet<>(egreDetaTmpSubProductos);
					egreDeta2.setEgreDetas(egreDetaSubProductos);

//					Buscar y asigna impuestos
					List<EgreDetaImpu> egreDetaImpus = new ArrayList<>();
					EgreDetaImpu egreDetaImpu = new EgreDetaImpu();
					egreDetaImpu.setEgreDeta(egreDeta2);

					egreDetaImpus = venta.buscarEgreDetaImpus(egreDetaImpu);

					Set<EgreDetaImpu> egreDetaImpus2 = new HashSet<EgreDetaImpu>(egreDetaImpus);
					egreDeta2.setEgreDetaImpus(egreDetaImpus2);

					this.egreDetaDataTable.add(0, egreDeta2);

				}

				this.calcularTotalEgres();

				// Se coloca la variable de impresion para poder reimprimir o descargar un
				// documento ya hecho
				// porque para imprimir se busca el Id del egreso en la sesion del usuario
				if (!this.egreso.getEstado().equals("GR")) {
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idDescargar",
							this.egresoId);
				}

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepción al buscar detalle documento"));
				e.printStackTrace();
			}
		}
	}

	public List<EgreTran> buscarEgreTrans(EgreTran egreTran) {

		List<EgreTran> egreTrans = new ArrayList<>();

		try {
			egreTrans = egreTranLista.buscar(egreTran, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar detalle de trasacciones"));
			e.printStackTrace();
		}

		return egreTrans;

	}

	public String buscarPersClies(Integer paginador) {

		if (paginador == 0) {
			this.paginaClie = 0;
		}

		if (this.persClie.getPersona().getCedulaRuc() != null) {
			this.persClie.getPersona().setApelli(null);
			this.persClie.getPersona().setNombre(null);
		}

		try {
			this.persClie.setEstado(true);

			this.persClies = venta.buscarPersClies(this.persClie, this.paginaClie,
					variablesSesion.getFilasClientesEgreso());
			this.numeroRegClie = persClies.size();
			this.contadorRegClie = venta.contarRegistrosClie(persClie);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar clientes"));
			e.printStackTrace();
		}

		return null;

	}

	public List<Mesa> buscarMesas() {

		Mesa mesa = new Mesa();
		List<Mesa> mesas = new ArrayList<>();

		mesa.setEstado(true);

		try {

			mesas = venta.buscarMesas(mesa);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar sucursales del rol"));
			e.printStackTrace();
		}

		return mesas;
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

	public List<TranPlan> buscarTranPlans() {

		TranPlan tranPlan = new TranPlan();

		List<TranPlan> tranPlans = new ArrayList<>();

		tranPlan.setModulo("COBROS");
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

//	Elimina el registro que se haya seleccionado para cxcSele
//	En caso que ya no se quiera que la nota de credito afecte a ninguna CXC
	public void elimnarCxc() {
		this.cxcSele = new Cxc();
		this.egreso.setEgreso(this.cxcSele.getEgreso());
	}

	// <<<<<<<<<<<<<<<<<<<< BOTONES >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< BOTONES >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< BOTONES >>>>>>>>>>>>>>>>>>>>

	// Grabar: solamente graba el documentos
	// Procesar: Grabar genera CXP, contabilidad
	// Anular: Elimina egreDeta, kardex, Cxc con sus respectivos pagos,
	// contabilidad,
	// retencion

	// Si se necesita modificar un egreso se puede:
	// 1. Seleccionar la opcion grabar para modificar el egreso y modificar
	// manualmente CXP, PAGOS, CONTABILIDAD, RETENCION
	// 2. Anular el documento y volver a procesar y si tiene pagos ingresar
	// manualmente
	// ya que al anular se eliminan todos los movimientos de esta egreso

	public String grabar() {

		String estadoDocu;
		String navegar = null;

//		Si el usuario solo graba en este campo se pone nulo ya que en el caso que sea 
//		nota de credito se tiene que volver a seleccionar a que documento se va a afectar
//		y esto se grabara al momento de procesar el documento
		this.egreso.setEgreso(null);

		estadoDocu = this.grabarEgreso("GR");

		if (estadoDocu == "grabado") {

			this.conversationEnd();

			navegar = "ventaDeta?faces-redirect=true";

			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Documento grabado No.:" + this.egreso.getNumero()));
		}

		return navegar;
	}

	public String grabarImpr() {

		String estadoDocu;
		String navegar = null;

//		Si el usuario solo graba en este campo se pone nulo ya que en el caso que sea 
//		nota de credito se tiene que volver a seleccionar a que documento se va a afectar
//		y esto se grabara al momento de procesar el documento
		this.egreso.setEgreso(null);

		estadoDocu = this.grabarEgreso("GR");

		if (estadoDocu == "grabado") {

			this.conversationEnd();

			navegar = "ventaDeta?faces-redirect=true";

			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idDescargar",
					this.getEgreso().getEgresoId());

			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Documento grabado No.:" + this.egreso.getNumero()));
		}

		return navegar;
	}

	public Long contarEgresos() {

		Long totalEgresos = null;

		Egreso egreso = new Egreso();
		PersClie persClie = new PersClie();
		persClie.setPersona(new Persona());

		DocuEgre docuEgre = new DocuEgre();
		docuEgre.setDocumento(new Documento());

		egreso.setSucursal(new Sucursal());

		CajaMovi cajaMoviNotaCred = new CajaMovi();
		cajaMoviNotaCred.setCaja(new Caja());

		egreso.setCajaMovi(cajaMoviNotaCred);
		egreso.setPersClie(persClie);
		egreso.setDocuEgre(docuEgre);

		Set<Integer> sucursalsBuscar = new HashSet<>();

		try {
			totalEgresos = venta.contarEgresos(egreso, sucursalsBuscar);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al contar egresos"));
			e.printStackTrace();
		}

		return totalEgresos;
	}

//	Recorre egreDetas y graba detalle de subProductos en egreDeta
	public String grabarEgreso(String estado) {

//		Validar Licencia
		if (!idenSistema.getEstadoLicencia().equals("activada")) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Licencia no activada"));

			Long registros = this.contarEgresos();
			if (registros >= 10) {

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"El periodo de prueba ha finalizado. Por favor comuniquese con su distribuidor para obtener su licencia"));
				return null;
			}
		}
//		Fin Validar Licencia

		TotalDocu totalDocu = new TotalDocu();
		totalDocu.setDescri("Total Documento");
		int indice = this.totalDocus.indexOf(totalDocu);

		if (this.egreso.getPersClie().getPersona().getCedulaRuc().equals("9999999999999")
				&& !this.egreso.getDocuEgre().getDocumeElec().equals("Ninguno") && totalDocus.get(indice).getValor()
						.setScale(6, RoundingMode.HALF_UP).compareTo(new BigDecimal(50)) == 1) {

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"No se puede crear una factura a consumidor final por mas de Usd 50.00"));

			return null;
		}

//		Graba el total de la factura ; estas tres lineas ya estan mas arriba
//		TotalDocu totalDocu = new TotalDocu();
//		totalDocu.setDescri("Total Documento");
//		int indice = this.totalDocus.indexOf(totalDocu);
		egreso.setTotal(totalDocus.get(indice).getValor());
//			Fin Graba el total de la factura

		String estadoDocu;

		CajaDocuEgre cajaDocuEgre = new CajaDocuEgre();
		CajaMovi cajaMovi = new CajaMovi();

		try {

			egreso.setFechaHoraEmis(egreso.getFechaEmis().atTime(LocalTime.now()));
			egreso.setEstado(estado);

			// Graba cabecera
			if (egresoId == null) {

//					Se coloca la sucursal en el caso de que se este convirtiendo documento
				Sucursal sucursal = new Sucursal();
				if (this.rolSucuConvertir == null) {
					sucursal = this.variablesSesion.getSucursal();
				} else {
					sucursal = this.rolSucuConvertir.getSucursal();
				}
//					Fin Se coloca la sucursal en el caso de que se este convirtiendo documento

//					Se coloca cajaMovi en el caso de que se este convirtiendo documento
				if (cajaMoviConvertir == null) {
					cajaMovi = this.buscarSesionVentas();
				} else {
					cajaMovi = cajaMoviConvertir;
				}
//					Fin Se coloca cajaMovi en el caso de que se este convirtiendo documento

				// Grabar el número de la factura
				// Recuperar el último número de factura
				cajaDocuEgre = this.buscarCajaDocuEgre(this.egreso, cajaMovi);
				this.egreso.setSerie1(cajaDocuEgre.getSerie1());
				this.egreso.setSerie2(cajaDocuEgre.getSerie2());
				this.egreso.setNumero(cajaDocuEgre.getNumero() + 1);
				// Fin Recuperar el último número de factura

				// Actualizar número de factura secuencial
				cajaDocuEgre.setNumero(this.egreso.getNumero());
				this.modificarCajaDocuEgre(cajaDocuEgre);
				// Fin Actualizar número de factura secuencial

				try (Formatter formatter = new Formatter()) {
					String numero = formatter.format("%09d", this.getEgreso().getNumero()).toString();

					String claveAcce = documeElectRegis.generarClaveAcceso(sucursal, this.getEgreso().getFechaEmis(),
							this.getEgreso().getDocuEgre().getDimm().getCodigo(), this.getEgreso().getSerie1(),
							this.getEgreso().getSerie2(), numero, this.getEgreso().getDocuEgre().getAmbien(),
							this.getEgreso().getDocuEgre().getTipoEmis(), variablesSesion.getCodiClav());
					// Fin Generar clave acceso

					this.egreso.setClaveAcce(claveAcce);
				}

				this.egreso.setSucursal(sucursal);

				Bodega bodega = new Bodega();
				bodega.setBodegaId(1);
				this.egreso.setBodega(bodega);

				if (!this.isActivarMesa()) {
					this.egreso.setMesa(this.getMesas().get(0));
				}
				this.egreso.setCajaMovi(cajaMovi);
				this.egreso.setEstadoDocuElec("NO ENVIADO");
				this.egreso.setLeyen1(variablesSesion.getLeyenda1());
				this.egreso.setLeyen2(variablesSesion.getLeyenda2());
				this.egreso.setMsgInfoAdic(variablesSesion.getMsgInfoAdicional());

				// Se asigna la variable egresoId ára poder buscar nuevamente el egreso actual
				// y poder modificar su estado al realizar un proceso de facturación electrónica
				this.egresoId = venta.grabar(this.egreso);

			} else {
				venta.modificar(this.egreso);
			}

//			Graba Detalles
			for (EgreDeta egreDeta : this.egreDetaDataTable) {

				egreDeta.setEgreso(this.egreso);

				if (egreDeta.getEgreDetaId() == null) {
					venta.grabarEgreDeta(egreDeta);
				} else {
					venta.modificarEgreDeta(egreDeta);
				}

//				Graba detalle de subProductos
				Set<EgreDeta> egreDetaSubProductos = egreDeta.getEgreDetas();

				if (!egreDetaSubProductos.isEmpty()) {
					for (EgreDeta egreDetaSubProducto : egreDetaSubProductos) {

						egreDetaSubProducto.setEgreso(this.egreso);

						if (egreDetaSubProducto.getEgreDetaId() == null) {
							venta.grabarEgreDeta(egreDetaSubProducto);
						} else {
							venta.modificarEgreDeta(egreDetaSubProducto);
						}
					}
				}
			}

			// Elimina ingreDeta usa CascadeDelete para eliminar egreDetaImpu
			for (EgreDeta egreDeta : this.egreDetaEliminados) {
				// Se busca nuevamente por Id para que el registro vuela a
				// relacionarse con EgreDetaImpu
				// sino borra todos los EgreDetaImpu de todas las facturas
				EgreDeta egreDetaEliminar = new EgreDeta();
				egreDetaEliminar = venta.buscarEgreDetaPorId(egreDeta);

//				Puede ir esto para saber que es combo y mejorar el proceso ????
//				if (egreDeta.getProducto().isCombo()) {
//				}					
//				Eliminar Subproductos
//				Se vuelve a realizar la busqueda de subproductos solamente por seguridad
				EgreDeta egreDetaBuscar = new EgreDeta(egreDetaEliminar.getEgreso(), egreDetaEliminar);
				List<EgreDeta> egreDetaSubProductos = venta.buscarEgreDetas(egreDetaBuscar, null);
				for (EgreDeta egreDetaSubProducto : egreDetaSubProductos) {
					venta.eliminarEgreDeta(egreDetaSubProducto);
				}

				venta.eliminarEgreDeta(egreDetaEliminar);
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

	public boolean validarUrl() {

		Boolean urlValido = false;
		try {

			urlValido = envioComprobantesWs.validarUrl(variablesSesion.getProxyIp(), variablesSesion.getProxyPuerto(),
					this.egreso.getDocuEgre().getAmbien(), variablesSesion.getUrlProduccion(),
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
					variablesSesion.getProxyPuerto(), this.egreso.getDocuEgre().getAmbien(),
					variablesSesion.getUrlProduccion(), variablesSesion.getUrlPruebas(), "RecepcionComprobantesOffline",
					variablesSesion.getRutaCertificadoSerWebSri());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"No existe conexión con el Sri, El documento no se enviara"));
			e.printStackTrace();
		}

		return existeConexion;
	}

	public String pagar() {

		Integer cxcId = 0;

		String estadoDocu = null;
		String estado = this.estadoActual();

		if (estado.equals("AN")) {

			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, "No se puede pagar un documento anulado"));

			return "/ppsj/egreso/egreso/lista.xhtml?faces-redirect=true";
		}

		if (estado.equals("GR")) {

			estadoDocu = this.grabarEgreso("PR");

			if (estadoDocu == "grabado") {

				this.conversationEnd();

//				Graba notas- informacion adicional
//				Este metodo va aqui para que se grabe al procesar la factura
//				si solo se graba la factura no se graba esta informacion y hay que volver a digitar
				this.grabarEgreNotas();

				if (this.getEgreso().getDocuEgre().getDocumento().getFactor() != 0) {
					this.crearKardex();
				}

//				Si el documento es diferente de proforma graba formPagoMoviEgre
//				y fpmeFormPago.
				if (this.egreso.getDocuEgre().getDocumento().getFactor() != 0) {
					this.insertarFormPagoMoviEgre();
				}

//				Crea cxc y formPagoMoviEgre porque esta pagando directamente desde el modulo de Ventas
//				La otra forma de pagar es por el modulo de CXC
				if (this.getEgreso().getDocuEgre().isGeneraCxc()) {

					FpmeFormPago fpmeFormPagoCrear = new FpmeFormPago();
					BigDecimal totalReci = new BigDecimal(0);

//					Creacion de cxc
					for (FpmeFormPago fpmeFormPago : this.fpmeFormPagos) {

//						En este if se crea las cxc que se haya seleccionado como credito
//						ya sea una o varias cxc, en el else se crea una cxc y un formPagoMoviEgre
//						por las demas formas de pago
						if (fpmeFormPago.getFormPago().getTipo().equals("CR")) {

//							Aqui puede crear una o mas cxc
							this.crearCxc(fpmeFormPago, false);
						} else {

							totalReci = totalReci.add(fpmeFormPago.getTotalReci());
						}
					}

//					Si totalReci es diferente de cero quiere decir que existe una o mas 
//					formas de pago que no son CR, entonce se crea una cxc para estas
//					formas de pago
					if (totalReci.compareTo(BigDecimal.ZERO) > 0) {

						fpmeFormPagoCrear.setFecha(this.egreso.getFechaEmis());
						fpmeFormPagoCrear.setFechaHora(this.egreso.getFechaEmis().atTime(LocalTime.now()));
						fpmeFormPagoCrear.setDiasPlaz((short) 0);
						fpmeFormPagoCrear.setTotalReci(totalReci);

						cxcId = this.crearCxc(fpmeFormPagoCrear, true);
					}

					for (FpmeFormPago fpmeFormPago : this.fpmeFormPagos) {
//							Procesa todos los movimientos cuando el tipo de forma de pago es FP, AN, RR, RI. La foma de pago "NC"
//							no deberia aperecer ya que es una venta y "NC" aparece solo cuando el documento es nota N/C
						if (fpmeFormPago.getFormPago().getTipo().equals("FP")
								|| fpmeFormPago.getFormPago().getTipo().equals("AN")
								|| fpmeFormPago.getFormPago().getTipo().equals("RR")
								|| fpmeFormPago.getFormPago().getTipo().equals("RI")) {

							// Se bajo este if (el anterior esta comentado mas arriba)
							// para que grabe la forma de pago credito
							// lo que en la version 1.1 no hacia
							if (totalReci.compareTo(BigDecimal.ZERO) > 0) {

								Cxc cxc = new Cxc();
								cxc.setCxcId(cxcId);

								this.insertarCobrDeta(fpmeFormPago, cxc);
							}
						}
					}
				} else {
//					Si se cumplen esta condicion es una nota de credito
					if (this.egreso.getDocuEgre().getDocumento().getFactor() == 1) {

//						Si se ha seleccionado una CXC osea cxcSele.getCxcId() es difere de null hace movimientos en el modulo de CXC, 
//						caso contraio solo hace movimientos en el modulo de contabilidad
//						if (this.cxcSele.getCxcId() != null) {

//							Como se esta procesando una nota de credito todas las formas de pago tiene que generar abono a una cxc
//							Entonces si se selecciono una CXC (cxcSele) se hace un abono caso contrario se hace un anticipo 
//						if (this.cxcSele.getSaldo().compareTo(this.formPagoMoviEgre.getTotal()) >= 0) {

						for (FpmeFormPago fpmeFormPago : this.fpmeFormPagos) {

							if (fpmeFormPago.getFormPago().getTipo().equals("NC")) {

//									Se asigna de la lista que selecciono el usuario
								cxcId = this.cxcSele.getCxcId();

//								this.insertarFormPagoMoviEgre();

								this.insertarCobrDeta(fpmeFormPago, cxcSele);

//								Modificar estado de cxp
//								En esta comparacion puede ser que cxcSele.getTotal sea menor que fpmi.getTotal
//								porque en nota de credito no se toman en cuenta las retenciones, entonces el total de 
//								puede ser 100 y el total de cxpSele 90, en este caso el saldo de cxp quedaria negativo
								if (this.cxcSele.getSaldo().setScale(2, RoundingMode.HALF_UP).compareTo(
										this.formPagoMoviEgre.getTotal().setScale(2, RoundingMode.HALF_UP)) <= 0) {
									try {

										this.cxcSele.setEstado(true);
										this.venta.modificarCxc(cxcSele);

									} catch (Exception e) {
										e.printStackTrace();
										FacesContext.getCurrentInstance().addMessage(null,
												new FacesMessage(FacesMessage.SEVERITY_WARN, null,
														"No se ha modificado el estado de cuenta por Cobrar"));
									}
								}
							}
						}
					}
				}

//				Si el documento es electronico se procesa y se envia por correo
				if (!this.egreso.getDocuEgre().getDocumeElec().equals("Ninguno")) {
//					Si el parametro es true envia la factura electrónica
//					si es false quiere decir que el envio es manual y 
//					se debe enviar desde la pagina reprocesar
					if (this.variablesSesion.getFactElecVentaAuto().equals("Activar")) {

//						if (this.validarUrl() == true) {
//							if (this.validarConexion() == true) {

						String estadoFactElec = this.procesarComprobanteElectronico();
//								egresoId se asigna al momento de grabar el registro actual
						this.buscarEgreso(this.egresoId);
						this.egreso.setMotivoRech(this.detalleProceso);
						this.egreso.setEstadoDocuElec(estadoFactElec);

						try {
							venta.modificar(this.egreso);
						} catch (Exception e) {

							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
									FacesMessage.SEVERITY_INFO, null, "Error al cambiar estado del documento"));
							e.printStackTrace();
						}

//								this.crearRideFacturaPdf(this.egresoId);
//							} else {
//								FacesContext.getCurrentInstance().addMessage(null,
//										new FacesMessage(FacesMessage.SEVERITY_INFO, null,
//												"No existe conexión con el Sri, documento no enviado"));
//							}
//						} else {
//							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
//									FacesMessage.SEVERITY_INFO, null, "Url no esta configurado correctamente"));
//						}
					}
				} else {

//					Si el documento no es electronico se envia por correo solo cuando tiene marcada la opcion de enviar por correo
//					Por ejemplo se puede enviar las proformas por correo electrónico
					if (this.egreso.getDocuEgre().getDocumento().isEnviaCorreo()) {

						this.ejecurarTareaCrearRideFactura();

						if (this.egreso.getPersClie().getPersona().getCorreo() != null
								&& this.egreso.getDocuEgre().getDocumento().isEnviaCorreo()) {
							this.ejecutarTareaEnviarCorreoDocu(this.egreso.getPersClie().getPersona().getCorreo(),
									this.egreso.getClaveAcce());
						}
					}
				}
				FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
						"Documento procesado No.:" + this.egreso.getNumero()));
			}
		}

//		Se contabiliza por separado ventas y cobros en el caso que se quiera contabilizar
//		la venta pero no el cobro o el cobro pero no la venta

//		Si el parametro de generacion por documento esta activado
//		genera una transaccion por cada documento
		if (variablesSesion.getGeneraTransaccion().equals("DO")) {

//			Contabiliza Venta
			if (this.getEgreso().getDocuEgre().getDocumento().isContab()) {

				this.contabilizarVenta(egreso);

			}

//		Contabiliza el cobro 
//	 	Si ya contabilizo la venta y si el documento Genera CXC y si el factor == -1
//	 	Si el documento no generaCxp se entiende que es una nota de credito
//	 	las notas de credito generan abonos, pero el abono se resta del modulo CXP
//	 	y solamente se contabiliza la nota de credito, no el pago, como en el caso
//	 	de que sea una compra que se esta pagando con cheque
			if (this.egreso.getDocuEgre().isGeneraCxc() && this.egreso.getDocuEgre().getDocumento().getFactor() == -1) {

//			Deberia entrar aca si el documento es diferente de nota de credito
//			entonces pregunta si la forma de pago procesa el credito, entonces se
//			entiende que de acuerdo a la forma de pago se selecciono una diferente
//			de credito y por lo tanto se procede a contabilizar en pago que se esta
//			haciendo directamente en este modulo (Venta)
//			Si la forma de pago no procesara el credito se contabiliza solamente la
//			compra como tal

//			Si el documento esta marcado para contabilizar se procede a generar
//			la transaccion
				if (this.formPagoMoviEgre.getDocuMoviEgre().getDocumento().isContab()) {

					if (formPagoMoviEgre.getDocuMoviEgre().getTipo().equals("PAGO-COBRO")) {

						this.contabilizarCobro(formPagoMoviEgre);

					} else if (this.formPagoMoviEgre.getDocuMoviEgre().getTipo().equals("DEPOSITO")) {

						this.contabilizarCobro(formPagoMoviEgre);

						Set<FpmeFormPago> fpmeFps = new HashSet<>(fpmeFormPagos);
						formPagoMoviEgre.setFpmeFormPagos(fpmeFps);

						List<FormPagoMoviIngr> fpmis = crearFpmiDepositos(formPagoMoviEgre);
						insertarFpmiDepositos(fpmis);
					}
				}
			}
		}

		return estadoDocu;
	}

	public Integer contabilizarVenta(Egreso egreso) {

		Integer transaccionId = 0;

		HashMap<Integer, String> transaccionInfo = new HashMap<Integer, String>();

		try {

//			Modifica la nota del ingreso para pasar eso a la nota de la tansaccion
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

			egreso.setNota(egreso.getNota() + " " + egreso.getSerie1() + "-" + egreso.getSerie2() + "-"
					+ egreso.getNumero() + " " + egreso.getPersClie().getPersona().getApelli() + " "
					+ egreso.getFechaEmis().format(dateTimeFormatter));

//			Revisar que la nota no tenga mas de 255 caracateres
			if (egreso.getNota().length() > 255) {
				egreso.setNota(egreso.getNota().substring(0, 254));
			}

			// Si cumple estas condiciones es una venta
			if (egreso.getDocuEgre().getDocumento().getFactor() == -1) {

				transaccionId = transaccionVenta.contabilizarVenta(egreso);
				if (transaccionId != 0) {
					transaccionInfo.put(transaccionId, "VENTA");
				}

				transaccionId = transaccionVenta.contabilizarCostoVenta(egreso);
				if (transaccionId != 0) {
					transaccionInfo.put(transaccionId, "COSTO");
				}

			}

			// Si cumple estas condiciones es una nota de credito
			if (egreso.getDocuEgre().getDocumento().getFactor() == 1) {

				transaccionId = transaccionVenta.contabilizarNotaCredito(egreso, formPagoMoviEgre);
				if (transaccionId != 0) {
					transaccionInfo.put(transaccionId, "NC");
				}

				transaccionId = transaccionVenta.contabilizarCostoVenta(egreso);
				if (transaccionId != 0) {
					transaccionInfo.put(transaccionId, "NC-COSTO");
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, null, "Transacción contable no se ha generado"));
			e.printStackTrace();
		}

//		Graba transacciones generadas en egreTrans
//		transaccionInfo contiene los Id de las transacciones generadas
//		Se le hace de esta manera ya que si por ejemplo da un error al grabar egreTrans 
//		en this.contabilizar()  el codigo se detiene y
//		no se generaban las de cobros, costo de ventas, etc
//		Aqui se espera que se grabe todas las transacciones y ahi graba la lista en cajaMoviTrans
		List<EgreTran> egreTrans = crearEgreTrans(egreso, transaccionInfo);

		for (EgreTran egreTran : egreTrans) {
			grabarEgreTran(egreTran);
		}

		return transaccionId;
	}

	public void grabarEgreTran(EgreTran egreTran) {

		try {
			egreTranRegis.insertar(egreTran);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al grabar información de transacción"));
			e.printStackTrace();
		}
	}

	public Integer contabilizarCobro(FormPagoMoviEgre formPagoMoviEgre) {

		Integer transaccionId = 0;

		try {

//			Modifica la nota del ingreso para pasar eso a la nota de la tansaccion
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

//			Revisar que la nota no tenga valores nulos
			if (formPagoMoviEgre.getNota() == null) {
				formPagoMoviEgre.setNota(formPagoMoviEgre.getDocuMoviEgre().getDocumento().getDescri() + " "
						+ formPagoMoviEgre.getNumero() + " " + formPagoMoviEgre.getPersona().getApelli() + " Ref:"
						+ formPagoMoviEgre.getRefere() + " " + formPagoMoviEgre.getFecha().format(dateTimeFormatter));
			} else {
				formPagoMoviEgre.setNota(formPagoMoviEgre.getNota() + " "
						+ formPagoMoviEgre.getDocuMoviEgre().getDocumento().getDescri() + " "
						+ formPagoMoviEgre.getNumero() + " " + formPagoMoviEgre.getPersona().getApelli() + " Ref:"
						+ formPagoMoviEgre.getRefere() + " " + formPagoMoviEgre.getFecha().format(dateTimeFormatter));
			}

//			Revisar que la nota no tenga mas de 255 caracateres
			if (formPagoMoviEgre.getNota().length() > 255) {
				formPagoMoviEgre.setNota(formPagoMoviEgre.getNota().substring(0, 254));
			}

			transaccionId = transaccionFpme.contabilizarCobro(formPagoMoviEgre);

			if (transaccionId != 0) {

				Transaccion transaccion = new Transaccion();
				transaccion.setTransaccionId(transaccionId);

				formPagoMoviEgre.setTransaccion(transaccion);
				try {
					this.venta.modificarFpme(formPagoMoviEgre);
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
							null, "Excepcion - No se ha registrado el Id de la transaccion en cobro"));
					e.printStackTrace();
					return null;
				}
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, null,
					"Transacción contable por el cobro del documento no se ha generado"));
			e.printStackTrace();
		}

		return transaccionId;
	}

	public List<EgreTran> crearEgreTrans(Egreso egreso, HashMap<Integer, String> transaccionInfo) {

		List<EgreTran> egreTrans = new ArrayList<>();

		Iterator<Map.Entry<Integer, String>> iteratorTransaccionInfo = transaccionInfo.entrySet().iterator();
		while (iteratorTransaccionInfo.hasNext()) {
			Map.Entry<Integer, String> entryTransaccionInfo = iteratorTransaccionInfo.next();

			Transaccion transaccion = new Transaccion();
			transaccion.setTransaccionId(entryTransaccionInfo.getKey());
			String descri = entryTransaccionInfo.getValue();

			EgreTran egreTran = new EgreTran(egreso, transaccion, descri);
			egreTrans.add(egreTran);
		}

		return egreTrans;
	}

	public List<FormPagoMoviIngr> crearFpmiDepositos(FormPagoMoviEgre fpme) {

		List<FormPagoMoviIngr> fpmis = new ArrayList<>();

		try {
			for (FpmeFormPago fpmeFp : fpme.getFpmeFormPagos()) {
				fpmis.add(transaccionFpme.crearDeposito(fpmeFp));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, null, "No se ha generado lista de depositos"));
			e.printStackTrace();
		}

		return fpmis;
	}

	public void insertarFpmiDepositos(List<FormPagoMoviIngr> fpmis) {

		try {
			transaccionFpme.insertarFpmiDepositos(fpmis);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, null, "Error al insertar deposito - FPMI"));
			e.printStackTrace();
		}
	}

//	Se ejecuta al dar click sobre algun dato en la lista de cxc (cxcList)
//	Se le colaca para actualizar en numero de documento que se va a modificar del outputLabel id="cxcSele"
//	Se coloca el egreso que se esta modificando en caso de ser nota de credito y con esto
//	se graba en la base en el campo egreso_supe_id
	public void seleccionarCxc() {
		this.egreso.setEgreso(this.cxcSele.getEgreso());
	}

//	Se ejecuta al abrir el dialogo del pago
	public void cargarFormPagoMoviEgre() {

		CajaMovi cajaMovi = new CajaMovi();
		DocuMoviEgre docuMoviEgrePred = null;

		this.docuMoviEgres = new ArrayList<>();
		this.formPagos = new ArrayList<>();

		TotalDocu totalDocu = new TotalDocu();
		totalDocu.setDescri("Total Documento");
		int indice = this.totalDocus.indexOf(totalDocu);

		try {
			this.persCobrs = venta.buscarPersCobrs();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error buscar cobradores"));
			e.printStackTrace();
		}

		try {
//			Realiza filtro de acuerdo a los permisos del rol
			this.docuMoviEgres = venta.filtrarDocuMoviEgres(docuMoviEgreTmps, this.persUsuaSesion, rolDocus);

//			Limpia y asigna las lista nuevamente para realizar mas abajo el segundo filtro de acuerdo al docuEgre seleccionado
			docuMoviEgreTmps.clear();
			docuMoviEgreTmps.addAll(this.docuMoviEgres);
			this.docuMoviEgres.clear();

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al filtar documentos de pago"));
			e.printStackTrace();
		}

		try {
			docuMoviEgrePred = venta.buscarDocuMoviEgrePred(this.docuMoviEgreTmps, rolDocus);
			this.formPagoMoviEgre.setDocuMoviEgre(docuMoviEgrePred);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar documento predeterminado"));
			e.printStackTrace();
		}

//		Filtra de acuerdo al docuEgre seleccionado
//		Se le coloca otro try catch para mostrar el mensaje de error respectivo a la comparación con el bigdecimal
//		this.cxpSele.getSaldo().compareTo(new BigDecimal(0)) ya que cxpSele puede ser null
		try {
			for (DocuMoviEgre docuMoviEgre : this.docuMoviEgreTmps) {

//				Recorrer y filtrar los documentos dependiendo si el documento es factura o N/C
//				Determina si es factura
				if (this.egreso.getDocuEgre().isGeneraCxc()
						&& this.egreso.getDocuEgre().getDocumento().getFactor() == -1) {

					if (docuMoviEgre.getTipo().equals("PAGO-COBRO") || docuMoviEgre.getTipo().equals("DEPOSITO")) {
						this.docuMoviEgres.add(docuMoviEgre);
					}
				}

//				Determina si es N/C
				if (!this.egreso.getDocuEgre().isGeneraCxc()
						&& this.egreso.getDocuEgre().getDocumento().getFactor() == 1) {

//					Se hace este redondeo para poder tener el valor exacto a dos decimales tanto del saldo como del documento
//					porque puede ser cxcSaldo = 54.9989 y totalDocu 54.999952, entonces no fucuncionaria correctamente 
//					BigDecimal cxcSaldo = this.cxcSele.getSaldo().setScale(2, RoundingMode.HALF_UP);
//					BigDecimal totalDocumento = totalDocus.get(indice).getValor().setScale(2, RoundingMode.HALF_UP);
//					if (cxcSaldo.compareTo(totalDocumento) >= 0 && docuMoviEgre.getTipo().equals("PAGO-COBRO")) {
					if (docuMoviEgre.getTipo().equals("PAGO-COBRO")) {
						this.docuMoviEgres.add(docuMoviEgre);
					}

//					if (cxcSaldo.compareTo(totalDocumento) < 0 && docuMoviEgre.getTipo().equals("ANTICIPO")) {
//						this.docuMoviEgres.add(docuMoviEgre);
//					}
				}

//				Si cumple estas condiciones es una Proforma
				if (this.egreso.getDocuEgre().getDocumento().getFactor() == 0) {
					if (docuMoviEgre.getTipo().equals("PAGO-COBRO")) {
						this.docuMoviEgres.add(docuMoviEgre);
					}
				}
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Error no ha seleccionado documento que modifique nota de crédito"));
			e.printStackTrace();
		}

		cajaMovi = this.buscarSesionVentas();

		TranPlan tranPlan = new TranPlan();
		List<TranPlan> tranPlans = this.buscarTranPlans();

		tranPlan = this.recorrerTranPlans(tranPlans);

		this.formPagoMoviEgre.setTranPlanId(tranPlan.getTranPlanId());

		this.formPagoMoviEgre.setSucursal(this.getVariablesSesion().getSucursal());
		this.formPagoMoviEgre.setPersCobr(variablesSesion.getPersCobr());
		this.formPagoMoviEgre.setPersona(this.getEgreso().getPersClie().getPersona());

//		Grabar el numero de inicio de sesion 
//		para saber los cobros que ha hecho un cajero en una jornada		
		this.formPagoMoviEgre.setCajaMoviId(cajaMovi.getCajaMoviId());
		this.formPagoMoviEgre.setFecha(this.getEgreso().getFechaEmis());
		this.formPagoMoviEgre.setTotal(totalDocus.get(indice).getValor());
		this.formPagoMoviEgre.setTotalReci(totalDocus.get(indice).getValor());
		this.formPagoMoviEgre.setEstado("PR");

		this.filtrarFormPago();

	}

	public void filtrarFormPago() {

		TotalDocu totalDocu = new TotalDocu();
		totalDocu.setDescri("Total Documento");
		int indice = this.totalDocus.indexOf(totalDocu);

		List<RolFormPago> rolFormPagos = variablesSesion.getRolFormPagos();

		try {

			this.formPagos.clear();
//			Realiza el primer filtro de acuerdo al los permisos del Rol
			this.formPagoTmps = venta.filtrarFormPagos(this.formPagoTmps, persUsuaSesion,
					variablesSesion.getRolFormPagos());

//			Realilza el segundo filtro de acuerdo al docuEgre y al docuMoviEgre seleccionado 
			for (FormPago formPago : this.formPagoTmps) {

//				Filtra forma de cobro de acuerdo a documento y a seleccion de cxcSele
//				Si cumple estas condiciones es una venta, añadir todas las formas de pago
//				excepto nota de credito (NC)
				if (this.egreso.getDocuEgre().getDocumento().getFactor() == -1) {

					if (formPagoMoviEgre.getDocuMoviEgre().getTipo().equals("PAGO-COBRO")) {

						if (formPago.getModulo().equals("Cajas-Ventas") && !formPago.getTipo().equals("NC")) {
							this.formPagos.add(formPago);
						}

					} else if (formPagoMoviEgre.getDocuMoviEgre().getTipo().equals("ANTICIPO")) {
//						ANTICIPO deberia aparecer solamente cuando es N/C
//						En el caso que entre aqui limpia la lista
						this.formPagos.clear();

					} else if (formPagoMoviEgre.getDocuMoviEgre().getTipo().equals("DEPOSITO")) {
						// Dejar solamente los de tipo Bancos
						if (formPago.getModulo().equals("Compras") && formPago.getTipo2().equals("BA")) {
							this.formPagos.add(formPago);
						}
					}
				}

//				Si cumple estas condiciones es una nota de credito
				if (this.egreso.getDocuEgre().getDocumento().getFactor() == 1) {

//					Determinar la lista de formas de pago dependiendo si selecciono o no una cxpSele
//					Si no selecciono el documento que se despliegue sera anticipo y se deplegaran todas las 
//					formas de pago de tipo FP
//					Se hace este redondeo para poder tener el valor exacto a dos decimales tanto del saldo como del documento
//					porque puede ser cxcSaldo = 54.9989 y totalDocu 54.999952, entonces no fucuncionaria correctamente 
					BigDecimal cxcSaldo = this.cxcSele.getSaldo().setScale(2, RoundingMode.HALF_UP);
					BigDecimal totalDocumento = totalDocus.get(indice).getValor().setScale(2, RoundingMode.HALF_UP);
					if (cxcSaldo.compareTo(totalDocumento) >= 0) {
//						Si selecciono el documento que se despliegue sera Pago-retiro y se deplegara solamente
//						la forma de pago de tipo NC
						if (formPago.getModulo().equals("Cajas-Ventas") && formPago.getTipo().equals("NC")) {
							this.formPagos.add(formPago);
						}
					} else {
						if (formPago.getModulo().equals("Cajas-Ventas") && formPago.getTipo().equals("FP")) {
							this.formPagos.add(formPago);
						}
					}
				}

//				Si cumple estas condiciones es una Proforma
				if (this.egreso.getDocuEgre().getDocumento().getFactor() == 0) {
					if (formPago.getModulo().equals("Cajas-Ventas") && formPago.getTipo().equals("CR")) {
						this.formPagos.add(formPago);
					}
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error buscar forma de pago"));
			e.printStackTrace();
		}

//		Seleccionar la forma de pago predetermida en rolFormPago
		for (FormPago formPago : formPagos) {
			for (RolFormPago rolFormPago : rolFormPagos) {
				if (formPago.getFormPagoId() == rolFormPago.getFormPago().getFormPagoId()
						&& rolFormPago.getPredet() == true) {
					this.formPagoPred = formPago;
				}
			}
		}

//		Coloca la forma de pago predeterminada en formPagoMoviEgre
//		Los demas atributos de formPagoMoviEgre se cargan en el metodo cargarFormPagoMoviEgre 
//		this.formPagoMoviEgre.setFormPago(formPagoPred);

//		Implementacion FpmeFormPago
//		Limpia la lista en el caso que haya puesto cancelar y se haya vuelto a abrir
//		y crea la primera linea en fpmeFormPago

		this.fpmeFormPagos.clear();
		this.crearFilaFpmeFormPago();
//		Fin Implementacion FpmeFormPago
	}

	public void insertarCobrDeta(FpmeFormPago fpmeFormPago, Cxc cxc) {

		CobrDeta cobrDeta = new CobrDeta();

		cobrDeta.setCxc(cxc);
		cobrDeta.setFpmeFormPago(fpmeFormPago);
		// Se registra como referencia para poder eliminar el abono en caso de NC
		cobrDeta.setEgresoId(this.egreso.getEgresoId());
//		El total del cobro en el caso que la forma de pago sea de contado
//		es el total que se ha recibido menos el cambio
		if (fpmeFormPago.getFormPago().getTipo2().equals("VN-CO")) {
//			Las dos lineas son equivalentes
//			cobrDeta.setTotal(fpmeFormPago.getTotalReci().subtract(formPagoMoviEgre.getTotalReci().subtract(formPagoMoviEgre.getTotal())));
			cobrDeta.setTotal(fpmeFormPago.getTotalReci().add(this.cambio));
		} else {
			cobrDeta.setTotal(fpmeFormPago.getTotalReci());
		}

		try {
			venta.insertarCobrDeta(cobrDeta);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error crear detalle de Pago"));
			e.printStackTrace();
		}
	}

	public String validarAnular() {

		String mensaje = "validado";

		Egreso egreso = new Egreso();

		List<Egreso> egresos = new ArrayList<>();
		Set<Integer> sucursals = new HashSet<>();

		PersClie persClie = new PersClie();
		persClie.setPersona(new Persona());

		DocuEgre docuEgre = new DocuEgre();
		docuEgre.setDocumento(new Documento());

		CajaMovi cajaMovi = new CajaMovi();
		cajaMovi.setCaja(new Caja());

		egreso.setSucursal(new Sucursal());
		egreso.setPersClie(persClie);
		egreso.setDocuEgre(docuEgre);
		egreso.setCajaMovi(cajaMovi);

		egreso.setEgreso(new Egreso());
		egreso.getEgreso().setEgresoId(this.egreso.getEgresoId());

		try {

			egresos = this.venta.buscarEgresos(egreso, sucursals);

			if (egresos.size() == 0) {
				mensaje = "validado";
			}

//			Recorre la lista para verificar que el documento que encontro este anulado
//			caso contrario no debe dejar anular o desprocesar el documento
			for (Egreso egreso2 : egresos) {
				if (egreso2.getEstado().equals("AN")) {
					mensaje = "validado";
				} else {
					mensaje = "Error no se puede anular o desprocesar - Existe Nota de Crédito para el documento";
					return mensaje;
				}
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar egreso - egresoSupe "));
			e.printStackTrace();
		}

		CobrDeta cobrDeta = new CobrDeta();
		cobrDeta.setEgresoId(this.egreso.getEgresoId());

		List<Object[]> objCobrDetas = new ArrayList<Object[]>();
		try {
			objCobrDetas = venta.buscarCobrosPorEgresoId(cobrDeta);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Error al buscar cobros del documento - EgresoId"));
			e.printStackTrace();
		}

		if (!objCobrDetas.isEmpty()) {
			try {
				objCobrDetas = venta.buscarCobrosPorFpmeFpId(objCobrDetas, this.egreso);

				if (objCobrDetas.isEmpty()) {
					mensaje = "validado";
				} else {
					mensaje = "Error no se puede anular o desprocesar - Documento tiene cobros combinados con otros documentos";
				}
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Error al buscar cobros del documento - FmeFpId"));
				e.printStackTrace();
			}
		}

		return mensaje;
	}

	public String anular() {

//		Se coloca la linea de abajo en el caso que el return sea return "ventaDeta?faces-redirect=true"; 
//		para que el numero de factura se ponga 0 sino envia a descargar el docuemeto
//		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idDescargar", 0);
		this.conversationEnd();

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		String mensajeValidacion = this.validarAnular();

		if (!mensajeValidacion.equals("validado")) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, mensajeValidacion));
			return "/ppsj/egreso/egreso/lista.xhtml?faces-redirect=true";
		}

		try {
//			Elimina solamente si es factura (factor = 1) o N/C (factor = -1)
//			Ya que si es proforma (factor == 0) da error al elimnar
			if (this.egreso.getDocuEgre().getDocumento().getFactor() != 0) {

				FormPagoMoviEgre fpme = new FormPagoMoviEgre();
				List<FormPagoMoviEgre> fpmes = new ArrayList<>();

				fpme.setPersona(new Persona());
				fpme.setEgresoId(this.egreso.getEgresoId());

				fpmes = this.buscarFpmes(fpme);

//				Si es diferente de cero, quiere decir que el documento esta procesado
//				caso contrario el documento solo esta grabado
				if (fpmes.size() != 0) {

					for (FormPagoMoviEgre formPagoMoviEgre : fpmes) {

						CobrDeta cobrDetaBuscar = new CobrDeta();
						Cxc cxc = new Cxc();
						cxc.setEgreso(this.egreso);

						List<CobrDeta> cobrDetas = new ArrayList<>();

						cobrDetaBuscar.setCxc(cxc);

						try {
							cobrDetas = this.venta.buscarCobrDetas(cobrDetaBuscar);
						} catch (Exception e) {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
											"Excepcion - Error al buscar detalle de cobros"));
							e.printStackTrace();
						}

						this.eliminarCxc();

						Set<FormPagoMoviEgre> fpmeCobros = new HashSet<>();

//						Anula el registro del credito
//						this.anularFpme(formPagoMoviEgre);

//						Coloca en la lista el registro del credito
//						para anular junto a los registros de cobros
						fpmeCobros.add(formPagoMoviEgre);

						for (CobrDeta cobrDeta : cobrDetas) {
							fpmeCobros.add(venta
									.buscarFpmesPorId(cobrDeta.getFpmeFormPago().getFormPagoMoviEgre().getFpmeId()));
						}

						for (FormPagoMoviEgre fpmeAnular : fpmeCobros) {
							this.anularFpme(fpmeAnular);
						}
//						Fin Anula los registros de los cobros

//						Anula registro en el caso que el documento se haya cobrado con deposito
						this.anularFpmi(formPagoMoviEgre);
					}
				}
			}

//			Verifica si esta procesado ya que si esta solo grabado todavia no se genera el kardex
//			y si por alguna razón se agraga un producto y se anula se borra todos los datos del kardex
			if (this.egreso.getEstado().equals("PR")) {
				this.eliminarKardex();
			}

			// Eliminar EgreDeta y por cascada EgreDetaImpu
			this.venta.anularEgreso(egreso);
			this.egreDetaDataTable.clear();
			this.egreDetaEliminados.clear();

			this.grabarEgreso("AN");

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar anular egreso"));
			e.printStackTrace();
		}

//		Buscar Transaccion
		Transaccion transaccionBuscar = new Transaccion();
		List<EgreTran> egreTrans = new ArrayList<>();

		EgreTran egreTranBuscar = new EgreTran(this.egreso, transaccionBuscar);
		egreTrans = this.buscarEgreTrans(egreTranBuscar);

		// Se anulan las transacciones del documento
		if (!egreTrans.isEmpty()) {

			for (EgreTran egreTran : egreTrans) {

				try {
					this.transaccionGestion.anularTransaccion(egreTran.getTransaccion().getTransaccionId());

					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							null, "Transacción anulada No. " + egreTran.getTransaccion().getNumero()));

				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
							null, "Excepcion - Error al anular transacciones del documento"));
					e.printStackTrace();
				}
			}
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Documento anulado No. " + this.egreso.getNumero()));

		return "/ppsj/egreso/egreso/lista.xhtml?faces-redirect=true";
//		return "ventaDeta?faces-redirect=true";
	}

//  El boton que activa este metodo se llama DesProcesar
//	Eliminar todos los movimiento del documento a excepción de los detalle de factura
//	Vuelve el estado de la factura a GR
//	Se utiliza para volver a procesar un documento
//	Cambia el estado del documento electrónico a NO ENVIADO para poder procesarlo nuevamente
//	ya sea individualmente o por lotes
	public String anular2() {

		this.conversationEnd();

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		String mensajeValidacion = this.validarAnular();

		if (!mensajeValidacion.equals("validado")) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, mensajeValidacion));
			return "/ppsj/egreso/egreso/lista.xhtml?faces-redirect=true";
		}

		try {
//			Elimina solamente si es factura (factor = 1) o N/C (factor = -1)
//			Ya que si es proforma (factor == 0) da error al elimnar
			if (this.egreso.getDocuEgre().getDocumento().getFactor() != 0) {

				FormPagoMoviEgre fpme = new FormPagoMoviEgre();
				List<FormPagoMoviEgre> fpmes = new ArrayList<>();

				fpme.setPersona(new Persona());
				fpme.setEgresoId(this.egreso.getEgresoId());

				fpmes = this.buscarFpmes(fpme);

//				Si es diferente de cero, quiere decir que el documento esta procesado
//				caso contrario el documento solo esta grabado
				if (fpmes.size() != 0) {

					for (FormPagoMoviEgre formPagoMoviEgre : fpmes) {

						CobrDeta cobrDetaBuscar = new CobrDeta();
						Cxc cxc = new Cxc();
						cxc.setEgreso(this.egreso);

						List<CobrDeta> cobrDetas = new ArrayList<>();

						cobrDetaBuscar.setCxc(cxc);

						try {
							cobrDetas = this.venta.buscarCobrDetas(cobrDetaBuscar);
						} catch (Exception e) {
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
											"Excepcion - Error al buscar detalle de cobros"));
							e.printStackTrace();
						}

						this.eliminarCxc();

						Set<FormPagoMoviEgre> fpmeCobros = new HashSet<>();

//						Anula el registro del credito
//						this.anularFpme(formPagoMoviEgre);

//						Coloca en la lista el registro del credito
//						para anular junto a los registros de cobros
						fpmeCobros.add(formPagoMoviEgre);

						for (CobrDeta cobrDeta : cobrDetas) {
							fpmeCobros.add(venta
									.buscarFpmesPorId(cobrDeta.getFpmeFormPago().getFormPagoMoviEgre().getFpmeId()));
						}

						for (FormPagoMoviEgre fpmeAnular : fpmeCobros) {
							this.anularFpme(fpmeAnular);
						}
//						Fin Anula los registros de los cobros

//						Anula registro en el caso que el documento se haya cobrado con deposito
						this.anularFpmi(formPagoMoviEgre);
					}
				}
			}

//			Verifica si esta procesado ya que si esta solo grabado todavia no se genera el kardex
//			y si por alguna razón se agraga un producto y se anula se borra todos los datos del kardex
			if (this.egreso.getEstado().equals("PR")) {
				this.eliminarKardex();
			}

			this.egreDetaDataTable.clear();
			this.egreDetaEliminados.clear();

			this.egreso.setEstadoDocuElec("NO ENVIADO");
			this.grabarEgreso("GR");

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al desprocesar egreso"));
			e.printStackTrace();
		}

//		Buscar Transaccion
		Transaccion transaccionBuscar = new Transaccion();
		List<EgreTran> egreTrans = new ArrayList<>();

		EgreTran egreTranBuscar = new EgreTran(this.egreso, transaccionBuscar);
		egreTrans = this.buscarEgreTrans(egreTranBuscar);

		// Se anulan las transacciones del documento
		if (!egreTrans.isEmpty()) {

			for (EgreTran egreTran : egreTrans) {

				try {
					this.transaccionGestion.anularTransaccion(egreTran.getTransaccion().getTransaccionId());

					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							null, "Transacción anulada No. " + egreTran.getTransaccion().getNumero()));

				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
							null, "Excepcion - Error al anular transacciones del documento"));
					e.printStackTrace();
				}
			}
		}

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
				"Documento Desprocesado No. " + this.egreso.getNumero()));

		return "/ppsj/egreso/egreso/lista.xhtml?faces-redirect=true";

	}

//	Buscar documento origen para Notas de credito
	public List<Egreso> buscarEgresos(Egreso egreso) {

		Set<Integer> sucursals = new HashSet<>();

		List<Egreso> egresos = new ArrayList<>();

		try {
			egresos = venta.buscarEgresos(egreso, sucursals);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar egresos"));
			e.printStackTrace();
		}

		return egresos;
	}

	// <<<<<<<<<<<<<<<<<<<< DETALLE DOCUMENTO >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< DETALLE DOCUMENTO >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< DETALLE DOCUMENTO >>>>>>>>>>>>>>>>>>>>

// 	Crea y asigna subproductos 
//	Los productos se asignan a egreDetaDataTable 
//	los subproductos a egreDetas que es un set<> de egreDeta
//	con esto se mantiene separado los productos y los subproductos
//	para efectos de visualizacion y calculos
	public String agregarProducto() {

		BigDecimal costo;
		BigDecimal cantidSald;

		// Acumula la cantidad de venta de los productos que se van pasando por el
		// scanner.
		// El sistema no crea otra linea si se pasa dos veces el mismo producto
		BigDecimal cantidadVenta = new BigDecimal(1);
		BigDecimal cantidadInicial = new BigDecimal(1);

//		Producto producto = new Producto();
		EgreDeta egreDeta = new EgreDeta();
//		Guarda el egreDeta en el caso que se este añadiendo otra vez a la lista
//		para acumular la cantidadVenta y no crear una nueva linea
		EgreDeta egreDetaExiste = new EgreDeta();
		egreDetaExiste.setCantid(new BigDecimal(0));

//		ProdPrec prodPrec = new ProdPrec();

		List<KardTotaView> kardTotaViews = new ArrayList<>();

		Set<EgreDetaImpu> egreDetaImpus = new HashSet<EgreDetaImpu>();

		if (this.egreDetaDataTable.size() >= this.egreso.getDocuEgre().getMaximoRegi()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Ha llegado al máximo de registros para este documento"));
			return null;
		}

//		Si el documento se esta modificando y estado del documento no es igual a grabado osea esta PR o AN
//		no puede agregar mas productos ya que si agrega y anula el egreso se borra toda la tabla Kardex
		if (this.egresoId != null && !this.egreso.getEstado().equals("GR")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"No se puede agregar productos en documentos procesados o anulados"));
			return null;
		}

//		Buscar costo actual - promedio , cantidad actual
		KardTotaView kardTotaView = new KardTotaView();

		kardTotaView.setSucursalId(variablesSesion.getSucursal().getSucursalId());
		kardTotaView.setBodegaId(1);
		kardTotaView.setProductoId(prodPrecSele.getProducto().getProductoId());

		kardTotaViews = this.buscarKardTotaViews(kardTotaView);

//		Valida en el caso que no exista ningun movimiento de ingreso
//		es decir que directamente se va registrar egresos, 
//		por lo tanto el producto no tiene cantidad ni costo
		if (kardTotaViews.size() != 0) {
//			costo = kardTotaViews.get(0).getCosto().divide(this.prodPrecSele.getFactor());
			costo = kardTotaViews.get(0).getCosto();
			cantidSald = kardTotaViews.get(0).getCantidSald();
		} else {
			costo = new BigDecimal(0);
			cantidSald = new BigDecimal(0);
		}

//		Recorre la lista de productos agregados para saber si el que se esta pasando ya esta en la lista
//		y sumar en uno la cantida de la venta = cantidadVenta, para luego controlar el inventario
		for (EgreDeta egreDetaBuscar : this.egreDetaDataTable) {

			if (prodPrecSele.getProducto().getProductoId().equals(egreDetaBuscar.getProducto().getProductoId())) {

				cantidadVenta = cantidadVenta.add(egreDetaBuscar.getCantid().multiply(egreDetaBuscar.getFactor()));
				egreDetaExiste = egreDetaBuscar;

				if (this.variablesSesion.isAcumularCantidad()) {
					break;
				}
			}
		}

		if ((cantidadVenta.subtract(cantidSald)).compareTo(BigDecimal.ZERO) > 0
				&& (cantidadVenta.subtract(cantidSald)).compareTo(new BigDecimal(1)) < 0) {

			cantidadVenta = cantidadVenta.subtract(new BigDecimal(0.99).setScale(2, RoundingMode.HALF_UP));
			cantidadInicial = new BigDecimal(0.01);

			PrimeFaces.current().ajax().update("ventaForm:saldoMenorQueUno");
			PrimeFaces.current().executeScript("PF('saldoMenorQueUnoDialogo').show();");
		}

//		Controla inventario solamente si el documento es factura (factor < 0)
		if (this.factor < 0) {

			if (cantidSald.compareTo(cantidadVenta.multiply(this.prodPrecSele.getFactor())) == -1) {

				if (prodPrecSele.getProducto().getControStoc().equals("Advertencia")) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							null, "No tiene suficiente Stock de este producto"));
				} else if (prodPrecSele.getProducto().getControStoc().equals("Controla")) {

					PrimeFaces.current().ajax().update("ventaForm:controlStock");
					PrimeFaces.current().executeScript("PF('controlStockDialogo').show();");

					return "null";
				}
			}
		}

		egreDeta.setProducto(prodPrecSele.getProducto());
//		egreDeta es el objeto que se va a agregar a la lista.
//		cantidadVenta siempre esta en uno, es decir si se agrega un producto que no este en la lista
//		siempre tendra un valor de uno. Si el producto ya esta en la lista simplemente se acumula la cantidad
//		y el objeto egreDeta ya no se añade a la lista, se utiliza solamente para la validacion del inventario
//		egreDeta.setCantid(cantidadInicial);
		egreDeta.setFactor(this.prodPrecSele.getFactor());
		egreDeta.setPrecio(this.prodPrecSele.getPrecio());
		egreDeta.setPrecioVent(this.prodPrecSele.getPrecioSinImpu());
		egreDeta.setDescue(new BigDecimal(0));
//		Se redondea a 6 decimales porque kardTotaViews.get(0).getCosto(); 
//		en la linea mas arriba puede devolver mas de 6 decimales y por lo tanto se desbordan los decimales
		egreDeta.setCosto(costo.setScale(6, RoundingMode.HALF_UP));

//		Controla si crea una nueva fila por cada producto o acumula la cantidad vendida
//		egreDetaExiste es una instancia de egreDeta
//		Si isAcumularCantidad es true esta activado el parametro 3127 entonces 
//		Pregunta si la cantidad de venta igual a 1, si es asi quiere decir que es un
//		producto nuevo y crea una nueva fila caso contrario acumula la cantidad vendida
//		Si isAcumularCantidad es false esta desactivado el parametro 3127 entonces
//		crea una fila nueva por cada producto que se venda
		if (this.variablesSesion.isAcumularCantidad()) {

//			if (cantidadVenta.equals(new BigDecimal(1))) {
			if (egreDetaExiste.getCantid().compareTo(BigDecimal.ZERO) == 0) {
				egreDeta.setCantid(cantidadInicial);
				this.egreDetaDataTable.add(0, egreDeta);
			} else {
//				Aqui no se porque pero se coloca estas dos filas
				egreDetaExiste.setCantid(cantidadVenta);
				egreDeta.setCantid(cantidadVenta);
			}

		} else {
			egreDeta.setCantid(cantidadInicial);
			this.egreDetaDataTable.add(0, egreDeta);
		}

//		Coloca el saldo actual del producto para poder controlar el máximo y el minimo
		egreDeta.getProducto().setStockActual(cantidSald);
//		Envia ingrDeta para colocar los datos de máximo y mínimo
		this.agregarControlMaxMin(egreDeta);
//		Se pinta las advertencias de máximo y mínimo
		this.controlarMaxMin(egreDeta);

		egreDetaImpus = this.crearEgreDetaImpu(egreDeta);
		egreDeta.setEgreDetaImpus(egreDetaImpus);

		this.calcularTotalEgres();

//		Crea egreDeta un registro recursivo por cada subproducto
		crearEgreDetaSubProducto(egreDeta);

		return "agregado";
	}

//	Se buscar y verifica costo actual - promedio , cantidad actual de los subproductos
//	y se graba el costo en egre_deta para poder registrar el costo de venta
	public void crearEgreDetaSubProducto(EgreDeta egreDeta) {

		Set<EgreDeta> egreDetas = new HashSet<>();

		ProdSubp ProdSubpBuscar = new ProdSubp(egreDeta.getProducto());
		List<ProdSubp> prodSubps = buscarProdSubps(ProdSubpBuscar);

		for (ProdSubp prodSubp : prodSubps) {

			EgreDeta egreDetaSubProducto = new EgreDeta();

			egreDetaSubProducto.setEgreDeta(egreDeta);
			egreDetaSubProducto.setEgreso(egreDeta.getEgreso());
			egreDetaSubProducto.setProducto(prodSubp.getSubProducto());
//			Asigna el mismo precio_id que tiene el producto principal
			egreDetaSubProducto.setPrecio(egreDeta.getPrecio());
//			egreDetaSubProducto.setFechaRegi(egreDeta.getFechaRegi());
//			Coloca la cantidad configurada en prodSubp multiplicado por lo que se esta vendiendo
			egreDetaSubProducto.setCantid(prodSubp.getCantid().multiply(egreDeta.getCantid()));
			egreDetaSubProducto.setFactor(egreDeta.getFactor());
			// Asigna el mismo precio de venta que tiene el producto principal
			egreDetaSubProducto.setPrecioVent(egreDeta.getPrecioVent());
			egreDetaSubProducto.setDescue(new BigDecimal(0));

//			Buscar y asignar costo actual - promedio , cantidad actual
			KardTotaView kardTotaView = new KardTotaView();
			kardTotaView.setSucursalId(variablesSesion.getSucursal().getSucursalId());
			kardTotaView.setBodegaId(1);
			kardTotaView.setProductoId(prodSubp.getSubProducto().getProductoId());

			List<KardTotaView> kardTotaViews = this.buscarKardTotaViews(kardTotaView);

//			Valida en el caso que no exista ningun movimiento de ingreso
//			es decir que directamente se va registrar egresos, 
//			por lo tanto el producto no tiene cantidad ni costo
			if (kardTotaViews.size() != 0) {
				egreDetaSubProducto.setCosto(kardTotaViews.get(0).getCosto());
			} else {
				egreDetaSubProducto.setCosto(new BigDecimal(0));
			}

			egreDetas.add(egreDetaSubProducto);
		}

		egreDeta.setEgreDetas(egreDetas);
	}

	public void modificarEgreDetaSubProducto(EgreDeta egreDeta) {

		ProdSubp ProdSubpBuscar = new ProdSubp(egreDeta.getProducto());
		List<ProdSubp> prodSubps = buscarProdSubps(ProdSubpBuscar);

		for (EgreDeta egreDetaModificar : egreDeta.getEgreDetas()) {

//			Asigna el mismo precio_id que tiene el producto principal
			egreDetaModificar.setPrecio(egreDeta.getPrecio());
//			egreDetaModificar.setFechaRegi(egreDeta.getFechaRegi());

//			Coloca la cantidad configurada en prodSubp multiplicado por lo que se esta vendiendo
//			Se debe buscar nuevamente la cantidad que esta almacenada en prodSubp para poder
//			multiplicar por la cantidad modificada
			for (ProdSubp prodSubp : prodSubps) {
				if (egreDetaModificar.getProducto().getProductoId().equals(prodSubp.getSubProducto().getProductoId())) {
					egreDetaModificar.setCantid(prodSubp.getCantid().multiply(egreDeta.getCantid()));
				}
			}

			egreDetaModificar.setFactor(egreDeta.getFactor());
			// Asigna el mismo precio de venta que tiene el producto principal
			egreDetaModificar.setPrecioVent(egreDeta.getPrecioVent());

//			No se necesita porque se esta modificando y estos datos ya se puso al crear
////			Buscar y asignar costo actual - promedio , cantidad actual
//			KardTotaView kardTotaView = new KardTotaView();
//			kardTotaView.setSucursalId(variablesSesion.getSucursal().getSucursalId());
//			kardTotaView.setBodegaId(1);
//			kardTotaView.setProductoId(egreDetaModificar.getProducto().getProductoId());
//
//			List<KardTotaView> kardTotaViews = this.buscarKardTotaViews(kardTotaView);
//
////			Valida en el caso que no exista ningun movimiento de ingreso
////			es decir que directamente se va registrar egresos, 
////			por lo tanto el producto no tiene cantidad ni costo
//			if (kardTotaViews.size() != 0) {
//				egreDetaModificar.setCosto(kardTotaViews.get(0).getCosto());
//			} else {
//				egreDetaModificar.setCosto(new BigDecimal(0));
//			}
		}
	}

	public void agregarControlMaxMin(EgreDeta egreDeta) {

		Bodega bodega = new Bodega();
		bodega.setBodegaId(1);

		ProdBode prodBodeBuscar = new ProdBode();
		prodBodeBuscar.setSucursal(this.variablesSesion.getSucursal());
		prodBodeBuscar.setBodega(bodega);
		prodBodeBuscar.setProducto(prodPrecSele.getProducto());

		List<ProdBode> prodBodes = this.buscarProdBode(prodBodeBuscar);

//		KardTotaView kardTotaViewBuscar = new KardTotaView();
//		kardTotaViewBuscar.setSucursalId(this.variablesSesion.getSucursal().getSucursalId());
//		kardTotaViewBuscar.setBodegaId(1);
//		kardTotaViewBuscar.setProductoId(prodPrecSele.getProducto().getProductoId());
//
//		List<KardTotaView> kardTotaViews = this.buscarKardTotaViews(kardTotaViewBuscar);

		if (prodBodes.size() != 0) {
			ProdBode prodBode = prodBodes.get(0);
			egreDeta.getProducto().setMaximo(prodBode.getMaximo());
			egreDeta.getProducto().setMinimo(prodBode.getMinimo());
		} else {
			egreDeta.getProducto().setMaximo(new BigDecimal(0));
			egreDeta.getProducto().setMinimo(new BigDecimal(0));
		}

//		if (kardTotaViews.size() != 0) {
//			KardTotaView kardTotaView = kardTotaViews.get(0);
//			egreDeta.getProducto().setStockActual(kardTotaView.getCantidSald());
//		} else {
//			egreDeta.getProducto().setStockActual(new BigDecimal(0));
//		}	
	}

	public void controlarMaxMin(EgreDeta egreDeta) {

		BigDecimal cantidadTotal = new BigDecimal(0);

//		Suma o resta del saldo actual dependiendo se el documento es compra, ingreso o N/C, egreso
		if (this.egreso.getDocuEgre().getDocumento().getFactor() >= 0) {
			cantidadTotal = egreDeta.getProducto().getStockActual().add(egreDeta.getCantid());
		} else {
			cantidadTotal = egreDeta.getProducto().getStockActual().subtract(egreDeta.getCantid());
		}

//		int maximo = kardTotaView.getCantidSald().compareTo(prodBode.getMaximo());
		if (egreDeta.getProducto().getMaximo().compareTo(new BigDecimal(0)) != 0
				&& cantidadTotal.compareTo(egreDeta.getProducto().getMaximo()) >= 0) {
			egreDeta.getProducto().setControlMaxMin("maximo");
		} else {
			egreDeta.getProducto().setControlMaxMin(null);
		}

//		int minimo = kardTotaView.getCantidSald().compareTo(prodBode.getMinimo()); 
		if (egreDeta.getProducto().getControlMaxMin() == null) {
			if (egreDeta.getProducto().getMinimo().compareTo(new BigDecimal(0)) != 0
					&& cantidadTotal.compareTo(egreDeta.getProducto().getMinimo()) <= 0) {
				egreDeta.getProducto().setControlMaxMin("minimo");
			} else {
				egreDeta.getProducto().setControlMaxMin("null");
			}
		}
	}

	public List<ProdBode> buscarProdBode(ProdBode prodBode) {

		List<ProdBode> prodBodes = new ArrayList<>();

		try {
			prodBodes = venta.buscarProdBodes(prodBode);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Ex - Error al buscar Producto-Bodega"));
			e.printStackTrace();
		}
		return prodBodes;
	}

	public Parametro buscarParametroPorId(Parametro parametroBuscar) {

		Parametro parametro = new Parametro();

		try {
			parametro = this.parametroRegis.buscarPorId(Parametro.class, parametroBuscar.getParametroId());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Ex - Error al buscar Parametro de descuento"));
			e.printStackTrace();
		}

		return parametro;
	}

	public void modificarCelda(CellEditEvent<?> event) {

		BigDecimal cantidadVenta = new BigDecimal(0);

		Producto producto = new Producto();
		EgreDeta egreDeta = new EgreDeta();

		List<KardTotaView> kardTotaViews = new ArrayList<>();

		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		egreDeta = (EgreDeta) ((DataTable) event.getComponent()).getRowData();

		if (newValue != null && !newValue.equals(oldValue)) {

			producto = egreDeta.getProducto();

			Parametro parametroBuscar = new Parametro();
			parametroBuscar.setParametroId(3151);
			Parametro parametro = this.buscarParametroPorId(parametroBuscar);

			BigDecimal descDetaMaximo = new BigDecimal(parametro.getDescri());

			if (egreDeta.getDescue().compareTo(descDetaMaximo) == 1) {
				egreDeta.setDescue((BigDecimal) oldValue);

				PrimeFaces.current().executeScript("PF('descuentoNoAutorizadoDialogo').show();");

			}

			// Controla inventario solamente si el documento es factura (factor < 0)
			if (this.factor < 0) {

//				Si isAcumularCantidad es false esta desactivado el parametro 3127 entonces
//				quiere decir que se crea una linea cada vez que se agrege un producto
//				Por lo tanto se recorre this.egreDetaDataTable para sumar la cantidad vendida
//				y poder controlar el inventario del producto que se esta modificando
//				caso contrario como quiere decir que la cantidad de venta se esta acumulando
//				solo se controla con cantidadVenta = egreDeta.getCantid();
				if (!this.variablesSesion.isAcumularCantidad()) {
//					Recorre la lista de productos agregados para saber si el que se esta pasando ya esta en la lista
//					y sumar en uno la cantida de la venta = cantidadVenta, para luego controlar el inventario
					for (EgreDeta egreDetaBuscar : this.egreDetaDataTable) {

						if (egreDeta.getProducto().getProductoId()
								.equals(egreDetaBuscar.getProducto().getProductoId())) {
							cantidadVenta = cantidadVenta
									.add(egreDetaBuscar.getCantid().multiply(egreDetaBuscar.getFactor()));
						}
					}
				} else {
					cantidadVenta = egreDeta.getCantid().multiply(egreDeta.getFactor());
				}

				KardTotaView kardTotaView = new KardTotaView();

				kardTotaView.setSucursalId(variablesSesion.getSucursal().getSucursalId());
				kardTotaView.setBodegaId(1);
				kardTotaView.setProductoId(producto.getProductoId());

				kardTotaViews = this.buscarKardTotaViews(kardTotaView);

				if (kardTotaViews.size() != 0) {

					if (kardTotaViews.get(0).getCantidSald().compareTo((BigDecimal) (cantidadVenta)) == -1) {

						if (producto.getControStoc().equals("Advertencia")) {

							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
									FacesMessage.SEVERITY_WARN, null, "No tiene suficiente Stock de este producto"));

						} else if (producto.getControStoc().equals("Controla")) {

							egreDeta.setCantid((BigDecimal) oldValue);

							PrimeFaces.current().ajax().update("ventaForm:controlStock");
							PrimeFaces.current().executeScript("PF('controlStockDialogo').show();");
						}
					}
				} else {

					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							null, "Información - Producto no tiene movimientos - kardTotaView"));
				}
			}

//			Se crea nuevamente el registro recursivo  egreDetas porque se esta modificando la fila
//			y puede ser que se este cambiando la cantidad vendida entones se crea
//			una nueva lista de egreDetas con las nuevas cantidades
			modificarEgreDetaSubProducto(egreDeta);

			this.calcularTotalEgres();
		}

		this.controlarMaxMin(egreDeta);

	}

	public String modificarEgreDetas() {

		return "/ppsj/egreso/venta/ventaDeta?faces-redirect=true&egresoId=" + this.getEgresoId();

	}

	// Carga una nueva lista de productos que se tienen que eliminar
	// Se elimina al grabarEgreso
	public void eliminarEgreDeta() {

		this.egreDetaDataTable.remove(egreDetaSele);

		try {

			if (egreDetaSele.getEgreDetaId() != null) {

				egreDetaEliminados.add(egreDetaSele);

			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al eliminar detalle documento"));
			e.printStackTrace();

		}

		this.calcularTotalEgres();
	}

	public List<KardTotaView> buscarKardTotaViews(KardTotaView kardTotaView) {

		List<KardTotaView> kardTotaViews = new ArrayList<>();

		try {
			kardTotaViews = venta.buscarKardTotaViews(kardTotaView);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar total kardex"));
			e.printStackTrace();
		}

		return kardTotaViews;
	}

//	Este Procedimiento no retorna nada ya que se la llama desde la pagina
//	y necesita actualizar y filtrar la lista prodPrecs
	public List<KardTotaView> buscarKardTotaViews() {

		List<Integer> sucursals = new ArrayList<>(this.sucursals);

		KardTotaView kardTotaView = new KardTotaView();

		kardTotaView.setBodegaId(1);
		kardTotaView.setProductoId(this.productoId);

		try {
			this.kardTotaViews = venta.buscarKardTotaViews(sucursals, kardTotaView);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion KardTotaView - Error al buscar saldos de producto"));
			e.printStackTrace();
		}

		return this.kardTotaViews;
	}

//	public List<Entry<String, BigDecimal>> getTotales() {
//		return new ArrayList(totalEgre.entrySet());
//	}

	// <<<<<<<<<<<<<<<<<<<< CABECERA DOCUMENTO >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< CABECERA DOCUMENTO >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< CABECERA DOCUMENTO >>>>>>>>>>>>>>>>>>>>

	public String modificarEgreso() {

		return "ventaCabe?faces-redirect=true&egresoId=" + this.getEgresoId();
	}

	public void calcularDiasPlaz() {

		for (FpmeFormPago fpmeFormPago : this.fpmeFormPagos) {

			fpmeFormPago.setDiasPlaz(
					(short) ChronoUnit.DAYS.between(this.formPagoMoviEgre.getFecha(), fpmeFormPago.getFecha()));
		}

//		Se cambia el formato de las fechas para que calcule bien los dias caso contrario 
//		toma en cuenta horas minutos y segundos
//		Date fechaPago = Util.cambiarFormatoFecha(this.fechaCobro, "dd-MM-yyyy");
//		Date fechaEmis = Util.cambiarFormatoFecha(this.egreso.getFechaEmis(), "dd-MM-yyyy");
//
//		this.egreso.setDiasPlaz((short) ((fechaPago.getTime() - fechaEmis.getTime()) / 86400000));
	}

	public void calcularFechaCobro() {

		for (FpmeFormPago fpmeFormPago : this.fpmeFormPagos) {

			fpmeFormPago.setFecha(this.egreso.getFechaEmis().plusDays(fpmeFormPago.getDiasPlaz()));
		}
	}

//	tambien bloquea los campos para que no se modifique la informacion si la forma de pago
//	es diferente de credito, esto se usa para sacar el reporte de las facturas por forma de pago
	public void alCambiarFormCobr() {

		for (FpmeFormPago fpmeFormPago : this.fpmeFormPagos) {

			if (fpmeFormPago.getFormPago().getTipo().equals("CR")) {
				fpmeFormPago.setDiasPlaz((short) 30);
				this.calcularFechaCobro();
			} else {
				fpmeFormPago.setDiasPlaz((short) 0);
				fpmeFormPago.setFecha(this.egreso.getFechaEmis());
			}
		}
	}

//	Validador Simple
//	Valida de acuerdo al descuento que tenga asignado el cliente y el parametro de descuento maximo
//	Si el cliente tiene grabado un descuento ese sera el descuento maximo, si el cliente no tiene
//	grabado descuento, se controlara con en el descuento que esta en parametro
	public void validarDescuento(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException {

		BigDecimal descueCabePorc = new BigDecimal(0);

		descueCabePorc = (BigDecimal) arg2;

		Parametro parametroBuscar = new Parametro();
		parametroBuscar.setParametroId(3150);
		Parametro parametro = this.buscarParametroPorId(parametroBuscar);

		BigDecimal descCabeMaximo = new BigDecimal(parametro.getDescri());

		if (this.egreso.getPersClie().getDescueMaxi().compareTo(BigDecimal.ZERO) == 0) {
			if (descueCabePorc.compareTo(descCabeMaximo) == 1) {
				throw new ValidatorException(new FacesMessage("Descuento es mayor que el autorizado en parametros"));
			}
		} else {
			if (descueCabePorc.compareTo(this.egreso.getPersClie().getDescueMaxi()) == 1) {
				throw new ValidatorException(new FacesMessage("Descuento es mayor que el autorizado al cliente"));
			}
		}
	}

	public void calcularTotalEgres() {

		BigDecimal totalBrutoCabe = new BigDecimal(0);
		BigDecimal totalBrutoDeta = new BigDecimal(0);
		BigDecimal descueCabe = new BigDecimal(0);
		BigDecimal descueDeta = new BigDecimal(0);
		BigDecimal descueTotal = new BigDecimal(0);
		BigDecimal totalNetoDeta = new BigDecimal(0);
//		BigDecimal totalNetoDetaTmp = new BigDecimal(0);
		BigDecimal impuestoDeta = new BigDecimal(0);
		BigDecimal impuestoDetaAcum = new BigDecimal(0);
		BigDecimal totalDeta = new BigDecimal(0);
		BigDecimal totalCabe = new BigDecimal(0);
		BigDecimal descueCabePorc = new BigDecimal(0);
		BigDecimal totalIce = new BigDecimal(0);

//		Se recupera el descuento que tiene en la cabecera para poder realizar los calculos respectivos
		if (this.getEgreso().getDescue() != null) {
			descueCabePorc = this.getEgreso().getDescue();
		}

		this.totalDocus.clear();

		try {

			for (EgreDeta egreDeta : egreDetaDataTable) {

				impuestoDetaAcum = new BigDecimal(0);

//				Calcula impuestos de acuerdo al tipo de documento seleccionado
				if (this.getEgreso().getDocuEgre().isGeneraImpu()) {

					// Recorrido de impuestos y retenciones grabados en cada producto
					for (EgreDetaImpu egreDetaImpu : egreDeta.getEgreDetaImpus()) {

//						Calcula el total del ICE por Fundas Plasticas
						if (egreDetaImpu.getTipo().equals("ICE")) {
							totalIce = totalIce.add(egreDeta.getCantid().multiply(egreDetaImpu.getPorcen()));
							egreDeta.setPrecioConImpu(new BigDecimal(0));
							egreDeta.setTotalConImpu(new BigDecimal(0));
//							continue;
						}
//						Fin Calcula el total del ICE por Fundas Plasticas

						BigDecimal impuestoProducto = new BigDecimal(0);

						impuestoProducto = (egreDetaImpu.getPorcen().divide(new BigDecimal(100), 6,
								RoundingMode.HALF_UP)).add(new BigDecimal(1.00));

//						Valores Trasient
						if (egreDeta.getPrecioConImpu() == null) {
							egreDeta.setPrecioConImpu(egreDeta.getPrecioVent().multiply(impuestoProducto));
						}
//						Fin Valores Trasient

						egreDeta.setPrecioVent(
								egreDeta.getPrecioConImpu().divide(impuestoProducto, 6, RoundingMode.HALF_UP));

						// copiado de arriba
						totalBrutoDeta = egreDeta.getCantid().multiply(egreDeta.getPrecioVent());
						descueDeta = totalBrutoDeta.multiply(egreDeta.getDescue()).divide(new BigDecimal(100));
						totalNetoDeta = totalBrutoDeta.subtract(descueDeta);
						egreDeta.setTotal(totalNetoDeta);

//						if (egreDeta.getEgreso() != null) {
						descueCabe = totalNetoDeta.multiply(descueCabePorc).divide(new BigDecimal(100));
						totalNetoDeta = totalNetoDeta.subtract(descueCabe);
//						}
						// fin copiado de arriba

						BigDecimal factor = new BigDecimal(egreDetaImpu.getFactor());

						impuestoDeta = (totalNetoDeta.multiply(egreDetaImpu.getPorcen()).divide(new BigDecimal(100)));
						impuestoDeta = impuestoDeta.multiply(factor);
//						impuestoDeta = impuestoDeta.setScale(6, RoundingMode.HALF_UP);

//						Valores Trasient
						egreDeta.setTotalConImpu(totalNetoDeta.add(impuestoDeta));
//						Fin Valores Trasient

						// Acumula impuesto solo para calculo del total del
						// documento cuando un producto esta grabado con mas de un impuesto
						impuestoDetaAcum = impuestoDetaAcum.add(impuestoDeta);
					}
				}

				totalDeta = totalNetoDeta.add(impuestoDetaAcum);
				totalBrutoCabe = totalBrutoCabe.add(totalBrutoDeta);
				descueTotal = descueTotal.add(descueDeta).add(descueCabe);
				totalCabe = totalCabe.add(totalDeta);
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Producto no tiene precio - Rol no tiene acceso a precios"));
			e.printStackTrace();
		}

		BigDecimal ivaIce = totalIce.multiply(dimmIva.getPorcen()).divide(new BigDecimal(100), 6, RoundingMode.HALF_UP);

//		Por una extraña razon aqui redondea siempre a dos decimales
		TotalDocu totalDocu = new TotalDocu();
		totalDocu.setDescri("Total Documento");
		totalDocu.setValor(totalCabe.setScale(6, RoundingMode.HALF_UP).add(totalIce).setScale(6, RoundingMode.HALF_UP)
				.add(ivaIce).setScale(6, RoundingMode.HALF_UP));
		totalDocus.add(totalDocu);

	}

	public Set<EgreDetaImpu> crearEgreDetaImpu(EgreDeta egreDeta) {

		ProdDimm prodDimm = new ProdDimm();
		prodDimm.setDimm(new Dimm());

		List<ProdDimm> prodDimms = new ArrayList<ProdDimm>();
		Set<EgreDetaImpu> egreDetaImpus = new HashSet<EgreDetaImpu>();

		prodDimm.setProducto(egreDeta.getProducto());

		try {

			prodDimms = venta.buscarProdDimms(prodDimm);

			for (ProdDimm prodDimm2 : prodDimms) {

				if (prodDimm2.getDimm().getFactor() > 0) {

					EgreDetaImpu egreDetaImpu = new EgreDetaImpu();
					egreDetaImpu.setEgreDeta(egreDeta);
					egreDetaImpu.setDimm(prodDimm2.getDimm());
					egreDetaImpu.setDescri(prodDimm2.getDimm().getDescri());
					egreDetaImpu.setCodigo(prodDimm2.getDimm().getCodigo());
					egreDetaImpu.setPorcen(prodDimm2.getDimm().getPorcen());
					egreDetaImpu.setFactor(prodDimm2.getDimm().getFactor());

//					if (prodDimm2.getDimm().getDimmId() >= 3000 && prodDimm2.getDimm().getDimmId() <= 3999) {
//						egreDetaImpu.setTipo("RR");
//					} else if (prodDimm2.getDimm().getDimmId() >= 12000 && prodDimm2.getDimm().getDimmId() <= 12999) {
//						egreDetaImpu.setTipo("RI");
//					} else if (prodDimm2.getDimm().getDimmId() >= 13000 && prodDimm2.getDimm().getDimmId() <= 13099) {
//						egreDetaImpu.setTipo("IVA");
//					} else if (prodDimm2.getDimm().getDimmId() >= 13100 && prodDimm2.getDimm().getDimmId() <= 13199) {
//						egreDetaImpu.setTipo("OTR");
//					}

					if (prodDimm2.getDimm().getDimmId() >= 13000 && prodDimm2.getDimm().getDimmId() <= 13099) {
						egreDetaImpu.setTipo("IVA");
					} else if (prodDimm2.getDimm().getDimmId() >= 11000 && prodDimm2.getDimm().getDimmId() <= 11100) {
//						El ultimo código 11100 es el ice de fundas plasticas
						egreDetaImpu.setTipo("ICE");
					}

					egreDetaImpus.add(egreDetaImpu);
				}
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al crear impuestos y retenciones"));
			e.printStackTrace();
		}

		return egreDetaImpus;
	}

//	Para que estan estos dos metodos si hacen lo mismo buscarEgreso, buscarEgresoPorId
	public void buscarEgreso(Integer egresoId) {
		try {
			this.egreso = venta.buscarEgresoPorId(egresoId);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar egreso"));
			e.printStackTrace();
		}

//		Coloca el tipo de documento que modifica cuando es una nota de crédito
		if (this.egreso.getEgreso() != null) {
			DocuEgre docuEgre = new DocuEgre();

			try {
				docuEgre = venta.buscarDocuEgrePorId(this.egreso.getEgreso().getDocuEgre().getDocumentoId());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al buscar documento que modifica"));
				e.printStackTrace();
			}

			this.egreso.getEgreso().setDocuEgre(docuEgre);
		}
	}

	public Egreso buscarEgresoPorId(Integer egresoId) {

		Egreso egreso = new Egreso();

		try {
			egreso = venta.buscarEgresoPorId(egresoId);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar egreso"));
			e.printStackTrace();
		}

		return egreso;

	}

	public List<DocuEgre> buscarDocuEgres() {

		Documento documento = new Documento();
		documento.setEstado(true);

		DocuEgre docuEgre = new DocuEgre();
		docuEgre.setDocumento(documento);

		List<DocuEgre> docuEgres = new ArrayList<>();

		try {
			docuEgres = venta.buscarDocuEgres(docuEgre);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar documentos egreso"));
			e.printStackTrace();
		}

		return docuEgres;
	}

	public void buscarPersVends() {

		PersVend persVend = new PersVend();
		persVend.setPersona(new Persona());
		persVend.setEstado(true);

		try {
			this.persVends = venta.buscarPersVends(persVend);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar vendedores"));
			e.printStackTrace();

		}
	}

	// <<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES >>>>>>>>>>>>>>>>>>>>

	public String estadoActual() {
		return this.getEgreso().getEstado();
	}

	// Este metodo no se usa
	// Se utilizaba para grabar el kardex al momento de grabar el egreso
	// Ahora se graba el kardex al procesar el egreso
	public void grabarKardex(EgreDeta egreDeta) {

		Kardex kardex = new Kardex();
		kardex.setProducto(egreDeta.getProducto());
		kardex.setDocumento(egreDeta.getEgreso().getDocuEgre().getDocumento());
		kardex.setEgreDeta(egreDeta);
		kardex.setEgreDeta(null);
		kardex.setDocumeFech(egreDeta.getEgreso().getFechaEmis());

		try {
			venta.grabarKardex(kardex);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al crear kardex"));
			e.printStackTrace();

		}

	}

//	Elimina kardex de productos y subprodutos
	public void eliminarKardex() {

		List<Kardex> kardexs = new ArrayList<>();

		try {

			for (EgreDeta egreDeta : this.egreDetaDataTable) {

				Kardex kardex = new Kardex();
				kardex.setIngrDeta(new IngrDeta());
				kardex.setEgreDeta(egreDeta);

				kardexs.addAll(venta.buscarKardexs(kardex));

				for (EgreDeta egreDetaSubProducto : egreDeta.getEgreDetas()) {

					kardex.setEgreDeta(egreDetaSubProducto);
					kardexs.addAll(venta.buscarKardexs(kardex));
				}
			}

			for (Kardex kardex : kardexs) {
				venta.eliminarKardex(kardex);
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar o eliminar kardex"));
			e.printStackTrace();
		}

	}

	public void crearKardex() {

		Bodega bodega = new Bodega();
		bodega.setBodegaId(1);

		for (EgreDeta egreDeta : this.egreDetaDataTable) {

			Producto producto = new Producto();
			try {
				producto = venta.buscarProductoPorId(egreDeta.getProducto().getProductoId());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion buscar producto para crear kardex " + egreDeta.getProducto().getDescri()));
				e.printStackTrace();
			}

//			Se crea kardex solamente si el producto pertenece a un grupo que
//			tenga true en el modulo de inventario			
			if (producto.getProdGrup().isModuloInve()) {

//				Se le coloca egreDeta porque el egreso se pone al grabar egreso antes de eso esta null
				Kardex kardexEgreDeta = new Kardex(egreDeta.getEgreso().getSucursal(), bodega,
						egreDeta.getEgreso().getDocuEgre().getDocumento(), egreDeta.getProducto(), null, egreDeta,
						egreso.getNumero(), egreDeta.getEgreso().getFechaEmis(),
						egreDeta.getEgreso().getFechaHoraEmis());

				try {

					venta.grabarKardex(kardexEgreDeta);

				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion al crear kardex"));
					e.printStackTrace();
				}
			}

			Set<EgreDeta> egreDetaSubProductos = egreDeta.getEgreDetas();
			if (!egreDetaSubProductos.isEmpty()) {

				for (EgreDeta egreDetaSubProducto : egreDetaSubProductos) {

					Producto subProducto = new Producto();
					try {
						subProducto = venta.buscarProductoPorId(egreDetaSubProducto.getProducto().getProductoId());
					} catch (Exception e) {
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
										"Excepcion buscar subProducto para crear kardex "
												+ egreDetaSubProducto.getProducto().getDescri()));
						e.printStackTrace();
					}

//					Se crea kardex solamente si el producto pertenece a un grupo que
//					tenga true en el modulo de inventario			
					if (subProducto.getProdGrup().isModuloInve()) {

//						Se le coloca egreDeta porque el egreso se pone al grabar egreso antes de eso esta null
						Kardex kardexEgreDetaSubProducto = new Kardex(egreDeta.getEgreso().getSucursal(), bodega,
								egreDetaSubProducto.getEgreso().getDocuEgre().getDocumento(),
								egreDetaSubProducto.getProducto(), null, egreDetaSubProducto, egreso.getNumero(),
								egreDetaSubProducto.getEgreso().getFechaEmis(),
								egreDetaSubProducto.getEgreso().getFechaHoraEmis());

						try {
							venta.grabarKardex(kardexEgreDetaSubProducto);
						} catch (Exception e) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
									FacesMessage.SEVERITY_FATAL, null, "Excepcion al crear kardex subProductos"));
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public Integer crearCxc(FpmeFormPago fpmeFormPago, Boolean estado) {

		Integer id = null;

		Cxc cxc = new Cxc();

		try {

			cxc = this.venta.generarCxc(this.egreso, fpmeFormPago, estado);

//			Se resta (suma el valor es negativo) el cambio en el caso que exista
//			sino el cambio siempre es cero
			cxc.setTotal(fpmeFormPago.getTotalReci().add(this.cambio));

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al generar cuenta por cobrar"));
			e.printStackTrace();
		}

		try {
			id = venta.insertarCxc(cxc);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al insertar cuenta por cobrar"));
			e.printStackTrace();
		}
		return id;
	}

	public void eliminarCxc() {

		Cxc cxcBuscar = new Cxc();

//		Coloca el ingreso si es factura o el documento modificado si es nota de credito
//		para poder realizar la busqueda de cxc
		if (this.egreso.getDocuEgre().isGeneraCxc()) {
			cxcBuscar.setEgreso(egreso);
		} else {
			cxcBuscar.setEgreso(egreso.getEgreso());
		}

		try {

			List<Cxc> cxcs = new ArrayList<>();
			cxcs = venta.buscarCxcs(cxcBuscar, null, variablesSesion.getFilasPagina());

			for (Cxc cxc : cxcs) {

				this.eliminarCobrDeta(cxc);

				if (this.egreso.getDocuEgre().isGeneraCxc()) {
					this.venta.eliminarCxc(cxc);
				} else {
//					Cambia el estado de la Cxp
					BigDecimal totalCobros = new BigDecimal(0);
					CobrDeta cobrDetaBuscar = new CobrDeta();
					List<CobrDeta> cobrDetas = new ArrayList<>();
					cobrDetaBuscar.setCxc(cxc);

					cobrDetas = this.venta.buscarCobrDetas(cobrDetaBuscar);
					for (CobrDeta cobrDeta : cobrDetas) {
						totalCobros = totalCobros.add(cobrDeta.getTotal());
					}

					if (totalCobros.compareTo(cxc.getTotal()) == 0) {
						cxc.setEstado(true);
					} else {
						cxc.setEstado(false);
					}

					this.venta.modificarCxc(cxc);
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al eliminar cuentas por cobrar"));
			e.printStackTrace();
		}
	}

//	Elimina cobrDeta, coloca estado anulado en FPME
	public void eliminarCobrDeta(Cxc cxc) {

		CobrDeta cobrDetaBuscar = new CobrDeta();

		List<CobrDeta> cobrDetas = new ArrayList<>();

		cobrDetaBuscar.setCxc(cxc);

		try {
			cobrDetas = this.venta.buscarCobrDetas(cobrDetaBuscar);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar detalle de cobros"));
			e.printStackTrace();
		}

		for (CobrDeta cobrDetaEliminar : cobrDetas) {

			FpmeFormPago fpmeFormPago = new FpmeFormPago();
//			FormPagoMoviEgre formPagoMoviEgreAnular = new FormPagoMoviEgre();

			try {
				fpmeFormPago = venta.buscarFpmeFormPagoPorId(cobrDetaEliminar.getFpmeFormPago().getFpmeFormPagoId());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al buscar FpmeFormPago"));
				e.printStackTrace();
			}

			cobrDetaEliminar.setFpmeFormPago(fpmeFormPago);

//			formPagoMoviEgreAnular = cobrDetaEliminar.getFpmeFormPago().getFormPagoMoviEgre();

			try {
//				Compara los egresoId para deteminar a que egreso pertenece el pago
//				y borrar solamente los abonos del documento que se esta procesando
//				si es venta borra todo si es NC borra solo el abono que se hizo con la NC
				if (cobrDetaEliminar.getEgresoId().equals(this.egreso.getEgresoId())) {

					this.venta.eliminarCobrDeta(cobrDetaEliminar);
//					this.venta.eliminarFpmeFormPago(fpmeFormPago);
//					this.anularFpme(formPagoMoviEgreAnular);

				}
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al eliminar detalle de cobros"));
				e.printStackTrace();
			}
		}
	}

	public void anularFpme(FormPagoMoviEgre fpme) {

		String nota;

		FpmeFormPago fpmeFormPago = new FpmeFormPago();

		List<FpmeFormPago> fpmeFormPagos = new ArrayList<FpmeFormPago>();

		fpmeFormPago.setFormPagoMoviEgre(fpme);

		try {
			fpmeFormPagos = this.venta.buscarFpmeFormPagos(fpmeFormPago);

			for (FpmeFormPago fpmeFormPagoEliminar : fpmeFormPagos) {
				this.venta.eliminarFpmeFormPago(fpmeFormPagoEliminar);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar o eliminar FpmeFormPago"));
			e.printStackTrace();
		}

		if (fpme.getNota() != null) {
			nota = fpme.getNota() + " " + "Anulado o desprocesado desde el modulo venta";
		} else {
			nota = "Anulado o desprocesado desde el modulo venta";
		}

		fpme.setNota(nota);
		fpme.setEstado("AN");

		try {
			this.venta.modificarFpme(fpme);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al modificar FPME"));
			e.printStackTrace();
		}

		if (fpme.getTransaccion() != null) {
//			Anular transaccion del cobro
			try {
				this.transaccionGestion.anularTransaccion(fpme.getTransaccion().getTransaccionId());

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
						"Transacción anulada No. " + fpme.getTransaccion().getNumero()));

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al anular transación de cobro del documento"));
				e.printStackTrace();
			}
		}
	}

//	Anula registro en el caso que el documento se haya cobrado con deposito
//	Al cobrar con deposito se genera un registro en formPagoMoviEgre que es
//	el que resta la cxc y un registro en formPagoMoviIngr que es el que genera
//	el movimiento de bancos. Entonces al anular el egreso
//	se buscar los movimientos que tengan la misma transaccion y se los anula
	public void anularFpmi(FormPagoMoviEgre fpme) {

		List<FormPagoMoviIngr> fpmis = new ArrayList<>();

		if (fpme.getTransaccion() == null) {
			return;
		}

		fpmis = buscarFpmi(fpme);

		for (FormPagoMoviIngr fpmi : fpmis) {

			String nota;

			if (fpmi.getNota() != null) {
				nota = fpmi.getNota() + " "
						+ "Anulado o desprocesado desde el modulo venta documento cobrado con deposito";
			} else {
				nota = "Anulado o desprocesado desde el modulo venta documento cobrado con deposito";
			}

			fpmi.setNota(nota);
			fpmi.setEstado("AN");

			anularFpmi(fpmi);
		}
	}

	public void anularFpmi(FormPagoMoviIngr fpmi) {

		try {
			formPagoMoviIngrRegis.modificar(fpmi);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al anular cobro del documento - deposito"));
			e.printStackTrace();
		}
	}

	public List<FormPagoMoviIngr> buscarFpmi(FormPagoMoviEgre fpme) {

		FormPagoMoviIngr formPagoMoviIngrBuscar = new FormPagoMoviIngr();
		formPagoMoviIngrBuscar.setTransaccion(new Transaccion());

		List<FormPagoMoviIngr> fpmis = new ArrayList<>();

		formPagoMoviIngrBuscar.getTransaccion().setTransaccionId(fpme.getTransaccion().getTransaccionId());

		try {

			fpmis = formPagoMoviIngrLista.buscarTransaccion(formPagoMoviIngrBuscar);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar transacción del documento"));
			e.printStackTrace();
		}

		return fpmis;
	}

	public List<FormPagoMoviEgre> buscarFpmes(FormPagoMoviEgre fpme) {

		List<FormPagoMoviEgre> fpmes = new ArrayList<>();

		try {
			fpmes = this.venta.buscarFpmes(fpme);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar pagos FPME"));
			e.printStackTrace();
		}

		return fpmes;

	}

	public Integer insertarFormPagoMoviEgre() {

		Integer id = null;

		try {

//			Graba Fecha y Hora
			this.formPagoMoviEgre.setFechaHora(this.formPagoMoviEgre.getFecha().atTime(LocalTime.now()));

			// Grabar el número de cobro
			this.formPagoMoviEgre.setNumero(this.formPagoMoviEgre.getDocuMoviEgre().getDocumento().getNumero() + 1);

			// Actualizar secuencial
			this.formPagoMoviEgre.getDocuMoviEgre().getDocumento().setNumero(this.formPagoMoviEgre.getNumero());
			venta.modificarDocumento((this.getFormPagoMoviEgre().getDocuMoviEgre().getDocumento()));

			this.getFormPagoMoviEgre().setEgresoId(this.egreso.getEgresoId());
			id = venta.insertarFpme(this.getFormPagoMoviEgre());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al insertar forma de pago"));
			e.printStackTrace();
		}

//		Implementacion FpmeFormPago
		for (FpmeFormPago fpmeFormPago : this.fpmeFormPagos) {
			try {
				fpmeFormPago.setFormPagoMoviEgre(this.formPagoMoviEgre);
				venta.insertarFpmeFormPago(fpmeFormPago);

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al insertar detalle de pago - FpmeFormPago"));
				e.printStackTrace();
			}
		}
//		Fin Implementacion FpmeFormPago

		return id;
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
				rolPrecs.addAll(venta.buscarRolPrecios(rolPrec));
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

	public void alCambiarDocuEgre() {

		this.factor = this.getEgreso().getDocuEgre().getDocumento().getFactor();

//		Limpia datos anteriores en caso de que el usario cambie de proveedor
//		y haya estado ingresando una nota de credito
		this.cxcSele = new Cxc();
		this.egreso.setEgreso(null);
		this.cxcs.clear();

//		RequestContext context = RequestContext.getCurrentInstance();
//		context.addCallbackParam("factor", this.factor);
//		context.execute("hide('pagar')");
//		context.execute("PF('pagar').hide();");
//		context.update("pagar");
//		context.execute("alert('Hola')");

		this.grabarImprimirEstado = grabarImprVerificarEstado();

	}

	public void buscarSesionVentasSucursal() {

		CajaMovi cajaMovi = new CajaMovi();
		DocuCaja docuCaja = new DocuCaja();
		PersCaje persCaje = new PersCaje();
		docuCaja.setDocumento(new Documento());

		persCaje.setPersona(new Persona());
		cajaMovi.setSucursal(this.rolSucuConvertir.getSucursal());
		cajaMovi.setCaja(new Caja());
		cajaMovi.setDocuCaja(docuCaja);
		cajaMovi.setPersCaje(persCaje);
		cajaMovi.setEstado(false);

		try {

			this.cajaMoviConvertirs = venta.buscarSesionVentas(cajaMovi);

		} catch (Exception e) {

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar movimientos de caja"));
			e.printStackTrace();

		}

	}

	public CajaMovi buscarSesionVentas() {

		CajaMovi cajaMoviUsuario = new CajaMovi();

		CajaMovi cajaMovi = new CajaMovi();
		DocuCaja docuCaja = new DocuCaja();
		docuCaja.setDocumento(new Documento());

		List<CajaMovi> cajaMovis = new ArrayList<>();

		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");

		cajaMovi.setSucursal(variablesSesion.getSucursal());
		cajaMovi.setCaja(new Caja());
		cajaMovi.setDocuCaja(docuCaja);
		cajaMovi.setEstado(false);
		cajaMovi.setPersCaje(persUsuaSesion.getPersona().getPersCaje());

		try {

			cajaMovis = venta.buscarSesionVentas(cajaMovi);

			if (cajaMovis.size() == 1) {
				cajaMoviUsuario = cajaMovis.get(0);
			}

		} catch (Exception e) {

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar movimientos de caja"));
			e.printStackTrace();

		}

		return cajaMoviUsuario;

	}

	public void buscarProdPrecKardTotaView() {
		buscarProdPrecs();
		buscarKardTotaViews();
	}

//	Este Procedimiento no retorna nada ya que se la llama desde la pagina
//	y necesita actualizar y filtrar el dialogo con de la lista prodPrecs
//	Aqui no se coloca en prodGrup el modulo ya que se buscar solo del producto seleccionado
	public void buscarProdPrecs() {

		ProdPrec prodPrec = new ProdPrec();
		Producto producto = new Producto();

		List<ProdPrec> prodPrecs = new ArrayList<>();

//		Se utiliza esta variable porque el parametro puede venir de egreDeta o ProdPrec
//		Si el parámetro viniera solo de ProdPrec se prodría usar ProdPrecSele
		producto.setProductoId(this.productoId);
		producto.setProdGrup(new ProdGrup());

		prodPrec.setSucursal(this.getVariablesSesion().getSucursal());
		prodPrec.setPrecio(new Precio());
		prodPrec.setProducto(producto);
		prodPrec.getProducto().setEstado(true);

		try {
			prodPrecs = venta.buscarProdPrecs(prodPrec, null, null, ordenColumna);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar precios del producto"));
			e.printStackTrace();
		}

		try {
			this.prodPrecSeles = venta.filtrarProdPrecs(prodPrecs, persUsuaSesion,
					this.getVariablesSesion().getRolPrecs(), this.getVariablesSesion().getSucursal());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al filtrar precios del producto"));
			e.printStackTrace();
		}
	}

	public void buscarProdPrecsBalanza(Integer paginador) {

		String codigoBarra = this.prodPrec.getProducto().getCodigoBarra();

		if (parametroActivarBalanza.getDescri().equals("Activar")
				&& this.prodPrec.getProducto().getCodigoBarra() != null && codigoBarra.length() == 13) {

			this.prodPrec.getProducto().setCodigoBarra(codigoBarra.substring(0, Integer.parseInt(digiCodigoPrecio[0])));
			this.prodPrec.getProducto().setCodigo(null);
			this.prodPrec.getProducto().setDescri(null);

			buscarProdPrecs(paginador);

			if (this.prodPrec.getProducto().getCodigoBarra() != null && this.prodPrecs.size() == 1) {

				if (this.prodPrecs.get(0).getProducto().isBalanz()) {
//					Coloca precio del producto que se lee desde el codigo de barra
					int digitosPrecio = Integer.parseInt(digiCodigoPrecio[0]);
					int digitosEnteros = Integer.parseInt(digiCodigoPrecio[1]);
					int digitosDecimales = Integer.parseInt(digiCodigoPrecio[2]);

					this.prodPrecSele = this.prodPrecs.get(0);

					ProdDimm prodDimmBuscar = new ProdDimm(prodPrecSele.getProducto(), new Dimm());
					ProdDimm prodDimmIva = buscarProdDimmIva(prodDimmBuscar);

					String precioEnteros = codigoBarra.substring(digitosPrecio, digitosPrecio + digitosEnteros);
					String precioDecimales = codigoBarra.substring(digitosPrecio + digitosEnteros,
							digitosPrecio + digitosEnteros + digitosDecimales);

					BigDecimal precio = new BigDecimal(precioEnteros + "." + precioDecimales);

					if (prodDimmIva.getDimm().getPorcen() != BigDecimal.ZERO) {

						BigDecimal impuestoProducto = (prodDimmIva.getDimm().getPorcen().divide(new BigDecimal(100), 2,
								RoundingMode.HALF_UP)).add(new BigDecimal(1.00));

						precio = precio.divide(impuestoProducto, 6, RoundingMode.HALF_UP);

					}

					this.prodPrecSele.setPrecioSinImpu(precio);
					this.agregarProducto();
					this.prodPrec.getProducto().setCodigoBarra(null);
					return;
				}
			}
		}

		this.prodPrec.getProducto().setCodigoBarra(codigoBarra);

		if (this.prodPrec.getProducto().getCodigoBarra() != null) {
			this.prodPrec.getProducto().setCodigo(null);
			this.prodPrec.getProducto().setDescri(null);
		}

		agregarComodin(prodPrec);
		buscarProdPrecs(paginador);
		quitarComodin(prodPrec);

		if (this.prodPrec.getProducto().getCodigoBarra() != null && this.prodPrecs.size() == 1) {
			this.prodPrecSele = this.prodPrecs.get(0);
			this.agregarProducto();

			this.prodPrec.getProducto().setCodigoBarra(null);
		}

		if (this.prodPrecs.size() == 0) {

			PrimeFaces.current().executeScript("PF('prodNoExisteDialogo').show();");
		}
	}

//	Busca el Iva que tiene el producto
	public ProdDimm buscarProdDimmIva(ProdDimm prodDimm) {

		List<ProdDimm> prodDimms = new ArrayList<ProdDimm>();
		ProdDimm prodDimmIva = new ProdDimm();

		try {

			prodDimms = venta.buscarProdDimms(prodDimm);
			for (ProdDimm prodDimmRecorrer : prodDimms) {
				prodDimmRecorrer.getDimm().getDescri().substring(0, 3).equals("Iva");
				prodDimmIva = prodDimmRecorrer;
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar impuestos - ProdDimm"));
			e.printStackTrace();
		}

		return prodDimmIva;

	}

//	Este metodo llena la lista de los productos que se van a agregar al documento
	public void buscarProdPrecs(Integer paginador) {

		if (paginador == 0) {
			this.pagina = 0;
		}
//		if (this.prodPrec.getProducto().getCodigoBarra() != null) {
//			this.prodPrec.getProducto().setCodigo(null);
//			this.prodPrec.getProducto().setDescri(null);
//		}
//
//		prodPrec.setSucursal(this.getVariablesSesion().getSucursal());
//		prodPrec.setPrecio(this.precioPred);
//		prodPrec.setProducto(this.prodPrec.getProducto());
//
//		this.prodPrec.getProducto().getProdGrup().setModuloVent(true);
//		this.prodPrec.getProducto().setEstado(true);

		try {
//			agregarComodin(prodPrec);

			this.prodPrecs = venta.buscarProdPrecs(prodPrec, this.pagina, variablesSesion.getFilasProductosEgreso(),
					this.ordenColumna);
			this.numeroReg = venta.tamanioProdPrecs();
			this.contadorReg = venta.contarProdPrecs(prodPrec);

//			if (this.prodPrec.getProducto().getCodigoBarra() != null && this.prodPrecs.size() == 1) {
////				this.productoId = this.prodPrecs.get(0).getProducto().getProductoId();
//				this.prodPrecSele = this.prodPrecs.get(0);
//				this.agregarProducto();
//
//				this.prodPrec.getProducto().setCodigoBarra(null);
//			}

//			quitarComodin(prodPrec);
//			if (this.prodPrecs.size() == 0) {
//
//				PrimeFaces.current().executeScript("PF('prodNoExisteDialogo').show();");
//			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar productos (ProdPrec)"));
			e.printStackTrace();

		}
	}

	public void agregarComodin(ProdPrec prodPrec) {

		String codigo = prodPrec.getProducto().getCodigo();

		if (codigo != null) {
			codigo = "%" + codigo + "%";
			prodPrec.getProducto().setCodigo(codigo);

		}
	}

	public void quitarComodin(ProdPrec prodPrec) {

		String codigo = prodPrec.getProducto().getCodigo();

		if (codigo != null) {

			String codigoSinComodin = codigo.replace("%", "");
			prodPrec.getProducto().setCodigo(codigoSinComodin);

		}
	}

	public void seleccionarPrecio() {

		EgreDeta egreDeta = new EgreDeta();
		EgreDeta egreDetaInicial = new EgreDeta();

		List<KardTotaView> kardTotaViews = new ArrayList<>();

		// ProdPrec prodPrec = new ProdPrec();
		//
		// egreDetaSele.setPrecio(precio);
		// prodPrec = this.buscarProdPrec(precio);
		// egreDetaSele.setPrecioVent(prodPrec.getPrecioSinImpu());
		// egreDetaSele.setPrecioConImpu(prodPrec.getPrecioConImpu());
		// egreDetaSele.setFactor(prodPrec.getFactor());

		// Entra a este if cuando se selecciona el precio abriendo el dialogo desde la
		// lista productos no desde productos agregados
		if (origenPrecio == 0) {

			String agregado = this.agregarProducto();

			if (agregado.equals("agregado")) {

				egreDeta = this.egreDetaDataTable.get(0);

				egreDeta.setPrecio(this.prodPrecSele.getPrecio());
				egreDeta.setPrecioConImpu(this.prodPrecSele.getPrecioConImpu());
				egreDeta.setFactor(this.prodPrecSele.getFactor());
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Producto no agregado, revise inventario"));
			}

			this.calcularTotalEgres();

			this.origenPrecio = 1;
			return;
		}

		egreDetaInicial.setPrecio(egreDetaSele.getPrecio());
		egreDetaInicial.setPrecioConImpu(egreDetaSele.getPrecioConImpu());
		egreDetaInicial.setFactor(egreDetaSele.getFactor());

		egreDetaSele.setPrecio(this.prodPrecSele.getPrecio());
		egreDetaSele.setPrecioConImpu(this.prodPrecSele.getPrecioConImpu());
		egreDetaSele.setFactor(this.prodPrecSele.getFactor());

		if (egreDetaSele.getPrecioConImpu().compareTo(BigDecimal.ZERO) == 0) {

			egreDetaSele.setPrecio(egreDetaInicial.getPrecio());
			egreDetaSele.setPrecioConImpu(egreDetaInicial.getPrecioConImpu());
			egreDetaSele.setFactor(egreDetaInicial.getFactor());

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Precio seleccionado es igual a cero, verificar"));
		}

		// Controla inventario solamente si el documento es factura (factor < 0)
		if (this.factor < 0) {

			KardTotaView kardTotaView = new KardTotaView();

			kardTotaView.setSucursalId(variablesSesion.getSucursal().getSucursalId());
			kardTotaView.setBodegaId(1);
			kardTotaView.setProductoId(egreDetaSele.getProducto().getProductoId());

			kardTotaViews = this.buscarKardTotaViews(kardTotaView);
//			kardTotaViews = this.buscarKardTotaViews(egreDetaSele.getProducto());

			if (kardTotaViews.size() != 0) {

				if (kardTotaViews.get(0).getCantidSald()
						.compareTo((BigDecimal) (egreDetaSele.getCantid().multiply(egreDetaSele.getFactor()))) == -1) {

					if (egreDetaSele.getProducto().getControStoc().equals("Advertencia")) {

						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
								null, "No tiene suficiente Stock de este producto"));

					} else if (egreDetaSele.getProducto().getControStoc().equals("Controla")) {

						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
								null, "No tiene suficiente Stock de este producto"));

						egreDetaSele.setPrecio(egreDetaInicial.getPrecio());
						egreDetaSele.setPrecioConImpu(egreDetaInicial.getPrecioConImpu());
						egreDetaSele.setFactor(egreDetaInicial.getFactor());

						return;
					}

//					egreDetaSele.setPrecio(egreDetaInicial.getPrecio());
//					egreDetaSele.setPrecioConImpu(egreDetaInicial.getPrecioConImpu());
//					egreDetaSele.setFactor(egreDetaInicial.getFactor());

//					return;
				}

			} else {

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
						"Información - Producto no tiene movimientos - kardTotaView"));
//				return;
			}
		}
		this.calcularTotalEgres();
	}

	public CajaDocuEgre buscarCajaDocuEgre(Egreso egreso, CajaMovi cajaMovi) {

//		CajaMovi cajaMovi = new CajaMovi();
		CajaDocuEgre cajaDocuEgre = new CajaDocuEgre();
		CajaDocuEgre cajaDocuEgreSecuencial = new CajaDocuEgre();

		List<CajaDocuEgre> cajaDocuEgres = new ArrayList<>();

//		cajaMovi = this.buscarSesionVentas();

		cajaDocuEgre.setDocuEgre(egreso.getDocuEgre());
		cajaDocuEgre.setCaja(cajaMovi.getCaja());

		try {

			cajaDocuEgres = venta.buscarCajaDocuEgre(cajaDocuEgre);

			for (CajaDocuEgre cajaDocuEgre2 : cajaDocuEgres) {
				cajaDocuEgreSecuencial = cajaDocuEgre2;
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar secuencial en Caja Documento Egreso"));
			e.printStackTrace();
		}

		return cajaDocuEgreSecuencial;
	}

	public void modificarCajaDocuEgre(CajaDocuEgre cajaDocuEgre) {
		try {
			venta.modificarCajaDocuEgre(cajaDocuEgre);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al actualizar número secuencial"));
			e.printStackTrace();
		}
	}

//	Este metodo tambien se usa para imprimir
//	se llama desde un boton oculta en egreDeta.xhtml
	public void descargar() {

//		Se buscar nuevamente el egreso porque antes de descargar se finaliza la conversacion
//		y se pierde todos los datos de los formularios

		this.idDescargar = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("idDescargar");

		this.buscarEgreso(this.idDescargar);

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		Parametro parametro = new Parametro();

//		Se coloca aqui la ruta para poder tener los reportes en diferentes y varias carpetas
//		Sino se podria poner en la clase GenerarJasperReport
		String rutaJrxml; // = "C:\\tecnoIntel\\soem\\jasperReportes\\ingreso\\";
//		En esta ruta se compila el reporte dentro del servidor de aplicaciones dentro del archivo .EAR .WEB
		String rutaReporteCompilado = "\\jasperReportes\\egreso\\";

		try {

			Integer egresoId = this.idDescargar;
//			Integer egresoId = this.egreso.getEgresoId();
			if (egresoId == null) {
				parametrosJasper.put("egresoId", 0);
			} else {
				parametrosJasper.put("egresoId", egresoId);
			}

			parametro = parametroRegis.buscarPorId(Parametro.class, 3000);
			rutaJrxml = parametro.getDescri();

//			INICIO SELECCION DE IMPRESION
//			Buscar la session de ventas del usuario actual 
//			para poder imprimir la factura en la caja que abrio el usuario
//			La factura no va a imprimir en la caja que se hizo originalmente
//			Esto es en el caso que abra la factura un administrador
//			Para que pueda imprimir en la caja que abrio el administrador, 
//			si abre la factura un cajero obviamente va a imprimir en la caja que se hizo la factura
//			ya que en la busqueda se filtra por sucursal y por caja
//			NOTA: si siempre se quiere imprimir en la caja que se hizo la factura cambiar:
//			esta linea: cajaPeri.setCaja(cajaMovi.getCaja());
//			por esta: cajaPeri.setCaja(this.egreso.getCajaMovi().getCaja());
			CajaMovi cajaMovi = this.buscarSesionVentas();

//			CajaPeri cajaPeri = new CajaPeri();
//			List<CajaPeri> cajaPeris = new ArrayList<>();
//			
//			cajaPeri.setCaja(cajaMovi.getCaja());
//			cajaPeri.setPeriferico(new Periferico());
//			cajaPeri.setAcceso(true);
//			
//			cajaPeris = venta.buscarCajaPeri(cajaPeri);
//			FIN SELECCION DE IMPRESION			

//			Imprime en todas las impresoras seleccionadas en cada caja
			CajaDocuEgre cajaDocuEgre = new CajaDocuEgre();
			CajaDocuEgre cajaDocuEgreParam = new CajaDocuEgre();
			List<CajaDocuEgre> cajaDocuEgres = new ArrayList<>();

			cajaDocuEgreParam.setCaja(cajaMovi.getCaja());
			cajaDocuEgreParam.setDocuEgre(this.egreso.getDocuEgre());
			cajaDocuEgres = venta.buscarCajaDocuEgre(cajaDocuEgreParam);

			for (CajaDocuEgre cajaDocuEgre2 : cajaDocuEgres) {
				cajaDocuEgre = cajaDocuEgre2;
			}

			SaliArch saliArch = new SaliArch();
			List<SaliArch> saliArchs = new ArrayList<>();

			saliArch.setCajaDocuEgre(cajaDocuEgre);
			saliArchs = venta.buscarSaliArchs(saliArch);
//			Recorre la lista de configuración impresora, archivo, correo y ejecuta solamente la que este predeterminado
			for (SaliArch saliArch2 : saliArchs) {
				if (saliArch2.getPredet()) {
					this.crearArchivo(parametrosJasper, rutaJrxml, rutaReporteCompilado, formatoReporte, saliArch2);
				}
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al descargar documento"));
			e.printStackTrace();
		}

		this.egreso = new Egreso();
		this.idDescargar = 0;

//		Se copio todo esto de iniciarVentaCabe ya que al descargar en pdf 
//		da un conflicto y no se ejecuta correctamente ningun metodo que se llame desde aqui 
		DocuEgre docuEgrePred = new DocuEgre();

		egreso.setFechaEmis(LocalDate.now());
		egreso.setFechaHoraEmis(LocalDateTime.now());
		egreso.setSerie1("0");
		egreso.setSerie2("0");
		egreso.setNumero(0);
		egreso.setAutori("0");
		egreso.setClaveAcce("0");
		egreso.setDescue(new BigDecimal(0));
		egreso.setNumeroCuot((short) 1);
		// Se diseño con el fin de crear cuotas por 30, 15, 7 o cualquier numero de dias
		// Se elimina de la pagina para generar solamente cuotas mensuales
		egreso.setDiasPlaz((short) 0);
		egreso.setEstado("GR");

		// Seleccionar el documento predetermido en rolDocu
		for (DocuEgre docuEgre : this.docuEgres) {
			for (RolDocu rolDocu : rolDocus) {
				if (docuEgre.getDocumentoId().equals(rolDocu.getDocumento().getDocumentoId())
						&& rolDocu.getPredet() == true) {
					docuEgrePred = docuEgre;
				}
			}
		}

		this.egreso.setDocuEgre(docuEgrePred);
		// Fin seleccionar el documento predetermido en rolDocu

		// Coloca al valor de la variable factor para habilitar o inhabilitar botones
		// Y para verificar el control de inventario segun el documento que seleccione
		this.factor = docuEgrePred.getDocumento().getFactor();

//		Seleccionar cliente y vendedor predeterminado
		egreso.setPersClie(variablesSesion.getPersClie());
		egreso.setPersVend(variablesSesion.getPersVend());

		if (this.egreso.getDocuEgre().getDocumento().getFactor() == -1) {
			egreso.setNota("Venta");
		} else if (this.egreso.getDocuEgre().getDocumento().getFactor() == 1) {
			egreso.setNota("Nota de Crédito");
		} else if (this.egreso.getDocuEgre().getDocumento().getFactor() == 0) {
			egreso.setNota("Proforma");
		}

		this.calcularFechaCobro();

		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idDescargar", 0);

	}

	public void crearArchivo(Map<String, Object> parametrosJasper, String rutaJrxml, String rutaReporteCompilado,
			String formato, SaliArch saliArch) {

		String nombreReporte = null;

		Parametro parametro = new Parametro();
		PersUsua persUsuaSesion = new PersUsua();

		Connection connection = null;

		try {

			persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
					.get("persUsua");

			connection = dataSource.getConnection();

			parametrosJasper.put("sucursal", this.getVariablesSesion().getSucursal().getDescri());
			parametrosJasper.put("usuario", persUsuaSesion.getPersona().getNombre());

//			Se usa la entidad parametro para traer todos los valores de la tabla
//			conforme se va buscando se va asignanado para no crear muchas entidades parametro

//			Traer ruta de la plantilla de la tabla parametro
			parametro = parametroRegis.buscarPorId(Parametro.class, 6001);
			parametrosJasper.put("estilo", parametro.getDescri());
//			Traer ruta del logo de la tabla parametro
			parametro = parametroRegis.buscarPorId(Parametro.class, 6002);
			parametrosJasper.put("logoReporte", parametro.getDescri());

//			Buscar variables iva para poder separar los subtotales e imprimir en la factura los valores por separado
//			dimm = venta.buscarDimmPorId(13030);
//			parametrosJasper.put("egreDetaImpuDescri12", dimm.getDescri().trim());
//			
//			dimm = venta.buscarDimmPorId(13040);
//			parametrosJasper.put("egreDetaImpuDescri0", dimm.getDescri().trim());

//			Envia como parametro el porcentaje predeterminado actual
			parametrosJasper.put("dimmIvaPorcen", dimmIva.getPorcen());
			parametrosJasper.put("rutaJrxml", rutaJrxml);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

			int paginasJasperPrint = 0;
			int paginasJasperPrint2 = 0;

			JasperPrint jasperPrint = new JasperPrint();
			JasperPrint jasperPrint2 = null;

			String[] formatos;
			formatos = saliArch.getFormat().split(";");

			if (formatos[0] != null) {
				nombreReporte = formatos[0];
				String rutaReporteJrxml = rutaJrxml + nombreReporte + ".jrxml";
				String reporteCompilado = nombreReporte + ".jasper";

				jasperPrint = this.crearJasperPrint(request, rutaReporteJrxml, rutaReporteCompilado, reporteCompilado,
						parametrosJasper, connection);
			}

			if (formatos.length > 0) {

				for (int count = 1; count < (formatos.length); count++) {

					nombreReporte = formatos[count];
					String rutaReporteJrxml = rutaJrxml + nombreReporte + ".jrxml";
					String reporteCompilado = nombreReporte + ".jasper";

					jasperPrint2 = this.crearJasperPrint(request, rutaReporteJrxml, rutaReporteCompilado,
							reporteCompilado, parametrosJasper, connection);
					paginasJasperPrint2 = jasperPrint2.getPages().size();

					// Junta los dos reportes en uno solo
					paginasJasperPrint = jasperPrint.getPages().size();
					for (int count2 = 0; count2 < (paginasJasperPrint2); count2++) {
						jasperPrint.addPage(paginasJasperPrint, (JRPrintPage) jasperPrint2.getPages().get(count2));
					}
				}
			}

			if (saliArch.getSalida().equals("IMPRESORA")) {

				PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
				PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
				printServiceAttributeSet.add(new PrinterName(saliArch.getImpres(), null));

				JRPrintServiceExporter jrPrintServiceExporter = new JRPrintServiceExporter();

				SimplePrintServiceExporterConfiguration simplePrintServiceExporterConfiguration = new SimplePrintServiceExporterConfiguration();
				simplePrintServiceExporterConfiguration.setPrintRequestAttributeSet(printRequestAttributeSet);
				simplePrintServiceExporterConfiguration.setPrintServiceAttributeSet(printServiceAttributeSet);
				simplePrintServiceExporterConfiguration.setDisplayPageDialog(false);
				simplePrintServiceExporterConfiguration.setDisplayPrintDialog(false);

				jrPrintServiceExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				jrPrintServiceExporter.setConfiguration(simplePrintServiceExporterConfiguration);
				jrPrintServiceExporter.exportReport();

			} else if (saliArch.getSalida().equals("ARCHIVO")) {

				// Se añade la extensión al nombre del archivo
				nombreReporte = nombreReporte + this.egreso.getNumero() + "." + formato;
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				JRPdfExporter exporterPdf = new JRPdfExporter();
				exporterPdf.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporterPdf.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();

				exporterPdf.setConfiguration(configuration);
				exporterPdf.exportReport();

				generaJasperReportes.abrirReporte(byteArrayOutputStream.toByteArray(), nombreReporte,
						"application/pdf");

			}
//			else if (saliArch.getSalida().equals("CORREO")) {
//				Esto se hace cuando el documento ya este autorizado por el SRI
//			}

		} catch (JRException jre) {
			jre.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error en reporte"));
			jre.printStackTrace();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	public JasperPrint crearJasperPrint(HttpServletRequest request, String rutaReporteJrxml,
			String rutaReporteCompilado, String reporteCompilado, Map<String, Object> parametrosJasper,
			Connection connection) throws JRException {

		JasperCompileManager.compileReportToFile(rutaReporteJrxml,
				request.getSession().getServletContext().getRealPath(rutaReporteCompilado + reporteCompilado));

		File file = new File(
				request.getSession().getServletContext().getRealPath(rutaReporteCompilado + reporteCompilado));

		JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(file.getPath());
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametrosJasper, connection);

		return jasperPrint;
	}

	public BigDecimal calcularCambio() {

//		this.cambio = this.getFormPagoMoviEgre().getTotalReci().subtract(this.getFormPagoMoviEgre().getTotal());
//		if (this.cambio.compareTo(new BigDecimal(0)) < 0) {
//
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
//					"Error al cobrar", "Valor recibido es menor que total documento"));
//		}
		BigDecimal totalReci = new BigDecimal(0);
		totalReci = this.calcularTotalReci();

		return this.getTotalDocumento().subtract(totalReci);
	}

//	Este metodo se usa para mostrar el cambio redondeado y el total recibido en la pagina
//	Se redondea el cambio para controlar que en las validaciones el cambio <= a cero 
	public void calcularCambioView() {

		BigDecimal cambio = new BigDecimal(0);
		BigDecimal totalReci = new BigDecimal(0);

		totalReci = this.calcularTotalReci();
		cambio = this.getTotalDocumento().subtract(totalReci);

		this.formPagoMoviEgre.setTotalReci(totalReci);
		this.cambio = cambio.setScale(2, RoundingMode.HALF_UP);
	}

	public BigDecimal calcularTotalReci() {
		BigDecimal totalReci = new BigDecimal(0);

		for (FpmeFormPago fpmeFormPago : this.fpmeFormPagos) {
			totalReci = totalReci.add(fpmeFormPago.getTotalReci());
		}

		return totalReci;
	}

	public String procesarImprimirDialogo(Boolean imprimir) {

		String navegar = null;
		String estadoDocu = null;

		int contadorFormPagoContado = 0;

		BigDecimal saldoCliente = new BigDecimal(0);
		BigDecimal totalCredito = new BigDecimal(0);
		BigDecimal totalNoContado = new BigDecimal(0);

		boolean procesar = false;

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

//		Si la forma de pago es crédito, si es factura (factor = -1) 
//		y si el valor de la factura mas el saldo es mayor que el cupo
//		controla el limite de credito
		for (FpmeFormPago fpmeFormPagoRecorrer : this.fpmeFormPagos) {

			if (fpmeFormPagoRecorrer.getFormPago().getTipo().equals("CR")) {
				totalCredito = totalCredito.add(fpmeFormPagoRecorrer.getTotalReci());
			}

			if (fpmeFormPagoRecorrer.getFormPago().getTipo2().equals("VN-CO")) {
				contadorFormPagoContado++;
			} else {
				totalNoContado = totalNoContado.add(fpmeFormPagoRecorrer.getTotalReci());
			}
		}

		saldoCliente = this.getSaldoCliente().add(totalCredito);

		if (this.formPagoMoviEgre.getTotal().setScale(2, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO) == 0) {
			procesar = false;
			navegar = null;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Total documento es igual a cero"));

		} else if (this.factor < 0 && totalCredito.compareTo(BigDecimal.ZERO) > 0
				&& saldoCliente.compareTo(this.egreso.getPersClie().getCupo()) > 0) {
			procesar = false;
			navegar = null;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Total Cxc supera cupo de crédito"));

		} else if (this.cambio.compareTo(new BigDecimal(0)) > 0) {
			procesar = false;
			navegar = null;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Valor recibido es menor que total documento"));

		} else if (totalNoContado.setScale(2, RoundingMode.HALF_UP)
				.compareTo(this.formPagoMoviEgre.getTotal().setScale(2, RoundingMode.HALF_UP)) > 0) {

			procesar = false;
			navegar = null;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Valor recibido es inconsistente con total documento"));

		} else if (contadorFormPagoContado > 1) {
			procesar = false;
			navegar = null;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Existe mas de un pago de contado"));

		} else if (contadorFormPagoContado == 0 && this.cambio.compareTo(new BigDecimal(0)) > 0) {
			procesar = false;
			navegar = null;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Valor recibido debe ser igual a total documento"));
		} else {

			procesar = true;

			estadoDocu = this.pagar();

			if (estadoDocu == "grabado") {
//				Coloca el id del egreso para poder imprimirlo, la impresion se realiza al abrir vetaDeta
				if (imprimir) {
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idDescargar",
							this.getEgreso().getEgresoId());
				} else {
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idDescargar", 0);
				}

				navegar = "/ppsj/egreso/venta/ventaDeta?faces-redirect=true";

			} else {
				navegar = null;
			}
		}
		PrimeFaces.current().ajax().addCallbackParam("procesar", procesar);
		return navegar;
	}

	public void buscarRolSucus() {

		List<RolSucu> rolSucus = new ArrayList<>();

		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");

		try {
			rolSucus = venta.buscarRolSucus(persUsuaSesion.getRolPersUsuas());
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

	public String convertir() {

		String estadoDocu;
		String navegar = null;

		if (this.docuEgreConvertir == null || this.rolSucuConvertir == null) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Seleccione sucursal y documento de destino"));
			return null;
		}

//		Para que pueda entrar al metodo grabar sino entra a modificar
//		Se coloca null en los ids de las entidades
		this.egresoId = null;

		for (EgreDeta egreDeta : this.egreDetaDataTable) {
			egreDeta.setEgreDetaId(null);

			for (EgreDetaImpu egreDetaImpu : egreDeta.getEgreDetaImpus()) {
				egreDetaImpu.setEgreDetaImpuId(null);
				egreDetaImpu.setEgreDeta(egreDeta);
			}

			for (EgreDeta egreDetaSubProducto : egreDeta.getEgreDetas()) {
				egreDetaSubProducto.setEgreDetaId(null);
				egreDetaSubProducto.setEgreDeta(egreDeta);
			}

//			egreDeta.setEgreDetaImpus(egreDeta.getEgreDetaImpus());
		}

//		El new Egreso(this.egreso) duplica el egreso actual;
		Egreso egresoDuplicado = new Egreso(this.egreso);

		egresoDuplicado.setFechaEmis(LocalDate.now());
		egresoDuplicado.setFechaHoraEmis(LocalDateTime.now());

//		Coloca nuevo documento y nueva sucursal
		egresoDuplicado.setDocuEgre(this.docuEgreConvertir);
		egresoDuplicado.setSucursal(this.rolSucuConvertir.getSucursal());

		egresoDuplicado.setNota("Documento Convertido");

		this.egreso = egresoDuplicado;

		estadoDocu = this.grabarEgreso("GR");

		if (estadoDocu == "grabado") {

			this.conversationEnd();

			navegar = "/ppsj/egreso/egreso/lista.xhtml?faces-redirect=true";

			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Documento grabado No.:" + this.egreso.getNumero()));
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

//		La lista de documentos se usa la lista inicial
//		this.buscarSucurals();

		try {

			PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
					.get("persUsua");

//			Buscar el acceso del rol a cada una de las sucursales
//			Se llena esta lista pero en el caso que un usuario tenga dos roles
//			se va a duplicar la sucursal, se tendría que sacar en otra lista aparte solo las sucursales
			List<RolSucu> rolSucus = venta.buscarRolSucus(persUsuaSesion.getRolPersUsuas());
			// PARA NO HACER ESTO SE PUEDE AUMENTAR EN LA CONSULTA UN GROUPBY
			rolSucuConvertirs = obtenerRolSucuUnicosPorSucursal(rolSucus);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar sucursales del usuario: " + persUsuaSesion.getPersona().getApelli()));
			e.printStackTrace();
		}

//		Se podria añadir a las sucursales en un lista por separado y desplegar el combo
//		for (RolSucu rolSucu : rolSucus) {
//			if (rolSucu.getAcceso() == true){
//				this.sucursals.add(rolSucu.getSucursal().getSucursalId());	
//			}
//		}
		
	}

	public void borraDatosConvertir() {
		this.rolSucuConvertir = null;
		this.cajaMoviConvertir = null;
	}

	public TranPlan recorrerTranPlans(List<TranPlan> tranplans) {

		TranPlan tranPlanCxp = new TranPlan();

		for (TranPlan tranPlan : tranplans) {
			if (tranPlan.getTipo().equals("CXC")) {
				tranPlanCxp = tranPlan;
				break;
			}
		}

		return tranPlanCxp;
	}

	// <<<<<<<<<<<<<<<<<<<< FACTURACION ELECTRONICA >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< FACTURACION ELECTRONICA >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< FACTURACION ELECTRONICA >>>>>>>>>>>>>>>>>>>>

	public String procesarComprobanteElectronico() {

		String rutaGenerados = null;
//		NO ENVIADO es el estado que se coloca de acuedo al SRI en el caso de que solo se grabe la factura
//		o en el caso de se intento enviar pero ni siquiera se creo el archivo xml (NoCreado)
		String estadoProceso = "NO ENVIADO";

		estadoProceso = this.crearArchivoXml();

		if (estadoProceso == "GENERADO") {

//			Representa el nombre del archivo con se graba para firmar
			String nombreArchivo = this.egreso.getClaveAcce() + ".xml";
			rutaGenerados = this.variablesSesion.getRutaGenerados() + nombreArchivo;

			try {

				firmarArchivo.signBes(this.egreso.getSucursal().getSucuCertEmis(), nombreArchivo,
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

//				Este proceso envia el documento en un proceso diferente
//				esperando el tiempo determinado en parametros ID = 3200
				this.ejecutarTareaEnviar(archivoFirmado);
				this.ejecutarTareaAutorizar(archivoFirmado);
				this.ejecurarTareaCrearRideFactura();
//
				if (this.egreso.getPersClie().getPersona().getCorreo() != null
						&& this.egreso.getDocuEgre().getDocumento().isEnviaCorreo()) {
					this.ejecutarTareaEnviarCorreoDocu(this.egreso.getPersClie().getPersona().getCorreo(),
							this.egreso.getClaveAcce());
				}

//				Este proceso envia el documento en el mismo proceso
//				como esta en el mismo hilo hay que esperar que se termine todo el proceso
//				estadoProceso = this.enviarArchivo(archivoFirmado, this.getVariablesSesion().getSucursal(), this.egreso.getDocuEgre().getDimm().getCodigo(), this.egreso.getClaveAcce());

			} else {

//				Mover es mensaje a otra variable para poder grabar en la base de datos en motivo_rech
//				cambiar el estadoProceso a GENERADO para colocar en estado_docu_elec
				this.detalleProceso = estadoProceso;

				estadoProceso = "GENERADO";

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error al firmar", estadoProceso));
			}
		}

		return estadoProceso;
	}

	public String crearArchivoXml() {

		String estado = "NO ENVIADO";

		Object object = null;

		FpmeFormPago fpmeFormPago = new FpmeFormPago();

//		Asignar fpmeFormPago = fpmeFormPagoRecorrer para saber la forma de pago
//		y enviar como parametro, si no existe porque la factura es a credito 
//		se manda fpmeFormPago vacio
		for (FpmeFormPago fpmeFormPagoRecorrer : this.fpmeFormPagos) {
//			if (fpmeFormPagoRecorrer.getFormPago().getTipo().equals("FP")) {
			fpmeFormPago = fpmeFormPagoRecorrer;
//			}
		}

//		Agraga notas al egreso para factira electrónica - InfoAdicional
		Set<EgreNota> egreNotasSet = new HashSet<>(this.egreNotas);
		this.egreso.setEgreNotas(egreNotasSet);

		try {
			object = documeElectRegis.generarComprobanteElectronico(this.egreso, this.egreDetaDataTable,
					variablesSesion.getCodigoIva(), variablesSesion.getCodigoIce(), variablesSesion.getCodigoIrbpnr(),
					fpmeFormPago);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepción - Error al generar comprobante"));
			e.printStackTrace();
		}

		try {

			if ((object instanceof Factura)) {
				documeElectRegis.marshalFactura((Factura) object, this.variablesSesion.getRutaGenerados());
			} else if ((object instanceof NotaCredito)) {
				documeElectRegis.marshalNotaCredito((NotaCredito) object, this.variablesSesion.getRutaGenerados());
			} else if ((object instanceof NotaDebito)) {
				documeElectRegis.marshalNotaDebito((NotaDebito) object, this.variablesSesion.getRutaGenerados());
			} else if ((object instanceof GuiaRemision)) {
				documeElectRegis.marshalGuiaRemision((GuiaRemision) object, this.variablesSesion.getRutaGenerados());
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

	public void ejecutarTareaEnviar(File archivoFirmado) {

		try {

			manejadorTareaEnvioDocu.ejecutarTareaEnviarDocu(this.egreso, archivoFirmado, this.egreso.getSucursal(),
					this.egreso.getDocuEgre().getDimm().getCodigo(), this.egreso.getClaveAcce(),
					variablesSesion.getProxyIp(), variablesSesion.getProxyPuerto(),
					this.egreso.getDocuEgre().getAmbien(), variablesSesion.getUrlProduccion(),
					variablesSesion.getUrlPruebas(), "RecepcionComprobantesOffline");

		} catch (ExecutionException | InterruptedException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Documento no enviado al SRI"));
			e.printStackTrace();
		}
	}

	public void ejecurarTareaCrearRideFactura() {

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

		Parametro parametro;
		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3203);
			Integer tiempoEspera = Integer.parseInt(parametro.getDescri());

			TimerTask timerTask = new TareaCrearRideFacturaPdf(this.egreso, this.parametroRegis, this.dimmRegis,
					this.dataSource, this.variablesSesion, request);

			Timer timer = new Timer(true);

//	        timer.scheduleAtFixedRate (timerTask, 5*1000, 0); Tareas Repetitiva
			timer.schedule(timerTask, tiempoEspera * 1000);

//			cancel after sometime
//	        try {
//	            Thread.sleep(120000);
//	        } catch (InterruptedException e) {
//	            e.printStackTrace();
//			Thread.currentThread().interrupt();
//	        }
//	        timer.cancel();
//	        System.out.println("TimerTask cancelled");
//	        try {
//	            Thread.sleep(30000);
//			Thread.currentThread().interrupt();
//	        } catch (InterruptedException e) {
//	            e.printStackTrace();
//	        }

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error el crear RideFactura.pdf o buscar parametro 3203"));
			e.printStackTrace();
		}

	}

	public void ejecutarTareaAutorizar(File archivoFirmado) {

		try {

			manejadorTareaAutoriDocu.ejecutarTareaAutoriDocu(this.egreso, archivoFirmado, this.egreso.getSucursal(),
					this.egreso.getDocuEgre().getDimm().getCodigo(), this.egreso.getClaveAcce(),
					variablesSesion.getProxyIp(), variablesSesion.getProxyPuerto(),
					this.egreso.getDocuEgre().getAmbien(), variablesSesion.getUrlProduccion(),
					variablesSesion.getUrlPruebas(), "AutorizacionComprobantesOffline");

		} catch (ExecutionException | InterruptedException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Documento no autorizado por el SRI"));
			e.printStackTrace();
		}
	}

//	Se usa para mostrar el total de documentos electronicos que no se han autorizado
	public void consultarEgresosNoAutorizados() {

		LocalDate fechaDesde = LocalDate.now().minusDays(variablesSesion.getTiempoEmisionDocuElec());
		LocalDate fechaHasta = LocalDate.now();

		Set<Integer> sucursals = new HashSet<>();
		sucursals.add(variablesSesion.getSucursal().getSucursalId());

		Egreso egresoBuscar = new Egreso();

		DocuEgre docuEgre = new DocuEgre();
		docuEgre.setDocumeElec("Ninguno");

		egresoBuscar.setDocuEgre(docuEgre);
		egresoBuscar.setEstado("PR");
		egresoBuscar.setEstadoDocuElec("AUTORIZADO");
		egresoBuscar.setCajaMovi(cajaMoviAbrioSesion);

		try {
			contadorDocuNoAuto = venta.contarEgresosNoAutorizados(egresoBuscar, fechaDesde, fechaHasta, sucursals);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void ejecutarTareaEnviarCorreoDocu(String destinatario, String claveAcce) {

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
					"Excepcion - Documento no enviado por correo al cliente " + destinatario));
			e.printStackTrace();
		}
	}

	public void saldoCliente() {

		BigDecimal sumaCxc = new BigDecimal(0);
		BigDecimal sumaCobrDeta = new BigDecimal(0);

		sumaCxc = this.sumarCxc();
		sumaCobrDeta = this.sumarCobrDeta();

		if (sumaCxc == null) {
			sumaCxc = new BigDecimal(0);
		}

		if (sumaCobrDeta == null) {
			sumaCobrDeta = new BigDecimal(0);
		}

		this.saldoCliente = sumaCxc.subtract(sumaCobrDeta);
		this.saldoDisponible = this.egreso.getPersClie().getCupo().subtract(this.saldoCliente);

	}

	public void seleccionarCliente() {

		this.saldoCliente();

//		Limpia datos anteriores en caso de que el usario cambie de cliente
//		y haya estado ingresando una nota de credito
		this.cxcSele = new Cxc();
		this.egreso.setEgreso(null);
		this.cxcs.clear();

//		Asigna el cliente que selecciona el usuario al cliente de busqueda
//		para buscar las Cxc del cliente en caso que sea nota de credito
		this.cxcBuscar.getEgreso().setPersClie(this.egreso.getPersClie());

//		Asigna el descuento maximo del cliente que se este seleccionando
		this.egreso.setDescue(this.egreso.getPersClie().getDescueMaxi());

	}

	public BigDecimal sumarCxc() {

		BigDecimal sumaCxc = new BigDecimal(0);

		PersClie persClie = new PersClie();
		persClie.setPersona(this.egreso.getPersClie().getPersona());

		Cxc cxc = new Cxc();
		Egreso egreso = new Egreso();
		egreso.setPersClie(persClie);
		cxc.setEgreso(egreso);

		try {

			sumaCxc = venta.sumarCxc(cxc);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al sumar cuentas por cobrar"));
			e.printStackTrace();
		}

		return sumaCxc;
	}

	public BigDecimal sumarCobrDeta() {

		BigDecimal sumaCobrDeta = new BigDecimal(0);

		PersClie persClie = new PersClie();
		Egreso egreso = new Egreso();
		Cxc cxc = new Cxc();
		CobrDeta cobrDeta = new CobrDeta();

		persClie.setPersona(this.egreso.getPersClie().getPersona());
		egreso.setPersClie(persClie);
		cxc.setEgreso(egreso);
		cobrDeta.setCxc(cxc);

		try {
			sumaCobrDeta = this.venta.sumarCobrDeta(cobrDeta);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al sumar cobros"));
			e.printStackTrace();
		}

		return sumaCobrDeta;
	}

	private boolean botonAnular;

	public boolean isBotonAnular() {
		return botonAnular;
	}

	public void setBotonAnular(boolean botonAnular) {
		this.botonAnular = botonAnular;
	}

	public void activarBotonAnular() {

		if (variablesSesion.isAnulaFactConsFina()) {
			botonAnular = true;
			return;
		}

		LocalDate fechaLimite = egreso.getFechaEmis().plusMonths(1)
				.withDayOfMonth(variablesSesion.getDiasAnulacionDocuElec());

		if (LocalDate.now().isAfter(fechaLimite) && egreso.getEstadoDocuElec().equals("AUTORIZADO")) {
			botonAnular = false;
		} else if (egreso.getPersClie().getPersona().getCedulaRuc().equals("9999999999999")
				&& egreso.getEstadoDocuElec().equals("AUTORIZADO")) {
			botonAnular = false;
		} else {
			botonAnular = true;
		}
	}

	// <<<<<<<<<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>>>>>>>>

	public int getFilasProductos() {
		return variablesSesion.getFilasProductosEgreso();
	}

	public int getFilasClientes() {
		return variablesSesion.getFilasClientesEgreso();
	}

	public int getColumnasProductos() {
		return variablesSesion.getColumnasProductos();
	}

	public boolean isActivarConsultaPrecio() {
		return variablesSesion.isActivarConsultaPrecio();
	}

	public boolean isActivarCodigoBarra() {
		return variablesSesion.isActivarCodigoBarra();
	}

	public boolean isActivarDescri() {
		return variablesSesion.isActivarDescri();
	}

	public boolean isActivarPrecio() {
		return variablesSesion.isActivarPrecio();
	}

	public boolean isActivarImagen() {
		return variablesSesion.isActivarImagen();
	}

	public boolean isActivarMesa() {
		return variablesSesion.isActivarMesa();
	}

	public Integer getProductoId() {
		return productoId;
	}

	public void setProductoId(Integer productoId) {
		this.productoId = productoId;
	}

	public List<DocuEgre> getDocuEgres() {
		return docuEgres;
	}

	public void setDocuEgres(List<DocuEgre> docuEgres) {
		this.docuEgres = docuEgres;
	}

	public List<PersClie> getPersClies() {
		return persClies;
	}

	public void setPersClies(List<PersClie> persClies) {
		this.persClies = persClies;
	}

	public List<Dimm> getDimms() {
		return dimms;
	}

	public void setDimms(List<Dimm> dimms) {
		this.dimms = dimms;
	}

	public Egreso getEgreso() {
		return egreso;
	}

	public void setEgreso(Egreso egreso) {
		this.egreso = egreso;
	}

	public Integer getEgresoId() {
		return egresoId;
	}

	public void setEgresoId(Integer egresoId) {
		this.egresoId = egresoId;
	}

	public List<EgreDeta> getEgreDetaDataTable() {
		return egreDetaDataTable;
	}

	public void setEgreDetaDataTable(List<EgreDeta> egreDetaDataTable) {
		this.egreDetaDataTable = egreDetaDataTable;
	}

	public Conversation getConversation() {
		return conversation;
	}

	public EgreDeta getEgreDetaSele() {
		return egreDetaSele;
	}

	public void setEgreDetaSele(EgreDeta egreDetaSele) {
		this.egreDetaSele = egreDetaSele;
	}

	public List<Egreso> getEgresos() {
		return egresos;
	}

	public void setEgresos(List<Egreso> egresos) {
		this.egresos = egresos;
	}

	public List<PersVend> getPersVends() {
		return persVends;
	}

	public void setPersVends(List<PersVend> persVends) {
		this.persVends = persVends;
	}

	public List<Precio> getPrecios() {
		return precios;
	}

	public void setPrecios(List<Precio> precios) {
		this.precios = precios;
	}

//	public Precio getPrecio() {
//		return precio;
//	}
//
//	public void setPrecio(Precio precio) {
//		this.precio = precio;
//	}

	public int getFactor() {
		return factor;
	}

	public void setFactor(int factor) {
		this.factor = factor;
	}

	public List<RolDocu> getRolDocus() {
		return rolDocus;
	}

	public void setRolDocus(List<RolDocu> rolDocus) {
		this.rolDocus = rolDocus;
	}

	public PersClie getPersClie() {
		return persClie;
	}

	public void setPersClie(PersClie persClie) {
		this.persClie = persClie;
	}

	public Integer getPaginaClie() {
		return paginaClie;
	}

	public void setPaginaClie(Integer paginaClie) {
		this.paginaClie = paginaClie;
	}

	public long getContadorRegClie() {
		return contadorRegClie;
	}

	public void setContadorRegClie(long contadorRegClie) {
		this.contadorRegClie = contadorRegClie;
	}

	public int getNumeroRegClie() {
		return numeroRegClie;
	}

	public void setNumeroRegClie(int numeroRegClie) {
		this.numeroRegClie = numeroRegClie;
	}

	public FormPagoMoviEgre getFormPagoMoviEgre() {
		return formPagoMoviEgre;
	}

	public void setFormPagoMoviEgre(FormPagoMoviEgre formPagoMoviEgre) {
		this.formPagoMoviEgre = formPagoMoviEgre;
	}

	public List<FormPago> getFormPagos() {
		return formPagos;
	}

	public void setFormPagos(List<FormPago> formPagos) {
		this.formPagos = formPagos;
	}

	public List<PersCobr> getPersCobrs() {
		return persCobrs;
	}

	public void setPersCobrs(List<PersCobr> persCobrs) {
		this.persCobrs = persCobrs;
	}

	public List<DocuMoviEgre> getDocuMoviEgres() {
		return docuMoviEgres;
	}

	public void setDocuMoviEgres(List<DocuMoviEgre> docuMoviEgres) {
		this.docuMoviEgres = docuMoviEgres;
	}

	public BigDecimal getCambio() {
		return cambio;
	}

	public void setCambio(BigDecimal cambio) {
		this.cambio = cambio;
	}

	public Integer getIdDescargar() {
		return idDescargar;
	}

	public void setIdDescargar(Integer idDescargar) {
		this.idDescargar = idDescargar;
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

	public DocuEgre getDocuEgreConvertir() {
		return docuEgreConvertir;
	}

	public void setDocuEgreConvertir(DocuEgre docuEgreConvertir) {
		this.docuEgreConvertir = docuEgreConvertir;
	}

	public List<RolSucu> getRolSucuConvertirs() {
		return rolSucuConvertirs;
	}

	public void setRolSucuConvertirs(List<RolSucu> rolSucuConvertirs) {
		this.rolSucuConvertirs = rolSucuConvertirs;
	}

	public RolSucu getRolSucuConvertir() {
		return rolSucuConvertir;
	}

	public void setRolSucuConvertir(RolSucu rolSucuConvertir) {
		this.rolSucuConvertir = rolSucuConvertir;
	}

	public List<CajaMovi> getCajaMoviConvertirs() {
		return cajaMoviConvertirs;
	}

	public void setCajaMoviConvertirs(List<CajaMovi> cajaMoviConvertirs) {
		this.cajaMoviConvertirs = cajaMoviConvertirs;
	}

	public CajaMovi getCajaMoviConvertir() {
		return cajaMoviConvertir;
	}

	public void setCajaMoviConvertir(CajaMovi cajaMoviConvertir) {
		this.cajaMoviConvertir = cajaMoviConvertir;
	}

	public List<ProdPrec> getProdPrecs() {
		return prodPrecs;
	}

	public void setProdPrecs(List<ProdPrec> prodPrecs) {
		this.prodPrecs = prodPrecs;
	}

	public List<ProdPrec> getProdPrecSeles() {
		return prodPrecSeles;
	}

	public void setProdPrecSeles(List<ProdPrec> prodPrecSeles) {
		this.prodPrecSeles = prodPrecSeles;
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

	public String getVentaDetaAlCargar() {
		return ventaDetaAlCargar;
	}

	public void setVentaDetaAlCargar(String ventaDetaAlCargar) {
		this.ventaDetaAlCargar = ventaDetaAlCargar;
	}

	public String getVentaDetaAlSeleProd() {
		return ventaDetaAlSeleProd;
	}

	public void setVentaDetaAlSeleProd(String ventaDetaAlSeleProd) {
		this.ventaDetaAlSeleProd = ventaDetaAlSeleProd;
	}

	public Integer getOrigenPrecio() {
		return origenPrecio;
	}

	public void setOrigenPrecio(Integer origenPrecio) {
		this.origenPrecio = origenPrecio;
	}

	public BigDecimal getSaldoCliente() {
		return saldoCliente;
	}

	public void setSaldoCliente(BigDecimal saldoCliente) {
		this.saldoCliente = saldoCliente;
	}

	public List<Mesa> getMesas() {
		return mesas;
	}

	public void setMesas(List<Mesa> mesas) {
		this.mesas = mesas;
	}

	public long getContadorRegCxc() {
		return contadorRegCxc;
	}

	public void setContadorRegCxc(long contadorRegCxc) {
		this.contadorRegCxc = contadorRegCxc;
	}

	public int getNumeroRegCxc() {
		return numeroRegCxc;
	}

	public void setNumeroRegCxc(int numeroRegCxc) {
		this.numeroRegCxc = numeroRegCxc;
	}

	public Integer getPaginaCxc() {
		return paginaCxc;
	}

	public void setPaginaCxc(Integer paginaCxc) {
		this.paginaCxc = paginaCxc;
	}

	public Cxc getCxcSele() {
		return cxcSele;
	}

	public void setCxcSele(Cxc cxcSele) {
		this.cxcSele = cxcSele;
	}

	public List<Cxc> getCxcs() {
		return cxcs;
	}

	public void setCxcs(List<Cxc> cxcs) {
		this.cxcs = cxcs;
	}

	public Cxc getCxcBuscar() {
		return cxcBuscar;
	}

	public void setCxcBuscar(Cxc cxcBuscar) {
		this.cxcBuscar = cxcBuscar;
	}

//	public Date getFechaCobro() {
//		return fechaCobro;
//	}
//
//	public void setFechaCobro(Date fechaCobro) {
//		this.fechaCobro = fechaCobro;
//	}

	public List<Parametro> getParametros() {
		return parametros;
	}

	public void setParametros(List<Parametro> parametros) {
		this.parametros = parametros;
	}

	public BigDecimal getTotalDocumento() {

		this.totalDocumento = BigDecimal.ZERO;

		for (TotalDocu totalDocu : this.totalDocus) {
			if (totalDocu.getDescri().toLowerCase().equals("total documento")) {

				this.totalDocumento = totalDocu.getValor();
			}
		}
		return totalDocumento;
	}

	public void setTotalDocumento(BigDecimal totalDocumento) {
		this.totalDocumento = totalDocumento;
	}

	public Producto getProductoDialogo() {
		return productoDialogo;
	}

	public void setProductoDialogo(Producto productoDialogo) {
		this.productoDialogo = productoDialogo;
	}

	public List<ClieGrup> getClieGrups() {
		return clieGrups;
	}

	public void setClieGrups(List<ClieGrup> clieGrups) {
		this.clieGrups = clieGrups;
	}

	public List<Dimm> getDimmTipoIdens() {
		return dimmTipoIdens;
	}

	public void setDimmTipoIdens(List<Dimm> dimmTipoIdens) {
		this.dimmTipoIdens = dimmTipoIdens;
	}

	public List<Dimm> getDimmTipoIdenClies() {
		return dimmTipoIdenClies;
	}

	public void setDimmTipoIdenClies(List<Dimm> dimmTipoIdenClies) {
		this.dimmTipoIdenClies = dimmTipoIdenClies;
	}

	public PersClie getPersClieRegis() {
		return persClieRegis;
	}

	public void setPersClieRegis(PersClie persClieRegis) {
		this.persClieRegis = persClieRegis;
	}

	public CajaMovi getCajaMoviAbrioSesion() {
		return cajaMoviAbrioSesion;
	}

	public void setCajaMoviAbrioSesion(CajaMovi cajaMoviAbrioSesion) {
		this.cajaMoviAbrioSesion = cajaMoviAbrioSesion;
	}

	public List<FpmeFormPago> getFpmeFormPagos() {
		return fpmeFormPagos;
	}

	public void setFpmeFormPagos(List<FpmeFormPago> fpmeFormPagos) {
		this.fpmeFormPagos = fpmeFormPagos;
	}

	public FpmeFormPago getFpmeFormPagoSele() {
		return fpmeFormPagoSele;
	}

	public void setFpmeFormPagoSele(FpmeFormPago fpmeFormPagoSele) {
		this.fpmeFormPagoSele = fpmeFormPagoSele;
	}

	public BigDecimal getSaldoDisponible() {
		return saldoDisponible;
	}

	public void setSaldoDisponible(BigDecimal saldoDisponible) {
		this.saldoDisponible = saldoDisponible;
	}

//	Buscar los subproductos para copiar en egreDeta
	public List<ProdSubp> buscarProdSubps(ProdSubp prodSubp) {

		List<ProdSubp> prodSubps = new ArrayList<ProdSubp>();
		try {

			prodSubps = venta.buscarProdSubps(prodSubp, null);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar SubProductos"));
			e.printStackTrace();
		}

		return prodSubps;
	}

	public void validarRefere(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException {

		String refere = (String) arg2;

		if (refere != null) {
			List<Object[]> objs = new ArrayList<>();
//			Busca si se esta ingresando una referencia duplicada
			try {
				objs = this.venta.buscarFpmeRefere(refere);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al buscar referencia de documento"));
				e.printStackTrace();
			}

			if (!objs.isEmpty()) {
				for (Object[] object : objs) {
					throw new ValidatorException(new FacesMessage("Referencia " + refere
							+ " ya existe en modulo de cobros con el Número " + object[0] + " Fecha " + object[1]));
				}
			}
		}
	}

	public void crearFilaEgreNota() {

		EgreNota egreNota = new EgreNota();

		egreNota.setEgreso(this.egreso);

		this.egreNotas.add(egreNota);
	}

	public void eliminarFilaEgreNota() {
		this.egreNotas.remove(this.egreNotaSele);
	}

	public void grabarEgreNotas() {

		for (EgreNota egreNota : this.egreNotas) {
			try {
				egreNotaRegis.insertar(egreNota);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
						"Error al grabar información adicional (EgreNota)"));
				e.printStackTrace();
			}
		}
	}

//	Se llama desde la pagina
	public void buscarEgreNotas() {

		if (!this.egreso.getEstado().equals("GR")) {
			EgreNota egreNota = new EgreNota(this.egreso);
			this.egreNotas = this.buscarEgreNotas(egreNota);
		}
	}

//	Se puede llamar desde cualquier lugar
	public List<EgreNota> buscarEgreNotas(EgreNota egreNota) {

		List<EgreNota> egreNotas = new ArrayList<>();

		try {
			egreNotas = egreNotaLista.buscar(egreNota, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Error al buscar información adicional (EgreNotas)"));
			e.printStackTrace();
		}

		return egreNotas;

	}

	public EgreNota getEgreNotaSele() {
		return egreNotaSele;
	}

	public void setEgreNotaSele(EgreNota egreNotaSele) {
		this.egreNotaSele = egreNotaSele;
	}

	public List<EgreNota> getEgreNotas() {
		return egreNotas;
	}

	public void setEgreNotas(List<EgreNota> egreNotas) {
		this.egreNotas = egreNotas;
	}

	public Boolean getGrabarImprimirEstado() {
		return grabarImprimirEstado;
	}

	public void setGrabarImprimirEstado(Boolean grabarImprimirEstado) {
		this.grabarImprimirEstado = grabarImprimirEstado;
	}
//	Busqueda recursiva en egreDeta
//	public List<EgreDeta> buscarEgreDetaSubProductos (EgreDeta egreDeta) {
//		
//		List<EgreDeta> egreDetas = new ArrayList<>();
//		
//		try {
//			egreDetas = venta.buscarEgreDetas(egreDeta, null);
//		} catch (Exception e) {
//			FacesContext.getCurrentInstance().addMessage(null,
//					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepción al buscar sub-productos recursivos en detalle documento"));
//			e.printStackTrace();
//		}
//		
//		return egreDetas;
//		
//	}

	public Boolean grabarImprVerificarEstado() {

		Boolean grabarImpr = false;

		RolDocu rolDocu = new RolDocu();
		try {

			if (this.egreso.getEstado().equals("GR")) {

				rolDocu = rolDocuRegis.buscarRolDocuPermisos(variablesSesion.getRolDocus(),
						this.egreso.getDocuEgre().getDocumento(), variablesSesion.getPersUsua());

				grabarImpr = rolDocu.getGrabarImpr();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al descargar o imprimir documento"));
			e.printStackTrace();
			return false;
		}
		return grabarImpr;
	}

	public long getContadorDocuNoAuto() {
		return contadorDocuNoAuto;
	}

	public void setContadorDocuNoAuto(long contadorDocuNoAuto) {
		this.contadorDocuNoAuto = contadorDocuNoAuto;
	}
}
