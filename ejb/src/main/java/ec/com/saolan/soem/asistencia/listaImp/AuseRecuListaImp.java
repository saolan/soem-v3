package ec.com.saolan.soem.asistencia.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.AuseRecuListaInt;
import ec.com.saolan.soem.asistencia.modelo.AuseRecu;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class AuseRecuListaImp extends GestorListaSoem<AuseRecu>
		implements
			AuseRecuListaInt,
			Serializable {

	private static final long serialVersionUID = 5925371808588404582L;

	@Override
	public List<AuseRecu> buscar(AuseRecu auseRecu, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<AuseRecu> query = builder.createQuery(AuseRecu.class);
		Root<AuseRecu> auseRecuRoot = query.from(AuseRecu.class);

		query.orderBy(builder.desc(auseRecuRoot.get("auseRecuId")));
		TypedQuery<AuseRecu> consulta = this.entityManager.createQuery(query
				.select(auseRecuRoot).where(
						getSearchPredicates(auseRecuRoot, auseRecu)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(
					filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(AuseRecu auseRecu) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<AuseRecu> auseRecuRoot = countQuery.from(AuseRecu.class);

		countQuery = countQuery.select(builder.count(auseRecuRoot)).where(
				getSearchPredicates(auseRecuRoot, auseRecu));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<AuseRecu> auseRecuRoot,
			AuseRecu auseRecu) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}