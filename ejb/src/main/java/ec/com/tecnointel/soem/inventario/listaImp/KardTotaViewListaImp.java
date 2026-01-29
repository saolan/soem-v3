package ec.com.tecnointel.soem.inventario.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.inventario.listaInt.KardTotaViewListaInt;
import ec.com.tecnointel.soem.inventario.modelo.KardTotaView;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class KardTotaViewListaImp extends GestorListaSoem<KardTotaView> implements KardTotaViewListaInt, Serializable {

	private static final long serialVersionUID = -2452844398095390620L;

	// Busca con paginaci?n
	@Override
	public List<KardTotaView> buscar(KardTotaView kardTotaView, Integer pagina) {

		// EntityGraph<?> KardexGraph =
		// this.entityManager.getEntityGraph("KardTotaView.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<KardTotaView> query = builder.createQuery(KardTotaView.class);
		Root<KardTotaView> kardTotaViewRoot = query.from(KardTotaView.class);

		query.orderBy(builder.asc(kardTotaViewRoot.get("sucuDescri")));
		TypedQuery<KardTotaView> consulta = this.entityManager
				.createQuery(query.select(kardTotaViewRoot).where(getSearchPredicates(kardTotaViewRoot, kardTotaView)));
		// consulta.setHint("jakarta.persistence.loadgraph", KardexGraph);

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(KardTotaView kardTotaView) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<KardTotaView> kardTotaViewRoot = countQuery.from(KardTotaView.class);

		countQuery = countQuery.select(builder.count(kardTotaViewRoot)).where(getSearchPredicates(kardTotaViewRoot, kardTotaView));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<KardTotaView> kardTotaViewRoot, KardTotaView kardTotaView) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer sucursalId = kardTotaView.getSucursalId();
		if (sucursalId != null) {
			predicates.add(builder.equal(kardTotaViewRoot.get("sucursalId"), sucursalId));
		}

		Integer bodegaId = kardTotaView.getBodegaId();
		if (bodegaId != null) {
			predicates.add(builder.equal(kardTotaViewRoot.get("bodegaId"), bodegaId));
		}

		Integer productoId = kardTotaView.getProductoId();
		if (productoId != null) {
			predicates.add(builder.equal(kardTotaViewRoot.get("productoId"), productoId));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	@Override
	public List<KardTotaView> buscar(List<Integer> sucursals, KardTotaView kardTotaView){
		
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<KardTotaView> query = builder.createQuery(KardTotaView.class);
		Root<KardTotaView> kardTotaViewRoot = query.from(KardTotaView.class);

		List<Predicate> predicates = new ArrayList<Predicate>();
				
		Integer productoId = kardTotaView.getProductoId();
		if (productoId != null) {
			predicates.add(builder.equal(kardTotaViewRoot.get("productoId"), productoId));
		}

		if (sucursals.size() != 0) {
			Expression<Integer> expression = kardTotaViewRoot.get("sucursalId");
			Predicate predicate = expression.in(sucursals);
			predicates.add(predicate);
		}
		
//		Falta poner como parametro de busqueda a la bodega

		query.orderBy(builder.asc(kardTotaViewRoot.get("sucuDescri")));
		TypedQuery<KardTotaView> consulta = this.entityManager
				.createQuery(query.select(kardTotaViewRoot).where(predicates.toArray(new Predicate[predicates.size()])));
		
		return consulta.getResultList();
	}

	// private List<KardTotaView> calcularTotales(Producto producto) {
	// return (List<KardTotaView>) entityManager
	// .createQuery(
	// "select p.descri, "
	// +
	// "COALESCE(sum(id.cantid * d.factor),0) - COALESCE(sum(ed.cantid *
	// d.factor),0) as Cantidad_Saldo, "
	// +
	// "COALESCE(sum(id.cantid * id.costo * d.factor),0) -
	// COALESCE(sum(ed.cantid * ed.costo * d.factor),0) as Total_Saldo, "
	// +
	// "(COALESCE(sum(id.cantid * id.costo * d.factor),0) -
	// COALESCE(sum(ed.cantid * ed.costo * d.factor),0)) /
	// (COALESCE(sum(id.cantid * d.factor),0) - COALESCE(sum(ed.cantid *
	// d.factor),0)) as Costo "
	// + "from kardTotaView k "
	// + "inner join documento d on d.documento_id = k.documento_id "
	// + "inner join producto p on k.producto_id = p.producto_id "
	// + "left join ingr_deta id on id.ingr_deta_id = k.ingr_deta_id "
	// + "left join egre_deta ed on ed.egre_deta_id = k.egre_deta_id "
	// + "group by p.producto_id")
	// .setParameter("productoId", producto.getProductoId())
	// .getSingleResult();
	// }

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