package ec.com.tecnointel.soem.ingreso.listaImp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.ingreso.listaInt.RetencionListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class RetencionListaImp extends GestorListaSoem<Retencion> implements RetencionListaInt, Serializable {

	private static final long serialVersionUID = -4929515347113141615L;

	// Busca con paginaci�n
	@Override
	public List<Retencion> buscar(Retencion retencion, Integer pagina) {

		EntityGraph<?> retencionGraph = this.entityManager.getEntityGraph("retencion.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Retencion> query = builder.createQuery(Retencion.class);
		Root<Retencion> retencionRoot = query.from(Retencion.class);

		query.orderBy(builder.asc(retencionRoot.get("retencionId")));
		TypedQuery<Retencion> consulta = this.entityManager
				.createQuery(query.select(retencionRoot).where(getSearchPredicates(retencionRoot, retencion)));
		consulta.setHint("jakarta.persistence.loadgraph", retencionGraph);

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
	public long contarRegistros(Retencion retencion) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<Retencion> retencionRoot = countQuery.from(Retencion.class);

		countQuery = countQuery.select(builder.count(retencionRoot))
				.where(getSearchPredicates(retencionRoot, retencion));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<Retencion> retencionRoot, Retencion retencion) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer retencionId = retencion.getRetencionId();
		if (retencionId != null) {
			predicates.add(builder.equal(retencionRoot.get("retencionId"), retencionId));
		}

		Integer ingresoId = retencion.getIngreso().getIngresoId();
		if (ingresoId != null) {
			predicates.add(builder.equal(retencionRoot.get("ingreso").get("ingresoId"), ingresoId));
		}

		Integer RetencionNumero = retencion.getNumero();
		if (RetencionNumero != null) {
			predicates.add(builder.equal(retencionRoot.get("numero"), RetencionNumero));
		}

		Integer facturaNumero = retencion.getIngreso().getNumero();
		if (facturaNumero != null) {
			predicates.add(builder.equal(retencionRoot.get("ingreso").get("numero"), facturaNumero));
		}

		LocalDate fechaEmis = retencion.getFechaEmis();
		if (fechaEmis != null) {
			predicates.add(builder.greaterThanOrEqualTo(retencionRoot.<LocalDate>get("fechaEmis"), fechaEmis));
		}

		String estado = retencion.getEstado();
		if (estado != null) {
			predicates.add(builder.equal(builder.lower(retencionRoot.<String>get("estado")), estado.toLowerCase()));
		}

		String tipoRete = retencion.getIngreso().getDocuIngr().getTipoRete();
		if (tipoRete != null) {
			predicates.add(
					builder.equal(builder.lower(retencionRoot.get("ingreso").get("docuIngr").<String>get("tipoRete")),
							tipoRete.toLowerCase()));
		}

		String estadoDocuElec = retencion.getEstadoDocuElec();
		if (estadoDocuElec != null) {
			predicates.add(builder.notEqual(builder.lower(retencionRoot.<String>get("estadoDocuElec")),
					estadoDocuElec.toLowerCase()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	@Override
	public List<Retencion> buscar2(Retencion retencion, LocalDate fechaEmisDesde, LocalDate fechaEmisHasta, Integer pagina) {

		EntityGraph<?> retencionGraph = this.entityManager.getEntityGraph("retencion.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Retencion> query = builder.createQuery(Retencion.class);
		Root<Retencion> retencionRoot = query.from(Retencion.class);

		query.orderBy(builder.asc(retencionRoot.get("retencionId")));
		TypedQuery<Retencion> consulta = this.entityManager
				.createQuery(query.select(retencionRoot).where(getSearchPredicates(retencionRoot, retencion, fechaEmisDesde, fechaEmisHasta)));
		consulta.setHint("jakarta.persistence.loadgraph", retencionGraph);

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
	public long contarRegistros2(Retencion retencion, LocalDate fechaEmisDesde, LocalDate fechaEmisHasta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<Retencion> retencionRoot = countQuery.from(Retencion.class);

		countQuery = countQuery.select(builder.count(retencionRoot))
				.where(getSearchPredicates(retencionRoot, retencion, fechaEmisDesde, fechaEmisHasta));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<Retencion> retencionRoot, Retencion retencion, LocalDate fechaEmisDesde, LocalDate fechaEmisHasta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		String tipoRete = retencion.getIngreso().getDocuIngr().getTipoRete();
		if (tipoRete != null) {
			predicates.add(
					builder.equal(builder.lower(retencionRoot.get("ingreso").get("docuIngr").<String>get("tipoRete")),
							tipoRete.toLowerCase()));
		}
		
		if (fechaEmisDesde != null && fechaEmisHasta != null) {
			predicates.add(builder.between(retencionRoot.<LocalDate> get("fechaEmis"), fechaEmisDesde, fechaEmisHasta));
		}

		Integer RetencionNumero = retencion.getNumero();
		if (RetencionNumero != null) {
			predicates.add(builder.equal(retencionRoot.get("numero"), RetencionNumero));
		}
		
		String estado = retencion.getEstado();
		if (estado != null) {
			predicates.add(builder.equal(builder.lower(retencionRoot.<String>get("estado")), estado.toLowerCase()));
		}
		
		String estadoDocuElec = retencion.getEstadoDocuElec();
		if (estadoDocuElec != null) {
			predicates.add(builder.notEqual(builder.lower(retencionRoot.<String>get("estadoDocuElec")),
					estadoDocuElec.toLowerCase()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}