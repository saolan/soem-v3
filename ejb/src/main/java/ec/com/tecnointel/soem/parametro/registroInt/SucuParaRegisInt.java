package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.SucuPara;
import jakarta.ejb.Local;

@Local
public interface SucuParaRegisInt {

	public Object insertar(SucuPara sucuPara) throws Exception;

	public void modificar(SucuPara sucuPara) throws Exception;

	public void eliminar(SucuPara sucuPara) throws Exception;

	public SucuPara buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
