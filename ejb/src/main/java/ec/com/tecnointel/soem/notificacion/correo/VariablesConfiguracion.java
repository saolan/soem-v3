package ec.com.tecnointel.soem.notificacion.correo;

import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.registroInt.ParametroRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

@Startup
@Singleton
public class VariablesConfiguracion {

	String rutaAutorizados;
	
	String rutaImagenCabecera;
	String rutaImagenPie;
	String rutaCertificados;
	String direccionesAdicionales;
	
	String subject;
	String linea1;
	String linea2;
	String linea3;
	
	StringBuilder parametrosSmtp = new StringBuilder();
	StringBuilder usuarioCorreo = new StringBuilder();
	
	@Inject
	ParametroRegisInt parametroRegis;
	
	@PostConstruct
	public void init() {
		this.buscarParametrosCorreo();
	}
	
	public void buscarParametrosCorreo() {

		Parametro parametro = new Parametro();
		
		try {

//			SMTP
//			Host
			parametro = parametroRegis.buscarPorId(Parametro.class, 100100);
			parametrosSmtp.append(parametro.getDescri());
			parametrosSmtp.append(",");
			
//			Puerto
			parametro = parametroRegis.buscarPorId(Parametro.class, 100101);
			parametrosSmtp.append(parametro.getDescri());
			parametrosSmtp.append(",");
			
//			StartTls
			parametro = parametroRegis.buscarPorId(Parametro.class, 100102);
			parametrosSmtp.append(parametro.getDescri());
			parametrosSmtp.append(",");
			
//			Auth
			parametro = parametroRegis.buscarPorId(Parametro.class, 100103);
			parametrosSmtp.append(parametro.getDescri());
			parametrosSmtp.append(",");
			
//			Usuario Correo
//			Usuario
			parametro = parametroRegis.buscarPorId(Parametro.class, 100150);
			usuarioCorreo.append(parametro.getDescri());
			usuarioCorreo.append(",");

//			Direccion correo
			parametro = parametroRegis.buscarPorId(Parametro.class, 100151);
			usuarioCorreo.append(parametro.getDescri());
			usuarioCorreo.append(",");
			
//			Alias
			parametro = parametroRegis.buscarPorId(Parametro.class, 100152);
			usuarioCorreo.append(parametro.getDescri());
			usuarioCorreo.append(",");
			
//			Clave
			parametro = parametroRegis.buscarPorId(Parametro.class, 100153);
			usuarioCorreo.append(parametro.getDescri());
			usuarioCorreo.append(",");

//			Ruta documentos autorizados
			parametro = parametroRegis.buscarPorId(Parametro.class, 3242);
			this.rutaAutorizados = parametro.getDescri();
			
//			Imagenes
			parametro = parametroRegis.buscarPorId(Parametro.class, 100200);
			this.rutaImagenCabecera = parametro.getDescri();
						
			parametro = parametroRegis.buscarPorId(Parametro.class, 100201);
			this.rutaImagenPie = parametro.getDescri();

//			Certificados
			parametro = parametroRegis.buscarPorId(Parametro.class, 100202);
			this.rutaCertificados = parametro.getDescri();
			
//			Cuerpo correo
			parametro = parametroRegis.buscarPorId(Parametro.class, 100210);
			this.subject = parametro.getDescri();

			parametro = parametroRegis.buscarPorId(Parametro.class, 100211);
			this.linea1 = parametro.getDescri();

			parametro = parametroRegis.buscarPorId(Parametro.class, 100212);
			this.linea2 = parametro.getDescri();

			parametro = parametroRegis.buscarPorId(Parametro.class, 100213);
			this.linea3 = parametro.getDescri();

//			Direcciones adicionales
//			parametro = parametroRegis.buscarPorId(100250);
//			this.direccionesAdicionales = parametro.getDescri();
							
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getRutaImagenCabecera() {
		return rutaImagenCabecera;
	}

	public void setRutaImagenCabecera(String rutaImagenCabecera) {
		this.rutaImagenCabecera = rutaImagenCabecera;
	}

	public String getRutaImagenPie() {
		return rutaImagenPie;
	}

	public void setRutaImagenPie(String rutaImagenPie) {
		this.rutaImagenPie = rutaImagenPie;
	}

	public String getRutaCertificados() {
		return rutaCertificados;
	}

	public void setRutaCertificados(String rutaCertificados) {
		this.rutaCertificados = rutaCertificados;
	}

	public String getDireccionesAdicionales() {
		return direccionesAdicionales;
	}

	public void setDireccionesAdicionales(String direccionesAdicionales) {
		this.direccionesAdicionales = direccionesAdicionales;
	}

	public StringBuilder getParametrosSmtp() {
		return parametrosSmtp;
	}

	public void setParametrosSmtp(StringBuilder parametrosSmtp) {
		this.parametrosSmtp = parametrosSmtp;
	}

	public StringBuilder getUsuarioCorreo() {
		return usuarioCorreo;
	}

	public void setUsuarioCorreo(StringBuilder usuarioCorreo) {
		this.usuarioCorreo = usuarioCorreo;
	}

	public ParametroRegisInt getParametroRegis() {
		return parametroRegis;
	}

	public void setParametroRegis(ParametroRegisInt parametroRegis) {
		this.parametroRegis = parametroRegis;
	}

	public String getRutaAutorizados() {
		return rutaAutorizados;
	}

	public void setRutaAutorizados(String rutaAutorizados) {
		this.rutaAutorizados = rutaAutorizados;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getLinea1() {
		return linea1;
	}

	public void setLinea1(String linea1) {
		this.linea1 = linea1;
	}

	public String getLinea2() {
		return linea2;
	}

	public void setLinea2(String linea2) {
		this.linea2 = linea2;
	}

	public String getLinea3() {
		return linea3;
	}

	public void setLinea3(String linea3) {
		this.linea3 = linea3;
	}

}
