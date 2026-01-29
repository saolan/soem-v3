package ec.com.tecnointel.soem.inventario.registroInt;

import ec.com.tecnointel.soem.inventario.modelo.Kardex;
import jakarta.ejb.Local;

@Local
public interface KardexRegisInt {

	public Object insertar(Kardex kardex) throws Exception;

	public void modificar(Kardex kardex) throws Exception;

	public void eliminar(Kardex kardex) throws Exception;

	public Kardex buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
