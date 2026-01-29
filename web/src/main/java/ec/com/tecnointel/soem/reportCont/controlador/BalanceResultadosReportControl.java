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
public class BalanceResultadosReportControl extends ContabilidadModuloControl implements Serializable {

	private static final long serialVersionUID = -4826309421876575096L;

	@Override
	public void descargar() {

		String nombreReporte = "balanceResultados";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();
			
			parametrosJasper.put("rutaJrxml", rutaJrxml);

			parametrosJasper.put("fechaDesd", Date.from(this.getTransaccionDesd().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			parametrosJasper.put("fechaHast", Date.from(this.getTransaccionHast().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));

			parametrosJasper.put("planCuenNiveId", this.planCuenNiveId);

//			parametrosJasper.put("sucursalIds", this.sucursalIds);

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper,
					rutaJrxml, rutaReporteCompilado, formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
				null,new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al descargar balance de resultados"));
			e.printStackTrace();
		}
	}
}
