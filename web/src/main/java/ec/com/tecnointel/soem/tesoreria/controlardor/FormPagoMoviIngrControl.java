package ec.com.tecnointel.soem.tesoreria.controlardor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.TransformerUtils;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;

import ec.com.tecnointel.soem.contabilidad.modelo.Transaccion;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionFpmiInt;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionGestionInt;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.ingreso.listaInt.PersProvListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.ProvGrupPlanCuenListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.modelo.PersProv;
import ec.com.tecnointel.soem.ingreso.modelo.ProvGrupPlanCuen;
import ec.com.tecnointel.soem.parametro.listaInt.DocuMoviIngrListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.FormPagoListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.FormPagoPlanCuenListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.TranPlanDetaListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.TranPlanListaInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuMoviIngr;
import ec.com.tecnointel.soem.parametro.modelo.DocuTran;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.parametro.modelo.FormPagoPlanCuen;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.modelo.TranPlan;
import ec.com.tecnointel.soem.parametro.modelo.TranPlanDeta;
import ec.com.tecnointel.soem.parametro.registroInt.DocumentoRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.PersonaRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.TranPlanRegisInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolDocu;
import ec.com.tecnointel.soem.tesoreria.listaInt.CxpListaInt;
import ec.com.tecnointel.soem.tesoreria.listaInt.FormPagoMoviIngrListaInt;
import ec.com.tecnointel.soem.tesoreria.listaInt.PagoDetaListaInt;
import ec.com.tecnointel.soem.tesoreria.modelo.Cxp;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import ec.com.tecnointel.soem.tesoreria.modelo.PagoDeta;
import ec.com.tecnointel.soem.tesoreria.registroInt.CxpRegisInt;
import ec.com.tecnointel.soem.tesoreria.registroInt.FormPagoMoviIngrRegisInt;
import ec.com.tecnointel.soem.tesoreria.registroInt.PagoDetaRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class FormPagoMoviIngrControl extends PaginaControl implements Serializable {

	private Integer ingresoId;
	private Integer paginaProv;

	private long contadorRegProv;
	private int numeroRegProv;

	private Integer cxpId;

	private BigDecimal saldoProveedor = new BigDecimal(0);

	private Cxp cxpSele = new Cxp();
	private Persona personaBuscar;
	private PersUsua persUsuaSesion;

	private FormPagoMoviIngr formPagoMoviIngr;

	private TranPlan tranPlan;

	private List<FormPagoMoviIngr> formPagoMoviIngrs;

	private List<TranPlan> tranPlans;
	private List<TranPlan> tranPlanTmps;
	private List<TranPlanDeta> tranPlanDetas;

	private Set<Cxp> cxpSeles = new HashSet<>();

	// Combos
	private List<Persona> personas;
	private List<FormPago> formPagos;
	private List<FormPago> formPagoTmps;
	private List<DocuMoviIngr> docuMoviIngrs;

	// Detalles
	private List<PagoDeta> pagoDetas;
	private List<Cxp> cxps;

	@Inject
	FormPagoMoviIngrListaInt formPagoMoviIngrLista;

	@Inject
	FormPagoMoviIngrRegisInt formPagoMoviIngrRegis;

	@Inject
	DocuMoviIngrListaInt docuMoviIngrLista;

	@Inject
	FormPagoListaInt formPagoLista;

	@Inject
	PagoDetaListaInt pagoDetaLista;

	@Inject
	PagoDetaRegisInt pagoDetaRegis;

	@Inject
	CxpListaInt cxpLista;

	@Inject
	CxpRegisInt cxpRegis;

	@Inject
	PersProvListaInt persProvLista;

	@Inject
	PersonaRegisInt personaRegis;

	@Inject
	DocumentoRegisInt documentoRegis;

	@Inject
	ProvGrupPlanCuenListaInt provGrupPlanCuenLista;

	@Inject
	FormPagoPlanCuenListaInt formPagoPlanCuenLista;

	@Inject
	TransaccionGestionInt transaccionGestion;

	@Inject
	TransaccionRegisInt transaccionRegis;

	@Inject
	TranPlanListaInt tranPlanLista;

	@Inject
	TranPlanRegisInt tranPlanRegis;

	@Inject
	TranPlanDetaListaInt tranPlanDetaLista;

	@Inject
	TransaccionFpmiInt transaccionFpmi;

	private static final long serialVersionUID = 3010512752912648715L;

	@PostConstruct
	public void cargar() {

		this.persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");

		this.paginaProv = 0;

		this.tranPlan = new TranPlan();

		personaBuscar = new Persona();

		formPagoMoviIngr = new FormPagoMoviIngr();
		formPagoMoviIngr.setPersona(new Persona());

//		Date fecha = Util.cambiarFormatoFecha(new Date(), "dd-MM-yyyy");
//		formPagoMoviIngr.setFecha(fecha);
		formPagoMoviIngr.setFecha(LocalDate.now());

		this.formPagos = new ArrayList<>();
		this.cxps = new ArrayList<>();
		this.tranPlans = new ArrayList<>();
	}

	public String anular() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		List<Cxp> cxps = new ArrayList<>();

		try {
			// En el metodo recuperar ya se asignan los registros a las listas
			// para poder eliminar y modificar
			for (PagoDeta pagoDeta : this.pagoDetas) {

				cxps.add(pagoDeta.getCxp());
				pagoDetaRegis.eliminar(pagoDeta);
			}

			// Se cambia el estado a false de todas las CXP que se este anulando
			// ya que si se elimina un pago la cxp siempre estara pendiente
			// y si todavia estaba pendiente no afecta en nada
			for (Cxp cxp : cxps) {
				cxp.setEstado(false);
				cxpRegis.modificar(cxp);
			}

			FormPagoMoviIngr formPagoMoviIngr = formPagoMoviIngrRegis.buscarPorId(FormPagoMoviIngr.class, this.getId());
//			formPagoMoviIngr.setTotal(new BigDecimal(0));
			formPagoMoviIngr.setEstado("AN");

			formPagoMoviIngrRegis.modificar(formPagoMoviIngr);

//			Se elimina la transaccion del pago
			if (formPagoMoviIngr.getTransaccion() != null) {
				this.transaccionGestion.anularTransaccion(formPagoMoviIngr.getTransaccion().getTransaccionId());
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

	public Integer contabilizarPago(FormPagoMoviIngr formPagoMoviIngr) {

		Integer transaccionId = 0;

		try {
			
//			Modifica la nota del ingreso para pasar eso a la nota de la tansaccion
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			
//			Revisar que la nota no tenga valores nulos
			if (formPagoMoviIngr.getNota() == null) {
				formPagoMoviIngr.setNota(formPagoMoviIngr.getDocuMoviIngr().getDocumento().getDescri() + " " + formPagoMoviIngr.getFormPago().getDescri() + " " + formPagoMoviIngr.getNumero() + " " + 
						formPagoMoviIngr.getPersona().getApelli() + " Ref:" + formPagoMoviIngr.getRefere() + " " +  formPagoMoviIngr.getFecha().format(dateTimeFormatter));				
			} else {
				formPagoMoviIngr.setNota(formPagoMoviIngr.getNota() + " " + formPagoMoviIngr.getDocuMoviIngr().getDocumento().getDescri() + " " + formPagoMoviIngr.getFormPago().getDescri() + " " + formPagoMoviIngr.getNumero() + " " + 
						formPagoMoviIngr.getPersona().getApelli() + " Ref:" + formPagoMoviIngr.getRefere() + " " +  formPagoMoviIngr.getFecha().format(dateTimeFormatter));
			}
			
//			Revisar que la nota no tenga mas de 255 caracateres
			if (formPagoMoviIngr.getNota().length() > 255) {
				formPagoMoviIngr.setNota(formPagoMoviIngr.getNota().substring(0,254));
			}
			
			if (this.formPagoMoviIngr.getDocuMoviIngr().getTipo().equals("PAGO-COBRO")) {

//				Si el pago es de CXP se utiliza las cuentas del proveedor
//				si es de gasto se utiliza las cuentas de tranPlanDeta
				if (this.tranPlan.getTipo().equals("CXP")) {

					transaccionId = transaccionFpmi.contabilizarPago(formPagoMoviIngr);

				} else if (this.tranPlan.getTipo().equals("GASTO")) {

					transaccionId = transaccionFpmi.contabilizarPago(formPagoMoviIngr, this.tranPlanDetas);
				}

			} else if (this.formPagoMoviIngr.getDocuMoviIngr().getTipo().equals("ANTICIPO")) {

				transaccionId = transaccionFpmi.contabilizarAnticipo(formPagoMoviIngr);

			}
			if (this.formPagoMoviIngr.getDocuMoviIngr().getTipo().equals("DEPOSITO")) {

				transaccionId = transaccionFpmi.contabilizarDeposito(formPagoMoviIngr);

			}

			if (transaccionId != 0) {

				Transaccion transaccion = new Transaccion();
				transaccion.setTransaccionId(transaccionId);

				this.formPagoMoviIngr.setTransaccion(transaccion);
				try {
					this.formPagoMoviIngrRegis.modificar(this.formPagoMoviIngr);
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

		nombreReporte = this.formPagoMoviIngr.getDocuMoviIngr().getDocumento().getFormat();

		// Se coloca aqui la ruta para poder tener los reportes en diferentes y varias
		// carpetas
		// Sino se podria poner en la clase GenerarJasperReport
		String rutaJrxml; // = "C:\\tecnoIntel\\soem\\jasperReportes\\ingreso\\";
		// En esta ruta se compila el reporte dentro del servidor de aplicaciones dentro
		// del archivo .EAR .WEB
		String rutaReporteCompilado = "\\jasperReportes\\tesoreria\\";

		try {

			Integer fpmiId = this.formPagoMoviIngr.getFpmiId();

			if (fpmiId == null) {
				parametrosJasper.put("fpmiId", 0);
			} else {
				parametrosJasper.put("fpmiId", fpmiId);
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

	public void filtrarFormPagoCompras() {

//		Se coloca para que eliminar la persona en el caso de que escogio un documento y luego otro
//		asi se obliga a ingresar nuevamente el proveedor
		this.formPagoMoviIngr.setPersona(null);

		this.cxps.clear();
		this.cxpSeles.clear();

		try {
			this.formPagos = formPagoLista.filtrarFormPagoCompras(formPagoMoviIngr, formPagoTmps, persUsuaSesion,
					variablesSesion.getRolFormPagos());

			if (this.formPagoMoviIngr.getDocuMoviIngr().getTipo().equals("DEPOSITO")) {
//				Coloca el SuperAdministrador en persona ya que en deposito no se selecciona proveedor
				Persona persona = this.buscarSuperAdministrador();
				this.formPagoMoviIngr.setPersona(persona);
			}
		} catch (Exception e) {

//			Si el usario no tiene acceso al documento predeterminado da este error ya que el filtro de formas de pago
//			se hace de acuerdo al documento seleccionado o predeterminado 
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, null,
					"Excepcion - Error al filtrar formas de pago, No tiene acceso a documento predeterminado"));
			e.printStackTrace();
		}

		this.filtrarTranPlans();
	}

	public void filtrarTranPlans() {

		this.tranPlans.clear();

		if (this.formPagoMoviIngr.getDocuMoviIngr().getTipo().equals("DEPOSITO")
				|| this.formPagoMoviIngr.getDocuMoviIngr().getTipo().equals("ANTICIPO")) {

//			Deja en la lista solamente la opcion ninguno para que el usuario no pueda seleccionar ningun elemento 
//			porque el anticipo y deposito se genera contra la cuenta de anticipo del proveedor y caja 
//			this.formPagoMoviIngr.setTranPlanId(0);

			for (TranPlan tranPlan : this.tranPlanTmps) {
				if (tranPlan.getTipo().equals("NINGUNO")) {
					this.tranPlans.add(tranPlan);
				}
			}
		} else if (this.formPagoMoviIngr.getDocuMoviIngr().getTipo().equals("PAGO-COBRO")) {

			for (TranPlan tranPlan : this.tranPlanTmps) {
				if (!tranPlan.getTipo().equals("NINGUNO")) {
					this.tranPlans.add(tranPlan);
				}
			}
		}
	}

	public Persona buscarSuperAdministrador() {

		Persona persona = new Persona();

		try {
			persona = this.personaRegis.buscarPorId(Persona.class, 1);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar persona Id, asigne como proveedor a super-administrador"));
			e.printStackTrace();
		}

		return persona;

	}

//	Se ejecuta la cambiar datos en el combo y al recuperar
	public void buscarTranPlan() {

		this.formPagoMoviIngr.setPersona(null);
		this.cxps.clear();
		this.cxpSeles.clear();

		try {
			this.tranPlan = this.tranPlanRegis.buscarPorId(TranPlan.class, this.formPagoMoviIngr.getTranPlanId());

//			Buscar detalle donde se encuentra el id del plan de cuentas solo si es gasto 
//			ya que en los otros tipos la cuenta se busca en el proveedor

			if (this.tranPlan.getTipo().equals("GASTO")) {

				try {
					this.tranPlanDetas = this.buscarTranPlanDetas(this.formPagoMoviIngr.getTranPlanId());
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
							null, "Excepcion - Error al buscar Transacción Plantilla Detalle"));
					e.printStackTrace();
				}

//				Poner al superadministrador en persona ya que el pago de gastos no tiene proveedor 
				Persona persona = this.buscarSuperAdministrador();
				this.formPagoMoviIngr.setPersona(persona);

			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar transacción Plantilla"));
			e.printStackTrace();
		}
	}

	public List<TranPlan> buscarTranPlans() {

		TranPlan tranPlan = new TranPlan();

		List<TranPlan> tranPlans = new ArrayList<>();

		tranPlan.setModulo("PAGOS");
		tranPlan.setEstado(true);

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

	public void recuperar() {

		DocuMoviIngr docuMoviIngrPred;

		List<RolDocu> rolDocus = variablesSesion.getRolDocus();
		List<DocuMoviIngr> docuMoviIngrs = new ArrayList<>();

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback()) {
			return;
		}

		docuMoviIngrs = this.buscarDocuMoviIngrs();
		this.tranPlanTmps = this.buscarTranPlans();

		try {
			this.docuMoviIngrs = this.docuMoviIngrLista.filtrarDocuMoviIngrs(docuMoviIngrs, this.persUsuaSesion,
					rolDocus);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al filtar documentos de pago"));
			e.printStackTrace();
		}

		try {
			docuMoviIngrPred = this.docuMoviIngrLista.buscarDocuMoviIngrPred(this.docuMoviIngrs, rolDocus);
			this.formPagoMoviIngr.setDocuMoviIngr(docuMoviIngrPred);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar documento predeterminado"));
			e.printStackTrace();
		}

		this.formPagoTmps = this.buscarFormPagos();
		this.filtrarFormPagoCompras();

		if (this.id == null) {

//			Tomando en cuenta que siempre sea el documento predeterminado PAGOS, 
//			se utiliza para poner el valor predeterminado en el campo tranPlanId
//			ya queLuego se ejecuta la linea this.buscarTranPlan();
//			que es la que pone el documento predeterminado en el campo
//			En el caso de que este puesto como predeterminado otro documento no afecta en nada ya que el campo TranPlanId 
//			solo sirve de referencia para saber que transaccion se hace en el caso que sea otro pago diferente de CXP
			for (TranPlan tranPlan : tranPlanTmps) {
				if (tranPlan.getTipo().equals("CXP")) {
					this.formPagoMoviIngr.setTranPlanId(tranPlan.getTranPlanId());
					break;
				}
			}

			this.buscarTranPlan();

			this.getFormPagoMoviIngr().setFecha(LocalDate.now());
			this.getFormPagoMoviIngr().setTotal(new BigDecimal(0));

		} else {

			try {

//				Se filtrar la forma de pago del documento que se esta recuperando,
//				ya que si no se hace esto no aparece la forma de pago
//				Se coloca antes de this.formPagoMoviIngr = formPagoMoviIngrRegis.buscarPorId(this.getId());
//				ya que al firtrar se pone null en Persona y al descargar el recibo de pago daria error

				this.filtrarFormPagoCompras();

				this.formPagoMoviIngr = formPagoMoviIngrRegis.buscarPorId(FormPagoMoviIngr.class, this.getId());
				this.buscarCxps(formPagoMoviIngr);
				this.buscarPagoDetas(formPagoMoviIngr);

//				Buscar y asignar transaccion si no tiene asignar una vacia
				if (this.formPagoMoviIngr.getTransaccion() != null) {

					Transaccion transaccion = this
							.buscarTransaccionPorId(this.formPagoMoviIngr.getTransaccion().getTransaccionId());
					this.formPagoMoviIngr.setTransaccion(transaccion);

				} else {
					
					DocuTran docuTran = new DocuTran();
					docuTran.setDocumento(new Documento());
					Transaccion transaccion = new Transaccion();
					transaccion.setDocuTran(docuTran);
					this.formPagoMoviIngr.setTransaccion(transaccion);
					
				}

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
				e.printStackTrace();
			}
		}

		this.filtrarTranPlans();

		// Esta parte se ejecuta cuando se abre la pagina desde la opcion pagar de la
		// pagina la compra la opcion pagar de la pagina de compra manda el parametro
		// ingresoId
		if (this.ingresoId != null) {

			List<Cxp> cxps = new ArrayList<>();

			cxps = this.buscarCxps(ingresoId);

			if (!cxps.isEmpty()) {
				this.formPagoMoviIngr.setPersona(cxps.get(0).getIngreso().getPersProv().getPersona());
				this.formPagoMoviIngr.setTotal(cxps.get(0).getTotal());
			}
		}
	}

	public String procesar() {

//		Integer transaccionId = 0;
//		String mensaje = validarGrabar();
//
//		if (!mensaje.equals("validado")) {
//			FacesContext.getCurrentInstance().addMessage(null,
//					new FacesMessage(FacesMessage.SEVERITY_INFO, null, mensaje));
//			return null;
//		}

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			if (this.id == null) {

				// Sucursal sucursal = variablesSesion.getSucursal();
				formPagoMoviIngr.setSucursal(this.getVariablesSesion().getSucursal());

//				Graba Fecha y Hora
				formPagoMoviIngr.setFechaHora(formPagoMoviIngr.getFecha().atTime(LocalTime.now()));
				
				// Grabar el número de cobro
				formPagoMoviIngr.setNumero(this.formPagoMoviIngr.getDocuMoviIngr().getDocumento().getNumero() + 1);

				// Actualizar secuencial
				this.formPagoMoviIngr.getDocuMoviIngr().getDocumento().setNumero(this.formPagoMoviIngr.getNumero());
				documentoRegis.modificar((this.getFormPagoMoviIngr().getDocuMoviIngr().getDocumento()));
				// Fin Actualizar secuencial
				
				formPagoMoviIngr.setEstado("PR");

				Object id = formPagoMoviIngrRegis.insertar(formPagoMoviIngr);
				this.id = (Integer) id;

				if (this.cxpSeles.size() != 0) {

					for (Cxp cxp : this.cxpSeles) {

						PagoDeta pagoDeta = new PagoDeta();
						pagoDeta.setFormPagoMoviIngr(formPagoMoviIngr);
						pagoDeta.setCxp(cxp);
						pagoDeta.setIngresoId(cxp.getIngreso().getIngresoId()); // Se registra como referencia para
																				// poder eliminar el abono en caso de NC
						pagoDeta.setTotal(cxp.getSaldo());

						pagoDetaRegis.insertar(pagoDeta);

						if ((cxp.getTotal().compareTo((pagoDeta.getTotal().add(cxp.getAbono()))) == 0)) {
							cxp.setEstado(true);
						} else {
							cxp.setEstado(false);
						}

						cxpRegis.modificar(cxp);
					}
				}
			}
			// Estos documentos no se pueden modificar hay que anularlos y volverlos a crear
			// else {
			// formPagoMoviIngrRegis.modificar(formPagoMoviIngr);
			// }
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		if (this.formPagoMoviIngr.getDocuMoviIngr().getDocumento().isContab()) {

			contabilizarPago(this.formPagoMoviIngr);
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "explora?faces-redirect=true&formPagoMoviIngrId=" + this.getId();
	}

	@Deprecated
	public String validarGrabar() {

		String mensaje = "validado";

		return mensaje;
	}

	public String modificar() {
		return "registra?faces-redirect=true&formPagoMoviIngrId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&formPagoMoviIngrId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {

			// En el metodo recuperar ya se asignan los regostros a las listas
			// para poder eliminar y modificar
			for (PagoDeta pagoDeta : this.pagoDetas) {

				cxps.add(pagoDeta.getCxp());
				pagoDetaRegis.eliminar(pagoDeta);
			}

			// Se cambia el estado a false de todas las CXP que se este anulando
			// ya que si se elimina un pago la cxp siempre estara pendiente
			// y si todavia estaba pendiente no afecta en nada
			for (Cxp cxp : cxps) {
				cxp.setEstado(false);
				cxpRegis.modificar(cxp);
			}

			FormPagoMoviIngr formPagoMoviIngr = formPagoMoviIngrRegis.buscarPorId(FormPagoMoviIngr.class, this.getId());
			formPagoMoviIngrRegis.eliminar(formPagoMoviIngr);

//			TODO: Implementar eliminar Transaccion Este boton esta desactivado en la pagina

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

	public void eliminarCxpSele() {

		// Se restablece el saldo antes de eliminar el registro de la lista ya que en el
		// caso que
		// se haya modificado el registro y se le haya borrado si se le vuelve a
		// seleccionar el registro
		// aparece con el saldo modificado
		this.cxpSele.setSaldo(this.cxpSele.getTotal().subtract(this.cxpSele.getAbono()));

		this.cxpSeles.remove(cxpSele);
		this.calcularTotal();

	}

	public void buscar() {

		try {

			formPagoMoviIngrLista.filasPagina(variablesSesion.getFilasPagina());

			this.formPagoMoviIngrs = formPagoMoviIngrLista.buscar(formPagoMoviIngr, this.pagina);
			this.numeroReg = formPagoMoviIngrs.size();
			this.contadorReg = formPagoMoviIngrLista.contarRegistros(formPagoMoviIngr);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public List<FormPagoMoviIngr> buscarTodo() {

		List<FormPagoMoviIngr> formPagoMoviIngrs = new ArrayList<>();

		try {
			formPagoMoviIngrs = formPagoMoviIngrLista.buscarTodo("formPagoMoviIngrId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return formPagoMoviIngrs;
	}

	// Busca PersClies pero retorna Personas;
	// Cuando se añada otros modulos cambiar a a buscar solo personas
	public List<Persona> buscarPersonas(Integer paginador) {

		if (paginador == 0) {
			this.paginaProv = 0;
		}

		if (this.personaBuscar.getCedulaRuc() != null) {
			this.personaBuscar.setApelli(null);
			this.personaBuscar.setNombre(null);
		}

		PersProv persProv = new PersProv();
		persProv.setPersona(this.personaBuscar);

		Collection<Persona> personasColeccion;
		List<Persona> personas = null;

		List<PersProv> persProvs = new ArrayList<>();

		persProv.setEstado(true);

		try {
			persProvLista.filasPagina(variablesSesion.getFilasPagina());
			persProvs = persProvLista.buscar(persProv, this.paginaProv);
			this.numeroRegProv = persProvs.size();
			this.contadorRegProv = persProvLista.contarRegistros(persProv);

			personasColeccion = CollectionUtils.collect(persProvs, TransformerUtils.invokerTransformer("getPersona"));
			personas = new ArrayList<>(personasColeccion);
			this.personas = personas;

		} catch (Exception e) {
			e.printStackTrace();
		}

		// RequestContext context = RequestContext.getCurrentInstance();
		// context.addCallbackParam("buscarPersona", true);

		return personas;
	}

	public List<DocuMoviIngr> buscarDocuMoviIngrs() {

		Documento documento = new Documento();
		documento.setEstado(true);

		DocuMoviIngr docuMoviIngr = new DocuMoviIngr();
		docuMoviIngr.setDocumento(documento);

		List<DocuMoviIngr> docuMoviIngrs = new ArrayList<>();

		try {
			docuMoviIngrs = docuMoviIngrLista.buscar(docuMoviIngr, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar DocuMoviIngr"));
			e.printStackTrace();
		}

		return docuMoviIngrs;
	}

	public List<FormPago> buscarFormPagos() {

		FormPago formPago = new FormPago();
		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 6050);
		} catch (Exception e1) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar módulo compra"));
			e1.printStackTrace();
		}

		formPago.setModulo(parametro.getDescri());
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

	public void buscarPagoDetas(FormPagoMoviIngr formPagoMoviIngr) {

		PagoDeta pagoDeta = new PagoDeta();

		Cxp cxp = new Cxp();
		Ingreso ingreso = new Ingreso();
		cxp.setIngreso(ingreso);
		pagoDeta.setCxp(cxp);

		FormPagoMoviIngr formPagoMoviIngrPagoDeta = new FormPagoMoviIngr();
		Persona persona = new Persona();
		FormPago formPago = new FormPago();
		formPagoMoviIngrPagoDeta.setPersona(persona);
		formPagoMoviIngrPagoDeta.setFormPago(formPago);
		formPagoMoviIngrPagoDeta.setFpmiId(formPagoMoviIngr.getFpmiId());

		pagoDeta.setFormPagoMoviIngr(formPagoMoviIngrPagoDeta);

		try {
			this.pagoDetas = pagoDetaLista.buscar(pagoDeta, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar detalle de pagos"));
			e.printStackTrace();
		}
	}

	public void buscarPagoDetas() {

		PagoDeta pagoDeta = new PagoDeta();

		Cxp cxp = new Cxp();
		cxp.setCxpId(this.cxpId);
		Ingreso ingreso = new Ingreso();
		cxp.setIngreso(ingreso);

		FormPagoMoviIngr formPagoMoviIngrPagoDeta = new FormPagoMoviIngr();
		Persona persona = new Persona();
		FormPago formPago = new FormPago();
		formPagoMoviIngrPagoDeta.setPersona(persona);
		formPagoMoviIngrPagoDeta.setFormPago(formPago);

		pagoDeta.setFormPagoMoviIngr(formPagoMoviIngrPagoDeta);
		pagoDeta.setCxp(cxp);

		try {
			this.pagoDetas = pagoDetaLista.buscar(pagoDeta, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar detalle de pagos"));
			e.printStackTrace();
		}
	}

	// Buscar CXP y pagos realizados a esta CXP para realizar los calculos de abonos
	// y saldos
	// Para buscar pagos se envia como parametro solamente la CXP
	// FormPagoMoviIngr y Persona deben ir sin datos de esta manera se buscar todos
	// los pagos realizados a una CXP,
	// sin importar quien ha hecho ni con que documento.
	// La entidad que se recibe como parametro (formPagoMoviIngr) se utiliza para
	// buscar las CXP de la persona que va a pagar
	public void buscarCxps(FormPagoMoviIngr formPagoMoviIngr) {

		PagoDeta pagoDeta = new PagoDeta();
		List<PagoDeta> pagoDetas = new ArrayList<>();

		// Entidad nueva para buscar PAGOS de una cuenta por pagar
		FormPagoMoviIngr formPagoMoviIngrPagoDeta = new FormPagoMoviIngr();
		formPagoMoviIngrPagoDeta.setPersona(new Persona());
		formPagoMoviIngrPagoDeta.setFormPago(new FormPago());

		PersProv persProv = new PersProv();
		if (formPagoMoviIngr.getPersona().getPersProv() == null) {
			persProv.setPersona(new Persona());
			persProv.setPersonaId(formPagoMoviIngr.getPersona().getPersonaId());
		} else {
			persProv = formPagoMoviIngr.getPersona().getPersProv();
		}

		Cxp cxp = new Cxp();
		Ingreso ingreso = new Ingreso();
		ingreso.setPersProv(persProv);
		cxp.setIngreso(ingreso);
		cxp.setEstado(false);

		try {

			this.cxps = cxpLista.buscar(cxp, null);

			for (Cxp cxpSaldo : cxps) {

				BigDecimal totalAbono = new BigDecimal(0);

				pagoDeta.setCxp(cxpSaldo);
				pagoDeta.setFormPagoMoviIngr(formPagoMoviIngrPagoDeta);

				pagoDetas = this.pagoDetaLista.buscar(pagoDeta, null);

				for (PagoDeta pagoDetaCxp : pagoDetas) {
					totalAbono = totalAbono.add(pagoDetaCxp.getTotal());
				}

//				Se realilza redondeo para solo para dar formato en la pagina
//				ya que este valor no se despliega en una caja de texto sino en un commanLink
				cxpSaldo.setAbono(totalAbono.setScale(2, RoundingMode.HALF_UP));
				cxpSaldo.setSaldo(cxpSaldo.getTotal().subtract(totalAbono));
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar cuentas por pagar"));
			e.printStackTrace();
		}

	}

	public List<Cxp> buscarCxps(Integer ingresoId) {

		PersProv persProv = new PersProv();
		persProv.setPersona(new Persona());
		Ingreso ingreso = new Ingreso();
		ingreso.setIngresoId(ingresoId);
		ingreso.setPersProv(persProv);

		Cxp cxp = new Cxp();
		cxp.setIngreso(ingreso);
		cxp.setEstado(false);

		try {
			this.cxps = cxpLista.buscar(cxp, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar Cuentas por pagar con ingres Id"));
			e.printStackTrace();
		}

		return cxps;

	}

	// Recupera el tipo de documento para generar transaccion
	public Documento buscarPorIdDocumento(Integer documentoId) throws Exception {
		return this.documentoRegis.buscarPorId(Documento.class, documentoId);
	}

	public List<ProvGrupPlanCuen> buscarPgpc(ProvGrupPlanCuen pgpc) throws Exception {
		return this.provGrupPlanCuenLista.buscar(pgpc, null);
	}

	public List<FormPagoPlanCuen> buscarFppc(FormPagoPlanCuen fppc) throws Exception {
		return this.formPagoPlanCuenLista.buscar(fppc, null);
	}

	public void seleccionarPersProv() {

		// Limpia la lista de cxpSeleccionadas y el total pago
		// Por ejemplos si se selecciono un proveedor y se seleccionaron cxp la lista
		// queda con registros
		// y en el caso que no se vaya a hacer el pago o se selecione otro proceedor la
		// lista queda con esos datos
		this.formPagoMoviIngr.setTotal(new BigDecimal(0));
		this.cxpSeles.clear();
		this.buscarCxps(this.formPagoMoviIngr);

		this.saldoProveedor();
	}

	public void onRowSelect(SelectEvent<?> event) {

		// FacesMessage msg = new FacesMessage("Cxp Selected", ((Cxp)
		// event.getObject()).getCxpId().toString());
		// FacesContext.getCurrentInstance().addMessage(null, msg);
		cxpSeles.add((Cxp) event.getObject());

		this.calcularTotal();

	}

	public void modificarCelda(CellEditEvent<?> event) {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		Cxp cxp = (Cxp) ((DataTable) event.getComponent()).getRowData();

		if (newValue != null && !newValue.equals(oldValue)) {

			if (cxp.getSaldo().compareTo(cxp.getTotal().subtract(cxp.getAbono())) == 1) {

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, null,
						"Valor abono es mayor que total cuenta por pagar"));

				cxp.setSaldo((BigDecimal) oldValue);

			}

			this.calcularTotal();
		}
	}

	public void calcularTotal() {

		BigDecimal totalPago = new BigDecimal(0);

		for (Cxp cxp : cxpSeles) {

			totalPago = totalPago.add(cxp.getSaldo());
		}

		this.formPagoMoviIngr.setTotal(totalPago);
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
		persProv.setPersona(this.formPagoMoviIngr.getPersona());

		Cxp cxp = new Cxp();
		Ingreso ingreso = new Ingreso();
		ingreso.setPersProv(persProv);
		cxp.setIngreso(ingreso);

		try {

			sumaCxp = this.cxpLista.sumarCxp(cxp);

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

		persProv.setPersona(this.formPagoMoviIngr.getPersona());
		ingreso.setPersProv(persProv);
		cxp.setIngreso(ingreso);
		pagoDeta.setCxp(cxp);

		try {
			sumaPagoDeta = this.pagoDetaLista.sumarPagoDeta(pagoDeta);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al sumar pagos"));
			e.printStackTrace();
		}

		return sumaPagoDeta;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public FormPagoMoviIngr getFormPagoMoviIngr() {
		return formPagoMoviIngr;
	}

	public void setFormPagoMoviIngr(FormPagoMoviIngr formPagoMoviIngr) {
		this.formPagoMoviIngr = formPagoMoviIngr;
	}

	public List<FormPagoMoviIngr> getFormPagoMoviIngrs() {
		return formPagoMoviIngrs;
	}

	public void setFormPagoMoviIngrs(List<FormPagoMoviIngr> formPagoMoviIngrs) {
		this.formPagoMoviIngrs = formPagoMoviIngrs;
	}

	public List<Persona> getPersonas() {
		return personas;
	}

	public void setPersonas(List<Persona> personas) {
		this.personas = personas;
	}

	public List<FormPago> getFormPagos() {
		return formPagos;
	}

	public void setFormPagos(List<FormPago> formPagos) {
		this.formPagos = formPagos;
	}

	public List<PagoDeta> getPagoDetas() {
		return pagoDetas;
	}

	public void setPagoDetas(List<PagoDeta> pagoDetas) {
		this.pagoDetas = pagoDetas;
	}

	public List<Cxp> getCxps() {
		return cxps;
	}

	public void setCxps(List<Cxp> cxps) {
		this.cxps = cxps;
	}

	public Integer getIngresoId() {
		return ingresoId;
	}

	public void setIngresoId(Integer ingresoId) {
		this.ingresoId = ingresoId;
	}

	public List<DocuMoviIngr> getDocuMoviIngrs() {
		return docuMoviIngrs;
	}

	public void setDocuMoviIngrs(List<DocuMoviIngr> docuMoviIngrs) {
		this.docuMoviIngrs = docuMoviIngrs;
	}

	public Persona getPersonaBuscar() {
		return personaBuscar;
	}

	public void setPersonaBuscar(Persona personaBuscar) {
		this.personaBuscar = personaBuscar;
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

	public int getNumeroRegProv() {
		return numeroRegProv;
	}

	public void setNumeroRegProv(int numeroRegProv) {
		this.numeroRegProv = numeroRegProv;
	}

	public Set<Cxp> getCxpSeles() {
		return cxpSeles;
	}

	public void setCxpSeles(Set<Cxp> cxpSeles) {
		this.cxpSeles = cxpSeles;
	}

	public Cxp getCxpSele() {
		return cxpSele;
	}

	public void setCxpSele(Cxp cxpSele) {
		this.cxpSele = cxpSele;
	}

	public Integer getCxpId() {
		return cxpId;
	}

	public void setCxpId(Integer cxpId) {
		this.cxpId = cxpId;
	}

	public BigDecimal getSaldoProveedor() {
		return saldoProveedor;
	}

	public void setSaldoProveedor(BigDecimal saldoProveedor) {
		this.saldoProveedor = saldoProveedor;
	}

	public List<TranPlan> getTranPlans() {
		return tranPlans;
	}

	public void setTranPlans(List<TranPlan> tranPlans) {
		this.tranPlans = tranPlans;
	}

	public TranPlan getTranPlan() {
		return tranPlan;
	}

	public void setTranPlan(TranPlan tranPlan) {
		this.tranPlan = tranPlan;
	}

}