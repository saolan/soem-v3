package ec.com.tecnointel.soem.notificacion.correo;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.Authenticator;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class GestorCorreoImp implements Serializable, GestorCorreoInt {

	private String host;
	private String puerto;
	private String startTls;
	private String auth;

	private String usuario;
	private String direccion;
	private String alias;
	private String clave;

	Session session;

	private static final long serialVersionUID = -5976925298378320858L;

	@Override
	public void enviarCorreo(String[] parametrosSmtp, String[] usuarioCorreo, String[] cuerpoCorreo,
			String rutaAutorizados, String nombreArchivo, String destinatarios) throws IOException, MessagingException {

		// if (!this.certificados.equals(null)) {
		// System.setProperty("javax.net.ssl.trustStore", this.certificados);
		// System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
		// }
		// for encrypted responses
		// IssuerAndSerialNumber issAndSer = new IssuerAndSerialNumber(
		// new X500Name(signerCert.getSubjectDN().getName()),
		// signerCert.getSerialNumber());
		// signedAttrs.add(new
		// SMIMEEncryptionKeyPreferenceAttribute(issAndSer));
		// SMIMESignedGenerator gen = new SMIMESignedGenerator();
		// gen.addSignerInfoGenerator(createSignerInfoGenerator(signedAttrs));
		// Store certs = new JcaCertStore(Arrays.asList(signerCert));
		// gen.addCertificates(certs);

		this.configurarSesion(parametrosSmtp, usuarioCorreo);
		Message message = this.crearMensaje(cuerpoCorreo, rutaAutorizados, nombreArchivo, destinatarios);
		this.enviarMensaje(message);
	}

	public void configurarSesion(String[] parametrosSmtp, String[] usuarioCorreo) {

		Properties properties = new Properties();

		this.host = parametrosSmtp[0];
		this.puerto = parametrosSmtp[1];
		this.startTls = parametrosSmtp[2];
		this.auth = parametrosSmtp[3];

		this.usuario = usuarioCorreo[0];
		this.direccion = usuarioCorreo[1];
		this.alias = usuarioCorreo[2];
		this.clave = usuarioCorreo[3];

		properties.put("mail.smtp.host", host); // Servidor SMTP
		properties.put("mail.smtp.port", puerto); // Puerto SMTP
		properties.put("mail.smtp.starttls.enable", startTls); // Para conectar de manera segura al servidor SMTP
		properties.put("mail.smtp.auth", auth); // Usar autenticacion mediante usuario y clave

		Authenticator authenticator = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(direccion, clave);
			}
		};

		session = Session.getInstance(properties, authenticator);
		session.setDebug(true);
	}

	public Message crearMensaje(String[] cuerpoCorreo, String rutaAutorizados, String nombreArchivo,
			String destinatarios) throws IOException, MessagingException {

		StringBuilder destintariosFinal = new StringBuilder();
		StringBuilder sb = new StringBuilder(); // StringBuffer sb = new
												// StringBuffer();

		String subjet = cuerpoCorreo[0];
		String linea1 = cuerpoCorreo[1];
		String linea2 = cuerpoCorreo[2];
		String linea3 = cuerpoCorreo[3];
		String rutaImagenCabecera = cuerpoCorreo[4];
		String rutaImagenPie = cuerpoCorreo[5];

		String adjuntoPdf = rutaAutorizados + nombreArchivo + ".pdf";
		String adjuntoXml = rutaAutorizados + nombreArchivo + ".xml";

		MimeMessage message = new MimeMessage(session);

		destintariosFinal.append(destinatarios);

		InternetAddress fromAddress = null;
		fromAddress = new InternetAddress(this.direccion, this.alias);

		InternetAddress[] toAddress = null;
		toAddress = InternetAddress.parse(destintariosFinal.toString(), false);

		message.setFrom(fromAddress);
		message.addRecipients(Message.RecipientType.TO, toAddress);
		message.setSentDate(new Date());
		message.setSubject(subjet);

		sb.append("<HTML>\n");
		sb.append("<HEAD>\n");
		sb.append("<TITLE>\n");
		sb.append("</TITLE>\n");
		sb.append("</HEAD>\n");
		sb.append("<BODY>\n");

		if (!rutaImagenCabecera.equals("nulo")) {
			sb.append("<img src=\"cid:imageCab\">");
		}

		// sb.append("<h1>\n");
		// sb.append("</h1>\n");
		sb.append("<p>\n");
		sb.append(linea1 + "<br/>");
		if (linea2 != null) {
			sb.append(linea2 + "<br/>");
		}
		if (linea3 != null) {
			sb.append(linea3 + "<br/>");
		}

		sb.append("</p>\n");

		if (!rutaImagenPie.equals("nulo")) {
			sb.append("<img src=\"cid:imagePie\">");
		}

		sb.append("</BODY>\n");
		sb.append("</HTML>\n");

		MimeMultipart multipart = new MimeMultipart("related");

		// Añade el texto del correo
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(sb.toString(), "text/html");
		multipart.addBodyPart(messageBodyPart);

		if (!rutaImagenCabecera.equals("nulo")) {
			messageBodyPart = new MimeBodyPart();

			DataSource cabeceraDataSource = new FileDataSource(rutaImagenCabecera);
			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setDataHandler(new DataHandler(cabeceraDataSource));
			messageBodyPart.setHeader("Content-ID", "<imageCab>");
			multipart.addBodyPart(messageBodyPart);
		}

		if (!rutaImagenPie.equals("nulo")) {
			DataSource pieDataSource = new FileDataSource(rutaImagenPie);
			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setDataHandler(new DataHandler(pieDataSource));
			messageBodyPart.setHeader("Content-ID", "<imagePie>");
			multipart.addBodyPart(messageBodyPart);
		}

		MimeBodyPart mineBodyPart = new MimeBodyPart();

//		Valida si existen los archivos para poder adjuntar
    	File archivoXml = new File(adjuntoXml);
		File archivoPdf = new File(adjuntoPdf);
		
		if (archivoPdf.exists()) {
			mineBodyPart.attachFile(adjuntoPdf);
			multipart.addBodyPart(mineBodyPart);
		}

		if (archivoXml.exists()) { 
			mineBodyPart = new MimeBodyPart();
			mineBodyPart.attachFile(adjuntoXml);
			multipart.addBodyPart(mineBodyPart);
		}

		message.setContent(multipart);

		return message;
	}

	public void enviarMensaje(Message message) throws MessagingException {
		Transport transport = null;

		if (transport == null) {
			transport = session.getTransport("smtp");
			// transport = message.getSession().getTransport("smtp");
		}

		if (!transport.isConnected()) {
			transport.connect(this.host, this.usuario, this.clave);
		}

		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// public void enviarCorreo() {
	//
	// final String usuario = "sandrolandovp@gmail.com";
	// final String clave = "sandro1713";
	//
	// // final String usuario = "srvp33@hotmail.com";
	// // final String clave = "1003Tamara";
	//
	// Properties properties = new Properties();
	//
	// // properties.put("mail.smtp.host", "smtp.live.com"); // Servidor SMTP de
	// Hotmail
	// // properties.put("mail.smtp.port", "587"); // Puerto SMTP seguro de
	// Hotmail
	// properties.put("mail.smtp.host", "smtp.gmail.com"); // Servidor SMTP de
	// Google
	// properties.put("mail.smtp.port", "587"); // Puerto SMTP seguro de Google
	// properties.put("mail.smtp.starttls.enable", "true"); // Para conectar de
	// manera segura al servidor SMTP
	// properties.put("mail.smtp.auth", "true"); // Usar autenticaci�n mediante
	// usuario y clave
	// // properties.put("mail.from", usuario);
	// // properties.put("mail.smtp.user", usuario); // Se esta usando una Clase
	// Authenticator esto no hace falta
	// // properties.put("mail.smtp.clave", clave); // Se esta usando una Clase
	// Authenticator esto no hace falta
	//
	// Authenticator authenticator = new Authenticator() {
	// public PasswordAuthentication getPasswordAuthentication() {
	// return new PasswordAuthentication(usuario, clave);
	// }
	// };
	//
	// Session session = Session.getInstance(properties, authenticator);
	// session.setDebug(true);
	//
	// // Message message = new MimeMessage(session);
	// MimeMessage message = new MimeMessage(session);
	//
	// String direcciones = "sndvll@hotmail.com,sandrolandovp@gmail.com";
	//
	// InternetAddress fromAddress = null;
	// try {
	// fromAddress = new InternetAddress(usuario, "Tecnointel" );
	// } catch (UnsupportedEncodingException e3) {
	// e3.printStackTrace();
	// }
	// // InternetAddress fromAddress =
	// InternetAddress.getLocalAddress(session); // Trae el usuario de session
	// de la propiedad mail.from
	// // try {
	// // fromAddress.setPersonal("TecnoIntel-Soem");
	// // } catch (UnsupportedEncodingException e3) {
	// // e3.printStackTrace();
	// // }
	//
	// // InternetAddress[] toAddresses = { new
	// InternetAddress("sndvll@hotmail.com") };
	// InternetAddress[] toAddress = null;
	// try {
	// // toAddress = InternetAddress.parse("sndvll@hotmail.com", false);
	// toAddress = InternetAddress.parse(direcciones, false);
	//
	// } catch (AddressException e2) {
	// e2.printStackTrace();
	// }
	//
	// try {
	// // message.setFrom(new InternetAddress(usuario));
	// message.setFrom(fromAddress);
	// // message.setRecipients(Message.RecipientType.TO, "sndvll@hotmail.com");
	// message.addRecipients(Message.RecipientType.TO, toAddress);
	// message.setSentDate(new Date());
	// message.setSubject("Prueba correo");
	// // message.setText("Mensaje enviado desde Java"); // Se usa solo para
	// enviar texto plano
	// message.setContent("<h1>Mensaje enviado desde java con etiquetas HTML</h1>",
	// "text/html");
	//
	// String line = " Texto del correo ";
	// String subject = message.getSubject();
	// StringBuilder sb = new StringBuilder(); // StringBuffer sb = new
	// StringBuffer();
	//
	// sb.append("<HTML>\n");
	// sb.append("<HEAD>\n");
	// sb.append("<TITLE>\n");
	// // sb.append(subject + "\n");
	// sb.append("</TITLE>\n");
	// sb.append("</HEAD>\n");
	// sb.append("<BODY>\n");
	// sb.append("<img src=\"cid:imageCab\">");
	// sb.append("<h1>Mensaje enviado desde java con etiquetas HTML</h1>");
	// sb.append("<H1>" + subject + "</H1>" + "\n");
	//
	// // while ((line = in.readLine()) != null) {
	// sb.append(line);
	// sb.append("\n");
	// // }
	//
	// sb.append("<h2>Pie de pagina</h2>");
	// sb.append("<img src=\"cid:imagePie\">");
	// sb.append("</BODY>\n");
	// sb.append("</HTML>\n");
	//
	// // try {
	// // message.setDataHandler(new DataHandler(new
	// // ByteArrayDataSource(sb.toString(), "text/html")));
	// // } catch (IOException e) {
	// // e.printStackTrace();
	// // }
	//
	// MimeMultipart multipart = new MimeMultipart("related");
	//
	// BodyPart messageBodyPart = new MimeBodyPart();
	// messageBodyPart.setContent(sb.toString(), "text/html");
	// multipart.addBodyPart(messageBodyPart);
	//
	// messageBodyPart = new MimeBodyPart();
	// // String cabecera =
	// "<img src=\"cid:imageCab\"><h1>Mensaje enviado desde java con etiquetas
	// HTML</h1>";
	// // messageBodyPart.setContent(cabecera, "text/html");
	// // multipart.addBodyPart(messageBodyPart);
	//
	// DataSource cabeceraDataSource = new
	// FileDataSource("C:\\tecnoIntel\\soem\\imagenes\\correo\\correoCabecera.png");
	// messageBodyPart = new MimeBodyPart();
	// messageBodyPart.setDataHandler(new DataHandler(cabeceraDataSource));
	// messageBodyPart.setHeader("Content-ID", "<imageCab>");
	// multipart.addBodyPart(messageBodyPart);
	//
	// // messageBodyPart = new MimeBodyPart();
	// // String text = "Cuerpo del documento";
	// // messageBodyPart.setText(text);
	// // multipart.addBodyPart(messageBodyPart);
	//
	// // messageBodyPart = new MimeBodyPart();
	// // String pie = "<img src=\"cid:imagePie\"><h2>Pie de Pagina</h2>";
	// // messageBodyPart.setContent(pie, "text/html");
	// // multipart.addBodyPart(messageBodyPart);
	//
	// DataSource pieDataSource = new
	// FileDataSource("C:\\tecnoIntel\\soem\\imagenes\\correo\\correoPie.png");
	// messageBodyPart = new MimeBodyPart();
	// messageBodyPart.setDataHandler(new DataHandler(pieDataSource));
	// messageBodyPart.setHeader("Content-ID", "<imagePie>");
	// multipart.addBodyPart(messageBodyPart);
	//
	// // MimeBodyPart mineBodyPart = new MimeBodyPart();
	// //
	// // try {
	// //
	// mineBodyPart.attachFile("C:\\tecnoIntel\\soem\\imagenes\\correo\\notasTemporal.txt");
	// // } catch (IOException e) {
	// // e.printStackTrace();
	// // }
	// // multipart.addBodyPart(mineBodyPart);
	// //
	// // mineBodyPart = new MimeBodyPart();
	// //
	// // try {
	// //
	// mineBodyPart.attachFile("C:\\tecnoIntel\\soem\\imagenes\\correo\\CurriculumSandro.docx");
	// // } catch (IOException e) {
	// // e.printStackTrace();
	// // }
	// // multipart.addBodyPart(mineBodyPart);
	//
	// // message.setContent(multipart,"text/html;charset=UTF-8");
	// message.setContent(multipart);
	//
	// } catch (MessagingException e1) {
	// e1.printStackTrace();
	// }
	//
	// Transport transport = null;
	// try {
	// if (transport == null){
	// transport = session.getTransport("smtp");
	// }
	//
	// if(!transport.isConnected()) {
	// transport.connect("smtp.gmail.com", usuario, clave);
	// // transport.connect("smtp.live.com", usuario, clave);
	// }
	//
	// transport.sendMessage(message, message.getAllRecipients());
	// transport.close();
	// } catch (NoSuchProviderException e) {
	// e.printStackTrace();
	// } catch (MessagingException e) {
	// e.printStackTrace();
	// }
	//
	// }

	// public static void enviarCorreo(String tipoServidor,String asunto, String
	// destinatarios, List<FileItem> adjuntos, String contenido) throws
	// Exception {
	// if(tipoServidor==null){
	// tipoServidor="smtp";
	// }
	// //objeto donde almacenamos la configuraci�n para conectarnos al servidor
	// Properties properties = new Properties();
	// //cargamos el archivo de configuracion
	// // OJO CON ESTA LINEA PARECE QUE ES PARA LEER ARCHIVOS DE TEXTO DE
	// CONFIGURACION
	// // properties.load(new
	// MailSender().getClass().getResourceAsStream("/org/ingenio/ds/util/mail/"+tipoServidor+".properties"));
	// //creamos una sesi�n
	// Session session = Session.getInstance(properties, null);
	// //creamos el mensaje a enviar
	// Message mensaje = new MimeMessage(session);
	// //agregamos la direcci�n que env�a el email
	// mensaje.setFrom(new
	// InternetAddress(properties.getProperty("mail.from")));
	// List<InternetAddress> emailsDestino = new ArrayList<InternetAddress>();
	// int i = 0;
	// StringTokenizer emailsSt = new StringTokenizer(destinatarios,";,");
	// while (emailsSt.hasMoreTokens()) {
	// String email=emailsSt.nextToken();
	// try{
	// //agregamos las direcciones de email que reciben el email, en el primer
	// parametro env�amos el tipo de receptor
	// mensaje.setRecipients(Message.RecipientType.TO, new
	// InternetAddress(email));
	// //Message.RecipientType.TO; para
	// //Message.RecipientType.CC; con copia
	// //Message.RecipientType.BCC; con copia oculta
	// }catch(Exception ex){
	// //en caso que el email est� mal formado lanzar� una exception y la
	// ignoramos
	// }
	// }
	// mensaje.setSubject(asunto);
	// //agregamos una fecha al email
	// mensaje.setSentDate(new Date(1,1,1));
	// BodyPart messageBodyPart = new MimeBodyPart();
	// messageBodyPart.setText(contenido);
	// Multipart multipart = new MimeMultipart();
	// multipart.addBodyPart(messageBodyPart);
	// if (adjuntos != null) {
	// for (FileItem adjunto : adjuntos) {
	// //agregamos los adjuntos
	// messageBodyPart = new MimeBodyPart();
	// DataSource source = new ByteArrayDataSource(adjunto.getInputStream(),
	// adjunto.getContentType());
	// messageBodyPart.setDataHandler(new DataHandler(source));
	// messageBodyPart.setFileName(adjunto.getName());
	// multipart.addBodyPart(messageBodyPart);
	// }
	// }
	//
	// mensaje.setContent(multipart);
	// SMTPTransport transport = (SMTPTransport) session.getTransport("smtp");
	// try {
	// //conectar al servidor
	// transport.connect(properties.getProperty("mail.user"),
	// properties.getProperty("mail.password"));
	// //enviar el mensaje
	// transport.sendMessage(mensaje, mensaje.getAllRecipients());
	// } finally {
	// //cerrar la conexi�n
	// transport.close();
	// }
	// }

}
