package ec.com.tecnointel.soem.egreso.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.egreso.listaInt.ClieGrupPlanCuenListaInt;
import ec.com.tecnointel.soem.egreso.modelo.ClieGrupPlanCuen;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class ClieGrupPlanCuenListaImp extends GestorListaSoem<ClieGrupPlanCuen>
		implements ClieGrupPlanCuenListaInt, Serializable {

	private static final long serialVersionUID = -2156471244323861531L;

	// Busca con paginaci�n
	@Override
	public List<ClieGrupPlanCuen> buscar(ClieGrupPlanCuen clieGrupPlanCuen, Integer pagina) {

		EntityGraph<?> cgpcGraph = this.entityManager.getEntityGraph("cgpc.Graph");
		
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<ClieGrupPlanCuen> query = builder.createQuery(ClieGrupPlanCuen.class);
		Root<ClieGrupPlanCuen> clieGrupPlanCuenRoot = query.from(ClieGrupPlanCuen.class);

		query.orderBy(builder.asc(clieGrupPlanCuenRoot.get("cgpcId")));
		TypedQuery<ClieGrupPlanCuen> consulta = this.entityManager.createQuery(
				query.select(clieGrupPlanCuenRoot).where(getSearchPredicates(clieGrupPlanCuenRoot, clieGrupPlanCuen)));
		consulta.setHint("jakarta.persistence.loadgraph", cgpcGraph);

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
	public long contarRegistros(ClieGrupPlanCuen clieGrupPlanCuen) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<ClieGrupPlanCuen> clieGrupPlanCuenRoot = countQuery.from(ClieGrupPlanCuen.class);

		countQuery = countQuery.select(builder.count(clieGrupPlanCuenRoot))
				.where(getSearchPredicates(clieGrupPlanCuenRoot, clieGrupPlanCuen));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<ClieGrupPlanCuen> clieGrupPlanCuenRoot, ClieGrupPlanCuen clieGrupPlanCuen) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer clieGrupoId = clieGrupPlanCuen.getClieGrup().getClieGrupId();
		if (clieGrupoId != null) {
			predicates.add(builder.equal(clieGrupPlanCuenRoot.get("clieGrup").<Integer> get("clieGrupId"), clieGrupoId));
		}

		Integer planCuenId = clieGrupPlanCuen.getPlanCuen().getPlanCuenId();
		if (planCuenId != null) {
			predicates.add(builder.equal(clieGrupPlanCuenRoot.get("planCuen").<Integer> get("planCuentId"), planCuenId));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(ClieGrupPlanCuen clieGrupPlanCuen) throws Exception {
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