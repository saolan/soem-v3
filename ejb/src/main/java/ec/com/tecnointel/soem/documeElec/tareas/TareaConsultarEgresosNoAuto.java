package ec.com.tecnointel.soem.documeElec.tareas;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import ec.com.tecnointel.soem.egreso.listaInt.EgresoListaInt;
import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.parametro.modelo.DocuEgre;
import jakarta.inject.Inject;

/**
Estas tareas todavia no se usan
 */
//@Dependent
public class TareaConsultarEgresosNoAuto implements Runnable {

	long egresosNoAutorizados;
	
	@Inject
	EgresoListaInt egresoLista;

	@Override
	public void run() {

		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Evento disparado...");
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Evento disparado...");
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Evento disparado...");

//		LocalDate fechaDesde = restarMinutosAFechaActual(23040); // 4320
//		LocalDate fechaHasta = restarMinutosAFechaActual(15);

		LocalDate fechaDesde = LocalDate.now();;
		LocalDate fechaHasta = LocalDate.now();

		Set<Integer> sucursals = new HashSet<>();
		Egreso egresoBuscar = new Egreso();

		DocuEgre docuEgre = new DocuEgre();
		docuEgre.setDocumeElec("Ninguno");

		egresoBuscar.setDocuEgre(docuEgre);
		egresoBuscar.setEstado("PR");
		egresoBuscar.setEstadoDocuElec("AUTORIZADO");

		try {
			egresosNoAutorizados = egresoLista.contarRegistros2(egresoBuscar, fechaDesde, fechaHasta, sucursals);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static LocalDateTime restarMinutosAFechaActual(long minutos) {
		// Obtener la fecha y hora actual
		LocalDateTime localDateTime = LocalDateTime.now();

		LocalDateTime fechaHoraEmisHasta = localDateTime.minusMinutes(minutos);

		return fechaHoraEmisHasta;
	}
}
