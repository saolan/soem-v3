package ec.com.tecnointel.soem.tesoreria.registroInt;

import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import jakarta.ejb.Local;

@Local
public interface FormPagoMoviIngrRegisInt {

	public Object insertar(FormPagoMoviIngr formPagoMoviIngr) throws Exception;

	public void modificar(FormPagoMoviIngr formPagoMoviIngr) throws Exception;

	public void eliminar(FormPagoMoviIngr formPagoMoviIngr) throws Exception;

	public FormPagoMoviIngr buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
