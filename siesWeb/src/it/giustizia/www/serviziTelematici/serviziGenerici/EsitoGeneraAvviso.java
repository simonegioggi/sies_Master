/**
 * EsitoGeneraAvviso.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class EsitoGeneraAvviso  implements java.io.Serializable {
    private byte[] bollettino;

    private java.lang.String numeroAvviso;

    public EsitoGeneraAvviso() {
    }

    public EsitoGeneraAvviso(
           byte[] bollettino,
           java.lang.String numeroAvviso) {
           this.bollettino = bollettino;
           this.numeroAvviso = numeroAvviso;
    }


    /**
     * Gets the bollettino value for this EsitoGeneraAvviso.
     * 
     * @return bollettino
     */
    public byte[] getBollettino() {
        return bollettino;
    }


    /**
     * Sets the bollettino value for this EsitoGeneraAvviso.
     * 
     * @param bollettino
     */
    public void setBollettino(byte[] bollettino) {
        this.bollettino = bollettino;
    }


    /**
     * Gets the numeroAvviso value for this EsitoGeneraAvviso.
     * 
     * @return numeroAvviso
     */
    public java.lang.String getNumeroAvviso() {
        return numeroAvviso;
    }


    /**
     * Sets the numeroAvviso value for this EsitoGeneraAvviso.
     * 
     * @param numeroAvviso
     */
    public void setNumeroAvviso(java.lang.String numeroAvviso) {
        this.numeroAvviso = numeroAvviso;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EsitoGeneraAvviso)) return false;
        EsitoGeneraAvviso other = (EsitoGeneraAvviso) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.bollettino==null && other.getBollettino()==null) || 
             (this.bollettino!=null &&
              java.util.Arrays.equals(this.bollettino, other.getBollettino()))) &&
            ((this.numeroAvviso==null && other.getNumeroAvviso()==null) || 
             (this.numeroAvviso!=null &&
              this.numeroAvviso.equals(other.getNumeroAvviso())));
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
        if (getBollettino() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBollettino());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBollettino(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNumeroAvviso() != null) {
            _hashCode += getNumeroAvviso().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EsitoGeneraAvviso.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "esitoGeneraAvviso"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bollettino");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bollettino"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroAvviso");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroAvviso"));
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
