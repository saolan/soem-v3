package ec.com.tecnointel.soem.reportInve.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
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
 * En esta clase se selecciona un precio al que se quiere valorar el inventario
 * y las sucursales necesarias
 *
 */

@Named
@ViewScoped
public class ProdCostSaldReportControl extends InventarioModuloControl implements Serializable {
	
	private static final long serialVersionUID = -3480135831609384509L;

	@PostConstruct
	public void cargarProdCostSaldReportControl() {
		
		this.prodGrup.setTipo("Mercaderia");
		this.buscarProdGrupos();
	}
	
	@Override
	public void descargar() {

		String nombreReporte = "prodCostSalds";
		
		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();
			
			parametrosJasper.put("prodCodigoDesd", productoDesd.getCodigo().toLowerCase());
			parametrosJasper.put("prodCodigoHast", productoHast.getCodigo().toLowerCase());

			parametrosJasper.put("prodCodigoBarrDesd", productoDesd.getCodigoBarra().toLowerCase());
			parametrosJasper.put("prodCodigoBarrHast", productoHast.getCodigoBarra().toLowerCase());

			parametrosJasper.put("prodDescriDesd", productoDesd.getDescri().toLowerCase());
			parametrosJasper.put("prodDescriHast", productoHast.getDescri().toLowerCase());

			parametrosJasper.put("ktvCantidMini", this.ktvCantidMini);
			parametrosJasper.put("ktvCantidMaxi", this.ktvCantidMaxi);

			parametrosJasper.put("precioId", this.precioId);
			
			parametrosJasper.put("sucursalIds", this.sucursalIds);
			parametrosJasper.put("prodGrupIds", this.prodGrupIds);
			
			parametrosJasper.put("valoraInve", this.valoraInve);
			parametrosJasper.put("prodEstado", this.prodEstado);
			
			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper,
					rutaJrxml, rutaReporteCompilado, formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,"Excepcion - Error al descargar reporte de costos y saldos"));
			e.printStackTrace();
		}
	}
	
//	Este procedimiento tambien esta igual el que esta en ProdPrecReportControl 
//	pero aqui se suprimie algunas lineas de codigo
	public void buscarRolPrecs(){
		
		RolPrec rolPrec = new RolPrec();
		
		List<RolSucu> rolSucus = new ArrayList<>();
		List<RolPrec> rolPrecs = new ArrayList<>();
		
		Set<Sucursal> sucursals = new HashSet<>();
		Set<Integer> rols = new HashSet<>();
		
		rolSucus = this.buscarRolSucus();
		
		for (RolSucu rolSucu : rolSucus) {
			rols.add(rolSucu.getRol().getRolId());
			sucursals.add(rolSucu.getSucursal());
		}
		
		List<Integer> rols1 = new ArrayList<>(rols);
		
		rolPrec.setAcceso(true);
		for (Sucursal sucursal : sucursals) {
			try {
				rolPrecs.addAll(rolPrecLista.buscar(sucursal, rols1, rolPrec));
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Excepción - Error al buscar rol-precio"));
				e.printStackTrace();
			}
		}
				
//		for (int i = 0; i < rolPrecs.size(); i++) {
//			rolPrecs.get(i).setRol(new Rol());
//		}
//		this.rolPrecs = new HashSet<>(rolPrecs);
		
//		this.precios.clear();
		for (RolPrec rolPrecFiltro : rolPrecs) {

//			this.sucursals.add(rolPrecFiltro.getSucursal());
			this.precios.add(rolPrecFiltro.getPrecio());
			
		}
	}
}
