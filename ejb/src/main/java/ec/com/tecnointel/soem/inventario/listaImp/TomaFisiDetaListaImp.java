package ec.com.tecnointel.soem.inventario.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.inventario.listaInt.TomaFisiDetaListaInt;
import ec.com.tecnointel.soem.inventario.modelo.TomaFisiDeta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class TomaFisiDetaListaImp extends GestorListaSoem<TomaFisiDeta> implements TomaFisiDetaListaInt, Serializable {
	
	private static final long serialVersionUID = 1598899697924061846L;

	@Override
	public List<TomaFisiDeta> buscar(TomaFisiDeta tomaFisiDeta, Integer pagina) {
		
		EntityGraph<?> tomaFisiDetaGraph = this.entityManager.getEntityGraph("tomaFisiDeta.Graph");
		
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<TomaFisiDeta> query = builder.createQuery(TomaFisiDeta.class);
		Root<TomaFisiDeta> productoRoot = query.from(TomaFisiDeta.class);
		
		query.orderBy(builder.asc(productoRoot.get("tomaFisiDetaId")));
		TypedQuery<TomaFisiDeta> consulta = this.entityManager
				.createQuery(query.select(productoRoot).where(getSearchPredicates(productoRoot, tomaFisiDeta)));
		consulta.setHint("jakarta.persistence.loadgraph", tomaFisiDetaGraph);

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(TomaFisiDeta tomaFisiDeta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<TomaFisiDeta> productoRoot = countQuery.from(TomaFisiDeta.class);

		countQuery = countQuery.select(builder.count(productoRoot)).where(getSearchPredicates(productoRoot, tomaFisiDeta));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<TomaFisiDeta> tomaFisiDetaRoot, TomaFisiDeta tomaFisiDeta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer tomaFisiId = tomaFisiDeta.getTomaFisi().getTomaFisiId();
		if (tomaFisiId != null) {
			predicates.add(builder.equal(tomaFisiDetaRoot.get("tomaFisi").<Integer> get("tomaFisiId"), tomaFisiId));
		}
		
		Integer tomaFisiNumero = tomaFisiDeta.getTomaFisi().getNumero();
		if (tomaFisiNumero != null) {
			predicates.add(builder.equal(tomaFisiDetaRoot.get("tomaFisi").<Integer> get("numero"), tomaFisiNumero));
		}

		if (tomaFisiDeta.getTomaFisi().getSucursal() != null) {
			Integer sucursalId = tomaFisiDeta.getTomaFisi().getSucursal().getSucursalId();
			if (sucursalId != null) {
				predicates.add(builder.equal(tomaFisiDetaRoot.get("tomaFisi").get("sucursal").<Integer> get("sucursalId"), sucursalId));
			}
		}

		if (tomaFisiDeta.getProducto() != null) {
			Integer productoId = tomaFisiDeta.getProducto().getProductoId();
			if (productoId != null) {
				predicates.add(builder.equal(tomaFisiDetaRoot.get("producto").<Integer> get("productoId"), productoId));
			}
		}
		
		
//		String codigo = tomaFisiDeta.getCodigo();
//		if (codigo != null && !"".equals(codigo)) {
//			predicates.add(
//					builder.like(builder.lower(productoRoot.<String>get("codigo")), '%' + codigo.toLowerCase() + '%'));
//		}
//
//		String descri = tomaFisiDeta.getDescri();
//		if (descri != null && !"".equals(descri)) {
//			predicates.add(
//					builder.like(builder.lower(productoRoot.<String>get("descri")), '%' + descri.toLowerCase() + '%'));
//		}
//		
//		String prodGrupTipo = tomaFisiDeta.getProdGrup().getTipo();
//		if (prodGrupTipo != null && !"".equals(prodGrupTipo) && !prodGrupTipo.equals("Todo")) {
//			predicates.add(
//					builder.equal(builder.lower(productoRoot.get("prodGrup").<String>get("tipo")), prodGrupTipo.toLowerCase()));
//		}
//		
//		boolean estado = tomaFisiDeta.isEstado();
//		predicates.add(builder.equal(productoRoot.get("estado"), estado));

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}