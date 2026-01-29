package ec.com.tecnointel.soem.inventario.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.inventario.modelo.ProdPrec;
import ec.com.tecnointel.soem.inventario.registroInt.ProdPrecRegisInt;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class ProdPrecRegisImp extends GestorRegisSoem<ProdPrec> implements ProdPrecRegisInt, Serializable {

	private static final long serialVersionUID = -4293879033414045431L;

	@Override
	public ProdPrec buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> prodPrecGraph = this.entityManager.getEntityGraph("prodPrec.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", prodPrecGraph);

		return (ProdPrec) entityManager.find(entidad, id, hints);
	}
	
	/**
	 * Calcula nuevo precio
	 * @throws Exception 
	*/
	@Override
	public ProdPrec calcularPrecio(String campoCalculo, ProdPrec prodPrec, Dimm dimm, int redondeo) throws Exception {
						
//		Recupera la implementacion que se va a ejecutar
		ProdPrecModiPrecio procPrecModiPrecio = recuperarInterfaceCalculo(campoCalculo);
			
		return procPrecModiPrecio.calcularPrecio(prodPrec, dimm.getPorcen(), redondeo);
	}
	
	/*
	Recorre un arreglo con las interfaces que calculan el precio para poder seleccionar una de acuerdo 
	al parametro campoCalculo
	*/
	private ProdPrecModiPrecio recuperarInterfaceCalculo (String campoCalculo) {
		
		// Crea el arreglo con las implentaciones del inteface
		ProdPrecModiPrecio[] procPrecModiPrecios = {new ProdPrecModiConImpu(), new ProdPrecModiSinImpu()};
		
		// Recorre las implementaciones
		for (ProdPrecModiPrecio procPrecModiPrecio : procPrecModiPrecios) {

//			Asigna la anotacion con la que esta marcada la implementacion 
			ProdPrecCampoCalculo campoRecalculo =  procPrecModiPrecio.getClass().getAnnotation(ProdPrecCampoCalculo.class);
			
//			Trae el nombre de la anotacion con la que esta marcada la implementacion
			String campo = campoRecalculo.value();
			
//			Compara el valor de la anotacion con el valor que selecciono el usuario
//			para determinar que implentacion seleccionar y retornar la elegida
			if (campoCalculo.equalsIgnoreCase(campo)) {
				return procPrecModiPrecio;
			}
		}
		
		return null;
	}
}