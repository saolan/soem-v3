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
public class VentaProdMMVendReportControl extends EgresoModuloControl implements Serializable {

	private static final long serialVersionUID = 2467459055840892281L;
	
	private String nombreReporte = "ventasProdMMVend";
	private String refere = "cantidad";
	private String orden = "desc";

	private Integer producLimi = 20;

	@Override
	public void descargar() {

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();

			if (refere.equals("cantidad")) {
				this.refere = "egre_deta_cantid";
			} else {
				this.refere = "egre_deta_tota_prec";
			}
			
			parametrosJasper.put("prodCodigoDesd", productoDesd.getCodigo().toLowerCase());
			parametrosJasper.put("prodCodigoHast", productoHast.getCodigo().toLowerCase());

			parametrosJasper.put("prodCodigoBarrDesd", productoDesd.getCodigoBarra().toLowerCase());
			parametrosJasper.put("prodCodigoBarrHast", productoHast.getCodigoBarra().toLowerCase());

			parametrosJasper.put("prodDescriDesd", productoDesd.getDescri().toLowerCase());
			parametrosJasper.put("prodDescriHast", productoHast.getDescri().toLowerCase());

			parametrosJasper.put("producLimi", this.producLimi);
			parametrosJasper.put("refere", this.refere);
			parametrosJasper.put("orden", this.orden);

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

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

	public String getRefere() {
		return refere;
	}

	public void setRefere(String refere) {
		this.refere = refere;
	}

	public Integer getProducLimi() {
		return producLimi;
	}

	public void setProducLimi(Integer producLimi) {
		this.producLimi = producLimi;
	}
}
