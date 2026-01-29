package ec.com.tecnointel.soem.caja.registroInt;

import ec.com.tecnointel.soem.caja.modelo.CajaMoviTran;
import jakarta.ejb.Local;

@Local
public interface CajaMoviTranRegisInt {

	public Object insertar(CajaMoviTran cajaMoviTran) throws Exception;

	public void modificar(CajaMoviTran cajaMoviTran) throws Exception;

	public void eliminar(CajaMoviTran cajaMoviTran) throws Exception;

	public CajaMoviTran buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
