package ec.com.tecnointel.soem.ingreso.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.ingreso.listaInt.IngrDimmListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDimm;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class IngrDimmListaImp extends GestorListaSoem<IngrDimm> implements IngrDimmListaInt, Serializable {


	private static final long serialVersionUID = -1607154843772768508L;

	// Busca con paginaci�n
	@Override
	public List<IngrDimm> buscar(IngrDimm ingrDimm, Integer pagina) {

		EntityGraph<?> ingrDimmGraph = this.entityManager.getEntityGraph("ingrDimm.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<IngrDimm> query = builder.createQuery(IngrDimm.class);
		Root<IngrDimm> ingrDimmRoot = query.from(IngrDimm.class);

		query.orderBy(builder.asc(ingrDimmRoot.get("ingrDimmId")));
		TypedQuery<IngrDimm> consulta = this.entityManager
				.createQuery(query.select(ingrDimmRoot).where(getSearchPredicates(ingrDimmRoot, ingrDimm)));
		consulta.setHint("jakarta.persistence.loadgraph", ingrDimmGraph);

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(IngrDimm ingrDimm) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<IngrDimm> ingrDimmRoot = countQuery.from(IngrDimm.class);

		countQuery = countQuery.select(builder.count(ingrDimmRoot)).where(getSearchPredicates(ingrDimmRoot, ingrDimm));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<IngrDimm> ingrDimmRoot, IngrDimm ingrDimm) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer ingresoId = ingrDimm.getIngreso().getIngresoId();
		if (ingresoId != null) {
			predicates.add(builder.equal(ingrDimmRoot.get("ingreso").get("ingresoId"), ingresoId));
		}
		
		Integer dimmId = ingrDimm.getDimm().getDimmId();
		if (dimmId != null) {
			predicates.add(builder.equal(ingrDimmRoot.get("dimm").<Integer> get("dimmId"), dimmId));
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