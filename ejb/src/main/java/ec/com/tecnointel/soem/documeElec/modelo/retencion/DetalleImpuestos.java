package ec.com.tecnointel.soem.documeElec.modelo.retencion;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detalleImpuestos", propOrder = {
    "detalleImpuesto"
})
public class DetalleImpuestos {

    @XmlElement(required = true)
    protected List<DetalleImpuesto> detalleImpuesto;
    
    public List<DetalleImpuesto> getDetalleImpuesto() {
        if (detalleImpuesto == null) {
            detalleImpuesto = new ArrayList<DetalleImpuesto>();
        }
        return this.detalleImpuesto;
    }

}
