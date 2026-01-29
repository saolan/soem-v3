package ec.com.tecnointel.soem.egreso.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.egreso.listaInt.EgreDetaImpuListaInt;
import ec.com.tecnointel.soem.egreso.modelo.EgreDetaImpu;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class EgreDetaImpuListaImp extends GestorListaSoem<EgreDetaImpu> implements EgreDetaImpuListaInt, Serializable {

	private static final long serialVersionUID = 6950805614435033800L;

	// Busca con paginaci�n
	@Override
	public List<EgreDetaImpu> buscar(EgreDetaImpu egreDetaImpu, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<EgreDetaImpu> query = builder.createQuery(EgreDetaImpu.class);
		Root<EgreDetaImpu> egreDetaImpuRoot = query.from(EgreDetaImpu.class);

		query.orderBy(builder.asc(egreDetaImpuRoot.get("egreDetaImpuId")));
		TypedQuery<EgreDetaImpu> consulta = this.entityManager
				.createQuery(query.select(egreDetaImpuRoot).where(getSearchPredicates(egreDetaImpuRoot, egreDetaImpu)));

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
	public long contarRegistros(EgreDetaImpu egreDetaImpu) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<EgreDetaImpu> egreDetaImpuRoot = countQuery.from(EgreDetaImpu.class);

		countQuery = countQuery.select(builder.count(egreDetaImpuRoot))
				.where(getSearchPredicates(egreDetaImpuRoot, egreDetaImpu));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<EgreDetaImpu> egreDetaImpuRoot, EgreDetaImpu egreDetaImpu) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer egreDetaImpuId = egreDetaImpu.getEgreDetaImpuId();
		if (egreDetaImpuId != null) {
			predicates.add(builder.equal(egreDetaImpuRoot.get("egreDetaImpuId"), egreDetaImpuId));
		}

		Integer egreDetaId = egreDetaImpu.getEgreDeta().getEgreDetaId();
		if (egreDetaId != null) {
			predicates.add(builder.equal(egreDetaImpuRoot.get("egreDeta").get("egreDetaId"), egreDetaId));
		}
		
		String egreDetaImpuCodigo = egreDetaImpu.getCodigo();
		if (egreDetaImpuCodigo != null && !"".equals(egreDetaImpuCodigo)) {
			predicates.add(builder.like(builder.lower(egreDetaImpuRoot.<String>get("egreDetaImpuCodigo")),
					'%' + egreDetaImpuCodigo.toLowerCase() + '%'));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(EgreDetaImpu egreDetaImpu) throws Exception {
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