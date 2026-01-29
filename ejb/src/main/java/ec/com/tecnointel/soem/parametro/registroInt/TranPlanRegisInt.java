package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.TranPlan;
import jakarta.ejb.Local;

@Local
public interface TranPlanRegisInt {

	public Object insertar(TranPlan tranPlan) throws Exception;

	public void modificar(TranPlan tranPlan) throws Exception;

	public void eliminar(TranPlan tranPlan) throws Exception;

	public TranPlan buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
