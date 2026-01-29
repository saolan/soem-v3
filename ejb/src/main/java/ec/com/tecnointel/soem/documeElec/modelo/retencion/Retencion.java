package ec.com.tecnointel.soem.documeElec.modelo.retencion;

import java.math.BigDecimal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "retencion", propOrder = {
    "codigo",
    "codigoRetencion",
    "baseImponible",
    "porcentajeRetener",
    "valorRetenido",
    "dividendos",
    "compraCajBanano"
})
public class Retencion {

    @XmlElement(required = true)
    protected String codigo;
    @XmlElement(required = true)
    protected String codigoRetencion;
    @XmlElement(required = true)
    protected BigDecimal baseImponible;
    @XmlElement(required = true)
    protected BigDecimal porcentajeRetener;
    @XmlElement(required = true)
    protected BigDecimal valorRetenido;
    protected Dividendos dividendos;
    protected CompraCajBanano compraCajBanano;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String value) {
        this.codigo = value;
    }

    public String getCodigoRetencion() {
        return codigoRetencion;
    }

    public void setCodigoRetencion(String value) {
        this.codigoRetencion = value;
    }

    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(BigDecimal value) {
        this.baseImponible = value;
    }

    public BigDecimal getPorcentajeRetener() {
        return porcentajeRetener;
    }

    public void setPorcentajeRetener(BigDecimal value) {
        this.porcentajeRetener = value;
    }

    public BigDecimal getValorRetenido() {
        return valorRetenido;
    }

    public void setValorRetenido(BigDecimal value) {
        this.valorRetenido = value;
    }

    public Dividendos getDividendos() {
        return dividendos;
    }

    public void setDividendos(Dividendos value) {
        this.dividendos = value;
    }

    public CompraCajBanano getCompraCajBanano() {
        return compraCajBanano;
    }

    public void setCompraCajBanano(CompraCajBanano value) {
        this.compraCajBanano = value;
    }
}
