package ec.com.tecnointel.soem.parametro.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.listaInt.DocuEgreListaInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuEgre;
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
public class DocuEgreListaImp extends GestorListaSoem<DocuEgre> implements DocuEgreListaInt, Serializable {

	private static final long serialVersionUID = 2592075584261340796L;

	// Busca con paginaci�n
	@Override
	public List<DocuEgre> buscar(DocuEgre docuEgre, Integer pagina) {
		
		EntityGraph<?> docuEgreGraph = this.entityManager.getEntityGraph("docuEgre.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<DocuEgre> query = builder.createQuery(DocuEgre.class);
		Root<DocuEgre> docuEgreRoot = query.from(DocuEgre.class);

		query.orderBy(builder.asc(docuEgreRoot.get("documentoId")));
		TypedQuery<DocuEgre> consulta = this.entityManager
				.createQuery(query.select(docuEgreRoot).where(getSearchPredicates(docuEgreRoot, docuEgre)));
		consulta.setHint("jakarta.persistence.loadgraph", docuEgreGraph);

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
	public long contarRegistros(DocuEgre docuEgre) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<DocuEgre> docuEgreRoot = countQuery.from(DocuEgre.class);

		countQuery = countQuery.select(builder.count(docuEgreRoot)).where(getSearchPredicates(docuEgreRoot, docuEgre));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<DocuEgre> docuEgreRoot, DocuEgre docuEgre) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		String descri = docuEgre.getDocumento().getDescri();
		if (descri != null && !"".equals(descri)) {
			predicates.add(builder.like(builder.lower(docuEgreRoot.get("documento"). <String> get("descri")),
					'%' + descri.toLowerCase() + '%'));
		}
		
		boolean estado = docuEgre.getDocumento().isEstado();
		predicates.add(builder.equal(docuEgreRoot.get("documento").get("estado"), estado));

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	@Override
	public List<DocuEgre> filtrarDocuEgres(List<DocuEgre> docuEgres, PersUsua persUsuaSesion, List<RolDocu> rolDocus) throws Exception {
		
		RolDocu rolDocu = new RolDocu();
		
		List<Rol> roles = new ArrayList<>();
//		List<RolDocu> rolDocus = new ArrayList<>();
		List<DocuEgre> filtroDocuEgres = new ArrayList<>();
		
		Set<DocuEgre> filtroDocuEgresSet = new HashSet<DocuEgre>();
		
//		rolDocus = variablesSesion.getRolDocus();
				
		Set<RolPersUsua> rolPersUsuas = persUsuaSesion.getRolPersUsuas();
		for (RolPersUsua rolPersUsua : rolPersUsuas) {
			roles.add(rolPersUsua.getRol());
		}
		
		for (Rol rol : roles) {
			for (DocuEgre docuEgre : docuEgres) {
				rolDocu.setRol(rol);
				rolDocu.setDocumento(docuEgre.getDocumento());
				if (rolDocus.contains(rolDocu) ) {
					filtroDocuEgresSet.add(docuEgre);
				}
			}
		}
	
		filtroDocuEgres.addAll(filtroDocuEgresSet);
		
		return filtroDocuEgres;
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