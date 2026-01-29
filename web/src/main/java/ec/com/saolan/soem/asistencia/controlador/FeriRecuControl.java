package ec.com.saolan.soem.asistencia.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.FeriRecuListaInt;
import ec.com.saolan.soem.asistencia.modelo.FeriRecu;
import ec.com.saolan.soem.asistencia.registroInt.FeriRecuRegisInt;
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
public class FeriRecuControl extends PaginaControl implements Serializable {

	private FeriRecu feriRecu;

	private List<FeriRecu> feriRecus;
	private List<Parametro> parameEstados;

	@Inject
	FeriRecuRegisInt feriRecuRegis;

	@Inject
	FeriRecuListaInt feriRecuLista;

	private static final long serialVersionUID = 6712963557323388126L;

	@PostConstruct
	public void cargar() {

		this.feriRecu = new FeriRecu();
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
			FeriRecu feriRecu = feriRecuRegis.buscarPorId(FeriRecu.class, this.getId());
			feriRecuRegis.eliminar(feriRecu);

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
		return "explora?faces-redirect=true&feriRecuId=" + this.getId();
	}

	public String grabar() {

		try {
			if (this.id == null) {
				Object id = feriRecuRegis.insertar(feriRecu);
				this.id = (Integer) id;
			} else {
				feriRecuRegis.modificar(feriRecu);
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

		return "explora?faces-redirect=true&feriRecuId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&feriRecuId=" + this.getId();
	}

	public void preCargarLista() {

//		this.parameEstados = this.buscarAgregarParameEstados();

		try {

			feriRecuLista.filasPagina(variablesSesion.getFilasPagina());

			this.feriRecus = feriRecuLista.buscar(this.feriRecu, this.pagina);
			this.numeroReg = feriRecus.size();
			this.contadorReg = feriRecuLista.contarRegistros(this.feriRecu);
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
			this.feriRecu = new FeriRecu();
		} else {

			try {
				this.feriRecu = feriRecuRegis.buscarPorId(FeriRecu.class, this.getId());
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

	public FeriRecu getFeriRecu() {
		return feriRecu;
	}

	public void setFeriRecu(FeriRecu feriRecu) {
		this.feriRecu = feriRecu;
	}

	public List<FeriRecu> getFeriRecus() {
		return feriRecus;
	}

	public void setFeriRecus(List<FeriRecu> feriRecus) {
		this.feriRecus = feriRecus;
	}

	public List<Parametro> getParameEstados() {
		return parameEstados;
	}

	public void setParameEstados(List<Parametro> parameEstados) {
		this.parameEstados = parameEstados;
	}
}