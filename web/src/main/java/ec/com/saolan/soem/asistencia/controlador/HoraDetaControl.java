package ec.com.saolan.soem.asistencia.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.HoraDetaListaInt;
import ec.com.saolan.soem.asistencia.modelo.HoraDeta;
import ec.com.saolan.soem.asistencia.registroInt.HoraDetaRegisInt;
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
public class HoraDetaControl extends PaginaControl implements Serializable {

	private HoraDeta horaDeta;

	private List<HoraDeta> horaDetas;
	private List<Parametro> parameEstados;

	@Inject
	HoraDetaRegisInt horaDetaRegis;

	@Inject
	HoraDetaListaInt horaDetaLista;

	private static final long serialVersionUID = -7824517109026531739L;

	@PostConstruct
	public void cargar() {

		this.horaDeta = new HoraDeta();
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
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
							"Ex - Error al buscar Parametro-EstadoRegistro"));
			e.printStackTrace();
		}

		return parametros;
	}

	public String eliminar() {

		try {
			HoraDeta horaDeta = horaDetaRegis.buscarPorId(HoraDeta.class, this.getId());
			horaDetaRegis.eliminar(horaDeta);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
							"Ex - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().getExternalContext().getFlash()
				.setKeepMessages(true);
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null,
						"Registro eliminado"));

		return "lista?faces-redirect=true";
	}

	public String explorar() {
		return "explora?faces-redirect=true&horaDetaId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = horaDetaRegis.insertar(horaDeta);
				this.id = (Integer) id;
			} else {
				horaDetaRegis.modificar(horaDeta);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
							"Ex - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().getExternalContext().getFlash()
				.setKeepMessages(true);
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null,
						"Registro grabado"));

		return "explora?faces-redirect=true&horaDetaId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&horaDetaId=" + this.getId();
	}

	public void preCargarLista() {

//		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			horaDetaLista.filasPagina(variablesSesion.getFilasPagina());

			this.horaDetas = horaDetaLista.buscar(this.horaDeta, this.pagina);
			this.numeroReg = horaDetas.size();
			this.contadorReg = horaDetaLista.contarRegistros(this.horaDeta);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
							"Ex - Error al buscar datos"));
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
			this.horaDeta = new HoraDeta();
		} else {

			try {
				this.horaDeta = horaDetaRegis.buscarPorId(HoraDeta.class, this.getId());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
								"Ex - Error al buscar Id"));
				e.printStackTrace();
			}
		}
	}

	// <<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>
	// <<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>
	// <<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>

	public HoraDeta getHoraDeta() {
		return horaDeta;
	}

	public void setHoraDeta(HoraDeta horaDeta) {
		this.horaDeta = horaDeta;
	}

	public List<HoraDeta> getHoraDetas() {
		return horaDetas;
	}

	public void setHoraDetas(List<HoraDeta> horaDetas) {
		this.horaDetas = horaDetas;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}