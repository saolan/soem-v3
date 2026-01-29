package ec.com.tecnointel.soem.parametro.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.listaInt.DocuTranListaInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuTran;
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
public class DocuTranListaImp extends GestorListaSoem<DocuTran> implements DocuTranListaInt, Serializable {

	private static final long serialVersionUID = -3988891175486316791L;

	// Busca con paginaci�n
	@Override
	public List<DocuTran> buscar(DocuTran docuTran, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<DocuTran> query = builder.createQuery(DocuTran.class);
		Root<DocuTran> docuTranRoot = query.from(DocuTran.class);

		query.orderBy(builder.asc(docuTranRoot.get("documento").get("descri")));
		TypedQuery<DocuTran> consulta = this.entityManager
				.createQuery(query.select(docuTranRoot).where(getSearchPredicates(docuTranRoot, docuTran)));

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
	public long contarRegistros(DocuTran docuTran) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<DocuTran> docuTranRoot = countQuery.from(DocuTran.class);

		countQuery = countQuery.select(builder.count(docuTranRoot)).where(getSearchPredicates(docuTranRoot, docuTran));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<DocuTran> docuTranRoot, DocuTran docuTran) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		String descri = docuTran.getDocumento().getDescri();
		if (descri != null && !"".equals(descri)) {
			predicates.add(builder.like(builder.lower(docuTranRoot.get("documento"). <String> get("descri")),
					'%' + descri.toLowerCase() + '%'));
		}

		boolean estado = docuTran.getDocumento().isEstado();
		predicates.add(builder.equal(docuTranRoot.get("documento").get("estado"), estado));

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(DocuTran docuTran) throws Exception {
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	@Override
	public List<DocuTran> filtrarDocuTrans(List<DocuTran> docuTrans, PersUsua persUsuaSesion, List<RolDocu> rolDocus) throws Exception {
		
		RolDocu rolDocu = new RolDocu();
		
		List<Rol> roles = new ArrayList<>();
		List<DocuTran> filtroDocuTrans = new ArrayList<>();
		
		Set<DocuTran> filtroDocuTransSet = new HashSet<DocuTran>();
		
		Set<RolPersUsua> rolPersUsuas = persUsuaSesion.getRolPersUsuas();
		for (RolPersUsua rolPersUsua : rolPersUsuas) {
			roles.add(rolPersUsua.getRol());
		}
		
		for (Rol rol : roles) {
			for (DocuTran docuTran : docuTrans) {
				rolDocu.setRol(rol);
				rolDocu.setDocumento(docuTran.getDocumento());
				if (rolDocus.contains(rolDocu) ) {
					filtroDocuTransSet.add(docuTran);
				}
			}
		}
			
		filtroDocuTrans.addAll(filtroDocuTransSet);
		
		return filtroDocuTrans;
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