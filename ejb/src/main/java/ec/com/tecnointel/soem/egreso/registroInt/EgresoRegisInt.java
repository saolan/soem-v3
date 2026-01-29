package ec.com.tecnointel.soem.egreso.registroInt;

import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import jakarta.ejb.Local;

@Local
public interface EgresoRegisInt {

	public Object insertar(Egreso egreso) throws Exception;

	public void modificar(Egreso egreso) throws Exception;

	public void eliminar(Egreso egreso) throws Exception;

	public Egreso buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
