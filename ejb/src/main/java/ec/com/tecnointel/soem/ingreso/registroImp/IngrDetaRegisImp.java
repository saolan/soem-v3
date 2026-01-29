package ec.com.tecnointel.soem.ingreso.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDeta;
import ec.com.tecnointel.soem.ingreso.registroInt.IngrDetaRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class IngrDetaRegisImp extends GestorRegisSoem<IngrDeta> implements IngrDetaRegisInt, Serializable {

	private static final long serialVersionUID = 2093156576702157475L;

	@Override
	public IngrDeta buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> ingrDetaGraph = this.entityManager.getEntityGraph("ingrDeta.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", ingrDetaGraph);

		return (IngrDeta) entityManager.find(entidad, id, hints);
	}

	// public void eliminar(IngrDeta ingrDeta) {
	// if(this.entityManager.contains(ingrDeta)){
	// this.entityManager.remove(this.entityManager.merge(ingrDeta));
	// }
	//
	// }

}
