package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import jakarta.ejb.Local;

@Local
public interface SucursalRegisInt {

	public Object insertar(Sucursal sucursal) throws Exception;

	public void modificar(Sucursal sucursal) throws Exception;

	public void eliminar(Sucursal sucursal) throws Exception;

	public Sucursal buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
