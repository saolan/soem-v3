package ec.com.tecnointel.soem.inventario.controlador;

import java.io.Serializable;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.inventario.listaInt.KardexListaInt;
import ec.com.tecnointel.soem.inventario.modelo.Kardex;
import ec.com.tecnointel.soem.inventario.registroInt.KardexRegisInt;
import ec.com.tecnointel.soem.parametro.listaInt.SucursalListaInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class KardexControl extends PaginaControl implements Serializable {

	private Kardex kardex;

	List<Kardex> kardexs;

	@Inject
	KardexRegisInt kardexRegis;

	@Inject
	KardexListaInt kardexLista;

	@Inject
	SucursalListaInt sucursalLista;

	// @Inject
	// VariablesSesionOld variablesSesion;

	private static final long serialVersionUID = -8944315350101486959L;

	@PostConstruct
	public void cargar() {

		kardex = new Kardex();

	}

	public void buscar() {

		try {

			kardexLista.filasPagina(variablesSesion.getFilasPagina());

			this.kardexs = kardexLista.buscar(kardex, this.pagina);
			this.numeroReg = kardexs.size();
			this.contadorReg = kardexLista.contarRegistros(kardex);
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
			this.kardex = new Kardex();
		} else {

			try {
				this.kardex = kardexRegis.buscarPorId(Kardex.class, this.getId());

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
				Object id = kardexRegis.insertar(kardex);
				// return "explora?faces-redirect=true&cateId=" + id;
				this.id = (Integer) id;
			} else {
				kardexRegis.modificar(kardex);
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
		return "registra?faces-redirect=true&kardexId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&kardexId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			Kardex kardex = kardexRegis.buscarPorId(Kardex.class, this.getId());
			kardexRegis.eliminar(kardex);

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

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public Kardex getKardex() {
		return kardex;
	}

	public void setKardex(Kardex kardex) {
		this.kardex = kardex;
	}

	public List<Kardex> getKardexs() {
		return kardexs;
	}

	public void setKardexs(List<Kardex> kardexs) {
		this.kardexs = kardexs;
	}
}