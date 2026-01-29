package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.PersPara;
import jakarta.ejb.Local;

@Local
public interface PersParaRegisInt {

	public Object insertar(PersPara persPara) throws Exception;

	public void modificar(PersPara persPara) throws Exception;

	public void eliminar(PersPara persPara) throws Exception;

	public PersPara buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
