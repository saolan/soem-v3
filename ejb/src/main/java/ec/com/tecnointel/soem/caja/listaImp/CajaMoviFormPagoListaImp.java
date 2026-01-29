package ec.com.tecnointel.soem.caja.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.caja.listaInt.CajaMoviFormPagoListaInt;
import ec.com.tecnointel.soem.caja.modelo.CajaMoviFormPago;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class CajaMoviFormPagoListaImp extends GestorListaSoem<CajaMoviFormPago>
		implements CajaMoviFormPagoListaInt, Serializable {

	private static final long serialVersionUID = 8074707529776344567L;

	// Busca con paginaci�n
	@Override
	public List<CajaMoviFormPago> buscar(CajaMoviFormPago cajaMoviFormPago, Integer pagina) {

		EntityGraph<?> cmfpGraph = this.entityManager.getEntityGraph("cmfp.Graph");
		
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<CajaMoviFormPago> query = builder.createQuery(CajaMoviFormPago.class);
		Root<CajaMoviFormPago> cajaMoviFormPagoRoot = query.from(CajaMoviFormPago.class);

		query.orderBy(builder.asc(cajaMoviFormPagoRoot.get("cmfpId")));
		TypedQuery<CajaMoviFormPago> consulta = this.entityManager.createQuery(
				query.select(cajaMoviFormPagoRoot).where(getSearchPredicates(cajaMoviFormPagoRoot, cajaMoviFormPago)));
		consulta.setHint("jakarta.persistence.loadgraph", cmfpGraph);

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
	public long contarRegistros(CajaMoviFormPago cajaMoviFormPago) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<CajaMoviFormPago> cajaMoviFormPagoRoot = countQuery.from(CajaMoviFormPago.class);

		countQuery = countQuery.select(builder.count(cajaMoviFormPagoRoot))
				.where(getSearchPredicates(cajaMoviFormPagoRoot, cajaMoviFormPago));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<CajaMoviFormPago> cajaMoviFormPagoRoot,
			CajaMoviFormPago cajaMoviFormPago) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer cmfpId = cajaMoviFormPago.getCmfpId();
		if (cmfpId != null) {
			predicates.add(builder.equal(cajaMoviFormPagoRoot.get("cmfpId"), cmfpId));
		}

		Integer cajaMoviId = cajaMoviFormPago.getCajaMovi().getCajaMoviId();
		if (cajaMoviId != null) {
			predicates.add(builder.equal(cajaMoviFormPagoRoot.get("cajaMovi").<Integer> get("cajaMoviId"), cajaMoviId));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(CajaMoviFormPago cajaMoviFormPago) throws Exception {
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