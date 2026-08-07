package ec.com.tecnointel.soem.documeElec.registroImp;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import ec.com.tecnointel.soem.documeElec.modelo.InfoTributaria;
import ec.com.tecnointel.soem.documeElec.modelo.factura.Factura;
import ec.com.tecnointel.soem.documeElec.modelo.factura.Factura.InfoFactura;
import ec.com.tecnointel.soem.documeElec.modelo.factura.Impuesto;
import ec.com.tecnointel.soem.documeElec.modelo.guia.Destinatario;
import ec.com.tecnointel.soem.documeElec.modelo.guia.Detalle;
import ec.com.tecnointel.soem.documeElec.modelo.guia.GuiaRemision;
import ec.com.tecnointel.soem.documeElec.modelo.guia.GuiaRemision.InfoGuiaRemision;
import ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.LiquidacionCompra;
import ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.LiquidacionCompra.Detalles;
import ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.LiquidacionCompra.InfoAdicional;
import ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.LiquidacionCompra.InfoAdicional.CampoAdicional;
import ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.LiquidacionCompra.InfoLiquidacionCompra;
import ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.LiquidacionCompra.InfoLiquidacionCompra.TotalConImpuestos.TotalImpuesto;
import ec.com.tecnointel.soem.documeElec.modelo.notaCredito.NotaCredito;
import ec.com.tecnointel.soem.documeElec.modelo.notaCredito.NotaCredito.InfoNotaCredito;
import ec.com.tecnointel.soem.documeElec.modelo.notaCredito.TotalConImpuestos;
import ec.com.tecnointel.soem.documeElec.modelo.notaDebito.NotaDebito;
import ec.com.tecnointel.soem.documeElec.modelo.notaDebito.NotaDebito.InfoNotaDebito;
import ec.com.tecnointel.soem.documeElec.modelo.notaDebito.NotaDebito.InfoNotaDebito.Impuestos;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.CompraCajBanano;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.ComprobanteRetencion;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.ComprobanteRetencion.DocsSustento;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.ComprobanteRetencion.InfoCompRetencion;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.DetalleImpuesto;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.DetalleImpuestos;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.Dividendos;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.DocSustento;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.ImpuestoDocSustento;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.ImpuestosDocSustento;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.Pago;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.Pagos;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.Reembolsos;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.Reembolsos.ReembolsoDetalle;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.Retenciones;
import ec.com.tecnointel.soem.documeElec.registroInt.DocumeElecRegisInt;
import ec.com.tecnointel.soem.egreso.modelo.EgreDeta;
import ec.com.tecnointel.soem.egreso.modelo.EgreDetaImpu;
import ec.com.tecnointel.soem.egreso.modelo.EgreNota;
import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.egreso.modelo.TotalDocu;
import ec.com.tecnointel.soem.egreso.registroInt.EgresoRegisInt;
import ec.com.tecnointel.soem.general.util.Util;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDeta;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDetaImpu;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.modelo.ReteDeta;
import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import ec.com.tecnointel.soem.inventario.listaInt.ProdDimmListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdDimm;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import ec.com.tecnointel.soem.tesoreria.modelo.FpmeFormPago;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

@Stateless
public class DocumeElectRegisImp implements DocumeElecRegisInt, Serializable {

	// Almacena el iva para el calcula del ICE
	Dimm dimmIva;
	String leyenda1;
	String leyenda2;
	String msgInfoAdicional;

	public static final String RUC_PROVEEDOR_SISTEMA = "RUC Proveedor";

	private static final long serialVersionUID = 6269695413974755962L;

	@Inject
	EgresoRegisInt egresoRegis;

	@Inject
	ProdDimmListaInt prodDimmLista;

	@Override
	public InfoTributaria cargarInfoTributaria(Sucursal sucursal, String codigoDocu, String serie1, String serie2,
			String numero, String ambien, String tipoEmis, String claveAcce) {

		String nombreComercial = sucursal.getNombreCome();

		InfoTributaria infoTributaria = new InfoTributaria();

		infoTributaria.setRuc(sucursal.getRuc());
		infoTributaria.setRazonSocial(sucursal.getRazonSoci());
		if (nombreComercial != null && !nombreComercial.isEmpty()) {
			infoTributaria.setNombreComercial(nombreComercial);
		}
		infoTributaria.setDirMatriz(sucursal.getDirecc());

		infoTributaria.setCodDoc(codigoDocu);
		infoTributaria.setEstab(serie1);
		infoTributaria.setPtoEmi(serie2);
		infoTributaria.setSecuencial(numero);
		infoTributaria.setClaveAcceso(claveAcce);
		infoTributaria.setAmbiente(ambien);
		infoTributaria.setTipoEmision(tipoEmis);

//		Estas Leyendas sale dependiendo del tipo de contrinuyente
//		y si en parametros (3170, 3171) esta activado estos mensajes 
		if (!this.leyenda1.equals("nulo")) {
			infoTributaria.setContribuyenteRimpe(this.leyenda1);
		}

		if (!this.leyenda2.equals("nulo")) {
			infoTributaria.setAgenteRetencion(this.leyenda2);
		}

		return infoTributaria;
	}

	@Override
	public String generarClaveAcceso(Sucursal sucursal, LocalDate fechaEmision, String codigoDocu, String serie1,
			String serie2, String numero, String ambien, String tipoEmis, String codiClav) {

		StringBuilder claveAcceso = new StringBuilder();

		claveAcceso.append(Util.cambiarFormatoFechaString(fechaEmision, "ddMMyyyy"));
		claveAcceso.append(codigoDocu); // Tipo de comprobante
		claveAcceso.append(sucursal.getRuc()); // Numero de ruc del que emite la
												// factura
		claveAcceso.append(ambien); // Este dato no tiene un combo para saber
									// que es
		claveAcceso.append(serie1);
		claveAcceso.append(serie2);
		claveAcceso.append(numero);
		claveAcceso.append(codiClav); // Digito interno para verificaci�n
										// crear uno en parametros o en algun
										// lugar
		claveAcceso.append(tipoEmis);

		int verificador = this.obtenerVerificador(claveAcceso.toString());

		claveAcceso.append(verificador);

		if (claveAcceso.length() != 49) {
			return null;
		}

		return claveAcceso.toString();
	}

	public int obtenerVerificador(String claveAcceso) {

		int baseMultiplicador = 7;
		int[] aux = new int[claveAcceso.length()];
		int multiplicador = 2;
		int total = 0;
		int verificador = 0;
		for (int i = aux.length - 1; i >= 0; i--) {
			aux[i] = Integer.parseInt("" + claveAcceso.charAt(i));
			aux[i] *= multiplicador;
			multiplicador++;
			if (multiplicador > baseMultiplicador) {
				multiplicador = 2;
			}
			total += aux[i];
		}
		if ((total == 0) || (total == 1)) {
			verificador = 0;
		} else {
			verificador = 11 - total % 11 == 11 ? 0 : 11 - total % 11;
		}
		if (verificador == 10) {
			verificador = 1;
		}
		return verificador;
	}

	@Override
	public String marshalFactura(Factura factura, String rutaGenerados) throws Exception {

		String estado = null;

		JAXBContext context = JAXBContext.newInstance(new Class[] { Factura.class });

		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty("jaxb.encoding", "UTF-8");
		marshaller.setProperty("jaxb.formatted.output", Boolean.valueOf(true));
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		OutputStreamWriter out = new OutputStreamWriter(byteArrayOutputStream, "UTF-8");
		marshaller.marshal(factura, out);
		OutputStream outputStream = new FileOutputStream(
				rutaGenerados + factura.getInfoTributaria().getClaveAcceso() + ".xml");
		byteArrayOutputStream.writeTo(outputStream);
		byteArrayOutputStream.close();
		outputStream.close();

		return estado;

	}

	@Override
	public String marshalNotaCredito(NotaCredito notaCredito, String rutaGenerados) throws Exception {
		String respuesta = null;

		JAXBContext context = JAXBContext.newInstance(new Class[] { NotaCredito.class });
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty("jaxb.encoding", "UTF-8");
		marshaller.setProperty("jaxb.formatted.output", Boolean.valueOf(true));
		OutputStreamWriter out = new OutputStreamWriter(
				new FileOutputStream(rutaGenerados + notaCredito.getInfoTributaria().getClaveAcceso() + ".xml"),
				"UTF-8");
		marshaller.marshal(notaCredito, out);
		out.close();

		return respuesta;
	}

	@Override
	public String marshalNotaDebito(NotaDebito notaDebito, String rutaGenerados) throws Exception {
		String respuesta = null;
		JAXBContext context = JAXBContext.newInstance(new Class[] { NotaDebito.class });
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty("jaxb.encoding", "UTF-8");
		marshaller.setProperty("jaxb.formatted.output", Boolean.valueOf(true));
		OutputStreamWriter out = new OutputStreamWriter(
				new FileOutputStream(rutaGenerados + notaDebito.getInfoTributaria().getClaveAcceso() + ".xml"),
				"UTF-8");
		marshaller.marshal(notaDebito, out);
		out.close();

		return respuesta;
	}

	@Override
	public String marshalGuiaRemision(GuiaRemision guiaRemision, String rutaGenerados) throws Exception {
		String respuesta = null;
		JAXBContext context = JAXBContext.newInstance(new Class[] { GuiaRemision.class });
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty("jaxb.encoding", "UTF-8");
		marshaller.setProperty("jaxb.formatted.output", Boolean.valueOf(true));
		OutputStreamWriter out = new OutputStreamWriter(
				new FileOutputStream(rutaGenerados + guiaRemision.getInfoTributaria().getClaveAcceso() + ".xml"),
				"UTF-8");
		marshaller.marshal(guiaRemision, out);
		out.close();

		return respuesta;
	}

	@Override
	public String marshalRetencion(ComprobanteRetencion comprobanteRetencion, String rutaGenerados) throws Exception {
		String respuesta = null;
		JAXBContext context = JAXBContext.newInstance(new Class[] { ComprobanteRetencion.class });
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty("jaxb.encoding", "UTF-8");
		marshaller.setProperty("jaxb.formatted.output", Boolean.valueOf(true));

		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(
				rutaGenerados + comprobanteRetencion.getInfoTributaria().getClaveAcceso() + ".xml"), "UTF-8");
		marshaller.marshal(comprobanteRetencion, out);
		out.close();

		return respuesta;
	}

	@Override
	public String marshalLiquidacion(LiquidacionCompra liquidacionCompra, String rutaGenerados) throws Exception {
		String respuesta = null;
		JAXBContext context = JAXBContext.newInstance(new Class[] { LiquidacionCompra.class });
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty("jaxb.encoding", "UTF-8");
		marshaller.setProperty("jaxb.formatted.output", Boolean.valueOf(true));

		OutputStreamWriter out = new OutputStreamWriter(
				new FileOutputStream(rutaGenerados + liquidacionCompra.getInfoTributaria().getClaveAcceso() + ".xml"),
				"UTF-8");
		marshaller.marshal(liquidacionCompra, out);
		out.close();

		return respuesta;
	}

	@Override
	public Object generarComprobanteElectronico(Egreso egreso, List<EgreDeta> egreDetaDataTable, String codigoIva,
			String codigoIce, String codigoIrbpnr, FpmeFormPago fpmeFormPago) throws Exception {

		BigDecimal totalBrutoCabe = new BigDecimal(0);
		BigDecimal totalBrutoDeta = new BigDecimal(0);
		BigDecimal descueCabe = new BigDecimal(0);
		BigDecimal descueDeta = new BigDecimal(0);
		BigDecimal descueTotal = new BigDecimal(0);
		BigDecimal totalNetoDeta = new BigDecimal(0);
		BigDecimal impuestoDeta = new BigDecimal(0);
		BigDecimal impuestoDetaAcum = new BigDecimal(0);
		BigDecimal totalDeta = new BigDecimal(0);
		BigDecimal totalCabe = new BigDecimal(0);
		BigDecimal descueCabePorc = new BigDecimal(0);
		BigDecimal totalIce = new BigDecimal(0);

		List<TotalDocu> totalDocus = new ArrayList<>();

		// Facturacion Electr�nica
		Factura.Detalles facturaDetalles = new Factura.Detalles();
		NotaCredito.Detalles notaCreditoDetalles = new NotaCredito.Detalles();

		NotaDebito.Motivos motivos = new NotaDebito.Motivos();
		// Este de declara aqui porque la nota de debito no tiene detalle
		NotaDebito.InfoNotaDebito.Impuestos notaDebitoImpuestos = new Impuestos();

		Destinatario destinatario = new Destinatario();
		GuiaRemision.Destinatarios destinatarios = new GuiaRemision.Destinatarios();
		Destinatario.Detalles guiaRemisionDetalles = new Destinatario.Detalles();

		if (egreso.getDocuEgre().getDocumeElec().equals("GuiaRemision")) {
			destinatario = this.cargarDestinatario(egreso);
		}
		// Fin facturacion Electr�nica

		// Se recupera el descuento que tiene en la cabecera para poder realizar
		// los calculos respectivos
		if (egreso.getDescue() != null) {
			descueCabePorc = egreso.getDescue();
		}

		for (EgreDeta egreDeta : egreDetaDataTable) {

			impuestoDetaAcum = new BigDecimal(0);

			// Facturacion Electr�nica
			Factura.Detalles.Detalle facturaDetalle = new Factura.Detalles.Detalle();
			NotaCredito.Detalles.Detalle notaCreditoDetalle = new NotaCredito.Detalles.Detalle();
			NotaDebito.Motivos.Motivo motivo = new NotaDebito.Motivos.Motivo();

			Detalle guiaRemisionDetalle = new Detalle();
			// Facturacion Electr�nica

			totalBrutoDeta = egreDeta.getCantid().multiply(egreDeta.getPrecioVent());
			descueDeta = totalBrutoDeta.multiply(egreDeta.getDescue()).divide(new BigDecimal(100));
			totalNetoDeta = totalBrutoDeta.subtract(descueDeta);
			egreDeta.setTotal(totalNetoDeta);

			descueCabe = totalNetoDeta.multiply(descueCabePorc).divide(new BigDecimal(100));
			totalNetoDeta = totalNetoDeta.subtract(descueCabe);

			// Facturacion Electr�nica
			if (egreso.getDocuEgre().getDocumeElec().equals("Factura")) {

				facturaDetalle.setCodigoPrincipal(egreDeta.getProducto().getCodigoBarra());
				facturaDetalle.setCodigoAuxiliar(egreDeta.getProducto().getCodigo());
				facturaDetalle.setDescripcion(egreDeta.getProducto().getDescri());
				facturaDetalle.setCantidad(egreDeta.getCantid().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
				facturaDetalle.setPrecioUnitario(
						egreDeta.getPrecioVent().setScale(4, RoundingMode.HALF_UP).stripTrailingZeros());
				// detalle.setPrecioSinSubsidio((BigDecimal)
				// this.modeloDetalle.getValueAt(i, 6)); // NO EXISTE EN LA BASE DE DATOS

				// Descuento en valor no en porcentaje
				facturaDetalle.setDescuento(descueDeta.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
				facturaDetalle.setPrecioTotalSinImpuesto(egreDeta.getTotal().setScale(2, RoundingMode.HALF_UP));

				facturaDetalles.getDetalle().add(facturaDetalle);

			} else if (egreso.getDocuEgre().getDocumeElec().equals("NotaCredito")) {

				notaCreditoDetalle.setCodigoInterno(egreDeta.getProducto().getCodigoBarra());
				notaCreditoDetalle.setCodigoAdicional(egreDeta.getProducto().getCodigo());
				notaCreditoDetalle.setDescripcion(egreDeta.getProducto().getDescri());
				notaCreditoDetalle
						.setCantidad(egreDeta.getCantid().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
				notaCreditoDetalle.setPrecioUnitario(
						egreDeta.getPrecioVent().setScale(4, RoundingMode.HALF_UP).stripTrailingZeros());

				// Descuento en valor no en porcentaje
				notaCreditoDetalle.setDescuento(descueDeta.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
				notaCreditoDetalle.setPrecioTotalSinImpuesto(egreDeta.getTotal().setScale(2, RoundingMode.HALF_UP));

				notaCreditoDetalles.getDetalle().add(notaCreditoDetalle);

			} else if (egreso.getDocuEgre().getDocumeElec().equals("NotaDebito")) {

				motivo.setRazon(egreDeta.getProducto().getDescri());
				motivo.setValor(egreDeta.getTotal().setScale(2, RoundingMode.HALF_UP));
				motivos.getMotivo().add(motivo);

			} else if (egreso.getDocuEgre().getDocumeElec().equals("GuiaRemision")) {

				guiaRemisionDetalle.setCodigoInterno(egreDeta.getProducto().getCodigoBarra());
				guiaRemisionDetalle.setCodigoAdicional(egreDeta.getProducto().getCodigo());
				guiaRemisionDetalle.setDescripcion(egreDeta.getProducto().getDescri());
				guiaRemisionDetalle
						.setCantidad(egreDeta.getCantid().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());

				guiaRemisionDetalles.getDetalle().add(guiaRemisionDetalle);

				destinatario.setDetalles(guiaRemisionDetalles);

			}
			// Fin facturacion Electr�nica

			// Recorrido de impuestos grabados en cada producto
			for (EgreDetaImpu egreDetaImpu : egreDeta.getEgreDetaImpus()) {

				Factura.Detalles.Detalle.Impuestos facturaImpuestos = new Factura.Detalles.Detalle.Impuestos();
				NotaCredito.Detalles.Detalle.Impuestos notaCreditoImpuestos = new NotaCredito.Detalles.Detalle.Impuestos();

//				Calcula el total del ICE por Fundas Plasticas
				if (egreDetaImpu.getTipo().equals("ICE")) {
					totalIce = totalIce.add(egreDeta.getCantid().multiply(egreDetaImpu.getPorcen()));
				}
//				Fin Calcula el total del ICE por Fundas Plasticas

				// Facturacion Electr�nica
				if (egreso.getDocuEgre().getDocumeElec().equals("Factura")) {

					Impuesto impuesto = new Impuesto();
					// Aqui Controla codigos de ice y IRBPNR
					// Codigo = 0,2,3,6,7
					if (egreDetaImpu.getTipo().equals("IVA")) {
						impuesto.setCodigo(codigoIva);
						impuesto.setBaseImponible(egreDeta.getTotal().setScale(2, RoundingMode.HALF_UP));
						impuesto.setValor(
								egreDeta.getTotal().multiply(egreDetaImpu.getPorcen().divide(new BigDecimal(100)))
										.setScale(2, RoundingMode.HALF_UP));

					} else if (egreDetaImpu.getTipo().equals("ICE")) {
						impuesto.setCodigo(codigoIce);
						impuesto.setBaseImponible(egreDeta.getCantid().setScale(2, RoundingMode.HALF_UP));
						impuesto.setValor(egreDeta.getCantid().multiply(egreDetaImpu.getPorcen()).setScale(2,
								RoundingMode.HALF_UP));

					} else if (egreDetaImpu.getTipo().equals("IRBPNR")) {
						impuesto.setCodigo(codigoIrbpnr);
						impuesto.setBaseImponible(egreDeta.getTotal().setScale(2, RoundingMode.HALF_UP));
						impuesto.setValor(
								egreDeta.getTotal().multiply(egreDetaImpu.getPorcen().divide(new BigDecimal(100)))
										.setScale(2, RoundingMode.HALF_UP));
					}

					// impuesto.setCodigo(codigoIva);
					impuesto.setCodigoPorcentaje(egreDetaImpu.getCodigo());
					impuesto.setTarifa(egreDetaImpu.getPorcen().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
					// impuesto.setBaseImponible(egreDeta.getTotal().setScale(2,
					// RoundingMode.HALF_UP));
					// impuesto.setValor(egreDeta.getTotal().multiply(egreDetaImpu.getPorcen().divide(new
					// BigDecimal(100)))
					// .setScale(2, RoundingMode.HALF_UP));
					facturaImpuestos.getImpuesto().add(impuesto);
					facturaDetalle.setImpuestos(facturaImpuestos);

				} else if (egreso.getDocuEgre().getDocumeElec().equals("NotaCredito")) {

					ec.com.tecnointel.soem.documeElec.modelo.notaCredito.Impuesto impuesto = new ec.com.tecnointel.soem.documeElec.modelo.notaCredito.Impuesto();

					// Aqui Controla codigos de ice y IRBPNR
					// Codigo = 0,2,3,6,7
					if (egreDetaImpu.getTipo().equals("IVA")) {
						impuesto.setCodigo(codigoIva);
						impuesto.setBaseImponible(egreDeta.getTotal().setScale(2, RoundingMode.HALF_UP));
						impuesto.setValor(
								egreDeta.getTotal().multiply(egreDetaImpu.getPorcen().divide(new BigDecimal(100)))
										.setScale(2, RoundingMode.HALF_UP));

					} else if (egreDetaImpu.getTipo().equals("ICE")) {
						impuesto.setCodigo(codigoIce);
						impuesto.setBaseImponible(egreDeta.getCantid().setScale(2, RoundingMode.HALF_UP));
						impuesto.setValor(egreDeta.getCantid().multiply(egreDetaImpu.getPorcen()).setScale(2,
								RoundingMode.HALF_UP));

					} else if (egreDetaImpu.getTipo().equals("IRBPNR")) {
						impuesto.setCodigo(codigoIrbpnr);
						impuesto.setBaseImponible(egreDeta.getTotal().setScale(2, RoundingMode.HALF_UP));
						impuesto.setValor(
								egreDeta.getTotal().multiply(egreDetaImpu.getPorcen().divide(new BigDecimal(100)))
										.setScale(2, RoundingMode.HALF_UP));
					}

//					impuesto.setCodigo(codigoIva);
					impuesto.setCodigoPorcentaje(egreDetaImpu.getCodigo());
					impuesto.setTarifa(egreDetaImpu.getPorcen().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
//					impuesto.setBaseImponible(egreDeta.getTotal().setScale(2, RoundingMode.HALF_UP));
//					impuesto.setValor(egreDeta.getTotal().multiply(egreDetaImpu.getPorcen().divide(new BigDecimal(100)))
//							.setScale(2, RoundingMode.HALF_UP));
					notaCreditoImpuestos.getImpuesto().add(impuesto);
					notaCreditoDetalle.setImpuestos(notaCreditoImpuestos);

				} else if (egreso.getDocuEgre().getDocumeElec().equals("NotaDebito")) {

					ec.com.tecnointel.soem.documeElec.modelo.notaDebito.Impuesto impuesto = new ec.com.tecnointel.soem.documeElec.modelo.notaDebito.Impuesto();
					impuesto.setCodigo(codigoIva);
					impuesto.setCodigoPorcentaje(egreDetaImpu.getCodigo());
					impuesto.setTarifa(egreDetaImpu.getPorcen().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
					impuesto.setBaseImponible(egreDeta.getTotal().setScale(2, RoundingMode.HALF_UP));
					impuesto.setValor(egreDeta.getTotal().multiply(egreDetaImpu.getPorcen().divide(new BigDecimal(100)))
							.setScale(2, RoundingMode.HALF_UP));
					notaDebitoImpuestos.getImpuesto().add(impuesto);

				}
				// Fin facturacion Electr�nica

				BigDecimal factor = new BigDecimal(egreDetaImpu.getFactor());

				impuestoDeta = (totalNetoDeta.multiply(egreDetaImpu.getPorcen()).divide(new BigDecimal(100)));
				impuestoDeta = impuestoDeta.multiply(factor);
				// impuestoDeta = impuestoDeta.setScale(2,
				// BigDecimal.ROUND_HALF_UP);

				// Acumula impuesto solo para calculo del total del
				// documento cuando un producto esta grabado con mas de un
				// impuesto
				impuestoDetaAcum = impuestoDetaAcum.add(impuestoDeta);

				// Crear Key para treeMap para separar y acumular impuestos
				// y subtotal de cada impuesto
				String claveImpuesto[] = egreDetaImpu.getDescri().split(";");
				String claveSubtotal = "Subtotal " + claveImpuesto[1];

				TotalDocu totalDocu1 = new TotalDocu();
				totalDocu1.setDescri(claveSubtotal);
				int indice1 = totalDocus.indexOf(totalDocu1);

				TotalDocu totalDocu2 = new TotalDocu();
				totalDocu2.setDescri(claveImpuesto[1]);
				int indice2 = totalDocus.indexOf(totalDocu2);

				if (indice1 == -1) {

					totalDocu1.setCodigo(egreDetaImpu.getCodigo());
					totalDocu1.setDescri(claveSubtotal);
					totalDocu1.setPorcen(egreDetaImpu.getPorcen());
//					totalDocu1.setValor(totalNetoDeta);
					totalDocu1.setTipoImpu(egreDetaImpu.getTipo());

					if (egreDetaImpu.getTipo().equals("ICE")) {
						totalDocu1.setValor(egreDeta.getCantid());
					} else {
						totalDocu1.setValor(totalNetoDeta);
					}

					totalDocus.add(totalDocu1);

					// TotalDocu totalDocu2 = new TotalDocu();
					// totalDocu.setCodigo(ingrDetaImpu.getCodigo());
					totalDocu2.setDescri(claveImpuesto[1]);
					// totalDocu.setPorcen(ingrDetaImpu.getPorcen());
					if (egreDetaImpu.getTipo().equals("ICE")) {
						totalDocu2.setValor(egreDeta.getCantid().multiply(egreDetaImpu.getPorcen()));
						totalDocus.add(totalDocu2);

					} else {
						totalDocu2.setValor(impuestoDeta);
						totalDocus.add(totalDocu2);
					}
				} else {

					BigDecimal total = new BigDecimal(0);
					TotalDocu totalDocu3 = totalDocus.get(indice1);
					total = totalDocu3.getValor();
					totalDocu3.setValor(total.add(totalNetoDeta));

					BigDecimal total1 = new BigDecimal(0);
					TotalDocu totalDocu4 = totalDocus.get(indice2);
					total1 = totalDocu4.getValor();
					totalDocu4.setValor(total1.add(impuestoDeta));
				}
			}

			totalDeta = totalNetoDeta.add(impuestoDetaAcum);
			totalBrutoCabe = totalBrutoCabe.add(totalBrutoDeta);
			descueTotal = descueTotal.add(descueDeta).add(descueCabe);
			totalCabe = totalCabe.add(totalDeta);

		}

		TotalDocu totalDocu = new TotalDocu();
		totalDocu.setDescri("Total Descuento");
		totalDocu.setValor(descueTotal);
		totalDocus.add(totalDocu);

		totalDocu = new TotalDocu();
		totalDocu.setDescri("Total Neto");
		totalDocu.setValor(totalBrutoCabe.subtract(descueTotal));
		totalDocus.add(totalDocu);

		BigDecimal ivaIce = totalIce.multiply(this.dimmIva.getPorcen()).divide(new BigDecimal(100), 6,
				RoundingMode.HALF_UP);

		totalDocu = new TotalDocu();
		totalDocu.setDescri("Total Documento");
//		totalDocu.setValor(totalCabe);
		totalDocu.setValor(totalCabe.add(totalIce).add(ivaIce));

		totalDocus.add(totalDocu);

		// Documento Electronico
		Object object = null;

		if (egreso.getDocuEgre().getDocumeElec().equals("Factura")) {

			Factura factura = new Factura();

			Factura.InfoAdicional infoAdicional = this.generarInformacionAdicionalFactura(egreso);
			factura.setInfoTributaria(this.cargarInfoTributaria(egreso));
			factura.setInfoFactura(
					this.cargarInfoFactura(egreso, codigoIva, codigoIce, codigoIrbpnr, totalDocus, fpmeFormPago));
			if (facturaDetalles != null) {
				factura.setDetalles(facturaDetalles);
			}
			if (infoAdicional.getCampoAdicional().size() > 0) {
				factura.setInfoAdicional(infoAdicional);
			}

			factura.setVersion("1.1.0");
			factura.setId("comprobante");

			object = factura;

		} else if (egreso.getDocuEgre().getDocumeElec().equals("NotaCredito")) {

			NotaCredito notaCredito = new NotaCredito();

			NotaCredito.InfoAdicional infoAdicional = generarInformacionAdicionalNotaCredito(egreso);
			notaCredito.setInfoTributaria(this.cargarInfoTributaria(egreso));
			notaCredito.setInfoNotaCredito(
					this.cargarInfoNotaCredito(egreso, codigoIva, codigoIce, codigoIrbpnr, totalDocus));
			if (notaCreditoDetalles != null) {
				notaCredito.setDetalles(notaCreditoDetalles);
			}
			if (infoAdicional.getCampoAdicional().size() > 0) {
				notaCredito.setInfoAdicional(infoAdicional);
			}
			notaCredito.setVersion("1.1.0");
			notaCredito.setId("comprobante");

			object = notaCredito;

		} else if (egreso.getDocuEgre().getDocumeElec().equals("NotaDebito")) {

			NotaDebito notaDebito = new NotaDebito();

			NotaDebito.InfoAdicional infoAdicional = generarInformacionAdicionalNotaDebito(egreso);
			notaDebito.setInfoTributaria(this.cargarInfoTributaria(egreso));
			notaDebito.setInfoNotaDebito(this.cargarInfoNotaDebito(egreso, totalDocus, fpmeFormPago));
			notaDebito.getInfoNotaDebito().setImpuestos(notaDebitoImpuestos);
			notaDebito.setMotivos(motivos);

			if (infoAdicional.getCampoAdicional().size() > 0) {
				notaDebito.setInfoAdicional(infoAdicional);
			}
			notaDebito.setVersion("1.0.0");
			notaDebito.setId("comprobante");

			object = notaDebito;

		} else if (egreso.getDocuEgre().getDocumeElec().equals("GuiaRemision")) {

			GuiaRemision guiaRemision = new GuiaRemision();

			GuiaRemision.InfoAdicional infoAdicional = generarInformacionAdicionalGuiaRemision(egreso);
			guiaRemision.setInfoTributaria(this.cargarInfoTributaria(egreso));
			guiaRemision.setInfoGuiaRemision(this.cargarInfoGuiaRemision(egreso, totalDocus));

			destinatarios.getDestinatario().add(destinatario);
			guiaRemision.setDestinatarios(destinatarios);

			if (facturaDetalles != null) {
				// guiaRemision.setDetalles(guiaRemisionDetalles);
			}
			if (infoAdicional.getCampoAdicional().size() > 0) {
				guiaRemision.setInfoAdicional(infoAdicional);
			}

			guiaRemision.setVersion("1.1.0");
			guiaRemision.setId("comprobante");

			object = guiaRemision;
		}

		return object;
	}

	@Override
	public Object generarComprobanteElectronico(Ingreso ingreso, List<IngrDeta> ingrDetas,
			FormPagoMoviIngr formPagoMoviIngr, Retencion retencion, String codigoReteIva, String codigoReteRenta,
			String codigoReteIsd, String codigoIva, String codigoIce, String codigoIrbpnr) {

		List<ec.com.tecnointel.soem.ingreso.modelo.TotalDocu> totalDocus = this.calcularTotalDocu(ingreso, ingrDetas);

		ComprobanteRetencion comprobanteRetencion = new ComprobanteRetencion();

		ComprobanteRetencion.InfoAdicional infoAdicional = generarInformacionAdicionalComprobanteRetencion(ingreso);
		comprobanteRetencion.setInfoTributaria(this.cargarInfoTributaria(ingreso, retencion));
		comprobanteRetencion.setInfoCompRetencion(this.cargarInfoComprobanteRetencion(ingreso));

		comprobanteRetencion.setDocsSustento(this.cargarDocSustento(ingreso, totalDocus, retencion, codigoReteIva,
				codigoReteRenta, codigoReteIsd, codigoIva, codigoIce, codigoIrbpnr, formPagoMoviIngr));

		if (infoAdicional.getCampoAdicional().size() > 0) {
			comprobanteRetencion.setInfoAdicional(infoAdicional);
		}

		comprobanteRetencion.setVersion("2.0.0");
		comprobanteRetencion.setId("comprobante");

		return comprobanteRetencion;
	}

	@Override
	public Object generarComprobanteElectronicoLiquidacion(Ingreso ingreso, List<IngrDeta> ingrDetas,
			FormPagoMoviIngr formPagoMoviIngr, String codigoReteIva, String codigoReteRenta, String codigoReteIsd,
			String codigoIva, String codigoIce, String codigoIrbpnr) {

//		Este codigo es una parte de calcularTotalDocu
		BigDecimal totalBrutoDeta = new BigDecimal(0);
		BigDecimal descueDeta = new BigDecimal(0);
		BigDecimal totalNetoDeta = new BigDecimal(0);
//		Fin Este codigo es una parte de calcularTotalDocu

		Detalles detalles = new Detalles();

		for (IngrDeta ingrDeta : ingrDetas) {

			totalBrutoDeta = ingrDeta.getCantid().multiply(ingrDeta.getCosto());
			descueDeta = totalBrutoDeta.multiply(ingrDeta.getDescue()).divide(new BigDecimal(100));
			totalNetoDeta = totalBrutoDeta.subtract(descueDeta).subtract(ingrDeta.getDescueValo());
			ingrDeta.setTotal(totalNetoDeta);

			ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.LiquidacionCompra.Detalles.Detalle detalle = new ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.LiquidacionCompra.Detalles.Detalle();

			detalle.setCodigoPrincipal(ingrDeta.getProducto().getCodigoBarra());
			detalle.setCodigoAuxiliar(ingrDeta.getProducto().getCodigo());
			detalle.setDescripcion(ingrDeta.getProducto().getDescri());
//				Opcional
//				detalle.setUnidadMedida(null);
			detalle.setCantidad(ingrDeta.getCantid().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
			detalle.setPrecioUnitario(ingrDeta.getCosto().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
//				Opcional
// 				Descuento en valor no en porcentaje
			detalle.setDescuento(descueDeta.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
			detalle.setPrecioTotalSinImpuesto(ingrDeta.getTotal().setScale(2, RoundingMode.HALF_UP));

			detalles.getDetalle().add(detalle);

			// Calcula impuestos de acuerdo al tipo de documento seleccionado
			if (ingreso.getDocuIngr().isGeneraImpu()) {

				// Recorrido de impuestos y retenciones grabados en cada producto
				for (IngrDetaImpu ingrDetaImpu : ingrDeta.getIngrDetaImpus()) {

//					Pregunta solo los impuestos que son iva ya que en egreDetaImpu puede haber retenciones
					if (ingrDetaImpu.getTipo().equals("IVA")) {
						ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.LiquidacionCompra.Detalles.Detalle.Impuestos impuestos = new ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.LiquidacionCompra.Detalles.Detalle.Impuestos();
						ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.Impuesto impuesto = new ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.Impuesto();

						impuesto.setCodigo(codigoIva);
						impuesto.setBaseImponible(ingrDeta.getTotal().setScale(2, RoundingMode.HALF_UP));
						impuesto.setValor(
								ingrDeta.getTotal().multiply(ingrDetaImpu.getPorcen().divide(new BigDecimal(100)))
										.setScale(2, RoundingMode.HALF_UP));
						impuesto.setCodigoPorcentaje(ingrDetaImpu.getCodigo());
						impuesto.setTarifa(
								ingrDetaImpu.getPorcen().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());

						impuestos.getImpuesto().add(impuesto);
						detalle.setImpuestos(impuestos);
					}
				}
			}
		}

		List<ec.com.tecnointel.soem.ingreso.modelo.TotalDocu> totalDocus = this.calcularTotalDocu(ingreso, ingrDetas);

		LiquidacionCompra liquidacionCompra = new LiquidacionCompra();

		InfoAdicional infoAdicional = generarInformacionAdicionalLiquidacion(ingreso);
		liquidacionCompra.setInfoTributaria(this.cargarInfoTributaria(ingreso));
		liquidacionCompra
				.setInfoLiquidacionCompra(this.cargarInfoLiquidacion(ingreso, codigoIva, totalDocus, formPagoMoviIngr));

		if (detalles != null) {
			liquidacionCompra.setDetalles(detalles);
		}

		if (infoAdicional.getCampoAdicional().size() > 0) {
			liquidacionCompra.setInfoAdicional(infoAdicional);
		}

		liquidacionCompra.setVersion("1.0.0");
		liquidacionCompra.setId("comprobante");

		return liquidacionCompra;
	}

	public List<ec.com.tecnointel.soem.ingreso.modelo.TotalDocu> calcularTotalDocu(Ingreso ingreso,
			List<IngrDeta> ingrDetas) {

// 		Calculo de totales es una copia de compraControl
		BigDecimal totalBrutoCabe = new BigDecimal(0);
		BigDecimal totalBrutoDeta = new BigDecimal(0);
		BigDecimal descueCabe = new BigDecimal(0);
		BigDecimal descueDeta = new BigDecimal(0);
		BigDecimal descueTotal = new BigDecimal(0);
		BigDecimal totalNetoDeta = new BigDecimal(0);
		BigDecimal totalNetoDetaTmp = new BigDecimal(0);
		BigDecimal impuestoDeta = new BigDecimal(0);
		BigDecimal impuestoDetaAcum = new BigDecimal(0);
		BigDecimal totalDeta = new BigDecimal(0);
		BigDecimal totalCabe = new BigDecimal(0);
		BigDecimal descueCabePorc = new BigDecimal(0);
		BigDecimal ivaIceIva = new BigDecimal(0);
		BigDecimal ivaIceRete = new BigDecimal(0);

		BigDecimal totalPrecioConImpu = new BigDecimal(0);

		List<ec.com.tecnointel.soem.ingreso.modelo.TotalDocu> totalDocus = new ArrayList<>();
		// Se inicializa esta variable ya que sino guarda el total anterior y acumula
		// demasiado
		totalPrecioConImpu = BigDecimal.ZERO;

		// Se recupera el descuento que tiene en la cabecera para poder realizar los
		// calculos respectivos
		if (ingreso.getDescue() != null) {
			descueCabePorc = ingreso.getDescue();
		}

		totalDocus.clear();

//		Para controlar esto se crea un producto con solo impuesto ICE y un Producto solo
//		con impuesto IRBPNR y en este for se calcula el iva del ice y se le acumula en el
//		iva generado en la factura
		for (IngrDeta ingrDeta : ingrDetas) {

			if (ingreso.getDocuIngr().isGeneraImpu()) {

				for (IngrDetaImpu ingrDetaImpu : ingrDeta.getIngrDetaImpus()) {

//					Calcula el iva del ICE
					if (ingrDetaImpu.getTipo().equals("ICE")) {

						ivaIceIva = ingrDeta.getCosto().multiply(this.dimmIva.getPorcen()).divide(new BigDecimal(100),
								6, RoundingMode.HALF_UP);
						ivaIceIva = ivaIceIva.setScale(6, RoundingMode.HALF_UP);
						ivaIceRete = ingrDeta.getCosto().multiply(this.dimmIva.getPorcen()).divide(new BigDecimal(100),
								6, RoundingMode.HALF_UP);
						ivaIceRete = ivaIceRete.setScale(6, RoundingMode.HALF_UP);
						break;
					}
//					Fin Calcula el iva del ICE
				}
			}
		}

		for (IngrDeta ingrDeta : ingrDetas) {

			impuestoDetaAcum = new BigDecimal(0);

			totalBrutoDeta = ingrDeta.getCantid().multiply(ingrDeta.getCosto());
			descueDeta = totalBrutoDeta.multiply(ingrDeta.getDescue()).divide(new BigDecimal(100));
			totalNetoDeta = totalBrutoDeta.subtract(descueDeta).subtract(ingrDeta.getDescueValo());
			ingrDeta.setTotal(totalNetoDeta);

			// if (ingrDeta.getIngreso() != null) {
			descueCabe = totalNetoDeta.multiply(descueCabePorc).divide(new BigDecimal(100));
			totalNetoDeta = totalNetoDeta.subtract(descueCabe);
			// }
			// Se grabo en un campo aparte para poder utilizarlo para el calculo del costo
			// promedio ya que si se coloca descuento kardTotaView agrupa por descuento
			// y duplica la fila
			ingrDeta.setCostoNeto(totalNetoDeta.divide(ingrDeta.getCantid(), 6, RoundingMode.HALF_UP));

			// Se guarda en una variable temporal para el calculo de retenciones de Iva
			totalNetoDetaTmp = totalNetoDeta;

			// Calcula impuestos de acuerdo al tipo de documento seleccionado
			if (ingreso.getDocuIngr().isGeneraImpu()) {

				// Recorrido de impuestos y retenciones grabados en cada producto
				for (IngrDetaImpu ingrDetaImpu : ingrDeta.getIngrDetaImpus()) {

					BigDecimal factor = new BigDecimal(ingrDetaImpu.getFactor());

					// No calcula retenciones en caso de ser nota de credito y si el factor de
					// impuesto es menor que 1
					if (ingreso.getDocuIngr().getDocumento().getFactor() <= 0
							&& factor.compareTo(BigDecimal.ZERO) <= 0) {
						continue;
					}

					// En caso de retencion Iva se cambia la base del calculo
					// la variable totalNetoDeta temporalmente almacena el valor del IVA
					// El valor "RI" se graba cuando se crea ingreDetaImpu (crearIngrDetaImpu())
					if (ingrDetaImpu.getTipo().equals("RI")) {

						ProdDimm prodDimmBuscar = new ProdDimm(ingrDeta.getProducto(), new Dimm());
						ProdDimm prodDimmIva = buscarProdDimmIva(prodDimmBuscar);

						// Se cambia el valor del totalNetoDeta por el valor del IVA
//						totalNetoDeta = totalNetoDeta.multiply(this.dimmIva.getPorcen()).divide(new BigDecimal(100));
						totalNetoDeta = totalNetoDeta.multiply(prodDimmIva.getDimm().getPorcen())
								.divide(new BigDecimal(100));
//						Se acumula iva del ice al subtotal Retencion Iva
						totalNetoDeta = totalNetoDeta.add(ivaIceRete);
//						Si no se coloca esta variable en cero acumula el ice para todos los productos en la factura 
						ivaIceRete = BigDecimal.ZERO;
						// totalNetoDeta = totalNetoDeta.setScale(2, BigDecimal.ROUND_HALF_UP);

					} else {
						// Se regresa el valor del totalLNetoDeta a su valor original
						// Para que no afecte en el proximo calculo
						totalNetoDeta = totalNetoDetaTmp;
					}

					totalNetoDeta = totalNetoDeta.setScale(6, RoundingMode.HALF_UP);

//					Se hace esto para que no calcule impuesto por ICE, ya que solo se necesita calcular 
//					el iva sobre el valor ingresado en costo
					if (ingrDetaImpu.getTipo().equals("ICE")) {
						impuestoDeta = BigDecimal.ZERO;
					} else if (ingrDetaImpu.getTipo().equals("IVA")) {
						impuestoDeta = (totalNetoDeta.multiply(ingrDetaImpu.getPorcen()).divide(new BigDecimal(100)));
//						Se acumula iva del ice al Iva						
						impuestoDeta = impuestoDeta.add(ivaIceIva);
//						Si no se coloca esta variable en cero acumula el ice para todos los productos en la factura						
						ivaIceIva = BigDecimal.ZERO;
					} else {
						impuestoDeta = (totalNetoDeta.multiply(ingrDetaImpu.getPorcen()).divide(new BigDecimal(100)));
					}
//					Fin

					impuestoDeta = impuestoDeta.multiply(factor);
					impuestoDeta = impuestoDeta.setScale(6, RoundingMode.HALF_UP);

					// Acumula impuesto solo para calculo del total del documento
					// cuando un producto esta grabado con mas de un impuesto
					impuestoDetaAcum = impuestoDetaAcum.add(impuestoDeta);

					// Crear Key para treeMap para separar y acumular impuestos y subtotal de cada
					// impuesto
					String claveImpuesto[] = ingrDetaImpu.getDescri().split(";");
					String claveSubtotal = "Subtotal " + claveImpuesto[1];
					// String Clave = "Subtotal " + ingrDetaImpu.getDescri();

					// Si Key No exite almacena impuestos y retenciones de acuerdo a lo que tenga
					// grabado el producto
					// Si Key existe acumula impuestos y retenciones de acuerdo a lo que tenga
					// grabado el producto
					ec.com.tecnointel.soem.ingreso.modelo.TotalDocu totalDocu1 = new ec.com.tecnointel.soem.ingreso.modelo.TotalDocu();
					totalDocu1.setDescri(claveSubtotal);
					int indice1 = totalDocus.indexOf(totalDocu1);

					ec.com.tecnointel.soem.ingreso.modelo.TotalDocu totalDocu2 = new ec.com.tecnointel.soem.ingreso.modelo.TotalDocu();
					totalDocu2.setDescri(claveImpuesto[1]);
					int indice2 = totalDocus.indexOf(totalDocu2);

					if (indice1 == -1) {

						totalDocu1.setCodigo(ingrDetaImpu.getCodigo());
						totalDocu1.setDescri(claveSubtotal);
						totalDocu1.setPorcen(ingrDetaImpu.getPorcen());
						totalDocu1.setValor(totalNetoDeta);
						totalDocu1.setTipoImpu(ingrDetaImpu.getTipo());
						totalDocus.add(totalDocu1);

						// TotalDocu totalDocu2 = new TotalDocu();
						// totalDocu.setCodigo(ingrDetaImpu.getCodigo());
						totalDocu2.setDescri(claveImpuesto[1]);
						// totalDocu.setPorcen(ingrDetaImpu.getPorcen());
						totalDocu2.setValor(impuestoDeta);
						totalDocus.add(totalDocu2);

					} else {

						BigDecimal total = new BigDecimal(0);
						ec.com.tecnointel.soem.ingreso.modelo.TotalDocu totalDocu3 = totalDocus.get(indice1);
						total = totalDocu3.getValor();
						totalDocu3.setValor(total.add(totalNetoDeta));

						BigDecimal total1 = new BigDecimal(0);
						ec.com.tecnointel.soem.ingreso.modelo.TotalDocu totalDocu4 = totalDocus.get(indice2);
						total1 = totalDocu4.getValor();
						totalDocu4.setValor(total1.add(impuestoDeta));
					}
				}
			}

			// Se regresa el valor del totalNetoDeta a su valor original
			// Para que no afecte en el proximo calculo
			totalNetoDeta = totalNetoDetaTmp;

			totalDeta = totalNetoDeta.add(impuestoDetaAcum);
			totalBrutoCabe = totalBrutoCabe.add(totalBrutoDeta);
			descueTotal = descueTotal.add(descueDeta).add(descueCabe).add(ingrDeta.getDescueValo());
			totalCabe = totalCabe.add(totalDeta);

			// Calcular total precio (Erotik)
//			ingrDeta.setTotalPrecioConImpu(
//					(ingrDeta.getCantid().multiply(ingrDeta.getPrecioConImpu())).setScale(6, BigDecimal.ROUND_HALF_UP));
//			totalPrecioConImpu = totalPrecioConImpu.add(ingrDeta.getTotalPrecioConImpu());
		}

		ec.com.tecnointel.soem.ingreso.modelo.TotalDocu totalDocu = new ec.com.tecnointel.soem.ingreso.modelo.TotalDocu();
		totalDocu.setDescri("Total Bruto");
		totalDocu.setValor(totalBrutoCabe.setScale(6, RoundingMode.HALF_UP));
		totalDocus.add(totalDocu);

		totalDocu = new ec.com.tecnointel.soem.ingreso.modelo.TotalDocu();
		totalDocu.setDescri("Total Descuento");
		totalDocu.setValor(descueTotal.setScale(6, RoundingMode.HALF_UP));
		totalDocus.add(totalDocu);

		totalDocu = new ec.com.tecnointel.soem.ingreso.modelo.TotalDocu();
		totalDocu.setDescri("Total Neto");
		totalDocu.setValor(totalBrutoCabe.subtract(descueTotal));
		totalDocus.add(totalDocu);

		totalDocu = new ec.com.tecnointel.soem.ingreso.modelo.TotalDocu();
		totalDocu.setDescri("Total Documento");
		totalDocu.setValor(totalCabe.setScale(6, RoundingMode.HALF_UP));
		totalDocus.add(totalDocu);
// 		Fin Calculo de totales es una copia de compraControl

		return totalDocus;
	}

//	Buscar Iva del producto
	public ProdDimm buscarProdDimmIva(ProdDimm prodDimm) {

		ProdDimm prodDimmIva = new ProdDimm();

		List<ProdDimm> prodDimms = buscarProdDimm(prodDimm);

		for (ProdDimm prodDimmRecorrer : prodDimms) {
			if (prodDimmRecorrer.getDimm().getTablaRefe().equals("Tabla12")) {
				prodDimmIva = prodDimmRecorrer;
			}
		}

		return prodDimmIva;
	}

	public List<ProdDimm> buscarProdDimm(ProdDimm prodDimm) {

		List<ProdDimm> prodDimms = new ArrayList<>();
		try {
			prodDimms = prodDimmLista.buscar(prodDimm, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return prodDimms;
	}

	// Se llama solo desde aqui no hay que usar @Override
	private Factura.InfoAdicional generarInformacionAdicionalFactura(Egreso egreso) {

		// Direccion, Correo son los que estan en el sistema de facturacion del
		// SRI, Se podria aumentar telefono
		Factura.InfoAdicional infoAdicional = new Factura.InfoAdicional();

		if ((egreso.getPersClie().getPersona().getDirecc() != null)
				&& (!egreso.getPersClie().getPersona().getDirecc().isEmpty())) {

			Factura.InfoAdicional.CampoAdicional campoAdicional = new Factura.InfoAdicional.CampoAdicional();
			campoAdicional.setNombre("Direccion");
			campoAdicional.setValue(egreso.getPersClie().getPersona().getDirecc());
			infoAdicional.getCampoAdicional().add(campoAdicional);

		}

		if ((egreso.getPersClie().getPersona().getCorreo() != null)
				&& (!egreso.getPersClie().getPersona().getCorreo().isEmpty())) {

			Factura.InfoAdicional.CampoAdicional campoAdicional = new Factura.InfoAdicional.CampoAdicional();
			campoAdicional.setNombre("Email");
			campoAdicional.setValue(egreso.getPersClie().getPersona().getCorreo());
			infoAdicional.getCampoAdicional().add(campoAdicional);
		}

//		Estas Leyendas sale dependiendo del tipo de contrinuyente
//		y si en parametros (3172) esta activado estos mensajes 
		if (!this.msgInfoAdicional.equals("nulo")) {
			Factura.InfoAdicional.CampoAdicional campoAdicional = new Factura.InfoAdicional.CampoAdicional();
			campoAdicional.setNombre(RUC_PROVEEDOR_SISTEMA);
			campoAdicional.setValue(this.msgInfoAdicional);
			infoAdicional.getCampoAdicional().add(campoAdicional);
		}

//		Agrega información adicional de la tabla egreNota
		for (EgreNota egreNota : egreso.getEgreNotas()) {
			Factura.InfoAdicional.CampoAdicional campoAdicional = new Factura.InfoAdicional.CampoAdicional();
			campoAdicional.setNombre(egreNota.getCodigo());
			campoAdicional.setValue(egreNota.getDescri());
			infoAdicional.getCampoAdicional().add(campoAdicional);
		}
//
//		if (!this.leyenda2.equals("nulo")) {
//			Factura.InfoAdicional.CampoAdicional campoAdicional = new Factura.InfoAdicional.CampoAdicional();
//			campoAdicional.setNombre("Agente de Retención");
//			campoAdicional.setValue(this.leyenda2);
//			infoAdicional.getCampoAdicional().add(campoAdicional);
//		}

		return infoAdicional;
	}

	private NotaCredito.InfoAdicional generarInformacionAdicionalNotaCredito(Egreso egreso) {

		// Direcci�n, Correo son los que estan en el sistema de facturacion del
		// SRI
		// Se podria aumentar telefono
		NotaCredito.InfoAdicional infoAdicional = new NotaCredito.InfoAdicional();

		if ((egreso.getPersClie().getPersona().getDirecc() != null)
				&& (!egreso.getPersClie().getPersona().getDirecc().isEmpty())) {

			NotaCredito.InfoAdicional.CampoAdicional campoAdicional = new NotaCredito.InfoAdicional.CampoAdicional();
			campoAdicional.setNombre("Direccion");
			campoAdicional.setValue(egreso.getPersClie().getPersona().getDirecc());
			infoAdicional.getCampoAdicional().add(campoAdicional);

		}

		if ((egreso.getPersClie().getPersona().getCorreo() != null)
				&& (!egreso.getPersClie().getPersona().getCorreo().isEmpty())) {

			NotaCredito.InfoAdicional.CampoAdicional campoAdicional = new NotaCredito.InfoAdicional.CampoAdicional();
			campoAdicional.setNombre("Email");
			campoAdicional.setValue(egreso.getPersClie().getPersona().getCorreo());
			infoAdicional.getCampoAdicional().add(campoAdicional);
		}

//		Estas Leyendas sale dependiendo del tipo de contrinuyente
//		y si en parametros (3172) esta activado estos mensajes 
		if (!this.msgInfoAdicional.equals("nulo")) {
			NotaCredito.InfoAdicional.CampoAdicional campoAdicional = new NotaCredito.InfoAdicional.CampoAdicional();
			campoAdicional.setNombre(RUC_PROVEEDOR_SISTEMA);
			campoAdicional.setValue(this.msgInfoAdicional);
			infoAdicional.getCampoAdicional().add(campoAdicional);
		}

//		Agrega información adicional de la tabla egreNota
		for (EgreNota egreNota : egreso.getEgreNotas()) {
			NotaCredito.InfoAdicional.CampoAdicional campoAdicional = new NotaCredito.InfoAdicional.CampoAdicional();
			campoAdicional.setNombre(egreNota.getCodigo());
			campoAdicional.setValue(egreNota.getDescri());
			infoAdicional.getCampoAdicional().add(campoAdicional);
		}

//		if (!this.leyenda2.equals("nulo")) {
//			NotaCredito.InfoAdicional.CampoAdicional campoAdicional = new NotaCredito.InfoAdicional.CampoAdicional();
//			campoAdicional.setNombre("Agente de Retención");
//			campoAdicional.setValue(this.leyenda2);
//			infoAdicional.getCampoAdicional().add(campoAdicional);
//		}

		return infoAdicional;
	}

	private NotaDebito.InfoAdicional generarInformacionAdicionalNotaDebito(Egreso egreso) {

		// Direcci�n, Correo son los que estan en el sistema de facturacion del
		// SRI
		// Se podria aumentar telefono
		NotaDebito.InfoAdicional infoAdicional = new NotaDebito.InfoAdicional();

		if ((egreso.getPersClie().getPersona().getDirecc() != null)
				&& (!egreso.getPersClie().getPersona().getDirecc().isEmpty())) {

			NotaDebito.InfoAdicional.CampoAdicional campoAdicional = new NotaDebito.InfoAdicional.CampoAdicional();
			campoAdicional.setNombre("Direccion");
			campoAdicional.setValue(egreso.getPersClie().getPersona().getDirecc());
			infoAdicional.getCampoAdicional().add(campoAdicional);

		}

		if ((egreso.getPersClie().getPersona().getCorreo() != null)
				&& (!egreso.getPersClie().getPersona().getCorreo().isEmpty())) {

			NotaDebito.InfoAdicional.CampoAdicional campoAdicional = new NotaDebito.InfoAdicional.CampoAdicional();
			campoAdicional = new NotaDebito.InfoAdicional.CampoAdicional();
			campoAdicional.setNombre("Email");
			campoAdicional.setValue(egreso.getPersClie().getPersona().getCorreo());
			infoAdicional.getCampoAdicional().add(campoAdicional);
		}

//		Estas Leyendas sale dependiendo del tipo de contrinuyente
//		y si en parametros (3172) esta activado estos mensajes 
		if (!this.msgInfoAdicional.equals("nulo")) {
			NotaDebito.InfoAdicional.CampoAdicional campoAdicional = new NotaDebito.InfoAdicional.CampoAdicional();
			campoAdicional.setNombre(RUC_PROVEEDOR_SISTEMA);
			campoAdicional.setValue(this.msgInfoAdicional);
			infoAdicional.getCampoAdicional().add(campoAdicional);
		}

//		Agrega información adicional de la tabla egreNota
		for (EgreNota egreNota : egreso.getEgreNotas()) {
			NotaDebito.InfoAdicional.CampoAdicional campoAdicional = new NotaDebito.InfoAdicional.CampoAdicional();
			campoAdicional.setNombre(egreNota.getCodigo());
			campoAdicional.setValue(egreNota.getDescri());
			infoAdicional.getCampoAdicional().add(campoAdicional);
		}

//		if (!this.leyenda2.equals("nulo")) {
//			NotaDebito.InfoAdicional.CampoAdicional campoAdicional = new NotaDebito.InfoAdicional.CampoAdicional();
//			campoAdicional.setNombre("Agente de Retención");
//			campoAdicional.setValue(this.leyenda2);
//			infoAdicional.getCampoAdicional().add(campoAdicional);
//		}

		return infoAdicional;
	}

	private GuiaRemision.InfoAdicional generarInformacionAdicionalGuiaRemision(Egreso egreso) {

		// Direcci�n, Correo son los que estan en el sistema de facturacion del
		// SRI
		// Se podria aumentar telefono
		GuiaRemision.InfoAdicional infoAdicional = new GuiaRemision.InfoAdicional();

		if ((egreso.getPersClie().getPersona().getDirecc() != null)
				&& (!egreso.getPersClie().getPersona().getDirecc().isEmpty())) {

			GuiaRemision.InfoAdicional.CampoAdicional campoAdicional = new GuiaRemision.InfoAdicional.CampoAdicional();
			campoAdicional.setNombre("Direccion");
			campoAdicional.setValue(egreso.getPersClie().getPersona().getDirecc());
			infoAdicional.getCampoAdicional().add(campoAdicional);

		}

		if ((egreso.getPersClie().getPersona().getCorreo() != null)
				&& (!egreso.getPersClie().getPersona().getCorreo().isEmpty())) {

			GuiaRemision.InfoAdicional.CampoAdicional campoAdicional = new GuiaRemision.InfoAdicional.CampoAdicional();
			campoAdicional.setNombre("Email");
			campoAdicional.setValue(egreso.getPersClie().getPersona().getCorreo());
			infoAdicional.getCampoAdicional().add(campoAdicional);
		}

//		Estas Leyendas sale dependiendo del tipo de contrinuyente
//		y si en parametros (3172) esta activado estos mensajes 
		if (!this.msgInfoAdicional.equals("nulo")) {
			GuiaRemision.InfoAdicional.CampoAdicional campoAdicional = new GuiaRemision.InfoAdicional.CampoAdicional();
			campoAdicional.setNombre(RUC_PROVEEDOR_SISTEMA);
			campoAdicional.setValue(this.msgInfoAdicional);
			infoAdicional.getCampoAdicional().add(campoAdicional);
		}
//
//		if (!this.leyenda2.equals("nulo")) {
//			GuiaRemision.InfoAdicional.CampoAdicional campoAdicional = new GuiaRemision.InfoAdicional.CampoAdicional();
//			campoAdicional.setNombre("Agente de Retención");
//			campoAdicional.setValue(this.leyenda2);
//			infoAdicional.getCampoAdicional().add(campoAdicional);
//		}

		return infoAdicional;
	}

	// Se llama solo desde aqui no hay que usar @Override
	private InfoAdicional generarInformacionAdicionalLiquidacion(Ingreso ingreso) {

		// Direccion, Correo son los que estan en el sistema de facturacion del
		// SRI, Se podria aumentar telefono
//		Factura.InfoAdicional infoAdicional = new Factura.InfoAdicional();
		InfoAdicional infoAdicional = new InfoAdicional();

		if ((ingreso.getPersProv().getPersona().getDirecc() != null)
				&& (!ingreso.getPersProv().getPersona().getDirecc().isEmpty())) {

			CampoAdicional campoAdicional = new CampoAdicional();
			campoAdicional.setNombre("Direccion");
			campoAdicional.setValue(ingreso.getPersProv().getPersona().getDirecc());
			infoAdicional.getCampoAdicional().add(campoAdicional);

		}

		if ((ingreso.getPersProv().getPersona().getCorreo() != null)
				&& (!ingreso.getPersProv().getPersona().getCorreo().isEmpty())) {

			CampoAdicional campoAdicional = new CampoAdicional();
			campoAdicional.setNombre("Email");
			campoAdicional.setValue(ingreso.getPersProv().getPersona().getCorreo());
			infoAdicional.getCampoAdicional().add(campoAdicional);
		}

//		Estas Leyendas sale dependiendo del tipo de contrinuyente
//		y si en parametros (3172) esta activado estos mensajes 
		if (!this.msgInfoAdicional.equals("nulo")) {
			CampoAdicional campoAdicional = new CampoAdicional();
			campoAdicional.setNombre(RUC_PROVEEDOR_SISTEMA);
			campoAdicional.setValue(this.msgInfoAdicional);
			infoAdicional.getCampoAdicional().add(campoAdicional);
		}

		return infoAdicional;
	}

	private ComprobanteRetencion.InfoAdicional generarInformacionAdicionalComprobanteRetencion(Ingreso ingreso) {

		// Direcci�n, Correo son los que estan en el sistema de facturacion del
		// SRI
		// Se podria aumentar telefono
		ComprobanteRetencion.InfoAdicional infoAdicional = new ComprobanteRetencion.InfoAdicional();

		if ((ingreso.getPersProv().getPersona().getDirecc() != null)
				&& (!ingreso.getPersProv().getPersona().getDirecc().isEmpty())) {

			ComprobanteRetencion.InfoAdicional.CampoAdicional campoAdicional = new ComprobanteRetencion.InfoAdicional.CampoAdicional();
			campoAdicional.setNombre("Direccion");
			campoAdicional.setValue(ingreso.getPersProv().getPersona().getDirecc());
			infoAdicional.getCampoAdicional().add(campoAdicional);

		}

		if ((ingreso.getPersProv().getPersona().getCorreo() != null)
				&& (!ingreso.getPersProv().getPersona().getCorreo().isEmpty())) {

			ComprobanteRetencion.InfoAdicional.CampoAdicional campoAdicional = new ComprobanteRetencion.InfoAdicional.CampoAdicional();
			campoAdicional.setNombre("Email");
			campoAdicional.setValue(ingreso.getPersProv().getPersona().getCorreo());
			infoAdicional.getCampoAdicional().add(campoAdicional);
		}

//		Estas Leyendas sale dependiendo del tipo de contrinuyente
//		y si en parametros (3172) esta activado estos mensajes 
		if (!this.msgInfoAdicional.equals("nulo")) {
			ComprobanteRetencion.InfoAdicional.CampoAdicional campoAdicional = new ComprobanteRetencion.InfoAdicional.CampoAdicional();
			campoAdicional.setNombre(RUC_PROVEEDOR_SISTEMA);
			campoAdicional.setValue(this.msgInfoAdicional);
			infoAdicional.getCampoAdicional().add(campoAdicional);
		}
//
//		if (!this.leyenda2.equals("nulo")) {
//			ComprobanteRetencion.InfoAdicional.CampoAdicional campoAdicional = new ComprobanteRetencion.InfoAdicional.CampoAdicional();
//			campoAdicional.setNombre("Agente de Retención");
//			campoAdicional.setValue(this.leyenda2);
//			infoAdicional.getCampoAdicional().add(campoAdicional);
//		}

		return infoAdicional;
	}

	public InfoTributaria cargarInfoTributaria(Egreso egreso) {

		String numero = null;

		InfoTributaria infoTributaria = new InfoTributaria();

		try (Formatter formatter = new Formatter()) {
			numero = formatter.format("%09d", egreso.getNumero()).toString();
		}

		infoTributaria = cargarInfoTributaria(egreso.getSucursal(), egreso.getDocuEgre().getDimm().getCodigo(),
				egreso.getSerie1(), egreso.getSerie2(), numero, egreso.getDocuEgre().getAmbien(),
				egreso.getDocuEgre().getTipoEmis(), egreso.getClaveAcce());

		return infoTributaria;
	}

	public InfoTributaria cargarInfoTributaria(Ingreso ingreso, Retencion retencion) {

		String numero = null;

		InfoTributaria infoTributaria = new InfoTributaria();

		try (Formatter formatter = new Formatter()) {
			numero = formatter.format("%09d", retencion.getNumero()).toString();
		}

		infoTributaria = cargarInfoTributaria(ingreso.getSucursal(), ingreso.getDocuIngr().getCodigoTipoCompRete(),
				retencion.getSerie1(), retencion.getSerie2(), numero, ingreso.getDocuIngr().getAmbien(),
				ingreso.getDocuIngr().getTipoEmis(), retencion.getClaveAcce());

		return infoTributaria;
	}

	public InfoTributaria cargarInfoTributaria(Ingreso ingreso) {

		String numero = null;

		InfoTributaria infoTributaria = new InfoTributaria();

		try (Formatter formatter = new Formatter()) {
			numero = formatter.format("%09d", ingreso.getNumero()).toString();
		}

		infoTributaria = cargarInfoTributaria(ingreso.getSucursal(), ingreso.getDocuIngr().getDimm().getCodigo(),
				ingreso.getSerie1(), ingreso.getSerie2(), numero, ingreso.getDocuIngr().getAmbien(),
				ingreso.getDocuIngr().getTipoEmis(), ingreso.getClaveAcce());

		return infoTributaria;
	}

	public InfoFactura cargarInfoFactura(Egreso egreso, String codigoIva, String codigoIce, String codigoIrbpnr,
			List<TotalDocu> totalDocus, FpmeFormPago fpmeFormPago) {

		// Consumidor final validar si se pasa de los 200.00
		// infoFactura.setCompensaciones(); esta campo no existe en la ficha
		// t�cnica

		String apellidoNombre = this.apellidoNombre(egreso.getPersClie().getPersona().getApelli(),
				egreso.getPersClie().getPersona().getNombre());

		InfoFactura infoFactura = new InfoFactura();

		if ((egreso.getSucursal().getDireccEsta() != null) && (!egreso.getSucursal().getDireccEsta().isEmpty())) {
			// Direccion de la sucursal no de la matriz la direcci�n de la
			// matriz va en infoTributaria
			infoFactura.setDirEstablecimiento(egreso.getSucursal().getDireccEsta());
		}

		if ((egreso.getSucursal().getContriEspe() != null) && (!egreso.getSucursal().getContriEspe().isEmpty())) {
			infoFactura.setContribuyenteEspecial(egreso.getSucursal().getContriEspe());
		}

		if (egreso.getSucursal().isObligaCont()) {
			infoFactura.setObligadoContabilidad("SI");
		} else {
			infoFactura.setObligadoContabilidad("NO");
		}

		infoFactura.setRazonSocialComprador(apellidoNombre);
		infoFactura.setTipoIdentificacionComprador(egreso.getPersClie().getDimm().getCodigo());
		infoFactura.setIdentificacionComprador(egreso.getPersClie().getPersona().getCedulaRuc());

		if ((egreso.getPersClie().getPersona().getDirecc() != null)
				&& (!egreso.getPersClie().getPersona().getDirecc().isEmpty())) {
			infoFactura.setDireccionComprador(egreso.getPersClie().getPersona().getDirecc());
		}

		infoFactura.setFechaEmision(Util.cambiarFormatoFechaString(egreso.getFechaEmis(), "dd/MM/yyyy"));

		int indice;
		TotalDocu totalDocu = new TotalDocu();

		totalDocu = new TotalDocu();
		totalDocu.setDescri("Total Neto");
		indice = totalDocus.indexOf(totalDocu);
		infoFactura.setTotalSinImpuestos(totalDocus.get(indice).getValor().setScale(2, RoundingMode.HALF_UP));

		totalDocu = new TotalDocu();
		totalDocu.setDescri("Total Descuento");
		indice = totalDocus.indexOf(totalDocu);
		infoFactura.setTotalDescuento(totalDocus.get(indice).getValor().setScale(2, RoundingMode.HALF_UP));

		totalDocu.setDescri("Total Documento");
		indice = totalDocus.indexOf(totalDocu);
		infoFactura.setImporteTotal(totalDocus.get(indice).getValor().setScale(2, RoundingMode.HALF_UP));

		infoFactura.setTotalConImpuestos(generaTotalesImpuestoFactura(codigoIva, codigoIce, codigoIrbpnr, totalDocus));

		infoFactura.setMoneda("DOLAR");
		// Validar que no sea mas del 10% de acuerdo al sistema de facturacion
		// del sri
		infoFactura.setPropina(new BigDecimal("0.00")); // Este valor se la
														// puede poner 0 sin
														// comillas, pero esta
														// como string para que
														// vaya con dos
														// decimales

		infoFactura.setPagos(cargarPagoFactura(fpmeFormPago, totalDocus));

		return infoFactura;

		// ESTOS CAMPOS FALTAN
		// if (Double.parseDouble(this.totalSubsidio.toString()) > 0.0D) {
		// infoFactura.setTotalSubsidio(this.totalSubsidioSinIva);
		// }
		// String guiaRemision = this.idenComprob.textGuia1.getText() + "-" +
		// this.idenComprob.textGuia2.getText() + "-" +
		// this.idenComprob.textGuia3.getText();
		// if (guiaRemision.length() == 17) {
		// infoFactura.setGuiaRemision(value);
		// } else if (guiaRemision.length() != 2) {
		// JOptionPane.showMessageDialog(this,
		// "Gu�a de Remisi�n debe ser de 15 n?meros: ej: 123-123-123456789",
		// "Se ha producido un error ", 0);
		// }

	}

	public InfoNotaCredito cargarInfoNotaCredito(Egreso egreso, String codigoIva, String codigoIce, String codigoIrbpnr,
			List<TotalDocu> totalDocus) throws Exception {

		// Consumidor final validar si se pasa de los 200.00
		// infoFactura.setCompensaciones(); esta campo no existe en la ficha
		// t�cnica

		String apellidoNombre = this.apellidoNombre(egreso.getPersClie().getPersona().getApelli(),
				egreso.getPersClie().getPersona().getNombre());

		InfoNotaCredito infoNotaCredito = new InfoNotaCredito();

		if ((egreso.getSucursal().getDireccEsta() != null) && (!egreso.getSucursal().getDireccEsta().isEmpty())) {
			// Direccion de la sucursal no de la matriz la direcci�n de la
			// matriz va en infoTributaria
			infoNotaCredito.setDirEstablecimiento(egreso.getSucursal().getDireccEsta());
		}

		if ((egreso.getSucursal().getContriEspe() != null) && (!egreso.getSucursal().getContriEspe().isEmpty())) {
			infoNotaCredito.setContribuyenteEspecial(egreso.getSucursal().getContriEspe());
		}

		if (egreso.getSucursal().isObligaCont()) {
			infoNotaCredito.setObligadoContabilidad("SI");
		} else {
			infoNotaCredito.setObligadoContabilidad("NO");
		}

		infoNotaCredito.setRazonSocialComprador(apellidoNombre);
		infoNotaCredito.setTipoIdentificacionComprador(egreso.getPersClie().getDimm().getCodigo());
		infoNotaCredito.setIdentificacionComprador(egreso.getPersClie().getPersona().getCedulaRuc());

		infoNotaCredito.setFechaEmision(Util.cambiarFormatoFechaString(egreso.getFechaEmis(), "dd/MM/yyyy"));

		int indice;
		TotalDocu totalDocu = new TotalDocu();

		totalDocu = new TotalDocu();
		totalDocu.setDescri("Total Neto");
		indice = totalDocus.indexOf(totalDocu);
		infoNotaCredito.setTotalSinImpuestos(totalDocus.get(indice).getValor().setScale(2, RoundingMode.HALF_UP));

		totalDocu = new TotalDocu();
		totalDocu.setDescri("Total Documento");
		indice = totalDocus.indexOf(totalDocu);
		infoNotaCredito.setValorModificacion(totalDocus.get(indice).getValor().setScale(2, RoundingMode.HALF_UP));

		try (Formatter formatter = new Formatter()) {
			String numDocModificado = egreso.getEgreso().getSerie1() + "-" + egreso.getEgreso().getSerie2() + "-"
					+ formatter.format("%09d", egreso.getEgreso().getNumero());

			Egreso egresoModificado = egresoRegis.buscarPorId(Egreso.class, egreso.getEgreso().getEgresoId());
			infoNotaCredito.setCodDocModificado(egresoModificado.getDocuEgre().getDimm().getCodigo());

			infoNotaCredito.setNumDocModificado(numDocModificado);
		}

		infoNotaCredito.setFechaEmisionDocSustento(Util.cambiarFormatoFechaString(egreso.getFechaEmis(), "dd/MM/yyyy"));
		infoNotaCredito.setMotivo(egreso.getNota());

		infoNotaCredito
				.setTotalConImpuestos(generaTotalesImpuestoNotaCredito(codigoIva, codigoIce, codigoIrbpnr, totalDocus));
		infoNotaCredito.setMotivo(egreso.getNota());
		infoNotaCredito.setMoneda("DOLAR");

		return infoNotaCredito;

	}

	public InfoNotaDebito cargarInfoNotaDebito(Egreso egreso, List<TotalDocu> totalDocus, FpmeFormPago fpmeFormPago)
			throws Exception {

		// infoFactura.setCompensaciones(); esta campo no existe en la ficha
		// t�cnica

		String apellidoNombre = this.apellidoNombre(egreso.getPersClie().getPersona().getApelli(),
				egreso.getPersClie().getPersona().getNombre());

		InfoNotaDebito infoNotaDebito = new InfoNotaDebito();

		if ((egreso.getSucursal().getDireccEsta() != null) && (!egreso.getSucursal().getDireccEsta().isEmpty())) {
			// Direccion de la sucursal no de la matriz la direcci�n de la
			// matriz va en infoTributaria
			infoNotaDebito.setDirEstablecimiento(egreso.getSucursal().getDireccEsta());
		}

		if ((egreso.getSucursal().getContriEspe() != null) && (!egreso.getSucursal().getContriEspe().isEmpty())) {
			infoNotaDebito.setContribuyenteEspecial(egreso.getSucursal().getContriEspe());
		}

		if (egreso.getSucursal().isObligaCont()) {
			infoNotaDebito.setObligadoContabilidad("SI");
		} else {
			infoNotaDebito.setObligadoContabilidad("NO");
		}

		infoNotaDebito.setRazonSocialComprador(apellidoNombre);
		infoNotaDebito.setTipoIdentificacionComprador(egreso.getPersClie().getDimm().getCodigo());
		infoNotaDebito.setIdentificacionComprador(egreso.getPersClie().getPersona().getCedulaRuc());

		// if ((this.getEgreso().getPersClie().getPersona().getDirecc() != null)
		// &&
		// (!this.getEgreso().getPersClie().getPersona().getDirecc().isEmpty()))
		// {
		// infoNotaDebito.setDireccionComprador(this.getEgreso().getPersClie().getPersona().getDirecc());
		// }

		infoNotaDebito.setFechaEmision(Util.cambiarFormatoFechaString(egreso.getFechaEmis(), "dd/MM/yyyy"));

		int indice;
		TotalDocu totalDocu = new TotalDocu();

		totalDocu = new TotalDocu();
		totalDocu.setDescri("Total Neto");
		indice = totalDocus.indexOf(totalDocu);
		infoNotaDebito.setTotalSinImpuestos(totalDocus.get(indice).getValor().setScale(2, RoundingMode.HALF_UP));

		totalDocu = new TotalDocu();
		totalDocu.setDescri("Total Documento");
		indice = totalDocus.indexOf(totalDocu);
		infoNotaDebito.setValorTotal(totalDocus.get(indice).getValor().setScale(2, RoundingMode.HALF_UP));

		try (Formatter formatter = new Formatter()) {
			String numDocModificado = egreso.getEgreso().getSerie1() + "-" + egreso.getEgreso().getSerie2() + "-"
					+ formatter.format("%09d", egreso.getEgreso().getNumero());

			Egreso egresoModificado = egresoRegis.buscarPorId(Egreso.class, egreso.getEgreso().getEgresoId());
			infoNotaDebito.setCodDocModificado(egresoModificado.getDocuEgre().getDimm().getCodigo());

			infoNotaDebito.setNumDocModificado(numDocModificado);
		}

		infoNotaDebito.setFechaEmisionDocSustento(Util.cambiarFormatoFechaString(egreso.getFechaEmis(), "dd/MM/yyyy"));

		infoNotaDebito.setPagos(cargarPagoNotaDebito(fpmeFormPago, totalDocus));

		return infoNotaDebito;

	}

	public InfoGuiaRemision cargarInfoGuiaRemision(Egreso egreso, List<TotalDocu> totalDocus) {

		// Consumidor final validar si se pasa de los 200.00
		// infoFactura.setCompensaciones(); esta campo no existe en la ficha
		// t�cnica

		InfoGuiaRemision infoGuiaRemision = new InfoGuiaRemision();

		if ((egreso.getSucursal().getDireccEsta() != null) && (!egreso.getSucursal().getDireccEsta().isEmpty())) {
			// Direccion de la sucursal no de la matriz la direcci�n de la
			// matriz va en infoTributaria
			infoGuiaRemision.setDirEstablecimiento(egreso.getSucursal().getDireccEsta());
		}

		if ((egreso.getSucursal().getContriEspe() != null) && (!egreso.getSucursal().getContriEspe().isEmpty())) {
			infoGuiaRemision.setContribuyenteEspecial(egreso.getSucursal().getContriEspe());
		}

		if (egreso.getSucursal().isObligaCont()) {
			infoGuiaRemision.setObligadoContabilidad("SI");
		} else {
			infoGuiaRemision.setObligadoContabilidad("NO");
		}

		infoGuiaRemision.setDirPartida("DIRECCION PARTIDA");
		infoGuiaRemision.setFechaIniTransporte("FECHA INI TRASP");
		infoGuiaRemision.setFechaFinTransporte("FECHA FIN TRASP");
		infoGuiaRemision.setRazonSocialTransportista("RAZON SOCILA TRASPORTISTA");
		infoGuiaRemision.setRucTransportista("RUC TRASPORTISTA");
		infoGuiaRemision.setPlaca("PLACA TRASPORTISTA");
		infoGuiaRemision.setTipoIdentificacionTransportista("TIPO IDENTIF. TRASPORT");

		return infoGuiaRemision;
	}

	public InfoLiquidacionCompra cargarInfoLiquidacion(Ingreso ingreso, String codigoIva,
			List<ec.com.tecnointel.soem.ingreso.modelo.TotalDocu> totalDocus, FormPagoMoviIngr formPagoMoviIngr) {

		String apellidoNombre = this.apellidoNombre(ingreso.getPersProv().getPersona().getApelli(),
				ingreso.getPersProv().getPersona().getNombre());

		InfoLiquidacionCompra infoLiquidacionCompra = new InfoLiquidacionCompra();

		if ((ingreso.getSucursal().getDireccEsta() != null) && (!ingreso.getSucursal().getDireccEsta().isEmpty())) {
			// Direccion de la sucursal no de la matriz la direcci�n de la
			// matriz va en infoTributaria
			infoLiquidacionCompra.setDirEstablecimiento(ingreso.getSucursal().getDireccEsta());
		}

		if ((ingreso.getSucursal().getContriEspe() != null) && (!ingreso.getSucursal().getContriEspe().isEmpty())) {
			infoLiquidacionCompra.setContribuyenteEspecial(ingreso.getSucursal().getContriEspe());
		}

		if (ingreso.getSucursal().isObligaCont()) {
			infoLiquidacionCompra.setObligadoContabilidad("SI");
		} else {
			infoLiquidacionCompra.setObligadoContabilidad("NO");
		}

		infoLiquidacionCompra.setTipoIdentificacionProveedor(ingreso.getPersProv().getDimm().getCodigo());
		infoLiquidacionCompra.setRazonSocialProveedor(apellidoNombre);
		infoLiquidacionCompra.setIdentificacionProveedor(ingreso.getPersProv().getPersona().getCedulaRuc());
		infoLiquidacionCompra.setFechaEmision(Util.cambiarFormatoFechaString(ingreso.getFechaRegi(), "dd/MM/yyyy"));
		infoLiquidacionCompra.setDireccionProveedor(ingreso.getPersProv().getPersona().getDirecc());

		int indice;
		ec.com.tecnointel.soem.ingreso.modelo.TotalDocu totalDocu = new ec.com.tecnointel.soem.ingreso.modelo.TotalDocu();

		totalDocu = new ec.com.tecnointel.soem.ingreso.modelo.TotalDocu();
		totalDocu.setDescri("Total Neto");
		indice = totalDocus.indexOf(totalDocu);
		infoLiquidacionCompra.setTotalSinImpuestos(totalDocus.get(indice).getValor().setScale(2, RoundingMode.HALF_UP));

		totalDocu = new ec.com.tecnointel.soem.ingreso.modelo.TotalDocu();
		totalDocu.setDescri("Total Descuento");
		indice = totalDocus.indexOf(totalDocu);
		infoLiquidacionCompra.setTotalDescuento(totalDocus.get(indice).getValor().setScale(2, RoundingMode.HALF_UP));

//		infoLiquidacionCompra.setCodDocReembolso("");
//		infoLiquidacionCompra.setTotalComprobantesReembolso(new BigDecimal(0));
//		infoLiquidacionCompra.setTotalBaseImponibleReembolso(new BigDecimal(0));
//		infoLiquidacionCompra.setTotalImpuestoReembolso(new BigDecimal(0));

		infoLiquidacionCompra.setTotalConImpuestos(generaTotalesImpuestoLiquidacion(codigoIva, totalDocus));

//		El total del documento de totalDocus esta restado las retenciones
//		Aqui se recorre y se suma estas retenciones al total documento
//		por eso se pregunta si valor es menor que 0 ya que las retenciones estan con saldo negativo
//		entonces de estos negativos se suma su valor absoluto
		BigDecimal totalDocumento = new BigDecimal(0);
		for (ec.com.tecnointel.soem.ingreso.modelo.TotalDocu totalDocuSinRetenciones : totalDocus) {
			if (totalDocuSinRetenciones.getDescri().toLowerCase().equals("total documento")
					|| totalDocuSinRetenciones.getValor().compareTo(BigDecimal.ZERO) < 0) {

				totalDocumento = totalDocumento.add(totalDocuSinRetenciones.getValor().abs());
			}
		}

		infoLiquidacionCompra.setImporteTotal(totalDocumento.setScale(2, RoundingMode.HALF_UP));

		infoLiquidacionCompra.setMoneda("DOLAR");

		infoLiquidacionCompra.setPagos(cargarPagoLiquidacion(formPagoMoviIngr, totalDocus, ingreso));

		return infoLiquidacionCompra;
	}

	public InfoCompRetencion cargarInfoComprobanteRetencion(Ingreso ingreso) {

		String apellidoNombre = this.apellidoNombre(ingreso.getPersProv().getPersona().getApelli(),
				ingreso.getPersProv().getPersona().getNombre());

		InfoCompRetencion infoComprobanteRetencion = new InfoCompRetencion();

		if ((ingreso.getSucursal().getDireccEsta() != null) && (!ingreso.getSucursal().getDireccEsta().isEmpty())) {
			// Direccion de la sucursal no de la matriz la direcci�n de la
			// matriz va en infoTributaria
			infoComprobanteRetencion.setDirEstablecimiento(ingreso.getSucursal().getDireccEsta());
		}

		if ((ingreso.getSucursal().getContriEspe() != null) && (!ingreso.getSucursal().getContriEspe().isEmpty())) {
			infoComprobanteRetencion.setContribuyenteEspecial(ingreso.getSucursal().getContriEspe());
		}

		if (ingreso.getSucursal().isObligaCont()) {
			infoComprobanteRetencion.setObligadoContabilidad("SI");
		} else {
			infoComprobanteRetencion.setObligadoContabilidad("NO");
		}

		infoComprobanteRetencion.setTipoIdentificacionSujetoRetenido(ingreso.getPersProv().getDimm().getCodigo());
		infoComprobanteRetencion.setRazonSocialSujetoRetenido(apellidoNombre);
		infoComprobanteRetencion.setIdentificacionSujetoRetenido(ingreso.getPersProv().getPersona().getCedulaRuc());
		infoComprobanteRetencion.setPeriodoFiscal(Util.cambiarFormatoFechaString(ingreso.getFechaRegi(), "MM/yyyy"));
		infoComprobanteRetencion.setFechaEmision(Util.cambiarFormatoFechaString(ingreso.getFechaRegi(), "dd/MM/yyyy"));

//		08=Identificacion del exterior
		if (ingreso.getPersProv().getDimm().getCodigo().equals("08")) {
//			01=Persona Natural; 02=Sociedad
			infoComprobanteRetencion.setTipoSujetoRetenido(ingreso.getPersProv().getNatura());
		}

		if (ingreso.getPersProv().isParteRela()) {
			infoComprobanteRetencion.setParteRel("SI");
		} else {
			infoComprobanteRetencion.setParteRel("NO");
		}

		return infoComprobanteRetencion;
	}

	public DocsSustento cargarDocSustento(Ingreso ingreso,
			List<ec.com.tecnointel.soem.ingreso.modelo.TotalDocu> totalDocus, Retencion retencion, String codigoReteIva,
			String codigoReteRenta, String codigoReteIsd, String codigoIva, String codigoIce, String codigoIrbpnr,
			FormPagoMoviIngr formPagoMoviIngr) {

		DocsSustento docsSustento = new DocsSustento();

//		Revisar si esto funciona ya que este metodo debe retornar un DocsSustento
		List<DocSustento> docSustentos = docsSustento.getDocSustento();

		DocSustento docSustento = new DocSustento();

		docSustento.setCodSustento(ingreso.getDimm().getCodigo());
		docSustento.setCodDocSustento(ingreso.getDocuIngr().getDimm().getCodigo());

//		Opcional y poner los 15 caracteres incluir cero a la izquierda

		try (Formatter formatter = new Formatter()) {
			String numero = formatter.format("%09d", ingreso.getNumero()).toString();
			docSustento.setNumDocSustento(ingreso.getSerie1() + ingreso.getSerie2() + numero);
		}

		docSustento.setFechaEmisionDocSustento(Util.cambiarFormatoFechaString(ingreso.getFechaEmis(), "dd/MM/yyyy"));

//		Opcional
		docSustento.setFechaRegistroContable(Util.cambiarFormatoFechaString(ingreso.getFechaEmis(), "dd/MM/yyyy"));

//		Opcional
		docSustento.setNumAutDocSustento(ingreso.getAutori());
//		Pago a residente o a no residente
		docSustento.setPagoLocExt("01");
//		Obligatorio cuando PagoLocExt sea = 2
		docSustento.setTipoRegi(null);
//		Obligatorio cuando PagoLocExt sea = 2
		docSustento.setPaisEfecPago(null);
//		Obligatorio cuando PagoLocExt sea = 2
		docSustento.setAplicConvDobTrib(null);
//		Obligatorio cuando PagoLocExt sea = 2
		docSustento.setPagExtSujRetNorLeg(null);
//		Obligatorio cuando PagoLocExt sea = 2
		docSustento.setPagoRegFis(null);
//		Obligatorio cuando CodDocSustento sea = 41
		docSustento.setTotalComprobantesReembolso(null);
//		Obligatorio cuando CodDocSustento sea = 41
		docSustento.setTotalBaseImponibleReembolso(null);
//		Obligatorio cuando CodDocSustento sea = 41
		docSustento.setTotalImpuestoReembolso(null);

		BigDecimal totalDocumento = new BigDecimal(0);
		BigDecimal totalSinImpuestos = new BigDecimal(0);

		for (ec.com.tecnointel.soem.ingreso.modelo.TotalDocu totalDocu : totalDocus) {

			if (totalDocu.getDescri().contains("Subtotal Iva")) {

				totalSinImpuestos = totalSinImpuestos.add(totalDocu.getValor());
				docSustento.setTotalSinImpuestos(totalSinImpuestos.setScale(2, RoundingMode.HALF_UP));
			}

			if (totalDocu.getDescri().equals("Total Documento")
					|| totalDocu.getValor().compareTo(BigDecimal.ZERO) < 0) {
				totalDocumento = totalDocumento.add(totalDocu.getValor().abs());
				docSustento.setImporteTotal(totalDocumento.setScale(2, RoundingMode.HALF_UP));
			}
		}

		docSustento.setImpuestosDocSustento(
				this.cargarImpuestosDocSustento(docSustento, codigoIva, codigoIce, codigoIrbpnr, totalDocus));
		docSustento.setRetenciones(this.cargarRetenciones(retencion, codigoReteIva, codigoReteRenta, codigoReteIsd));
//		docSustento.setReembolsos(this.cargarReembolsos(docSustento));
		docSustento.setPagos(this.cargarPagos(formPagoMoviIngr, totalDocus));

		docSustentos.add(docSustento);

		return docsSustento;
	}

	public ImpuestosDocSustento cargarImpuestosDocSustento(DocSustento docSustento, String codigoIva, String codigoIce,
			String codigoIrbpnr, List<ec.com.tecnointel.soem.ingreso.modelo.TotalDocu> totalDocus) {

		ImpuestosDocSustento impuestosDocSustento = new ImpuestosDocSustento();

//		Revisar si esto funciona ya que este metodo debe retornar un DocsSustento
		List<ImpuestoDocSustento> ImpuestoDocSustentos = impuestosDocSustento.getImpuestoDocSustento();

		for (ec.com.tecnointel.soem.ingreso.modelo.TotalDocu totalDocu : totalDocus) {

			if (totalDocu.getTipoImpu().equals("IVA")) {

				ImpuestoDocSustento impuestoDocSustento = new ImpuestoDocSustento();
//				2=Iva; 3=ICE; 5=IRBPNR 
				impuestoDocSustento.setCodImpuestoDocSustento(codigoIva);
//				0=0%; 2=12%; 3=14% etc
				impuestoDocSustento.setCodigoPorcentaje(totalDocu.getCodigo());
				impuestoDocSustento.setBaseImponible(totalDocu.getValor().setScale(2, RoundingMode.HALF_UP));
				impuestoDocSustento
						.setTarifa(totalDocu.getPorcen().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
				impuestoDocSustento.setValorImpuesto(totalDocu.getValor()
						.multiply(totalDocu.getPorcen().divide(new BigDecimal(100))).setScale(2, RoundingMode.HALF_UP));

				ImpuestoDocSustentos.add(impuestoDocSustento);
			}
		}

		return impuestosDocSustento;
	}

	public Retenciones cargarRetenciones(Retencion retencionCargar, String codigoReteIva, String codigoReteRenta,
			String codigoReteIsd) {

		String codigo = null;

		Retenciones retenciones = new Retenciones();

		List<ec.com.tecnointel.soem.documeElec.modelo.retencion.Retencion> retencions = retenciones.getRetencion();

		for (ReteDeta reteDeta : retencionCargar.getReteDetas()) {

			ec.com.tecnointel.soem.documeElec.modelo.retencion.Retencion retencion = new ec.com.tecnointel.soem.documeElec.modelo.retencion.Retencion();

			if (reteDeta.getImpues().equals("Iva")) {
				codigo = codigoReteIva;
			} else if (reteDeta.getImpues().equals("Renta")) {
				codigo = codigoReteRenta;
			} else if (reteDeta.getImpues().equals("Isd")) {
				codigo = codigoReteIsd;
			}

			retencion.setCodigo(codigo);
			retencion.setCodigoRetencion(reteDeta.getCodigoImpu());
			retencion.setBaseImponible(reteDeta.getBase().setScale(2, RoundingMode.HALF_UP));
			retencion.setPorcentajeRetener(reteDeta.getPorcen().setScale(2, RoundingMode.HALF_UP));
			retencion.setValorRetenido(reteDeta.getPorcen().multiply(reteDeta.getBase().divide(new BigDecimal(100)))
					.setScale(2, RoundingMode.HALF_UP));

//			Obligatorio si el codSustento = 10 (Ditribucion de dividendos beneficios o utilidades)		
//			retencion.setDividendos(this.cargarDividendos());
//			Obligatorio cuando corresponda
//			retencion.setCompraCajBanano(this.cargarCompraCajBanano("20", new BigDecimal(12.00)));

			retencions.add(retencion);
		}

		return retenciones;
	}

//	Obligatorio si el codSustento = 10 (Ditribucion de dividendos beneficios o utilidades)
	public Dividendos cargarDividendos() {

		Dividendos dividendos = new Dividendos();
		dividendos.setFechaPagoDiv("2022-09-28");
		dividendos.setImRentaSoc(new BigDecimal(0));
		dividendos.setEjerFisUtDiv("2022");

		return dividendos;

	}

//	Obligatorio cuando corresponda
	public CompraCajBanano cargarCompraCajBanano(String numCajBan, BigDecimal precCajBan) {

		CompraCajBanano compraCajBanano = new CompraCajBanano();

		compraCajBanano.setNumCajBan(numCajBan);
		compraCajBanano.setPrecCajBan(precCajBan);

		return compraCajBanano;
	}

	public Reembolsos cargarReembolsos(DocSustento docSustento) {

//		Reembolsos reembolsos = new Reembolsos();

		ReembolsoDetalle reembolsoDetalle = new ReembolsoDetalle();

		reembolsoDetalle.setTipoIdentificacionProveedorReembolso("04");
		reembolsoDetalle.setIdentificacionProveedorReembolso("1234567890001");
		reembolsoDetalle.setCodPaisPagoProveedorReembolso("593");
		reembolsoDetalle.setTipoProveedorReembolso("01");
		reembolsoDetalle.setCodDocReembolso("01");
		reembolsoDetalle.setEstabDocReembolso("001");
		reembolsoDetalle.setPtoEmiDocReembolso("001");
		reembolsoDetalle.setSecuencialDocReembolso("1");
		reembolsoDetalle.setFechaEmisionDocReembolso("2022-09-30");
		reembolsoDetalle.setNumeroAutorizacionDocReemb("1234567890");

//		Poner en un for para cargar todos los impuestos
		reembolsoDetalle.setDetalleImpuestos(this.cargarDetalleImpuestos());

		Reembolsos reembolsos = new Reembolsos();
		reembolsos.getReembolsoDetalle();

		reembolsos.getReembolsoDetalle().add(reembolsoDetalle);

		return reembolsos;
	}

//	Este metodo todaia no se usa es para reembolsos
	public DetalleImpuestos cargarDetalleImpuestos() {

		DetalleImpuesto detalleImpuesto = new DetalleImpuesto();
		detalleImpuesto.setCodigo("2");
		detalleImpuesto.setCodigoPorcentaje("0");
		detalleImpuesto.setTarifa("0");
		detalleImpuesto.setBaseImponibleReembolso(new BigDecimal(0));
		detalleImpuesto.setImpuestoReembolso(new BigDecimal(0));

		DetalleImpuestos detalleImpuestos = new DetalleImpuestos();

		detalleImpuestos.getDetalleImpuesto().add(detalleImpuesto);

		return detalleImpuestos;

	}

	public Pagos cargarPagos(FormPagoMoviIngr formPagoMoviIngr,
			List<ec.com.tecnointel.soem.ingreso.modelo.TotalDocu> totalDocus) {

		Pago pago = new Pago();

		BigDecimal totalDocumento = new BigDecimal(0);

		for (ec.com.tecnointel.soem.ingreso.modelo.TotalDocu totalDocu : totalDocus) {

//			BigDecimal totalSinImpuestos = new BigDecimal(0);

//			if (totalDocu.getTipoImpu().equals("IVA")) {
//
//				totalSinImpuestos = totalSinImpuestos.add(totalDocu.getValor());
//
//			}

			if (totalDocu.getDescri().equals("Total Documento")
					|| totalDocu.getValor().compareTo(BigDecimal.ZERO) < 0) {
				totalDocumento = totalDocumento.add(totalDocu.getValor().abs());
			}
		}

		pago.setFormaPago(formPagoMoviIngr.getFormPago().getDimm().getCodigo());
		pago.setTotal(totalDocumento.setScale(2, RoundingMode.HALF_UP));

		Pagos pagos = new Pagos();
		pagos.getPago().add(pago);

		return pagos;
	}

	public Destinatario cargarDestinatario(Egreso egreso) {

		String apellidoNombre = this.apellidoNombre(egreso.getPersClie().getPersona().getApelli(),
				egreso.getPersClie().getPersona().getNombre());

		Destinatario destinatario = new Destinatario();

		destinatario.setIdentificacionDestinatario(egreso.getPersClie().getPersona().getCedulaRuc());
		destinatario.setRazonSocialDestinatario(apellidoNombre); // No es
																	// obligatorio
		destinatario.setDirDestinatario(egreso.getPersClie().getPersona().getDirecc()); // No es obligatorio
		destinatario.setMotivoTraslado("MOTIVO TRASLADO");
		destinatario.setDocAduaneroUnico("DOC-ADUANERO");
		destinatario.setCodEstabDestino("COD-ESTAB");
		destinatario.setRuta("RUTA");
		destinatario.setCodDocSustento("COD-SUSTENTO");
		destinatario.setNumDocSustento("NUM-SUSTENTO");
		destinatario.setNumAutDocSustento("NUM-AUTORIZA");
		destinatario.setFechaEmisionDocSustento("FECHA-EMISION");

		return destinatario;
	}

	private Factura.InfoFactura.TotalConImpuestos generaTotalesImpuestoFactura(String codigoIva, String codigoIce,
			String codigoIrbpnr, List<TotalDocu> totalDocus) {

//		Determina si existe iva ya sea 0 o diferente de cero ya que puede darse el caso que la venta sea solo de fundas
//		entoces no hay referencia para aumentar o crear el detalle de iva por las fundas
		Boolean existeIva = false;

//		Se solamente en el caso que haya ice, para saber si en el documento hay iva diferente de cero
//		y acumular o crear un totalImpuesto
		Boolean existeIvaDifeCero = false;

		BigDecimal valorIce = new BigDecimal(0);

		Factura.InfoFactura.TotalConImpuestos totalConImpuestos = new Factura.InfoFactura.TotalConImpuestos();

//		Graba el valor del iva en caso que exista valores por concepto de Ice (Fundas Plasticas),
//		ya que el en caso de simplemente vender fundas o vender solo porductos sin iva y fundas no se tiene el valor
//		donde referenciar el iva de este Ice
		Factura.InfoFactura.TotalConImpuestos.TotalImpuesto totalImpuestoIvaIce = new Factura.InfoFactura.TotalConImpuestos.TotalImpuesto();

//		Recorre una primera ves para saber si hay ICE porque este tiene que sumarse a la base imponible del iva
		for (TotalDocu totalDocu : totalDocus) {

			if (totalDocu.getTipoImpu().equals("ICE")) {

//				Se pone con seis decimales ya que se pone dos al final
				valorIce = totalDocu.getValor().multiply(totalDocu.getPorcen()).setScale(6, RoundingMode.HALF_UP);

//				Crea objeto de iva por concepto de Ice
				totalImpuestoIvaIce.setCodigo(codigoIva);
				totalImpuestoIvaIce.setCodigoPorcentaje(this.dimmIva.getCodigo());
				totalImpuestoIvaIce.setBaseImponible(valorIce);
				totalImpuestoIvaIce
						.setTarifa(this.dimmIva.getPorcen().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
				totalImpuestoIvaIce.setValor(valorIce.multiply(this.dimmIva.getPorcen().divide(new BigDecimal(100)))
						.setScale(6, RoundingMode.HALF_UP));

			}
			if (totalDocu.getTipoImpu().equals("IVA")) {
				existeIva = true;
//				Determina si hay un subtotal con iva diferente de cero
				if (totalDocu.getPorcen().compareTo(new BigDecimal(0)) > 0 && !existeIvaDifeCero) {
					existeIvaDifeCero = true;
				}
			}
		}

		for (TotalDocu totalDocu : totalDocus) {

			if (totalDocu.getTipoImpu().equals("IVA") || totalDocu.getTipoImpu().equals("ICE")
					|| totalDocu.getTipoImpu().equals("IRBPNR")) {

				BigDecimal valor = new BigDecimal(0);

				Factura.InfoFactura.TotalConImpuestos.TotalImpuesto totalImpuesto = new Factura.InfoFactura.TotalConImpuestos.TotalImpuesto();

				if (totalDocu.getTipoImpu().equals("IVA")) {

//					Si es null no hay ice se crea el impuesto normalmente sera 0 o diferente de 0
//					tambien se puede verificar si valorIce es == 0
					if (valorIce.compareTo(BigDecimal.ZERO) == 0) {

						totalImpuesto.setCodigo(codigoIva);
						totalImpuesto.setCodigoPorcentaje(totalDocu.getCodigo());
						totalImpuesto.setBaseImponible(totalDocu.getValor().setScale(2, RoundingMode.HALF_UP));
						totalImpuesto.setTarifa(
								totalDocu.getPorcen().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
						totalImpuesto.setValor(
								totalDocu.getValor().multiply(totalDocu.getPorcen().divide(new BigDecimal(100)))
										.setScale(2, RoundingMode.HALF_UP));

					} else {

						if (existeIvaDifeCero) {
//							Existe iva direferente de cero
//							Si hay valores de ice y el porcentaje es 0 crea totalImpuesto con valores para iva 
							if (totalDocu.getPorcen().compareTo(BigDecimal.ZERO) == 0) {
//								
								totalImpuesto.setCodigo(codigoIva);
								totalImpuesto.setCodigoPorcentaje(totalDocu.getCodigo());
								totalImpuesto.setBaseImponible(totalDocu.getValor().setScale(2, RoundingMode.HALF_UP));
								totalImpuesto.setTarifa(
										totalDocu.getPorcen().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
								totalImpuesto.setValor(
										totalDocu.getValor().multiply(totalDocu.getPorcen().divide(new BigDecimal(100)))
												.setScale(2, RoundingMode.HALF_UP));

							} else {

//								Se pone con seis decimales ya que se pone dos al final
								valor = totalDocu.getValor().multiply(totalDocu.getPorcen().divide(new BigDecimal(100)))
										.setScale(6, RoundingMode.HALF_UP);

//								Si hay valores de ice y el porcentaje es diferente de 0 suma valores de iva con iva de totalImpuestoIvaIce
								totalImpuesto.setCodigo(totalImpuestoIvaIce.getCodigo());
								totalImpuesto.setCodigoPorcentaje(totalImpuestoIvaIce.getCodigoPorcentaje());
								totalImpuesto.setBaseImponible(totalImpuestoIvaIce.getBaseImponible()
										.add(totalDocu.getValor()).setScale(2, RoundingMode.HALF_UP));
								totalImpuesto.setTarifa(totalImpuestoIvaIce.getTarifa()
										.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
								totalImpuesto.setValor(
										(totalImpuestoIvaIce.getValor().add(valor)).setScale(2, RoundingMode.HALF_UP));
							}
						} else {
//							No existe iva direferente de cero, es decir solo hay productos con iva cero en el documento 
//							por lo tanto no hay un total impuesto para el valor del iva, entonces se crea un totalImpuesto
//							para el iva cero y se agrega totalImpuestoIvaIce para detallar el valor del iva por concepto de Ice
							totalImpuesto.setCodigo(codigoIva);
							totalImpuesto.setCodigoPorcentaje(totalDocu.getCodigo());
							totalImpuesto.setBaseImponible(totalDocu.getValor().setScale(2, RoundingMode.HALF_UP));
							totalImpuesto.setTarifa(
									totalDocu.getPorcen().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
							totalImpuesto.setValor(
									totalDocu.getValor().multiply(totalDocu.getPorcen().divide(new BigDecimal(100)))
											.setScale(2, RoundingMode.HALF_UP));

//							Redondea estos valores ya que originalmente estan con seis decimales
							BigDecimal redondeoBaseImpo = totalImpuestoIvaIce.getBaseImponible().setScale(2,
									RoundingMode.HALF_UP);
							BigDecimal RedondeoValor = totalImpuestoIvaIce.getValor().setScale(2, RoundingMode.HALF_UP);

							totalImpuestoIvaIce.setBaseImponible(redondeoBaseImpo);
							totalImpuestoIvaIce.setValor(RedondeoValor);
							totalConImpuestos.getTotalImpuesto().add(totalImpuestoIvaIce);
						}
					}

				} else if (totalDocu.getTipoImpu().equals("ICE")) {

					totalImpuesto.setCodigo(codigoIce);
					totalImpuesto.setCodigoPorcentaje(totalDocu.getCodigo());
					totalImpuesto.setBaseImponible(totalDocu.getValor().setScale(2, RoundingMode.HALF_UP));
					totalImpuesto
							.setTarifa(totalDocu.getPorcen().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
					totalImpuesto.setValor(valorIce.setScale(2, RoundingMode.HALF_UP));

				} else if (totalDocu.getTipoImpu().equals("IRBPNR")) {

					totalImpuesto.setCodigo(codigoIrbpnr);
					totalImpuesto.setCodigoPorcentaje(totalDocu.getCodigo());
					totalImpuesto.setBaseImponible(totalDocu.getValor().setScale(2, RoundingMode.HALF_UP));
					totalImpuesto
							.setTarifa(totalDocu.getPorcen().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
					totalImpuesto
							.setValor(totalDocu.getValor().multiply(totalDocu.getPorcen().divide(new BigDecimal(100)))
									.setScale(2, RoundingMode.HALF_UP));
				}

				totalConImpuestos.getTotalImpuesto().add(totalImpuesto);
			}
		}

//		Quiere decir que solo vendio fundas por lo tanto no entro a crear iva por las fundas
//		Aqui se registra solo el iva de las fundas
		if (!existeIva && valorIce.compareTo(BigDecimal.ZERO) != 0) {
//			Redondea estos valores ya que originalmente estan con seis decimales
			BigDecimal redondeoBaseImpo = totalImpuestoIvaIce.getBaseImponible().setScale(2, RoundingMode.HALF_UP);
			BigDecimal RedondeoValor = totalImpuestoIvaIce.getValor().setScale(2, RoundingMode.HALF_UP);

			totalImpuestoIvaIce.setBaseImponible(redondeoBaseImpo);
			totalImpuestoIvaIce.setValor(RedondeoValor);
			totalConImpuestos.getTotalImpuesto().add(totalImpuestoIvaIce);
		}

		return totalConImpuestos;
	}

	private TotalConImpuestos generaTotalesImpuestoNotaCredito(String codigoIva, String codigoIce, String codigoIrbpnr,
			List<TotalDocu> totalDocus) {

//		Determina si existe iva ya sea 0 o diferente de cero ya que puede darse el caso que la venta sea solo de fundas
//		entoces no hay referencia para aumentar o crear el detalle de iva por las fundas
		Boolean existeIva = false;

//		Se solamente en el caso que haya ice, para saber si en el documento hay iva diferente de cero
//		y acumular o crear un totalImpuesto
		Boolean existeIvaDifeCero = false;

		BigDecimal valorIce = new BigDecimal(0);

		TotalConImpuestos totalConImpuestos = new TotalConImpuestos();

//		Graba el valor del iva en caso que exista valores por concepto de Ice (Fundas Plasticas),
//		ya que el en caso de simplemente vender fundas o vender solo porductos sin iva y fundas no se tiene el valor
//		donde referenciar el iva de este Ice
		TotalConImpuestos.TotalImpuesto totalImpuestoIvaIce = new TotalConImpuestos.TotalImpuesto();

//		Recorre una primera ves para saber si hay ICE porque este tiene que sumarse a la base imponible del iva
		for (TotalDocu totalDocu : totalDocus) {

			if (totalDocu.getTipoImpu().equals("ICE")) {

//				Se pone con seis decimales ya que se pone dos al final
				valorIce = totalDocu.getValor().multiply(totalDocu.getPorcen()).setScale(6, RoundingMode.HALF_UP);

//				Crea objeto de iva por concepto de Ice
				totalImpuestoIvaIce.setCodigo(codigoIva);
				totalImpuestoIvaIce.setCodigoPorcentaje(this.dimmIva.getCodigo());
				totalImpuestoIvaIce.setBaseImponible(valorIce);
//				totalImpuestoIvaIce.setTarifa(this.dimmIva.getPorcen());
				totalImpuestoIvaIce.setValor(valorIce.multiply(this.dimmIva.getPorcen().divide(new BigDecimal(100)))
						.setScale(6, RoundingMode.HALF_UP));

			}
			if (totalDocu.getTipoImpu().equals("IVA")) {
				existeIva = true;
//				Determina si hay un subtotal con iva diferente de cero
				if (totalDocu.getPorcen().compareTo(new BigDecimal(0)) > 0 && !existeIvaDifeCero) {
					existeIvaDifeCero = true;
				}
			}
		}

		for (TotalDocu totalDocu : totalDocus) {

			if (totalDocu.getTipoImpu().equals("IVA") || totalDocu.getTipoImpu().equals("ICE")
					|| totalDocu.getTipoImpu().equals("IRBPNR")) {

				BigDecimal valor = new BigDecimal(0);

				TotalConImpuestos.TotalImpuesto totalImpuesto = new TotalConImpuestos.TotalImpuesto();

				if (totalDocu.getTipoImpu().equals("IVA")) {

//					Si es null no hay ice se crea el impuesto normalmente sera 0 o diferente de 0
//					tambien se puede verificar si valorIce es == 0
					if (valorIce.compareTo(BigDecimal.ZERO) == 0) {

						totalImpuesto.setCodigo(codigoIva);
						totalImpuesto.setCodigoPorcentaje(totalDocu.getCodigo());
						totalImpuesto.setBaseImponible(totalDocu.getValor().setScale(2, RoundingMode.HALF_UP));
//						totalImpuesto.setTarifa(totalDocu.getPorcen().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
						totalImpuesto.setValor(
								totalDocu.getValor().multiply(totalDocu.getPorcen().divide(new BigDecimal(100)))
										.setScale(2, RoundingMode.HALF_UP));

					} else {

						if (existeIvaDifeCero) {
//							Existe iva direferente de cero
//							Si hay valores de ice y el porcentaje es 0 crea totalImpuesto con valores para iva 
							if (totalDocu.getPorcen().compareTo(BigDecimal.ZERO) == 0) {
//								
								totalImpuesto.setCodigo(codigoIva);
								totalImpuesto.setCodigoPorcentaje(totalDocu.getCodigo());
								totalImpuesto.setBaseImponible(totalDocu.getValor().setScale(2, RoundingMode.HALF_UP));
//								totalImpuesto.setTarifa(totalDocu.getPorcen().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
								totalImpuesto.setValor(
										totalDocu.getValor().multiply(totalDocu.getPorcen().divide(new BigDecimal(100)))
												.setScale(2, RoundingMode.HALF_UP));

							} else {

//								Se pone con seis decimales ya que se pone dos al final
								valor = totalDocu.getValor().multiply(totalDocu.getPorcen().divide(new BigDecimal(100)))
										.setScale(6, RoundingMode.HALF_UP);

//								Si hay valores de ice y el porcentaje es diferente de 0 suma valores de iva con iva de totalImpuestoIvaIce
								totalImpuesto.setCodigo(totalImpuestoIvaIce.getCodigo());
								totalImpuesto.setCodigoPorcentaje(totalImpuestoIvaIce.getCodigoPorcentaje());
								totalImpuesto.setBaseImponible(totalImpuestoIvaIce.getBaseImponible()
										.add(totalDocu.getValor()).setScale(2, RoundingMode.HALF_UP));
//								totalImpuesto.setTarifa(totalImpuestoIvaIce.getTarifa().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
								totalImpuesto.setValor(
										(totalImpuestoIvaIce.getValor().add(valor)).setScale(2, RoundingMode.HALF_UP));
							}
						} else {
//							No existe iva direferente de cero, es decir solo hay productos con iva cero en el documento 
//							por lo tanto no hay un total impuesto para el valor del iva, entonces se crea un totalImpuesto
//							para el iva cero y se agrega totalImpuestoIvaIce para detallar el valor del iva por concepto de Ice
							totalImpuesto.setCodigo(codigoIva);
							totalImpuesto.setCodigoPorcentaje(totalDocu.getCodigo());
							totalImpuesto.setBaseImponible(totalDocu.getValor().setScale(2, RoundingMode.HALF_UP));
//							totalImpuesto.setTarifa(totalDocu.getPorcen().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
							totalImpuesto.setValor(
									totalDocu.getValor().multiply(totalDocu.getPorcen().divide(new BigDecimal(100)))
											.setScale(2, RoundingMode.HALF_UP));

//							Redondea estos valores ya que originalmente estan con seis decimales
							BigDecimal redondeoBaseImpo = totalImpuestoIvaIce.getBaseImponible().setScale(2,
									RoundingMode.HALF_UP);
							BigDecimal RedondeoValor = totalImpuestoIvaIce.getValor().setScale(2, RoundingMode.HALF_UP);

							totalImpuestoIvaIce.setBaseImponible(redondeoBaseImpo);
							totalImpuestoIvaIce.setValor(RedondeoValor);
							totalConImpuestos.getTotalImpuesto().add(totalImpuestoIvaIce);
						}
					}

				} else if (totalDocu.getTipoImpu().equals("ICE")) {

					totalImpuesto.setCodigo(codigoIce);
					totalImpuesto.setCodigoPorcentaje(totalDocu.getCodigo());
					totalImpuesto.setBaseImponible(totalDocu.getValor().setScale(2, RoundingMode.HALF_UP));
//					totalImpuesto.setTarifa(totalDocu.getPorcen().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
					totalImpuesto.setValor(valorIce.setScale(2, RoundingMode.HALF_UP));

				} else if (totalDocu.getTipoImpu().equals("IRBPNR")) {

					totalImpuesto.setCodigo(codigoIrbpnr);
					totalImpuesto.setCodigoPorcentaje(totalDocu.getCodigo());
					totalImpuesto.setBaseImponible(totalDocu.getValor().setScale(2, RoundingMode.HALF_UP));
//					totalImpuesto.setTarifa(totalDocu.getPorcen().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
					totalImpuesto
							.setValor(totalDocu.getValor().multiply(totalDocu.getPorcen().divide(new BigDecimal(100)))
									.setScale(2, RoundingMode.HALF_UP));
				}

				totalConImpuestos.getTotalImpuesto().add(totalImpuesto);
			}
		}

//		Quiere decir que solo vendio fundas por lo tanto no entro a crear iva por las fundas
//		Aqui se registra solo el iva de las fundas
		if (!existeIva && valorIce.compareTo(BigDecimal.ZERO) != 0) {
//			Redondea estos valores ya que originalmente estan con seis decimales
			BigDecimal redondeoBaseImpo = totalImpuestoIvaIce.getBaseImponible().setScale(2, RoundingMode.HALF_UP);
			BigDecimal RedondeoValor = totalImpuestoIvaIce.getValor().setScale(2, RoundingMode.HALF_UP);

			totalImpuestoIvaIce.setBaseImponible(redondeoBaseImpo);
			totalImpuestoIvaIce.setValor(RedondeoValor);
			totalConImpuestos.getTotalImpuesto().add(totalImpuestoIvaIce);
		}

		return totalConImpuestos;
	}

	private ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.LiquidacionCompra.InfoLiquidacionCompra.TotalConImpuestos generaTotalesImpuestoLiquidacion(
			String codigoIva, List<ec.com.tecnointel.soem.ingreso.modelo.TotalDocu> totalDocus) {

		ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.LiquidacionCompra.InfoLiquidacionCompra.TotalConImpuestos totalConImpuestos = new ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.LiquidacionCompra.InfoLiquidacionCompra.TotalConImpuestos();

		for (ec.com.tecnointel.soem.ingreso.modelo.TotalDocu totalDocu : totalDocus) {

			if (totalDocu.getTipoImpu().equals("IVA")) {

				TotalImpuesto totalImpuesto = new TotalImpuesto();

				totalImpuesto.setCodigo(codigoIva);
				totalImpuesto.setCodigoPorcentaje(totalDocu.getCodigo());
				totalImpuesto.setBaseImponible(totalDocu.getValor().setScale(2, RoundingMode.HALF_UP));
				totalImpuesto.setTarifa(totalDocu.getPorcen().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
				totalImpuesto.setValor(totalDocu.getValor().multiply(totalDocu.getPorcen().divide(new BigDecimal(100)))
						.setScale(2, RoundingMode.HALF_UP));

				totalConImpuestos.getTotalImpuesto().add(totalImpuesto);
			}
		}

		return totalConImpuestos;
	}

	private Factura.InfoFactura.Pago cargarPagoFactura(FpmeFormPago fpmeFormPago, List<TotalDocu> totalDocus) {

		Factura.InfoFactura.Pago pago = new Factura.InfoFactura.Pago();
		Factura.InfoFactura.Pago.DetallePago detallePago = new Factura.InfoFactura.Pago.DetallePago();

		if (fpmeFormPago.getFormPago() != null) {
			detallePago.setFormaPago(fpmeFormPago.getFormPago().getDimm().getCodigo());
		} else {
			detallePago.setFormaPago("01"); // Sin utilización del sistema financiero
		}

		detallePago.setPlazo(fpmeFormPago.getDiasPlaz());
		detallePago.setUnidadTiempo(fpmeFormPago.getUnidadTiem());
		int indice;
		TotalDocu totalDocu = new TotalDocu();

		totalDocu = new TotalDocu();
		totalDocu.setDescri("Total Documento");
		indice = totalDocus.indexOf(totalDocu);

		detallePago.setTotal(totalDocus.get(indice).getValor().setScale(2, RoundingMode.HALF_UP));

		pago.getPagos().add(detallePago);

		return pago;
	}

	private NotaDebito.InfoNotaDebito.Pago cargarPagoNotaDebito(FpmeFormPago fpmeFormPago, List<TotalDocu> totalDocus) {

		NotaDebito.InfoNotaDebito.Pago pago = new NotaDebito.InfoNotaDebito.Pago();
		NotaDebito.InfoNotaDebito.Pago.DetallePago detallePago = new NotaDebito.InfoNotaDebito.Pago.DetallePago();

		if (fpmeFormPago.getFormPago() != null) {
			detallePago.setFormaPago(fpmeFormPago.getFormPago().getDimm().getCodigo());
		} else {
			// Sin utilizacion del sistema financiero
			detallePago.setFormaPago("01");
		}

		int indice;
		TotalDocu totalDocu = new TotalDocu();

		totalDocu = new TotalDocu();
		totalDocu.setDescri("Total Documento");
		indice = totalDocus.indexOf(totalDocu);

		detallePago.setTotal(totalDocus.get(indice).getValor().setScale(2, RoundingMode.HALF_UP));

		pago.getPagos().add(detallePago);

		return pago;
	}

	private ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.Pagos cargarPagoLiquidacion(
			FormPagoMoviIngr formPagoMoviIngr, List<ec.com.tecnointel.soem.ingreso.modelo.TotalDocu> totalDocus,
			Ingreso ingreso) {

		ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.Pagos pagos = new ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.Pagos();
		ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.Pagos.Pago pago = new ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.Pagos.Pago();

		if (formPagoMoviIngr.getFormPago() != null) {
			pago.setFormaPago(formPagoMoviIngr.getFormPago().getDimm().getCodigo());
		} else {
			pago.setFormaPago("01"); // Sin utilización del sistema financiero
		}

		BigDecimal totalDocumento = new BigDecimal(0);

		for (ec.com.tecnointel.soem.ingreso.modelo.TotalDocu totalDocu : totalDocus) {

			if (totalDocu.getDescri().equals("Total Documento")
					|| totalDocu.getValor().compareTo(BigDecimal.ZERO) < 0) {
				totalDocumento = totalDocumento.add(totalDocu.getValor().abs());
			}
		}

		pago.setPlazo(new BigDecimal(ingreso.getDiasPlaz()));
		pago.setTotal(totalDocumento.setScale(2, RoundingMode.HALF_UP));

		pagos.getPago().add(pago);

		return pagos;
	}

	public String apellidoNombre(String apellido, String nombre) {
		if (nombre != null) {
			return apellido + " " + nombre;
		} else {
			return apellido;
		}
	}

	@Override
	public void registrarDimmIva(Dimm dimm) {
		this.dimmIva = dimm;
	}

	@Override
	public void leyenda1(String leyenda1) {
		this.leyenda1 = leyenda1;
	}

	@Override
	public void leyenda2(String leyenda2) {
		this.leyenda2 = leyenda2;
	}

	@Override
	public void msgInfoAdicional(String msgInfoAdicional) {
		this.msgInfoAdicional = msgInfoAdicional;
	}
}
