package ec.com.tecnointel.soem.documeElec.modelo.retencion;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pagos", propOrder = {
    "pago"
})
public class Pagos {

    @XmlElement(required = true)
    protected List<Pago> pago;

    public List<Pago> getPago() {
        if (pago == null) {
            pago = new ArrayList<Pago>();
        }
        return this.pago;
    }

}
