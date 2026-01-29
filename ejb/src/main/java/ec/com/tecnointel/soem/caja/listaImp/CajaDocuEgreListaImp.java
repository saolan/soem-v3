package ec.com.tecnointel.soem.caja.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.caja.listaInt.CajaDocuEgreListaInt;
import ec.com.tecnointel.soem.caja.modelo.CajaDocuEgre;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class CajaDocuEgreListaImp extends GestorListaSoem<CajaDocuEgre> implements CajaDocuEgreListaInt, Serializable {

	private static final long serialVersionUID = 4579550119143009259L;

	@Override
	public List<CajaDocuEgre> buscar(CajaDocuEgre cajaDocuEgre, Integer pagina) {
		
		EntityGraph<?> cajaDocuEgreGraph = this.entityManager.getEntityGraph("cajaDocuEgre.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<CajaDocuEgre> query = builder.createQuery(CajaDocuEgre.class);
		Root<CajaDocuEgre> cajaDocuEgreRoot = query.from(CajaDocuEgre.class);

		query.orderBy(builder.asc(cajaDocuEgreRoot.get("caja").<String> get("descri")));
		TypedQuery<CajaDocuEgre> consulta = this.entityManager
				.createQuery(query.select(cajaDocuEgreRoot).where(getSearchPredicates(cajaDocuEgreRoot, cajaDocuEgre)));
		consulta.setHint("jakarta.persistence.loadgraph", cajaDocuEgreGraph);

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
	public long contarRegistros(CajaDocuEgre cajaDocuEgre) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<CajaDocuEgre> cajaDocuEgreRoot = countQuery.from(CajaDocuEgre.class);

		countQuery = countQuery.select(builder.count(cajaDocuEgreRoot))
				.where(getSearchPredicates(cajaDocuEgreRoot, cajaDocuEgre));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<CajaDocuEgre> cajaDocuEgreRoot, CajaDocuEgre cajaDocuEgre) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer cajaId = cajaDocuEgre.getCaja().getCajaId();
		if (cajaId != null) {
			predicates.add(builder.equal(cajaDocuEgreRoot.get("caja").<Integer> get("cajaId"), cajaId));
		}
		
		String cajaDescri = cajaDocuEgre.getCaja().getDescri();
		if (cajaDescri != null) {
			predicates.add(builder.like(builder.lower(cajaDocuEgreRoot.get("caja").<String> get("descri")), '%' + cajaDescri.toLowerCase() + '%'));
		}

		Integer documentoId = cajaDocuEgre.getDocuEgre().getDocumentoId();
		if (documentoId != null) {
			predicates.add(builder.equal(cajaDocuEgreRoot.get("docuEgre").<Integer> get("documentoId"), documentoId));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(CajaDocuEgre cajaDocuEgre) throws Exception {
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