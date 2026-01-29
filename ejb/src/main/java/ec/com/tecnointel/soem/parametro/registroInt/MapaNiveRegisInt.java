package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.MapaNive;
import jakarta.ejb.Local;

@Local
public interface MapaNiveRegisInt {

	public Object insertar(MapaNive mapaNive) throws Exception;

	public void modificar(MapaNive mapaNive) throws Exception;

	public void eliminar(MapaNive mapaNive) throws Exception;

	public MapaNive buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
