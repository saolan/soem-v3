package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.TranPlanDeta;
import jakarta.ejb.Local;

@Local
public interface TranPlanDetaRegisInt {

	public Object insertar(TranPlanDeta tranPlanDeta) throws Exception;

	public void modificar(TranPlanDeta tranPlanDeta) throws Exception;

	public void eliminar(TranPlanDeta tranPlanDeta) throws Exception;

	public TranPlanDeta buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
