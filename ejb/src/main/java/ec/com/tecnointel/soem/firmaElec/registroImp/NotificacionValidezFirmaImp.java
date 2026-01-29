package ec.com.tecnointel.soem.firmaElec.registroImp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import ec.com.tecnointel.soem.firmaElec.registroInt.NotificacionValidezFirmaInt;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.registroInt.ParametroRegisInt;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class NotificacionValidezFirmaImp implements NotificacionValidezFirmaInt {
	
	@Inject
	ParametroRegisInt parametroRegis;

	@Override
	public String notificacion(Sucursal sucursal) throws Exception  {
		
		String mensaje = "FIRMA_VALIDA";
		
		LocalDate fecha = LocalDate.now();
		Date fechaNotificacion = new Date();
		
		Parametro parametro = parametroRegis.buscarPorId(Parametro.class, 6251);
		fecha = fecha.plusDays(Long.parseLong(parametro.getDescri()));
		fechaNotificacion = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		Path path = Paths.get(sucursal.getSucuCertEmis().getRuta(), sucursal.getSucuCertEmis().getDescri());

		if (!Files.exists(path)) {
			return "Revisar Datos de Firma - SucuCertEmis";
		}

		RecuperarDatosCertEmis recuperarDatosCertEmis = new RecuperarDatosCertEmis(sucursal);
		X509Certificate signingCertificate = recuperarDatosCertEmis.recuperarDatos();
		
		if (fechaNotificacion.after(signingCertificate.getNotAfter())) {

			String pattern = "yyyy-MM-dd";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

			mensaje = "Su firma caduca el " + simpleDateFormat.format(signingCertificate.getNotAfter()) + ", realize la renovación";	
		}
		
		return mensaje;
	}	
}