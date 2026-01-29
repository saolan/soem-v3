package ec.com.tecnointel.soem.caja.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.caja.modelo.CajaMovi;
import ec.com.tecnointel.soem.caja.registroInt.CajaMoviRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class CajaMoviRegisImp extends GestorRegisSoem<CajaMovi> implements CajaMoviRegisInt, Serializable {

	private static final long serialVersionUID = 8082718544036401337L;
	
	@Override
	public CajaMovi buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> cajaMoviGraph = this.entityManager.getEntityGraph("cajaMovi.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", cajaMoviGraph);

		return (CajaMovi) entityManager.find(entidad, id, hints);

	}	

}
