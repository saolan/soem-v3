package ec.com.tecnointel.soem.inventario.registroInt;

import ec.com.tecnointel.soem.inventario.modelo.ProdCost;
import jakarta.ejb.Local;

@Local
public interface ProdCostRegisInt {

	public Object insertar(ProdCost prodCost) throws Exception;

	public void modificar(ProdCost prodCost) throws Exception;

	public void eliminar(ProdCost prodCost) throws Exception;

	public ProdCost buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
