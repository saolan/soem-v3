package ec.com.tecnointel.soem.egreso.registroInt;

import ec.com.tecnointel.soem.egreso.modelo.ClieGrupPlanCuen;
import jakarta.ejb.Local;

@Local
public interface ClieGrupPlanCuenRegisInt {

	public Object insertar(ClieGrupPlanCuen clieGrupPlanCuen) throws Exception;

	public void modificar(ClieGrupPlanCuen clieGrupPlanCuen) throws Exception;

	public void eliminar(ClieGrupPlanCuen clieGrupPlanCuen) throws Exception;

	public ClieGrupPlanCuen buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
