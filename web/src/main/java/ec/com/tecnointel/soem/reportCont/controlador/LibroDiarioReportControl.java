package ec.com.tecnointel.soem.reportCont.controlador;

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
public class LibroDiarioReportControl extends ContabilidadModuloControl implements Serializable {
	
	private static final long serialVersionUID = 8593683554790806151L;
	
	@Override
	public void descargar() {

		String nombreReporte = "libroDiario";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();

			parametrosJasper.put("transaccionFechaDesd", Date.from(this.getTransaccionDesd().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			parametrosJasper.put("transaccionFechaHast", Date.from(this.getTransaccionHast().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));

			parametrosJasper.put("transaccionNumeroDesd", this.getTransaccionDesd().getNumero());
			parametrosJasper.put("transaccionNumeroHast", this.getTransaccionHast().getNumero());
			
			parametrosJasper.put("sucursalIds", this.sucursalIds);
			parametrosJasper.put("docuTranIds", this.docuTranIds);
			parametrosJasper.put("estados", this.estados);

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper,
					rutaJrxml, rutaReporteCompilado, formatoReporte);

		} catch (Exception e) {FacesContext.getCurrentInstance()
					.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
									"Excepcion - Error al descargar libro diario"));
			e.printStackTrace();
		}
	}
}
