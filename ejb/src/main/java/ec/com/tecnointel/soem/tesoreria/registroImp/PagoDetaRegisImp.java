package ec.com.tecnointel.soem.tesoreria.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.tesoreria.modelo.PagoDeta;
import ec.com.tecnointel.soem.tesoreria.registroInt.PagoDetaRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class PagoDetaRegisImp extends GestorRegisSoem<PagoDeta> implements PagoDetaRegisInt, Serializable {

	private static final long serialVersionUID = 7085832508510161713L;
	
	@Override
	public PagoDeta buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> pagoDetaGraph = this.entityManager.getEntityGraph("pagoDeta.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", pagoDetaGraph);

		return (PagoDeta) entityManager.find(entidad, id, hints);

	}

}
