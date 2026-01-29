package ec.com.tecnointel.soem.documeElec.modelo.notaDebito;

import java.math.BigDecimal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "impuesto", propOrder = { "codigo", "codigoPorcentaje", "tarifa", "baseImponible", "valor" })
public class Impuesto {
	@XmlElement(required = true)
	protected String codigo;
	@XmlElement(required = true)
	protected String codigoPorcentaje;
	@XmlElement(required = true)
	protected BigDecimal tarifa;
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

	public BigDecimal getTarifa() {
		return this.tarifa;
	}

	public void setTarifa(BigDecimal value) {
		this.tarifa = value;
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
