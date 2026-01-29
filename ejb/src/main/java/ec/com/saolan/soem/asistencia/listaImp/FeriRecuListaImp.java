package ec.com.saolan.soem.asistencia.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.FeriRecuListaInt;
import ec.com.saolan.soem.asistencia.modelo.FeriRecu;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class FeriRecuListaImp extends GestorListaSoem<FeriRecu>
		implements
			FeriRecuListaInt,
			Serializable {

	private static final long serialVersionUID = -3956085399358758876L;

	@Override
	public List<FeriRecu> buscar(FeriRecu feriRecu, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<FeriRecu> query = builder.createQuery(FeriRecu.class);
		Root<FeriRecu> feriRecuRoot = query.from(FeriRecu.class);

		query.orderBy(builder.desc(feriRecuRoot.get("feriRecuId")));
		TypedQuery<FeriRecu> consulta = this.entityManager.createQuery(query
				.select(feriRecuRoot).where(
						getSearchPredicates(feriRecuRoot, feriRecu)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(
					filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(FeriRecu feriRecu) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<FeriRecu> feriRecuRoot = countQuery.from(FeriRecu.class);

		countQuery = countQuery.select(builder.count(feriRecuRoot)).where(
				getSearchPredicates(feriRecuRoot, feriRecu));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<FeriRecu> feriRecuRoot,
			FeriRecu feriRecu) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}