package ec.com.tecnointel.soem.caja.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ec.com.tecnointel.soem.caja.listaInt.CajaDocuEgreListaInt;
import ec.com.tecnointel.soem.caja.listaInt.CajaListaInt;
import ec.com.tecnointel.soem.caja.listaInt.CajaPeriListaInt;
import ec.com.tecnointel.soem.caja.modelo.Caja;
import ec.com.tecnointel.soem.caja.modelo.CajaDocuEgre;
import ec.com.tecnointel.soem.caja.modelo.CajaPeri;
import ec.com.tecnointel.soem.caja.modelo.Periferico;
import ec.com.tecnointel.soem.caja.registroInt.CajaRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.modelo.DocuEgre;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.listaInt.RolSucuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class CajaControl extends PaginaControl implements Serializable {

	private Caja caja;
//	private String formatoReporte;
	
	private List<Caja> cajas;
	private Set<Sucursal> sucursals;
	private Set<Sucursal> sucursalIds;
	private List<CajaPeri> cajaPeris;
	private List<CajaDocuEgre> cajaDocuEgres;
//	private List<Parametro> formatoReportes;
	
	@Inject
	CajaRegisInt cajaRegis;

	@Inject
	CajaListaInt cajaLista;
		
	@Inject
	CajaPeriListaInt cajaPeriLista;
	
	@Inject
	CajaDocuEgreListaInt cajaDocuEgreLista;
	
	@Inject
	RolSucuListaInt rolSucuLista;
	
//	@Inject
//	GestorCorreoInt gestorCorreo;
//	
//	@Inject
//	VariablesConfiguracion variablesConfiguracion;
		
	private static final long serialVersionUID = -8076894982157340084L;

	@PostConstruct
	public void cargar() {

		caja = new Caja();
		caja.setEstado(true);
		
		this.sucursals = new HashSet<>();
		
		this.buscarRolSucus();
		
//		Asigna la lista de sucursales que el rol tiene acceso al parametro de busqueda
		this.sucursalIds = sucursals;
		
	}

	public void buscar() {
		
		try {
			cajaLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.cajas = cajaLista.buscar(caja, this.pagina, sucursalIds);
			this.numeroReg = cajas.size();
			this.contadorReg = cajaLista.contarRegistros(caja, sucursalIds);
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
		
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}
		
		this.buscarRolSucus();

		if (this.id == null) {
			this.caja = new Caja();
			this.caja.setEstado(true);
		} else {

			try {
				this.caja = cajaRegis.buscarPorId(Caja.class, this.getId());
				this.buscarCajaPeris(caja);
				this.buscarCajaDocuEgre(caja);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
				e.printStackTrace();
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
				Object id = cajaRegis.insertar(caja);
				this.id = (Integer) id;
			} else {
				cajaRegis.modificar(caja);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "explora?faces-redirect=true&cajaId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&cajaId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&cajaId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			Caja caja = cajaRegis.buscarPorId(Caja.class, this.getId());
			cajaRegis.eliminar(caja);

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

	public List<Caja> buscarTodo() {

		List<Caja> cajas = new ArrayList<>();

		try {
			cajas = cajaLista.buscarTodo("descri");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return cajas;
	}
		
	public void buscarCajaPeris(Caja caja){

		CajaPeri cajaPeri = new CajaPeri();
		cajaPeri.setCaja(caja);
		cajaPeri.setPeriferico(new Periferico());
				
		try {
			this.cajaPeris = cajaPeriLista.buscar(cajaPeri, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Caja - Periférico"));
			e.printStackTrace();
		}
	}
	
	public void buscarCajaDocuEgre(Caja caja){

		CajaDocuEgre cajaDocuEgre = new CajaDocuEgre();
		cajaDocuEgre.setCaja(caja);
		cajaDocuEgre.setDocuEgre(new DocuEgre());
				
		try {
			this.cajaDocuEgres = cajaDocuEgreLista.buscar(cajaDocuEgre, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Caja - Documento Egreso"));
			e.printStackTrace();
		}
	}
	
	public void buscarRolSucus() {
		
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
			this.sucursals.add(rolSucu.getSucursal());
		}

	}	

	public void descargar(){
		
//		Nombre que esta en archivo fuente en jasperReport
		String nombreReporte = "Cajas";
		
		Map<String, Object> parametrosJasper = new HashMap<String, Object>();
				
		Parametro parametro = new Parametro();

//		Se coloca aqui la ruta para poder tener los reportes en diferentes y varias carpetas
//		Sino se podria poner en la clase GenerarJasperReport
		String rutaJrxml; // = "C:\\tecnoIntel\\soem\\jasperReportes\\";
//		En esta ruta se compila el reporte dentro del servidor de aplicaciones dentro del archivo .EAR .WEB
		String rutaReporteCompilado = "\\jasperReportes\\caja\\";

		try {

			String codigo = this.caja.getCodigo();
			String descri = this.caja.getDescri();
			
			if (caja.getCodigo() == null) {
				parametrosJasper.put("codigo","%");
			} else {
				parametrosJasper.put("codigo",codigo);
			}
			
			if (caja.getDescri() == null) {
				parametrosJasper.put("descri","%");
			} else {
				parametrosJasper.put("descri",descri);
			}
			
			parametro = parametroRegis.buscarPorId(Parametro.class, 1000);
			rutaJrxml = parametro.getDescri();
				
			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper, rutaJrxml, rutaReporteCompilado , formatoReporte);
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public Caja getCaja() {
		return caja;
	}

	public void setCaja(Caja caja) {
		this.caja = caja;
	}

	public List<Caja> getCajas() {
		return cajas;
	}

	public void setCajas(List<Caja> cajas) {
		this.cajas = cajas;
	}

	public List<CajaPeri> getCajaPeris() {
		return cajaPeris;
	}

	public void setCajaPeris(List<CajaPeri> cajaPeris) {
		this.cajaPeris = cajaPeris;
	}

	public List<CajaDocuEgre> getCajaDocuEgres() {
		return cajaDocuEgres;
	}

	public void setCajaDocuEgres(List<CajaDocuEgre> cajaDocuEgres) {
		this.cajaDocuEgres = cajaDocuEgres;
	}

    public Set<Sucursal> getSucursals() {
		return sucursals;
	}

	public void setSucursals(Set<Sucursal> sucursals) {
		this.sucursals = sucursals;
	}

	public Set<Sucursal> getSucursalIds() {
		return sucursalIds;
	}

	public void setSucursalIds(Set<Sucursal> sucursalIds) {
		this.sucursalIds = sucursalIds;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		      
//	public void enviarCorreoPrueba() {
//
//		// if (destinatarios != null){
//		// destintariosFinal.append(destinatarios);
//		//// TODO; ojo cuando no hay uno de los dos no va la coma
//		// destintariosFinal.append(",");
//		// }
//
//		// Parametro 100250 configurar si se desea enviar los documentos electronicos a
//		// otras direcciones
//		// if (variablesSesion.getDireccionesAdicionales() != null){
//		// destintariosFinal.append(variablesSesion.getDireccionesAdicionales());
//		// }
//
//		try {
//
//			String[] parametrosSmtp = variablesConfiguracion.getParametrosSmtp().toString().split(",");
//			String[] usuarioCorreo = variablesConfiguracion.getUsuarioCorreo().toString().split(",");
//			String[] cuerpoCorreo = { 
//					variablesConfiguracion.getSubject(),
//					variablesConfiguracion.getLinea1(),
//					variablesConfiguracion.getLinea2(),
//					variablesConfiguracion.getLinea3(),
//					variablesConfiguracion.getRutaImagenCabecera(),
//					variablesConfiguracion.getRutaImagenPie()
//					};
//			String rutaAutorizados = variablesConfiguracion.getRutaAutorizados();
//			String nombreArchivo = "1601201901171312262800110010010000000920325071819";
//			String destinatarios = "sandrolandovp@gmail.com";
//
//			gestorCorreo.enviarCorreo(parametrosSmtp, usuarioCorreo, cuerpoCorreo, rutaAutorizados, nombreArchivo,
//					destinatarios);
//
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (AddressException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (MessagingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
    
//	public void buscarParametrosCorreo() {
//
//		Parametro parametro = new Parametro();
//		
//		StringBuilder parametrosSmtp = new StringBuilder();
//		StringBuilder usuarioCorreo = new StringBuilder();
//
//		try {
//
////			SMTP
////			Host
//			parametro = parametroRegis.buscarPorId(100100);
//			parametrosSmtp.append(parametro.getDescri());
//			parametrosSmtp.append(",");
//			
////			Puerto
//			parametro = parametroRegis.buscarPorId(100101);
//			parametrosSmtp.append(parametro.getDescri());
//			parametrosSmtp.append(",");
//			
////			StartTls
//			parametro = parametroRegis.buscarPorId(100102);
//			parametrosSmtp.append(parametro.getDescri());
//			parametrosSmtp.append(",");
//			
////			Auth
//			parametro = parametroRegis.buscarPorId(100103);
//			parametrosSmtp.append(parametro.getDescri());
//			parametrosSmtp.append(",");
//			variablesSesion.setParametrosSmtp(parametrosSmtp);			
//			
////			Usuario Correo
////			Usuario
//			parametro = parametroRegis.buscarPorId(100150);
//			usuarioCorreo.append(parametro.getDescri());
//			usuarioCorreo.append(",");
//
////			Direccion correo
//			parametro = parametroRegis.buscarPorId(100151);
//			usuarioCorreo.append(parametro.getDescri());
//			usuarioCorreo.append(",");
//			
////			Alias
//			parametro = parametroRegis.buscarPorId(100152);
//			usuarioCorreo.append(parametro.getDescri());
//			usuarioCorreo.append(",");
//			
////			Clave
//			parametro = parametroRegis.buscarPorId(100153);
//			usuarioCorreo.append(parametro.getDescri());
//			usuarioCorreo.append(",");
//			variablesSesion.setUsuarioCorreo(usuarioCorreo);
//
////			Imagenes
//			parametro = parametroRegis.buscarPorId(100200);
//			variablesSesion.setRutaImagenCabecera(parametro.getDescri());
////			this.imagenCabecera = parametro.getDescri();
//						
//			parametro = parametroRegis.buscarPorId(100201);
//			variablesSesion.setRutaImagenPie(parametro.getDescri());
////			this.imagenPie = parametro.getDescri();
//
////			Certificados
//			parametro = parametroRegis.buscarPorId(100202);
//			variablesSesion.setRutaCertificado(parametro.getDescri());
////			this.certificados = parametro.getDescri();
//			
////			Direcciones adicionales
//			parametro = parametroRegis.buscarPorId(100250);
//			variablesSesion.setDireccionesAdicionales(parametro.getDescri());
////			this.direccionesAdicionales = parametro.getDescri();
//							
//		} catch (Exception e) {
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
//					"Excepcion - Error al buscar configuración correo"));
//			e.printStackTrace();
//		}
//	}

}
