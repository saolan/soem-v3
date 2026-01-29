package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.SucuBode;
import jakarta.ejb.Local;

@Local
public interface SucuBodeRegisInt {

	public Object insertar(SucuBode sucuBode) throws Exception;

	public void modificar(SucuBode sucuBode) throws Exception;

	public void eliminar(SucuBode sucuBode) throws Exception;

	public SucuBode buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
