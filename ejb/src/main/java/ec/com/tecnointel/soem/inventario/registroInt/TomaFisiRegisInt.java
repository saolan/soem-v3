package ec.com.tecnointel.soem.inventario.registroInt;

import ec.com.tecnointel.soem.inventario.modelo.TomaFisi;
import jakarta.ejb.Local;

@Local
public interface TomaFisiRegisInt {

	public Object insertar(TomaFisi tomaFisi) throws Exception;

	public void modificar(TomaFisi tomaFisi) throws Exception;

	public void eliminar(TomaFisi tomaFisi) throws Exception;

	public TomaFisi buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
