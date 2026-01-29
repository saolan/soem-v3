package ec.com.tecnointel.soem.tesoreria.controlardor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.tesoreria.listaInt.PagoDetaListaInt;
import ec.com.tecnointel.soem.tesoreria.modelo.Cxp;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import ec.com.tecnointel.soem.tesoreria.modelo.PagoDeta;
import ec.com.tecnointel.soem.tesoreria.registroInt.CxpRegisInt;
import ec.com.tecnointel.soem.tesoreria.registroInt.FormPagoMoviIngrRegisInt;
import ec.com.tecnointel.soem.tesoreria.registroInt.PagoDetaRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class PagoDetaControl extends PaginaControl implements Serializable {

	private Integer fpmiId;
	private Integer cxpId;
	
	private PagoDeta pagoDeta;
	private Cxp cxp;
	private FormPagoMoviIngr formPagoMoviIngr;
	
	private List<PagoDeta> pagoDetas;
	
	@Inject
	PagoDetaRegisInt pagoDetaRegis;

	@Inject
	PagoDetaListaInt pagoDetaLista;
	
	@Inject
	CxpRegisInt cxpRegis;
	
	@Inject
	FormPagoMoviIngrRegisInt formPagoMoviIngrRegis ;

	private static final long serialVersionUID = -1256186504600740046L;

	@PostConstruct
	public void cargar() {

		pagoDeta = new PagoDeta();
		
		Cxp cxp = new Cxp();
		Ingreso ingreso = new Ingreso();
		cxp.setIngreso(ingreso);
		pagoDeta.setCxp(cxp);
		
		Persona persona = new Persona();
		FormPago formPago = new FormPago();
		FormPagoMoviIngr formPagoMoviIngr = new FormPagoMoviIngr();
		formPagoMoviIngr.setPersona(persona);
		formPagoMoviIngr.setFormPago(formPago);
		pagoDeta.setFormPagoMoviIngr(formPagoMoviIngr);
		
		this.rolPermiso = variablesSesion.getRolPermiso();

	}

	public void buscar() {

		try {
			
			pagoDetaLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.pagoDetas = pagoDetaLista.buscar(pagoDeta, this.pagina);
			this.numeroReg = pagoDetas.size();
			this.contadorReg = pagoDetaLista.contarRegistros(pagoDeta);
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
			
//			Buscar CXP y FPMI ya que un detalle de pago siempre debe tener estas dos relaciones
			this.cxp = this.buscarCxp();
			this.formPagoMoviIngr = this.buscarFormPagoMoviIngr();
			
			this.pagoDeta.setCxp(cxp);
			this.pagoDeta.setFormPagoMoviIngr(formPagoMoviIngr);

		} else {

			try {
				
				this.pagoDeta = pagoDetaRegis.buscarPorId(PagoDeta.class, this.getId());
				
//				Asigna CXP y FPMI a entidades separadas para calcular abonos, saldos y realizar validaciones
				this.cxp = pagoDeta.getCxp();
				this.formPagoMoviIngr = pagoDeta.getFormPagoMoviIngr();
				
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
				e.printStackTrace();
			}
		}

		
//		Buscar pagos de CXP para calcular abonos y saldo
		BigDecimal totalAbono = new BigDecimal(0);

//		Se envia como parametro de busqueda
		PagoDeta pagoDeta = new PagoDeta();
		
		List<PagoDeta> pagoDetas = new ArrayList<>();
		
		FormPagoMoviIngr formPagoMoviIngr = new FormPagoMoviIngr();
		formPagoMoviIngr.setPersona(new Persona());
		formPagoMoviIngr.setFormPago(new FormPago());
		pagoDeta.setFormPagoMoviIngr(formPagoMoviIngr);				
		pagoDeta.setCxp(this.cxp);
		
		try {
			pagoDetas = this.pagoDetaLista.buscar(pagoDeta, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar detalle de pagos"));
			e.printStackTrace();
		}
		
//		Si se esta modificando el pago
//		Se elimina de la lista para que no le tome en cuenta en el calculo del abono y saldo
//		Para que la validacion tenga el abono y el saldo sin el registro actual
		if (this.id != null) {
			pagoDetas.remove(this.pagoDeta);
		}
		
		for (PagoDeta pagoDetaCxp : pagoDetas) {
			totalAbono = totalAbono.add(pagoDetaCxp.getTotal());
		}
		
//		Calcula abono y saldo de CXP
		this.cxp.setAbono(totalAbono);
		this.cxp.setSaldo(this.cxp.getTotal().subtract(totalAbono));
	}

	public String grabar() {

		String mensaje = validarGrabar();
		
		if (!mensaje.equals("validado")) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, mensaje));
			return null;
		}
		
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			if (this.id == null) {
				
				Object id = pagoDetaRegis.insertar(pagoDeta);
				this.id = (Integer) id;
								
			} else {
				
				pagoDetaRegis.modificar(pagoDeta);
				
			}
			
//			El saldo de CXP se calculo en momento de recuperar
//			Aqui se esta restando al saldo calculado el pago actual
			if ((this.cxp.getSaldo().subtract(this.pagoDeta.getTotal())).compareTo(new BigDecimal(0)) == 0 ) {
				cxp.setEstado(true);
			} else {
				cxp.setEstado(false);
			}
			
			cxpRegis.modificar(cxp);
						
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "/ppsj/tesoreria/formPagoMoviIngr/explora?faces-redirect=true&formPagoMoviIngrId=" + this.pagoDeta.getFormPagoMoviIngr().getFpmiId();
	}
	
	public String validarGrabar() {

		String mensaje = "validado";
		BigDecimal totalPago = new BigDecimal(0);
		PagoDeta pagoDeta = new PagoDeta();

		List<PagoDeta> pagoDetas = new ArrayList<>();
		
//		VALIDACION: El pago actual no puede se mayor que el saldo de la CXP
		if (this.pagoDeta.getTotal().compareTo(this.cxp.getSaldo()) == 1) {
			mensaje = "Pago es mayor que saldo del documento";
		}

		Cxp cxp = new Cxp();
		cxp.setIngreso(new Ingreso());
		pagoDeta.setCxp(cxp);
		
		formPagoMoviIngr.setFormPago(new FormPago());
		pagoDeta.setFormPagoMoviIngr(formPagoMoviIngr);
			
//		VALIDACION: El total del pago no puede ser mayor que el total del documento
		try {
			pagoDetas = this.pagoDetaLista.buscar(pagoDeta, null);
			
//			Si se esta modificando el pago
//			Se elimina de la lista para que no le tome en cuenta en el calculo del abono y saldo
//			Para que la validacion tenga el abono y el saldo sin el registro actual
			if (this.id != null) {
				pagoDetas.remove(this.pagoDeta);
			}
			
			for (PagoDeta pagoDetaCxp : pagoDetas) {
				totalPago = totalPago.add(pagoDetaCxp.getTotal());
			}
			
			totalPago = totalPago.add(this.getPagoDeta().getTotal());
				if (totalPago.compareTo(formPagoMoviIngr.getTotal()) == 1) {
				mensaje = "Pago a cuenta por pagar es mayor que valor del documento";
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar detalle de pagos"));
			e.printStackTrace();
		}
		
		return mensaje;
	}
	

	public String modificar() {
		return "/ppsj/tesoreria/pagoDeta/registra?faces-redirect=true&pagoDetaId=" + this.getId();
	}
	
	public String explorar() {
		return "explora?faces-redirect=true&pagoDetaId=" + this.getId();
	}

	public String eliminar() {

//		Variables creada para navegacion entre paginas
		Integer fpmiId = 0;
		
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			
			if (cxp.getEstado() == true) {
				cxp.setEstado(false);
				cxpRegis.modificar(cxp);
			} 

			PagoDeta pagoDeta = pagoDetaRegis.buscarPorId(PagoDeta.class, this.getId());
			fpmiId = pagoDeta.getFormPagoMoviIngr().getFpmiId();
			pagoDetaRegis.eliminar(pagoDeta);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		return "/ppsj/tesoreria/formPagoMoviIngr/explora?faces-redirect=true&formPagoMoviIngrId=" + fpmiId;
	}
	
	public String nuevo() {
		return "/ppsj/tesoreria/pagoDeta/registra?faces-redirect=true&fpmiId=" + this.getFpmiId();
	}
	
//	Este metodo se llama desde la pagina FPMI-explora
	public String seleccionarCxp() {
		return "/ppsj/tesoreria/pagoDeta/registra?faces-redirect=true&fpmiId=" + this.getFpmiId() + "&cxpId=" + this.getCxpId();	
	}

	public List<PagoDeta> buscarTodo() {

		List<PagoDeta> pagoDetas = new ArrayList<>();

		try {
			pagoDetas = pagoDetaLista.buscarTodo("pagoDetaId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return pagoDetas;
	}
	
	public Cxp buscarCxp() {
		Cxp cxp = new Cxp();

		try {
			cxp = this.cxpRegis.buscarPorId(Cxp.class, cxpId);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar cuenta por pagar"));
			e.printStackTrace();

		}

		return cxp;
	}
		
	public FormPagoMoviIngr buscarFormPagoMoviIngr() {
		
		FormPagoMoviIngr formPagoMoviIngr = new FormPagoMoviIngr();
		
		try {
			
			formPagoMoviIngr = formPagoMoviIngrRegis.buscarPorId(FormPagoMoviIngr.class, fpmiId);
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar movimiento ingreso ID"));
			e.printStackTrace();
		}
		return formPagoMoviIngr;
	}
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public PagoDeta getPagoDeta() {
		return pagoDeta;
	}

	public void setPagoDeta(PagoDeta pagoDeta) {
		this.pagoDeta = pagoDeta;
	}

	public List<PagoDeta> getPagoDetas() {
		return pagoDetas;
	}

	public void setPagoDetas(List<PagoDeta> pagoDetas) {
		this.pagoDetas = pagoDetas;
	}

	public Integer getFpmiId() {
		return fpmiId;
	}

	public void setFpmiId(Integer fpmiId) {
		this.fpmiId = fpmiId;
	}

	public Integer getCxpId() {
		return cxpId;
	}

	public void setCxpId(Integer cxpId) {
		this.cxpId = cxpId;
	}

	public Cxp getCxp() {
		return cxp;
	}

	public void setCxp(Cxp cxp) {
		this.cxp = cxp;
	}

	public FormPagoMoviIngr getFormPagoMoviIngr() {
		return formPagoMoviIngr;
	}

	public void setFormPagoMoviIngr(FormPagoMoviIngr formPagoMoviIngr) {
		this.formPagoMoviIngr = formPagoMoviIngr;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}