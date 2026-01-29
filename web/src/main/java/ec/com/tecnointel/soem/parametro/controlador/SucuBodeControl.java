package ec.com.tecnointel.soem.parametro.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.BodegaListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.SucuBodeListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.SucursalListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Bodega;
import ec.com.tecnointel.soem.parametro.modelo.SucuBode;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.registroInt.SucuBodeRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class SucuBodeControl extends PaginaControl implements Serializable {

//	private Integer sucursalId;
	private Integer bodegaId;
	private String paginaRuta;
	
	private SucuBode sucuBode;

	private List<SucuBode> sucuBodes;
	private List<Sucursal> sucursales;
	private List<Bodega> bodegas;
	
	@Inject
	SucuBodeRegisInt sucuBodeRegis;

	@Inject
	SucuBodeListaInt sucuBodeLista;
	
	@Inject
	SucursalListaInt sucursalLista;

	@Inject
	BodegaListaInt bodegaLista;

	private static final long serialVersionUID = 568334023702750165L;

	@PostConstruct
	public void cargar() {
		sucuBode = new SucuBode();
		sucuBode.setSucursal(new Sucursal());
		sucuBode.setBodega(new Bodega());
	}

	public void buscar() {

		try {
			
			sucuBodeLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.sucuBodes = sucuBodeLista.buscar(sucuBode, this.pagina);
			this.numeroReg = sucuBodes.size();
			this.contadorReg = sucuBodeLista.contarRegistros(sucuBode);
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
		
//		this.getSucuBode().getSucursal().setSucursalId(sucursalId);
		this.getSucuBode().getBodega().setBodegaId(bodegaId);
		this.buscarBodegas();
		this.buscarSucursales();

		if (this.id == null) {
			this.sucuBode = new SucuBode();
			this.sucuBode.setAcceso(true);
		} else {

			try {
				this.sucuBode = sucuBodeRegis.buscarPorId(SucuBode.class, this.getId());
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
				Object id = sucuBodeRegis.insertar(sucuBode);
				this.id = (Integer) id;
			} else {
				sucuBodeRegis.modificar(sucuBode);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		if (this.getBodegaId() != null){
			return paginaRuta + "?faces-redirect=true&bodegaId=" + this.getBodegaId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public String nuevo() {
		
		if (this.getBodegaId() != null){
			return "/ppsj/parametro/sucuBode/registra?faces-redirect=true&bodegaId=" + this.getBodegaId() + "&paginaRuta=" + this.getPaginaRuta();			
		} else {
			return "/ppsj/parametro/sucuBode/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta(); 
		}

	}
	
	public String modificar() {
		if (this.getBodegaId() != null){
			return "/ppsj/parametro/sucuBode/registra?faces-redirect=true&sucuBodeId=" + this.getId() + "&bodegaId=" + this.getBodegaId() + "&paginaRuta=" + this.getPaginaRuta();
		} else {
			return "/ppsj/parametro/sucuBode/registra?faces-redirect=true&sucuBodeId=" + this.getId() + "&paginaRuta=" + this.getPaginaRuta();
		}
	}

	public String explorar() {
		return "explora?faces-redirect=true&sucuBodeId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			SucuBode sucuBode = sucuBodeRegis.buscarPorId(SucuBode.class, this.getId());
			sucuBodeRegis.eliminar(sucuBode);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		if (this.getBodegaId() != null){
			return paginaRuta + "?faces-redirect=true&bodegaId=" + this.getBodegaId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public String cancelar(){
		
		if (this.getBodegaId() != null){
			return paginaRuta + "?faces-redirect=true&bodegaId=" + this.getBodegaId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public List<SucuBode> buscarTodo() {

		List<SucuBode> sucuBodes = new ArrayList<>();

		try {
			sucuBodes = sucuBodeLista.buscarTodo("descri");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return sucuBodes;
	}
	
	public void buscarSucursales() {
		
		try {
			this.sucursales = sucursalLista.buscar(this.getSucuBode().getSucursal(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Sucursal - Error al buscar sucursales"));
			e.printStackTrace();
		}
	}
	
	public void buscarBodegas() {

		this.getSucuBode().getBodega().setEstado(true);
		
		try {
			this.bodegas = bodegaLista.buscar(this.getSucuBode().getBodega(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Bodega - Error al buscar bodegas"));
			e.printStackTrace();
		}
	}
	
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public SucuBode getSucuBode() {
		return sucuBode;
	}

	public void setSucuBode(SucuBode sucuBode) {
		this.sucuBode = sucuBode;
	}

	public List<SucuBode> getSucuBodes() {
		return sucuBodes;
	}

	public void setSucuBodes(List<SucuBode> sucuBodes) {
		this.sucuBodes = sucuBodes;
	}

//	public Integer getSucursalId() {
//		return sucursalId;
//	}
//
//	public void setSucursalId(Integer sucursalId) {
//		this.sucursalId = sucursalId;
//	}

	public List<Sucursal> getSucursales() {
		return sucursales;
	}

	public void setSucursales(List<Sucursal> sucursales) {
		this.sucursales = sucursales;
	}

	public List<Bodega> getBodegas() {
		return bodegas;
	}

	public void setBodegas(List<Bodega> bodegas) {
		this.bodegas = bodegas;
	}

	public Integer getBodegaId() {
		return bodegaId;
	}

	public void setBodegaId(Integer bodegaId) {
		this.bodegaId = bodegaId;
	}

	public String getPaginaRuta() {
		return paginaRuta;
	}

	public void setPaginaRuta(String paginaRuta) {
		this.paginaRuta = paginaRuta;
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