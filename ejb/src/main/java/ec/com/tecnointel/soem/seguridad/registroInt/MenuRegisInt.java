package ec.com.tecnointel.soem.seguridad.registroInt;

import ec.com.tecnointel.soem.seguridad.modelo.Menu;
import jakarta.ejb.Local;

@Local
public interface MenuRegisInt {

	public Object insertar(Menu menu) throws Exception;

	public void modificar(Menu menu) throws Exception;

	public void eliminar(Menu menu) throws Exception;

	public Menu buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
