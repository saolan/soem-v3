package ec.com.tecnointel.soem.caja.registroInt;

import ec.com.tecnointel.soem.caja.modelo.Periferico;
import jakarta.ejb.Local;

@Local
public interface PerifericoRegisInt {

	public Object insertar(Periferico periferico) throws Exception;

	public void modificar(Periferico periferico) throws Exception;

	public void eliminar(Periferico periferico) throws Exception;

	public Periferico buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
