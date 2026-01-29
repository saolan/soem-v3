package ec.com.tecnointel.soem.tesoreria.controlardor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.modelo.PersProv;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.tesoreria.listaInt.CxpListaInt;
import ec.com.tecnointel.soem.tesoreria.listaInt.PagoDetaListaInt;
import ec.com.tecnointel.soem.tesoreria.modelo.Cxp;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import ec.com.tecnointel.soem.tesoreria.modelo.PagoDeta;
import ec.com.tecnointel.soem.tesoreria.registroInt.CxpRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class CxpControl extends PaginaControl implements Serializable {

	private Cxp cxp;
	private List<PagoDeta> pagoDetas;

	List<Cxp> cxps;

	@Inject
	CxpRegisInt cxpRegis;

	@Inject
	CxpListaInt cxpLista;
	
	@Inject
	PagoDetaListaInt pagoDetaLista;

	private static final long serialVersionUID = 8048591670410633182L;

	@PostConstruct
	public void cargar() {

		cxp = new Cxp();
		PersProv persProv = new PersProv();
		persProv.setPersona(new Persona());
		Ingreso ingreso = new Ingreso();
		ingreso.setPersProv(persProv);
		
//		Date fechaEmis = Util.cambiarFormatoFecha(new Date(), "dd-MM-yyyy");
//		ingreso.setFechaEmis(fechaEmis);
//		cxp.setFechaVenc(fechaEmis);
		
		cxp.setIngreso(ingreso);
		cxp.setEstado(false);
	}

	public void buscar() {
		
		try {
			
			cxpLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.cxps = cxpLista.buscar(cxp, this.pagina);
			this.numeroReg = cxps.size();
			this.contadorReg = cxpLista.contarRegistros(cxp);

			for (Cxp cxpSaldo : cxps) {
				
				BigDecimal totalAbono = new BigDecimal(0);
				
				this.buscarPagoDetas(cxpSaldo);
				
				for (PagoDeta pagoDetaCxp : pagoDetas) {
					totalAbono = totalAbono.add(pagoDetaCxp.getTotal());
				}
				
				cxpSaldo.setAbono(totalAbono);
				cxpSaldo.setSaldo(cxpSaldo.getTotal().subtract(totalAbono));
			}
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public void buscarPagoDetas(Cxp cxp) {
	
		PagoDeta pagoDeta = new PagoDeta();
				
		Cxp cxpBuscar = new Cxp();
		cxpBuscar.setCxpId(cxp.getCxpId());
		Ingreso ingreso = new Ingreso();
		cxpBuscar.setIngreso(ingreso);
		
		FormPagoMoviIngr formPagoMoviIngrPagoDeta = new FormPagoMoviIngr();
		Persona persona = new Persona();
		FormPago formPago = new FormPago();
		formPagoMoviIngrPagoDeta.setPersona(persona);
		formPagoMoviIngrPagoDeta.setFormPago(formPago);
	
		pagoDeta.setFormPagoMoviIngr(formPagoMoviIngrPagoDeta);
		pagoDeta.setCxp(cxpBuscar);
	
		try {
			this.pagoDetas = pagoDetaLista.buscar(pagoDeta, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar detalle de pagos"));
			e.printStackTrace();
		}
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback()) {
			return;
		}

		if (this.id == null) {
			this.cxp = new Cxp();
		} else {

			try {
				
				BigDecimal totalAbono = new BigDecimal(0);
				
				this.cxp = cxpRegis.buscarPorId(Cxp.class, this.getId());
				
				this.buscarPagoDetas(this.cxp);
				
				for (PagoDeta pagoDetaCxp : pagoDetas) {
					totalAbono = totalAbono.add(pagoDetaCxp.getTotal());
				}
				
				this.cxp.setAbono(totalAbono);
				this.cxp.setSaldo(cxp.getTotal().subtract(totalAbono));
				
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
				Object id = cxpRegis.insertar(cxp);
				this.id = (Integer) id;
			} else {
				cxpRegis.modificar(cxp);
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
		return "registra?faces-redirect=true&cxpId=" + this.getId();
	}

	public String explorar() {
		return "/ppsj/tesoreria/cxp/explora?faces-redirect=true&cxpId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			Cxp cxp = cxpRegis.buscarPorId(Cxp.class, this.getId());
			cxpRegis.eliminar(cxp);

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

	public List<Cxp> buscarTodo() {

		List<Cxp> cxps = new ArrayList<>();

		try {
			cxps = cxpLista.buscarTodo("cxpId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return cxps;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public Cxp getCxp() {
		return cxp;
	}

	public void setCxp(Cxp cxp) {
		this.cxp = cxp;
	}

	public List<Cxp> getCxps() {
		return cxps;
	}

	public void setCxps(List<Cxp> cxps) {
		this.cxps = cxps;
	}

	public List<PagoDeta> getPagoDetas() {
		return pagoDetas;
	}

	public void setPagoDetas(List<PagoDeta> pagoDetas) {
		this.pagoDetas = pagoDetas;
	}
}