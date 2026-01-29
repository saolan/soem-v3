package ec.com.tecnointel.soem.parametro.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.listaInt.DimmPlanCuenListaInt;
import ec.com.tecnointel.soem.parametro.modelo.DimmPlanCuen;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class DimmPlanCuenListaImp extends GestorListaSoem<DimmPlanCuen> implements DimmPlanCuenListaInt, Serializable {

	private static final long serialVersionUID = 4286682202856409917L;

	@Override
	public List<DimmPlanCuen> buscar(DimmPlanCuen dimmPlanCuen, Integer pagina) {

		EntityGraph<?> dimmPlanCuenGraph = this.entityManager.getEntityGraph("dimmPlanCuen.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<DimmPlanCuen> query = builder.createQuery(DimmPlanCuen.class);
		Root<DimmPlanCuen> dimmPlanCuenRoot = query.from(DimmPlanCuen.class);

		query.orderBy(builder.asc(dimmPlanCuenRoot.get("dimmPlanCuenId")));
		TypedQuery<DimmPlanCuen> consulta = this.entityManager
				.createQuery(query.select(dimmPlanCuenRoot).where(getSearchPredicates(dimmPlanCuenRoot, dimmPlanCuen)));
		consulta.setHint("jakarta.persistence.loadgraph", dimmPlanCuenGraph);

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(DimmPlanCuen dimmPlanCuen) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<DimmPlanCuen> dimmPlanCuenRoot = countQuery.from(DimmPlanCuen.class);

		countQuery = countQuery.select(builder.count(dimmPlanCuenRoot))
				.where(getSearchPredicates(dimmPlanCuenRoot, dimmPlanCuen));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<DimmPlanCuen> dimmPlanCuenCuenRoot, DimmPlanCuen dimmPlanCuen) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer dimmId = dimmPlanCuen.getDimm().getDimmId();
		if (dimmId != null) {
			predicates.add(builder.equal(dimmPlanCuenCuenRoot.get("dimm").get("dimmId"), dimmId));
		}
		
		Integer planCuenId = dimmPlanCuen.getPlanCuen().getPlanCuenId();
		if (planCuenId != null) {
			predicates.add(builder.equal(dimmPlanCuenCuenRoot.get("planCuen").<Integer>get("planCuenId"), planCuenId));
		}
		
		String dimmCodigo = dimmPlanCuen.getDimm().getCodigo();
		if (dimmCodigo != null && !"".equals(dimmCodigo)) {
			predicates.add(
					builder.like(builder.lower(dimmPlanCuenCuenRoot.get("dimm").<String>get("codigo")), dimmCodigo.toLowerCase()));
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