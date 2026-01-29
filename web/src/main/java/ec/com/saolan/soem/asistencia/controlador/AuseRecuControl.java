package ec.com.saolan.soem.asistencia.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.AuseRecuListaInt;
import ec.com.saolan.soem.asistencia.modelo.AuseRecu;
import ec.com.saolan.soem.asistencia.registroInt.AuseRecuRegisInt;
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
public class AuseRecuControl extends PaginaControl implements Serializable {

	private AuseRecu auseRecu;

	private List<AuseRecu> auseRecus;
	private List<Parametro> parameEstados;

	@Inject
	AuseRecuRegisInt auseRecuRegis;

	@Inject
	AuseRecuListaInt auseRecuLista;

	private static final long serialVersionUID = -4472282217633284623L;

	@PostConstruct
	public void cargar() {

		this.auseRecu = new AuseRecu();
		// this.auseRecu.setEstado("Activo");
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
			AuseRecu auseRecu = auseRecuRegis.buscarPorId(AuseRecu.class, this.getId());
			auseRecuRegis.eliminar(auseRecu);

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
		return "explora?faces-redirect=true&auseRecuId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = auseRecuRegis.insertar(auseRecu);
				this.id = (Integer) id;
			} else {
				auseRecuRegis.modificar(auseRecu);
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

		return "explora?faces-redirect=true&auseRecuId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&auseRecuId=" + this.getId();
	}

	public void preCargarLista() {

//		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			auseRecuLista.filasPagina(variablesSesion.getFilasPagina());

			this.auseRecus = auseRecuLista.buscar(this.auseRecu, this.pagina);
			this.numeroReg = auseRecus.size();
			this.contadorReg = auseRecuLista.contarRegistros(this.auseRecu);
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
			this.auseRecu = new AuseRecu();
			// this.auseRecu.setEstado("Activo");
		} else {

			try {
				this.auseRecu = auseRecuRegis.buscarPorId(AuseRecu.class, this.getId());
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

	public AuseRecu getAuseRecu() {
		return auseRecu;
	}

	public void setAuseRecu(AuseRecu auseRecu) {
		this.auseRecu = auseRecu;
	}

	public List<AuseRecu> getAuseRecus() {
		return auseRecus;
	}

	public void setAuseRecus(List<AuseRecu> auseRecus) {
		this.auseRecus = auseRecus;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}