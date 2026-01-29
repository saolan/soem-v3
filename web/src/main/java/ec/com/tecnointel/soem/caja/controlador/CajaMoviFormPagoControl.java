package ec.com.tecnointel.soem.caja.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.caja.listaInt.CajaMoviFormPagoListaInt;
import ec.com.tecnointel.soem.caja.modelo.Caja;
import ec.com.tecnointel.soem.caja.modelo.CajaMovi;
import ec.com.tecnointel.soem.caja.modelo.CajaMoviFormPago;
import ec.com.tecnointel.soem.caja.modelo.PersCaje;
import ec.com.tecnointel.soem.caja.registroInt.CajaMoviFormPagoRegisInt;
import ec.com.tecnointel.soem.caja.registroInt.CajaMoviRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.listaInt.FormPagoListaInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuCaja;
import ec.com.tecnointel.soem.parametro.modelo.DocuMoviEgre;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviEgre;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class CajaMoviFormPagoControl extends PaginaControl implements Serializable {

	private Integer cajaMoviId; 
	private CajaMoviFormPago cajaMoviFormPago;
	
	private PersUsua persUsuaSesion;

	private List<CajaMoviFormPago> cajaMoviFormPagos;

	// Combos
	private List<FormPago> formPagos;
	private List<FormPago> formPagoTmps;
	private List<CajaMovi> cajaMovis;	
	
	@Inject
	CajaMoviFormPagoRegisInt cajaMoviFormPagoRegis;

	@Inject
	CajaMoviFormPagoListaInt cajaMoviFormPagoLista;
	
	@Inject
	FormPagoListaInt formPagoLista;
	
//	@Inject
//	CajaMoviListaInt cajaMoviLista;

	@Inject
	CajaMoviRegisInt cajaMoviRegis;

	private static final long serialVersionUID = 5837734475248335456L;

	@PostConstruct
	public void cargar() {
		
		this.persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");
		
		cajaMoviFormPago = new CajaMoviFormPago();
		cajaMoviFormPago.setCajaMovi(new CajaMovi());
		cajaMoviFormPago.setFormPago(new FormPago());

		this.rolPermiso = variablesSesion.getRolPermiso();
		
		this.formPagos = new ArrayList<>();
		
	}

	public void buscar() {

		try {
			
			cajaMoviFormPagoLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.cajaMoviFormPagos = cajaMoviFormPagoLista.buscar(cajaMoviFormPago, this.pagina);
			this.numeroReg = cajaMoviFormPagos.size();
			this.contadorReg = cajaMoviFormPagoLista.contarRegistros(cajaMoviFormPago);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public void recuperar() {

//		List<FormPago> formaPagos = new ArrayList<>();
				
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}
		
 		this.formPagoTmps = this.buscarFormPagos();
		this.filtrarFormPagoVentas();
		this.formPagoTmps = new ArrayList<>(this.formPagos);
		this.formPagos.clear();

		// Poner solo FP y AN, quitar CR y NC
		for (FormPago formPago : formPagoTmps) {
			if (formPago.getTipo().equals("FP") || formPago.getTipo().equals("RR") || formPago.getTipo().equals("RI")) {
				this.formPagos.add(formPago);
			}
		}

		if (this.id == null) {
			
			this.cajaMoviFormPago = new CajaMoviFormPago();
			this.cajaMoviFormPago.setCajaMovi(new CajaMovi());
			this.cajaMoviFormPago.setFormPago(new FormPago());

		} else {

			try {
				this.cajaMoviFormPago = cajaMoviFormPagoRegis.buscarPorId(CajaMoviFormPago.class, this.getId());
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

		CajaMovi cajaMovi = new CajaMovi();
		
		try {
			if (this.id == null) {
				
				cajaMovi = this.buscarCajaMovi();
				this.cajaMoviFormPago.setCajaMovi(cajaMovi);
				Object id = cajaMoviFormPagoRegis.insertar(cajaMoviFormPago);
				this.id = (Integer) id;
			} else {
				cajaMoviFormPagoRegis.modificar(cajaMoviFormPago);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

//		return "explora?faces-redirect=true&cmfpId=" + this.getId();
//		return "/ppsj/caja/cajaMovi/explora?faces-redirect=true";
		return "/ppsj/caja/cajaMovi/explora?faces-redirect=true&cajaMoviId=" + this.getCajaMoviId();
	}

	public String modificar() {
		if (this.getCajaMoviId() != null){
			return "/ppsj/caja/cajaMoviFormPago/registra?faces-redirect=true&cmfpId=" + this.getId() + "&cajaMoviId=" + this.getCajaMoviId();	
		} else {
			return "/ppsj/caja/cajaMoviFormPago/registra?faces-redirect=true&cmfpId=" + this.getId();	
		}
	}

	public String explorar() {
		return "explora?faces-redirect=true&cmfpId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			CajaMoviFormPago cajaMoviFormPago = cajaMoviFormPagoRegis.buscarPorId(CajaMoviFormPago.class, this.getId());
			cajaMoviFormPagoRegis.eliminar(cajaMoviFormPago);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		return "/ppsj/caja/cajaMovi/explora?faces-redirect=true&cajaMoviId=" + this.getCajaMoviId();
	}
	
	public void filtrarFormPagoVentas () {
		
		FormPagoMoviEgre formPagoMoviEgre = new FormPagoMoviEgre();
		formPagoMoviEgre.setDocuMoviEgre(new DocuMoviEgre());
		formPagoMoviEgre.getDocuMoviEgre().setTipo("PAGO-COBRO");

		try {
			this.formPagos = formPagoLista.filtrarFormPagoVentas(formPagoMoviEgre, formPagoTmps, persUsuaSesion, variablesSesion.getRolFormPagos());
		} catch (Exception e) {
			
//			Si el usario no tiene acceso al documento predeterminado da este error ya que el filtro de formas de pago
//			se hace de acuerdo al documento seleccionado o predeterminado 
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, null, "Excepcion - Error al filtrar formas de pago, No tiene acceso a documento predeterminado"));
			e.printStackTrace();
		}
		
		formPagoMoviEgre.getDocuMoviEgre().setTipo("DEPOSITO");
		try {
			
			List<FormPago> formPagos = formPagoLista.filtrarFormPagoVentas(formPagoMoviEgre, formPagoTmps, persUsuaSesion, variablesSesion.getRolFormPagos());
			this.formPagos.addAll(formPagos);
			
		} catch (Exception e) {
			
//			Si el usario no tiene acceso al documento predeterminado da este error ya que el filtro de formas de pago
//			se hace de acuerdo al documento seleccionado o predeterminado 
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, null, "Excepcion - Error al filtrar formas de pago, No tiene acceso a documento predeterminado"));
			e.printStackTrace();
		}

	}
	
	public String nuevo() {
		return "/ppsj/caja/cajaMoviFormPago/registra?faces-redirect=true&cajaMoviId=" + this.getCajaMoviId();
	}

	public List<CajaMoviFormPago> buscarTodo() {

		List<CajaMoviFormPago> cajaMoviFormPagos = new ArrayList<>();

		try {
			cajaMoviFormPagos = cajaMoviFormPagoLista.buscarTodo("cmfpId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return cajaMoviFormPagos;
	}

	public CajaMovi buscarCajaMovi() {
		CajaMovi cajaMovi = new CajaMovi();
		try {
			cajaMovi = cajaMoviRegis.buscarPorId(CajaMovi.class, cajaMoviId);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar movimiento caja ID"));
			e.printStackTrace();
		}
		return cajaMovi;
	}
	
	public List<FormPago> buscarFormPagos() {

		FormPago formPago = new FormPago();
		formPago.setEstado(true);

		List<FormPago> formPagos = new ArrayList<>();

		try {
			formPagos = formPagoLista.buscar(formPago, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar forma de pago"));
			e.printStackTrace();
		}

		return formPagos;

	}
	
//	public List<FormPago> buscarFormPagos() {
//		
//		List<FormPago> formPagos = new ArrayList<>();
//
//		FormPago formPago = new FormPago();
//		Parametro parametro = new Parametro();
//		
//		try {
//			parametro = parametroRegis.buscarPorId(6051);
//		} catch (Exception e1) {
//			FacesContext.getCurrentInstance().addMessage(null,
//					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar forma pago del modulo caja-ventas"));
//			e1.printStackTrace();
//		}
//		
//		formPago.setModulo(parametro.getDescri());
//		formPago.setEstado(true);
//		
//		try {
//			formPagos = formPagoLista.buscar(formPago, null);
//		} catch (Exception e) {
//			FacesContext.getCurrentInstance().addMessage(null,
//					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar forma de pago"));
//			e.printStackTrace();
//		}
//		
//		return formPagos;
//
//	}
	
	public void buscarCajaMovis() {
	
		try {
			
//			Se inicializa estas variables porque el procedimiento cajaMoviLista.buscar 
//			tambien se llama desde otro lugar y requiere estos parametros para la busqueda
//			Sino se inicializa da nullPointerException
//			Se llama desde: cajaMoviControl
			
			PersCaje persCaje = new PersCaje();
			persCaje.setPersona(new Persona());
			
			DocuCaja docuCaja = new DocuCaja();
			docuCaja.setDocumento(new Documento());
			
			this.getCajaMoviFormPago().getCajaMovi().setDocuCaja(docuCaja);
			this.getCajaMoviFormPago().getCajaMovi().setPersCaje(persCaje);
			this.getCajaMoviFormPago().getCajaMovi().setCaja(new Caja());
			
//			this.cajaMovis = cajaMoviLista.buscar(this.getCajaMoviFormPago().getCajaMovi(), null);
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar forma de pago"));
			e.printStackTrace();
		}
		
	}
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public CajaMoviFormPago getCajaMoviFormPago() {
		return cajaMoviFormPago;
	}

	public void setCajaMoviFormPago(CajaMoviFormPago cajaMoviFormPago) {
		this.cajaMoviFormPago = cajaMoviFormPago;
	}

	public List<CajaMoviFormPago> getCajaMoviFormPagos() {
		return cajaMoviFormPagos;
	}

	public void setCajaMoviFormPagos(List<CajaMoviFormPago> cajaMoviFormPagos) {
		this.cajaMoviFormPagos = cajaMoviFormPagos;
	}

	public List<FormPago> getFormPagos() {
		return formPagos;
	}

	public void setFormPagos(List<FormPago> formPagos) {
		this.formPagos = formPagos;
	}

	public Integer getCajaMoviId() {
		return cajaMoviId;
	}

	public void setCajaMoviId(Integer cajaMoviId) {
		this.cajaMoviId = cajaMoviId;
	}

	public List<CajaMovi> getCajaMovis() {
		return cajaMovis;
	}

	public void setCajaMovis(List<CajaMovi> cajaMovis) {
		this.cajaMovis = cajaMovis;
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