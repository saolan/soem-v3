package ec.com.tecnointel.soem.reportInve.controlador;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ProductoReportControl extends InventarioModuloControl implements Serializable {

	private static final long serialVersionUID = -1080678850757963323L;

	@PostConstruct
	public void cargarProductoReportControl() {
		
		this.prodGrup.setTipo(null);
		this.buscarProdGrupos();
	}
	
	@Override
	public void descargar() {

//		Nombre del reporte en JasperReport
		String nombreReporte = "productos";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();
			
			parametrosJasper.put("prodCodigoDesd", productoDesd.getCodigo().toLowerCase());
			parametrosJasper.put("prodCodigoHast", productoHast.getCodigo().toLowerCase());

			parametrosJasper.put("prodCodigoBarrDesd", productoDesd.getCodigoBarra().toLowerCase());
			parametrosJasper.put("prodCodigoBarrHast", productoHast.getCodigoBarra().toLowerCase());

			parametrosJasper.put("prodDescriDesd", productoDesd.getDescri().toLowerCase());
			parametrosJasper.put("prodDescriHast", productoHast.getDescri().toLowerCase());

			parametrosJasper.put("prodGrupIds", this.prodGrupIds);
			
			parametrosJasper.put("prodEstado", this.prodEstado);
			
			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper,
					rutaJrxml, rutaReporteCompilado, formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,"Excepcion - Error al descargar reporte productos"));
			e.printStackTrace();
		}
	}
}
