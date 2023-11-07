/**
 * DatiSingoloVersamento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class DatiSingoloVersamento  implements java.io.Serializable {
    private java.math.BigDecimal importo;

    private java.lang.String causale;

    private java.lang.String datiSpecificiRiscossione;

    private it.giustizia.www.serviziTelematici.serviziGenerici.DatiMarcaBolloDigitale datiMarcaBolloDigitale;

    public DatiSingoloVersamento() {
    }

    public DatiSingoloVersamento(
           java.math.BigDecimal importo,
           java.lang.String causale,
           java.lang.String datiSpecificiRiscossione,
           it.giustizia.www.serviziTelematici.serviziGenerici.DatiMarcaBolloDigitale datiMarcaBolloDigitale) {
           this.importo = importo;
           this.causale = causale;
           this.datiSpecificiRiscossione = datiSpecificiRiscossione;
           this.datiMarcaBolloDigitale = datiMarcaBolloDigitale;
    }


    /**
     * Gets the importo value for this DatiSingoloVersamento.
     * 
     * @return importo
     */
    public java.math.BigDecimal getImporto() {
        return importo;
    }


    /**
     * Sets the importo value for this DatiSingoloVersamento.
     * 
     * @param importo
     */
    public void setImporto(java.math.BigDecimal importo) {
        this.importo = importo;
    }


    /**
     * Gets the causale value for this DatiSingoloVersamento.
     * 
     * @return causale
     */
    public java.lang.String getCausale() {
        return causale;
    }


    /**
     * Sets the causale value for this DatiSingoloVersamento.
     * 
     * @param causale
     */
    public void setCausale(java.lang.String causale) {
        this.causale = causale;
    }


    /**
     * Gets the datiSpecificiRiscossione value for this DatiSingoloVersamento.
     * 
     * @return datiSpecificiRiscossione
     */
    public java.lang.String getDatiSpecificiRiscossione() {
        return datiSpecificiRiscossione;
    }


    /**
     * Sets the datiSpecificiRiscossione value for this DatiSingoloVersamento.
     * 
     * @param datiSpecificiRiscossione
     */
    public void setDatiSpecificiRiscossione(java.lang.String datiSpecificiRiscossione) {
        this.datiSpecificiRiscossione = datiSpecificiRiscossione;
    }


    /**
     * Gets the datiMarcaBolloDigitale value for this DatiSingoloVersamento.
     * 
     * @return datiMarcaBolloDigitale
     */
    public it.giustizia.www.serviziTelematici.serviziGenerici.DatiMarcaBolloDigitale getDatiMarcaBolloDigitale() {
        return datiMarcaBolloDigitale;
    }


    /**
     * Sets the datiMarcaBolloDigitale value for this DatiSingoloVersamento.
     * 
     * @param datiMarcaBolloDigitale
     */
    public void setDatiMarcaBolloDigitale(it.giustizia.www.serviziTelematici.serviziGenerici.DatiMarcaBolloDigitale datiMarcaBolloDigitale) {
        this.datiMarcaBolloDigitale = datiMarcaBolloDigitale;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatiSingoloVersamento)) return false;
        DatiSingoloVersamento other = (DatiSingoloVersamento) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.importo==null && other.getImporto()==null) || 
             (this.importo!=null &&
              this.importo.equals(other.getImporto()))) &&
            ((this.causale==null && other.getCausale()==null) || 
             (this.causale!=null &&
              this.causale.equals(other.getCausale()))) &&
            ((this.datiSpecificiRiscossione==null && other.getDatiSpecificiRiscossione()==null) || 
             (this.datiSpecificiRiscossione!=null &&
              this.datiSpecificiRiscossione.equals(other.getDatiSpecificiRiscossione()))) &&
            ((this.datiMarcaBolloDigitale==null && other.getDatiMarcaBolloDigitale()==null) || 
             (this.datiMarcaBolloDigitale!=null &&
              this.datiMarcaBolloDigitale.equals(other.getDatiMarcaBolloDigitale())));
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
        if (getImporto() != null) {
            _hashCode += getImporto().hashCode();
        }
        if (getCausale() != null) {
            _hashCode += getCausale().hashCode();
        }
        if (getDatiSpecificiRiscossione() != null) {
            _hashCode += getDatiSpecificiRiscossione().hashCode();
        }
        if (getDatiMarcaBolloDigitale() != null) {
            _hashCode += getDatiMarcaBolloDigitale().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatiSingoloVersamento.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "datiSingoloVersamento"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "importo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("causale");
        elemField.setXmlName(new javax.xml.namespace.QName("", "causale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datiSpecificiRiscossione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "datiSpecificiRiscossione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datiMarcaBolloDigitale");
        elemField.setXmlName(new javax.xml.namespace.QName("", "datiMarcaBolloDigitale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "datiMarcaBolloDigitale"));
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
