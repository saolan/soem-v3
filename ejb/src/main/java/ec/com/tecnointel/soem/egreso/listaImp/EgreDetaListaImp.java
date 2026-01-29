package ec.com.tecnointel.soem.egreso.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.egreso.listaInt.EgreDetaListaInt;
import ec.com.tecnointel.soem.egreso.modelo.EgreDeta;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class EgreDetaListaImp extends GestorListaSoem<EgreDeta> implements EgreDetaListaInt, Serializable {

	private static final long serialVersionUID = 4015868933187791780L;

	// Busca con paginaci�n
	@Override
	public List<EgreDeta> buscar(EgreDeta egreDeta, Integer pagina) {
		
		EntityGraph<?> egreDetaGraph = this.entityManager.getEntityGraph("egreDeta.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<EgreDeta> query = builder.createQuery(EgreDeta.class);
		Root<EgreDeta> egreDetaRoot = query.from(EgreDeta.class);

		query.orderBy(builder.asc(egreDetaRoot.get("egreDetaId")));
		TypedQuery<EgreDeta> consulta = this.entityManager
				.createQuery(query.select(egreDetaRoot).where(getSearchPredicates(egreDetaRoot, egreDeta)));
		consulta.setHint("jakarta.persistence.loadgraph", egreDetaGraph);

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
	public long contarRegistros(EgreDeta egreDeta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<EgreDeta> egreDetaRoot = countQuery.from(EgreDeta.class);

		countQuery = countQuery.select(builder.count(egreDetaRoot)).where(getSearchPredicates(egreDetaRoot, egreDeta));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<EgreDeta> egreDetaRoot, EgreDeta egreDeta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

//		Integer egreDetaId = egreDeta.getEgreDetaId();
//		if (egreDetaId != null) {
//			predicates.add(builder.equal(egreDetaRoot.get("egreDetaId"), egreDetaId));
//		}
		
		Integer egresoId = egreDeta.getEgreso().getEgresoId();
		if (egresoId != null) {
			predicates.add(builder.equal(egreDetaRoot.get("egreso").get("egresoId"), egresoId));
		}

		Integer egreDetaSupeId = egreDeta.getEgreDeta().getEgreDetaId();
		if (egreDetaSupeId != null) {
			predicates.add(builder.equal(egreDetaRoot.get("egreDeta").get("egreDetaId"), egreDetaSupeId));
		} else {
			predicates.add(builder.isNull(egreDetaRoot.get("egreDeta").get("egreDetaId") ));	
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}