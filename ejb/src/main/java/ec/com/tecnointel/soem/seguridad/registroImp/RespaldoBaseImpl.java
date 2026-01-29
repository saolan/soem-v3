package ec.com.tecnointel.soem.seguridad.registroImp;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.time.LocalDateTime;

import ec.com.tecnointel.soem.general.util.Util;
import ec.com.tecnointel.soem.parametro.registroInt.ParametroRegisInt;
import ec.com.tecnointel.soem.seguridad.registroInt.RespaldoBaseInt;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class RespaldoBaseImpl implements RespaldoBaseInt, Serializable {

	private Process process;
	private ProcessBuilder processBuilder;

	private static final long serialVersionUID = 7443303391620017053L;
	
	@Inject
	ParametroRegisInt parametroRegis;

	@Override
	public boolean respaldar(String host, String puerto, String usuario, String clave, String formato,
								String base, String rutaRespaldo, String rutaPgDump) {

//		String lineaRespaldo;
		String fechaHoraRespaldo;
		
		boolean respaldo;
		
		fechaHoraRespaldo = Util.cambiarFormatoFechaHoraString(LocalDateTime.now(), "yyyyMMddHHmm");
		
		rutaRespaldo = rutaRespaldo + fechaHoraRespaldo + ".backup";
		
		try {
			
			File pgdump = new File(rutaPgDump);
			
			if (pgdump.exists()) {
				
				if (!rutaRespaldo.equalsIgnoreCase("")) {
					processBuilder = new ProcessBuilder(rutaPgDump, "--verbose", "--format", formato, "-f", rutaRespaldo);
				} else {
					processBuilder = new ProcessBuilder(rutaPgDump, "--verbose", "--inserts", "--column-inserts", "-f", rutaRespaldo);
					System.out.println("<<<<<<<<<<<<<<<<<<<<<< ERROR AL RESPALDAR PGDUMP NO EXISTE");
					System.out.println("<<<<<<<<<<<<<<<<<<<<<< ERROR AL RESPALDAR PGDUMP NO EXISTE");
					System.out.println("<<<<<<<<<<<<<<<<<<<<<< ERROR AL RESPALDAR PGDUMP NO EXISTE");
				}
				
				processBuilder.environment().put("PGHOST", host);
				processBuilder.environment().put("PGPORT", puerto);
				processBuilder.environment().put("PGUSER", usuario);
				processBuilder.environment().put("PGPASSWORD", clave);
				processBuilder.environment().put("PGDATABASE", base);
				processBuilder.redirectErrorStream(true);
				process = processBuilder.start();
				
//				Se utiliza este codigo junto con la opcion --verbose que es la que detalla información del respaldo
//				Con esta opcion estas lineas son obligatorias
//				si no se desea utilizar este detalle quitar la opcion --verbose
//				Si se usa la opcion --verbose y no se utiliza estas lineas el respaldo espera a detener el sistema
//				para terminar respaldar hasta eso el archivo de respaldo tiene un tamaño de 0 Bits
				InputStream inputStream = process.getInputStream();  
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				
//				while ((lineaRespaldo = bufferedReader.readLine()) != null) {
//					resumenRespaldo.append(lineaRespaldo);
//				  	System.out.println(lineaRespaldo);
//				}
				
				bufferedReader.close();
				inputStreamReader.close();
				inputStream.close();
//              process.waitFor();
//              process.destroy();
				respaldo = true;
				
			} else {
				
				if (!rutaRespaldo.equalsIgnoreCase("")) {
					processBuilder = new ProcessBuilder(rutaPgDump, "--verbose", "--format", formato, "-f", rutaRespaldo);
				} else {
					processBuilder = new ProcessBuilder(rutaPgDump, "--verbose", "--inserts", "--column-inserts", "-f", rutaRespaldo);
					System.out.println("<<<<<<<<<<<<<<<<<<<<<< ERROR AL RESPALDAR PGDUMP NO EXISTE");
					System.out.println("<<<<<<<<<<<<<<<<<<<<<< ERROR AL RESPALDAR PGDUMP NO EXISTE");
					System.out.println("<<<<<<<<<<<<<<<<<<<<<< ERROR AL RESPALDAR PGDUMP NO EXISTE");
				}
				
				processBuilder.environment().put("PGHOST", host);
				processBuilder.environment().put("PGPORT", puerto);
				processBuilder.environment().put("PGUSER", usuario);
				processBuilder.environment().put("PGPASSWORD", clave);
				processBuilder.environment().put("PGDATABASE", base);
				processBuilder.redirectErrorStream(true);
				process = processBuilder.start();
				
				InputStream inputStream = process.getInputStream();  
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				
//				while ((lineaRespaldo = bufferedReader.readLine()) != null) {
//				}

				bufferedReader.close();
				inputStreamReader.close();
				inputStream.close();

				respaldo = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			respaldo = false;
			
		} 
		return respaldo;
	}
}
