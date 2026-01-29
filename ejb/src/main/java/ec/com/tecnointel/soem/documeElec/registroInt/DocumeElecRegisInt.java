package ec.com.tecnointel.soem.documeElec.registroInt;

import java.time.LocalDate;
import java.util.List;

import ec.com.tecnointel.soem.documeElec.modelo.InfoTributaria;
import ec.com.tecnointel.soem.documeElec.modelo.factura.Factura;
import ec.com.tecnointel.soem.documeElec.modelo.guia.GuiaRemision;
import ec.com.tecnointel.soem.documeElec.modelo.liquiCompra.LiquidacionCompra;
import ec.com.tecnointel.soem.documeElec.modelo.notaCredito.NotaCredito;
import ec.com.tecnointel.soem.documeElec.modelo.notaDebito.NotaDebito;
import ec.com.tecnointel.soem.documeElec.modelo.retencion.ComprobanteRetencion;
import ec.com.tecnointel.soem.egreso.modelo.EgreDeta;
import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDeta;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import ec.com.tecnointel.soem.tesoreria.modelo.FpmeFormPago;
import jakarta.ejb.Local;

@Local
public interface DocumeElecRegisInt {

	public Object generarComprobanteElectronico(Egreso egreso, List<EgreDeta> egreDetaDataTable, String codigoIva,
			String codigoIce, String codigoIrbpnr, FpmeFormPago fpmeFormPago) throws Exception;

	public Object generarComprobanteElectronico(Ingreso ingreso, List<IngrDeta> ingrDetas,
			FormPagoMoviIngr formPagoMoviIngr, Retencion retencion, String codigoReteIva, String codigoReteRenta,
			String codigoReteIsd, String codigoIva, String codigoIce, String codigoIrbpnr);

	public Object generarComprobanteElectronicoLiquidacion(Ingreso ingreso, List<IngrDeta> ingrDetas,
			FormPagoMoviIngr formPagoMoviIngr, String codigoReteIva, String codigoReteRenta,
			String codigoReteIsd, String codigoIva, String codigoIce, String codigoIrbpnr);

	public String generarClaveAcceso(Sucursal sucursal, LocalDate fechaEmision, String codigoDocu, String serie1,
			String serie2, String numero, String ambien, String tipoEmis, String codiClav);

	public InfoTributaria cargarInfoTributaria(Sucursal sucursal, String codigoDocu, String serie1, String serie2,
			String numero, String ambien, String tipoEmis, String claveAcce);

	public String marshalFactura(Factura factura, String rutaGenerados) throws Exception;

	public String marshalNotaCredito(NotaCredito notaCredito, String rutaGenerados) throws Exception;

	public String marshalNotaDebito(NotaDebito notaDebito, String rutaGenerados) throws Exception;

	public String marshalGuiaRemision(GuiaRemision guiaRemision, String rutaGenerados) throws Exception;

	public String marshalRetencion(ComprobanteRetencion comprobanteRetencion, String rutaGenerados) throws Exception;

	public String marshalLiquidacion(LiquidacionCompra liquidacionCompra, String rutaGenerados) throws Exception;
	
	public void registrarDimmIva(Dimm dimm);

	public void leyenda1(String leyenda1);

	public void leyenda2(String leyenda2);

	public void msgInfoAdicional(String msgInfoAdicional);

	

}
