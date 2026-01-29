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
public class FpmiReportControl extends CxpModuloControl implements Serializable {
	
	// Este nombre viene desde la pagina al seleccionar tipo de reporte
	private String nombreReporte = "fpmisDeta";

	private static final long serialVersionUID = -1303086932506393044L;

	@Override
	public void descargar() {

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();
			
			parametrosJasper.put("persProvApelliDesd", this.getPersProvDesd().getPersona().getApelli());
			parametrosJasper.put("persProvApelliHast", this.getPersProvHast().getPersona().getApelli());
			
			parametrosJasper.put("persProvCedulaDesd", this.getPersProvDesd().getPersona().getCedulaRuc());
			parametrosJasper.put("persProvCedulaHast", this.getPersProvHast().getPersona().getCedulaRuc());
					
			parametrosJasper.put("fpmiFechaDesd", Date.from(this.getFpmiDesd().getFecha().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			parametrosJasper.put("fpmiFechaHast", Date.from(this.getFpmiHast().getFecha().atStartOfDay(ZoneId.systemDefault()).toInstant()));

			parametrosJasper.put("formPagoIds", this.formPagoIds);
			parametrosJasper.put("estadoDescris", this.estadoDescris);
			
			parametrosJasper.put("persProvEstado", this.persProvEstado);

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper,
					rutaJrxml, rutaReporteCompilado, formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,"Excepcion - Error al descargar pagos"));
			e.printStackTrace();
		}
	}

	public String getNombreReporte() {
		return nombreReporte;
	}

	public void setNombreReporte(String nombreReporte) {
		this.nombreReporte = nombreReporte;
	}	
}
