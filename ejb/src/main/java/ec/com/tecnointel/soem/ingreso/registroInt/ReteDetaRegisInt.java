package ec.com.tecnointel.soem.ingreso.registroInt;

import ec.com.tecnointel.soem.ingreso.modelo.ReteDeta;
import jakarta.ejb.Local;

@Local
public interface ReteDetaRegisInt {

	public Object insertar(ReteDeta reteDeta) throws Exception;

	public void modificar(ReteDeta reteDeta) throws Exception;

	public void eliminar(ReteDeta reteDeta) throws Exception;

	public ReteDeta buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
