package ec.com.tecnointel.soem.inventario.registroInt;

import ec.com.tecnointel.soem.inventario.modelo.ProdBode;
import jakarta.ejb.Local;

@Local
public interface ProdBodeRegisInt {

	public Object insertar(ProdBode prodBode) throws Exception;

	public void modificar(ProdBode prodBode) throws Exception;

	public void eliminar(ProdBode prodBode) throws Exception;

	public ProdBode buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
