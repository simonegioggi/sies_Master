/**
 * Applicazioni.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.pstbe.gestione;

public class Applicazioni  extends it.giustizia.www.serviziTelematici.pstbe.gestione.Anagrafica  implements java.io.Serializable {
    private java.lang.String id;

    private java.lang.String certificatoHash;

    private byte[] certificatoSSL;

    private java.lang.String codPagTel;

    private java.lang.String descrizione;

    private java.lang.String url;

    public Applicazioni() {
    }

    public Applicazioni(
           java.lang.String id,
           java.lang.String certificatoHash,
           byte[] certificatoSSL,
           java.lang.String codPagTel,
           java.lang.String descrizione,
           java.lang.String url) {
        this.id = id;
        this.certificatoHash = certificatoHash;
        this.certificatoSSL = certificatoSSL;
        this.codPagTel = codPagTel;
        this.descrizione = descrizione;
        this.url = url;
    }


    /**
     * Gets the id value for this Applicazioni.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this Applicazioni.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the certificatoHash value for this Applicazioni.
     * 
     * @return certificatoHash
     */
    public java.lang.String getCertificatoHash() {
        return certificatoHash;
    }


    /**
     * Sets the certificatoHash value for this Applicazioni.
     * 
     * @param certificatoHash
     */
    public void setCertificatoHash(java.lang.String certificatoHash) {
        this.certificatoHash = certificatoHash;
    }


    /**
     * Gets the certificatoSSL value for this Applicazioni.
     * 
     * @return certificatoSSL
     */
    public byte[] getCertificatoSSL() {
        return certificatoSSL;
    }


    /**
     * Sets the certificatoSSL value for this Applicazioni.
     * 
     * @param certificatoSSL
     */
    public void setCertificatoSSL(byte[] certificatoSSL) {
        this.certificatoSSL = certificatoSSL;
    }


    /**
     * Gets the codPagTel value for this Applicazioni.
     * 
     * @return codPagTel
     */
    public java.lang.String getCodPagTel() {
        return codPagTel;
    }


    /**
     * Sets the codPagTel value for this Applicazioni.
     * 
     * @param codPagTel
     */
    public void setCodPagTel(java.lang.String codPagTel) {
        this.codPagTel = codPagTel;
    }


    /**
     * Gets the descrizione value for this Applicazioni.
     * 
     * @return descrizione
     */
    public java.lang.String getDescrizione() {
        return descrizione;
    }


    /**
     * Sets the descrizione value for this Applicazioni.
     * 
     * @param descrizione
     */
    public void setDescrizione(java.lang.String descrizione) {
        this.descrizione = descrizione;
    }


    /**
     * Gets the url value for this Applicazioni.
     * 
     * @return url
     */
    public java.lang.String getUrl() {
        return url;
    }


    /**
     * Sets the url value for this Applicazioni.
     * 
     * @param url
     */
    public void setUrl(java.lang.String url) {
        this.url = url;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Applicazioni)) return false;
        Applicazioni other = (Applicazioni) obj;
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
            ((this.certificatoHash==null && other.getCertificatoHash()==null) || 
             (this.certificatoHash!=null &&
              this.certificatoHash.equals(other.getCertificatoHash()))) &&
            ((this.certificatoSSL==null && other.getCertificatoSSL()==null) || 
             (this.certificatoSSL!=null &&
              java.util.Arrays.equals(this.certificatoSSL, other.getCertificatoSSL()))) &&
            ((this.codPagTel==null && other.getCodPagTel()==null) || 
             (this.codPagTel!=null &&
              this.codPagTel.equals(other.getCodPagTel()))) &&
            ((this.descrizione==null && other.getDescrizione()==null) || 
             (this.descrizione!=null &&
              this.descrizione.equals(other.getDescrizione()))) &&
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
        if (getCertificatoHash() != null) {
            _hashCode += getCertificatoHash().hashCode();
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
        if (getCodPagTel() != null) {
            _hashCode += getCodPagTel().hashCode();
        }
        if (getDescrizione() != null) {
            _hashCode += getDescrizione().hashCode();
        }
        if (getUrl() != null) {
            _hashCode += getUrl().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Applicazioni.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "applicazioni"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("certificatoHash");
        elemField.setXmlName(new javax.xml.namespace.QName("", "certificatoHash"));
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
        elemField.setFieldName("codPagTel");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codPagTel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
