package ec.com.tecnointel.soem.parametro.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.listaInt.TranPlanDetaListaInt;
import ec.com.tecnointel.soem.parametro.modelo.TranPlanDeta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class TranPlanDetaListaImp extends GestorListaSoem<TranPlanDeta> implements TranPlanDetaListaInt, Serializable {

	private static final long serialVersionUID = -4211861387187254137L;

	@Override
	public List<TranPlanDeta> buscar(TranPlanDeta tranPlanDeta, Integer pagina) {
		
		EntityGraph<?> tranPlanDetaGraph = this.entityManager.getEntityGraph("tranPlanDeta.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<TranPlanDeta> query = builder.createQuery(TranPlanDeta.class);
		Root<TranPlanDeta> tranPlanDetaRoot = query.from(TranPlanDeta.class);

		query.orderBy(builder.asc(tranPlanDetaRoot.get("tranPlanDetaId")));
		TypedQuery<TranPlanDeta> consulta = this.entityManager
				.createQuery(query.select(tranPlanDetaRoot).where(getSearchPredicates(tranPlanDetaRoot, tranPlanDeta)));
		consulta.setHint("jakarta.persistence.loadgraph", tranPlanDetaGraph);

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(TranPlanDeta tranPlanDeta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<TranPlanDeta> tranPlanDetaRoot = countQuery.from(TranPlanDeta.class);

		countQuery = countQuery.select(builder.count(tranPlanDetaRoot))
				.where(getSearchPredicates(tranPlanDetaRoot, tranPlanDeta));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<TranPlanDeta> tranPlanDetaRoot, TranPlanDeta tranPlanDeta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer tranPlanId = tranPlanDeta.getTranPlan().getTranPlanId();
		if (tranPlanId != null) {
			predicates.add(builder.equal(tranPlanDetaRoot.get("tranPlan").<Integer>get("tranPlanId"), tranPlanId));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}