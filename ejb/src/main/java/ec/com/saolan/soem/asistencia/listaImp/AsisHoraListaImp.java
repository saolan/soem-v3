package ec.com.saolan.soem.asistencia.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.AsisHoraListaInt;
import ec.com.saolan.soem.asistencia.modelo.AsisHora;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class AsisHoraListaImp extends GestorListaSoem<AsisHora> implements AsisHoraListaInt, Serializable {

	private static final long serialVersionUID = 3703808345596683592L;

	@Override
	public List<AsisHora> buscar(AsisHora asisHora, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<AsisHora> query = builder.createQuery(AsisHora.class);
		Root<AsisHora> asisHoraRoot = query.from(AsisHora.class);

		query.orderBy(builder.desc(asisHoraRoot.get("asisHoraId")));
		TypedQuery<AsisHora> consulta = this.entityManager
				.createQuery(query.select(asisHoraRoot).where(getSearchPredicates(asisHoraRoot, asisHora)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(AsisHora asisHora) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<AsisHora> asisHoraRoot = countQuery.from(AsisHora.class);

		countQuery = countQuery.select(builder.count(asisHoraRoot)).where(getSearchPredicates(asisHoraRoot, asisHora));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<AsisHora> asisHoraRoot, AsisHora asisHora) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}