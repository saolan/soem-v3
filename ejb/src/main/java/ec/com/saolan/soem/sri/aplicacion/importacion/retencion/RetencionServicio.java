package ec.com.saolan.soem.sri.aplicacion.importacion.retencion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

import ec.com.tecnointel.soem.ingreso.modelo.ReteDeta;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RetencionServicio {

	private static final BigDecimal CIEN = BigDecimal.valueOf(100);
	private static final int ESCALA = 6;
	private static final RoundingMode REDONDEO = RoundingMode.HALF_UP;

	public void calcularReteDeta(List<ReteDeta> reteDetas) {

		Objects.requireNonNull(reteDetas, "La lista de detalles de retención no puede ser null");

		for (ReteDeta reteDeta : reteDetas) {
			BigDecimal baseImponible = reteDeta.getBase() != null ? reteDeta.getBase() : BigDecimal.ZERO;
			BigDecimal porcentage = reteDeta.getPorcen() != null ? reteDeta.getPorcen() : BigDecimal.ZERO;

			reteDeta.setReteDetaTotal(baseImponible.multiply(porcentage).divide(CIEN, ESCALA, REDONDEO));
		}
	}
}
