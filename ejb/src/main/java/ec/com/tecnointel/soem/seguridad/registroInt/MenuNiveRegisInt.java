package ec.com.tecnointel.soem.seguridad.registroInt;

import ec.com.tecnointel.soem.seguridad.modelo.MenuNive;
import jakarta.ejb.Local;

@Local
public interface MenuNiveRegisInt {

	public Object insertar(MenuNive menuNive) throws Exception;

	public void modificar(MenuNive menuNive) throws Exception;

	public void eliminar(MenuNive menuNive) throws Exception;

	public MenuNive buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
