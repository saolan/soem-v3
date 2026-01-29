package ec.com.tecnointel.soem.tesoreria.registroImp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.tesoreria.modelo.Cxc;
import ec.com.tecnointel.soem.tesoreria.modelo.FpmeFormPago;
import ec.com.tecnointel.soem.tesoreria.registroInt.CxcRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class CxcRegisImp extends GestorRegisSoem<Cxc> implements CxcRegisInt, Serializable {

	private static final long serialVersionUID = -6745951211318759699L;

	@Override
	public Cxc buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> cxcGraph = this.entityManager.getEntityGraph("cxc.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", cxcGraph);

		return (Cxc) entityManager.find(entidad, id, hints);
	}

	@Override
//	Genera varias cxc de acuerdo al numero de cuotas ingresado en la cabecera del egreso
//	Por el momento no se usa ya que se se aumento la tabla FpmeFormPago y ahi tiene la informacion
//	para generar las cxc
	public List<Cxc> generarCxc(Egreso egreso, BigDecimal total, Boolean estado) throws Exception {

		List<Cxc> cxcs = new ArrayList<>();

		for (int i = 0; i < egreso.getNumeroCuot(); i++) {

//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(egreso.getFechaEmis());
//
//			if (egreso.getDiasPlaz() == 30 && egreso.getDiasPlaz() == 31) {
//				calendar.add(Calendar.MONTH, (i + 1));
//			} else {
//				calendar.add(Calendar.DATE, egreso.getDiasPlaz());
//			}

			Cxc cxc = new Cxc();
			cxc.setEgreso(egreso);
			cxc.setFechaVenc(egreso.getFechaEmis().plusDays(egreso.getDiasPlaz()));
			cxc.setFechaHoraVenc(cxc.getFechaVenc().atTime(LocalTime.now()));
			cxc.setTotal(total.divide(new BigDecimal(egreso.getNumeroCuot()), 6, RoundingMode.HALF_UP));
			cxc.setEstado(estado);
			cxcs.add(cxc);
		}

		return cxcs;
	}

	@Override
	public Cxc generarCxc(Egreso egreso, FpmeFormPago fpmeFormPago, Boolean estado) throws Exception {

		Cxc cxc = new Cxc();

		cxc.setEgreso(egreso);
		cxc.setFechaVenc(fpmeFormPago.getFecha());
		cxc.setFechaHoraVenc(fpmeFormPago.getFechaHora());
//		El total se coloca despues, porque hay que restar el cambio en caso que sea de contado
//		cxc.setTotal(fpmeFormPago.getTotalReci());
		cxc.setEstado(estado);

		return cxc;
	}

}
