package ec.com.tecnointel.soem.seguridad.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.seguridad.listaInt.PersUsuaListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class PersUsuaListaImp extends GestorListaSoem<PersUsua> implements PersUsuaListaInt, Serializable {

	private static final long serialVersionUID = 3567270909559137881L;

	@Override
	public List<PersUsua> buscar(PersUsua persUsua, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<PersUsua> query = builder.createQuery(PersUsua.class);
		Root<PersUsua> persUsuaRoot = query.from(PersUsua.class);

		query.orderBy(builder.asc(persUsuaRoot.get("persona").<String> get("apelli")));
		TypedQuery<PersUsua> consulta = this.entityManager
				.createQuery(query.select(persUsuaRoot).where(getSearchPredicates(persUsuaRoot, persUsua)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(PersUsua persUsua) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<PersUsua> persUsuaRoot = countQuery.from(PersUsua.class);

		countQuery = countQuery.select(builder.count(persUsuaRoot)).where(getSearchPredicates(persUsuaRoot, persUsua));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<PersUsua> persUsuaRoot, PersUsua persUsua) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer personaId = persUsua.getPersonaId();
		if (personaId != null) {
			predicates.add(builder.equal(persUsuaRoot.<Integer> get("personaId"), personaId));
		}
		
		String cedula = persUsua.getPersona().getCedulaRuc();
		if (cedula != null && !"".equals(cedula)) {
			predicates.add(builder.equal(persUsuaRoot.get("persona").<String> get("cedulaRuc"), cedula));
		}

		String apelli = persUsua.getPersona().getApelli();
		if (apelli != null && !"".equals(apelli)) {
			predicates.add(
					builder.like(builder.lower(persUsuaRoot.get("persona").<String> get("apelli")), '%' + apelli.toLowerCase() + '%'));
		}

		String nombre = persUsua.getPersona().getNombre();
		if (nombre != null && !"".equals(nombre)) {
			predicates.add(
					builder.like(builder.lower(persUsuaRoot.get("persona").<String> get("nombre")), '%' + nombre.toLowerCase() + '%'));
		}
		
		String usuari = persUsua.getUsuari();
		if (usuari != null && !"".equals(usuari)) {
			predicates.add(builder.equal(persUsuaRoot.<String> get("usuari"), usuari));
		}

		boolean estado = persUsua.isEstado();
		predicates.add(builder.equal(persUsuaRoot.get("estado"), estado));
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	@Override
	public PersUsua buscar(PersUsua persUsua) {

		PersUsua usuario = null;

		List<Predicate> predicates = new ArrayList<Predicate>();

		EntityGraph<?> persUsuaGrap = this.entityManager.getEntityGraph("persUsua.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<PersUsua> query = builder.createQuery(PersUsua.class);
		Root<PersUsua> persUsuaRoot = query.from(PersUsua.class);

		predicates.add(
				builder.equal(persUsuaRoot.<String>get("usuari"), persUsua.getUsuari()));
		predicates.add(
				builder.equal(persUsuaRoot.<String>get("clave"), persUsua.getClave()));

		TypedQuery<PersUsua> consulta = this.entityManager
				.createQuery(query.select(persUsuaRoot).where(predicates.toArray(new Predicate[predicates.size()])));
		consulta.setHint("jakarta.persistence.loadgraph", persUsuaGrap);

		List<PersUsua> persUsuarios = consulta.getResultList();

		if (!persUsuarios.isEmpty()) {
			usuario = persUsuarios.get(0);
		}
		return usuario;
	}

	@Override
	public void imprimir(PersUsua persUsua) throws Exception {
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

	// @Override
	// public PersUsua buscarUsuario(PersUsua persUsua) throws Exception {
	//
	// EntityGraph<PersUsua> persUsuaGrap =
	// this.entityManager.createEntityGraph(PersUsua.class);
	// EntityGraph<?> persUsuaGrap =
	// this.entityManager.getEntityGraph("persUsua.Graph");
	//
	// Map<String, Object> hints = new HashMap<String, Object>();
	// hints.put("jakarta.persistence.loadgraph", persUsuaGrap);
	//
	// System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> buscarUsuario " );
	//
	// PersUsua usuario =
	// this.entityManager.find(PersUsua.class,persUsua.getPersonaId(),hints);
	//
	// return usuario;
	// }
}