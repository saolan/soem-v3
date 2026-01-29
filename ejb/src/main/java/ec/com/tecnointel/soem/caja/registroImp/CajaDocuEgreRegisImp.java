package ec.com.tecnointel.soem.caja.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.caja.modelo.CajaDocuEgre;
import ec.com.tecnointel.soem.caja.registroInt.CajaDocuEgreRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class CajaDocuEgreRegisImp extends GestorRegisSoem<CajaDocuEgre> implements CajaDocuEgreRegisInt, Serializable {

	private static final long serialVersionUID = 8983560126509212831L;

	@Override
	public CajaDocuEgre buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> cajaDocuEgreGraph = this.entityManager.getEntityGraph("cajaDocuEgre.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", cajaDocuEgreGraph);

		return (CajaDocuEgre) entityManager.find(entidad, id, hints);		
	}
}
