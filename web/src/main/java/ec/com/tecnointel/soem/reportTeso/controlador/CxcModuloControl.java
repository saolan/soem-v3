package ec.com.tecnointel.soem.reportTeso.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.caja.listaInt.CajaListaInt;
import ec.com.tecnointel.soem.caja.modelo.Caja;
import ec.com.tecnointel.soem.egreso.listaInt.ClieGrupListaInt;
import ec.com.tecnointel.soem.egreso.modelo.ClieGrup;
import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.egreso.modelo.PersClie;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.DocuEgreListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.FormPagoListaInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuEgre;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.listaInt.RolSucuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import ec.com.tecnointel.soem.tesoreria.modelo.Cxc;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviEgre;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

public class CxcModuloControl extends PaginaControl implements Serializable {

	protected String rutaJrxml;
	protected String rutaReporteCompilado = "\\jasperReportes\\tesoreria\\";

	protected Double saldoMini = 0.01d;
	protected Double saldoMaxi = 50000d;

	protected Cxc cxcDesd;
	protected Cxc cxcHast;
	
	protected List<Caja> cajas;
	protected ArrayList<String> cajaIds;

	protected Set<Sucursal> sucursals;
	protected ArrayList<String> sucursalIds;

	protected List<DocuEgre> docuEgres;
	protected ArrayList<String> docuEgreIds;
	
	protected Egreso egresoDesd;
	protected Egreso egresoHast;

	protected List<ClieGrup> clieGrups;
	protected ArrayList<String> clieGrupIds;

	protected PersClie persClieDesd;
	protected PersClie persClieHast;
	
	protected FormPagoMoviEgre fpmeDesd;
	protected FormPagoMoviEgre fpmeHast;
	
	protected Boolean persClieEstado;
	protected Boolean cxcEstado;
	
	protected List<FormPago> formPagos;
	protected ArrayList<String> formPagoIds;

	protected List<Parametro> parametroEstados;
	protected ArrayList<String> estadoDescris;

	private static final long serialVersionUID = -5146061902582820093L;

	@Inject
	FormPagoListaInt formPagoLista;

	@Inject
	DocuEgreListaInt docuEgreLista;

	@Inject
	CajaListaInt cajaLista;

	@Inject
	RolSucuListaInt rolSucuLista;
	
	@Inject
	ClieGrupListaInt clieGrupLista;

	@PostConstruct
	public void cargar() {

		List<FormPago> formPagos = new ArrayList<>();
		List<DocuEgre> docuEgres = new ArrayList<>();
		
		this.cxcDesd = new Cxc();
		this.cxcHast = new Cxc();
		this.egresoDesd = new Egreso();
		this.egresoHast = new Egreso();
		this.persClieDesd = new PersClie();
		this.persClieHast = new PersClie();
		
		this.persClieDesd.setPersona(new Persona());
		this.persClieHast.setPersona(new Persona());
		
		this.fpmeDesd = new FormPagoMoviEgre();
		this.fpmeHast = new FormPagoMoviEgre();

		this.persClieEstado = true;
		this.cxcEstado = true;
		
		this.estadoDescris = new ArrayList<>();
		this.estadoDescris.add("PR");

		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap()
				.get("persUsua");

		this.sucursals = new HashSet<>();
		
		this.buscarRolSucus();
		this.buscarCajas();
		this.buscarEstados();
		
		formPagos = this.buscarFormPagos();
		this.filtrarFormPagos(formPagos, persUsuaSesion);
		
		docuEgres = this.buscarDocuEgres();
		this.filtrarDocuEgres(docuEgres, persUsuaSesion);
		
		this.clieGrups = this.buscarClieGrups();

	}

	
	public List<ClieGrup> buscarClieGrups() {
		
		List<ClieGrup> clieGrups = new ArrayList<ClieGrup>();
		ClieGrup clieGrup = new ClieGrup();
		
		clieGrup.setEstado(true);
		
		try {
			clieGrups = clieGrupLista.buscar(clieGrup, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error Grupos de Clientes"));
			e.printStackTrace();
		}
		
		return clieGrups;
	}


	public void buscarCajas(){

		Caja caja = new Caja();
		caja.setEstado(true);
				
		try {
			this.cajas = cajaLista.buscar(caja, null, this.sucursals);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Caja"));
			e.printStackTrace();
		}
	}

	public void buscarEstados() {
		
		Parametro parametro = new Parametro();
		
		parametro.setCodigo("Parametro-Estado");
		parametro.setEstado(true);
		
		try {
			this.parametroEstados = parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepción- Error al buscar estado de documentos"));
			e.printStackTrace();
		}
	}
	
	public List<DocuEgre> buscarDocuEgres() {

		DocuEgre docuEgre = new DocuEgre();
		
		docuEgre.setDocumento(new Documento());
		docuEgre.getDocumento().setEstado(true);

		List<DocuEgre> docuIngrs = new ArrayList<>();

		try {
			docuIngrs = docuEgreLista.buscar(docuEgre, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
							"Excepcion - Error al buscar tipo de documento"));
			e.printStackTrace();
		}
		
		return docuIngrs;

	}

	public void filtrarDocuEgres(List<DocuEgre> docuIngrs, PersUsua persUsuaSesion){
		try {
			
			this.docuEgres = docuEgreLista.filtrarDocuEgres(docuIngrs, persUsuaSesion, variablesSesion.getRolDocus());
						
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al filtrar documentos egreso"));
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
	
	public void descargar() throws Exception {

		this.verificarParametros();

		Parametro parametro = new Parametro();
		parametro = parametroRegis.buscarPorId(Parametro.class, 8000);
		rutaJrxml = parametro.getDescri();

	}

	public void verificarParametros() {
		if (persClieDesd.getPersona().getApelli() == null) {
			persClieDesd.getPersona().setApelli(" ");
		}
		
		if (persClieHast.getPersona().getApelli() == null) {
			persClieHast.getPersona().setApelli("zz");
		}
		
		if (persClieDesd.getPersona().getCedulaRuc() == null) {
			persClieDesd.getPersona().setCedulaRuc(" ");
		}
		
		if (persClieHast.getPersona().getCedulaRuc() == null) {
			persClieHast.getPersona().setCedulaRuc("zz");
		}
		
	}

	public List<FormPago> buscarFormPagos() {
		
		List<FormPago> formPagos = new ArrayList<>();

		FormPago formPago = new FormPago();
		Parametro parametro = new Parametro();
		
		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 6051);
		} catch (Exception e1) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar forma pago del modulo caja-ventas"));
			e1.printStackTrace();
		}
		
		formPago.setModulo(parametro.getDescri());
		formPago.setEstado(true);
		
		try {
			formPagos = formPagoLista.buscar(formPago, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar forma de pago"));
			e.printStackTrace();
		}
		
		return formPagos;

	}
	
	public void filtrarFormPagos(List<FormPago> formaPagos, PersUsua persUsuaSesion){
		try {
			this.formPagos = formPagoLista.filtrarFormPagos(formaPagos, persUsuaSesion, variablesSesion.getRolFormPagos());
		} catch (Exception e1) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error filtra formas de pago"));
			e1.printStackTrace();
		}

	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	
	public Cxc getCxcDesd() {
		return cxcDesd;
	}

	public void setCxcDesd(Cxc cxcDesd) {
		this.cxcDesd = cxcDesd;
	}

	public Cxc getCxcHast() {
		return cxcHast;
	}

	public void setCxcHast(Cxc cxcHast) {
		this.cxcHast = cxcHast;
	}

	public Egreso getEgresoDesd() {
		return egresoDesd;
	}

	public void setEgresoDesd(Egreso egresoDesd) {
		this.egresoDesd = egresoDesd;
	}

	public Egreso getEgresoHast() {
		return egresoHast;
	}

	public void setEgresoHast(Egreso egresoHast) {
		this.egresoHast = egresoHast;
	}

	public PersClie getPersClieDesd() {
		return persClieDesd;
	}

	public void setPersClieDesd(PersClie persClieDesd) {
		this.persClieDesd = persClieDesd;
	}

	public PersClie getPersClieHast() {
		return persClieHast;
	}

	public void setPersClieHast(PersClie persClieHast) {
		this.persClieHast = persClieHast;
	}

	public FormPagoMoviEgre getFpmeDesd() {
		return fpmeDesd;
	}

	public void setFpmeDesd(FormPagoMoviEgre fpmeDesd) {
		this.fpmeDesd = fpmeDesd;
	}

	public FormPagoMoviEgre getFpmeHast() {
		return fpmeHast;
	}

	public void setFpmeHast(FormPagoMoviEgre fpmeHast) {
		this.fpmeHast = fpmeHast;
	}

	public Boolean getPersClieEstado() {
		return persClieEstado;
	}

	public void setPersClieEstado(Boolean persClieEstado) {
		this.persClieEstado = persClieEstado;
	}

	public Boolean getCxcEstado() {
		return cxcEstado;
	}

	public void setCxcEstado(Boolean cxcEstado) {
		this.cxcEstado = cxcEstado;
	}

	public List<FormPago> getFormPagos() {
		return formPagos;
	}

	public void setFormPagos(List<FormPago> formPagos) {
		this.formPagos = formPagos;
	}

	public ArrayList<String> getFormPagoIds() {
		return formPagoIds;
	}

	public void setFormPagoIds(ArrayList<String> formPagoIds) {
		this.formPagoIds = formPagoIds;
	}

	public List<Caja> getCajas() {
		return cajas;
	}

	public void setCajas(List<Caja> cajas) {
		this.cajas = cajas;
	}

	public ArrayList<String> getCajaIds() {
		return cajaIds;
	}

	public void setCajaIds(ArrayList<String> cajaIds) {
		this.cajaIds = cajaIds;
	}

	public Double getSaldoMini() {
		return saldoMini;
	}

	public void setSaldoMini(Double saldoMini) {
		this.saldoMini = saldoMini;
	}


	public Double getSaldoMaxi() {
		return saldoMaxi;
	}

	public void setSaldoMaxi(Double saldoMaxi) {
		this.saldoMaxi = saldoMaxi;
	}


	public List<Parametro> getParametroEstados() {
		return parametroEstados;
	}


	public void setParametroEstados(List<Parametro> parametroEstados) {
		this.parametroEstados = parametroEstados;
	}


	public ArrayList<String> getEstadoDescris() {
		return estadoDescris;
	}


	public void setEstadoDescris(ArrayList<String> estadoDescris) {
		this.estadoDescris = estadoDescris;
	}


	public List<DocuEgre> getDocuEgres() {
		return docuEgres;
	}


	public void setDocuEgres(List<DocuEgre> docuEgres) {
		this.docuEgres = docuEgres;
	}


	public ArrayList<String> getDocuEgreIds() {
		return docuEgreIds;
	}


	public void setDocuEgreIds(ArrayList<String> docuEgreIds) {
		this.docuEgreIds = docuEgreIds;
	}


	public List<ClieGrup> getClieGrups() {
		return clieGrups;
	}


	public void setClieGrups(List<ClieGrup> clieGrups) {
		this.clieGrups = clieGrups;
	}


	public ArrayList<String> getClieGrupIds() {
		return clieGrupIds;
	}


	public void setClieGrupIds(ArrayList<String> clieGrupIds) {
		this.clieGrupIds = clieGrupIds;
	}
}

