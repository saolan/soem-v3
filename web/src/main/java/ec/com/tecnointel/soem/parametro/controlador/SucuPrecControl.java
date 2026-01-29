package ec.com.tecnointel.soem.parametro.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.PrecioListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.SucuPrecListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.SucursalListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Precio;
import ec.com.tecnointel.soem.parametro.modelo.SucuPrec;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.registroInt.SucuPrecRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class SucuPrecControl extends PaginaControl implements Serializable {

//	private Integer sucursalId;
	private Integer precioId;
	private String paginaRuta;
	
	private SucuPrec sucuPrec;

	private List<SucuPrec> sucuPrecs;
	private List<Sucursal> sucursales;
	private List<Precio> precios;
	
	@Inject
	SucuPrecRegisInt sucuPrecRegis;

	@Inject
	SucuPrecListaInt sucuPrecLista;
	
	@Inject
	SucursalListaInt sucursalLista;

	@Inject
	PrecioListaInt precioLista;

	private static final long serialVersionUID = -8098694063208680721L;

	@PostConstruct
	public void cargar() {
		sucuPrec = new SucuPrec();
		sucuPrec.setSucursal(new Sucursal());
		sucuPrec.setPrecio(new Precio());
	}

	public void buscar() {

		try {
			
			sucuPrecLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.sucuPrecs = sucuPrecLista.buscar(sucuPrec, this.pagina);
			this.numeroReg = sucuPrecs.size();
			this.contadorReg = sucuPrecLista.contarRegistros(sucuPrec);
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
		
//		this.getSucuPrec().getSucursal().setSucursalId(sucursalId);
		this.getSucuPrec().getPrecio().setPrecioId(precioId);
		this.buscarPrecios();
		this.buscarSucursales();

		if (this.id == null) {
			this.sucuPrec = new SucuPrec();
			this.sucuPrec.setAcceso(true);
		} else {

			try {
				this.sucuPrec = sucuPrecRegis.buscarPorId(SucuPrec.class, this.getId());
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
				Object id = sucuPrecRegis.insertar(sucuPrec);
				this.id = (Integer) id;
			} else {
				sucuPrecRegis.modificar(sucuPrec);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		if (this.getPrecioId() != null){
			return paginaRuta + "?faces-redirect=true&precioId=" + this.getPrecioId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public String nuevo() {
		
		if (this.getPrecioId() != null){
			return "/ppsj/parametro/sucuPrec/registra?faces-redirect=true&precioId=" + this.getPrecioId() + "&paginaRuta=" + this.getPaginaRuta();			
		} else {
			return "/ppsj/parametro/sucuPrec/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta(); 
		}

	}
	
	public String modificar() {
		if (this.getPrecioId() != null){
			return "/ppsj/parametro/sucuPrec/registra?faces-redirect=true&sucuPrecId=" + this.getId() + "&precioId=" + this.getPrecioId() + "&paginaRuta=" + this.getPaginaRuta();	
		} else {
			return "/ppsj/parametro/sucuPrec/registra?faces-redirect=true&sucuPrecId=" + this.getId() + "&paginaRuta=" + this.getPaginaRuta();	
		}
	}

	public String explorar() {
		return "explora?faces-redirect=true&sucuPrecId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			SucuPrec sucuPrec = sucuPrecRegis.buscarPorId(SucuPrec.class, this.getId());
			sucuPrecRegis.eliminar(sucuPrec);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		if (this.getPrecioId() != null){
			return paginaRuta + "?faces-redirect=true&precioId=" + this.getPrecioId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public String cancelar(){
		
		if (this.getPrecioId() != null){
			return paginaRuta + "?faces-redirect=true&precioId=" + this.getPrecioId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}


	}
	
	public List<SucuPrec> buscarTodo() {

		List<SucuPrec> sucuPrecs = new ArrayList<>();

		try {
			sucuPrecs = sucuPrecLista.buscarTodo("descri");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return sucuPrecs;
	}
	
	public void buscarSucursales() {
		
		try {
			this.sucursales = sucursalLista.buscar(this.getSucuPrec().getSucursal(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Sucursal - Error al buscar sucursales"));
			e.printStackTrace();
		}
	}
	
	public void buscarPrecios() {

		this.getSucuPrec().getPrecio().setEstado(true);
		
		try {
			this.precios = precioLista.buscar(this.getSucuPrec().getPrecio(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Precios - Error al buscar precios"));
			e.printStackTrace();
		}
	}
	
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public SucuPrec getSucuPrec() {
		return sucuPrec;
	}

	public void setSucuPrec(SucuPrec sucuPrec) {
		this.sucuPrec = sucuPrec;
	}

	public List<SucuPrec> getSucuPrecs() {
		return sucuPrecs;
	}

	public void setSucuPrecs(List<SucuPrec> sucuPrecs) {
		this.sucuPrecs = sucuPrecs;
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

	public List<Precio> getPrecios() {
		return precios;
	}

	public void setPrecios(List<Precio> precios) {
		this.precios = precios;
	}

	public Integer getPrecioId() {
		return precioId;
	}

	public void setPrecioId(Integer precioId) {
		this.precioId = precioId;
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

}

