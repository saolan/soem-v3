package ec.com.tecnointel.soem.parametro.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.parametro.modelo.DocuMoviIngr;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolDocu;
import jakarta.ejb.Local;

@Local
public interface DocuMoviIngrListaInt {

	public List<DocuMoviIngr> buscarTodo(String columna) throws Exception;

	public List<DocuMoviIngr> buscar(DocuMoviIngr docuFormPago, Integer pagina) throws Exception;
	
	public DocuMoviIngr buscarDocuMoviIngrPred(List<DocuMoviIngr> docuMoviIngrs, List<RolDocu> rolDocus) throws Exception;

	public long contarRegistros(DocuMoviIngr docuFormPago) throws Exception;

	public void imprimir(DocuMoviIngr docuFormPago) throws Exception;

	public void filasPagina(int filasPagina);

	public List<DocuMoviIngr> filtrarDocuMoviIngrs(List<DocuMoviIngr> docuMoviIngrs, PersUsua persUsuaSesion,
			List<RolDocu> rolDocus) throws Exception;
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
