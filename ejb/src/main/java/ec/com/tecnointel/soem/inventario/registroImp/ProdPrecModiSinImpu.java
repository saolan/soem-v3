package ec.com.tecnointel.soem.inventario.registroImp;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ec.com.tecnointel.soem.inventario.modelo.ProdPrec;

@ProdPrecCampoCalculo("PRECIO_SIN_IMPU")
public class ProdPrecModiSinImpu implements ProdPrecModiPrecio {

	@Override
	public ProdPrec calcularPrecio(ProdPrec prodPrec, BigDecimal porcentaje, int redondeo) throws Exception {

		BigDecimal incremento = (porcentaje.divide(new BigDecimal(100))).add(new BigDecimal(1));
		
		prodPrec.setPrecioSinImpu(prodPrec.getPrecioConImpu().divide(incremento,redondeo,RoundingMode.HALF_UP));
		
		return prodPrec;
	}
}
