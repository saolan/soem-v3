package ec.com.tecnointel.soem.inventario.registroInt;

import ec.com.tecnointel.soem.inventario.modelo.ProdPrec;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import jakarta.ejb.Local;

@Local
public interface ProdPrecRegisInt {

	public Object insertar(ProdPrec prodPrec) throws Exception;

	public void modificar(ProdPrec prodPrec) throws Exception;

	public void eliminar(ProdPrec prodPrec) throws Exception;

	public ProdPrec buscarPorId(Class<?> entidad, Integer id) throws Exception;

	/**
	 * Calcula nuevo precio por cambio de IVA
	*/
	ProdPrec calcularPrecio(String campoCalculo, ProdPrec prodPrec, Dimm dimm, int redondeo) throws Exception;
}
