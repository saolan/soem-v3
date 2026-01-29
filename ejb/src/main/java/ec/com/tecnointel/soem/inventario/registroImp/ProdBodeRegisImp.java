package ec.com.tecnointel.soem.inventario.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.inventario.modelo.ProdBode;
import ec.com.tecnointel.soem.inventario.registroInt.ProdBodeRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class ProdBodeRegisImp extends GestorRegisSoem<ProdBode> implements ProdBodeRegisInt, Serializable {

	private static final long serialVersionUID = -1342171553071588161L;

	@Override
	public ProdBode buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> prodBodeGraph = this.entityManager.getEntityGraph("prodBode.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", prodBodeGraph);

		return (ProdBode) entityManager.find(entidad, id, hints);
	}
}
