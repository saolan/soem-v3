package ec.com.tecnointel.soem.caja.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.caja.listaInt.SaliArchListaInt;
import ec.com.tecnointel.soem.caja.modelo.SaliArch;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class SaliArchListaImp extends GestorListaSoem<SaliArch> implements SaliArchListaInt, Serializable {

	private static final long serialVersionUID = 2786672208033376932L;

	@Override
	public List<SaliArch> buscar(SaliArch saliArch, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<SaliArch> query = builder.createQuery(SaliArch.class);
		Root<SaliArch> saliArchRoot = query.from(SaliArch.class);

		query.orderBy(builder.asc(saliArchRoot.get("saliArchId")));
		TypedQuery<SaliArch> consulta = this.entityManager
				.createQuery(query.select(saliArchRoot).where(getSearchPredicates(saliArchRoot, saliArch)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(SaliArch saliArch) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<SaliArch> saliArchRoot = countQuery.from(SaliArch.class);

		countQuery = countQuery.select(builder.count(saliArchRoot)).where(getSearchPredicates(saliArchRoot, saliArch));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<SaliArch> saliArchRoot, SaliArch saliArch) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Integer cajaDocuEgreId = saliArch.getCajaDocuEgre().getCajaDocuEgreId();
		if (cajaDocuEgreId != null) {
			predicates.add(builder.equal(saliArchRoot.get("cajaDocuEgre").<Integer> get("cajaDocuEgreId"), cajaDocuEgreId));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
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
