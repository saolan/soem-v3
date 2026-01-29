package ec.com.tecnointel.soem.documeElec.modelo.retencion;

import java.math.BigDecimal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pago", propOrder = {
    "formaPago",
    "total"
})
public class Pago {

    @XmlElement(required = true)
    protected String formaPago;
    @XmlElement(required = true)
    protected BigDecimal total;

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String value) {
        this.formaPago = value;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal value) {
        this.total = value;
    }
}
