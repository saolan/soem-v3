package ec.com.tecnointel.soem.reportInve.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

/**
 * @author Drosan
 *
 * En esta clase se selecciona una sucursal y luego se despliega las lista de precio necesaria
 *
 */

@Named
@ViewScoped
public class ProdPrecReportControl extends InventarioModuloControl implements Serializable {

	private static final long serialVersionUID = -1787115473197728195L;

	@PostConstruct
	public void cargarProdCostSaldReportControl() {
		
		this.prodGrup.setTipo(null);
		this.buscarProdGrupos();
	}
	
	@Override
	public void descargar() {

//		Nombre del reporte en JasperReport
		String nombreReporte = "prodPrecs";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();
			
			parametrosJasper.put("prodCodigoDesd", productoDesd.getCodigo().toLowerCase());
			parametrosJasper.put("prodCodigoHast", productoHast.getCodigo().toLowerCase());

			parametrosJasper.put("prodCodigoBarrDesd", productoDesd.getCodigoBarra().toLowerCase());
			parametrosJasper.put("prodCodigoBarrHast", productoHast.getCodigoBarra().toLowerCase());

			parametrosJasper.put("prodDescriDesd", productoDesd.getDescri().toLowerCase());
			parametrosJasper.put("prodDescriHast", productoHast.getDescri().toLowerCase());

			parametrosJasper.put("sucursalId", this.getSucursal().getSucursalId());
			
			parametrosJasper.put("precioIds", this.precioIds);
			parametrosJasper.put("prodGrupIds", this.prodGrupIds);

			
			parametrosJasper.put("prodEstado", this.prodEstado);
			
			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper,
					rutaJrxml, rutaReporteCompilado, formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,"Excepcion - Error al descargar reporte de precios"));
			e.printStackTrace();
		}
	}
	
//	Este procedimiento es igual el que esta en ProdCostSaldReportControl 
//	pero alla se suprime algunas lineas de codigo
	public void buscarRolPrecs(){
		
		RolPrec rolPrec = new RolPrec();
		
		List<RolPrec> rolPrecs = new ArrayList<>();
		
		Set<Integer> rols = new HashSet<>();
		
		for (RolSucu rolSucu : rolSucus) {
			rols.add(rolSucu.getRol().getRolId());
		}
		
		List<Integer> rols1 = new ArrayList<>(rols);
		
		rolPrec.setAcceso(true);
			try {
				rolPrecs.addAll(rolPrecLista.buscar(this.getSucursal(), rols1, rolPrec));
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepción - Error al buscar rol-precio"));
				e.printStackTrace();
			}
		
		this.precios.clear();
		for (RolPrec rolPrecFiltro : rolPrecs) {

			this.precios.add(rolPrecFiltro.getPrecio());
			
		}
	}	
}
