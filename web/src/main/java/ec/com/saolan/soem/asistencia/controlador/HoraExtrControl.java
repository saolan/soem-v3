package ec.com.saolan.soem.asistencia.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.HoraExtrListaInt;
import ec.com.saolan.soem.asistencia.modelo.HoraExtr;
import ec.com.saolan.soem.asistencia.registroInt.HoraExtrRegisInt;
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
public class HoraExtrControl extends PaginaControl implements Serializable {

	private HoraExtr horaExtr;

	private List<HoraExtr> horaExtrs;
	private List<Parametro> parameEstados;

	@Inject
	HoraExtrRegisInt horaExtrRegis;

	@Inject
	HoraExtrListaInt horaExtrLista;

	private static final long serialVersionUID = -3945642608414906859L;

	@PostConstruct
	public void cargar() {

		this.horaExtr = new HoraExtr();
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
			HoraExtr horaExtr = horaExtrRegis.buscarPorId(HoraExtr.class, this.getId());
			horaExtrRegis.eliminar(horaExtr);

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
		return "explora?faces-redirect=true&horaExtrId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = horaExtrRegis.insertar(horaExtr);
				this.id = (Integer) id;
			} else {
				horaExtrRegis.modificar(horaExtr);
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

		return "explora?faces-redirect=true&horaExtrId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&horaExtrId=" + this.getId();
	}

	public void preCargarLista() {

//		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			horaExtrLista.filasPagina(variablesSesion.getFilasPagina());

			this.horaExtrs = horaExtrLista.buscar(this.horaExtr, this.pagina);
			this.numeroReg = horaExtrs.size();
			this.contadorReg = horaExtrLista.contarRegistros(this.horaExtr);
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
			this.horaExtr = new HoraExtr();
		} else {

			try {
				this.horaExtr = horaExtrRegis.buscarPorId(HoraExtr.class, this.getId());
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

	public HoraExtr getHoraExtr() {
		return horaExtr;
	}

	public void setHoraExtr(HoraExtr horaExtr) {
		this.horaExtr = horaExtr;
	}

	public List<HoraExtr> getHoraExtrs() {
		return horaExtrs;
	}

	public void setHoraExtrs(List<HoraExtr> horaExtrs) {
		this.horaExtrs = horaExtrs;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}