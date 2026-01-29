package ec.com.tecnointel.soem.inventario.registroImp;

import java.io.Serializable;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.inventario.modelo.Kardex;
import ec.com.tecnointel.soem.inventario.registroInt.KardexRegisInt;
import jakarta.ejb.Stateless;

@Stateless
public class KardexRegisImp extends GestorRegisSoem<Kardex> implements KardexRegisInt, Serializable {

	private static final long serialVersionUID = -5521921261803334294L;

	// @Override
	// public Producto buscarPorId(Class<?> entidad, Integer id) {
	//
	// EntityGraph<?> productoGraph =
	// this.entityManager.getEntityGraph("producto.Graph");
	// Map<String, Object> hints = new HashMap<String, Object>();
	// hints.put("jakarta.persistence.loadgraph", productoGraph);
	//
	// return (Kardex) entityManager.find(entidad, id, hints);
	//
	// }

}
