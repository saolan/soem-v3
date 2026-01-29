package ec.com.tecnointel.soem.parametro.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.listaInt.FormPagoPlanCuenListaInt;
import ec.com.tecnointel.soem.parametro.modelo.FormPagoPlanCuen;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class FormPagoPlanCuenListaImp extends GestorListaSoem<FormPagoPlanCuen>
		implements FormPagoPlanCuenListaInt, Serializable {

	private static final long serialVersionUID = -6163485667145020037L;

	// Busca con paginaci?n
	@Override
	public List<FormPagoPlanCuen> buscar(FormPagoPlanCuen formPagoPlanCuen, Integer pagina) {

		EntityGraph<?> fppcGraph = this.entityManager.getEntityGraph("fppc.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<FormPagoPlanCuen> query = builder.createQuery(FormPagoPlanCuen.class);
		Root<FormPagoPlanCuen> formPagoPlanCuenRoot = query.from(FormPagoPlanCuen.class);

		query.orderBy(builder.asc(formPagoPlanCuenRoot.get("fppcId")));
		TypedQuery<FormPagoPlanCuen> consulta = this.entityManager.createQuery(
				query.select(formPagoPlanCuenRoot).where(getSearchPredicates(formPagoPlanCuenRoot, formPagoPlanCuen)));
		consulta.setHint("jakarta.persistence.loadgraph", fppcGraph);

		// Si se pasa null a pagina se listan todos los datos de acuerdo a
		// parametros
		// Es decir no realiza paginaci?n
		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		// consulta.setFirstResult(pagina *
		// getFilasPagina()).setMaxResults(getFilasPagina());

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(FormPagoPlanCuen formPagoPlanCuen) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<FormPagoPlanCuen> formPagoPlanCuenRoot = countQuery.from(FormPagoPlanCuen.class);

		countQuery = countQuery.select(builder.count(formPagoPlanCuenRoot))
				.where(getSearchPredicates(formPagoPlanCuenRoot, formPagoPlanCuen));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<FormPagoPlanCuen> formPagoPlanCuenRoot,
			FormPagoPlanCuen formPagoPlanCuen) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer formPagoId = formPagoPlanCuen.getFormPago().getFormPagoId();
		if (formPagoId != null) {
			predicates.add(builder.equal(formPagoPlanCuenRoot.get("formPago").get("formPagoId"), formPagoId));
		}
		
		Integer planCuenId = formPagoPlanCuen.getPlanCuen().getPlanCuenId();
		if (planCuenId != null) {
			predicates.add(builder.equal(formPagoPlanCuenRoot.get("planCuen").<Integer>get("planCuenId"), planCuenId));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
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