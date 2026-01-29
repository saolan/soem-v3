package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.CentCost;
import jakarta.ejb.Local;

@Local
public interface CentCostRegisInt {

	public Object insertar(CentCost centCost) throws Exception;

	public void modificar(CentCost centCost) throws Exception;

	public void eliminar(CentCost centCost) throws Exception;

	public CentCost buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
