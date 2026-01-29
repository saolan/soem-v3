package ec.com.saolan.soem.asistencia.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.EmplHoraListaInt;
import ec.com.saolan.soem.asistencia.modelo.EmplHora;
import ec.com.saolan.soem.asistencia.registroInt.EmplHoraRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class EmplHoraControl extends PaginaControl implements Serializable {

	private EmplHora emplHora;

	private List<EmplHora> emplHoras;
	private List<Parametro> parameEstados;

	@Inject
	EmplHoraRegisInt emplHoraRegis;

	@Inject
	EmplHoraListaInt emplHoraLista;

	private static final long serialVersionUID = -7729467300235271340L;

	@PostConstruct
	public void cargar() {

		this.emplHora = new EmplHora();
		// this.emplHora.setEstado("Activo");
	}

	public List<Parametro> agregarEstado(List<Parametro> parameEstados) {

		Parametro parametro = new Parametro();
		parametro.setDescri("Todo");
		parametro.setEstado(true);

		parameEstados.add(parametro);

		return parameEstados;
	}

	public List<Parametro> buscarAgregarParameEstados() {

		List<Parametro> parameEstados = this.buscarParameEstados();

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
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Ex - Error al buscar Parametro-EstadoRegistro"));
			e.printStackTrace();
		}

		return parametros;
	}

	public String eliminar() {

		try {
			EmplHora emplHora = emplHoraRegis.buscarPorId(EmplHora.class, this.getId());
			emplHoraRegis.eliminar(emplHora);

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
		return "explora?faces-redirect=true&emplHoraId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = emplHoraRegis.insertar(emplHora);
				this.id = (Integer) id;
			} else {
				emplHoraRegis.modificar(emplHora);
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

		return "explora?faces-redirect=true&emplHoraId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&emplHoraId=" + this.getId();
	}

	public void preCargarLista() {

//		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			emplHoraLista.filasPagina(variablesSesion.getFilasPagina());

			this.emplHoras = emplHoraLista.buscar(this.emplHora, this.pagina);
			this.numeroReg = emplHoras.size();
			this.contadorReg = emplHoraLista.contarRegistros(this.emplHora);
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

//		this.parameEstados = this.buscarParameEstados();

		if (this.id == null) {
			this.emplHora = new EmplHora();
			// this.emplHora.setEstado("Activo");
		} else {

			try {
				this.emplHora = emplHoraRegis.buscarPorId(EmplHora.class, this.getId());
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

	public EmplHora getEmplHora() {
		return emplHora;
	}

	public void setEmplHora(EmplHora emplHora) {
		this.emplHora = emplHora;
	}

	public List<EmplHora> getEmplHoras() {
		return emplHoras;
	}

	public void setEmplHoras(List<EmplHora> emplHoras) {
		this.emplHoras = emplHoras;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}