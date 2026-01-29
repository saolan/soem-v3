package ec.com.tecnointel.soem.seguridad.registroInt;

import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import jakarta.ejb.Local;

@Local
public interface RolRegisInt {

	public Object insertar(Rol rol) throws Exception;

	public void modificar(Rol rol) throws Exception;

	public void eliminar(Rol rol) throws Exception;

	public Rol buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
