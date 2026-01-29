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
public class AnexoAnulsReportControl extends IngresoModuloControl implements Serializable {

	private static final long serialVersionUID = 943304491065273360L;

	@Override
	public void descargar() {

		String nombreReporte = "anexoAnuls";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();

			parametrosJasper.put("ingresoFechaDesd", Date.from(this.getIngresoDesd().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			parametrosJasper.put("ingresoFechaHast", Date.from(this.getIngresoHast().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));

			// parametrosJasper.put("sucursalIds", this.sucursalIds);
			// parametrosJasper.put("docuIngrIds", this.docuIngrIds);

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper, rutaJrxml, rutaReporteCompilado,
					formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al descargar reporte de compras"));
			e.printStackTrace();
		}
	}
}
