package ec.com.tecnointel.soem.seguridad.registroInt;

import ec.com.tecnointel.soem.seguridad.modelo.Permiso;
import jakarta.ejb.Local;

@Local
public interface PermisoRegisInt {

	public Object insertar(Permiso permiso) throws Exception;

	public void modificar(Permiso permiso) throws Exception;

	public void eliminar(Permiso permiso) throws Exception;

	public Permiso buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
