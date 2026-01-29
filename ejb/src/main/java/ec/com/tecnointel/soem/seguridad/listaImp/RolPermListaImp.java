package ec.com.tecnointel.soem.seguridad.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.seguridad.listaInt.RolPermListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.RolPerm;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class RolPermListaImp extends GestorListaSoem<RolPerm> implements RolPermListaInt, Serializable {

	private static final long serialVersionUID = -1155988989319914752L;

	@Override
	public List<RolPerm> buscar(RolPerm rolPerm, Integer pagina) {

		EntityGraph<?> rolPermGraph = this.entityManager.getEntityGraph("rolPerm.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<RolPerm> query = builder.createQuery(RolPerm.class);
		Root<RolPerm> auditoriaRoot = query.from(RolPerm.class);

		query.orderBy(builder.asc(auditoriaRoot.get("rolPermId")));
		TypedQuery<RolPerm> consulta = this.entityManager
				.createQuery(query.select(auditoriaRoot).where(getSearchPredicates(auditoriaRoot, rolPerm)));
		consulta.setHint("jakarta.persistence.loadgraph", rolPermGraph);
		
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
	public long contarRegistros(RolPerm rolPerm) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<RolPerm> auditoriaRoot = countQuery.from(RolPerm.class);

		countQuery = countQuery.select(builder.count(auditoriaRoot)).where(getSearchPredicates(auditoriaRoot, rolPerm));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<RolPerm> rolPermRoot, RolPerm rolPerm) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();


		Integer rolId = rolPerm.getRol().getRolId();
		if (rolId != null) {
			predicates.add(builder.equal(rolPermRoot.get("rol").<Integer> get("rolId"), rolId));
		}

		Integer permisoId = rolPerm.getPermiso().getPermisoId();
		if (permisoId != null) {
			predicates.add(builder.equal(rolPermRoot.get("permiso").<Integer> get("permisoId"), permisoId));
		}

		Boolean acceso = rolPerm.getAcceso();
		if(acceso != null){
			predicates.add(builder.equal(rolPermRoot.get("acceso"), acceso));	
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(RolPerm rolPerm) throws Exception {
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

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