package ec.com.tecnointel.soem.inventario.registroInt;

import ec.com.tecnointel.soem.inventario.modelo.ProdTipo;
import jakarta.ejb.Local;

@Local
public interface ProdTipoRegisInt {

	public Object insertar(ProdTipo prodTipo) throws Exception;

	public void modificar(ProdTipo prodTipo) throws Exception;

	public void eliminar(ProdTipo prodTipo) throws Exception;

	public ProdTipo buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
