package ec.com.tecnointel.soem.inventario.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.inventario.listaInt.ProdBodeListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdBode;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class ProdBodeListaImp extends GestorListaSoem<ProdBode> implements ProdBodeListaInt, Serializable {

	private static final long serialVersionUID = 3415868071431360820L;

	// Busca con paginaci�n
	@Override
	public List<ProdBode> buscar(ProdBode prodBode, Integer pagina) {
		
		EntityGraph<?> prodBodeGraph = this.entityManager.getEntityGraph("prodBode.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<ProdBode> query = builder.createQuery(ProdBode.class);
		Root<ProdBode> prodBodeRoot = query.from(ProdBode.class);

		query.orderBy(builder.asc(prodBodeRoot.get("prodBodeId")));
		TypedQuery<ProdBode> consulta = this.entityManager
				.createQuery(query.select(prodBodeRoot).where(getSearchPredicates(prodBodeRoot, prodBode)));
		consulta.setHint("jakarta.persistence.loadgraph", prodBodeGraph);

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
	public long contarRegistros(ProdBode prodBode) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<ProdBode> prodBodeRoot = countQuery.from(ProdBode.class);

		countQuery = countQuery.select(builder.count(prodBodeRoot)).where(getSearchPredicates(prodBodeRoot, prodBode));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<ProdBode> prodBodeRoot, ProdBode prodBode) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer sucursalId = prodBode.getSucursal().getSucursalId();
		if (sucursalId != null) {
			predicates.add(builder.equal(prodBodeRoot.get("sucursal").get("sucursalId"), sucursalId));
		}

		Integer productoId = prodBode.getProducto().getProductoId();
		if (productoId != null) {
			predicates.add(builder.equal(prodBodeRoot.get("producto").<Integer> get("productoId"), productoId));
		}

		Integer bodegaId = prodBode.getBodega().getBodegaId();
		if (bodegaId != null) {
			predicates.add(builder.equal(prodBodeRoot.get("bodega").<Integer> get("bodegaId"), bodegaId));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(ProdBode prodBode) throws Exception {
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	@Override
	public List<ProdBode> buscar(Sucursal sucursal, List<Integer> bodegas, Producto producto ){
		
		EntityGraph<?> prodBodeGraph = this.entityManager.getEntityGraph("prodBode.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<ProdBode> query = builder.createQuery(ProdBode.class);
		Root<ProdBode> prodBodeRoot = query.from(ProdBode.class);

		List<Predicate> predicates = new ArrayList<Predicate>();
		
//		if (sucursals.size() != 0) {
//			Expression<Integer> sucursalExpression = prodBodeRoot.get("sucursal").<Integer> get("sucursalId");
//			Predicate predicate = sucursalExpression.in(sucursals);
//			predicates.add(predicate);
//		}

		Integer sucursalId = sucursal.getSucursalId();
		if (sucursalId != null) {
			predicates.add(builder.equal(prodBodeRoot.get("sucursal").get("sucursalId"), sucursalId));
		}

		Integer productoId = producto.getProductoId();
		if (productoId != null) {
			predicates.add(builder.equal(prodBodeRoot.get("producto").get("productoId"), productoId));
		}
		
		if (bodegas.size() != 0) {
			Expression<Integer> expression = prodBodeRoot.get("bodega").<Integer> get("bodegaId");
			Predicate predicate = expression.in(bodegas);
			predicates.add(predicate);
		}
		
		query.orderBy(builder.asc(prodBodeRoot.get("prodBodeId")));
		TypedQuery<ProdBode> consulta = this.entityManager
				.createQuery(query.select(prodBodeRoot).where(predicates.toArray(new Predicate[predicates.size()])));
		consulta.setHint("jakarta.persistence.loadgraph", prodBodeGraph);

		return consulta.getResultList();

	}

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