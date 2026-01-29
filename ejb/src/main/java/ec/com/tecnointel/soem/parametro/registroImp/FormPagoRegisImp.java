package ec.com.tecnointel.soem.parametro.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.parametro.registroInt.FormPagoRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class FormPagoRegisImp extends GestorRegisSoem<FormPago> implements FormPagoRegisInt, Serializable {

	private static final long serialVersionUID = 873507002642788633L;

	@Override
	public FormPago buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> formPagoGraph = this.entityManager.getEntityGraph("formPago.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", formPagoGraph);

		return (FormPago) entityManager.find(entidad, id, hints);

	}
	
}
