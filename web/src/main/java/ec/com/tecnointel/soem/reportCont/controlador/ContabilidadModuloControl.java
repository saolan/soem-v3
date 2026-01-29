package ec.com.tecnointel.soem.reportCont.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.contabilidad.listaInt.PlanCuenListaInt;
import ec.com.tecnointel.soem.contabilidad.listaInt.PlanCuenNiveListaInt;
import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuen;
import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuenNive;
import ec.com.tecnointel.soem.contabilidad.modelo.Transaccion;
import ec.com.tecnointel.soem.contabilidad.registroInt.PlanCuenRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.DocuTranListaInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuTran;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.listaInt.RolSucuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

public class ContabilidadModuloControl extends PaginaControl implements Serializable {
	
	protected long contadorRegPlanCuen;
	protected int numeroRegPlanCuen;
	protected Integer paginaPlanCuen;

	protected Integer planCuenNiveId;
	
	protected String rutaJrxml;
	protected String rutaReporteCompilado = "\\jasperReportes\\contabilidad\\";
	
	protected Transaccion transaccionDesd;
	protected Transaccion transaccionHast;
	
	protected PlanCuen planCuenDesd;
	protected PlanCuen planCuenHast;

	private PlanCuen planCuenBuscar;
	
	protected List<DocuTran> docuTrans;
	protected ArrayList<String> docuTranIds;
	
	protected Set<Sucursal> sucursals;
	protected ArrayList<String> sucursalIds;	

	protected List<PlanCuenNive> planCuenNives;

	protected List<Parametro> parametroEstados;
	protected ArrayList<String> estados;

	private List<PlanCuen> planCuens;
	
	@Inject
	DocuTranListaInt docuTranLista;

	@Inject
	PlanCuenNiveListaInt planCuenNiveLista;
	
	@Inject
	PlanCuenListaInt planCuenLista;

	@Inject
	PlanCuenRegisInt planCuenRegis;
		
	@Inject
	RolSucuListaInt rolSucuLista;

	private static final long serialVersionUID = 8088041325325427363L;
	
	@PostConstruct
	public void cargar() {

//		Se desactiva temporalmente porque no realiza bien el filtro 
		List<DocuTran> docuTrans = new ArrayList<>();
		
		this.planCuenDesd = new PlanCuen();
		this.planCuenHast = new PlanCuen();
		this.planCuenBuscar = new PlanCuen();
		
		this.transaccionDesd = new Transaccion();
		this.transaccionHast = new Transaccion();
				
		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap()
				.get("persUsua");
		
		sucursals = new HashSet<>();
		
		this.buscarRolSucus();

		this.docuTrans = this.buscarDocuTrans();
//		docuTrans = this.buscarDocuTrans();
//		Se desactiva temporalmente porque no realiza bien el filtro 
//		this.filtrarDocuTrans(docuTrans, persUsuaSesion);
				
		this.planCuenNives = this.buscarPlanCuenNives();
		
		this.parametroEstados = this.buscarTransaccionEstado();

	}

	public void descargar() throws Exception {

		this.verificarParametros();

		Parametro parametro = new Parametro();
		parametro = parametroRegis.buscarPorId(Parametro.class, 2000);
		rutaJrxml = parametro.getDescri();

	}

	public void verificarParametros() {
		
		if (planCuenDesd.getCodigo() == null) {
			planCuenDesd.setCodigo(" ");
		}
		
		if (planCuenHast.getCodigo() == null) {
			planCuenHast.setCodigo("zz");
		}
		
		if (this.transaccionDesd.getNumero() == null) {
			this.transaccionDesd.setNumero(1);
		}
		
		if (this.transaccionHast.getNumero() == null) {
			this.transaccionHast.setNumero(99999999);
		}
	}

	public List<DocuTran> buscarDocuTrans() {

		DocuTran docuTran = new DocuTran();
		
		docuTran.setDocumento(new Documento());
		docuTran.getDocumento().setEstado(true);

		List<DocuTran> docuTrans = new ArrayList<>();

		try {
			docuTrans = docuTranLista.buscar(docuTran, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
							"Excepcion - Error al buscar tipo de documento"));
			e.printStackTrace();
		}
		
		return docuTrans;

	}

	public List<PlanCuenNive> buscarPlanCuenNives() {

		PlanCuenNive planCuenNive = new PlanCuenNive();
		
		List<PlanCuenNive> planCuenNives = new ArrayList<>();

		try {
			planCuenNives = planCuenNiveLista.buscar(planCuenNive, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
							"Excepcion - Error al buscar niveles de plan de cuentas"));
			e.printStackTrace();
		}
		
		return planCuenNives;

	}
	
	public List<PlanCuen> buscarPlanCuens(Integer paginador) {

		if (paginador == 0) {
			this.paginaPlanCuen = 0;
		}

		if (this.planCuenBuscar.getCodigo() != null) {
			this.planCuenBuscar.setDescri(null);
		}

		List<PlanCuen> planCuens = new ArrayList<>();
		this.planCuenBuscar.setDetall(true);
		this.planCuenBuscar.setEstado(true);

		try {

			planCuenLista.filasPagina(variablesSesion.getFilasPagina());
			planCuens = this.planCuenLista.buscar(this.planCuenBuscar, this.paginaPlanCuen);

			this.numeroRegPlanCuen = planCuens.size();
			this.contadorRegPlanCuen = planCuenLista.contarRegistros(planCuenBuscar);

			this.planCuens = planCuens;

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Plan de Cuentas"));
			e.printStackTrace();
		}

		return planCuens;
	}
	
	public void filtrarDocuTrans(List<DocuTran> docuTrans, PersUsua persUsuaSesion){
		try {
			
			this.docuTrans = docuTranLista.filtrarDocuTrans(docuTrans, persUsuaSesion, variablesSesion.getRolDocus());
						
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al filtrar documentos transacción"));
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
	
	public List<Parametro> buscarTransaccionEstado() {
		
		Parametro parametro = new Parametro();
		
		List<Parametro> parametroEstados = new ArrayList<>();
		
		parametro.setCodigo("Parametro-Estado");
		parametro.setEstado(true);
		
		try {
			parametroEstados = parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepción- Error al buscar estado de documentos"));
			e.printStackTrace();
		}
		
		return parametroEstados;
		
	}

	public Transaccion getTransaccionDesd() {
		return transaccionDesd;
	}

	public void setTransaccionDesd(Transaccion transaccionDesd) {
		this.transaccionDesd = transaccionDesd;
	}

	public Transaccion getTransaccionHast() {
		return transaccionHast;
	}

	public void setTransaccionHast(Transaccion transaccionHast) {
		this.transaccionHast = transaccionHast;
	}

	public ArrayList<String> getDocuTranIds() {
		return docuTranIds;
	}

	public void setDocuTranIds(ArrayList<String> docuTranIds) {
		this.docuTranIds = docuTranIds;
	}

	public List<DocuTran> getDocuTrans() {
		return docuTrans;
	}

	public void setDocuTrans(List<DocuTran> docuTrans) {
		this.docuTrans = docuTrans;
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

	public PlanCuen getPlanCuenDesd() {
		return planCuenDesd;
	}

	public void setPlanCuenDesd(PlanCuen planCuenDesd) {
		this.planCuenDesd = planCuenDesd;
	}

	public PlanCuen getPlanCuenHast() {
		return planCuenHast;
	}

	public void setPlanCuenHast(PlanCuen planCuenHast) {
		this.planCuenHast = planCuenHast;
	}

	public List<PlanCuenNive> getPlanCuenNives() {
		return planCuenNives;
	}

	public void setPlanCuenNives(List<PlanCuenNive> planCuenNives) {
		this.planCuenNives = planCuenNives;
	}

	public Integer getPlanCuenNiveId() {
		return planCuenNiveId;
	}

	public void setPlanCuenNiveId(Integer planCuenNiveId) {
		this.planCuenNiveId = planCuenNiveId;
	}

	public ArrayList<String> getEstados() {
		return estados;
	}

	public void setEstados(ArrayList<String> estados) {
		this.estados = estados;
	}

	public List<Parametro> getParametroEstados() {
		return parametroEstados;
	}

	public void setParametroEstados(List<Parametro> parametroEstados) {
		this.parametroEstados = parametroEstados;
	}

	public PlanCuen getPlanCuenBuscar() {
		return planCuenBuscar;
	}

	public void setPlanCuenBuscar(PlanCuen planCuenBuscar) {
		this.planCuenBuscar = planCuenBuscar;
	}

	public List<PlanCuen> getPlanCuens() {
		return planCuens;
	}

	public void setPlanCuens(List<PlanCuen> planCuens) {
		this.planCuens = planCuens;
	}

	public Integer getPaginaPlanCuen() {
		return paginaPlanCuen;
	}

	public void setPaginaPlanCuen(Integer paginaPlanCuen) {
		this.paginaPlanCuen = paginaPlanCuen;
	}

	public long getContadorRegPlanCuen() {
		return contadorRegPlanCuen;
	}

	public void setContadorRegPlanCuen(long contadorRegPlanCuen) {
		this.contadorRegPlanCuen = contadorRegPlanCuen;
	}
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
