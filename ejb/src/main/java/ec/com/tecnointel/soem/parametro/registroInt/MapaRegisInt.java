package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.Mapa;
import jakarta.ejb.Local;

@Local
public interface MapaRegisInt {

	public Object insertar(Mapa mapa) throws Exception;

	public void modificar(Mapa mapa) throws Exception;

	public void eliminar(Mapa mapa) throws Exception;

	public Mapa buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
