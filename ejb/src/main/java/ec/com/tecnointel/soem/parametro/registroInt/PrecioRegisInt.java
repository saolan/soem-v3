package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.Precio;
import jakarta.ejb.Local;

@Local
public interface PrecioRegisInt {

	public Object insertar(Precio precio) throws Exception;

	public void modificar(Precio precio) throws Exception;

	public void eliminar(Precio precio) throws Exception;

	public Precio buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
