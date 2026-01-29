package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import jakarta.ejb.Local;

@Local
public interface DimmRegisInt {

	public Object insertar(Dimm dimm) throws Exception;

	public void modificar(Dimm dimm) throws Exception;

	public void eliminar(Dimm dimm) throws Exception;

	public Dimm buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
