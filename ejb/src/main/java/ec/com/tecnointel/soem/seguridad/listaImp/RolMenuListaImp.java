package ec.com.tecnointel.soem.seguridad.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.seguridad.listaInt.RolMenuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolMenu;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class RolMenuListaImp extends GestorListaSoem<RolMenu> implements RolMenuListaInt, Serializable {

	private static final long serialVersionUID = -3615095344689294323L;

	@Override
	public List<RolMenu> buscar(RolMenu rolMenu, Integer pagina) {

		EntityGraph<?> rolMenuGrap = this.entityManager.getEntityGraph("rolMenu.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<RolMenu> query = builder.createQuery(RolMenu.class);
		Root<RolMenu> rolMenuRoot = query.from(RolMenu.class);

		// Join<RolMenu, Rol> rolesJoin = rolMenuRoot.join("rol");
		// Root<Rol> rolRoot = query.from(Rol.class);
		// query.orderBy(builder.asc(rolesJoin.get("rolId")));

		query.orderBy(builder.asc(rolMenuRoot.join("rol").get("rolId")));

		TypedQuery<RolMenu> consulta = this.entityManager
				.createQuery(query.select(rolMenuRoot).where(getSearchPredicates(rolMenuRoot, rolMenu)));
		consulta.setHint("jakarta.persistence.loadgraph", rolMenuGrap);

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(RolMenu rolMenu) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<RolMenu> rolMenuRoot = countQuery.from(RolMenu.class);

		countQuery = countQuery.select(builder.count(rolMenuRoot)).where(getSearchPredicates(rolMenuRoot, rolMenu));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<RolMenu> rolMenuRoot, RolMenu rolMenu) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer rolId = rolMenu.getRol().getRolId();
		if (rolId != null) {
			predicates.add(builder.equal(rolMenuRoot.join("rol").get("rolId"), rolId));
		}
		
		String rolDescri = rolMenu.getRol().getDescri();
		if (rolDescri != null) {
			predicates.add(
					builder.like(builder.lower(rolMenuRoot.get("rol").<String> get("descri")), '%' + rolDescri.toLowerCase() + '%'));
		}
		
		String menuDescri = rolMenu.getMenu().getDescri();
		if (menuDescri != null) {
			predicates.add(
					builder.like(builder.lower(rolMenuRoot.get("menu").<String> get("descri")), '%' + menuDescri.toLowerCase() + '%'));
		}

		Boolean acceso = rolMenu.getAcceso();
		if (acceso != null) {
			predicates.add(builder.equal(rolMenuRoot.get("acceso"), acceso));
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
	public List<RolMenu> buscar(RolMenu rolMenu, Rol rol) throws Exception {

		EntityGraph<?> rolMenuGrap = this.entityManager.getEntityGraph("rolMenu.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<RolMenu> query = builder.createQuery(RolMenu.class);
		Root<RolMenu> rolMenuRoot = query.from(RolMenu.class);

		query.orderBy(builder.asc(rolMenuRoot.join("rol").get("rolId")));

		TypedQuery<RolMenu> consulta = this.entityManager
				.createQuery(query.select(rolMenuRoot).where(getSearchPredicates(rolMenuRoot, rolMenu, rol)));
		consulta.setHint("jakarta.persistence.loadgraph", rolMenuGrap);

		return consulta.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<RolMenu> rolMenuRoot, RolMenu rolMenu, Rol rol) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer rolId = rol.getRolId();
		if (rolId != null) {
			predicates.add(builder.equal(rolMenuRoot.join("rol").get("rolId"), rolId));
		}

		Boolean acceso = rolMenu.getAcceso();
		if (acceso != null) {
			predicates.add(builder.equal(rolMenuRoot.get("acceso"), acceso));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(RolMenu rolMenu) throws Exception {
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