package ec.com.tecnointel.soem.notificacion.correo;

import java.io.IOException;

import jakarta.mail.MessagingException;

public interface GestorCorreoInt {

	public void enviarCorreo(String[] parametrosSmtp, String[] usuarioCorreo, String[] textoCorreo, String rutaAutorizados,
			String nombreArchivo, String destinatarios) throws IOException, MessagingException;

}
