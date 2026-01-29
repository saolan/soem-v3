package ec.com.saolan.soem.nomina.listaInt;

import java.util.List;

import ec.com.saolan.soem.nomina.modelo.AcciPersActu;
import jakarta.ejb.Local;

@Local
public interface AcciPersActuListaInt {

	public List<AcciPersActu> buscar(AcciPersActu acciPersActu, Integer pagina) throws Exception;

	public long contarRegistros(AcciPersActu acciPersActu) throws Exception;

	public void filasPagina(int filasPagina);
	
}
