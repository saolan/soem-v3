package ec.com.tecnointel.soem.contabilidad.registroImp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.contabilidad.modelo.TranPlantilla;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionFpmiInt;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.TranPlanDeta;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

@Stateless
public class TransaccionFpmiImp extends TransaccionGestionImp implements TransaccionFpmiInt {

	private static final long serialVersionUID = -1280612346012301157L;

	@Override
	public Integer contabilizarPago(FormPagoMoviIngr fpmi) throws Exception {
//		Se utiliza para dar un orden en tranDetas para luego ordenar por este campo
		Integer orden = 100;
		Integer transaccionId = 0;

//		Estos objetos se usan para almacenar plan_cuen_id y
//		el valor que va ir en cada fila de tran_deta
		List<Object[]> totalDebe = new ArrayList<Object[]>();
		List<Object[]> totalHaber = new ArrayList<Object[]>();

		List<TranPlantilla> tranPlantillas = new ArrayList<>();
		List<TranPlantilla> tranPlantillaTmps = new ArrayList<>();

//		Buscar para transaccion (Comprobante Ingreso, egreso , diario)
		Documento docuTran = buscarDocumentoPorId(fpmi.getDocuMoviIngr().getDocumento());

//		Buscar cuenta de proveedores
		totalDebe = buscarProvGrupPlanCuen(fpmi, "CXP-CXC");
		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalDebe, docuTran, fpmi, 0, 1, orden, new BigDecimal(1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

//		Buscar pago de documentos (Compras) con forma de pago
		totalHaber = buscarFormPagoPlancuen(fpmi);
		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalHaber, docuTran, fpmi, 0, 1, orden, new BigDecimal(-1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

//		Busca pago del documento con anticipo es decir se pago en documento con anticipo
//		No es lo mismo que contabilizar un documento anticipo que usa el metodo contabilizarAnticipo
		if (totalHaber.isEmpty()) {
			totalHaber = buscarProvGrupPlanCuen(fpmi, "ANTICIPO");
			orden = 100 + tranPlantillas.size();
			tranPlantillaTmps = crearTranPlantilla(totalHaber, docuTran, fpmi, 0, 1, orden, new BigDecimal(-1), 1);
			tranPlantillas.addAll(tranPlantillaTmps);
		}

		transaccionId = gestionarTranPlantilla(tranPlantillas);

		return transaccionId;
	}

	@Override
//	Pago de Gastos
	public Integer contabilizarPago(FormPagoMoviIngr fpmi, List<TranPlanDeta> tranPlanDetas) throws Exception {
//		Se utiliza para dar un orden en tranDetas para luego ordenar por este campo
		Integer orden = 100;
		Integer transaccionId = 0;

//		Estos objetos se usan para almacenar plan_cuen_id y
//		el valor que va ir en cada fila de tran_deta
		List<Object[]> totalDebe = new ArrayList<Object[]>();
		List<Object[]> totalHaber = new ArrayList<Object[]>();

		List<TranPlantilla> tranPlantillas = new ArrayList<>();
		List<TranPlantilla> tranPlantillaTmps = new ArrayList<>();

//		Buscar para transaccion (Comprobante Ingreso, egreso , diario)
		Documento docuTran = buscarDocumentoPorId(fpmi.getDocuMoviIngr().getDocumento());

		totalDebe = buscarTranPlan(fpmi, tranPlanDetas.get(0).getPlanCuenId());
		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalDebe, docuTran, fpmi, 0, 1, orden, new BigDecimal(1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		totalHaber = buscarFormPagoPlancuen(fpmi);
		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalHaber, docuTran, fpmi, 0, 1, orden, new BigDecimal(-1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		transaccionId = gestionarTranPlantilla(tranPlantillas);

		return transaccionId;
	}

	// Contabiliza los documentos que se utilizan para dar pagos por adelentado al
	// proveedor
	@Override
	public Integer contabilizarAnticipo(FormPagoMoviIngr fpmi) throws Exception {

//		Se utiliza para dar un orden en tranDetas para luego ordenar por este campo
		Integer orden = 100;
		Integer transaccionId = 0;

//		Estos objetos se usan para almacenar plan_cuen_id y
//		el valor que va ir en cada fila de tran_deta
		List<Object[]> totalDebe = new ArrayList<Object[]>();
		List<Object[]> totalHaber = new ArrayList<Object[]>();

		List<TranPlantilla> tranPlantillas = new ArrayList<>();
		List<TranPlantilla> tranPlantillaTmps = new ArrayList<>();

//		Buscar para transaccion (Comprobante Ingreso, egreso , diario)
		Documento docuTran = buscarDocumentoPorId(fpmi.getDocuMoviIngr().getDocumento());

		totalDebe = buscarProvGrupPlanCuen(fpmi, "ANTICIPO");
		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalDebe, docuTran, fpmi, 0, 1, orden, new BigDecimal(1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		totalHaber = buscarFormPagoPlancuen(fpmi);
		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalHaber, docuTran, fpmi, 0, 1, orden, new BigDecimal(-1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		transaccionId = gestionarTranPlantilla(tranPlantillas);

		return transaccionId;

	}

	@Override
	public Integer contabilizarDeposito(FormPagoMoviIngr fpmi) throws Exception {
//		Se utiliza para dar un orden en tranDetas para luego ordenar por este campo
		Integer orden = 100;
		Integer transaccionId = 0;

//		Estos objetos se usan para almacenar plan_cuen_id y
//		el valor que va ir en cada fila de tran_deta
		List<Object[]> totalDebe = new ArrayList<Object[]>();
		List<Object[]> totalHaber = new ArrayList<Object[]>();

		List<TranPlantilla> tranPlantillas = new ArrayList<>();
		List<TranPlantilla> tranPlantillaTmps = new ArrayList<>();

//		Buscar para transaccion (Comprobante Ingreso, egreso , diario)
		Documento docuTran = buscarDocumentoPorId(fpmi.getDocuMoviIngr().getDocumento());

		totalDebe = buscarFormPagoPlancuen(fpmi);
		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalDebe, docuTran, fpmi, 0, 1, orden, new BigDecimal(1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		totalHaber = buscarFormPagoPlancuenDeposito(fpmi);
		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalHaber, docuTran, fpmi, 0, 1, orden, new BigDecimal(-1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		transaccionId = gestionarTranPlantilla(tranPlantillas);

		return transaccionId;
	}

	public List<Object[]> buscarProvGrupPlanCuen(FormPagoMoviIngr fpmi, String tipoTran) throws Exception {

		StringBuilder sentencia = new StringBuilder();

		sentencia.append("select pgpc.plan_cuen_id as plan_cuen_id, ");
		sentencia.append("coalesce(pd.total, fpmi.total) as total ");
		sentencia.append("from form_pago_movi_ingr fpmi ");
		sentencia.append("left join pago_deta pd on pd.fpmi_id = fpmi.fpmi_id ");
		sentencia.append("inner join pers_prov pp on pp.persona_id = fpmi.persona_id ");
		sentencia.append("inner join prov_grup pg on pg.prov_grup_id = pp.prov_grup_id ");
		sentencia.append("inner join prov_grup_plan_cuen pgpc on pgpc.prov_grup_id = pg.prov_grup_id ");
		sentencia.append("where fpmi.fpmi_id = ?1 ");
		sentencia.append("and pgpc.documento_id = ?2 ");
		sentencia.append("and pgpc.tipo_tran = ?3 ");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, fpmi.getFpmiId());
		query.setParameter(2, fpmi.getDocuMoviIngr().getDocumentoId());
		query.setParameter(3, tipoTran);

		return query.getResultList();
	}

//	Busca la cuenta del banco que se esta seleccionando para hacer el deposito
	public List<Object[]> buscarFormPagoPlancuen(FormPagoMoviIngr fpmi) throws Exception {

		StringBuilder sentencia = new StringBuilder();

		sentencia.append("select fppc.plan_cuen_id as plan_cuen_id, ");
		sentencia.append("fpmi.total as total ");
		sentencia.append("from form_pago_movi_ingr fpmi ");
		sentencia.append("inner join form_pago fp on fp.form_pago_id = fpmi.form_pago_id ");
		sentencia.append("inner join form_pago_plan_cuen fppc on fppc.form_pago_id = fp.form_pago_id ");
		sentencia.append("where fpmi.fpmi_id = ?1 ");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, fpmi.getFpmiId());

		return query.getResultList();
	}

//	Buscar en form_pago_plan_cuen el registro que este colocado DEPOSITO
//	ya que esta sera la cuenta de caja que va a afectar al momento de realizar al deposito
	public List<Object[]> buscarFormPagoPlancuenDeposito(FormPagoMoviIngr fpmi) throws Exception {

		StringBuilder sentencia = new StringBuilder();

		sentencia.append(
				"select (select plan_cuen_id from form_pago_plan_cuen where tipo_tran = 'DEPOSITO') as plan_cuen_id, ");
		sentencia.append("fpmi.total as total ");
		sentencia.append("from form_pago_movi_ingr fpmi ");
		sentencia.append("where fpmi.fpmi_id = ?1 ");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, fpmi.getFpmiId());

		return query.getResultList();
	}

	private List<Object[]> buscarTranPlan(FormPagoMoviIngr fpmi, Integer planCuenId) {
		StringBuilder sentencia = new StringBuilder();

		sentencia.append("select ");
		sentencia.append(planCuenId);
		sentencia.append(" ");
		sentencia.append("as plan_cuen_id, ");
		sentencia.append("fpmi.total as total ");
		sentencia.append("from form_pago_movi_ingr fpmi ");
		sentencia.append("where fpmi.fpmi_id = ?1 ");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, fpmi.getFpmiId());

		return query.getResultList();
	}
}