package ec.com.tecnointel.soem.ingreso.registroInt;

import ec.com.tecnointel.soem.ingreso.modelo.IngrDimm;
import jakarta.ejb.Local;

@Local
public interface IngrDimmRegisInt {

	public Object insertar(IngrDimm ingrDimm) throws Exception;

	public void modificar(IngrDimm ingrDimm) throws Exception;

	public void eliminar(IngrDimm ingrDimm) throws Exception;

	public IngrDimm buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
