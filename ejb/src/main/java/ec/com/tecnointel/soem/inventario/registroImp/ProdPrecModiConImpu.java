package ec.com.tecnointel.soem.inventario.registroImp;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ec.com.tecnointel.soem.inventario.modelo.ProdPrec;

@ProdPrecCampoCalculo("PRECIO_CON_IMPU")
public class ProdPrecModiConImpu implements ProdPrecModiPrecio {

	@Override
	public ProdPrec calcularPrecio(ProdPrec prodPrec, BigDecimal porcentaje, int redondeo) throws Exception {
		
		BigDecimal incremento = (prodPrec.getPrecioSinImpu().multiply(porcentaje)).divide(new BigDecimal(100), 6, RoundingMode.HALF_UP);
		
		prodPrec.setPrecioConImpu((prodPrec.getPrecioSinImpu().add(incremento)).setScale(redondeo, RoundingMode.HALF_UP));
		
		return prodPrec;
	}
}
