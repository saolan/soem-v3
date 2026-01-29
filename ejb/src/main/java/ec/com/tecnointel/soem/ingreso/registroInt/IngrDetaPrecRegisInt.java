package ec.com.tecnointel.soem.ingreso.registroInt;

import ec.com.tecnointel.soem.ingreso.modelo.IngrDetaPrec;
import jakarta.ejb.Local;

@Local
public interface IngrDetaPrecRegisInt {

	public Object insertar(IngrDetaPrec IngrDetaPrec) throws Exception;

	public void modificar(IngrDetaPrec IngrDetaPrec) throws Exception;

	public void eliminar(IngrDetaPrec IngrDetaPrec) throws Exception;

	public IngrDetaPrec buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
