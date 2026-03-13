package ec.com.saolan.soem.sri.infraestructura.unmarshaller;

public interface SriUnmarshaller<T> {
    T unmarshall(String xml);
}
