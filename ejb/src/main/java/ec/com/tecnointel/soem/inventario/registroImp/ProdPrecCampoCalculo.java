package ec.com.tecnointel.soem.inventario.registroImp;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ METHOD, FIELD, PARAMETER, TYPE })
public @interface ProdPrecCampoCalculo {
	
//	Con esta descripcion se puede poner @ProdPrecCampoCalculo("PRECIO_CON_IMPU")
//	al asignar la anotacion
	String value(); 
	
//	Con esta descripcion se puede poner @ProdPrecCampoCalculo(name = "PRECIO_CON_IMPU")
//	al asignar la anotacion
//	String name(); 
}
