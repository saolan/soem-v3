package ec.com.tecnointel.soem.inventario.registroInt;

import ec.com.tecnointel.soem.inventario.modelo.TomaFisiDeta;
import jakarta.ejb.Local;

@Local
public interface TomaFisiDetaRegisInt {

	public Object insertar(TomaFisiDeta tomaFisiDeta) throws Exception;

	public void modificar(TomaFisiDeta tomaFisiDeta) throws Exception;

	public void eliminar(TomaFisiDeta tomaFisiDeta) throws Exception;

	public TomaFisiDeta buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
