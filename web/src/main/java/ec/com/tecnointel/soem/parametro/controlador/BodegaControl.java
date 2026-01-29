package ec.com.tecnointel.soem.parametro.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.BodegaListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.SucuBodeListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Bodega;
import ec.com.tecnointel.soem.parametro.modelo.SucuBode;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.registroInt.BodegaRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class BodegaControl extends PaginaControl implements Serializable {

	private Bodega bodega;

	private List<Bodega> bodegas;
	private List<SucuBode> sucuBodes;

	@Inject
	BodegaRegisInt bodegaRegis;

	@Inject
	BodegaListaInt bodegaLista;
	
	@Inject
	SucuBodeListaInt sucuBodeLista;

	private static final long serialVersionUID = -2927226705211833797L;

	@PostConstruct
	public void cargar() {

		bodega = new Bodega();
		bodega.setEstado(true);

		this.rolPermiso = variablesSesion.getRolPermiso();
		
	}

	public void buscar() {

		try {
			
			bodegaLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.bodegas = bodegaLista.buscar(bodega, this.pagina);
			this.numeroReg = bodegas.size();
			this.contadorReg = bodegaLista.contarRegistros(bodega);
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
			this.bodega = new Bodega();
			this.bodega.setEstado(true);
		} else {

			try {
				this.bodega = bodegaRegis.buscarPorId(Bodega.class, this.getId());
				this.buscarSucuBodes(bodega);
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
				Object id = bodegaRegis.insertar(bodega);
				this.id = (Integer) id;
			} else {
				bodegaRegis.modificar(bodega);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepción - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "explora?faces-redirect=true&bodegaId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&bodegaId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&bodegaId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			Bodega bodega = bodegaRegis.buscarPorId(Bodega.class, this.getId());
			bodegaRegis.eliminar(bodega);

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

	public List<Bodega> buscarTodo() {

		List<Bodega> bodegas = new ArrayList<>();

		try {
			bodegas = bodegaLista.buscarTodo("descri");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return bodegas;
	}
	
	public void buscarSucuBodes(Bodega bodega){
		
		SucuBode sucuBode = new SucuBode();
		sucuBode.setBodega(bodega);
		sucuBode.setSucursal(new Sucursal());
		
		try {
			this.sucuBodes = sucuBodeLista.buscar(sucuBode, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar sucursales"));

			e.printStackTrace();
		}

	}
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public Bodega getBodega() {
		return bodega;
	}

	public void setBodega(Bodega bodega) {
		this.bodega = bodega;
	}

	public List<Bodega> getBodegas() {
		return bodegas;
	}

	public void setBodegas(List<Bodega> bodegas) {
		this.bodegas = bodegas;
	}

	public List<SucuBode> getSucuBodes() {
		return sucuBodes;
	}

	public void setSucuBodes(List<SucuBode> sucuBodes) {
		this.sucuBodes = sucuBodes;
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