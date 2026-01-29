package ec.com.tecnointel.soem.caja.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.caja.modelo.CajaMoviFormPago;
import ec.com.tecnointel.soem.caja.registroInt.CajaMoviFormPagoRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class CajaMoviFormPagoRegisImp extends GestorRegisSoem<CajaMoviFormPago>
		implements CajaMoviFormPagoRegisInt, Serializable {

	private static final long serialVersionUID = 6060563913524346394L;

	@Override
	public CajaMoviFormPago buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> cmfpGraph = this.entityManager.getEntityGraph("cmfp.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", cmfpGraph);

		return (CajaMoviFormPago) entityManager.find(entidad, id, hints);

	}
}
