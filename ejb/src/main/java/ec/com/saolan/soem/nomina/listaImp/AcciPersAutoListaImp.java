package ec.com.saolan.soem.nomina.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.nomina.listaInt.AcciPersAutoListaInt;
import ec.com.saolan.soem.nomina.modelo.AcciPersAuto;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class AcciPersAutoListaImp extends GestorListaSoem<AcciPersAuto>
		implements
			AcciPersAutoListaInt,
			Serializable {

	private static final long serialVersionUID = 5437804472947849927L;

	@Override
	public List<AcciPersAuto> buscar(AcciPersAuto acciAuto, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<AcciPersAuto> query = builder
				.createQuery(AcciPersAuto.class);
		Root<AcciPersAuto> acciAutoRoot = query.from(AcciPersAuto.class);

		query.orderBy(builder.desc(acciAutoRoot.get("acciAutoId")));
		TypedQuery<AcciPersAuto> consulta = this.entityManager
				.createQuery(query.select(acciAutoRoot).where(
						getSearchPredicates(acciAutoRoot, acciAuto)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(
					filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(AcciPersAuto acciAuto) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<AcciPersAuto> acciAutoRoot = countQuery.from(AcciPersAuto.class);

		countQuery = countQuery.select(builder.count(acciAutoRoot)).where(
				getSearchPredicates(acciAutoRoot, acciAuto));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<AcciPersAuto> acciAutoRoot,
			AcciPersAuto acciAuto) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}