package ec.com.tecnointel.soem.seguridad.registroInt;

import ec.com.tecnointel.soem.seguridad.modelo.RolMenu;
import jakarta.ejb.Local;

@Local
public interface RolMenuRegisInt {

	public Object insertar(RolMenu rolMenu) throws Exception;

	public void modificar(RolMenu rolMenu) throws Exception;

	public void eliminar(RolMenu rolMenu) throws Exception;

	public RolMenu buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
