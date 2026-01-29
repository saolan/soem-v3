package ec.com.tecnointel.soem.contabilidad.registroInt;

import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuen;
import jakarta.ejb.Local;

@Local
public interface PlanCuenRegisInt {

	public Object insertar(PlanCuen planCuen) throws Exception;

	public void modificar(PlanCuen planCuen) throws Exception;

	public void eliminar(PlanCuen planCuen) throws Exception;

	public PlanCuen buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
