package ec.com.tecnointel.soem.inventario.registroInt;

import ec.com.tecnointel.soem.inventario.modelo.ProdGrup;
import jakarta.ejb.Local;

@Local
public interface ProdGrupRegisInt {

	public Object insertar(ProdGrup prodGrup) throws Exception;

	public void modificar(ProdGrup prodGrup) throws Exception;

	public void eliminar(ProdGrup prodGrup) throws Exception;

	public ProdGrup buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
