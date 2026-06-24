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
public class ParametroConexionSriCache implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ParametroConexionSriCache.class.getName());

	@Inject
	ParametroRegisInt parametroRegis;

	// -- Parámetros SRI --
	private Parametro proxyIp;
	private Parametro proxyPuerto;
	private Parametro urlProduccion;
	private Parametro urlPruebas;

	// -- Parámetros generales --
//	private Parametro filasPagina;

	// -- Parámetros email --
//	private Parametro emailHost;
//	private Parametro emailPuerto;

	@PostConstruct
	public void inicializar() {
		cargarParametroDocuElectronico();
//		cargarParametrosGenerales();
//		cargarParametrosEmail();
	}

	// Métodos privados por grupo, fácil de mantener
	private void cargarParametroDocuElectronico() {
		try {
			proxyIp = parametroRegis.buscarPorId(Parametro.class, 3211);
			proxyPuerto = parametroRegis.buscarPorId(Parametro.class, 3212);
			urlProduccion = parametroRegis.buscarPorId(Parametro.class, 3220);
			urlPruebas = parametroRegis.buscarPorId(Parametro.class, 3221);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error al buscar parametros de conexión al SRI", e);
			throw new InfraestructuraExcepcion("Error al buscar parametros de conexión al SRI", e);
		}
	}

//	private void cargarParametrosGenerales() {
//		try {
//			filasPagina = parametroRegis.buscarPorId(Parametro.class, 6100);
//		} catch (Exception e) {
//			LOGGER.log(Level.SEVERE, "Error al cargar parámetros generales", e);
//		}
//	}

//	private void cargarParametrosEmail() {
//		try {
//			emailHost = parametroRegis.buscarPorId(Parametro.class, 5100);
//			emailPuerto = parametroRegis.buscarPorId(Parametro.class, 5101);
//		} catch (Exception e) {
//			LOGGER.log(Level.SEVERE, "Error al cargar parámetros email", e);
//		}
//	}

	// Refresco por grupo o total
	@Lock(LockType.WRITE)
	public void refrescarSri() {
		cargarParametroDocuElectronico();
	}

	@Lock(LockType.WRITE)
	public void refrescarTodo() {
		inicializar();
	}

	// Getters
	public Parametro getProxyIp() {
		return proxyIp;
	}

	public Parametro getProxyPuerto() {
		return proxyPuerto;
	}

	public Parametro getUrlProduccion() {
		return urlProduccion;
	}

	public Parametro getUrlPruebas() {
		return urlPruebas;
	}
}
