package ec.com.tecnointel.soem.inventario.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.inventario.registroInt.ProductoRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class ProductoRegisImp extends GestorRegisSoem<Producto> implements ProductoRegisInt, Serializable {

	private static final long serialVersionUID = -1374248277967880746L;
	
	@Override
	public Producto buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> productoGraph = this.entityManager.getEntityGraph("producto.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", productoGraph);

		return (Producto) entityManager.find(entidad, id, hints);

	}

}
