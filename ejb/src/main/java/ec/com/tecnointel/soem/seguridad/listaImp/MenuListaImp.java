package ec.com.tecnointel.soem.seguridad.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.seguridad.listaInt.MenuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.Menu;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class MenuListaImp extends GestorListaSoem<Menu> implements MenuListaInt, Serializable {

	private static final long serialVersionUID = 6539022274720593106L;

	@Override
	public List<Menu> buscar(Menu menu, Integer pagina) {

		EntityGraph<?> menuGrap = this.entityManager.getEntityGraph("menu.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Menu> query = builder.createQuery(Menu.class);
		Root<Menu> menuRoot = query.from(Menu.class);

		query.orderBy(builder.asc(menuRoot.get("menuId")));
		TypedQuery<Menu> consulta = this.entityManager
				.createQuery(query.select(menuRoot).where(getSearchPredicates(menuRoot, menu)));
		consulta.setHint("jakarta.persistence.loadgraph", menuGrap);

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(Menu menu) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<Menu> menuRoot = countQuery.from(Menu.class);

		countQuery = countQuery.select(builder.count(menuRoot)).where(getSearchPredicates(menuRoot, menu));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<Menu> menuRoot, Menu menu) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();


		Integer menuId = menu.getMenuId();
		if (menuId != null) {
			predicates.add(builder.equal(menuRoot.get("menuId"), menuId));
		}
		
		String descri = menu.getDescri();
		if (descri != null && !"".equals(descri)) {
			predicates
					.add(builder.like(builder.lower(menuRoot.<String>get("descri")), '%' + descri.toLowerCase() + '%'));
		}

		boolean estado = menu.isEstado();
		predicates.add(builder.equal(menuRoot.get("estado"), estado));

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(Menu menu) throws Exception {
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