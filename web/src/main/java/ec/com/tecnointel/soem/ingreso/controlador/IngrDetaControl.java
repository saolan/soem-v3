package ec.com.tecnointel.soem.ingreso.controlador;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.ingreso.listaInt.IngrDetaListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDeta;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.modelo.PersProvDimm;
import ec.com.tecnointel.soem.ingreso.registroInt.IngrDetaRegisInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProductoListaInt;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class IngrDetaControl extends PaginaControl implements Serializable {

	private Ingreso ingreso;
	private IngrDeta ingrDeta;
	private Producto producto;

	private List<IngrDeta> ingrDetas;
	private List<Producto> productos;

	Integer ingresoId;

	@Inject
	IngrDetaRegisInt ingrDetaRegis;

	@Inject
	IngrDetaListaInt ingrDetaLista;

	@Inject
	ProductoListaInt productoLista;

	private static final long serialVersionUID = 2480425137168572842L;

	@PostConstruct
	public void cargar() {

		ingreso = new Ingreso();
		ingrDeta = new IngrDeta();
		producto = new Producto();

		try {

			ingrDetaLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.ingrDeta.setIngreso(ingreso);
			this.ingrDetas = ingrDetaLista.buscar(this.getIngrDeta(), this.pagina);
			this.numeroReg = ingrDetas.size();
			this.contadorReg = ingrDetaLista.contarRegistros(ingrDeta);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al cargar datos"));
			e.printStackTrace();
		}
	}

	public void buscar() {

		try {
			this.ingrDetas = ingrDetaLista.buscar(this.getIngrDeta(), this.pagina);
			this.numeroReg = ingrDetas.size();
			this.contadorReg = ingrDetaLista.contarRegistros(ingrDeta);
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
			this.ingrDeta = new IngrDeta();
		} else {

			try {
				this.ingrDeta = ingrDetaRegis.buscarPorId(IngrDeta.class, this.getId());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
				e.printStackTrace();
			}
		}
	}

	public String grabar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			if (this.id == null) {
				this.ingreso.setIngresoId(this.ingresoId);
				this.ingrDeta.setIngreso(this.ingreso);
				this.ingrDeta.setProducto(this.producto);
				this.ingrDeta.setFechaRegi(LocalDate.now());
				Object id = ingrDetaRegis.insertar(ingrDeta);
				this.id = (Integer) id;
			} else {
				ingrDetaRegis.modificar(ingrDeta);
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
		return "/ppsj/ingreso/ingrDeta/registra?faces-redirect=true&ingrDetaId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&ingrDetaId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			IngrDeta ingrDeta = ingrDetaRegis.buscarPorId(PersProvDimm.class, this.getId());
			ingrDetaRegis.eliminar(ingrDeta);

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

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public Ingreso getIngreso() {
		return ingreso;
	}

	public void setIngreso(Ingreso ingreso) {
		this.ingreso = ingreso;
	}

	public List<IngrDeta> getIngrDetas() {
		return ingrDetas;
	}

	public void setIngrDetas(List<IngrDeta> ingrDetas) {
		this.ingrDetas = ingrDetas;
	}

	public IngrDeta getIngrDeta() {
		return ingrDeta;
	}

	public void setIngrDeta(IngrDeta ingrDeta) {
		this.ingrDeta = ingrDeta;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Integer getIngresoId() {
		return ingresoId;
	}

	public void setIngresoId(Integer ingresoId) {
		this.ingresoId = ingresoId;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}