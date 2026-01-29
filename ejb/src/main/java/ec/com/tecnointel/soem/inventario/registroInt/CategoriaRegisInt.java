package ec.com.tecnointel.soem.inventario.registroInt;

import ec.com.tecnointel.soem.inventario.modelo.Categoria;
import jakarta.ejb.Local;

@Local
public interface CategoriaRegisInt {

	public Object insertar(Categoria categoria) throws Exception;

	public void modificar(Categoria categoria) throws Exception;

	public void eliminar(Categoria categoria) throws Exception;

	public Categoria buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
