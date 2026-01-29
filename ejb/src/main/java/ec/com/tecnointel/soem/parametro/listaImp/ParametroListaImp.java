package ec.com.tecnointel.soem.parametro.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.listaInt.ParametroListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class ParametroListaImp extends GestorListaSoem<Parametro> implements ParametroListaInt, Serializable {

	private static final long serialVersionUID = -6933412902106504945L;

	@Override
	public List<Parametro> buscar(Parametro parametro, Integer pagina) throws Exception {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Parametro> criteria = builder.createQuery(Parametro.class);
		Root<Parametro> parametroRoot = criteria.from(Parametro.class);

		criteria.orderBy(builder.asc(parametroRoot.get("parametroId")));
		TypedQuery<Parametro> consulta = this.entityManager
				.createQuery(criteria.select(parametroRoot).where(getSearchPredicates(parametroRoot, parametro)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(Parametro parametro) throws Exception {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<Parametro> parametroRoot = countCriteria.from(Parametro.class);
		countCriteria = countCriteria.select(builder.count(parametroRoot))
				.where(getSearchPredicates(parametroRoot, parametro));
		return this.entityManager.createQuery(countCriteria).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<Parametro> parametroRoot, Parametro parametro) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		String codigo = parametro.getCodigo();
		if (codigo != null && !"".equals(codigo)) {
			predicates.add(
					builder.equal(builder.lower(parametroRoot.<String>get("codigo")),codigo.toLowerCase()));
		}

		String descri = parametro.getDescri();
		if (descri != null && !"".equals(descri)) {
			predicates.add(
					builder.like(builder.lower(parametroRoot.<String>get("descri")), '%' + descri.toLowerCase() + '%'));
		}
		
		boolean estado = parametro.isEstado();
		predicates.add(builder.equal(parametroRoot.get("estado"), estado));
		
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