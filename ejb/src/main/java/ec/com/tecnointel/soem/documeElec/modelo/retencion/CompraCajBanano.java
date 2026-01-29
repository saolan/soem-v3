package ec.com.tecnointel.soem.documeElec.modelo.retencion;

import java.math.BigDecimal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "compraCajBanano", propOrder = {
    "numCajBan",
    "precCajBan"
})
public class CompraCajBanano {

    @XmlElement(required = true)
    protected String numCajBan;
    @XmlElement(required = true)
    protected BigDecimal precCajBan;

    public String getNumCajBan() {
        return numCajBan;
    }

    public void setNumCajBan(String value) {
        this.numCajBan = value;
    }

    public BigDecimal getPrecCajBan() {
        return precCajBan;
    }

    public void setPrecCajBan(BigDecimal value) {
        this.precCajBan = value;
    }
}
