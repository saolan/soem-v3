package ec.com.tecnointel.soem.reportIngr.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.ingreso.listaInt.ProvGrupListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.modelo.PersProv;
import ec.com.tecnointel.soem.ingreso.modelo.ProvGrup;
import ec.com.tecnointel.soem.parametro.listaInt.DocuIngrListaInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuIngr;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
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

public class IngresoModuloControl extends PaginaControl implements Serializable {

	protected String rutaJrxml;
	protected String rutaReporteCompilado = "\\jasperReportes\\ingreso\\";

	protected Boolean persProvEstado;

	protected Ingreso ingresoDesd;
	protected Ingreso ingresoHast;

	protected PersProv persProvDesd;
	protected PersProv persProvHast;

	protected List<DocuIngr> docuIngrs;
	protected ArrayList<String> docuIngrIds;

	protected List<ProvGrup> provGrups;
	protected ArrayList<String> provGrupIds;

	protected Set<Sucursal> sucursals;
	protected ArrayList<String> sucursalIds;

	@Inject
	DocuIngrListaInt docuIngrLista;

	@Inject
	ProvGrupListaInt provGrupLista;

	@Inject
	RolSucuListaInt rolSucuLista;

	private static final long serialVersionUID = 1058413300947953043L;

	@PostConstruct
	public void cargar() {

		List<DocuIngr> docuIngrs = new ArrayList<>();

		this.ingresoDesd = new Ingreso();
		this.ingresoHast = new Ingreso();

		this.persProvDesd = new PersProv();
		this.persProvHast = new PersProv();

		this.persProvDesd.setPersona(new Persona());
		this.persProvHast.setPersona(new Persona());

		this.persProvEstado = true;

		this.sucursals = new HashSet<>();

		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");

		docuIngrs = this.buscarDocuIngrs();
		this.filtrarDocuIngrs(docuIngrs, persUsuaSesion);

		this.buscarProvGrups();
		this.buscarRolSucus();
	}

	public void descargar() throws Exception {

		this.verificarParametros();

		Parametro parametro = new Parametro();
		parametro = parametroRegis.buscarPorId(Parametro.class, 4000);
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

	public List<DocuIngr> buscarDocuIngrs() {

		DocuIngr docuIngr = new DocuIngr();

		docuIngr.setDocumento(new Documento());
		docuIngr.getDocumento().setEstado(true);

		// Documento documento = new Documento();
		// documento.setEstado(true);
		//
		// DocuIngr docuIngr = new DocuIngr();
		// docuIngr.setDocumento(documento);

		List<DocuIngr> docuIngrs = new ArrayList<>();

		try {
			docuIngrs = docuIngrLista.buscar(docuIngr, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar tipo de documento"));

			e.printStackTrace();
		}

		return docuIngrs;

	}

	public void filtrarDocuIngrs(List<DocuIngr> docuIngrs, PersUsua persUsuaSesion) {
		try {

			this.docuIngrs = docuIngrLista.filtrarDocuIngrs(docuIngrs, persUsuaSesion, variablesSesion.getRolDocus());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al filtrar documentos ingreso"));
			e.printStackTrace();
		}
	}

	public void buscarProvGrups() {

		ProvGrup provGrup = new ProvGrup();
		provGrup.setEstado(true);

		try {
			this.provGrups = provGrupLista.buscar(provGrup, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
					"Excepcion - Error al buscar grupo proveedores"));
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

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

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

	public List<DocuIngr> getDocuIngrs() {
		return docuIngrs;
	}

	public void setDocuIngrs(List<DocuIngr> docuIngrs) {
		this.docuIngrs = docuIngrs;
	}

	public ArrayList<String> getDocuIngrIds() {
		return docuIngrIds;
	}

	public void setDocuIngrIds(ArrayList<String> docuIngrIds) {
		this.docuIngrIds = docuIngrIds;
	}

	public List<ProvGrup> getProvGrups() {
		return provGrups;
	}

	public void setProvGrups(List<ProvGrup> provGrups) {
		this.provGrups = provGrups;
	}

	public ArrayList<String> getProvGrupIds() {
		return provGrupIds;
	}

	public void setProvGrupIds(ArrayList<String> provGrupIds) {
		this.provGrupIds = provGrupIds;
	}

	public Boolean getPersProvEstado() {
		return persProvEstado;
	}

	public void setPersProvEstado(Boolean persProvEstado) {
		this.persProvEstado = persProvEstado;
	}

	public Set<Sucursal> getSucursals() {
		return sucursals;
	}

	public void setSucursals(Set<Sucursal> sucursals) {
		this.sucursals = sucursals;
	}

	public ArrayList<String> getSucursalIds() {
		return sucursalIds;
	}

	public void setSucursalIds(ArrayList<String> sucursalIds) {
		this.sucursalIds = sucursalIds;
	}
}
