package ec.com.tecnointel.soem.tesoreria.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import ec.com.tecnointel.soem.tesoreria.registroInt.FormPagoMoviIngrRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class FormPagoMoviIngrRegisImp extends GestorRegisSoem<FormPagoMoviIngr>
		implements FormPagoMoviIngrRegisInt, Serializable {

	private static final long serialVersionUID = -3349768012732129381L;

	@Override
	public FormPagoMoviIngr buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> fpmiGraph = this.entityManager.getEntityGraph("fpmi.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", fpmiGraph);

		return (FormPagoMoviIngr) entityManager.find(entidad, id, hints);

	}
	
}
