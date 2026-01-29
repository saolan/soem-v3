package ec.com.tecnointel.soem.parametro.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.listaInt.DocuMoviIngrListaInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuMoviIngr;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolDocu;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class DocuMoviIngrListaImp extends GestorListaSoem<DocuMoviIngr> implements DocuMoviIngrListaInt, Serializable {

	private static final long serialVersionUID = 6617550182972005647L;

	// Busca con paginaci�n
	@Override
	public List<DocuMoviIngr> buscar(DocuMoviIngr docuMoviIngr, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<DocuMoviIngr> query = builder.createQuery(DocuMoviIngr.class);
		Root<DocuMoviIngr> docuMoviIngrRoot = query.from(DocuMoviIngr.class);

		query.orderBy(builder.asc(docuMoviIngrRoot.get("documentoId")));
		TypedQuery<DocuMoviIngr> consulta = this.entityManager
				.createQuery(query.select(docuMoviIngrRoot).where(getSearchPredicates(docuMoviIngrRoot, docuMoviIngr)));

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
	public DocuMoviIngr buscarDocuMoviIngrPred(List<DocuMoviIngr> docuMoviIngrs, List<RolDocu> rolDocus) throws Exception {
		
		DocuMoviIngr docuMoviIngrPred = null;
		for (DocuMoviIngr docuMoviIngr : docuMoviIngrs) {
			for (RolDocu rolDocu : rolDocus) {
								
				if (docuMoviIngr.getDocumentoId().equals(rolDocu.getDocumento().getDocumentoId()) && 
						rolDocu.getPredet() == true){
					docuMoviIngrPred = docuMoviIngr;
				}
			}
		}
		return docuMoviIngrPred; 
	}
	
	@Override
	public long contarRegistros(DocuMoviIngr docuMoviIngr) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<DocuMoviIngr> docuMoviIngrRoot = countQuery.from(DocuMoviIngr.class);

		countQuery = countQuery.select(builder.count(docuMoviIngrRoot))
				.where(getSearchPredicates(docuMoviIngrRoot, docuMoviIngr));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}
	
	@Override
	public List<DocuMoviIngr> filtrarDocuMoviIngrs(List<DocuMoviIngr> docuMoviIngrs, PersUsua persUsuaSesion, List<RolDocu> rolDocus) throws Exception {
		
		RolDocu rolDocu = new RolDocu();
		
		List<Rol> roles = new ArrayList<>();
		List<DocuMoviIngr> filtroDocuIngrs = new ArrayList<>();
		
		Set<DocuMoviIngr> filtroDocuMoviIngrsSet = new HashSet<DocuMoviIngr>();
		
		Set<RolPersUsua> rolPersUsuas = persUsuaSesion.getRolPersUsuas();
		for (RolPersUsua rolPersUsua : rolPersUsuas) {
			roles.add(rolPersUsua.getRol());
		}
		
		for (Rol rol : roles) {
			for (DocuMoviIngr docuIngr : docuMoviIngrs) {
				rolDocu.setRol(rol);
				rolDocu.setDocumento(docuIngr.getDocumento());
				if (rolDocus.contains(rolDocu) ) {
					filtroDocuMoviIngrsSet.add(docuIngr);
				}
			}
		}
	
		filtroDocuIngrs.addAll(filtroDocuMoviIngrsSet);
		
		return filtroDocuIngrs;
	}


	private Predicate[] getSearchPredicates(Root<DocuMoviIngr> docuMoviIngrRoot, DocuMoviIngr docuMoviIngr) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		String descri = docuMoviIngr.getDocumento().getDescri();
		if (descri != null && !"".equals(descri)) {
			predicates.add(builder.like(builder.lower(docuMoviIngrRoot.get("documento"). <String> get("descri")),
					'%' + descri.toLowerCase() + '%'));
		}
		
		boolean estado = docuMoviIngr.getDocumento().isEstado();
		predicates.add(builder.equal(docuMoviIngrRoot.get("documento").get("estado"), estado));
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(DocuMoviIngr docuMoviIngr) throws Exception {
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