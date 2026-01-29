package ec.com.saolan.soem.asistencia.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.DepaContListaInt;
import ec.com.saolan.soem.asistencia.modelo.DepaCont;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class DepaContListaImp extends GestorListaSoem<DepaCont>
		implements
			DepaContListaInt,
			Serializable {

	private static final long serialVersionUID = 1666173898490896423L;

	@Override
	public List<DepaCont> buscar(DepaCont depaCont, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<DepaCont> query = builder.createQuery(DepaCont.class);
		Root<DepaCont> depaContRoot = query.from(DepaCont.class);

		query.orderBy(builder.desc(depaContRoot.get("depaContId")));
		TypedQuery<DepaCont> consulta = this.entityManager.createQuery(query
				.select(depaContRoot).where(
						getSearchPredicates(depaContRoot, depaCont)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(
					filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(DepaCont depaCont) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<DepaCont> depaContRoot = countQuery.from(DepaCont.class);

		countQuery = countQuery.select(builder.count(depaContRoot)).where(
				getSearchPredicates(depaContRoot, depaCont));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<DepaCont> depaContRoot,
			DepaCont depaCont) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}