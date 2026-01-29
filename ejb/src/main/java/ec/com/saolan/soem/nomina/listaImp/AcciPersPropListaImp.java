package ec.com.saolan.soem.nomina.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.nomina.listaInt.AcciPersPropListaInt;
import ec.com.saolan.soem.nomina.modelo.AcciPersProp;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class AcciPersPropListaImp extends GestorListaSoem<AcciPersProp>
		implements
			AcciPersPropListaInt,
			Serializable {

	private static final long serialVersionUID = -4885964573182578446L;

	@Override
	public List<AcciPersProp> buscar(AcciPersProp acciPersProp, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<AcciPersProp> query = builder
				.createQuery(AcciPersProp.class);
		Root<AcciPersProp> acciPersPropRoot = query.from(AcciPersProp.class);

		query.orderBy(builder.desc(acciPersPropRoot.get("acciPersPropId")));
		TypedQuery<AcciPersProp> consulta = this.entityManager
				.createQuery(query.select(acciPersPropRoot).where(
						getSearchPredicates(acciPersPropRoot, acciPersProp)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(
					filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(AcciPersProp acciPersProp) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<AcciPersProp> acciPersPropRoot = countQuery
				.from(AcciPersProp.class);

		countQuery = countQuery.select(builder.count(acciPersPropRoot)).where(
				getSearchPredicates(acciPersPropRoot, acciPersProp));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(
			Root<AcciPersProp> acciPersPropRoot, AcciPersProp acciPersProp) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}