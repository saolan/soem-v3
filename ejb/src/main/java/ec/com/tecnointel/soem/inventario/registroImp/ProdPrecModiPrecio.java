package ec.com.tecnointel.soem.inventario.registroImp;

import java.math.BigDecimal;

import ec.com.tecnointel.soem.inventario.modelo.ProdPrec;
import jakarta.ejb.Local;

@Local	
public interface ProdPrecModiPrecio {

	ProdPrec calcularPrecio(ProdPrec prodPrec, BigDecimal porcentaje, int redondeo) throws Exception;

}
