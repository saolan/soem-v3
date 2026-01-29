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
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para detalleAirComprasType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="detalleAirComprasType">
 *   &lt;complexContent>
 *     &lt;extension base="{}detalleAirType">
 *       &lt;sequence>
 *         &lt;element name="fechaPagoDiv" type="{}fechaPagoDivType" minOccurs="0"/>
 *         &lt;element name="imRentaSoc" type="{}imRentaSocType" minOccurs="0"/>
 *         &lt;element name="anioUtDiv" type="{}anioUtDivType" minOccurs="0"/>
 *         &lt;element name="numCajBan" type="{}numCajBanType" minOccurs="0"/>
 *         &lt;element name="precCajBan" type="{}precCajBanType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detalleAirComprasType", propOrder = {
    "fechaPagoDiv",
    "imRentaSoc",
    "anioUtDiv",
    "numCajBan",
    "precCajBan"
})
public class DetalleAirComprasType
    extends DetalleAirType
{

    protected String fechaPagoDiv;
    protected BigDecimal imRentaSoc;
    protected String anioUtDiv;
    protected BigDecimal numCajBan;
    protected BigDecimal precCajBan;

    /**
     * Obtiene el valor de la propiedad fechaPagoDiv.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaPagoDiv() {
        return fechaPagoDiv;
    }

    /**
     * Define el valor de la propiedad fechaPagoDiv.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaPagoDiv(String value) {
        this.fechaPagoDiv = value;
    }

    /**
     * Obtiene el valor de la propiedad imRentaSoc.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImRentaSoc() {
        return imRentaSoc;
    }

    /**
     * Define el valor de la propiedad imRentaSoc.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImRentaSoc(BigDecimal value) {
        this.imRentaSoc = value;
    }

    /**
     * Obtiene el valor de la propiedad anioUtDiv.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnioUtDiv() {
        return anioUtDiv;
    }

    /**
     * Define el valor de la propiedad anioUtDiv.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnioUtDiv(String value) {
        this.anioUtDiv = value;
    }

    /**
     * Obtiene el valor de la propiedad numCajBan.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNumCajBan() {
        return numCajBan;
    }

    /**
     * Define el valor de la propiedad numCajBan.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNumCajBan(BigDecimal value) {
        this.numCajBan = value;
    }

    /**
     * Obtiene el valor de la propiedad precCajBan.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrecCajBan() {
        return precCajBan;
    }

    /**
     * Define el valor de la propiedad precCajBan.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrecCajBan(BigDecimal value) {
        this.precCajBan = value;
    }

}
