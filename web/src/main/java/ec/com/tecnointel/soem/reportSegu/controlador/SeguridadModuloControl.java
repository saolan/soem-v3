package ec.com.tecnointel.soem.reportSegu.controlador;

import java.io.Serializable;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import jakarta.annotation.PostConstruct;

public class SeguridadModuloControl extends PaginaControl
		implements
			Serializable {

	protected String rutaJrxml;
	protected String rutaReporteCompilado = "\\jasperReportes\\seguridad\\";

	private static final long serialVersionUID = -2132163223226854398L;

	@PostConstruct
	public void cargar() {

	}

	public void descargar() throws Exception {
		// Nombre que esta en archivo fuente en jasperReport
		// String nombreReporte = "productos";
		// Se coloca aqui la ruta para poder tener los reportes en diferentes y
		// varias carpetas
		// Sino se podria poner en la clase GenerarJasperReport
		// String rutaJrxml; //
		// "C:\\tecnoIntel\\soem\\jasperReportes\\ingreso\\";
		// En esta ruta se compila el reporte dentro del servidor de
		// aplicaciones dentro del archivo .EAR .WEB
		// String rutaReporteCompilado = "\\jasperReportes\\inventario\\";
		// Map<String, Object> parametrosJasper = new HashMap<String, Object>();
		// Parametro parametro = new Parametro();
		// try {
		// parametrosJasper.put("productoCodigoDesde",
		// productoDesde.getCodigo().toLowerCase());
		// parametro = parametroRegis.buscarPorId(5000);
		// rutaJrxml = parametro.getDescri();
		// generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper,
		// rutaJrxml, rutaReporteCompilado, formatoReporte);
		// } catch (Exception e) {
		// FacesContext.getCurrentInstance().addMessage(null,
		// new FacesMessage(FacesMessage.SEVERITY_FATAL,
		// null,"Excepcion - Error al descargar reporte productos"));
		// e.printStackTrace();
		// }

		this.verificarParametros();

		Parametro parametro = new Parametro();
		parametro = parametroRegis.buscarPorId(Parametro.class, 7000);
		rutaJrxml = parametro.getDescri();

	}

	public void verificarParametros() {

		// if (ingresoDesde.getCodigoBarra() == null) {
		// ingresoDesde.setCodigoBarra(" ");
		// }
		//
		// if (ingresoHasta.getCodigoBarra() == null) {
		// ingresoHasta.setCodigoBarra("zz");
		// }
	}

	// public void buscarDocuIngrs() {
	//
	// DocuIngr docuIngr = new DocuIngr();
	// docuIngr.setDocumento(new Documento());
	// docuIngr.getDocumento().setEstado(true);
	//
	// try {
	// this.docuIngrs = docuIngrLista.buscar(docuIngr, null);
	// } catch (Exception e) {
	// FacesContext.getCurrentInstance().addMessage(
	// null,
	// new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
	// "Excepcion - Error al buscar tipo de documento"));
	//
	// e.printStackTrace();
	// }
	//
	// }

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
