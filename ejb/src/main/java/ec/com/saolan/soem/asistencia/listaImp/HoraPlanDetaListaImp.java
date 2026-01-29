package ec.com.saolan.soem.asistencia.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.HoraPlanDetaListaInt;
import ec.com.saolan.soem.asistencia.modelo.HoraPlanDeta;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class HoraPlanDetaListaImp extends GestorListaSoem<HoraPlanDeta>
		implements
			HoraPlanDetaListaInt,
			Serializable {

	private static final long serialVersionUID = -1139533972657829586L;

	@Override
	public List<HoraPlanDeta> buscar(HoraPlanDeta horaPlanDeta, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<HoraPlanDeta> query = builder
				.createQuery(HoraPlanDeta.class);
		Root<HoraPlanDeta> horaPlanDetaRoot = query.from(HoraPlanDeta.class);

		query.orderBy(builder.desc(horaPlanDetaRoot.get("horaPlanDetaId")));
		TypedQuery<HoraPlanDeta> consulta = this.entityManager
				.createQuery(query.select(horaPlanDetaRoot).where(
						getSearchPredicates(horaPlanDetaRoot, horaPlanDeta)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(
					filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(HoraPlanDeta horaPlanDeta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<HoraPlanDeta> horaPlanDetaRoot = countQuery
				.from(HoraPlanDeta.class);

		countQuery = countQuery.select(builder.count(horaPlanDetaRoot)).where(
				getSearchPredicates(horaPlanDetaRoot, horaPlanDeta));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(
			Root<HoraPlanDeta> horaPlanDetaRoot, HoraPlanDeta horaPlanDeta) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}