package ec.com.tecnointel.soem.reportCaja.controlador;

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
public class CajaMoviReportControl extends CajaModuloControl implements Serializable {

	private static final long serialVersionUID = -7228251438932512927L;
	
	@Override
	public void descargar() {

//		Nombre del reporte en JasperReport
		String nombreReporte = "cajaMovis";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();

			parametrosJasper.put("cajaMoviFechaDesd", Date.from(this.cajaMoviDesde.getFecha().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			parametrosJasper.put("cajaMoviFechaHast", Date.from(this.cajaMoviHasta.getFecha().atStartOfDay(ZoneId.systemDefault()).toInstant()));

			parametrosJasper.put("docuCajaIds", this.docuCajaIds);
			parametrosJasper.put("cajaIds", this.cajaIds);
			parametrosJasper.put("persCajeIds", this.persCajeIds);
			parametrosJasper.put("formPagoIds", this.formPagoIds);
			
			parametrosJasper.put("cajaEstado", this.cajaEstado);
						
			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper,
					rutaJrxml, rutaReporteCompilado, formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,"Excepcion - Error al descargar reporte productos"));
			e.printStackTrace();
		}
	}

}
