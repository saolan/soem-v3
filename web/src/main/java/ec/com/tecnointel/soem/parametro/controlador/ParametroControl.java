package ec.com.tecnointel.soem.parametro.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.general.util.IdenSistema;
import ec.com.tecnointel.soem.parametro.listaInt.ParametroListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.registroInt.ParametroRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ParametroControl extends PaginaControl implements Serializable {

	String direMac;
	String sucursalCiRuc;
	String idSistema;

	private Parametro parametro;

	List<Parametro> parametros;

	@Inject
	ParametroRegisInt parametroRegis;

	@Inject
	ParametroListaInt parametroLista;

	@Inject
	IdenSistema idenSistema;

	private static final long serialVersionUID = -9158575523127108900L;

	@PostConstruct
	public void cargar() {

		parametro = new Parametro();
		parametro.setEstado(true);

	}

	public void buscar() {

		try {

			parametroLista.filasPagina(variablesSesion.getFilasPagina());

			this.parametros = parametroLista.buscar(parametro, this.pagina);
			this.numeroReg = parametros.size();
			this.contadorReg = parametroLista.contarRegistros(parametro);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}

		if (this.id == null) {
			this.parametro = new Parametro();
			this.parametro.setEstado(true);
		} else {

			try {
				this.parametro = parametroRegis.buscarPorId(Parametro.class, this.getId());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
				e.printStackTrace();
			}
		}
	}

	public String grabar() {

		// Los mensajes tienen RequestScope y se borran al navegar a otro pagina
		// Con este comando se guarda los mensages de confirmaci�n en navegacion
		// entre paginas
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			if (this.id == null) {
				Object id = parametroRegis.insertar(parametro);
				this.id = (Integer) id;
			} else {
				parametroRegis.modificar(parametro);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "lista?faces-redirect=true";
	}
	
	public String modificar() {
		return "registra?faces-redirect=true&parametroId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&parametroId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			Parametro parametro = parametroRegis.buscarPorId(Parametro.class, this.getId());
			parametroRegis.eliminar(parametro);

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

	public List<Parametro> buscarTodo() {

		List<Parametro> parametros = new ArrayList<>();

		try {
			parametros = parametroLista.buscarTodo("descri");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return parametros;
	}

	// <<<<<<<<<<<<<<<<<<<<< Gestionar Licencia >>>>>>>>>>>>>>>>>>>>>

	private static String buscador = "kOr3aKw0mDuQb7Gs6CiyLB5c2AVovNdJvlXtMe9H8fWTj4xg1EpPShZFYnRIUq";
	private static String encriptador = "Wvi2adXqPe6rcQwUfs4HhYAb7IpTFu1lOgNy8JkVC5nSt9ERmK3LB0ZzDoMjGx";
	
	public void recuperarDatos() {
		this.buscarParametroLicenciaNumero();
		this.encriptarIdSistema();
	}

	public void buscarParametroLicenciaNumero() {

		try {
			
			this.parametro = parametroRegis.buscarPorId(Parametro.class, 11);
			this.id = parametro.getParametroId();
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar parametro - licencia"));
			e.printStackTrace();
		}
	}

	public void encriptarIdSistema() {

		String seriePlaca;
		String ciRuc;
		String idSistemaAux = null;

		seriePlaca = this.buscarSeriePlaca();
		ciRuc = this.buscarCiRuc();
		this.sucursalCiRuc = ciRuc;
		idSistemaAux = seriePlaca + ciRuc;

		this.idSistema = idenSistema.encriptarCadena(idSistemaAux, buscador, encriptador);
	}

	public String buscarSeriePlaca() {

		String seriePlaca = null;

		seriePlaca = idenSistema.obtenerSeriePlaca();
		seriePlaca = idenSistema.depurarSeriePlaca(seriePlaca);

		if (seriePlaca == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar idSistema"));

		}

		return seriePlaca;
	}

	public String buscarCiRuc() {

		String ciRuc = null;

		ciRuc = this.variablesSesion.getSucursal().getRuc();

		if (ciRuc.length() == 13) {
			ciRuc = this.variablesSesion.getSucursal().getRuc().substring(0, this.variablesSesion.getSucursal().getRuc().length() - 3);
		} else {
			ciRuc = this.variablesSesion.getSucursal().getRuc();
		}

		return ciRuc;
	}

	public String grabarLicencia() {

		// Los mensajes tienen RequestScope y se borran al navegar a otro pagina
		// Con este comando se guarda los mensages de confirmaci�n en navegacion
		// entre paginas
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			if (this.id == null) {
				Object id = parametroRegis.insertar(parametro);
				this.id = (Integer) id;
			} else {
				parametroRegis.modificar(parametro);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Gracias por registra su licencia de uso. Por favor reinice totalmente el sistema para completar el proceso"));

		return "/inicio?faces-redirect=true";
//		return "lista?faces-redirect=true";
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public Parametro getParametro() {
		return parametro;
	}

	public void setParametro(Parametro parametro) {
		this.parametro = parametro;
	}

	public List<Parametro> getParametros() {
		return parametros;
	}

	public void setParametros(List<Parametro> parametros) {
		this.parametros = parametros;
	}

	public String getDireMac() {
		return direMac;
	}

	public void setDireMac(String direMac) {
		this.direMac = direMac;
	}

	public String getSucursalCiRuc() {
		return sucursalCiRuc;
	}

	public void setSucursalCiRuc(String sucursalCiRuc) {
		this.sucursalCiRuc = sucursalCiRuc;
	}

	public String getIdSistema() {
		return idSistema;
	}

	public void setIdSistema(String idSistema) {
		this.idSistema = idSistema;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// Estas 3 Lineas llaman al metodo cuando se abra la pagina es similar a
	// @PostConstructor
	// <f:metadata>
	// <f:event type="preRenderView"
	// listener='#{categoriaControlador.buscarTodo}' />
	// </f:metadata>

	// public void buscarTodo() {
	// try {
	// this.categorias =
	// categoriaConsultaInterface.categoriaConsultar(this.getCategoria());
	// } catch (Exception e) {
	// // TODOs Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

}