package ec.com.saolan.soem.nomina.registroInt;

import ec.com.saolan.soem.nomina.modelo.AcciPersProp;
import jakarta.ejb.Local;

@Local
public interface AcciPersPropRegisInt {

	public Object insertar(AcciPersProp acciPersProp) throws Exception;

	public void modificar(AcciPersProp acciPersProp) throws Exception;

	public void eliminar(AcciPersProp acciPersProp) throws Exception;

	public AcciPersProp buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
