package ec.com.tecnointel.soem.inventario.registroInt;

import ec.com.tecnointel.soem.inventario.modelo.ProdGrupPlanCuen;
import jakarta.ejb.Local;

@Local
public interface ProdGrupPlanCuenRegisInt {

	public Object insertar(ProdGrupPlanCuen prodGrupPlanCuen) throws Exception;

	public void modificar(ProdGrupPlanCuen prodGrupPlanCuen) throws Exception;

	public void eliminar(ProdGrupPlanCuen prodGrupPlanCuen) throws Exception;

	public ProdGrupPlanCuen buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
