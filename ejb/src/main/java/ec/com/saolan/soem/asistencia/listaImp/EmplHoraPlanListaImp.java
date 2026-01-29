package ec.com.saolan.soem.asistencia.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.EmplHoraPlanListaInt;
import ec.com.saolan.soem.asistencia.modelo.EmplHoraPlan;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class EmplHoraPlanListaImp extends GestorListaSoem<EmplHoraPlan>
		implements
			EmplHoraPlanListaInt,
			Serializable {

	private static final long serialVersionUID = 3044397262123240584L;

	@Override
	public List<EmplHoraPlan> buscar(EmplHoraPlan emplHoraPlan, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<EmplHoraPlan> query = builder
				.createQuery(EmplHoraPlan.class);
		Root<EmplHoraPlan> acciAutoRoot = query.from(EmplHoraPlan.class);

		query.orderBy(builder.desc(acciAutoRoot.get("acciAutoId")));
		TypedQuery<EmplHoraPlan> consulta = this.entityManager
				.createQuery(query.select(acciAutoRoot).where(
						getSearchPredicates(acciAutoRoot, emplHoraPlan)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(
					filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(EmplHoraPlan emplHoraPlan) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<EmplHoraPlan> acciAutoRoot = countQuery.from(EmplHoraPlan.class);

		countQuery = countQuery.select(builder.count(acciAutoRoot)).where(
				getSearchPredicates(acciAutoRoot, emplHoraPlan));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<EmplHoraPlan> acciAutoRoot,
			EmplHoraPlan emplHoraPlan) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}