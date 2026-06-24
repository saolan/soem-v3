package ec.com.saolan.soem.compartido.cache;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import ec.com.saolan.soem.compartido.excepcion.InfraestructuraExcepcion;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.registroInt.ParametroRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

@Singleton
@Startup
@Lock(LockType.READ)
public class ParametroRutaCache implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ParametroRutaCache.class.getName());

	@Inject
	ParametroRegisInt parametroRegis;

	private Parametro rutaDescargados;

	@PostConstruct
	public void inicializar() {
		cargarParametroRuta();
	}

	private void cargarParametroRuta() {
		try {
			rutaDescargados = parametroRegis.buscarPorId(Parametro.class, 4251);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error al cargar parámetros SRI", e);
			throw new InfraestructuraExcepcion("Error al buscar parametros de rutas", e);
		}
	}

	@Lock(LockType.WRITE)
	public void refrescarParametroRuta() {
		cargarParametroRuta();
	}

	public Parametro getRutaDescargados() {
		return rutaDescargados;
	}

	public void setRutaDescargados(Parametro rutaDescargados) {
		this.rutaDescargados = rutaDescargados;
	}
}
