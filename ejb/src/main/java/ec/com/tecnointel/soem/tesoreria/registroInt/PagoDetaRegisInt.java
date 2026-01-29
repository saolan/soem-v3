package ec.com.tecnointel.soem.tesoreria.registroInt;

import ec.com.tecnointel.soem.tesoreria.modelo.PagoDeta;
import jakarta.ejb.Local;

@Local
public interface PagoDetaRegisInt {

	public Object insertar(PagoDeta pagoDeta) throws Exception;

	public void modificar(PagoDeta pagoDeta) throws Exception;

	public void eliminar(PagoDeta pagoDeta) throws Exception;

	public PagoDeta buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
