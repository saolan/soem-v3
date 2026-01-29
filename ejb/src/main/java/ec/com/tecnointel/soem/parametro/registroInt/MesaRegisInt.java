package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.Mesa;
import jakarta.ejb.Local;

@Local
public interface MesaRegisInt {

	public Object insertar(Mesa mesa) throws Exception;

	public void modificar(Mesa mesa) throws Exception;

	public void eliminar(Mesa mesa) throws Exception;

	public Mesa buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
