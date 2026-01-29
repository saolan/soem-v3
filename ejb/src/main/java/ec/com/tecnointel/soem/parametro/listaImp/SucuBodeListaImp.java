package ec.com.tecnointel.soem.parametro.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.listaInt.SucuBodeListaInt;
import ec.com.tecnointel.soem.parametro.modelo.SucuBode;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class SucuBodeListaImp extends GestorListaSoem<SucuBode> implements SucuBodeListaInt, Serializable {

	private static final long serialVersionUID = 6156886226956741568L;

	// Busca con paginaci�n
	@Override
	public List<SucuBode> buscar(SucuBode sucuBode, Integer pagina) {

		EntityGraph<?> sucuBodeGraph = this.entityManager.getEntityGraph("sucuBode.Graph");
		
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<SucuBode> query = builder.createQuery(SucuBode.class);
		Root<SucuBode> bodegaRoot = query.from(SucuBode.class);

		query.orderBy(builder.asc(bodegaRoot.get("sucuBodeId")));
		TypedQuery<SucuBode> consulta = this.entityManager
				.createQuery(query.select(bodegaRoot).where(getSearchPredicates(bodegaRoot, sucuBode)));
		consulta.setHint("jakarta.persistence.loadgraph", sucuBodeGraph);

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
	public long contarRegistros(SucuBode sucuBode) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<SucuBode> bodegaRoot = countQuery.from(SucuBode.class);

		countQuery = countQuery.select(builder.count(bodegaRoot)).where(getSearchPredicates(bodegaRoot, sucuBode));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<SucuBode> sucuBodeRoot, SucuBode sucuBode) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer sucursalId = sucuBode.getSucursal().getSucursalId();
		if (sucursalId != null) {
			predicates.add(builder.equal(sucuBodeRoot.get("sucursal").<Integer> get("sucursalId"), sucursalId));
		}

		Integer bodegaId = sucuBode.getBodega().getBodegaId();
		if (bodegaId != null) {
			predicates.add(builder.equal(sucuBodeRoot.get("bodega").<Integer> get("bodegaId"), bodegaId));
		}

		Boolean acceso = sucuBode.getAcceso();
		if(acceso != null){
			predicates.add(builder.equal(sucuBodeRoot.get("acceso"), acceso));	
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(SucuBode sucuBode) throws Exception {
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