package ec.com.saolan.soem.asistencia.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.TipoContListaInt;
import ec.com.saolan.soem.asistencia.modelo.TipoCont;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class TipoContListaImp extends GestorListaSoem<TipoCont>
		implements
			TipoContListaInt,
			Serializable {

	private static final long serialVersionUID = -5447210761307337151L;

	@Override
	public List<TipoCont> buscar(TipoCont tipoCont, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<TipoCont> query = builder.createQuery(TipoCont.class);
		Root<TipoCont> tipoContRoot = query.from(TipoCont.class);

		query.orderBy(builder.desc(tipoContRoot.get("tipoContId")));
		TypedQuery<TipoCont> consulta = this.entityManager.createQuery(query
				.select(tipoContRoot).where(
						getSearchPredicates(tipoContRoot, tipoCont)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(
					filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(TipoCont tipoCont) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<TipoCont> tipoContRoot = countQuery.from(TipoCont.class);

		countQuery = countQuery.select(builder.count(tipoContRoot)).where(
				getSearchPredicates(tipoContRoot, tipoCont));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<TipoCont> tipoContRoot,
			TipoCont tipoCont) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}