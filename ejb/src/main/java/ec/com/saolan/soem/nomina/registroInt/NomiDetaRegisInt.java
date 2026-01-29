package ec.com.saolan.soem.nomina.registroInt;

import ec.com.saolan.soem.nomina.modelo.NomiDeta;
import jakarta.ejb.Local;

@Local
public interface NomiDetaRegisInt {

	public Object insertar(NomiDeta nomiMovi) throws Exception;

	public void modificar(NomiDeta nomiMovi) throws Exception;

	public void eliminar(NomiDeta nomiMovi) throws Exception;

	public NomiDeta buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
