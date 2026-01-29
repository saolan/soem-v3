package ec.com.tecnointel.soem.contabilidad.registroInt;

import java.util.List;

import ec.com.tecnointel.soem.caja.modelo.CajaMovi;
import ec.com.tecnointel.soem.parametro.modelo.DocuEgre;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import jakarta.ejb.Local;

@Local
public interface TransaccionCajaMoviInt {

	Integer contabilizarVentas(CajaMovi cajaMoviInicio, CajaMovi cajaMoviCierre, List<DocuEgre> docuEgres, Documento docuTran) throws Exception;

	Integer contabilizarCostoVentas(CajaMovi cajaMoviInicio, CajaMovi cajaMoviCierre, List<DocuEgre> docuEgres, Documento docuTran) throws Exception;

	Integer contabilizarCostoVentasNC(CajaMovi cajaMoviInicio, CajaMovi cajaMoviCierre, List<DocuEgre> docuEgres, Documento docuTran) throws Exception;
	
	Integer contabilizarCobros(CajaMovi cajaMoviInicio, CajaMovi cajaMoviCierre, List<DocuEgre> docuEgres, Documento docuTran) throws Exception;
	
	Integer contabilizarAnticipos(CajaMovi cajaMoviInicio, CajaMovi cajaMoviCierre, Documento documento) throws Exception;
	
	Integer contabilizarNotasCredito(CajaMovi cajaMoviInicio, CajaMovi cajaMoviCierre, List<DocuEgre> docuEgres, Documento docuTran) throws Exception;

	List<Object[]> sumarDepositos(Integer cajaMoviId, Integer factor) throws Exception;
}
