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
public class VentaVendReportControl extends EgresoModuloControl implements Serializable {

	private static final long serialVersionUID = 103540141265480834L;

	@Override
	public void descargar() {

		String nombreReporte = "ventasVend";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();

			parametrosJasper.put("persVendApelliDesd", this.getPersVendDesd().getPersona().getApelli());
			parametrosJasper.put("persVendApelliHast", this.getPersVendHast().getPersona().getApelli());

			parametrosJasper.put("persVendCedulaDesd", this.getPersVendDesd().getPersona().getCedulaRuc());
			parametrosJasper.put("persVendCedulaHast", this.getPersVendHast().getPersona().getCedulaRuc());

			parametrosJasper.put("egresoFechaDesd", Date.from(this.getEgresoDesd().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			parametrosJasper.put("egresoFechaHast", Date.from(this.getEgresoHast().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			

			parametrosJasper.put("sucursalIds", this.sucursalIds);
			parametrosJasper.put("docuEgreIds", this.docuEgreIds);

			parametrosJasper.put("persVendEstado", this.persVendEstado);

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper, rutaJrxml, rutaReporteCompilado,
					formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al descargar reporte productos"));
			e.printStackTrace();
		}
	}
}
