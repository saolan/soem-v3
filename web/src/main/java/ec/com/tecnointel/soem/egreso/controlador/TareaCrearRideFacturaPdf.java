package ec.com.tecnointel.soem.egreso.controlador;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import javax.sql.DataSource;

import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.general.controlador.VariablesSesion;
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

public class TareaCrearRideFacturaPdf extends TimerTask {

	private Egreso egreso;
	private ParametroRegisInt parametroRegis;
	private DimmRegisInt dimmRegis;
	private DataSource dataSource;
	private VariablesSesion variablesSesion;
	private HttpServletRequest request;
	
	public TareaCrearRideFacturaPdf(Egreso egreso, ParametroRegisInt parametroRegis, DimmRegisInt dimmRegis,
			DataSource dataSource, VariablesSesion variablesSesion, HttpServletRequest request) {
		this.egreso = egreso;
		this.parametroRegis = parametroRegis;
		this.dimmRegis = dimmRegis;
		this.dataSource = dataSource;
		this.variablesSesion = variablesSesion;
		this.request = request;
	}

    @Override
    public void run() {
        crearRideFacturaPdf();
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
    
	public void crearRideFacturaPdf() {

		String nombreReporte = this.egreso.getDocuEgre().getForma2(); //"rideFactura";
		String rutaReporteCompilado = "\\jasperReportes\\egreso\\";
		String rutaJrxml = null;

		Parametro parametro = new Parametro();
		
		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		Connection connection = null;

		parametrosJasper.put("egresoId", this.egreso.getEgresoId());

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3000);
			rutaJrxml = parametro.getDescri();
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Error al buscar parametro 3000 - rutaJrxml"));
		}

		try {
			
			connection = dataSource.getConnection();

			parametrosJasper.put("sucursal", variablesSesion.getSucursal().getDescri());
			parametrosJasper.put("usuario", variablesSesion.getPersUsua().getPersona().getNombre());

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
//			parametrosJasper.put("egreDetaImpuDescri12", dimm.getDescri().trim());
//			dimm = dimmRegis.buscarPorId(13040);
//			parametrosJasper.put("egreDetaImpuDescri0", dimm.getDescri().trim());

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
			JasperExportManager.exportReportToPdfFile(jasperPrint, variablesSesion.getRutaAutorizados() + this.egreso.getClaveAcce() + ".pdf");
			
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
//    private void completeTask() {
//    	System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ejecutando tarea" + this.egreso.getAutori());
//        try {
//            //assuming it takes 20 secs to complete the task
//        	System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ejecutando tarea");
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

}
