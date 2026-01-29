package ec.com.tecnointel.soem.egreso.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.egreso.listaInt.EgreTranListaInt;
import ec.com.tecnointel.soem.egreso.modelo.EgreTran;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class EgreTranListaImp extends GestorListaSoem<EgreTran> implements EgreTranListaInt, Serializable {

	private static final long serialVersionUID = 7128531796602655832L;

	@Override
	public List<EgreTran> buscar(EgreTran egreTran, Integer pagina) {

		EntityGraph<?> egreTranGraph = this.entityManager.getEntityGraph("egreTran.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<EgreTran> query = builder.createQuery(EgreTran.class);
		Root<EgreTran> egreTranRoot = query.from(EgreTran.class);

		query.orderBy(builder.asc(egreTranRoot.get("egreTranId")));
		TypedQuery<EgreTran> consulta = this.entityManager
				.createQuery(query.select(egreTranRoot).where(getSearchPredicates(egreTranRoot, egreTran)));
		consulta.setHint("jakarta.persistence.loadgraph", egreTranGraph);

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(EgreTran egreTran) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<EgreTran> egreTranRoot = countQuery.from(EgreTran.class);

		countQuery = countQuery.select(builder.count(egreTranRoot)).where(getSearchPredicates(egreTranRoot, egreTran));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<EgreTran> egreTranRoot, EgreTran egreTran) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer egresoId = egreTran.getEgreso().getEgresoId();
		if (egresoId != null) {
			predicates.add(builder.equal(egreTranRoot.get("egreso").<Integer>get("egresoId"), egresoId));
		}

		Integer transaccionId = egreTran.getTransaccion().getTransaccionId();
		if (transaccionId != null) {
			predicates.add(builder.equal(egreTranRoot.get("transaccion").<Integer>get("transaccionId"), transaccionId));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}