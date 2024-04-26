/**
 * InfoPagamentoRendicontato.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class InfoPagamentoRendicontato  implements java.io.Serializable {
    private java.lang.String iuv;

    private it.giustizia.www.serviziTelematici.serviziGenerici.DettaglioCrsPagamento[] dettaglioCrs;

    public InfoPagamentoRendicontato() {
    }

    public InfoPagamentoRendicontato(
           java.lang.String iuv,
           it.giustizia.www.serviziTelematici.serviziGenerici.DettaglioCrsPagamento[] dettaglioCrs) {
           this.iuv = iuv;
           this.dettaglioCrs = dettaglioCrs;
    }


    /**
     * Gets the iuv value for this InfoPagamentoRendicontato.
     * 
     * @return iuv
     */
    public java.lang.String getIuv() {
        return iuv;
    }


    /**
     * Sets the iuv value for this InfoPagamentoRendicontato.
     * 
     * @param iuv
     */
    public void setIuv(java.lang.String iuv) {
        this.iuv = iuv;
    }


    /**
     * Gets the dettaglioCrs value for this InfoPagamentoRendicontato.
     * 
     * @return dettaglioCrs
     */
    public it.giustizia.www.serviziTelematici.serviziGenerici.DettaglioCrsPagamento[] getDettaglioCrs() {
        return dettaglioCrs;
    }


    /**
     * Sets the dettaglioCrs value for this InfoPagamentoRendicontato.
     * 
     * @param dettaglioCrs
     */
    public void setDettaglioCrs(it.giustizia.www.serviziTelematici.serviziGenerici.DettaglioCrsPagamento[] dettaglioCrs) {
        this.dettaglioCrs = dettaglioCrs;
    }

    public it.giustizia.www.serviziTelematici.serviziGenerici.DettaglioCrsPagamento getDettaglioCrs(int i) {
        return this.dettaglioCrs[i];
    }

    public void setDettaglioCrs(int i, it.giustizia.www.serviziTelematici.serviziGenerici.DettaglioCrsPagamento _value) {
        this.dettaglioCrs[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InfoPagamentoRendicontato)) return false;
        InfoPagamentoRendicontato other = (InfoPagamentoRendicontato) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.iuv==null && other.getIuv()==null) || 
             (this.iuv!=null &&
              this.iuv.equals(other.getIuv()))) &&
            ((this.dettaglioCrs==null && other.getDettaglioCrs()==null) || 
             (this.dettaglioCrs!=null &&
              java.util.Arrays.equals(this.dettaglioCrs, other.getDettaglioCrs())));
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
        if (getIuv() != null) {
            _hashCode += getIuv().hashCode();
        }
        if (getDettaglioCrs() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDettaglioCrs());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDettaglioCrs(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InfoPagamentoRendicontato.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "InfoPagamentoRendicontato"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("iuv");
        elemField.setXmlName(new javax.xml.namespace.QName("", "iuv"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dettaglioCrs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dettaglioCrs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "DettaglioCrsPagamento"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
