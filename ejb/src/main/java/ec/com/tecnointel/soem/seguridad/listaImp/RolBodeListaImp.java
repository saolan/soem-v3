package ec.com.tecnointel.soem.seguridad.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.listaInt.RolBodeListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.RolBode;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class RolBodeListaImp extends GestorListaSoem<RolBode> implements RolBodeListaInt, Serializable {

	private static final long serialVersionUID = -940074503096504009L;

	@Override
	public List<RolBode> buscar(RolBode rolBode, Integer pagina) {

		EntityGraph<?> rolBodeGraph = this.entityManager.getEntityGraph("rolBode.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<RolBode> query = builder.createQuery(RolBode.class);
		Root<RolBode> rolBodeRoot = query.from(RolBode.class);

		query.orderBy(builder.asc(rolBodeRoot.get("rolBodeId")));
		TypedQuery<RolBode> consulta = this.entityManager
				.createQuery(query.select(rolBodeRoot).where(getSearchPredicates(rolBodeRoot, rolBode)));
		consulta.setHint("jakarta.persistence.loadgraph", rolBodeGraph);

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(RolBode rolBode) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<RolBode> rolBodeRoot = countQuery.from(RolBode.class);

		countQuery = countQuery.select(builder.count(rolBodeRoot)).where(getSearchPredicates(rolBodeRoot, rolBode));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<RolBode> rolBodeRoot, RolBode rolBode) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Integer rolId = rolBode.getRol().getRolId();
		if (rolId != null) {
			predicates.add(builder.equal(rolBodeRoot.get("rol").<Integer> get("rolId"), rolId));
		}

		String rolDescri = rolBode.getRol().getDescri();
		if (rolDescri != null) {
			predicates.add(
					builder.like(builder.lower(rolBodeRoot.get("rol").<String>get("descri")), rolDescri.toLowerCase()));
		}

		Integer bodegaId = rolBode.getBodega().getBodegaId();
		if (bodegaId != null) {
			predicates.add(builder.equal(rolBodeRoot.get("bodega").<Integer> get("bodegaId"), bodegaId));
		}

		String bodeDescri = rolBode.getBodega().getDescri();
		if (bodeDescri != null) {
			predicates.add(builder.like(builder.lower(rolBodeRoot.get("rol").get("bodega").<String> get("descri")),
					bodeDescri.toLowerCase()));
		}

		Integer sucursalId = rolBode.getSucursal().getSucursalId();
		if (sucursalId != null) {
			predicates.add(builder.equal(rolBodeRoot.get("sucursal").<Integer> get("sucursalId"), sucursalId));
		}

		String sucursalDescri = rolBode.getSucursal().getDescri();
		if (sucursalDescri != null) {
			predicates.add(builder.like(builder.lower(rolBodeRoot.get("sucursal").<String>get("descri")),
					sucursalDescri.toLowerCase()));
		}

		Boolean predet = rolBode.getPredet();
		if (predet != null){
			predicates.add(builder.equal(rolBodeRoot.get("predet"), predet));			
		}

		Boolean acceso = rolBode.getAcceso();
		if(acceso != null){
			predicates.add(builder.equal(rolBodeRoot.get("acceso"), acceso));	
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(RolBode rolBode) throws Exception {
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	@Override
	public List<RolBode> buscar(Sucursal sucursal, List<Integer> rols, RolBode rolBode) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<RolBode> query = builder.createQuery(RolBode.class);
		Root<RolBode> rolBodeRoot = query.from(RolBode.class);

		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Integer sucursalId = sucursal.getSucursalId();
		if (sucursalId != null) {
			predicates.add(builder.equal(rolBodeRoot.get("sucursal").<Integer> get("sucursalId"), sucursalId));
		}
		
		Boolean acceso = rolBode.getAcceso();
		if(acceso != null){
			predicates.add(builder.equal(rolBodeRoot.get("acceso"), acceso)); 
		}

		if (rols.size() != 0){
			Expression<Integer> expression = rolBodeRoot.get("rol").<Integer> get("rolId");
			Predicate predicate = expression.in(rols);
			predicates.add(predicate);
		}
		
		query.orderBy(builder.asc(rolBodeRoot.get("rolBodeId")));
		TypedQuery<RolBode> consulta = this.entityManager
				.createQuery(query.select(rolBodeRoot).where(predicates.toArray(new Predicate[predicates.size()])));

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