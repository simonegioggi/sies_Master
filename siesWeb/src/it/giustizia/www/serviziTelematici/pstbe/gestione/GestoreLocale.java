/**
 * GestoreLocale.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.pstbe.gestione;

public class GestoreLocale  extends it.giustizia.www.serviziTelematici.pstbe.gestione.Anagrafica  implements java.io.Serializable {
    private java.lang.String id;

    private byte[] certificatoSSL;

    private java.lang.String descrizione;

    private it.giustizia.www.serviziTelematici.pstbe.gestione.Distretti distretto;

    private java.lang.String url;

    public GestoreLocale() {
    }

    public GestoreLocale(
           java.lang.String id,
           byte[] certificatoSSL,
           java.lang.String descrizione,
           it.giustizia.www.serviziTelematici.pstbe.gestione.Distretti distretto,
           java.lang.String url) {
        this.id = id;
        this.certificatoSSL = certificatoSSL;
        this.descrizione = descrizione;
        this.distretto = distretto;
        this.url = url;
    }


    /**
     * Gets the id value for this GestoreLocale.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this GestoreLocale.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the certificatoSSL value for this GestoreLocale.
     * 
     * @return certificatoSSL
     */
    public byte[] getCertificatoSSL() {
        return certificatoSSL;
    }


    /**
     * Sets the certificatoSSL value for this GestoreLocale.
     * 
     * @param certificatoSSL
     */
    public void setCertificatoSSL(byte[] certificatoSSL) {
        this.certificatoSSL = certificatoSSL;
    }


    /**
     * Gets the descrizione value for this GestoreLocale.
     * 
     * @return descrizione
     */
    public java.lang.String getDescrizione() {
        return descrizione;
    }


    /**
     * Sets the descrizione value for this GestoreLocale.
     * 
     * @param descrizione
     */
    public void setDescrizione(java.lang.String descrizione) {
        this.descrizione = descrizione;
    }


    /**
     * Gets the distretto value for this GestoreLocale.
     * 
     * @return distretto
     */
    public it.giustizia.www.serviziTelematici.pstbe.gestione.Distretti getDistretto() {
        return distretto;
    }


    /**
     * Sets the distretto value for this GestoreLocale.
     * 
     * @param distretto
     */
    public void setDistretto(it.giustizia.www.serviziTelematici.pstbe.gestione.Distretti distretto) {
        this.distretto = distretto;
    }


    /**
     * Gets the url value for this GestoreLocale.
     * 
     * @return url
     */
    public java.lang.String getUrl() {
        return url;
    }


    /**
     * Sets the url value for this GestoreLocale.
     * 
     * @param url
     */
    public void setUrl(java.lang.String url) {
        this.url = url;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GestoreLocale)) return false;
        GestoreLocale other = (GestoreLocale) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.certificatoSSL==null && other.getCertificatoSSL()==null) || 
             (this.certificatoSSL!=null &&
              java.util.Arrays.equals(this.certificatoSSL, other.getCertificatoSSL()))) &&
            ((this.descrizione==null && other.getDescrizione()==null) || 
             (this.descrizione!=null &&
              this.descrizione.equals(other.getDescrizione()))) &&
            ((this.distretto==null && other.getDistretto()==null) || 
             (this.distretto!=null &&
              this.distretto.equals(other.getDistretto()))) &&
            ((this.url==null && other.getUrl()==null) || 
             (this.url!=null &&
              this.url.equals(other.getUrl())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getCertificatoSSL() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCertificatoSSL());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCertificatoSSL(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDescrizione() != null) {
            _hashCode += getDescrizione().hashCode();
        }
        if (getDistretto() != null) {
            _hashCode += getDistretto().hashCode();
        }
        if (getUrl() != null) {
            _hashCode += getUrl().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GestoreLocale.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "gestoreLocale"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("certificatoSSL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "certificatoSSL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descrizione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descrizione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distretto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "distretto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "distretti"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("url");
        elemField.setXmlName(new javax.xml.namespace.QName("", "url"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
