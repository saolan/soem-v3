package ec.com.tecnointel.soem.parametro.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.listaInt.CentCostListaInt;
import ec.com.tecnointel.soem.parametro.modelo.CentCost;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class CentCostListaImp extends GestorListaSoem<CentCost> implements CentCostListaInt, Serializable {

	private static final long serialVersionUID = -5303378403499920696L;

	@Override
	public List<CentCost> buscar(CentCost centCost, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<CentCost> query = builder.createQuery(CentCost.class);
		Root<CentCost> centCostRoot = query.from(CentCost.class);

		query.orderBy(builder.asc(centCostRoot.get("descri")));
		TypedQuery<CentCost> consulta = this.entityManager
				.createQuery(query.select(centCostRoot).where(getSearchPredicates(centCostRoot, centCost)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(CentCost centCost) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<CentCost> centCostRoot = countQuery.from(CentCost.class);

		countQuery = countQuery.select(builder.count(centCostRoot)).where(getSearchPredicates(centCostRoot, centCost));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<CentCost> centCostRoot, CentCost centCost) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		String codigo = centCost.getCodigo();
		if (codigo != null && !"".equals(codigo)) {
			predicates.add(builder.equal(builder.lower(centCostRoot.<String>get("codigo")), codigo.toLowerCase()));
		}

		String descri = centCost.getDescri();
		if (descri != null && !"".equals(descri)) {
			predicates.add(
					builder.like(builder.lower(centCostRoot.<String>get("descri")), '%' + descri.toLowerCase() + '%'));
		}

		String estado = centCost.getEstado();
		if (estado != null && !"".equals(codigo) && !estado.equals("Todo")) {
			predicates.add(builder.equal(builder.lower(centCostRoot.<String>get("estado")), estado.toLowerCase()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}