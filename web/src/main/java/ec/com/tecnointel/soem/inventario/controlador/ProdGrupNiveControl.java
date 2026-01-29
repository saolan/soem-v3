package ec.com.tecnointel.soem.inventario.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.inventario.listaInt.ProdGrupNiveListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrupNive;
import ec.com.tecnointel.soem.inventario.registroInt.ProdGrupNiveRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ProdGrupNiveControl extends PaginaControl implements Serializable {

	private ProdGrupNive prodGrupNive;

	List<ProdGrupNive> prodGrupNives;

	@Inject
	ProdGrupNiveRegisInt prodGrupNiveaRegis;

	@Inject
	ProdGrupNiveListaInt prodGrupNiveLista;

	private static final long serialVersionUID = 744659961199511280L;

	@PostConstruct
	public void cargar() {

		prodGrupNive = new ProdGrupNive();

	}

	public void buscar() {

		try {
			
			prodGrupNiveLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.prodGrupNives = prodGrupNiveLista.buscar(prodGrupNive, this.pagina);
			this.numeroReg = prodGrupNives.size();
			this.contadorReg = prodGrupNiveLista.contarRegistros(prodGrupNive);
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
			this.prodGrupNive = new ProdGrupNive();
		} else {

			try {
				this.prodGrupNive = prodGrupNiveaRegis.buscarPorId(ProdGrupNive.class, this.getId());
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

		try {
			if (this.id == null) {
				Object id = prodGrupNiveaRegis.insertar(prodGrupNive);
				this.id = (Integer) id;
			} else {
				prodGrupNiveaRegis.modificar(prodGrupNive);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "lista?faces-redirect=true";
	}

	public String modificar() {
		return "registra?faces-redirect=true&prodGrupNiveId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&prodGrupNiveId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			ProdGrupNive prodGrupNive = prodGrupNiveaRegis.buscarPorId(ProdGrupNive.class, this.getId());
			prodGrupNiveaRegis.eliminar(prodGrupNive);

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

	public List<ProdGrupNive> buscarTodo() {

		List<ProdGrupNive> prodGrupNives = new ArrayList<>();

		try {
			prodGrupNives = prodGrupNiveLista.buscarTodo("prodGrupNiveId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return prodGrupNives;
	}
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public ProdGrupNive getProdGrupNive() {
		return prodGrupNive;
	}

	public void setProdGrupNive(ProdGrupNive prodGrupNive) {
		this.prodGrupNive = prodGrupNive;
	}

	public List<ProdGrupNive> getProdGrupNives() {
		return prodGrupNives;
	}

	public void setProdGrupNives(List<ProdGrupNive> prodGrupNives) {
		this.prodGrupNives = prodGrupNives;
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