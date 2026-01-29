package ec.com.saolan.soem.nomina.registroInt;

import ec.com.saolan.soem.nomina.modelo.AcciPersTipo;
import jakarta.ejb.Local;

@Local
public interface AcciPersTipoRegisInt {

	public Object insertar(AcciPersTipo acciPersTipo) throws Exception;

	public void modificar(AcciPersTipo acciPersTipo) throws Exception;

	public void eliminar(AcciPersTipo acciPersTipo) throws Exception;

	public AcciPersTipo buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
