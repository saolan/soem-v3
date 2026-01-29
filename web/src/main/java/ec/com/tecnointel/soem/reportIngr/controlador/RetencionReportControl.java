package ec.com.tecnointel.soem.reportIngr.controlador;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class RetencionReportControl extends IngresoModuloControl implements Serializable {

	private static final long serialVersionUID = -3063403190631466196L;

	@Override
	public void descargar() {

		String nombreReporte = "retenciones";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();

			parametrosJasper.put("persProvApelliDesd", this.getPersProvDesd().getPersona().getApelli());
			parametrosJasper.put("persProvApelliHast", this.getPersProvHast().getPersona().getApelli());

			parametrosJasper.put("persProvCedulaDesd", this.getPersProvDesd().getPersona().getCedulaRuc());
			parametrosJasper.put("persProvCedulaHast", this.getPersProvHast().getPersona().getCedulaRuc());

			parametrosJasper.put("fechaDesd", Date.from(this.getIngresoDesd().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			parametrosJasper.put("fechaHast", Date.from(this.getIngresoHast().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));

			parametrosJasper.put("sucursalIds", this.sucursalIds);

			parametrosJasper.put("persProvEstado", this.persProvEstado);

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper, rutaJrxml, rutaReporteCompilado,
					formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al descargar reporte de compras"));
			e.printStackTrace();
		}
	}
}
