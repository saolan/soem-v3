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
public class CxcSaldoGrupoReportControl extends CxcModuloControl implements Serializable {
	
	private static final long serialVersionUID = -6238053905471789443L;

	@Override
	public void descargar() {

//		Nombre del reporte en JasperReport
		String nombreReporte = "cxcSaldosGrupos";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();
			
			parametrosJasper.put("clieGrupIds", this.clieGrupIds);
			
			parametrosJasper.put("persClieApelliDesd", this.getPersClieDesd().getPersona().getApelli());
			parametrosJasper.put("persClieApelliHast", this.getPersClieHast().getPersona().getApelli());
			
			parametrosJasper.put("persClieCedulaDesd", this.getPersClieDesd().getPersona().getCedulaRuc());
			parametrosJasper.put("persClieCedulaHast", this.getPersClieHast().getPersona().getCedulaRuc());
			
			parametrosJasper.put("egresoFechaDesd", this.getEgresoDesd().getFechaRegi());
			parametrosJasper.put("egresoFechaHast", this.getEgresoHast().getFechaRegi());

			parametrosJasper.put("cxcFechaVencDesd", Date.from(this.getCxcDesd().getFechaVenc().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			parametrosJasper.put("cxcFechaVencHast", Date.from(this.getCxcHast().getFechaVenc().atStartOfDay(ZoneId.systemDefault()).toInstant()));

			parametrosJasper.put("saldoMini", this.saldoMini);
			parametrosJasper.put("saldoMaxi", this.saldoMaxi);
			
			parametrosJasper.put("persClieEstado", this.persClieEstado);
			parametrosJasper.put("cxpEstado", this.cxcEstado);
			
			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper,
					rutaJrxml, rutaReporteCompilado, formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,"Excepcion - Error al descargar  cuentas por cobrar saldos"));
			e.printStackTrace();
		}
	}		
}
