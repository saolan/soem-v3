package ec.com.tecnointel.soem.seguridad.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.listaInt.RolPrecListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class RolPrecListaImp extends GestorListaSoem<RolPrec> implements RolPrecListaInt, Serializable {

	private static final long serialVersionUID = 3081157021381895209L;

	@Override
	public List<RolPrec> buscar(RolPrec rolPrec, Integer pagina) {

		EntityGraph<?> rolPrecGraph = this.entityManager.getEntityGraph("rolPrec.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<RolPrec> query = builder.createQuery(RolPrec.class);
		Root<RolPrec> rolPrecRoot = query.from(RolPrec.class);

		query.orderBy(builder.asc(rolPrecRoot.get("rolPrecId")));
		TypedQuery<RolPrec> consulta = this.entityManager
				.createQuery(query.select(rolPrecRoot).where(getSearchPredicates(rolPrecRoot, rolPrec)));
		consulta.setHint("jakarta.persistence.loadgraph", rolPrecGraph);

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
	public long contarRegistros(RolPrec rolPrec) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<RolPrec> rolPrecRoot = countQuery.from(RolPrec.class);

		countQuery = countQuery.select(builder.count(rolPrecRoot)).where(getSearchPredicates(rolPrecRoot, rolPrec));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<RolPrec> rolPrecRoot, RolPrec rolPrec) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer rolId = rolPrec.getRol().getRolId();
		if (rolId != null) {
			predicates.add(builder.equal(rolPrecRoot.get("rol").<Integer>get("rolId"), rolId));
		}

		Integer precioId = rolPrec.getPrecio().getPrecioId();
		if (precioId != null) {
			predicates.add(builder.equal(rolPrecRoot.get("precio").<Integer>get("precioId"), precioId));
		}

		Integer sucursalId = rolPrec.getSucursal().getSucursalId();
		if (sucursalId != null) {
			predicates.add(builder.equal(rolPrecRoot.get("sucursal").<Integer>get("sucursalId"), sucursalId));
		}

		String sucursalDescri = rolPrec.getSucursal().getDescri();
		if (sucursalDescri != null) {
			predicates.add(builder.like(builder.lower(rolPrecRoot.get("sucursal").<String>get("descri")),
					sucursalDescri.toLowerCase()));
		}

		Boolean predet = rolPrec.getPredet();
		if (predet != null) {
			predicates.add(builder.equal(rolPrecRoot.get("predet"), predet));
		}

		Boolean acceso = rolPrec.getAcceso();
		if (acceso != null) {
			predicates.add(builder.equal(rolPrecRoot.get("acceso"), acceso));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public List<RolPrec> buscar(Sucursal sucursal, List<Integer> rols, RolPrec rolPrec) {

		EntityGraph<?> rolPrecGraph = this.entityManager.getEntityGraph("rolPrec.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<RolPrec> query = builder.createQuery(RolPrec.class);
		Root<RolPrec> rolPrecRoot = query.from(RolPrec.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

//		if (sucursals.size() != 0) {
//			Expression<Integer> expression = rolPrecRoot.get("sucursal").<Integer> get("sucursalId");
//			Predicate predicate = expression.in(sucursals);
//			predicates.add(predicate);
//		}

		Integer sucursalId = sucursal.getSucursalId();
		if (sucursalId != null) {
			predicates.add(builder.equal(rolPrecRoot.get("sucursal").<Integer>get("sucursalId"), sucursalId));
		}

		Boolean acceso = rolPrec.getAcceso();
		if (acceso != null) {
			predicates.add(builder.equal(rolPrecRoot.get("acceso"), acceso));
		}

		if (rols.size() != 0) {
			Expression<Integer> expression = rolPrecRoot.get("rol").<Integer>get("rolId");
			Predicate predicate = expression.in(rols);
			predicates.add(predicate);
		}

		query.orderBy(builder.asc(rolPrecRoot.get("rolPrecId")));
		TypedQuery<RolPrec> consulta = this.entityManager
				.createQuery(query.select(rolPrecRoot).where(predicates.toArray(new Predicate[predicates.size()])));
		consulta.setHint("jakarta.persistence.loadgraph", rolPrecGraph);

		return consulta.getResultList();
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	@Override
	public RolPrec buscarPrecioPred(RolPrec rolPrec, Set<RolPersUsua> rolPersUsuas) throws Exception {

		List<RolPrec> rolPrecs = new ArrayList<>();
		List<Rol> rols = new ArrayList<>();

		Stream<RolPersUsua> streamRolPersUsuas = rolPersUsuas.stream();
		streamRolPersUsuas.forEach(p -> rols.add(p.getRol()));

		for (Rol rol : rols) {

			rolPrec.setRol(rol);

			rolPrecs.addAll(buscar(rolPrec, null));
		}

//		Hacen lo mismo
//		Optional<RolPrec> rolPrecPredet = rolPrecs.stream().filter(rp -> rp.getPredet()).findAny();
		Optional<RolPrec> rolPrecPredet = rolPrecs.stream().filter((RolPrec rp) -> rp.getPredet()).findAny();

		return rolPrecPredet.get();
	}

}