package ec.com.tecnointel.soem.egreso.registroInt;

import ec.com.tecnointel.soem.egreso.modelo.EgreDeta;
import jakarta.ejb.Local;

@Local
public interface EgreDetaRegisInt {

	public Object insertar(EgreDeta egreDeta) throws Exception;

	public void modificar(EgreDeta egreDeta) throws Exception;

	public void eliminar(EgreDeta egreDeta) throws Exception;

	public EgreDeta buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
