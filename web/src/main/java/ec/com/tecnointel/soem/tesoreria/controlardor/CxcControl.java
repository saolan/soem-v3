package ec.com.tecnointel.soem.tesoreria.controlardor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.egreso.modelo.PersClie;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.tesoreria.listaInt.CobrDetaListaInt;
import ec.com.tecnointel.soem.tesoreria.listaInt.CxcListaInt;
import ec.com.tecnointel.soem.tesoreria.modelo.CobrDeta;
import ec.com.tecnointel.soem.tesoreria.modelo.Cxc;
import ec.com.tecnointel.soem.tesoreria.modelo.FpmeFormPago;
import ec.com.tecnointel.soem.tesoreria.registroInt.CxcRegisInt;
import ec.com.tecnointel.soem.tesoreria.registroInt.FpmeFormPagoRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class CxcControl extends PaginaControl implements Serializable {

	private Cxc cxc;
	private List<CobrDeta> cobrDetas;

	List<Cxc> cxcs;

	@Inject
	CxcRegisInt cxcRegis;

	@Inject
	CxcListaInt cxcLista;
	
	@Inject
	CobrDetaListaInt cobrDetaLista;

	@Inject
	FpmeFormPagoRegisInt fpmeFormPagoRegis;
	
	private static final long serialVersionUID = -941872837832077818L;

	@PostConstruct
	public void cargar() {

		cxc = new Cxc();
		PersClie persClie = new PersClie();
		persClie.setPersona(new Persona());
		Egreso egreso = new Egreso();
		egreso.setPersClie(persClie);

//		Date fechaVenc = Util.cambiarFormatoFecha(new Date(), "dd-MM-yyyy");
//		cxc.setFechaVenc(fechaVenc);

		cxc.setEgreso(egreso);
		cxc.setEstado(false);

	}

	public void buscar() {

		try {

			cxcLista.filasPagina(variablesSesion.getFilasPagina());

			this.cxcs = cxcLista.buscar(cxc, this.pagina);
			this.numeroReg = cxcs.size();
			this.contadorReg = cxcLista.contarRegistros(cxc);
			
			for (Cxc cxcSaldo : cxcs) {
				
				BigDecimal totalAbono = new BigDecimal(0);
				
				this.buscarCobrDetas(cxcSaldo);
				
				for (CobrDeta cobrDetaCxc : cobrDetas) {
					totalAbono = totalAbono.add(cobrDetaCxc.getTotal());
				}
				
				cxcSaldo.setAbono(totalAbono);
				cxcSaldo.setSaldo(cxcSaldo.getTotal().subtract(totalAbono));
			}
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public void buscarCobrDetas(Cxc cxc) {
		
		CobrDeta cobrDetaBuscar = new CobrDeta();
				
		Cxc cxcBuscar = new Cxc();
		cxcBuscar.setCxcId(cxc.getCxcId());
		Egreso egreso = new Egreso();
		cxcBuscar.setEgreso(egreso);
		
		cobrDetaBuscar.setCxc(cxcBuscar);
	
		try {
			this.cobrDetas = cobrDetaLista.buscar(cobrDetaBuscar, null);
			
			for (CobrDeta cobrDeta : cobrDetas) {
				
				FpmeFormPago fpmeFormPago = new FpmeFormPago();
				fpmeFormPago = this.fpmeFormPagoRegis.buscarPorId(FpmeFormPago.class, cobrDeta.getFpmeFormPago().getFpmeFormPagoId());
				
				cobrDeta.setFpmeFormPago(fpmeFormPago);
			}
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar detalle de cobros"));
			e.printStackTrace();
		}
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}

		if (this.id == null) {
			this.cxc = new Cxc();
		} else {

			try {

				BigDecimal totalAbono = new BigDecimal(0);

				this.cxc = cxcRegis.buscarPorId(Cxc.class, this.getId());

				this.buscarCobrDetas(this.cxc);

				for (CobrDeta cobrDetaCxp : cobrDetas) {
					totalAbono = totalAbono.add(cobrDetaCxp.getTotal());
				}

				this.cxc.setAbono(totalAbono);
				this.cxc.setSaldo(cxc.getTotal().subtract(totalAbono));

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
				Object id = cxcRegis.insertar(cxc);
				this.id = (Integer) id;
			} else {
				cxcRegis.modificar(cxc);
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
		return "registra?faces-redirect=true&cxcId=" + this.getId();
	}

	public String explorar() {
		return "/ppsj/tesoreria/cxc/explora?faces-redirect=true&cxcId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			Cxc cxc = cxcRegis.buscarPorId(Cxc.class, this.getId());
			cxcRegis.eliminar(cxc);

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

	public List<Cxc> buscarTodo() {

		List<Cxc> cxcs = new ArrayList<>();

		try {
			cxcs = cxcLista.buscarTodo("cxcId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return cxcs;
	}
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public Cxc getCxc() {
		return cxc;
	}

	public void setCxc(Cxc cxc) {
		this.cxc = cxc;
	}

	public List<Cxc> getCxcs() {
		return cxcs;
	}

	public void setCxcs(List<Cxc> cxcs) {
		this.cxcs = cxcs;
	}

	public List<CobrDeta> getCobrDetas() {
		return cobrDetas;
	}

	public void setCobrDetas(List<CobrDeta> cobrDetas) {
		this.cobrDetas = cobrDetas;
	}
}