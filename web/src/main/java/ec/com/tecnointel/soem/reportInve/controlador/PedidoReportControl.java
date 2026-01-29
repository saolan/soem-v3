package ec.com.tecnointel.soem.reportInve.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.com.tecnointel.soem.ingreso.listaInt.PersProvListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.PersProv;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
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
public class PedidoReportControl extends InventarioModuloControl implements Serializable {

	private static final long serialVersionUID = 18429978350645557L;

	private ArrayList<String> persProvIds;
	
	private List<PersProv> persProvs;
	
	@Inject
	PersProvListaInt persProvLista;

	@PostConstruct
	public void cargarPedidoReportControl() {

		this.buscarPersProvs();
		
	}
	
	@Override
	public void descargar() {

		String nombreReporte = "pedidos";
		
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
			parametrosJasper.put("persProvIds", this.persProvIds);
						
			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper,
					rutaJrxml, rutaReporteCompilado, formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,"Excepcion - Error al descargar reporte de costos y saldos"));
			e.printStackTrace();
		}
	}

	public void buscarPersProvs() {

		try {
			
			PersProv persProv = new PersProv();
			persProv.setPersona(new Persona());
			persProv.setEstado(true);
			
			persProvLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.persProvs = persProvLista.buscar(persProv, null);
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public List<PersProv> getPersProvs() {
		return persProvs;
	}

	public void setPersProvs(List<PersProv> persProvs) {
		this.persProvs = persProvs;
	}

	public ArrayList<String> getPersProvIds() {
		return persProvIds;
	}

	public void setPersProvIds(ArrayList<String> persProvIds) {
		this.persProvIds = persProvIds;
	}
}
