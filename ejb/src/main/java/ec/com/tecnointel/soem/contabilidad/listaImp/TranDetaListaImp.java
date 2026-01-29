package ec.com.tecnointel.soem.contabilidad.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.contabilidad.listaInt.TranDetaListaInt;
import ec.com.tecnointel.soem.contabilidad.modelo.TranDeta;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class TranDetaListaImp extends GestorListaSoem<TranDeta> implements TranDetaListaInt, Serializable {

	private static final long serialVersionUID = -5476325738189402848L;

	@Override
	public List<TranDeta> buscar(TranDeta tranDeta, Integer pagina) {

		EntityGraph<?> tranDetaGraph = this.entityManager.getEntityGraph("tranDeta.Graph");
		
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<TranDeta> query = builder.createQuery(TranDeta.class);
		Root<TranDeta> tranDetaRoot = query.from(TranDeta.class);

		query.orderBy(builder.asc(tranDetaRoot.get("tranDetaId")));
		TypedQuery<TranDeta> consulta = this.entityManager
				.createQuery(query.select(tranDetaRoot).where(getSearchPredicates(tranDetaRoot, tranDeta)));
		consulta.setHint("jakarta.persistence.loadgraph", tranDetaGraph);

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
	public long contarRegistros(TranDeta tranDeta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<TranDeta> tranDetaRoot = countQuery.from(TranDeta.class);

		countQuery = countQuery.select(builder.count(tranDetaRoot)).where(getSearchPredicates(tranDetaRoot, tranDeta));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<TranDeta> tranDetaRoot, TranDeta tranDeta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer tranDetaId = tranDeta.getTranDetaId();
		if (tranDetaId != null) {
			predicates.add(builder.equal(tranDetaRoot.get("tranDetaId"), tranDetaId));
		}

		Integer transaccionId = tranDeta.getTransaccion().getTransaccionId();
		if (transaccionId != null) {
			predicates.add(builder.equal(tranDetaRoot.get("transaccion").get("transaccionId"), transaccionId));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
}