package ec.com.tecnointel.soem.tesoreria.listaImp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.tesoreria.listaInt.FpmeFormPagoListaInt;
import ec.com.tecnointel.soem.tesoreria.modelo.FpmeFormPago;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class FpmeFormPagoListaImp extends GestorListaSoem<FpmeFormPago> implements FpmeFormPagoListaInt, Serializable {

	private static final long serialVersionUID = -1538832097908984589L;

	@Override
	public List<FpmeFormPago> buscar(FpmeFormPago fpmeFormPago, Integer pagina) {

		EntityGraph<?> fpmeFormPagoGrap = this.entityManager.getEntityGraph("fpmeFormPago.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<FpmeFormPago> query = builder.createQuery(FpmeFormPago.class);
		Root<FpmeFormPago> fpmeFormPagoRoot = query.from(FpmeFormPago.class);

		query.orderBy(builder.asc(fpmeFormPagoRoot.get("fpmeFormPagoId")));
		TypedQuery<FpmeFormPago> consulta = this.entityManager
				.createQuery(query.select(fpmeFormPagoRoot).where(getSearchPredicates(fpmeFormPagoRoot, fpmeFormPago)));
		consulta.setHint("jakarta.persistence.loadgraph", fpmeFormPagoGrap);

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(FpmeFormPago fpmeFormPago) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<FpmeFormPago> fpmeFormPagoRoot = countQuery.from(FpmeFormPago.class);

		countQuery = countQuery.select(builder.count(fpmeFormPagoRoot))
				.where(getSearchPredicates(fpmeFormPagoRoot, fpmeFormPago));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<FpmeFormPago> fpmeFormPagoRoot, FpmeFormPago fpmeFormPago) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (fpmeFormPago.getFormPagoMoviEgre() != null) {

			Integer fpmeId = fpmeFormPago.getFormPagoMoviEgre().getFpmeId();
			if (fpmeId != null) {
				predicates.add(builder.equal(fpmeFormPagoRoot.get("formPagoMoviEgre").get("fpmeId"), fpmeId));
			}

//			String cedulaRuc = fpmeFormPago.getFormPagoMoviEgre().getPersona().getCedulaRuc();
//			if (cedulaRuc != null) {
//				predicates
//						.add(builder.equal(builder.lower(fpmeFormPagoRoot.get("formPagoMoviEgre").get("persona").<String>get("cedulaRuc")),
//								cedulaRuc.toLowerCase()));
//			}
//
//			String apelli = fpmeFormPago.getFormPagoMoviEgre().getPersona().getApelli();
//			if (apelli != null) {
//				predicates.add(builder.like(builder.lower(fpmeFormPagoRoot.get("formPagoMoviEgre").get("persona").<String>get("apelli")),
//						'%' + apelli.toLowerCase() + '%'));
//			}
//
//			String nombre = fpmeFormPago.getFormPagoMoviEgre().getPersona().getNombre();
//			if (nombre != null) {
//				predicates.add(builder.like(builder.lower(fpmeFormPagoRoot.get("formPagoMoviEgre").get("persona").<String>get("nombre")),
//						'%' + nombre.toLowerCase() + '%'));
//			}
		
			LocalDate fecha = fpmeFormPago.getFormPagoMoviEgre().getFecha();
			if (fecha != null) {
				predicates.add(builder.greaterThanOrEqualTo(fpmeFormPagoRoot.get("formPagoMoviEgre").<LocalDate>get("fecha"),
						fecha));
			}
//
//		Integer ingresoId = fpmeFormPago.getIngresoId();
//		if (ingresoId != null) {
//			predicates.add(builder.equal(formPagoMoviIngrRoot.get("ingresoId"), ingresoId));
//		}
//
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}