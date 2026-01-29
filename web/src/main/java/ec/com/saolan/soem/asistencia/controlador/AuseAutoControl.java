package ec.com.saolan.soem.asistencia.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.AuseAutoListaInt;
import ec.com.saolan.soem.asistencia.modelo.AuseAuto;
import ec.com.saolan.soem.asistencia.registroInt.AuseAutoRegisInt;
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
public class AuseAutoControl extends PaginaControl implements Serializable {

	private AuseAuto auseAuto;

	private List<AuseAuto> auseAutos;
	private List<Parametro> parameEstados;

	@Inject
	AuseAutoRegisInt auseAutoRegis;

	@Inject
	AuseAutoListaInt auseAutoLista;

	private static final long serialVersionUID = -7828803453128757307L;

	@PostConstruct
	public void cargar() {

		this.auseAuto = new AuseAuto();
		this.auseAuto.setEstado("Activo");
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
			AuseAuto auseAuto = auseAutoRegis.buscarPorId(AuseAuto.class, this.getId());
			auseAutoRegis.eliminar(auseAuto);

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
		return "explora?faces-redirect=true&auseAutoId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = auseAutoRegis.insertar(auseAuto);
				this.id = (Integer) id;
			} else {
				auseAutoRegis.modificar(auseAuto);
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

		return "explora?faces-redirect=true&auseAutoId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&auseAutoId=" + this.getId();
	}

	public void preCargarLista() {

		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			auseAutoLista.filasPagina(variablesSesion.getFilasPagina());

			this.auseAutos = auseAutoLista.buscar(this.auseAuto, this.pagina);
			this.numeroReg = auseAutos.size();
			this.contadorReg = auseAutoLista.contarRegistros(this.auseAuto);
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

		this.parameEstados = this.buscarParameEstados();

		if (this.id == null) {
			this.auseAuto = new AuseAuto();
			this.auseAuto.setEstado("Activo");
		} else {

			try {
				this.auseAuto = auseAutoRegis.buscarPorId(AuseAuto.class, this.getId());
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

	public AuseAuto getAuseAuto() {
		return auseAuto;
	}

	public void setAuseAuto(AuseAuto auseAuto) {
		this.auseAuto = auseAuto;
	}

	public List<AuseAuto> getAuseAutos() {
		return auseAutos;
	}

	public void setAuseAutos(List<AuseAuto> auseAutos) {
		this.auseAutos = auseAutos;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}