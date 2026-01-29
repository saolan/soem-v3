package ec.com.tecnointel.soem.contabilidad.registroImp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.contabilidad.modelo.TranPlantilla;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionFpmeInt;
import ec.com.tecnointel.soem.parametro.listaInt.DocuMoviIngrListaInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuMoviIngr;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviEgre;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import ec.com.tecnointel.soem.tesoreria.modelo.FpmeFormPago;
import ec.com.tecnointel.soem.tesoreria.registroInt.FormPagoMoviIngrRegisInt;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.Query;

@Stateless
public class TransaccionFpmeImp extends TransaccionGestionImp implements TransaccionFpmeInt {

	private static final long serialVersionUID = -2264063143210849543L;

	@Inject
	DocuMoviIngrListaInt docuMoviIngrLista;

	@Inject
	FormPagoMoviIngrRegisInt formPagoMoviIngrRegis;

	@Override
	public Integer contabilizarCobro(FormPagoMoviEgre fpme) throws Exception {
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
		Documento docuTran = buscarDocumentoPorId(fpme.getDocuMoviEgre().getDocumento());

//		Buscar pago de documentos (Ventas) con forma de pago (FP) y anticipos (AN)
		totalDebe = buscarFormPagoPlancuen(fpme);
		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalDebe, docuTran, fpme, 0, 1, orden, new BigDecimal(1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

//		Buscar cuenta de clientes
		totalHaber = buscarClieGrupPlanCuen(fpme);
		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalHaber, docuTran, fpme, 0, 1, orden, new BigDecimal(-1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		if (!tranPlantillas.isEmpty()) {
			transaccionId = gestionarTranPlantilla(tranPlantillas);
		}

		return transaccionId;
	}

	@Override
	public Integer contabilizarAnticipo(FormPagoMoviEgre fpme) throws Exception {

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
		Documento docuTran = buscarDocumentoPorId(fpme.getDocuMoviEgre().getDocumento());

		totalDebe = buscarformPagoPlanCuenAnticipos(fpme);
		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalDebe, docuTran, fpme, 0, 1, orden, new BigDecimal(1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		totalHaber = buscarClieGrupPlanCuenAnticipos(fpme);
		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = crearTranPlantilla(totalHaber, docuTran, fpme, 0, 1, orden, new BigDecimal(-1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		transaccionId = gestionarTranPlantilla(tranPlantillas);

		return transaccionId;
	}

	@Override
	public FormPagoMoviIngr crearDeposito(FpmeFormPago fpmeFp) throws Exception {

		FormPagoMoviIngr fpmi = new FormPagoMoviIngr();

		List<DocuMoviIngr> docuMoviIngrs = new ArrayList<>();

		docuMoviIngrs = buscarDocuMoviIngrs();

		DocuMoviIngr docuMoviIngr = new DocuMoviIngr();
		Persona persona = new Persona();

		persona.setPersonaId(1); // SuperAdministrador
		docuMoviIngr = seleccionarDocuMoviIngr(docuMoviIngrs, fpmeFp.getFormPagoMoviEgre().getDocuMoviEgre().getTipo());

		fpmi.setTranPlanId(2);

		fpmi.setSucursal(fpmeFp.getFormPagoMoviEgre().getSucursal());
		fpmi.setDocuMoviIngr(docuMoviIngr);
//		Grabar la misma transaccion que en el cobro
//		para poder tener una referencia y poder anular
//		los dos movimientos en el caso que se anule el egreso
		fpmi.setTransaccion(fpmeFp.getFormPagoMoviEgre().getTransaccion());
		fpmi.setFormPago(fpmeFp.getFormPago());
		fpmi.setPersona(persona);
		fpmi.setFecha(fpmeFp.getFormPagoMoviEgre().getFecha());
		fpmi.setFechaHora(fpmeFp.getFormPagoMoviEgre().getFechaHora());
		fpmi.setFechaHoraRegi(fpmeFp.getFormPagoMoviEgre().getFechaHoraRegi());
		fpmi.setRefere(fpmeFp.getFormPagoMoviEgre().getRefere());
		fpmi.setNota(fpmeFp.getFormPagoMoviEgre().getNota());
		fpmi.setTotal(fpmeFp.getFormPagoMoviEgre().getTotal());
		fpmi.setEstado("PR");

		return fpmi;
	}

	@Override
	public void insertarFpmiDepositos(List<FormPagoMoviIngr> fpmis) throws Exception {

		for (FormPagoMoviIngr formPagoMoviIngr : fpmis) {

			// Grabar el número de documento
			formPagoMoviIngr.setNumero(formPagoMoviIngr.getDocuMoviIngr().getDocumento().getNumero() + 1);

			// Actualizar secuencial
			formPagoMoviIngr.getDocuMoviIngr().getDocumento().setNumero(formPagoMoviIngr.getNumero());
			documentoRegis.modificar((formPagoMoviIngr.getDocuMoviIngr().getDocumento()));
			// Fin Actualizar secuencial

			formPagoMoviIngrRegis.insertar(formPagoMoviIngr);
		}
	}

	public List<DocuMoviIngr> buscarDocuMoviIngrs() throws Exception {

		Documento documento = new Documento();
		documento.setEstado(true);

		DocuMoviIngr docuMoviIngr = new DocuMoviIngr();
		docuMoviIngr.setDocumento(documento);

		List<DocuMoviIngr> docuMoviIngrs = new ArrayList<>();

		docuMoviIngrs = docuMoviIngrLista.buscar(docuMoviIngr, null);

		return docuMoviIngrs;
	}

	public DocuMoviIngr seleccionarDocuMoviIngr(List<DocuMoviIngr> docuMoviIngrs, String tipo) {

		DocuMoviIngr docuMoviIngrSeleccion = new DocuMoviIngr();

		for (DocuMoviIngr docuMoviIngr : docuMoviIngrs) {

			if (docuMoviIngr.getTipo().equals(tipo)) {
				docuMoviIngrSeleccion = docuMoviIngr;
				break;
			}
		}
		return docuMoviIngrSeleccion;
	}

//	Suma los documentos en fpme de tipo anticipo para contabilizar el debe en la transaccion 
	public List<Object[]> buscarformPagoPlanCuenAnticipos(FormPagoMoviEgre fpme) throws Exception {

		StringBuilder sentencia = new StringBuilder();
		sentencia.append("select fppc.plan_cuen_id as plan_cuen_id, ");
		sentencia.append("fpmeFp.total_reci as total ");
		sentencia.append("from form_pago_movi_egre fpme ");
		sentencia.append("inner join docu_movi_egre dme on dme.documento_id = fpme.documento_id ");
		sentencia.append(sentenciaJoinFormPago());
		sentencia.append("where fpme.fpme_id = ?1 and dme.tipo = ?2 and fpme.egreso_id is null ");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, fpme.getFpmeId());
		query.setParameter(2, "ANTICIPO");

		return query.getResultList();
	}

	public List<Object[]> buscarClieGrupPlanCuenAnticipos(FormPagoMoviEgre fpme) throws Exception {

		StringBuilder sentencia = new StringBuilder();
		sentencia.append("select cgpc.plan_cuen_id as plan_cuen_id, ");
		sentencia.append("sum(fpme.total) as total ");
		sentencia.append("from form_pago_movi_egre fpme ");
		sentencia.append("inner join docu_movi_egre dme on dme.documento_id = fpme.documento_id ");
		sentencia.append("inner join persona p on p.persona_id = fpme.persona_id ");
		sentencia.append("inner join pers_clie pc on pc.persona_id = p.persona_id ");
		sentencia.append("inner join clie_grup cg on cg.clie_grup_id = pc.clie_grup_id ");
		sentencia.append(
				"inner join clie_grup_plan_cuen cgpc on cgpc.clie_grup_id = cg.clie_grup_id and cgpc.documento_id = dme.documento_id ");
		sentencia.append("inner join plan_cuen pcuen on pcuen.plan_cuen_id = cgpc.plan_cuen_id ");
		sentencia.append(
				"where fpme.fpme_id = ?1 and dme.tipo = ?2 and cgpc.tipo_tran = ?2 and fpme.egreso_id is null ");
		sentencia.append("group by cgpc.plan_cuen_id");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, fpme.getFpmeId());
		query.setParameter(2, "ANTICIPO");

		return query.getResultList();
	}

//  Lista para contabilizar el debe en una transaccion de cobros
//	La primera consulta se hace para obtener los cobros de acuerdo a la cuenta contable de la forma de pago
//	se une con la segunda que se hace para obtener los cobros realizados por anticipos y en este caso se debe 
//	usar la cuenta contable de anticipo que esta en el proveedor
	public List<Object[]> buscarFormPagoPlancuen(FormPagoMoviEgre fpme) throws Exception {

		StringBuilder sentencia = new StringBuilder();
		sentencia.append("select fppc.plan_cuen_id as plan_cuen_id, ");
		sentencia.append("cd.total as cobro_deta_total ");
		sentencia.append(sentenciaJoinCobrDeta());
		sentencia.append("inner join form_pago_plan_cuen fppc on fppc.form_pago_id = fp.form_pago_id ");
		sentencia.append("where fpme.fpme_id = ?1 ");

		sentencia.append("union all ");

		sentencia.append("select cgpc.plan_cuen_id as plan_cuen_id, ");
		sentencia.append("cd.total as cobro_deta_total ");
		sentencia.append(sentenciaJoinCobrDeta());
		sentencia.append(sentenciaJoinPersClie());
		sentencia.append("where fpme.fpme_id = ?1 ");
		sentencia.append("and cgpc.documento_id = ?2 ");
		sentencia.append("and cgpc.tipo_tran = ?3 ");
		sentencia.append("and fp.tipo = ?4 ");
		sentencia.append("order by plan_cuen_id");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, fpme.getFpmeId());
		query.setParameter(2, fpme.getDocuMoviEgre().getDocumentoId());
		query.setParameter(3, "ANTICIPO");
		query.setParameter(4, "AN");

		return query.getResultList();
	}

//	Lista para contabilizar el haber en una transaccion de cobros	
//	Esta consulta es igual a sumarCobroCxcsNcs con la diferencia que el join se hace a e.egreso_id
//	y se tiene un parametro mas
	public List<Object[]> buscarClieGrupPlanCuen(FormPagoMoviEgre fpme) throws Exception {

		StringBuilder sentencia = new StringBuilder();
		sentencia.append("select cgpc.plan_cuen_id as plan_cuen_id, ");
		sentencia.append("sum(cd.total) as cobro_deta_total ");
		sentencia.append(sentenciaJoinCobrDeta());
		sentencia.append(sentenciaJoinPersClie());
		sentencia.append("where fpme.fpme_id = ?1 ");
		sentencia.append("and cgpc.documento_id = ?2 ");
		sentencia.append("and cgpc.tipo_tran = ?3 ");
		sentencia.append("and fp.tipo <> ?4 ");
		sentencia.append("group by cgpc.plan_cuen_id ");
		sentencia.append("order by cgpc.plan_cuen_id");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, fpme.getFpmeId());
		query.setParameter(2, fpme.getDocuMoviEgre().getDocumentoId());
		query.setParameter(3, "CXP-CXC");
		query.setParameter(4, "NC");

		return query.getResultList();
	}

	public StringBuilder sentenciaJoinCobrDeta() {
		StringBuilder sentencia = new StringBuilder("from cobr_deta cd ");
		sentencia.append("inner join fpme_form_pago fpmeFp on fpmeFp.fpme_form_pago_id = cd.fpme_form_pago_id ");
		sentencia.append("inner join form_pago fp on fp.form_pago_id = fpmeFp.form_pago_id ");
		sentencia.append("inner join form_pago_movi_egre fpme on fpme.fpme_id = fpmeFp.fpme_id ");

		return sentencia;
	}

	public StringBuilder sentenciaJoinPersClie() {
		StringBuilder sentencia = new StringBuilder("inner join pers_clie pc on pc.persona_id = fpme.persona_id ");
		sentencia.append("inner join clie_grup cg on cg.clie_grup_id = pc.clie_grup_id ");
		sentencia.append("inner join clie_grup_plan_cuen cgpc on cgpc.clie_grup_id = cg.clie_grup_id ");
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
