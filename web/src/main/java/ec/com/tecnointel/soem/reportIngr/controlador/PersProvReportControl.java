package ec.com.tecnointel.soem.reportIngr.controlador;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class PersProvReportControl extends IngresoModuloControl implements Serializable {

	private static final long serialVersionUID = 7720105703570082688L;
	
	@Override
	public void descargar() {

//		Nombre del reporte en JasperReport
		String nombreReporte = "persProvs";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();
						
			parametrosJasper.put("persProvApelliDesd", this.getPersProvDesd().getPersona().getApelli());
			parametrosJasper.put("persProvApelliHast", this.getPersProvHast().getPersona().getApelli());
			
			parametrosJasper.put("persProvCedulaDesd", this.getPersProvDesd().getPersona().getCedulaRuc());
			parametrosJasper.put("persProvCedulaHast", this.getPersProvHast().getPersona().getCedulaRuc());
			
			parametrosJasper.put("provGrupIds", this.provGrupIds);
			
			parametrosJasper.put("persProvEstado", this.persProvEstado);

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper,
					rutaJrxml, rutaReporteCompilado, formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,"Excepcion - Error al descargar reporte productos"));
			e.printStackTrace();
		}
	}		

}
