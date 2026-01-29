package ec.com.saolan.soem.asistencia.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.HoraPlanListaInt;
import ec.com.saolan.soem.asistencia.modelo.HoraPlan;
import ec.com.saolan.soem.asistencia.registroInt.HoraPlanRegisInt;
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
public class HoraPlanControl extends PaginaControl implements Serializable {

	private HoraPlan horaPlan;

	private List<HoraPlan> horaPlans;
	private List<Parametro> parameEstados;

	@Inject
	HoraPlanRegisInt horaPlanRegis;

	@Inject
	HoraPlanListaInt horaPlanLista;

	private static final long serialVersionUID = 9118819012297918380L;

	@PostConstruct
	public void cargar() {

		this.horaPlan = new HoraPlan();
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
			HoraPlan horaPlan = horaPlanRegis.buscarPorId(HoraPlan.class, this.getId());
			horaPlanRegis.eliminar(horaPlan);

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
		return "explora?faces-redirect=true&horaPlanId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = horaPlanRegis.insertar(horaPlan);
				this.id = (Integer) id;
			} else {
				horaPlanRegis.modificar(horaPlan);
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

		return "explora?faces-redirect=true&horaPlanId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&horaPlanId=" + this.getId();
	}

	public void preCargarLista() {

//		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			horaPlanLista.filasPagina(variablesSesion.getFilasPagina());

			this.horaPlans = horaPlanLista.buscar(this.horaPlan, this.pagina);
			this.numeroReg = horaPlans.size();
			this.contadorReg = horaPlanLista.contarRegistros(this.horaPlan);
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
			this.horaPlan = new HoraPlan();
		} else {

			try {
				this.horaPlan = horaPlanRegis.buscarPorId(HoraPlan.class, this.getId());
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

	public HoraPlan getHoraPlan() {
		return horaPlan;
	}

	public void setHoraPlan(HoraPlan horaPlan) {
		this.horaPlan = horaPlan;
	}

	public List<HoraPlan> getHoraPlans() {
		return horaPlans;
	}

	public void setHoraPlans(List<HoraPlan> horaPlans) {
		this.horaPlans = horaPlans;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}