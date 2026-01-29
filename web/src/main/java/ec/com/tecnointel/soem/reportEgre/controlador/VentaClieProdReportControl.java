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
public class VentaClieProdReportControl extends EgresoModuloControl implements Serializable {

	private static final long serialVersionUID = 6491791662777181109L;
	
	private String nombreReporte = "ventasClieProdDeta";

	@Override
	public void descargar() {

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();
			
			parametrosJasper.put("clieGrupIds", this.clieGrupIds);
			
			parametrosJasper.put("persClieApelliDesd", this.getPersClieDesd().getPersona().getApelli());
			parametrosJasper.put("persClieApelliHast", this.getPersClieHast().getPersona().getApelli());
			
			parametrosJasper.put("persClieCedulaDesd", this.getPersClieDesd().getPersona().getCedulaRuc());
			parametrosJasper.put("persClieCedulaHast", this.getPersClieHast().getPersona().getCedulaRuc());

			parametrosJasper.put("prodCodigoBarrDesd", productoDesd.getCodigoBarra().toLowerCase());
			parametrosJasper.put("prodCodigoBarrHast", productoHast.getCodigoBarra().toLowerCase());

			parametrosJasper.put("prodDescriDesd", productoDesd.getDescri().toLowerCase());
			parametrosJasper.put("prodDescriHast", productoHast.getDescri().toLowerCase());

			parametrosJasper.put("egresoFechaDesd", Date.from(this.getEgresoDesd().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			parametrosJasper.put("egresoFechaHast", Date.from(this.getEgresoHast().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));

			parametrosJasper.put("sucursalIds", this.sucursalIds);

			parametrosJasper.put("docuEgreIds", this.docuEgreIds);

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper, rutaJrxml, rutaReporteCompilado,
					formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al descargar reporte productos"));
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
