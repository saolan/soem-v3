package ec.com.saolan.soem.nomina.registroInt;

import ec.com.saolan.soem.nomina.modelo.AcciPers;
import jakarta.ejb.Local;

@Local
public interface AcciPersRegisInt {

	public Object insertar(AcciPers acciPers) throws Exception;

	public void modificar(AcciPers acciPers) throws Exception;

	public void eliminar(AcciPers acciPers) throws Exception;

	public AcciPers buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
