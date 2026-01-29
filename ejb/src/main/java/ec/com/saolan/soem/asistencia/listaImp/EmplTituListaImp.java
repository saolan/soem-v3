package ec.com.saolan.soem.asistencia.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.EmplTituListaInt;
import ec.com.saolan.soem.asistencia.modelo.EmplTitu;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class EmplTituListaImp extends GestorListaSoem<EmplTitu>
		implements
			EmplTituListaInt,
			Serializable {

	private static final long serialVersionUID = 8078037111287023178L;

	@Override
	public List<EmplTitu> buscar(EmplTitu emplTitu, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<EmplTitu> query = builder.createQuery(EmplTitu.class);
		Root<EmplTitu> emplTituRoot = query.from(EmplTitu.class);

		query.orderBy(builder.desc(emplTituRoot.get("emplTituId")));
		TypedQuery<EmplTitu> consulta = this.entityManager.createQuery(query
				.select(emplTituRoot).where(
						getSearchPredicates(emplTituRoot, emplTitu)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(
					filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(EmplTitu emplTitu) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<EmplTitu> emplTituRoot = countQuery.from(EmplTitu.class);

		countQuery = countQuery.select(builder.count(emplTituRoot)).where(
				getSearchPredicates(emplTituRoot, emplTitu));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<EmplTitu> emplTituRoot,
			EmplTitu emplTitu) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}