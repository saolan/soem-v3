package ec.com.tecnointel.soem.egreso.registroInt;

import ec.com.tecnointel.soem.egreso.modelo.PersVend;
import jakarta.ejb.Local;

@Local
public interface PersVendRegisInt {

	public Object insertar(PersVend persVend) throws Exception;

	public void modificar(PersVend persVend) throws Exception;

	public void eliminar(PersVend persVend) throws Exception;

	public PersVend buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
