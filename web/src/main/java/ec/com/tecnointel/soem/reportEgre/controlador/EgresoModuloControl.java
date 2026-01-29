package ec.com.tecnointel.soem.reportEgre.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.caja.listaInt.CajaListaInt;
import ec.com.tecnointel.soem.caja.modelo.Caja;
import ec.com.tecnointel.soem.caja.modelo.CajaMovi;
import ec.com.tecnointel.soem.egreso.listaInt.ClieGrupListaInt;
import ec.com.tecnointel.soem.egreso.modelo.ClieGrup;
import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.egreso.modelo.PersClie;
import ec.com.tecnointel.soem.egreso.modelo.PersVend;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.parametro.listaInt.DocuEgreListaInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuEgre;
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

public class EgresoModuloControl extends PaginaControl implements Serializable {

	protected String rutaJrxml;
	protected String rutaReporteCompilado = "\\jasperReportes\\egreso\\";
	
	protected Boolean persClieEstado;
	protected Boolean persVendEstado;

	protected CajaMovi cajaMoviDesd;
	protected CajaMovi cajaMoviHast;

	protected Egreso egresoDesd;
	protected Egreso egresoHast;
	
	protected PersClie persClieDesd;
	protected PersClie persClieHast;

	protected PersVend persVendDesd;
	protected PersVend persVendHast;
	
	protected Producto productoDesd;
	protected Producto productoHast;
	
	protected List<DocuEgre> docuEgres;
	protected ArrayList<String> docuEgreIds;
		
	protected List<ClieGrup> clieGrups;
	protected ArrayList<String> clieGrupIds;
	
	protected List<Caja> cajas;
	protected ArrayList<String> cajaIds;
		
	protected Set<Sucursal> sucursals;
	protected ArrayList<String> sucursalIds;
	
	@Inject
	DocuEgreListaInt docuEgreLista;
	
	@Inject
	ClieGrupListaInt clieGrupLista;
	
	@Inject
	CajaListaInt cajaLista;
	
	@Inject
	RolSucuListaInt rolSucuLista;

	private static final long serialVersionUID = 6283273060501774406L;

	@PostConstruct
	public void cargar() {

		List<DocuEgre> docuEgres = new ArrayList<>();
		
		this.cajaMoviDesd = new CajaMovi();
		this.cajaMoviHast = new CajaMovi();

		this.egresoDesd = new Egreso();
		this.egresoHast = new Egreso();
		
		this.persClieDesd = new PersClie();
		this.persClieHast = new PersClie();
		
		this.persClieDesd.setPersona(new Persona());
		this.persClieHast.setPersona(new Persona());

		this.persClieEstado = true;
		
		this.persVendDesd = new PersVend();
		this.persVendHast = new PersVend();
		
		this.persVendDesd.setPersona(new Persona());
		this.persVendHast.setPersona(new Persona());

		this.persVendEstado = true;
		
		this.productoDesd = new Producto();
		this.productoHast = new Producto();
		
		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap()
				.get("persUsua");
		
		sucursals = new HashSet<>();
		
		this.buscarRolSucus();

		docuEgres = this.buscarDocuEgres();
		this.filtrarDocuEgres(docuEgres, persUsuaSesion);
				
		this.buscarClieGrups();
		this.buscarCajas();
	}

	public void descargar() throws Exception {

		this.verificarParametrosClie();
		this.verificarParametrosProd();
		this.verificarParametrosVend();

		Parametro parametro = new Parametro();
		parametro = parametroRegis.buscarPorId(Parametro.class, 3000);
		rutaJrxml = parametro.getDescri();

	}

	public void verificarParametrosClie() {

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
	
	public void verificarParametrosProd(){
		
		if (productoDesd.getCodigo() == null) {
			productoDesd.setCodigo(" ");
		}

		if (productoHast.getCodigo() == null) {
			productoHast.setCodigo("zz");
		}

		if (productoDesd.getDescri() == null) {
			productoDesd.setDescri(" ");
		}

		if (productoHast.getDescri() == null) {
			productoHast.setDescri("zz");
		}

		if (productoDesd.getCodigoBarra() == null) {
			productoDesd.setCodigoBarra(" ");
		}

		if (productoHast.getCodigoBarra() == null) {
			productoHast.setCodigoBarra("zz");
		}
	}
	
	public void verificarParametrosVend() {

		if (persVendDesd.getPersona().getApelli() == null) {
			persVendDesd.getPersona().setApelli(" ");
		}
		
		if (persVendHast.getPersona().getApelli() == null) {
			persVendHast.getPersona().setApelli("zz");
		}
		
		if (persVendDesd.getPersona().getCedulaRuc() == null) {
			persVendDesd.getPersona().setCedulaRuc(" ");
		}
		
		if (persVendHast.getPersona().getCedulaRuc() == null) {
			persVendHast.getPersona().setCedulaRuc("zz");
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

	public void buscarClieGrups() {
		
		ClieGrup clieGrup = new ClieGrup();
		clieGrup.setEstado(true);
		
		try {
			clieGrups = clieGrupLista.buscar(clieGrup, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al buscar grupo clientes"));
			e.printStackTrace();
		}
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

	public Boolean getPersClieEstado() {
		return persClieEstado;
	}

	public void setPersClieEstado(Boolean persClieEstado) {
		this.persClieEstado = persClieEstado;
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

	public PersVend getPersVendDesd() {
		return persVendDesd;
	}

	public void setPersVendDesd(PersVend persVendDesd) {
		this.persVendDesd = persVendDesd;
	}

	public PersVend getPersVendHast() {
		return persVendHast;
	}

	public void setPersVendHast(PersVend persVendHast) {
		this.persVendHast = persVendHast;
	}

	public Boolean getPersVendEstado() {
		return persVendEstado;
	}

	public void setPersVendEstado(Boolean persVendEstado) {
		this.persVendEstado = persVendEstado;
	}

	public Producto getProductoDesd() {
		return productoDesd;
	}

	public void setProductoDesd(Producto productoDesd) {
		this.productoDesd = productoDesd;
	}

	public Producto getProductoHast() {
		return productoHast;
	}

	public void setProductoHast(Producto productoHast) {
		this.productoHast = productoHast;
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

	public CajaMovi getCajaMoviDesd() {
		return cajaMoviDesd;
	}

	public void setCajaMoviDesd(CajaMovi cajaMoviDesd) {
		this.cajaMoviDesd = cajaMoviDesd;
	}

	public CajaMovi getCajaMoviHast() {
		return cajaMoviHast;
	}

	public void setCajaMoviHast(CajaMovi cajaMoviHast) {
		this.cajaMoviHast = cajaMoviHast;
	}

	
}