package ec.com.tecnointel.soem.documeElec.tareas;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import ec.com.tecnointel.soem.notificacion.correo.GestorCorreoInt;
import ec.com.tecnointel.soem.notificacion.correo.VariablesConfiguracion;
import jakarta.inject.Inject;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;

public class TareaEnviarCorreoDocu implements Runnable {

	private String destinatarios;
	private String claveAcceso;

	@Inject
	GestorCorreoInt gestorCorreo;
	
	@Inject
	VariablesConfiguracion variablesConfiguracion;
	
	@Override
	public void run() {
	
//		Se quita estas validaciones porque se pasaron a la clase GestorCorreoImp
//		al momento de adjuntar cada uno de los archivos
//    	File archivoXml = new File(variablesConfiguracion.getRutaAutorizados() + claveAcceso + ".xml");
//		File archivoPdf = new File(variablesConfiguracion.getRutaAutorizados() + claveAcceso + ".pdf");
    	
//		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<< archivoXml " + archivoXml);
//		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<< archivoPdf " + archivoPdf);
//		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<< destinatarios " + destinatarios);
		
//		if (archivoXml.exists() && archivoPdf.exists() && 
//				!this.destinatarios.isEmpty() && this.destinatarios != null) {
    		this.enviarCorreo();	
//    	}
	}

	public void enviarCorreo() {

		try {

			String[] parametrosSmtp = variablesConfiguracion.getParametrosSmtp().toString().split(",");
			String[] usuarioCorreo = variablesConfiguracion.getUsuarioCorreo().toString().split(",");
			String[] cuerpoCorreo = { 
					variablesConfiguracion.getSubject(),
					variablesConfiguracion.getLinea1(),
					variablesConfiguracion.getLinea2(),
					variablesConfiguracion.getLinea3(),
					variablesConfiguracion.getRutaImagenCabecera(),
					variablesConfiguracion.getRutaImagenPie()
					};
			String rutaAutorizados = variablesConfiguracion.getRutaAutorizados();
			String nombreArchivo = this.claveAcceso;
			String destinatarios = this.destinatarios;

			gestorCorreo.enviarCorreo(parametrosSmtp, usuarioCorreo, cuerpoCorreo, rutaAutorizados, nombreArchivo,
					destinatarios);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public String getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(String destinatarios) {
		this.destinatarios = destinatarios;
	}
	
	public String getClaveAcceso() {
		return claveAcceso;
	}

	public void setClaveAcceso(String claveAcceso) {
		this.claveAcceso = claveAcceso;
	}

}
