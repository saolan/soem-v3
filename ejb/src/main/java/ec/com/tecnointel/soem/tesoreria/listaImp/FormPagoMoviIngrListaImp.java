package ec.com.tecnointel.soem.tesoreria.listaImp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.tesoreria.listaInt.FormPagoMoviIngrListaInt;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class FormPagoMoviIngrListaImp extends GestorListaSoem<FormPagoMoviIngr>
		implements FormPagoMoviIngrListaInt, Serializable {

	private static final long serialVersionUID = 70979153362344625L;

	// Busca con paginaci�n
	@Override
	public List<FormPagoMoviIngr> buscar(FormPagoMoviIngr formPagoMoviIngr, Integer pagina) {

		EntityGraph<?> fpmiGrap = this.entityManager.getEntityGraph("fpmi.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<FormPagoMoviIngr> query = builder.createQuery(FormPagoMoviIngr.class);
		Root<FormPagoMoviIngr> formPagoMoviIngrRoot = query.from(FormPagoMoviIngr.class);

		query.orderBy(builder.asc(formPagoMoviIngrRoot.get("numero")));
		TypedQuery<FormPagoMoviIngr> consulta = this.entityManager.createQuery(
				query.select(formPagoMoviIngrRoot).where(getSearchPredicates(formPagoMoviIngrRoot, formPagoMoviIngr)));
		consulta.setHint("jakarta.persistence.loadgraph", fpmiGrap);
		
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
	public long contarRegistros(FormPagoMoviIngr formPagoMoviIngr) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<FormPagoMoviIngr> formPagoMoviIngrRoot = countQuery.from(FormPagoMoviIngr.class);

		countQuery = countQuery.select(builder.count(formPagoMoviIngrRoot))
				.where(getSearchPredicates(formPagoMoviIngrRoot, formPagoMoviIngr));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<FormPagoMoviIngr> formPagoMoviIngrRoot,
			FormPagoMoviIngr formPagoMoviIngr) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer fpmiNumero = formPagoMoviIngr.getNumero();
		if (fpmiNumero != null) {
			predicates.add(builder.equal(formPagoMoviIngrRoot.<Integer>get("numero"), fpmiNumero));
		}
		
		String cedulaRuc = formPagoMoviIngr.getPersona().getCedulaRuc();
		if (cedulaRuc != null) {
			predicates.add(builder.equal(builder.lower(formPagoMoviIngrRoot.get("persona").<String> get("cedulaRuc")), cedulaRuc.toLowerCase()));
		}
		
		String apelli = formPagoMoviIngr.getPersona().getApelli();
		if (apelli != null) {
			predicates.add(builder.like(builder.lower(formPagoMoviIngrRoot.get("persona").<String> get("apelli")), '%' + apelli.toLowerCase() + '%'));
		}
		
		String nombre = formPagoMoviIngr.getPersona().getNombre();
		if (nombre != null) {
			predicates.add(builder.like(builder.lower(formPagoMoviIngrRoot.get("persona").<String> get("nombre")), '%' + nombre.toLowerCase() + '%'));
		}
		
		LocalDate fecha = formPagoMoviIngr.getFecha();
		if (fecha != null) {
			predicates.add(builder.greaterThanOrEqualTo(formPagoMoviIngrRoot.<LocalDate> get("fecha"), fecha));
		}

		Integer ingresoId = formPagoMoviIngr.getIngresoId();
		if (ingresoId != null) {
			predicates.add(builder.equal(formPagoMoviIngrRoot.get("ingresoId"), ingresoId));
		}

//		String estado = formPagoMoviIngr.getEstado();
//		if (estado != null) {
//			predicates.add(builder.equal(builder.lower(formPagoMoviIngrRoot.<String> get("estado")), estado.toLowerCase()));
//		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(FormPagoMoviIngr formPagoMoviIngr) throws Exception {
	}

	@Override
	public List<FormPagoMoviIngr> buscarTransaccion(FormPagoMoviIngr formPagoMoviIngr) {

		EntityGraph<?> fpmiGrap = this.entityManager.getEntityGraph("fpmi.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<FormPagoMoviIngr> query = builder.createQuery(FormPagoMoviIngr.class);
		Root<FormPagoMoviIngr> formPagoMoviIngrRoot = query.from(FormPagoMoviIngr.class);

		query.orderBy(builder.asc(formPagoMoviIngrRoot.get("numero")));
		TypedQuery<FormPagoMoviIngr> consulta = this.entityManager.createQuery(
				query.select(formPagoMoviIngrRoot).where(getSearchPredicatesTransaccion(formPagoMoviIngrRoot, formPagoMoviIngr)));
		consulta.setHint("jakarta.persistence.loadgraph", fpmiGrap);
		
		return consulta.getResultList();
	}

	private Predicate[] getSearchPredicatesTransaccion(Root<FormPagoMoviIngr> formPagoMoviIngrRoot,
			FormPagoMoviIngr formPagoMoviIngr) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer transaccionId = formPagoMoviIngr.getTransaccion().getTransaccionId();
		if (transaccionId != null) {
			predicates.add(builder.equal(formPagoMoviIngrRoot.get("transaccion").get("transaccionId"), transaccionId));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

}