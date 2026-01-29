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
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para detalleAirType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="detalleAirType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codRetAir" type="{}codRetAirType"/>
 *         &lt;element name="baseImpAir" type="{}baseImponibleType"/>
 *         &lt;element name="porcentajeAir" type="{}porcentajeAirType"/>
 *         &lt;element name="valRetAir" type="{}valorRetBienesType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detalleAirType", propOrder = {
    "codRetAir",
    "baseImpAir",
    "porcentajeAir",
    "valRetAir"
})
@XmlSeeAlso({
    DetalleAirComprasType.class
})
public class DetalleAirType {

    @XmlElement(required = true)
    protected String codRetAir;
    @XmlElement(required = true)
    protected BigDecimal baseImpAir;
    @XmlElement(required = true)
    protected BigDecimal porcentajeAir;
    @XmlElement(required = true)
    protected BigDecimal valRetAir;

    /**
     * Obtiene el valor de la propiedad codRetAir.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodRetAir() {
        return codRetAir;
    }

    /**
     * Define el valor de la propiedad codRetAir.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodRetAir(String value) {
        this.codRetAir = value;
    }

    /**
     * Obtiene el valor de la propiedad baseImpAir.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBaseImpAir() {
        return baseImpAir;
    }

    /**
     * Define el valor de la propiedad baseImpAir.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBaseImpAir(BigDecimal value) {
        this.baseImpAir = value;
    }

    /**
     * Obtiene el valor de la propiedad porcentajeAir.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPorcentajeAir() {
        return porcentajeAir;
    }

    /**
     * Define el valor de la propiedad porcentajeAir.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPorcentajeAir(BigDecimal value) {
        this.porcentajeAir = value;
    }

    /**
     * Obtiene el valor de la propiedad valRetAir.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValRetAir() {
        return valRetAir;
    }

    /**
     * Define el valor de la propiedad valRetAir.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValRetAir(BigDecimal value) {
        this.valRetAir = value;
    }

}
