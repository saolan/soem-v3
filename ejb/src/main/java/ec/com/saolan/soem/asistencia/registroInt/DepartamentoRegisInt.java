package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.Departamento;
import jakarta.ejb.Local;

@Local
public interface DepartamentoRegisInt {

	public Object insertar(Departamento departamento) throws Exception;

	public void modificar(Departamento departamento) throws Exception;

	public void eliminar(Departamento departamento) throws Exception;

	public Departamento buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
