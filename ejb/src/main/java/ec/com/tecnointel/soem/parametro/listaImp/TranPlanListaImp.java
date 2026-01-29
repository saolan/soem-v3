package ec.com.tecnointel.soem.parametro.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.listaInt.TranPlanListaInt;
import ec.com.tecnointel.soem.parametro.modelo.TranPlan;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class TranPlanListaImp extends GestorListaSoem<TranPlan> implements TranPlanListaInt, Serializable {

	private static final long serialVersionUID = -2616936286290326089L;

	// Busca con paginaci?n
	@Override
	public List<TranPlan> buscar(TranPlan tranPlan, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<TranPlan> query = builder.createQuery(TranPlan.class);
		Root<TranPlan> tranPlanRoot = query.from(TranPlan.class);

		query.orderBy(builder.asc(tranPlanRoot.get("descri")));
		TypedQuery<TranPlan> consulta = this.entityManager
				.createQuery(query.select(tranPlanRoot).where(getSearchPredicates(tranPlanRoot, tranPlan)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(TranPlan tranPlan) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<TranPlan> tranPlanRoot = countQuery.from(TranPlan.class);

		countQuery = countQuery.select(builder.count(tranPlanRoot)).where(getSearchPredicates(tranPlanRoot, tranPlan));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<TranPlan> tranPlanRoot, TranPlan tranPlan) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer tranPlanId = tranPlan.getTranPlanId();
		if (tranPlanId != null) {
			predicates.add(builder.equal(tranPlanRoot.<Integer>get("tranPlanId"), tranPlanId));
		}

		String descri = tranPlan.getDescri();
		if (descri != null && !"".equals(descri)) {
			predicates.add(
					builder.like(builder.lower(tranPlanRoot.<String>get("descri")), '%' + descri.toLowerCase() + '%'));
		}

		String modulo = tranPlan.getModulo();
		if (modulo != null && !"".equals(modulo)) {
			predicates.add(
					builder.equal(builder.lower(tranPlanRoot.<String>get("modulo")), modulo.toLowerCase()));
		}

		
		boolean estado = tranPlan.isEstado();
		predicates.add(builder.equal(tranPlanRoot.get("estado"), estado));

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