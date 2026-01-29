package ec.com.tecnointel.soem.atsSri.registroImp;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import ec.com.tecnointel.soem.atsSri.modelo.AirType;
import ec.com.tecnointel.soem.atsSri.modelo.AplicConvDobTribType;
import ec.com.tecnointel.soem.atsSri.modelo.CodigoOperativoType;
import ec.com.tecnointel.soem.atsSri.modelo.ComprasType;
import ec.com.tecnointel.soem.atsSri.modelo.DetalleAirComprasType;
import ec.com.tecnointel.soem.atsSri.modelo.DetalleComprasType;
import ec.com.tecnointel.soem.atsSri.modelo.FormasDePagoType;
import ec.com.tecnointel.soem.atsSri.modelo.IvaType;
import ec.com.tecnointel.soem.atsSri.modelo.PagoExteriorType;
import ec.com.tecnointel.soem.atsSri.modelo.ParteRelType;
import ec.com.tecnointel.soem.atsSri.registroInt.GenerarAtsSriInt;
import ec.com.tecnointel.soem.ingreso.listaInt.IngresoListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.SucursalListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.registroInt.ParametroRegisInt;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

@Stateless
public class GenerarAtsSriImp implements GenerarAtsSriInt, Serializable {

	private static final long serialVersionUID = -1157670290051359361L;

	@Inject
	SucursalListaInt sucursalLista;
	
	@Inject
	IngresoListaInt ingresoLista;
	
	@Inject
	ParametroRegisInt parametroRegis;
	
	@Override
	public ByteArrayOutputStream generarAtsSriXml(Sucursal sucursal, LocalDate fechaDesd, LocalDate fechaHast) throws Exception{

		IvaType ivaType = generarIvaType(sucursal, fechaDesd, fechaHast);
		
		ComprasType comprasType = generarCompras(fechaDesd, fechaHast);
		
		ivaType.setCompras(comprasType);
		
		return marshalIvaType(ivaType);
		
	}
	
	public IvaType generarIvaType(Sucursal sucursal, LocalDate fechaDesd, LocalDate fechaHast) throws Exception{
		
		IvaType ivaType = new IvaType();
		
		Sucursal sucursalMatriz = sucursalLista.buscarMatriz(sucursal, null);
		String numEstabRuc = buscarNumeroEstablecimientosString();
		
		String formato = "MM";
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formato);
		String fechaFormat = fechaDesd.format(dateTimeFormatter);
		
		ivaType.setTipoIDInformante("R");
		ivaType.setIdInformante(sucursalMatriz.getRuc());
		ivaType.setRazonSocial(sucursalMatriz.getDescri());
		ivaType.setAnio(fechaDesd.getYear());
		ivaType.setMes(fechaFormat);
		ivaType.setRegimenMicroempresa(null);
		ivaType.setNumEstabRuc(numEstabRuc);
		ivaType.setTotalVentas(BigDecimal.ZERO);
		ivaType.setCodigoOperativo(CodigoOperativoType.IVA);
		
		return ivaType;
		
	}
	
	public ComprasType generarCompras(LocalDate fechaDesd, LocalDate fechaHast) throws Exception{
		
		ComprasType comprasType = new ComprasType();
		
		List<Object[]> objectDetalleCompras = selectDetalleCompras(fechaDesd, fechaHast);
		
		BigDecimal limiteFormaPago = buscarLimiteFormaPago();

		for (Object[] registroCompra : objectDetalleCompras) {
			
			DetalleComprasType detalleComprasType = generarDetalleCompras(registroCompra, limiteFormaPago);
			comprasType.getDetalleCompras().add(detalleComprasType);			
		}
		
		return comprasType;
		
	}
	
	public DetalleComprasType generarDetalleCompras(Object[] registroCompra, BigDecimal limiteFormaPago) throws Exception{
		
		String tipoIdProveedor = "";
		
		switch ((String) registroCompra[2]) {
		case "04":
			tipoIdProveedor = "01";	
			break;
		case "05":
			tipoIdProveedor = "02";	
			break;
		case "06":
			tipoIdProveedor = "03";	
			break;
		default:
			break;
		}
		
		DetalleComprasType detalleComprasType = new DetalleComprasType();
				
		detalleComprasType.setCodSustento((String) registroCompra[1]);
		detalleComprasType.setTpIdProv(tipoIdProveedor);
		detalleComprasType.setIdProv((String) registroCompra[3]);
		detalleComprasType.setTipoComprobante((String) registroCompra[4]);
		
//		En un campo condicional se debe ingresar información solamente cuando el código
//		del tipo de identificación del proveedor o retenido es igual a 03 (compra–
//		pasaporte). Corresponde a uno de los códigos de la Tabla 14
//		detalleComprasType.setTipoProv(null);
//		detalleComprasType.setDenoProv(null);
		
		if ((boolean) registroCompra[8]) {
			detalleComprasType.setParteRel(ParteRelType.SI);	
		} else {
			detalleComprasType.setParteRel(ParteRelType.NO);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		detalleComprasType.setFechaRegistro(sdf.format(registroCompra[9]));
		detalleComprasType.setEstablecimiento((String) registroCompra[10]);
		detalleComprasType.setPuntoEmision((String) registroCompra[11]);
		detalleComprasType.setSecuencial((int) registroCompra[12]);
		detalleComprasType.setFechaEmision(sdf.format(registroCompra[13]));
		detalleComprasType.setAutorizacion((String) registroCompra[14]);
		
//		No objeto de Iva		
		detalleComprasType.setBaseNoGraIva(BigDecimal.ZERO);
		
//		Tarifa 0%
		detalleComprasType.setBaseImponible(((BigDecimal) registroCompra[15]).setScale(2, RoundingMode.HALF_UP).abs());
		
//		Tarifa diferente de 0%
		detalleComprasType.setBaseImpGrav(((BigDecimal) registroCompra[16]).setScale(2, RoundingMode.HALF_UP).abs());
		detalleComprasType.setBaseImpExe(BigDecimal.ZERO);
		detalleComprasType.setMontoIce(BigDecimal.ZERO);
		detalleComprasType.setMontoIva(((BigDecimal) registroCompra[17]).setScale(2, RoundingMode.HALF_UP).abs());

		List<Object[]> airs = selectAir((Integer) registroCompra[0], "Iva");
		
		if (!airs.isEmpty()) {
			
			for (Object[] detalleAir : airs) {
				
				if (((BigDecimal) detalleAir[3]).compareTo(new BigDecimal(10)) == 0) {

					detalleComprasType.setValRetBien10(((BigDecimal) detalleAir[4]).setScale(2, RoundingMode.HALF_UP));
					detalleComprasType.setValRetServ20(BigDecimal.ZERO);
					detalleComprasType.setValorRetBienes(BigDecimal.ZERO);
					detalleComprasType.setValRetServ50(BigDecimal.ZERO);
					detalleComprasType.setValorRetServicios(BigDecimal.ZERO);
					detalleComprasType.setValRetServ100(BigDecimal.ZERO);
					
				} else if (((BigDecimal) detalleAir[3]).compareTo(new BigDecimal(20)) == 0) {
					
					detalleComprasType.setValRetServ20(((BigDecimal) detalleAir[4]).setScale(2, RoundingMode.HALF_UP));
					detalleComprasType.setValRetBien10(BigDecimal.ZERO);
					detalleComprasType.setValorRetBienes(BigDecimal.ZERO);
					detalleComprasType.setValRetServ50(BigDecimal.ZERO);
					detalleComprasType.setValorRetServicios(BigDecimal.ZERO);
					detalleComprasType.setValRetServ100(BigDecimal.ZERO);
					
				} else if (((BigDecimal) detalleAir[3]).compareTo(new BigDecimal(30)) == 0) {
					
					detalleComprasType.setValorRetBienes(((BigDecimal) detalleAir[4]).setScale(2, RoundingMode.HALF_UP));
					detalleComprasType.setValRetBien10(BigDecimal.ZERO);
					detalleComprasType.setValRetServ20(BigDecimal.ZERO);
					detalleComprasType.setValRetServ50(BigDecimal.ZERO);
					detalleComprasType.setValorRetServicios(BigDecimal.ZERO);
					detalleComprasType.setValRetServ100(BigDecimal.ZERO);

				} else if (((BigDecimal) detalleAir[3]).compareTo(new BigDecimal(50)) == 0) {
					
					detalleComprasType.setValRetServ50(((BigDecimal) detalleAir[4]).setScale(2, RoundingMode.HALF_UP));
					detalleComprasType.setValRetBien10(BigDecimal.ZERO);
					detalleComprasType.setValRetServ20(BigDecimal.ZERO);
					detalleComprasType.setValorRetBienes(BigDecimal.ZERO);
					detalleComprasType.setValorRetServicios(BigDecimal.ZERO);
					detalleComprasType.setValRetServ100(BigDecimal.ZERO);
					
				} else if (((BigDecimal) detalleAir[3]).compareTo(new BigDecimal(70)) == 0) {
					
					detalleComprasType.setValorRetServicios(((BigDecimal) detalleAir[4]).setScale(2, RoundingMode.HALF_UP));
					detalleComprasType.setValRetBien10(BigDecimal.ZERO);
					detalleComprasType.setValRetServ20(BigDecimal.ZERO);
					detalleComprasType.setValorRetBienes(BigDecimal.ZERO);
					detalleComprasType.setValRetServ50(BigDecimal.ZERO);
					detalleComprasType.setValRetServ100(BigDecimal.ZERO);

				} else if (((BigDecimal) detalleAir[3]).compareTo(new BigDecimal(100)) == 0) {
					
					detalleComprasType.setValRetServ100(((BigDecimal) detalleAir[4]).setScale(2, RoundingMode.HALF_UP));
					detalleComprasType.setValRetBien10(BigDecimal.ZERO);
					detalleComprasType.setValRetServ20(BigDecimal.ZERO);
					detalleComprasType.setValorRetBienes(BigDecimal.ZERO);
					detalleComprasType.setValRetServ50(BigDecimal.ZERO);
					detalleComprasType.setValorRetServicios(BigDecimal.ZERO);
					
				} 
			}
		} else {
			detalleComprasType.setValRetBien10(BigDecimal.ZERO);
			detalleComprasType.setValRetServ20(BigDecimal.ZERO);
			detalleComprasType.setValorRetBienes(BigDecimal.ZERO);
			detalleComprasType.setValRetServ50(BigDecimal.ZERO);
			detalleComprasType.setValorRetServicios(BigDecimal.ZERO);
			detalleComprasType.setValRetServ100(BigDecimal.ZERO);
		}
		
		detalleComprasType.setValorRetencionNc(BigDecimal.ZERO);
		detalleComprasType.setTotbasesImpReemb(BigDecimal.ZERO);

		PagoExteriorType pagoExteriorType = generarPagoExterior();
		detalleComprasType.setPagoExterior(pagoExteriorType);
				
		if ( ((BigDecimal) registroCompra[18]).compareTo(limiteFormaPago) >= 0) {
			FormasDePagoType formasDePagoType = generarFormasDePago((Integer) registroCompra[0]);
			detalleComprasType.setFormasDePago(formasDePagoType);
		}

		AirType airType = generarAirTypeRenta((Integer) registroCompra[0]);
		detalleComprasType.setAir(airType);

		detalleComprasType.setEstabRetencion1((String)registroCompra[19]);
		detalleComprasType.setPtoEmiRetencion1((String)registroCompra[20]);
		detalleComprasType.setSecRetencion1((Integer)registroCompra[21]);
		detalleComprasType.setAutRetencion1((String)registroCompra[22]);
		
		if (registroCompra[23] == null) {
			detalleComprasType.setFechaEmiRet1((String)registroCompra[23]);	
		} else {
			detalleComprasType.setFechaEmiRet1(sdf.format(registroCompra[23]));	
		}
		
		detalleComprasType.setDocModificado((String) registroCompra[24]);
		detalleComprasType.setEstabModificado((String)registroCompra[25]);
		detalleComprasType.setPtoEmiModificado((String)registroCompra[26]);
		detalleComprasType.setSecModificado((Integer)registroCompra[27]);
		detalleComprasType.setAutModificado((String)registroCompra[28]);
		
		return detalleComprasType;
		
	}
		
	//	Codigo quemado no se genera desde el sistema ningun dato para este nodo
	public PagoExteriorType generarPagoExterior() throws Exception{
		
		PagoExteriorType pagoExteriorType = new PagoExteriorType();
		
		pagoExteriorType.setPagoLocExt("01");
		pagoExteriorType.setPaisEfecPago("NA");
		pagoExteriorType.setAplicConvDobTrib(AplicConvDobTribType.NA);
		pagoExteriorType.setPagExtSujRetNorLeg(AplicConvDobTribType.NA);
		
		return pagoExteriorType;
		
	}
	
	public FormasDePagoType generarFormasDePago(Integer ingresoId) throws Exception{
		
		FormasDePagoType formasDePagoType =  new FormasDePagoType();
		
		List<Object[]> formasDePago = selectFormaPago(ingresoId);
		
		for (Object[] formaPago : formasDePago) {
			formasDePagoType.getFormaPago().add((String) formaPago[1]);			
		}
		
		return formasDePagoType;
	}


	public AirType generarAirTypeRenta(Integer ingresoId) throws Exception {
		
		String impuesto = "Renta";
		
		AirType airType = new AirType();
		
		List<Object[]> airs = selectAir(ingresoId, impuesto);
			
		DetalleAirComprasType detalleAirComprasType = new DetalleAirComprasType();
			
		for (Object[] detalleAir : airs) {
				
			detalleAirComprasType.setCodRetAir((String) detalleAir[1]);
			detalleAirComprasType.setBaseImpAir(((BigDecimal) detalleAir[2]).setScale(2, RoundingMode.HALF_UP));
			detalleAirComprasType.setPorcentajeAir((BigDecimal) detalleAir[3]);
			detalleAirComprasType.setValRetAir(((BigDecimal) detalleAir[4]).setScale(2, RoundingMode.HALF_UP));
				
			airType.getDetalleAir().add(detalleAirComprasType);
			
		}
		
		return airType;
	}
	
	public String buscarNumeroEstablecimientosString() throws Exception {
		
		Sucursal sucursal = new Sucursal();
		sucursal.setEstado(true);
		
		Integer numeroEstablecimientos = sucursalLista.numeroEstablecimientos(sucursal);

		return String.format("%03d" , numeroEstablecimientos);

	}

	public BigDecimal buscarLimiteFormaPago () throws Exception {
		
		BigDecimal limiteFormaPago = new BigDecimal(0);
		
		Parametro parametroLimite = parametroRegis.buscarPorId(Parametro.class, 6300);
		
		limiteFormaPago = new BigDecimal(parametroLimite.getDescri());
		
		return limiteFormaPago;
	}
		
	//	Falta pasar tipos de documentos
	public List<Object[]> selectDetalleCompras (LocalDate fechaDesd, LocalDate fechaHast) throws Exception {
		return ingresoLista.buscarIngresAtsSri(fechaDesd, fechaHast);
	}

	public List<Object[]> selectFormaPago(Integer ingresoId) throws Exception {
		return ingresoLista.buscarFormaPago(ingresoId);
	}
	
	public List<Object[]> selectAir(Integer ingresoId, String impuesto) throws Exception {
		return ingresoLista.buscarAir(ingresoId, impuesto);
	}
			
	public Object buscarAnulados(LocalDate fechaDesd, LocalDate fechaHast) {
		return new Object();
	}

	public ByteArrayOutputStream marshalIvaType(IvaType ivaType) throws Exception {

		JAXBContext context = JAXBContext.newInstance(new Class[] { IvaType.class });

		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty("jaxb.encoding", "UTF-8");
		marshaller.setProperty("jaxb.formatted.output", Boolean.valueOf(true));
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		OutputStreamWriter out = new OutputStreamWriter(byteArrayOutputStream, "UTF-8");
		
		marshaller.marshal(ivaType, out);
//		
//		Estas Lineas crean un archivo en la ruta especificada
//		OutputStream outputStream = new FileOutputStream("D:\\Snd\\ATS\\.xml");
//		byteArrayOutputStream.writeTo(outputStream);
		byteArrayOutputStream.close();
//		outputStream.close();
		return byteArrayOutputStream;
		
	}
}