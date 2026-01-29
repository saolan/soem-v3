package ec.com.tecnointel.soem.caja.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.caja.modelo.CajaMovi;
import jakarta.ejb.Local;

@Local
public interface CajaMoviListaInt {

	public List<CajaMovi> buscarTodo(String columna) throws Exception;

	public List<CajaMovi> buscar(CajaMovi cajaMovi, Integer pagina) throws Exception;

	public long contarRegistros(CajaMovi cajaMovi) throws Exception;

	public void filasPagina(int filasPagina);
	

//	@Deprecated
//	public List<Object[]> sumarCxcs(Integer cajaMoviId, Integer factor) throws Exception;
//	
//	public List<Object[]> sumarCuentasCxcs(Integer cajaMoviId, DocuEgre docuEgre, String tipoTran) throws Exception;
//	
//	public List<Object[]> sumarVentas(Integer cajaMoviId, DocuEgre docuEgre, String tipoTran) throws Exception;
//	
//	public List<Object[]> sumarCostoVentas(Integer cajaMoviId, DocuEgre docuEgre, String tipoTran) throws Exception;
//	
//	public List<Object[]> sumarCobroFps(Integer cajaMoviId, DocuEgre docuEgre) throws Exception;
//
//	public List<Object[]> sumarCobroCxcs(Integer cajaMoviId, DocuEgre docuEgre) throws Exception;
//	
//	public List<Object[]> sumarCobroCxcsNcs(Integer cajaMoviId, DocuEgre docuEgre, String tipoTran) throws Exception;
//	
//
//	public List<Object[]> sumarFpmeAnticipos(Integer cajaMoviId, String esNulo) throws Exception;
//
//	public List<Object[]> sumarAnticipos(Integer cajaMoviId) throws Exception;
//
//	public List<Object[]> sumarDepositos(Integer cajaMoviId, Integer factor) throws Exception;

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
