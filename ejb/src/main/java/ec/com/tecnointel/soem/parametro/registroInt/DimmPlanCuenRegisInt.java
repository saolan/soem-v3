package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.DimmPlanCuen;
import jakarta.ejb.Local;

@Local
public interface DimmPlanCuenRegisInt {

	public Object insertar(DimmPlanCuen dimmPlanCuen) throws Exception;

	public void modificar(DimmPlanCuen dimmPlanCuen) throws Exception;

	public void eliminar(DimmPlanCuen dimmPlanCuen) throws Exception;

	public DimmPlanCuen buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
