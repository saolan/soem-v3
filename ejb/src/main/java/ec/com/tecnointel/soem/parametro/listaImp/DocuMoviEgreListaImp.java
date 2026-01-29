package ec.com.tecnointel.soem.parametro.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.listaInt.DocuMoviEgreListaInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuMoviEgre;
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
public class DocuMoviEgreListaImp extends GestorListaSoem<DocuMoviEgre> implements DocuMoviEgreListaInt, Serializable {

	private static final long serialVersionUID = 3043764765108299719L;

	// Busca con paginaci?n
	@Override
	public List<DocuMoviEgre> buscar(DocuMoviEgre docuMoviEgre, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<DocuMoviEgre> query = builder
				.createQuery(DocuMoviEgre.class);
		Root<DocuMoviEgre> docuMoviEgreRoot = query.from(DocuMoviEgre.class);

		query.orderBy(builder.asc(docuMoviEgreRoot.get("documentoId")));
		TypedQuery<DocuMoviEgre> consulta = this.entityManager
				.createQuery(query.select(docuMoviEgreRoot).where(
						getSearchPredicates(docuMoviEgreRoot, docuMoviEgre)));

		// Si se pasa null a pagina se listan todos los datos de acuerdo a
		// parametros
		// Es decir no realiza paginaci?n
		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		// consulta.setFirstResult(pagina *
		// getFilasPagina()).setMaxResults(getFilasPagina());

		return consulta.getResultList();
	}

	@Override
	public DocuMoviEgre buscarDocuMoviEgrePred(List<DocuMoviEgre> docuMoviEgres, List<RolDocu> rolDocus) throws Exception {
		
		DocuMoviEgre docuMoviEgrePred = null;
		for (DocuMoviEgre docuMoviEgre : docuMoviEgres) {
			for (RolDocu rolDocu : rolDocus) {
								
				if (docuMoviEgre.getDocumentoId().equals(rolDocu.getDocumento().getDocumentoId()) && 
						rolDocu.getPredet() == true){
					docuMoviEgrePred = docuMoviEgre;
				}
			}
		}
	
		return docuMoviEgrePred; 
	}
	
	@Override
	public long contarRegistros(DocuMoviEgre docuMoviEgre) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<DocuMoviEgre> docuMoviEgreRoot = countQuery
				.from(DocuMoviEgre.class);

		countQuery = countQuery.select(builder.count(docuMoviEgreRoot)).where(
				getSearchPredicates(docuMoviEgreRoot, docuMoviEgre));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	@Override
	public List<DocuMoviEgre> filtrarDocuMoviEgres(List<DocuMoviEgre> docuMoviEgres, PersUsua persUsuaSesion, List<RolDocu> rolDocus) throws Exception {
		
		RolDocu rolDocu = new RolDocu();
		
		List<Rol> roles = new ArrayList<>();
		List<DocuMoviEgre> filtroDocuEgres = new ArrayList<>();
		
		Set<DocuMoviEgre> filtroDocuMoviEgresSet = new HashSet<DocuMoviEgre>();
		
		Set<RolPersUsua> rolPersUsuas = persUsuaSesion.getRolPersUsuas();
		for (RolPersUsua rolPersUsua : rolPersUsuas) {
			roles.add(rolPersUsua.getRol());
		}
		
		for (Rol rol : roles) {
			for (DocuMoviEgre docuEgre : docuMoviEgres) {
				rolDocu.setRol(rol);
				rolDocu.setDocumento(docuEgre.getDocumento());
				if (rolDocus.contains(rolDocu) ) {
					filtroDocuMoviEgresSet.add(docuEgre);
				}
			}
		}
	
		filtroDocuEgres.addAll(filtroDocuMoviEgresSet);
		
		return filtroDocuEgres;
	}

	private Predicate[] getSearchPredicates(
			Root<DocuMoviEgre> docuMoviEgreRoot, DocuMoviEgre docuMoviEgre) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		String descri = docuMoviEgre.getDocumento().getDescri();
		if (descri != null && !"".equals(descri)) {
			predicates.add(builder.like(builder.lower(docuMoviEgreRoot.get(
					"documento").<String> get("descri")), '%' + descri
					.toLowerCase() + '%'));
		}

		boolean estado = docuMoviEgre.getDocumento().isEstado();
		predicates.add(builder.equal(
				docuMoviEgreRoot.get("documento").get("estado"), estado));
		return predicates.toArray(new Predicate[predicates.size()]);
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