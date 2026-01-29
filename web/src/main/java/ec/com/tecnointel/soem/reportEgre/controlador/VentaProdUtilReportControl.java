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
public class VentaProdUtilReportControl extends EgresoModuloControl implements Serializable {

	// Este nombre viene desde la pagina al seleccionar tipo de reporte
	private String nombreReporte = "ventasProdUtilDeta";

	private Double cantidMini = -5000d;
	private Double cantidMaxi = 50000d;

	private static final long serialVersionUID = -6715711553621636396L;

	@Override
	public void descargar() {

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();

			parametrosJasper.put("prodCodigoDesd", productoDesd.getCodigo().toLowerCase());
			parametrosJasper.put("prodCodigoHast", productoHast.getCodigo().toLowerCase());

			parametrosJasper.put("prodCodigoBarrDesd", productoDesd.getCodigoBarra().toLowerCase());
			parametrosJasper.put("prodCodigoBarrHast", productoHast.getCodigoBarra().toLowerCase());

			parametrosJasper.put("prodDescriDesd", productoDesd.getDescri().toLowerCase());
			parametrosJasper.put("prodDescriHast", productoHast.getDescri().toLowerCase());

			parametrosJasper.put("cantidMini", this.cantidMini);
			parametrosJasper.put("cantidMaxi", this.cantidMaxi);

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

	public Double getCantidMini() {
		return cantidMini;
	}

	public void setCantidMini(Double cantidMini) {
		this.cantidMini = cantidMini;
	}

	public Double getCantidMaxi() {
		return cantidMaxi;
	}

	public void setCantidMaxi(Double cantidMaxi) {
		this.cantidMaxi = cantidMaxi;
	}
}
