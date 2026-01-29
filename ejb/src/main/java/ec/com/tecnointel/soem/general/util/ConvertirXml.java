package ec.com.tecnointel.soem.general.util;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

public class ConvertirXml {

	public ConvertirXml() {
	}

	public static void main(String[] args) {
		
		String convertir = "<autorizacion><estado>AUTORIZADO</estado><numeroAutorizacion>120620</numeroAutorizacion></autorizacion>";
		
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            JAXBContext context = JAXBContext.newInstance(convertir);
            Marshaller marshaller = context.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(convertir, bufferedWriter);

            // Flushing y cerrando el bufferedWriter
            bufferedWriter.flush();
            bufferedWriter.close();

            // Obtener el XML como bytes
            byte[] xmlBytes = outputStream.toByteArray();
            
            crearArchivo(xmlBytes, "test.xml", "C:\\saolan\\soem\\comprobantes\\autorizados\\");

            // Hacer algo con los bytes, en este caso, probablemente lo retornarías o lo usarías de alguna manera
        } catch (JAXBException | UnsupportedEncodingException e) {
            // Manejar la excepción
            e.printStackTrace();
        } catch (Exception e) {
            // Otra excepción no esperada
            e.printStackTrace();
        }
    }

	
	public static File crearArchivo(byte[] archivo, String nombreArchivo, String directorioEnum) {

		File archivoDestino = null;

		try {
			// ConfiguracionDirectorioSQL confDirectorio = new ConfiguracionDirectorioSQL();
			// ConfiguracionDirectorio directorio =
			// confDirectorio.obtenerDirectorio(directorioEnum.getCode());
			// File directorioDestino = new File(directorio.getPath());
//			String rutaArchivoDestino = directorioDestino + File.separator + nombreArchivo;

			String rutaArchivoDestino = directorioEnum + File.separator + nombreArchivo + ".xml";
			archivoDestino = new File(rutaArchivoDestino);
			FileOutputStream fileOutputStream = new FileOutputStream(archivoDestino);
			fileOutputStream.write(archivo);
			fileOutputStream.close();

		}
		// catch (SQLException ex)
		// {
		//// LOG.error(ex);
		// throw new DirectorioException(String.format("Verifique si est� registrado el
		// directorio : %s", new Object[] { directorioEnum.toString() }));
		// }
		// catch (ClassNotFoundException ex)
		// {
		//// LOG.error(ex);
		// throw new DirectorioException(String.format("Se produjo un error al consultar
		// el direcotorio : %s", new Object[] { directorioEnum.toString() }));
		// }
		catch (IOException e) {
			e.printStackTrace();
			// LOG.error(ex);
			// throw new DirectorioException(String.format("Error al mover el archivo al
			// directorio: %s", new Object[] { directorioEnum.toString() }));
		}
		return archivoDestino;
	}

}

