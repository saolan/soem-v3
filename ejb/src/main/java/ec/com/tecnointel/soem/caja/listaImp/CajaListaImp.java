package ec.com.tecnointel.soem.caja.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.caja.listaInt.CajaListaInt;
import ec.com.tecnointel.soem.caja.modelo.Caja;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class CajaListaImp extends GestorListaSoem<Caja> implements CajaListaInt, Serializable {

	private static final long serialVersionUID = 1598113421877670411L;

	@Override
	public List<Caja> buscar(Caja caja, Integer pagina, Set<Sucursal> sucursals) {

		EntityGraph<?> cajaGraph = this.entityManager.getEntityGraph("caja.Graph");
		
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Caja> query = builder.createQuery(Caja.class);
		Root<Caja> cajaRoot = query.from(Caja.class);

		query.orderBy(builder.asc(cajaRoot.get("descri")));
		TypedQuery<Caja> consulta = this.entityManager
				.createQuery(query.select(cajaRoot).where(getSearchPredicates(cajaRoot, caja, sucursals)));
		consulta.setHint("jakarta.persistence.loadgraph", cajaGraph);

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
	public long contarRegistros(Caja caja, Set<Sucursal> sucursals) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<Caja> cajaRoot = countQuery.from(Caja.class);

		countQuery = countQuery.select(builder.count(cajaRoot)).where(getSearchPredicates(cajaRoot, caja, sucursals));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	
	private Predicate[] getSearchPredicates(Root<Caja> cajaRoot, Caja caja, Set<Sucursal> sucursals) {

		List<Integer> sucursalIds = new ArrayList<Integer>();
		
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		for (Sucursal sucursal : sucursals) {
			sucursalIds.add(sucursal.getSucursalId());
		}
		
		if (sucursals.size() != 0) {
			Expression<Integer> expression = cajaRoot.get("sucursal").<Integer>get("sucursalId");
			Predicate predicate = expression.in(sucursalIds);
			predicates.add(predicate);
		}
		
		Integer cajaId = caja.getCajaId();
		if (cajaId != null) {
			predicates.add(builder.equal(cajaRoot.<Integer> get("cajaId"), cajaId));
		}

		String descri = caja.getDescri();
		if (descri != null && !"".equals(descri)) {
			predicates.add(builder.like(builder.lower(cajaRoot.<String>get("descri")),
					'%' + descri.toLowerCase() + '%'));
		}
		
		String cajaCodigo = caja.getCodigo();
		if (cajaCodigo != null) {
			predicates.add(builder.equal(cajaRoot.get("codigo"), cajaCodigo));
		}

		boolean estado = caja.isEstado();
		predicates.add(builder.equal(cajaRoot.get("estado"), estado));
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
//	@Override
//	public List<Object[]> buscarCajas() {
//				
//		String consulta1 = "SELECT c.descri as caja_descri, c.codigo as caja_codigo, s.descri as sucu_descri FROM Caja c inner join sucursal s on s.sucursal_id = c.sucursal_id";
//		
//		String consulta2 = "select " + 
//				"pcuen.plan_cuen_id as plan_cuen_id, pcuen.codigo as plan_cuen_codigo," + 
//				"sum(cxc.total) as cxc_total " + 
//				"from cxc cxc " + 
//				"inner join egreso e on e.egreso_id = cxc.egreso_id " + 
//				"inner join sucursal s on s.sucursal_id = e.sucursal_id " + 
//				"inner join pers_clie pc on pc.persona_id = e.persona_id " + 
//				"inner join clie_grup cg on cg.clie_grup_id = pc.clie_grup_id " + 
//				"inner join clie_grup_plan_cuen cgpc on cgpc.clie_grup_id = cg.clie_grup_id " + 
//				"inner join plan_cuen pcuen on pcuen.plan_cuen_id = cgpc.plan_cuen_id " + 
//				"inner join caja_movi cm on cm.caja_movi_id = e.caja_movi_id " + 
//				"inner join docu_egre de on de.documento_id = e.documento_id " + 
//				"inner join documento d on d.documento_id = de.documento_id " +  
//				"where d.contab = ?1 and de.genera_cxc = ?2 and d.factor = ?3 and e.caja_movi_id = ?4 " +
//				"group by pcuen.plan_cuen_id " + 
//				"order by pcuen.codigo";
//		
//		Query query = (Query) this.entityManager.createNativeQuery(consulta2);
//		query.setParameter(1, true);
//		query.setParameter(2, true);
//		query.setParameter(3, -1);
//		query.setParameter(4, 1019);
//	
//		return query.getResultList();
////		List<Object[]> cajas = query.getResultList();
////		List<Object[]> cajas = (List<Object[]>) this.entityManager.createNativeQuery(consulta2).getResultList();
////	    return cajas;
//	}

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