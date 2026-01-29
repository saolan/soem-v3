package ec.com.tecnointel.soem.caja.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.caja.modelo.CajaMoviTran;
import ec.com.tecnointel.soem.caja.registroInt.CajaMoviTranRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class CajaMoviTranRegisImp extends GestorRegisSoem<CajaMoviTran> implements CajaMoviTranRegisInt, Serializable {

	private static final long serialVersionUID = 6473799087558151348L;

	@Override
	public CajaMoviTran buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> cajaMoviGraph = this.entityManager.getEntityGraph("cajaMoviTran.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", cajaMoviGraph);

		return (CajaMoviTran) entityManager.find(entidad, id, hints);

	}
}
