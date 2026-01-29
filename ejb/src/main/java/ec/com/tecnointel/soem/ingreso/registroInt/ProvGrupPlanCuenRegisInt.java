package ec.com.tecnointel.soem.ingreso.registroInt;

import ec.com.tecnointel.soem.ingreso.modelo.ProvGrupPlanCuen;
import jakarta.ejb.Local;

@Local
public interface ProvGrupPlanCuenRegisInt {

	public Object insertar(ProvGrupPlanCuen provGrupPlanCuen) throws Exception;

	public void modificar(ProvGrupPlanCuen provGrupPlanCuen) throws Exception;

	public void eliminar(ProvGrupPlanCuen provGrupPlanCuen) throws Exception;

	public ProvGrupPlanCuen buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
