package ec.com.tecnointel.soem.general.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ec.com.tecnointel.soem.documeElec.tareas.ManejadorTareaEnviarHoraFija;
import ec.com.tecnointel.soem.parametro.listaInt.SucursalListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.registroInt.ParametroRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

@Startup
@Singleton
public class IdenSistema {

	private String estadoLicencia;

	private static String buscador = "pxsHztLvPAbFTq2lJh6uQ9gC8Ufi3ykNoZraS4m0Vd7w5En1YcIMOeRjDWGKBX";
	private static String encriptador = "8JEYu4aN6CdyPg3BoViI0cDZt2GRjx7LvSqA1plThH9eKrXkObs5wnQFfUmMzW";
		
	@Inject
	ParametroRegisInt parametroRegis;
	
	@Inject
	SucursalListaInt sucursalLista;
	
	@Inject
	ManejadorTareaEnviarHoraFija manejadorTareaEnviarDocuLote;
	
	@PostConstruct
	public void init() {

		String ciRuc;
		String seriePlaca;
		String licencia = null;
		
		Parametro parametro = new Parametro();
		Sucursal sucursalBuscar = new Sucursal();
		Sucursal sucursal = new Sucursal();

		List<Sucursal> sucursales =  new ArrayList<>();
		
		seriePlaca = obtenerSeriePlaca();
		seriePlaca = this.depurarSeriePlaca(seriePlaca);
		
		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 11);
		} catch (Exception e) {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + "Error el buscar parametro - numero licencia");
			e.printStackTrace();
		}
		
		try {
			
			sucursales = sucursalLista.buscar(sucursalBuscar, null);
			if (sucursales.size() != 0) {
				
				sucursal = sucursales.get(0);
				
				System.out.println("Cedula o Ruc - Licencia: " + sucursal.getRuc());
				
				if (sucursal.getRuc().length() == 13) {
					ciRuc = sucursal.getRuc().substring(0, sucursal.getRuc().length() - 3);	
				} else {
					ciRuc = sucursal.getRuc();
				}
				
				licencia = seriePlaca + ciRuc;
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		/* Prueba de activacin de tarea a una hora determinda 
		 * con ejecucion periodica una vez todos los dias
		 * Utiliza las clases ManejadorTareaEnviarDocuLote y 
		 * TareaEnviarDocuLote */ 
//		try {
//			manejadorTareaEnviarDocuLote.ejecutarTareaEnviarDocu();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		/* Fin Prueba de activacion de tarea a una hora determinda 
		 * con ejecucion periodica una vez todos los dias */

		licencia = this.encriptarCadena(licencia, buscador, encriptador);
		
		if (licencia.equals(parametro.getDescri())) {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + "Licencia Activada");
			estadoLicencia = "activada";
		} else {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + "Licencia Desactivada");
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + "Licencia Desactivada");
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + "Licencia Desactivada");
			estadoLicencia = "";
		}

	}
	
	public String depurarSeriePlaca(String seriePlaca) {

		String seriePlacaId = "";

		for (int i = 0; i < seriePlaca.length(); i++) {

			if (Character.isDigit(seriePlaca.charAt(i)) || Character.isLetter(seriePlaca.charAt(i))) {

				String valorSerie = Character.toString(seriePlaca.charAt(i));

				seriePlacaId = seriePlacaId + valorSerie;
			}
		}
		return seriePlacaId;
	}
	
	public String encriptarCadena(String cadena, String buscador, String encriptador) {

		String result = "";

		for (int i = 0; i < cadena.length(); i++) {
			result += encriptarCaracter(cadena.substring(i, i + 1), cadena.length(), i, buscador, encriptador);
		}

		return result;
	}

	private String encriptarCaracter(String caracter, Integer longitudCadena, Integer posicion, String buscador, String encriptador) {

		Integer indice;

		if (buscador.indexOf(caracter) != -1) {
			indice = (buscador.indexOf(caracter) + longitudCadena + posicion) % buscador.length();
			String val = encriptador.substring(indice, indice + 1);
			return val;
		}

		return caracter;

	}
	
	public String desEncriptarCadena(String cadena) {
		String result = "";

		for (int i = 0; i < cadena.length(); i++) {
			result += desEncriptarCaracter(cadena.substring(i, i + 1), cadena.length(), i);
		}

		return result;

	}

	private String desEncriptarCaracter(String caracter, Integer longitudCadena, Integer posicion) {

		Integer indice;

		if (encriptador.indexOf(caracter) != -1) {

			if ((encriptador.indexOf(caracter) - longitudCadena - posicion) > 0) {
				indice = (encriptador.indexOf(caracter) - longitudCadena - posicion) % encriptador.length();
			} else {
				indice = (buscador.length())
						+ ((encriptador.indexOf(caracter) - longitudCadena - posicion) % encriptador.length());
			}

			indice = indice % encriptador.length();
			return buscador.substring(indice, indice + 1);

		} else {
			return caracter;
		}
	}

	public String obtenerDireccionMac() throws IOException, InterruptedException {

		boolean isWin = System.getProperty("os.name").toLowerCase().indexOf("win") != -1;

		Process aProc = Runtime.getRuntime().exec(isWin ? "ipconfig /all" : "/sbin/ifconfig -a");

		BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(aProc.getInputStream())));

		Pattern macAddressPattern = Pattern
				.compile("((\\p{XDigit}\\p{XDigit}" + (isWin ? "-" : ":") + "){5}\\p{XDigit}\\p{XDigit})");

		for (String outputLine = ""; outputLine != null; outputLine = br.readLine()) {
			Matcher macAddressMatcher = macAddressPattern.matcher(outputLine);
			if (macAddressMatcher.find()) {
				return macAddressMatcher.group(0);
			}
		}

		aProc.destroy();
		aProc.waitFor();

		return null;
	}

	public String obtenerSeriePlaca() {
		try {
			String OSName = System.getProperty("os.name");
			if (OSName.contains("Windows")) {
				return (obtenerSeriePlacaWin());
			} else {
				return (obtenerSeriePlacaLinux());
			}
		} catch (Exception e) {
			System.err.println("Excepcion - Error al buscar idSistema  : " + e.getMessage()); 
			return null;
		}
	}

	private String obtenerSeriePlacaWin() {
		String result = "";
		try {
			File file = File.createTempFile("realhowto", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);

			String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
					+ "Set colItems = objWMIService.ExecQuery _ \n" + "   (\"Select * from Win32_BaseBoard\") \n"
					+ "For Each objItem in colItems \n" + "    Wscript.Echo objItem.SerialNumber \n"
					+ "    exit for  ' do the first cpu only! \n" + "Next \n";

			fw.write(vbs);
			fw.close();

			Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
		} catch (Exception E) {
			System.err.println("Windows MotherBoard Exp : " + E.getMessage());
		}
		return result.trim();
	}

	private String obtenerSeriePlacaLinux() {
		String command = "cat /etc/machine-id";
		String maquinaId = null;
		try {
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			maquinaId = bufferedReader.readLine().trim();
			process.waitFor();
			bufferedReader.close();
		} catch (Exception ex) {
			System.err.println("Error obteniendo machine-id: " + ex.getMessage());
			maquinaId = null;
		}
		return maquinaId;
	}
	
	public String getEstadoLicencia() {
		return estadoLicencia;
	}

	public void setEstadoLicencia(String estadoLicencia) {
		this.estadoLicencia = estadoLicencia;
	}

}
