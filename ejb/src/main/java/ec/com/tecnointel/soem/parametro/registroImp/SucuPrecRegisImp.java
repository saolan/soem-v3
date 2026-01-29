package ec.com.tecnointel.soem.parametro.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.parametro.modelo.SucuPrec;
import ec.com.tecnointel.soem.parametro.registroInt.SucuPrecRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class SucuPrecRegisImp extends GestorRegisSoem<SucuPrec> implements SucuPrecRegisInt, Serializable {

	private static final long serialVersionUID = 6018126350441856933L;
	
	@Override
	public SucuPrec buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> sucuPrecGraph = this.entityManager.getEntityGraph("sucuPrec.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", sucuPrecGraph);

		return (SucuPrec) entityManager.find(entidad, id, hints);
	}

}
