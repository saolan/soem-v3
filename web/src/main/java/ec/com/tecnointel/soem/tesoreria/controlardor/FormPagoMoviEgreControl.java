package ec.com.tecnointel.soem.tesoreria.controlardor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.TransformerUtils;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;

import ec.com.saolan.soem.sri.infraestructura.aplicacion.retencion.RetencionServicio;
import ec.com.saolan.soem.sri.infraestructura.aplicacion.retencion.RetencionSriServicio;
import ec.com.tecnointel.soem.caja.listaInt.CajaMoviListaInt;
import ec.com.tecnointel.soem.caja.modelo.Caja;
import ec.com.tecnointel.soem.caja.modelo.CajaMovi;
import ec.com.tecnointel.soem.contabilidad.modelo.Transaccion;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionFpmeInt;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionGestionInt;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionRegisInt;
import ec.com.tecnointel.soem.egreso.listaInt.PersClieListaInt;
import ec.com.tecnointel.soem.egreso.listaInt.PersCobrListaInt;
import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.egreso.modelo.PersClie;
import ec.com.tecnointel.soem.egreso.modelo.PersCobr;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.ingreso.listaInt.RetencionListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.ReteDeta;
import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import ec.com.tecnointel.soem.ingreso.registroInt.ReteDetaRegisInt;
import ec.com.tecnointel.soem.ingreso.registroInt.RetencionRegisInt;
import ec.com.tecnointel.soem.parametro.listaInt.DimmListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.DocuMoviEgreListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.FormPagoListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.TranPlanDetaListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.TranPlanListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.DocuCaja;
import ec.com.tecnointel.soem.parametro.modelo.DocuMoviEgre;
import ec.com.tecnointel.soem.parametro.modelo.DocuTran;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.modelo.TranPlan;
import ec.com.tecnointel.soem.parametro.modelo.TranPlanDeta;
import ec.com.tecnointel.soem.parametro.registroInt.DocumentoRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.FormPagoRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.TranPlanRegisInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolDocu;
import ec.com.tecnointel.soem.tesoreria.listaInt.CobrDetaListaInt;
import ec.com.tecnointel.soem.tesoreria.listaInt.CxcListaInt;
import ec.com.tecnointel.soem.tesoreria.listaInt.FormPagoMoviEgreListaInt;
import ec.com.tecnointel.soem.tesoreria.listaInt.FormPagoMoviIngrListaInt;
import ec.com.tecnointel.soem.tesoreria.listaInt.FpmeFormPagoListaInt;
import ec.com.tecnointel.soem.tesoreria.modelo.CobrDeta;
import ec.com.tecnointel.soem.tesoreria.modelo.Cxc;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviEgre;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import ec.com.tecnointel.soem.tesoreria.modelo.FpmeFormPago;
import ec.com.tecnointel.soem.tesoreria.registroInt.CobrDetaRegisInt;
import ec.com.tecnointel.soem.tesoreria.registroInt.CxcRegisInt;
import ec.com.tecnointel.soem.tesoreria.registroInt.FormPagoMoviEgreRegisInt;
import ec.com.tecnointel.soem.tesoreria.registroInt.FormPagoMoviIngrRegisInt;
import ec.com.tecnointel.soem.tesoreria.registroInt.FpmeFormPagoRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class FormPagoMoviEgreControl extends PaginaControl implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(FormPagoMoviEgreControl.class.getName());

	public static final String TIPO2_VN_RR = "VN-RR";
	public static final String TIPO2_VN_RI = "VN-RI";

	private Integer egresoId;
	private Integer paginaClie;

	private long contadorRegClie;
	private int numeroRegClie;

	private Integer cxcId;

	private BigDecimal saldoCliente = new BigDecimal(0);

	private boolean incluirCredito;

	private Cxc cxcSele = new Cxc();
	private Persona personaBuscar;
	private PersUsua persUsuaSesion;

	private CajaMovi cajaMovi;
	private FormPagoMoviEgre formPagoMoviEgre;
	private FpmeFormPago fpmeFormPagoSele;
	private FormPago formPagoPred;

	private TranPlan tranPlan;

	private List<FormPagoMoviEgre> formPagoMoviEgres;

	private List<TranPlan> tranPlans;
	private List<TranPlan> tranPlanTmps;
	private List<TranPlanDeta> tranPlanDetas;

	private Set<Cxc> cxcSeles = new HashSet<>();

	// Combos
	private List<Persona> personas;
	private List<PersCobr> persCobrs;
	private List<PersClie> persClies;
	private List<FormPago> formPagos;
	private List<FormPago> formPagoTmps;
	private List<DocuMoviEgre> docuMoviEgres;

	// Detalles
	private List<CobrDeta> cobrDetas;
	private List<Cxc> cxcs;

	private List<FpmeFormPago> fpmeFormPagos;

	@Inject
	CajaMoviListaInt cajaMoviLista;

	@Inject
	FormPagoMoviEgreListaInt formPagoMoviEgreLista;

	@Inject
	FormPagoMoviEgreRegisInt formPagoMoviEgreRegis;

	@Inject
	DocuMoviEgreListaInt docuMoviEgreLista;

	@Inject
	DocumentoRegisInt documentoRegis;

	@Inject
	FormPagoListaInt formPagoLista;

	@Inject
	FpmeFormPagoListaInt fpmeFormPagoLista;

	@Inject
	FpmeFormPagoRegisInt fpmeFormPagoRegis;

	@Inject
	CobrDetaListaInt cobrDetaLista;

	@Inject
	CobrDetaRegisInt cobrDetaRegis;

	@Inject
	CxcListaInt cxcLista;

	@Inject
	CxcRegisInt cxcRegis;

	@Inject
	PersCobrListaInt persCobrLista;

	@Inject
	PersClieListaInt persClieLista;

	@Inject
	TranPlanListaInt tranPlanLista;

	@Inject
	TranPlanRegisInt tranPlanRegis;

	@Inject
	TranPlanDetaListaInt tranPlanDetaLista;

	@Inject
	TransaccionFpmeInt transaccionFpme;

	@Inject
	TransaccionGestionInt transaccionGestion;

	@Inject
	TransaccionRegisInt transaccionRegis;

	@Inject
	FormPagoMoviIngrListaInt formPagoMoviIngrLista;

	@Inject
	FormPagoMoviIngrRegisInt formPagoMoviIngrRegis;

	@Inject
	FormPagoRegisInt formPagoRegis;

	@Inject
	RetencionRegisInt retencionRegis;

	// Implementacion carga retenciones
	private boolean tieneRetencion;
	private boolean existeRetencionNumeroAutorizacion;

	public static final String TABLA_RETEN_RENTA = "Tabla3";
	public static final String TABLA_RETEN_IVA = "Tabla11";

	public static final String IMPUESTO_RENTA = "Renta";
	public static final String IMPUESTO_IVA = "Iva";

	public static final String ESTADO_PR = "PR";
	public static final String ESTADO_DOC_ELEC_AUTORIZADO = "AUTORIZADO";

	BigDecimal retencionTotal = new BigDecimal(0);

	private Retencion retencion = new Retencion();
	private ReteDeta reteDetaSele = new ReteDeta();

	private List<Dimm> dimmRetencionRentas;
	private List<Dimm> dimmRetencionIvas;

	@Inject
	RetencionServicio retencionServicio;

	@Inject
	RetencionListaInt retencionLista;

	@Inject
	ReteDetaRegisInt reteDetaRegis;

	@Inject
	DimmListaInt dimmLista;

	private List<ReteDeta> reteDetas = new ArrayList<ReteDeta>();

	@PostConstruct
	public void cargar() {

		this.persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");

		this.tranPlan = new TranPlan();

		personaBuscar = new Persona();

		this.cobrDetas = new ArrayList<CobrDeta>();

		this.paginaClie = 0;
		// Busca el movimiento de caja del usuario
		// para activar el boton nuevo y para grabar cajaMoviId en formPagoMoviEgre
		this.buscarSesionVentas();

		formPagoMoviEgre = new FormPagoMoviEgre();
		formPagoMoviEgre.setPersona(new Persona());

//		Date fecha = Util.cambiarFormatoFecha(new Date(), "dd-MM-yyyy");
//		formPagoMoviEgre.setFecha(fecha);
		formPagoMoviEgre.setFecha(LocalDate.now());

		this.fpmeFormPagos = new ArrayList<FpmeFormPago>();

		this.cxcs = new ArrayList<>();
		this.formPagos = new ArrayList<>();
		this.tranPlans = new ArrayList<>();

		cargarDimmRetenciones();
	}

	public String anular() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		List<Cxc> cxcs = new ArrayList<>();

		try {
			// En el metodo recuperar ya se asignan los registros a las listas
			// para poder eliminar y modifica
			for (CobrDeta cobrDeta : this.cobrDetas) {

				cxcs.add(cobrDeta.getCxc());
				cobrDetaRegis.eliminar(cobrDeta);
			}

			// Se cambia el estado a false de todas las CXP que se este anulando
			// ya que si se elimina un pago la cxp siempre estara pendiente
			// y si todavia estaba pendiente no afecta en nada
			for (Cxc cxc : cxcs) {
				cxc.setEstado(false);
				cxcRegis.modificar(cxc);
			}

			Retencion retencionEliminar = null;
			for (FpmeFormPago fpmeFormPago : this.fpmeFormPagos) {

				ReteDeta reteDeta = fpmeFormPago.getReteDeta();
				if (reteDeta != null) {
//			        Guardar referencia antes de romper relaciones para luego eliminar
					Retencion retencion = reteDeta.getRetencion();
					if (retencion != null) {
						retencionEliminar = reteDeta.getRetencion();
					}

//			        Eliminar relaciones
					fpmeFormPago.setReteDeta(null);
					reteDeta.setFpmeFormPago(null);

					fpmeFormPagoRegis.modificar(fpmeFormPago);
					reteDetaRegis.eliminar(reteDeta);
				}
				fpmeFormPagoRegis.eliminar(fpmeFormPago);
			}

			if (retencionEliminar != null) {
				retencionRegis.eliminar(retencionEliminar);
			}

			FormPagoMoviEgre fpme = formPagoMoviEgreRegis.buscarPorId(FormPagoMoviEgre.class, this.getId());
			fpme.setEstado("AN");

			formPagoMoviEgreRegis.modificar(fpme);

			if (fpme.getTransaccion() != null) {

//				Anula registro en el caso que el documento se haya cobrado con deposito
				anularFpmi(fpme);

//				Anular transaccion del cobro
				try {
					this.transaccionGestion.anularTransaccion(fpme.getTransaccion().getTransaccionId());

					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							null, "Transacción anulada No. " + fpme.getTransaccion().getNumero()));

				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
							null, "Excepcion - Error al anular transación de cobro del documento"));
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al anular registro"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro anulado"));

		return "lista?faces-redirect=true";
	}

//	Anula registro en el caso que el documento se haya cobrado con deposito
//	Al cobrar con deposito se genera un registro en formPagoMoviEgre que es
//	el que resta la cxc y un registro en formPagoMoviIngr que es el que genera
//	el movimiento de bancos. Entonces al anular el egreso
//	se buscar los movimientos que tengan la misma transaccion y se los anula
	public void anularFpmi(FormPagoMoviEgre fpme) {

		List<FormPagoMoviIngr> fpmis = buscarFpmi(fpme);

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

//	Anula fpmi en caso que el cobro se haya hecho con deposito
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

	public BigDecimal calcularCambio() {

		BigDecimal totalReci = new BigDecimal(0);

		totalReci = this.calcularTotalReci();

		return this.formPagoMoviEgre.getTotal().subtract(totalReci);
	}

	public void calcularDiasPlaz() {

		for (FpmeFormPago fpmeFormPago : this.fpmeFormPagos) {

			fpmeFormPago.setDiasPlaz(
					(short) ChronoUnit.DAYS.between(fpmeFormPago.getFecha(), this.formPagoMoviEgre.getFecha()));
		}
	}

	public BigDecimal calcularTotalReci() {
		BigDecimal totalReci = new BigDecimal(0);

		for (FpmeFormPago fpmeFormPago : this.fpmeFormPagos) {
			totalReci = totalReci.add(fpmeFormPago.getTotalReci());
		}

		return totalReci;
	}

	public void calcularTotalReciView() {

		BigDecimal totalReci = new BigDecimal(0);

		totalReci = this.calcularTotalReci();
		this.formPagoMoviEgre.setTotalReci(totalReci);
	}

	public Integer contabilizarFpme(FormPagoMoviEgre formPagoMoviEgre) {

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

			if (formPagoMoviEgre.getDocuMoviEgre().getTipo().equals("PAGO-COBRO")) {

				transaccionId = contabilizarCobro(formPagoMoviEgre);

			} else if (formPagoMoviEgre.getDocuMoviEgre().getTipo().equals("ANTICIPO")) {

				transaccionId = contabilizarAnticipo(formPagoMoviEgre);

			} else if (this.formPagoMoviEgre.getDocuMoviEgre().getTipo().equals("DEPOSITO")) {

				transaccionId = contabilizarCobro(formPagoMoviEgre);

				Set<FpmeFormPago> fpmeFps = new HashSet<>(fpmeFormPagos);
				formPagoMoviEgre.setFpmeFormPagos(fpmeFps);

				List<FormPagoMoviIngr> fpmis = crearFpmiDepositos(formPagoMoviEgre);
				insertarFpmiDepositos(fpmis);

			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, null,
					"Transacción contable por el cobro del documento no se ha generado"));
			e.printStackTrace();
		}
		return transaccionId;
	}

	public Integer contabilizarCobro(FormPagoMoviEgre formPagoMoviEgre) {

		Integer transaccionId = 0;

		try {

			transaccionId = transaccionFpme.contabilizarCobro(formPagoMoviEgre);

			if (transaccionId != 0) {

				Transaccion transaccion = new Transaccion();
				transaccion.setTransaccionId(transaccionId);

				formPagoMoviEgre.setTransaccion(transaccion);
				try {
					formPagoMoviEgreRegis.modificar(this.formPagoMoviEgre);
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
							null, "Excepcion - No se ha registrado el Id de la transaccion en pago"));
					e.printStackTrace();
					return null;
				}
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, null,
					"Transacción contable del cobro no se ha generado"));
			e.printStackTrace();
		}

		return transaccionId;
	}

	public Integer contabilizarAnticipo(FormPagoMoviEgre formPagoMoviEgre) {

		Integer transaccionId = 0;

		try {

			transaccionId = transaccionFpme.contabilizarAnticipo(formPagoMoviEgre);

			if (transaccionId != 0) {

				Transaccion transaccion = new Transaccion();
				transaccion.setTransaccionId(transaccionId);

				formPagoMoviEgre.setTransaccion(transaccion);
				try {
					formPagoMoviEgreRegis.modificar(this.formPagoMoviEgre);
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
							null, "Excepcion - No se ha registrado el Id de la transaccion en anticipo"));
					e.printStackTrace();
					return null;
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, null,
					"Transacción contable del anticipo no se ha generado"));
			e.printStackTrace();
		}

		return transaccionId;
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

	public void calcularFechaCobro() {

		for (FpmeFormPago fpmeFormPago : this.fpmeFormPagos) {

//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(this.formPagoMoviEgre.getFecha());
//
//			if (fpmeFormPago.getDiasPlaz() == 30 && fpmeFormPago.getDiasPlaz() == 31) {
//				calendar.add(Calendar.MONTH, 1);
//			} else {
//				calendar.add(Calendar.DATE, fpmeFormPago.getDiasPlaz());
//			}

			fpmeFormPago.setFecha(fpmeFormPago.getFecha().plusDays(fpmeFormPago.getDiasPlaz()));
		}
	}

	public void descargar() {

		if (id == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Error - Por favor primero grabe o procese el documento para poder descargarlo"));
			return;
		}

		Parametro parametro = new Parametro();
		// Nombre que esta en archivo fuente en jasperReport
		String nombreReporte = "";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		nombreReporte = this.formPagoMoviEgre.getDocuMoviEgre().getDocumento().getFormat();

		// Se coloca aqui la ruta para poder tener los reportes en diferentes y varias
		// carpetas
		// Sino se podria poner en la clase GenerarJasperReport
		String rutaJrxml; // = "C:\\tecnoIntel\\soem\\jasperReportes\\ingreso\\";
		// En esta ruta se compila el reporte dentro del servidor de aplicaciones dentro
		// del archivo .EAR .WEB
		String rutaReporteCompilado = "\\jasperReportes\\tesoreria\\";

		try {

			Integer fpmeId = this.formPagoMoviEgre.getFpmeId();

			if (fpmeId == null) {
				parametrosJasper.put("fpmeId", 0);
			} else {
				parametrosJasper.put("fpmeId", fpmeId);
			}

			parametro = parametroRegis.buscarPorId(Parametro.class, 8000);
			rutaJrxml = parametro.getDescri();

			parametrosJasper.put("rutaJrxml", rutaJrxml);

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper, rutaJrxml, rutaReporteCompilado,
					formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al descargar documento"));
			e.printStackTrace();
		}
	}

	public void filtrarFormPagoVentas() {

//		Se coloca para que eliminar la persona en el caso de que escogio un documento y luego otro
//		asi se obliga a ingresar nuevamente el cliente
		this.formPagoMoviEgre.setPersona(null);

		this.cxcs.clear();
		this.cxcSeles.clear();

		try {
			this.formPagos = formPagoLista.filtrarFormPagoVentas(formPagoMoviEgre, formPagoTmps, persUsuaSesion,
					variablesSesion.getRolFormPagos());
		} catch (Exception e) {

//			Si el usario no tiene acceso al documento predeterminado da este error ya que el filtro de formas de pago
//			se hace de acuerdo al documento seleccionado o predeterminado 
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, null,
					"Excepcion - Error al filtrar formas de pago, No tiene acceso a documento predeterminado"));
			e.printStackTrace();
		}
	}

//	Llama desde la pagina aqui todavia no esta implementado
	public void filtrarTranPlans() {

		this.tranPlans.clear();

		if (this.formPagoMoviEgre.getDocuMoviEgre().getTipo().equals("DEPOSITO")
				|| this.formPagoMoviEgre.getDocuMoviEgre().getTipo().equals("ANTICIPO")) {

//			Deja en la lista solamente la opcion ninguno para que el usuario no pueda seleccionar ningun elemento 
//			porque el anticipo y deposito se genera contra la cuenta de anticipo del proveedor y caja 
//			this.formPagoMoviIngr.setTranPlanId(0);

			for (TranPlan tranPlan : this.tranPlanTmps) {
				if (tranPlan.getTipo().equals("NINGUNO")) {
					this.tranPlans.add(tranPlan);
				}
			}
		} else if (this.formPagoMoviEgre.getDocuMoviEgre().getTipo().equals("PAGO-COBRO")) {

			for (TranPlan tranPlan : this.tranPlanTmps) {
				if (!tranPlan.getTipo().equals("NINGUNO")) {
					this.tranPlans.add(tranPlan);
				}
			}
		}
	}

	public void recuperar() {

		FpmeFormPago fpmeFormPagoBuscar = new FpmeFormPago();

		DocuMoviEgre docuMoviEgrePred;

		List<RolDocu> rolDocus = variablesSesion.getRolDocus();
		List<DocuMoviEgre> docuMoviEgres = new ArrayList<>();

//		List<FpmeFormPago> fpmeFormPagos = new ArrayList<FpmeFormPago>();

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback()) {
			return;
		}

		this.persCobrs = this.buscarPersCobrs();

		docuMoviEgres = this.buscarDocuMoviEgres();
		this.tranPlanTmps = this.buscarTranPlans();

		try {
			this.docuMoviEgres = this.docuMoviEgreLista.filtrarDocuMoviEgres(docuMoviEgres, this.persUsuaSesion,
					rolDocus);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al filtar documentos de pago"));
			e.printStackTrace();
		}

		try {
			docuMoviEgrePred = this.docuMoviEgreLista.buscarDocuMoviEgrePred(this.docuMoviEgres, rolDocus);
			this.formPagoMoviEgre.setDocuMoviEgre(docuMoviEgrePred);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar documento predeterminado"));
			e.printStackTrace();
		}

		this.formPagoTmps = this.buscarFormPagos();
		this.filtrarFormPagoVentas();
//		this.filtrarFormPagos();

		if (this.id == null) {

//			Tomando en cuenta que como predeterminado este el documento COBROS, 
//			se utiliza para poner el valor predeterminado en el campo tranPlanId
//			ya que luego se ejecuta la linea this.buscarTranPlan();
//			que es la que pone el documento predeterminado en el campo
//			En el caso de que este puesto como predeterminado otro documento no afecta en nada ya que el campo TranPlanId 
//			solo sirve de referencia para saber que transaccion se hace en el caso que sea otro pago diferente de CXC
			for (TranPlan tranPlan : tranPlanTmps) {
				if (tranPlan.getTipo().equals("CXC")) {
					this.formPagoMoviEgre.setTranPlanId(tranPlan.getTranPlanId());
					break;
				}
			}

			this.buscarTranPlan();
			// this.formPagoMoviEgre = new FormPagoMoviEgre();
			this.getFormPagoMoviEgre().setFecha(LocalDate.now());
			this.getFormPagoMoviEgre().setFechaHora(LocalDateTime.now());
			this.getFormPagoMoviEgre().setTotal(new BigDecimal(0));

		} else {

			try {
//				Se filtrar la forma de pago del documento que se esta recuperando,
//				ya que si no se hace esto no aparece la forma de pago
//				Se coloca antes de this.formPagoMoviEgre = formPagoMoviEgreRegis.buscarPorId(this.getId());
//				ya que al firtrar se pone null en Persona y al descargar el recibo de pago daria error
//				this.filtrarFormPagos();
				this.filtrarFormPagoVentas();

				this.formPagoMoviEgre = formPagoMoviEgreRegis.buscarPorId(FormPagoMoviEgre.class, this.getId());
				this.buscarCxcs(formPagoMoviEgre);

				fpmeFormPagoBuscar.setFormPagoMoviEgre(formPagoMoviEgre);
				this.fpmeFormPagos = this.buscarFpmeFormPagos(fpmeFormPagoBuscar);

				for (FpmeFormPago fpmeFormPago : this.fpmeFormPagos) {

					CobrDeta cobrDeta = new CobrDeta();

					fpmeFormPago.setFormPagoMoviEgre(this.formPagoMoviEgre);
					cobrDeta.setFpmeFormPago(fpmeFormPago);

					this.buscarCobrDetas(cobrDeta);
				}
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
				e.printStackTrace();
			}

//			Buscar y asignar transaccion si no tiene asignar una vacia
			if (this.formPagoMoviEgre.getTransaccion() != null) {

				Transaccion transaccion = this
						.buscarTransaccionPorId(this.formPagoMoviEgre.getTransaccion().getTransaccionId());
				this.formPagoMoviEgre.setTransaccion(transaccion);

			} else {

				DocuTran docuTran = new DocuTran();
				docuTran.setDocumento(new Documento());
				Transaccion transaccion = new Transaccion();
				transaccion.setDocuTran(docuTran);
				this.formPagoMoviEgre.setTransaccion(transaccion);

			}

		}

		// Esta parte se ejecuta cuando se abre la pagina desde la opcion pagar de la
		// pagina la compra la opcion pagar de la pagina de compra manda el parametro
		// ingresoId
		if (this.egresoId != null) {

			List<Cxc> cxcs = new ArrayList<>();

			cxcs = this.buscarCxcs(egresoId);

			this.formPagoMoviEgre.setPersona(cxcs.get(0).getEgreso().getPersClie().getPersona());
			this.formPagoMoviEgre.setTotal(cxcs.get(0).getTotal());
		}
	}

	public Transaccion buscarTransaccionPorId(Integer transaccionId) {

		Transaccion transaccion = new Transaccion();

		try {
			transaccion = transaccionRegis.buscarPorId(Transaccion.class, transaccionId);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar transacción"));
			e.printStackTrace();
		}

		return transaccion;
	}

	public void insertarFpmeFormPagos() {

		for (FpmeFormPago fpmeFormPago : this.fpmeFormPagos) {

			try {

				this.fpmeFormPagoRegis.insertar(fpmeFormPago);

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al grabar FpmeFormPagos"));
				e.printStackTrace();
			}
		}
	}

	public void validarRefere(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException {

		String refere = (String) arg2;

		if (refere != null) {
			List<Object[]> objs = new ArrayList<>();
//			Busca si se esta ingresando una referencia duplicada
			try {
				objs = this.formPagoMoviEgreRegis.buscarFpmeRefere(refere);
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

	public String procesar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		if (!validarAntesDeProcesar()) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, "No se ha grabado el cobro"));
			return null;
		}

		try {

			if (this.id == null) {

				// Sucursal sucursal = variablesSesion.getSucursal();
				formPagoMoviEgre.setSucursal(this.getVariablesSesion().getSucursal());

//				Graba Fecha y Hora
				formPagoMoviEgre.setFechaHora(formPagoMoviEgre.getFecha().atTime(LocalTime.now()));

				// Grabar el número de cobro
				formPagoMoviEgre.setNumero(this.formPagoMoviEgre.getDocuMoviEgre().getDocumento().getNumero() + 1);

				// Actualizar secuencial
				this.formPagoMoviEgre.getDocuMoviEgre().getDocumento().setNumero(this.formPagoMoviEgre.getNumero());
				documentoRegis.modificar((this.getFormPagoMoviEgre().getDocuMoviEgre().getDocumento()));
				// Fin Actualizar secuencial

				// Grabar el numero de inicio de sesion
				// para saber los cobros que ha hecho un cajero en una jornada
				// this.cajaMovi ya se busco en @PostConstructor
				formPagoMoviEgre.setCajaMoviId(this.cajaMovi.getCajaMoviId());

				formPagoMoviEgre.setEstado("PR");

				Object id = formPagoMoviEgreRegis.insertar(formPagoMoviEgre);
				this.id = (Integer) id;

				this.grabarRetencion();

				this.insertarFpmeFormPagos();

//				Si selecciono mas de una CXC solo puede tener una forma de pago,
//				si selecciona una CXC puede tener varias formas de pago.
//				No se puede tener varias CXC con varias formas de pago, porque 
//				no es posible saber que forma de pago pertenece a que CXC, para 
//				hacer esto se tendria que diseñar la pantalla de una manera diferente
//				donde cada detalle de cobro tenga asignada la forma de pago o algo asi
				if (this.cxcSeles.size() > 1) {

					for (Cxc cxc : this.cxcSeles) {

						CobrDeta cobrDeta = new CobrDeta();
						cobrDeta.setFpmeFormPago(this.fpmeFormPagos.get(0));
						cobrDeta.setCxc(cxc);
						// Se registra como referencia para poder eliminar el abono en caso de NC
						cobrDeta.setEgresoId(cxc.getEgreso().getEgresoId());
						cobrDeta.setTotal(cxc.getSaldo());

						cobrDetaRegis.insertar(cobrDeta);

						if ((cxc.getTotal().compareTo((cobrDeta.getTotal().add(cxc.getAbono()))) == 0)) {
							cxc.setEstado(true);
						} else {
							cxc.setEstado(false);
						}

						cxcRegis.modificar(cxc);
					}

				} else if (this.cxcSeles.size() == 1) {

					for (Cxc cxc : this.cxcSeles) {

						for (FpmeFormPago fpmeFormPago : this.fpmeFormPagos) {

							CobrDeta cobrDeta = new CobrDeta();

							cobrDeta.setFpmeFormPago(fpmeFormPago);
							cobrDeta.setCxc(cxc);
							// Se registra como referencia para poder eliminar el abono en caso de NC
							cobrDeta.setEgresoId(cxc.getEgreso().getEgresoId());
							cobrDeta.setTotal(fpmeFormPago.getTotalReci());

							cobrDetaRegis.insertar(cobrDeta);

						}

						if ((cxc.getTotal().compareTo((this.formPagoMoviEgre.getTotal().add(cxc.getAbono()))) == 0)) {
							cxc.setEstado(true);
						} else {
							cxc.setEstado(false);
						}

						cxcRegis.modificar(cxc);

					}
				}
			}
			// else {
			// Estos documentos nos e pueden modificar hay que anularlos y volverlos a crear
			// formPagoMoviIngrRegis.modificar(formPagoMoviIngr);
			// }

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

//		Si el parametro de generacion por documento esta activado
//		genera una transaccion por cada documento
		if (variablesSesion.getGeneraTransaccion().equals("DO")) {
			if (this.formPagoMoviEgre.getDocuMoviEgre().getDocumento().isContab()) {

				contabilizarFpme(this.formPagoMoviEgre);
			}
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "explora?faces-redirect=true&formPagoMoviEgreId=" + this.getId();
	}

	@Deprecated
	public String validarGrabar() {

		String mensaje = "validado";

//		try {
//
//			List<FormPagoMoviEgre> fpmes = new ArrayList<FormPagoMoviEgre>();
////			Busca si se esta ingresando una referencia duplicada
//			fpmes = this.formPagoMoviEgreRegis.buscarFpmeRefere(this.formPagoMoviEgre.getRefere());
//			if (!fpmes.isEmpty()) {
//
//				mensaje = "Referencia ya existe ";
//			}
//		} catch (Exception e) {
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
//					"Excepcion - Error al buscar referencia de documento"));
//			e.printStackTrace();
//		}

		return mensaje;
	}

	private boolean validarAntesDeProcesar() {
		FacesContext context = FacesContext.getCurrentInstance();

		if (formPagoMoviEgre == null || formPagoMoviEgre.getTotal() == null) {
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, null, "El total del cobro es obligatorio"));
			return false;
		}

		// Validar que el total sea mayor a 0
		if (formPagoMoviEgre.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, null, "El total del cobro debe ser mayor a 0"));
			return false;
		}

		// Validar que exista al menos una cuenta por cobrar seleccionada
		if (cxcSeles == null || cxcSeles.isEmpty()) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, null,
					"Debe seleccionar al menos una cuenta por cobrar"));
			return false;
		}

		// Sumar lo abonado / recibido en formas de pago
		BigDecimal totalAbonado = BigDecimal.ZERO;
		if (fpmeFormPagos != null) {
			for (FpmeFormPago fp : fpmeFormPagos) {
				if (fp.getTotalReci() != null) {
					totalAbonado = totalAbonado.add(fp.getTotalReci());
				}
			}
		}

		// Validar que el total abonado sea mayor a 0
		if (totalAbonado.compareTo(BigDecimal.ZERO) <= 0) {
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, null, "El total abonado debe ser mayor a 0"));
			return false;
		}

		// Validar que el cobro no sea menor que el total abonado
		if (formPagoMoviEgre.getTotal().compareTo(totalAbonado) < 0) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, null,
					"El total del cobro no puede ser menor que el total abonado"));
			return false;
		}

		return true;
	}

	public String modificar() {
		return "registra?faces-redirect=true&formPagoMoviEgreId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&formPagoMoviEgreId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {

			// En el metodo recuperar ya se asignan los regostros a las listas
			// para poder eliminar y modificar
			for (CobrDeta cobrDeta : this.cobrDetas) {

				cxcs.add(cobrDeta.getCxc());
				cobrDetaRegis.eliminar(cobrDeta);
			}

			// Se cambia el estado a false de todas las CXP que se este anulando
			// ya que si se elimina un pago la cxp siempre estara pendiente
			// y si todavia estaba pendiente no afecta en nada
			for (Cxc cxc : cxcs) {
				cxc.setEstado(false);
				cxcRegis.modificar(cxc);
			}

			FormPagoMoviEgre formPagoMoviEgre = formPagoMoviEgreRegis.buscarPorId(FormPagoMoviEgre.class, this.getId());
			formPagoMoviEgreRegis.eliminar(formPagoMoviEgre);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar registro"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		return "lista?faces-redirect=true";
	}

	public void eliminarCxcSele() {

		// Se restablece el saldo antes de eliminar el registro de la lista ya que en el
		// caso que
		// se haya modificado el registro y se le haya borrado si se le vuelve a
		// seleccionar el registro
		// aparece con el saldo modificado
		this.cxcSele.setSaldo(this.cxcSele.getTotal().subtract(this.cxcSele.getAbono()));

		this.cxcSeles.remove(cxcSele);
		this.calcularTotal();
	}

	public void cargarDialogoFpmeFormPago() {

		this.fpmeFormPagos.clear();
		this.crearFilaFpmeFormPago();

	}

	public void crearFilaFpmeFormPago() {

		BigDecimal totalReciFpmeFp = new BigDecimal(0);

		FpmeFormPago fpmeFormPago = new FpmeFormPago();

		if (this.fpmeFormPagos.size() == 0 || this.fpmeFormPagos.isEmpty()) {

			fpmeFormPago.setFormPagoMoviEgre(this.formPagoMoviEgre);
			fpmeFormPago.setFormPago(formPagoPred);
			fpmeFormPago.setFecha(this.formPagoMoviEgre.getFecha());
			fpmeFormPago.setFechaHora(this.formPagoMoviEgre.getFecha().atTime(LocalTime.now()));
			fpmeFormPago.setDiasPlaz((short) 0);
			fpmeFormPago.setTotalReci(this.getFormPagoMoviEgre().getTotal());

		} else {

			fpmeFormPago.setFormPagoMoviEgre(this.formPagoMoviEgre);
			fpmeFormPago.setFormPago(new FormPago());
			fpmeFormPago.setFecha(this.formPagoMoviEgre.getFecha());
			fpmeFormPago.setFechaHora(this.formPagoMoviEgre.getFecha().atTime(LocalTime.now()));
			fpmeFormPago.setDiasPlaz((short) 0);

			totalReciFpmeFp = this.calcularCambio();
			fpmeFormPago.setTotalReci(totalReciFpmeFp);

		}

//		Se pone siempre el total porque cuando se añade una fila nueva
//		el saldo o cambio se calcula automaticamente, por lo tanto
//		la suma de fpmeFormPago.totalReci siempre va a ser el total del documento
		this.formPagoMoviEgre.setTotalReci(this.getFormPagoMoviEgre().getTotal());
		this.fpmeFormPagos.add(fpmeFormPago);

//		Siempre que se agrega una fila en cambio debe ser cero
//		ya que el total en cada fila aparece automaticamene
//		this.cambio = BigDecimal.ZERO;
	}

	public void eliminarFilaFpmeFormPago() {

		if (isTieneRetencion() && (fpmeFormPagoSele.getFormPago().getTipo2().equals(TIPO2_VN_RR)
				|| fpmeFormPagoSele.getFormPago().getTipo2().equals(TIPO2_VN_RI))) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"No se puede elimimar. Tiene registrada retención"));
			return;
		}

		this.fpmeFormPagos.remove(this.fpmeFormPagoSele);

		if (this.fpmeFormPagos.size() == 0 || this.fpmeFormPagos.isEmpty()) {
			this.formPagoMoviEgre.setTotalReci(BigDecimal.ZERO);
//			this.setCambio(BigDecimal.ZERO);
		} else {
//			this.cambio = this.calcularCambio();	
		}
	}

	public void buscar() {

		try {

			formPagoMoviEgreLista.filasPagina(variablesSesion.getFilasPagina());

			this.formPagoMoviEgres = formPagoMoviEgreLista.buscar(formPagoMoviEgre, this.pagina);
			this.numeroReg = formPagoMoviEgres.size();
			this.contadorReg = formPagoMoviEgreLista.contarRegistros(formPagoMoviEgre);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}

//		Cuando la factura se le registra como credito genera un cobro con forma de pago credito
//		y cuando se paga la misma factura se genera otro cobro con la forma de pago seleccionada
//		entonces la esta factura va a tener dos cobros y esa es la razon de la confucion
		if (!incluirCredito) {
			this.eliminarFpmesCredito(this.formPagoMoviEgres);
		}
	}

	public void eliminarFpmesCredito(List<FormPagoMoviEgre> formPagoMoviEgres) {

		Iterator<FormPagoMoviEgre> formPagoMoviEgre = formPagoMoviEgres.iterator();
		while (formPagoMoviEgre.hasNext()) {
			FormPagoMoviEgre fpme = formPagoMoviEgre.next();

			List<FpmeFormPago> fpmeFormPagos = this.buscarFpmeFormPagos(new FpmeFormPago(fpme));

//			TODO:
//			Solucion temporal
//			Si se pago la factura con mas de una forma de pago cuando entra aca da error
//			Solucion definitiva
//			Hacer la busqueda por forma de pago par no tener que hacer este filtro
			if (fpmeFormPagos.size() == 1) {
				for (FpmeFormPago fpmeFormPago : fpmeFormPagos) {
					if (fpmeFormPago.getFormPago().getTipo().equals("CR")) {
						formPagoMoviEgre.remove();
					}
				}
			}
		}
	}

	public List<FormPagoMoviEgre> buscarTodo() {

		List<FormPagoMoviEgre> formPagoMoviEgres = new ArrayList<>();

		try {
			formPagoMoviEgres = formPagoMoviEgreLista.buscarTodo("formPagoMoviEgreId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return formPagoMoviEgres;
	}

	// Busca PersClies pero retorna Personas;
	// Cuando se añada otros modulos cambiar a a buscar solo personas
	public List<Persona> buscarPersonas(Integer paginador) {

		if (paginador == 0) {
			this.paginaClie = 0;
		}

		if (this.personaBuscar.getCedulaRuc() != null) {
			this.personaBuscar.setApelli(null);
			this.personaBuscar.setNombre(null);
		}

		PersClie persClie = new PersClie();
		persClie.setPersona(this.personaBuscar);

		Collection<Persona> personasColeccion;
		List<Persona> personas = null;

		List<PersClie> persClies = new ArrayList<>();

		persClie.setEstado(true);

		try {
			persClies = persClieLista.buscar(persClie, this.paginaClie, variablesSesion.getFilasClientesEgreso());
			this.numeroRegClie = persClies.size();
			this.contadorRegClie = persClieLista.contarRegistros(persClie);

			personasColeccion = CollectionUtils.collect(persClies, TransformerUtils.invokerTransformer("getPersona"));
			personas = new ArrayList<>(personasColeccion);
			this.personas = personas;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return personas;
	}

	public List<PersCobr> buscarPersCobrs() {

		PersCobr persCobr = new PersCobr();
		persCobr.setEstado(true);

		Persona persona = new Persona();
		persCobr.setPersona(persona);

		List<PersCobr> persCobrs = new ArrayList<>();

		try {
			persCobrs = persCobrLista.buscar(persCobr, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return persCobrs;
	}

	public List<DocuMoviEgre> buscarDocuMoviEgres() {

		Documento documento = new Documento();
		documento.setEstado(true);

		DocuMoviEgre docuMoviEgre = new DocuMoviEgre();
		docuMoviEgre.setDocumento(documento);

		List<DocuMoviEgre> docuMoviEgres = new ArrayList<>();

		try {
			docuMoviEgres = docuMoviEgreLista.buscar(docuMoviEgre, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return docuMoviEgres;
	}

	public List<FormPago> buscarFormPagos() {

		FormPago formPago = new FormPago();
		formPago.setEstado(true);

		List<FormPago> formPagos = new ArrayList<>();

		try {
			formPagos = formPagoLista.buscar(formPago, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar forma de pago"));
			e.printStackTrace();
		}

		return formPagos;

	}

	public List<FpmeFormPago> buscarFpmeFormPagos(FpmeFormPago fpmeFormPago) {

		List<FpmeFormPago> fpmeFormPagos = new ArrayList<FpmeFormPago>();

		try {

			fpmeFormPagos = this.fpmeFormPagoLista.buscar(fpmeFormPago, null);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar fpmeFormPagos"));
			e.printStackTrace();
		}
		return fpmeFormPagos;
	}

	public void buscarCobrDetas(CobrDeta cobrDetaBuscar) {

		List<CobrDeta> cobrDetas = new ArrayList<CobrDeta>();

		try {
			cobrDetas = cobrDetaLista.buscar(cobrDetaBuscar, null);

			for (CobrDeta cobrDeta : cobrDetas) {

				FpmeFormPago fpmeFormPago = new FpmeFormPago();
				fpmeFormPago = this.fpmeFormPagoRegis.buscarPorId(FpmeFormPago.class,
						cobrDeta.getFpmeFormPago().getFpmeFormPagoId());

				cobrDeta.setFpmeFormPago(fpmeFormPago);
			}

			this.cobrDetas.addAll(cobrDetas);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar detalle de pagos"));
			e.printStackTrace();
		}
	}

//	Se llama desde la pagina para consultar los abonos a una cuenta
//	al dar click sobre la columna abono en Seleccione Cuentas Por Cobrar
	public void buscarCobrDetas() {

		CobrDeta cobrDeta = new CobrDeta();

		Cxc cxc = new Cxc();
		cxc.setCxcId(this.cxcId);
		Egreso egreso = new Egreso();
		cxc.setEgreso(egreso);

		cobrDeta.setCxc(cxc);

		try {
			this.cobrDetas = cobrDetaLista.buscar(cobrDeta, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar detalle de cobros"));
			e.printStackTrace();
		}
	}

	// Buscar CXP y pagos realizados a esta CXP para realizar los calculos de abonos
	// y saldos. Para buscar pagos se envia como parametro solamente la CXP
	// FormPagoMoviEgre y Persona deben ir sin datos de esta manera se buscar todos
	// los pagos realizados a una CXP, sin importar quien ha hecho ni con que
	// documento.
	// La entidad que se recibe como parametro (formPagoMoviEgre) se utiliza para
	// buscar las CXP de la persona que va a pagar
	public void buscarCxcs(FormPagoMoviEgre formPagoMoviEgre) {

		CobrDeta cobrDeta = new CobrDeta();
		List<CobrDeta> cobrDetas = new ArrayList<>();

		// Entidad nueva para buscar PAGOS de una cuenta por pagar
		FormPagoMoviEgre formPagoMoviEgreCobrDeta = new FormPagoMoviEgre();
		formPagoMoviEgreCobrDeta.setPersona(new Persona());

		PersClie persClie = new PersClie();
		if (formPagoMoviEgre.getPersona().getPersClie() == null) {
			persClie.setPersona(new Persona());
			persClie.setPersonaId(formPagoMoviEgre.getPersona().getPersonaId());
		} else {
			persClie = formPagoMoviEgre.getPersona().getPersClie();
		}

		Cxc cxc = new Cxc();
		Egreso egreso = new Egreso();
		egreso.setPersClie(persClie);
		cxc.setEgreso(egreso);
		cxc.setEstado(false);

		try {

			this.cxcs = cxcLista.buscar(cxc, null);

			for (Cxc cxcSaldo : cxcs) {

				BigDecimal totalAbono = new BigDecimal(0);

				cobrDeta.setCxc(cxcSaldo);

				cobrDetas = this.cobrDetaLista.buscar(cobrDeta, null);

				for (CobrDeta cobrDetaCxc : cobrDetas) {
					totalAbono = totalAbono.add(cobrDetaCxc.getTotal());
				}

//				Se realilza redondeo para solo para dar formato en la pagina
//				ya que este valor no se despliega en una caja de texto sino en un commanLink
				cxcSaldo.setAbono(totalAbono.setScale(2, RoundingMode.HALF_UP));
				cxcSaldo.setSaldo(cxcSaldo.getTotal().subtract(totalAbono));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar cuentas por cobrar"));
			e.printStackTrace();
		}
	}

	public List<Cxc> buscarCxcs(Integer egresoId) {

		PersClie persClie = new PersClie();
		persClie.setPersona(new Persona());
		Egreso egreso = new Egreso();
		egreso.setEgresoId(egresoId);
		egreso.setPersClie(persClie);

		Cxc cxc = new Cxc();
		cxc.setEgreso(egreso);
		cxc.setEstado(false);

		try {
			this.cxcs = cxcLista.buscar(cxc, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar Cuentas por pagar con egreso Id"));
			e.printStackTrace();
		}

		return cxcs;
	}

	public void buscarSesionVentas() {

		CajaMovi cajaMovi = new CajaMovi();
		DocuCaja docuCaja = new DocuCaja();
		docuCaja.setDocumento(new Documento());

		List<CajaMovi> cajaMovis = new ArrayList<>();

		cajaMovi.setSucursal(variablesSesion.getSucursal());
		cajaMovi.setCaja(new Caja());
		cajaMovi.setDocuCaja(docuCaja);
		cajaMovi.setEstado(false);
		cajaMovi.setPersCaje(this.persUsuaSesion.getPersona().getPersCaje());

		try {

			cajaMovis = cajaMoviLista.buscar(cajaMovi, null);

			if (cajaMovis.size() == 0) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, null, "No se ha registrado una sesión de ventas"));

			} else if (cajaMovis.size() > 1) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
						"Se ha registrado mas de una sesión de ventas"));

			} else if (cajaMovis.size() == 1) {

				this.cajaMovi = cajaMovis.get(0);

			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar sesion de ventas"));
			e.printStackTrace();
		}
	}

	public void buscarTranPlan() {

		this.formPagoMoviEgre.setPersona(null);
		this.cxcs.clear();
		this.cxcSeles.clear();

		try {
			this.tranPlan = this.tranPlanRegis.buscarPorId(TranPlan.class, this.formPagoMoviEgre.getTranPlanId());

//			Buscar detalle donde se encuentra el id del plan de cuentas solo si es gasto 
//			ya que en los otros tipos la cuenta se busca en el proveedor

			if (this.tranPlan.getTipo().equals("GASTO")) {

				try {
					this.tranPlanDetas = this.buscarTranPlanDetas(this.formPagoMoviEgre.getTranPlanId());
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
							null, "Excepcion - Error al buscar Transacción Plantilla Detalle"));
					e.printStackTrace();
				}

//				Poner al superadministrador en persona ya que el pago de gastos no tiene proveedor 
//				Persona persona = this.buscarSuperAdministrador();
//				this.formPagoMoviIngr.setPersona(persona);

			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar transacción Plantilla"));
			e.printStackTrace();
		}
	}

	public List<TranPlan> buscarTranPlans() {

		TranPlan tranPlan = new TranPlan();
		tranPlan.setModulo("COBROS");
		tranPlan.setEstado(true);

		List<TranPlan> tranPlans = new ArrayList<>();

		try {
			tranPlans = tranPlanLista.buscar(tranPlan, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar Transacción Plantilla"));
			e.printStackTrace();
		}

		return tranPlans;
	}

	public List<TranPlanDeta> buscarTranPlanDetas(Integer tranPlanId) throws Exception {

		TranPlanDeta tranPlanDeta = new TranPlanDeta();
		TranPlan tranPlan = new TranPlan();
		List<TranPlanDeta> tranPlanDetas = new ArrayList<>();

		tranPlan.setTranPlanId(tranPlanId);
		tranPlanDeta.setTranPlan(tranPlan);

		tranPlanDetas = this.tranPlanDetaLista.buscar(tranPlanDeta, null);

		return tranPlanDetas;
	}

	public void seleccionarPersClie() {

		// Limpia la lista de cxpSeleccionadas y el total pago
		// Por ejemplos si se selecciono un proveedor y se seleccionaron cxp la lista
		// queda con registros
		// y en el caso que no se vaya a hacer el pago o se selecione otro proceedor la
		// lista queda con esos datos
		this.formPagoMoviEgre.setTotal(new BigDecimal(0));
		this.cxcSeles.clear();
		this.buscarCxcs(this.formPagoMoviEgre);

		this.saldoCliente();
	}

	public void onRowSelect(SelectEvent<?> event) {

		// FacesMessage msg = new FacesMessage("Cxp Selected", ((Cxp)
		// event.getObject()).getCxpId().toString());
		// FacesContext.getCurrentInstance().addMessage(null, msg);
		cxcSeles.add((Cxc) event.getObject());

		this.calcularTotal();

	}

	public void modificarCelda(CellEditEvent<?> event) {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		Cxc cxc = (Cxc) ((DataTable) event.getComponent()).getRowData();

		if (newValue != null && !newValue.equals(oldValue)) {

			if (cxc.getSaldo().compareTo(cxc.getTotal().subtract(cxc.getAbono())) == 1) {

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, null,
						"Valor abono es mayor que total cuenta por cobrar"));

				cxc.setSaldo((BigDecimal) oldValue);

			}

			this.calcularTotal();
		}
	}

	public void calcularTotal() {

		BigDecimal totalCobro = new BigDecimal(0);

		for (Cxc cxc : cxcSeles) {

			totalCobro = totalCobro.add(cxc.getSaldo());
		}

		this.formPagoMoviEgre.setTotal(totalCobro);
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
	}

	public BigDecimal sumarCxc() {

		BigDecimal sumaCxc = new BigDecimal(0);

		PersClie persClie = new PersClie();
		persClie.setPersona(this.formPagoMoviEgre.getPersona());

		Cxc cxc = new Cxc();
		Egreso egreso = new Egreso();
		egreso.setPersClie(persClie);
		cxc.setEgreso(egreso);

		try {

			sumaCxc = this.cxcLista.sumarCxc(cxc);

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

		persClie.setPersona(this.formPagoMoviEgre.getPersona());
		egreso.setPersClie(persClie);
		cxc.setEgreso(egreso);
		cobrDeta.setCxc(cxc);

		try {
			sumaCobrDeta = this.cobrDetaLista.sumarCobrDeta(cobrDeta);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al sumar cobros"));
			e.printStackTrace();
		}

		return sumaCobrDeta;
	}

	public void insertarRetencion() {

		try {

			retencion.setFechaHoraEmis(retencion.getFechaEmis().atTime(LocalTime.now()));
			retencion.setFechaRegi(LocalDate.now());
			retencion.setFechaHoraRegi(LocalDateTime.now());
			retencion.setClaveAcce(retencion.getAutori());
			retencion.setEstado(ESTADO_PR);
			retencion.setEstadoDocuElec(ESTADO_DOC_ELEC_AUTORIZADO);
			retencion.setDocumeElec(true);

			retencionRegis.insertar(retencion);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error: Retencion no se ha grabado", e);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Error: Retencion no se ha grabado"));
		}
	}

	public void insertarReteDeta() {

		for (ReteDeta reteDeta : reteDetas) {
			reteDeta.setRetencion(retencion);
			try {
				reteDetaRegis.insertar(reteDeta);
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "Error: Detalles de retencion no se han grabado", e);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Error: Detalles de retencion no se ha grabado"));
			}
		}
	}

//	Se ejecuta al abrir dialogo para cargar retencion
	public void iniciarCargarRetencion() {
		retencion = new Retencion();
		reteDetas = new ArrayList<ReteDeta>();
	}

//	Se ejecuta hacer click sobre le boton aceptar del dialogo para cargar retecion
	public void cargarFpmeFormPagoRetencion() {

		this.fpmeFormPagos.clear();
		this.crearFpmeFormPagoRetencion();
		crearFilaFpmeFormPago();
		setTieneRetencion(true);
	}

//	Se ejecuta hacer click sobre le boton cancelar del dialogo para cargar retecion
	public void cancelarCargarRetencion() {
		setTieneRetencion(false);
		iniciarCargarRetencion();
		cargarDialogoFpmeFormPago();
	}

	public void validarRetencionNumeroAutorizacion() {

		FacesContext context = FacesContext.getCurrentInstance();

		existeRetencionNumeroAutorizacion = false;
		
		String autorizacion = retencion != null ? retencion.getAutori() : null;

		if (autorizacion == null || autorizacion.isBlank()) {
			return;
		}

		boolean existe = retencionLista.existeNumeroAutorizacion(retencion.getAutori().trim());

		if (existe) {
			existeRetencionNumeroAutorizacion = true;
			UIComponent component = UIComponent.getCurrentComponent(context);

			if (component instanceof UIInput input) {
				input.setValid(false);
			}

			context.addMessage(component.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR,
					null, "El número de autorización ya esta registrado en el sistema"));
		}
	}

	public void cargarDimmRetenciones() {
		try {
			Dimm dimmRenta = new Dimm();
			dimmRenta.setTablaRefe(TABLA_RETEN_RENTA);
			dimmRenta.setEstado(true);
			dimmRetencionRentas = dimmLista.buscar(dimmRenta, dimmRenta, null);

			Dimm dimmIva = new Dimm();
			dimmIva.setTablaRefe(TABLA_RETEN_IVA);
			dimmIva.setEstado(true);
			dimmRetencionIvas = dimmLista.buscar(dimmIva, dimmIva, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Dimm> obtenerDimmRetencions(String impues) {
		if (IMPUESTO_RENTA.equals(impues)) {
			return dimmRetencionRentas;
		} else if (IMPUESTO_IVA.equals(impues)) {
			return dimmRetencionIvas;
		}
		return java.util.Collections.emptyList();
	}

	public void alCambiarReteDetaImpues(ReteDeta reteDeta) {
		reteDeta.setCodigoImpu(null); // limpiar selección anterior
		reteDeta.setPorcen(BigDecimal.ZERO); // o BigDecimal.ZERO, según tu tipo
		reteDeta.setReteDetaTotal(BigDecimal.ZERO); // si tienes total de retención calculado
	}

	public void alSeleccionarDimmRetencion(ReteDeta reteDeta) {

		if (reteDeta == null || reteDeta.getCodigoImpu() == null || reteDeta.getCodigoImpu().trim().isEmpty()) {
			reteDeta.setPorcen(BigDecimal.ZERO);
			return;
		}

		List<Dimm> listaRetenciones = obtenerDimmRetencions(reteDeta.getImpues());

		if (listaRetenciones == null || listaRetenciones.isEmpty()) {
			reteDeta.setPorcen(BigDecimal.ZERO);
			return;
		}

		for (Dimm dimm : listaRetenciones) {
			if (reteDeta.getCodigoImpu().equals(dimm.getCodigo())) {

				reteDeta.setPorcen(dimm.getPorcen());
				calcularTotalReteDeta();
				return;
			}

		}
		// si no encuentra coincidencia
		reteDeta.setPorcen(BigDecimal.ZERO);
	}

	public List<FpmeFormPago> crearFpmeFormPagoRetencion() {

		for (ReteDeta reteDeta : reteDetas) {
			FpmeFormPago fpmeFormPago = new FpmeFormPago();
			fpmeFormPago.setReteDeta(reteDeta);
			fpmeFormPago.setFormPagoMoviEgre(formPagoMoviEgre);
			fpmeFormPago.setFecha(this.formPagoMoviEgre.getFecha());
			fpmeFormPago.setFechaHora(this.formPagoMoviEgre.getFecha().atTime(LocalTime.now()));
			fpmeFormPago.setFormPago(obtenerFormaPagoRetencion(reteDeta.getImpues()));
			fpmeFormPago.setDiasPlaz((short) 0);
			fpmeFormPago.setTotalReci(reteDeta.getReteDetaTotal());

			this.fpmeFormPagos.add(fpmeFormPago);
		}
		return fpmeFormPagos;
	}

	private FormPago obtenerFormaPagoRetencion(String impuesto) {

		if (impuesto == null || impuesto.isBlank()) {
			throw new IllegalArgumentException("El impuesto es obligatorio");
		}

		String tipo2;

		if (IMPUESTO_RENTA.equals(impuesto)) {
			tipo2 = TIPO2_VN_RR;
		} else if (IMPUESTO_IVA.equals(impuesto)) {
			tipo2 = TIPO2_VN_RI;
		} else {
			throw new IllegalArgumentException("No existe configuración de forma de pago para impuesto: " + impuesto);
		}

		return formPagos.stream().filter(Objects::nonNull).filter(fp -> Boolean.TRUE.equals(fp.isEstado()))
				.filter(fp -> tipo2.equals(fp.getTipo2())).findFirst().orElseThrow(() -> new IllegalStateException(
						"No existe forma de pago activa configurada para impuesto: " + impuesto));
	}

	public void grabarRetencion() {
		
		if (retencion.getAutori() != null) {
			this.insertarRetencion();
			this.insertarReteDeta();
		}
	}

	public void agregarReteDeta() {

		ReteDeta reteDeta = new ReteDeta();

		reteDeta.setEjerciFisc(this.retencion.getFechaEmis());
		reteDeta.setBase(new BigDecimal(0));
		reteDeta.setPorcen(new BigDecimal(0));

		this.reteDetas.add(reteDeta);
	}

	// Se elimina de la lista
	public void eliminarReteDeta() {
		this.reteDetas.remove(this.reteDetaSele);
	}

	public void calcularTotalReteDeta() {
		retencionServicio.calcularReteDeta(reteDetas);
	}

// Comienza descarga archivo retencion del sri
	@Inject
	RetencionSriServicio retencionSriServicio;

	public void cargarXmlDesdeSri() {

		try {

			Retencion retencionBuscada = retencionSriServicio.descargarRetencionSri(retencion.getAutori());

			if (retencionBuscada == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, null,
								"No se encontró la retención en el SRI o no está autorizada - Clave: "
										+ retencion.getAutori()));

				return;
			}

			retencion = retencionBuscada;
			reteDetas = new ArrayList<>(retencion.getReteDetas());
//			Se coloca este clear porque la clase Retencion viene con un set de reteDetas y 
//			tiene cascade en persist entonces al grabar sale error porque intenta grabar nuevamente
//			con el clear deja el set vacio y grabar el list sin errores
//			Se hace esto porque aqui se ve la retencion en pantalla, mientras que en compra no se ve
//			la retencion entonces graba con el set de reteDeta
//			La IA aconseja si el elemento es visual utilizar list e lugar de set
			retencion.getReteDetas().clear();

			calcularTotalReteDeta();

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Documento cargado desde SRI, revisar y procesar..."));

		} catch (Exception e) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, e.getMessage()));

			e.printStackTrace();

		}
	}

	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public FormPagoMoviEgre getFormPagoMoviEgre() {
		return formPagoMoviEgre;
	}

	public void setFormPagoMoviEgre(FormPagoMoviEgre formPagoMoviEgre) {
		this.formPagoMoviEgre = formPagoMoviEgre;
	}

	public List<FormPagoMoviEgre> getFormPagoMoviEgres() {
		return formPagoMoviEgres;
	}

	public void setFormPagoMoviEgres(List<FormPagoMoviEgre> formPagoMoviEgres) {
		this.formPagoMoviEgres = formPagoMoviEgres;
	}

	public List<FormPago> getFormPagos() {
		return formPagos;
	}

	public void setFormPagos(List<FormPago> formPagos) {
		this.formPagos = formPagos;
	}

	public List<DocuMoviEgre> getDocuMoviEgres() {
		return docuMoviEgres;
	}

	public void setDocuMoviEgres(List<DocuMoviEgre> docuMoviEgres) {
		this.docuMoviEgres = docuMoviEgres;
	}

	public List<Cxc> getCxcs() {
		return cxcs;
	}

	public void setCxcs(List<Cxc> cxcs) {
		this.cxcs = cxcs;
	}

	public List<CobrDeta> getCobrDetas() {
		return cobrDetas;
	}

	public void setCobrDetas(List<CobrDeta> cobrDetas) {
		this.cobrDetas = cobrDetas;
	}

	public List<PersCobr> getPersCobrs() {
		return persCobrs;
	}

	public void setPersCobrs(List<PersCobr> persCobrs) {
		this.persCobrs = persCobrs;
	}

	public Integer getEgresoId() {
		return egresoId;
	}

	public void setEgresoId(Integer egresoId) {
		this.egresoId = egresoId;
	}

	public List<PersClie> getPersClies() {
		return persClies;
	}

	public void setPersClies(List<PersClie> persClies) {
		this.persClies = persClies;
	}

	public List<Persona> getPersonas() {
		return personas;
	}

	public void setPersonas(List<Persona> personas) {
		this.personas = personas;
	}

	public CajaMovi getCajaMovi() {
		return cajaMovi;
	}

	public void setCajaMovi(CajaMovi cajaMovi) {
		this.cajaMovi = cajaMovi;
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

	public int getFilasClientes() {
		return variablesSesion.getFilasClientesEgreso();
	}

	public Integer getCxcId() {
		return cxcId;
	}

	public void setCxcId(Integer cxcId) {
		this.cxcId = cxcId;
	}

	public Persona getPersonaBuscar() {
		return personaBuscar;
	}

	public void setPersonaBuscar(Persona personaBuscar) {
		this.personaBuscar = personaBuscar;
	}

	public Cxc getCxcSele() {
		return cxcSele;
	}

	public void setCxcSele(Cxc cxcSele) {
		this.cxcSele = cxcSele;
	}

	public Set<Cxc> getCxcSeles() {
		return cxcSeles;
	}

	public void setCxcSeles(Set<Cxc> cxcSeles) {
		this.cxcSeles = cxcSeles;
	}

	public BigDecimal getSaldoCliente() {
		return saldoCliente;
	}

	public void setSaldoCliente(BigDecimal saldoCliente) {
		this.saldoCliente = saldoCliente;
	}

	public TranPlan getTranPlan() {
		return tranPlan;
	}

	public void setTranPlan(TranPlan tranPlan) {
		this.tranPlan = tranPlan;
	}

	public List<TranPlan> getTranPlans() {
		return tranPlans;
	}

	public void setTranPlans(List<TranPlan> tranPlans) {
		this.tranPlans = tranPlans;
	}

	public List<TranPlanDeta> getTranPlanDetas() {
		return tranPlanDetas;
	}

	public void setTranPlanDetas(List<TranPlanDeta> tranPlanDetas) {
		this.tranPlanDetas = tranPlanDetas;
	}

	public FpmeFormPago getFpmeFormPagoSele() {
		return fpmeFormPagoSele;
	}

	public void setFpmeFormPagoSele(FpmeFormPago fpmeFormPagoSele) {
		this.fpmeFormPagoSele = fpmeFormPagoSele;
	}

	public List<FpmeFormPago> getFpmeFormPagos() {
		return fpmeFormPagos;
	}

	public void setFpmeFormPagos(List<FpmeFormPago> fpmeFormPagos) {
		this.fpmeFormPagos = fpmeFormPagos;
	}

	public boolean isIncluirCredito() {
		return incluirCredito;
	}

	public void setIncluirCredito(boolean incluirCredito) {
		this.incluirCredito = incluirCredito;
	}

	public Retencion getRetencion() {
		return retencion;
	}

	public void setRetencion(Retencion retencion) {
		this.retencion = retencion;
	}

	public List<ReteDeta> getReteDetas() {
		return reteDetas;
	}

	public void setReteDetas(List<ReteDeta> reteDetas) {
		this.reteDetas = reteDetas;
	}

	public BigDecimal getRetencionTotal() {
		return retencionTotal;
	}

	public void setRetencionTotal(BigDecimal retencionTotal) {
		this.retencionTotal = retencionTotal;
	}

	public ReteDeta getReteDetaSele() {
		return reteDetaSele;
	}

	public void setReteDetaSele(ReteDeta reteDetaSele) {
		this.reteDetaSele = reteDetaSele;
	}

	public List<Dimm> getDimmRetencionRentas() {
		return dimmRetencionRentas;
	}

	public void setDimmRetencionRentas(List<Dimm> dimmRetencionRentas) {
		this.dimmRetencionRentas = dimmRetencionRentas;
	}

	public List<Dimm> getDimmRetencionIvas() {
		return dimmRetencionIvas;
	}

	public void setDimmRetencionIvas(List<Dimm> dimmRetencionIvas) {
		this.dimmRetencionIvas = dimmRetencionIvas;
	}

	public boolean isTieneRetencion() {
		return tieneRetencion;
	}

	public void setTieneRetencion(boolean tieneRetencion) {
		this.tieneRetencion = tieneRetencion;
	}

	public boolean isExisteRetencionNumeroAutorizacion() {
		return existeRetencionNumeroAutorizacion;
	}

	public void setExisteRetencionNumeroAutorizacion(boolean existeRetencionNumeroAutorizacion) {
		this.existeRetencionNumeroAutorizacion = existeRetencionNumeroAutorizacion;
	}
}