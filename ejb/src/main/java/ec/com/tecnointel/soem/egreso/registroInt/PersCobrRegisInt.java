package ec.com.tecnointel.soem.egreso.registroInt;

import ec.com.tecnointel.soem.egreso.modelo.PersCobr;
import jakarta.ejb.Local;

@Local
public interface PersCobrRegisInt {

	public Object insertar(PersCobr persCobr) throws Exception;

	public void modificar(PersCobr persCobr) throws Exception;

	public void eliminar(PersCobr persCobr) throws Exception;

	public PersCobr buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
