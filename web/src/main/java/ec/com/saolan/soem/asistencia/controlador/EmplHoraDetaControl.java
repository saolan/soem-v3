package ec.com.saolan.soem.asistencia.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.EmplHoraDetaListaInt;
import ec.com.saolan.soem.asistencia.modelo.EmplHoraDeta;
import ec.com.saolan.soem.asistencia.registroInt.EmplHoraDetaRegisInt;
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
public class EmplHoraDetaControl extends PaginaControl implements Serializable {

	private EmplHoraDeta emplHoraDeta;

	private List<EmplHoraDeta> emplHoraDetas;
	private List<Parametro> parameEstados;

	@Inject
	EmplHoraDetaRegisInt emplHoraDetaRegis;

	@Inject
	EmplHoraDetaListaInt emplHoraDetaLista;

	private static final long serialVersionUID = 7566672638247314425L;

	@PostConstruct
	public void cargar() {

		this.emplHoraDeta = new EmplHoraDeta();
		// this.emplHoraDeta.setEstado("Activo");
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
			EmplHoraDeta emplHoraDeta = emplHoraDetaRegis.buscarPorId(EmplHoraDeta.class, this.getId());
			emplHoraDetaRegis.eliminar(emplHoraDeta);

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
		return "explora?faces-redirect=true&emplHoraDetaId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = emplHoraDetaRegis.insertar(emplHoraDeta);
				this.id = (Integer) id;
			} else {
				emplHoraDetaRegis.modificar(emplHoraDeta);
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

		return "explora?faces-redirect=true&emplHoraDetaId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&emplHoraDetaId=" + this.getId();
	}

	public void preCargarLista() {

//		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			emplHoraDetaLista.filasPagina(variablesSesion.getFilasPagina());

			this.emplHoraDetas = emplHoraDetaLista.buscar(this.emplHoraDeta, this.pagina);
			this.numeroReg = emplHoraDetas.size();
			this.contadorReg = emplHoraDetaLista.contarRegistros(this.emplHoraDeta);
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
			this.emplHoraDeta = new EmplHoraDeta();
			// this.emplHoraDeta.setEstado("Activo");
		} else {

			try {
				this.emplHoraDeta = emplHoraDetaRegis.buscarPorId(EmplHoraDeta.class, this.getId());
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

	public EmplHoraDeta getEmplHoraDeta() {
		return emplHoraDeta;
	}

	public void setEmplHoraDeta(EmplHoraDeta emplHoraDeta) {
		this.emplHoraDeta = emplHoraDeta;
	}

	public List<EmplHoraDeta> getEmplHoraDetas() {
		return emplHoraDetas;
	}

	public void setEmplHoraDetas(List<EmplHoraDeta> emplHoraDetas) {
		this.emplHoraDetas = emplHoraDetas;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}