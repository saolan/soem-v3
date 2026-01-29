package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.SucuPrec;
import jakarta.ejb.Local;

@Local
public interface SucuPrecRegisInt {

	public Object insertar(SucuPrec sucuPrec) throws Exception;

	public void modificar(SucuPrec sucuPrec) throws Exception;

	public void eliminar(SucuPrec sucuPrec) throws Exception;

	public SucuPrec buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
