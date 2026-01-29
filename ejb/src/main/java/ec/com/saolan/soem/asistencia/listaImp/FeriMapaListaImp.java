package ec.com.saolan.soem.asistencia.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.FeriMapaListaInt;
import ec.com.saolan.soem.asistencia.modelo.FeriMapa;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class FeriMapaListaImp extends GestorListaSoem<FeriMapa>
		implements
			FeriMapaListaInt,
			Serializable {

	private static final long serialVersionUID = 3270834135172958002L;

	@Override
	public List<FeriMapa> buscar(FeriMapa feriMapa, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<FeriMapa> query = builder.createQuery(FeriMapa.class);
		Root<FeriMapa> feriMapaRoot = query.from(FeriMapa.class);

		query.orderBy(builder.desc(feriMapaRoot.get("feriMapaId")));
		TypedQuery<FeriMapa> consulta = this.entityManager.createQuery(query
				.select(feriMapaRoot).where(
						getSearchPredicates(feriMapaRoot, feriMapa)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(
					filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(FeriMapa feriMapa) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<FeriMapa> feriMapaRoot = countQuery.from(FeriMapa.class);

		countQuery = countQuery.select(builder.count(feriMapaRoot)).where(
				getSearchPredicates(feriMapaRoot, feriMapa));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<FeriMapa> feriMapaRoot,
			FeriMapa feriMapa) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}