package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.AsisRegi;
import jakarta.ejb.Local;

@Local
public interface AsisRegiRegisInt {

	public Object insertar(AsisRegi asisRegi) throws Exception;

	public void modificar(AsisRegi asisRegi) throws Exception;

	public void eliminar(AsisRegi asisRegi) throws Exception;

	public AsisRegi buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
