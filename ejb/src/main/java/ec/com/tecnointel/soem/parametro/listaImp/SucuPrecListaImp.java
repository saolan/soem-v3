package ec.com.tecnointel.soem.parametro.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.listaInt.SucuPrecListaInt;
import ec.com.tecnointel.soem.parametro.modelo.SucuPrec;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class SucuPrecListaImp extends GestorListaSoem<SucuPrec> implements SucuPrecListaInt, Serializable {

	private static final long serialVersionUID = 4798620063426955754L;

	// Busca con paginaci�n
	@Override
	public List<SucuPrec> buscar(SucuPrec sucuPrec, Integer pagina) {

		EntityGraph<?> sucuPrecGraph = this.entityManager.getEntityGraph("sucuPrec.Graph");
		
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<SucuPrec> query = builder.createQuery(SucuPrec.class);
		Root<SucuPrec> bodegaRoot = query.from(SucuPrec.class);

		query.orderBy(builder.asc(bodegaRoot.get("sucuPrecId")));
		TypedQuery<SucuPrec> consulta = this.entityManager
				.createQuery(query.select(bodegaRoot).where(getSearchPredicates(bodegaRoot, sucuPrec)));
		consulta.setHint("jakarta.persistence.loadgraph", sucuPrecGraph);

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
	public long contarRegistros(SucuPrec sucuPrec) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<SucuPrec> bodegaRoot = countQuery.from(SucuPrec.class);

		countQuery = countQuery.select(builder.count(bodegaRoot)).where(getSearchPredicates(bodegaRoot, sucuPrec));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<SucuPrec> sucuPrecRoot, SucuPrec sucuPrec) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer sucursalId = sucuPrec.getSucursal().getSucursalId();
		if (sucursalId != null) {
			predicates.add(builder.equal(sucuPrecRoot.get("sucursal").<Integer> get("sucursalId"), sucursalId));
		}

		Integer precioId = sucuPrec.getPrecio().getPrecioId();
		if (precioId != null) {
			predicates.add(builder.equal(sucuPrecRoot.get("precio").<Integer> get("precioId"), precioId));
		}

		Boolean acceso = sucuPrec.getAcceso();
		if(acceso != null){
			predicates.add(builder.equal(sucuPrecRoot.get("acceso"), acceso));	
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(SucuPrec sucuPrec) throws Exception {
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