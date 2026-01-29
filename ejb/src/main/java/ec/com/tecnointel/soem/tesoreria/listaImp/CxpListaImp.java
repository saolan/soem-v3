package ec.com.tecnointel.soem.tesoreria.listaImp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.tesoreria.listaInt.CxpListaInt;
import ec.com.tecnointel.soem.tesoreria.modelo.Cxp;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class CxpListaImp extends GestorListaSoem<Cxp> implements CxpListaInt, Serializable {

	private static final long serialVersionUID = 8577780573657149772L;

	// Busca con paginaci�n
	@Override
	public List<Cxp> buscar(Cxp cxp, Integer pagina) {

		EntityGraph<?> cxpGrap = this.entityManager.getEntityGraph("cxp.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Cxp> query = builder.createQuery(Cxp.class);
		Root<Cxp> cxpRoot = query.from(Cxp.class);

		query.orderBy(builder.asc(cxpRoot.get("cxpId")));
		TypedQuery<Cxp> consulta = this.entityManager
				.createQuery(query.select(cxpRoot).where(getSearchPredicates(cxpRoot, cxp)));
		consulta.setHint("jakarta.persistence.loadgraph", cxpGrap);

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
	public long contarRegistros(Cxp cxp) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<Cxp> cxpRoot = countQuery.from(Cxp.class);

		countQuery = countQuery.select(builder.count(cxpRoot)).where(getSearchPredicates(cxpRoot, cxp));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}
	
	@Override
	public BigDecimal sumarCxp(Cxp cxp) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<BigDecimal> sumQuery = builder.createQuery(BigDecimal.class);
		Root<Cxp> cxpRoot = sumQuery.from(Cxp.class);
		
		sumQuery = sumQuery.select(builder.sum(cxpRoot.<BigDecimal> get("total"))).where(getSearchPredicates(cxpRoot, cxp));
		return this.entityManager.createQuery(sumQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<Cxp> cxpRoot, Cxp cxp) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer personaId = cxp.getIngreso().getPersProv().getPersonaId();
		if (personaId != null) {
			predicates.add(builder.equal(cxpRoot.get("ingreso").get("persProv").get("personaId"), personaId));
		}

		String cedulaRuc = cxp.getIngreso().getPersProv().getPersona().getCedulaRuc();
		if (cedulaRuc != null) {
			predicates.add(builder.equal(builder.lower(cxpRoot.get("ingreso").get("persProv").get("persona").<String> get("cedulaRuc")), cedulaRuc.toLowerCase()));
		}
		
		String apelli = cxp.getIngreso().getPersProv().getPersona().getApelli();
		if (apelli != null) {
			predicates.add(builder.like(builder.lower(cxpRoot.get("ingreso").get("persProv").get("persona").<String> get("apelli")), '%' + apelli.toLowerCase() + '%'));
		}
		
		String nombre = cxp.getIngreso().getPersProv().getPersona().getNombre();
		if (nombre != null) {
			predicates.add(builder.like(builder.lower(cxpRoot.get("ingreso").get("persProv").get("persona").<String> get("nombre")), '%' + nombre.toLowerCase() + '%'));
		}

		Integer ingresoId = cxp.getIngreso().getIngresoId();
		if (ingresoId != null) {
			predicates.add(builder.equal(cxpRoot.get("ingreso").<Integer> get("ingresoId"), ingresoId));
		}

		Integer numero = cxp.getIngreso().getNumero();
		if (numero != null) {
			predicates.add(builder.equal(cxpRoot.get("ingreso").<Integer> get("numero"), numero));
		}

		LocalDate fechaEmis = cxp.getIngreso().getFechaEmis();
		if (fechaEmis != null) {
			predicates.add(builder.greaterThanOrEqualTo(cxpRoot.get("ingreso").<LocalDate> get("fechaEmis"), fechaEmis));
		}

		LocalDate fechaVenc = cxp.getFechaVenc();
		if (fechaVenc != null) {
			predicates.add(builder.greaterThanOrEqualTo(cxpRoot.<LocalDate> get("fechaVenc"), fechaVenc));
		}
		
		Boolean estado = cxp.getEstado();
		if (estado != null) {
			predicates.add(builder.equal(cxpRoot.get("estado"), estado));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
}