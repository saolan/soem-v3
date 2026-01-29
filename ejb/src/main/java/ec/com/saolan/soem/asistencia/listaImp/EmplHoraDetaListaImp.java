package ec.com.saolan.soem.asistencia.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.EmplHoraDetaListaInt;
import ec.com.saolan.soem.asistencia.modelo.EmplHoraDeta;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class EmplHoraDetaListaImp extends GestorListaSoem<EmplHoraDeta>
		implements
			EmplHoraDetaListaInt,
			Serializable {

	private static final long serialVersionUID = 6038229761279063675L;

	@Override
	public List<EmplHoraDeta> buscar(EmplHoraDeta emplHoraDeta, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<EmplHoraDeta> query = builder
				.createQuery(EmplHoraDeta.class);
		Root<EmplHoraDeta> acciAutoRoot = query.from(EmplHoraDeta.class);

		query.orderBy(builder.desc(acciAutoRoot.get("acciAutoId")));
		TypedQuery<EmplHoraDeta> consulta = this.entityManager
				.createQuery(query.select(acciAutoRoot).where(
						getSearchPredicates(acciAutoRoot, emplHoraDeta)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(
					filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(EmplHoraDeta emplHoraDeta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<EmplHoraDeta> acciAutoRoot = countQuery.from(EmplHoraDeta.class);

		countQuery = countQuery.select(builder.count(acciAutoRoot)).where(
				getSearchPredicates(acciAutoRoot, emplHoraDeta));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<EmplHoraDeta> acciAutoRoot,
			EmplHoraDeta emplHoraDeta) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}