package ec.com.tecnointel.soem.atsSri.registroInt;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import jakarta.ejb.Local;

@Local
public interface GenerarAtsSriInt {
	public ByteArrayOutputStream generarAtsSriXml(Sucursal sucursal, LocalDate fechaDesd, LocalDate fechaHast) throws Exception;
}
