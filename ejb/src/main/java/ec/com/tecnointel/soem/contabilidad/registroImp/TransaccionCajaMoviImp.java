package ec.com.tecnointel.soem.contabilidad.registroImp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.caja.modelo.CajaMovi;
import ec.com.tecnointel.soem.contabilidad.modelo.TranPlantilla;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionCajaMoviInt;
import ec.com.tecnointel.soem.parametro.modelo.DocuEgre;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;

@Stateless
public class TransaccionCajaMoviImp extends TransaccionGestionImp implements TransaccionCajaMoviInt {

	private static final long serialVersionUID = 449911408507285909L;

	@Override
	public Integer contabilizarVentas(CajaMovi cajaMoviInicio, CajaMovi cajaMoviCierre, List<DocuEgre> docuEgres, Documento docuTran)
			throws Exception {

		Integer orden = 100;

		List<Object[]> totalDebe = new ArrayList<Object[]>();
		List<Object[]> totalHaber = new ArrayList<Object[]>();

		List<TranPlantilla> tranPlantillas = new ArrayList<>();
		List<TranPlantilla> tranPlantillaTmps = new ArrayList<>();

		for (DocuEgre docuEgreContabilizar : docuEgres) {
//				Determina si tiene permiso para contabilizar y que sean facturas (factor = -1) 
			if (docuEgreContabilizar.getDocumento().isContab()
					&& docuEgreContabilizar.getDocumento().getFactor() == -1) {

//				Contabiliza el debe en transaccion de Ventas
//				Suma las Ventas de las facturas que se hicieron en el inicio de caja
				totalDebe = sumarCuentasCxcs(cajaMoviInicio.getCajaMoviId(), docuEgreContabilizar, "CXP-CXC");
				orden = 100 + tranPlantillas.size();
				tranPlantillaTmps = this.crearTranPlantilla(totalDebe, docuTran, cajaMoviCierre, 0, 7, orden,
						new BigDecimal(1), 1);
				tranPlantillas.addAll(tranPlantillaTmps);

				totalHaber = sumarVentas(cajaMoviInicio.getCajaMoviId(), docuEgreContabilizar, "VENTA SIN IMPU");
				orden = 100 + tranPlantillas.size();
				tranPlantillaTmps = this.crearTranPlantilla(totalHaber, docuTran, cajaMoviCierre, 0, 4, orden,
						new BigDecimal(-1), 1);
				tranPlantillas.addAll(tranPlantillaTmps);

				totalHaber = sumarVentas(cajaMoviInicio.getCajaMoviId(), docuEgreContabilizar, "VENTA CON IMPU");
				orden = 100 + tranPlantillas.size();
				tranPlantillaTmps = this.crearTranPlantilla(totalHaber, docuTran, cajaMoviCierre, 0, 5, orden,
						new BigDecimal(-1), 1);
				tranPlantillas.addAll(tranPlantillaTmps);

				totalHaber = sumarVentas(cajaMoviInicio.getCajaMoviId(), docuEgreContabilizar, "VENTA IMPU");
				orden = 100 + tranPlantillas.size();
				tranPlantillaTmps = this.crearTranPlantilla(totalHaber, docuTran, cajaMoviCierre, 0, 6, orden,
						new BigDecimal(-1), 1);
				tranPlantillas.addAll(tranPlantillaTmps);

//			totalHaber = cajaMoviLista.sumarVentaIvas(cajaMovi.getCajaMoviId(), docuEgreContabilizar, "ICE");
//			orden = 100 + tranPlantillas.size();
//			tranPlantillaTmps = this.crearTranPlantilla(totalHaber, documento, this.cajaMovi, 0, 6, orden,
//					new BigDecimal(-1), 1);
//			tranPlantillas.addAll(tranPlantillaTmps);

			}
		}

//			Si no encuentra datos en (ventas) no contabiliza nada
		if (tranPlantillas.isEmpty()) {
			return 0;
		}

		return gestionarTranPlantilla(tranPlantillas);

	}

	@Override
	public Integer contabilizarCostoVentas(CajaMovi cajaMoviInicio, CajaMovi cajaMoviCierre, List<DocuEgre> docuEgres, Documento docuTran)
			throws Exception {

		Integer orden = 100;

		List<Object[]> totalDebe = new ArrayList<Object[]>();
		List<Object[]> totalHaber = new ArrayList<Object[]>();

		List<TranPlantilla> tranPlantillas = new ArrayList<>();
		List<TranPlantilla> tranPlantillaTmps = new ArrayList<>();

		for (DocuEgre docuEgreContabilizar : docuEgres) {
//			Determina si tiene permiso para contabilizar y que sean facturas (factor = -1) 
			if (docuEgreContabilizar.getDocumento().isContab()
					&& docuEgreContabilizar.getDocumento().getFactor() == -1) {

				totalDebe.addAll(sumarCostoVentas(cajaMoviInicio.getCajaMoviId(), docuEgreContabilizar, "COSTO"));

				totalHaber.addAll(sumarCostoVentas(cajaMoviInicio.getCajaMoviId(), docuEgreContabilizar, "COMPRA"));
				
				totalDebe.addAll(sumarCostoVentasSubProductos(cajaMoviInicio.getCajaMoviId(), docuEgreContabilizar, "COSTO"));

				totalHaber.addAll(sumarCostoVentasSubProductos(cajaMoviInicio.getCajaMoviId(), docuEgreContabilizar, "COMPRA"));
			}
		}

		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = this.crearTranPlantilla(totalDebe, docuTran, cajaMoviCierre, 0, 9, orden, new BigDecimal(1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = this.crearTranPlantilla(totalHaber, docuTran, cajaMoviCierre, 0, 9, orden, new BigDecimal(-1),
				1);
		tranPlantillas.addAll(tranPlantillaTmps);

//		Si no encuentra datos en costoVentas no contabiliza nada
		if (tranPlantillas.isEmpty()) {
			return 0;
		}

		return gestionarTranPlantilla(tranPlantillas);

	}

	public Integer contabilizarCostoVentasNC(CajaMovi cajaMoviInicio, CajaMovi cajaMoviCierre, List<DocuEgre> docuEgres, Documento docuTran)
			throws Exception {

		Integer orden = 100;

		List<Object[]> totalDebe = new ArrayList<Object[]>();
		List<Object[]> totalHaber = new ArrayList<Object[]>();

		List<TranPlantilla> tranPlantillas = new ArrayList<>();
		List<TranPlantilla> tranPlantillaTmps = new ArrayList<>();

		for (DocuEgre docuEgreContabilizar : docuEgres) {
//			Determina si tiene permiso para contabilizar y que sean notas de credito (factor = 1) 
			if (docuEgreContabilizar.getDocumento().isContab()
					&& docuEgreContabilizar.getDocumento().getFactor() == 1) {

				totalDebe.addAll(sumarCostoVentas(cajaMoviInicio.getCajaMoviId(), docuEgreContabilizar, "COMPRA"));

				totalHaber.addAll(sumarCostoVentas(cajaMoviInicio.getCajaMoviId(), docuEgreContabilizar, "COSTO"));

				totalDebe.addAll(sumarCostoVentasSubProductos(cajaMoviInicio.getCajaMoviId(), docuEgreContabilizar, "COMPRA"));

				totalHaber.addAll(sumarCostoVentasSubProductos(cajaMoviInicio.getCajaMoviId(), docuEgreContabilizar, "COSTO"));

			}
		}

		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = this.crearTranPlantilla(totalDebe, docuTran, cajaMoviCierre, 0, 9, orden, new BigDecimal(-1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = this.crearTranPlantilla(totalHaber, docuTran, cajaMoviCierre, 0, 9, orden, new BigDecimal(1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

//		Si no encuentra datos en costoVentas no contabiliza nada
		if (tranPlantillas.isEmpty()) {
			return 0;
		}

		return gestionarTranPlantilla(tranPlantillas);
	}

	public Integer contabilizarCobros(CajaMovi cajaMoviInicio, CajaMovi cajaMoviCierre, List<DocuEgre> docuEgres, Documento docuTran)
			throws Exception {

		Integer orden = 100;

		List<Object[]> totalDebe = new ArrayList<Object[]>();
		List<Object[]> totalHaber = new ArrayList<Object[]>();

//		List<Object[]> cobroFps;
//		List<Object[]> cobroCxcs;

		List<TranPlantilla> tranPlantillas = new ArrayList<>();
		List<TranPlantilla> tranPlantillaTmps = new ArrayList<>();
		// tranPlantillas.clear();

		for (DocuEgre docuEgreContabilizar : docuEgres) {
//				Determina si tiene permiso para contabilizar y que sean facturas (factor = -1) 
			if (docuEgreContabilizar.getDocumento().isContab()
					&& docuEgreContabilizar.getDocumento().getFactor() == -1) {

				// Suma los cobros por forma de pago de las facturas que se hicieron en el
				// inicio de caja
				totalDebe.addAll(sumarCobroFps(cajaMoviInicio.getCajaMoviId(), docuEgreContabilizar));

				totalHaber.addAll(sumarCobroCxcs(cajaMoviInicio.getCajaMoviId(), docuEgreContabilizar));

			}
		}

		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = this.crearTranPlantilla(totalDebe, docuTran, cajaMoviCierre, 0, 1, orden, new BigDecimal(1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = this.crearTranPlantilla(totalHaber, docuTran, cajaMoviCierre, 0, 1, orden, new BigDecimal(-1),
				1);
		tranPlantillas.addAll(tranPlantillaTmps);

//		Si no encuentra datos no contabiliza nada
		if (tranPlantillas.isEmpty()) {
			return 0;
		}

		return gestionarTranPlantilla(tranPlantillas);
	}

	public Integer contabilizarAnticipos(CajaMovi cajaMoviInicio, CajaMovi cajaMoviCierre, Documento docuTran) throws Exception {

		Integer orden = 100;

		List<Object[]> anticipos;
		List<Object[]> fpmeAntis;

		List<TranPlantilla> tranPlantillas = new ArrayList<>();
		List<TranPlantilla> tranPlantillaTmps = new ArrayList<>();

		// Contabiliza el Debe en transaccion de docuemtnos de anticipos
		anticipos = sumarAnticipos(cajaMoviInicio.getCajaMoviId());

		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = this.crearTranPlantilla(anticipos, docuTran, cajaMoviCierre, 0, 1, orden, new BigDecimal(1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

		// Suma Fpme de tipo de documento anticipo
		fpmeAntis = sumarFpmeAnticipos(cajaMoviInicio.getCajaMoviId(), "is null");

		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = this.crearTranPlantilla(fpmeAntis, docuTran, cajaMoviCierre, 0, 1, orden, new BigDecimal(-1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

//		Si no encuentra datos no contabiliza nada
		if (tranPlantillas.isEmpty()) {
			return 0;
		}

		return gestionarTranPlantilla(tranPlantillas);
	}

	public Integer contabilizarNotasCredito(CajaMovi cajaMoviInicio, CajaMovi cajaMoviCierre, List<DocuEgre> docuEgres, Documento docuTran)
			throws Exception {

		Integer orden = 100;

		List<Object[]> totalDebe = new ArrayList<Object[]>();
		List<Object[]> totalHaber = new ArrayList<Object[]>();

		List<TranPlantilla> tranPlantillas = new ArrayList<>();
		List<TranPlantilla> tranPlantillaTmps = new ArrayList<>();

		for (DocuEgre docuEgreContabilizar : docuEgres) {
//					Determina si tiene permiso para contabilizar y que sean facturas (factor = -1) 
			if (docuEgreContabilizar.getDocumento().isContab()
					&& docuEgreContabilizar.getDocumento().getFactor() == 1) {

				// Contabiliza el Debe en transaccion de Nota de credito
				// Suma las Ventas de las Notas de credito que se hicieron en el inicio de caja
				totalDebe = sumarVentas(cajaMoviInicio.getCajaMoviId(), docuEgreContabilizar, "VENTA SIN IMPU");
				orden = 100 + tranPlantillas.size();
				tranPlantillaTmps = this.crearTranPlantilla(totalDebe, docuTran, cajaMoviCierre, 0, 4, orden,
						new BigDecimal(-1), 1);
				tranPlantillas.addAll(tranPlantillaTmps);

				totalDebe = sumarVentas(cajaMoviInicio.getCajaMoviId(), docuEgreContabilizar, "VENTA CON IMPU");
				orden = 100 + tranPlantillas.size();
				tranPlantillaTmps = this.crearTranPlantilla(totalDebe, docuTran, cajaMoviCierre, 0, 5, orden,
						new BigDecimal(-1), 1);
				tranPlantillas.addAll(tranPlantillaTmps);

				totalDebe = sumarVentas(cajaMoviInicio.getCajaMoviId(), docuEgreContabilizar, "VENTA IMPU");
				orden = 100 + tranPlantillas.size();
				tranPlantillaTmps = this.crearTranPlantilla(totalDebe, docuTran, cajaMoviCierre, 0, 6, orden,
						new BigDecimal(-1), 1);
				tranPlantillas.addAll(tranPlantillaTmps);

//				totalDebe = cajaMoviLista.sumarVentaIvas(cajaMovi.getCajaMoviId(), docuEgreContabilizar, "ICE");
//				orden = 100 + tranPlantillas.size();
//				tranPlantillaTmps = this.crearTranPlantilla(totalDebe, documento, this.cajaMovi, 0, 6, orden,
//						new BigDecimal(-1), 1);
//				tranPlantillas.addAll(tranPlantillaTmps);

				totalHaber = sumarCobroCxcsNcs(cajaMoviInicio.getCajaMoviId(), docuEgreContabilizar, "CXP-CXC");
				orden = 100 + tranPlantillas.size();
				tranPlantillaTmps = this.crearTranPlantilla(totalHaber, docuTran, cajaMoviCierre, 0, 1, orden,
						new BigDecimal(-1), 1);
				tranPlantillas.addAll(tranPlantillaTmps);

			}
		}

		List<Object[]> fpmeAntis;
		// Suma Fpme de tipo anticipo cuando se hizo una N/C sin selecionar una Cxc
		// (cxcSele)
		fpmeAntis = sumarFpmeAnticipos(cajaMoviInicio.getCajaMoviId(), "is not null");

		orden = 100 + tranPlantillas.size();
		tranPlantillaTmps = this.crearTranPlantilla(fpmeAntis, docuTran, cajaMoviCierre, 0, 1, orden, new BigDecimal(-1), 1);
		tranPlantillas.addAll(tranPlantillaTmps);

//		Si no encuentra datos no contabiliza nada
		if (tranPlantillas.isEmpty()) {
			return 0;
		}

		return gestionarTranPlantilla(tranPlantillas);
	}

//	Lista para contabilizar el debe en una transaccion de ventas
//	Se agrupo por group by pcuen.plan_cuen_id, d.factor para que ponga un solo valor y acumule en la cuenta de ventas del cliente
//	Si se agrupa de esta manera group by pcuen.plan_cuen_id, d.documento_id coloca una linea de total por cada documento
//	Los mismo para el metodo sumarCostoVentas
	public List<Object[]> sumarCuentasCxcs(Integer cajaMoviId, DocuEgre docuEgre, String tipoTran) throws Exception {

		StringBuilder sentencia = new StringBuilder();
		sentencia.append(sentenciaSelectVentas());
		sentencia.append(sentenciaJoinVentas());
		sentencia.append(sentenciaJoinVentasCliente());
		sentencia.append("where e.caja_movi_id = ?1 and cgpc.tipo_tran = ?2 and d.documento_id = ?3 ");
		sentencia.append("group by d.documento_id, pcuen.plan_cuen_id ");
		sentencia.append("order by d.documento_id, pcuen.codigo");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, cajaMoviId);
		query.setParameter(2, tipoTran);
		query.setParameter(3, docuEgre.getDocumentoId());

		return query.getResultList();
	}

//	Lista para contabilizar el haber en una transaccion de ventas
//	Se agrupo por group by pcuen.plan_cuen_id, d.factor para que ponga un solo valor de todos los documentos Ej: facturas, Facturas electronicas, notas de venta
//	Si se agrupa de esta manera group by pcuen.plan_cuen_id, d.documento_id coloca una linea de total por cada documento
//	Los mismo para el metodo sumarCostoVentas
	public List<Object[]> sumarVentas(Integer cajaMoviId, DocuEgre docuEgre, String tipoTran) throws Exception {

		StringBuilder sentencia = new StringBuilder();
		sentencia.append(sentenciaSelectVentas());
		sentencia.append(sentenciaJoinVentas());
		sentencia.append(sentenciaJoinVentasProducto());
		sentencia.append("where e.caja_movi_id = ?1 and pgpc.tipo_tran = ?2 and d.documento_id = ?3 ");
		sentencia.append("group by d.documento_id, pcuen.plan_cuen_id ");
		sentencia.append("order by d.documento_id, pcuen.codigo");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, cajaMoviId);
		query.setParameter(2, tipoTran);
		query.setParameter(3, docuEgre.getDocumentoId());

		return query.getResultList();
	}

	public List<Object[]> sumarCostoVentas(Integer cajaMoviId, DocuEgre docuEgre, String tipoTran) throws Exception {

// 		Se utiliza este ultimo parametro modulo_inve para determinar que calcule
// 		solamente el costo de los productos que pertenecen a un grupo de inventario 
//		ya que puede existir productos de gastos o servicios que se registren
// 		en el modulo de compras y en el modulo de ventas

		StringBuilder sentencia = new StringBuilder();
		sentencia.append(sentenciaSelectVentas());
		sentencia.append(sentenciaJoinVentas());
		sentencia.append(sentenciaJoinVentasProducto());
		sentencia.append("where e.caja_movi_id = ?1 and pgpc.tipo_tran = ?2 and d.documento_id = ?3 and pg.modulo_inve = ?4 ");
		sentencia.append("group by d.documento_id, pcuen.plan_cuen_id ");
		sentencia.append("order by d.documento_id, pcuen.codigo");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, cajaMoviId);
		query.setParameter(2, tipoTran);
		query.setParameter(3, docuEgre.getDocumentoId());
		query.setParameter(4, true);

		return query.getResultList();
	}

	public List<Object[]> sumarCostoVentasSubProductos(Integer cajaMoviId, DocuEgre docuEgre, String tipoTran) throws Exception {

//		Calcula el costo de los productos compuestos
//		Se le aumento columnas ya que la plantilla requiere el valor de la columna 9
// 		Se utiliza este ultimo parametro modulo_inve para determinar que calcule
// 		solamente el costo de los productos que pertenecen a un grupo de inventario 
//		ya que puede existir productos de gastos o servicios que se registren
// 		en el modulo de compras y en el modulo de ventas

		StringBuilder sentencia = new StringBuilder();
		sentencia.append("select pcuen.plan_cuen_id as plan_cuen_id, ");
		sentencia.append("1 as c1,2 as c2,3 as c3,4 as c4,5 as c5,6 as c6,7 as c7,8 as c8, ");
		sentencia.append("sum(ed.cantid * ed.costo) * -d.factor as egre_costo ");
		sentencia.append("from egre_deta ed ");
		sentencia.append("inner join egreso e on e.egreso_id = ed.egreso_id ");
		sentencia.append("inner join docu_egre de on de.documento_id = e.documento_id ");
		sentencia.append("inner join documento d on d.documento_id = de.documento_id ");
		sentencia.append("inner join producto pr on pr.producto_id = ed.producto_id ");
		sentencia.append("inner join prod_grup pg on pg.prod_grup_id = pr.prod_grup_id ");
		sentencia.append("inner join prod_grup_plan_cuen pgpc on pgpc.prod_grup_id = pg.prod_grup_id and d.documento_id = pgpc.documento_id ");
		sentencia.append("inner join plan_cuen pcuen on pcuen.plan_cuen_id = pgpc.plan_cuen_id ");
		sentencia.append("where e.caja_movi_id = ?1 ");
		sentencia.append("and pgpc.tipo_tran = ?2 ");
		sentencia.append("and d.documento_id = ?3 ");
		sentencia.append("and pg.modulo_inve = ?4 ");
		sentencia.append("and ed.egre_deta_supe_id is not null ");
		sentencia.append("group by d.documento_id, pcuen.plan_cuen_id ");
		sentencia.append("order by d.documento_id, pcuen.codigo");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, cajaMoviId);
		query.setParameter(2, tipoTran);
		query.setParameter(3, docuEgre.getDocumentoId());
		query.setParameter(4, true);

		return query.getResultList();
	}

//  Lista para contabilizar el debe en una transaccion de cobros
//	La primera consulta se hace para obtener los cobros de acuerdo a la cuenta contable de la forma de pago
//	se une con la segunda que se hace para obtener los cobros realizados por anticipos y en este caso se debe 
//	usar la cuenta contable de anticipo que esta en el proveedor
	public List<Object[]> sumarCobroFps(Integer cajaMoviId, DocuEgre docuEgre) throws Exception {

		StringBuilder sentencia = new StringBuilder();
		sentencia.append("select fppc.plan_cuen_id as plan_cuen_id, ");
		sentencia.append("sum(cd.total) as cobro_deta_total ");
		sentencia.append(sentenciaJoinCobrDeta());
		sentencia.append("inner join form_pago_plan_cuen fppc on fppc.form_pago_id = fp.form_pago_id ");
		sentencia.append("where fpme.caja_movi_id = ?1 and e.documento_id = ?4 ");
		sentencia.append("group by fppc.plan_cuen_id ");
		sentencia.append("union ");
		sentencia.append("select cgpc.plan_cuen_id as plan_cuen_id, ");
		sentencia.append("sum(cd.total) as cobro_deta_total ");
		sentencia.append(sentenciaJoinCobrDeta());
		sentencia.append(sentenciaJoinPersClie());
		sentencia.append(
				"where fpme.caja_movi_id = ?1 and cgpc.tipo_tran = ?2 and fp.tipo = ?3 and e.documento_id = ?4 ");
		sentencia.append("group by cgpc.plan_cuen_id ");
		sentencia.append("order by plan_cuen_id");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, cajaMoviId);
		query.setParameter(2, "ANTICIPO");
		query.setParameter(3, "AN");
		query.setParameter(4, docuEgre.getDocumentoId());

		return query.getResultList();
	}

//	Lista para contabilizar el haber en una transaccion de cobros	
//	Esta consulta es igual a sumarCobroCxcsNcs con la diferencia que el join se hace a e.egreso_id
//	y se tiene un parametro mas
	public List<Object[]> sumarCobroCxcs(Integer cajaMoviId, DocuEgre docuEgre) throws Exception {

		StringBuilder sentencia = new StringBuilder();
		sentencia.append("select cgpc.plan_cuen_id as plan_cuen_id, ");
		sentencia.append("sum(cd.total) as cobro_deta_total ");
		sentencia.append(sentenciaJoinCobrDeta());
		sentencia.append(sentenciaJoinPersClie());
		sentencia.append(
				"where fpme.caja_movi_id = ?1 and cgpc.tipo_tran = ?2 and fp.tipo <> ?3 and e.documento_id = ?4 ");
		sentencia.append("group by cgpc.plan_cuen_id ");
		sentencia.append("order by cgpc.plan_cuen_id");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, cajaMoviId);
		query.setParameter(2, "CXP-CXC");
		query.setParameter(3, "NC");
		query.setParameter(4, docuEgre.getDocumentoId());

		return query.getResultList();
	}

//	La primer consulta contabiliza las notas de credito pagadas con FP, para que por ejemplo 
//	si una factura se hizo de contado la nota de credito se reste de contado
//	La segunda parte de esta consulta es igual a sumarCobroCxcs con la diferencia que el join
//	se hacia a e.egreso_supe_id peor al hacer varias devoluciones apuntado a un mismo documento
//	la contablizacion duplicaba o triplicaba valores pero si tiene un parametro menos.
//	Contabiliza las notas de credito pagadas con NC
	public List<Object[]> sumarCobroCxcsNcs(Integer cajaMoviId, DocuEgre docuEgre, String tipoTran) throws Exception {

		StringBuilder sentencia = new StringBuilder();
		sentencia.append("select fppc.plan_cuen_id as plan_cuen_id, ");
		sentencia.append("sum(fpmeFp.total_reci) as total ");
		sentencia.append("from form_pago_movi_egre fpme ");
		sentencia.append("inner join egreso e on e.egreso_id = fpme.egreso_id ");
		sentencia.append("inner join documento d on d.documento_id = e.documento_id ");
		sentencia.append(sentenciaJoinFormPago());
		sentencia.append("where fpme.caja_movi_id = ?1 and e.documento_id = ?4 and fpme.egreso_id is not null ");
		sentencia.append("group by fppc.plan_cuen_id ");
		sentencia.append("union ");
		sentencia.append("select cgpc.plan_cuen_id as plan_cuen_id, ");
		sentencia.append("sum(cd.total) as cobro_deta_total ");
		sentencia.append(sentenciaJoinCobrDeta());
		sentencia.append(sentenciaJoinPersClie());
		sentencia.append(
				"where fpme.caja_movi_id = ?1 and cgpc.tipo_tran = ?2 and fp.tipo = ?3 and e.documento_id = ?4 ");
		sentencia.append("group by cgpc.plan_cuen_id ");
		sentencia.append("order by plan_cuen_id");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, cajaMoviId);
		query.setParameter(2, "CXP-CXC");
		query.setParameter(3, "NC");
		query.setParameter(4, docuEgre.getDocumentoId());

		return query.getResultList();
	}

	public List<Object[]> sumarFpmeAnticipos(Integer cajaMoviId, String esNulo) throws Exception {

		StringBuilder sentencia = new StringBuilder();
		sentencia.append("select cgpc.plan_cuen_id as plan_cuen_id, ");
		sentencia.append("sum(fpme.total) as total ");
		sentencia.append("from form_pago_movi_egre fpme ");
		sentencia.append("inner join docu_movi_egre dme on dme.documento_id = fpme.documento_id ");
		sentencia.append("inner join persona p on p.persona_id = fpme.persona_id ");
		sentencia.append("inner join pers_clie pc on pc.persona_id = p.persona_id ");
		sentencia.append("inner join clie_grup cg on cg.clie_grup_id = pc.clie_grup_id ");
		sentencia.append("inner join clie_grup_plan_cuen cgpc on cgpc.clie_grup_id = cg.clie_grup_id and cgpc.documento_id = dme.documento_id ");
		sentencia.append("inner join plan_cuen pcuen on pcuen.plan_cuen_id = cgpc.plan_cuen_id ");
		sentencia.append("where dme.tipo = ?1 and cgpc.tipo_tran = ?1 and fpme.caja_movi_id = ?2 and fpme.egreso_id ");
		sentencia.append(esNulo);
		sentencia.append(" ");
		sentencia.append("group by cgpc.plan_cuen_id");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, "ANTICIPO");
		query.setParameter(2, cajaMoviId);

		return query.getResultList();
	}

//	Suma los documentos en fpme de tipo anticipo para contabilizar el debe en la transaccion 
	public List<Object[]> sumarAnticipos(Integer cajaMoviId) throws Exception {

		StringBuilder sentencia = new StringBuilder();
		sentencia.append("select fppc.plan_cuen_id as plan_cuen_id, ");
		sentencia.append("sum(fpmeFp.total_reci) as total ");
		sentencia.append("from form_pago_movi_egre fpme ");
		sentencia.append("inner join docu_movi_egre dme on dme.documento_id = fpme.documento_id ");
		sentencia.append(sentenciaJoinFormPago());
		sentencia.append("where dme.tipo = ?1 and fpme.caja_movi_id = ?2 and fpme.egreso_id is null ");
		sentencia.append("group by fppc.plan_cuen_id");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, "ANTICIPO");
		query.setParameter(2, cajaMoviId);

		return query.getResultList();
	}

	@Override
	public List<Object[]> sumarDepositos(Integer cajaMoviId, Integer factor) throws Exception {

		StringBuilder sentencia = new StringBuilder();
		sentencia.append(
				"select dfpme.tipo, fp.form_pago_id, fpme.refere, fpme.nota, sum(cd.total) as cobro_deta_total, fpme.fecha ");
		sentencia.append("from cobr_deta cd ");
		sentencia.append("right join cxc cxc on cxc.cxc_id = cd.cxc_id ");
		sentencia.append("inner join egreso e on e.egreso_id = cxc.egreso_id ");
		sentencia.append("inner join documento d on d.documento_id = e.documento_id ");
		sentencia.append("left join fpme_form_pago fpmeFp on fpmeFp.fpme_form_pago_id = cd.fpme_form_pago_id ");
		sentencia.append("left join form_pago fp on fp.form_pago_id = fpmeFp.form_pago_id ");
		sentencia.append("left join form_pago_movi_egre fpme on fpme.fpme_id = fpmeFp.fpme_id ");
		sentencia.append("left join docu_movi_egre dfpme on dfpme.documento_id = fpme.documento_id ");
		sentencia.append("inner join form_pago_plan_cuen fppc on fppc.form_pago_id = fp.form_pago_id ");
		sentencia.append("inner join plan_cuen pc on pc.plan_cuen_id = fppc.plan_cuen_id ");
		sentencia.append("where d.contab = ?1 ");
		sentencia.append("and d.factor = ?2 ");
		sentencia.append("and fpme.caja_movi_id = ?3 ");
		sentencia.append("and fp.tipo2 = ?4 ");
		sentencia.append("group by dfpme.tipo, fp.form_pago_id, fpme.fpme_id");

		Query query = (Query) this.entityManager.createNativeQuery(sentencia.toString());
		query.setParameter(1, true);
		query.setParameter(2, factor);
		query.setParameter(3, cajaMoviId);
		query.setParameter(4, "BA");

		return query.getResultList();
	}

	public StringBuilder sentenciaSelectVentas() {

		StringBuilder sentencia = new StringBuilder("select pcuen.plan_cuen_id as plan_cuen_id, ");
		sentencia.append(
				"sum((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100) - ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) * -d.factor as egre_total_neto, ");
		sentencia.append("sum(ed.cantid * ed.precio_vent) * -d.factor as egre_total_bruto, ");
		sentencia.append(
				"sum((ed.cantid * ed.precio_vent * ed.descue / 100) + ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) * -d.factor as egre_total_descue, ");
		sentencia.append("sum( ");
		sentencia.append("( ");
		sentencia.append(
				" ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100) - ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) - ");
		sentencia.append(
				"	(((((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100) - ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) * (edi.porcen / 100)) ) / COALESCE((nullif((porcen / 100),0)),1)) ");
		sentencia.append(") * -d.factor ");
		sentencia.append(") as egre_sin_iva, ");
		sentencia.append(
				"sum(((((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100) - ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) * (edi.porcen / 100)) * -d.factor) / COALESCE((nullif((porcen / 100),0)),1)) as egre_con_iva, ");
		sentencia.append(
				"sum(((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100) - ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) * (edi.porcen / 100) + ");
		sentencia.append(
				"   (case when edi.tipo = 'ICE' then edi.porcen * ed.cantid else 0 end * edi.porcen / 100) ");
		sentencia.append("   ) * -d.factor as egre_iva, ");
		sentencia.append(
				"sum(((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100) - ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) + ");
		sentencia.append(
				"	(((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100) - ((ed.cantid * ed.precio_vent) - (ed.cantid * ed.precio_vent * ed.descue / 100)) * (e.descue / 100)) * (edi.porcen / 100)) + ");
		sentencia.append("	(case when edi.tipo = 'ICE' then edi.porcen * ed.cantid else 0 end) + ");
		sentencia.append(
				"	(case when edi.tipo = 'ICE' then edi.porcen * ed.cantid else 0 end * edi.porcen / 100) ");
		sentencia.append("   ) * -d.factor as egre_total, ");
		sentencia.append(
				"sum(case when edi.tipo = 'ICE' then edi.porcen * ed.cantid else 0 end) * -d.factor as egre_ice, ");
		sentencia.append("sum(ed.cantid * ed.costo) * -d.factor as egre_costo ");
		sentencia.append("from egre_deta_impu edi ");

		return sentencia;
	}

	public StringBuilder sentenciaJoinVentas() {
		StringBuilder sentencia = new StringBuilder("inner join egre_deta ed on ed.egre_deta_id = edi.egre_deta_id ");
		sentencia.append("inner join egreso e on e.egreso_id = ed.egreso_id ");
		sentencia.append("inner join sucursal s on s.sucursal_id = e.sucursal_id ");
		sentencia.append("inner join docu_egre de on de.documento_id = e.documento_id ");
		sentencia.append("inner join documento d on d.documento_id = de.documento_id ");

		return sentencia;
	}

	public StringBuilder sentenciaJoinVentasProducto() {
		StringBuilder sentencia = new StringBuilder("inner join producto pr on pr.producto_id = ed.producto_id ");
		sentencia.append("inner join prod_grup pg on pg.prod_grup_id = pr.prod_grup_id ");
		sentencia.append(
				"inner join prod_grup_plan_cuen pgpc on pgpc.prod_grup_id = pg.prod_grup_id and d.documento_id = pgpc.documento_id ");
		sentencia.append("inner join plan_cuen pcuen on pcuen.plan_cuen_id = pgpc.plan_cuen_id ");

		return sentencia;
	}

	public StringBuilder sentenciaJoinVentasCliente() {
		StringBuilder sentencia = new StringBuilder("inner join pers_clie pc on pc.persona_id = e.persona_id ");
		sentencia.append("inner join clie_grup cg on cg.clie_grup_id = pc.clie_grup_id ");
		sentencia.append(
				"inner join clie_grup_plan_cuen cgpc on cgpc.clie_grup_id = cg.clie_grup_id and cgpc.documento_id = d.documento_id ");
		sentencia.append("inner join plan_cuen pcuen on pcuen.plan_cuen_id = cgpc.plan_cuen_id ");

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
				"inner join clie_grup_plan_cuen cgpc on cgpc.clie_grup_id = cg.clie_grup_id and cgpc.documento_id = e.documento_id ");
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
