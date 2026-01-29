package ec.com.tecnointel.soem.caja.registroInt;

import ec.com.tecnointel.soem.caja.modelo.PersCaje;
import jakarta.ejb.Local;

@Local
public interface PersCajeRegisInt {

	public Object insertar(PersCaje persCaje) throws Exception;

	public void modificar(PersCaje persCaje) throws Exception;

	public void eliminar(PersCaje persCaje) throws Exception;

	public PersCaje buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
