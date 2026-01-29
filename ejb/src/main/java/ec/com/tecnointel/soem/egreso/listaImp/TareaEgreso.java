package ec.com.tecnointel.soem.egreso.listaImp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.caja.modelo.CajaMovi;
import ec.com.tecnointel.soem.egreso.listaInt.EgresoListaInt;
import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.egreso.modelo.PersClie;
import ec.com.tecnointel.soem.parametro.modelo.DocuEgre;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class TareaEgreso {

	@Inject
	EgresoListaInt egresoLista;
	
//	EgresoListaImp e = new EgresoListaImp();
	
//	public void execute(JobExecutionContext arg0) throws JobExecutionException {
	public void listar() {
	
		Set<Integer> sucursals = new HashSet<>();
		
		PersClie persClie = new PersClie();
		persClie.setPersona(new Persona());
		
		DocuEgre docuEgre = new DocuEgre();
		docuEgre.setDocumento(new Documento());
		
		Egreso egreso = new Egreso();
		egreso.setSucursal(new Sucursal());
		
		CajaMovi cajaMovi = new CajaMovi();
		
		egreso.setCajaMovi(cajaMovi);
		egreso.setPersClie(persClie);
		egreso.setDocuEgre(docuEgre);
//		Date fecha = Util.cambiarFormatoFecha(new Date(), "dd-MM-yyyy");		
//		egreso.setFechaEmis(fecha);

		List<Egreso> egresos = new ArrayList<>();
		
		try {
			egresos = egresoLista.buscar(egreso, null, sucursals);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (Egreso egreso2 : egresos) {
			System.out.println("Lista de egresos >>>>>>>>>>>>>>>>>>>>>>>>>>>> " + egreso2.getNumero());
		}
	}

//	@Schedule(second="*/10", minute="*",hour="*", persistent=false)
	public void ejemplo() {
		System.out.println("Lista de egresos  >>>>>>>>>>>>>>>>>>>>>>>>>>>>" );
		this.listar();
	}
}
