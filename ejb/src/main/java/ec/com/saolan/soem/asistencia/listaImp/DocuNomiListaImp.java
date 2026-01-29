package ec.com.saolan.soem.asistencia.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.saolan.soem.asistencia.listaInt.DocuNomiListaInt;
import ec.com.saolan.soem.nomina.modelo.DocuNomi;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class DocuNomiListaImp extends GestorListaSoem<DocuNomi> implements DocuNomiListaInt, Serializable {

	private static final long serialVersionUID = -898276617594703128L;

	@Override
	public List<DocuNomi> buscar(DocuNomi docuNomi, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<DocuNomi> query = builder.createQuery(DocuNomi.class);
		Root<DocuNomi> docuNomiRoot = query.from(DocuNomi.class);

		query.orderBy(builder.desc(docuNomiRoot.get("docuNomiId")));
		TypedQuery<DocuNomi> consulta = this.entityManager
				.createQuery(query.select(docuNomiRoot).where(getSearchPredicates(docuNomiRoot, docuNomi)));

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(DocuNomi docuNomi) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<DocuNomi> docuNomiRoot = countQuery.from(DocuNomi.class);

		countQuery = countQuery.select(builder.count(docuNomiRoot)).where(getSearchPredicates(docuNomiRoot, docuNomi));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<DocuNomi> docuNomiRoot, DocuNomi docuNomi) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}