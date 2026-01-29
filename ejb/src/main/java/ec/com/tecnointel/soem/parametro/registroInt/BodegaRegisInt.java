package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.Bodega;
import jakarta.ejb.Local;

@Local
public interface BodegaRegisInt {

	public Object insertar(Bodega bodega) throws Exception;

	public void modificar(Bodega bodega) throws Exception;

	public void eliminar(Bodega bodega) throws Exception;

	public Bodega buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
