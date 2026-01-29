package ec.com.tecnointel.soem.contabilidad.registroImp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.contabilidad.modelo.TranPlantilla;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionVentaInt;
import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviEgre;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

@Stateless
public class TransaccionVentaImp extends TransaccionGestionImp implements TransaccionVentaInt {

	private static final long serialVersionUID = 6989727821718692677L;

	@Override
	public Integer contabilizarVenta(Egreso egreso) throws Exception {

		Integer orden = 100;
		Integer transaccionId = 0;

		List<Object[]> totalDebe = new ArrayList<Object[]>();
		List<Object[]> totalHaber = new ArrayList<Object[]>();

		List<TranPlantilla> tranPlantillas = new ArrayList<>();
		List<TranPlantilla> tranPlantillaTmps = new ArrayList<>();

		// Buscar para transacion (Comprobante Ingreso, egreso , diario)
		Documento docuTran = buscarDocumentoPorId(egreso.getDocuEgre().getDocumento());

//		Contabiliza el debe en transaccion de Ventas
//		Suma las Ventas de las facturas que se hicieron en el inicio de caja
		totalDebe = buscarClieGrupPlanCuen(egreso, "CXP-CXC");
		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalDebe, docuTran, egreso, 0, 7, orden, new BigDecimal(1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		totalHaber = buscarProdGrupPlanCuen(egreso, "VENTA SIN IMPU");
		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalHaber, docuTran, egreso, 0, 4, orden, new BigDecimal(-1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		totalHaber = buscarProdGrupPlanCuen(egreso, "VENTA CON IMPU");
		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalHaber, docuTran, egreso, 0, 5, orden, new BigDecimal(-1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		totalHaber = buscarProdGrupPlanCuen(egreso, "VENTA IMPU");
		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalHaber, docuTran, egreso, 0, 6, orden, new BigDecimal(-1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		totalHaber = buscarProdGrupPlanCuen(egreso, "ICE");
		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalHaber, docuTran, egreso, 0, 8, orden, new BigDecimal(-1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		transaccionId = gestionarTranPlantilla(tranPlantillas);
		return transaccionId;
	}

//	Contabiliza el costo de ventas de producto simples y de subProductos
	@Override
	public Integer contabilizarCostoVenta(Egreso egreso) throws Exception {

//		Coloca las filas de una forma ascendente o descendente
//		para determinar el order de debe y del haber porque aqui se 
//		contabilizara ingreso y egresos de bodega
		int factorOrden = egreso.getDocuEgre().getDocumento().getFactor() * -1;

		Integer orden = 100;
		Integer transaccionId = 0;

//		Coloca valores positivos o negativos dependiendo del factor del documento
		BigDecimal factorDebe;
		BigDecimal factorHaber;

		List<Object[]> totalDebe = new ArrayList<Object[]>();
		List<Object[]> totalHaber = new ArrayList<Object[]>();

		List<TranPlantilla> tranPlantillas = new ArrayList<>();
		List<TranPlantilla> tranPlantillaTmps = new ArrayList<>();

		if (egreso.getDocuEgre().getDocumento().getFactor() == -1) {
			factorDebe = new BigDecimal(1);
			factorHaber = new BigDecimal(-1);
		} else {
			factorDebe = new BigDecimal(-1);
			factorHaber = new BigDecimal(1);
		}

		// Buscar para transacion (Comprobante Ingreso, egreso , diario)
		Documento docuTran = buscarDocumentoPorId(egreso.getDocuEgre().getDocumento());

//		Producto normal
		totalDebe = buscarProdGrupPlanCuenCosto(egreso, "COSTO");
		orden = orden + tranPlantillas.size() * factorOrden;
		tranPlantillaTmps = this.crearTranPlantilla(totalDebe, docuTran, egreso, 0, 9, orden, factorDebe, factorOrden);
		tranPlantillas.addAll(tranPlantillaTmps);

//		SubProducto
		totalDebe = buscarProdGrupPlanCuenCostoSubProducto(egreso, "COSTO");
		orden = orden + tranPlantillas.size() * factorOrden;
		tranPlantillaTmps = this.crearTranPlantilla(totalDebe, docuTran, egreso, 0, 1, orden, factorDebe, factorOrden);
		tranPlantillas.addAll(tranPlantillaTmps);

//		Producto normal
		totalHaber = buscarProdGrupPlanCuenCosto(egreso, "COMPRA");
		orden = orden + tranPlantillas.size() * factorOrden;
		tranPlantillaTmps = this.crearTranPlantilla(totalHaber, docuTran, egreso, 0, 9, orden, factorHaber,
				factorOrden);
		tranPlantillas.addAll(tranPlantillaTmps);

//		SubProducto
		totalHaber = buscarProdGrupPlanCuenCostoSubProducto(egreso, "COMPRA");
		orden = orden + tranPlantillas.size() * factorOrden;
		tranPlantillaTmps = this.crearTranPlantilla(totalHaber, docuTran, egreso, 0, 1, orden, factorHaber,
				factorOrden);
		tranPlantillas.addAll(tranPlantillaTmps);

//		Si no encuentra datos en costoVentas no contabiliza nada
		if (tranPlantillas.isEmpty()) {
			return 0;
		}
		
		transaccionId = gestionarTranPlantilla(tranPlantillas);
		return transaccionId;
	}

	@Override
	public Integer contabilizarNotaCredito(Egreso egreso, FormPagoMoviEgre fpme) throws Exception {
		Integer orden = 100;
		Integer transaccionId = 0;

		List<Object[]> totalDebe = new ArrayList<Object[]>();
		List<Object[]> totalHaber = new ArrayList<Object[]>();

		List<TranPlantilla> tranPlantillas = new ArrayList<>();
		List<TranPlantilla> tranPlantillaTmps = new ArrayList<>();

		// Buscar para transacion (Comprobante Ingreso, egreso , diario)
		Documento docuTran = buscarDocumentoPorId(egreso.getDocuEgre().getDocumento());

		totalDebe = buscarProdGrupPlanCuen(egreso, "VENTA SIN IMPU");
		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalDebe, docuTran, egreso, 0, 4, orden, new BigDecimal(1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		totalDebe = buscarProdGrupPlanCuen(egreso, "VENTA CON IMPU");
		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalDebe, docuTran, egreso, 0, 5, orden, new BigDecimal(1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		totalDebe = buscarProdGrupPlanCuen(egreso, "VENTA IMPU");
		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalDebe, docuTran, egreso, 0, 6, orden, new BigDecimal(1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		totalDebe = buscarProdGrupPlanCuen(egreso, "ICE");
		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalDebe, docuTran, egreso, 0, 8, orden, new BigDecimal(1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

//		Contabiliza el debe en transaccion de Ventas
//		Suma las Ventas de las facturas que se hicieron en el inicio de caja
		totalHaber = buscarCobroCxcsNcs(fpme);
		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalHaber, docuTran, egreso, 0, 1, orden, new BigDecimal(-1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		transaccionId = gestionarTranPlantilla(tranPlantillas);
		return transaccionId;
	}

//	Lista para contabilizar el debe en una transaccion de ventas
//	Se agrupo por group by pcuen.plan_cuen_id, d.factor para que ponga un solo valor y acumule en la cuenta de ventas del cliente
//	Si se agrupa de esta manera group by pcuen.plan_cuen_id, d.documento_id coloca una linea de total por cada documento
//	Los mismo para el metodo sumarCostoVentas
	public List<Object[]> buscarClieGrupPlanCuen(Egreso egreso, String tipoTran) throws Exception {

		StringBuilder sentencia = new StringBuilder();
		sentencia.append(sentenciaSelectVentas());
		sentencia.append(sentenciaJoinVentas());
		sentencia.append(sentenciaJoinPersClie());
		sentencia.append("where e.egreso_id = ?1 ");
		sentencia.append("and cgpc.documento_id = ?2 ");
		sentencia.append("and cgpc.tipo_tran = ?3 ");
		sentencia.append("group by e.documento_id, cgpc.plan_cuen_id");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, egreso.getEgresoId());
		query.setParameter(2, egreso.getDocuEgre().getDocumentoId());
		query.setParameter(3, tipoTran);

		return query.getResultList();
	}

//	Lista para contabilizar el haber en una transaccion de ventas
//	Se agrupo por group by pcuen.plan_cuen_id, d.factor para que ponga un solo valor de todos los documentos Ej: facturas, Facturas electronicas, notas de venta
//	Si se agrupa de esta manera group by pcuen.plan_cuen_id, d.documento_id coloca una linea de total por cada documento
//	Los mismo para el metodo sumarCostoVentas
	public List<Object[]> buscarProdGrupPlanCuen(Egreso egreso, String tipoTran) throws Exception {

		StringBuilder sentencia = new StringBuilder();
		sentencia.append(sentenciaSelectVentas());
		sentencia.append(sentenciaJoinVentas());
		sentencia.append(sentenciaJoinVentasProducto());
		sentencia.append("where e.egreso_id = ?1 ");
		sentencia.append("and pgpc.documento_id = ?2 ");
		sentencia.append("and pgpc.tipo_tran = ?3 ");
		sentencia.append("group by e.documento_id, pgpc.plan_cuen_id");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, egreso.getEgresoId());
		query.setParameter(2, egreso.getDocuEgre().getDocumentoId());
		query.setParameter(3, tipoTran);

		return query.getResultList();
	}

	public List<Object[]> buscarProdGrupPlanCuenCosto(Egreso egreso, String tipoTran) throws Exception {
// 		Se utiliza este ultimo parametro modulo_inve para determinar que calcule
// 		solamente el costo de los productos que pertenecen a un grupo de inventario 
//		ya que puede existir productos de gastos o servicios que se registren
// 		en el modulo de compras y en el modulo de ventas

		StringBuilder sentencia = new StringBuilder();
		sentencia.append(sentenciaSelectVentas());
		sentencia.append(sentenciaJoinVentas());
		sentencia.append(sentenciaJoinVentasProducto());
		sentencia.append("where e.egreso_id = ?1 ");
		sentencia.append("and pgpc.documento_id = ?2 ");
		sentencia.append("and pgpc.tipo_tran = ?3 ");
		sentencia.append("and pg.modulo_inve = ?4 ");
		sentencia.append("group by e.documento_id, pgpc.plan_cuen_id");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, egreso.getEgresoId());
		query.setParameter(2, egreso.getDocuEgre().getDocumentoId());
		query.setParameter(3, tipoTran);
		query.setParameter(4, true);

		return query.getResultList();
	}
	
	public List<Object[]> buscarProdGrupPlanCuenCostoSubProducto(Egreso egreso, String tipoTran) throws Exception {

		StringBuilder sentencia = new StringBuilder();
		sentencia.append("select plan_cuen_id as plan_cuen_id, ");
		sentencia.append("sum(ed.cantid * ed.costo) as egre_costo ");
		sentencia.append("from egre_deta ed ");
		sentencia.append("inner join egreso e on e.egreso_id = ed.egreso_id ");
		sentencia.append("inner join producto pr on pr.producto_id = ed.producto_id ");
		sentencia.append("inner join prod_grup pg on pg.prod_grup_id = pr.prod_grup_id ");
		sentencia.append("inner join prod_grup_plan_cuen pgpc on pgpc.prod_grup_id = pg.prod_grup_id ");
		sentencia.append("where e.egreso_id = ?1 ");
		sentencia.append("and pgpc.documento_id = ?2 ");
		sentencia.append("and pgpc.tipo_tran = ?3 ");
//		Verifica que el producto controle inventario
		sentencia.append("and pg.modulo_inve = ?4 ");
//		verifica que sea un subproducto
		sentencia.append("and ed.egre_deta_supe_id is not null ");
		sentencia.append("group by e.documento_id, pgpc.plan_cuen_id");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, egreso.getEgresoId());
		query.setParameter(2, egreso.getDocuEgre().getDocumentoId());
		query.setParameter(3, tipoTran);
		query.setParameter(4, true);

		return query.getResultList();
	}

	public List<Object[]> buscarCobroCxcsNcs(FormPagoMoviEgre fpme) throws Exception {

		StringBuilder sentencia = new StringBuilder();
		sentencia.append("select fppc.plan_cuen_id as plan_cuen_id, ");
		sentencia.append("fpmeFp.total_reci as total ");
		sentencia.append("from form_pago_movi_egre fpme ");
		sentencia.append("inner join egreso e on e.egreso_id = fpme.egreso_id ");
		sentencia.append("inner join documento d on d.documento_id = e.documento_id ");
		sentencia.append(sentenciaJoinFormPago());
		sentencia.append("where fpme.fpme_id = ?1 ");
		sentencia.append("and fpme.egreso_id is not null ");

		sentencia.append("union all ");

		sentencia.append("select cgpc.plan_cuen_id as plan_cuen_id, ");
		sentencia.append("cd.total as cobro_deta_total ");
		sentencia.append(sentenciaJoinCobrDeta());
		sentencia.append(sentenciaJoinPersClie());
		sentencia.append("where fpme.fpme_id = ?1 ");
		sentencia.append("and cgpc.tipo_tran = ?2 ");
		sentencia.append("and fp.tipo = ?3 ");
		sentencia.append("and cgpc.documento_id = ?4 ");
		sentencia.append("order by plan_cuen_id");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, fpme.getFpmeId());
		query.setParameter(2, "CXP-CXC");
		query.setParameter(3, "NC");
		query.setParameter(4, fpme.getDocuMoviEgre().getDocumentoId());

		return query.getResultList();

	}

	public StringBuilder sentenciaSelectVentas() {

		StringBuilder sentencia = new StringBuilder("select plan_cuen_id as plan_cuen_id, ");
		sentencia.append(
				"sum((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100) - ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) as egre_total_neto, ");
		sentencia.append("sum(ed.cantid * ed.precio_vent) as egre_total_bruto, ");
		sentencia.append(
				"sum((ed.cantid * ed.precio_vent * ed.descue / 100) + ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) as egre_total_descue, ");
		sentencia.append("sum( ");
		sentencia.append("( ");
		sentencia.append(
				"((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100) - ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) - ");
		sentencia.append(
				"(((((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100) - ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) * (edi.porcen / 100)) ) / COALESCE((nullif((porcen / 100),0)),1)) ");
		sentencia.append(") ");
		sentencia.append(") as egre_sin_iva, ");
		sentencia.append(
				"sum(((((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100) - ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) * (edi.porcen / 100))) / COALESCE((nullif((porcen / 100),0)),1)) as egre_con_iva, ");
		sentencia.append(
				"sum(((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100) - ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) * (edi.porcen / 100) + ");
		sentencia.append(
				"(case when edi.tipo = 'ICE' then edi.porcen * ed.cantid else 0 end * edi.porcen / 100) ");
		sentencia.append(") as egre_iva, ");
		sentencia.append(
				"sum(((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100) - ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) + ");
		sentencia.append(
				"(((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100) - ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) * (edi.porcen / 100)) + ");
		sentencia.append("(case when edi.tipo = 'ICE' then edi.porcen * ed.cantid else 0 end) + ");
		sentencia.append(
				"(case when edi.tipo = 'ICE' then edi.porcen * ed.cantid else 0 end * edi.porcen / 100) ");
		sentencia.append(") as egre_total, ");
		sentencia.append("sum(case when edi.tipo = 'ICE' then edi.porcen * ed.cantid else 0 end) as egre_ice, ");
		sentencia.append("sum(ed.cantid * ed.costo) as egre_costo ");
		sentencia.append("from egre_deta_impu edi ");

		return sentencia;
	}

	public StringBuilder sentenciaJoinVentas() {
		StringBuilder sentencia = new StringBuilder("inner join egre_deta ed on ed.egre_deta_id = edi.egre_deta_id ");
		sentencia.append("inner join egreso e on e.egreso_id = ed.egreso_id ");

		return sentencia;
	}

	public StringBuilder sentenciaJoinVentasProducto() {
		StringBuilder sentencia = new StringBuilder("inner join producto pr on pr.producto_id = ed.producto_id ");
		sentencia.append("inner join prod_grup pg on pg.prod_grup_id = pr.prod_grup_id ");
		sentencia.append(
				"inner join prod_grup_plan_cuen pgpc on pgpc.prod_grup_id = pg.prod_grup_id ");
//		TODO: la de abajo es la original revisar y cambiar
//		sentencia.append(
//				"inner join prod_grup_plan_cuen pgpc on pgpc.prod_grup_id = pg.prod_grup_id and pgpc.documento_id = e.documento_id ");

		return sentencia;
	}

	public StringBuilder sentenciaJoinCobrDeta() {
		StringBuilder sentencia = new StringBuilder("from cobr_deta cd ");
		sentencia.append("inner join cxc cxc on cxc.cxc_id = cd.cxc_id ");
		sentencia.append("inner join egreso e on e.egreso_id = cd.egreso_id ");
		sentencia.append("inner join fpme_form_pago fpmeFp on fpmeFp.fpme_form_pago_id = cd.fpme_form_pago_id ");
		sentencia.append("inner join form_pago fp on fp.form_pago_id = fpmeFp.form_pago_id ");
		sentencia.append("inner join form_pago_movi_egre fpme on fpme.fpme_id = fpmeFp.fpme_id ");

		return sentencia;
	}

	public StringBuilder sentenciaJoinPersClie() {
		StringBuilder sentencia = new StringBuilder("inner join pers_clie pc on pc.persona_id = e.persona_id ");
		sentencia.append("inner join clie_grup cg on cg.clie_grup_id = pc.clie_grup_id ");
		sentencia.append(
				"inner join clie_grup_plan_cuen cgpc on cgpc.clie_grup_id = cg.clie_grup_id ");
//		TODO: lo mismo que el otro todo
//		sentencia.append(
//				"inner join clie_grup_plan_cuen cgpc on cgpc.clie_grup_id = cg.clie_grup_id and cgpc.documento_id = e.documento_id ");

		return sentencia;
	}

	public StringBuilder sentenciaJoinFormPago() {
		StringBuilder sentencia = new StringBuilder(
				"inner join fpme_form_pago fpmeFp on fpmeFp.fpme_id = fpme.fpme_id ");
		sentencia.append("inner join form_pago fp on fp.form_pago_id = fpmeFp.form_pago_id ");
		sentencia.append("inner join form_pago_plan_cuen fppc on fppc.form_pago_id = fp.form_pago_id ");

		return sentencia;
	}	
}
