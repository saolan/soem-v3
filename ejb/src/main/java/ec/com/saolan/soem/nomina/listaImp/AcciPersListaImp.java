package ec.com.saolan.soem.nomina.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.nomina.listaInt.AcciPersListaInt;
import ec.com.saolan.soem.nomina.modelo.AcciPers;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class AcciPersListaImp extends GestorListaSoem<AcciPers>
		implements
			AcciPersListaInt,
			Serializable {

	private static final long serialVersionUID = -7358259394221499937L;

	@Override
	public List<AcciPers> buscar(AcciPers acciPers, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<AcciPers> query = builder.createQuery(AcciPers.class);
		Root<AcciPers> acciPersRoot = query.from(AcciPers.class);

		query.orderBy(builder.desc(acciPersRoot.get("acciPersId")));
		TypedQuery<AcciPers> consulta = this.entityManager.createQuery(query
				.select(acciPersRoot).where(
						getSearchPredicates(acciPersRoot, acciPers)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(
					filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(AcciPers acciPers) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<AcciPers> acciPersRoot = countQuery.from(AcciPers.class);

		countQuery = countQuery.select(builder.count(acciPersRoot)).where(
				getSearchPredicates(acciPersRoot, acciPers));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<AcciPers> acciPersRoot,
			AcciPers acciPers) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}