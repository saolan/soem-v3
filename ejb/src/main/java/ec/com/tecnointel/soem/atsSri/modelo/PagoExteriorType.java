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
 * <p>Clase Java para pagoExteriorType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="pagoExteriorType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pagoLocExt" type="{}pagoLocExtType"/>
 *         &lt;element name="tipoRegi" type="{}tipoRegiType" minOccurs="0"/>
 *         &lt;element name="paisEfecPagoGen" type="{}paisEfecPagoType" minOccurs="0"/>
 *         &lt;element name="paisEfecPagoParFis" type="{}paraisoFiscalType" minOccurs="0"/>
 *         &lt;element name="denopagoRegFis" type="{}razonSocialType" minOccurs="0"/>
 *         &lt;element name="paisEfecPago" type="{}paisEfecPagoType" minOccurs="0"/>
 *         &lt;element name="aplicConvDobTrib" type="{}aplicConvDobTribType"/>
 *         &lt;element name="pagExtSujRetNorLeg" type="{}aplicConvDobTribType"/>
 *         &lt;element name="pagoRegFis" type="{}aplicConvDobTribType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pagoExteriorType", propOrder = {
    "pagoLocExt",
    "tipoRegi",
    "paisEfecPagoGen",
    "paisEfecPagoParFis",
    "denopagoRegFis",
    "paisEfecPago",
    "aplicConvDobTrib",
    "pagExtSujRetNorLeg",
    "pagoRegFis"
})
public class PagoExteriorType {

    @XmlElement(required = true)
    protected String pagoLocExt;
    protected String tipoRegi;
    protected String paisEfecPagoGen;
    protected String paisEfecPagoParFis;
    protected String denopagoRegFis;
    protected String paisEfecPago;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected AplicConvDobTribType aplicConvDobTrib;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected AplicConvDobTribType pagExtSujRetNorLeg;
    @XmlSchemaType(name = "string")
    protected AplicConvDobTribType pagoRegFis;

    /**
     * Obtiene el valor de la propiedad pagoLocExt.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPagoLocExt() {
        return pagoLocExt;
    }

    /**
     * Define el valor de la propiedad pagoLocExt.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPagoLocExt(String value) {
        this.pagoLocExt = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoRegi.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoRegi() {
        return tipoRegi;
    }

    /**
     * Define el valor de la propiedad tipoRegi.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoRegi(String value) {
        this.tipoRegi = value;
    }

    /**
     * Obtiene el valor de la propiedad paisEfecPagoGen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaisEfecPagoGen() {
        return paisEfecPagoGen;
    }

    /**
     * Define el valor de la propiedad paisEfecPagoGen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaisEfecPagoGen(String value) {
        this.paisEfecPagoGen = value;
    }

    /**
     * Obtiene el valor de la propiedad paisEfecPagoParFis.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaisEfecPagoParFis() {
        return paisEfecPagoParFis;
    }

    /**
     * Define el valor de la propiedad paisEfecPagoParFis.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaisEfecPagoParFis(String value) {
        this.paisEfecPagoParFis = value;
    }

    /**
     * Obtiene el valor de la propiedad denopagoRegFis.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenopagoRegFis() {
        return denopagoRegFis;
    }

    /**
     * Define el valor de la propiedad denopagoRegFis.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenopagoRegFis(String value) {
        this.denopagoRegFis = value;
    }

    /**
     * Obtiene el valor de la propiedad paisEfecPago.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaisEfecPago() {
        return paisEfecPago;
    }

    /**
     * Define el valor de la propiedad paisEfecPago.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaisEfecPago(String value) {
        this.paisEfecPago = value;
    }

    /**
     * Obtiene el valor de la propiedad aplicConvDobTrib.
     * 
     * @return
     *     possible object is
     *     {@link AplicConvDobTribType }
     *     
     */
    public AplicConvDobTribType getAplicConvDobTrib() {
        return aplicConvDobTrib;
    }

    /**
     * Define el valor de la propiedad aplicConvDobTrib.
     * 
     * @param value
     *     allowed object is
     *     {@link AplicConvDobTribType }
     *     
     */
    public void setAplicConvDobTrib(AplicConvDobTribType value) {
        this.aplicConvDobTrib = value;
    }

    /**
     * Obtiene el valor de la propiedad pagExtSujRetNorLeg.
     * 
     * @return
     *     possible object is
     *     {@link AplicConvDobTribType }
     *     
     */
    public AplicConvDobTribType getPagExtSujRetNorLeg() {
        return pagExtSujRetNorLeg;
    }

    /**
     * Define el valor de la propiedad pagExtSujRetNorLeg.
     * 
     * @param value
     *     allowed object is
     *     {@link AplicConvDobTribType }
     *     
     */
    public void setPagExtSujRetNorLeg(AplicConvDobTribType value) {
        this.pagExtSujRetNorLeg = value;
    }

    /**
     * Obtiene el valor de la propiedad pagoRegFis.
     * 
     * @return
     *     possible object is
     *     {@link AplicConvDobTribType }
     *     
     */
    public AplicConvDobTribType getPagoRegFis() {
        return pagoRegFis;
    }

    /**
     * Define el valor de la propiedad pagoRegFis.
     * 
     * @param value
     *     allowed object is
     *     {@link AplicConvDobTribType }
     *     
     */
    public void setPagoRegFis(AplicConvDobTribType value) {
        this.pagoRegFis = value;
    }

}
