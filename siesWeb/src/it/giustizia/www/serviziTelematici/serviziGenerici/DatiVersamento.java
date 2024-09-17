/**
 * DatiVersamento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class DatiVersamento  implements java.io.Serializable {
    private java.math.BigDecimal importoTotale;

    private it.giustizia.www.serviziTelematici.serviziGenerici.DatiSingoloVersamento[] datiSingoloVersamento;

    public DatiVersamento() {
    }

    public DatiVersamento(
           java.math.BigDecimal importoTotale,
           it.giustizia.www.serviziTelematici.serviziGenerici.DatiSingoloVersamento[] datiSingoloVersamento) {
           this.importoTotale = importoTotale;
           this.datiSingoloVersamento = datiSingoloVersamento;
    }


    /**
     * Gets the importoTotale value for this DatiVersamento.
     * 
     * @return importoTotale
     */
    public java.math.BigDecimal getImportoTotale() {
        return importoTotale;
    }


    /**
     * Sets the importoTotale value for this DatiVersamento.
     * 
     * @param importoTotale
     */
    public void setImportoTotale(java.math.BigDecimal importoTotale) {
        this.importoTotale = importoTotale;
    }


    /**
     * Gets the datiSingoloVersamento value for this DatiVersamento.
     * 
     * @return datiSingoloVersamento
     */
    public it.giustizia.www.serviziTelematici.serviziGenerici.DatiSingoloVersamento[] getDatiSingoloVersamento() {
        return datiSingoloVersamento;
    }


    /**
     * Sets the datiSingoloVersamento value for this DatiVersamento.
     * 
     * @param datiSingoloVersamento
     */
    public void setDatiSingoloVersamento(it.giustizia.www.serviziTelematici.serviziGenerici.DatiSingoloVersamento[] datiSingoloVersamento) {
        this.datiSingoloVersamento = datiSingoloVersamento;
    }

    public it.giustizia.www.serviziTelematici.serviziGenerici.DatiSingoloVersamento getDatiSingoloVersamento(int i) {
        return this.datiSingoloVersamento[i];
    }

    public void setDatiSingoloVersamento(int i, it.giustizia.www.serviziTelematici.serviziGenerici.DatiSingoloVersamento _value) {
        this.datiSingoloVersamento[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatiVersamento)) return false;
        DatiVersamento other = (DatiVersamento) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.importoTotale==null && other.getImportoTotale()==null) || 
             (this.importoTotale!=null &&
              this.importoTotale.equals(other.getImportoTotale()))) &&
            ((this.datiSingoloVersamento==null && other.getDatiSingoloVersamento()==null) || 
             (this.datiSingoloVersamento!=null &&
              java.util.Arrays.equals(this.datiSingoloVersamento, other.getDatiSingoloVersamento())));
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
        if (getImportoTotale() != null) {
            _hashCode += getImportoTotale().hashCode();
        }
        if (getDatiSingoloVersamento() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDatiSingoloVersamento());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDatiSingoloVersamento(), i);
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
        new org.apache.axis.description.TypeDesc(DatiVersamento.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "datiVersamento"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importoTotale");
        elemField.setXmlName(new javax.xml.namespace.QName("", "importoTotale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datiSingoloVersamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "datiSingoloVersamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "datiSingoloVersamento"));
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
