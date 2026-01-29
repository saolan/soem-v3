package ec.com.tecnointel.soem.seguridad.registroInt;

import ec.com.tecnointel.soem.seguridad.modelo.RolPerm;
import jakarta.ejb.Local;

@Local
public interface RolPermRegisInt {

	public Object insertar(RolPerm rolPerm) throws Exception;

	public void modificar(RolPerm rolPerm) throws Exception;

	public void eliminar(RolPerm rolPerm) throws Exception;

	public RolPerm buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
