package ec.com.tecnointel.soem.parametro.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.listaInt.DimmListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class DimmListaImp extends GestorListaSoem<Dimm> implements DimmListaInt, Serializable {

	private static final long serialVersionUID = 8940581570834156219L;

	@Override
	public List<Dimm> buscar(Dimm dimmDesde, Dimm dimmHasta, Integer pagina) throws Exception {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Dimm> criteria = builder.createQuery(Dimm.class);
		Root<Dimm> dimmRoot = criteria.from(Dimm.class);

		criteria.orderBy(builder.asc(dimmRoot.get("tablaDesc")), builder.asc(dimmRoot.get("codigo")));
		TypedQuery<Dimm> consulta = this.entityManager
				.createQuery(criteria.select(dimmRoot).where(getSearchPredicates(dimmRoot, dimmDesde, dimmHasta)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(Dimm dimmDesde, Dimm dimmHasta) throws Exception {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<Dimm> dimmRoot = countCriteria.from(Dimm.class);
		countCriteria = countCriteria.select(builder.count(dimmRoot))
				.where(getSearchPredicates(dimmRoot, dimmDesde, dimmHasta));
		return this.entityManager.createQuery(countCriteria).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<Dimm> dimmRoot, Dimm dimmDesde, Dimm dimmHasta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

//		En dimmHasta por ahora tiene que venir NULL, 
//		Esta puesto para implementar luego
//		una busqueda con un rango inicial y un rango final
		
//		Integer dimmDesdeId = dimmDesde.getDimmId();
//		Integer dimmHastaId = dimmHasta.getDimmId();
//		if (dimmDesdeId != null && dimmHastaId != null) {
//			predicatesList.add(builder.between(dimmRoot.get("dimmId").as(Integer.class), dimmDesdeId, dimmHastaId));
//		}
//
//		if (dimmDesdeId != null && dimmHastaId == null) {
//			predicatesList.add(builder.equal(dimmRoot.get("dimmId"), dimmDesdeId));
//
//		}
		
		Integer dimmDesdeId = dimmDesde.getDimmId();
		if (dimmDesdeId != null) {
			predicates.add(builder.equal(dimmRoot.<Integer> get("dimmId"), dimmDesdeId));
		}
		
		String dimmDesdeTablaRefe = dimmDesde.getTablaRefe();
		if (dimmDesdeTablaRefe != null) {
			predicates.add(
					builder.equal(builder.lower(dimmRoot.<String>get("tablaRefe")), dimmDesdeTablaRefe.toLowerCase()));
		}

		String dimmDesdeCodigo = dimmDesde.getCodigo();
		if (dimmDesdeCodigo != null) {
			predicates.add(builder.like(builder.lower(dimmRoot.<String>get("codigo")), '%' + dimmDesdeCodigo.toLowerCase() + '%'));
		}

		String dimmDesdeDescri = dimmDesde.getDescri();
		if (dimmDesdeDescri != null) {
			predicates.add(builder.like(builder.lower(dimmRoot.<String>get("descri")), '%' + dimmDesdeDescri.toLowerCase() + '%'));
		}

		boolean estado = dimmDesde.isEstado();
		predicates.add(builder.equal(dimmRoot.get("estado"), estado));

		return predicates.toArray(new Predicate[predicates.size()]);
		
	}

	@Override
	public void imprimir(Dimm dimm) throws Exception {
		// TODO Auto-generated method stub
	}

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