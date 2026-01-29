package ec.com.saolan.soem.asistencia.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.DispositivoListaInt;
import ec.com.saolan.soem.asistencia.modelo.Dispositivo;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class DispositivoListaImp extends GestorListaSoem<Dispositivo> implements DispositivoListaInt, Serializable {

	private static final long serialVersionUID = -8943465250782601121L;

	@Override
	public List<Dispositivo> buscar(Dispositivo dispositivo, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Dispositivo> query = builder.createQuery(Dispositivo.class);
		Root<Dispositivo> dispositivoRoot = query.from(Dispositivo.class);

		query.orderBy(builder.desc(dispositivoRoot.get("dispositivoId")));
		TypedQuery<Dispositivo> consulta = this.entityManager
				.createQuery(query.select(dispositivoRoot).where(getSearchPredicates(dispositivoRoot, dispositivo)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(Dispositivo dispositivo) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<Dispositivo> dispositivoRoot = countQuery.from(Dispositivo.class);

		countQuery = countQuery.select(builder.count(dispositivoRoot))
				.where(getSearchPredicates(dispositivoRoot, dispositivo));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<Dispositivo> dispositivoRoot, Dispositivo dispositivo) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}