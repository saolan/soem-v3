package ec.com.tecnointel.soem.reportPara.controlador;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.parametro.modelo.Persona;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class PersonaReportControl extends ParametroModuloControl implements Serializable {

	Persona personaDesd = new Persona();
	Persona personaHast = new Persona();
	
	private static final long serialVersionUID = 764904442538683390L;
	
	@PostConstruct
	public void cargar() {
		
		this.personaDesd = new Persona();
		this.personaHast = new Persona();
		
	}
	
	@Override
	public void descargar() {

//		Nombre del reporte en JasperReport
		String nombreReporte = "personas";

		Map<String, Object> parametrosJasper = new HashMap<String, Object>();

		try {
			
			super.descargar();
			
			parametrosJasper.put("personaCedulaDesd", this.getPersonaDesd().getCedulaRuc());
			parametrosJasper.put("personaCedulaHast", this.getPersonaHast().getCedulaRuc());

			parametrosJasper.put("personaApelliDesd", this.getPersonaDesd().getApelli());
			parametrosJasper.put("personaApelliHast", this.getPersonaHast().getApelli());
			
			parametrosJasper.put("personaNombreDesd", this.getPersonaDesd().getApelli());
			parametrosJasper.put("personaNombreHast", this.getPersonaHast().getApelli());
			
			this.verificarParametros();
			
			generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper,
					rutaJrxml, rutaReporteCompilado, formatoReporte);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,"Excepcion - Error al descargar reporte productos"));
			e.printStackTrace();
		}
	}

	public void verificarParametros() {
		
		if (personaDesd.getApelli() == null){
			personaDesd.setApelli("!");
		}
		
		if (personaHast.getApelli() == null){
			personaHast.setApelli("zz");
		}

		if (personaDesd.getNombre() == null){
			personaDesd.setNombre("!");
		}
		
		if (personaHast.getNombre() == null){
			personaHast.setNombre("zz");
		}

		if (personaDesd.getCedulaRuc() == null){ 
			personaDesd.setCedulaRuc("!");       		
		}                                      		
		                                       		
		if (personaHast.getCedulaRuc() == null){ 		
			personaHast.setCedulaRuc("zz");      		
		}                                      		
		                                       		
	}
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER	public Persona getPersonaDesde() {
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>		return personaDesde;
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER	}
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public Persona getPersonaDesd() {
		return personaDesd;
	}

	public void setPersonaDesd(Persona personaDesd) {
		this.personaDesd = personaDesd;
	}

	public Persona getPersonaHast() {
		return personaHast;
	}

	public void setPersonaHast(Persona personaHast) {
		this.personaHast = personaHast;
	}
	
}
