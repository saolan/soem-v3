package ec.com.tecnointel.soem.contabilidad.registroInt;

import ec.com.tecnointel.soem.contabilidad.modelo.TranDeta;
import jakarta.ejb.Local;

@Local
public interface TranDetaRegisInt {

	public Object insertar(TranDeta tranDeta) throws Exception;

	public void modificar(TranDeta tranDeta) throws Exception;

	public void eliminar(TranDeta tranDeta) throws Exception;

	public TranDeta buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
