package ec.com.tecnointel.soem.reportCaja.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.caja.listaInt.CajaListaInt;
import ec.com.tecnointel.soem.caja.listaInt.PersCajeListaInt;
import ec.com.tecnointel.soem.caja.modelo.Caja;
import ec.com.tecnointel.soem.caja.modelo.CajaMovi;
import ec.com.tecnointel.soem.caja.modelo.PersCaje;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.DocuCajaListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.FormPagoListaInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuCaja;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.listaInt.RolSucuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

public class CajaModuloControl extends PaginaControl implements Serializable {

	protected String rutaJrxml;
	protected String rutaReporteCompilado = "\\jasperReportes\\caja\\";

	protected CajaMovi cajaMoviDesde;
	protected CajaMovi cajaMoviHasta;
	
	protected List<DocuCaja> docuCajas;
	protected ArrayList<String> docuCajaIds;
	
	protected List<Caja> cajas;
	protected ArrayList<String> cajaIds;

	protected List<PersCaje> persCajes;
	protected ArrayList<String> persCajeIds;
	
	protected List<FormPago> formPagos;
	protected ArrayList<String> formPagoIds;

	protected Boolean cajaEstado;
	
	protected Set<Sucursal> sucursals;
	
	@Inject
	DocuCajaListaInt docuCajaLista; 
	
	@Inject
	CajaListaInt cajaLista;
	
	@Inject
	PersCajeListaInt persCajeLista;
	
	@Inject
	FormPagoListaInt formPagoLista;

	@Inject
	RolSucuListaInt rolSucuLista;
	
	private static final long serialVersionUID = -8368435182674123434L;

	@PostConstruct
	public void cargar() {

		List<DocuCaja> docuCajas = new ArrayList<>();
		List<FormPago> formPagos = new ArrayList<>();
				
		this.cajaMoviDesde = new CajaMovi();
		this.cajaMoviHasta = new CajaMovi();
		this.cajaEstado = true;
		
		sucursals = new HashSet<>();
		
		this.buscarRolSucus();
		
		this.buscarCajas();
		this.buscarPersCajes();
		
		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap()
				.get("persUsua");
		
		formPagos = this.buscarFormPagos();
		this.filtrarFormPagos(formPagos, persUsuaSesion);
		
		docuCajas = this.buscarDocuCajas();
		this.filtrarDocuCajas(docuCajas, persUsuaSesion);

	}

	public void descargar() throws Exception {

		this.verificarParametros();

		Parametro parametro = new Parametro();
		parametro = parametroRegis.buscarPorId(Parametro.class, 1000);
		rutaJrxml = parametro.getDescri();

	}

	public void verificarParametros() {

		// if (ingresoDesde.getCodigoBarra() == null) {
		// ingresoDesde.setCodigoBarra(" ");
		// }
		//
		// if (ingresoHasta.getCodigoBarra() == null) {
		// ingresoHasta.setCodigoBarra("zz");
		// }
	}

	// public void buscarDocuIngrs() {
	//
	// DocuIngr docuIngr = new DocuIngr();
	// docuIngr.setDocumento(new Documento());
	// docuIngr.getDocumento().setEstado(true);
	//
	// try {
	// this.docuIngrs = docuIngrLista.buscar(docuIngr, null);
	// } catch (Exception e) {
	// FacesContext.getCurrentInstance().addMessage(
	// null,
	// new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
	// "Excepcion - Error al buscar tipo de documento"));
	//
	// e.printStackTrace();
	// }
	//
	// }

	public List<DocuCaja> buscarDocuCajas(){

		List<DocuCaja> docuCajas = new ArrayList<>();
		
		DocuCaja docuCaja = new DocuCaja();
		docuCaja.setDocumento(new Documento());
		
		docuCaja.getDocumento().setEstado(true);

		try {
			docuCajas = docuCajaLista.buscar(docuCaja, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Caja - Documento Caja"));
			e.printStackTrace();
		}
		
		return docuCajas;
	}
	

	public void filtrarDocuCajas(List<DocuCaja> docuCajas, PersUsua persUsuaSesion){
		
		try {
			this.docuCajas = docuCajaLista.filtrarDocuCajas(docuCajas, persUsuaSesion, variablesSesion.getRolDocus());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error filtra documentos"));
			e.printStackTrace();
		}			

	}
	
	public void buscarCajas() {

		Caja caja = new Caja();
		caja.setEstado(true);
		
		try {
			this.cajas = cajaLista.buscar(caja, null, this.sucursals);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
							"Excepcion - Error al buscar cajas"));

			e.printStackTrace();
		}

	}
	
	public void buscarPersCajes(){

		PersCaje persCaje = new PersCaje();
		persCaje.setPersona(new Persona());
		persCaje.setEstado(true);
				
		try {
			this.persCajes = persCajeLista.buscar(persCaje, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar cajero"));
			e.printStackTrace();
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

	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	
	public CajaMovi getCajaMoviDesde() {
		return cajaMoviDesde;
	}

	public void setCajaMoviDesde(CajaMovi cajaMoviDesde) {
		this.cajaMoviDesde = cajaMoviDesde;
	}

	public CajaMovi getCajaMoviHasta() {
		return cajaMoviHasta;
	}

	public void setCajaMoviHasta(CajaMovi cajaMoviHasta) {
		this.cajaMoviHasta = cajaMoviHasta;
	}

	public List<DocuCaja> getDocuCajas() {
		return docuCajas;
	}

	public void setDocuCajas(List<DocuCaja> docuCajas) {
		this.docuCajas = docuCajas;
	}

	public ArrayList<String> getDocuCajaIds() {
		return docuCajaIds;
	}

	public void setDocuCajaIds(ArrayList<String> docuCajaIds) {
		this.docuCajaIds = docuCajaIds;
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

	public List<PersCaje> getPersCajes() {
		return persCajes;
	}

	public void setPersCajes(List<PersCaje> persCajes) {
		this.persCajes = persCajes;
	}

	public ArrayList<String> getPersCajeIds() {
		return persCajeIds;
	}

	public void setPersCajeIds(ArrayList<String> persCajeIds) {
		this.persCajeIds = persCajeIds;
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

	public Boolean getCajaEstado() {
		return cajaEstado;
	}

	public void setCajaEstado(Boolean cajaEstado) {
		this.cajaEstado = cajaEstado;
	}
}
