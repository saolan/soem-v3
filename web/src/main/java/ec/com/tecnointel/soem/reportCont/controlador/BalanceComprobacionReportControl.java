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
public class BalanceComprobacionReportControl extends ContabilidadModuloControl implements Serializable {

	private static final long serialVersionUID = 5966250406505351056L;

	@Override
	public void descargar() {

		String nombreReporte = "balanceComprobacion";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();
			
			parametrosJasper.put("rutaJrxml", rutaJrxml);
			
//			Se toma el valor de this.getTransaccionDesd().getFechaEmis() 
//			pero solo por estandarizar la captura de parametros con los otros reportes de contabilidad
//			El valor para el reporte se toma de tran_deta.fecha_emis			
			parametrosJasper.put("fechaDesd", Date.from(this.getTransaccionDesd().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			parametrosJasper.put("fechaHast", Date.from(this.getTransaccionHast().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));

//			Para el balance de comprobación se controla los niveles en la opcion 
//			Print When Expresion de jasperReport no en la consulta
			parametrosJasper.put("planCuenNiveId", this.planCuenNiveId);
			
//			parametrosJasper.put("planCuenDesd", this.getPlanCuenDesd().getCodigo());
//			parametrosJasper.put("planCuenHast", this.getPlanCuenHast().getCodigo());

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper,
					rutaJrxml, rutaReporteCompilado, formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al descargar balance de comprobación"));
			e.printStackTrace();
		}
	}
}
