package ec.com.saolan.soem.sri.infraestructura.aplicacion.compartido;

public interface ImportarDocumeElecSri<T> {
	T importar(String claveAcceso, ImportarDocumeElecSriParametros importarDocumeElecSriParametros);
}
