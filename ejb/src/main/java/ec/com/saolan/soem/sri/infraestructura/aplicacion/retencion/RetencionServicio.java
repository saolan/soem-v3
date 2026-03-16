package ec.com.saolan.soem.sri.infraestructura.aplicacion.retencion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import ec.com.tecnointel.soem.ingreso.modelo.ReteDeta;
import jakarta.ejb.Stateless;

@Stateless
public class RetencionServicio implements Serializable, IRetencionServicio {

	private static final long serialVersionUID = 1L;

	private static final BigDecimal CIEN = BigDecimal.valueOf(100);
	private static final int ESCALA = 6;
	private static final RoundingMode REDONDEO = RoundingMode.HALF_UP;

	@Override
	public void calcularReteDeta(List<ReteDeta> reteDetas) {

		if (reteDetas == null) {
			return;
		}

		for (ReteDeta reteDeta : reteDetas) {
			BigDecimal baseImponible = reteDeta.getBase() != null ? reteDeta.getBase() : BigDecimal.ZERO;
			BigDecimal porcentage = reteDeta.getPorcen() != null ? reteDeta.getPorcen() : BigDecimal.ZERO;

			reteDeta.setReteDetaTotal(baseImponible.multiply(porcentage).divide(CIEN, ESCALA, REDONDEO));
		}
	}
}
