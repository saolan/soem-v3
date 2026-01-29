package ec.com.tecnointel.soem.inventario.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.inventario.listaInt.ProdPrecListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdPrec;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.parametro.modelo.Precio;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class ProdPrecListaImp extends GestorListaSoem<ProdPrec> implements ProdPrecListaInt, Serializable {

	String orden = "descri";

	private static final long serialVersionUID = -3064378996590398010L;

	// Busca con paginación
	@Override
	public List<ProdPrec> buscar(ProdPrec prodPrec, Integer pagina, Integer filas) {

		EntityGraph<?> prodPrecGraph = this.entityManager.getEntityGraph("prodPrec.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<ProdPrec> query = builder.createQuery(ProdPrec.class);
		Root<ProdPrec> prodPrecRoot = query.from(ProdPrec.class);

		query.orderBy(builder.asc(prodPrecRoot.get("producto").get(orden)),
				builder.asc(prodPrecRoot.get("precio").get("codigo")));
		TypedQuery<ProdPrec> consulta = this.entityManager
				.createQuery(query.select(prodPrecRoot).where(getSearchPredicates(prodPrecRoot, prodPrec)));
		consulta.setHint("jakarta.persistence.loadgraph", prodPrecGraph);

		// Si se pasa null a pagina se listan todos los datos de acuerdo a parametros
		// Es decir no realiza paginaci�n
		if (pagina != null) {
			consulta.setFirstResult(pagina * filas).setMaxResults(filas);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(ProdPrec prodPrec) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<ProdPrec> prodPrecRoot = countQuery.from(ProdPrec.class);

		countQuery = countQuery.select(builder.count(prodPrecRoot)).where(getSearchPredicates(prodPrecRoot, prodPrec));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<ProdPrec> prodPrecRoot, ProdPrec prodPrec) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer sucursalId = prodPrec.getSucursal().getSucursalId();
		if (sucursalId != null) {
			predicates.add(builder.equal(prodPrecRoot.get("sucursal").get("sucursalId"), sucursalId));
		}

		Integer precioId = prodPrec.getPrecio().getPrecioId();
		if (precioId != null) {
			predicates.add(builder.equal(prodPrecRoot.get("precio").<Integer>get("precioId"), precioId));
		}

		Integer productoId = prodPrec.getProducto().getProductoId();
		if (productoId != null) {
			predicates.add(builder.equal(prodPrecRoot.get("producto").get("productoId"), productoId));
		}

		String codigoBarra = prodPrec.getProducto().getCodigoBarra();
		if (codigoBarra != null && !"".equals(codigoBarra)) {
			predicates.add(builder.equal(builder.lower(prodPrecRoot.get("producto").<String>get("codigoBarra")),
					codigoBarra.toLowerCase()));
		}

		String codigo = prodPrec.getProducto().getCodigo();
		if (codigo != null && !"".equals(codigo)) {
			predicates.add(builder.like(builder.lower(prodPrecRoot.get("producto").<String>get("codigo")),
					codigo.toLowerCase()));
//			predicates.add(builder.like(builder.lower(prodPrecRoot.get("producto").<String>get("codigo")),
//					'%' + codigo.toLowerCase() + '%'));

		}

		String descri = prodPrec.getProducto().getDescri();
		if (descri != null && !"".equals(descri)) {
			predicates.add(builder.like(builder.lower(prodPrecRoot.get("producto").<String>get("descri")),
					'%' + descri.toLowerCase() + '%'));
		}

		String prodGrupTipo = prodPrec.getProducto().getProdGrup().getTipo();
		if (prodGrupTipo != null && !"".equals(prodGrupTipo) && !prodGrupTipo.equals("Todo")) {
			predicates
					.add(builder.equal(builder.lower(prodPrecRoot.get("producto").get("prodGrup").<String>get("tipo")),
							prodGrupTipo.toLowerCase()));
		}
		
		boolean moduloComp = prodPrec.getProducto().getProdGrup().isModuloComp();
		if (moduloComp) {
			predicates.add(builder.equal(prodPrecRoot.get("producto").get("prodGrup").get("moduloComp"), moduloComp));	
		}

		boolean moduloVent = prodPrec.getProducto().getProdGrup().isModuloVent();
		if (moduloVent) {
			predicates.add(builder.equal(prodPrecRoot.get("producto").get("prodGrup").get("moduloVent"), moduloVent));	
		}

		boolean estado = prodPrec.getProducto().isEstado();
		predicates.add(builder.equal(prodPrecRoot.get("producto").get("estado"), estado));

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private Predicate[] getSearchPredicates(Root<ProdPrec> prodPrecRoot, Set<Sucursal> sucursals, Set<Precio> precios,
			ProdPrec prodPrec) {

		List<Integer> sucursalIds = new ArrayList<Integer>();
		List<Integer> precioIds = new ArrayList<Integer>();

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		// Recorrer sucursals porque se envia la lista como parametro para la clausula
		// IN
		for (Sucursal sucursal : sucursals) {
			// if (sucursal.esEstado()){
			sucursalIds.add(sucursal.getSucursalId());
			// }
		}

		// Recorrer precios porque se envia la lista como parametro para la clausula IN
		for (Precio precio : precios) {
			if (precio.isEstado()) {
				precioIds.add(precio.getPrecioId());
			}
		}

		if (sucursals.size() != 0) {
			Expression<Integer> expression = prodPrecRoot.get("sucursal").<Integer>get("sucursalId");
			Predicate predicate = expression.in(sucursalIds);
			predicates.add(predicate);
		}

		if (precios.size() != 0) {
			Expression<Integer> expression = prodPrecRoot.get("precio").<Integer>get("precioId");
			Predicate predicate = expression.in(precioIds);
			predicates.add(predicate);
		}

		String codigoBarra = prodPrec.getProducto().getCodigoBarra();
		if (codigoBarra != null && !"".equals(codigoBarra)) {
			predicates.add(builder.equal(builder.lower(prodPrecRoot.get("producto").<String>get("codigoBarra")),
					codigoBarra.toLowerCase()));
		}

		String codigo = prodPrec.getProducto().getCodigo();
		if (codigo != null && !"".equals(codigo)) {
			predicates.add(builder.like(builder.lower(prodPrecRoot.get("producto").<String>get("codigo")),
					'%' + codigo.toLowerCase() + '%'));
		}

		String descri = prodPrec.getProducto().getDescri();
		if (descri != null && !"".equals(descri)) {
			predicates.add(builder.like(builder.lower(prodPrecRoot.get("producto").<String>get("descri")),
					'%' + descri.toLowerCase() + '%'));
		}

		String prodGrupTipo = prodPrec.getProducto().getProdGrup().getTipo();
		if (prodGrupTipo != null && !"".equals(prodGrupTipo) && !prodGrupTipo.equals("Todo")) {
			predicates
					.add(builder.equal(builder.lower(prodPrecRoot.get("producto").get("prodGrup").<String>get("tipo")),
							prodGrupTipo.toLowerCase()));
		}

		boolean estado = prodPrec.getProducto().isEstado();
		predicates.add(builder.equal(prodPrecRoot.get("producto").get("estado"), estado));

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(ProdPrec prodPrec) throws Exception {
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	@Override
	public void ordenar(String columna) {
		this.orden = columna;
	}

	@Override
	public List<ProdPrec> filtrarProdPrec(List<ProdPrec> prodPrecs, PersUsua persUsuaSesion, List<RolPrec> rolPrecs,
			Sucursal sucursal) throws Exception {

		RolPrec rolPrec = new RolPrec();

		List<Rol> roles = new ArrayList<>();
//		List<RolPrec> rolPrecs = new ArrayList<>();
		List<ProdPrec> filtroProdPrecs = new ArrayList<>();

		Set<ProdPrec> filtroProdPrecSet = new HashSet<ProdPrec>();

//		rolPrecs = variablesSesion.getRolPrecs();
//		Sucursal sucursal = variablesSesion.getSucursal();

		Set<RolPersUsua> rolPersUsuas = persUsuaSesion.getRolPersUsuas();
		for (RolPersUsua rolPersUsua : rolPersUsuas) {
			roles.add(rolPersUsua.getRol());
		}

		for (Rol rol : roles) {
			for (ProdPrec prodPrec : prodPrecs) {
				rolPrec.setSucursal(sucursal);
				rolPrec.setRol(rol);
				rolPrec.setPrecio(prodPrec.getPrecio());
				if (rolPrecs.contains(rolPrec)) {
					filtroProdPrecSet.add(prodPrec);
				}
			}
		}

		filtroProdPrecs.addAll(filtroProdPrecSet);

		Collections.sort(filtroProdPrecs, new Comparator() {
			@Override
			public int compare(Object prodPrec1, Object prodPrec2) {
				return new String(((ProdPrec) prodPrec1).getPrecio().getCodigo())
						.compareTo(new String(((ProdPrec) prodPrec2).getPrecio().getCodigo()));
			}
		});

		return filtroProdPrecs;
	}

	@Override
	public List<ProdPrec> buscar(Sucursal sucursal, List<Integer> precios, Producto producto) {

		EntityGraph<?> prodPrecGraph = this.entityManager.getEntityGraph("prodPrec.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<ProdPrec> query = builder.createQuery(ProdPrec.class);
		Root<ProdPrec> prodPrecRoot = query.from(ProdPrec.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

//		if (sucursals.size() != 0) {
//			Expression<Integer> expression = prodPrecRoot.get("sucursal").<Integer> get("sucursalId");
//			Predicate predicate = expression.in(sucursals);
//			predicates.add(predicate);
//		}

		Integer sucursalId = sucursal.getSucursalId();
		if (sucursalId != null) {
			predicates.add(builder.equal(prodPrecRoot.get("sucursal").get("sucursalId"), sucursalId));
		}

		Integer productoId = producto.getProductoId();
		if (productoId != null) {
			predicates.add(builder.equal(prodPrecRoot.get("producto").get("productoId"), productoId));
		}

		if (precios.size() != 0) {
			Expression<Integer> expression = prodPrecRoot.get("precio").<Integer>get("precioId");
			Predicate predicate = expression.in(precios);
			predicates.add(predicate);
		}

		query.orderBy(builder.asc(prodPrecRoot.get("prodPrecId")));
		TypedQuery<ProdPrec> consulta = this.entityManager
				.createQuery(query.select(prodPrecRoot).where(predicates.toArray(new Predicate[predicates.size()])));
		consulta.setHint("jakarta.persistence.loadgraph", prodPrecGraph);

		return consulta.getResultList();

	}

	@Override
	public List<ProdPrec> buscar(Set<Sucursal> sucursals, Set<Precio> precios, ProdPrec prodPrec, Integer pagina,
			Integer filas) {

		EntityGraph<?> prodPrecGraph = this.entityManager.getEntityGraph("prodPrec.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<ProdPrec> query = builder.createQuery(ProdPrec.class);
		Root<ProdPrec> prodPrecRoot = query.from(ProdPrec.class);

		query.orderBy(builder.asc(prodPrecRoot.get("producto").get(orden)),
				builder.asc(prodPrecRoot.get("precio").get("codigo")),
				builder.asc(prodPrecRoot.get("sucursal").get("codigo")));
		TypedQuery<ProdPrec> consulta = this.entityManager.createQuery(
				query.select(prodPrecRoot).where(getSearchPredicates(prodPrecRoot, sucursals, precios, prodPrec)));

		consulta.setHint("jakarta.persistence.loadgraph", prodPrecGraph);

		if (pagina != null) {
			consulta.setFirstResult(pagina * filas).setMaxResults(filas);
		}

		return consulta.getResultList();

	}

	@Override
	public List<Object[]> buscarProdComps(Producto producto, List<RolPrec> rolPrecs) {

		String prodComps = "select s.descri as sucursal, pe.cedula_ruc as cedula_ruc, concat(pe.apelli, ' ' , pe.nombre) as proveedor, "
				+ "i.fecha_emis, i.numero as numero_fact, id.cantid as cantidad, id.costo as costo, id.descue as descue, id.costo_neto as costo_neto, "
				+ "idp.utilid as utilidad, idp.precio_sin_impu as precio_sin_Impu, idp.precio_con_impu as precio_con_impu "
				+ "from ingr_deta_prec idp " + "inner join ingr_deta id on id.ingr_deta_id = idp.ingr_deta_id "
				+ "inner join producto p on p.producto_id = id.producto_id "
				+ "inner join ingreso i on i.ingreso_id = id.ingreso_id "
				+ "inner join sucursal s on s.sucursal_id = i.sucursal_id "
				+ "inner join persona pe on pe.persona_id = i.persona_id "
				+ "where p.producto_id = ?1 and idp.precio_id in (?2) " + "order by i.fecha_emis desc " + "limit 12";

		ArrayList<Integer> precioIds = new ArrayList<>();

		for (RolPrec rolPrec : rolPrecs) {

			if (rolPrec.getPredet()) {
				precioIds.add(rolPrec.getPrecio().getPrecioId());
			}

		}

		Query query = (Query) this.entityManager.createNativeQuery(prodComps);
		query.setParameter(1, producto.getProductoId());
		query.setParameter(2, precioIds);

		return query.getResultList();

//		List<Object[]> cajas = (List<Object[]>) this.entityManager.createNativeQuery(consulta2).getResultList();
//		return cajas;     
	}

	@Override
	public long contarRegistros(Set<Sucursal> sucursals, Set<Precio> precios, ProdPrec prodPrec) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<ProdPrec> prodPrecRoot = countQuery.from(ProdPrec.class);

		countQuery = countQuery.select(builder.count(prodPrecRoot))
				.where(getSearchPredicates(prodPrecRoot, sucursals, precios, prodPrec));
		return this.entityManager.createQuery(countQuery).getSingleResult();
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