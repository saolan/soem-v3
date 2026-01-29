package ec.com.tecnointel.soem.caja.listaImp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.caja.listaInt.CajaMoviListaInt;
import ec.com.tecnointel.soem.caja.modelo.CajaMovi;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class CajaMoviListaImp extends GestorListaSoem<CajaMovi> implements CajaMoviListaInt, Serializable {

	private static final long serialVersionUID = -1080167029832404976L;

	// Busca con paginacion
	@Override
	public List<CajaMovi> buscar(CajaMovi cajaMovi, Integer pagina) {

		EntityGraph<?> cajaMoviGraph = this.entityManager.getEntityGraph("cajaMovi.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<CajaMovi> query = builder.createQuery(CajaMovi.class);
		Root<CajaMovi> cajaMoviRoot = query.from(CajaMovi.class);

		query.orderBy(builder.asc(cajaMoviRoot.get("cajaMoviId")));
		TypedQuery<CajaMovi> consulta = this.entityManager
				.createQuery(query.select(cajaMoviRoot).where(getSearchPredicates(cajaMoviRoot, cajaMovi)));
		consulta.setHint("jakarta.persistence.loadgraph", cajaMoviGraph);

		// Si se pasa null a pagina se listan todos los datos de acuerdo a
		// parametros
		// Es decir no realiza paginaci�n
		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(CajaMovi cajaMovi) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<CajaMovi> cajaMoviRoot = countQuery.from(CajaMovi.class);

		countQuery = countQuery.select(builder.count(cajaMoviRoot)).where(getSearchPredicates(cajaMoviRoot, cajaMovi));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<CajaMovi> cajaMoviRoot, CajaMovi cajaMovi) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer cajaMoviId = cajaMovi.getCajaMoviId();
		if (cajaMoviId != null) {
			predicates.add(builder.equal(cajaMoviRoot.get("cajaMoviId"), cajaMoviId));
		}

		Integer sucursalId = cajaMovi.getSucursal().getSucursalId();
		if (sucursalId != null) {
			predicates.add(builder.equal(cajaMoviRoot.get("sucursal").<Integer>get("sucursalId"), sucursalId));
		}

		String docuDescri = cajaMovi.getDocuCaja().getDocumento().getDescri();
		if (docuDescri != null) {
			predicates.add(
					builder.like(builder.lower(cajaMoviRoot.get("docuCaja").get("documento").<String>get("descri")),
							'%' + docuDescri.toLowerCase() + '%'));
		}

		// Integer cajaId = cajaMovi.getCaja().getCajaId();
		// if (cajaId != null) {
		// predicates.add(builder.equal(cajaMoviRoot.get("caja").get("cajaId"),
		// cajaId));
		// }

		String cajaDescri = cajaMovi.getCaja().getDescri();
		if (cajaDescri != null) {
			predicates.add(builder.like(builder.lower(cajaMoviRoot.get("caja").<String>get("descri")),
					'%' + cajaDescri.toLowerCase() + '%'));
		}

		Integer personaId = cajaMovi.getPersCaje().getPersonaId();
		if (personaId != null) {
			predicates.add(builder.equal(cajaMoviRoot.get("persCaje").get("personaId"), personaId));
		}

		String apelli = cajaMovi.getPersCaje().getPersona().getApelli();
		if (apelli != null) {
			predicates
					.add(builder.like(builder.lower(cajaMoviRoot.get("persCaje").get("persona").<String>get("apelli")),
							'%' + apelli.toLowerCase() + '%'));
		}

		LocalDate fecha = cajaMovi.getFecha();
		if (fecha != null) {
			predicates.add(builder.greaterThanOrEqualTo(cajaMoviRoot.<LocalDate>get("fecha"), fecha));
		}

		Boolean estado = cajaMovi.getEstado();
		if (estado != null) {
			predicates.add(builder.equal(cajaMoviRoot.get("estado"), estado));
		}

		// Integer cajaMoviSupeId = cajaMovi.getCajaMovi().getCajaMoviId();
		// if(cajaMoviSupeId != null){
		// predicates.add(builder.equal(cajaMoviRoot.get("cajaMovi").get("cajaMoviId") ,
		// cajaMoviSupeId));
		// }

		// predicates.add(builder.isNull(cajaMoviRoot.get("cajaMovi")));

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	// <<<<<<<<<<<<<<<< METODOS ADICIONALES >>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<< METODOS ADICIONALES >>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<< METODOS ADICIONALES >>>>>>>>>>>>>>>>>>>>>>

//	Lista para contabilizar el debe en una transaccion de ventas
//	Ya no se usa porque no cuadran decimales revisar y eliminar
//	@Deprecated
//	@Override
//	public List<Object[]> sumarCxcs(Integer cajaMoviId, Integer factor) {
//
//		String sumarCxc = "select pcuen.plan_cuen_id as plan_cuen_id, pcuen.codigo as plan_cuen_codigo,"
//				+ "sum(cxc.total) as cxc_total " + "from cxc cxc "
//				+ "inner join egreso e on e.egreso_id = cxc.egreso_id "
//				+ "inner join pers_clie pc on pc.persona_id = e.persona_id "
//				+ "inner join clie_grup cg on cg.clie_grup_id = pc.clie_grup_id "
//				+ "inner join clie_grup_plan_cuen cgpc on cgpc.clie_grup_id = cg.clie_grup_id "
//				+ "inner join plan_cuen pcuen on pcuen.plan_cuen_id = cgpc.plan_cuen_id "
//				+ "inner join docu_egre de on de.documento_id = e.documento_id "
//				+ "inner join documento d on d.documento_id = de.documento_id "
//				+ "where d.contab = ?1 and de.genera_cxc = ?2 and cgpc.tipo_tran = ?3 and d.factor = ?4 and e.caja_movi_id = ?5 "
//				+ "group by pcuen.plan_cuen_id " + "order by pcuen.codigo";
//
//		Query query = (Query) this.entityManager.createNativeQuery(sumarCxc);
//		query.setParameter(1, true);
//		query.setParameter(2, true);
//		query.setParameter(3, "CXP-CXC");
//		query.setParameter(4, factor);
//		query.setParameter(5, cajaMoviId);
//
//		return query.getResultList();
//
////		List<Object[]> cajas = (List<Object[]>) this.entityManager.createNativeQuery(consulta2).getResultList();
////		return cajas;     
//	}
//
////	Lista para contabilizar el debe en una transaccion de ventas
////	Se agrupo por group by pcuen.plan_cuen_id, d.factor para que ponga un solo valor y acumule en la cuenta de ventas del cliente
////	Si se agrupa de esta manera group by pcuen.plan_cuen_id, d.documento_id coloca una linea de total por cada documento
////	Los mismo para el metodo sumarCostoVentas
//	@Override
//	public List<Object[]> sumarCuentasCxcs(Integer cajaMoviId, DocuEgre docuEgre, String tipoTran) throws Exception {
//
//		StringBuilder senteciaWhere = new StringBuilder(
//				"where e.caja_movi_id = ?1 and cgpc.tipo_tran = ?2 and d.documento_id = ?3 ");
//		senteciaWhere.append("group by d.documento_id, pcuen.plan_cuen_id ");
//		senteciaWhere.append("order by d.documento_id, pcuen.codigo");
//
//		StringBuilder sumarCuentasCxcs = new StringBuilder();
//		sumarCuentasCxcs.append(sentenciaSelectVentas());
//		sumarCuentasCxcs.append(sentenciaJoinVentas());
//		sumarCuentasCxcs.append(sentenciaJoinVentasCliente());
//		sumarCuentasCxcs.append(senteciaWhere);
//
//		Query query = (Query) this.entityManager.createNativeQuery(sumarCuentasCxcs.toString());
//		query.setParameter(1, cajaMoviId);
//		query.setParameter(2, tipoTran);
//		query.setParameter(3, docuEgre.getDocumentoId());
//
//		return query.getResultList();
//	}
//
////	Lista para contabilizar el haber en una transaccion de ventas
////	Se agrupo por group by pcuen.plan_cuen_id, d.factor para que ponga un solo valor de todos los documentos Ej: facturas, Facturas electronicas, notas de venta
////	Si se agrupa de esta manera group by pcuen.plan_cuen_id, d.documento_id coloca una linea de total por cada documento
////	Los mismo para el metodo sumarCostoVentas
//	@Override
//	public List<Object[]> sumarVentas(Integer cajaMoviId, DocuEgre docuEgre, String tipoTran) throws Exception {
//
//		StringBuilder sentenciaWhere = new StringBuilder(
//				"where e.caja_movi_id = ?1 and pgpc.tipo_tran = ?2 and d.documento_id = ?3 ");
//		sentenciaWhere.append("group by d.documento_id, pcuen.plan_cuen_id ");
//		sentenciaWhere.append("order by d.documento_id, pcuen.codigo");
//
//		StringBuilder sumarVentas = new StringBuilder();
//		sumarVentas.append(sentenciaSelectVentas());
//		sumarVentas.append(sentenciaJoinVentas());
//		sumarVentas.append(sentenciaJoinVentasProducto());
//		sumarVentas.append(sentenciaWhere);
//
//		Query query = (Query) this.entityManager.createNativeQuery(sumarVentas.toString());
//		query.setParameter(1, cajaMoviId);
//		query.setParameter(2, tipoTran);
//		query.setParameter(3, docuEgre.getDocumentoId());
//
//		return query.getResultList();
//	}
//
//	@Override
//	public List<Object[]> sumarCostoVentas(Integer cajaMoviId, DocuEgre docuEgre, String tipoTran) throws Exception {
//
//// 		Se utiliza este ultimo parametro modulo_inve para determinar que calcule
//// 		solamente el costo de los productos que pertenecen a un grupo de inventario 
////		ya que puede existir productos de gastos o servicios que se registren
//// 		en el modulo de compras y en el modulo de ventas
//
//		StringBuilder sentenciaWhere = new StringBuilder(
//				"where e.caja_movi_id = ?1 and pgpc.tipo_tran = ?2 and d.documento_id = ?3 and pg.modulo_inve = ?4 ");
//		sentenciaWhere.append("group by d.documento_id, pcuen.plan_cuen_id ");
//		sentenciaWhere.append("order by d.documento_id, pcuen.codigo");
//
//		StringBuilder sumarVentas = new StringBuilder();
//		sumarVentas.append(sentenciaSelectVentas());
//		sumarVentas.append(sentenciaJoinVentas());
//		sumarVentas.append(sentenciaJoinVentasProducto());
//		sumarVentas.append(sentenciaWhere);
//
//		System.out.println(sentenciaWhere.toString());
//
//		Query query = (Query) this.entityManager.createNativeQuery(sumarVentas.toString());
//		query.setParameter(1, cajaMoviId);
//		query.setParameter(2, tipoTran);
//		query.setParameter(3, docuEgre.getDocumentoId());
//		query.setParameter(4, true);
//
//		return query.getResultList();
//	}
//
//	// Lista para contabilizar el debe en una transaccion de cobros
////	La primera consulta se hace para obtener los cobros de acuerdo a la cuenta contable de la forma de pago
////	se une con la segunda que se hace para obtener los cobros realizados por anticipos y en este caso se debe 
////	usar la cuenta contable de anticipo que esta en el proveedor
//	@Override
//	public List<Object[]> sumarCobroFps(Integer cajaMoviId, DocuEgre docuEgre) throws Exception {
//
////		String sumarCobros = "select fppc.plan_cuen_id as plan_cuen_id,  " + "sum(cd.total) as cobro_deta_total "
////				+ "from cobr_deta cd " + "right join cxc cxc on cxc.cxc_id = cd.cxc_id "
////				+ "inner join egreso e on e.egreso_id = cxc.egreso_id "
////				+ "left join fpme_form_pago fpmeFp on fpmeFp.fpme_form_pago_id = cd.fpme_form_pago_id "
////				+ "left join form_pago fp on fp.form_pago_id = fpmeFp.form_pago_id "
////				+ "left join form_pago_movi_egre fpme on fpme.fpme_id = fpmeFp.fpme_id "
////				+ "inner join form_pago_plan_cuen fppc on fppc.form_pago_id = fp.form_pago_id "
////				+ "where fpme.caja_movi_id = ?1 and e.documento_id = ?4 " + "group by fppc.plan_cuen_id " + "union "
////				+ "select cgpc.plan_cuen_id as plan_cuen_id, " + "sum(cd.total) as cobro_deta_total "
////				+ "from cobr_deta cd " + "inner join cxc cxc on cxc.cxc_id = cd.cxc_id "
////				+ "inner join fpme_form_pago fpmeFp on fpmeFp.fpme_form_pago_id = cd.fpme_form_pago_id "
////				+ "inner join form_pago fp on fp.form_pago_id = fpmeFp.form_pago_id "
////				+ "inner join form_pago_movi_egre fpme on fpme.fpme_id = fpmeFp.fpme_id	"
////				+ "inner join egreso e on e.egreso_id = cxc.egreso_id "
////				+ "inner join pers_clie pclie on pclie.persona_id = e.persona_id "
////				+ "inner join clie_grup cg on cg.clie_grup_id = pclie.clie_grup_id "
////				+ "inner join clie_grup_plan_cuen cgpc on cgpc.clie_grup_id = cg.clie_grup_id "
////				+ "where fpme.caja_movi_id = ?1 and cgpc.tipo_tran = ?2 and fp.tipo = ?3 "
////				+ "group by cgpc.plan_cuen_id " + "order by plan_cuen_id";
//
//		StringBuilder sumarCobros = new StringBuilder();
//		sumarCobros.append("select fppc.plan_cuen_id as plan_cuen_id, ");
//		sumarCobros.append("sum(cd.total) as cobro_deta_total ");
//		sumarCobros.append(sentenciaJoinCobrDeta());
//		sumarCobros.append("inner join form_pago_plan_cuen fppc on fppc.form_pago_id = fp.form_pago_id ");
//		sumarCobros.append("where fpme.caja_movi_id = ?1 and e.documento_id = ?4 ");
//		sumarCobros.append("group by fppc.plan_cuen_id ");
//		sumarCobros.append("union ");
//		sumarCobros.append("select cgpc.plan_cuen_id as plan_cuen_id, ");
//		sumarCobros.append("sum(cd.total) as cobro_deta_total ");
//		sumarCobros.append(sentenciaJoinCobrDeta());
//		sumarCobros.append(sentenciaJoinPersClie());
//		sumarCobros.append("where fpme.caja_movi_id = ?1 and cgpc.tipo_tran = ?2 and fp.tipo = ?3 and e.documento_id = ?4 ");
//		sumarCobros.append("group by cgpc.plan_cuen_id ");
//		sumarCobros.append("order by plan_cuen_id");		
//		
//		Query query = (Query) this.entityManager.createNativeQuery(sumarCobros.toString());
//		query.setParameter(1, cajaMoviId);
//		query.setParameter(2, "ANTICIPO");
//		query.setParameter(3, "AN");
//		query.setParameter(4, docuEgre.getDocumentoId());
//
//		return query.getResultList();
//	}
//
////	Lista para contabilizar el haber en una transaccion de cobros	
////	Esta consulta es igual a sumarCobroCxcsNcs con la diferencia que el join se hace a e.egreso_id
////	y se tiene un parametro mas
//	@Override
//	public List<Object[]> sumarCobroCxcs(Integer cajaMoviId, DocuEgre docuEgre) throws Exception {
//
////		String sumarCxcCobros = "select cgpc.plan_cuen_id as plan_cuen_id, " + "sum(cd.total) as cobro_deta_total "
////				+ "from cobr_deta cd " + "inner join cxc cxc on cxc.cxc_id = cd.cxc_id "
////				+ "inner join fpme_form_pago fpmeFp on fpmeFp.fpme_form_pago_id = cd.fpme_form_pago_id "
////				+ "inner join form_pago fp on fp.form_pago_id = fpmeFp.form_pago_id "
////				+ "inner join form_pago_movi_egre fpme on fpme.fpme_id = fpmeFp.fpme_id "
////				+ "inner join egreso e on e.egreso_id = cxc.egreso_id "
////				+ "inner join pers_clie pc on pc.persona_id = e.persona_id "
////				+ "inner join clie_grup cg on cg.clie_grup_id = pc.clie_grup_id "
////				+ "inner join clie_grup_plan_cuen cgpc on cgpc.clie_grup_id = cg.clie_grup_id and cgpc.documento_id = e.documento_id "
////				+ "where fpme.caja_movi_id = ?1 and cgpc.tipo_tran = ?2 and fp.tipo <> ?3 and e.documento_id = ?4 "
////				+ "group by cgpc.plan_cuen_id " + "order by cgpc.plan_cuen_id";
//
//		StringBuilder sumarCobros = new StringBuilder();
//		sumarCobros.append("select cgpc.plan_cuen_id as plan_cuen_id, ");
//		sumarCobros.append("sum(cd.total) as cobro_deta_total ");
//		sumarCobros.append(sentenciaJoinCobrDeta());
//		sumarCobros.append(sentenciaJoinPersClie());
//		sumarCobros.append("where fpme.caja_movi_id = ?1 and cgpc.tipo_tran = ?2 and fp.tipo <> ?3 and e.documento_id = ?4 ");
//		sumarCobros.append("group by cgpc.plan_cuen_id ");
//		sumarCobros.append("order by cgpc.plan_cuen_id");		
//
//		Query query = (Query) this.entityManager.createNativeQuery(sumarCobros.toString());
//		query.setParameter(1, cajaMoviId);
//		query.setParameter(2, "CXP-CXC");
//		query.setParameter(3, "NC");
//		query.setParameter(4, docuEgre.getDocumentoId());
//
//		return query.getResultList();
//	}
//
////	La primer consulta contabiliza las notas de credito pagadas con FP, para que por ejemplo 
////	si una factura se hizo de contado la nota de credito se reste de contado
////	La segunda parte de esta consulta es igual a sumarCobroCxcs con la diferencia que el join
////	se hacia a e.egreso_supe_id peor al hacer varias devoluciones apuntado a un mismo documento
////	la contablizacion duplicaba o triplicaba valores pero si tiene un parametro menos.
////	Contabiliza las notas de credito pagadas con NC
//	@Override
//	public List<Object[]> sumarCobroCxcsNcs(Integer cajaMoviId, DocuEgre docuEgre, String tipoTran) throws Exception {
//
////		String sumarCxcCobros = "select fppc.plan_cuen_id as plan_cuen_id, " + "sum(fpmeFp.total_reci) as total "
////				+ "from form_pago_movi_egre fpme " + "inner join egreso e on e.egreso_id = fpme.egreso_id "
////				+ "inner join documento d on d.documento_id = e.documento_id "
////				+ "inner join fpme_form_pago fpmeFp on fpmeFp.fpme_id = fpme.fpme_id "
////				+ "inner join form_pago fp on fp.form_pago_id = fpmeFp.form_pago_id "
////				+ "inner join form_pago_plan_cuen fppc on fppc.form_pago_id = fp.form_pago_id "
////				+ "where fpme.caja_movi_id = ?1 and e.documento_id = ?4 and fpme.egreso_id is not null "
////				+ "group by fppc.plan_cuen_id " + "union " + "select cgpc.plan_cuen_id as plan_cuen_id, "
////				+ "sum(cd.total) as cobro_deta_total " + "from cobr_deta cd "
////				+ "inner join cxc cxc on cxc.cxc_id = cd.cxc_id "
////				+ "inner join fpme_form_pago fpmeFp on fpmeFp.fpme_form_pago_id = cd.fpme_form_pago_id "
////				+ "inner join form_pago fp on fp.form_pago_id = fpmeFp.form_pago_id "
////				+ "inner join form_pago_movi_egre fpme on fpme.fpme_id = fpmeFp.fpme_id "
////				+ "inner join egreso e on e.egreso_id = fpme.egreso_id "
////				+ "inner join documento d on d.documento_id = fpme.documento_id "
////				+ "inner join pers_clie pc on pc.persona_id = fpme.persona_id "
////				+ "inner join clie_grup cg on cg.clie_grup_id = pc.clie_grup_id "
////				+ "inner join clie_grup_plan_cuen cgpc on cgpc.clie_grup_id = cg.clie_grup_id and cgpc.documento_id = e.documento_id "
////				+ "where fpme.caja_movi_id = ?1 and cgpc.tipo_tran = ?2 and fp.tipo = ?3 and e.documento_id = ?4 "
////				+ "group by cgpc.plan_cuen_id " + "order by plan_cuen_id";
//
//		StringBuilder sumarCobros = new StringBuilder();
//		sumarCobros.append("select fppc.plan_cuen_id as plan_cuen_id, ");
//		sumarCobros.append("sum(fpmeFp.total_reci) as total ");
//		sumarCobros.append("from form_pago_movi_egre fpme ");
//		sumarCobros.append("inner join egreso e on e.egreso_id = fpme.egreso_id ");
//		sumarCobros.append("inner join documento d on d.documento_id = e.documento_id ");
//		sumarCobros.append(sentenciaJoinFormPago());
//		sumarCobros.append("where fpme.caja_movi_id = ?1 and e.documento_id = ?4 and fpme.egreso_id is not null ");
//		sumarCobros.append("group by fppc.plan_cuen_id ");
//		sumarCobros.append("union ");
//		sumarCobros.append("select cgpc.plan_cuen_id as plan_cuen_id, ");
//		sumarCobros.append("sum(cd.total) as cobro_deta_total ");
//		sumarCobros.append(sentenciaJoinCobrDeta());
//		sumarCobros.append(sentenciaJoinPersClie());
//		sumarCobros.append("where fpme.caja_movi_id = ?1 and cgpc.tipo_tran = ?2 and fp.tipo = ?3 and e.documento_id = ?4 ");
//		sumarCobros.append("group by cgpc.plan_cuen_id ");
//		sumarCobros.append("order by plan_cuen_id");		
//
//		
//		Query query = (Query) this.entityManager.createNativeQuery(sumarCobros.toString());
//		query.setParameter(1, cajaMoviId);
//		query.setParameter(2, "CXP-CXC");
//		query.setParameter(3, "NC");
//		query.setParameter(4, docuEgre.getDocumentoId());
//
//		return query.getResultList();
//	}
//
//	@Override
//	public List<Object[]> sumarFpmeAnticipos(Integer cajaMoviId, String esNulo) throws Exception {
//
////		String sumarFpme = "select pcuen.plan_cuen_id as plan_cuen_id, pcuen.codigo as plan_cuen_codigo, "
////				+ "sum(fpme.total) as total " 
////				+ "from form_pago_movi_egre fpme "
////				+ "inner join docu_movi_egre dme on dme.documento_id = fpme.documento_id "
////				+ "inner join persona p on p.persona_id = fpme.persona_id "
////				+ "inner join pers_clie pc on pc.persona_id = p.persona_id "
////				+ "inner join clie_grup cg on cg.clie_grup_id = pc.clie_grup_id "
////				+ "inner join clie_grup_plan_cuen cgpc on cgpc.clie_grup_id = cg.clie_grup_id "
////				+ "inner join plan_cuen pcuen on pcuen.plan_cuen_id = cgpc.plan_cuen_id "
////				+ "where dme.tipo = ?1 and cgpc.tipo_tran = ?1 and fpme.caja_movi_id = ?2 and fpme.egreso_id "
////				+ esNulo + " " + "group by pcuen.plan_cuen_id " + "order by pcuen.plan_cuen_id";
//
//		StringBuilder sumarFpme = new StringBuilder();
//		sumarFpme.append("select cgpc.plan_cuen_id as plan_cuen_id, ");
//		sumarFpme.append("sum(fpme.total) as total ");
//		sumarFpme.append("from form_pago_movi_egre fpme ");
//		sumarFpme.append("inner join docu_movi_egre dme on dme.documento_id = fpme.documento_id "); 
//		sumarFpme.append("inner join persona p on p.persona_id = fpme.persona_id ");
//		sumarFpme.append("inner join pers_clie pc on pc.persona_id = p.persona_id ");
//		sumarFpme.append("inner join clie_grup cg on cg.clie_grup_id = pc.clie_grup_id ");
//		sumarFpme.append("inner join clie_grup_plan_cuen cgpc on cgpc.clie_grup_id = cg.clie_grup_id ");
//		sumarFpme.append("inner join plan_cuen pcuen on pcuen.plan_cuen_id = cgpc.plan_cuen_id ");
//		sumarFpme.append("where dme.tipo = ?1 and cgpc.tipo_tran = ?1 and fpme.caja_movi_id = ?2 and fpme.egreso_id ");
//		sumarFpme.append(esNulo);
//		sumarFpme.append(" ");
//		sumarFpme.append("group by cgpc.plan_cuen_id");
//
//		Query query = (Query) this.entityManager.createNativeQuery(sumarFpme.toString());
//		query.setParameter(1, "ANTICIPO");
//		query.setParameter(2, cajaMoviId);
//
//		return query.getResultList();
//	}
//
////	Suma los documentos en fpme de tipo anticipo para contabilizar el debe en la transaccion 
//	@Override
//	public List<Object[]> sumarAnticipos(Integer cajaMoviId) throws Exception {
//
////		String sumarFpme = "select pc.plan_cuen_id as plan_cuen_id, pc.codigo as plan_cuen_codigo, "
////				+ "sum(fpmeFp.total_reci) as total " 
////				+ "from form_pago_movi_egre fpme "
////				+ "inner join docu_movi_egre dme on dme.documento_id = fpme.documento_id "
////				+ "inner join fpme_form_pago fpmeFp on fpmeFp.fpme_id = fpme.fpme_id "
////				+ "inner join form_pago fp on fp.form_pago_id = fpmeFp.form_pago_id "
////				+ "inner join form_pago_plan_cuen fppc on fppc.form_pago_id = fp.form_pago_id "
////				+ "inner join plan_cuen pc on pc.plan_cuen_id = fppc.plan_cuen_id "
////				+ "where dme.tipo = ?1 and fpme.caja_movi_id = ?2 and fpme.egreso_id is null "
////				+ "group by pc.plan_cuen_id  " + "order by pc.plan_cuen_id";
//
//		StringBuilder sumarFpme = new StringBuilder();
//		sumarFpme.append("select fppc.plan_cuen_id as plan_cuen_id, ");
//		sumarFpme.append("sum(fpmeFp.total_reci) as total ");
//		sumarFpme.append("from form_pago_movi_egre fpme ");
//		sumarFpme.append("inner join docu_movi_egre dme on dme.documento_id = fpme.documento_id ");
//		sumarFpme.append(sentenciaJoinFormPago());
//		sumarFpme.append("where dme.tipo = ?1 and fpme.caja_movi_id = ?2 and fpme.egreso_id is null ");
//		sumarFpme.append("group by fppc.plan_cuen_id");
//				
//		Query query = (Query) this.entityManager.createNativeQuery(sumarFpme.toString());
//		query.setParameter(1, "ANTICIPO");
//		query.setParameter(2, cajaMoviId);
//
//		return query.getResultList();
//	}
//
//	@Override
//	public List<Object[]> sumarDepositos(Integer cajaMoviId, Integer factor) throws Exception {
//
//		String sumarFpme = "select dfpme.tipo, fp.form_pago_id, fpme.refere, fpme.nota, sum(cd.total) as cobro_deta_total, fpme.fecha "
//				+ "from cobr_deta cd " + "right join cxc cxc on cxc.cxc_id = cd.cxc_id "
//				+ "inner join egreso e on e.egreso_id = cxc.egreso_id "
//				+ "inner join documento d on d.documento_id = e.documento_id "
//				+ "left join fpme_form_pago fpmeFp on fpmeFp.fpme_form_pago_id = cd.fpme_form_pago_id "
//				+ "left join form_pago fp on fp.form_pago_id = fpmeFp.form_pago_id "
//				+ "left join form_pago_movi_egre fpme on fpme.fpme_id = fpmeFp.fpme_id "
//				+ "left join docu_movi_egre dfpme on dfpme.documento_id = fpme.documento_id "
//				+ "inner join form_pago_plan_cuen fppc on fppc.form_pago_id = fp.form_pago_id "
//				+ "inner join plan_cuen pc on pc.plan_cuen_id = fppc.plan_cuen_id "
//				+ "where d.contab = ?1 and d.factor = ?2 and fpme.caja_movi_id = ?3 and fp.tipo2 = ?4 "
//				+ "group by dfpme.tipo, fp.form_pago_id, fpme.fpme_id";
//
//		Query query = (Query) this.entityManager.createNativeQuery(sumarFpme);
//		query.setParameter(1, true);
//		query.setParameter(2, factor);
//		query.setParameter(3, cajaMoviId);
//		query.setParameter(4, "BA");
//
//		return query.getResultList();
//	}

//	public StringBuilder sentenciaSelectVentas() {
//
//		StringBuilder stringSelect = new StringBuilder("select pcuen.plan_cuen_id as plan_cuen_id, ");
//		stringSelect.append(
//				"sum((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100) - ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) * -d.factor as egre_total_neto, ");
//		stringSelect.append("sum(ed.cantid * ed.precio_vent) * -d.factor as egre_total_bruto, ");
//		stringSelect.append(
//				"sum((ed.cantid * ed.precio_vent * ed.descue / 100) + ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) * -d.factor as egre_total_descue, ");
//		stringSelect.append("sum( ");
//		stringSelect.append("( ");
//		stringSelect.append(
//				" ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100) - ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) - ");
//		stringSelect.append(
//				"	(((((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100) - ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) * (edi.porcen / 100)) ) / COALESCE((nullif((porcen / 100),0)),1)) ");
//		stringSelect.append(") * -d.factor ");
//		stringSelect.append(") as egre_sin_iva, ");
//		stringSelect.append(
//				"sum(((((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100) - ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) * (edi.porcen / 100)) * -d.factor) / COALESCE((nullif((porcen / 100),0)),1)) as egre_con_iva, ");
//		stringSelect.append(
//				"sum(((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100) - ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) * (edi.porcen / 100) + ");
//		stringSelect.append(
//				"   (case when edi.tipo = 'ICE' then edi.porcen * ed.cantid else 0 end * (select d.porcen from dimm d where d.dimm_id = 13030) / 100) ");
//		stringSelect.append("   ) * -d.factor as egre_iva, ");
//		stringSelect.append(
//				"sum(((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100) - ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) + ");
//		stringSelect.append(
//				"	(((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100) - ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) * (edi.porcen / 100)) + ");
//		stringSelect.append("	(case when edi.tipo = 'ICE' then edi.porcen * ed.cantid else 0 end) + ");
//		stringSelect.append(
//				"	(case when edi.tipo = 'ICE' then edi.porcen * ed.cantid else 0 end * (select d.porcen from dimm d where d.dimm_id = 13030) / 100) ");
//		stringSelect.append("   ) * -d.factor as egre_total, ");
//		stringSelect.append(
//				"sum(case when edi.tipo = 'ICE' then edi.porcen * ed.cantid else 0 end) * -d.factor as egre_ice, ");
//		stringSelect.append("sum(ed.cantid * ed.costo) * -d.factor as egre_costo ");
//		stringSelect.append("from egre_deta_impu edi ");
//
//		return stringSelect;
//	}
//
//	public StringBuilder sentenciaJoinVentas() {
//		StringBuilder stringJoin = new StringBuilder("inner join egre_deta ed on ed.egre_deta_id = edi.egre_deta_id ");
//		stringJoin.append("inner join egreso e on e.egreso_id = ed.egreso_id ");
//		stringJoin.append("inner join sucursal s on s.sucursal_id = e.sucursal_id ");
//		stringJoin.append("inner join docu_egre de on de.documento_id = e.documento_id ");
//		stringJoin.append("inner join documento d on d.documento_id = de.documento_id ");
//
//		return stringJoin;
//	}
//
//	public StringBuilder sentenciaJoinVentasProducto() {
//		StringBuilder stringJoin = new StringBuilder("inner join producto pr on pr.producto_id = ed.producto_id ");
//		stringJoin.append("inner join prod_grup pg on pg.prod_grup_id = pr.prod_grup_id ");
//		stringJoin.append(
//				"inner join prod_grup_plan_cuen pgpc on pgpc.prod_grup_id = pg.prod_grup_id and d.documento_id = pgpc.documento_id ");
//		stringJoin.append("inner join plan_cuen pcuen on pcuen.plan_cuen_id = pgpc.plan_cuen_id ");
//
//		return stringJoin;
//	}
//
//	public StringBuilder sentenciaJoinVentasCliente() {
//		StringBuilder stringJoin = new StringBuilder("inner join pers_clie pc on pc.persona_id = e.persona_id ");
//		stringJoin.append("inner join clie_grup cg on cg.clie_grup_id = pc.clie_grup_id ");
//		stringJoin.append(
//				"inner join clie_grup_plan_cuen cgpc on cgpc.clie_grup_id = cg.clie_grup_id and cgpc.documento_id = d.documento_id ");
//		stringJoin.append("inner join plan_cuen pcuen on pcuen.plan_cuen_id = cgpc.plan_cuen_id ");
//
//		return stringJoin;
//	}
//
//	public StringBuilder sentenciaJoinCobrDeta() {
//		StringBuilder stringJoin = new StringBuilder("from cobr_deta cd ");
//		stringJoin.append("inner join cxc cxc on cxc.cxc_id = cd.cxc_id ");
//		stringJoin.append("inner join egreso e on e.egreso_id = cd.egreso_id ");
//		stringJoin.append("inner join fpme_form_pago fpmeFp on fpmeFp.fpme_form_pago_id = cd.fpme_form_pago_id ");
//		stringJoin.append("inner join form_pago fp on fp.form_pago_id = fpmeFp.form_pago_id ");
//		stringJoin.append("inner join form_pago_movi_egre fpme on fpme.fpme_id = fpmeFp.fpme_id ");
//
//		return stringJoin;
//	}
//
//	public StringBuilder sentenciaJoinPersClie() {
//		StringBuilder stringJoin = new StringBuilder("inner join pers_clie pc on pc.persona_id = e.persona_id ");
//		stringJoin.append("inner join clie_grup cg on cg.clie_grup_id = pc.clie_grup_id ");
//		stringJoin.append(
//				"inner join clie_grup_plan_cuen cgpc on cgpc.clie_grup_id = cg.clie_grup_id and cgpc.documento_id = e.documento_id ");
//		return stringJoin;
//	}
//
//	public StringBuilder sentenciaJoinFormPago() {
//		StringBuilder stringJoin = new StringBuilder("inner join fpme_form_pago fpmeFp on fpmeFp.fpme_id = fpme.fpme_id ");
//		stringJoin.append("inner join form_pago fp on fp.form_pago_id = fpmeFp.form_pago_id ");
//		stringJoin.append("inner join form_pago_plan_cuen fppc on fppc.form_pago_id = fp.form_pago_id ");
//
//		return stringJoin;
//	}
}
