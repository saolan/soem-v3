package ec.com.tecnointel.soem.general.controlador;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.registroInt.ParametroRegisInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

@Stateless
public class GeneraJasperReportes implements Serializable {

//	@Inject
//	VariablesSesionOld variablesSesion;

	@Inject
	ParametroRegisInt parametroRegis;

	@Resource(mappedName = "java:jboss/datasources/soemDS")
	private DataSource dataSource;

	private static final long serialVersionUID = 6984563067575501189L;

	public void crearArchivo(String nombreReporte, Map<String, Object> parametrosJasper, String rutaJrxml, String rutaReporteCompilado, String formato) {
		
		String rutaReporteJrxml = rutaJrxml + nombreReporte + ".jrxml";
		String reporteCompilado = nombreReporte + ".jasper";

		Sucursal sucursal = new Sucursal();
		Parametro parametro = new Parametro();
		
		VariablesSesion variablesSesion = (VariablesSesion) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("variablesSesion");
		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");
		
		sucursal = variablesSesion.getSucursal();

		Connection connection = null;

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
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametrosJasper, connection);

			if (formato.toLowerCase().equals("pdf")) {

				// Se añade la extensión al nombre del archivo
				nombreReporte = nombreReporte + "." + formato;
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				JRPdfExporter exporterPdf = new JRPdfExporter();
				exporterPdf.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporterPdf.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
				
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				exporterPdf.setConfiguration(configuration);
				exporterPdf.exportReport();
				
				this.abrirReporte(byteArrayOutputStream.toByteArray(), nombreReporte, "application/pdf");

			} else if (formato.toLowerCase().equals("xlsx") || formato.toLowerCase().equals("xls")) {

				// Se añade la extensión al nombre del archivo
				nombreReporte = nombreReporte + "." + formato;
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				JRXlsExporter exporterXLS = new JRXlsExporter();
				exporterXLS.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporterXLS.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
				SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
				configuration.setOnePagePerSheet(false);
				configuration.setDetectCellType(true);
				configuration.setCollapseRowSpan(false);
				exporterXLS.setConfiguration(configuration);
				// exporterXLS.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, true);
				// exporterXLS.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, false);
				exporterXLS.exportReport();

				this.abrirReporte(byteArrayOutputStream.toByteArray(), nombreReporte, "application/vnd.ms-excel");

			}

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

	public Boolean impresionServidor(){
		return true;
	}
	
//	>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//	>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//	>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		
}
