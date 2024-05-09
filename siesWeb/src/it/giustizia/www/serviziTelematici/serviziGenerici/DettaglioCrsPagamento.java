/**
 * DettaglioCrsPagamento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class DettaglioCrsPagamento  implements java.io.Serializable {
    private java.lang.String codice;

    private java.lang.String[] flussiRendicontazione;

    private java.lang.String idUnivocoRiscossione;

    private float importo;

    public DettaglioCrsPagamento() {
    }

    public DettaglioCrsPagamento(
           java.lang.String codice,
           java.lang.String[] flussiRendicontazione,
           java.lang.String idUnivocoRiscossione,
           float importo) {
           this.codice = codice;
           this.flussiRendicontazione = flussiRendicontazione;
           this.idUnivocoRiscossione = idUnivocoRiscossione;
           this.importo = importo;
    }


    /**
     * Gets the codice value for this DettaglioCrsPagamento.
     * 
     * @return codice
     */
    public java.lang.String getCodice() {
        return codice;
    }


    /**
     * Sets the codice value for this DettaglioCrsPagamento.
     * 
     * @param codice
     */
    public void setCodice(java.lang.String codice) {
        this.codice = codice;
    }


    /**
     * Gets the flussiRendicontazione value for this DettaglioCrsPagamento.
     * 
     * @return flussiRendicontazione
     */
    public java.lang.String[] getFlussiRendicontazione() {
        return flussiRendicontazione;
    }


    /**
     * Sets the flussiRendicontazione value for this DettaglioCrsPagamento.
     * 
     * @param flussiRendicontazione
     */
    public void setFlussiRendicontazione(java.lang.String[] flussiRendicontazione) {
        this.flussiRendicontazione = flussiRendicontazione;
    }


    /**
     * Gets the idUnivocoRiscossione value for this DettaglioCrsPagamento.
     * 
     * @return idUnivocoRiscossione
     */
    public java.lang.String getIdUnivocoRiscossione() {
        return idUnivocoRiscossione;
    }


    /**
     * Sets the idUnivocoRiscossione value for this DettaglioCrsPagamento.
     * 
     * @param idUnivocoRiscossione
     */
    public void setIdUnivocoRiscossione(java.lang.String idUnivocoRiscossione) {
        this.idUnivocoRiscossione = idUnivocoRiscossione;
    }


    /**
     * Gets the importo value for this DettaglioCrsPagamento.
     * 
     * @return importo
     */
    public float getImporto() {
        return importo;
    }


    /**
     * Sets the importo value for this DettaglioCrsPagamento.
     * 
     * @param importo
     */
    public void setImporto(float importo) {
        this.importo = importo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DettaglioCrsPagamento)) return false;
        DettaglioCrsPagamento other = (DettaglioCrsPagamento) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codice==null && other.getCodice()==null) || 
             (this.codice!=null &&
              this.codice.equals(other.getCodice()))) &&
            ((this.flussiRendicontazione==null && other.getFlussiRendicontazione()==null) || 
             (this.flussiRendicontazione!=null &&
              java.util.Arrays.equals(this.flussiRendicontazione, other.getFlussiRendicontazione()))) &&
            ((this.idUnivocoRiscossione==null && other.getIdUnivocoRiscossione()==null) || 
             (this.idUnivocoRiscossione!=null &&
              this.idUnivocoRiscossione.equals(other.getIdUnivocoRiscossione()))) &&
            this.importo == other.getImporto();
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
        if (getCodice() != null) {
            _hashCode += getCodice().hashCode();
        }
        if (getFlussiRendicontazione() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFlussiRendicontazione());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFlussiRendicontazione(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIdUnivocoRiscossione() != null) {
            _hashCode += getIdUnivocoRiscossione().hashCode();
        }
        _hashCode += new Float(getImporto()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DettaglioCrsPagamento.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "DettaglioCrsPagamento"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codice");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("flussiRendicontazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "flussiRendicontazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "idFlusso"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idUnivocoRiscossione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idUnivocoRiscossione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "importo"));
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
