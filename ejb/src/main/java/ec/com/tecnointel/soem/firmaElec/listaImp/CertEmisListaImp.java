package ec.com.tecnointel.soem.firmaElec.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.firmaElec.listaInt.CertEmisListaInt;
import ec.com.tecnointel.soem.firmaElec.modelo.CertEmis;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class CertEmisListaImp extends GestorListaSoem<CertEmis> implements CertEmisListaInt, Serializable {

	private static final long serialVersionUID = 1575471332954648256L;

	@Override
	public List<CertEmis> buscar(CertEmis certEmis, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<CertEmis> query = builder.createQuery(CertEmis.class);
		Root<CertEmis> certEmisRoot = query.from(CertEmis.class);

		query.orderBy(builder.asc(certEmisRoot.get("descri")));
		TypedQuery<CertEmis> consulta = this.entityManager
				.createQuery(query.select(certEmisRoot).where(getSearchPredicates(certEmisRoot, certEmis)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(CertEmis certEmis) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<CertEmis> certEmisRoot = countQuery.from(CertEmis.class);

		countQuery = countQuery.select(builder.count(certEmisRoot)).where(getSearchPredicates(certEmisRoot, certEmis));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<CertEmis> certEmisRoot, CertEmis certEmis) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer certEmisId = certEmis.getCertEmisId();
		if (certEmisId != null) {
			predicates.add(builder.equal(certEmisRoot.<Integer> get("certEmisId"), certEmisId));
		}
		
		String descri = certEmis.getDescri();
		if (descri != null && !"".equals(descri)) {
			predicates.add(builder.like(builder.lower(certEmisRoot.<String>get("descri")),
					'%' + descri.toLowerCase() + '%'));
		}

		Boolean estado = certEmis.getEstado();
		predicates.add(builder.equal(certEmisRoot.get("estado"), estado));

		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
}