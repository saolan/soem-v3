package ec.com.tecnointel.soem.caja.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.caja.modelo.SaliArch;
import jakarta.ejb.Local;

@Local
public interface SaliArchListaInt {

	public List<SaliArch> buscar(SaliArch saliArch, Integer pagina) throws Exception;

	public long contarRegistros(SaliArch saliArch) throws Exception;

	public void filasPagina(int filasPagina);
	
}
