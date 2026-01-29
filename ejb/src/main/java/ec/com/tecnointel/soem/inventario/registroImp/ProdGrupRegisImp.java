package ec.com.tecnointel.soem.inventario.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrup;
import ec.com.tecnointel.soem.inventario.registroInt.ProdGrupRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class ProdGrupRegisImp extends GestorRegisSoem<ProdGrup> implements ProdGrupRegisInt, Serializable {

	private static final long serialVersionUID = -432348853702846693L;
	
	@Override
	public ProdGrup buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> prodGrupGraph = this.entityManager.getEntityGraph("prodGrup.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", prodGrupGraph);

		return (ProdGrup) entityManager.find(entidad, id, hints);

	}

}
