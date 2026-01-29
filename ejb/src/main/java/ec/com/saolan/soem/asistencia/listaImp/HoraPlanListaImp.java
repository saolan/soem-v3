package ec.com.saolan.soem.asistencia.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.HoraPlanListaInt;
import ec.com.saolan.soem.asistencia.modelo.HoraPlan;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class HoraPlanListaImp extends GestorListaSoem<HoraPlan>
		implements
			HoraPlanListaInt,
			Serializable {

	private static final long serialVersionUID = -1116822584669775258L;

	@Override
	public List<HoraPlan> buscar(HoraPlan horaPlan, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<HoraPlan> query = builder.createQuery(HoraPlan.class);
		Root<HoraPlan> horaPlanRoot = query.from(HoraPlan.class);

		query.orderBy(builder.desc(horaPlanRoot.get("horaPlanId")));
		TypedQuery<HoraPlan> consulta = this.entityManager.createQuery(query
				.select(horaPlanRoot).where(
						getSearchPredicates(horaPlanRoot, horaPlan)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(
					filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(HoraPlan horaPlan) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<HoraPlan> horaPlanRoot = countQuery.from(HoraPlan.class);

		countQuery = countQuery.select(builder.count(horaPlanRoot)).where(
				getSearchPredicates(horaPlanRoot, horaPlan));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<HoraPlan> horaPlanRoot,
			HoraPlan horaPlan) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}