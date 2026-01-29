package ec.com.tecnointel.soem.general.util;

import ec.com.tecnointel.soem.general.interfac.EntityManagerSoem;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class ProducerEntityManagerSoem {

	@Produces
	@EntityManagerSoem
	@PersistenceContext(unitName = "soemPU")
	private EntityManager entityManager;

}
