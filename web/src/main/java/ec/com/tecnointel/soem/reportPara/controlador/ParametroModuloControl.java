package ec.com.tecnointel.soem.reportPara.controlador;

import java.io.Serializable;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import jakarta.annotation.PostConstruct;

public class ParametroModuloControl extends PaginaControl implements Serializable {

	protected String rutaJrxml;
	protected String rutaReporteCompilado = "\\jasperReportes\\parametro\\";

	private static final long serialVersionUID = -5463297274774183431L;

	@PostConstruct
	public void cargar() {

	}

	public void descargar() throws Exception {

		this.verificarParametros();

		Parametro parametro = new Parametro();
		parametro = parametroRegis.buscarPorId(Parametro.class, 6000);
		rutaJrxml = parametro.getDescri();

	}

	public void verificarParametros() {
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
