package ec.com.tecnointel.soem.parametro.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.listaInt.SucursalListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class SucursalListaImp extends GestorListaSoem<Sucursal> implements SucursalListaInt, Serializable {

	private static final long serialVersionUID = 2439985869360389599L;

	@Override
	public List<Sucursal> buscar(Sucursal sucursal, Integer pagina) {

		EntityGraph<?> sucursalGraph = this.entityManager.getEntityGraph("sucursal.Graph");
		
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		CriteriaQuery<Sucursal> criteria = builder.createQuery(Sucursal.class);
		Root<Sucursal> sucursalRoot = criteria.from(Sucursal.class);
		criteria.orderBy(builder.asc(sucursalRoot.get("descri")));

		TypedQuery<Sucursal> consulta = this.entityManager
				.createQuery(criteria.select(sucursalRoot).where(getSearchPredicates(sucursalRoot, sucursal)));
		consulta.setHint("jakarta.persistence.loadgraph", sucursalGraph);

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(Sucursal sucursal) throws Exception {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<Sucursal> sucursalRoot = countCriteria.from(Sucursal.class);

		countCriteria = countCriteria.select(builder.count(sucursalRoot))
				.where(getSearchPredicates(sucursalRoot, sucursal));
		return this.entityManager.createQuery(countCriteria).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<Sucursal> sucursalRoot, Sucursal sucursal) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String codigo = sucursal.getCodigo();
		if (codigo != null) {
			predicatesList.add(builder.equal(sucursalRoot.get("codigo"), codigo));
		}

		String descri = sucursal.getDescri();
		if (descri != null && !"".equals(descri)) {
			predicatesList.add(
					builder.like(builder.lower(sucursalRoot.<String>get("descri")), '%' + descri.toLowerCase() + '%'));
		}

		String ruc = sucursal.getRuc();
		if (ruc != null) {
			predicatesList.add(builder.equal(sucursalRoot.get("ruc"), ruc));
		}
		
		Boolean estado = sucursal.getEstado();
		if (estado != null) {
			predicatesList.add(builder.equal(sucursalRoot.get("estado"), estado));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}


	@Override
	public Sucursal buscarMatriz(Sucursal sucursal, Integer pagina) throws Exception {

		List<Sucursal> sucursals = this.buscar(sucursal, pagina);
		Sucursal sucursalMatriz = new Sucursal();
//
//		for (Sucursal sucursal2 : sucursals) {
//			if (sucursal2.isMatriz()) {
//				sucursalMatriz = sucursal2;
//
//			}
//		}

		Optional<Sucursal> streamSucursal = sucursals.stream().filter(s -> s.isMatriz()).findAny();

		if (streamSucursal.isPresent()) {
			sucursalMatriz = streamSucursal.get();
		}

		return sucursalMatriz;

	}
	
	@Override
	public Integer numeroEstablecimientos(Sucursal sucursal) {
		
		List<Sucursal> sucursals = buscar(sucursal, null);
		
		return sucursals.size();
		
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