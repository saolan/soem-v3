package ec.com.tecnointel.soem.parametro.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.listaInt.DocuCajaListaInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuCaja;
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
public class DocuCajaListaImp extends GestorListaSoem<DocuCaja> implements DocuCajaListaInt, Serializable {

	private static final long serialVersionUID = 389815757473837169L;
	
	// Busca con paginaci�n
	@Override
	public List<DocuCaja> buscar(DocuCaja docuCaja, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<DocuCaja> query = builder.createQuery(DocuCaja.class);
		Root<DocuCaja> docuCajaRoot = query.from(DocuCaja.class);

		query.orderBy(builder.asc(docuCajaRoot.get("documentoId")));
		TypedQuery<DocuCaja> consulta = this.entityManager
				.createQuery(query.select(docuCajaRoot).where(getSearchPredicates(docuCajaRoot, docuCaja)));

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
	public long contarRegistros(DocuCaja docuCaja) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<DocuCaja> docuCajaRoot = countQuery.from(DocuCaja.class);

		countQuery = countQuery.select(builder.count(docuCajaRoot)).where(getSearchPredicates(docuCajaRoot, docuCaja));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<DocuCaja> docuCajaRoot, DocuCaja docuCaja) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		String descri = docuCaja.getDocumento().getDescri();
		if (descri != null && !"".equals(descri)) {
			predicates.add(builder.like(builder.lower(docuCajaRoot.get("documento"). <String> get("descri")),
					'%' + descri.toLowerCase() + '%'));
		}

		boolean estado = docuCaja.getDocumento().isEstado();
		predicates.add(builder.equal(docuCajaRoot.get("documento").get("estado"), estado));

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(DocuCaja docuCaja) throws Exception {
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	
	@Override
	public List<DocuCaja> filtrarDocuCajas(List<DocuCaja> docuCajas, PersUsua persUsuaSesion, List<RolDocu> rolDocus) {
		
		RolDocu rolDocu = new RolDocu();
		
		List<Rol> roles = new ArrayList<>();
//		List<RolDocu> rolDocus = new ArrayList<>();
		List<DocuCaja> filtroDocuCajas = new ArrayList<>();
		
		Set<DocuCaja> filtroDocuCajasSet = new HashSet<DocuCaja>();
		
//		rolDocus = variablesSesion.getRolDocus();
				
		Set<RolPersUsua> rolPersUsuas = persUsuaSesion.getRolPersUsuas();
		for (RolPersUsua rolPersUsua : rolPersUsuas) {
			roles.add(rolPersUsua.getRol());
		}
		
		for (Rol rol : roles) {
			for (DocuCaja docuCaja : docuCajas) {
				rolDocu.setRol(rol);
				rolDocu.setDocumento(docuCaja.getDocumento());
				if (rolDocus.contains(rolDocu) ) {
//					filtroDocuCajas.add(docuCaja);
					filtroDocuCajasSet.add(docuCaja);
				}
			}
		}
	
		filtroDocuCajas.addAll(filtroDocuCajasSet);
		
		// Eliminar Duplicados de la lista ya que puede exitir varios roles con acceso a un documento
//		Set<DocuCaja> linkedDocuCajas = new LinkedHashSet<DocuCaja>();
//		linkedDocuCajas.addAll(filtroDocuCajas);
//		filtroDocuCajas.clear();
//		filtroDocuCajas.addAll(linkedDocuCajas);

		return filtroDocuCajas;
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