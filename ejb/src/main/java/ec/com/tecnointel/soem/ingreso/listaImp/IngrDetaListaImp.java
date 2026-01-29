package ec.com.tecnointel.soem.ingreso.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.ingreso.listaInt.IngrDetaListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDeta;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class IngrDetaListaImp extends GestorListaSoem<IngrDeta> implements IngrDetaListaInt, Serializable {

	private static final long serialVersionUID = 2378767926810784424L;

	// Busca con paginaci�n
	@Override
	public List<IngrDeta> buscar(IngrDeta ingrDeta, Integer pagina) {

		EntityGraph<?> ingrDetaGraph = this.entityManager.getEntityGraph("ingrDeta.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<IngrDeta> query = builder.createQuery(IngrDeta.class);
		Root<IngrDeta> ingrDetaRoot = query.from(IngrDeta.class);
		
//		Fetch<IngrDeta,Ingreso> i = ingrDetaRoot.fetch("ingreso");
//		Fetch<IngrDeta,Producto> p = ingrDetaRoot.fetch("producto");

		query.orderBy(builder.asc(ingrDetaRoot.get("ingrDetaId")));
		TypedQuery<IngrDeta> consulta = this.entityManager
				.createQuery(query.select(ingrDetaRoot).where(getSearchPredicates(ingrDetaRoot, ingrDeta)));
		consulta.setHint("jakarta.persistence.loadgraph", ingrDetaGraph);		
		
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
	public long contarRegistros(IngrDeta ingrDeta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<IngrDeta> ingrDetaRoot = countQuery.from(IngrDeta.class);

		countQuery = countQuery.select(builder.count(ingrDetaRoot)).where(getSearchPredicates(ingrDetaRoot, ingrDeta));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<IngrDeta> ingrDetaRoot, IngrDeta ingrDeta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer ingresoId = ingrDeta.getIngreso().getIngresoId();
		if (ingresoId != null) {
			predicates.add(builder.equal(ingrDetaRoot.get("ingreso").get("ingresoId"), ingresoId));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(IngrDeta ingrDeta) throws Exception {
	}
}