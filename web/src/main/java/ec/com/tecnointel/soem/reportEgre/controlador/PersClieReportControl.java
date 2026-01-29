package ec.com.tecnointel.soem.reportEgre.controlador;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class PersClieReportControl extends EgresoModuloControl implements Serializable {

	private static final long serialVersionUID = -319741433574696801L;

	@Override
	public void descargar() {

//		Nombre del reporte en JasperReport
		String nombreReporte = "persClies";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();
			
			parametrosJasper.put("persClieApelliDesd", this.getPersClieDesd().getPersona().getApelli());
			parametrosJasper.put("persClieApelliHast", this.getPersClieHast().getPersona().getApelli());
			
			parametrosJasper.put("persClieCedulaDesd", this.getPersClieDesd().getPersona().getCedulaRuc());
			parametrosJasper.put("persClieCedulaHast", this.getPersClieHast().getPersona().getCedulaRuc());
			
			parametrosJasper.put("clieGrupIds", this.clieGrupIds);
			
			parametrosJasper.put("persClieEstado", this.persClieEstado);

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper,
					rutaJrxml, rutaReporteCompilado, formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,"Excepcion - Error al descargar reporte productos"));
			e.printStackTrace();
		}
	}	
}
