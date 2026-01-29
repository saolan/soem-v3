//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantaci�n de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perder�n si se vuelve a compilar el esquema de origen. 
// Generado el: 2023.12.05 a las 03:49:23 PM COT 
//


package ec.com.tecnointel.soem.atsSri.modelo;

import java.math.BigDecimal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ventaEstType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ventaEstType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codEstab" type="{}ventasEstabType"/>
 *         &lt;element name="ventasEstab" type="{}totalVentasType"/>
 *         &lt;element name="ivaComp" type="{}totalVentasType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ventaEstType", propOrder = {
    "codEstab",
    "ventasEstab",
    "ivaComp"
})
public class VentaEstType {

    @XmlSchemaType(name = "integer")
    protected int codEstab;
    @XmlElement(required = true)
    protected BigDecimal ventasEstab;
    protected BigDecimal ivaComp;

    /**
     * Obtiene el valor de la propiedad codEstab.
     * 
     */
    public int getCodEstab() {
        return codEstab;
    }

    /**
     * Define el valor de la propiedad codEstab.
     * 
     */
    public void setCodEstab(int value) {
        this.codEstab = value;
    }

    /**
     * Obtiene el valor de la propiedad ventasEstab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVentasEstab() {
        return ventasEstab;
    }

    /**
     * Define el valor de la propiedad ventasEstab.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVentasEstab(BigDecimal value) {
        this.ventasEstab = value;
    }

    /**
     * Obtiene el valor de la propiedad ivaComp.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIvaComp() {
        return ivaComp;
    }

    /**
     * Define el valor de la propiedad ivaComp.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIvaComp(BigDecimal value) {
        this.ivaComp = value;
    }

}
