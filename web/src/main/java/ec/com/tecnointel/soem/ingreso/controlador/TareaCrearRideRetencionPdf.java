package ec.com.tecnointel.soem.ingreso.controlador;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import javax.sql.DataSource;

import ec.com.tecnointel.soem.general.controlador.VariablesSesion;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.registroInt.DimmRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.ParametroRegisInt;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

public class TareaCrearRideRetencionPdf extends TimerTask {

	private Object object;

//	private Retencion retencion;	
	private ParametroRegisInt parametroRegis;
	private DataSource dataSource;
	private VariablesSesion variablesSesion;
	private HttpServletRequest request;

	private DimmRegisInt dimmRegis;
	
//	public TareaCrearRideRetencionPdf(Retencion retencion, ParametroRegisInt parametroRegis,
//			DataSource dataSource, VariablesSesion variablesSesion, HttpServletRequest request) {
//		this.retencion = retencion;
//		this.parametroRegis = parametroRegis;
//		this.dataSource = dataSource;
//		this.variablesSesion = variablesSesion;
//		this.request = request;
//	}
	
	public TareaCrearRideRetencionPdf(Object object, ParametroRegisInt parametroRegis, DimmRegisInt dimmRegis,
			DataSource dataSource, VariablesSesion variablesSesion, HttpServletRequest request) {
		this.object = object;
		this.parametroRegis = parametroRegis;
		this.dimmRegis = dimmRegis;
		this.dataSource = dataSource;
		this.variablesSesion = variablesSesion;
		this.request = request;
	}
	
	@Override
	public void run() {
		this.crearRideRetencionPdf();
	}
	
    public Dimm buscarDimmIva () {

    	Dimm dimm = new Dimm();
    	
		try {
			dimm = dimmRegis.buscarPorId(Dimm.class, variablesSesion.getDimmIdIvaActual());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error no se encuentra Dimm Iva Predeterminado"));
			e.printStackTrace();
		}
		
		return dimm;
    }

	public void crearRideRetencionPdf() {
		
		Retencion retencion = new Retencion();
		Ingreso ingreso = new Ingreso();
		
		String nombreReporte = null;
		String rutaReporteCompilado = "\\jasperReportes\\ingreso\\";
		String rutaJrxml = null;

		Parametro parametro = new Parametro();

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		Connection connection = null;

		String claveAcce = null;
		
		if(this.object instanceof Retencion) {
			retencion = (Retencion) object;
						
			try {
				parametro = parametroRegis.buscarPorId(Parametro.class, 104007);
				nombreReporte = parametro.getDescri();
			} catch (Exception e) {
				e.printStackTrace();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
						"Error al buscar parametro 4000 - Nombre de reporte Retención"));
			}
			
			parametrosJasper.put("retencionId",retencion.getRetencionId());
			claveAcce = retencion.getClaveAcce();
		}

		if(this.object instanceof Ingreso) {

			ingreso = (Ingreso) object;
			nombreReporte = ingreso.getDocuIngr().getForma2();
			
			parametrosJasper.put("ingresoId",ingreso.getIngresoId());
			claveAcce = ingreso.getClaveAcce();
		}

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 4000);
			rutaJrxml = parametro.getDescri();
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Error al buscar parametro 4000 - rutaJrxml"));
		}

		try {
			
			connection = dataSource.getConnection();

//			Se usa la entidad parametro para traer todos los valores de la tabla
//			conforme se va buscando se va asignanado para no crear muchas entidades parametro
		
//			Traer ruta de la plantilla de la tabla parametro
			parametro = parametroRegis.buscarPorId(Parametro.class, 6001);
			parametrosJasper.put("estilo", parametro.getDescri());
//			Traer ruta del logo de la tabla parametro
			parametro = parametroRegis.buscarPorId(Parametro.class, 6002);
			parametrosJasper.put("logoReporte", parametro.getDescri());
//			Buscar variables iva para poder separar los subtotales e imprimir en la factura los valores por separado
//			dimm = dimmRegis.buscarPorId(13030);
//			parametrosJasper.put("ingrDetaImpuDescri12", dimm.getDescri().trim());
//			dimm = dimmRegis.buscarPorId(13040);
//			parametrosJasper.put("ingrDetaImpuDescri0", dimm.getDescri().trim());

			Dimm dimmIvaPredet = buscarDimmIva();
//			Envia como parametro el porcentaje predeterminado actual 
			parametrosJasper.put("dimmIvaPorcen", dimmIvaPredet.getPorcen());

			parametrosJasper.put("rutaJrxml", rutaJrxml);
			
//			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//			HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
									
			JasperPrint jasperPrint = new JasperPrint();
						
			String rutaReporteJrxml = rutaJrxml + nombreReporte + ".jrxml";
			String reporteCompilado = nombreReporte + ".jasper";

			jasperPrint = this.crearJasperPrint(this.request, rutaReporteJrxml, rutaReporteCompilado, reporteCompilado,
						parametrosJasper, connection);
			
//			Descarga el reporte directamente en la ruta especificada
			JasperExportManager.exportReportToPdfFile(jasperPrint, variablesSesion.getRutaAutorizados() + claveAcce + ".pdf");
			
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

	public JasperPrint crearJasperPrint(HttpServletRequest request, String rutaReporteJrxml, String rutaReporteCompilado, 
			String reporteCompilado, Map<String, Object> parametrosJasper, Connection connection) throws JRException{
	
		JasperCompileManager.compileReportToFile(rutaReporteJrxml,
				request.getSession().getServletContext().getRealPath(rutaReporteCompilado + reporteCompilado));

		File file = new File(
				request.getSession().getServletContext().getRealPath(rutaReporteCompilado + reporteCompilado));

		JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(file.getPath());
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametrosJasper, connection);

		return jasperPrint;
	}
	
	
}
