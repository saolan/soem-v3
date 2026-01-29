package ec.com.tecnointel.soem.parametro.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.CentCostListaInt;
import ec.com.tecnointel.soem.parametro.modelo.CentCost;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.registroInt.CentCostRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class CentCostControl extends PaginaControl implements Serializable {

	private static final long serialVersionUID = 2962341863999631590L;

	private CentCost centCost;

	private List<CentCost> centCosts;
	private List<Parametro> parameEstados;

	@Inject
	CentCostRegisInt centCostRegis;

	@Inject
	CentCostListaInt centCostLista;

	@PostConstruct
	public void cargar() {

		this.centCost = new CentCost();
		this.centCost.setEstado("Activo");
	}
	
	public List<Parametro> buscarAgregarParameEstados() {
		
		List<Parametro> parameEstados =  this.buscarParameEstados();
		
		Parametro parametro = new Parametro();
		parametro.setDescri("Todo");
		parametro.setEstado(true);
		
		parameEstados.add(parametro);
		
		return parameEstados;
	}

	public List<Parametro> buscarParameEstados() {

		Parametro parametro = new Parametro();
		parametro.setCodigo("Parametro-EstadoRegistro");
		parametro.setEstado(true);

		List<Parametro> parametros = new ArrayList<>();
		
		try {
			parametros = parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Ex - Error al buscar Parametro-EstadoRegistro"));
			e.printStackTrace();
		}
		
		return parametros;
	}
	
	public String eliminar() {
		
		try {
			CentCost centCost = centCostRegis.buscarPorId(CentCost.class, this.getId());
			centCostRegis.eliminar(centCost);
	
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Ex - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}
	
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));
	
		return "lista?faces-redirect=true";
	}

	public String explorar() {
		return "explora?faces-redirect=true&centCostId=" + this.getId();
	}

	public String grabar() {
	
		try {
			if (this.id == null) {
				Object id = centCostRegis.insertar(centCost);
				this.id = (Integer) id;
			} else {
				centCostRegis.modificar(centCost);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Ex - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}
	
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));
	
		return "explora?faces-redirect=true&centCostId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&centCostId=" + this.getId();
	}

	public void preCargarLista() {
	
//		Se activa o se desactiva depandiendo si la tabla tiene o no el campo estado
		this.parameEstados = this.buscarAgregarParameEstados();
		
		try {
	
			centCostLista.filasPagina(variablesSesion.getFilasPagina());
	
			this.centCosts = centCostLista.buscar(this.centCost, this.pagina);
			this.numeroReg = centCosts.size();
			this.contadorReg = centCostLista.contarRegistros(this.centCost);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Ex - Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public void preCargarRegExp() {
	
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}
		
//		Se activa o se desactiva depandiendo si la tabla tiene o no el campo estado
		this.parameEstados = this.buscarParameEstados();
		
		if (this.id == null) {
			this.centCost = new CentCost();
			this.centCost.setEstado("Activo");
		} else {
	
			try {
				this.centCost = centCostRegis.buscarPorId(CentCost.class, this.getId());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Ex - Error al buscar Id"));
				e.printStackTrace();
			}
		}
	}

	// <<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>
	// <<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>
	// <<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>
	
	public CentCost getCentCost() {
		return centCost;
	}

	public void setCentCost(CentCost centCost) {
		this.centCost = centCost;
	}

	public List<CentCost> getCentCosts() {
		return centCosts;
	}

	public void setCentCosts(List<CentCost> centCosts) {
		this.centCosts = centCosts;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}