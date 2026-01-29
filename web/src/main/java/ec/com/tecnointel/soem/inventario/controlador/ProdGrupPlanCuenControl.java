package ec.com.tecnointel.soem.inventario.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.contabilidad.listaInt.PlanCuenListaInt;
import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuen;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.inventario.listaInt.ProdGrupListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdGrupPlanCuenListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrup;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrupPlanCuen;
import ec.com.tecnointel.soem.inventario.registroInt.ProdGrupPlanCuenRegisInt;
import ec.com.tecnointel.soem.parametro.listaInt.DocumentoListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ProdGrupPlanCuenControl extends PaginaControl implements Serializable {

	private Integer prodGrupId;
	private String paginaRuta;

	private ProdGrupPlanCuen prodGrupPlanCuen;

	private List<ProdGrupPlanCuen> prodGrupPlanCuens;
	private List<ProdGrup> prodGrups;
	private List<PlanCuen> planCuens;
	private List<Parametro> parametros;
	private List<Documento> documentos;

	@Inject
	ProdGrupPlanCuenRegisInt prodGrupPlanCuenRegis;

	@Inject
	ProdGrupPlanCuenListaInt prodGrupPlanCuenLista;

	@Inject
	ProdGrupListaInt prodGrupLista;

	@Inject
	PlanCuenListaInt planCuenLista;

	@Inject
	DocumentoListaInt documentoLista;

	private static final long serialVersionUID = -2841464542181163473L;

	@PostConstruct
	public void cargar() {
		prodGrupPlanCuen = new ProdGrupPlanCuen();
		prodGrupPlanCuen.setProdGrup(new ProdGrup());
		prodGrupPlanCuen.setPlanCuen(new PlanCuen());

		this.parametros = this.buscarTipoTrans();
	}

	public void buscar() {

		try {

			prodGrupPlanCuenLista.filasPagina(variablesSesion.getFilasPagina());

			this.prodGrupPlanCuens = prodGrupPlanCuenLista.buscar(prodGrupPlanCuen, this.pagina);
			this.numeroReg = prodGrupPlanCuens.size();
			this.contadorReg = prodGrupPlanCuenLista.contarRegistros(prodGrupPlanCuen);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public List<Documento> buscarDocumentos(Documento documento) {
		
		List<Documento> documentos = new ArrayList<>();
		
		try {
			documentos = documentoLista.buscar(documento, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepción al buscar documentos"));
			e.printStackTrace();
		}
		return documentos;
	}

	public void buscarProdGrups() {

		// Se coloca el prodGrupId para buscar el registro que viene de la pagina anterior
		// y que se despliegue en el combo de tipo de producto, si no busca todos y
		// se tendria que seleccionar el tipo de producto que se necesite

		ProdGrup prodGrup = new ProdGrup();
		prodGrup.setProdGrupId(prodGrupId);
		prodGrup.setEstado(true);

		try {
			this.prodGrups = prodGrupLista.buscar(prodGrup, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Producto Grupo - Error al buscar grupos de productos"));
			e.printStackTrace();
		}
	}

	public void buscarPlanCuens() {

		PlanCuen planCuen = new PlanCuen();
		planCuen.setEstado(true);
		planCuen.setDetall(true);

		try {
			this.planCuens = planCuenLista.buscar(planCuen, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Plan Cuenta - Error al buscar plan de cuentas"));
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
		if (facesContext.isPostback()) {
			return;
		}

		this.buscarProdGrups();
		this.buscarPlanCuens();

		Documento documento = new Documento();
		documentos = buscarDocumentos(documento);
		
		if (this.id == null) {
			this.prodGrupPlanCuen = new ProdGrupPlanCuen();
		} else {

			try {
				this.prodGrupPlanCuen = prodGrupPlanCuenRegis.buscarPorId(ProdGrupPlanCuen.class, this.getId());
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
				Object id = prodGrupPlanCuenRegis.insertar(prodGrupPlanCuen);
				this.id = (Integer) id;
			} else {
				prodGrupPlanCuenRegis.modificar(prodGrupPlanCuen);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		if (this.getProdGrupId() != null) {
			return paginaRuta + "?faces-redirect=true&prodGrupId=" + this.getProdGrupId();
		} else {
			return paginaRuta + "?faces-redirect=true";
		}

	}

	public String nuevo() {

		if (this.getProdGrupId() != null) {
			return "/ppsj/inventario/prodGrupPlanCuen/registra?faces-redirect=true&prodGrupId=" + this.getProdGrupId()
					+ "&paginaRuta=" + this.getPaginaRuta();
		} else {
			return "/ppsj/inventario/prodGrupPlanCuen/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta();
		}

	}

	public String modificar() {
		if (this.getProdGrupId() != null) {
			return "/ppsj/inventario/prodGrupPlanCuen/registra?faces-redirect=true&pgpcId=" + this.getId()
					+ "&prodGrupId=" + this.getProdGrupId() + "&paginaRuta=" + this.getPaginaRuta();
		} else {
			return "/ppsj/inventario/prodGrupPlanCuen/registra?faces-redirect=true&pgpcId=" + this.getId()
					+ "&paginaRuta=" + this.getPaginaRuta();
		}
	}

	public String explorar() {
		return "explora?faces-redirect=true&pgpcId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			ProdGrupPlanCuen prodGrupPlanCuen = prodGrupPlanCuenRegis.buscarPorId(ProdGrupPlanCuen.class, this.getId());
			prodGrupPlanCuenRegis.eliminar(prodGrupPlanCuen);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		if (this.getProdGrupId() != null) {
			return paginaRuta + "?faces-redirect=true&prodGrupId=" + this.getProdGrupId();
		} else {
			return paginaRuta + "?faces-redirect=true";
		}

	}

	public String cancelar() {

		if (this.getProdGrupId() != null) {
			return paginaRuta + "?faces-redirect=true&prodGrupId=" + this.getProdGrupId();
		} else {
			return paginaRuta + "?faces-redirect=true";
		}

	}

	public List<ProdGrupPlanCuen> buscarTodo() {

		List<ProdGrupPlanCuen> prodGrupPlanCuens = new ArrayList<>();

		try {
			prodGrupPlanCuens = prodGrupPlanCuenLista.buscarTodo("prodGrupPlanCuenId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return prodGrupPlanCuens;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public ProdGrupPlanCuen getProdGrupPlanCuen() {
		return prodGrupPlanCuen;
	}

	public void setProdGrupPlanCuen(ProdGrupPlanCuen prodGrupPlanCuen) {
		this.prodGrupPlanCuen = prodGrupPlanCuen;
	}

	public List<ProdGrupPlanCuen> getProdGrupPlanCuens() {
		return prodGrupPlanCuens;
	}

	public void setProdGrupPlanCuens(List<ProdGrupPlanCuen> prodGrupPlanCuens) {
		this.prodGrupPlanCuens = prodGrupPlanCuens;
	}

	public Integer getProdGrupId() {
		return prodGrupId;
	}

	public void setProdGrupId(Integer prodGrupId) {
		this.prodGrupId = prodGrupId;
	}

	public List<ProdGrup> getProdGrups() {
		return prodGrups;
	}

	public void setProdGrups(List<ProdGrup> prodGrups) {
		this.prodGrups = prodGrups;
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

	public List<Parametro> getParametros() {
		return parametros;
	}

	public void setParametros(List<Parametro> parametros) {
		this.parametros = parametros;
	}

	public List<Documento> getDocumentos() {
		return documentos;
	}

	public void setDocumentos(List<Documento> documentos) {
		this.documentos = documentos;
	}
}
