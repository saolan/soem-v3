package ec.com.tecnointel.soem.parametro.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.listaInt.PersParaListaInt;
import ec.com.tecnointel.soem.parametro.modelo.PersPara;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class PersParaListaImp extends GestorListaSoem<PersPara> implements PersParaListaInt, Serializable {

	private static final long serialVersionUID = 1039069900037918641L;

	// Busca con paginaci�n
	@Override
	public List<PersPara> buscar(PersPara persPara, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<PersPara> query = builder.createQuery(PersPara.class);
		Root<PersPara> persParaRoot = query.from(PersPara.class);

		query.orderBy(builder.asc(persParaRoot.get("persParaDescri")));
		TypedQuery<PersPara> consulta = this.entityManager
				.createQuery(query.select(persParaRoot).where(getSearchPredicates(persParaRoot, persPara)));

		// Si se pasa null a pagina se listan todos los datos de acuerdo a
		// parametros
		// Es decir no realiza paginaci�n
		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		// consulta.setFirstResult(pagina *
		// getFilasPagina()).setMaxResults(getFilasPagina());

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(PersPara persPara) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<PersPara> persParaRoot = countQuery.from(PersPara.class);

		countQuery = countQuery.select(builder.count(persParaRoot)).where(getSearchPredicates(persParaRoot, persPara));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<PersPara> persParaRoot, PersPara persPara) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer persParaId = persPara.getPersParaId();
		if (persParaId != null) {
			predicates.add(builder.equal(persParaRoot.get("persParaId"), persParaId));
		}

		String persParaDescri = persPara.getDescri();
		if (persParaDescri != null && !"".equals(persParaDescri)) {
			predicates.add(builder.like(builder.lower(persParaRoot.<String>get("persParaDescri")),
					'%' + persParaDescri.toLowerCase() + '%'));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(PersPara persPara) throws Exception {
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}