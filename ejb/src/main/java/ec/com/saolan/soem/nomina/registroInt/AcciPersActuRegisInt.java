package ec.com.saolan.soem.nomina.registroInt;

import ec.com.saolan.soem.nomina.modelo.AcciPersActu;
import jakarta.ejb.Local;

@Local
public interface AcciPersActuRegisInt {

	public Object insertar(AcciPersActu acciPersActu) throws Exception;

	public void modificar(AcciPersActu acciPersActu) throws Exception;

	public void eliminar(AcciPersActu acciPersActu) throws Exception;

	public AcciPersActu buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
