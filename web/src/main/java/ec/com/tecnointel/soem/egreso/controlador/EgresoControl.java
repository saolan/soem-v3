package ec.com.tecnointel.soem.egreso.controlador;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
import javax.sql.DataSource;

import ec.com.tecnointel.soem.caja.listaInt.CajaDocuEgreListaInt;
import ec.com.tecnointel.soem.caja.listaInt.CajaMoviListaInt;
import ec.com.tecnointel.soem.caja.listaInt.SaliArchListaInt;
import ec.com.tecnointel.soem.caja.modelo.Caja;
import ec.com.tecnointel.soem.caja.modelo.CajaDocuEgre;
import ec.com.tecnointel.soem.caja.modelo.CajaMovi;
import ec.com.tecnointel.soem.caja.modelo.SaliArch;
import ec.com.tecnointel.soem.contabilidad.modelo.Transaccion;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionRegisInt;
import ec.com.tecnointel.soem.documeElec.tareas.ManejadorTareaEnviarCorreoDocu;
import ec.com.tecnointel.soem.egreso.listaInt.EgreTranListaInt;
import ec.com.tecnointel.soem.egreso.listaInt.EgresoListaInt;
import ec.com.tecnointel.soem.egreso.modelo.EgreDeta;
import ec.com.tecnointel.soem.egreso.modelo.EgreTran;
import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.egreso.modelo.PersClie;
import ec.com.tecnointel.soem.egreso.registroInt.EgresoRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.DocuCaja;
import ec.com.tecnointel.soem.parametro.modelo.DocuEgre;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.registroInt.DimmRegisInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolSucuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolDocu;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import ec.com.tecnointel.soem.seguridad.registroInt.RolDocuRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
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

@Named
@ViewScoped
public class EgresoControl extends PaginaControl implements Serializable {

	private Integer idDescargar;

	private String orden;

	private Boolean sesionVentas;

	private List<String> ordens = new ArrayList<>();

	private List<EgreTran> egreTrans;

	public List<EgreTran> getEgreTrans() {
		return egreTrans;
	}

	public void setEgreTrans(List<EgreTran> egreTrans) {
		this.egreTrans = egreTrans;
	}

	private Egreso egreso;
	private Egreso egresoSele;

	private List<Egreso> egresos;
	private List<Parametro> parametros;
	private List<SaliArch> saliArchs;

	private Dimm dimmIva;
	@Resource(mappedName = "java:jboss/datasources/soemDS")
	private DataSource dataSource;

	@Inject
	DimmRegisInt dimmRegis;

	@Inject
	CajaDocuEgreListaInt cajaDocuEgreLista;

	@Inject
	ManejadorTareaEnviarCorreoDocu manejadorTareaEnviarCorreoDocu;

	@Inject
	EgresoRegisInt egresoRegis;

	@Inject
	EgresoListaInt egresoLista;

	@Inject
	CajaMoviListaInt cajaMoviLista;

	@Inject
	RolSucuListaInt rolSucuLista;

	@Inject
	SaliArchListaInt saliArchLista;

	@Inject
	TransaccionRegisInt transaccionRegis;

	@Inject
	EgreTranListaInt egreTranLista;

	@Inject
	RolDocuRegisInt rolDocuRegis;

	private static final long serialVersionUID = -6044434366206204856L;

	@PostConstruct
	public void cargar() {

		this.ordens.add("Asc");
		this.ordens.add("Desc");

		this.orden = "Asc";

		PersClie persClie = new PersClie();
		persClie.setPersona(new Persona());

		DocuEgre docuEgre = new DocuEgre();
		docuEgre.setDocumento(new Documento());

		egreTrans = new ArrayList<>();

		egreso = new Egreso();

		egreso.setPersClie(persClie);
		egreso.setDocuEgre(docuEgre);
//		Date fecha = Util.cambiarFormatoFecha(new Date(), "dd-MM-yyyy");		
//		egreso.setFechaEmis(fecha);
		egreso.setFechaEmis(LocalDate.now());

		egreso.setEstado("GR");
//		Se inicia esta variable en cero para que no de confictos
//		al momento de modificar documentos, porque se quedaba en memoria
//		y al momento de volver a modificar o al crear una nueva factura
//		mandaba a imprimir
		try {
			this.dimmIva = dimmRegis.buscarPorId(Dimm.class, variablesSesion.getDimmIdIvaActual());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Ex - No se encuentra Dimm Iva"));
			e.printStackTrace();
		}

		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idDescargar", 0);
	}

	public void buscar() {

		Set<Integer> sucursals = new HashSet<>();

		CajaMovi cajaMoviSesion = new CajaMovi();

//		Busca sesion de ventas para poder activar boton nuevo
//		y para controlar acceso a documentos de otras sucursales y otras cajas
		cajaMoviSesion = this.buscarSesionVentas();

//		Si gestiona documentos es decir puede ver todas las facturas
//		de todas las sucursales y de todas las cajas
		if (variablesSesion.getRolPermiso().get(3110) == true) {

//			Trae un set de enteros de sucursales para buscar facturas por cada sucursal
			sucursals = this.buscarRolSucus();
			CajaMovi cajaMovi = new CajaMovi();
			cajaMovi.setCaja(new Caja());
			egreso.setCajaMovi(cajaMovi);

		} else {

//			Si no ha iniciado caja tampoco puede buscar documentos
			if (cajaMoviSesion.getCajaMoviId() == null) {

				sucursals.add(0);
				cajaMoviSesion.setCajaMoviId(0);
				egreso.setCajaMovi(cajaMoviSesion);

			} else {
//				Solo puede ver las facturas de su sucursal y de la caja abierta
				sucursals.add(cajaMoviSesion.getSucursal().getSucursalId());
				egreso.setCajaMovi(cajaMoviSesion);
			}
		}

		try {

			egresoLista.filasPagina(variablesSesion.getFilasPagina());
			egresoLista.orden(this.orden);

			this.egresos = egresoLista.buscar(egreso, this.pagina, sucursals);
			this.numeroReg = egresos.size();
			this.contadorReg = egresoLista.contarRegistros(egreso, sucursals);
			
			this.buscarEgresoEstado();

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
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

	public Transaccion buscarTransaccionPorId(Integer transaccionId) {

		Transaccion transaccion = new Transaccion();

		try {
			transaccion = transaccionRegis.buscarPorId(Transaccion.class, transaccionId);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar transacción por Id"));
			e.printStackTrace();
		}

		return transaccion;
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}

		if (this.id == null) {
			this.egreso = new Egreso();
		} else {

			try {
				this.egreso = egresoRegis.buscarPorId(Egreso.class, this.getId());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
				e.printStackTrace();
			}

//			Buscar Transaccion
			Transaccion transaccionBuscar = new Transaccion();

			EgreTran egreTranBuscar = new EgreTran(this.egreso, transaccionBuscar);
			this.egreTrans = this.buscarEgreTrans(egreTranBuscar);

//			Asignar transaccion con el tipo de documento
			for (EgreTran egreTran : this.egreTrans) {
				Transaccion transaccion = this.buscarTransaccionPorId(egreTran.getTransaccion().getTransaccionId());
				egreTran.setTransaccion(transaccion);
			}

		}
	}

	public String grabar() {

		// Los mensajes tienen RequestScope y se borran al navegar a otro pagina
		// Con este comando se guarda los mensages de confirmaci�n en navegacion
		// entre paginas
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			if (this.id == null) {
				Object id = egresoRegis.insertar(egreso);
				this.id = (Integer) id;
			} else {
				egresoRegis.modificar(egreso);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "lista?faces-redirect=true";
	}

	public String modificar() {
		return "registra?faces-redirect=true&egresoId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&egresoId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			Egreso egreso = egresoRegis.buscarPorId(Egreso.class, this.getId());
			egresoRegis.eliminar(egreso);

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

	public List<Egreso> buscarTodo() {

		List<Egreso> egresos = new ArrayList<>();

		try {
			egresos = egresoLista.buscarTodo("egresoId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return egresos;
	}

	public CajaMovi buscarSesionVentas() {

		CajaMovi cajaMoviSesion = new CajaMovi();
		cajaMoviSesion.setCaja(new Caja());

		CajaMovi cajaMovi = new CajaMovi();
		DocuCaja docuCaja = new DocuCaja();
		docuCaja.setDocumento(new Documento());

		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");

		cajaMovi.setSucursal(variablesSesion.getSucursal());
		cajaMovi.setCaja(new Caja());
		cajaMovi.setDocuCaja(docuCaja);
		cajaMovi.setEstado(false);
		cajaMovi.setPersCaje(persUsuaSesion.getPersona().getPersCaje());

		List<CajaMovi> cajaMovis = new ArrayList<>();

		try {

			cajaMovis = cajaMoviLista.buscar(cajaMovi, null);
			if (cajaMovis.size() == 0) {

				sesionVentas = false;
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, null, "No ha iniciado una sesión de ventas"));

			} else if (cajaMovis.size() > 1) {

				sesionVentas = false;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
						"Se ha registrado mas de una sesión de ventas"));

			} else {

				sesionVentas = true;

				for (CajaMovi cajaMovi2 : cajaMovis) {
					cajaMoviSesion = cajaMovi2;
				}

			}

		} catch (Exception e) {

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar movimientos de caja"));
			e.printStackTrace();

		}

		return cajaMoviSesion;

	}

	public void buscarEgresoEstado() {

		Parametro parametro = new Parametro();

		parametro.setCodigo("Parametro-Estado");
		parametro.setEstado(true);

		try {
			this.parametros = parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepción- Error al buscar estado de documentos"));
			e.printStackTrace();
		}
	}

	public Set<Integer> buscarRolSucus() {

		Set<Integer> sucursals = new HashSet<>();

		List<RolSucu> rolSucus = new ArrayList<>();

		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");

		try {

			rolSucus = rolSucuLista.buscar(persUsuaSesion.getRolPersUsuas());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar sucursales del rol"));
			e.printStackTrace();
		}

		for (RolSucu rolSucu : rolSucus) {
			if (rolSucu.getAcceso() == true) {
				sucursals.add(rolSucu.getSucursal().getSucursalId());
			}
		}

		return sucursals;
	}

	public void buscarSaliArchs() {

//		INICIO SELECCION DE IMPRESION
//		Buscar la session de ventas del usuario actual 
//		para poder imprimir la factura en la caja que abrio el usuario
//		La factura no va a imprimir en la caja que se hizo originalmente
//		Esto es en el caso que abra la factura un administrador
//		Para que pueda imprimir en la caja que abrio el administrador, 
//		si abre la factura un cajero obviamente va a imprimir en la caja que se hizo la factura
//		ya que en la busqueda se filtra por sucursal y por caja
//		NOTA: si siempre se quiere imprimir en la caja que se hizo la factura cambiar:
//		esta linea: cajaPeri.setCaja(cajaMovi.getCaja());
//		por esta: cajaPeri.setCaja(this.egreso.getCajaMovi().getCaja());
		CajaMovi cajaMovi = this.buscarSesionVentas();

//		FIN SELECCION DE IMPRESION			

//		Imprime en todas las impresoras seleccionadas en cada caja
		CajaDocuEgre cajaDocuEgre = new CajaDocuEgre();
		CajaDocuEgre cajaDocuEgreParam = new CajaDocuEgre();
		List<CajaDocuEgre> cajaDocuEgres = new ArrayList<>();

		cajaDocuEgreParam.setCaja(cajaMovi.getCaja());
		cajaDocuEgreParam.setDocuEgre(this.egresoSele.getDocuEgre());

		try {

			cajaDocuEgres = cajaDocuEgreLista.buscar(cajaDocuEgreParam, null);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"No se encuentra documentos de caja - cajaDocuEgre"));
			
			e.printStackTrace();
		}

		for (CajaDocuEgre cajaDocuEgre2 : cajaDocuEgres) {
			cajaDocuEgre = cajaDocuEgre2;
		}

		SaliArch saliArch = new SaliArch();
		saliArch.setCajaDocuEgre(cajaDocuEgre);

		saliArch.setCajaDocuEgre(cajaDocuEgre);

		try {
			this.saliArchs = saliArchLista.buscar(saliArch, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar salida de archivo"));
			e.printStackTrace();
		}

	}

//	Permite remprimir descargar y reenviar un documento procesado
	public void descargar() {

//		Solo tiene que re-imprimir descargar o enviar documentos procesados PR 
//		if (this.egresoSele.getEstado().equals("GR")) {
//			return;
//		}

		RolDocu rolDocu = new RolDocu();
		try {

			if (this.egresoSele.getEstado().equals("GR")) {

				rolDocu = rolDocuRegis.buscarRolDocuPermisos(variablesSesion.getRolDocus(),
						this.egresoSele.getDocuEgre().getDocumento(), variablesSesion.getPersUsua());

				if (!rolDocu.getGrabarImpr()) {
					return;
				}
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al descargar o imprimimr documento"));
			e.printStackTrace();
			return;
		}

		this.idDescargar = this.egresoSele.getEgresoId();

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		Parametro parametro = new Parametro();

//		Se coloca aqui la ruta para poder tener los reportes en diferentes y varias carpetas
//		Sino se podria poner en la clase GenerarJasperReport
		String rutaJrxml; // = "C:\\tecnoIntel\\soem\\jasperReportes\\ingreso\\";
//		En esta ruta se compila el reporte dentro del servidor de aplicaciones dentro del archivo .EAR .WEB
		String rutaReporteCompilado = "\\jasperReportes\\egreso\\";

		try {

			Integer egresoId = this.idDescargar;

			if (egresoId == null) {
				parametrosJasper.put("egresoId", 0);
			} else {
				parametrosJasper.put("egresoId", egresoId);
			}

			parametro = parametroRegis.buscarPorId(Parametro.class, 3000);
			rutaJrxml = parametro.getDescri();

			for (SaliArch saliArch2 : this.saliArchs) {
				if (saliArch2.getPredet()) {
					this.crearArchivo(parametrosJasper, rutaJrxml, rutaReporteCompilado, formatoReporte, saliArch2);
				}
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al descargar documento"));
			e.printStackTrace();
		}

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
//			dimm = dimmRegis.buscarPorId(13030);
//			parametrosJasper.put("egreDetaImpuDescri12", dimm.getDescri().trim());
//			dimm = dimmRegis.buscarPorId(13040);
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
				nombreReporte = nombreReporte + this.egresoSele.getNumero() + "." + formato;
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				JRPdfExporter exporterPdf = new JRPdfExporter();
				exporterPdf.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporterPdf.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();

				exporterPdf.setConfiguration(configuration);
				exporterPdf.exportReport();

				generaJasperReportes.abrirReporte(byteArrayOutputStream.toByteArray(), nombreReporte,
						"application/pdf");

			} else if (saliArch.getSalida().equals("CORREO")) {

				if (this.egresoSele.getPersClie().getPersona().getCorreo() != null
						&& this.egresoSele.getDocuEgre().getDocumento().isEnviaCorreo()) {
					this.ejecutarTareaEnviarCorreoDocu(this.egresoSele.getPersClie().getPersona().getCorreo(),
							this.egresoSele.getClaveAcce());
				}
			}

		} catch (JRException jre) {
			jre.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error en reporte"));
		} catch (SQLException sqle) {
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

	public void ejecutarTareaEnviarCorreoDocu(String destinatario, String claveAcce) {

		try {

			manejadorTareaEnviarCorreoDocu.ejecutarTareaEnviarCorreoDocu(destinatario, claveAcce);

		} catch (ExecutionException | InterruptedException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Documento no enviado por correo al cliente " + destinatario));
			e.printStackTrace();
		}
	}

	public boolean isActivarMesa() {
		return variablesSesion.isActivarMesa();
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

	public Boolean getSesionVentas() {
		return sesionVentas;
	}

	public void setSesionVentas(Boolean sesionVentas) {
		this.sesionVentas = sesionVentas;
	}

	public List<Parametro> getParametros() {
		return parametros;
	}

	public void setParametros(List<Parametro> parametros) {
		this.parametros = parametros;
	}

	public List<SaliArch> getSaliArchs() {
		return saliArchs;
	}

	public void setSaliArchs(List<SaliArch> saliArchs) {
		this.saliArchs = saliArchs;
	}

	public Egreso getEgresoSele() {
		return egresoSele;
	}

	public void setEgresoSele(Egreso egresoSele) {
		this.egresoSele = egresoSele;
	}

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

	public List<String> getOrdens() {
		return ordens;
	}

	public void setOrdens(List<String> ordens) {
		this.ordens = ordens;
	}	
}