package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.TipoCont;
import jakarta.ejb.Local;

@Local
public interface TipoContRegisInt {

	public Object insertar(TipoCont tipoCont) throws Exception;

	public void modificar(TipoCont tipoCont) throws Exception;

	public void eliminar(TipoCont tipoCont) throws Exception;

	public TipoCont buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
