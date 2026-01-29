package ec.com.tecnointel.soem.egreso.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.egreso.listaInt.PersClieListaInt;
import ec.com.tecnointel.soem.egreso.modelo.PersClie;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class PersClieListaImp extends GestorListaSoem<PersClie> implements PersClieListaInt, Serializable {

	private static final long serialVersionUID = -3521952272532497981L;

	// Busca con paginaci�n
	@Override
	public List<PersClie> buscar(PersClie persClie, Integer pagina, Integer filas) {

		EntityGraph<?> persClieGraph = this.entityManager.getEntityGraph("persClie.Graph");
		
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<PersClie> query = builder.createQuery(PersClie.class);
		Root<PersClie> persClieRoot = query.from(PersClie.class);

		query.orderBy(builder.asc(persClieRoot.get("persona").<String> get("apelli")));
		TypedQuery<PersClie> consulta = this.entityManager
				.createQuery(query.select(persClieRoot).where(getSearchPredicates(persClieRoot, persClie)));
		consulta.setHint("jakarta.persistence.loadgraph", persClieGraph);
		
		// Si se pasa null a pagina se listan todos los datos de acuerdo a
		// parametros
		// Es decir no realiza paginaci�n
		if (pagina != null) {
			consulta.setFirstResult(pagina * filas).setMaxResults(filas);
		}

		// consulta.setFirstResult(pagina *
		// getFilasPagina()).setMaxResults(getFilasPagina());

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(PersClie persClie) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<PersClie> persClieRoot = countQuery.from(PersClie.class);

		countQuery = countQuery.select(builder.count(persClieRoot)).where(getSearchPredicates(persClieRoot, persClie));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<PersClie> persClieRoot, PersClie persClie) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		String cedula = persClie.getPersona().getCedulaRuc();
		if (cedula != null && !"".equals(cedula)) {
			predicates.add(builder.equal(persClieRoot.get("persona").<String> get("cedulaRuc"), cedula));
		}

		String apelli = persClie.getPersona().getApelli();
		if (apelli != null && !"".equals(apelli)) {
			predicates.add(
					builder.like(builder.lower(persClieRoot.get("persona").<String> get("apelli")), '%' + apelli.toLowerCase() + '%'));
		}

		String nombre = persClie.getPersona().getNombre();
		if (nombre != null && !"".equals(nombre)) {
			predicates.add(
					builder.like(builder.lower(persClieRoot.get("persona").<String> get("nombre")), '%' + nombre.toLowerCase() + '%'));
		}

		String razonSoci = persClie.getRazonSoci();
		if (razonSoci != null && !"".equals(razonSoci)) {
			predicates.add(builder.like(builder.lower(persClieRoot.<String>get("razonSoci")),
					'%' + razonSoci.toLowerCase() + '%'));
		}
		
		boolean estado = persClie.isEstado();
		predicates.add(builder.equal(persClieRoot.get("estado"), estado));

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(PersClie persClie) throws Exception {
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