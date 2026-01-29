package ec.com.saolan.soem.asistencia.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.HoraPlanDetaListaInt;
import ec.com.saolan.soem.asistencia.modelo.HoraPlanDeta;
import ec.com.saolan.soem.asistencia.registroInt.HoraPlanDetaRegisInt;
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
public class HoraPlanDetaControl extends PaginaControl implements Serializable {

	private HoraPlanDeta horaPlanDeta;

	private List<HoraPlanDeta> horaPlanDetas;
	private List<Parametro> parameEstados;

	@Inject
	HoraPlanDetaRegisInt horaPlanDetaRegis;

	@Inject
	HoraPlanDetaListaInt horaPlanDetaLista;

	private static final long serialVersionUID = 424146746676998540L;

	@PostConstruct
	public void cargar() {

		this.horaPlanDeta = new HoraPlanDeta();
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
			HoraPlanDeta horaPlanDeta = horaPlanDetaRegis.buscarPorId(HoraPlanDeta.class, this.getId());
			horaPlanDetaRegis.eliminar(horaPlanDeta);

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
		return "explora?faces-redirect=true&horaPlanDetaId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = horaPlanDetaRegis.insertar(horaPlanDeta);
				this.id = (Integer) id;
			} else {
				horaPlanDetaRegis.modificar(horaPlanDeta);
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

		return "explora?faces-redirect=true&horaPlanDetaId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&horaPlanDetaId=" + this.getId();
	}

	public void preCargarLista() {

//		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			horaPlanDetaLista.filasPagina(variablesSesion.getFilasPagina());

			this.horaPlanDetas = horaPlanDetaLista.buscar(this.horaPlanDeta, this.pagina);
			this.numeroReg = horaPlanDetas.size();
			this.contadorReg = horaPlanDetaLista.contarRegistros(this.horaPlanDeta);
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
			this.horaPlanDeta = new HoraPlanDeta();
		} else {

			try {
				this.horaPlanDeta = horaPlanDetaRegis.buscarPorId(HoraPlanDeta.class, this.getId());
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

	public HoraPlanDeta getHoraPlanDeta() {
		return horaPlanDeta;
	}

	public void setHoraPlanDeta(HoraPlanDeta horaPlanDeta) {
		this.horaPlanDeta = horaPlanDeta;
	}

	public List<HoraPlanDeta> getHoraPlanDetas() {
		return horaPlanDetas;
	}

	public void setHoraPlanDetas(List<HoraPlanDeta> horaPlanDetas) {
		this.horaPlanDetas = horaPlanDetas;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}