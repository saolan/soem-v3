package ec.com.tecnointel.soem.ingreso.listaInt;

import java.time.LocalDate;
import java.util.List;

import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import jakarta.ejb.Local;

@Local
public interface RetencionListaInt {

	public List<Retencion> buscarTodo(String columna) throws Exception;

	public List<Retencion> buscar(Retencion retencion, Integer pagina) throws Exception;

	public long contarRegistros(Retencion retencion) throws Exception;

	public void filasPagina(int filasPagina);

	List<Retencion> buscar2(Retencion retencion, LocalDate fechaEmisDesde, LocalDate fechaEmisHasta,
			Integer pagina) throws Exception;

	long contarRegistros2(Retencion retencion, LocalDate fechaEmisDesde, LocalDate fechaEmisHasta) throws Exception;
	
}
