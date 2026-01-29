package ec.com.tecnointel.soem.general.util;

import java.io.Serializable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Transactional
public abstract class GestorRegisSoem<T> implements Serializable {

	private static final long serialVersionUID = -7890045488569183413L;

	@PersistenceContext(unitName = "soemPU")
	protected EntityManager entityManager;

	// Si se usa este metodo hay que implementar el constructor en las clases
	// que heredan esta
	// private Class<T> entityClass;
	// public ManagerCrudSoem(Class<T> entityClass) {
	// this.entityClass = entityClass;
	// }

//	private Class<T> entityClass;

	public GestorRegisSoem() {
	}

	// Retorna en ID que se esta generando al momento de persistir
	// Se usa este metodo a cambio del metodo public String insertarDatos(T
	// entidad) {}
//	@AuditInterceptorInt
	public Object insertar(T entidad) {
		this.entityManager.persist(entidad);
		Object idEntidad = entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entidad);
		return idEntidad;
	}

	// Please note: I'm using explicit transaction handling here
	// (entityManager.getTransaction().begin()).
	// The reason is that I'm not using an EJB container anymore. If you are
	// using EJB you can remove the transaction handling statements.
	//
	public void insertarDatos(T entidad) {
		// entityManager.getTransaction().begin();
		this.entityManager.persist(entidad);
		// entityManager.getTransaction().commit();
		// this.entityManager.flush();
		// this.entityManager.refresh(entidad);
	}

	public void modificar(T entidad) {
		this.entityManager.merge(entidad);
	}

//	@AuditInterceptorInt
	public void eliminar(T entidad) {
		this.entityManager.remove(this.entityManager.merge(entidad));
	}

	// Para Utilizar esta opcion hay que implemntar el constructor de la clase
//	public T buscarPorId(Integer id) {
//		return entityManager.find(entityClass, id);
//	}

//	 Otra Opcion para buscar por Id con dos parametros Entidad y ID
	public T buscarPorId(Class<?> entidad, Integer id) {
		return (T) entityManager.find(entidad, id);
	}

	// Retorna una lista con los Id de todas las entidades
	// public List<Object> getEntityIdentifiers(List<T> entidades) {
	// PersistenceUnitUtil persistenceUnitUtil =
	// entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
	// List<Object> entidadIds = new ArrayList<Object>();
	// for (T entidad : entidades) {
	// entidadIds.add(persistenceUnitUtil.getIdentifier(entidad));
	// }
	//
	// return entidadIds;
	// }
}
