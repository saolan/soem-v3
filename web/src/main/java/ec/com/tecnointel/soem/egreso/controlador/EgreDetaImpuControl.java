package ec.com.tecnointel.soem.egreso.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.egreso.listaInt.EgreDetaImpuListaInt;
import ec.com.tecnointel.soem.egreso.modelo.EgreDetaImpu;
import ec.com.tecnointel.soem.egreso.registroInt.EgreDetaImpuRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class EgreDetaImpuControl extends PaginaControl implements Serializable {

	private EgreDetaImpu egreDetaImpu;

	List<EgreDetaImpu> egreDetaImpus;

	@Inject
	EgreDetaImpuRegisInt egreDetaImpuRegis;

	@Inject
	EgreDetaImpuListaInt egreDetaImpuLista;

	private static final long serialVersionUID = 4627176367222564965L;

	@PostConstruct
	public void cargar() {

		egreDetaImpu = new EgreDetaImpu();

	}

	public void buscar() {

		try {
			
			egreDetaImpuLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.egreDetaImpus = egreDetaImpuLista.buscar(egreDetaImpu, this.pagina);
			this.numeroReg = egreDetaImpus.size();
			this.contadorReg = egreDetaImpuLista.contarRegistros(egreDetaImpu);
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
			this.egreDetaImpu = new EgreDetaImpu();
		} else {

			try {
				this.egreDetaImpu = egreDetaImpuRegis.buscarPorId(EgreDetaImpu.class, this.getId());
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
				Object id = egreDetaImpuRegis.insertar(egreDetaImpu);
				this.id = (Integer) id;
			} else {
				egreDetaImpuRegis.modificar(egreDetaImpu);
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
		return "registra?faces-redirect=true&egreDetaImpuId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&egreDetaImpuId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			EgreDetaImpu egreDetaImpu = egreDetaImpuRegis.buscarPorId(EgreDetaImpu.class, this.getId());
			egreDetaImpuRegis.eliminar(egreDetaImpu);

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

	public List<EgreDetaImpu> buscarTodo() {

		List<EgreDetaImpu> egreDetaImpus = new ArrayList<>();

		try {
			egreDetaImpus = egreDetaImpuLista.buscarTodo("egreDetaImpuId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return egreDetaImpus;
	}
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public EgreDetaImpu getEgreDetaImpu() {
		return egreDetaImpu;
	}

	public void setEgreDetaImpu(EgreDetaImpu egreDetaImpu) {
		this.egreDetaImpu = egreDetaImpu;
	}

	public List<EgreDetaImpu> getEgreDetaImpus() {
		return egreDetaImpus;
	}

	public void setEgreDetaImpus(List<EgreDetaImpu> egreDetaImpus) {
		this.egreDetaImpus = egreDetaImpus;
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