package ec.com.tecnointel.soem.inventario.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.inventario.listaInt.ProdCostListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdCost;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class ProdCostListaImp extends GestorListaSoem<ProdCost> implements ProdCostListaInt, Serializable {

	private static final long serialVersionUID = 8604159114022645113L;

	// Busca con paginaci�n
	@Override
	public List<ProdCost> buscar(ProdCost prodCost, Integer pagina) {
		
		EntityGraph<?> prodCostGraph = this.entityManager.getEntityGraph("prodCost.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<ProdCost> query = builder.createQuery(ProdCost.class);
		Root<ProdCost> prodCostRoot = query.from(ProdCost.class);

		query.orderBy(builder.asc(prodCostRoot.get("prodCostId")));
		TypedQuery<ProdCost> consulta = this.entityManager
				.createQuery(query.select(prodCostRoot).where(getSearchPredicates(prodCostRoot, prodCost)));
		consulta.setHint("jakarta.persistence.loadgraph", prodCostGraph);

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
	public long contarRegistros(ProdCost prodCost) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<ProdCost> prodCostRoot = countQuery.from(ProdCost.class);

		countQuery = countQuery.select(builder.count(prodCostRoot)).where(getSearchPredicates(prodCostRoot, prodCost));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<ProdCost> prodCostRoot, ProdCost prodCost) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer sucursalId = prodCost.getSucursal().getSucursalId();
		if (sucursalId != null) {
			predicates.add(builder.equal(prodCostRoot.get("sucursal").get("sucursalId"), sucursalId));
		}
		
		Integer productoId = prodCost.getProducto().getProductoId();
		if (productoId != null) {
			predicates.add(builder.equal(prodCostRoot.get("producto").get("productoId"), productoId));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(ProdCost prodCost) throws Exception {
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	@Override
	public List<ProdCost> buscar(List<Integer> sucursals, Producto producto ){
		
		EntityGraph<?> prodCostGraph = this.entityManager.getEntityGraph("prodCost.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<ProdCost> query = builder.createQuery(ProdCost.class);
		Root<ProdCost> prodCostRoot = query.from(ProdCost.class);

		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if (sucursals.size() != 0) {
			Expression<Integer> expression = prodCostRoot.get("sucursal").<Integer> get("sucursalId");
			Predicate predicate = expression.in(sucursals);
			predicates.add(predicate);
		}

		Integer productoId = producto.getProductoId();
		if (productoId != null) {
			predicates.add(builder.equal(prodCostRoot.get("producto").get("productoId"), productoId));
		}
		
		query.orderBy(builder.asc(prodCostRoot.get("prodCostId")));
		TypedQuery<ProdCost> consulta = this.entityManager
				.createQuery(query.select(prodCostRoot).where(predicates.toArray(new Predicate[predicates.size()])));
		consulta.setHint("jakarta.persistence.loadgraph", prodCostGraph);

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