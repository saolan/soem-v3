package ec.com.tecnointel.soem.ingreso.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.ingreso.listaInt.IngrDimmListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.IngresoListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDimm;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.registroInt.IngrDimmRegisInt;
import ec.com.tecnointel.soem.parametro.listaInt.DimmListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class IngrDimmControl extends PaginaControl implements Serializable {

	private Integer ingresoId;
	private String paginaRuta;
	private String listarIvaRete;

	private IngrDimm ingrDimm;

	private List<IngrDimm> ingrDimms;
	private List<Ingreso> ingresos;
	private List<Dimm> dimms;

	@Inject
	IngrDimmRegisInt ingrDimmRegis;

	@Inject
	IngrDimmListaInt ingrDimmLista;

	@Inject
	IngresoListaInt ingresoLista;

	@Inject
	DimmListaInt dimmLista;

	private static final long serialVersionUID = -3956237317612947402L;
	
	@PostConstruct
	public void cargar() {
		ingrDimm = new IngrDimm();
		ingrDimm.setIngreso(new Ingreso());
		ingrDimm.setDimm(new Dimm());

		dimms = new ArrayList<Dimm>();
	}

	public void buscar() {

		try {

			ingrDimmLista.filasPagina(variablesSesion.getFilasPagina());

			this.ingrDimms = ingrDimmLista.buscar(ingrDimm, this.pagina);
			this.numeroReg = ingrDimms.size();
			this.contadorReg = ingrDimmLista.contarRegistros(ingrDimm);
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

		this.getIngrDimm().getIngreso().setIngresoId(ingresoId);
		this.buscarIngresos();

//		Recupera retencion renta, retencion iva, impuestos dependiendo de lo que selecciono en la pantalla explorar
//		se separa de esta manera sino en el listado de seleccion sale mezclado iva, retenciones, ice
//		el valor de la variable listarIvaRete viene desde la pagina
		if (this.listarIvaRete.equals("iva")) {
			this.dimms.addAll(this.buscarDimms("Tabla12"));
			this.dimms.addAll(this.buscarDimms("ReteICE"));
			this.dimms.addAll(this.buscarDimms("IRBPNR"));
		} else if (this.listarIvaRete.equals("reteRent")) {
			this.dimms.addAll(this.buscarDimms("Tabla3"));
		} else if (this.listarIvaRete.equals("reteIva")) {
			this.dimms.addAll(this.buscarDimms("Tabla11"));
		}

		if (this.id == null) {
			this.ingrDimm = new IngrDimm();
		} else {

			try {
				this.ingrDimm = ingrDimmRegis.buscarPorId(IngrDimm.class, this.getId());
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

				Object id = ingrDimmRegis.insertar(ingrDimm);
				this.id = (Integer) id;
			} else {
				ingrDimmRegis.modificar(ingrDimm);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

//		return "explora?faces-redirect=true&ingrDimmId=" + this.getId();
		if (this.getIngresoId() != null) {
			return paginaRuta + "?faces-redirect=true&ingresoId=" + this.getIngresoId();
		} else {
			return paginaRuta + "?faces-redirect=true";
		}

	}

	public String nuevo() {

		if (this.getIngresoId() != null) {
			return "/ppsj/inventario/ingrDimm/registra?faces-redirect=true&ingresoId=" + this.getIngresoId()
					+ "&paginaRuta=" + this.getPaginaRuta() + "&listarIvaRete=" + this.getListarIvaRete();
		} else {
			return "/ppsj/inventario/ingrDimm/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta()
					+ "&listarIvaRete=" + this.getListarIvaRete();
		}
	}

	public String modificar() {
		if (this.getIngresoId() != null) {
			return "/ppsj/inventario/ingrDimm/registra?faces-redirect=true&ingrDimmId=" + this.getId() + "&ingresoId="
					+ this.getIngresoId() + "&paginaRuta=" + this.getPaginaRuta() + "&listarIvaRete="
					+ this.getListarIvaRete();
		} else {
			return "/ppsj/inventario/ingrDimm/registra?faces-redirect=true&ingrDimmId=" + this.getId() + "&paginaRuta="
					+ this.getPaginaRuta() + "&listarIvaRete=" + this.getListarIvaRete();
		}
	}

	public String explorar() {
		return "explora?faces-redirect=true&ingrDimmId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			IngrDimm ingrDimm = ingrDimmRegis.buscarPorId(IngrDimm.class, this.getId());
			ingrDimmRegis.eliminar(ingrDimm);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		if (this.getIngresoId() != null) {
			return paginaRuta + "?faces-redirect=true&ingresoId=" + this.getIngresoId();
		} else {
			return paginaRuta + "?faces-redirect=true";
		}

	}

	public String cancelar() {

		if (this.getIngresoId() != null) {
			return paginaRuta + "?faces-redirect=true&ingresoId=" + this.getIngresoId();
		} else {
			return paginaRuta + "?faces-redirect=true";
		}

	}

	public void buscarIngresos() {

		try {
			this.ingresos = ingresoLista.buscar(this.getIngrDimm().getIngreso(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar ingreso - Error al buscar ingresos"));
			e.printStackTrace();
		}
	}

	public List<Dimm> buscarDimms(String tablaRefe) {

		Dimm dimmDesde = new Dimm();

		dimmDesde.setTablaRefe(tablaRefe);
		dimmDesde.setEstado(true);

		List<Dimm> dimms = new ArrayList<>();

		try {
			dimms = dimmLista.buscar(dimmDesde, dimmDesde, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Dimm - Error al buscar dimm"));
			e.printStackTrace();
		}

		return dimms;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public IngrDimm getIngrDimm() {
		return ingrDimm;
	}

	public void setIngrDimm(IngrDimm ingrDimm) {
		this.ingrDimm = ingrDimm;
	}

	public List<IngrDimm> getIngrDimms() {
		return ingrDimms;
	}

	public void setIngrDimms(List<IngrDimm> ingrDimms) {
		this.ingrDimms = ingrDimms;
	}

	public Integer getIngresoId() {
		return ingresoId;
	}

	public void setIngresoId(Integer ingresoId) {
		this.ingresoId = ingresoId;
	}

	public List<Ingreso> getIngresos() {
		return ingresos;
	}

	public void setIngresos(List<Ingreso> ingresos) {
		this.ingresos = ingresos;
	}

	public List<Dimm> getDimms() {
		return dimms;
	}

	public void setDimms(List<Dimm> dimms) {
		this.dimms = dimms;
	}

	public String getPaginaRuta() {
		return paginaRuta;
	}

	public void setPaginaRuta(String paginaRuta) {
		this.paginaRuta = paginaRuta;
	}

	public String getListarIvaRete() {
		return listarIvaRete;
	}

	public void setListarIvaRete(String listarIvaRete) {
		this.listarIvaRete = listarIvaRete;
	}
}
