package ec.com.saolan.soem.asistencia.listaInt;

import java.util.List;

import ec.com.saolan.soem.asistencia.modelo.Departamento;
import jakarta.ejb.Local;

@Local
public interface DepartamentoListaInt {

	public List<Departamento> buscar(Departamento departamento, Integer pagina) throws Exception;

	public long contarRegistros(Departamento departamento) throws Exception;

	public void filasPagina(int filasPagina);
	
}
