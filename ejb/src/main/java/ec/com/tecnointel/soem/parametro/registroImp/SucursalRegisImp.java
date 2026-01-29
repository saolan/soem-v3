package ec.com.tecnointel.soem.parametro.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.registroInt.SucursalRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class SucursalRegisImp extends GestorRegisSoem<Sucursal> implements SucursalRegisInt, Serializable {

	private static final long serialVersionUID = -6570866476042744317L;
	
	@Override
	public Sucursal buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> sucursalGraph = this.entityManager.getEntityGraph("sucursal.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", sucursalGraph);

		return (Sucursal) entityManager.find(entidad, id, hints);

	}

}
