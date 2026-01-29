package ec.com.tecnointel.soem.inventario.registroInt;

import ec.com.tecnointel.soem.inventario.modelo.ProdSubp;
import jakarta.ejb.Local;

@Local
public interface ProdSubpRegisInt {

	public Object insertar(ProdSubp prodSubp) throws Exception;

	public void modificar(ProdSubp prodSubp) throws Exception;

	public void eliminar(ProdSubp prodSubp) throws Exception;

	public ProdSubp buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
