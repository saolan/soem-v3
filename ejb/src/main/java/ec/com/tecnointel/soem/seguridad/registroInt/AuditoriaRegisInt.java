package ec.com.tecnointel.soem.seguridad.registroInt;

import ec.com.tecnointel.soem.seguridad.modelo.Auditoria;
import jakarta.ejb.Local;

@Local
public interface AuditoriaRegisInt {

	public Object insertar(Auditoria auditoria) throws Exception;

	public void modificar(Auditoria auditoria) throws Exception;

	public void eliminar(Auditoria auditoria) throws Exception;

	public Auditoria buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
