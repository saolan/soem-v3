package ec.com.tecnointel.soem.inventario.registroInt;

import ec.com.tecnointel.soem.inventario.modelo.ProdGrupNive;
import jakarta.ejb.Local;

@Local
public interface ProdGrupNiveRegisInt {

	public Object insertar(ProdGrupNive prodGrupNive) throws Exception;

	public void modificar(ProdGrupNive prodGrupNive) throws Exception;

	public void eliminar(ProdGrupNive prodGrupNive) throws Exception;

	public ProdGrupNive buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
