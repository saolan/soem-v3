package ec.com.tecnointel.soem.ingreso.registroInt;

import ec.com.tecnointel.soem.ingreso.modelo.IngrDeta;
import jakarta.ejb.Local;

@Local
public interface IngrDetaRegisInt {

	public Object insertar(IngrDeta ingrDeta) throws Exception;

	public void modificar(IngrDeta ingrDeta) throws Exception;

	public void eliminar(IngrDeta ingrDeta) throws Exception;

	public IngrDeta buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
