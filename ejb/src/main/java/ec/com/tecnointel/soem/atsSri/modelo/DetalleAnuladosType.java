//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantaci�n de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perder�n si se vuelve a compilar el esquema de origen. 
// Generado el: 2023.12.05 a las 03:49:23 PM COT 
//


package ec.com.tecnointel.soem.atsSri.modelo;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para detalleAnuladosType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="detalleAnuladosType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tipoComprobante" type="{}tipoComprobanteCompraAnuType"/>
 *         &lt;element name="establecimiento" type="{}establecimientoType"/>
 *         &lt;element name="puntoEmision" type="{}ptoEmisionType"/>
 *         &lt;element name="secuencialInicio" type="{}secuencialType"/>
 *         &lt;element name="secuencialFin" type="{}secuencialType"/>
 *         &lt;element name="autorizacion" type="{}autorizacionType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detalleAnuladosType", propOrder = {
    "tipoComprobante",
    "establecimiento",
    "puntoEmision",
    "secuencialInicio",
    "secuencialFin",
    "autorizacion"
})
public class DetalleAnuladosType {

    @XmlElement(required = true)
    protected String tipoComprobante;
    @XmlElement(required = true)
    protected String establecimiento;
    @XmlElement(required = true)
    protected String puntoEmision;
    @XmlSchemaType(name = "integer")
    protected int secuencialInicio;
    @XmlSchemaType(name = "integer")
    protected int secuencialFin;
    @XmlElement(required = true)
    protected String autorizacion;

    /**
     * Obtiene el valor de la propiedad tipoComprobante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoComprobante() {
        return tipoComprobante;
    }

    /**
     * Define el valor de la propiedad tipoComprobante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoComprobante(String value) {
        this.tipoComprobante = value;
    }

    /**
     * Obtiene el valor de la propiedad establecimiento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstablecimiento() {
        return establecimiento;
    }

    /**
     * Define el valor de la propiedad establecimiento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstablecimiento(String value) {
        this.establecimiento = value;
    }

    /**
     * Obtiene el valor de la propiedad puntoEmision.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPuntoEmision() {
        return puntoEmision;
    }

    /**
     * Define el valor de la propiedad puntoEmision.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPuntoEmision(String value) {
        this.puntoEmision = value;
    }

    /**
     * Obtiene el valor de la propiedad secuencialInicio.
     * 
     */
    public int getSecuencialInicio() {
        return secuencialInicio;
    }

    /**
     * Define el valor de la propiedad secuencialInicio.
     * 
     */
    public void setSecuencialInicio(int value) {
        this.secuencialInicio = value;
    }

    /**
     * Obtiene el valor de la propiedad secuencialFin.
     * 
     */
    public int getSecuencialFin() {
        return secuencialFin;
    }

    /**
     * Define el valor de la propiedad secuencialFin.
     * 
     */
    public void setSecuencialFin(int value) {
        this.secuencialFin = value;
    }

    /**
     * Obtiene el valor de la propiedad autorizacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutorizacion() {
        return autorizacion;
    }

    /**
     * Define el valor de la propiedad autorizacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutorizacion(String value) {
        this.autorizacion = value;
    }

}
