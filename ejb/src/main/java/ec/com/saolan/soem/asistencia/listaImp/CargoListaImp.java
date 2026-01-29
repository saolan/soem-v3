package ec.com.saolan.soem.asistencia.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.CargoListaInt;
import ec.com.saolan.soem.asistencia.modelo.Cargo;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class CargoListaImp extends GestorListaSoem<Cargo>
		implements
			CargoListaInt,
			Serializable {

	private static final long serialVersionUID = 5208681181202204711L;

	@Override
	public List<Cargo> buscar(Cargo cargo, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Cargo> query = builder.createQuery(Cargo.class);
		Root<Cargo> cargoRoot = query.from(Cargo.class);

		query.orderBy(builder.desc(cargoRoot.get("cargoId")));
		TypedQuery<Cargo> consulta = this.entityManager
				.createQuery(query.select(cargoRoot).where(
						getSearchPredicates(cargoRoot, cargo)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(
					filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(Cargo cargo) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<Cargo> cargoRoot = countQuery.from(Cargo.class);

		countQuery = countQuery.select(builder.count(cargoRoot)).where(
				getSearchPredicates(cargoRoot, cargo));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<Cargo> cargoRoot, Cargo cargo) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}