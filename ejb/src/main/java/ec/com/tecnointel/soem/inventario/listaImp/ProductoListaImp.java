package ec.com.tecnointel.soem.inventario.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.inventario.listaInt.ProductoListaInt;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class ProductoListaImp extends GestorListaSoem<Producto> implements ProductoListaInt, Serializable {
	
	String orden;
	
	private static final long serialVersionUID = -6503530244655187986L;
	
	// Busca con paginacion
	@Override
	public List<Producto> buscar(Producto producto, Integer pagina, Integer filas) {
		
		EntityGraph<?> productoGraph = this.entityManager.getEntityGraph("producto.Graph");
		
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Producto> query = builder.createQuery(Producto.class);
		Root<Producto> productoRoot = query.from(Producto.class);
		
		query.orderBy(builder.asc(productoRoot.get(orden)));
		TypedQuery<Producto> consulta = this.entityManager
				.createQuery(query.select(productoRoot).where(getSearchPredicates(productoRoot, producto)));
		consulta.setHint("jakarta.persistence.loadgraph", productoGraph);

		// Si se pasa null a pagina se listan todos los datos de acuerdo a parametros
		// Es decir no realiza paginacion
		if (pagina != null) {
			consulta.setFirstResult(pagina * filas).setMaxResults(filas);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(Producto producto) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<Producto> productoRoot = countQuery.from(Producto.class);

		countQuery = countQuery.select(builder.count(productoRoot)).where(getSearchPredicates(productoRoot, producto));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<Producto> productoRoot, Producto producto) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer productoId = producto.getProductoId();
		if (productoId != null) {
			predicates.add(builder.equal(productoRoot.<Integer> get("productoId"), productoId));
		}
		
		String codigoBarra = producto.getCodigoBarra();
		if (codigoBarra != null && !"".equals(codigoBarra)) {
			predicates.add(builder.equal(builder.lower(productoRoot.<String> get("codigoBarra")), codigoBarra.toLowerCase() ));
		}
		
		String codigo = producto.getCodigo();
		if (codigo != null && !"".equals(codigo)) {
			predicates.add(
					builder.like(builder.lower(productoRoot.<String>get("codigo")), codigo.toLowerCase()));
//			predicates.add(
//					builder.like(builder.lower(productoRoot.<String>get("codigo")), '%' + codigo.toLowerCase() + '%'));
			
		}

		String descri = producto.getDescri();
		if (descri != null && !"".equals(descri)) {
			predicates.add(
					builder.like(builder.lower(productoRoot.<String>get("descri")), '%' + descri.toLowerCase() + '%'));
		}
		
		String prodGrupTipo = producto.getProdGrup().getTipo();
		if (prodGrupTipo != null && !"".equals(prodGrupTipo) && !prodGrupTipo.equals("Todo")) {
			predicates.add(
					builder.equal(builder.lower(productoRoot.get("prodGrup").<String>get("tipo")), prodGrupTipo.toLowerCase()));
		}
		
		boolean estado = producto.isEstado();
		predicates.add(builder.equal(productoRoot.get("estado"), estado));

		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	@Override
	public void ordenar (String columna){
		this.orden = columna;
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