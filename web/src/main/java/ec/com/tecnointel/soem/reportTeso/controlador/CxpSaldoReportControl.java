package ec.com.tecnointel.soem.reportTeso.controlador;

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
public class CxpSaldoReportControl extends CxpModuloControl implements Serializable {

	private static final long serialVersionUID = 929714423296559176L;
	
	@Override
	public void descargar() {

//		Nombre del reporte en JasperReport
		String nombreReporte = "cxpSaldos";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();
			
			parametrosJasper.put("persProvApelliDesd", this.getPersProvDesd().getPersona().getApelli());
			parametrosJasper.put("persProvApelliHast", this.getPersProvHast().getPersona().getApelli());
			
			parametrosJasper.put("persProvCedulaDesd", this.getPersProvDesd().getPersona().getCedulaRuc());
			parametrosJasper.put("persProvCedulaHast", this.getPersProvHast().getPersona().getCedulaRuc());
			
			parametrosJasper.put("ingresoFechaDesd", this.getIngresoDesd().getFechaEmis());
			parametrosJasper.put("ingresoFechaHast", this.getIngresoHast().getFechaEmis());

			parametrosJasper.put("cxpFechaVencDesd", Date.from(this.getCxpDesd().getFechaVenc().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			parametrosJasper.put("cxpFechaVencHast", Date.from(this.getCxpHast().getFechaVenc().atStartOfDay(ZoneId.systemDefault()).toInstant()));

			parametrosJasper.put("saldoMini", this.saldoMini);
			parametrosJasper.put("saldoMaxi", this.saldoMaxi);
			
			parametrosJasper.put("persProvEstado", this.persProvEstado);
			parametrosJasper.put("cxpEstado", this.cxpEstado);

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper,
					rutaJrxml, rutaReporteCompilado, formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,"Excepcion - Error al descargar cuentas por pagar saldos"));
			e.printStackTrace();
		}
	}		
}
