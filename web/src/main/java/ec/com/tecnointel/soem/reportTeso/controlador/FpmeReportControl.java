package ec.com.tecnointel.soem.reportTeso.controlador;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class FpmeReportControl extends CxcModuloControl implements Serializable {

	// Este nombre viene desde la pagina al seleccionar tipo de reporte
	private String nombreReporte = "fpmes";

	private static final long serialVersionUID = -7815530322579152622L;

	@PostConstruct
	public void cargarFpmeReportControl() {
		this.modificarFormPagos();
	}

	@Override
	public void descargar() {

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();

			parametrosJasper.put("persClieApelliDesd", this.getPersClieDesd().getPersona().getApelli());
			parametrosJasper.put("persClieApelliHast", this.getPersClieHast().getPersona().getApelli());

			parametrosJasper.put("persClieCedulaDesd", this.getPersClieDesd().getPersona().getCedulaRuc());
			parametrosJasper.put("persClieCedulaHast", this.getPersClieHast().getPersona().getCedulaRuc());

			parametrosJasper.put("fpmeFechaDesd", Date.from(this.getFpmeDesd().getFecha().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			parametrosJasper.put("fpmeFechaHast", Date.from(this.getFpmeHast().getFecha().atStartOfDay(ZoneId.systemDefault()).toInstant()));

			parametrosJasper.put("formPagoIds", this.formPagoIds);
			parametrosJasper.put("cajaIds", this.cajaIds);
			parametrosJasper.put("estadoDescris", this.estadoDescris);

			parametrosJasper.put("persClieEstado", this.persClieEstado);

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper, rutaJrxml, rutaReporteCompilado,
					formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al descargar pagos"));
			e.printStackTrace();
		}
	}

	public String getNombreReporte() {
		return nombreReporte;
	}

	public void setNombreReporte(String nombreReporte) {
		this.nombreReporte = nombreReporte;
	}

	public void modificarFormPagos() {

		List<FormPago> formPagos = new ArrayList<>(this.formPagos);
//		this.formPagos.clear();

		for (FormPago formPago : formPagos) {
			
			if (formPago.getTipo().equals("CR")) {
				this.formPagos.remove(formPago);
			}
		}
	}
}
