package ec.com.tecnointel.soem.reportEgre.controlador;

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
public class VentaReteCodiImpuReportControl extends EgresoModuloControl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public void descargar() {

		String nombreReporte = "ventasReteCodiImpus";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();

			parametrosJasper.put("fechaDesd",
					Date.from(this.getEgresoDesd().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			parametrosJasper.put("fechaHast",
					Date.from(this.getEgresoHast().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));

			parametrosJasper.put("sucursalIds", this.sucursalIds);

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper, rutaJrxml, rutaReporteCompilado,
					formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al descargar reporte de compras"));
			e.printStackTrace();
		}
	}
}