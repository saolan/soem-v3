package ec.com.tecnointel.soem.seguridad.registroInt;

import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import jakarta.ejb.Local;

@Local
public interface RolPrecRegisInt {

	public Object insertar(RolPrec rolPrec) throws Exception;

	public void modificar(RolPrec rolPrec) throws Exception;

	public void eliminar(RolPrec rolPrec) throws Exception;

	public RolPrec buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
