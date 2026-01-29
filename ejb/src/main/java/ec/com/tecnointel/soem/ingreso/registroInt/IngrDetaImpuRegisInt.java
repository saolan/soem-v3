package ec.com.tecnointel.soem.ingreso.registroInt;

import ec.com.tecnointel.soem.ingreso.modelo.IngrDetaImpu;
import jakarta.ejb.Local;

@Local
public interface IngrDetaImpuRegisInt {

	public Object insertar(IngrDetaImpu ingrDetaImpu) throws Exception;

	public void modificar(IngrDetaImpu ingrDetaImpu) throws Exception;

	public void eliminar(IngrDetaImpu ingrDetaImpu) throws Exception;

	public IngrDetaImpu buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
