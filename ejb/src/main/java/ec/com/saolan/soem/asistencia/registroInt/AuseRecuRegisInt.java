package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.AuseRecu;
import jakarta.ejb.Local;

@Local
public interface AuseRecuRegisInt {

	public Object insertar(AuseRecu auseRecu) throws Exception;

	public void modificar(AuseRecu auseRecu) throws Exception;

	public void eliminar(AuseRecu auseRecu) throws Exception;

	public AuseRecu buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
