package ec.com.tecnointel.soem.documeElec.modelo.liquiCompra;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "maquinaFiscal", propOrder = {
    "marca",
    "modelo",
    "serie"
})
public class MaquinaFiscal {

    @XmlElement(required = true)
    protected String marca;
    @XmlElement(required = true)
    protected String modelo;
    @XmlElement(required = true)
    protected String serie;

    public String getMarca() {
        return marca;
    }

    public void setMarca(String value) {
        this.marca = value;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String value) {
        this.modelo = value;
    }

    public String getSerie() {
        return serie;
    }
    public void setSerie(String value) {
        this.serie = value;
    }
}
