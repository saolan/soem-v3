package ec.com.saolan.soem.sri.infraestructura.importacion;

public interface ImportarDocumeElecSri<T> {
	T importar(String claveAcceso, ImportarDocumeElecSriParametros importarDocumeElecSriParametros);
}
