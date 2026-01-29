package ec.com.tecnointel.soem.documeElec.modelo.retencion;

import java.math.BigDecimal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "impuestoDocSustento", propOrder = { "codImpuestoDocSustento", "codigoPorcentaje", "baseImponible",
		"tarifa", "valorImpuesto" })
public class ImpuestoDocSustento {

	@XmlElement(required = true)
	protected String codImpuestoDocSustento;
	@XmlElement(required = true)
	protected String codigoPorcentaje;
	@XmlElement(required = true)
	protected BigDecimal baseImponible;
	@XmlElement(required = true)
	protected BigDecimal tarifa;
	@XmlElement(required = true)
	protected BigDecimal valorImpuesto;

	public String getCodImpuestoDocSustento() {
		return codImpuestoDocSustento;
	}

	public void setCodImpuestoDocSustento(String value) {
		this.codImpuestoDocSustento = value;
	}

	public String getCodigoPorcentaje() {
		return codigoPorcentaje;
	}

	public void setCodigoPorcentaje(String value) {
		this.codigoPorcentaje = value;
	}

	public BigDecimal getBaseImponible() {
		return baseImponible;
	}

	public void setBaseImponible(BigDecimal value) {
		this.baseImponible = value;
	}

	public BigDecimal getTarifa() {
		return tarifa;
	}

	public void setTarifa(BigDecimal value) {
		this.tarifa = value;
	}

	public BigDecimal getValorImpuesto() {
		return valorImpuesto;
	}

	public void setValorImpuesto(BigDecimal value) {
		this.valorImpuesto = value;
	}
}
