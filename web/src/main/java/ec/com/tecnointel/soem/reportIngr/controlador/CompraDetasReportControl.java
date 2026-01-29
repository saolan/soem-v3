package ec.com.tecnointel.soem.reportIngr.controlador;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import ec.com.tecnointel.soem.general.controlador.VariablesSesion;
import ec.com.tecnointel.soem.parametro.modelo.DocuIngr;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

@Named
@ViewScoped
public class CompraDetasReportControl extends IngresoModuloControl implements Serializable {

	private List<DocuIngr> docuIngrAnexos;

	private static final long serialVersionUID = -545489945228761773L;

//	Se cambia el nombre del metodo porque sino no se ejecuta el postconstructor de la clase que se esta heredando
	@PostConstruct
	public void cargarCompraDetas() {

		this.docuIngrAnexos = new ArrayList<>();

		for (DocuIngr docuIngr : this.docuIngrs) {
			if (docuIngr.isGeneraAnex()) {
				this.docuIngrAnexos.add(docuIngr);
			}
		}
	}

	@Override
	public void descargar() {

		String nombreReporte = "compraDetas";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();

			parametrosJasper.put("ingresoFechaDesd",
					Date.from(this.getIngresoDesd().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			parametrosJasper.put("ingresoFechaHast",
					Date.from(this.getIngresoHast().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));

			parametrosJasper.put("sucursalIds", this.sucursalIds);
			parametrosJasper.put("docuIngrIds", this.docuIngrIds);

			parametrosJasper.put("persProvEstado", this.persProvEstado);

			parametrosJasper.put("rutaJrxml", this.rutaJrxml);

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper, rutaJrxml, rutaReporteCompilado,
					formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al descargar reporte de compras"));
			e.printStackTrace();
		}
	}

	public void descargarFp() {

		String nombreReporte = "compraFormPagos";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();

			parametrosJasper.put("ingresoFechaDesd",
					Date.from(this.getIngresoDesd().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			parametrosJasper.put("ingresoFechaHast",
					Date.from(this.getIngresoHast().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));

			parametrosJasper.put("sucursalIds", this.sucursalIds);
			parametrosJasper.put("docuIngrIds", this.docuIngrIds);

			parametrosJasper.put("persProvEstado", this.persProvEstado);

			parametrosJasper.put("rutaJrxml", this.rutaJrxml);

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper, rutaJrxml, rutaReporteCompilado,
					formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al descargar reporte de compras"));
			e.printStackTrace();
		}
	}

	public ArrayList<String> getDocuIngrIds() {
		return docuIngrIds;
	}

	public void setDocuIngrIds(ArrayList<String> docuIngrIds) {
		this.docuIngrIds = docuIngrIds;
	}

	public List<DocuIngr> getDocuIngrAnexos() {
		return docuIngrAnexos;
	}

	public void setDocuIngrAnexos(List<DocuIngr> docuIngrAnexos) {
		this.docuIngrAnexos = docuIngrAnexos;
	}

	@Resource(mappedName = "java:jboss/datasources/soemDS")
	private DataSource dataSource;

	public void descargarTodo() {

		List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();
		
		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();

			parametrosJasper.put("ingresoFechaDesd",
					Date.from(this.getIngresoDesd().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			parametrosJasper.put("ingresoFechaHast",
					Date.from(this.getIngresoHast().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));

			parametrosJasper.put("sucursalIds", this.sucursalIds);
			parametrosJasper.put("docuIngrIds", this.docuIngrIds);

			parametrosJasper.put("persProvEstado", this.persProvEstado);

			parametrosJasper.put("rutaJrxml", this.rutaJrxml);

			jasperPrints.add(crearJasperPrint("compraDetas", parametrosJasper, rutaJrxml, rutaReporteCompilado,
					formatoReporte));

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al descargar reporte de compras"));
			e.printStackTrace();
		}

		parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();

			parametrosJasper.put("ingresoFechaDesd",
					Date.from(this.getIngresoDesd().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			parametrosJasper.put("ingresoFechaHast",
					Date.from(this.getIngresoHast().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));

			parametrosJasper.put("sucursalIds", this.sucursalIds);
			parametrosJasper.put("docuIngrIds", this.docuIngrIds);

			parametrosJasper.put("persProvEstado", this.persProvEstado);

			parametrosJasper.put("rutaJrxml", this.rutaJrxml);

			jasperPrints.add(crearJasperPrint("compraFormPagos", parametrosJasper, rutaJrxml, rutaReporteCompilado,
					formatoReporte));

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al descargar reporte de compras"));
			e.printStackTrace();
		}

		parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();

			parametrosJasper.put("fechaDesd",
					Date.from(this.getIngresoDesd().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			parametrosJasper.put("fechaHast",
					Date.from(this.getIngresoHast().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));

			parametrosJasper.put("sucursalIds", this.sucursalIds);

			parametrosJasper.put("persProvEstado", this.persProvEstado);

			jasperPrints.add(crearJasperPrint("compraRetes", parametrosJasper, rutaJrxml, rutaReporteCompilado,
					formatoReporte));

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al descargar reporte de compras"));
			e.printStackTrace();
		}
		
		crearArchivo(jasperPrints);
	}

	public JasperPrint crearJasperPrint(String nombreReporte, Map<String, Object> parametrosJasper, String rutaJrxml,
			String rutaReporteCompilado, String formato) {

		String rutaReporteJrxml = rutaJrxml + nombreReporte + ".jrxml";
		String reporteCompilado = nombreReporte + ".jasper";

		Sucursal sucursal = new Sucursal();
		Parametro parametro = new Parametro();

		VariablesSesion variablesSesion = (VariablesSesion) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("variablesSesion");
		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");

		sucursal = variablesSesion.getSucursal();

		Connection connection = null;

		JasperPrint jasperPrint = new JasperPrint();

		try {

			connection = dataSource.getConnection();

			parametrosJasper.put("sucursal", sucursal.getDescri());
			parametrosJasper.put("usuario", persUsuaSesion.getPersona().getNombre());

//			Se usa la entidad parametro para traer todos los valores de la tabla
//			conforme se va buscando se va asignanado
//			para no crear muchas entidades parametro

//			Traer ruta de la plantilla de la tabla parametro
			parametro = parametroRegis.buscarPorId(Parametro.class, 6001);
			parametrosJasper.put("estilo", parametro.getDescri());
			// Traer ruta del logo de la tabla parametro
			parametro = parametroRegis.buscarPorId(Parametro.class, 6002);
			parametrosJasper.put("logoReporte", parametro.getDescri());

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

			// Compila el reporte primer parametro ruta reporte fuente
			// Segundo parametro ruta en la que se va a grabar el reporte compilado
			JasperCompileManager.compileReportToFile(rutaReporteJrxml,
					request.getSession().getServletContext().getRealPath(rutaReporteCompilado + reporteCompilado));

			// Crea un archivo del reporte compilado en el comando anterior
			File file = new File(
					request.getSession().getServletContext().getRealPath(rutaReporteCompilado + reporteCompilado));

			JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(file.getPath());
			jasperPrint = JasperFillManager.fillReport(jasperReport, parametrosJasper, connection);

		} catch (JRException jre) {
			jre.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error en reporte"));
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}

		return jasperPrint;
	}

	public void crearArchivo(List<JasperPrint> jasperPrints) {
		
//		List<JasperPrint> sheets = new ArrayList<JasperPrint>();
//		for (int i=1;i<=8;i++){
//		   JasperPrint print = JasperFillManager.fillReport("subReport_" + i + ".jasper", paramMap, connection);
//		   sheets.add(print); 
//		}
		
//		JRXlsxExporter exporter = new JRXlsxExporter();
//		exporter.setExporterInput(SimpleExporterInput.getInstance(sheets));
//		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new File("text.xlxs"));
//		SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
//		configuration.setSheetNames(sheetNames): //sheets names is an array of the different names.
//		configuration.setOnePagePerSheet(false); //remove that it break on new page
//		configuration.setDetectCellType(true);
//		exporter.setConfiguration(configuration);
//		exporter.exportReport();
		
		// Se añade la extensión al nombre del archivo
//		nombreReporte = nombreReporte + "." + formato;
		String nombreReporte = "anexo" + "." + "xls";
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		JRXlsExporter exporterXLS = new JRXlsExporter();
		exporterXLS.setExporterInput(SimpleExporterInput.getInstance(jasperPrints));
//		exporterXLS.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporterXLS.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
		SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
		configuration.setOnePagePerSheet(false);
		configuration.setDetectCellType(true);
		configuration.setCollapseRowSpan(false);
		exporterXLS.setConfiguration(configuration);
		// exporterXLS.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, true);
		// exporterXLS.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, false);
		try {
			exporterXLS.exportReport();
		} catch (JRException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error en reporte"));
			e.printStackTrace();
		}

		this.abrirReporte(byteArrayOutputStream.toByteArray(), nombreReporte, "application/vnd.ms-excel");

	}

	public void abrirReporte(byte[] reporte, String nombreReporte, String tipoArchivo) {

		FacesContext context = FacesContext.getCurrentInstance();

		try {
			if (reporte != null) {

				HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
				response.setContentType(tipoArchivo);

				try {

					ServletOutputStream ouputStream = response.getOutputStream();

					response.reset();
					response.setDateHeader("Expires", 0);
					response.setContentLength(reporte.length);
					response.setHeader("Pragma", "no-cache");
					response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
//					response.setHeader("Content-disposition", "inline; filename=" + nombreReporte);
					response.setHeader("Content-disposition", "attachment; filename=" + nombreReporte);

					ouputStream.write(reporte, 0, reporte.length);
					ouputStream.flush();
					ouputStream.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			context.responseComplete();
		}
	}

}
