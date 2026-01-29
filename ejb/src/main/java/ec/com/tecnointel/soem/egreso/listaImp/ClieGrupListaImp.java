package ec.com.tecnointel.soem.egreso.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.egreso.listaInt.ClieGrupListaInt;
import ec.com.tecnointel.soem.egreso.modelo.ClieGrup;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class ClieGrupListaImp extends GestorListaSoem<ClieGrup> implements ClieGrupListaInt, Serializable {

	private static final long serialVersionUID = 3465239136555113473L;

	// Busca con paginaci�n
	@Override
	public List<ClieGrup> buscar(ClieGrup clieGrup, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<ClieGrup> query = builder.createQuery(ClieGrup.class);
		Root<ClieGrup> clieGrupRoot = query.from(ClieGrup.class);

		query.orderBy(builder.asc(clieGrupRoot.get("descri")));
		TypedQuery<ClieGrup> consulta = this.entityManager
				.createQuery(query.select(clieGrupRoot).where(getSearchPredicates(clieGrupRoot, clieGrup)));

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
	public long contarRegistros(ClieGrup clieGrup) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<ClieGrup> clieGrupRoot = countQuery.from(ClieGrup.class);

		countQuery = countQuery.select(builder.count(clieGrupRoot)).where(getSearchPredicates(clieGrupRoot, clieGrup));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<ClieGrup> clieGrupRoot, ClieGrup clieGrup) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer clieGrupId = clieGrup.getClieGrupId();
		if (clieGrupId != null) {
			predicates.add(builder.equal(clieGrupRoot.<Integer> get("clieGrupId"), clieGrupId));
		}

		String descri = clieGrup.getDescri();
		if (descri != null && !"".equals(descri)) {
			predicates.add(builder.like(builder.lower(clieGrupRoot.<String>get("descri")),
					'%' + descri.toLowerCase() + '%'));
		}

		boolean estado = clieGrup.isEstado();
		predicates.add(builder.equal(clieGrupRoot.get("estado"), estado));

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(ClieGrup clieGrup) throws Exception {
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