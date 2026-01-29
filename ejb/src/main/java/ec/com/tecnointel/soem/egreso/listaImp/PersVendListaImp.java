package ec.com.tecnointel.soem.egreso.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.egreso.listaInt.PersVendListaInt;
import ec.com.tecnointel.soem.egreso.modelo.PersVend;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class PersVendListaImp extends GestorListaSoem<PersVend> implements PersVendListaInt, Serializable {

	private static final long serialVersionUID = 2334893101991000320L;

	// Busca con paginaci�n
	@Override
	public List<PersVend> buscar(PersVend persVend, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<PersVend> query = builder.createQuery(PersVend.class);
		Root<PersVend> persVendRoot = query.from(PersVend.class);

		query.orderBy(builder.asc(persVendRoot.get("persona").<String> get("apelli")));
		TypedQuery<PersVend> consulta = this.entityManager
				.createQuery(query.select(persVendRoot).where(getSearchPredicates(persVendRoot, persVend)));

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
	public long contarRegistros(PersVend persVend) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<PersVend> persVendRoot = countQuery.from(PersVend.class);

		countQuery = countQuery.select(builder.count(persVendRoot)).where(getSearchPredicates(persVendRoot, persVend));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<PersVend> persVendRoot, PersVend persVend) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		String cedula = persVend.getPersona().getCedulaRuc();
		if (cedula != null && !"".equals(cedula)) {
			predicates.add(builder.equal(persVendRoot.get("persona").<String> get("cedulaRuc"), cedula));
		}

		String apelli = persVend.getPersona().getApelli();
		if (apelli != null && !"".equals(apelli)) {
			predicates.add(
					builder.like(builder.lower(persVendRoot.get("persona").<String> get("apelli")), '%' + apelli.toLowerCase() + '%'));
		}

		String nombre = persVend.getPersona().getNombre();
		if (nombre != null && !"".equals(nombre)) {
			predicates.add(
					builder.like(builder.lower(persVendRoot.get("persona").<String> get("nombre")), '%' + nombre.toLowerCase() + '%'));
		}

		boolean estado = persVend.isEstado();
		predicates.add(builder.equal(persVendRoot.get("estado"), estado));

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(PersVend persVend) throws Exception {
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