package ec.com.tecnointel.soem.caja.controlador;

import java.io.Serializable;
import java.util.List;

import ec.com.tecnointel.soem.caja.listaInt.SaliArchListaInt;
import ec.com.tecnointel.soem.caja.modelo.CajaDocuEgre;
import ec.com.tecnointel.soem.caja.modelo.SaliArch;
import ec.com.tecnointel.soem.caja.registroInt.SaliArchRegisInt;
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
public class SaliArchControl extends PaginaControl implements Serializable {

	private Integer cajaDocuEgreId;
	private String paginaRuta;

	private SaliArch saliArch;

	private List<SaliArch> saliArchs;
	
	private List<Parametro> salidas;

	@Inject
	SaliArchRegisInt salidaArchRegis;

	@Inject
	SaliArchListaInt salidaArchLista;

	private static final long serialVersionUID = -2738264322755729896L;

	@PostConstruct
	public void cargar() {

		saliArch = new SaliArch();
		saliArch.setCajaDocuEgre(new CajaDocuEgre());

	}

	public void buscar() {

		try {

			salidaArchLista.filasPagina(variablesSesion.getFilasPagina());

			this.saliArchs = salidaArchLista.buscar(saliArch, this.pagina);
			this.numeroReg = saliArchs.size();
			this.contadorReg = salidaArchLista.contarRegistros(saliArch);

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

		this.buscarSalidas();
		this.getSaliArch().getCajaDocuEgre().setCajaDocuEgreId(cajaDocuEgreId);

		if (this.id == null) {
			this.saliArch = new SaliArch();
		} else {

			try {
				this.saliArch = salidaArchRegis.buscarPorId(SaliArch.class, this.getId());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
				e.printStackTrace();
			}
		}
	}

	public String grabar() {

		// Los mensajes tienen RequestScope y se borran al navegar a otro pagina
		// Con este comando se guarda los mensages de confirmaci?n en navegacion
		// entre paginas
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			if (this.id == null) {
				
				CajaDocuEgre cajaDocuEgre = new CajaDocuEgre();
				cajaDocuEgre.setCajaDocuEgreId(cajaDocuEgreId);
				saliArch.setCajaDocuEgre(cajaDocuEgre);
				
				Object id = salidaArchRegis.insertar(saliArch);
				this.id = (Integer) id;
			} else {
				salidaArchRegis.modificar(saliArch);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		if (this.getCajaDocuEgreId() != null) {
			return paginaRuta + "?faces-redirect=true&cajaDocuEgreId=" + this.getCajaDocuEgreId();
		} else {
			return paginaRuta + "?faces-redirect=true";
		}

	}

	public String nuevo() {

		if (this.getCajaDocuEgreId() != null) {
			return "/ppsj/caja/saliArch/registra?faces-redirect=true&cajaDocuEgreId=" + this.getCajaDocuEgreId()
					+ "&paginaRuta=" + this.getPaginaRuta();
		} else {
			return "/ppsj/caja/saliAech/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta();
		}

	}

	public String modificar() {

		if (this.getCajaDocuEgreId() != null) {
			return "/ppsj/caja/saliArch/registra?faces-redirect=true&saliArchId=" + this.getId() + "&cajaDocuEgreId="
					+ this.getCajaDocuEgreId() + "&paginaRuta=" + this.getPaginaRuta();
		} else {
			return "/ppsj/caja/saliArch/registra?faces-redirect=true&saliArchId=" + this.getId() + "&paginaRuta="
					+ this.getPaginaRuta();
		}

	}

	public String explorar() {
		return "explora?faces-redirect=true&saliArchId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			SaliArch saliArch = salidaArchRegis.buscarPorId(SaliArch.class, this.getId());
			salidaArchRegis.eliminar(saliArch);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		if (this.getCajaDocuEgreId() != null) {
			return paginaRuta + "?faces-redirect=true&cajaDocuEgreId=" + this.getCajaDocuEgreId();
		} else {
			return paginaRuta + "?faces-redirect=true";
		}
	}

	public String cancelar() {

		if (this.getCajaDocuEgreId() != null) {
			return paginaRuta + "?faces-redirect=true&cajaDocuEgreId=" + this.getCajaDocuEgreId();
		} else {
			return paginaRuta + "?faces-redirect=true";
		}
	}
	
	public void buscarSalidas(){
		
		Parametro parametro = new Parametro();

		parametro.setCodigo("Caja-SalidaArchivo");
		parametro.setEstado(true);
		
		try {
			salidas = parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
							"Excepcion - Error al buscar salida de archivo"));
			e.printStackTrace();
		}

	}
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	public List<SaliArch> getSaliArchs() {
		return saliArchs;
	}

	public void setSaliArchs(List<SaliArch> saliArchs) {
		this.saliArchs = saliArchs;
	}

	public SaliArch getSaliArch() {
		return saliArch;
	}

	public void setSaliArch(SaliArch saliArch) {
		this.saliArch = saliArch;
	}

	public Integer getCajaDocuEgreId() {
		return cajaDocuEgreId;
	}

	public void setCajaDocuEgreId(Integer cajaDocuEgreId) {
		this.cajaDocuEgreId = cajaDocuEgreId;
	}

	public String getPaginaRuta() {
		return paginaRuta;
	}

	public void setPaginaRuta(String paginaRuta) {
		this.paginaRuta = paginaRuta;
	}

	public List<Parametro> getSalidas() {
		return salidas;
	}

	public void setSalidas(List<Parametro> salidas) {
		this.salidas = salidas;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}
