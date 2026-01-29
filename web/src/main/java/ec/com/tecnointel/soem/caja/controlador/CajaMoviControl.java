package ec.com.tecnointel.soem.caja.controlador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ec.com.tecnointel.soem.caja.listaInt.CajaListaInt;
import ec.com.tecnointel.soem.caja.listaInt.CajaMoviFormPagoListaInt;
import ec.com.tecnointel.soem.caja.listaInt.CajaMoviListaInt;
import ec.com.tecnointel.soem.caja.listaInt.CajaMoviTranListaInt;
import ec.com.tecnointel.soem.caja.listaInt.PersCajeListaInt;
import ec.com.tecnointel.soem.caja.modelo.Caja;
import ec.com.tecnointel.soem.caja.modelo.CajaMovi;
import ec.com.tecnointel.soem.caja.modelo.CajaMoviFormPago;
import ec.com.tecnointel.soem.caja.modelo.CajaMoviTran;
import ec.com.tecnointel.soem.caja.modelo.PersCaje;
import ec.com.tecnointel.soem.caja.registroInt.CajaMoviFormPagoRegisInt;
import ec.com.tecnointel.soem.caja.registroInt.CajaMoviRegisInt;
import ec.com.tecnointel.soem.caja.registroInt.CajaMoviTranRegisInt;
import ec.com.tecnointel.soem.contabilidad.modelo.Transaccion;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionCajaMoviInt;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionRegisInt;
import ec.com.tecnointel.soem.docuElec.controlador.DocuElecControl;
import ec.com.tecnointel.soem.egreso.listaInt.EgresoListaInt;
import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.egreso.modelo.PersClie;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.DimmPlanCuenListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.DocuCajaListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.DocuEgreListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.DocuMoviIngrListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.FormPagoListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.TranPlanListaInt;
import ec.com.tecnointel.soem.parametro.modelo.DimmPlanCuen;
import ec.com.tecnointel.soem.parametro.modelo.DocuCaja;
import ec.com.tecnointel.soem.parametro.modelo.DocuEgre;
import ec.com.tecnointel.soem.parametro.modelo.DocuMoviEgre;
import ec.com.tecnointel.soem.parametro.modelo.DocuMoviIngr;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.modelo.TranPlan;
import ec.com.tecnointel.soem.parametro.registroInt.DocumentoRegisInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolPersUsuaListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolSucuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviEgre;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import ec.com.tecnointel.soem.tesoreria.registroInt.FormPagoMoviEgreRegisInt;
import ec.com.tecnointel.soem.tesoreria.registroInt.FormPagoMoviIngrRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class CajaMoviControl extends PaginaControl implements Serializable {

	private long contadorDocuNoAuto = 0;
	
	private CajaMovi cajaMovi;

	private PersUsua persUsuaSesion;

	Set<Sucursal> sucursals;
	private List<CajaMovi> cajaMovis;

//	Combos
	private List<DocuCaja> docuCajas;
	private List<Caja> cajas;
	private List<PersCaje> persCajes;

	private List<TranPlan> tranPlans;

	private List<CajaMoviTran> cajaMoviTrans;

//	Detalles
	private List<CajaMoviFormPago> cajaMoviFormPagos;

	@Inject
	CajaMoviRegisInt cajaMoviRegis;

	@Inject
	CajaMoviTranRegisInt cajaMoviTranRegis;

	@Inject
	CajaMoviTranListaInt cajaMoviTranLista;

	@Inject
	CajaMoviListaInt cajaMoviLista;

	@Inject
	DocuCajaListaInt docuCajaLista;

	@Inject
	CajaListaInt cajaLista;

	@Inject
	PersCajeListaInt persCajeLista;

	@Inject
	FormPagoListaInt formPagoLista;

	@Inject
	CajaMoviFormPagoListaInt cajaMoviFormPagoLista;

	@Inject
	CajaMoviFormPagoRegisInt cajaMoviFormPagoRegis;

	@Inject
	RolSucuListaInt rolSucuLista;

	@Inject
	RolPersUsuaListaInt rolPersUsuaLista;

	@Inject
	EgresoListaInt egresoLista;

	@Inject
	DocumentoRegisInt documentoRegis;

	@Inject
	DimmPlanCuenListaInt dimmPlanCuenLista;

	@Inject
	FormPagoMoviEgreRegisInt formPagoMoviEgreRegis;

	@Inject
	FormPagoMoviIngrRegisInt formPagoMoviIngrRegis;

	@Inject
	DocuMoviIngrListaInt docuMoviIngrLista;

	@Inject
	TranPlanListaInt tranPlanLista;

//	@Inject
//	TransaccionGestionInt transaccionGestion;

	@Inject
	TransaccionRegisInt transaccionRegis;

	@Inject
	DocuElecControl docuElectControl;

	@Inject
	DocuEgreListaInt docuEgreLista;

	@Inject
	TransaccionCajaMoviInt transaccionCajaMovi;

	private static final long serialVersionUID = -5013555310745759475L;

	@PostConstruct
	public void cargar() {

		this.persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");

		DocuCaja docuCaja = new DocuCaja();
		docuCaja.setDocumento(new Documento());

		PersCaje persCaje = new PersCaje();
		persCaje.setPersona(new Persona());

		cajaMovi = new CajaMovi();
		cajaMovi.setDocuCaja(docuCaja);
		cajaMovi.setCaja(new Caja());
		cajaMovi.setPersCaje(persCaje);

		cajaMovi.setCajaMovi(new CajaMovi());
//		LocalDate fecha = Util.cambiarFormatoFecha(LocalDate.now(), "dd-MM-yyyy");
//		cajaMovi.setFecha(fecha);
		cajaMovi.setFecha(LocalDate.now());

		cajaMovi.setSucursal(new Sucursal());

		docuCajas = new ArrayList<>();
		cajas = new ArrayList<>();
		persCajes = new ArrayList<>();

		rolPermiso = variablesSesion.getRolPermiso();

		sucursals = new HashSet<>();

		cajaMoviTrans = new ArrayList<>();

		this.buscarRolSucus();

	}

	public void actualizarListas() {

		Set<Sucursal> sucursals = new HashSet<>();
		sucursals.add(this.cajaMovi.getSucursal());

		this.buscarCajas(sucursals);

//		Si el documento inicia sesion de ventas y no gestiona se carga el usuario que ingreso al sistema
		if (variablesSesion.getRolPermiso().get(1104) == true) {
			this.buscarPersCajes();
		} else {
//			PersUsua que abrio sesion de ventas
			PersCaje persCaje = this.buscarPersCaje();
			this.persCajes.clear();
			this.persCajes.add(persCaje);
			this.cajaMovi.setPersCaje(persCaje);
		}
	}

	public void asignarInicio() {

		CajaMovi cajaMoviInicio = null;

		List<CajaMovi> cajaMovis = new ArrayList<>();

//		Si el documento no inicia sesion de ventas (es gasto o cierre) se carga el usuario que ingreso al sistema
//		y la caja de la sesion iniciada

//		Si el usuario no esta asignado como cajero y no gestiona obviamente no podra hacer movimientos de su usuario
//		Si se quisiera hacer algun movimiento tendria que gestionar o asignarse como cajero
		if (!this.cajaMovi.getDocuCaja().isAbreCaja()) {

			if (!variablesSesion.getRolPermiso().get(1104) == true) {

//				Trae el usuario que esta en sesión
				PersCaje persCaje = this.buscarPersCaje();

				cajaMovis = this.buscarCajaMovis();

				for (CajaMovi cajaMovi : cajaMovis) {
					if (cajaMovi.getPersCaje().equals(persCaje)) {
						cajaMoviInicio = cajaMovi;
					}
				}

				if (cajaMoviInicio != null) {

					this.persCajes.clear();
					this.persCajes.add(persCaje);
					this.cajaMovi.setPersCaje(persCaje);

					this.cajas.clear();
					this.cajas.add(cajaMoviInicio.getCaja());
					this.cajaMovi.setCaja(cajaMoviInicio.getCaja());
				}
			}
		}

		this.cajaMoviFormPagos = this.crearCajaMoviFormPagos(this.cajaMovi.getDocuCaja().isAbreCaja());
	}

	public void buscar() {

		try {

//			Chequea si tiene permiso para ver movimientos de otros usuarios
//			sino solo ve sus los movimientos del usuario actual
			if (!rolPermiso.get(1110)) {

				PersCaje persCaje = this.buscarPersCaje();
				cajaMovi.getPersCaje().setPersonaId(persCaje.getPersonaId());
				cajaMovi.getSucursal().setSucursalId(variablesSesion.getSucursal().getSucursalId());

			}

			cajaMoviLista.filasPagina(variablesSesion.getFilasPagina());

			this.cajaMovis = cajaMoviLista.buscar(cajaMovi, this.pagina);
			this.numeroReg = cajaMovis.size();
			this.contadorReg = cajaMoviLista.contarRegistros(cajaMovi);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public List<CajaMovi> buscarTodo() {

		List<CajaMovi> cajaMovis = new ArrayList<>();

		try {
			cajaMovis = cajaMoviLista.buscarTodo("cajaMoviId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return cajaMovis;
	}

	public List<DocuCaja> buscarDocuCajas() {

		List<DocuCaja> docuCajas = new ArrayList<>();

		DocuCaja docuCaja = new DocuCaja();
		docuCaja.setDocumento(new Documento());
		this.getCajaMovi().setDocuCaja(docuCaja);
		this.getCajaMovi().getDocuCaja().getDocumento().setEstado(true);

		try {
			docuCajas = docuCajaLista.buscar(this.getCajaMovi().getDocuCaja(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar Caja - Documento Caja"));
			e.printStackTrace();
		}

		return docuCajas;
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
					new FacesMessage(FacesMessage.SEVERITY_WARN, null, "Error el buscar DocuMoviIngr"));
			e.printStackTrace();
		}

		return docuMoviIngrs;
	}

	public List<DocuEgre> buscarDocuEgres(DocuEgre docuEgre) throws Exception {
		return docuEgreLista.buscar(docuEgre, null);
	}

	public DimmPlanCuen buscarDimmPlanCuenIva(DimmPlanCuen dimmPlanCuenBuscar) {

//		DimmPlanCuen dimmPlanCuenBuscar = new DimmPlanCuen();
		DimmPlanCuen dimmPlanCuenContabilizar = new DimmPlanCuen();

		List<DimmPlanCuen> dimmPlanCuens = new ArrayList<>();

		try {
			dimmPlanCuens = dimmPlanCuenLista.buscar(dimmPlanCuenBuscar, null);

			for (DimmPlanCuen dimmPlanCuen : dimmPlanCuens) {

				if (dimmPlanCuen.getTipoTran().equals("VENTA")) {
					dimmPlanCuenContabilizar = dimmPlanCuen;
				}
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, null,
					"Cuenta contable IVA no existe, Transacción contable no se ha generado"));
			e.printStackTrace();
		}

		return dimmPlanCuenContabilizar;
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

	public void buscarCajas(Set<Sucursal> sucursals) {

		Caja caja = new Caja();
		caja.setEstado(true);

		try {
			this.cajas = cajaLista.buscar(caja, null, sucursals);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Caja"));
			e.printStackTrace();
		}
	}

	public List<CajaMovi> buscarCajaMovis() {

		DocuCaja docuCaja = new DocuCaja();
		docuCaja.setDocumento(new Documento());

		CajaMovi cajaMovi = new CajaMovi();

		List<CajaMovi> cajaMovis = new ArrayList<>();

		cajaMovi.setSucursal(this.cajaMovi.getSucursal());
		cajaMovi.setDocuCaja(docuCaja);
		cajaMovi.setCaja(new Caja());

		PersCaje persCaje = new PersCaje();
		persCaje.setPersona(new Persona());
		cajaMovi.setPersCaje(persCaje);
		cajaMovi.setEstado(false);

		try {

			cajaMovis = cajaMoviLista.buscar(cajaMovi, this.pagina);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar documento de inicio sesion ventas"));
			e.printStackTrace();
		}

		return cajaMovis;
	}

	public List<CajaMoviTran> buscarCajaMoviTrans(CajaMoviTran cajaMoviTran) {

		List<CajaMoviTran> cajaMoviTrans = new ArrayList<>();

		try {
			cajaMoviTrans = cajaMoviTranLista.buscar(cajaMoviTran, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar detalle de trasacciones"));
			e.printStackTrace();
		}

		return cajaMoviTrans;

	}

	public PersCaje buscarPersCaje() {

//		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext()
//				.getSessionMap()
//				.get("persUsua");

		return this.persUsuaSesion.getPersona().getPersCaje();

	}

	public void buscarCmfps(CajaMovi cajaMovi) {

		CajaMoviFormPago cajaMoviFormPago = new CajaMoviFormPago();
		cajaMoviFormPago.setCajaMovi(cajaMovi);

		try {
			this.cajaMoviFormPagos = cajaMoviFormPagoLista.buscar(cajaMoviFormPago, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar movimientos caja forma pago"));
			e.printStackTrace();
		}

	}

	public void buscarRolSucus() {

		List<RolSucu> rolSucus = new ArrayList<>();

//		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
//				.get("persUsua");

		try {
//			Envia como parametro los roles del usuario que esta en sesión
			rolSucus = rolSucuLista.buscar(persUsuaSesion.getRolPersUsuas());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar sucursales del rol"));
			e.printStackTrace();
		}

//		Si tiene autorizacion para hacer movimientos de otros usuarios y de otras sucursales
//		llena la lista de sucursales caso contrario solo se muetra la sucursal a la que ingreso
		if (variablesSesion.getRolPermiso().get(1104) == true) {
			for (RolSucu rolSucu : rolSucus) {
				this.sucursals.add(rolSucu.getSucursal());
			}
		} else {
			this.sucursals.add(variablesSesion.getSucursal());
		}
	}

	public void buscarPersCajes() {

		RolSucu rolSucu = new RolSucu();

		List<RolSucu> rolSucus = new ArrayList<>();
		List<RolPersUsua> rolPersUsuas = new ArrayList<>();

		Set<PersCaje> persCajesSet = new HashSet<PersCaje>();
		
		rolSucu.setSucursal(this.cajaMovi.getSucursal());
		rolSucu.setRol(new Rol());
		rolSucu.setAcceso(true);

		this.persCajes.clear();

		try {

			rolSucus = rolSucuLista.buscar(rolSucu, null);

			for (RolSucu rolSucu2 : rolSucus) {

				RolPersUsua rolPersUsua = new RolPersUsua();
				rolPersUsua.setRol(rolSucu2.getRol());
				rolPersUsua.setPersUsua(new PersUsua());

				rolPersUsuas.addAll(rolPersUsuaLista.buscar(rolPersUsua, null));
			}

			for (RolPersUsua rolPersUsua : rolPersUsuas) {

				PersCaje persCaje = new PersCaje();
				persCaje.setPersona(rolPersUsua.getPersUsua().getPersona());
				persCaje.setEstado(true);

//				Se quita esta linea porque salen los usuarios repetidos
//				al poner en un HashSet quedan solo uno de cada usuario
//				this.persCajes.addAll(persCajeLista.buscar(persCaje, null));
				persCajesSet.addAll(persCajeLista.buscar(persCaje, null));
			}

			this.persCajes.addAll(persCajesSet);
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar cajeros"));
			e.printStackTrace();
		}

	}

	public List<TranPlan> buscarTranPlans() {

		TranPlan tranPlan = new TranPlan();

		List<TranPlan> tranPlans = new ArrayList<>();

		tranPlan.setModulo("COBROS");
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

//	Recupera el tipo de documento para generar transaccion
	public Documento buscarDocumentoPorId(Integer documentoId) throws Exception {
		return this.documentoRegis.buscarPorId(Documento.class, documentoId);
	}

	public Parametro buscarParametro(Integer parametroId) {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, parametroId);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar parametro Hora envio documentos " + parametroId));
			e.printStackTrace();
		}

		return parametro;
	}

//	Se contabiliza aqui y no en grabar porque puede estar grabando un inicio o gasto
//	y eso se tendria que contabilizar de otra manera
	public void cerraSesionVentas(CajaMovi cajaMoviInicio) {

		cajaMoviInicio.setEstado(true);

		try {
			cajaMoviRegis.modificar(cajaMoviInicio);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, "La sesión de ventas no se ha cerrado"));
			e.printStackTrace();
		}

	}

	public void contabilizar(CajaMovi cajaMoviInicio, CajaMovi cajaMoviCierre) {

		HashMap<Integer, String> transaccionInfo = new HashMap<Integer, String>();
		Integer transaccionId;

		Documento docuTran;
//		Buscar el documento contable (comprobante) que se creara en la transaccion
//		Aqui se esta poniendo a todos las transacciones generadas el comprobante superior
//		que este almacenado en cierre de caja, sin tomar en cuenta el comprobante superior
//		de cada documento
		try {
			docuTran = this
					.buscarDocumentoPorId(this.cajaMovi.getDocuCaja().getDocumento().getDocumento().getDocumentoId());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"No se ha encontrado documento contable para generar transacción"));
			e.printStackTrace();
			return;
		}

		DocuEgre docuEgre = new DocuEgre();
		Documento documento = new Documento();
		documento.setEstado(true);
		docuEgre.setDocumento(documento);

//		Buscar todos los documentos de ventas y si tiene marcada la opción
//		contabilizar se generan los movimientos contables
		List<DocuEgre> docuEgres = new ArrayList<>();
		try {
			docuEgres = this.buscarDocuEgres(docuEgre);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, null, "Error el buscar documentos de ventas"));
			e.printStackTrace();
		}

//		Modifica la nota del ingreso para pasar eso a la nota de la tansaccion
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
//		Revisar que la nota no tenga valores nulos
		if (cajaMoviCierre.getNota() == null) {
			cajaMoviCierre.setNota(cajaMoviCierre.getDocuCaja().getDocumento().getDescri() + " " + cajaMoviCierre.getSucursal().getDescri() + " " + cajaMoviCierre.getCaja().getDescri() + " " + 
					cajaMoviCierre.getPersCaje().getPersona().getApelli() + " " + cajaMoviCierre.getCajaMoviId() + " " + cajaMoviCierre.getFecha().format(dateTimeFormatter));
		} else {
			cajaMoviCierre.setNota(cajaMoviCierre.getNota() + " " + cajaMoviCierre.getDocuCaja().getDocumento().getDescri() + " " + cajaMoviCierre.getSucursal().getDescri() + " " + cajaMoviCierre.getCaja().getDescri() + " " + 
					cajaMoviCierre.getPersCaje().getPersona().getApelli() + " " + cajaMoviCierre.getCajaMoviId() + " " +  cajaMoviCierre.getFecha().format(dateTimeFormatter));	
		}

//		Revisar que la nota no tenga mas de 255 caracateres
		if (cajaMoviCierre.getNota().length() > 255) {
			cajaMoviCierre.setNota(cajaMoviCierre.getNota().substring(0,254));
		}

		if (this.cajaMovi.getDocuCaja().getDocumento().isContab()) {

//			Contabilizar Ventas				
			try {
				transaccionId = transaccionCajaMovi.contabilizarVentas(cajaMoviInicio, cajaMoviCierre, docuEgres, docuTran);

				if (transaccionId != 0) {
					transaccionInfo.put(transaccionId, "VENTA");
				}

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, null, "No se contabilizo ventas"));
				e.printStackTrace();
			}

//			Contabiliza costo de ventas - facturas
			try {

				transaccionId = transaccionCajaMovi.contabilizarCostoVentas(cajaMoviInicio, cajaMoviCierre, docuEgres, docuTran);
				if (transaccionId != 0) {
					transaccionInfo.put(transaccionId, "COSTO");
				}
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, null, "No se contabilizo costo de ventas"));
				e.printStackTrace();
			}

//			Contabilizar cobros
			try {

				transaccionId = transaccionCajaMovi.contabilizarCobros(cajaMoviInicio, cajaMoviCierre, docuEgres, docuTran);
				if (transaccionId != 0) {
					transaccionInfo.put(transaccionId, "COBROS");
				}
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, null, "No se contabilizo cobros"));
				e.printStackTrace();
			}

//			Contabilizar Notas de credito
			try {

				transaccionId = transaccionCajaMovi.contabilizarNotasCredito(cajaMoviInicio, cajaMoviCierre, docuEgres, docuTran);
				if (transaccionId != 0) {
					transaccionInfo.put(transaccionId, "NC");
				}
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, null, "No se contabilizo notas de crédito"));
				e.printStackTrace();
			}

//			Contabiliza costo de ventas - notas de credito
			try {

				transaccionId = transaccionCajaMovi.contabilizarCostoVentasNC(cajaMoviInicio, cajaMoviCierre, docuEgres, docuTran);
				if (transaccionId != 0) {
					transaccionInfo.put(transaccionId, "NC-COSTO");
				}
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
						"No se contabilizo costo de ventas de notas de crédito"));
				e.printStackTrace();
			}

//			Contabilizar Documentos de tipo anticipo que se hacen en fpme en el modulo de CXC
			try {

				transaccionId = transaccionCajaMovi.contabilizarAnticipos(cajaMoviInicio, cajaMoviCierre,  docuTran);
				if (transaccionId != 0) {
					transaccionInfo.put(transaccionId, "ANTICIPOS");
				}
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, null, "No se contabilizo anticipos"));
				e.printStackTrace();
			}

//			Graba transacciones generadas en cajaMoviTrans
//			transaccionInfo contiene los Id de las transacciones generadas
//			Se le hace de esta manera ya que si por ejemplo da un error al grabar cajaMoviTrans 
//			en this.contabilizar(documento, cajaMovi, -1)  el codigo se detiene y
//			no se generaban las de cobros, costo de ventas, etc
//			Aqui se espera que se grabe todas las transacciones y ahi graba la lista en cajaMoviTrans
			List<CajaMoviTran> cajaMoviTrans = this.crearCajaMoviTrans(cajaMoviCierre, transaccionInfo);

			for (CajaMoviTran cajaMoviTran : cajaMoviTrans) {
				this.grabarCajaMoviTran(cajaMoviTran);
			}
		}
	}

	public List<CajaMoviTran> crearCajaMoviTrans(CajaMovi cajaMovi, HashMap<Integer, String> transaccionInfo) {

		List<CajaMoviTran> cajaMoviTrans = new ArrayList<>();

		Iterator<Map.Entry<Integer, String>> iteratorTransaccionInfo = transaccionInfo.entrySet().iterator();
		while (iteratorTransaccionInfo.hasNext()) {
			Map.Entry<Integer, String> entryTransaccionInfo = iteratorTransaccionInfo.next();

			Transaccion transaccion = new Transaccion();
			transaccion.setTransaccionId(entryTransaccionInfo.getKey());
			String descri = entryTransaccionInfo.getValue();

			CajaMoviTran cajaMoviTran = new CajaMoviTran(cajaMovi, transaccion, descri);
			cajaMoviTrans.add(cajaMoviTran);
		}

		return cajaMoviTrans;
	}

	public List<CajaMoviFormPago> crearCajaMoviFormPagos(Boolean abreCaja) {

		CajaMovi cajaMovi = new CajaMovi();

		List<CajaMoviFormPago> cajaMoviFormPagos = new ArrayList<CajaMoviFormPago>();
		List<FormPago> formPagos = new ArrayList<FormPago>();

		formPagos = this.buscarFormPagos();
		formPagos = this.filtrarFormPagoVentas(formPagos);

		// Coloca las formas de pago que se llenaran automaticamente
		for (FormPago formPagoInsertar : formPagos) {

			// Si es inicio o gasto solo pone contado (getFormPagoId() == 4)
			if (this.cajaMovi.getDocuCaja().getDocumento().getFactor() != 0) {

//					if (formPagoInsertar.getTipo().equals("FP")) {
				if (formPagoInsertar.getFormPagoId() == 4) {

					CajaMoviFormPago cajaMoviFormPago = new CajaMoviFormPago();

					cajaMoviFormPago.setCajaMovi(cajaMovi);
					cajaMoviFormPago.setFormPago(formPagoInsertar);
					cajaMoviFormPago.setTotal(new BigDecimal(0));

					cajaMoviFormPagos.add(cajaMoviFormPago);
				}

			} else {

				if (formPagoInsertar.getTipo().equals("FP") || formPagoInsertar.getTipo().equals("RR")
						|| formPagoInsertar.getTipo().equals("RI")) {

					CajaMoviFormPago cajaMoviFormPago = new CajaMoviFormPago();

					cajaMoviFormPago.setCajaMovi(cajaMovi);
					cajaMoviFormPago.setFormPago(formPagoInsertar);
					cajaMoviFormPago.setTotal(new BigDecimal(0));

					cajaMoviFormPagos.add(cajaMoviFormPago);
				}
			}
		}

		return cajaMoviFormPagos;
	}

	public List<FormPagoMoviIngr> crearListaDepositos(List<Object[]> depositos) {

		TranPlan tranPlanDepo = new TranPlan();

		List<FormPagoMoviIngr> fpmis = new ArrayList<>();

		List<DocuMoviIngr> docuMoviIngrs = new ArrayList<>();
		docuMoviIngrs = buscarDocuMoviIngrs();

		// Tomando en cuenta que como predeterminado este el documento COBROS,
		// se utiliza para poner el valor predeterminado en el campo tranPlanId
		// ya que luego se ejecuta la linea this.buscarTranPlan();
		// que es la que pone el documento predeterminado en el campo
		// En el caso de que este puesto como predeterminado otro documento no afecta en
		// nada ya que el campo TranPlanId
		// solo sirve de referencia para saber que transaccion se hace en el caso que
		// sea otro pago diferente de CXC
		for (TranPlan tranPlan : tranPlans) {
			if (tranPlan.getTipo().equals("CXC")) {
				tranPlanDepo = tranPlan;
				break;
			}
		}

		for (Object[] object : depositos) {

			FormPagoMoviIngr fpmi = new FormPagoMoviIngr();
			DocuMoviIngr docuMoviIngr = new DocuMoviIngr();
			FormPago formPago = new FormPago();
			Persona persona = new Persona();

			formPago.setFormPagoId((Integer) object[1]);
			persona.setPersonaId(1); // SuperAdministrador
			docuMoviIngr = seleccionarDocuMoviIngr(docuMoviIngrs, (String) object[0]);

			fpmi.setTranPlanId(tranPlanDepo.getTranPlanId());

			fpmi.setSucursal(this.cajaMovi.getSucursal());
			fpmi.setDocuMoviIngr(docuMoviIngr);
			fpmi.setFormPago(formPago);
			fpmi.setPersona(persona);
			fpmi.setFecha(((Date) object[5]).toLocalDate());
			fpmi.setFechaHora(fpmi.getFecha().atTime(LocalTime.now()));
			fpmi.setFechaHoraRegi(LocalDateTime.now());			
			fpmi.setRefere((String) object[2]);
			fpmi.setNota((String) object[3]);
			fpmi.setTotal((BigDecimal) object[4]);
			fpmi.setEstado("PR");

			fpmis.add(fpmi);
		}
		return fpmis;
	}

//	public List<TranPlantilla> crearTranPlantilla(List<Object[]> objects, Documento documento, CajaMovi cajaMovi,
//			int posicion1, int posicion2, int orden, BigDecimal factorDebeHaber, int factorOrden) {
//
//		List<TranPlantilla> tranPlantillas = new ArrayList<>();
//
//		for (Object[] object : objects) {
//
//			PlanCuen planCuen = new PlanCuen();
//
//			planCuen.setPlanCuenId((Integer) object[posicion1]);
//			BigDecimal total = (BigDecimal) object[posicion2];
//
////			Controla que no se generen movimientos con valores en cero
//			if (total.compareTo(BigDecimal.ZERO) != 0) {
//
//				TranPlantilla tranPlantilla = new TranPlantilla(orden, cajaMovi.getNota(), null, cajaMovi.getFecha(),
//						cajaMovi.getFecha(), total.multiply(factorDebeHaber), documento.getDocuTran(), planCuen);
//
//				tranPlantillas.add(tranPlantilla);
//
//				orden = orden + factorOrden;
//			}
//		}
//
//		return tranPlantillas;
//	}

	public void crearLibroBancos(CajaMovi cajaMovi, Integer factor) {

		List<Object[]> depositos = sumarDepositos(cajaMovi, factor);
		List<FormPagoMoviIngr> fpmis = crearListaDepositos(depositos);
		insertarDepositos(fpmis);
	}

	public void descargar() {

		String nombreReporte = this.getCajaMovi().getDocuCaja().getDocumento().getFormat();

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		Parametro parametro = new Parametro();

		// Se coloca aqui la ruta para poder tener los reportes en diferentes y varias
		// carpetas
		// Sino se podria poner en la clase GenerarJasperReport
		String rutaJrxml; // = "C:\\tecnoIntel\\soem\\jasperReportes\\ingreso\\";
		// En esta ruta se compila el reporte dentro del servidor de aplicaciones dentro
		// del archivo .EAR .WEB
		String rutaReporteCompilado = "\\jasperReportes\\caja\\";

		try {

			Integer id;
			if (this.getCajaMovi().getDocuCaja().getDocumento().getFactor() == 0) {
				id = this.getCajaMovi().getCajaMovi().getCajaMoviId();
			} else {
				id = this.getCajaMovi().getCajaMoviId();
			}

			if (id == null) {
				parametrosJasper.put("cajaMoviId", 0);
			} else {
				parametrosJasper.put("cajaMoviId", id);
			}

			parametro = parametroRegis.buscarPorId(Parametro.class, 1000);
			rutaJrxml = parametro.getDescri();

			parametrosJasper.put("rutaJrxml", rutaJrxml);

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper, rutaJrxml, rutaReporteCompilado, "pdf");

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al descargar documento"));
			e.printStackTrace();
		}
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			CajaMovi cajaMovi = cajaMoviRegis.buscarPorId(CajaMovi.class, this.getId());
			cajaMoviRegis.eliminar(cajaMovi);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		return "lista?faces-redirect=true";
	}

	public String explorar() {
		return "explora?faces-redirect=true&cajaMoviId=" + this.getId();
	}

//	Implementado temporalmente de esta manera para Romolo e Remo
//	Realiza el proceso con la clase docuElectControl donde se coloca todos los
//	parametros en el constructor
//	En una proxima actualización colocar una o varias horas para hacer este proceso
	public void enviarDocumentosElectronicos() {
		
//		Al poner null en pagina recupera todas las facturas es decir ingnora la paginación
		docuElectControl.setPagina(null);

		Set<Integer> sucursals = new HashSet<>();
		sucursals.add(cajaMovi.getSucursal().getSucursalId());
		docuElectControl.setSucursals(sucursals);
		
		docuElectControl.getEgreso().getCajaMovi().setCaja(this.getCajaMovi().getCaja());
		docuElectControl.setFechaDesde(LocalDate.now().minusDays(variablesSesion.getTiempoEmisionDocuElec()));
		docuElectControl.setFechaHasta(LocalDate.now());
		docuElectControl.setReenviarEgresosRecibida(true);
//		docuElectControl.getEgreso().setFechaEmis(this.getCajaMovi().getFecha());
		docuElectControl.buscar();
		docuElectControl.procesarTodos();

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, null, "Facturas Electrónicas Procesadas"));

//		docuElectControl.setPagina(0);
//		docuElectControl.buscar();
//		docuElectControl.getEgresos();
//		for (Egreso egreso : docuElectControl.getEgresos()) {
//			System.out.println(egreso.getEgresoId() + "  Numero:  " + egreso.getNumero());
//		}

//		docuElectControl.setPagina(1);
//		docuElectControl.buscar();
//		docuElectControl.getEgresos();
//		for (Egreso egreso : docuElectControl.getEgresos()) {
//			System.out.println(egreso.getEgresoId() + "  Numero:  " + egreso.getNumero());
//		}
	}

	public List<FormPago> filtrarFormPagoVentas(List<FormPago> formPagoTmps) {

		FormPagoMoviEgre formPagoMoviEgre = new FormPagoMoviEgre();
		formPagoMoviEgre.setDocuMoviEgre(new DocuMoviEgre());
		formPagoMoviEgre.getDocuMoviEgre().setTipo("PAGO-COBRO");

		List<FormPago> formPagos = new ArrayList<FormPago>();

		try {
			formPagos = formPagoLista.filtrarFormPagoVentas(formPagoMoviEgre, formPagoTmps, persUsuaSesion,
					variablesSesion.getRolFormPagos());
		} catch (Exception e) {

			// Si el usario no tiene acceso al documento predeterminado da este error ya que
			// el filtro de formas de pago
			// se hace de acuerdo al documento seleccionado o predeterminado
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, null,
					"Excepcion - Error al filtrar formas de pago, No tiene acceso a documento predeterminado"));
			e.printStackTrace();
		}

		formPagoMoviEgre.getDocuMoviEgre().setTipo("DEPOSITO");
		try {

			List<FormPago> formPagoDepos = formPagoLista.filtrarFormPagoVentas(formPagoMoviEgre, formPagoTmps,
					persUsuaSesion, variablesSesion.getRolFormPagos());
			formPagos.addAll(formPagoDepos);

		} catch (Exception e) {

//			Si el usario no tiene acceso al documento predeterminado da este error ya que el filtro de formas de pago
//			se hace de acuerdo al documento seleccionado o predeterminado 
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, null,
					"Excepcion - Error al filtrar formas de pago, No tiene acceso a documento predeterminado"));
			e.printStackTrace();
		}

		return formPagos;
	}

	public void grabarCajaMoviTran(CajaMoviTran cajaMoviTran) {

		try {
			cajaMoviTranRegis.insertar(cajaMoviTran);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al grabar información de transacción"));
			e.printStackTrace();
		}
	}

	public String grabar() {

		String mensaje = validarGrabar();

		CajaMovi cajaMoviInicio = null;

		List<CajaMovi> cajaMovis = new ArrayList<>();

		if (!mensaje.equals("validado")) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, mensaje));
			return null;
		}

//		Los mensajes tienen RequestScope y se borran al navegar a otro pagina
//		Con este comando se guarda los mensages de confirmaci�n en navegacion
//		entre paginas
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		
//		LocalDateTime ldt = Util.crearFechaHora(cajaMovi.getFecha());
		cajaMovi.setFechaHora(cajaMovi.getFecha().atTime(LocalTime.now()));
		
		if (this.id == null) {

//			Graba la sucursal ya al momento de seleccionar la caja tambien
//			se selecciona la sucursal a la que pertenece dicha caja
			cajaMovi.setSucursal(cajaMovi.getCaja().getSucursal());

			if (cajaMovi.getDocuCaja().isAbreCaja()) {
				// Iniciar sesion ventas
				cajaMovi.setEstado(false);
			} else {
//				Registro de otros documentos gastos, cierre
				cajaMovis = this.buscarCajaMovis();

				for (CajaMovi cajaMovi : cajaMovis) {
					if (cajaMovi.getPersCaje().equals(this.cajaMovi.getPersCaje())) {
						cajaMoviInicio = cajaMovi;
					}
				}

//					Se coloca esta validacion para que si no encontro el inicio
//					no grabe ya que a veces daba error y el cierre se grabada
//					el inicio correspondiente y no valia descargar y tampoco contabilizaba
				if (cajaMoviInicio == null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
							null, "No se encontro sesión de ventas abierta"));
					return null;
				}

				cajaMovi.setCajaMovi(cajaMoviInicio);
				cajaMovi.setEstado(true);
			}

			Object id;
			try {

				id = cajaMoviRegis.insertar(cajaMovi);
				this.id = (Integer) id;

				if (this.id != 0) {
					this.insertarCajaMoviFormPagos(this.id);
				}
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion al insertar formas de pago en FPME"));
				e.printStackTrace();
			}

//			Terminar session ventas
			if (this.getCajaMovi().getDocuCaja().getDocumento().getFactor() == 0) {

//				Si el parametro de generacion por documento esta activado
//				genera una transaccion por cada documento
				if (variablesSesion.getGeneraTransaccion().equals("CC")) {
//					Genera movimientos contables
					this.contabilizar(cajaMoviInicio, this.cajaMovi);
					
//					Genera los movimientos en libro bancos es decir la forma de pago de tipo2 = "BA"
					this.crearLibroBancos(cajaMoviInicio, -1);
				}
				
				this.cerraSesionVentas(cajaMoviInicio);
			}
		} else {

			try {
				this.cajaMoviRegis.modificar(cajaMovi);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion al modificar movimiento de caja"));
				e.printStackTrace();
			}
			this.modificarCajaMoviFormPagos();

		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

//		Si es cierre de caja y esta el parametro configurado para el envio al cerrar
		if (this.getCajaMovi().getDocuCaja().getDocumento().getFactor() == 0) {

			Parametro parametro = this.buscarParametro(3252);

			if (parametro.getDescri().equals("Activar")) {
				this.enviarDocumentosElectronicos();
			}
		}

		return "explora?faces-redirect=true&cajaMoviId=" + this.getId();
	}

	public void insertarCajaMoviFormPagos(Integer cajaMoviId) {

		CajaMovi cajaMovi = new CajaMovi();

		for (CajaMoviFormPago cajaMoviFormPago : this.cajaMoviFormPagos) {

			try {
				cajaMovi.setCajaMoviId(cajaMoviId);
				cajaMoviFormPago.setCajaMovi(cajaMovi);
				cajaMoviFormPagoRegis.insertar(cajaMoviFormPago);

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al insertar formas de pago en FPME"));
				e.printStackTrace();
			}
		}
	}

	public void insertarDepositos(List<FormPagoMoviIngr> fpmis) {

		for (FormPagoMoviIngr formPagoMoviIngr : fpmis) {
			try {

				// Grabar el número de documento
				formPagoMoviIngr.setNumero(formPagoMoviIngr.getDocuMoviIngr().getDocumento().getNumero() + 1);

				// Actualizar secuencial
				formPagoMoviIngr.getDocuMoviIngr().getDocumento().setNumero(formPagoMoviIngr.getNumero());
				documentoRegis.modificar((formPagoMoviIngr.getDocuMoviIngr().getDocumento()));
				// Fin Actualizar secuencial

				formPagoMoviIngrRegis.insertar(formPagoMoviIngr);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, null, "Error al insertar deposito - FPMI"));
				e.printStackTrace();
			}
		}
	}

	public String modificar() {
		return "registra?faces-redirect=true&cajaMoviId=" + this.getId();
	}

	public void modificarCajaMoviFormPagos() {

		for (CajaMoviFormPago cajaMoviFormPago : this.cajaMoviFormPagos) {

			try {
				cajaMoviFormPagoRegis.modificar(cajaMoviFormPago);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepcion - Error al modificar formas de pago en FPME"));
				e.printStackTrace();
			}
		}
	}

	public void recuperar() {

		List<DocuCaja> docuCajas = new ArrayList<>();

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback()) {
			return;
		}

		this.tranPlans = this.buscarTranPlans();

		if (this.id == null) {

			this.cajaMovi = new CajaMovi();
			this.cajaMovi.setFecha(LocalDate.now());
			docuCajas = this.buscarDocuCajas();

			try {
				// Filtra documento de acuerdo a ROL_DOCU
				this.docuCajas = docuCajaLista.filtrarDocuCajas(docuCajas, persUsuaSesion,
						variablesSesion.getRolDocus());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error filtra documentos"));
				e.printStackTrace();
			}

		} else {

			try {

				this.cajaMovi = cajaMoviRegis.buscarPorId(CajaMovi.class, this.getId());
				this.buscarCmfps(cajaMovi);

				// Al modificar un documento coloca en la lista solo la caja que realizo el
				// movimiento es decir no se puede modificar el tipo de documento
				// if (this.id != null) {
				this.docuCajas.clear();
				this.docuCajas.add(cajaMovi.getDocuCaja());
				// }

				this.actualizarListas();

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
				e.printStackTrace();
			}
			
//			Buscar Transaccion
			Transaccion transaccionBuscar = new Transaccion();

			CajaMoviTran cajaMoviTranBuscar = new CajaMoviTran(this.cajaMovi, transaccionBuscar);
			this.cajaMoviTrans = this.buscarCajaMoviTrans(cajaMoviTranBuscar);

//			Asignar transaccion con el tipo de documento
			for (CajaMoviTran cajaMoviTran : this.cajaMoviTrans) {
				Transaccion transaccion = this
						.buscarTransaccionPorId(cajaMoviTran.getTransaccion().getTransaccionId());
				cajaMoviTran.setTransaccion(transaccion);
			}
		}
	}

	public DocuMoviIngr seleccionarDocuMoviIngr(List<DocuMoviIngr> docuMoviIngrs, String tipo) {

		DocuMoviIngr docuMoviIngrSeleccion = new DocuMoviIngr();

		for (DocuMoviIngr docuMoviIngr : docuMoviIngrs) {

			if (docuMoviIngr.getTipo().equals(tipo)) {
				docuMoviIngrSeleccion = docuMoviIngr;
				break;
			}
		}
		return docuMoviIngrSeleccion;
	}

	public List<Object[]> sumarDepositos(CajaMovi cajaMovi, Integer factor) {

		List<Object[]> depositos = new ArrayList<>();

		try {
			depositos = transaccionCajaMovi.sumarDepositos(cajaMovi.getCajaMoviId(), -1);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, null, "Error al sumar depositos"));
			e.printStackTrace();
		}

		return depositos;
	}

	public String validarGrabar() {

		String mensaje = "validado";

		CajaMovi cajaMoviInicio = null;
		Egreso egreso = new Egreso(); // Parámetro para buscar las facturas no procesadas

		List<CajaMovi> cajaMovis = new ArrayList<>();
		List<Egreso> egresos = new ArrayList<>();

		cajaMovis = this.buscarCajaMovis();

		for (CajaMovi cajaMovi : cajaMovis) {

			if (cajaMovi.getPersCaje().equals(this.cajaMovi.getPersCaje())
					|| cajaMovi.getCaja().equals(this.cajaMovi.getCaja())) {
				cajaMoviInicio = cajaMovi;
			}
		}

		// No encontro un inicio de sesión de ventas ni del cajero ni de la caja
		// seleccionada
		if (cajaMoviInicio == null) {

			if (this.getId() == null) {

				if (!this.getCajaMovi().getDocuCaja().isAbreCaja()) {
					mensaje = "No se ha inicado una sesión de ventas";
				}
			}

		} else {

			// Encontro un inicio de sesión de ventas del cajero o de la caja selecciona
			// Valida que no abra una sesión de ventas un usuario o una caja ya abierta
			if (this.getCajaMovi().getDocuCaja().isAbreCaja() && this.getId() == null) {
				mensaje = "Usuario o Caja ya tiene una sesión de ventas abierta";
			}

			// Pregunta por negacion si tanto la caja como el usuario que inicio
			// (cajaMoviInicio)
			// es igual a la caja y usuario que se esta ingresando
			// Aqui se valida que el usuario y la caja que se encontro (cajaMoviInicio) sea
			// igual al que ingreso
			if (!(cajaMoviInicio.getCaja().equals(this.cajaMovi.getCaja())
					&& cajaMoviInicio.getPersCaje().equals(cajaMovi.getPersCaje()))) {

				mensaje = "Cajero o caja no tiene una sesión de ventas abierta";
			}

			// Pregunta si tanto la caja como el usuario que inicio (cajaMoviInicio)
			// es igual a la caja y usuario que se esta ingresando
			if (cajaMoviInicio.getCaja().equals(this.cajaMovi.getCaja())
					&& cajaMoviInicio.getPersCaje().equals(cajaMovi.getPersCaje())) {

				// Pregunta si es cierre de caja
				if (this.cajaMovi.getDocuCaja().getDocumento().getFactor() == 0) {

					// Buscar que todas los documentos que no estan procesados sino no se puede
					// cerrar caja
					try {

						egreso.setSucursal(new Sucursal());
						PersClie persClie = new PersClie();
						persClie.setPersona(new Persona());
						egreso.setPersClie(persClie);
						DocuEgre docuEgre = new DocuEgre();
						docuEgre.setDocumento(new Documento());
						egreso.setDocuEgre(docuEgre);

						egreso.setCajaMovi(cajaMoviInicio);
						egreso.setEstado("GR");
						egresos = egresoLista.buscar(egreso, null, new HashSet<Integer>());

						// Controla que todos los tipos de documentos esten procesados
						if (egresos.size() != 0 || !egresos.isEmpty()) {
							mensaje = "Existen documentos no procesados";
						}

						// Controla documentos procesados de acuerdo al tipo de documento
						// en este caso se puede omitir los documentos como proformas
						// for (Egreso egresoNoProcesado : egresos) {
						//
						// if (egresoNoProcesado.getDocuEgre().getDocumento().getFactor() != 0) {
						// mensaje = "Existen documentos no procesados";
						// }
						//
						// }

					} catch (Exception e) {
						mensaje = "Excepción - Error al buscar documentos no procesados";
						// Este mensaje porque se muestra al grabar(); aqui no hace falta
						// FacesContext.getCurrentInstance().addMessage(null,
						// new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al
						// buscar documentos no procesados"));
						e.printStackTrace();
					}

				}
			}
		}

		return mensaje;
	}
	
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
		egresoBuscar.setCajaMovi(cajaMovi);
		
		try {
			contadorDocuNoAuto = egresoLista.contarRegistros2(egresoBuscar, fechaDesde, fechaHasta, sucursals);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	public CajaMovi getCajaMovi() {
		return cajaMovi;
	}

	public void setCajaMovi(CajaMovi cajaMovi) {
		this.cajaMovi = cajaMovi;
	}

	public List<CajaMovi> getCajaMovis() {
		return cajaMovis;
	}

	public void setCajaMovis(List<CajaMovi> cajaMovis) {
		this.cajaMovis = cajaMovis;
	}

	public List<DocuCaja> getDocuCajas() {
		return docuCajas;
	}

	public void setDocuCajas(List<DocuCaja> docuCajas) {
		this.docuCajas = docuCajas;
	}

	public List<Caja> getCajas() {
		return cajas;
	}

	public void setCajas(List<Caja> cajas) {
		this.cajas = cajas;
	}

	public List<PersCaje> getPersCajes() {
		return persCajes;
	}

	public void setPersCajes(List<PersCaje> persCajes) {
		this.persCajes = persCajes;
	}

	public List<CajaMoviFormPago> getCajaMoviFormPagos() {
		return cajaMoviFormPagos;
	}

	public void setCajaMoviFormPagos(List<CajaMoviFormPago> cajaMoviFormPagos) {
		this.cajaMoviFormPagos = cajaMoviFormPagos;
	}

	public Set<Sucursal> getSucursals() {
		return sucursals;
	}

	public void setSucursals(Set<Sucursal> sucursals) {
		this.sucursals = sucursals;
	}

	public List<CajaMoviTran> getCajaMoviTrans() {
		return cajaMoviTrans;
	}

	public void setCajaMoviTrans(List<CajaMoviTran> cajaMoviTrans) {
		this.cajaMoviTrans = cajaMoviTrans;
	}

	public long getContadorDocuNoAuto() {
		return contadorDocuNoAuto;
	}

	public void setContadorDocuNoAuto(long contadorDocuNoAuto) {
		this.contadorDocuNoAuto = contadorDocuNoAuto;
	}
}
