package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import jakarta.ejb.Local;

@Local
public interface ParametroRegisInt {

	public Object insertar(Parametro parametro) throws Exception;

	public void modificar(Parametro parametro) throws Exception;

	public void eliminar(Parametro parametro) throws Exception;

	public Parametro buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
