package ec.com.tecnointel.soem.parametro.controlador;

import java.io.Serializable;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.ParametroListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.SucuParaListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.SucursalListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.ParametroBuilder;
import ec.com.tecnointel.soem.parametro.modelo.SucuPara;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.registroInt.SucuParaRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class SucuParaControl extends PaginaControl implements Serializable {

	private static final long serialVersionUID = -4525420250781784197L;

	private SucuPara sucuPara;

	private List<SucuPara> sucuParas;
	private List<Sucursal> sucursales;
	private List<Parametro> parametros;

	@Inject
	SucuParaRegisInt sucuParaRegis;

	@Inject
	SucuParaListaInt sucuParaLista;

	@Inject
	SucursalListaInt sucursalLista;

	@Inject
	ParametroListaInt parametroLista;

	@PostConstruct
	public void cargar() {
		sucuPara = new SucuPara();
		sucuPara.setSucursal(new Sucursal());
		sucuPara.setParametro(new Parametro());
	}

	public void buscar() {

		try {

			sucuParaLista.filasPagina(variablesSesion.getFilasPagina());

			this.sucuParas = sucuParaLista.buscar(sucuPara, this.pagina);
			this.numeroReg = sucuParas.size();
			this.contadorReg = sucuParaLista.contarRegistros(sucuPara);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}

//		Parametro parametro = new ParametroBuilder()
//                .withParametroId(1)
//                .withCodigo("COD001")
//                .withDescri("Descripción de ejemplo")
//                .withEstado(true)
//                .build();

//		Cuando se hace el builder con campos obligatorios
//		Parametro parametro = new ParametroBuilder(true)
//                .withCodigo("Parametro-Firma")
//                .build();

		Parametro parametro = new ParametroBuilder()
                .withCodigo("Parametro-Firma")
                .withEstado(true)
                .build();

		this.buscarParametros(parametro);
		this.buscarSucursales();

		if (this.id == null) {
			this.sucuPara = new SucuPara();
			this.sucuPara.setEstado(true);
		} else {

			try {
				this.sucuPara = sucuParaRegis.buscarPorId(SucuPara.class, this.getId());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar Id"));
				e.printStackTrace();
			}
		}
	}

	public String grabar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			if (this.id == null) {
				Object id = sucuParaRegis.insertar(sucuPara);
				this.id = (Integer) id;
			} else {
				sucuParaRegis.modificar(sucuPara);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "explora?faces-redirect=true&sucuParaId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&sucuParaId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&sucuParaId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			SucuPara sucuPara = sucuParaRegis.buscarPorId(SucuPara.class, this.getId());
			sucuParaRegis.eliminar(sucuPara);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		return "lista?faces-redirect=true";
	}

	public void buscarSucursales() {

		try {
			this.sucursales = sucursalLista.buscar(this.getSucuPara().getSucursal(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Sucursal - Error al buscar sucursales"));
			e.printStackTrace();
		}
	}

	public void buscarParametros(Parametro parametro) {

		try {
			this.parametros = parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Error al buscar parametros firma"));
			e.printStackTrace();
		}
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public SucuPara getSucuPara() {
		return sucuPara;
	}

	public void setSucuPara(SucuPara sucuPara) {
		this.sucuPara = sucuPara;
	}

	public List<SucuPara> getSucuParas() {
		return sucuParas;
	}

	public void setSucuParas(List<SucuPara> sucuParas) {
		this.sucuParas = sucuParas;
	}

	public List<Sucursal> getSucursales() {
		return sucursales;
	}

	public void setSucursales(List<Sucursal> sucursales) {
		this.sucursales = sucursales;
	}

	public List<Parametro> getParametros() {
		return parametros;
	}

	public void setParametros(List<Parametro> parametros) {
		this.parametros = parametros;
	}

}