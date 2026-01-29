package ec.com.tecnointel.soem.egreso.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.egreso.listaInt.EgreNotaListaInt;
import ec.com.tecnointel.soem.egreso.modelo.EgreNota;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class EgreNotaListaImp extends GestorListaSoem<EgreNota> implements EgreNotaListaInt, Serializable {

	private static final long serialVersionUID = -1617113730675921006L;

	@Override
	public List<EgreNota> buscar(EgreNota egreNota, Integer pagina) {
		
//		EntityGraph<?> egreNotaGraph = this.entityManager.getEntityGraph("egreNota.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<EgreNota> query = builder.createQuery(EgreNota.class);
		Root<EgreNota> egreNotaRoot = query.from(EgreNota.class);

		query.orderBy(builder.asc(egreNotaRoot.get("egreNotaId")));
		TypedQuery<EgreNota> consulta = this.entityManager
				.createQuery(query.select(egreNotaRoot).where(getSearchPredicates(egreNotaRoot, egreNota)));
//		consulta.setHint("jakarta.persistence.loadgraph", egreNotaGraph);

		// Si se pasa null a pagina se listan todos los datos de acuerdo a
		// parametros
		// Es decir no realiza paginaci�n
		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(EgreNota egreNota) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<EgreNota> egreNotaRoot = countQuery.from(EgreNota.class);

		countQuery = countQuery.select(builder.count(egreNotaRoot)).where(getSearchPredicates(egreNotaRoot, egreNota));
		return this.entityManager.createQuery(countQuery).getSingleResult();
	}

	private Predicate[] getSearchPredicates(Root<EgreNota> egreNotaRoot, EgreNota egreNota) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Integer egresoId = egreNota.getEgreso().getEgresoId();
		if (egresoId != null) {
			predicates.add(builder.equal(egreNotaRoot.get("egreso").get("egresoId"), egresoId));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}