/**
 * GetAPAinKO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class GetAPAinKO  implements java.io.Serializable {
    private java.util.Calendar dataRichiestaDa;

    private java.util.Calendar dataRichiestaA;

    private java.lang.String codiceDistretto;

    private java.lang.String codiceUfficio;

    private int dimensionePagina;

    private int numeroPagina;

    public GetAPAinKO() {
    }

    public GetAPAinKO(
           java.util.Calendar dataRichiestaDa,
           java.util.Calendar dataRichiestaA,
           java.lang.String codiceDistretto,
           java.lang.String codiceUfficio,
           int dimensionePagina,
           int numeroPagina) {
           this.dataRichiestaDa = dataRichiestaDa;
           this.dataRichiestaA = dataRichiestaA;
           this.codiceDistretto = codiceDistretto;
           this.codiceUfficio = codiceUfficio;
           this.dimensionePagina = dimensionePagina;
           this.numeroPagina = numeroPagina;
    }


    /**
     * Gets the dataRichiestaDa value for this GetAPAinKO.
     * 
     * @return dataRichiestaDa
     */
    public java.util.Calendar getDataRichiestaDa() {
        return dataRichiestaDa;
    }


    /**
     * Sets the dataRichiestaDa value for this GetAPAinKO.
     * 
     * @param dataRichiestaDa
     */
    public void setDataRichiestaDa(java.util.Calendar dataRichiestaDa) {
        this.dataRichiestaDa = dataRichiestaDa;
    }


    /**
     * Gets the dataRichiestaA value for this GetAPAinKO.
     * 
     * @return dataRichiestaA
     */
    public java.util.Calendar getDataRichiestaA() {
        return dataRichiestaA;
    }


    /**
     * Sets the dataRichiestaA value for this GetAPAinKO.
     * 
     * @param dataRichiestaA
     */
    public void setDataRichiestaA(java.util.Calendar dataRichiestaA) {
        this.dataRichiestaA = dataRichiestaA;
    }


    /**
     * Gets the codiceDistretto value for this GetAPAinKO.
     * 
     * @return codiceDistretto
     */
    public java.lang.String getCodiceDistretto() {
        return codiceDistretto;
    }


    /**
     * Sets the codiceDistretto value for this GetAPAinKO.
     * 
     * @param codiceDistretto
     */
    public void setCodiceDistretto(java.lang.String codiceDistretto) {
        this.codiceDistretto = codiceDistretto;
    }


    /**
     * Gets the codiceUfficio value for this GetAPAinKO.
     * 
     * @return codiceUfficio
     */
    public java.lang.String getCodiceUfficio() {
        return codiceUfficio;
    }


    /**
     * Sets the codiceUfficio value for this GetAPAinKO.
     * 
     * @param codiceUfficio
     */
    public void setCodiceUfficio(java.lang.String codiceUfficio) {
        this.codiceUfficio = codiceUfficio;
    }


    /**
     * Gets the dimensionePagina value for this GetAPAinKO.
     * 
     * @return dimensionePagina
     */
    public int getDimensionePagina() {
        return dimensionePagina;
    }


    /**
     * Sets the dimensionePagina value for this GetAPAinKO.
     * 
     * @param dimensionePagina
     */
    public void setDimensionePagina(int dimensionePagina) {
        this.dimensionePagina = dimensionePagina;
    }


    /**
     * Gets the numeroPagina value for this GetAPAinKO.
     * 
     * @return numeroPagina
     */
    public int getNumeroPagina() {
        return numeroPagina;
    }


    /**
     * Sets the numeroPagina value for this GetAPAinKO.
     * 
     * @param numeroPagina
     */
    public void setNumeroPagina(int numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetAPAinKO)) return false;
        GetAPAinKO other = (GetAPAinKO) obj;
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
            ((this.codiceDistretto==null && other.getCodiceDistretto()==null) || 
             (this.codiceDistretto!=null &&
              this.codiceDistretto.equals(other.getCodiceDistretto()))) &&
            ((this.codiceUfficio==null && other.getCodiceUfficio()==null) || 
             (this.codiceUfficio!=null &&
              this.codiceUfficio.equals(other.getCodiceUfficio()))) &&
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
        if (getCodiceDistretto() != null) {
            _hashCode += getCodiceDistretto().hashCode();
        }
        if (getCodiceUfficio() != null) {
            _hashCode += getCodiceUfficio().hashCode();
        }
        _hashCode += getDimensionePagina();
        _hashCode += getNumeroPagina();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetAPAinKO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "getAPAinKO"));
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
        elemField.setFieldName("codiceDistretto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceDistretto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceUfficio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceUfficio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
