package ec.com.tecnointel.soem.ingreso.listaImp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.ingreso.listaInt.IngresoListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class IngresoListaImp extends GestorListaSoem<Ingreso> implements IngresoListaInt, Serializable {

	private static final long serialVersionUID = 6529969720872352365L;

	@Override
	public List<Ingreso> buscar(Ingreso ingreso, Integer pagina) {

		EntityGraph<?> ingresoGraph = this.entityManager.getEntityGraph("ingreso.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Ingreso> query = builder.createQuery(Ingreso.class);
		Root<Ingreso> ingresoRoot = query.from(Ingreso.class);

		if (this.orden.equals("Desc")) {
			query.orderBy(builder.desc(ingresoRoot.get("nuemro")));
		} else {
			query.orderBy(builder.asc(ingresoRoot.get("numero")));
		}

		TypedQuery<Ingreso> consulta = this.entityManager
				.createQuery(query.select(ingresoRoot).where(getSearchPredicates(ingresoRoot, ingreso)));
		consulta.setHint("jakarta.persistence.loadgraph", ingresoGraph);

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(Ingreso ingreso) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<Ingreso> ingresoRoot = countQuery.from(Ingreso.class);

		countQuery = countQuery.select(builder.count(ingresoRoot)).where(getSearchPredicates(ingresoRoot, ingreso));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<Ingreso> ingresoRoot, Ingreso ingreso) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer sucursalId = ingreso.getSucursal().getSucursalId();
		if (sucursalId != null) {
			predicates.add(builder.equal(ingresoRoot.get("sucursal").<Integer>get("sucursalId"), sucursalId));
		}

		String cedulaRuc = ingreso.getPersProv().getPersona().getCedulaRuc();
		if (cedulaRuc != null) {
			predicates.add(
					builder.equal(builder.lower(ingresoRoot.get("persProv").get("persona").<String>get("cedulaRuc")),
							cedulaRuc.toLowerCase()));
		}

		String apelli = ingreso.getPersProv().getPersona().getApelli();
		if (apelli != null) {
			predicates.add(builder.like(builder.lower(ingresoRoot.get("persProv").get("persona").<String>get("apelli")),
					'%' + apelli.toLowerCase() + '%'));
		}

		String nombre = ingreso.getPersProv().getPersona().getNombre();
		if (nombre != null) {
			predicates.add(builder.like(builder.lower(ingresoRoot.get("persProv").get("persona").<String>get("nombre")),
					'%' + nombre.toLowerCase() + '%'));
		}

		String docuIngrDescri = ingreso.getDocuIngr().getDocumento().getDescri();
		if (docuIngrDescri != null) {
			predicates
					.add(builder.like(builder.lower(ingresoRoot.get("docuIngr").get("documento").<String>get("descri")),
							'%' + docuIngrDescri.toLowerCase() + '%'));
		}

		String docuEgreDocumeElec = ingreso.getDocuIngr().getDocumeElec();
		if (docuEgreDocumeElec != null) {
			predicates.add(builder.notEqual(builder.lower(ingresoRoot.get("docuIngr").<String>get("documeElec")),
					docuEgreDocumeElec.toLowerCase()));
		}

		Integer ingresoId = ingreso.getIngresoId();
		if (ingresoId != null) {
			predicates.add(builder.equal(ingresoRoot.get("ingresoId"), ingresoId));
		}

		if (ingreso.getIngreso() != null) {
			Integer ingresoSupeId = ingreso.getIngreso().getIngresoId();
			if (ingresoSupeId != null) {
				predicates.add(builder.equal(ingresoRoot.get("ingreso").get("ingresoId"), ingresoSupeId));
			}
		}

		String serie1 = ingreso.getSerie1();
		if (serie1 != null) {
			predicates.add(builder.equal(ingresoRoot.get("serie1"), serie1));
		}

		String serie2 = ingreso.getSerie2();
		if (serie2 != null) {
			predicates.add(builder.equal(ingresoRoot.get("serie2"), serie2));
		}

		Integer numero = ingreso.getNumero();
		if (numero != null) {
			predicates.add(builder.equal(ingresoRoot.get("numero"), numero));
		}

		LocalDate fechaEmis = ingreso.getFechaEmis();
		if (fechaEmis != null) {
			predicates.add(builder.greaterThanOrEqualTo(ingresoRoot.<LocalDate>get("fechaEmis"), fechaEmis));
		}

		String estadoDocuElec = ingreso.getEstadoDocuElec();
		if (estadoDocuElec != null) {
			predicates.add(builder.notEqual(builder.lower(ingresoRoot.<String>get("estadoDocuElec")),
					estadoDocuElec.toLowerCase()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public List<Ingreso> buscarTransaccion(Ingreso ingreso) {

		EntityGraph<?> ingresoGraph = this.entityManager.getEntityGraph("ingreso.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Ingreso> query = builder.createQuery(Ingreso.class);
		Root<Ingreso> ingresoRoot = query.from(Ingreso.class);

		TypedQuery<Ingreso> consulta = this.entityManager
				.createQuery(query.select(ingresoRoot).where(getSearchPredicatesTransaccion(ingresoRoot, ingreso)));
		consulta.setHint("jakarta.persistence.loadgraph", ingresoGraph);

		return consulta.getResultList();
	}

	private Predicate[] getSearchPredicatesTransaccion(Root<Ingreso> ingresoRoot, Ingreso ingreso) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer transaccionId = ingreso.getTransaccion().getTransaccionId();
		if (transaccionId != null) {
			predicates.add(builder.equal(ingresoRoot.get("transaccion").<Integer>get("transaccionId"), transaccionId));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public List<Object[]> buscarIngresAtsSri(LocalDate fechaDesd, LocalDate fechaHast) throws Exception{

		StringBuilder selectDetalleCompras = new StringBuilder();
			selectDetalleCompras.append("select i.ingreso_id as ingreso_id, ");
			selectDetalleCompras.append("dimm_ingr.codigo as codigo_sust, ");
			selectDetalleCompras.append("dimm_prov.codigo as tipo_iden_prov, ");
			selectDetalleCompras.append("p.cedula_ruc as nume_iden_prov, ");
			selectDetalleCompras.append("di.tipo_comp as tipo_compr, ");
			selectDetalleCompras.append("pp.natura as tipo_prov, ");
			selectDetalleCompras.append("p.apelli as pers_apelli, ");
			selectDetalleCompras.append("p.nombre as pers_nombre, ");
			selectDetalleCompras.append("pp.parte_rela as parte_rela, "); 
			selectDetalleCompras.append("i.fecha_regi as ingres_fecha_regi, ");
			selectDetalleCompras.append("i.serie1 as ingres_esta, ");
			selectDetalleCompras.append("i.serie2 as ingres_punt_emis, ");
			selectDetalleCompras.append("i.numero as ingres_secu, ");
			selectDetalleCompras.append("i.fecha_emis as ingres_fecha_emis, ");
			selectDetalleCompras.append("i.autori as ingres_auto, ");
//			esto da error cuando se ejecuta la sentencia			
//			sentencia.append(" null as base_no_obje, "); 
			selectDetalleCompras.append("sum( (");
			selectDetalleCompras.append("    ((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100) - ((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100)) * (i.descue / 100)) - ");
			selectDetalleCompras.append("    (((((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100) - ((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100)) * (i.descue / 100)) * (idi.porcen / 100))) / COALESCE((nullif((idi.porcen / 100),0)),1))");
			selectDetalleCompras.append(") * d.factor ");
			selectDetalleCompras.append(") as base_tari_0, ");
			selectDetalleCompras.append("sum(((((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100) - ((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100)) * (i.descue / 100)) * (idi.porcen / 100)) * d.factor) / COALESCE((nullif((idi.porcen / 100),0)),1)) as base_grab, ");
//			esto da error cuando se ejecuta la sentencia			
//			sentencia.append("null as base_exen, null as monto_ice, ");
			selectDetalleCompras.append("sum(((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100) - ((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100)) * (i.descue / 100)) * (idi.porcen / 100)) * d.factor as monto_iva, ");
			
			selectDetalleCompras.append("sum(((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100) - ((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100)) * (i.descue / 100)) + ");
			selectDetalleCompras.append("(((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100) - ((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100)) * (i.descue / 100)) * (case when di.genera_impu then  idi.porcen / 100 else 0 end))) * d.factor as ingr_total, ");

			selectDetalleCompras.append("r.serie1 as retenc_esta, r.serie2 as retenc_punt_emis, r.numero as retenc_secu, r.autori as retenc_auto, r.fecha_emis as retenc_fecha_emis, ");
			selectDetalleCompras.append("di_supe.tipo_comp as ingr_supe_tipo_docu, ingr_supe.serie1 as ingr_supe_estab, ingr_supe.serie2 as ingr_supe_punt_emis, ingr_supe.numero as ingr_supe_secu, ingr_supe.autori as ingr_supe_auto ");
			selectDetalleCompras.append("from ingr_deta_impu idi ");
			selectDetalleCompras.append("inner join ingr_deta id on id.ingr_deta_id = idi.ingr_deta_id ");
			selectDetalleCompras.append("inner join ingreso i on i.ingreso_id = id.ingreso_id ");
			selectDetalleCompras.append("left join ingreso ingr_supe on ingr_supe.ingreso_id = i.ingreso_supe_id ");
			selectDetalleCompras.append("left join docu_ingr di_supe on di_supe.documento_id = ingr_supe.documento_id ");
			selectDetalleCompras.append("left join retencion r on r.ingreso_id = i.ingreso_id ");
			selectDetalleCompras.append("inner join dimm dimm_ingr on dimm_ingr.dimm_id = i.dimm_id ");
			selectDetalleCompras.append("inner join sucursal s on s.sucursal_id = i.sucursal_id ");
			selectDetalleCompras.append("inner join pers_prov pp on pp.persona_id = i.persona_id ");
			selectDetalleCompras.append("inner join persona p on p.persona_id = pp.persona_id ");
			selectDetalleCompras.append("inner join dimm dimm_prov on dimm_prov.dimm_id = pp.dimm_id ");
			selectDetalleCompras.append("inner join docu_ingr di on di.documento_id = i.documento_id ");
			selectDetalleCompras.append("inner join documento d on d.documento_id = di.documento_id ");
			selectDetalleCompras.append("inner join dimm dimm_docu on dimm_docu.dimm_id = di.dimm_id ");
			selectDetalleCompras.append("where i.fecha_emis between ?1 and ?2 and ");
			selectDetalleCompras.append("di.genera_anex = ?3 and ");
			selectDetalleCompras.append("idi.tipo = 'IVA' and i.estado = 'PR' ");
			selectDetalleCompras.append("group by dimm_ingr.dimm_id, dimm_prov.dimm_id, dimm_docu.dimm_id, p.persona_id, pp.persona_id, ");
			selectDetalleCompras.append("d.documento_id, di.documento_id, i.ingreso_id, ingr_supe.ingreso_id, di_supe.documento_id, r.retencion_id ");
			selectDetalleCompras.append("order by p.apelli, p.nombre, d.descri, i.fecha_emis, i.numero");
		
			Query query = (Query) this.entityManager.createNativeQuery(selectDetalleCompras.toString());
			query.setParameter(1, fechaDesd);
			query.setParameter(2, fechaHast);
			query.setParameter(3, true);

		return query.getResultList();
	}
	
	@Override
	public List<Object[]> buscarFormaPago(Integer ingresoId) throws Exception {
		
		StringBuilder selectFormaPago = new StringBuilder();
		
		selectFormaPago.append("select cxp.ingreso_id as ingreso_id, COALESCE(dimm.codigo,'01') as forma_pago ");
		selectFormaPago.append("from pago_deta pd ");
		selectFormaPago.append("inner join form_pago_movi_ingr fpmi on fpmi.fpmi_id = pd.fpmi_id ");
		selectFormaPago.append("inner join form_pago fp on fp.form_pago_id = fpmi.form_pago_id ");
		selectFormaPago.append("right join cxp cxp on cxp.cxp_id = pd.cxp_id ");
		selectFormaPago.append("inner join ingreso i on i.ingreso_id = cxp.ingreso_id ");
		selectFormaPago.append("inner join pers_prov pp on pp.persona_id = i.persona_id ");
		selectFormaPago.append("inner join docu_ingr di on di.documento_id = i.documento_id ");
		selectFormaPago.append("left join dimm dimm on dimm.dimm_id = fp.dimm_id "); 
		selectFormaPago.append("where i.ingreso_id = ?1 ");
		selectFormaPago.append("and i.total > ?2");
	
		Query query = (Query) this.entityManager.createNativeQuery(selectFormaPago.toString());
		query.setParameter(1, ingresoId);
//		TODO: parametrizar este valor ya que lo determina el SRI
//		antes era 1000
		query.setParameter(2, 500);

		return query.getResultList();
	}
	
	@Override
	public List<Object[]> buscarAir(Integer ingresoId, String impuesto) throws Exception{
		
		StringBuilder selectAir = new StringBuilder();
		
		selectAir.append("select i.ingreso_id as ingreso_id, ");
		selectAir.append("codigo_impu as codigo_rete, ");
		selectAir.append("base as base_impo, ");
		selectAir.append("porcen as porcen_rete, ");
		selectAir.append("(base * porcen / 100) as valor_rete ");
		selectAir.append("from rete_deta rd ");
		selectAir.append("inner join retencion r on r.retencion_id = rd.retencion_id ");
		selectAir.append("inner join ingreso i on i.ingreso_id = r.ingreso_id ");
		selectAir.append("where i.ingreso_id = ?1 ");
		selectAir.append("and impues = ?2");
	
		Query query = (Query) this.entityManager.createNativeQuery(selectAir.toString());
		query.setParameter(1, ingresoId);
		query.setParameter(2, impuesto);

		return query.getResultList();
	}
	
	@Override
	public List<Ingreso> buscar2(Ingreso ingreso, LocalDate fechaEmisDesde, LocalDate fechaEmisHasta, Integer pagina) {

		EntityGraph<?> ingresoGraph = this.entityManager.getEntityGraph("ingreso.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Ingreso> query = builder.createQuery(Ingreso.class);
		Root<Ingreso> ingresoRoot = query.from(Ingreso.class);

		if (this.orden.equals("Desc")) {
			query.orderBy(builder.desc(ingresoRoot.get("ingresoId")));
		} else {
			query.orderBy(builder.asc(ingresoRoot.get("ingresoId")));
		}

		TypedQuery<Ingreso> consulta = this.entityManager
				.createQuery(query.select(ingresoRoot).where(getSearchPredicates(ingresoRoot, ingreso, fechaEmisDesde, fechaEmisHasta)));
		consulta.setHint("jakarta.persistence.loadgraph", ingresoGraph);

		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros2(Ingreso ingreso, LocalDate fechaEmisDesde, LocalDate fechaEmisHasta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<Ingreso> ingresoRoot = countQuery.from(Ingreso.class);

		countQuery = countQuery.select(builder.count(ingresoRoot)).where(getSearchPredicates(ingresoRoot, ingreso, fechaEmisDesde, fechaEmisHasta));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<Ingreso> ingresoRoot, Ingreso ingreso, LocalDate fechaEmisDesde, LocalDate fechaEmisHasta) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer sucursalId = ingreso.getSucursal().getSucursalId();
		if (sucursalId != null) {
			predicates.add(builder.equal(ingresoRoot.get("sucursal").<Integer>get("sucursalId"), sucursalId));
		}
		
		String docuEgreDocumeElec = ingreso.getDocuIngr().getDocumeElec();
		if (docuEgreDocumeElec != null) {
			predicates.add(builder.notEqual(builder.lower(ingresoRoot.get("docuIngr").<String>get("documeElec")),
					docuEgreDocumeElec.toLowerCase()));
		}
		Integer numero = ingreso.getNumero();
		if (numero != null) {
			predicates.add(builder.equal(ingresoRoot.get("numero"), numero));
		}
		
		if (fechaEmisDesde != null && fechaEmisHasta != null) {
			predicates.add(builder.between(ingresoRoot.<LocalDate> get("fechaEmis"), fechaEmisDesde, fechaEmisHasta));
		}

		String estadoDocuElec = ingreso.getEstadoDocuElec();
		if (estadoDocuElec != null) {
			predicates.add(builder.notEqual(builder.lower(ingresoRoot.<String>get("estadoDocuElec")),
					estadoDocuElec.toLowerCase()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

}