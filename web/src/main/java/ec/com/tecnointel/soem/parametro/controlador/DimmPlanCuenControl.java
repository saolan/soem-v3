package ec.com.tecnointel.soem.parametro.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.contabilidad.listaInt.PlanCuenListaInt;
import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuen;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.DimmListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.DimmPlanCuenListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.DimmPlanCuen;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.registroInt.DimmPlanCuenRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class DimmPlanCuenControl extends PaginaControl implements Serializable {

	private Integer dimmId;
	private String paginaRuta;

	private DimmPlanCuen dimmPlanCuen;

	private List<DimmPlanCuen> dimmPlanCuens;
	private List<Dimm> dimms;
	private List<PlanCuen> planCuens;
	
	private List<Parametro> parametros;

	@Inject
	DimmPlanCuenRegisInt dimmPlanCuenRegis;

	@Inject
	DimmPlanCuenListaInt dimmPlanCuenLista;

	@Inject
	DimmListaInt dimmLista;

	@Inject
	PlanCuenListaInt planCuenLista;

	private static final long serialVersionUID = 3509983390757942698L;

	@PostConstruct
	public void cargar() {
		dimmPlanCuen = new DimmPlanCuen();
		dimmPlanCuen.setDimm(new Dimm());
		dimmPlanCuen.setPlanCuen(new PlanCuen());
		
		this.parametros = this.buscarTipoTrans();
	}

	public void buscar() {

		try {

			dimmPlanCuenLista.filasPagina(variablesSesion.getFilasPagina());

			this.dimmPlanCuens = dimmPlanCuenLista.buscar(dimmPlanCuen, this.pagina);
			this.numeroReg = dimmPlanCuens.size();
			this.contadorReg = dimmPlanCuenLista.contarRegistros(dimmPlanCuen);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public void buscarDimms() {
		
		Dimm dimm = new Dimm();
		dimm.setDimmId(dimmId);
		dimm.setEstado(true);

		try {
			this.dimms = dimmLista.buscar(dimm, dimm, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar dimm"));
			e.printStackTrace();
		}
	}

	public void buscarPlanCuens() {

		this.getDimmPlanCuen().getPlanCuen().setEstado(true);
		this.getDimmPlanCuen().getPlanCuen().setDetall(true);

		try {
			this.planCuens = planCuenLista.buscar(this.getDimmPlanCuen().getPlanCuen(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar plan de cuentas"));
			e.printStackTrace();
		}
	}

	public List<Parametro> buscarTipoTrans() {
		
		Parametro parametro = new Parametro();
		
		List<Parametro> parametros = new ArrayList<>();
		
		parametro.setCodigo("Contabilidad-TipoTran");
		parametro.setEstado(true);
		
		try {
			parametros = this.parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Plan Cuenta - Error al buscar plan de cuentas"));
			e.printStackTrace();
		}
		
		return parametros;
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}

		this.buscarDimms();
		this.buscarPlanCuens();

		if (this.id == null) {
			this.dimmPlanCuen = new DimmPlanCuen();
		} else {

			try {
				this.dimmPlanCuen = dimmPlanCuenRegis.buscarPorId(DimmPlanCuen.class, this.getId());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
				e.printStackTrace();
			}
		}
	}

	public String grabar() {

		// Los mensajes tienen RequestScope y se borran al navegar a otro pagina
		// Con este comando se guarda los mensages de confirmaci?n en navegacion
		// entre paginas
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			if (this.id == null) {
				Object id = dimmPlanCuenRegis.insertar(dimmPlanCuen);
				this.id = (Integer) id;
			} else {
				dimmPlanCuenRegis.modificar(dimmPlanCuen);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		if (this.getDimmId() != null) {
			return paginaRuta + "?faces-redirect=true&dimmId=" + this.getDimmId();
		} else {
			return paginaRuta + "?faces-redirect=true";
		}

	}

	public String nuevo() {

		if (this.getDimmId() != null) {
			return "/ppsj/parametro/dimmPlanCuen/registra?faces-redirect=true&dimmId=" + this.getDimmId()
					+ "&paginaRuta=" + this.getPaginaRuta();
		} else {
			return "/ppsj/parametro/dimmPlanCuen/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta();
		}
	}

	public String modificar() {
		if (this.getDimmId() != null) {
			return "/ppsj/parametro/dimmPlanCuen/registra?faces-redirect=true&dimmPlanCuenId=" + this.getId()
					+ "&dimmId=" + this.getDimmId() + "&paginaRuta=" + this.getPaginaRuta();
		} else {
			return "/ppsj/parametro/dimmPlanCuen/registra?faces-redirect=true&dimmPlanCuenId=" + this.getId()
					+ "&paginaRuta=" + this.getPaginaRuta();
		}
	}

	public String explorar() {
		return "explora?faces-redirect=true&dimmPlanCuenId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			DimmPlanCuen dimmPlanCuen = dimmPlanCuenRegis.buscarPorId(DimmPlanCuen.class, this.getId());
			dimmPlanCuenRegis.eliminar(dimmPlanCuen);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		if (this.getDimmId() != null) {
			return paginaRuta + "?faces-redirect=true&dimmId=" + this.getDimmId();
		} else {
			return paginaRuta + "?faces-redirect=true";
		}

	}

	public String cancelar() {

		if (this.getDimmId() != null) {
			return paginaRuta + "?faces-redirect=true&dimmId=" + this.getDimmId();
		} else {
			return paginaRuta + "?faces-redirect=true";
		}
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public DimmPlanCuen getDimmPlanCuen() {
		return dimmPlanCuen;
	}

	public void setDimmPlanCuen(DimmPlanCuen dimmPlanCuen) {
		this.dimmPlanCuen = dimmPlanCuen;
	}

	public List<DimmPlanCuen> getDimmPlanCuens() {
		return dimmPlanCuens;
	}

	public void setDimmPlanCuens(List<DimmPlanCuen> dimmPlanCuens) {
		this.dimmPlanCuens = dimmPlanCuens;
	}

	public List<PlanCuen> getPlanCuens() {
		return planCuens;
	}

	public void setPlanCuens(List<PlanCuen> planCuens) {
		this.planCuens = planCuens;
	}

	public String getPaginaRuta() {
		return paginaRuta;
	}

	public void setPaginaRuta(String paginaRuta) {
		this.paginaRuta = paginaRuta;
	}

	public Integer getDimmId() {
		return dimmId;
	}

	public void setDimmId(Integer dimmId) {
		this.dimmId = dimmId;
	}

	public List<Dimm> getDimms() {
		return dimms;
	}

	public void setDimms(List<Dimm> dimms) {
		this.dimms = dimms;
	}

	public List<Parametro> getParametros() {
		return parametros;
	}

	public void setParametros(List<Parametro> parametros) {
		this.parametros = parametros;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}
