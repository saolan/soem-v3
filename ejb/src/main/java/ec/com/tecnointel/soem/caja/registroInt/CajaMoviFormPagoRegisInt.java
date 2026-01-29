package ec.com.tecnointel.soem.caja.registroInt;

import ec.com.tecnointel.soem.caja.modelo.CajaMoviFormPago;
import jakarta.ejb.Local;

@Local
public interface CajaMoviFormPagoRegisInt {

	public Object insertar(CajaMoviFormPago cajaMoviFormPago) throws Exception;

	public void modificar(CajaMoviFormPago cajaMoviFormPago) throws Exception;

	public void eliminar(CajaMoviFormPago cajaMoviFormPago) throws Exception;

	public CajaMoviFormPago buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
