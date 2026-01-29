package ec.com.tecnointel.soem.parametro.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.listaInt.SucuParaListaInt;
import ec.com.tecnointel.soem.parametro.modelo.SucuPara;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class SucuParaListaImp extends GestorListaSoem<SucuPara> implements SucuParaListaInt, Serializable {

	private static final long serialVersionUID = 6266004128588501548L;

	@Override
	public List<SucuPara> buscar(SucuPara sucuPara, Integer pagina) {

		EntityGraph<?> sucuParaGraph = this.entityManager.getEntityGraph("sucuPara.Graph");
		
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<SucuPara> query = builder.createQuery(SucuPara.class);
		Root<SucuPara> bodegaRoot = query.from(SucuPara.class);

		query.orderBy(builder.asc(bodegaRoot.get("sucuParaId")));
		TypedQuery<SucuPara> consulta = this.entityManager
				.createQuery(query.select(bodegaRoot).where(getSearchPredicates(bodegaRoot, sucuPara)));
		consulta.setHint("jakarta.persistence.loadgraph", sucuParaGraph);

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(SucuPara sucuPara) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<SucuPara> bodegaRoot = countQuery.from(SucuPara.class);

		countQuery = countQuery.select(builder.count(bodegaRoot)).where(getSearchPredicates(bodegaRoot, sucuPara));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<SucuPara> sucuParaRoot, SucuPara sucuPara) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer sucursalId = sucuPara.getSucursal().getSucursalId();
		if (sucursalId != null) {
			predicates.add(builder.equal(sucuParaRoot.get("sucursal").<Integer> get("sucursalId"), sucursalId));
		}

		Integer parametroId = sucuPara.getParametro().getParametroId();
		if (parametroId != null) {
			predicates.add(builder.equal(sucuParaRoot.get("parametro").<Integer> get("parametroId"), parametroId));
		}

		Boolean estado = sucuPara.getEstado();
		if(estado != null){
			predicates.add(builder.equal(sucuParaRoot.get("estado"), estado));	
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}