package ec.com.tecnointel.soem.tesoreria.listaImp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.tesoreria.listaInt.FormPagoMoviEgreListaInt;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviEgre;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class FormPagoMoviEgreListaImp extends GestorListaSoem<FormPagoMoviEgre>
		implements FormPagoMoviEgreListaInt, Serializable {

	private static final long serialVersionUID = 3779988800803561548L;

	// Busca con paginaci�n
	@Override
	public List<FormPagoMoviEgre> buscar(FormPagoMoviEgre formPagoMoviEgre, Integer pagina) {
		
		EntityGraph<?> fpmeGrap = this.entityManager.getEntityGraph("fpme.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<FormPagoMoviEgre> query = builder.createQuery(FormPagoMoviEgre.class);
		Root<FormPagoMoviEgre> formPagoMoviEgreRoot = query.from(FormPagoMoviEgre.class);

		query.orderBy(builder.asc(formPagoMoviEgreRoot.get("numero")));
		TypedQuery<FormPagoMoviEgre> consulta = this.entityManager.createQuery(
				query.select(formPagoMoviEgreRoot).where(getSearchPredicates(formPagoMoviEgreRoot, formPagoMoviEgre)));
		consulta.setHint("jakarta.persistence.loadgraph", fpmeGrap);

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
	public long contarRegistros(FormPagoMoviEgre formPagoMoviEgre) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<FormPagoMoviEgre> formPagoMoviEgreRoot = countQuery.from(FormPagoMoviEgre.class);

		countQuery = countQuery.select(builder.count(formPagoMoviEgreRoot))
				.where(getSearchPredicates(formPagoMoviEgreRoot, formPagoMoviEgre));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<FormPagoMoviEgre> formPagoMoviEgreRoot,
			FormPagoMoviEgre formPagoMoviEgre) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();
				
		Integer fpmeNumero = formPagoMoviEgre.getNumero();
		if (fpmeNumero != null) {
			predicates.add(builder.equal(formPagoMoviEgreRoot.<Integer>get("numero"), fpmeNumero));
		}

		String cedulaRuc = formPagoMoviEgre.getPersona().getCedulaRuc();
		if (cedulaRuc != null) {
			predicates.add(builder.equal(builder.lower(formPagoMoviEgreRoot.get("persona").<String> get("cedulaRuc")), cedulaRuc.toLowerCase()));
		}
		
		String apelli = formPagoMoviEgre.getPersona().getApelli();
		if (apelli != null) {
			predicates.add(builder.like(builder.lower(formPagoMoviEgreRoot.get("persona").<String> get("apelli")), '%' + apelli.toLowerCase() + '%'));
		}
		
		String nombre = formPagoMoviEgre.getPersona().getNombre();
		if (nombre != null) {
			predicates.add(builder.like(builder.lower(formPagoMoviEgreRoot.get("persona").<String> get("nombre")), '%' + nombre.toLowerCase() + '%'));
		}
		
		LocalDate fecha = formPagoMoviEgre.getFecha();
		if (fecha != null) {
			predicates.add(builder.greaterThanOrEqualTo(formPagoMoviEgreRoot.<LocalDate> get("fecha"), fecha));
		}
		
		Integer egresoId = formPagoMoviEgre.getEgresoId();
		if (egresoId != null) {
			predicates.add(builder.equal(formPagoMoviEgreRoot.get("egresoId"), egresoId));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public List<FormPagoMoviEgre> buscarTransaccion(FormPagoMoviEgre formPagoMoviEgre) {

		EntityGraph<?> fpmeGrap = this.entityManager.getEntityGraph("fpme.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<FormPagoMoviEgre> query = builder.createQuery(FormPagoMoviEgre.class);
		Root<FormPagoMoviEgre> formPagoMoviEgreRoot = query.from(FormPagoMoviEgre.class);

		query.orderBy(builder.asc(formPagoMoviEgreRoot.get("numero")));
		TypedQuery<FormPagoMoviEgre> consulta = this.entityManager.createQuery(
				query.select(formPagoMoviEgreRoot).where(getSearchPredicatesTransaccion(formPagoMoviEgreRoot, formPagoMoviEgre)));
		consulta.setHint("jakarta.persistence.loadgraph", fpmeGrap);
		
		return consulta.getResultList();
	}

	private Predicate[] getSearchPredicatesTransaccion(Root<FormPagoMoviEgre> formPagoMoviEgreRoot,
			FormPagoMoviEgre formPagoMoviEgre) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer transaccionId = formPagoMoviEgre.getTransaccion().getTransaccionId();
		if (transaccionId != null) {
			predicates.add(builder.equal(formPagoMoviEgreRoot.get("transaccion").get("transaccionId"), transaccionId));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
}