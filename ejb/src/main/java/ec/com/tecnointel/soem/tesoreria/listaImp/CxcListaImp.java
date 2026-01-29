package ec.com.tecnointel.soem.tesoreria.listaImp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.tesoreria.listaInt.CxcListaInt;
import ec.com.tecnointel.soem.tesoreria.modelo.Cxc;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class CxcListaImp extends GestorListaSoem<Cxc> implements CxcListaInt, Serializable {

	private static final long serialVersionUID = -1025179254339508749L;

	// Busca con paginaci�n
	@Override
	public List<Cxc> buscar(Cxc cxc, Integer pagina) {
		
		EntityGraph<?> cxcGrap = this.entityManager.getEntityGraph("cxc.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Cxc> query = builder.createQuery(Cxc.class);
		Root<Cxc> cxcRoot = query.from(Cxc.class);

		query.orderBy(builder.asc(cxcRoot.get("cxcId")));
		TypedQuery<Cxc> consulta = this.entityManager
				.createQuery(query.select(cxcRoot).where(getSearchPredicates(cxcRoot, cxc)));
		consulta.setHint("jakarta.persistence.loadgraph", cxcGrap);
		
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
	public long contarRegistros(Cxc cxc) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<Cxc> cxcRoot = countQuery.from(Cxc.class);

		countQuery = countQuery.select(builder.count(cxcRoot)).where(getSearchPredicates(cxcRoot, cxc));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}
	
	@Override
	public BigDecimal sumarCxc(Cxc cxc) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<BigDecimal> sumQuery = builder.createQuery(BigDecimal.class);
		Root<Cxc> cxcRoot = sumQuery.from(Cxc.class);
		
		sumQuery = sumQuery.select(builder.sum(cxcRoot.<BigDecimal> get("total"))).where(getSearchPredicates(cxcRoot, cxc));
		return this.entityManager.createQuery(sumQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<Cxc> cxcRoot, Cxc cxc) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer personaId = cxc.getEgreso().getPersClie().getPersonaId();
		if (personaId != null) {
			predicates.add(builder.equal(cxcRoot.get("egreso").get("persClie").get("personaId"), personaId));
		}

		String cedulaRuc = cxc.getEgreso().getPersClie().getPersona().getCedulaRuc();
		if (cedulaRuc != null) {
			predicates.add(builder.equal(builder.lower(cxcRoot.get("egreso").get("persClie").get("persona").<String> get("cedulaRuc")), cedulaRuc.toLowerCase()));
		}
		
		String apelli = cxc.getEgreso().getPersClie().getPersona().getApelli();
		if (apelli != null) {
			predicates.add(builder.like(builder.lower(cxcRoot.get("egreso").get("persClie").get("persona").<String> get("apelli")), '%' + apelli.toLowerCase() + '%'));
		}
		
		String nombre = cxc.getEgreso().getPersClie().getPersona().getNombre();
		if (nombre != null) {
			predicates.add(builder.like(builder.lower(cxcRoot.get("egreso").get("persClie").get("persona").<String> get("nombre")), '%' + nombre.toLowerCase() + '%'));
		}

		Integer egresoId = cxc.getEgreso().getEgresoId();
		if (egresoId != null) {
			predicates.add(builder.equal(cxcRoot.get("egreso").<Integer> get("egresoId"), egresoId));
		}

		Integer numero = cxc.getEgreso().getNumero();
		if (numero != null) {
			predicates.add(builder.equal(cxcRoot.get("egreso").<Integer> get("numero"), numero));
		}

		LocalDate fechaEmis = cxc.getEgreso().getFechaEmis();
		if (fechaEmis != null) {
			predicates.add(builder.greaterThanOrEqualTo(cxcRoot.get("egreso").<LocalDate> get("fechaEmis"), fechaEmis));
		}

		LocalDate fechaVenc = cxc.getFechaVenc();
		if (fechaVenc != null) {
			predicates.add(builder.greaterThanOrEqualTo(cxcRoot.<LocalDate> get("fechaVenc"), fechaVenc));
		}
		
		Boolean estado = cxc.getEstado();
		if (estado != null) {
			predicates.add(builder.equal(cxcRoot.get("estado"), estado));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}