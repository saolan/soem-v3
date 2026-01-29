package ec.com.tecnointel.soem.general.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Formatter;

public class Util {

	// Este es el codigo original del SRI
	// public static byte[] archivoToByte(File file) throws IOException {
	// String archivo = FileUtils.readFileToString(file, "UTF-8");
	// return archivo.getBytes(Charset.forName("UTF-8"));
	// }

	public static byte[] archivoToByte(File file) throws IOException {

		// String archivo = new String(Files.readAllBytes(Paths.get("manifest.mf")),
		// "UTF-8");

		String archivo = new String(Files.readAllBytes(file.toPath()), "UTF-8");
		return archivo.getBytes(Charset.forName("UTF-8"));

	}

	// public static File stringToArchivo(String rutaArchivo, String
	// contenidoArchivo) {
	//
	// FileOutputStream fileOutputStream = null;
	// // archivoCreado = null;
	// try {
	//
	// fileOutputStream = new FileOutputStream(rutaArchivo);
	// OutputStreamWriter out = new OutputStreamWriter(fileOutputStream, "UTF-8");
	//
	// for (int i = 0; i < contenidoArchivo.length(); i++) {
	// out.write(contenidoArchivo.charAt(i));
	// }
	//
	// out.close();
	//
	// return new File(rutaArchivo);
	//
	// } catch (Exception e) {
	// // LOG.error(ex);
	// return null;
	// } finally {
	// try {
	// if (fileOutputStream != null) {
	// fileOutputStream.close();
	// }
	// } catch (Exception e) {
	// // LOG.error(ex);
	// }
	// }
	// }

	// public static File crearArchivo(byte[] archivo, String nombreArchivo,
	// DirectorioEnum directorioEnum) throws DirectorioException
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

	// Cambia de formato a la fecha para realizar las busquedas con exactitud
	// Date() contiene horas, minutos, segundos, etc
	// muchas veces esto no es necesario para una busqueda
	// Con este metodo se puede modificar el formato de la fecha de acuerdo a lo
	// que sea necesario

	// Ejemplo Formato "yyyyMMdd" o "EEE MMM dd 12:00:00 z yyyy" o "dd-MM-yyyy"
	public static Date cambiarFormatoFecha(Date fecha, String Formato) {

		String stringFecha;
		SimpleDateFormat formato = new SimpleDateFormat(Formato);

		stringFecha = formato.format(fecha);

		try {
			fecha = formato.parse(stringFecha);
		} catch (ParseException pe) {
			pe.printStackTrace();
		}

		return fecha;
	}

	public static LocalDate cambiarFormatoFecha(LocalDate fecha, String formato) {

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formato);
		String fechaFormat = fecha.format(dateTimeFormatter);

		return LocalDate.parse(fechaFormat);
	}

	@Deprecated
	public static String cambiarFormatoFechaString(Date fecha, String Formato) {

		String stringFecha;
		SimpleDateFormat formato = new SimpleDateFormat(Formato);

		stringFecha = formato.format(fecha);

		return stringFecha;
	}

	public static String cambiarFormatoFechaString(LocalDate fecha, String formato) {

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formato);
		return fecha.format(dateTimeFormatter);
	}

	public static String cambiarFormatoFechaHoraString(LocalDateTime fechaHora, String formato) {

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formato);
		return fechaHora.format(dateTimeFormatter);
	}

	public static Integer convertirStringANumero(String numero) throws NumberFormatException {
		return Integer.parseInt(numero);
	}

	public static String llenarCeros(Integer numero, String formato) {

//		Ej: formato = "%09d" - Llena hasta con nueve ceros

		try (Formatter formatter = new Formatter()) {
			return formatter.format(formato, numero).toString();
		}

	}

}