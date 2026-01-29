package ec.com.tecnointel.soem.documeElec.modelo.retencion;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "impuestosDocSustento", propOrder = {
    "impuestoDocSustento"
})
public class ImpuestosDocSustento {

    @XmlElement(required = true)
    protected List<ImpuestoDocSustento> impuestoDocSustento;

    public List<ImpuestoDocSustento> getImpuestoDocSustento() {
        if (impuestoDocSustento == null) {
            impuestoDocSustento = new ArrayList<ImpuestoDocSustento>();
        }
        return this.impuestoDocSustento;
    }

}
