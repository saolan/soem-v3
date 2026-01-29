package ec.com.tecnointel.soem.inventario.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.inventario.listaInt.CategoriaListaInt;
import ec.com.tecnointel.soem.inventario.modelo.Categoria;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class CategoriaListaImp extends GestorListaSoem<Categoria> implements CategoriaListaInt, Serializable {

	private static final long serialVersionUID = -3714508651033925541L;

	// Busca con paginaci�n
	@Override
	public List<Categoria> buscar(Categoria categoria, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Categoria> query = builder.createQuery(Categoria.class);
		Root<Categoria> categoriaRoot = query.from(Categoria.class);

		query.orderBy(builder.asc(categoriaRoot.get("cateDescri")));
		TypedQuery<Categoria> consulta = this.entityManager
				.createQuery(query.select(categoriaRoot).where(getSearchPredicates(categoriaRoot, categoria)));

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
	public long contarRegistros(Categoria categoria) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<Categoria> categoriaRoot = countQuery.from(Categoria.class);

		countQuery = countQuery.select(builder.count(categoriaRoot))
				.where(getSearchPredicates(categoriaRoot, categoria));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<Categoria> categoriaRoot, Categoria categoria) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		// Ejemplo cuando se busca un numero no poner
		Integer cateId = categoria.getCateId();
		if (cateId != null) {
			predicates.add(builder.equal(categoriaRoot.get("cateId"), cateId));
		}

		String cateDescri = categoria.getCateDescri();
		if (cateDescri != null && !"".equals(cateDescri)) {
			predicates.add(builder.like(builder.lower(categoriaRoot.<String>get("cateDescri")),
					'%' + cateDescri.toLowerCase() + '%'));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(Categoria categoria) throws Exception {
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

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Otras maneras de hacer consultas
	//
	// // Consulta NamedQuery la consulta esta definida en la entidad
	// @Override
	// public List<Categoria> buscarCategoriaTodo() throws Exception {
	// Query query = entityManager.createNamedQuery("consultaCategoriaTodo");
	// List<Categoria> categorias = (List<Categoria>) query.getResultList();
	// return categorias;
	// }
	//
	// // Usando Hibernate hay que a�adir la dependencia
	// // <dependency>
	// // <groupId>org.hibernate</groupId>
	// // <artifactId>hibernate-core</artifactId>
	// // <version>5.1.0.Final</version>
	// // </dependency>
	//
	// public List<Categoria> buscarCategoriaNombre(String cateDescri) {
	// cateDescri = "abc";
	//
	// List<Categoria> categorias = null;
	//
	// // JPA 1 Capturar hibernate Session para usarlo con entityManager
	// // Session Session = (Session) entityManager.getDelegate();
	// // Criteria criteria = ((Session)
	// // entityManager.getDelegate()).createCriteria(Categoria.class);
	// // Criteria criteria1 =
	// // hibernateSession.createCriteria(Categoria.class);
	//
	// // JPA 2 Capturar hibernate Session para usarlo con entityManager
	// Session session = entityManager.unwrap(Session.class);
	// Criteria criteria = session.createCriteria(Categoria.class);
	// criteria.add(Restrictions.eq("cateDescri", cateDescri));
	// // criteria.createAlias("producto", "prod");
	// // criteria.setFetchMode("prod", FetchMode.JOIN);
	// categorias = criteria.list();
	// return categorias;
	// }
	//
	// public List<Categoria> buscarCategoriaHibe() {
	// List<Categoria> categorias = null;
	//
	// // JPA 1 Capturar hibernate Session para usarlo con entityManager
	// // Session Session = (Session) entityManager.getDelegate();
	// // Criteria criteria = ((Session)
	// // entityManager.getDelegate()).createCriteria(Categoria.class);
	// // Criteria criteria1 =
	// // hibernateSession.createCriteria(Categoria.class);
	//
	// // JPA 2 Capturar hibernate Session para usarlo con entityManager
	// Session session = entityManager.unwrap(Session.class);
	// Criteria criteria = session.createCriteria(Categoria.class);
	// // criteria.add(Restrictions.eq("cateDescri", cateDescri));
	// // criteria.createAlias("producto", "prod");
	// // criteria.setFetchMode("prod", FetchMode.JOIN);
	// categorias = criteria.list();
	// return categorias;
	// }
	//
	// // Usando JPA - Criteria
	// public List<Categoria> findCategoriaAll1() {
	// CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	// CriteriaQuery<Categoria> criteriaQuery = cb.createQuery(Categoria.class);
	// Root<Categoria> categoria = criteriaQuery.from(Categoria.class);
	// criteriaQuery.select(categoria).orderBy(cb.asc(categoria.get("cateDescri")));
	// return entityManager.createQuery(criteriaQuery).getResultList();
	// }
	//
	// // Usando JPA - Criteria
	// public List<Categoria> findCategoriaAll2() {
	// CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	// CriteriaQuery<Categoria> criteriaQuery = cb.createQuery(Categoria.class);
	// Root<Categoria> categoria = criteriaQuery.from(Categoria.class);
	// criteriaQuery.select(categoria);
	// // TypedQuery puede ser una entidad, caracter , numero , object
	// TypedQuery<Categoria> typedQuery =
	// entityManager.createQuery(criteriaQuery);
	// List<Categoria> categorias = (List<Categoria>)
	// typedQuery.getResultList();
	// return categorias;
	// }
}