package ec.com.tecnointel.soem.seguridad.registroInt;

import ec.com.tecnointel.soem.seguridad.modelo.RolBode;
import jakarta.ejb.Local;

@Local
public interface RolBodeRegisInt {

	public Object insertar(RolBode rolBode) throws Exception;

	public void modificar(RolBode rolBode) throws Exception;

	public void eliminar(RolBode rolBode) throws Exception;

	public RolBode buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
