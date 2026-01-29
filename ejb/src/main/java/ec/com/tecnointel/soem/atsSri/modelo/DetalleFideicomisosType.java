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
 * <p>Clase Java para detalleFideicomisosType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="detalleFideicomisosType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tipoBeneficiario" type="{}tpIdProvType"/>
 *         &lt;element name="idBeneficiario" type="{}idProvType"/>
 *         &lt;element name="parteRelExp" type="{}parteRelType" minOccurs="0"/>
 *         &lt;element name="tipoBeneficiarioCli" type="{}tipoProvType" minOccurs="0"/>
 *         &lt;element name="denoBenefi" type="{}denoProvType" minOccurs="0"/>
 *         &lt;element name="rucFideicomiso" type="{}numeroRucType"/>
 *         &lt;element name="fValor" type="{}fValorType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detalleFideicomisosType", propOrder = {
    "tipoBeneficiario",
    "idBeneficiario",
    "parteRelExp",
    "tipoBeneficiarioCli",
    "denoBenefi",
    "rucFideicomiso",
    "fValor"
})
public class DetalleFideicomisosType {

    @XmlElement(required = true)
    protected String tipoBeneficiario;
    @XmlElement(required = true)
    protected String idBeneficiario;
    @XmlSchemaType(name = "string")
    protected ParteRelType parteRelExp;
    protected String tipoBeneficiarioCli;
    protected String denoBenefi;
    @XmlElement(required = true)
    protected String rucFideicomiso;
    @XmlElement(required = true)
    protected FValorType fValor;

    /**
     * Obtiene el valor de la propiedad tipoBeneficiario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoBeneficiario() {
        return tipoBeneficiario;
    }

    /**
     * Define el valor de la propiedad tipoBeneficiario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoBeneficiario(String value) {
        this.tipoBeneficiario = value;
    }

    /**
     * Obtiene el valor de la propiedad idBeneficiario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdBeneficiario() {
        return idBeneficiario;
    }

    /**
     * Define el valor de la propiedad idBeneficiario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdBeneficiario(String value) {
        this.idBeneficiario = value;
    }

    /**
     * Obtiene el valor de la propiedad parteRelExp.
     * 
     * @return
     *     possible object is
     *     {@link ParteRelType }
     *     
     */
    public ParteRelType getParteRelExp() {
        return parteRelExp;
    }

    /**
     * Define el valor de la propiedad parteRelExp.
     * 
     * @param value
     *     allowed object is
     *     {@link ParteRelType }
     *     
     */
    public void setParteRelExp(ParteRelType value) {
        this.parteRelExp = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoBeneficiarioCli.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoBeneficiarioCli() {
        return tipoBeneficiarioCli;
    }

    /**
     * Define el valor de la propiedad tipoBeneficiarioCli.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoBeneficiarioCli(String value) {
        this.tipoBeneficiarioCli = value;
    }

    /**
     * Obtiene el valor de la propiedad denoBenefi.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenoBenefi() {
        return denoBenefi;
    }

    /**
     * Define el valor de la propiedad denoBenefi.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenoBenefi(String value) {
        this.denoBenefi = value;
    }

    /**
     * Obtiene el valor de la propiedad rucFideicomiso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRucFideicomiso() {
        return rucFideicomiso;
    }

    /**
     * Define el valor de la propiedad rucFideicomiso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRucFideicomiso(String value) {
        this.rucFideicomiso = value;
    }

    /**
     * Obtiene el valor de la propiedad fValor.
     * 
     * @return
     *     possible object is
     *     {@link FValorType }
     *     
     */
    public FValorType getFValor() {
        return fValor;
    }

    /**
     * Define el valor de la propiedad fValor.
     * 
     * @param value
     *     allowed object is
     *     {@link FValorType }
     *     
     */
    public void setFValor(FValorType value) {
        this.fValor = value;
    }

}
