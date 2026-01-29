package ec.com.tecnointel.soem.egreso.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.egreso.listaInt.PersCobrListaInt;
import ec.com.tecnointel.soem.egreso.modelo.PersCobr;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class PersCobrListaImp extends GestorListaSoem<PersCobr> implements PersCobrListaInt, Serializable {

	private static final long serialVersionUID = 3704780441593394692L;

	// Busca con paginaci�n
	@Override
	public List<PersCobr> buscar(PersCobr persCobr, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<PersCobr> query = builder.createQuery(PersCobr.class);
		Root<PersCobr> persCobrRoot = query.from(PersCobr.class);

		query.orderBy(builder.asc(persCobrRoot.get("persona").<String> get("apelli")));
		TypedQuery<PersCobr> consulta = this.entityManager
				.createQuery(query.select(persCobrRoot).where(getSearchPredicates(persCobrRoot, persCobr)));

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
	public long contarRegistros(PersCobr persCobr) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<PersCobr> persCobrRoot = countQuery.from(PersCobr.class);

		countQuery = countQuery.select(builder.count(persCobrRoot)).where(getSearchPredicates(persCobrRoot, persCobr));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<PersCobr> persCobrRoot, PersCobr persCobr) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		String cedula = persCobr.getPersona().getCedulaRuc();
		if (cedula != null && !"".equals(cedula)) {
			predicates.add(builder.equal(persCobrRoot.get("persona").<String> get("cedulaRuc"), cedula));
		}

		String apelli = persCobr.getPersona().getApelli();
		if (apelli != null && !"".equals(apelli)) {
			predicates.add(
					builder.like(builder.lower(persCobrRoot.get("persona").<String> get("apelli")), '%' + apelli.toLowerCase() + '%'));
		}

		String nombre = persCobr.getPersona().getNombre();
		if (nombre != null && !"".equals(nombre)) {
			predicates.add(
					builder.like(builder.lower(persCobrRoot.get("persona").<String> get("nombre")), '%' + nombre.toLowerCase() + '%'));
		}

		boolean estado = persCobr.isEstado();
		predicates.add(builder.equal(persCobrRoot.get("estado"), estado));

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(PersCobr persCobr) throws Exception {
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