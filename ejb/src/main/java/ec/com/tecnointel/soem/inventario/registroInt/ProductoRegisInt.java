package ec.com.tecnointel.soem.inventario.registroInt;

import ec.com.tecnointel.soem.inventario.modelo.Producto;
import jakarta.ejb.Local;

@Local
public interface ProductoRegisInt {

	public Object insertar(Producto producto) throws Exception;

	public void modificar(Producto producto) throws Exception;

	public void eliminar(Producto producto) throws Exception;

	public Producto buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
