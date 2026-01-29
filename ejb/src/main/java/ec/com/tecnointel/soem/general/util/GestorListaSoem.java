package ec.com.tecnointel.soem.general.util;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public abstract class GestorListaSoem<T> implements Serializable {

	private static final long serialVersionUID = -4528601166664785916L;

	protected int filasPagina;

	protected String orden = "Asc";
	
	@PersistenceContext(unitName = "soemPU")
	protected EntityManager entityManager;

	private Class<T> entityClass;

	public GestorListaSoem() {
	}

	// Busca todos los registros ordenados por alguna columna
	// Se usa para llenar combos
	public List<T> buscarTodo(String columna) throws Exception {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(entityClass);
		Root<T> root = query.from(entityClass);

		query.orderBy(builder.asc(root.get(columna)));
		TypedQuery<T> consulta = this.entityManager.createQuery(query.select(root));

		return consulta.getResultList();
	}

	public void filasPagina(int filas){
		this.filasPagina = filas;	
	}
	
	public void orden(String orden){
		this.orden = orden;	
	}

}