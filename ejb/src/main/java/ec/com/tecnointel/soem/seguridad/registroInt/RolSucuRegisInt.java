package ec.com.tecnointel.soem.seguridad.registroInt;

import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import jakarta.ejb.Local;

@Local
public interface RolSucuRegisInt {

	public Object insertar(RolSucu rolSucu) throws Exception;

	public void modificar(RolSucu rolsucu) throws Exception;

	public void eliminar(RolSucu rolSucu) throws Exception;

	public RolSucu buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
