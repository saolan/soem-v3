package ec.com.tecnointel.soem.seguridad.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.seguridad.listaInt.RolPersUsuaListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class RolPersUsuaListaImp extends GestorListaSoem<RolPersUsua> implements RolPersUsuaListaInt, Serializable {

	private static final long serialVersionUID = -708575083477825905L;

	// Busca con paginaci�n
	@Override
	public List<RolPersUsua> buscar(RolPersUsua rolPersUsua, Integer pagina) {

		EntityGraph<?> rolPersUsuaGraph = this.entityManager.getEntityGraph("rolPersUsua.Graph");
		
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<RolPersUsua> query = builder.createQuery(RolPersUsua.class);
		Root<RolPersUsua> rolPersUsuaRoot = query.from(RolPersUsua.class);

		query.orderBy(builder.asc(rolPersUsuaRoot.get("persUsua").<String> get("usuari")));
		TypedQuery<RolPersUsua> consulta = this.entityManager
				.createQuery(query.select(rolPersUsuaRoot).where(getSearchPredicates(rolPersUsuaRoot, rolPersUsua)));
		consulta.setHint("jakarta.persistence.loadgraph", rolPersUsuaGraph);

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
	public long contarRegistros(RolPersUsua rolPersUsua) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<RolPersUsua> auditoriaRoot = countQuery.from(RolPersUsua.class);

		countQuery = countQuery.select(builder.count(auditoriaRoot))
				.where(getSearchPredicates(auditoriaRoot, rolPersUsua));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<RolPersUsua> rolPersUsuaRoot, RolPersUsua rolPersUsua) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer rolId = rolPersUsua.getRol().getRolId();
		if (rolId != null) {
			predicates.add(builder.equal(rolPersUsuaRoot.get("rol").<Integer> get("rolId"), rolId));
		}

		String rolDescri = rolPersUsua.getRol().getDescri();
		if (rolDescri != null) {
			predicates.add(
					builder.like(builder.lower(rolPersUsuaRoot.get("rol").<String> get("descri")), '%' + rolDescri.toLowerCase() + '%'));
		}

		Integer personaId = rolPersUsua.getPersUsua().getPersonaId();
		if (personaId != null) {
			predicates.add(builder.equal(rolPersUsuaRoot.get("persUsua").<Integer> get("personaId"), personaId));
		}

		String usuari = rolPersUsua.getPersUsua().getUsuari();
		if (usuari != null) {
			predicates.add(
					builder.like(builder.lower(rolPersUsuaRoot.get("persUsua").<String> get("usuari")), '%' + usuari.toLowerCase() + '%'));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(RolPersUsua rolPersUsua) throws Exception {
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