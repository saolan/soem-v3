package ec.com.tecnointel.soem.contabilidad.registroInt;

import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuenNive;
import jakarta.ejb.Local;

@Local
public interface PlanCuenNiveRegisInt {

	public Object insertar(PlanCuenNive planCuenNive) throws Exception;

	public void modificar(PlanCuenNive planCuenNive) throws Exception;

	public void eliminar(PlanCuenNive planCuenNive) throws Exception;

	public PlanCuenNive buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
