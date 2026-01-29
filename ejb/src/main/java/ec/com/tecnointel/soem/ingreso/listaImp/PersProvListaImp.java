package ec.com.tecnointel.soem.ingreso.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.ingreso.listaInt.PersProvListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.PersProv;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class PersProvListaImp extends GestorListaSoem<PersProv> implements PersProvListaInt, Serializable {

	private static final long serialVersionUID = 262420741922043642L;

	// Busca con paginaci�n
	@Override
	public List<PersProv> buscar(PersProv persProv, Integer pagina) {

		EntityGraph<?> persProvGraph = this.entityManager.getEntityGraph("persProv.Graph");
		
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<PersProv> query = builder.createQuery(PersProv.class);
		Root<PersProv> persProvRoot = query.from(PersProv.class);

		query.orderBy(builder.asc(persProvRoot.get("persona").<String> get("apelli")));
		TypedQuery<PersProv> consulta = this.entityManager
				.createQuery(query.select(persProvRoot).where(getSearchPredicates(persProvRoot, persProv)));
		consulta.setHint("jakarta.persistence.loadgraph", persProvGraph);

		// Si se pasa null a pagina se listan todos los datos de acuerdo a
		// parametros es decir no realiza paginaci�n
		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(PersProv persProv) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<PersProv> persProvRoot = countQuery.from(PersProv.class);

		countQuery = countQuery.select(builder.count(persProvRoot)).where(getSearchPredicates(persProvRoot, persProv));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<PersProv> persProvRoot, PersProv persProv) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		String cedula = persProv.getPersona().getCedulaRuc();
		if (cedula != null && !"".equals(cedula)) {
			predicates.add(builder.equal(persProvRoot.get("persona").<String> get("cedulaRuc"), cedula));
		}

		String apelli = persProv.getPersona().getApelli();
		if (apelli != null && !"".equals(apelli)) {
			predicates.add(
					builder.like(builder.lower(persProvRoot.get("persona").<String> get("apelli")), '%' + apelli.toLowerCase() + '%'));
		}

		String nombre = persProv.getPersona().getNombre();
		if (nombre != null && !"".equals(nombre)) {
			predicates.add(
					builder.like(builder.lower(persProvRoot.get("persona").<String> get("nombre")), '%' + nombre.toLowerCase() + '%'));
		}

		Integer personaId = persProv.getPersonaId();
		if (personaId != null) {
			predicates.add(builder.equal(persProvRoot.<Integer> get("personaId"), personaId));
		}
		
		String razonSoci = persProv.getRazonSoci();
		if (razonSoci != null && !"".equals(razonSoci)) {
			predicates.add(builder.like(builder.lower(persProvRoot.<String>get("razonSoci")),
					'%' + razonSoci.toLowerCase() + '%'));
		}

		boolean estado = persProv.isEstado();
		predicates.add(builder.equal(persProvRoot.get("estado"), estado));

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(PersProv persProv) throws Exception {
	}
}