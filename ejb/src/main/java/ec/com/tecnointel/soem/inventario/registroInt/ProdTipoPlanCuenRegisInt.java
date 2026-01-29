package ec.com.tecnointel.soem.inventario.registroInt;

import ec.com.tecnointel.soem.inventario.modelo.ProdTipoPlanCuen;
import jakarta.ejb.Local;

@Local
public interface ProdTipoPlanCuenRegisInt {

	public Object insertar(ProdTipoPlanCuen prodTipoPlanCuen) throws Exception;

	public void modificar(ProdTipoPlanCuen prodTipoPlanCuen) throws Exception;

	public void eliminar(ProdTipoPlanCuen prodTipoPlanCuen) throws Exception;

	public ProdTipoPlanCuen buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
