package ec.com.tecnointel.soem.seguridad.controlador;

import java.io.IOException;
import java.io.Serializable;
import java.security.cert.CertificateParsingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.egreso.listaInt.PersClieListaInt;
import ec.com.tecnointel.soem.egreso.listaInt.PersCobrListaInt;
import ec.com.tecnointel.soem.egreso.listaInt.PersVendListaInt;
import ec.com.tecnointel.soem.egreso.modelo.PersClie;
import ec.com.tecnointel.soem.egreso.modelo.PersCobr;
import ec.com.tecnointel.soem.egreso.modelo.PersVend;
import ec.com.tecnointel.soem.firmaElec.registroImp.validarM1;
import ec.com.tecnointel.soem.firmaElec.registroImp.validarM2;
import ec.com.tecnointel.soem.firmaElec.registroInt.NotificacionValidezFirmaInt;
import ec.com.tecnointel.soem.firmaElec.registroInt.ValidarRucFirmaInt;
import ec.com.tecnointel.soem.general.controlador.VariablesSesion;
import ec.com.tecnointel.soem.parametro.listaInt.ParametroListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.SucuBodeListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.SucuPrecListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.SucursalListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Bodega;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.modelo.Precio;
import ec.com.tecnointel.soem.parametro.modelo.SucuBode;
import ec.com.tecnointel.soem.parametro.modelo.SucuPrec;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.registroInt.DimmRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.ParametroRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.SucursalRegisInt;
import ec.com.tecnointel.soem.seguridad.listaInt.PersUsuaListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolBodeListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolDocuListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolFormPagoListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolPermListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolPrecListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolSucuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.Permiso;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolBode;
import ec.com.tecnointel.soem.seguridad.modelo.RolDocu;
import ec.com.tecnointel.soem.seguridad.modelo.RolFormPago;
import ec.com.tecnointel.soem.seguridad.modelo.RolPerm;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import xades4j.providers.SigningCertChainException;
import xades4j.verification.UnexpectedJCAException;

@Named
@ViewScoped
public class IniciarSesionControl implements Serializable {

	private PersUsua persUsua;
	private Sucursal sucursal;

	private VariablesSesion variablesSesion;

	private List<Sucursal> sucursals;

	@Inject
	PersUsuaListaInt persUsuaLista;

	@Inject
	SucursalListaInt sucursalLista;

	@Inject
	SucursalRegisInt sucursalRegis;

	@Inject
	RolPermListaInt rolPermLista;

	@Inject
	RolPrecListaInt rolPrecLista;

	@Inject
	RolSucuListaInt rolSucuLista;

	@Inject
	RolBodeListaInt rolBodeLista;

	@Inject
	RolDocuListaInt rolDocuLista;

	@Inject
	RolFormPagoListaInt rolFormPagoLista;

	@Inject
	SucuBodeListaInt sucuBodeLista;

	@Inject
	SucuPrecListaInt sucuPrecLista;

	@Inject
	ParametroRegisInt parametroRegis;

	@Inject
	ParametroListaInt parametroLista;

	@Inject
	PersClieListaInt persClieLista;

	@Inject
	PersVendListaInt persVendLista;

	@Inject
	PersCobrListaInt persCobrLista;

	@Inject
	DimmRegisInt DimmRegis;

	@Inject
	NotificacionValidezFirmaInt notificacionValidezFirma;

	private static final long serialVersionUID = -3701038239830665291L;

	@PostConstruct
	public void iniciar() {
		persUsua = new PersUsua();
		sucursal = new Sucursal();

		variablesSesion = new VariablesSesion();

		this.buscarSucursals();
	}

	public String buscarUsuarios() {

		Boolean accesoSucursal = false;

//		List<Integer> rols = new ArrayList<Integer>();

		List<RolSucu> rolSucus = new ArrayList<>();

		this.cerrarSesion();

		String navegar = "";

		PersUsua persUsua = new PersUsua();

		try {

			persUsua = persUsuaLista.buscar(this.getPersUsua());

			if (persUsua != null) {

				try {

//					Buscar el acceso del rol a cada una de las sucursales
					rolSucus = rolSucuLista.buscar(persUsua.getRolPersUsuas());

//					Comparar sucursal del rol con la sucursal que esta intentando ingresar
					for (RolSucu rolSucu : rolSucus) {
						if (rolSucu.getSucursal().equals(this.getSucursal())) {
							accesoSucursal = true;
							break;
						}
					}

				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
							null, "Excepcion - Error al buscar roles por sucursal"));
					e.printStackTrace();
				}

//				Si no coincide la sucursal del rol con la sucursal del usuario. No ingresa al sistema
				if (accesoSucursal.equals(false)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
							null, "Usuario no tiene acceso a esta sucursal"));
					return null;
				}

				this.cargarFilasPagina();
				this.cargarFilasProductosIngreso();
				this.cargarFilasProductosEgreso();
				this.cargarFilasClientesEgreso();
				this.cargarPersClie();
				this.cargarPersVend();
				this.cargarDatosRoles();
				this.cargarRolPerms(persUsua);
				this.cargarColumnasProductos();
				this.cargarActivarCodigoBarra();
				this.cargarActivarDescri();
				this.cargarActivarPrecio();
				this.cargarActivarImagen();
				this.cargarActivarConsultaPrecio();
				this.cargarActivarMesa();
				this.cargarAcumularCantidad();
				this.cargarActivarIngrDetaCodigoBarra();
				this.cargarActivarIngrDetaDescri();
				this.cargarActivarIngrDetaPrecio();
				this.cargarActivarIngrDetaCostoUlti();
				this.cargarActivarIngrDetaCostoProm();
				this.cargarCodigosImpuestos();
				this.cargarParametrosFactElec();
				this.cargarNavegacionVentaDeta();
				this.cargarMostrarCodigoEgreDeta();
				this.cargarMostrarCodigoIngrDeta();
				this.cargarGeneraTransaccion();
				this.cargarTieneVariosCertificados();
				this.cargarTarifaIvaActual();
				cargarTiempoEmisionDocuElec();
				cargarDiasAnulacionDocuElec();
				cargarAnulaFacturasConsumidorFinal();

				Boolean firmaValida = this.validarRucFirma();

				Parametro parametro = buscarParametro(new Parametro(6250));
				if (parametro.getDescri().equals("true") && firmaValida) {
					verificarFechaFirma();
				}

				variablesSesion.setSucursal(this.sucursal);
				variablesSesion.setPersUsua(persUsua);

				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("variablesSesion",
						variablesSesion);
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("persUsua", persUsua);

				navegar = "inicio?faces-redirect=true";

			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Credenciales incorrectas"));
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar usuario"));
			e.printStackTrace();
		}

//		if (!idenSistema.getEstadoLicencia().equals("activado")) {
//
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
//					"Licencia Incorrecta, comuniquese con su distribuidor"));
//
//			navegar = "/ppsj/parametro/parametro/registraLice.xhtml";
//		}

		return navegar;
	}

	private void cargarTiempoEmisionDocuElec() {
		Parametro parametro = new Parametro();

		try {
//			Este parametro 5061 indica el tiempo de emision de comprobantes electronicos
			parametro = parametroRegis.buscarPorId(Parametro.class, 3281);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar parametro 3281"));
			e.printStackTrace();
		}

		variablesSesion.setTiempoEmisionDocuElec(Integer.parseInt(parametro.getDescri()));		
	}

	public void cargarAnulaFacturasConsumidorFinal() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3270);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar parametro 3270 - Anualcion facturas a consumidor final"));
			e.printStackTrace();
		}

		if (parametro.getDescri().equals("true")) {
			variablesSesion.setAnulaFactConsFina(true);
		} else {
			variablesSesion.setAnulaFactConsFina(false);
		}
	}

	private void cargarDiasAnulacionDocuElec() {
		Parametro parametro = new Parametro();

		try {
//			Este parametro 5061 indica el tiempo de emision de comprobantes electronicos
			parametro = parametroRegis.buscarPorId(Parametro.class, 3280);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar parametro 3280"));
			e.printStackTrace();
		}

		variablesSesion.setDiasAnulacionDocuElec(Integer.parseInt(parametro.getDescri()));		
	}

	private Boolean validarRucFirma() {

		Boolean firmaValida = false;

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		ValidarRucFirmaInt validarRucFirmaM1 = new validarM1();
		ValidarRucFirmaInt validarRucFirmaM2 = new validarM2();

		try {

			boolean rucValidoM1 = validarRucFirmaM1.validarRucFirma(this.sucursal);
			boolean rucValidoM2 = validarRucFirmaM2.validarRucFirma(this.sucursal);

			if (!rucValidoM1 && !rucValidoM2) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Ruc de la aplicación no corresponde al Ruc del certificado electrónico"));
			} else {
				firmaValida = true;
			}

		} catch (SigningCertChainException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"No reconoce firma electrónica. No contiene codigo para Ruc 3.11"));
			e.printStackTrace();
		} catch (UnexpectedJCAException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"No reconoce firma electrónica. Clave, ruta o descripción incorrecto - SucuCertEmis"));
			e.printStackTrace();
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Firma Electrónica - Error de lectura"));
			e.printStackTrace();
		} catch (CertificateParsingException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Firma Electrónica - Error de lectura de certificado"));
			e.printStackTrace();
		}
		return firmaValida;
	}

	private void cargarTarifaIvaActual() {

		Parametro parametro = new Parametro();

		try {
//			Este parametro 5061 indica el dimm_id del iva actual
			parametro = parametroRegis.buscarPorId(Parametro.class, 5061);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error parametro iva actual 5061"));
			e.printStackTrace();
		}

		variablesSesion.setDimmIdIvaActual(Integer.parseInt(parametro.getDescri()));
	}

	public void buscarSucursals() {

		Sucursal sucursal = new Sucursal();

		try {

			this.sucursals = sucursalLista.buscar(sucursal, null);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar sucursales"));
			e.printStackTrace();
		}

	}

	public void cerrarSesion() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	}

	public void cargarFilasPagina() {

		Parametro parametro = new Parametro();

		try {
//			6100 Id para traer el numero de filas que se mostraran en todas las paginas (Lista)
//			1100, 2100, 3100, 4100, 5100, 6100, 7100, 8100 parametros reservaddos
//			para implementar paginacion por cada modulo
			parametro = parametroRegis.buscarPorId(Parametro.class, 6100);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar filas por pagina 6100"));
			e.printStackTrace();
		}

		variablesSesion.setFilasPagina(Integer.parseInt(parametro.getDescri()));

	}

	public void cargarFilasProductosEgreso() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3101);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar filas productos para egreso 3101"));
			e.printStackTrace();
		}

		variablesSesion.setFilasProductosEgreso(Integer.parseInt(parametro.getDescri()));

	}

	public void cargarFilasClientesEgreso() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3102);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar filas clientes egreso 3102"));
			e.printStackTrace();
		}

		variablesSesion.setFilasClientesEgreso(Integer.parseInt(parametro.getDescri()));

	}

	public void cargarFilasProductosIngreso() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 4101);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar productos Ingreso 4101"));
			e.printStackTrace();
		}

		variablesSesion.setFilasProductosIngreso(Integer.parseInt(parametro.getDescri()));

	}

//	Modificar cuando se active sucursales
//	ya que se aumenta una tabla que gestione estos parametros por cada sucursal
	public void cargarPersClie() {

		Parametro parametro = new Parametro();
		Persona persona = new Persona();

		try {

			parametro = parametroRegis.buscarPorId(Parametro.class, 3103);

		} catch (Exception e1) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar parametro cliente predeterminado 3103"));
			e1.printStackTrace();
		}

		persona.setCedulaRuc(parametro.getDescri());

		List<PersClie> persClies = new ArrayList<>();

		PersClie persClie = new PersClie();
		persClie.setPersona(persona);
		persClie.setEstado(true);

		try {

			persClies = persClieLista.buscar(persClie, null, null);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar cliente predeterminado"));
			e.printStackTrace();
		}

		if (persClies.size() != 0) {
			variablesSesion.setPersClie(persClies.get(0));
		} else {
			variablesSesion.setPersClie(new PersClie());
		}
	}

//	Modificar cuando se active sucursales
//	ya que se aumenta una tabla que gestione estos parametros por cada sucursal
	public void cargarPersVend() {

		Parametro parametro = new Parametro();
		Persona persona = new Persona();

		try {

			parametro = parametroRegis.buscarPorId(Parametro.class, 3104);

		} catch (Exception e1) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar parametro vendedor predeterminado 3104"));
			e1.printStackTrace();
		}

		persona.setCedulaRuc(parametro.getDescri());

		List<PersVend> persVends = new ArrayList<>();

		PersVend persVend = new PersVend();
		persVend.setPersona(persona);
		persVend.setEstado(true);

		try {

			persVends = persVendLista.buscar(persVend, null);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar vendedor predeterminado"));
			e.printStackTrace();
		}

		if (persVends.size() != 0) {
			variablesSesion.setPersVend(persVends.get(0));
		} else {
			variablesSesion.setPersVend(new PersVend());
		}

	}

//	Modificar cuando se active sucursales
//	ya que se aumenta una tabla que gestione estos parametros por cada sucursal
	public void cargarPersCobr() {

		Parametro parametro = new Parametro();
		Persona persona = new Persona();

		try {

			parametro = parametroRegis.buscarPorId(Parametro.class, 3105);

		} catch (Exception e1) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar parametro cobrador predeterminado 3105"));
			e1.printStackTrace();
		}

		persona.setCedulaRuc(parametro.getDescri());

		List<PersCobr> persCobrs = new ArrayList<>();

		PersCobr persCobr = new PersCobr();
		persCobr.setPersona(persona);
		persCobr.setEstado(true);

		try {

			persCobrs = persCobrLista.buscar(persCobr, null);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar cobrador predeterminado"));
			e.printStackTrace();
		}

		if (persCobrs.size() != 0) {
			variablesSesion.setPersCobr(persCobrs.get(0));
		} else {
			variablesSesion.setPersCobr(new PersCobr());
		}

	}

	public void cargarDatosRoles() {

//		Estas listas se utilizan para hacer comparaciones y de acuerdo a eso filtrar datos de acuerd a cada rol
		RolPerm rolPerm = new RolPerm();
		RolDocu rolDocu = new RolDocu();
		RolPrec rolPrec = new RolPrec();
		RolSucu rolSucu = new RolSucu();
		RolBode rolBode = new RolBode();
		RolFormPago rolFormPago = new RolFormPago();

		SucuBode sucuBode = new SucuBode();
		SucuPrec sucuPrec = new SucuPrec();

		List<RolPerm> rolPerms = new ArrayList<RolPerm>();
		List<RolDocu> rolDocus = new ArrayList<RolDocu>();
		List<RolPrec> rolPrecs = new ArrayList<RolPrec>();
		List<RolSucu> rolSucus = new ArrayList<RolSucu>();
		List<RolBode> rolBodes = new ArrayList<RolBode>();
		List<RolFormPago> rolFormPagos = new ArrayList<RolFormPago>();

		List<SucuBode> sucuBodes = new ArrayList<SucuBode>();
		List<SucuPrec> sucuPrecs = new ArrayList<SucuPrec>();

		// Cargar permisos
		rolPerm.setRol(new Rol());
		rolPerm.setPermiso(new Permiso());
		rolPerm.setAcceso(true);

		try {
			rolPerms = rolPermLista.buscar(rolPerm, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Error al crear menu. cargar roles-documentos"));
			e.printStackTrace();
		}

		variablesSesion.setRolPerms(rolPerms);

		// Carga documentos
		rolDocu.setRol(new Rol());
		rolDocu.setDocumento(new Documento());
		rolDocu.setAcceso(true);

		try {
			rolDocus = rolDocuLista.buscar(rolDocu, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Error al crear menu. cargar roles-documentos"));
			e.printStackTrace();
		}

		variablesSesion.setRolDocus(rolDocus);

		// Cargar precios
		rolPrec.setRol(new Rol());
		rolPrec.setPrecio(new Precio());
		rolPrec.setSucursal(new Sucursal());
		rolPrec.setAcceso(true);

		try {
			rolPrecs = rolPrecLista.buscar(rolPrec, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Error al crear menu. cargar roles-precios"));
			e.printStackTrace();
		}

		variablesSesion.setRolPrecs(rolPrecs);

		// Cargar sucursales
		rolSucu.setRol(new Rol());
		rolSucu.setSucursal(new Sucursal());
		rolSucu.setAcceso(true);

		try {
			rolSucus = rolSucuLista.buscar(rolSucu, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Error al crear menu. cargar roles-sucursales"));
			e.printStackTrace();
		}

		variablesSesion.setRolSucus(rolSucus);

		// Cargar bodegas
		rolBode.setRol(new Rol());
		rolBode.setBodega(new Bodega());
		rolBode.setSucursal(new Sucursal());
		rolBode.setAcceso(true);

		try {
			rolBodes = rolBodeLista.buscar(rolBode, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Error al crear menu. cargar roles-bodegas"));
			e.printStackTrace();
		}

		variablesSesion.setRolBodes(rolBodes);

		// Cargar formas de pago
		rolFormPago.setRol(new Rol());
		rolFormPago.setFormPago(new FormPago());
		rolFormPago.setAcceso(true);

		try {
			rolFormPagos = rolFormPagoLista.buscar(rolFormPago, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Error al crear menu. cargar roles-formas de pago"));
			e.printStackTrace();
		}

		variablesSesion.setRolFormPagos(rolFormPagos);

		// Cargar sucursal-bodegas
//		Sucursal sucursal = variablesSesion.getSucursal();

		sucuBode.setSucursal(this.sucursal);
		sucuBode.setBodega(new Bodega());
		sucuBode.setAcceso(true);

		try {
			sucuBodes = sucuBodeLista.buscar(sucuBode, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Error al crear menu. cargar sucursal-bodega"));
			e.printStackTrace();
		}

		variablesSesion.setSucuBodes(sucuBodes);

		// Cargar sucursal-precios
		sucuPrec.setSucursal(sucursal);
		sucuPrec.setPrecio(new Precio());
		sucuPrec.setAcceso(true);

		try {
			sucuPrecs = sucuPrecLista.buscar(sucuPrec, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Error al crear menu. cargar sucursal-precio"));
			e.printStackTrace();
		}

		variablesSesion.setSucuPrecs(sucuPrecs);

	}

	public void cargarCodigosImpuestos() {

		Dimm dimm = new Dimm();

		try {
			dimm = DimmRegis.buscarPorId(Dimm.class,7050);
			variablesSesion.setCodigoIva(dimm.getCodigo());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Error al buscar códigos de impuestos 7050 - Iva"));
			e.printStackTrace();
		}

		try {
			dimm = DimmRegis.buscarPorId(Dimm.class, 7051);
			variablesSesion.setCodigoIce(dimm.getCodigo());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Error al buscar códigos de impuestos 7051 - Ice"));
			e.printStackTrace();
		}

		try {
			dimm = DimmRegis.buscarPorId(Dimm.class, 7052);
			variablesSesion.setCodigoIrbpnr(dimm.getCodigo());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Error al buscar códigos de impuestos 7052 - Irbpnr"));
			e.printStackTrace();
		}

		try {
			dimm = DimmRegis.buscarPorId(Dimm.class, 7100);
			variablesSesion.setCodigoReteRenta(dimm.getCodigo());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Error al buscar códigos de retención 7100 - Renta"));
			e.printStackTrace();
		}

		try {
			dimm = DimmRegis.buscarPorId(Dimm.class, 7101);
			variablesSesion.setCodigoReteIva(dimm.getCodigo());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Error al buscar códigos de retencion 7101 - Iva"));
			e.printStackTrace();
		}

		try {
			dimm = DimmRegis.buscarPorId(Dimm.class, 7102);
			variablesSesion.setCodigoReteIsd(dimm.getCodigo());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Error al buscar códigos de retencion 7102 - Isd"));
			e.printStackTrace();
		}

	}

//	Grabar permisos de cada rol
	public void cargarRolPerms(PersUsua persUsua) {

		List<Rol> roles = new ArrayList<>();
		RolPerm rolPermBuscar = new RolPerm();
		List<RolPerm> rolPerms = new ArrayList<>();
		Set<Permiso> permisos = new HashSet<>();

		variablesSesion.getRolPermiso().clear();

		rolPerms = variablesSesion.getRolPerms();

//		Recorre rolPerm para determinar cual tiene acceso true
		for (RolPerm rolPerm : rolPerms) {
			permisos.add(rolPerm.getPermiso());
		}

		Set<RolPersUsua> rolPersUsuas = persUsua.getRolPersUsuas();
		for (RolPersUsua rolPersUsua : rolPersUsuas) {
			roles.add(rolPersUsua.getRol());
		}

		for (Rol rol : roles) {
			for (Permiso permiso : permisos) {
				rolPermBuscar.setRol(rol);
				rolPermBuscar.setPermiso(permiso);
				if (rolPerms.contains(rolPermBuscar)) {
					variablesSesion.getRolPermiso().set(permiso.getPermisoId(), true);
				}
			}
		}
	}

	public void cargarColumnasProductos() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3120);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar número de columnas 3120"));
			e.printStackTrace();
		}

		variablesSesion.setColumnasProductos(Integer.parseInt(parametro.getDescri()));
	}

	public void cargarActivarCodigoBarra() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3121);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar activar código barra 3121"));
			e.printStackTrace();
		}

		if (parametro.getDescri().equals("true")) {
			variablesSesion.setActivarCodigoBarra(true);
		} else {
			variablesSesion.setActivarCodigoBarra(false);
		}
	}

	public void cargarActivarDescri() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3122);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar activar descripción 3122"));
			e.printStackTrace();
		}

		if (parametro.getDescri().equals("true")) {
			variablesSesion.setActivarDescri(true);
		} else {
			variablesSesion.setActivarDescri(false);
		}
	}

	public void cargarActivarPrecio() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3123);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar activar precio 3123"));
			e.printStackTrace();
		}

		if (parametro.getDescri().equals("true")) {
			variablesSesion.setActivarPrecio(true);
		} else {
			variablesSesion.setActivarPrecio(false);
		}
	}

	public void cargarActivarImagen() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3124);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar activar imagen 3124"));
			e.printStackTrace();
		}

		if (parametro.getDescri().equals("true")) {
			variablesSesion.setActivarImagen(true);
		} else {
			variablesSesion.setActivarImagen(false);
		}
	}

	public void cargarActivarConsultaPrecio() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3125);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar activar consulta precio 3125"));
			e.printStackTrace();
		}

		if (parametro.getDescri().equals("true")) {
			variablesSesion.setActivarConsultaPrecio(true);
		} else {
			variablesSesion.setActivarConsultaPrecio(false);
		}
	}

	public void cargarActivarMesa() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3126);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar activar mesa 3126"));
			e.printStackTrace();
		}

		if (parametro.getDescri().equals("true")) {
			variablesSesion.setActivarMesa(true);
		} else {
			variablesSesion.setActivarMesa(false);
		}
	}

	public void cargarAcumularCantidad() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3149);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar acumular cantidad 3149"));
			e.printStackTrace();
		}

		if (parametro.getDescri().equals("true")) {
			variablesSesion.setAcumularCantidad(true);
		} else {
			variablesSesion.setAcumularCantidad(false);
		}
	}

	public void cargarFactElecVentaAuto() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3250);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al activar facturación electrónica 3250"));
			e.printStackTrace();
		}

		variablesSesion.setFactElecVentaAuto(parametro.getDescri());
	}

	public void cargarFactElecCompraAuto() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3251);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al activar retención electrónica 3251"));
			e.printStackTrace();
		}

		variablesSesion.setFactElecCompraAuto(parametro.getDescri());
	}

	public void cargarGeneraTransaccion() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 2100);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepción - No se encontro generar transacción 2100"));
			e.printStackTrace();
		}

		variablesSesion.setGeneraTransaccion(parametro.getDescri());
	}

	public void cargarTieneVariosCertificados() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3400);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepción - No se encontro tiene varios certificados 3400"));
			e.printStackTrace();
		}

		variablesSesion.setVariosCertificados(parametro.getDescri());
	}

	public void cargarActivarIngrDetaCodigoBarra() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 4121);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar activar código barra 4121"));
			e.printStackTrace();
		}

		if (parametro.getDescri().equals("true")) {
			variablesSesion.setActivarIngrDetaCodigoBarra(true);
		} else {
			variablesSesion.setActivarIngrDetaCodigoBarra(false);
		}
	}

	public void cargarActivarIngrDetaDescri() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 4122);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar activar descripción 4122"));
			e.printStackTrace();
		}

		if (parametro.getDescri().equals("true")) {
			variablesSesion.setActivarIngrDetaDescri(true);
		} else {
			variablesSesion.setActivarIngrDetaDescri(false);
		}
	}

	public void cargarActivarIngrDetaPrecio() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 4123);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar activar precio 4123"));
			e.printStackTrace();
		}

		if (parametro.getDescri().equals("true")) {
			variablesSesion.setActivarIngrDetaPrecio(true);
		} else {
			variablesSesion.setActivarIngrDetaPrecio(false);
		}
	}

	public void cargarActivarIngrDetaCostoUlti() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 4124);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar activar costo último 4124"));
			e.printStackTrace();
		}

		if (parametro.getDescri().equals("true")) {
			variablesSesion.setActivarIngrDetaCostoUlti(true);
		} else {
			variablesSesion.setActivarIngrDetaCostoUlti(false);
		}
	}

	public void cargarActivarIngrDetaCostoProm() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 4125);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar activar costo promedio 4125"));
			e.printStackTrace();
		}

		if (parametro.getDescri().equals("true")) {
			variablesSesion.setActivarIngrDetaCostoProm(true);
		} else {
			variablesSesion.setActivarIngrDetaCostoProm(false);
		}
	}

	public void cargarParametrosFactElec() {

		String token = null;

		Parametro parametro = new Parametro();
		List<Parametro> parametros = new ArrayList<>();

		parametro.setCodigo("Egreso-FactElecToken");
		parametro.setEstado(true);
		try {

			parametros = parametroLista.buscar(parametro, null);
			for (Parametro parametroToken : parametros) {
				if (parametroToken.isEstado()) {
					token = parametroToken.getDescri();
					break;
				}
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar token facturación electrónica"));
			e.printStackTrace();
		}
		variablesSesion.setToken(token);

		try {

			parametro = parametroRegis.buscarPorId(Parametro.class, 3200);
			variablesSesion.setEsperaAuto(Integer.parseInt(parametro.getDescri()));

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar tiempo de espera autorización facturación electrónica"));
			e.printStackTrace();
		}

		try {

			parametro = parametroRegis.buscarPorId(Parametro.class, 3210);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar proxy activar facturación electrónica"));
			e.printStackTrace();
		}

		if (parametro.getDescri().equals("true")) {
			variablesSesion.setProxyActivar(true);
		} else {
			variablesSesion.setProxyActivar(false);
		}

		try {

			parametro = parametroRegis.buscarPorId(Parametro.class, 3211);
			variablesSesion.setProxyIp(parametro.getDescri());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar proxy IP facturación electrónica"));
			e.printStackTrace();
		}

		try {

			parametro = parametroRegis.buscarPorId(Parametro.class, 3212);
			variablesSesion.setProxyPuerto(parametro.getDescri());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar proxy puerto facturación electrónica"));
			e.printStackTrace();
		}

		try {

			parametro = parametroRegis.buscarPorId(Parametro.class, 3213);
			variablesSesion.setProxyUsuario(parametro.getDescri());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar proxy usuario facturación electrónica"));
			e.printStackTrace();
		}

		try {

			parametro = parametroRegis.buscarPorId(Parametro.class, 3214);
			variablesSesion.setProxyClave(parametro.getDescri());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar proxy clave facturación electrónica"));
			e.printStackTrace();
		}

		try {

			parametro = parametroRegis.buscarPorId(Parametro.class, 3220);
			variablesSesion.setUrlProduccion(parametro.getDescri());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar URL producción facturación electrónica"));
			e.printStackTrace();
		}

		try {

			parametro = parametroRegis.buscarPorId(Parametro.class, 3221);
			variablesSesion.setUrlPruebas(parametro.getDescri());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar URL pruebas facturación electrónica"));
			e.printStackTrace();
		}

		try {

			parametro = parametroRegis.buscarPorId(Parametro.class, 3222);
			variablesSesion.setRutaCertificadoSerWebSri(parametro.getDescri());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion 3222 - Error al buscar ruta certificado servicio Web "));
			e.printStackTrace();
		}

		try {

			parametro = parametroRegis.buscarPorId(Parametro.class, 3230);
			variablesSesion.setCodiClav(parametro.getDescri());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar codígo facturación electrónica"));
			e.printStackTrace();
		}

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3240);
			variablesSesion.setRutaGenerados(parametro.getDescri());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar carpeta de documentos generados"));
			e.printStackTrace();
		}

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3241);
			variablesSesion.setRutaFirmados(parametro.getDescri());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar carpeta de documentos firmados"));
			e.printStackTrace();
		}

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3242);
			variablesSesion.setRutaAutorizados(parametro.getDescri());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar carpeta de documentos autorizados"));
			e.printStackTrace();
		}

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3170);
			variablesSesion.setLeyenda1(parametro.getDescri());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar leyenda 1"));
			e.printStackTrace();
		}

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3171);
			variablesSesion.setLeyenda2(parametro.getDescri());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar leyenda 2"));
			e.printStackTrace();
		}

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3172);
			variablesSesion.setMsgInfoAdicional(parametro.getDescri());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar msgInfoAdicional"));
			e.printStackTrace();
		}

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 4251);
			variablesSesion.setRutaDescargados(parametro.getDescri());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar carpeta de documentos descargados"));
			e.printStackTrace();
		}
	}

	public void cargarNavegacionVentaDeta() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3350);
			variablesSesion.setVentaDetaAlCargar(parametro.getDescri());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar ventaDetaAlcargar - 3350"));
			e.printStackTrace();
		}

//		La unión de los parametros 3351 y 3352 forman al valor para la navegacion en la pagina
		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3351);
			variablesSesion.setVentaDetaAlSeleProd1(parametro.getDescri());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar ventaDetaAlSeleProd1 - 3351"));
			e.printStackTrace();
		}

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3352);
			variablesSesion.setVentaDetaAlSeleProd2(parametro.getDescri());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar ventaDetaAlSeleProd2 - 3352"));
			e.printStackTrace();
		}

		this.cargarFactElecVentaAuto();
		this.cargarFactElecCompraAuto();
	}

	public void cargarMostrarCodigoEgreDeta() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3127);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar mostrar codigo en detalle de ventas 3127"));
			e.printStackTrace();
		}

		if (parametro.getDescri().equals("true")) {
			variablesSesion.setMostrarCodigoEgreDeta(true);
		} else {
			variablesSesion.setMostrarCodigoEgreDeta(false);
		}
	}

	public void cargarMostrarCodigoIngrDeta() {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 4126);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar mostrar codigo en detalle de compras 4126"));
			e.printStackTrace();
		}

		if (parametro.getDescri().equals("true")) {
			variablesSesion.setMostrarCodigoIngrDeta(true);
		} else {
			variablesSesion.setMostrarCodigoIngrDeta(false);
		}
	}

//	Metodo para validar firma
	public Parametro buscarParametro(Parametro parametroBuscar) {

		Parametro parametro = new Parametro();

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, parametroBuscar.getParametroId());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar mostrar codigo en detalle de compras 4126"));
			e.printStackTrace();
		}

		return parametro;
	}

	public void verificarFechaFirma() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {

			String mensaje = notificacionValidezFirma.notificacion(this.sucursal);

			if (!mensaje.equals("FIRMA_VALIDA")) {

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, null, mensaje));
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar fecha de validez de firma electrónica"));
			e.printStackTrace();
		}
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public PersUsua getPersUsua() {
		return persUsua;
	}

	public void setPersUsua(PersUsua persUsua) {
		this.persUsua = persUsua;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public List<Sucursal> getSucursals() {
		return sucursals;
	}

	public void setSucursals(List<Sucursal> sucursals) {
		this.sucursals = sucursals;
	}

}
