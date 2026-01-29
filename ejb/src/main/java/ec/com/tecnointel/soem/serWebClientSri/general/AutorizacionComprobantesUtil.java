package ec.com.tecnointel.soem.serWebClientSri.general;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.glassfish.jaxb.core.marshaller.CharacterEscapeHandler;

import com.thoughtworks.xstream.XStream;

import ec.com.tecnointel.soem.general.util.Util;
import ec.com.tecnointel.soem.serWebClientSri.autorizacion.Autorizacion;
import ec.com.tecnointel.soem.serWebClientSri.autorizacion.Mensaje;
import ec.com.tecnointel.soem.serWebClientSri.autorizacion.RespuestaComprobante;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

public class AutorizacionComprobantesUtil {

	private RespuestaComprobante respuestaComprobante;
	private String nombreArchivo;

	public AutorizacionComprobantesUtil(RespuestaComprobante respuestaComprobante, String nombreArchivo) {
		this.respuestaComprobante = respuestaComprobante;
		this.nombreArchivo = nombreArchivo;
	}

	public AutorizacionComprobantesUtil(RespuestaComprobante respuestaComprobante) {
		this.respuestaComprobante = respuestaComprobante;
	}

	public AutorizacionComprobantesUtil(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	// public void validarRespuestaAutorizacion(AutorizacionDTO autorizacionDTO)
	// throws DirectorioException, MergeRespuestaException,
	// RespuestaAutorizacionException, ConvertidorXMLException
	// {

	public void validarRespuestaAutorizacion(AutorizacionDTO autorizacionDTO, String rutaAutorizados)
			throws RespuestaAutorizacionException, ConvertidorXMLException {

		byte[] archivoRespuestaAutorizacionXML = obtenerRepuestaAutorizacionXML2(autorizacionDTO.getAutorizacion());

		if (EstadoAutorizacion.AUT.equals(autorizacionDTO.getEstadoAutorizacion())) {
			// ArchivoUtils.crearArchivo(archivoRespuestaAutorizacionXML,
			// this.nombreArchivo, DirectorioEnum.AUTORIZADOS);
			Util.crearArchivo(archivoRespuestaAutorizacionXML, this.nombreArchivo, rutaAutorizados);
			
		} else {

			if (EstadoAutorizacion.NAU.equals(autorizacionDTO.getEstadoAutorizacion())) {
				// ArchivoUtils.crearArchivo(archivoRespuestaAutorizacionXML,
				// this.nombreArchivo, DirectorioEnum.NO_AUTORIZADOS);
//				Util.crearArchivo(archivoRespuestaAutorizacionXML, this.nombreArchivo, "TempNoAutorizados");
				throw new RespuestaAutorizacionException(
						String.format("Error al validar el comprobante estado :",
								new Object[] { autorizacionDTO.getEstadoAutorizacion().getDescripcion(),
										autorizacionDTO.getMensaje() }));
			}

			if (EstadoAutorizacion.PRO.equals(autorizacionDTO.getEstadoAutorizacion())) {
				throw new RespuestaAutorizacionException(String.format("Error al validar el comprobante estado : ",
						new Object[] { autorizacionDTO.getEstadoAutorizacion().getDescripcion() }));
			}
		}
	}
	
	public AutorizacionDTO obtenerEstadoAutorizacion() {

		for (Autorizacion autorizacion : this.respuestaComprobante.getAutorizaciones().getAutorizacion()) {
			EstadoAutorizacion estadoAutorizacion = EstadoAutorizacion.getEstadoAutorizacion(autorizacion.getEstado());
			if (EstadoAutorizacion.AUT.equals(estadoAutorizacion)) {
				return new AutorizacionDTO(autorizacion, EstadoAutorizacion.AUT);
			}
			if (EstadoAutorizacion.PRO.equals(estadoAutorizacion)) {
				return new AutorizacionDTO(autorizacion, EstadoAutorizacion.AUT);
			}
		}
		Autorizacion autorizacion = (Autorizacion) this.respuestaComprobante.getAutorizaciones().getAutorizacion()
				.get(0);

		return new AutorizacionDTO(autorizacion, EstadoAutorizacion.NAU, obtieneMensajesAutorizacion(autorizacion));
	}

	private void setXMLCDATA(Autorizacion autorizacion) {
		autorizacion.setComprobante("<![CDATA[" + autorizacion.getComprobante() + "]]>");
	}

//	Ya no se usa
	@Deprecated
	private byte[] obtenerRepuestaAutorizacionXML(Autorizacion autorizacion) throws ConvertidorXMLException {
		try {
			XStream xstream = XStreamUtil.getRespuestaXStream();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
			setXMLCDATA(autorizacion);
			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			xstream.toXML(autorizacion, writer);
			return outputStream.toByteArray();
		} catch (IOException e) {
			throw new ConvertidorXMLException("Se produjo un error al convetir el archivo al formato XML", e);
		}
	}

	private byte[] obtenerRepuestaAutorizacionXML2(Autorizacion autorizacion) throws ConvertidorXMLException {

//		setXMLCDATA(autorizacion);
		autorizacion.setComprobante(autorizacion.getComprobante());

		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
//			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
			
			JAXBContext context = JAXBContext.newInstance(new Class[] { Autorizacion.class });
			Marshaller marshaller = context.createMarshaller();
			
			marshaller.setProperty("jaxb.encoding", "UTF-8");
			marshaller.setProperty("jaxb.formatted.output", Boolean.valueOf(true));

//			Se coloca esta propiedad para que no cambie < > por &lt &gt
			CharacterEscapeHandler escapeHandler = NoEscapeHandler.theInstance;
			marshaller.setProperty("org.glassfish.jaxb.characterEscapeHandler",escapeHandler);

//			Tambien funciona asi
//			marshaller.setProperty("org.glassfish.jaxb.characterEscapeHandler",
//					  new CharacterEscapeHandler() {
//					    public void escape(char[] ac, int i, int j, boolean flag, Writer writer) throws IOException {
//					      writer.write(ac, i, j);
//					    }
//					});
			
			marshaller.marshal(autorizacion, outputStreamWriter);
			
			return outputStream.toByteArray();
			
		} catch (IOException | JAXBException e) {
			throw new ConvertidorXMLException("Se produjo un error al convetir el archivo al formato XML", e);
		}
	}

//	Este metodo crea el archivo xml de cualquier documento que este en el sri
//	este o no autorizado
	public void crearArchivoXml(AutorizacionDTO autorizacionDTO, String ruta)
			throws RespuestaAutorizacionException, ConvertidorXMLException {

//		byte[] archivoRespuestaAutorizacionXML = obtenerRepuestaAutorizacionXML(autorizacionDTO.getAutorizacion());
//		byte[] archivoRespuestaAutorizacionXML = obtenerXML(autorizacionDTO.getAutorizacion());

//		Util.crearArchivo(archivoRespuestaAutorizacionXML, this.nombreArchivo, ruta);
		
		
        Path path = Paths.get(ruta + nombreArchivo);
        String text = autorizacionDTO.getAutorizacion().getComprobante();
 
        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8))
        {
            bw.write(text);
//            System.out.println("================================= Archivo creado");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

	}

//	Este metodo tiene codigo parecido obtenerRepuestaAutorizacionXML
	private byte[] obtenerXML(Autorizacion autorizacion) throws ConvertidorXMLException {
		try {
//			XStream xstream = XStreamUtil.getRespuestaXStream();
			XStream xstream = new XStream();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
//			setXMLCDATA(autorizacion);
//			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			xstream.toXML(autorizacion.getComprobante(), writer);
			return outputStream.toByteArray();
		} catch (IOException e) {
			throw new ConvertidorXMLException("Se produjo un error al convetir el archivo al formato XML", e);
		}
	}
	
	public static String obtieneMensajesAutorizacion(Autorizacion autorizacion) {
		StringBuilder mensaje = new StringBuilder();
		for (Mensaje m : autorizacion.getMensajes().getMensaje()) {
			if (m.getInformacionAdicional() != null) {
				mensaje.append(String.format("\n%s:%s", new Object[] { m.getMensaje(), m.getInformacionAdicional() }));
			} else {
				mensaje.append(String.format("\n%s", new Object[] { m.getMensaje() }));
			}
		}
		return mensaje.toString();
	}

}
