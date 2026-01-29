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
 * <p>Clase Java para detallefValorType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="detallefValorType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tipoFideicomiso" type="{}tipoFideicomisoType"/>
 *         &lt;element name="totalF" type="{}totalFType"/>
 *         &lt;element name="individualF" type="{}individualFType"/>
 *         &lt;element name="porRetF" type="{}porRetFType"/>
 *         &lt;element name="valorRetF" type="{}valorRetBienesType"/>
 *         &lt;element name="fechaPagoDiv" type="{}fechaType" minOccurs="0"/>
 *         &lt;element name="imRentaSoc" type="{}valorRetBienesType" minOccurs="0"/>
 *         &lt;element name="anioUtDiv" type="{}anioType" minOccurs="0"/>
 *         &lt;element name="pagoExterior" type="{}pagoExteriorType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detallefValorType", propOrder = {
    "tipoFideicomiso",
    "totalF",
    "individualF",
    "porRetF",
    "valorRetF",
    "fechaPagoDiv",
    "imRentaSoc",
    "anioUtDiv",
    "pagoExterior"
})
public class DetallefValorType {

    @XmlSchemaType(name = "integer")
    protected int tipoFideicomiso;
    @XmlElement(required = true)
    protected BigDecimal totalF;
    @XmlElement(required = true)
    protected BigDecimal individualF;
    @XmlElement(required = true)
    protected BigDecimal porRetF;
    @XmlElement(required = true)
    protected BigDecimal valorRetF;
    protected String fechaPagoDiv;
    protected BigDecimal imRentaSoc;
    @XmlSchemaType(name = "integer")
    protected Integer anioUtDiv;
    protected PagoExteriorType pagoExterior;

    /**
     * Obtiene el valor de la propiedad tipoFideicomiso.
     * 
     */
    public int getTipoFideicomiso() {
        return tipoFideicomiso;
    }

    /**
     * Define el valor de la propiedad tipoFideicomiso.
     * 
     */
    public void setTipoFideicomiso(int value) {
        this.tipoFideicomiso = value;
    }

    /**
     * Obtiene el valor de la propiedad totalF.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalF() {
        return totalF;
    }

    /**
     * Define el valor de la propiedad totalF.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalF(BigDecimal value) {
        this.totalF = value;
    }

    /**
     * Obtiene el valor de la propiedad individualF.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIndividualF() {
        return individualF;
    }

    /**
     * Define el valor de la propiedad individualF.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIndividualF(BigDecimal value) {
        this.individualF = value;
    }

    /**
     * Obtiene el valor de la propiedad porRetF.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPorRetF() {
        return porRetF;
    }

    /**
     * Define el valor de la propiedad porRetF.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPorRetF(BigDecimal value) {
        this.porRetF = value;
    }

    /**
     * Obtiene el valor de la propiedad valorRetF.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorRetF() {
        return valorRetF;
    }

    /**
     * Define el valor de la propiedad valorRetF.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorRetF(BigDecimal value) {
        this.valorRetF = value;
    }

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
     *     {@link Integer }
     *     
     */
    public Integer getAnioUtDiv() {
        return anioUtDiv;
    }

    /**
     * Define el valor de la propiedad anioUtDiv.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAnioUtDiv(Integer value) {
        this.anioUtDiv = value;
    }

    /**
     * Obtiene el valor de la propiedad pagoExterior.
     * 
     * @return
     *     possible object is
     *     {@link PagoExteriorType }
     *     
     */
    public PagoExteriorType getPagoExterior() {
        return pagoExterior;
    }

    /**
     * Define el valor de la propiedad pagoExterior.
     * 
     * @param value
     *     allowed object is
     *     {@link PagoExteriorType }
     *     
     */
    public void setPagoExterior(PagoExteriorType value) {
        this.pagoExterior = value;
    }

}
