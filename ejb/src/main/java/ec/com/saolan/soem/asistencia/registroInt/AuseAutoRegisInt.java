package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.AuseAuto;
import jakarta.ejb.Local;

@Local
public interface AuseAutoRegisInt {

	public Object insertar(AuseAuto auseAuto) throws Exception;

	public void modificar(AuseAuto auseAuto) throws Exception;

	public void eliminar(AuseAuto auseAuto) throws Exception;

	public AuseAuto buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
