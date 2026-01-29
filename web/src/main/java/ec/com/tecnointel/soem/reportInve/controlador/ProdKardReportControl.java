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
public class ProdKardReportControl extends InventarioModuloControl implements Serializable {

	private static final long serialVersionUID = -7682762920676898700L;

	@PostConstruct
	public void cargarProdKardReportControl() {
		this.prodGrup.setTipo("Mercaderia");
		this.buscarProdGrupos();
	}

	@Override
	public void descargar() {

		String nombreReporte = "prodKards";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();
			
			parametrosJasper.put("prodCodigoDesd", productoDesd.getCodigo().toLowerCase());
			parametrosJasper.put("prodCodigoHast", productoHast.getCodigo().toLowerCase());

			parametrosJasper.put("prodCodigoBarrDesd", productoDesd.getCodigoBarra().toLowerCase());
			parametrosJasper.put("prodCodigoBarrHast", productoHast.getCodigoBarra().toLowerCase());

			parametrosJasper.put("prodDescriDesd", productoDesd.getDescri().toLowerCase());
			parametrosJasper.put("prodDescriHast", productoHast.getDescri().toLowerCase());

			parametrosJasper.put("sucursalIds", this.sucursalIds);
			parametrosJasper.put("prodGrupIds", this.prodGrupIds);
			
			parametrosJasper.put("prodEstado", this.prodEstado);
			
			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper,
					rutaJrxml, rutaReporteCompilado, formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,"Excepcion - Error al descargar kardex"));
			e.printStackTrace();
		}
	}
}
