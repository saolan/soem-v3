package ec.com.tecnointel.soem.ingreso.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.ingreso.listaInt.IngrDetaImpuListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDeta;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDetaImpu;
import ec.com.tecnointel.soem.ingreso.registroInt.IngrDetaImpuRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class IngrDetaImpuControl extends PaginaControl implements Serializable {

	private IngrDetaImpu ingrDetaImpu;

	List<IngrDetaImpu> ingrDetaImpus;

	@Inject
	IngrDetaImpuRegisInt ingrDetaImpuRegis;

	@Inject
	IngrDetaImpuListaInt ingrDetaImpuLista;

	private static final long serialVersionUID = 7649585272993216300L;

	@PostConstruct
	public void cargar() {

		ingrDetaImpu = new IngrDetaImpu();
		ingrDetaImpu.setIngrDeta(new IngrDeta());

	}

	public void buscar() {

		try {
			
			ingrDetaImpuLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.ingrDetaImpus = ingrDetaImpuLista.buscar(ingrDetaImpu, this.pagina);
			this.numeroReg = ingrDetaImpus.size();
			this.contadorReg = ingrDetaImpuLista.contarRegistros(ingrDetaImpu);
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
			this.ingrDetaImpu = new IngrDetaImpu();
		} else {

			try {
				this.ingrDetaImpu = ingrDetaImpuRegis.buscarPorId(IngrDetaImpu.class, this.getId());
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
				Object id = ingrDetaImpuRegis.insertar(ingrDetaImpu);
				this.id = (Integer) id;
			} else {
				ingrDetaImpuRegis.modificar(ingrDetaImpu);
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
		return "registra?faces-redirect=true&ingrDetaImpuId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&ingrDetaImpuId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			IngrDetaImpu ingrDetaImpu = ingrDetaImpuRegis.buscarPorId(IngrDetaImpu.class, this.getId());
			ingrDetaImpuRegis.eliminar(ingrDetaImpu);

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

	public List<IngrDetaImpu> buscarTodo() {

		List<IngrDetaImpu> ingrDetaImpus = new ArrayList<>();

		try {
			ingrDetaImpus = ingrDetaImpuLista.buscarTodo("retencionId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return ingrDetaImpus;
	}
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public IngrDetaImpu getIngrDetaImpu() {
		return ingrDetaImpu;
	}

	public void setIngrDetaImpu(IngrDetaImpu ingrDetaImpu) {
		this.ingrDetaImpu = ingrDetaImpu;
	}

	public List<IngrDetaImpu> getIngrDetaImpus() {
		return ingrDetaImpus;
	}

	public void setIngrDetaImpus(List<IngrDetaImpu> ingrDetaImpus) {
		this.ingrDetaImpus = ingrDetaImpus;
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