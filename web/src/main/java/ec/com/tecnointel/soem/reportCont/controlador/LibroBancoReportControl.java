package ec.com.tecnointel.soem.reportCont.controlador;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.com.tecnointel.soem.parametro.listaInt.FormPagoListaInt;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class LibroBancoReportControl extends ContabilidadModuloControl implements Serializable {

	@Inject
	FormPagoListaInt formPagoLista;

	private PersUsua persUsuaSesion;
	
	private List<FormPago> formPagos;
	private ArrayList<String> formPagoIds;

	private static final long serialVersionUID = -8020153345363859330L;

	// Se cambia el nombre del metodo porque sino no se ejecuta el
	// postconstructor de la clase que se esta heredando
	@PostConstruct
	public void cargarLibroBanco() {

		List<FormPago> formPagoTmps = new ArrayList<>();
		
		this.formPagos = new ArrayList<>();
		
		persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");	

		formPagoTmps = this.buscarFormPagos();
		formPagoTmps = this.filtrarFormPagos(formPagoTmps);
		this.formPagos = this.filtrarBancos(formPagoTmps);
	}

	@Override
	public void descargar() {

		String nombreReporte = "libroBanco";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();

			parametrosJasper.put("rutaJrxml", this.rutaJrxml);

			parametrosJasper.put("fechaDesd", Date.from(this.getTransaccionDesd().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			parametrosJasper.put("fechaHast", Date.from(this.getTransaccionHast().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			
			parametrosJasper.put("formPagoIds", this.formPagoIds);
			// parametrosJasper.put("sucursalIds", this.sucursalIds);
			// parametrosJasper.put("estados", this.estados);

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper, rutaJrxml, rutaReporteCompilado,
					formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al descargar libro diario"));
			e.printStackTrace();
		}
	}

	public List<FormPago> buscarFormPagos() {

		FormPago formPago = new FormPago();

		formPago.setModulo("Compras");
		formPago.setEstado(true);

		List<FormPago> formPagos = new ArrayList<>();

		try {
			formPagos = formPagoLista.buscar(formPago, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar formas de pago"));
			e.printStackTrace();
		}

		return formPagos;

	}
	
	public List<FormPago> filtrarFormPagos(List<FormPago> formPagos) {
		
		try {
			formPagos = this.formPagoLista.filtrarFormPagos(formPagos, persUsuaSesion, variablesSesion.getRolFormPagos());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al filtrar formas de pago"));
			e.printStackTrace();
		}
		
		return formPagos;
	}
	
	public List<FormPago> filtrarBancos(List<FormPago> formPagos) {
		
		List<FormPago> formPagoBancos = new ArrayList<>();
		
		for (FormPago formPago : formPagos) {
			if (formPago.getTipo2().equals("BA")) {
				formPagoBancos.add(formPago);
			}
		}
		
		return formPagoBancos;
	}

	public ArrayList<String> getFormPagoIds() {
		return formPagoIds;
	}

	public void setFormPagoIds(ArrayList<String> formPagoIds) {
		this.formPagoIds = formPagoIds;
	}

	public List<FormPago> getFormPagos() {
		return formPagos;
	}

	public void setFormPagos(List<FormPago> formPagos) {
		this.formPagos = formPagos;
	}
}
