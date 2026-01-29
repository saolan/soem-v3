package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.Cargo;
import jakarta.ejb.Local;

@Local
public interface CargoRegisInt {

	public Object insertar(Cargo cargo) throws Exception;

	public void modificar(Cargo cargo) throws Exception;

	public void eliminar(Cargo cargo) throws Exception;

	public Cargo buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
