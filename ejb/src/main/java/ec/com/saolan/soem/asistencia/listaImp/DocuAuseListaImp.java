package ec.com.saolan.soem.asistencia.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.DocuAuseListaInt;
import ec.com.saolan.soem.asistencia.modelo.DocuAuse;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class DocuAuseListaImp extends GestorListaSoem<DocuAuse> implements DocuAuseListaInt, Serializable {

	private static final long serialVersionUID = -8433089512109117581L;

	@Override
	public List<DocuAuse> buscar(DocuAuse docuAuse, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<DocuAuse> query = builder.createQuery(DocuAuse.class);
		Root<DocuAuse> docuAuseRoot = query.from(DocuAuse.class);

		query.orderBy(builder.desc(docuAuseRoot.get("docuAuseId")));
		TypedQuery<DocuAuse> consulta = this.entityManager
				.createQuery(query.select(docuAuseRoot).where(getSearchPredicates(docuAuseRoot, docuAuse)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(DocuAuse docuAuse) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<DocuAuse> docuAuseRoot = countQuery.from(DocuAuse.class);

		countQuery = countQuery.select(builder.count(docuAuseRoot)).where(getSearchPredicates(docuAuseRoot, docuAuse));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<DocuAuse> docuAuseRoot, DocuAuse docuAuse) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}