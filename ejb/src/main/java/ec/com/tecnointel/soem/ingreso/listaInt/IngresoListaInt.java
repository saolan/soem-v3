package ec.com.tecnointel.soem.ingreso.listaInt;

import java.time.LocalDate;
import java.util.List;

import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import jakarta.ejb.Local;

@Local
public interface IngresoListaInt {

	public void filasPagina(int filasPagina);

	public void orden(String orden);

	public List<Ingreso> buscarTodo(String columna) throws Exception;

	public List<Ingreso> buscar(Ingreso ingreso, Integer pagina) throws Exception;

	public long contarRegistros(Ingreso ingreso) throws Exception;

	public List<Ingreso> buscarTransaccion(Ingreso ingreso) throws Exception;

	public List<Object[]> buscarIngresAtsSri(LocalDate fechaDesd, LocalDate fechaHast) throws Exception;

	public List<Object[]> buscarFormaPago(Integer ingresoId) throws Exception;

	public List<Object[]> buscarAir(Integer ingresoId, String impuesto) throws Exception;

	List<Ingreso> buscar2(Ingreso ingreso, LocalDate fechaEmisDesde, LocalDate fechaEmisHasta,
			Integer pagina) throws Exception;

	long contarRegistros2(Ingreso ingreso, LocalDate fechaEmisDesde, LocalDate fechaEmisHasta) throws Exception;

}
