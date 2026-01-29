package ec.com.tecnointel.soem.contabilidad.registroImp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.contabilidad.modelo.TranPlantilla;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionCompraInt;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

@Stateless
public class TransaccionCompraImp extends TransaccionGestionImp implements TransaccionCompraInt {

	private static final long serialVersionUID = 164789376905416919L;

	@Override
	public Integer contabilizarCompra(Ingreso ingreso) throws Exception {

		Integer orden = 100;
		Integer transaccionId = 0;

		List<Object[]> totalDebe = new ArrayList<Object[]>();
		List<Object[]> totalHaber = new ArrayList<Object[]>();

		List<TranPlantilla> tranPlantillas = new ArrayList<>();
		List<TranPlantilla> tranPlantillaTmps = new ArrayList<>();

		// Buscar para transacion (Comprobante Ingreso, egreso , diario)
		Documento docuTran = buscarDocumentoPorId(ingreso.getDocuIngr().getDocumento());

		totalDebe = buscarProdGrupPlanCuen(ingreso, "COMPRA");
		orden = orden + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalDebe, docuTran, ingreso, 0, 3, orden, new BigDecimal(1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		totalDebe = buscarProdGrupPlanCuen(ingreso, "COMPRA IMPU");
		orden = orden + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalDebe, docuTran, ingreso, 0, 6, orden, new BigDecimal(1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

//		Busca cuentas de retenciones (rete_deta)
		totalHaber = buscarDimmPlanCuen(ingreso);
		orden = orden + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalHaber, docuTran, ingreso, 0, 5, orden, new BigDecimal(-1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

//		Buscar cxp que se genera por concepto de compra
		totalHaber = buscarProvGrupPlanCuen(ingreso);
		orden = orden + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalHaber, docuTran, ingreso, 0, 1, orden, new BigDecimal(-1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		transaccionId = gestionarTranPlantilla(tranPlantillas);

		return transaccionId;
	}

	@Override
	public Integer contabilizarNotaCredito(Ingreso ingreso, FormPagoMoviIngr fpmi, String tipoTran) throws Exception {

		Integer orden = 100;
		Integer transaccionId = 0;

//		Dependiendo del documento totalDebe puede pasar totalHaber y totalHaber a totalDebe
//		Ejemplo: Si es compra funciona normal si es nota de credito
//		se invierte y el totalDebe es totalHaber
		List<Object[]> totalDebe = new ArrayList<Object[]>();
		List<Object[]> totalHaber = new ArrayList<Object[]>();

		List<TranPlantilla> tranPlantillas = new ArrayList<>();
		List<TranPlantilla> tranPlantillaTmps = new ArrayList<>();

		// Buscar para transacion (Comprobante Ingreso, egreso , diario)
		Documento docuTran = buscarDocumentoPorId(ingreso.getDocuIngr().getDocumento());

//		Dependiendo de tipoTran carga la cuenta de CXC o anticipo
		if (tipoTran.equals("CXP-CXC")) {

//			Cargar contra la cuenta de cxc
			totalDebe = buscarProvGrupPlanCuenNc(ingreso, "CXP-CXC");
			orden = orden + tranPlantillas.size();
			tranPlantillaTmps = crearTranPlantilla(totalDebe, docuTran, ingreso, 0, 1, orden, new BigDecimal(1), 1);
			tranPlantillas.addAll(tranPlantillaTmps);

		} else if (tipoTran.equals("ANTICIPO")) {

//			Cargar contra la cuenta de anticipo
			totalDebe = buscarProvGrupPlanCuenAnticipo(fpmi);
			orden = orden + tranPlantillas.size();
			tranPlantillaTmps = crearTranPlantilla(totalDebe, docuTran, ingreso, 0, 1, orden, new BigDecimal(1), 1);
			tranPlantillas.addAll(tranPlantillaTmps);
		}

		totalHaber = buscarProdGrupPlanCuen(ingreso, "COMPRA");
		orden = orden + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalHaber, docuTran, ingreso, 0, 3, orden, new BigDecimal(-1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		totalHaber = buscarProdGrupPlanCuen(ingreso, "COMPRA IMPU");
		orden = orden + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalHaber, docuTran, ingreso, 0, 6, orden, new BigDecimal(-1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		transaccionId = gestionarTranPlantilla(tranPlantillas);

		return transaccionId;
	}

//	Contabiliza ingreso y egresos de bodega
//	Se determina porque tiene verdadero el campo afectaCost
	public Integer contabilizarIngreso(Ingreso ingreso) throws Exception {

//		Coloca las filas de una forma ascendente o descendente
//		para determinar el order de debe y del haber porque aqui se 
//		contabilizara ingreso y egresos de bodega
		int factorOrden = ingreso.getDocuIngr().getDocumento().getFactor();

//		Valor inicial para ordenar puede ir ascendente o descendente
		Integer orden = 100;
		Integer transaccionId = 0;

//		Coloca valores positivos o negativos dependiendo del factor del documento
		BigDecimal factorDebe;
		BigDecimal factorHaber;

//		Estos objetos se usan para almacenar plan_cuen_id y
//		el valor que va ir en cada fila de tran_deta
//		Dependiendo del documento totalDebe puede pasar totalHaber y totalHaber a totalDebe
//		Ejemplo: Si es ingreso de bodega funciona normal si es egreso de bodega
//		se invierte y el totalDebe es totalHaber
		List<Object[]> totalDebe = new ArrayList<Object[]>();
		List<Object[]> totalHaber = new ArrayList<Object[]>();

		List<TranPlantilla> tranPlantillas = new ArrayList<>();
		List<TranPlantilla> tranPlantillaTmps = new ArrayList<>();

		if (ingreso.getDocuIngr().getDocumento().getFactor() == 1) {
			factorDebe = new BigDecimal(1);
			factorHaber = new BigDecimal(-1);
		} else {
			factorDebe = new BigDecimal(-1);
			factorHaber = new BigDecimal(1);
		}

//		Documento para generar transaccion (Comprobante Ingreso, egreso , diario)
		Documento docuTran = buscarDocumentoPorId(ingreso.getDocuIngr().getDocumento());

//		Buscar cuenta en grupos de productos
		totalDebe = buscarProdGrupPlanCuen(ingreso, "COMPRA");
		orden = orden + tranPlantillas.size() * factorOrden;
		tranPlantillaTmps = crearTranPlantilla(totalDebe, docuTran, ingreso, 0, 3, orden, factorDebe, factorOrden);
		tranPlantillas.addAll(tranPlantillaTmps);

//		Buscar cuenta en grupos de proveedores
		totalHaber = buscarProvGrupPlanCuen(ingreso, "CXP-CXC");
		orden = orden + tranPlantillas.size() * factorOrden;
		tranPlantillaTmps = crearTranPlantilla(totalHaber, docuTran, ingreso, 0, 3, orden, factorHaber, factorOrden);
		tranPlantillas.addAll(tranPlantillaTmps);

		transaccionId = gestionarTranPlantilla(tranPlantillas);

		return transaccionId;
	}

	public List<Object[]> buscarProdGrupPlanCuen(Ingreso ingreso, String tipoTran) throws Exception {

		StringBuilder sentencia = new StringBuilder();

		sentencia.append(sentenciaSelect());
		sentencia.append(sentenciaJoinProducto());
		sentencia.append("where i.ingreso_id = ?1 ");
		sentencia.append("and pgpc.documento_id = ?2 ");
		sentencia.append("and pgpc.tipo_tran = ?3 ");
		sentencia.append("and idi.tipo = ?4 ");
		sentencia.append("group by pgpc.plan_cuen_id, d.documento_id");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, ingreso.getIngresoId());
		query.setParameter(2, ingreso.getDocuIngr().getDocumentoId());
		query.setParameter(3, tipoTran);
		query.setParameter(4, "IVA");

		return query.getResultList();
	}

//	Se use esta consulta para los ingresos y egresos de bodega ya que no generan cxp
//	entonces se obtiene el valor de tran_deta del mismo documento que se esta procesando
//	no de cxp como en caso de la compra.
//	Se envia el parametro CXP-CXC para para enlazar la cuenta contable	
	public List<Object[]> buscarProvGrupPlanCuen(Ingreso ingreso, String tipoTran) throws Exception {

		StringBuilder sentencia = new StringBuilder();

		sentencia.append(sentenciaSelect());
		sentencia.append(sentenciaJoinProvedor());
		sentencia.append("where i.ingreso_id = ?1 ");
		sentencia.append("and pgpc.documento_id = ?2 ");
		sentencia.append("and pgpc.tipo_tran = ?3 ");
		sentencia.append("and idi.tipo = ?4 ");
		sentencia.append("group by pgpc.plan_cuen_id, d.documento_id");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, ingreso.getIngresoId());
		query.setParameter(2, ingreso.getDocuIngr().getDocumentoId());
		query.setParameter(3, tipoTran);
		query.setParameter(4, "IVA");

		return query.getResultList();
	}

//	Este metodo se usa para la compra ya que todas se registran contra cxp
//	El valor se toma directamente de cxp y no del total de la compra 
//	ya que la compra puede tener retenciones y el valor de cxp seria diferente	
	public List<Object[]> buscarProvGrupPlanCuen(Ingreso ingreso) throws Exception {

		StringBuilder sentencia = new StringBuilder();

		sentencia.append("select pgpc.plan_cuen_id as plan_cuen_id, ");
		sentencia.append("cxp.total as total ");
		sentencia.append("from cxp cxp ");
		sentencia.append("inner join ingreso i on i.ingreso_id = cxp.ingreso_id ");
		sentencia.append("inner join pers_prov pp on pp.persona_id = i.persona_id ");
		sentencia.append("inner join prov_grup pg on pg.prov_grup_id = pp.prov_grup_id ");
		sentencia.append("inner join prov_grup_plan_cuen pgpc on pgpc.prov_grup_id = pg.prov_grup_id ");
		sentencia.append("inner join docu_ingr di on di.documento_id = i.documento_id ");
		sentencia.append("where i.ingreso_id = ?1 ");
		sentencia.append("and pgpc.documento_id = ?2 ");
		sentencia.append("and pgpc.tipo_tran = ?3");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, ingreso.getIngresoId());
		query.setParameter(2, ingreso.getDocuIngr().getDocumentoId());
		query.setParameter(3, "CXP-CXC");

		return query.getResultList();
	}

	public List<Object[]> buscarDimmPlanCuen(Ingreso ingreso) throws Exception {

		StringBuilder sentencia = new StringBuilder();

		sentencia.append("select dpc.plan_cuen_id, ");
		sentencia.append("rd.impues as impues, rd.codigo_impu as codigo_impu, ");
		sentencia.append("rd.base as base, rd.porcen as porcen, ");
		sentencia.append("(rd.base * rd.porcen / 100) as rete_deta_total ");
		sentencia.append("from rete_deta rd ");
		sentencia.append("inner join dimm dimm on dimm.codigo = rd.codigo_impu ");
		sentencia.append("inner join dimm_plan_cuen dpc on dpc.dimm_id = dimm.dimm_id ");
		sentencia.append("inner join retencion r on r.retencion_id = rd.retencion_id ");
		sentencia.append("where r.ingreso_id = ?1 ");
		sentencia.append("and dimm.tabla_refe in (?2,?3)");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, ingreso.getIngresoId());
		query.setParameter(2, "Tabla3");
		query.setParameter(3, "Tabla11");

		return query.getResultList();
	}

//	Se paga la nota de credito con nota de credito
	public List<Object[]> buscarProvGrupPlanCuenNc(Ingreso ingreso, String tipoTran) throws Exception {

		StringBuilder sentencia = new StringBuilder();

		sentencia.append("select pgpc.plan_cuen_id as plan_cuen_id, ");
		sentencia.append("pd.total as total ");
		sentencia.append("from pago_deta pd ");
		sentencia.append("inner join ingreso i on i.ingreso_id = pd.ingreso_id ");
		sentencia.append("inner join pers_prov pp on pp.persona_id = i.persona_id ");
		sentencia.append("inner join prov_grup pg on pg.prov_grup_id = pp.prov_grup_id ");
		sentencia.append("inner join prov_grup_plan_cuen pgpc on pgpc.prov_grup_id = pg.prov_grup_id ");
		sentencia.append("inner join docu_ingr di on di.documento_id = i.documento_id ");
		sentencia.append("where i.ingreso_id = ?1 ");
		sentencia.append("and pgpc.documento_id = ?2 ");
		sentencia.append("and pgpc.tipo_tran = ?3");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, ingreso.getIngresoId());
		query.setParameter(2, ingreso.getDocuIngr().getDocumentoId());
		query.setParameter(3, tipoTran);

		return query.getResultList();
	}

//	Se paga la nota de credito con anticipo
	public List<Object[]> buscarProvGrupPlanCuenAnticipo(FormPagoMoviIngr fpmi) throws Exception {

		StringBuilder sentencia = new StringBuilder();

		sentencia.append("select pgpc.plan_cuen_id as plan_cuen_id, ");
		sentencia.append("fpmi.total as total ");
		sentencia.append("from form_pago_movi_ingr fpmi ");
		sentencia.append("inner join pers_prov pp on pp.persona_id = fpmi.persona_id ");
		sentencia.append("inner join prov_grup pg on pg.prov_grup_id = pp.prov_grup_id ");
		sentencia.append("inner join prov_grup_plan_cuen pgpc on pgpc.prov_grup_id = pg.prov_grup_id ");
		sentencia.append("where fpmi.fpmi_id = ?1 ");
		sentencia.append("and pgpc.documento_id = ?2 ");
		sentencia.append("and pgpc.tipo_tran = ?3");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, fpmi.getFpmiId());
		query.setParameter(2, fpmi.getDocuMoviIngr().getDocumentoId());
		query.setParameter(3, "ANTICIPO");

		return query.getResultList();
	}

	public StringBuilder sentenciaSelect() {
		StringBuilder sentencia = new StringBuilder();
		sentencia.append("select pgpc.plan_cuen_id as plan_cuen_id, ");
		sentencia.append("sum(id.cantid * id.costo) as ingr_total_bruto, ");
		sentencia.append(
				"sum((id.cantid * id.costo * id.descue / 100) + ((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100)) * (i.descue / 100)) as ingr_total_descue, ");
		sentencia.append(
				"sum((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100) - ((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100)) * (i.descue / 100)) as ingr_total_neto, ");
		sentencia.append("sum( ");
		sentencia.append("( ");
		sentencia.append(
				"((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100) - ((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100)) * (i.descue / 100)) - ");
		sentencia.append(
				"(((((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100) - ((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100)) * (i.descue / 100)) * (idi.porcen / 100))) / COALESCE((nullif((porcen / 100),0)),1)) ");
		sentencia.append(") ");
		sentencia.append(") as ingr_sin_iva, ");
		sentencia.append(
				"sum(((((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100) - ((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100)) * (i.descue / 100)) * (idi.porcen / 100))) / COALESCE((nullif((porcen / 100),0)),1)) as ingr_con_iva, ");
		sentencia.append(
				"sum(((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100) - ((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100)) * (i.descue / 100)) * (idi.porcen / 100)) as ingr_iva, ");
		sentencia.append(
				"sum(((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100) - ((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100)) * (i.descue / 100)) + ");
		sentencia.append(
				"(((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100) - ((id.cantid * id.costo) - (id.cantid * id.costo * id.descue / 100)) * (i.descue / 100)) * (idi.porcen / 100))) as ingr_total ");
		sentencia.append("from ingr_deta_impu idi ");

		return sentencia;
	}

	public StringBuilder sentenciaJoinProducto() {
		StringBuilder sentencia = new StringBuilder();
		sentencia.append("inner join ingr_deta id on id.ingr_deta_id = idi.ingr_deta_id ");
		sentencia.append("inner join producto p on p.producto_id = id.producto_id ");
		sentencia.append("inner join prod_grup pg on pg.prod_grup_id = p.prod_grup_id ");
		sentencia.append("inner join prod_grup_plan_cuen pgpc on pgpc.prod_grup_id = pg.prod_grup_id ");
		sentencia.append("inner join ingreso i on i.ingreso_id = id.ingreso_id ");
		sentencia.append("inner join documento d on d.documento_id = i.documento_id ");
		return sentencia;
	}

	public StringBuilder sentenciaJoinProvedor() {
		StringBuilder sentencia = new StringBuilder();
		sentencia.append("inner join ingr_deta id on id.ingr_deta_id = idi.ingr_deta_id ");
		sentencia.append("inner join ingreso i on i.ingreso_id = id.ingreso_id ");
		sentencia.append("inner join pers_prov pp on pp.persona_id = i.persona_id ");
		sentencia.append("inner join prov_grup pg on pg.prov_grup_id = pp.prov_grup_id ");
		sentencia.append("inner join prov_grup_plan_cuen pgpc on pgpc.prov_grup_id = pg.prov_grup_id ");
		sentencia.append("inner join documento d on d.documento_id = i.documento_id ");

		return sentencia;
	}
}
