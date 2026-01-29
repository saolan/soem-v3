package ec.com.saolan.soem.asistencia.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.EmplHoraPlanListaInt;
import ec.com.saolan.soem.asistencia.modelo.EmplHoraPlan;
import ec.com.saolan.soem.asistencia.registroInt.EmplHoraPlanRegisInt;
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
public class EmplHoraPlanControl extends PaginaControl implements Serializable {

	private EmplHoraPlan emplHoraPlan;

	private List<EmplHoraPlan> emplHoraPlans;
	private List<Parametro> parameEstados;

	@Inject
	EmplHoraPlanRegisInt emplHoraPlanRegis;

	@Inject
	EmplHoraPlanListaInt emplHoraPlanLista;

	private static final long serialVersionUID = -6243613263367803913L;

	@PostConstruct
	public void cargar() {

		this.emplHoraPlan = new EmplHoraPlan();
		// this.emplHoraPlan.setEstado("Activo");
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
			EmplHoraPlan emplHoraPlan = emplHoraPlanRegis.buscarPorId(EmplHoraPlan.class, this.getId());
			emplHoraPlanRegis.eliminar(emplHoraPlan);

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
		return "explora?faces-redirect=true&emplHoraPlanId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = emplHoraPlanRegis.insertar(emplHoraPlan);
				this.id = (Integer) id;
			} else {
				emplHoraPlanRegis.modificar(emplHoraPlan);
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

		return "explora?faces-redirect=true&emplHoraPlanId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&emplHoraPlanId=" + this.getId();
	}

	public void preCargarLista() {

//		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			emplHoraPlanLista.filasPagina(variablesSesion.getFilasPagina());

			this.emplHoraPlans = emplHoraPlanLista.buscar(this.emplHoraPlan, this.pagina);
			this.numeroReg = emplHoraPlans.size();
			this.contadorReg = emplHoraPlanLista.contarRegistros(this.emplHoraPlan);
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
			this.emplHoraPlan = new EmplHoraPlan();
			// this.emplHoraPlan.setEstado("Activo");
		} else {

			try {
				this.emplHoraPlan = emplHoraPlanRegis.buscarPorId(EmplHoraPlan.class, this.getId());
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

	public EmplHoraPlan getEmplHoraPlan() {
		return emplHoraPlan;
	}

	public void setEmplHoraPlan(EmplHoraPlan emplHoraPlan) {
		this.emplHoraPlan = emplHoraPlan;
	}

	public List<EmplHoraPlan> getEmplHoraPlans() {
		return emplHoraPlans;
	}

	public void setEmplHoraPlans(List<EmplHoraPlan> emplHoraPlans) {
		this.emplHoraPlans = emplHoraPlans;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}