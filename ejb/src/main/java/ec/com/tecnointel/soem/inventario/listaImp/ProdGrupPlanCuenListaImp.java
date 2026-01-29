package ec.com.tecnointel.soem.inventario.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.inventario.listaInt.ProdGrupPlanCuenListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrupPlanCuen;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class ProdGrupPlanCuenListaImp extends GestorListaSoem<ProdGrupPlanCuen>
		implements ProdGrupPlanCuenListaInt, Serializable {

	private static final long serialVersionUID = 1430666842839207774L;

	@Override
	public List<ProdGrupPlanCuen> buscar(ProdGrupPlanCuen prodGrupPlanCuen, Integer pagina) {

		EntityGraph<?> prodgpcGraph = this.entityManager.getEntityGraph("prodgpc.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<ProdGrupPlanCuen> query = builder.createQuery(ProdGrupPlanCuen.class);
		Root<ProdGrupPlanCuen> prodGrupPlanCuenRoot = query.from(ProdGrupPlanCuen.class);

		query.orderBy(builder.asc(prodGrupPlanCuenRoot.get("pgpcId")));
		TypedQuery<ProdGrupPlanCuen> consulta = this.entityManager.createQuery(
				query.select(prodGrupPlanCuenRoot).where(getSearchPredicates(prodGrupPlanCuenRoot, prodGrupPlanCuen)));
		consulta.setHint("jakarta.persistence.loadgraph", prodgpcGraph);

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		// consulta.setFirstResult(pagina *
		// getFilasPagina()).setMaxResults(getFilasPagina());

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(ProdGrupPlanCuen prodGrupPlanCuen) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<ProdGrupPlanCuen> prodGrupPlanCuenRoot = countQuery.from(ProdGrupPlanCuen.class);

		countQuery = countQuery.select(builder.count(prodGrupPlanCuenRoot))
				.where(getSearchPredicates(prodGrupPlanCuenRoot, prodGrupPlanCuen));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<ProdGrupPlanCuen> prodGrupPlanCuenRoot,
			ProdGrupPlanCuen prodGrupPlanCuen) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer prdTipoId = prodGrupPlanCuen.getProdGrup().getProdGrupId();
		if (prdTipoId != null) {
			predicates.add(builder.equal(prodGrupPlanCuenRoot.get("prodGrup").get("prodGrupId"), prdTipoId));
		}

		Integer planCuenId = prodGrupPlanCuen.getPlanCuen().getPlanCuenId();
		if (planCuenId != null) {
			predicates.add(builder.equal(prodGrupPlanCuenRoot.get("planCuen").<Integer>get("planCuenId"), planCuenId));
		}

		String tipoTran = prodGrupPlanCuen.getTipoTran();
		if (tipoTran != null && !"".equals(tipoTran)) {
			predicates.add(
					builder.like(builder.lower(prodGrupPlanCuenRoot.<String>get("tipoTran")), tipoTran.toLowerCase()));
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