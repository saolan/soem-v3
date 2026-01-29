package ec.com.tecnointel.soem.ingreso.registroInt;

import ec.com.tecnointel.soem.ingreso.modelo.PersProv;
import jakarta.ejb.Local;

@Local
public interface PersProvRegisInt {

	public Object insertar(PersProv persProv) throws Exception;

	public void modificar(PersProv persProv) throws Exception;

	public void eliminar(PersProv persProv) throws Exception;

	public PersProv buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
