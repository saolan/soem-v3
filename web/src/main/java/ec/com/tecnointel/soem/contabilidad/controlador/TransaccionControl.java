package ec.com.tecnointel.soem.contabilidad.controlador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;

import ec.com.tecnointel.soem.caja.listaInt.CajaMoviTranListaInt;
import ec.com.tecnointel.soem.caja.modelo.CajaMovi;
import ec.com.tecnointel.soem.caja.modelo.CajaMoviTran;
import ec.com.tecnointel.soem.caja.registroInt.CajaMoviRegisInt;
import ec.com.tecnointel.soem.contabilidad.listaInt.PlanCuenListaInt;
import ec.com.tecnointel.soem.contabilidad.listaInt.TranDetaListaInt;
import ec.com.tecnointel.soem.contabilidad.listaInt.TransaccionListaInt;
import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuen;
import ec.com.tecnointel.soem.contabilidad.modelo.TranDeta;
import ec.com.tecnointel.soem.contabilidad.modelo.Transaccion;
import ec.com.tecnointel.soem.contabilidad.registroInt.TranDetaRegisInt;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionRegisInt;
import ec.com.tecnointel.soem.egreso.listaInt.EgreTranListaInt;
import ec.com.tecnointel.soem.egreso.modelo.EgreTran;
import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.egreso.registroInt.EgresoRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.ingreso.listaInt.IngresoListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.parametro.listaInt.DocuTranListaInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuTran;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.registroInt.DocuTranRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.DocumentoRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.PersonaRegisInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolDocu;
import ec.com.tecnointel.soem.tesoreria.listaInt.FormPagoMoviEgreListaInt;
import ec.com.tecnointel.soem.tesoreria.listaInt.FormPagoMoviIngrListaInt;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviEgre;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class TransaccionControl extends PaginaControl implements Serializable {

	private long contadorRegPlanCuen;
	private int numeroRegPlanCuen;

	private Integer paginaPlanCuen;
	private int filasTranDetas;

	private BigDecimal totalDebe;
	private BigDecimal totalHabe;
	private BigDecimal difere;

	// private DocuTran docuTran;
	private Transaccion transaccion;
	private TranDeta tranDeta;
	private PlanCuen planCuenBuscar;

	private List<DocuTran> docuTrans;
	private List<PlanCuen> planCuens;
	private List<Transaccion> transaccions;
	private List<TranDeta> tranDetas;
	private List<TranDeta> tranDetaEliminados;
	private List<Parametro> parametroEstados;
	private List<RolDocu> rolDocus;

	private List<Ingreso> ingresos;
	private List<FormPagoMoviIngr> formPagoMoviIngrs;
	private List<EgreTran> egreTrans;

	private List<FormPagoMoviEgre> formPagoMoviEgres;

	private List<CajaMoviTran> cajaMoviTrans;

	@Inject
	DocuTranListaInt docuTranLista;

	@Inject
	PlanCuenListaInt planCuenLista;

	@Inject
	TransaccionRegisInt transaccionRegis;

	@Inject
	TransaccionListaInt transaccionLista;

	@Inject
	TranDetaRegisInt tranDetaRegis;

	@Inject
	TranDetaListaInt tranDetaLista;

	@Inject
	DocumentoRegisInt documentoRegis;

	@Inject
	DocuTranRegisInt docuTranRegis;

	@Inject
	IngresoListaInt ingresoLista;

	@Inject
	FormPagoMoviIngrListaInt formPagoMoviIngrLista;

	@Inject
	EgreTranListaInt egreTranLista;

	@Inject
	FormPagoMoviEgreListaInt formPagoMoviEgreLista;

	@Inject
	CajaMoviTranListaInt cajaMoviTranLista;

	@Inject
	CajaMoviRegisInt cajaMoviRegis;

	@Inject
	EgresoRegisInt egresoRegis;

	@Inject
	PersonaRegisInt personaRegis;

	private static final long serialVersionUID = -8972585226838186514L;

	@PostConstruct
	public void cargar() {

		DocuTran docuTran = new DocuTran();
		docuTran.setDocumento(new Documento());

		transaccion = new Transaccion();
		transaccion.setDocuTran(docuTran);

		tranDeta = new TranDeta();
		planCuenBuscar = new PlanCuen();

		this.tranDetas = new ArrayList<>();
		this.tranDetaEliminados = new ArrayList<>();

		this.ingresos = new ArrayList<>();
		this.formPagoMoviIngrs = new ArrayList<>();
		this.egreTrans = new ArrayList<>();
		this.formPagoMoviEgres = new ArrayList<>();
		this.cajaMoviTrans = new ArrayList<>();

//		Date fecha = Util.cambiarFormatoFecha(new Date(), "dd-MM-yyyy");
//		transaccion.setFechaEmis(fecha);
		transaccion.setFechaEmis(LocalDate.now());

		transaccion.setEstado("GR");

		rolDocus = variablesSesion.getRolDocus();

//		Esto va aqui para que se llene las lista de documentos al abrir transaccionLista 
		this.docuTrans = this.buscarDocuTrans();
	}

	public String anular() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {

			this.eliminarTranDetas();
			this.transaccion.setEstado("AN");
			this.transaccionRegis.modificar(transaccion);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Transacción Eliminada"));

		return "lista?faces-redirect=true";
	}

	public void agregarTranDeta() {

		this.buscarPlanCuens(0);

		if (this.planCuens.size() == 1) {

			TranDeta tranDeta = new TranDeta();

			tranDeta.setPlanCuen(this.planCuens.get(0));
			tranDeta.setTotal(new BigDecimal(0));

			this.tranDetas.add(tranDeta);

//			RequestContext.getCurrentInstance().execute("myAlert('alerta');");
//			RequestContext.getCurrentInstance().update(":planCuenRegistra:tranDetaList,:planCuenRegistra:filasTranDetas");
//			RequestContext.getCurrentInstance().execute("moverCursorDetalle('planCuenRegistra:codigoPlanCuenAux','planCuenRegistra:tranDetaList'," + this.tranDetas.size() + "})");

		} else {
			if (this.planCuenBuscar.getCodigo() != "") {
//				RequestContext.getCurrentInstance().execute("PF('seleccionCuenta').show();");
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Cuenta contable no existe"));

			}
		}

		this.planCuenBuscar.setCodigo(null);
		this.filasTranDetas = this.tranDetas.size();
		this.calcularTotal();
	}

	public CajaMovi buscarCajaMoviPorId(Integer cajaMoviId) {

		CajaMovi cajaMovi = new CajaMovi();

		try {
			cajaMovi = this.cajaMoviRegis.buscarPorId(CajaMovi.class, cajaMoviId);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar movimiento de caja por id"));
		}

		return cajaMovi;
	}

	public Egreso buscarEgresoPorId(Integer egresoId) {

		Egreso egreso = new Egreso();

		try {
			egreso = this.egresoRegis.buscarPorId(Egreso.class, egresoId);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar egreso por id"));
		}

		return egreso;
	}

	public List<DocuTran> buscarDocuTrans() {

		DocuTran docuTranPred = new DocuTran();

		Documento documento = new Documento();
		documento.setEstado(true);

		DocuTran docuTranBuscar = new DocuTran();
		docuTranBuscar.setDocumento(documento);

		List<DocuTran> docuTrans = new ArrayList<>();

		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");

		try {
			docuTrans = this.docuTranLista.buscar(docuTranBuscar, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar documentos transacción"));
			e.printStackTrace();
		}
//		TODO: Se desactiva fitro no funciona no encuentra documento a pesar que esta en la lista
//		revisar rolDocus = rolDocuLista.buscar(rolDocu, null); en IniciarSesionControl tambien aparece 
//		roldocus pero en documento sale nulo
//		try {
//			docuTrans = this.docuTranLista.filtrarDocuTrans(docuTrans, persUsuaSesion, variablesSesion.getRolDocus());
//		} catch (Exception e) {
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
//					"Excepcion - Error al filtrar documentos transacción"));
//			e.printStackTrace();
//		}

		// Seleccionar el documento predetermido en rolDocu
		for (DocuTran docuTran : docuTrans) {
			for (RolDocu rolDocu : rolDocus) {
				if (docuTran.getDocumentoId().equals(rolDocu.getDocumento().getDocumentoId())
						&& rolDocu.getPredet() == true) {
					docuTranPred = docuTran;
				}
			}
		}

		this.transaccion.setDocuTran(docuTranPred);

		return docuTrans;
	}

	public Documento buscarDocumento(Documento documento) {
		try {

			return documentoRegis.buscarPorId(Documento.class, documento.getDocumentoId());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar documentos"));
			e.printStackTrace();
		}

		return null;
	}

	public List<Ingreso> buscarDocumentoFuente(Ingreso ingreso) {

		List<Ingreso> ingresos = new ArrayList<>();

		try {
			ingresos = this.ingresoLista.buscarTransaccion(ingreso);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar documento fuente compra"));
			e.printStackTrace();
		}

		return ingresos;
	}

	public List<FormPagoMoviIngr> buscarDocumentoFuente(FormPagoMoviIngr formPagoMoviIngr) {

		List<FormPagoMoviIngr> formPagoMoviIngrs = new ArrayList<>();

		try {
			formPagoMoviIngrs = this.formPagoMoviIngrLista.buscarTransaccion(formPagoMoviIngr);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar documento fuente pago"));
		}

		return formPagoMoviIngrs;
	}

	public List<EgreTran> buscarDocumentoFuente(EgreTran egreTran) {

		List<EgreTran> egreTrans = new ArrayList<>();

		try {

			egreTrans = this.egreTranLista.buscar(egreTran, null);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Error al buscar documento fuente en transaccion egresos"));
		}

		for (EgreTran egreTranRecorrer : egreTrans) {

			Egreso egreso = this.buscarEgresoPorId(egreTranRecorrer.getEgreso().getEgresoId());
			egreTranRecorrer.setEgreso(egreso);

			Persona persona = new Persona();
			persona = this.buscarPersonaPorId(egreTranRecorrer.getEgreso().getPersClie().getPersonaId());

			egreTranRecorrer.getEgreso().getPersClie().setPersona(persona);
		}

		return egreTrans;
	}

	public List<FormPagoMoviEgre> buscarDocumentoFuente(FormPagoMoviEgre formPagoMoviEgre) {

		List<FormPagoMoviEgre> formPagoMoviEgres = new ArrayList<>();

		try {
			formPagoMoviEgres = this.formPagoMoviEgreLista.buscarTransaccion(formPagoMoviEgre);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar documento fuente cobro"));
		}

		return formPagoMoviEgres;
	}

	public List<CajaMoviTran> buscarDocumentoFuente(CajaMoviTran cajaMoviTran) {

		List<CajaMoviTran> cajaMoviTrans = new ArrayList<>();

		try {

			cajaMoviTrans = this.cajaMoviTranLista.buscar(cajaMoviTran, null);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Error al buscar documento fuente movimiento de caja"));
		}

		for (CajaMoviTran cajaMoviTranRecorrer : cajaMoviTrans) {

			CajaMovi cajaMovi = this.buscarCajaMoviPorId(cajaMoviTranRecorrer.getCajaMovi().getCajaMoviId());
			cajaMoviTranRecorrer.setCajaMovi(cajaMovi);

			Persona persona = new Persona();
			persona = this.buscarPersonaPorId(cajaMoviTranRecorrer.getCajaMovi().getPersCaje().getPersonaId());

			cajaMoviTranRecorrer.getCajaMovi().getPersCaje().setPersona(persona);
		}

		return cajaMoviTrans;
	}

	public List<TranDeta> buscarTranDetas(TranDeta tranDeta) {

		List<TranDeta> tranDetas = new ArrayList<>();

		try {

//			Se comenta esta linea porque no se quiere utilizar paginacion
//			y por eso el segundo paramero del metodo buscar es null
//			Si se deseara paginar esta linea es obligarotia ya que sino 
//			no se realiza la busqueda
//			tranDetaLista.filasPagina(variablesSesion.getFilasPagina());
			tranDetas = tranDetaLista.buscar(tranDeta, null);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al detalle de transacción"));
			e.printStackTrace();
		}

		return tranDetas;
	}

	public void buscarTransaccions() {

		try {

			this.buscarTransaccionEstado();

			transaccionLista.filasPagina(variablesSesion.getFilasPagina());

			this.transaccions = transaccionLista.buscar(transaccion, this.pagina);
			this.numeroReg = transaccions.size();
			this.contadorReg = transaccionLista.contarRegistros(transaccion);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar transacción"));
			e.printStackTrace();
		}
	}

	public void buscarTransaccionEstado() {

		Parametro parametro = new Parametro();

		parametro.setCodigo("Parametro-Estado");
		parametro.setEstado(true);

		try {
			this.parametroEstados = parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepción- Error al buscar estado de documentos"));
			e.printStackTrace();
		}

	}

	public List<PlanCuen> buscarPlanCuens(Integer paginador) {

		if (paginador == 0) {
			this.paginaPlanCuen = 0;
		}

		if (this.planCuenBuscar.getCodigo() != null) {
			this.planCuenBuscar.setDescri(null);
		}

		List<PlanCuen> planCuens = new ArrayList<>();
		this.planCuenBuscar.setDetall(true);
		this.planCuenBuscar.setEstado(true);

		try {

			planCuenLista.filasPagina(variablesSesion.getFilasPagina());
			planCuens = this.planCuenLista.buscar(this.planCuenBuscar, this.paginaPlanCuen);

			this.numeroRegPlanCuen = planCuens.size();
			this.contadorRegPlanCuen = planCuenLista.contarRegistros(planCuenBuscar);

			this.planCuens = planCuens;

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Plan de Cuentas"));
			e.printStackTrace();
		}

		return planCuens;
	}

	public Persona buscarPersonaPorId(Integer personaid) {

		Persona persona = new Persona();

		try {
			persona = this.personaRegis.buscarPorId(Persona.class, personaid);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar persona por id"));
		}

		return persona;

	}

	public void calcularTotal() {

		this.totalDebe = new BigDecimal(0);
		this.totalHabe = new BigDecimal(0);
		this.difere = new BigDecimal(0);

		for (TranDeta tranDeta : tranDetas) {

			if (tranDeta.getTotal().compareTo(new BigDecimal(0)) > 0) {
				this.totalDebe = this.totalDebe.add(tranDeta.getTotal());
			} else {
				this.totalHabe = this.totalHabe.add(tranDeta.getTotal());
			}

			this.difere = this.difere.add(tranDeta.getTotal());
		}

	}

	public void descargar() {

		if (this.id == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Error - Por favor primero grabe o procese el documento para poder descargarlo"));
			return;
		}
		// Nombre que esta en archivo fuente en jasperReport
		String nombreReporte = this.getTransaccion().getDocuTran().getDocumento().getFormat();

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		Parametro parametro = new Parametro();

		String rutaJrxml; // = "C:\\tecnoIntel\\soem\\jasperReportes\\ingreso\\";
		String rutaReporteCompilado = "\\jasperReportes\\contabilidad\\";

		try {

			Integer transaccionId = this.getTransaccion().getTransaccionId();

			if (transaccionId == null) {
				parametrosJasper.put("transaccionId", 0);
			} else {
				parametrosJasper.put("transaccionId", transaccionId);
			}

			parametro = parametroRegis.buscarPorId(Parametro.class, 2000);
			rutaJrxml = parametro.getDescri();

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper, rutaJrxml, rutaReporteCompilado,
					formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al descargar documento"));
			e.printStackTrace();
		}
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {

			this.eliminarTranDetas();
			transaccionRegis.eliminar(this.getTransaccion());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Transacción Eliminada"));

		return "lista?faces-redirect=true";
	}

	public void eliminarTranDeta() {

		this.tranDetas.remove(tranDeta);
		this.filasTranDetas = this.tranDetas.size();
		this.calcularTotal();

		if (tranDeta.getTranDetaId() != null) {

			tranDetaEliminados.add(tranDeta);

		}
	}

	public void eliminarTranDetas() {

		for (TranDeta tranDeta : this.tranDetas) {
			try {

				tranDetaRegis.eliminar(tranDeta);

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al eliminar detalle de transacción"));
				e.printStackTrace();
			}
		}
	}

	public String explorar() {
		return "explora?faces-redirect=true&transaccionId=" + this.getId();
	}

	public String grabar() {

		// Los mensajes tienen RequestScope y se borran al navegar a otro pagina
		// Con este comando se guarda los mensages de confirmaci�n en navegacion
		// entre paginas
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		if (this.difere.compareTo(new BigDecimal(0)) == 0) {
			this.transaccion.setEstado("PR");
		} else {
			this.transaccion.setEstado("GR");
		}

		this.transaccion.setFechaHoraEmis(this.transaccion.getFechaEmis().atTime(LocalTime.now()));
		this.transaccion.setFechaRegi(LocalDate.now());
		this.transaccion.setFechaHoraRegi(LocalDateTime.now());
		
		this.transaccion.setTotalDebe(totalDebe);
		this.transaccion.setTotalHabe(totalHabe);
		this.transaccion.setDifere(difere);

		try {
			if (this.id == null) {

				// Grabar el número de la transaccion
				// Recuperar el último número de transaccion
				Documento documento = new Documento();
				documento = this.buscarDocumento(this.transaccion.getDocuTran().getDocumento());
				this.transaccion.setNumero(documento.getNumero() + 1);

				// Actualizar número secuencial
				documento.setNumero(this.transaccion.getNumero());
				this.documentoRegis.modificar(documento);

				Object id = transaccionRegis.insertar(transaccion);
				this.id = (Integer) id;

			} else {
				transaccionRegis.modificar(transaccion);
			}

			for (TranDeta tranDeta : this.tranDetas) {

				tranDeta.setTransaccion(this.transaccion);
//				Graba la fecha de la cabecera en el detalle porque con esta fecha se hacen los balances
				tranDeta.setFechaEmis(transaccion.getFechaEmis());
				tranDeta.setFechaHoraEmis(transaccion.getFechaHoraEmis());
//				Graba la nota de la cabecera en los detalles
				tranDeta.setNota(transaccion.getNota());

				if (tranDeta.getTranDetaId() == null) {
					tranDetaRegis.insertar(tranDeta);
				} else {
					tranDetaRegis.modificar(tranDeta);
				}
			}

//				Elimina tranDeta
			for (TranDeta tranDeta : this.tranDetaEliminados) {
				tranDetaRegis.eliminar(tranDeta);
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();

			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado " + transaccion.getDocuTran().getDocumento().getDescri() + " " + transaccion.getNumero()));

		return "explora?faces-redirect=true&transaccionId=" + this.getId();
		
	}

	public String modificar() {
		return "registra?faces-redirect=true&transaccionId=" + this.getId();
	}

	public void modificarCelda(CellEditEvent<?> event) {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

//		TranDeta tranDeta = (TranDeta) ((DataTable) event.getComponent()).getRowData();

		if (newValue != null && !newValue.equals(oldValue)) {

			// if
			// (tranDeta.getSaldo().compareTo(tranDeta.getTotal().subtract(tranDeta.getAbono()))
			// == 1 ) {
			// FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
			// FacesMessage.SEVERITY_WARN, null, "Valor abono es mayor que total cuenta por
			// cobrar"));
			// tranDeta.setSaldo((BigDecimal) oldValue);
			// }

			this.calcularTotal();
		}
	}

	public void onRowSelect(SelectEvent<?> event) {

		// FacesMessage msg = new FacesMessage("Cxp Selected", ((Cxp)
		// event.getObject()).getCxpId().toString());
		// FacesContext.getCurrentInstance().addMessage(null, msg);
		TranDeta tranDeta = new TranDeta();
		PlanCuen planCuen = new PlanCuen();

		planCuen = (PlanCuen) event.getObject();
		tranDeta.setPlanCuen(planCuen);
		tranDeta.setTotal(new BigDecimal(0));

		this.tranDetas.add(tranDeta);
		this.filasTranDetas = this.tranDetas.size();

		this.calcularTotal();

		PrimeFaces.current().executeScript("PF('seleccionCuenta').hide();");
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback()) {
			return;
		}

		TranDeta tranDeta = new TranDeta();

		if (this.id == null) {

			this.transaccion = new Transaccion();
			this.transaccion.setFechaEmis(LocalDate.now());
			this.transaccion.setNumero(0);

			this.docuTrans = this.buscarDocuTrans();
			
		} else {

			try {

				this.transaccion = transaccionRegis.buscarPorId(Transaccion.class, this.getId());
				tranDeta.setTransaccion(this.transaccion);

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al buscar transacción por Id"));
				e.printStackTrace();
			}

			this.tranDetas = this.buscarTranDetas(tranDeta);

			this.filasTranDetas = this.tranDetas.size();
			this.calcularTotal();

//			Buscar de que documento se genero la transaccion
//			y despliega esta informacion en transaccion-explora
			Transaccion transaccion = new Transaccion();
			transaccion.setTransaccionId(this.getTransaccion().getTransaccionId());

			Ingreso ingreso = new Ingreso();
			ingreso.setTransaccion(transaccion);
			this.ingresos = this.buscarDocumentoFuente(ingreso);

			FormPagoMoviIngr formPagoMoviIngr = new FormPagoMoviIngr();
			formPagoMoviIngr.setTransaccion(transaccion);
			this.formPagoMoviIngrs = this.buscarDocumentoFuente(formPagoMoviIngr);

			EgreTran egreTran = new EgreTran(new Egreso(), new Transaccion());
			egreTran.setTransaccion(transaccion);
			this.egreTrans = this.buscarDocumentoFuente(egreTran);

			FormPagoMoviEgre formPagoMoviEgre = new FormPagoMoviEgre();
			formPagoMoviEgre.setTransaccion(transaccion);
			this.formPagoMoviEgres = this.buscarDocumentoFuente(formPagoMoviEgre);

			CajaMoviTran cajaMoviTran = new CajaMoviTran(new CajaMovi(), new Transaccion());
			cajaMoviTran.setTransaccion(transaccion);
			this.cajaMoviTrans = this.buscarDocumentoFuente(cajaMoviTran);
		}
	}

	public Transaccion getTransaccion() {
		return transaccion;
	}

	public void setTransaccion(Transaccion transaccion) {
		this.transaccion = transaccion;
	}

	public List<Transaccion> getTransaccions() {
		return transaccions;
	}

	public void setTransaccions(List<Transaccion> transaccions) {
		this.transaccions = transaccions;
	}

	public List<DocuTran> getDocuTrans() {
		return docuTrans;
	}

	public void setDocuTrans(List<DocuTran> docuTrans) {
		this.docuTrans = docuTrans;
	}

	public PlanCuen getPlanCuenBuscar() {
		return planCuenBuscar;
	}

	public void setPlanCuenBuscar(PlanCuen planCuenBuscar) {
		this.planCuenBuscar = planCuenBuscar;
	}

	public Integer getPaginaPlanCuen() {
		return paginaPlanCuen;
	}

	public void setPaginaPlanCuen(Integer paginaPlanCuen) {
		this.paginaPlanCuen = paginaPlanCuen;
	}

	public int getNumeroRegPlanCuen() {
		return numeroRegPlanCuen;
	}

	public void setNumeroRegPlanCuen(int numeroRegPlanCuen) {
		this.numeroRegPlanCuen = numeroRegPlanCuen;
	}

	public long getContadorRegPlanCuen() {
		return contadorRegPlanCuen;
	}

	public void setContadorRegPlanCuen(long contadorRegPlanCuen) {
		this.contadorRegPlanCuen = contadorRegPlanCuen;
	}

	public List<PlanCuen> getPlanCuens() {
		return planCuens;
	}

	public void setPlanCuens(List<PlanCuen> planCuens) {
		this.planCuens = planCuens;
	}

	public List<TranDeta> getTranDetas() {
		return tranDetas;
	}

	public void setTranDetas(List<TranDeta> tranDetas) {
		this.tranDetas = tranDetas;
	}

	public TranDeta getTranDeta() {
		return tranDeta;
	}

	public void setTranDeta(TranDeta tranDeta) {
		this.tranDeta = tranDeta;
	}

	public BigDecimal getTotalDebe() {
		return totalDebe;
	}

	public void setTotalDebe(BigDecimal totalDebe) {
		this.totalDebe = totalDebe;
	}

	public BigDecimal getTotalHabe() {
		return totalHabe;
	}

	public void setTotalHabe(BigDecimal totalHabe) {
		this.totalHabe = totalHabe;
	}

	public BigDecimal getDifere() {
		return difere;
	}

	public void setDifere(BigDecimal difere) {
		this.difere = difere;
	}

	public int getFilasTranDetas() {
		return filasTranDetas;
	}

	public void setFilasTranDetas(int filasTranDetas) {
		this.filasTranDetas = filasTranDetas;
	}

	public List<Parametro> getParametroEstados() {
		return parametroEstados;
	}

	public void setParametroEstados(List<Parametro> parametroEstados) {
		this.parametroEstados = parametroEstados;
	}

	public List<Ingreso> getIngresos() {
		return ingresos;
	}

	public void setIngresos(List<Ingreso> ingresos) {
		this.ingresos = ingresos;
	}

	public List<FormPagoMoviIngr> getFormPagoMoviIngrs() {
		return formPagoMoviIngrs;
	}

	public void setFormPagoMoviIngrs(List<FormPagoMoviIngr> formPagoMoviIngrs) {
		this.formPagoMoviIngrs = formPagoMoviIngrs;
	}

	public List<FormPagoMoviEgre> getFormPagoMoviEgres() {
		return formPagoMoviEgres;
	}

	public void setFormPagoMoviEgres(List<FormPagoMoviEgre> formPagoMoviEgres) {
		this.formPagoMoviEgres = formPagoMoviEgres;
	}

	public List<EgreTran> getEgreTrans() {
		return egreTrans;
	}

	public void setEgreTrans(List<EgreTran> egreTrans) {
		this.egreTrans = egreTrans;
	}

	public List<CajaMoviTran> getCajaMoviTrans() {
		return cajaMoviTrans;
	}

	public void setCajaMoviTrans(List<CajaMoviTran> cajaMoviTrans) {
		this.cajaMoviTrans = cajaMoviTrans;
	}

}