package ec.com.tecnointel.soem.tesoreria.controlardor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.tesoreria.listaInt.CobrDetaListaInt;
import ec.com.tecnointel.soem.tesoreria.modelo.CobrDeta;
import ec.com.tecnointel.soem.tesoreria.modelo.Cxc;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviEgre;
import ec.com.tecnointel.soem.tesoreria.registroInt.CobrDetaRegisInt;
import ec.com.tecnointel.soem.tesoreria.registroInt.CxcRegisInt;
import ec.com.tecnointel.soem.tesoreria.registroInt.FormPagoMoviEgreRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * @author SandroLand
 *
 * Esta clase ya no se usa porque se unio todo en FormPagoMoviEgreControl
 *
 */
@Named
@ViewScoped
public class CobrDetaControl extends PaginaControl implements Serializable {

	private Integer cxcId;
	private Integer fpmeFormPagoId;
	private Integer fpmeId;
	
	private CobrDeta cobrDeta;
	private Cxc cxc;
	private FormPagoMoviEgre formPagoMoviEgre;

	private List<CobrDeta> cobrDetas;

	@Inject
	CobrDetaRegisInt cobrDetaRegis;

	@Inject
	CobrDetaListaInt cobrDetaLista;

	@Inject
	CxcRegisInt cxcRegis;

	@Inject
	FormPagoMoviEgreRegisInt formPagoMoviEgreRegis;

	private static final long serialVersionUID = -1000249712505966383L;

	@PostConstruct
	public void cargar() {

		cobrDeta = new CobrDeta();

		Cxc cxc = new Cxc();
		Egreso egreso = new Egreso();
		cxc.setEgreso(egreso);
		cobrDeta.setCxc(cxc);

		Persona persona = new Persona();
		FormPagoMoviEgre formPagoMoviEgre = new FormPagoMoviEgre();
		formPagoMoviEgre.setPersona(persona);

		this.rolPermiso = variablesSesion.getRolPermiso();

	}

	public void buscar() {

		try {

			cobrDetaLista.filasPagina(variablesSesion.getFilasPagina());

			this.cobrDetas = cobrDetaLista.buscar(cobrDeta, this.pagina);
			this.numeroReg = cobrDetas.size();
			this.contadorReg = cobrDetaLista.contarRegistros(cobrDeta);
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

			// Buscar CXP y FPME ya que un detalle de cobro siempre debe tener
			// estas dos relaciones
			this.cxc = this.buscarCxc();
			this.formPagoMoviEgre = this.buscarFormPagoMoviEgre();

			this.cobrDeta.setCxc(cxc);

		} else {

			try {

				this.cobrDeta = cobrDetaRegis.buscarPorId(CobrDeta.class, this.getId());
				// Asigna CXC y FPME a entidades separadas para calcular abonos,
				// saldos y realizar validaciones
				this.cxc = cobrDeta.getCxc();

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
				e.printStackTrace();
			}
		}

		// Buscar pagos de CXC para calcular abonos y saldo
		BigDecimal totalAbono = new BigDecimal(0);

		// Se envia como parametro de busqueda
		CobrDeta cobrDeta = new CobrDeta();

		List<CobrDeta> cobrDetas = new ArrayList<>();

		FormPagoMoviEgre formPagoMoviEgre = new FormPagoMoviEgre();
		formPagoMoviEgre.setPersona(new Persona());
		cobrDeta.setCxc(this.cxc);

		try {
			cobrDetas = this.cobrDetaLista.buscar(cobrDeta, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar detalle de pagos"));
			e.printStackTrace();
		}

		// Si se esta modificando el pago se elimina de la lista para que 
		// no le tome en cuenta en el calculo del abono y saldo
		// Para que la validacion tenga el abono y el saldo sin el registro
		// actual
		if (this.id != null) {
			cobrDetas.remove(this.cobrDeta);
		}

		for (CobrDeta cobrDetaCxc : cobrDetas) {
			totalAbono = totalAbono.add(cobrDetaCxc.getTotal());
		}

		// Calcula abono y saldo de CXP
		this.cxc.setAbono(totalAbono);
		this.cxc.setSaldo(this.cxc.getTotal().subtract(totalAbono));
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

				Object id = cobrDetaRegis.insertar(cobrDeta);
				this.id = (Integer) id;

			} else {

				cobrDetaRegis.modificar(cobrDeta);
			}

			// El saldo de CXC se calculo en momento de recuperar
			// Aqui se esta restando al saldo calculado el pago actual
			if ((this.cxc.getSaldo().subtract(this.cobrDeta.getTotal())).compareTo(new BigDecimal(0)) == 0) {
				cxc.setEstado(true);
			} else {
				cxc.setEstado(false);
			}

			cxcRegis.modificar(cxc);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

//		return "/ppsj/tesoreria/formPagoMoviEgre/explora?faces-redirect=true&formPagoMoviEgreId="
//				+ this.cobrDeta.getFormPagoMoviEgre().getFpmeId();
		
		return null;
	}

	public String validarGrabar() {

		String mensaje = "validado";
		BigDecimal totalPago = new BigDecimal(0);
		CobrDeta cobrDeta = new CobrDeta();

		List<CobrDeta> cobrDetas = new ArrayList<>();

		// VALIDACION: El pago actual no puede se mayor que el saldo de la CXP
		if (this.cobrDeta.getTotal().compareTo(this.cxc.getSaldo()) == 1) {
			mensaje = "Pago es mayor que saldo del documento";
		}

		Cxc cxc = new Cxc();
		cxc.setEgreso(new Egreso());
		cobrDeta.setCxc(cxc);

		// VALIDACION: El total del pago no puede ser mayor que el total del
		// documento
		try {
			cobrDetas = this.cobrDetaLista.buscar(cobrDeta, null);

			// Si se esta modificando el pago se elimina de la lista para que 
			// no le tome en cuenta en el calculo del abono y saldo
			// Para que la validacion tenga el abono y el saldo sin el registro
			// actual
			if (this.id != null) {
				cobrDetas.remove(this.cobrDeta);
			}

			for (CobrDeta cobrDetaCxc : cobrDetas) {
				totalPago = totalPago.add(cobrDetaCxc.getTotal());
			}

			totalPago = totalPago.add(this.getCobrDeta().getTotal());
			if (totalPago.compareTo(formPagoMoviEgre.getTotal()) == 1) {
				mensaje = "Pago a cuenta por pagar es mayor que valor del documento";
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar detalle de pagos"));
			e.printStackTrace();
		}

		return mensaje;
	}

	public String modificar() {
		return "/ppsj/tesoreria/cobrDeta/registra?faces-redirect=true&cobrDetaId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&cobrDetaId=" + this.getId();
	}

	public String eliminar() {

		// Variables creada para navegacion entre paginas
		Integer fpmeId = 0;

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {

			if (cxc.getEstado() == true) {
				cxc.setEstado(false);
				cxcRegis.modificar(cxc);
			}

			CobrDeta cobrDeta = cobrDetaRegis.buscarPorId(CobrDeta.class, this.getId());
			cobrDetaRegis.eliminar(cobrDeta);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		return "/ppsj/tesoreria/formPagoMoviEgre/explora?faces-redirect=true&formPagoMoviEgreId=" + fpmeId;
	}

	public String nuevo() {
		return "/ppsj/tesoreria/cobrDeta/registra?faces-redirect=true&fpmeId=" + this.getFpmeId();
	}

	// Este metodo se llama desde la pagina FPMI-explora
	public String seleccionarCxc() {
		return "/ppsj/tesoreria/cobrDeta/registra?faces-redirect=true&fpmeId=" + this.getFpmeId() + "&cxcId="
				+ this.getCxcId();
	}

	public List<CobrDeta> buscarTodo() {

		List<CobrDeta> cobrDetas = new ArrayList<>();

		try {
			cobrDetas = cobrDetaLista.buscarTodo("cobrDetaId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return cobrDetas;
	}

	public Cxc buscarCxc() {
		Cxc cxc = new Cxc();

		try {
			cxc = this.cxcRegis.buscarPorId(Cxc.class, cxcId);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar cuenta por pagar"));
			e.printStackTrace();

		}

		return cxc;
	}

	public FormPagoMoviEgre buscarFormPagoMoviEgre() {

		FormPagoMoviEgre formPagoMoviEgre = new FormPagoMoviEgre();

		try {

			formPagoMoviEgre = formPagoMoviEgreRegis.buscarPorId(FormPagoMoviEgre.class, fpmeId);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar movimiento egreso ID"));
			e.printStackTrace();
		}
		return formPagoMoviEgre;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public CobrDeta getCobrDeta() {
		return cobrDeta;
	}

	public void setCobrDeta(CobrDeta cobrDeta) {
		this.cobrDeta = cobrDeta;
	}

	public List<CobrDeta> getCobrDetas() {
		return cobrDetas;
	}

	public void setCobrDetas(List<CobrDeta> cobrDetas) {
		this.cobrDetas = cobrDetas;
	}

	public Integer getFpmeId() {
		return fpmeId;
	}

	public void setFpmeId(Integer fpmeId) {
		this.fpmeId = fpmeId;
	}

	public Integer getCxcId() {
		return cxcId;
	}

	public void setCxcId(Integer cxcId) {
		this.cxcId = cxcId;
	}

	public Cxc getCxc() {
		return cxc;
	}

	public void setCxc(Cxc cxc) {
		this.cxc = cxc;
	}

	public FormPagoMoviEgre getFormPagoMoviEgre() {
		return formPagoMoviEgre;
	}

	public void setFormPagoMoviEgre(FormPagoMoviEgre formPagoMoviEgre) {
		this.formPagoMoviEgre = formPagoMoviEgre;
	}

	public Integer getFpmeFormPagoId() {
		return fpmeFormPagoId;
	}

	public void setFpmeFormPagoId(Integer fpmeFormPagoId) {
		this.fpmeFormPagoId = fpmeFormPagoId;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}