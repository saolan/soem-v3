package ec.com.tecnointel.soem.caja.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.caja.listaInt.CajaPeriListaInt;
import ec.com.tecnointel.soem.caja.modelo.CajaPeri;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class CajaPeriListaImp extends GestorListaSoem<CajaPeri> implements CajaPeriListaInt, Serializable {

	private static final long serialVersionUID = -8300311626385707722L;

	// Busca con paginaci�n
	@Override
	public List<CajaPeri> buscar(CajaPeri cajaPeri, Integer pagina) {
		
		EntityGraph<?> cajaPeriGraph = this.entityManager.getEntityGraph("cajaPeri.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<CajaPeri> query = builder.createQuery(CajaPeri.class);
		Root<CajaPeri> cajaPeriRoot = query.from(CajaPeri.class);

		query.orderBy(builder.asc(cajaPeriRoot.get("cajaPeriId")));
		TypedQuery<CajaPeri> consulta = this.entityManager
				.createQuery(query.select(cajaPeriRoot).where(getSearchPredicates(cajaPeriRoot, cajaPeri)));
		consulta.setHint("jakarta.persistence.loadgraph", cajaPeriGraph);

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
	public long contarRegistros(CajaPeri cajaPeri) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<CajaPeri> cajaPeriRoot = countQuery.from(CajaPeri.class);

		countQuery = countQuery.select(builder.count(cajaPeriRoot)).where(getSearchPredicates(cajaPeriRoot, cajaPeri));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<CajaPeri> cajaPeriRoot, CajaPeri cajaPeri) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer cajaId = cajaPeri.getCaja().getCajaId();
		if (cajaId != null) {
			predicates.add(builder.equal(cajaPeriRoot.get("caja").<Integer> get("cajaId"), cajaId));
		}

		Integer perifericoId = cajaPeri.getPeriferico().getPerifericoId();
		if (perifericoId != null) {
			predicates.add(builder.equal(cajaPeriRoot.get("periferico").<Integer> get("perifericoId"), perifericoId));
		}

		Boolean acceso = cajaPeri.getAcceso();
		if(acceso != null){
			predicates.add(builder.equal(cajaPeriRoot.get("acceso"), acceso));	
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(CajaPeri cajaPeri) throws Exception {
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