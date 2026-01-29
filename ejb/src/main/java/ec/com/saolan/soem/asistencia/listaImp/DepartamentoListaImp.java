package ec.com.saolan.soem.asistencia.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.DepartamentoListaInt;
import ec.com.saolan.soem.asistencia.modelo.Departamento;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class DepartamentoListaImp extends GestorListaSoem<Departamento>
		implements
			DepartamentoListaInt,
			Serializable {

	private static final long serialVersionUID = -3597260513557611741L;

	@Override
	public List<Departamento> buscar(Departamento departamento, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Departamento> query = builder
				.createQuery(Departamento.class);
		Root<Departamento> departamentoRoot = query.from(Departamento.class);

		query.orderBy(builder.desc(departamentoRoot.get("departamentoId")));
		TypedQuery<Departamento> consulta = this.entityManager
				.createQuery(query.select(departamentoRoot).where(
						getSearchPredicates(departamentoRoot, departamento)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(
					filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(Departamento departamento) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<Departamento> departamentoRoot = countQuery
				.from(Departamento.class);

		countQuery = countQuery.select(builder.count(departamentoRoot)).where(
				getSearchPredicates(departamentoRoot, departamento));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(
			Root<Departamento> departamentoRoot, Departamento departamento) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}