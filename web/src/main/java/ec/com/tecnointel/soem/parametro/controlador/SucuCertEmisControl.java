package ec.com.tecnointel.soem.parametro.controlador;

import java.io.Serializable;
import java.util.List;

import ec.com.tecnointel.soem.firmaElec.listaInt.CertEmisListaInt;
import ec.com.tecnointel.soem.firmaElec.modelo.CertEmis;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.SucuCertEmisListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.SucursalListaInt;
import ec.com.tecnointel.soem.parametro.modelo.SucuCertEmis;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.registroInt.SucuCertEmisRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.SucursalRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * 
 */
@Named
@ViewScoped
public class SucuCertEmisControl extends PaginaControl implements Serializable {

	private Integer sucursalId;

	private SucuCertEmis sucuCertEmis;

	private List<SucuCertEmis> sucuCertEmiss;
	private List<Sucursal> sucursales;
	private List<CertEmis> certEmiss;

	private static final long serialVersionUID = 7212876971755434512L;

	@Inject
	SucuCertEmisRegisInt sucuCertEmisRegis;

	@Inject
	SucuCertEmisListaInt sucuCertEmisLista;

	@Inject
	SucursalListaInt sucursalLista;

	@Inject
	SucursalRegisInt sucursalRegis;

	@Inject
	CertEmisListaInt certEmisLista;

	@PostConstruct
	public void cargar() {
		sucuCertEmis = new SucuCertEmis();
		sucuCertEmis.setSucursal(new Sucursal());
		sucuCertEmis.setCertEmis(new CertEmis());
	}

	public void buscar() {

		try {

			sucuCertEmisLista.filasPagina(variablesSesion.getFilasPagina());

			this.sucuCertEmiss = sucuCertEmisLista.buscar(sucuCertEmis, this.pagina);
			this.numeroReg = sucuCertEmiss.size();
			this.contadorReg = sucuCertEmisLista.contarRegistros(sucuCertEmis);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public String cancelar() {
		return "/ppsj/parametro/sucursal/explora.xhtml?faces-redirect=true&sucursalId=" + this.getSucursalId();
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}

//		this.getSucuCertEmis().getSucursal().setSucursalId(sucursalId);
		this.buscarCertEmis();
		this.buscarSucursales();

		if (this.id == null) {
			this.sucuCertEmis = new SucuCertEmis();
			
			if (this.sucursalId != null) {

				Sucursal sucursal = new Sucursal();
				try {
					sucursal = sucursalRegis.buscarPorId(Sucursal.class, this.getSucursalId());
					this.sucuCertEmis.setSucursal(sucursal);
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al persona"));
					e.printStackTrace();
				}
			}
		} else {

			try {
				this.sucuCertEmis = sucuCertEmisRegis.buscarPorId(SucuCertEmis.class, this.getId());
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
				Object id = sucuCertEmisRegis.insertar(sucuCertEmis);
				this.id = (Integer) id;
			} else {
				sucuCertEmisRegis.modificar(sucuCertEmis);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, null, "Registro grabado. Cierre el sistema e ingrese nuevamente para que los cambios se hagan efectivos"));

		return "/ppsj/parametro/sucursal/explora?faces-redirect=true&sucursalId=" + this.getSucursalId();
	}

	public String nuevo() {
		return "/ppsj/parametro/sucuCertEmis/registra?faces-redirect=true&sucursalId=" + this.getSucursalId();
	}

	public String modificar() {
		return "/ppsj/parametro/sucuCertEmis/registra?faces-redirect=true&sucuCertEmisId=" + this.getId() + "&sucursalId="
				+ this.getSucursalId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&sucuCertEmisId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			SucuCertEmis sucuCertEmis = sucuCertEmisRegis.buscarPorId(SucuCertEmis.class, this.getId());
			sucuCertEmisRegis.eliminar(sucuCertEmis);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		return "/ppsj/parametro/sucursal/explora?faces-redirect=true&sucursalId=" + this.getSucursalId();
	}

	public void buscarSucursales() {

		try {
			this.sucursales = sucursalLista.buscar(this.getSucuCertEmis().getSucursal(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Sucursal - Error al buscar sucursales"));
			e.printStackTrace();
		}
	}

	public void buscarCertEmis() {

		this.getSucuCertEmis().getCertEmis().setEstado(true);

		try {
			this.certEmiss = certEmisLista.buscar(this.getSucuCertEmis().getCertEmis(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar certificados"));
			e.printStackTrace();
		}
	}

	public SucuCertEmis getSucuCertEmis() {
		return sucuCertEmis;
	}

	public void setSucuCertEmis(SucuCertEmis sucuCertEmis) {
		this.sucuCertEmis = sucuCertEmis;
	}

	public Integer getSucursalId() {
		return sucursalId;
	}

	public void setSucursalId(Integer sucursalId) {
		this.sucursalId = sucursalId;
	}

	public List<Sucursal> getSucursales() {
		return sucursales;
	}

	public void setSucursales(List<Sucursal> sucursales) {
		this.sucursales = sucursales;
	}

	public List<CertEmis> getCertEmiss() {
		return certEmiss;
	}

	public void setCertEmiss(List<CertEmis> certEmiss) {
		this.certEmiss = certEmiss;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}