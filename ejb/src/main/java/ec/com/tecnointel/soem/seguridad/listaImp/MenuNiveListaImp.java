package ec.com.tecnointel.soem.seguridad.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.seguridad.listaInt.MenuNiveListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.MenuNive;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class MenuNiveListaImp extends GestorListaSoem<MenuNive> implements MenuNiveListaInt, Serializable {

	private static final long serialVersionUID = 2075093587184340736L;

	@Override
	public List<MenuNive> buscar(MenuNive menuNive, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<MenuNive> query = builder.createQuery(MenuNive.class);
		Root<MenuNive> menuNiveRoot = query.from(MenuNive.class);

		query.orderBy(builder.asc(menuNiveRoot.get("descri")));
		TypedQuery<MenuNive> consulta = this.entityManager
				.createQuery(query.select(menuNiveRoot).where(getSearchPredicates(menuNiveRoot, menuNive)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(MenuNive menuNive) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<MenuNive> menuNiveRoot = countQuery.from(MenuNive.class);

		countQuery = countQuery.select(builder.count(menuNiveRoot)).where(getSearchPredicates(menuNiveRoot, menuNive));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<MenuNive> menuNiveRoot, MenuNive menuNive) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		String descri = menuNive.getDescri();
		if (descri != null && !"".equals(descri)) {
			predicates.add(
					builder.like(builder.lower(menuNiveRoot.<String>get("descri")), '%' + descri.toLowerCase() + '%'));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}