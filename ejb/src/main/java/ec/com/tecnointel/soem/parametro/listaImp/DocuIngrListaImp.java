package ec.com.tecnointel.soem.parametro.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.listaInt.DocuIngrListaInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuIngr;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolDocu;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class DocuIngrListaImp extends GestorListaSoem<DocuIngr> implements DocuIngrListaInt, Serializable {

	private static final long serialVersionUID = 6863851036380664344L;

	// Busca con paginaci�n
	@Override
	public List<DocuIngr> buscar(DocuIngr docuIngr, Integer pagina) {

		EntityGraph<?> docuIngrGraph = this.entityManager.getEntityGraph("docuIngr.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<DocuIngr> query = builder.createQuery(DocuIngr.class);
		Root<DocuIngr> docuIngrRoot = query.from(DocuIngr.class);

		query.orderBy(builder.asc(docuIngrRoot.get("documentoId")));
		TypedQuery<DocuIngr> consulta = this.entityManager
				.createQuery(query.select(docuIngrRoot).where(getSearchPredicates(docuIngrRoot, docuIngr)));
		consulta.setHint("jakarta.persistence.loadgraph", docuIngrGraph);

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(DocuIngr docuIngr) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<DocuIngr> docuIngrRoot = countQuery.from(DocuIngr.class);

		countQuery = countQuery.select(builder.count(docuIngrRoot)).where(getSearchPredicates(docuIngrRoot, docuIngr));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<DocuIngr> docuIngrRoot, DocuIngr docuIngr) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		String descri = docuIngr.getDocumento().getDescri();
		if (descri != null && !"".equals(descri)) {
			predicates.add(builder.like(builder.lower(docuIngrRoot.get("documento"). <String> get("descri")),
					'%' + descri.toLowerCase() + '%'));
		}
		
		boolean estado = docuIngr.getDocumento().isEstado();
		predicates.add(builder.equal(docuIngrRoot.get("documento").get("estado"), estado));

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	
	@Override
	public List<DocuIngr> filtrarDocuIngrs(List<DocuIngr> docuIngrs, PersUsua persUsuaSesion, List<RolDocu> rolDocus) throws Exception {
		
		RolDocu rolDocu = new RolDocu();
		
		List<Rol> roles = new ArrayList<>();
//		List<RolDocu> rolDocus = new ArrayList<>();
		List<DocuIngr> filtroDocuIngrs = new ArrayList<>();
		
		Set<DocuIngr> filtroDocuIngrsSet = new HashSet<DocuIngr>();
		
//		rolDocus = variablesSesion.getRolDocus();
				
		Set<RolPersUsua> rolPersUsuas = persUsuaSesion.getRolPersUsuas();
		for (RolPersUsua rolPersUsua : rolPersUsuas) {
			roles.add(rolPersUsua.getRol());
		}
		
		for (Rol rol : roles) {
			for (DocuIngr docuIngr : docuIngrs) {
				rolDocu.setRol(rol);
				rolDocu.setDocumento(docuIngr.getDocumento());
				if (rolDocus.contains(rolDocu) ) {
					filtroDocuIngrsSet.add(docuIngr);
				}
			}
		}
	
		filtroDocuIngrs.addAll(filtroDocuIngrsSet);
		
		return filtroDocuIngrs;
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