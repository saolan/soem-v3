package ec.com.tecnointel.soem.ingreso.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.ingreso.listaInt.IngrDetaImpuListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDetaImpu;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class IngrDetaImpuListaImp extends GestorListaSoem<IngrDetaImpu> implements IngrDetaImpuListaInt, Serializable {

	private static final long serialVersionUID = -5072689991925332009L;

	// Busca con paginaci�n
	@Override
	public List<IngrDetaImpu> buscar(IngrDetaImpu ingrDetaImpu, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<IngrDetaImpu> query = builder.createQuery(IngrDetaImpu.class);
		Root<IngrDetaImpu> ingrDetaImpuRoot = query.from(IngrDetaImpu.class);

		query.orderBy(builder.asc(ingrDetaImpuRoot.get("ingrDetaImpuId")));
		TypedQuery<IngrDetaImpu> consulta = this.entityManager
				.createQuery(query.select(ingrDetaImpuRoot).where(getSearchPredicates(ingrDetaImpuRoot, ingrDetaImpu)));

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
	public long contarRegistros(IngrDetaImpu ingrDetaImpu) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<IngrDetaImpu> ingrDetaImpuRoot = countQuery.from(IngrDetaImpu.class);

		countQuery = countQuery.select(builder.count(ingrDetaImpuRoot))
				.where(getSearchPredicates(ingrDetaImpuRoot, ingrDetaImpu));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<IngrDetaImpu> ingrDetaImpuRoot, IngrDetaImpu ingrDetaImpu) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer ingrDetaImpuId = ingrDetaImpu.getIngrDetaImpuId();
		if (ingrDetaImpuId != null) {
			predicates.add(builder.equal(ingrDetaImpuRoot.get("ingrDetaImpuId"), ingrDetaImpuId));
		}

		Integer ingrDetaId = ingrDetaImpu.getIngrDeta().getIngrDetaId();
		if (ingrDetaId != null) {
			predicates.add(builder.equal(ingrDetaImpuRoot.get("ingrDeta").get("ingrDetaId"), ingrDetaId));
		}
		
		String ingrDetaImpuCodigo = ingrDetaImpu.getCodigo();
		if (ingrDetaImpuCodigo != null && !"".equals(ingrDetaImpuCodigo)) {
			predicates.add(builder.like(builder.lower(ingrDetaImpuRoot.<String>get("ingrDetaImpuCodigo")),
					'%' + ingrDetaImpuCodigo.toLowerCase() + '%'));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(IngrDetaImpu ingrDetaImpu) throws Exception {
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