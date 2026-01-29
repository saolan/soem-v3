package ec.com.tecnointel.soem.inventario.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.inventario.listaInt.ProdTipoPlanCuenListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdTipoPlanCuen;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class ProdTipoPlanCuenListaImp extends GestorListaSoem<ProdTipoPlanCuen>
		implements ProdTipoPlanCuenListaInt, Serializable {

	private static final long serialVersionUID = -993298331515142455L;

	// Busca con paginaci�n
	@Override
	public List<ProdTipoPlanCuen> buscar(ProdTipoPlanCuen prodTipoPlanCuen, Integer pagina) {
		
		EntityGraph<?> ptpcGraph = this.entityManager.getEntityGraph("ptpc.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<ProdTipoPlanCuen> query = builder.createQuery(ProdTipoPlanCuen.class);
		Root<ProdTipoPlanCuen> prodTipoPlanCuenRoot = query.from(ProdTipoPlanCuen.class);

		query.orderBy(builder.asc(prodTipoPlanCuenRoot.get("ptpcId")));
		TypedQuery<ProdTipoPlanCuen> consulta = this.entityManager.createQuery(
				query.select(prodTipoPlanCuenRoot).where(getSearchPredicates(prodTipoPlanCuenRoot, prodTipoPlanCuen)));
		consulta.setHint("jakarta.persistence.loadgraph", ptpcGraph);

		// Si se pasa null a pagina se listan todos los datos de acuerdo a
		// parametros
		// Es decir no realiza paginaci�n
		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		// consulta.setFirstResult(pagina *
		// getFilasPagina()).setMaxResults(getFilasPagina());

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(ProdTipoPlanCuen prodTipoPlanCuen) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<ProdTipoPlanCuen> prodTipoPlanCuenRoot = countQuery.from(ProdTipoPlanCuen.class);

		countQuery = countQuery.select(builder.count(prodTipoPlanCuenRoot))
				.where(getSearchPredicates(prodTipoPlanCuenRoot, prodTipoPlanCuen));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<ProdTipoPlanCuen> prodTipoPlanCuenRoot, ProdTipoPlanCuen prodTipoPlanCuen) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer prdTipoId = prodTipoPlanCuen.getProdTipo().getProdTipoId();
		if (prdTipoId != null) {
			predicates.add(builder.equal(prodTipoPlanCuenRoot.get("prodTipo").get("prodTipoId"), prdTipoId));
		}
		
		Integer planCuenId = prodTipoPlanCuen.getPlanCuen().getPlanCuenId();
		if (planCuenId != null) {
			predicates.add(builder.equal(prodTipoPlanCuenRoot.get("planCuen").<Integer> get("planCuenId"), planCuenId));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(ProdTipoPlanCuen prodTipoPlanCuen) throws Exception {
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