package ec.com.tecnointel.soem.inventario.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.inventario.listaInt.ProdGrupListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrup;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class ProdGrupListaImp extends GestorListaSoem<ProdGrup> implements ProdGrupListaInt, Serializable {

	private static final long serialVersionUID = 6404851566118972886L;

	// Busca con paginaci�n
	@Override
	public List<ProdGrup> buscar(ProdGrup prodGrup, Integer pagina) {

		EntityGraph<?> prodGrupGraph = this.entityManager.getEntityGraph("prodGrup.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<ProdGrup> query = builder.createQuery(ProdGrup.class);
		Root<ProdGrup> prodGrupRoot = query.from(ProdGrup.class);

		query.orderBy(builder.asc(prodGrupRoot.get("descri")));
		TypedQuery<ProdGrup> consulta = this.entityManager
				.createQuery(query.select(prodGrupRoot).where(getSearchPredicates(prodGrupRoot, prodGrup)));
		consulta.setHint("jakarta.persistence.loadgraph", prodGrupGraph);

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
	public long contarRegistros(ProdGrup prodGrup) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<ProdGrup> prodGrupRoot = countQuery.from(ProdGrup.class);

		countQuery = countQuery.select(builder.count(prodGrupRoot)).where(getSearchPredicates(prodGrupRoot, prodGrup));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<ProdGrup> prodGrupRoot, ProdGrup prodGrup) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer prodGrupId = prodGrup.getProdGrupId();
		if (prodGrupId != null) {
			predicates.add(builder.equal(prodGrupRoot.<Integer>get("prodGrupId"), prodGrupId));
		}

		String descri = prodGrup.getDescri();
		if (descri != null && !"".equals(descri)) {
			predicates.add(
					builder.like(builder.lower(prodGrupRoot.<String>get("descri")), '%' + descri.toLowerCase() + '%'));
		}

		String tipo = prodGrup.getTipo();
		if (tipo != null && !"".equals(tipo)) {
			predicates.add(
					builder.like(builder.lower(prodGrupRoot.<String>get("tipo")), tipo.toLowerCase()));
		}
		
		Boolean detall = prodGrup.getDetall();
		if (detall != null) {
			predicates.add(builder.equal(prodGrupRoot.get("detall"), detall));
		}

		boolean estado = prodGrup.isEstado();
		predicates.add(builder.equal(prodGrupRoot.get("estado"), estado));

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(ProdGrup prodGrup) throws Exception {
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