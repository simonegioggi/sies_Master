/**
 * RevocaPagamento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class RevocaPagamento  implements java.io.Serializable {
    private java.lang.String codiceCRS;

    private java.util.Calendar dataRevoca;

    private float importoRTRevocata;

    public RevocaPagamento() {
    }

    public RevocaPagamento(
           java.lang.String codiceCRS,
           java.util.Calendar dataRevoca,
           float importoRTRevocata) {
           this.codiceCRS = codiceCRS;
           this.dataRevoca = dataRevoca;
           this.importoRTRevocata = importoRTRevocata;
    }


    /**
     * Gets the codiceCRS value for this RevocaPagamento.
     * 
     * @return codiceCRS
     */
    public java.lang.String getCodiceCRS() {
        return codiceCRS;
    }


    /**
     * Sets the codiceCRS value for this RevocaPagamento.
     * 
     * @param codiceCRS
     */
    public void setCodiceCRS(java.lang.String codiceCRS) {
        this.codiceCRS = codiceCRS;
    }


    /**
     * Gets the dataRevoca value for this RevocaPagamento.
     * 
     * @return dataRevoca
     */
    public java.util.Calendar getDataRevoca() {
        return dataRevoca;
    }


    /**
     * Sets the dataRevoca value for this RevocaPagamento.
     * 
     * @param dataRevoca
     */
    public void setDataRevoca(java.util.Calendar dataRevoca) {
        this.dataRevoca = dataRevoca;
    }


    /**
     * Gets the importoRTRevocata value for this RevocaPagamento.
     * 
     * @return importoRTRevocata
     */
    public float getImportoRTRevocata() {
        return importoRTRevocata;
    }


    /**
     * Sets the importoRTRevocata value for this RevocaPagamento.
     * 
     * @param importoRTRevocata
     */
    public void setImportoRTRevocata(float importoRTRevocata) {
        this.importoRTRevocata = importoRTRevocata;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RevocaPagamento)) return false;
        RevocaPagamento other = (RevocaPagamento) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codiceCRS==null && other.getCodiceCRS()==null) || 
             (this.codiceCRS!=null &&
              this.codiceCRS.equals(other.getCodiceCRS()))) &&
            ((this.dataRevoca==null && other.getDataRevoca()==null) || 
             (this.dataRevoca!=null &&
              this.dataRevoca.equals(other.getDataRevoca()))) &&
            this.importoRTRevocata == other.getImportoRTRevocata();
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
        if (getCodiceCRS() != null) {
            _hashCode += getCodiceCRS().hashCode();
        }
        if (getDataRevoca() != null) {
            _hashCode += getDataRevoca().hashCode();
        }
        _hashCode += new Float(getImportoRTRevocata()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RevocaPagamento.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "revocaPagamento"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceCRS");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceCRS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataRevoca");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataRevoca"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importoRTRevocata");
        elemField.setXmlName(new javax.xml.namespace.QName("", "importoRTRevocata"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
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
