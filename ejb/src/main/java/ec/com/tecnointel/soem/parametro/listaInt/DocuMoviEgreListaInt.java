package ec.com.tecnointel.soem.parametro.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.parametro.modelo.DocuMoviEgre;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolDocu;
import jakarta.ejb.Local;

@Local
public interface DocuMoviEgreListaInt {

	public List<DocuMoviEgre> buscarTodo(String columna) throws Exception;

	public List<DocuMoviEgre> buscar(DocuMoviEgre docuFormPago, Integer pagina) throws Exception;
	
	public DocuMoviEgre buscarDocuMoviEgrePred(List<DocuMoviEgre> docuMoviEgres, List<RolDocu> rolDocus) throws Exception;

	public long contarRegistros(DocuMoviEgre docuFormPago) throws Exception;

	public void filasPagina(int filasPagina);

	public List<DocuMoviEgre> filtrarDocuMoviEgres(List<DocuMoviEgre> docuMoviEgres, PersUsua persUsuaSesion,
			List<RolDocu> rolDocus) throws Exception;

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
