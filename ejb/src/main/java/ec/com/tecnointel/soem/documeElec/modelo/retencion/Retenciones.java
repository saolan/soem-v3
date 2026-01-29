package ec.com.tecnointel.soem.documeElec.modelo.retencion;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "retenciones", propOrder = {
    "retencion"
})
public class Retenciones {

    @XmlElement(required = true)
    protected List<Retencion> retencion;

    public List<Retencion> getRetencion() {
        if (retencion == null) {
            retencion = new ArrayList<Retencion>();
        }
        return this.retencion;
    }

}
