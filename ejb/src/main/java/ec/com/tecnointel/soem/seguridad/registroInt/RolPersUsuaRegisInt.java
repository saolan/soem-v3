package ec.com.tecnointel.soem.seguridad.registroInt;

import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import jakarta.ejb.Local;

@Local
public interface RolPersUsuaRegisInt {

	public Object insertar(RolPersUsua rolPersUsua) throws Exception;

	public void modificar(RolPersUsua rolPersUsua) throws Exception;

	public void eliminar(RolPersUsua rolPersUsua) throws Exception;

	public RolPersUsua buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
