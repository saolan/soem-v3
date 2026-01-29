package ec.com.tecnointel.soem.seguridad.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.seguridad.modelo.RolFormPago;
import ec.com.tecnointel.soem.seguridad.registroInt.RolFormPagoRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class RolFormPagoRegisImp extends GestorRegisSoem<RolFormPago> implements RolFormPagoRegisInt, Serializable {

	private static final long serialVersionUID = -679897362885198335L;

	@Override
	public RolFormPago buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> rolFormPagoGraph = this.entityManager.getEntityGraph("rolFormPago.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", rolFormPagoGraph);

		return (RolFormPago) entityManager.find(entidad, id, hints);
	}
}
