package ec.com.tecnointel.soem.documeElec.modelo.notaCredito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "totalImpuesto" })
@XmlRootElement(name = "totalConImpuestos")
public class TotalConImpuestos {
	@XmlElement(required = true)
	protected List<TotalImpuesto> totalImpuesto;

	public List<TotalImpuesto> getTotalImpuesto() {
		if (this.totalImpuesto == null) {
			this.totalImpuesto = new ArrayList();
		}
		return this.totalImpuesto;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "codigo", "codigoPorcentaje", "baseImponible", "valor" })
	public static class TotalImpuesto {
		@XmlElement(required = true)
		protected String codigo;
		@XmlElement(required = true)
		protected String codigoPorcentaje;
		@XmlElement(required = true)
		protected BigDecimal baseImponible;
		@XmlElement(required = true)
		protected BigDecimal valor;

		public String getCodigo() {
			return this.codigo;
		}

		public void setCodigo(String value) {
			this.codigo = value;
		}

		public String getCodigoPorcentaje() {
			return this.codigoPorcentaje;
		}

		public void setCodigoPorcentaje(String value) {
			this.codigoPorcentaje = value;
		}

		public BigDecimal getBaseImponible() {
			return this.baseImponible;
		}

		public void setBaseImponible(BigDecimal value) {
			this.baseImponible = value;
		}

		public BigDecimal getValor() {
			return this.valor;
		}

		public void setValor(BigDecimal value) {
			this.valor = value;
		}
	}
}
