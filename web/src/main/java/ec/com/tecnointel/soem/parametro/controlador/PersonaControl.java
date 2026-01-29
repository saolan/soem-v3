package ec.com.tecnointel.soem.parametro.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ec.com.tecnointel.soem.caja.listaInt.PersCajeListaInt;
import ec.com.tecnointel.soem.caja.modelo.PersCaje;
import ec.com.tecnointel.soem.egreso.listaInt.PersClieListaInt;
import ec.com.tecnointel.soem.egreso.listaInt.PersCobrListaInt;
import ec.com.tecnointel.soem.egreso.listaInt.PersVendListaInt;
import ec.com.tecnointel.soem.egreso.modelo.PersClie;
import ec.com.tecnointel.soem.egreso.modelo.PersCobr;
import ec.com.tecnointel.soem.egreso.modelo.PersVend;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.ingreso.listaInt.PersProvListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.PersProv;
import ec.com.tecnointel.soem.parametro.listaInt.PersonaListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.registroInt.PersonaRegisInt;
import ec.com.tecnointel.soem.seguridad.listaInt.PersUsuaListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class PersonaControl extends PaginaControl implements Serializable {

	private Persona persona;

	private List<Persona> personas;
	private List<PersUsua> persUsuas;
	private List<PersCaje> persCajes;
	private List<PersClie> persClies;
	private List<PersCobr> persCobrs;
	private List<PersVend> persVends;
	private List<PersProv> persProvs;
	

	@Inject
	PersonaRegisInt personaRegis;

	@Inject
	PersonaListaInt personaLista;
	
	@Inject
	PersClieListaInt persClieLista;

	@Inject
	PersUsuaListaInt persUsuaLista;

	@Inject
	PersCajeListaInt persCajeLista;

	@Inject
	PersCobrListaInt persCobrLista;

	@Inject
	PersProvListaInt persProvLista;

	@Inject
	PersVendListaInt persVendLista;


	private static final long serialVersionUID = -5396475069025018871L;

	@PostConstruct
	public void cargar() {

		persona = new Persona();
		
		this.rolPermiso = variablesSesion.getRolPermiso();

	}

	public void buscar() {

		try {
			
			personaLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.personas = personaLista.buscar(persona, this.pagina);
			this.numeroReg = personas.size();
			this.contadorReg = personaLista.contarRegistros(persona);
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

		if (this.id == null) {
			this.persona = new Persona();
		} else {

			try {
				this.persona = personaRegis.buscarPorId(Persona.class, this.getId());
				this.buscarPersClies(this.persona);
				this.buscarPersProvs(this.persona);
				this.buscarPersUsuas(this.persona);
				this.buscarPersCajes(this.persona);
				this.buscarPersVends(this.persona);
				this.buscarPersCobrs(this.persona);
				
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

		String campoTexto;
		
		campoTexto = this.getPersona().getCedulaRuc().trim();
		this.getPersona().setCedulaRuc(campoTexto);

		campoTexto = this.getPersona().getApelli().trim();
		this.getPersona().setApelli(campoTexto);

		campoTexto = this.getPersona().getNombre().trim();
		this.getPersona().setNombre(campoTexto);
		
		try {
			if (this.id == null) {
				Object id = personaRegis.insertar(persona);
				this.id = (Integer) id;
			} else {
				personaRegis.modificar(persona);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "explora?faces-redirect=true&personaId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&personaId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&personaId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			Persona persona = personaRegis.buscarPorId(Persona.class, this.getId());
			personaRegis.eliminar(persona);

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

	public List<Persona> buscarTodo() {

		List<Persona> personas = new ArrayList<>();

		try {
			personas = personaLista.buscarTodo("apelli");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return personas;
	}
	
	public void buscarPersClies(Persona persona){
		
		PersClie persClie = new PersClie();
		persClie.setPersona(persona);
		persClie.setEstado(true);
		
		try {
			this.persClies =  persClieLista.buscar(persClie, null, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar clientes"));
			e.printStackTrace();
		}
	}

	public void buscarPersVends(Persona persona){
		
		PersVend persVend = new PersVend();
		persVend.setPersona(persona);
		persVend.setEstado(true);
		
		try {
			this.persVends =  persVendLista.buscar(persVend, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar vendedores"));
			e.printStackTrace();
		}
	}

	public void buscarPersProvs(Persona persona){
		
		PersProv persProv = new PersProv();
		persProv.setPersona(persona);
		persProv.setEstado(true);
		
		try {
			this.persProvs =  persProvLista.buscar(persProv, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar proveedores"));
			e.printStackTrace();
		}
	}

	public void buscarPersUsuas(Persona persona){
		
		PersUsua persUsua = new PersUsua();
		persUsua.setPersona(persona);
		persUsua.setEstado(true);
		
		try {
			this.persUsuas =  persUsuaLista.buscar(persUsua, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar usuarios"));
			e.printStackTrace();
		}
	}
	
	public void buscarPersCajes(Persona persona){
		
		PersCaje persCaje = new PersCaje();
		persCaje.setPersona(persona);
		persCaje.setEstado(true);
		
		try {
			this.persCajes =  persCajeLista.buscar(persCaje, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar cajeros"));
			e.printStackTrace();
		}
	}

	public void buscarPersCobrs(Persona persona){
		
		PersCobr persCobr = new PersCobr();
		persCobr.setPersona(persona);
		persCobr.setEstado(true);
		
		try {
			this.persCobrs =  persCobrLista.buscar(persCobr, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar cobradores"));
			e.printStackTrace();
		}
	}
	

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< REPORTES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< REPORTES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< REPORTES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	
	String apelliInicial;
	String apelliFinal;
	String orden[];
	LinkedList<String> orden1 = new LinkedList<>();
	
	public void descargar(){
				
		// Nombre que esta en archivo fuente en jasperReport
		String nombreReporte = "personas";
		
		Map<String, Object> parametrosJasper = new HashMap<String, Object>();
				
		Parametro parametro = new Parametro();

//		Se coloca aqui la ruta para poder tener los reportes en diferentes y varias carpetas
//		Sino se podria poner en la clase GenerarJasperReport
		String rutaJrxml; // = "C:\\tecnoIntel\\soem\\jasperReportes\\ingreso\\";
//		En esta ruta se compila el reporte dentro del servidor de aplicaciones dentro del archivo .EAR .WEB
		String rutaReporteCompilado = "\\jasperReportes\\parametro\\";

		try {
			
			parametrosJasper.put("apelliDesde",apelliInicial.toLowerCase());
			parametrosJasper.put("apelliHasta",apelliFinal.toLowerCase());
			parametrosJasper.put("orden",orden);
		
			parametro = parametroRegis.buscarPorId(Parametro.class, 6000);
			rutaJrxml = parametro.getDescri();
				
			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper, rutaJrxml, rutaReporteCompilado , formatoReporte);
				
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al descargar listado personas"));
			e.printStackTrace();
		}
	}
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public List<Persona> getPersonas() {
		return personas;
	}

	public void setPersonas(List<Persona> personas) {
		this.personas = personas;
	}

	public List<PersUsua> getPersUsuas() {
		return persUsuas;
	}

	public void setPersUsuas(List<PersUsua> persUsuas) {
		this.persUsuas = persUsuas;
	}

	public List<PersCaje> getPersCajes() {
		return persCajes;
	}

	public void setPersCajes(List<PersCaje> persCajes) {
		this.persCajes = persCajes;
	}

	public List<PersClie> getPersClies() {
		return persClies;
	}

	public void setPersClies(List<PersClie> persClies) {
		this.persClies = persClies;
	}

	public List<PersCobr> getPersCobrs() {
		return persCobrs;
	}

	public void setPersCobrs(List<PersCobr> persCobrs) {
		this.persCobrs = persCobrs;
	}

	public List<PersVend> getPersVends() {
		return persVends;
	}

	public void setPersVends(List<PersVend> persVends) {
		this.persVends = persVends;
	}

	public List<PersProv> getPersProvs() {
		return persProvs;
	}

	public void setPersProvs(List<PersProv> persProvs) {
		this.persProvs = persProvs;
	}

	public String getApelliInicial() {
		return apelliInicial;
	}

	public void setApelliInicial(String apelliInicial) {
		this.apelliInicial = apelliInicial;
	}

	public String getApelliFinal() {
		return apelliFinal;
	}

	public void setApelliFinal(String apelliFinal) {
		this.apelliFinal = apelliFinal;
	}

	public String[] getOrden() {
		return orden;
	}

	public void setOrden(String[] orden) {
		this.orden = orden;
	}

	public LinkedList<String> getOrden1() {
		return orden1;
	}

	public void setOrden1(LinkedList<String> orden1) {
		this.orden1 = orden1;
	}


	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// Estas 3 Lineas llaman al metodo cuando se abra la pagina es similar a
	// @PostConstructor
	// <f:metadata>
	// <f:event type="preRenderView"
	// listener='#{categoriaControlador.buscarTodo}' />
	// </f:metadata>

	// public void buscarTodo() {
	// try {
	// this.categorias =
	// categoriaConsultaInterface.categoriaConsultar(this.getCategoria());
	// } catch (Exception e) {
	// // TODOs Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

}