package ec.com.saolan.soem.nomina.registroInt;

import ec.com.saolan.soem.nomina.modelo.Nomina;
import jakarta.ejb.Local;

@Local
public interface NominaRegisInt {

	public Object insertar(Nomina nomina) throws Exception;

	public void modificar(Nomina nomina) throws Exception;

	public void eliminar(Nomina nomina) throws Exception;

	public Nomina buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
