package ec.com.tecnointel.soem.docuElec.controlador;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import javax.sql.DataSource;

import ec.com.tecnointel.soem.caja.modelo.Caja;
import ec.com.tecnointel.soem.caja.modelo.CajaMovi;
import ec.com.tecnointel.soem.documeElec.modelo.factura.Factura;
import ec.com.tecnointel.soem.documeElec.modelo.guia.GuiaRemision;
import ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.LiquidacionCompra;
import ec.com.tecnointel.soem.documeElec.modelo.notaCredito.NotaCredito;
import ec.com.tecnointel.soem.documeElec.modelo.notaDebito.NotaDebito;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.ComprobanteRetencion;
import ec.com.tecnointel.soem.documeElec.registroInt.DocumeElecRegisInt;
import ec.com.tecnointel.soem.documeElec.tareas.ManejadorTareaAutoriDocu;
import ec.com.tecnointel.soem.documeElec.tareas.ManejadorTareaEnviarCorreoDocu;
import ec.com.tecnointel.soem.documeElec.tareas.ManejadorTareaEnviarDocu;
import ec.com.tecnointel.soem.egreso.controlador.TareaCrearRideFacturaPdf;
import ec.com.tecnointel.soem.egreso.listaInt.EgreDetaImpuListaInt;
import ec.com.tecnointel.soem.egreso.listaInt.EgreDetaListaInt;
import ec.com.tecnointel.soem.egreso.listaInt.EgreNotaListaInt;
import ec.com.tecnointel.soem.egreso.listaInt.EgresoListaInt;
import ec.com.tecnointel.soem.egreso.modelo.EgreDeta;
import ec.com.tecnointel.soem.egreso.modelo.EgreDetaImpu;
import ec.com.tecnointel.soem.egreso.modelo.EgreNota;
import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.egreso.modelo.PersClie;
import ec.com.tecnointel.soem.egreso.registroInt.EgresoRegisInt;
import ec.com.tecnointel.soem.firmaElec.registroInt.FirmarArchivoInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.ingreso.controlador.TareaCrearRideRetencionPdf;
import ec.com.tecnointel.soem.ingreso.listaInt.IngrDetaImpuListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.IngrDetaListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.IngresoListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.ReteDetaListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.RetencionListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDeta;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDetaImpu;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.modelo.PersProv;
import ec.com.tecnointel.soem.ingreso.modelo.ReteDeta;
import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import ec.com.tecnointel.soem.ingreso.registroInt.IngresoRegisInt;
import ec.com.tecnointel.soem.ingreso.registroInt.PersProvRegisInt;
import ec.com.tecnointel.soem.ingreso.registroInt.RetencionRegisInt;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.DocuEgre;
import ec.com.tecnointel.soem.parametro.modelo.DocuIngr;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.registroInt.DimmRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.DocuIngrRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.SucursalRegisInt;
import ec.com.tecnointel.soem.serWebSri.registroInt.AutorizacionComprobantesWsInt;
import ec.com.tecnointel.soem.tesoreria.listaInt.CobrDetaListaInt;
import ec.com.tecnointel.soem.tesoreria.listaInt.FormPagoMoviEgreListaInt;
import ec.com.tecnointel.soem.tesoreria.listaInt.FormPagoMoviIngrListaInt;
import ec.com.tecnointel.soem.tesoreria.listaInt.FpmeFormPagoListaInt;
import ec.com.tecnointel.soem.tesoreria.modelo.CobrDeta;
import ec.com.tecnointel.soem.tesoreria.modelo.Cxc;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviEgre;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import ec.com.tecnointel.soem.tesoreria.modelo.FpmeFormPago;
import ec.com.tecnointel.soem.tesoreria.registroInt.FpmeFormPagoRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

@Named
@ViewScoped
public class DocuElecControl extends PaginaControl implements Serializable {

	private String orden;

	private Boolean reenviarEgresosRecibida;
	private Boolean reenviarIngresosRecibida;
	private Boolean reenviarRetencionesRecibida;

	private LocalDate fechaDesde;
	private LocalDate fechaHasta;

	private Egreso egreso;
	private Ingreso ingreso;
	private Retencion retencion;

	private Dimm dimmIva;

	private List<Egreso> egresos;
	private List<Egreso> egresoNoEnviados;
	private List<Egreso> egresoRecibidos;

	private List<Retencion> retencions;
	private List<Retencion> retencionNoEnviados;
	private List<Retencion> retencionRecibidos;

	private List<Ingreso> ingresos;
	private List<Ingreso> ingresoNoEnviados;
	private List<Ingreso> ingresoRecibidos;

	Set<Integer> sucursals = new HashSet<>();

	private Boolean enviarCorreo;

	private Boolean buscarSucursalSesion;

	private boolean bloquearBotonProcesarTodos = false;

	@Inject
	EgresoRegisInt egresoRegis;

	@Inject
	EgresoListaInt egresoLista;

	@Inject
	EgreNotaListaInt egreNotaLista;

	@Inject
	EgreDetaListaInt egreDetaLista;

	@Inject
	EgreDetaImpuListaInt egreDetaImpuLista;

	@Inject
	FormPagoMoviEgreListaInt formPagoMoviEgreLista;

	@Inject
	FpmeFormPagoRegisInt fpmeFormPagoRegis;

	@Inject
	FpmeFormPagoListaInt fpmeFormPagoLista;

	@Inject
	CobrDetaListaInt cobrDetaLista;

	@Inject
	RetencionRegisInt retencionRegis;

	@Inject
	RetencionListaInt retencionLista;

	@Inject
	ReteDetaListaInt reteDetaLista;

	@Inject
	IngresoRegisInt ingresoRegis;

	@Inject
	IngresoListaInt ingresoLista;

	@Inject
	IngrDetaListaInt ingrDetaLista;

	@Inject
	IngrDetaImpuListaInt ingrDetaImpuList;

	@Inject
	FormPagoMoviIngrListaInt formPagoMoviIngrLista;

	@Inject
	IngrDetaImpuListaInt ingrDetaImpuLista;

	@Inject
	PersProvRegisInt persProvRegis;

	@Inject
	SucursalRegisInt sucursalRegis;

	@Inject
	DocumeElecRegisInt documeElectRegis;

	@Inject
	DocuIngrRegisInt docuIngrRegis;

	@Inject
	DimmRegisInt dimmRegis;

	@Inject
	AutorizacionComprobantesWsInt autorizacionComprobantes;

	@Inject
	ManejadorTareaEnviarDocu manejadorTareaEnvioDocu;

	@Inject
	ManejadorTareaAutoriDocu manejadorTareaAutoriDocu;

	@Inject
	ManejadorTareaEnviarCorreoDocu manejadorTareaEnviarCorreoDocu;

	@Inject
	FirmarArchivoInt firmarArchivo;

	@Resource(mappedName = "java:jboss/datasources/soemDS")
	private DataSource dataSource;

	private static final long serialVersionUID = -7215087044771123778L;

	@PostConstruct
	public void cargar() {

		this.orden = "Asc";

		egresoNoEnviados = new ArrayList<>();
		egresoRecibidos = new ArrayList<>();

		ingresoNoEnviados = new ArrayList<>();
		ingresoRecibidos = new ArrayList<>();

		retencionNoEnviados = new ArrayList<>();
		retencionRecibidos = new ArrayList<>();

		PersClie persClie = new PersClie();
		persClie.setPersona(new Persona());

		PersProv persProv = new PersProv();
		persProv.setPersona(new Persona());

		DocuEgre docuEgre = new DocuEgre();
		docuEgre.setDocumento(new Documento());

		CajaMovi cajaMovi = new CajaMovi();
		cajaMovi.setCaja(new Caja());

		DocuIngr docuIngr = new DocuIngr();
		docuIngr.setDocumento(new Documento());

		egreso = new Egreso();
		egreso.setSucursal(new Sucursal());
		egreso.setPersClie(persClie);
		egreso.setDocuEgre(docuEgre);
		egreso.setCajaMovi(cajaMovi);

//		Date fechaEgreso = Util.cambiarFormatoFecha(new Date(), "dd-MM-yyyy");
//		egreso.setFechaEmis(fechaEgreso);
		egreso.setFechaEmis(LocalDate.now());

		egreso.setEstadoDocuElec("AUTORIZADO");
		egreso.getDocuEgre().setDocumeElec("Ninguno");
		egreso.setEstado("PR");

		// Ingreso para las liquidaciones
		ingreso = new Ingreso();
		ingreso.setSucursal(new Sucursal());
		ingreso.setPersProv(persProv);
		ingreso.setDocuIngr(docuIngr);

//		Date fechaIngreso = Util.cambiarFormatoFecha(new Date(), "dd-MM-yyyy");
//		ingreso.setFechaEmis(fechaIngreso);
		ingreso.setFechaEmis(LocalDate.now());

		ingreso.setEstadoDocuElec("AUTORIZADO");
		ingreso.getDocuIngr().setDocumeElec("Ninguno");
		ingreso.setEstado("PR");

		retencion = new Retencion();
		retencion.setIngreso(new Ingreso());

//		Date fechaRetencion = Util.cambiarFormatoFecha(new Date(), "dd-MM-yyyy");
//		retencion.setFechaEmis(fechaRetencion);
		retencion.setFechaEmis(LocalDate.now());

		// Ingreso para la retención
		Ingreso ingreso = new Ingreso();
		ingreso.setDocuIngr(new DocuIngr());

		retencion.setIngreso(ingreso);
		retencion.setEstadoDocuElec("AUTORIZADO");
		retencion.getIngreso().getDocuIngr().setTipoRete("Electronica");
		retencion.setEstado("PR");

		this.enviarCorreo = true;
		buscarSucursalSesion = false;

		this.reenviarEgresosRecibida = false;
		this.reenviarIngresosRecibida = false;
		this.reenviarRetencionesRecibida = false;

		try {
//			this.dimmIva = this.dimmRegis.buscarPorId(13030);
//			documeElectRegis.registrarDimmIva(dimmIva);
			this.dimmIva = this.dimmRegis.buscarPorId(Dimm.class, variablesSesion.getDimmIdIvaActual());
			documeElectRegis.registrarDimmIva(dimmIva);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "No se encuentra Dimm Iva"));
			e.printStackTrace();
		}

		fechaDesde = LocalDate.now().minusDays(variablesSesion.getTiempoEmisionDocuElec());
		fechaHasta = LocalDate.now();

		documeElectRegis.leyenda1(variablesSesion.getLeyenda1());
		documeElectRegis.leyenda2(variablesSesion.getLeyenda2());
		documeElectRegis.msgInfoAdicional(variablesSesion.getMsgInfoAdicional());
	}

	public void seleccionarSucursalSesion() {

		if (buscarSucursalSesion) {
			sucursals.add(variablesSesion.getSucursal().getSucursalId());
		} else {
			sucursals.clear();
		}
	}

	public void buscar() {

		this.buscarEgresos2();
		this.buscarIngresos2();
		this.buscarRetencions2();
	}

	public DocuIngr buscarDocuIngrPorId(Integer documentoId) {

		DocuIngr docuIngr = new DocuIngr();

		try {
			docuIngr = docuIngrRegis.buscarPorId(DocuIngr.class, documentoId);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar documento ingreso"));
			e.printStackTrace();
		}

		return docuIngr;
	}

	@Deprecated
	public void buscarEgresos() {

		Set<Integer> sucursals = new HashSet<>();

		try {

			egresoLista.filasPagina(variablesSesion.getFilasPagina());
			egresoLista.orden(this.orden);

			this.egresos = egresoLista.buscar(egreso, this.pagina, sucursals);
			this.numeroReg = egresos.size();
			this.contadorReg = egresoLista.contarRegistros(egreso, sucursals);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}

//		Agrega las notas adicionales en caso que exista
		for (Egreso egreso : this.egresos) {
			EgreNota egreNota = new EgreNota(egreso);
			List<EgreNota> egreNotas = buscarEgreNotas(egreNota);

			Set<EgreNota> egreNotasSet = new HashSet<>(egreNotas);
			this.egreso.setEgreNotas(egreNotasSet);
			egreso.setEgreNotas(egreNotasSet);
		}
	}

//	Este metodo ya agrega las notas adicionales en los egresos
	public void buscarEgresos2() {

		try {

			egresoLista.filasPagina(variablesSesion.getFilasPagina());
			egresoLista.orden(this.orden);

			this.egresos = egresoLista.buscar2(egreso, fechaDesde, fechaHasta, null, sucursals);
			this.numeroReg = egresos.size();
			this.contadorReg = egresoLista.contarRegistros2(egreso, fechaDesde, fechaHasta, sucursals);

//			for (Egreso egreso : egresos) {
//				System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<< Egreso id " + egreso.getEgresoId() + " " + egreso.getEstado() + " " + egreso.getEstadoDocuElec() + " " + egreso.getDocuEgre().getDocumeEle>c());
//				Set<EgreNota>  egreNotas = egreso.getEgreNotas();
//				System.out.println("Notasssssssssssssssssssssssssssssssssss " + egreNotas.size());
//			}
//			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar datos egresos"));
			e.printStackTrace();
		}
	}

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

	public List<FormPagoMoviIngr> buscarFormPagoMoviIngr(FormPagoMoviIngr formPagoMoviIngrBuscar) {

		List<FormPagoMoviIngr> fpmis = new ArrayList<>();
		try {
			fpmis = this.formPagoMoviIngrLista.buscar(formPagoMoviIngrBuscar, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Buscar forma de pago en compras"));
			e.printStackTrace();
		}

		return fpmis;
	}

	public List<IngrDeta> buscarIngrDetas(IngrDeta ingrDetaBuscar) {

		List<IngrDeta> ingrDetas = new ArrayList<>();

		try {

			ingrDetas = ingrDetaLista.buscar(ingrDetaBuscar, null);
			for (IngrDeta ingrDeta : ingrDetas) {

				List<IngrDetaImpu> ingrDetaImpus = new ArrayList<>();

				IngrDetaImpu ingrDetaImpuBuscar = new IngrDetaImpu();

				// Si el ingreso esta grabado solo carga el iva porque aun no estan grabados
				// impuestos ni retenciones
				// si ya esta procesado carga los impuestos que ya estan grabados
				// if (this.ingreso.getEstado().equals("PR")) {
				ingrDetaImpuBuscar.setIngrDeta(ingrDeta);
				ingrDetaImpus = ingrDetaImpuList.buscar(ingrDetaImpuBuscar, null);
				Set<IngrDetaImpu> ingrDetaImpus2 = new HashSet<IngrDetaImpu>(ingrDetaImpus);
				ingrDeta.setIngrDetaImpus(ingrDetaImpus2);
				// }
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar detalle documento"));
			e.printStackTrace();
		}

		return ingrDetas;
	}

	public Ingreso buscarIngresoPorId(Integer ingresoId) {

		Ingreso ingreso = new Ingreso();
		try {

			ingreso = ingresoRegis.buscarPorId(Ingreso.class, ingresoId);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Buscar ingreso por id"));
			e.printStackTrace();
		}

		return ingreso;

	}

	@Deprecated
	public void buscarIngresos() {

		this.ingreso.setFechaEmis(this.getEgreso().getFechaEmis());
		this.ingreso.setNumero(this.getEgreso().getNumero());

		try {

			ingresoLista.filasPagina(variablesSesion.getFilasPagina());
			ingresoLista.orden(this.orden);

			this.ingresos = ingresoLista.buscar(ingreso, this.pagina);
			this.numeroReg = ingresos.size();
			this.contadorReg = ingresoLista.contarRegistros(ingreso);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public void buscarIngresos2() {

		this.ingreso.setNumero(this.getEgreso().getNumero());

		try {

			ingresoLista.filasPagina(variablesSesion.getFilasPagina());
			ingresoLista.orden(this.orden);

			this.ingresos = ingresoLista.buscar2(ingreso, fechaDesde, fechaHasta, null);
			this.numeroReg = ingresos.size();
			this.contadorReg = ingresoLista.contarRegistros2(ingreso, fechaDesde, fechaHasta);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar ingresos "));
			e.printStackTrace();
		}
	}

	public PersProv buscarPersProvPorId(Integer personaId) {

		PersProv persProv = new PersProv();

		try {
			persProv = persProvRegis.buscarPorId(PersProv.class, personaId);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar compra para retención"));
			e.printStackTrace();
		}

		return persProv;
	}

	@Deprecated
	public void buscarRetencions() {

		this.retencion.setFechaEmis(this.getEgreso().getFechaEmis());
		this.retencion.setNumero(this.getEgreso().getNumero());

		try {
//			Controla manualmente el numero de filas en la pagina
			retencionLista.filasPagina(variablesSesion.getFilasPagina());

			this.retencions = retencionLista.buscar(retencion, this.pagina);
			this.numeroReg = retencions.size();
			this.contadorReg = retencionLista.contarRegistros(retencion);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public void buscarRetencions2() {

		this.retencion.setNumero(this.getEgreso().getNumero());

		try {
//			Controla manualmente el numero de filas en la pagina
			retencionLista.filasPagina(variablesSesion.getFilasPagina());

			this.retencions = retencionLista.buscar2(retencion, fechaDesde, fechaHasta, null);
			this.numeroReg = retencions.size();
			this.contadorReg = retencionLista.contarRegistros2(retencion, fechaDesde, fechaHasta);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar datos retenciones"));
			e.printStackTrace();
		}
	}

	public void agruparEgresosRecibidas() {

		this.egresoNoEnviados.addAll(egresoRecibidos);
		egresoRecibidos.clear();
	}

	public void agruparIngresosRecibidas() {

		this.ingresoNoEnviados.addAll(ingresoRecibidos);
		ingresoRecibidos.clear();
	}

	public void agruparRetencionesRecibidas() {

		this.retencionNoEnviados.addAll(retencionRecibidos);
		retencionRecibidos.clear();
	}

//	public void procesarRecibidos() {
//
//		this.organizarListaEgresos();
//		List<Object> objetos = new ArrayList<Object>(this.egresoRecibidos);
//		this.enviarRecibidos(objetos);
//
//		this.organizarListaIngresos();
//		objetos = new ArrayList<Object>(this.ingresoRecibidos);
//		this.enviarRecibidos(objetos);
//
//		this.organizarListaRetenciones();
//		objetos = new ArrayList<Object>(this.retencionRecibidos);
//		this.enviarRecibidos(objetos);
//
//		this.egresos.clear();
//		this.ingresos.clear();
//		this.retencions.clear();
//		this.egresoRecibidos.clear();
//		this.ingresoRecibidos.clear();
//		this.retencionRecibidos.clear();

//		FacesContext.getCurrentInstance().addMessage(null,
//				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Documentos recibidos reprocesados y reenviados"));
//	}

//	public void procesarOtros() {
//
//		this.organizarListaEgresos();
//		this.organizarListaIngresos();
//		this.organizarListaRetenciones();
//
//		this.procesarComprobantesElectronicos();

//		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
//				"Documentos con otros estados reprocesados y reenviados"));

//		this.egresos.clear();
//		this.ingresos.clear();
//		this.retencions.clear();
//		this.egresoNoEnviados.clear();
//		this.retencionNoEnviados.clear();

////		Para procesar los documentos que se estan enviando en el proceso anterior
////		revisar si se requiere poner nuevamente la fecha de busqueda
////		Poner un delay
////		this.buscarEgresos();
////		this.buscarRetencions();
////		this.procesarRecibidos();
//	}

//	public void procesarTodosAntes() {
//		this.procesarRecibidos();
//		this.procesarOtros();
//
//		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
//				"Documentos con todos los estados reprocesados y reenviados"));
//
//	}

	public void procesarTodos() {

		this.organizarListaEgresos();
		this.organizarListaIngresos();
		this.organizarListaRetenciones();

		if (reenviarEgresosRecibida) {
			agruparEgresosRecibidas();
		}

		if (reenviarIngresosRecibida) {
			agruparIngresosRecibidas();
		}

		if (reenviarRetencionesRecibida) {
			agruparRetencionesRecibidas();
		}

		List<Object> objetosEgresosRecibidas = new ArrayList<Object>(this.egresoRecibidos);
		this.enviarRecibidos(objetosEgresosRecibidas);

		List<Object> objetosIngresosRecibidas = new ArrayList<Object>(this.ingresoRecibidos);
		this.enviarRecibidos(objetosIngresosRecibidas);

		List<Object> objetosRetencionesRecibidas = new ArrayList<Object>(this.retencionRecibidos);
		this.enviarRecibidos(objetosRetencionesRecibidas);

//		Envia los que tiene otro estado diferenta a RECIBIDA
		this.procesarComprobantesElectronicos();

		this.egresos.clear();
		this.ingresos.clear();
		this.retencions.clear();
		this.egresoRecibidos.clear();
		this.ingresoRecibidos.clear();
		this.retencionRecibidos.clear();

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Documentos Reprocesados"));
	}

	public void organizarListaEgresos() {

		egresoRecibidos.addAll(egresos);

//		Se comento estas lineas porque en el estado que devuelve el SRI es inconsistente
//		a veces sale recibida, Could no send a message, html, o no sale el mensaje NO AUTORIZADO
//		Entonces se pone toda las lista para consultar la autorizacion
//		y cuando se marca la casilla reenviar se vuelve a firmar, enviar, autorizar y enviar correo.
//		for (Egreso egreso : this.egresos) {
//
//			switch (egreso.getEstadoDocuElec().toLowerCase()) {
//
//			case "recibida":
//
//				egresoRecibidos.add(egreso);
//
//				break;
//
//			default:
//
//				egresoNoEnviados.add(egreso);
//
//				break;
//			}
//		}
	}

	public void organizarListaIngresos() {

		ingresoRecibidos.addAll(ingresos);

//		for (Ingreso ingreso : this.ingresos) {
//
//			switch (ingreso.getEstadoDocuElec().toLowerCase()) {
//
//			case "recibida":
//
//				ingresoRecibidos.add(ingreso);
//
//				break;
//
//			default:
//
//				ingresoNoEnviados.add(ingreso);
//
//				break;
//			}
//		}
	}

	public void organizarListaRetenciones() {

		retencionRecibidos.addAll(retencions);

//		for (Retencion retencion : this.retencions) {
//
//			switch (retencion.getEstadoDocuElec().toLowerCase()) {
//
//			case "recibida":
//
//				retencionRecibidos.add(retencion);
//
//				break;
//
//			default:
//
//				retencionNoEnviados.add(retencion);
//
//				break;
//			}
//		}
	}

	public void enviarRecibidos(List<Object> objectos) {

		File archivoFirmado = new File("");

		for (Object objecto : objectos) {

			Egreso egreso = new Egreso();
			Ingreso ingreso = new Ingreso();
			Retencion retencion = new Retencion();

			if (objecto instanceof Egreso) {

				egreso = (Egreso) objecto;

				this.ejecutarTareaAutorizarEgreso(archivoFirmado, egreso);
				this.ejecutarTareaCrearRideFactura(egreso);

				if (this.enviarCorreo) {
					if (egreso.getPersClie().getPersona().getCorreo() != null
							&& egreso.getDocuEgre().getDocumento().isEnviaCorreo()) {
						this.ejecutarTareaEnviarCorreoDocu(egreso.getPersClie().getPersona().getCorreo(),
								egreso.getClaveAcce());
					}
				}

			} else if (objecto instanceof Retencion) {

				retencion = (Retencion) objecto;

				this.ejecutarTareaAutorizarRetencion(archivoFirmado, retencion);
				this.ejecutarTareaCrearRideRetencion(retencion);

				if (this.enviarCorreo) {
					if (retencion.getIngreso().getPersProv().getPersona().getCorreo() != null
							&& retencion.getIngreso().getDocuIngr().getDocumento().isEnviaCorreo()) {
						this.ejecutarTareaEnviarCorreoDocu(
								retencion.getIngreso().getPersProv().getPersona().getCorreo(),
								retencion.getClaveAcce());
					}
				}
			} else if (objecto instanceof Ingreso) {

				ingreso = (Ingreso) objecto;

				this.ejecutarTareaAutorizarIngreso(archivoFirmado, ingreso);
				this.ejecutarTareaCrearRideRetencion(ingreso);

				if (this.enviarCorreo) {
					if (ingreso.getPersProv().getPersona().getCorreo() != null
							&& ingreso.getDocuIngr().getDocumento().isEnviaCorreo()) {
						this.ejecutarTareaEnviarCorreoDocu(ingreso.getPersProv().getPersona().getCorreo(),
								egreso.getClaveAcce());
					}
				}
			}
		}
	}

	public void procesarComprobantesElectronicos() {

		List<Egreso> egresoGenerados = new ArrayList<>();
		List<Ingreso> ingresoGenerados = new ArrayList<>();
		List<Retencion> retencionGenerados = new ArrayList<>();

		egresoGenerados = this.crearArchivoEgresoXmls();
		List<Object> objects = new ArrayList<Object>(egresoGenerados);
		if (objects.size() != 0) {
			this.firmarLote(objects);
		}

		ingresoGenerados = this.crearArchivoIngresoXmls();
		objects = new ArrayList<Object>(ingresoGenerados);
		if (objects.size() != 0) {
			this.firmarLote(objects);
		}

		retencionGenerados = this.crearArchivoRetencionXmls();
		objects = new ArrayList<Object>(retencionGenerados);
		if (objects.size() != 0) {
			this.firmarLote(objects);
		}
	}

	public List<FormPagoMoviEgre> buscarformPagoMoviEgres(Egreso egreso) {

		List<FormPagoMoviEgre> formPagoMoviEgres = new ArrayList<FormPagoMoviEgre>();

		FormPagoMoviEgre formPagoMoviEgre = new FormPagoMoviEgre();
		Persona persona = new Persona();

		formPagoMoviEgre.setEgresoId(egreso.getEgresoId());
		formPagoMoviEgre.setPersona(persona);

		try {
			formPagoMoviEgres = formPagoMoviEgreLista.buscar(formPagoMoviEgre, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Error al buscar forma de pago - FormPagoMoviEgre: " + egreso.getNumero()));
			e.printStackTrace();
		}

		return formPagoMoviEgres;
	}

	public List<Egreso> crearArchivoEgresoXmls() {

		Object object = null;

		List<Egreso> egresosGenerados = new ArrayList<>();

		for (Egreso egreso : this.egresoNoEnviados) {

//			egreDeta.setEgreso(egreso);
			EgreDeta egreDeta = new EgreDeta(egreso, new EgreDeta());

			List<EgreDeta> egreDetas = new ArrayList<>();
			List<CobrDeta> cobrDetas = new ArrayList<>();

//			Buscar los detalles de cada factura para poder reprocesar
			try {
				egreDetas = egreDetaLista.buscar(egreDeta, null);

				for (EgreDeta egreDetaBuscar : egreDetas) {

					List<EgreDetaImpu> egreDetaImpus = new ArrayList<>();

					EgreDetaImpu egreDetaImpu = new EgreDetaImpu();
					egreDetaImpu.setEgreDeta(egreDetaBuscar);

					egreDetaImpus = egreDetaImpuLista.buscar(egreDetaImpu, null);

					Set<EgreDetaImpu> egreDetaImpus2 = new HashSet<EgreDetaImpu>(egreDetaImpus);
					egreDetaBuscar.setEgreDetaImpus(egreDetaImpus2);
				}

			} catch (Exception e1) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepción - Error al buscar detalle de factura: " + egreso.getNumero()));
				e1.printStackTrace();
			}

			List<FormPagoMoviEgre> formPagoMoviEgres = buscarformPagoMoviEgres(egreso);

			List<FpmeFormPago> fpmeFormPagos = new ArrayList<FpmeFormPago>();

			for (FormPagoMoviEgre formPagoMoviEgre : formPagoMoviEgres) {

				FpmeFormPago fpmeFormpago = new FpmeFormPago();

				fpmeFormpago.setFormPagoMoviEgre(formPagoMoviEgre);

				try {
					fpmeFormPagos = fpmeFormPagoLista.buscar(fpmeFormpago, null);
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
							"Error al buscar forma de pago - FpmeFormPago: " + egreso.getNumero()));
					e.printStackTrace();
				}
			}

//			Cxc cxc = new Cxc();
//			cxc.setEgreso(egreso);
//
//			CobrDeta cobrDetaBuscar = new CobrDeta();
//			cobrDetaBuscar.setCxc(cxc);
//
//			try {
//				cobrDetas = cobrDetaLista.buscar(cobrDetaBuscar, null);
//			} catch (Exception e) {
//				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
//						"Excepción - Error al buscar detalle de cobros: " + egreso.getNumero()));
//				e.printStackTrace();
//			}
//			
//			List<FpmeFormPago> fpmeFormPagos = new ArrayList<FpmeFormPago>();
			FpmeFormPago fpmeFormPago = new FpmeFormPago();
//
//			for (CobrDeta cobrDeta : cobrDetas) {
//				try {
//
//					FpmeFormPago fpmeFormPagoBuscar = new FpmeFormPago();
//					fpmeFormPagoBuscar = fpmeFormPagoRegis.buscarPorId(FpmeFormPago.class,
//							cobrDeta.getFpmeFormPago().getFpmeFormPagoId());
//					fpmeFormPagos.add(fpmeFormPagoBuscar);
//
//				} catch (Exception e) {
//					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
//							null, "Excepción - Error al buscar detalle de factura: " + egreso.getNumero()));
//					e.printStackTrace();
//				}
//			}

//			Asignar fpmeFormPago = fpmeFormPagoRecorrer para saber la forma de pago
//			y enviar como parametro, si no existe porque la factura es a credito 
//			se manda fpmeFormPago vacio
			for (FpmeFormPago fpmeFormPagoRecorrer : fpmeFormPagos) {
//				if (fpmeFormPagoRecorrer.getFormPago().getTipo().equals("FP")) {
					fpmeFormPago = fpmeFormPagoRecorrer;
//				}
			}

			try {
				object = documeElectRegis.generarComprobanteElectronico(egreso, egreDetas,
						variablesSesion.getCodigoIva(), variablesSesion.getCodigoIce(),
						variablesSesion.getCodigoIrbpnr(), fpmeFormPago);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepción - Error al generar comprobante"));
				e.printStackTrace();
			}

			try {

//				Crea el archivo en la ruta especificada en parametros
				if ((object instanceof Factura)) {
					documeElectRegis.marshalFactura((Factura) object, this.variablesSesion.getRutaGenerados());
				} else if ((object instanceof NotaCredito)) {
					documeElectRegis.marshalNotaCredito((NotaCredito) object, this.variablesSesion.getRutaGenerados());
				} else if ((object instanceof NotaDebito)) {
					documeElectRegis.marshalNotaDebito((NotaDebito) object, this.variablesSesion.getRutaGenerados());
				} else if ((object instanceof GuiaRemision)) {
					documeElectRegis.marshalGuiaRemision((GuiaRemision) object,
							this.variablesSesion.getRutaGenerados());
				}

				egresosGenerados.add(egreso);

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - No se genero el comprobante XML"));
				e.printStackTrace();
			}
		}
		return egresosGenerados;
	}

	public List<Ingreso> crearArchivoIngresoXmls() {

		Object object = null;

		List<Ingreso> ingresosGenerados = new ArrayList<>();

		for (Ingreso ingreso : this.ingresoNoEnviados) {

			try {

//				Se hace esta busqueda para que encuentre el dimm de docuIngr
				DocuIngr docuIngr = this.buscarDocuIngrPorId(ingreso.getDocuIngr().getDocumentoId());
				ingreso.setDocuIngr(docuIngr);

//				Se hace esta busqueda para que encuentre el dimm de persProv
				PersProv persProv = this.buscarPersProvPorId(ingreso.getPersProv().getPersonaId());
				ingreso.setPersProv(persProv);

				IngrDeta ingrDeta = new IngrDeta();
				ingrDeta.setIngreso(ingreso);
				List<IngrDeta> ingrDetas = this.buscarIngrDetas(ingrDeta);

//				Buscar FormPagoMoviIngr
				FormPagoMoviIngr fpmiBuscar = new FormPagoMoviIngr();

				fpmiBuscar.setPersona(new Persona());
				fpmiBuscar.setIngresoId(ingreso.getIngresoId());

				List<FormPagoMoviIngr> fpmis = this.buscarFormPagoMoviIngr(fpmiBuscar);

				FormPagoMoviIngr fpmi = new FormPagoMoviIngr();
//				Si no encontro el pago quiere decir que fue credito
//				por lo tanto no encuentra fpmis entonces se crea una con la forma de pago credito
				if (!fpmis.isEmpty()) {
					fpmi = fpmis.get(0);
				} else {
					FormPago formPago = new FormPago();
					Dimm dimm = new Dimm();
					dimm.setCodigo("01");

					formPago.setDimm(dimm);
					fpmi.setFormPago(formPago);
				}

				object = documeElectRegis.generarComprobanteElectronicoLiquidacion(ingreso, ingrDetas, fpmi,
						variablesSesion.getCodigoReteIva(), variablesSesion.getCodigoReteRenta(),
						variablesSesion.getCodigoReteIsd(), variablesSesion.getCodigoIva(),
						variablesSesion.getCodigoIce(), variablesSesion.getCodigoIrbpnr());

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

				ingresosGenerados.add(ingreso);

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - No se genero el comprobante XML"));
				e.printStackTrace();
			}
		}

		return ingresosGenerados;
	}

	public List<Retencion> crearArchivoRetencionXmls() {

		Object object = null;

		List<Retencion> retencionesGenerados = new ArrayList<>();

		for (Retencion retencion : this.retencionNoEnviados) {

			try {

//				AL buscar ingreso ya encuentra sucursal, docuIngr, persProv, dimm, transaccion, ingreso(recuersivo);
				Ingreso ingreso = this.buscarIngresoPorId(retencion.getIngreso().getIngresoId());
				retencion.setIngreso(ingreso);

				DocuIngr docuIngr = this.buscarDocuIngrPorId(retencion.getIngreso().getDocuIngr().getDocumentoId());
				retencion.getIngreso().setDocuIngr(docuIngr);

				PersProv persProv = this.buscarPersProvPorId(retencion.getIngreso().getPersProv().getPersonaId());
				retencion.getIngreso().setPersProv(persProv);

				IngrDeta ingrDeta = new IngrDeta();
				ingrDeta.setIngreso(retencion.getIngreso());
				List<IngrDeta> ingrDetas = this.buscarIngrDetas(ingrDeta);

//				Buscar FormPagoMoviIngr
				FormPagoMoviIngr fpmiBuscar = new FormPagoMoviIngr();

				fpmiBuscar.setPersona(new Persona());
				fpmiBuscar.setIngresoId(ingreso.getIngresoId());

				List<FormPagoMoviIngr> fpmis = this.buscarFormPagoMoviIngr(fpmiBuscar);

				FormPagoMoviIngr fpmi = new FormPagoMoviIngr();
//				Si no encontro el pago quiere decir que fue credito
//				por lo tanto no encuentra fpmis entonces se crea una con la forma de pago credito
				if (!fpmis.isEmpty()) {
					fpmi = fpmis.get(0);
				} else {
					FormPago formPago = new FormPago();
					Dimm dimm = new Dimm();
					dimm.setCodigo("01");

					formPago.setDimm(dimm);
					fpmi.setFormPago(formPago);
				}

				ReteDeta reteDeta = new ReteDeta();
				List<ReteDeta> reteDetasBuscar = new ArrayList<>();

				try {
					reteDeta.setRetencion(retencion);
					reteDetasBuscar = reteDetaLista.buscar(reteDeta, null);
					Set<ReteDeta> reteDetas = new HashSet<>(reteDetasBuscar);
					retencion.setReteDetas(reteDetas);
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
							null, "Error al buscar detalle de retención"));
					e.printStackTrace();
				}

				object = documeElectRegis.generarComprobanteElectronico(retencion.getIngreso(), ingrDetas, fpmi,
						retencion, variablesSesion.getCodigoReteIva(), variablesSesion.getCodigoReteRenta(),
						variablesSesion.getCodigoReteIsd(), variablesSesion.getCodigoIva(),
						variablesSesion.getCodigoIce(), variablesSesion.getCodigoIrbpnr());

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepción - Error al generar comprobante - Retención"));
				e.printStackTrace();
			}

			try {

				if ((object instanceof ComprobanteRetencion)) {
					documeElectRegis.marshalRetencion((ComprobanteRetencion) object,
							this.variablesSesion.getRutaGenerados());
				}

				retencionesGenerados.add(retencion);

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - No se genero el comprobante XML"));
				e.printStackTrace();
			}
		}

		return retencionesGenerados;
	}

	public void firmarLote(List<Object> objectos) {

		String rutaGenerados = null;

		Egreso egreso = new Egreso();
		Ingreso ingreso = new Ingreso();
		Retencion retencion = new Retencion();

		for (Object objecto : objectos) {

			String estadoProceso = "NO ENVIADO";
			String nombreArchivo = null;

			if (objecto instanceof Egreso) {

				egreso = (Egreso) objecto;

//				Representa el nombre del archivo con se graba para firmar
				nombreArchivo = egreso.getClaveAcce() + ".xml";
				rutaGenerados = this.variablesSesion.getRutaGenerados() + nombreArchivo;

				try {

					firmarArchivo.signBes(egreso.getSucursal().getSucuCertEmis(), nombreArchivo,
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

					this.ejecutarTareaEnviarEgreso(archivoFirmado, egreso);
					this.ejecutarTareaAutorizarEgreso(archivoFirmado, egreso);
					this.ejecutarTareaCrearRideFactura(egreso);

					if (this.enviarCorreo) {
						if (egreso.getPersClie().getPersona().getCorreo() != null
								&& egreso.getDocuEgre().getDocumento().isEnviaCorreo()) {
							this.ejecutarTareaEnviarCorreoDocu(egreso.getPersClie().getPersona().getCorreo(),
									egreso.getClaveAcce());
						}
					}

				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error al firmar documento", estadoProceso));
				}

			} else if (objecto instanceof Retencion) {

				retencion = (Retencion) objecto;
//				Representa el nombre del archivo con se graba para firmar
				nombreArchivo = retencion.getClaveAcce() + ".xml";
				rutaGenerados = this.variablesSesion.getRutaGenerados() + nombreArchivo;

				try {

					firmarArchivo.signBes(retencion.getIngreso().getSucursal().getSucuCertEmis(), nombreArchivo,
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

					this.ejecutarTareaEnviarRetencion(archivoFirmado, retencion);
					this.ejecutarTareaAutorizarRetencion(archivoFirmado, retencion);
					this.ejecutarTareaCrearRideRetencion(retencion);

					if (this.enviarCorreo) {
						if (retencion.getIngreso().getPersProv().getPersona().getCorreo() != null
								&& retencion.getIngreso().getDocuIngr().getDocumento().isEnviaCorreo()) {
							this.ejecutarTareaEnviarCorreoDocu(
									retencion.getIngreso().getPersProv().getPersona().getCorreo(),
									retencion.getClaveAcce());
						}
					}

				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error al firmar retención", estadoProceso));
				}
			} else if (objecto instanceof Ingreso) {

				ingreso = (Ingreso) objecto;

//				Representa el nombre del archivo con se graba para firmar
				nombreArchivo = ingreso.getClaveAcce() + ".xml";
				rutaGenerados = this.variablesSesion.getRutaGenerados() + nombreArchivo;

				try {

					firmarArchivo.signBes(ingreso.getSucursal().getSucuCertEmis(), nombreArchivo,
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

					this.ejecutarTareaEnviarIngreso(archivoFirmado, ingreso);
					this.ejecutarTareaAutorizarIngreso(archivoFirmado, ingreso);
					this.ejecutarTareaCrearRideRetencion(ingreso);

					if (this.enviarCorreo) {
						if (ingreso.getPersProv().getPersona().getCorreo() != null
								&& ingreso.getDocuIngr().getDocumento().isEnviaCorreo()) {
							this.ejecutarTareaEnviarCorreoDocu(ingreso.getPersProv().getPersona().getCorreo(),
									ingreso.getClaveAcce());
						}
					}

				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error al firmar documento", estadoProceso));
				}
			}

//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}

		}
	}

	public void ejecutarTareaEnviarEgreso(File archivoFirmado, Egreso egreso) {

		try {

			manejadorTareaEnvioDocu.ejecutarTareaEnviarDocu(egreso, archivoFirmado, egreso.getSucursal(),
					egreso.getDocuEgre().getDimm().getCodigo(), egreso.getClaveAcce(), variablesSesion.getProxyIp(),
					variablesSesion.getProxyPuerto(), egreso.getDocuEgre().getAmbien(),
					variablesSesion.getUrlProduccion(), variablesSesion.getUrlPruebas(),
					"RecepcionComprobantesOffline");

		} catch (ExecutionException | InterruptedException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Documento no enviado al SRI"));
			e.printStackTrace();
		}
	}

	public void ejecutarTareaAutorizarEgreso(File archivoFirmado, Egreso egreso) {

		try {

			manejadorTareaAutoriDocu.ejecutarTareaAutoriDocu(egreso, archivoFirmado, egreso.getSucursal(),
					egreso.getDocuEgre().getDimm().getCodigo(), egreso.getClaveAcce(), variablesSesion.getProxyIp(),
					variablesSesion.getProxyPuerto(), egreso.getDocuEgre().getAmbien(),
					variablesSesion.getUrlProduccion(), variablesSesion.getUrlPruebas(),
					"AutorizacionComprobantesOffline");

		} catch (ExecutionException | InterruptedException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Documento no autorizado por el SRI"));
			e.printStackTrace();
		}
	}

	public void ejecutarTareaCrearRideFactura(Egreso egreso) {

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

		TimerTask timerTask = new TareaCrearRideFacturaPdf(egreso, this.parametroRegis, this.dimmRegis, this.dataSource,
				this.variablesSesion, request);

		Timer timer = new Timer(true);

		timer.schedule(timerTask, 80 * 1000);
	}

	public void ejecutarTareaEnviarIngreso(File archivoFirmado, Ingreso ingreso) {

		try {
			manejadorTareaEnvioDocu.ejecutarTareaEnviarDocu(ingreso, archivoFirmado, ingreso.getSucursal(),
					ingreso.getDocuIngr().getDimm().getCodigo(), ingreso.getClaveAcce(), variablesSesion.getProxyIp(),
					variablesSesion.getProxyPuerto(), ingreso.getDocuIngr().getAmbien(),
					variablesSesion.getUrlProduccion(), variablesSesion.getUrlPruebas(),
					"RecepcionComprobantesOffline");

		} catch (ExecutionException | InterruptedException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Documento no enviado al SRI"));
			e.printStackTrace();
		}
	}

	public void ejecutarTareaAutorizarIngreso(File archivoFirmado, Ingreso ingreso) {

		try {
			manejadorTareaAutoriDocu.ejecutarTareaAutoriDocu(ingreso, archivoFirmado, ingreso.getSucursal(),
					ingreso.getDocuIngr().getDimm().getCodigo(), ingreso.getClaveAcce(), variablesSesion.getProxyIp(),
					variablesSesion.getProxyPuerto(), ingreso.getDocuIngr().getAmbien(),
					variablesSesion.getUrlProduccion(), variablesSesion.getUrlPruebas(),
					"AutorizacionComprobantesOffline");

		} catch (ExecutionException | InterruptedException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Documento no autorizado por el SRI"));
			e.printStackTrace();
		}
	}

	public void ejecutarTareaEnviarRetencion(File archivoFirmado, Retencion retencion) {

		try {
			manejadorTareaEnvioDocu.ejecutarTareaEnviarDocu(retencion, archivoFirmado,
					retencion.getIngreso().getSucursal(), retencion.getIngreso().getDocuIngr().getCodigoTipoCompRete(),
					retencion.getClaveAcce(), variablesSesion.getProxyIp(), variablesSesion.getProxyPuerto(),
					retencion.getIngreso().getDocuIngr().getAmbien(), variablesSesion.getUrlProduccion(),
					variablesSesion.getUrlPruebas(), "RecepcionComprobantesOffline");

		} catch (ExecutionException | InterruptedException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Documento no enviado al SRI"));
			e.printStackTrace();
		}
	}

	public void ejecutarTareaAutorizarRetencion(File archivoFirmado, Retencion retencion) {

		try {
			manejadorTareaAutoriDocu.ejecutarTareaAutoriDocu(retencion, archivoFirmado,
					retencion.getIngreso().getSucursal(), retencion.getIngreso().getDocuIngr().getCodigoTipoCompRete(),
					retencion.getClaveAcce(), variablesSesion.getProxyIp(), variablesSesion.getProxyPuerto(),
					retencion.getIngreso().getDocuIngr().getAmbien(), variablesSesion.getUrlProduccion(),
					variablesSesion.getUrlPruebas(), "AutorizacionComprobantesOffline");

		} catch (ExecutionException | InterruptedException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Documento no autorizado por el SRI"));
			e.printStackTrace();
		}
	}

	public void ejecutarTareaCrearRideRetencion(Object object) {

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

	public void ejecutarTareaEnviarCorreoDocu(String destinatario, String claveAcce) {

		try {

			manejadorTareaEnviarCorreoDocu.ejecutarTareaEnviarCorreoDocu(destinatario, claveAcce);

		} catch (ExecutionException | InterruptedException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Documento no enviado por correo al cliente " + destinatario));
			e.printStackTrace();
		}
	}

	public void bloquearProcesarTodos() {

		LocalDate fechaActual = LocalDate.now();

		bloquearBotonProcesarTodos = false;

		if (ChronoUnit.DAYS.between(fechaDesde, fechaActual) > variablesSesion.getTiempoEmisionDocuElec()
				|| ChronoUnit.DAYS.between(fechaHasta, fechaActual) > variablesSesion.getTiempoEmisionDocuElec()) {
			bloquearBotonProcesarTodos = true;
		}
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public Egreso getEgreso() {
		return egreso;
	}

	public void setEgreso(Egreso egreso) {
		this.egreso = egreso;
	}

	public List<Egreso> getEgresos() {
		return egresos;
	}

	public void setEgresos(List<Egreso> egresos) {
		this.egresos = egresos;
	}

	public List<Egreso> getEgresoNoEnviados() {
		return egresoNoEnviados;
	}

	public void setEgresoNoEnviados(List<Egreso> egresoNoEnviados) {
		this.egresoNoEnviados = egresoNoEnviados;
	}

	public List<Egreso> getEgresoRecibidos() {
		return egresoRecibidos;
	}

	public void setEgresoRecibidos(List<Egreso> egresoRecibidos) {
		this.egresoRecibidos = egresoRecibidos;
	}

	public List<Retencion> getRetencionNoEnviados() {
		return retencionNoEnviados;
	}

	public void setRetencionNoEnviados(List<Retencion> retencionNoEnviados) {
		this.retencionNoEnviados = retencionNoEnviados;
	}

	public List<Retencion> getRetencionRecibidos() {
		return retencionRecibidos;
	}

	public void setRetencionRecibidos(List<Retencion> retencionRecibidos) {
		this.retencionRecibidos = retencionRecibidos;
	}

	public List<Retencion> getRetencions() {
		return retencions;
	}

	public void setRetencions(List<Retencion> retencions) {
		this.retencions = retencions;
	}

	public Boolean getEnviarCorreo() {
		return enviarCorreo;
	}

	public void setEnviarCorreo(Boolean enviarCorreo) {
		this.enviarCorreo = enviarCorreo;
	}

	public Boolean getReenviarEgresosRecibida() {
		return reenviarEgresosRecibida;
	}

	public void setReenviarEgresosRecibida(Boolean reenviarEgresosRecibida) {
		this.reenviarEgresosRecibida = reenviarEgresosRecibida;
	}

	public Boolean getReenviarIngresosRecibida() {
		return reenviarIngresosRecibida;
	}

	public void setReenviarIngresosRecibida(Boolean reenviarIngresosRecibida) {
		this.reenviarIngresosRecibida = reenviarIngresosRecibida;
	}

	public Boolean getReenviarRetencionesRecibida() {
		return reenviarRetencionesRecibida;
	}

	public void setReenviarRetencionesRecibida(Boolean reenviarRetencionesRecibida) {
		this.reenviarRetencionesRecibida = reenviarRetencionesRecibida;
	}

	public List<Ingreso> getIngresos() {
		return ingresos;
	}

	public void setIngresos(List<Ingreso> ingresos) {
		this.ingresos = ingresos;
	}

	public LocalDate getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(LocalDate fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public LocalDate getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(LocalDate fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public Set<Integer> getSucursals() {
		return sucursals;
	}

	public void setSucursals(Set<Integer> sucursals) {
		this.sucursals = sucursals;
	}

	public Boolean getBuscarSucursalSesion() {
		return buscarSucursalSesion;
	}

	public void setBuscarSucursalSesion(Boolean buscarSucursalSesion) {
		this.buscarSucursalSesion = buscarSucursalSesion;
	}

	public boolean isBloquearBotonProcesarTodos() {
		return bloquearBotonProcesarTodos;
	}

	public void setBloquearBotonProcesarTodos(boolean bloquearBotonProcesarTodos) {
		this.bloquearBotonProcesarTodos = bloquearBotonProcesarTodos;
	}
}