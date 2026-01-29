package ec.com.tecnointel.soem.tesoreria.listaImp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.tesoreria.listaInt.CobrDetaListaInt;
import ec.com.tecnointel.soem.tesoreria.modelo.CobrDeta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class CobrDetaListaImp extends GestorListaSoem<CobrDeta> implements CobrDetaListaInt, Serializable {

	private static final long serialVersionUID = -8350193222661954122L;

	// Busca con paginaci�n
	@Override
	public List<CobrDeta> buscar(CobrDeta cobrDeta, Integer pagina) {

		EntityGraph<?> cobrDetaGrap = this.entityManager.getEntityGraph("cobrDeta.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<CobrDeta> query = builder.createQuery(CobrDeta.class);
		Root<CobrDeta> cobrDetaRoot = query.from(CobrDeta.class);

		query.orderBy(builder.asc(cobrDetaRoot.get("cobrDetaId")));
		TypedQuery<CobrDeta> consulta = this.entityManager
				.createQuery(query.select(cobrDetaRoot).where(getSearchPredicates(cobrDetaRoot, cobrDeta)));
		consulta.setHint("jakarta.persistence.loadgraph", cobrDetaGrap);

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
	public long contarRegistros(CobrDeta cobrDeta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<CobrDeta> cobrDetaRoot = countQuery.from(CobrDeta.class);

		countQuery = countQuery.select(builder.count(cobrDetaRoot)).where(getSearchPredicates(cobrDetaRoot, cobrDeta));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<CobrDeta> cobrDetaRoot, CobrDeta cobrDeta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (cobrDeta.getCxc() != null) {

			Integer cxcId = cobrDeta.getCxc().getCxcId();
			if (cxcId != null) {
				predicates.add(builder.equal(cobrDetaRoot.get("cxc").get("cxcId"), cxcId));
			}

			Integer numero = cobrDeta.getCxc().getEgreso().getNumero();
			if (numero != null) {
				predicates.add(builder.equal(cobrDetaRoot.get("cxc").get("egreso").<Integer>get("numero"), numero));
			}

			Integer egresoId = cobrDeta.getCxc().getEgreso().getEgresoId();
			if (egresoId != null) {
				predicates.add(builder.equal(cobrDetaRoot.get("cxc").get("egreso").<Integer>get("egresoId"), egresoId));
			}
		}

		if (cobrDeta.getFpmeFormPago() != null) {

			Integer fpmeFormPagoId = cobrDeta.getFpmeFormPago().getFpmeFormPagoId();
			if (fpmeFormPagoId != null) {
				predicates.add(builder.equal(cobrDetaRoot.get("fpmeFormPago").get("fpmeFormPagoId"), fpmeFormPagoId));
			}

			Integer fpmeId = cobrDeta.getFpmeFormPago().getFormPagoMoviEgre().getFpmeId();
			if (fpmeId != null) {
				predicates.add(
						builder.equal(cobrDetaRoot.get("fpmeFormPago").get("formPagoMoviEgre").get("fpmeId"), fpmeId));
			}

			String cedulaRuc = cobrDeta.getFpmeFormPago().getFormPagoMoviEgre().getPersona().getCedulaRuc();
			if (cedulaRuc != null) {
				predicates.add(builder.equal(builder.lower(cobrDetaRoot.get("fpmeFormPago").get("formPagoMoviEgre")
						.get("persona").<String>get("cedulaRuc")), cedulaRuc.toLowerCase()));
			}

			String apelli = cobrDeta.getFpmeFormPago().getFormPagoMoviEgre().getPersona().getApelli();
			if (apelli != null) {
				predicates.add(builder.like(builder.lower(
						cobrDetaRoot.get("fpmeFormPago").get("formPagoMoviEgre").get("persona").<String>get("apelli")),
						'%' + apelli.toLowerCase() + '%'));
			}

			String nombre = cobrDeta.getFpmeFormPago().getFormPagoMoviEgre().getPersona().getNombre();
			if (nombre != null) {
				predicates.add(builder.like(builder.lower(
						cobrDetaRoot.get("fpmeFormPago").get("formPagoMoviEgre").get("persona").<String>get("nombre")),
						'%' + nombre.toLowerCase() + '%'));
			}

			String formPagoDescri = cobrDeta.getFpmeFormPago().getFormPago().getDescri();
			if (formPagoDescri != null) {
				predicates.add(builder.like(
						builder.lower(cobrDetaRoot.get("fpmeFormPago").get("formPago").<String>get("descri")),
						'%' + formPagoDescri.toLowerCase() + '%'));
			}

			LocalDate fecha = cobrDeta.getFpmeFormPago().getFecha();
			if (fecha != null) {
				predicates
						.add(builder.greaterThanOrEqualTo(cobrDetaRoot.get("fpmeFormPago").<LocalDate>get("fecha"), fecha));
			}
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
//	Se utiliza para sumar el total de pagos de una persona
//	Utiliza getSearchPredicatesSuma para no mezclar con la otra busqueda
	public BigDecimal sumarCobrDeta(CobrDeta cobrDeta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<BigDecimal> sumQuery = builder.createQuery(BigDecimal.class);
		Root<CobrDeta> cobrDetaRoot = sumQuery.from(CobrDeta.class);

		sumQuery = sumQuery.select(builder.sum(cobrDetaRoot.<BigDecimal>get("total")))
				.where(getSearchPredicatesSuma(cobrDetaRoot, cobrDeta));
		return this.entityManager.createQuery(sumQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicatesSuma(Root<CobrDeta> cobrDetaRoot, CobrDeta cobrDeta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer personaId = cobrDeta.getCxc().getEgreso().getPersClie().getPersona().getPersonaId();
		if (personaId != null) {
			predicates.add(builder.equal(
					cobrDetaRoot.get("cxc").get("egreso").get("persClie").get("persona").get("personaId"), personaId));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public List<Object[]> buscarPorEgresoId(CobrDeta cobrDeta) throws Exception {

		StringBuilder sentencia = new StringBuilder();
		sentencia.append("select fpme_form_pago_id from cobr_deta where egreso_id = ?1");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, cobrDeta.getEgresoId());

		return query.getResultList();
	}

	@Override
	public List<Object[]> buscarPorFpmeFpId(List<Object[]> objs, Egreso egreso) throws Exception {

		StringBuilder sentencia = new StringBuilder();
		sentencia.append("select egreso_id from cobr_deta where egreso_id <> ?1 and fpme_form_pago_id in ?2");

		List<Integer> fpmeFpIds = new ArrayList<>();
		for (Object object : objs) {
			fpmeFpIds.add((Integer) object);
		}
		
		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, egreso.getEgresoId());
		query.setParameter(2, fpmeFpIds);

		return query.getResultList();
	}
}