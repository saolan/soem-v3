package ec.com.tecnointel.soem.reportInve.controlador;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class TomaFisiReportControl extends InventarioModuloControl implements Serializable {

	private static final long serialVersionUID = 2415142739100876860L;
	
	private Integer tomaFisiId;
	private Integer tomaFisiNumero;
	
	@Override
	public void descargar() {

		String nombreReporte = "tomaFisis";
		
		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();
			
			parametrosJasper.put("tomaFisiId", this.tomaFisiId);
			parametrosJasper.put("tomaFisiNumero", this.tomaFisiNumero);
			
			parametrosJasper.put("sucursalIds", this.sucursalIds);			
			
			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper,
					rutaJrxml, rutaReporteCompilado, formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,"Excepcion - Error al descargar reporte de costos y saldos"));
			e.printStackTrace();
		}
	}

	public Integer getTomaFisiId() {
		return tomaFisiId;
	}

	public void setTomaFisiId(Integer tomaFisiId) {
		this.tomaFisiId = tomaFisiId;
	}

	public Integer getTomaFisiNumero() {
		return tomaFisiNumero;
	}

	public void setTomaFisiNumero(Integer tomaFisiNumero) {
		this.tomaFisiNumero = tomaFisiNumero;
	}	
}
