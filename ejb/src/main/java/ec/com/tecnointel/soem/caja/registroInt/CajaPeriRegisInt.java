package ec.com.tecnointel.soem.caja.registroInt;

import ec.com.tecnointel.soem.caja.modelo.CajaPeri;
import jakarta.ejb.Local;

@Local
public interface CajaPeriRegisInt {

	public Object insertar(CajaPeri cajaPeri) throws Exception;

	public void modificar(CajaPeri cajaPeri) throws Exception;

	public void eliminar(CajaPeri cajaPeri) throws Exception;

	public CajaPeri buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
