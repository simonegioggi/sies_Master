/**
 * GetPagamentoByDate.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class GetPagamentoByDate  implements java.io.Serializable {
    private java.util.Calendar dataRichiestaDa;

    private java.util.Calendar dataRichiestaA;

    private boolean senzaRicevuta;

    private int dimensionePagina;

    private int numeroPagina;

    public GetPagamentoByDate() {
    }

    public GetPagamentoByDate(
           java.util.Calendar dataRichiestaDa,
           java.util.Calendar dataRichiestaA,
           boolean senzaRicevuta,
           int dimensionePagina,
           int numeroPagina) {
           this.dataRichiestaDa = dataRichiestaDa;
           this.dataRichiestaA = dataRichiestaA;
           this.senzaRicevuta = senzaRicevuta;
           this.dimensionePagina = dimensionePagina;
           this.numeroPagina = numeroPagina;
    }


    /**
     * Gets the dataRichiestaDa value for this GetPagamentoByDate.
     * 
     * @return dataRichiestaDa
     */
    public java.util.Calendar getDataRichiestaDa() {
        return dataRichiestaDa;
    }


    /**
     * Sets the dataRichiestaDa value for this GetPagamentoByDate.
     * 
     * @param dataRichiestaDa
     */
    public void setDataRichiestaDa(java.util.Calendar dataRichiestaDa) {
        this.dataRichiestaDa = dataRichiestaDa;
    }


    /**
     * Gets the dataRichiestaA value for this GetPagamentoByDate.
     * 
     * @return dataRichiestaA
     */
    public java.util.Calendar getDataRichiestaA() {
        return dataRichiestaA;
    }


    /**
     * Sets the dataRichiestaA value for this GetPagamentoByDate.
     * 
     * @param dataRichiestaA
     */
    public void setDataRichiestaA(java.util.Calendar dataRichiestaA) {
        this.dataRichiestaA = dataRichiestaA;
    }


    /**
     * Gets the senzaRicevuta value for this GetPagamentoByDate.
     * 
     * @return senzaRicevuta
     */
    public boolean isSenzaRicevuta() {
        return senzaRicevuta;
    }


    /**
     * Sets the senzaRicevuta value for this GetPagamentoByDate.
     * 
     * @param senzaRicevuta
     */
    public void setSenzaRicevuta(boolean senzaRicevuta) {
        this.senzaRicevuta = senzaRicevuta;
    }


    /**
     * Gets the dimensionePagina value for this GetPagamentoByDate.
     * 
     * @return dimensionePagina
     */
    public int getDimensionePagina() {
        return dimensionePagina;
    }


    /**
     * Sets the dimensionePagina value for this GetPagamentoByDate.
     * 
     * @param dimensionePagina
     */
    public void setDimensionePagina(int dimensionePagina) {
        this.dimensionePagina = dimensionePagina;
    }


    /**
     * Gets the numeroPagina value for this GetPagamentoByDate.
     * 
     * @return numeroPagina
     */
    public int getNumeroPagina() {
        return numeroPagina;
    }


    /**
     * Sets the numeroPagina value for this GetPagamentoByDate.
     * 
     * @param numeroPagina
     */
    public void setNumeroPagina(int numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetPagamentoByDate)) return false;
        GetPagamentoByDate other = (GetPagamentoByDate) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dataRichiestaDa==null && other.getDataRichiestaDa()==null) || 
             (this.dataRichiestaDa!=null &&
              this.dataRichiestaDa.equals(other.getDataRichiestaDa()))) &&
            ((this.dataRichiestaA==null && other.getDataRichiestaA()==null) || 
             (this.dataRichiestaA!=null &&
              this.dataRichiestaA.equals(other.getDataRichiestaA()))) &&
            this.senzaRicevuta == other.isSenzaRicevuta() &&
            this.dimensionePagina == other.getDimensionePagina() &&
            this.numeroPagina == other.getNumeroPagina();
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
        if (getDataRichiestaDa() != null) {
            _hashCode += getDataRichiestaDa().hashCode();
        }
        if (getDataRichiestaA() != null) {
            _hashCode += getDataRichiestaA().hashCode();
        }
        _hashCode += (isSenzaRicevuta() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getDimensionePagina();
        _hashCode += getNumeroPagina();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetPagamentoByDate.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "getPagamentoByDate"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataRichiestaDa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataRichiestaDa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataRichiestaA");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataRichiestaA"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("senzaRicevuta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "senzaRicevuta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dimensionePagina");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dimensionePagina"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroPagina");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroPagina"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
