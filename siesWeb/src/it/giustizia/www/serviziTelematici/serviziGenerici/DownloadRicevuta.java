/**
 * DownloadRicevuta.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class DownloadRicevuta  implements java.io.Serializable {
    private java.lang.String codiceCRS;

    private boolean originale;

    public DownloadRicevuta() {
    }

    public DownloadRicevuta(
           java.lang.String codiceCRS,
           boolean originale) {
           this.codiceCRS = codiceCRS;
           this.originale = originale;
    }


    /**
     * Gets the codiceCRS value for this DownloadRicevuta.
     * 
     * @return codiceCRS
     */
    public java.lang.String getCodiceCRS() {
        return codiceCRS;
    }


    /**
     * Sets the codiceCRS value for this DownloadRicevuta.
     * 
     * @param codiceCRS
     */
    public void setCodiceCRS(java.lang.String codiceCRS) {
        this.codiceCRS = codiceCRS;
    }


    /**
     * Gets the originale value for this DownloadRicevuta.
     * 
     * @return originale
     */
    public boolean isOriginale() {
        return originale;
    }


    /**
     * Sets the originale value for this DownloadRicevuta.
     * 
     * @param originale
     */
    public void setOriginale(boolean originale) {
        this.originale = originale;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DownloadRicevuta)) return false;
        DownloadRicevuta other = (DownloadRicevuta) obj;
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
            this.originale == other.isOriginale();
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
        _hashCode += (isOriginale() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DownloadRicevuta.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "downloadRicevuta"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceCRS");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceCRS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("originale");
        elemField.setXmlName(new javax.xml.namespace.QName("", "originale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
