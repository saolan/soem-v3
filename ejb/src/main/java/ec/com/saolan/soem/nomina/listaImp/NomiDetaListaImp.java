package ec.com.saolan.soem.nomina.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.nomina.listaInt.NomiDetaListaInt;
import ec.com.saolan.soem.nomina.modelo.NomiDeta;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class NomiDetaListaImp extends GestorListaSoem<NomiDeta> implements NomiDetaListaInt, Serializable {

	private static final long serialVersionUID = -5736764332599659722L;

	@Override
	public List<NomiDeta> buscar(NomiDeta nomiDeta, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<NomiDeta> query = builder.createQuery(NomiDeta.class);
		Root<NomiDeta> nomiDetaRoot = query.from(NomiDeta.class);

		query.orderBy(builder.desc(nomiDetaRoot.get("nomiDetaId")));
		TypedQuery<NomiDeta> consulta = this.entityManager
				.createQuery(query.select(nomiDetaRoot).where(getSearchPredicates(nomiDetaRoot, nomiDeta)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(NomiDeta nomiDeta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<NomiDeta> nomiDetaRoot = countQuery.from(NomiDeta.class);

		countQuery = countQuery.select(builder.count(nomiDetaRoot)).where(getSearchPredicates(nomiDetaRoot, nomiDeta));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<NomiDeta> nomiDetaRoot, NomiDeta nomiDeta) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}