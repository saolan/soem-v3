package ec.com.tecnointel.soem.reportEgre.controlador;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.com.tecnointel.soem.parametro.modelo.DocuEgre;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class VentaCliesReportControl extends EgresoModuloControl implements Serializable {

	private List<DocuEgre> docuEgreAnexos;

	private static final long serialVersionUID = 5052698414770891324L;

	// Se cambia el nombre del metodo porque sino no se ejecuta el postconstructor
	// de la clase que se esta heredando
	@PostConstruct
	public void cargarVentaClies() {

		this.docuEgreAnexos = new ArrayList<>();

		for (DocuEgre docuEgre : this.docuEgres) {
			if (docuEgre.isGeneraAnex()) {
				this.docuEgreAnexos.add(docuEgre);
			}
		}
	}

	@Override
	public void descargar() {

		String nombreReporte = "ventaClies";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {

			super.descargar();

			parametrosJasper.put("egresoFechaDesd", Date.from(this.getEgresoDesd().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			parametrosJasper.put("egresoFechaHast", Date.from(this.getEgresoHast().getFechaEmis().atStartOfDay(ZoneId.systemDefault()).toInstant()));

			parametrosJasper.put("cajaIds", this.cajaIds);
			parametrosJasper.put("docuEgreIds", this.docuEgreIds);
			
			parametrosJasper.put("persClieEstado", this.persClieEstado);

			parametrosJasper.put("rutaJrxml", this.rutaJrxml);

			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper, rutaJrxml, rutaReporteCompilado,
					formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al descargar reporte productos"));
			e.printStackTrace();
		}
	}

	public List<DocuEgre> getDocuEgreAnexos() {
		return docuEgreAnexos;
	}

	public void setDocuEgreAnexos(List<DocuEgre> docuEgreAnexos) {
		this.docuEgreAnexos = docuEgreAnexos;
	}
}
