package ec.com.tecnointel.soem.inventario.registroInt;

import ec.com.tecnointel.soem.inventario.modelo.ProdDimm;
import jakarta.ejb.Local;

@Local
public interface ProdDimmRegisInt {

	public Object insertar(ProdDimm prodDimm) throws Exception;

	public void modificar(ProdDimm prodDimm) throws Exception;

	public void eliminar(ProdDimm prodDimm) throws Exception;

	public ProdDimm buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
