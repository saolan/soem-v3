package ec.com.tecnointel.soem.reportCont.controlador;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuen;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class LibroMayorReportControl extends ContabilidadModuloControl implements Serializable {

	private boolean seleccionPlanCuenDesd;
	private boolean seleccionPlanCuenHast;
	
	private static final long serialVersionUID = 5152439132789680035L;

	@Override
	public void descargar() {

		String nombreReporte = "libroMayor";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();

			parametrosJasper.put("rutaJrxml", this.rutaJrxml);
			
			parametrosJasper.put("planCuenDesd", this.getPlanCuenDesd().getCodigo());
			parametrosJasper.put("planCuenHast", this.getPlanCuenHast().getCodigo());

			parametrosJasper.put("transaccionFechaDesd", Date.from(this.getTransaccionDesd().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			parametrosJasper.put("transaccionFechaHast", Date.from(this.getTransaccionHast().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			
			parametrosJasper.put("sucursalIds", this.sucursalIds);

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper,
					rutaJrxml, rutaReporteCompilado, formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
							null,new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
									"Excepcion - Error al descargar libro mayor"));
			e.printStackTrace();
		}
	}
	
	public void onRowSelect(SelectEvent<?> event) {

		if (this.seleccionPlanCuenDesd) {
			this.setPlanCuenDesd((PlanCuen) event.getObject());
		}
		
		if (this.seleccionPlanCuenHast) {
			this.setPlanCuenHast((PlanCuen) event.getObject());
		}

		this.seleccionPlanCuenDesd = false;
		this.seleccionPlanCuenHast = false;

		PrimeFaces.current().executeScript("PF('seleccionCuentaDialogo').hide();");
	}


	public void buscarPlanCuenDesd() {
		this.seleccionPlanCuenDesd = true;
		this.seleccionPlanCuenHast = false;
	}

	public void buscarPlanCuenHast() {
		this.seleccionPlanCuenHast = true;
		this.seleccionPlanCuenDesd = false;
	}
}
