package ec.com.tecnointel.soem.reportTeso.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.modelo.PersProv;
import ec.com.tecnointel.soem.parametro.listaInt.FormPagoListaInt;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.tesoreria.modelo.Cxp;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

public class CxpModuloControl extends PaginaControl implements Serializable {

	protected String rutaJrxml;
	protected String rutaReporteCompilado = "\\jasperReportes\\tesoreria\\";
	
	protected Double saldoMini = 0.01d;
	protected Double saldoMaxi = 50000d;
	
	protected Cxp cxpDesd;
	protected Cxp cxpHast;

	protected Ingreso ingresoDesd;
	protected Ingreso ingresoHast;

	protected PersProv persProvDesd;
	protected PersProv persProvHast;
	
	protected FormPagoMoviIngr fpmiDesd;
	protected FormPagoMoviIngr fpmiHast;
	
	protected Boolean persProvEstado;
	protected Boolean cxpEstado;

	protected List<FormPago> formPagos;
	protected ArrayList<String> formPagoIds;
	
	protected List<Parametro> parametroEstados;
	protected ArrayList<String> estadoDescris;

	private static final long serialVersionUID = 620471514494133705L;

	@Inject
	FormPagoListaInt formPagoLista;
	
	@PostConstruct
	public void cargar() {
		
		List<FormPago> formPagos = new ArrayList<>();
		
		this.cxpDesd = new Cxp();
		this.cxpHast = new Cxp();
		this.ingresoDesd = new Ingreso();
		this.ingresoHast = new Ingreso();
		
		this.persProvDesd = new PersProv();
		this.persProvHast = new PersProv();
		
		this.persProvDesd.setPersona(new Persona());
		this.persProvHast.setPersona(new Persona());
		
		this.fpmiDesd = new FormPagoMoviIngr();
		this.fpmiHast = new FormPagoMoviIngr();

		this.persProvEstado = true;
		this.cxpEstado = false;
		
		this.estadoDescris = new ArrayList<>();
		this.estadoDescris.add("PR");

		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap()
				.get("persUsua");

		this.buscarEstados();
		formPagos = this.buscarFormPagos();
		
		this.filtrarFormPagos(formPagos, persUsuaSesion);
	}

	public void descargar() throws Exception {

		this.verificarParametros();

		Parametro parametro = new Parametro();
		parametro = parametroRegis.buscarPorId(Parametro.class, 8000);
		rutaJrxml = parametro.getDescri();

	}

	public void verificarParametros() {

		if (persProvDesd.getPersona().getApelli() == null) {
			persProvDesd.getPersona().setApelli(" ");
		}
		
		if (persProvHast.getPersona().getApelli() == null) {
			persProvHast.getPersona().setApelli("zz");
		}
		
		if (persProvDesd.getPersona().getCedulaRuc() == null) {
			persProvDesd.getPersona().setCedulaRuc(" ");
		}
		
		if (persProvHast.getPersona().getCedulaRuc() == null) {
			persProvHast.getPersona().setCedulaRuc("zz");
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


	public List<FormPago> buscarFormPagos() {
		
		List<FormPago> formPagos = new ArrayList<>();

		FormPago formPago = new FormPago();
		Parametro parametro = new Parametro();
		
		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 6050);
		} catch (Exception e1) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar módulo caja"));
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

	public Cxp getCxpDesd() {
		return cxpDesd;
	}

	public void setCxpDesd(Cxp cxpDesd) {
		this.cxpDesd = cxpDesd;
	}

	public Cxp getCxpHast() {
		return cxpHast;
	}

	public void setCxpHast(Cxp cxpHast) {
		this.cxpHast = cxpHast;
	}

	public Ingreso getIngresoDesd() {
		return ingresoDesd;
	}

	public void setIngresoDesd(Ingreso ingresoDesd) {
		this.ingresoDesd = ingresoDesd;
	}

	public Ingreso getIngresoHast() {
		return ingresoHast;
	}

	public void setIngresoHast(Ingreso ingresoHast) {
		this.ingresoHast = ingresoHast;
	}

	public PersProv getPersProvDesd() {
		return persProvDesd;
	}

	public void setPersProvDesd(PersProv persProvDesd) {
		this.persProvDesd = persProvDesd;
	}

	public PersProv getPersProvHast() {
		return persProvHast;
	}

	public void setPersProvHast(PersProv persProvHast) {
		this.persProvHast = persProvHast;
	}

	public FormPagoMoviIngr getFpmiDesd() {
		return fpmiDesd;
	}

	public void setFpmiDesd(FormPagoMoviIngr fpmiDesd) {
		this.fpmiDesd = fpmiDesd;
	}

	public FormPagoMoviIngr getFpmiHast() {
		return fpmiHast;
	}

	public void setFpmiHast(FormPagoMoviIngr fpmiHast) {
		this.fpmiHast = fpmiHast;
	}

	public Boolean getPersProvEstado() {
		return persProvEstado;
	}

	public void setPersProvEstado(Boolean persProvEstado) {
		this.persProvEstado = persProvEstado;
	}

	public Boolean getCxpEstado() {
		return cxpEstado;
	}

	public void setCxpEstado(Boolean cxpEstado) {
		this.cxpEstado = cxpEstado;
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

}
