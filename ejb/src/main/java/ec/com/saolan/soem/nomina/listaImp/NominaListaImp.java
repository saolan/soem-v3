package ec.com.saolan.soem.nomina.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.nomina.listaInt.NominaListaInt;
import ec.com.saolan.soem.nomina.modelo.Nomina;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class NominaListaImp extends GestorListaSoem<Nomina>
		implements
			NominaListaInt,
			Serializable {

	private static final long serialVersionUID = 557848379228935292L;

	@Override
	public List<Nomina> buscar(Nomina nomina, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Nomina> query = builder.createQuery(Nomina.class);
		Root<Nomina> nominaRoot = query.from(Nomina.class);

		query.orderBy(builder.desc(nominaRoot.get("nominaId")));
		TypedQuery<Nomina> consulta = this.entityManager.createQuery(query
				.select(nominaRoot).where(
						getSearchPredicates(nominaRoot, nomina)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(
					filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(Nomina nomina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<Nomina> nominaRoot = countQuery.from(Nomina.class);

		countQuery = countQuery.select(builder.count(nominaRoot)).where(
				getSearchPredicates(nominaRoot, nomina));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<Nomina> nominaRoot,
			Nomina nomina) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}