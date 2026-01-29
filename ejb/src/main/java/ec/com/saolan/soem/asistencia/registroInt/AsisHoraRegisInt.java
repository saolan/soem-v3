package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.AsisHora;
import jakarta.ejb.Local;

@Local
public interface AsisHoraRegisInt {

	public Object insertar(AsisHora asisHora) throws Exception;

	public void modificar(AsisHora asisHora) throws Exception;

	public void eliminar(AsisHora asisHora) throws Exception;

	public AsisHora buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
