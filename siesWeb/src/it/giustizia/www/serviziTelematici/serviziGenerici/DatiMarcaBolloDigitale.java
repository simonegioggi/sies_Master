/**
 * DatiMarcaBolloDigitale.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class DatiMarcaBolloDigitale  implements java.io.Serializable {
    private java.lang.String tipoBollo;

    private java.lang.String hashDocumento;

    private java.lang.String provinciaResidenza;

    public DatiMarcaBolloDigitale() {
    }

    public DatiMarcaBolloDigitale(
           java.lang.String tipoBollo,
           java.lang.String hashDocumento,
           java.lang.String provinciaResidenza) {
           this.tipoBollo = tipoBollo;
           this.hashDocumento = hashDocumento;
           this.provinciaResidenza = provinciaResidenza;
    }


    /**
     * Gets the tipoBollo value for this DatiMarcaBolloDigitale.
     * 
     * @return tipoBollo
     */
    public java.lang.String getTipoBollo() {
        return tipoBollo;
    }


    /**
     * Sets the tipoBollo value for this DatiMarcaBolloDigitale.
     * 
     * @param tipoBollo
     */
    public void setTipoBollo(java.lang.String tipoBollo) {
        this.tipoBollo = tipoBollo;
    }


    /**
     * Gets the hashDocumento value for this DatiMarcaBolloDigitale.
     * 
     * @return hashDocumento
     */
    public java.lang.String getHashDocumento() {
        return hashDocumento;
    }


    /**
     * Sets the hashDocumento value for this DatiMarcaBolloDigitale.
     * 
     * @param hashDocumento
     */
    public void setHashDocumento(java.lang.String hashDocumento) {
        this.hashDocumento = hashDocumento;
    }


    /**
     * Gets the provinciaResidenza value for this DatiMarcaBolloDigitale.
     * 
     * @return provinciaResidenza
     */
    public java.lang.String getProvinciaResidenza() {
        return provinciaResidenza;
    }


    /**
     * Sets the provinciaResidenza value for this DatiMarcaBolloDigitale.
     * 
     * @param provinciaResidenza
     */
    public void setProvinciaResidenza(java.lang.String provinciaResidenza) {
        this.provinciaResidenza = provinciaResidenza;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatiMarcaBolloDigitale)) return false;
        DatiMarcaBolloDigitale other = (DatiMarcaBolloDigitale) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tipoBollo==null && other.getTipoBollo()==null) || 
             (this.tipoBollo!=null &&
              this.tipoBollo.equals(other.getTipoBollo()))) &&
            ((this.hashDocumento==null && other.getHashDocumento()==null) || 
             (this.hashDocumento!=null &&
              this.hashDocumento.equals(other.getHashDocumento()))) &&
            ((this.provinciaResidenza==null && other.getProvinciaResidenza()==null) || 
             (this.provinciaResidenza!=null &&
              this.provinciaResidenza.equals(other.getProvinciaResidenza())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getTipoBollo() != null) {
            _hashCode += getTipoBollo().hashCode();
        }
        if (getHashDocumento() != null) {
            _hashCode += getHashDocumento().hashCode();
        }
        if (getProvinciaResidenza() != null) {
            _hashCode += getProvinciaResidenza().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatiMarcaBolloDigitale.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "datiMarcaBolloDigitale"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoBollo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoBollo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hashDocumento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "hashDocumento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("provinciaResidenza");
        elemField.setXmlName(new javax.xml.namespace.QName("", "provinciaResidenza"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
