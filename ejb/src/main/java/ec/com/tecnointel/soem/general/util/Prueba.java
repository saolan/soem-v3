package ec.com.tecnointel.soem.general.util;

import java.util.logging.Logger;

import ec.com.tecnointel.soem.general.interfac.EntityManagerSoem;
import ec.com.tecnointel.soem.general.interfac.LoggerSoem;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

public class Prueba {

	@Inject
	@EntityManagerSoem
	private EntityManager entityManager;

	@Inject
	@LoggerSoem
	Logger logger;

	public void mensage() {
		logger.info("Prueba mensaje logger");
	}

}
