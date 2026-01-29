package ec.com.tecnointel.soem.egreso.registroInt;

import ec.com.tecnointel.soem.egreso.modelo.EgreTran;
import jakarta.ejb.Local;

@Local
public interface EgreTranRegisInt {

	public Object insertar(EgreTran egreTran) throws Exception;

	public void modificar(EgreTran egreTran) throws Exception;

	public void eliminar(EgreTran egreTran) throws Exception;

	public EgreTran buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
