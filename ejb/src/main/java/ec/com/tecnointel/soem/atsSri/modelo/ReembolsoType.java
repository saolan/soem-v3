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
 * <p>Clase Java para reembolsoType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="reembolsoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tipoComprobanteReemb" type="{}tipoComprobanteReembType"/>
 *         &lt;element name="tpIdProvReemb" type="{}tpIdProvReembType"/>
 *         &lt;element name="idProvReemb" type="{}idProvType"/>
 *         &lt;element name="establecimientoReemb" type="{}establecimientoType"/>
 *         &lt;element name="puntoEmisionReemb" type="{}ptoEmisionType"/>
 *         &lt;element name="secuencialReemb" type="{}secuencialType"/>
 *         &lt;element name="fechaEmisionReemb" type="{}fechaType"/>
 *         &lt;element name="autorizacionReemb" type="{}autorizacionReembType"/>
 *         &lt;element name="baseImponibleReemb" type="{}monedaType"/>
 *         &lt;element name="baseImpGravReemb" type="{}monedaType"/>
 *         &lt;element name="baseNoGraIvaReemb" type="{}monedaType"/>
 *         &lt;element name="baseImpExeReemb" type="{}monedaType"/>
 *         &lt;element name="montoIceRemb" type="{}monedaType"/>
 *         &lt;element name="montoIvaRemb" type="{}monedaType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reembolsoType", propOrder = {
    "tipoComprobanteReemb",
    "tpIdProvReemb",
    "idProvReemb",
    "establecimientoReemb",
    "puntoEmisionReemb",
    "secuencialReemb",
    "fechaEmisionReemb",
    "autorizacionReemb",
    "baseImponibleReemb",
    "baseImpGravReemb",
    "baseNoGraIvaReemb",
    "baseImpExeReemb",
    "montoIceRemb",
    "montoIvaRemb"
})
public class ReembolsoType {

    @XmlElement(required = true)
    protected String tipoComprobanteReemb;
    @XmlElement(required = true)
    protected String tpIdProvReemb;
    @XmlElement(required = true)
    protected String idProvReemb;
    @XmlElement(required = true)
    protected String establecimientoReemb;
    @XmlElement(required = true)
    protected String puntoEmisionReemb;
    @XmlSchemaType(name = "integer")
    protected int secuencialReemb;
    @XmlElement(required = true)
    protected String fechaEmisionReemb;
    @XmlElement(required = true)
    protected String autorizacionReemb;
    @XmlElement(required = true)
    protected BigDecimal baseImponibleReemb;
    @XmlElement(required = true)
    protected BigDecimal baseImpGravReemb;
    @XmlElement(required = true)
    protected BigDecimal baseNoGraIvaReemb;
    @XmlElement(required = true)
    protected BigDecimal baseImpExeReemb;
    @XmlElement(required = true)
    protected BigDecimal montoIceRemb;
    @XmlElement(required = true)
    protected BigDecimal montoIvaRemb;

    /**
     * Obtiene el valor de la propiedad tipoComprobanteReemb.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoComprobanteReemb() {
        return tipoComprobanteReemb;
    }

    /**
     * Define el valor de la propiedad tipoComprobanteReemb.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoComprobanteReemb(String value) {
        this.tipoComprobanteReemb = value;
    }

    /**
     * Obtiene el valor de la propiedad tpIdProvReemb.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTpIdProvReemb() {
        return tpIdProvReemb;
    }

    /**
     * Define el valor de la propiedad tpIdProvReemb.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTpIdProvReemb(String value) {
        this.tpIdProvReemb = value;
    }

    /**
     * Obtiene el valor de la propiedad idProvReemb.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdProvReemb() {
        return idProvReemb;
    }

    /**
     * Define el valor de la propiedad idProvReemb.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdProvReemb(String value) {
        this.idProvReemb = value;
    }

    /**
     * Obtiene el valor de la propiedad establecimientoReemb.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstablecimientoReemb() {
        return establecimientoReemb;
    }

    /**
     * Define el valor de la propiedad establecimientoReemb.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstablecimientoReemb(String value) {
        this.establecimientoReemb = value;
    }

    /**
     * Obtiene el valor de la propiedad puntoEmisionReemb.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPuntoEmisionReemb() {
        return puntoEmisionReemb;
    }

    /**
     * Define el valor de la propiedad puntoEmisionReemb.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPuntoEmisionReemb(String value) {
        this.puntoEmisionReemb = value;
    }

    /**
     * Obtiene el valor de la propiedad secuencialReemb.
     * 
     */
    public int getSecuencialReemb() {
        return secuencialReemb;
    }

    /**
     * Define el valor de la propiedad secuencialReemb.
     * 
     */
    public void setSecuencialReemb(int value) {
        this.secuencialReemb = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaEmisionReemb.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaEmisionReemb() {
        return fechaEmisionReemb;
    }

    /**
     * Define el valor de la propiedad fechaEmisionReemb.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaEmisionReemb(String value) {
        this.fechaEmisionReemb = value;
    }

    /**
     * Obtiene el valor de la propiedad autorizacionReemb.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutorizacionReemb() {
        return autorizacionReemb;
    }

    /**
     * Define el valor de la propiedad autorizacionReemb.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutorizacionReemb(String value) {
        this.autorizacionReemb = value;
    }

    /**
     * Obtiene el valor de la propiedad baseImponibleReemb.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBaseImponibleReemb() {
        return baseImponibleReemb;
    }

    /**
     * Define el valor de la propiedad baseImponibleReemb.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBaseImponibleReemb(BigDecimal value) {
        this.baseImponibleReemb = value;
    }

    /**
     * Obtiene el valor de la propiedad baseImpGravReemb.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBaseImpGravReemb() {
        return baseImpGravReemb;
    }

    /**
     * Define el valor de la propiedad baseImpGravReemb.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBaseImpGravReemb(BigDecimal value) {
        this.baseImpGravReemb = value;
    }

    /**
     * Obtiene el valor de la propiedad baseNoGraIvaReemb.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBaseNoGraIvaReemb() {
        return baseNoGraIvaReemb;
    }

    /**
     * Define el valor de la propiedad baseNoGraIvaReemb.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBaseNoGraIvaReemb(BigDecimal value) {
        this.baseNoGraIvaReemb = value;
    }

    /**
     * Obtiene el valor de la propiedad baseImpExeReemb.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBaseImpExeReemb() {
        return baseImpExeReemb;
    }

    /**
     * Define el valor de la propiedad baseImpExeReemb.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBaseImpExeReemb(BigDecimal value) {
        this.baseImpExeReemb = value;
    }

    /**
     * Obtiene el valor de la propiedad montoIceRemb.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontoIceRemb() {
        return montoIceRemb;
    }

    /**
     * Define el valor de la propiedad montoIceRemb.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontoIceRemb(BigDecimal value) {
        this.montoIceRemb = value;
    }

    /**
     * Obtiene el valor de la propiedad montoIvaRemb.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontoIvaRemb() {
        return montoIvaRemb;
    }

    /**
     * Define el valor de la propiedad montoIvaRemb.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontoIvaRemb(BigDecimal value) {
        this.montoIvaRemb = value;
    }

}
