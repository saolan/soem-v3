package ec.com.tecnointel.soem.seguridad.registroInt;

import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import jakarta.ejb.Local;

@Local
public interface PersUsuaRegisInt {

	public Object insertar(PersUsua persUsua) throws Exception;

	public void modificar(PersUsua persUsua) throws Exception;

	public void eliminar(PersUsua persUsua) throws Exception;

	public PersUsua buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
