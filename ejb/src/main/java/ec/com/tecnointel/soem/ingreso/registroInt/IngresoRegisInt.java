package ec.com.tecnointel.soem.ingreso.registroInt;

import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import jakarta.ejb.Local;

@Local
public interface IngresoRegisInt {

	public Object insertar(Ingreso ingreso) throws Exception;

	public void modificar(Ingreso ingreso) throws Exception;

	public void eliminar(Ingreso ingreso) throws Exception;

	public Ingreso buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
