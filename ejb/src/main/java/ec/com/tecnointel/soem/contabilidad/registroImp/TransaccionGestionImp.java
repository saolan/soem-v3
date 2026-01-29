package ec.com.tecnointel.soem.contabilidad.registroImp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ec.com.tecnointel.soem.caja.modelo.CajaMovi;
import ec.com.tecnointel.soem.contabilidad.listaInt.TranDetaListaInt;
import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuen;
import ec.com.tecnointel.soem.contabilidad.modelo.TranDeta;
import ec.com.tecnointel.soem.contabilidad.modelo.TranPlantilla;
import ec.com.tecnointel.soem.contabilidad.modelo.Transaccion;
import ec.com.tecnointel.soem.contabilidad.registroInt.TranDetaRegisInt;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionGestionInt;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionRegisInt;
import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.general.interfac.EntityManagerSoem;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.registroInt.DocumentoRegisInt;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviEgre;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@Stateless
public class TransaccionGestionImp implements TransaccionGestionInt, Serializable {

	@Inject
	@EntityManagerSoem
	protected EntityManager entityManager;

	@Inject
	TransaccionRegisInt transaccionRegis;

	@Inject
	TranDetaListaInt tranDetaLista;

	@Inject
	TranDetaRegisInt tranDetaRegis;

	@Inject
	DocumentoRegisInt documentoRegis;

	private static final long serialVersionUID = -5715126892320160786L;

	@Override
	public Integer anularTransaccion(Integer transaccionId) throws Exception {

		Transaccion transaccion = new Transaccion();

		List<TranDeta> tranDetas = new ArrayList<>();

		tranDetas = this.buscarTranDetas(transaccionId);
		this.eliminarTranDetas(tranDetas);

		transaccion = this.transaccionRegis.buscarPorId(Transaccion.class, transaccionId);
		transaccion.setEstado("AN");
		transaccionRegis.modificar(transaccion);

		return transaccion.getNumero();
	}

	public List<TranDeta> buscarTranDetas(Integer transaccionId) throws Exception {

		TranDeta tranDeta = new TranDeta();
		Transaccion transaccion = new Transaccion();

		transaccion.setTransaccionId(transaccionId);
		tranDeta.setTransaccion(transaccion);

		return this.tranDetaLista.buscar(tranDeta, null);
	}

	public Documento buscarDocumentoPorId(Documento documento) throws Exception {

		return documentoRegis.buscarPorId(Documento.class, documento.getDocumento().getDocumentoId());
	}

	public List<TranPlantilla> crearTranPlantilla(List<Object[]> objects, Documento documento, Object o, int posicion1,
			int posicion2, int orden, BigDecimal factorDebeHaber, int factorOrden) {

		List<TranPlantilla> tranPlantillas = new ArrayList<>();

		for (Object[] object : objects) {

			PlanCuen planCuen = new PlanCuen();

			planCuen.setPlanCuenId((Integer) object[posicion1]);
			BigDecimal total = (BigDecimal) object[posicion2];

			// Controla que no se generen movimientos con valores en cero
			if (total.compareTo(BigDecimal.ZERO) != 0) {

				if (o instanceof Ingreso) {
					Ingreso ingreso = (Ingreso) o;
					TranPlantilla tranPlantilla = new TranPlantilla(orden, ingreso.getNota(), null,
							ingreso.getFechaEmis(), ingreso.getFechaRegi(), ingreso.getFechaHoraEmis(), ingreso.getFechaHoraRegi(),
							total.multiply(factorDebeHaber),
							documento.getDocuTran(), planCuen);
					tranPlantillas.add(tranPlantilla);
				} else if (o instanceof FormPagoMoviIngr) {
					FormPagoMoviIngr formPagoMoviIngr = (FormPagoMoviIngr) o;
					TranPlantilla tranPlantilla = new TranPlantilla(orden, formPagoMoviIngr.getNota(), null,
							formPagoMoviIngr.getFecha(), formPagoMoviIngr.getFecha(), formPagoMoviIngr.getFechaHora(), formPagoMoviIngr.getFechaHora(), 
							total.multiply(factorDebeHaber),
							documento.getDocuTran(), planCuen);
					tranPlantillas.add(tranPlantilla);
				} else if (o instanceof CajaMovi) {
					CajaMovi cajaMovi = (CajaMovi) o;
					TranPlantilla tranPlantilla = new TranPlantilla(orden, cajaMovi.getNota(), null,
							cajaMovi.getFecha(), cajaMovi.getFecha(), cajaMovi.getFechaHora(), cajaMovi.getFechaHora(),
							total.multiply(factorDebeHaber),
							documento.getDocuTran(), planCuen);
					tranPlantillas.add(tranPlantilla);
				} else if (o instanceof Egreso) {
					Egreso egreso = (Egreso) o;
					TranPlantilla tranPlantilla = new TranPlantilla(orden, egreso.getNota(), null,
							egreso.getFechaEmis(), egreso.getFechaRegi(), egreso.getFechaHoraEmis(), egreso.getFechaHoraRegi(),  
							total.multiply(factorDebeHaber),
							documento.getDocuTran(), planCuen);
					tranPlantillas.add(tranPlantilla);
				} else if (o instanceof FormPagoMoviEgre) {
					FormPagoMoviEgre formPagoMoviEgre = (FormPagoMoviEgre) o;
					TranPlantilla tranPlantilla = new TranPlantilla(orden, formPagoMoviEgre.getNota(), null,
							formPagoMoviEgre.getFecha(), formPagoMoviEgre.getFecha(), formPagoMoviEgre.getFechaHora(), formPagoMoviEgre.getFechaHora(), 
							total.multiply(factorDebeHaber),
							documento.getDocuTran(), planCuen);
					tranPlantillas.add(tranPlantilla);
				}

				orden = orden + factorOrden;
			}
		}

		return tranPlantillas;
	}

	public void eliminarTranDetas(List<TranDeta> tranDetas) throws Exception {

		for (TranDeta tranDeta : tranDetas) {
			tranDetaRegis.eliminar(tranDeta);
		}
	}

	public void eliminarTransaccion(Transaccion transaccion) throws Exception {
		this.transaccionRegis.eliminar(transaccion);
	}

	@Override
	public Integer gestionarTranPlantilla(List<TranPlantilla> tranPlantillas) throws Exception {

		Integer transaccionNumero;
		Integer transaccionId;

		BigDecimal totalDebe = new BigDecimal(0);
		BigDecimal totalHabe = new BigDecimal(0);
		BigDecimal difere = new BigDecimal(0);

		Transaccion transaccion = new Transaccion();
		// Set<TranDeta> tranDetas = new HashSet<>();
		List<TranDeta> tranDetas = new ArrayList<>();

		transaccionNumero = this.transaccionNumero(tranPlantillas.get(0).getDocuTran().getDocumento());

		// Ordena la plantilla por el atributo orden antes de recorrer
		// Caso contrario salen mezclado los valores positivos (Debe) con los
		// negativos (haber)
		Collections.sort(tranPlantillas, new Comparator<TranPlantilla>() {
			@Override
			public int compare(TranPlantilla tranPlantilla1, TranPlantilla tranPlantilla2) {
				return tranPlantilla1.getOrden() - tranPlantilla2.getOrden();
			}
		});

		for (TranPlantilla tranPlantilla : tranPlantillas) {

			TranDeta tranDeta = new TranDeta();

			tranDeta.setTransaccion(transaccion);
			tranDeta.setPlanCuen(tranPlantilla.getPlanCuen());
			tranDeta.setFechaEmis(tranPlantilla.getFechaEmis());
			tranDeta.setFechaHoraEmis(tranPlantilla.getFechaHoraEmis());
			tranDeta.setNota(tranPlantilla.getNotaDeta());
			tranDeta.setTotal(tranPlantilla.getTotal());

			tranDetas.add(tranDeta);

			if (tranPlantilla.getTotal().compareTo(new BigDecimal(0)) > 0) {
				totalDebe = totalDebe.add(tranPlantilla.getTotal());
			} else {
				totalHabe = totalHabe.add(tranPlantilla.getTotal());
			}

			totalDebe = totalDebe.setScale(6, RoundingMode.HALF_UP);
			totalHabe = totalHabe.setScale(6, RoundingMode.HALF_UP);

			transaccion.setDocuTran(tranPlantilla.getDocuTran());
			transaccion.setNumero(transaccionNumero);
			transaccion.setFechaRegi(tranPlantilla.getFechaRegi());
			transaccion.setFechaHoraRegi(tranPlantilla.getFechaHoraRegi());
			transaccion.setFechaEmis(tranPlantilla.getFechaEmis());
			transaccion.setFechaHoraEmis(tranPlantilla.getFechaHoraEmis());
			transaccion.setTotalDebe(totalDebe);
			transaccion.setTotalHabe(totalHabe);

			// Estas dos lineas hacen lo mismo
			difere = totalDebe.add(totalHabe);

//			Se coloca dos decimales porque con seis salia 0.000001 de diferencia
//			y en la pagina salia -0.00
			difere = difere.setScale(2, RoundingMode.HALF_UP);
			// difere = difere.add(tranDeta.getTotal());

			transaccion.setDifere(difere);
			transaccion.setNota(tranPlantilla.getNotaCabe());

			if (difere.compareTo(new BigDecimal(0)) == 0) {
				transaccion.setEstado("PR");
			} else {
				transaccion.setEstado("GR");
			}
		}

		transaccionId = this.insertarTransaccion(transaccion);
		this.insertarTranDeta(tranDetas);

		return transaccionId;
	}

	public Integer insertarTransaccion(Transaccion transaccion) throws Exception {
		Object id = transaccionRegis.insertar(transaccion);
		return (Integer) id;

	}

	public void insertarTranDeta(List<TranDeta> tranDetas) throws Exception {

		for (TranDeta tranDeta : tranDetas) {
			tranDetaRegis.insertar(tranDeta);
		}
	}

	public Integer transaccionNumero(Documento documentoBuscar) throws Exception {

		Integer numero;
		// Recuperar el ultimo numero de transaccion
		Documento documento = new Documento();
		documento = documentoRegis.buscarPorId(Documento.class, documentoBuscar.getDocumentoId());
		numero = documento.getNumero() + 1;

		// Actualizar secuencial
		documento.setNumero(numero);
		this.documentoRegis.modificar(documento);

		return numero;
	}
}
