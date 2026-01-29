package ec.com.tecnointel.soem.inventario.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.inventario.listaInt.KardexListaInt;
import ec.com.tecnointel.soem.inventario.modelo.Kardex;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class KardexListaImp extends GestorListaSoem<Kardex> implements KardexListaInt, Serializable {

	private static final long serialVersionUID = -2017259298538777302L;

	// Busca con paginaci?n
	@Override
	public List<Kardex> buscar(Kardex kardex, Integer pagina) {

		// EntityGraph<?> KardexGraph =
		// this.entityManager.getEntityGraph("Kardex.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Kardex> query = builder.createQuery(Kardex.class);
		Root<Kardex> kardexRoot = query.from(Kardex.class);

		query.orderBy(builder.asc(kardexRoot.get("kardexId")));
		TypedQuery<Kardex> consulta = this.entityManager
				.createQuery(query.select(kardexRoot).where(getSearchPredicates(kardexRoot, kardex)));
		// consulta.setHint("jakarta.persistence.loadgraph", KardexGraph);

		// Si se pasa null a pagina se listan todos los datos de acuerdo a
		// parametros
		// Es decir no realiza paginaci?n
		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(Kardex kardex) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<Kardex> kardexRoot = countQuery.from(Kardex.class);

		countQuery = countQuery.select(builder.count(kardexRoot)).where(getSearchPredicates(kardexRoot, kardex));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<Kardex> kardexRoot, Kardex kardex) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer kardexId = kardex.getKardexId();
		if (kardexId != null) {
			predicates.add(builder.equal(kardexRoot.get("kardexId"), kardexId));
		}

		Integer egreDetaId = kardex.getEgreDeta().getEgreDetaId();
		if (egreDetaId != null) {
			predicates.add(builder.equal(kardexRoot.get("egreDeta").get("egreDetaId"), egreDetaId));
		}

		Integer ingrDetaId = kardex.getIngrDeta().getIngrDetaId();
		if (ingrDetaId != null) {
			predicates.add(builder.equal(kardexRoot.get("ingrDeta").get("ingrDetaId"), ingrDetaId));
		}

//		String codigoBarra = kardex.getProducto().getCodigoBarra();
//		if (codigoBarra != null && !"".equals(codigoBarra)) {
//			predicates.add(builder.equal(
//					builder.lower(kardexRoot.get("producto").<String> get(
//							"codigoBarra")), codigoBarra.toLowerCase()));
//		}
//
//		String codigo = kardex.getProducto().getCodigo();
//		if (codigo != null && !"".equals(codigo)) {
//			predicates.add(builder.like(
//					builder.lower(kardexRoot.get("producto").<String> get(
//							"codigo")), '%' + codigo.toLowerCase() + '%'));
//		}
//
//		String descri = kardex.getProducto().getDescri();
//		if (descri != null && !"".equals(descri)) {
//			predicates.add(builder.like(
//					builder.lower(kardexRoot.get("producto").<String> get(
//							"descri")), '%' + descri.toLowerCase() + '%'));
//		}
//
//		boolean estado = kardex.getProducto().isEstado();
//		predicates.add(builder.equal(kardexRoot.get("producto").get("estado"),
//				estado));

		return predicates.toArray(new Predicate[predicates.size()]);
	}

//	Este metodo se usa para validar al borrar un producto
	@Override
	public List<Kardex> buscar(Kardex kardex) {

		
		List<Predicate> predicates = new ArrayList<Predicate>();

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Kardex> query = builder.createQuery(Kardex.class);
		Root<Kardex> kardexRoot = query.from(Kardex.class);
		
		Integer productoId = kardex.getProducto().getProductoId();
		if (productoId != null) {
			predicates.add(builder.equal(kardexRoot.get("producto").get("productoId"), productoId));
		}

		query.orderBy(builder.asc(kardexRoot.get("kardexId")));
		
		TypedQuery<Kardex> consulta = this.entityManager
				.createQuery(query.select(kardexRoot).where(predicates.toArray(new Predicate[predicates.size()])));
		
		return consulta.getResultList();
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//	private List<Kardex> calcularTotales(Producto producto) {
//		return (List<Kardex>) entityManager
//				.createQuery(
//						"select p.descri, "
//						+ "COALESCE(sum(id.cantid * d.factor),0) - COALESCE(sum(ed.cantid * d.factor),0) as Cantidad_Saldo, "
//						+ "COALESCE(sum(id.cantid * id.costo * d.factor),0) -  COALESCE(sum(ed.cantid * ed.costo * d.factor),0) as Total_Saldo, "
//						+ "(COALESCE(sum(id.cantid * id.costo * d.factor),0) -  COALESCE(sum(ed.cantid * ed.costo * d.factor),0)) / (COALESCE(sum(id.cantid * d.factor),0) - COALESCE(sum(ed.cantid * d.factor),0)) as Costo "
//						+ "from kardex k "
//						+ "inner join documento d on d.documento_id = k.documento_id "
//						+ "inner join producto p on k.producto_id = p.producto_id "
//						+ "left join ingr_deta id on id.ingr_deta_id = k.ingr_deta_id "
//						+ "left join egre_deta ed on ed.egre_deta_id = k.egre_deta_id "
//						+ "group by p.producto_id")
//				.setParameter("productoId", producto.getProductoId())
//				.getSingleResult();
//	}

	@Override
	public Integer saldoProducto(Kardex kardex) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Integer> sumQuery = builder.createQuery(Integer.class);
		Root<Kardex> kardexRoot = sumQuery.from(Kardex.class);

		sumQuery = sumQuery.select(builder.sum(kardexRoot.<Integer>get("kardexId")))
				.where(getSearchPredicates(kardexRoot, kardex));
		return this.entityManager.createQuery(sumQuery).getSingleResult();

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