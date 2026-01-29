package ec.com.tecnointel.soem.documeElec.modelo.retencion;

import java.math.BigDecimal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detalleImpuesto", propOrder = { "codigo", "codigoPorcentaje", "tarifa", "baseImponibleReembolso",
		"impuestoReembolso" })
public class DetalleImpuesto {

	@XmlElement(required = true)
	protected String codigo;
	@XmlElement(required = true)
	protected String codigoPorcentaje;
	@XmlElement(required = true)
	protected String tarifa;
	@XmlElement(required = true)
	protected BigDecimal baseImponibleReembolso;
	@XmlElement(required = true)
	protected BigDecimal impuestoReembolso;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String value) {
		this.codigo = value;
	}

	public String getCodigoPorcentaje() {
		return codigoPorcentaje;
	}

	public void setCodigoPorcentaje(String value) {
		this.codigoPorcentaje = value;
	}

	public String getTarifa() {
		return tarifa;
	}

	public void setTarifa(String value) {
		this.tarifa = value;
	}

	public BigDecimal getBaseImponibleReembolso() {
		return baseImponibleReembolso;
	}

	public void setBaseImponibleReembolso(BigDecimal value) {
		this.baseImponibleReembolso = value;
	}

	public BigDecimal getImpuestoReembolso() {
		return impuestoReembolso;
	}

	public void setImpuestoReembolso(BigDecimal value) {
		this.impuestoReembolso = value;
	}
}
