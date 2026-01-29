package ec.com.tecnointel.soem.tesoreria.listaImp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.tesoreria.listaInt.PagoDetaListaInt;
import ec.com.tecnointel.soem.tesoreria.modelo.PagoDeta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class PagoDetaListaImp extends GestorListaSoem<PagoDeta> implements PagoDetaListaInt, Serializable {

	private static final long serialVersionUID = 4649255978936033792L;

	// Busca con paginaci�n
	@Override
	public List<PagoDeta> buscar(PagoDeta pagoDeta, Integer pagina) {
		
		EntityGraph<?> pagoDetaGrap = this.entityManager.getEntityGraph("pagoDeta.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<PagoDeta> query = builder.createQuery(PagoDeta.class);
		Root<PagoDeta> pagoDetaRoot = query.from(PagoDeta.class);

		query.orderBy(builder.asc(pagoDetaRoot.get("pagoDetaId")));
		TypedQuery<PagoDeta> consulta = this.entityManager
				.createQuery(query.select(pagoDetaRoot).where(getSearchPredicates(pagoDetaRoot, pagoDeta)));
		consulta.setHint("jakarta.persistence.loadgraph", pagoDetaGrap);

		// Si se pasa null a pagina se listan todos los datos de acuerdo a parametros
		// Es decir no realiza paginaci�n
		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		// consulta.setFirstResult(pagina *
		// getFilasPagina()).setMaxResults(getFilasPagina());

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(PagoDeta pagoDeta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<PagoDeta> pagoDetaRoot = countQuery.from(PagoDeta.class);

		countQuery = countQuery.select(builder.count(pagoDetaRoot)).where(getSearchPredicates(pagoDetaRoot, pagoDeta));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}
	
	private Predicate[] getSearchPredicates(Root<PagoDeta> pagoDetaRoot, PagoDeta pagoDeta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

//		Integer pagoDetaId = pagoDeta.getPagoDetaId();
//		if (pagoDetaId != null) {
//			predicates.add(builder.equal(pagoDetaRoot.get("pagoDetaId"), pagoDetaId));
//		}

		Integer cxpId = pagoDeta.getCxp().getCxpId();
		if (cxpId != null) {
			predicates.add(builder.equal(pagoDetaRoot.get("cxp").get("cxpId"), cxpId));
		}

		Integer numero = pagoDeta.getCxp().getIngreso().getNumero();
		if (numero != null) {
			predicates.add(builder.equal(pagoDetaRoot.get("cxp").get("ingreso").<Integer> get("numero"), numero));
		}
		
		
		Integer fpmiId = pagoDeta.getFormPagoMoviIngr().getFpmiId();
		if (fpmiId != null) {
			predicates.add(builder.equal(pagoDetaRoot.get("formPagoMoviIngr").get("fpmiId"), fpmiId));
		}

		
		String cedulaRuc = pagoDeta.getFormPagoMoviIngr().getPersona().getCedulaRuc();
		if (cedulaRuc != null) {
			predicates.add(builder.equal(builder.lower(pagoDetaRoot.get("formPagoMoviIngr").get("persona").<String> get("cedulaRuc")), cedulaRuc.toLowerCase()));
		}
		
		String apelli = pagoDeta.getFormPagoMoviIngr().getPersona().getApelli();
		if (apelli != null) {
			predicates.add(builder.like(builder.lower(pagoDetaRoot.get("formPagoMoviIngr").get("persona").<String> get("apelli")), '%' + apelli.toLowerCase() + '%'));
		}
		
		String nombre = pagoDeta.getFormPagoMoviIngr().getPersona().getNombre();
		if (nombre != null) {
			predicates.add(builder.like(builder.lower(pagoDetaRoot.get("formPagoMoviIngr").get("persona").<String> get("nombre")), '%' + nombre.toLowerCase() + '%'));
		}

		String formPagoDescri = pagoDeta.getFormPagoMoviIngr().getFormPago().getDescri();
		if (formPagoDescri != null) {
			predicates.add(builder.like(builder.lower(pagoDetaRoot.get("formPagoMoviIngr").get("formPago").<String> get("descri")), '%' + formPagoDescri.toLowerCase() + '%'));
		}
		
		LocalDate fecha = pagoDeta.getFormPagoMoviIngr().getFecha();
		if (fecha != null) {
			predicates.add(builder.greaterThanOrEqualTo(pagoDetaRoot.get("formPagoMoviIngr").<LocalDate> get("fecha"), fecha));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
//	Se utiliza para sumar el total de pagos de una persona
//	Utiliza getSearchPredicatesSuma para no mezclar con la otra busqueda
	public BigDecimal sumarPagoDeta(PagoDeta pagoDeta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<BigDecimal> countQuery = builder.createQuery(BigDecimal.class);
		Root<PagoDeta> pagoDetaRoot = countQuery.from(PagoDeta.class);

		countQuery = countQuery.select(builder.sum(pagoDetaRoot.<BigDecimal> get("total"))).where(getSearchPredicatesSuma(pagoDetaRoot, pagoDeta));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}
	
	private Predicate[] getSearchPredicatesSuma(Root<PagoDeta> pagoDetaRoot, PagoDeta pagoDeta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer personaId = pagoDeta.getCxp().getIngreso().getPersProv().getPersona().getPersonaId();
		if (personaId != null) {
			predicates.add(builder.equal(pagoDetaRoot.get("cxp").get("ingreso").get("persProv").get("persona").get("personaId"), personaId));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public List<Object[]> buscarPorIngresoId(PagoDeta pagoDeta) throws Exception {

		StringBuilder sentencia = new StringBuilder();
		sentencia.append("select fpmi_id from pago_deta where ingreso_id = ?1");
		
		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, pagoDeta.getIngresoId());

		return query.getResultList();
	}

	@Override
	public List<Object[]> buscarPorFpmeId(List<Object[]> objs, Ingreso ingreso) throws Exception {

		StringBuilder sentencia = new StringBuilder();
		sentencia.append("select ingreso_id from pago_deta where ingreso_id <> ?1 and fpmi_id in ?2");

		List<Integer> fpmiIds = new ArrayList<>();
		for (Object object : objs) {
			fpmiIds.add((Integer) object);
		}
		
		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, ingreso.getIngresoId());
		query.setParameter(2, fpmiIds);

		return query.getResultList();
	}

}