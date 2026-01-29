package ec.com.saolan.soem.asistencia.listaInt;

import java.util.List;

import ec.com.saolan.soem.nomina.modelo.DocuNomi;
import jakarta.ejb.Local;

@Local
public interface DocuNomiListaInt {

	public List<DocuNomi> buscar(DocuNomi docuNomi, Integer pagina) throws Exception;

	public long contarRegistros(DocuNomi docuNomi) throws Exception;

	public void filasPagina(int filasPagina);
	
}
