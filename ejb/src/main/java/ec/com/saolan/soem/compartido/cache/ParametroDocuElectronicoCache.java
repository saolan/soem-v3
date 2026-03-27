package ec.com.saolan.soem.compartido.cache;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

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
public class ParametroDocuElectronicoCache implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ParametroDocuElectronicoCache.class.getName());

	@Inject
	ParametroRegisInt parametroRegis;

	// -- Parámetros SRI --
	private Parametro proxyIp;
	private Parametro proxyPuerto;
	private Parametro urlProduccion;
	private Parametro urlPruebas;
	private Parametro rutaDescargados;

	// -- Parámetros generales --
//	private Parametro filasPagina;

	// -- Parámetros email --
//	private Parametro emailHost;
//	private Parametro emailPuerto;

	@PostConstruct
	public void inicializar() {
		cargarParametrosSri();
//		cargarParametrosGenerales();
//		cargarParametrosEmail();
	}

	// Métodos privados por grupo, fácil de mantener
	private void cargarParametrosSri() {
		try {
			proxyIp = parametroRegis.buscarPorId(Parametro.class, 3211);
			proxyPuerto = parametroRegis.buscarPorId(Parametro.class, 3212);
			urlProduccion = parametroRegis.buscarPorId(Parametro.class, 3220);
			urlPruebas = parametroRegis.buscarPorId(Parametro.class, 3221);
			rutaDescargados = parametroRegis.buscarPorId(Parametro.class, 4251);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error al cargar parámetros SRI", e);
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
		cargarParametrosSri();
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

	public Parametro getRutaDescargados() {
		return rutaDescargados;
	}

//	public Parametro getFilasPagina() {
//		return filasPagina;
//	}

//	public Parametro getEmailHost() {
//		return emailHost;
//	}
//
//	public Parametro getEmailPuerto() {
//		return emailPuerto;
//	}
}
