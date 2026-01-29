package ec.com.tecnointel.soem.inventario.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.inventario.listaInt.ProdSubpListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdSubp;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class ProdSubpListaImp extends GestorListaSoem<ProdSubp> implements ProdSubpListaInt, Serializable {
	
	private static final long serialVersionUID = -3543861743260604128L;

	@Override
	public List<ProdSubp> buscar(ProdSubp prodSubp, Integer pagina) {
		
		EntityGraph<?> prodSubpGraph = this.entityManager.getEntityGraph("prodSubp.Graph");
		
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<ProdSubp> query = builder.createQuery(ProdSubp.class);
		Root<ProdSubp> productoRoot = query.from(ProdSubp.class);
		
		query.orderBy(builder.asc(productoRoot.get("prodSubpId")));
		TypedQuery<ProdSubp> consulta = this.entityManager
				.createQuery(query.select(productoRoot).where(getSearchPredicates(productoRoot, prodSubp)));
		consulta.setHint("jakarta.persistence.loadgraph", prodSubpGraph);

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(ProdSubp prodSubp) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<ProdSubp> productoRoot = countQuery.from(ProdSubp.class);

		countQuery = countQuery.select(builder.count(productoRoot)).where(getSearchPredicates(productoRoot, prodSubp));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<ProdSubp> prodSubpRoot, ProdSubp prodSubp) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

//		Integer tomaFisiId = prodSubp.getTomaFisi().getTomaFisiId();
//		if (tomaFisiId != null) {
//			predicates.add(builder.equal(prodSubpRoot.get("tomaFisi").<Integer> get("tomaFisiId"), tomaFisiId));
//		}
//		
//		Integer tomaFisiNumero = prodSubp.getTomaFisi().getNumero();
//		if (tomaFisiNumero != null) {
//			predicates.add(builder.equal(prodSubpRoot.get("tomaFisi").<Integer> get("numero"), tomaFisiNumero));
//		}
//
//		if (prodSubp.getTomaFisi().getSucursal() != null) {
//			Integer sucursalId = prodSubp.getTomaFisi().getSucursal().getSucursalId();
//			if (sucursalId != null) {
//				predicates.add(builder.equal(prodSubpRoot.get("tomaFisi").get("sucursal").<Integer> get("sucursalId"), sucursalId));
//			}
//		}

		if (prodSubp.getProducto() != null) {
			Integer productoId = prodSubp.getProducto().getProductoId();
			if (productoId != null) {
				predicates.add(builder.equal(prodSubpRoot.get("producto").<Integer> get("productoId"), productoId));
			}
		}
		
		
//		String codigo = prodSubp.getCodigo();
//		if (codigo != null && !"".equals(codigo)) {
//			predicates.add(
//					builder.like(builder.lower(productoRoot.<String>get("codigo")), '%' + codigo.toLowerCase() + '%'));
//		}
//
//		String descri = prodSubp.getDescri();
//		if (descri != null && !"".equals(descri)) {
//			predicates.add(
//					builder.like(builder.lower(productoRoot.<String>get("descri")), '%' + descri.toLowerCase() + '%'));
//		}
//		
//		String prodGrupTipo = prodSubp.getProdGrup().getTipo();
//		if (prodGrupTipo != null && !"".equals(prodGrupTipo) && !prodGrupTipo.equals("Todo")) {
//			predicates.add(
//					builder.equal(builder.lower(productoRoot.get("prodGrup").<String>get("tipo")), prodGrupTipo.toLowerCase()));
//		}
//		
//		boolean estado = prodSubp.isEstado();
//		predicates.add(builder.equal(productoRoot.get("estado"), estado));

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}