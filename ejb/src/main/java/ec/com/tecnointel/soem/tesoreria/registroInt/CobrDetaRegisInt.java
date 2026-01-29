package ec.com.tecnointel.soem.tesoreria.registroInt;

import ec.com.tecnointel.soem.tesoreria.modelo.CobrDeta;
import jakarta.ejb.Local;

@Local
public interface CobrDetaRegisInt {

	public Object insertar(CobrDeta cobrDeta) throws Exception;

	public void modificar(CobrDeta cobrDeta) throws Exception;

	public void eliminar(CobrDeta cobrDeta) throws Exception;

	public CobrDeta buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
