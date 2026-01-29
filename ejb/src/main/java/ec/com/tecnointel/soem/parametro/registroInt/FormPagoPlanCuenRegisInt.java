package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.FormPagoPlanCuen;
import jakarta.ejb.Local;

@Local
public interface FormPagoPlanCuenRegisInt {

	public Object insertar(FormPagoPlanCuen formPagoPlanCuen) throws Exception;

	public void modificar(FormPagoPlanCuen formPagoPlanCuen) throws Exception;

	public void eliminar(FormPagoPlanCuen formPagoPlanCuen) throws Exception;

	public FormPagoPlanCuen buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
