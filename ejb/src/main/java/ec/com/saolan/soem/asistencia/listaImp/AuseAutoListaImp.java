package ec.com.saolan.soem.asistencia.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.AuseAutoListaInt;
import ec.com.saolan.soem.asistencia.modelo.AuseAuto;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class AuseAutoListaImp extends GestorListaSoem<AuseAuto>
		implements
			AuseAutoListaInt,
			Serializable {

	private static final long serialVersionUID = 8025408760601595731L;

	@Override
	public List<AuseAuto> buscar(AuseAuto auseAuto, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<AuseAuto> query = builder.createQuery(AuseAuto.class);
		Root<AuseAuto> auseAutoRoot = query.from(AuseAuto.class);

		query.orderBy(builder.desc(auseAutoRoot.get("auseAutoId")));
		TypedQuery<AuseAuto> consulta = this.entityManager.createQuery(query
				.select(auseAutoRoot).where(
						getSearchPredicates(auseAutoRoot, auseAuto)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(
					filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(AuseAuto auseAuto) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<AuseAuto> auseAutoRoot = countQuery.from(AuseAuto.class);

		countQuery = countQuery.select(builder.count(auseAutoRoot)).where(
				getSearchPredicates(auseAutoRoot, auseAuto));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<AuseAuto> auseAutoRoot,
			AuseAuto auseAuto) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}