/**
 * FlussoRendicontazione.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class FlussoRendicontazione  implements java.io.Serializable {
    private java.lang.String idRendicontazione;

    private java.lang.String identificativoPsp;

    private java.lang.String idFlusso;

    private java.util.Calendar dataOraFlusso;

    private boolean avvenutoScarico;

    private java.util.Calendar dataScarico;

    private boolean avvenutoControllo;

    private java.util.Calendar dataControllo;

    private byte[] rendicontazione;

    private java.lang.String iuRegolamento;

    private java.lang.String numTotalePagamenti;

    private float importoTotale;

    private byte[] crsErrore;

    public FlussoRendicontazione() {
    }

    public FlussoRendicontazione(
           java.lang.String idRendicontazione,
           java.lang.String identificativoPsp,
           java.lang.String idFlusso,
           java.util.Calendar dataOraFlusso,
           boolean avvenutoScarico,
           java.util.Calendar dataScarico,
           boolean avvenutoControllo,
           java.util.Calendar dataControllo,
           byte[] rendicontazione,
           java.lang.String iuRegolamento,
           java.lang.String numTotalePagamenti,
           float importoTotale,
           byte[] crsErrore) {
           this.idRendicontazione = idRendicontazione;
           this.identificativoPsp = identificativoPsp;
           this.idFlusso = idFlusso;
           this.dataOraFlusso = dataOraFlusso;
           this.avvenutoScarico = avvenutoScarico;
           this.dataScarico = dataScarico;
           this.avvenutoControllo = avvenutoControllo;
           this.dataControllo = dataControllo;
           this.rendicontazione = rendicontazione;
           this.iuRegolamento = iuRegolamento;
           this.numTotalePagamenti = numTotalePagamenti;
           this.importoTotale = importoTotale;
           this.crsErrore = crsErrore;
    }


    /**
     * Gets the idRendicontazione value for this FlussoRendicontazione.
     * 
     * @return idRendicontazione
     */
    public java.lang.String getIdRendicontazione() {
        return idRendicontazione;
    }


    /**
     * Sets the idRendicontazione value for this FlussoRendicontazione.
     * 
     * @param idRendicontazione
     */
    public void setIdRendicontazione(java.lang.String idRendicontazione) {
        this.idRendicontazione = idRendicontazione;
    }


    /**
     * Gets the identificativoPsp value for this FlussoRendicontazione.
     * 
     * @return identificativoPsp
     */
    public java.lang.String getIdentificativoPsp() {
        return identificativoPsp;
    }


    /**
     * Sets the identificativoPsp value for this FlussoRendicontazione.
     * 
     * @param identificativoPsp
     */
    public void setIdentificativoPsp(java.lang.String identificativoPsp) {
        this.identificativoPsp = identificativoPsp;
    }


    /**
     * Gets the idFlusso value for this FlussoRendicontazione.
     * 
     * @return idFlusso
     */
    public java.lang.String getIdFlusso() {
        return idFlusso;
    }


    /**
     * Sets the idFlusso value for this FlussoRendicontazione.
     * 
     * @param idFlusso
     */
    public void setIdFlusso(java.lang.String idFlusso) {
        this.idFlusso = idFlusso;
    }


    /**
     * Gets the dataOraFlusso value for this FlussoRendicontazione.
     * 
     * @return dataOraFlusso
     */
    public java.util.Calendar getDataOraFlusso() {
        return dataOraFlusso;
    }


    /**
     * Sets the dataOraFlusso value for this FlussoRendicontazione.
     * 
     * @param dataOraFlusso
     */
    public void setDataOraFlusso(java.util.Calendar dataOraFlusso) {
        this.dataOraFlusso = dataOraFlusso;
    }


    /**
     * Gets the avvenutoScarico value for this FlussoRendicontazione.
     * 
     * @return avvenutoScarico
     */
    public boolean isAvvenutoScarico() {
        return avvenutoScarico;
    }


    /**
     * Sets the avvenutoScarico value for this FlussoRendicontazione.
     * 
     * @param avvenutoScarico
     */
    public void setAvvenutoScarico(boolean avvenutoScarico) {
        this.avvenutoScarico = avvenutoScarico;
    }


    /**
     * Gets the dataScarico value for this FlussoRendicontazione.
     * 
     * @return dataScarico
     */
    public java.util.Calendar getDataScarico() {
        return dataScarico;
    }


    /**
     * Sets the dataScarico value for this FlussoRendicontazione.
     * 
     * @param dataScarico
     */
    public void setDataScarico(java.util.Calendar dataScarico) {
        this.dataScarico = dataScarico;
    }


    /**
     * Gets the avvenutoControllo value for this FlussoRendicontazione.
     * 
     * @return avvenutoControllo
     */
    public boolean isAvvenutoControllo() {
        return avvenutoControllo;
    }


    /**
     * Sets the avvenutoControllo value for this FlussoRendicontazione.
     * 
     * @param avvenutoControllo
     */
    public void setAvvenutoControllo(boolean avvenutoControllo) {
        this.avvenutoControllo = avvenutoControllo;
    }


    /**
     * Gets the dataControllo value for this FlussoRendicontazione.
     * 
     * @return dataControllo
     */
    public java.util.Calendar getDataControllo() {
        return dataControllo;
    }


    /**
     * Sets the dataControllo value for this FlussoRendicontazione.
     * 
     * @param dataControllo
     */
    public void setDataControllo(java.util.Calendar dataControllo) {
        this.dataControllo = dataControllo;
    }


    /**
     * Gets the rendicontazione value for this FlussoRendicontazione.
     * 
     * @return rendicontazione
     */
    public byte[] getRendicontazione() {
        return rendicontazione;
    }


    /**
     * Sets the rendicontazione value for this FlussoRendicontazione.
     * 
     * @param rendicontazione
     */
    public void setRendicontazione(byte[] rendicontazione) {
        this.rendicontazione = rendicontazione;
    }


    /**
     * Gets the iuRegolamento value for this FlussoRendicontazione.
     * 
     * @return iuRegolamento
     */
    public java.lang.String getIuRegolamento() {
        return iuRegolamento;
    }


    /**
     * Sets the iuRegolamento value for this FlussoRendicontazione.
     * 
     * @param iuRegolamento
     */
    public void setIuRegolamento(java.lang.String iuRegolamento) {
        this.iuRegolamento = iuRegolamento;
    }


    /**
     * Gets the numTotalePagamenti value for this FlussoRendicontazione.
     * 
     * @return numTotalePagamenti
     */
    public java.lang.String getNumTotalePagamenti() {
        return numTotalePagamenti;
    }


    /**
     * Sets the numTotalePagamenti value for this FlussoRendicontazione.
     * 
     * @param numTotalePagamenti
     */
    public void setNumTotalePagamenti(java.lang.String numTotalePagamenti) {
        this.numTotalePagamenti = numTotalePagamenti;
    }


    /**
     * Gets the importoTotale value for this FlussoRendicontazione.
     * 
     * @return importoTotale
     */
    public float getImportoTotale() {
        return importoTotale;
    }


    /**
     * Sets the importoTotale value for this FlussoRendicontazione.
     * 
     * @param importoTotale
     */
    public void setImportoTotale(float importoTotale) {
        this.importoTotale = importoTotale;
    }


    /**
     * Gets the crsErrore value for this FlussoRendicontazione.
     * 
     * @return crsErrore
     */
    public byte[] getCrsErrore() {
        return crsErrore;
    }


    /**
     * Sets the crsErrore value for this FlussoRendicontazione.
     * 
     * @param crsErrore
     */
    public void setCrsErrore(byte[] crsErrore) {
        this.crsErrore = crsErrore;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FlussoRendicontazione)) return false;
        FlussoRendicontazione other = (FlussoRendicontazione) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idRendicontazione==null && other.getIdRendicontazione()==null) || 
             (this.idRendicontazione!=null &&
              this.idRendicontazione.equals(other.getIdRendicontazione()))) &&
            ((this.identificativoPsp==null && other.getIdentificativoPsp()==null) || 
             (this.identificativoPsp!=null &&
              this.identificativoPsp.equals(other.getIdentificativoPsp()))) &&
            ((this.idFlusso==null && other.getIdFlusso()==null) || 
             (this.idFlusso!=null &&
              this.idFlusso.equals(other.getIdFlusso()))) &&
            ((this.dataOraFlusso==null && other.getDataOraFlusso()==null) || 
             (this.dataOraFlusso!=null &&
              this.dataOraFlusso.equals(other.getDataOraFlusso()))) &&
            this.avvenutoScarico == other.isAvvenutoScarico() &&
            ((this.dataScarico==null && other.getDataScarico()==null) || 
             (this.dataScarico!=null &&
              this.dataScarico.equals(other.getDataScarico()))) &&
            this.avvenutoControllo == other.isAvvenutoControllo() &&
            ((this.dataControllo==null && other.getDataControllo()==null) || 
             (this.dataControllo!=null &&
              this.dataControllo.equals(other.getDataControllo()))) &&
            ((this.rendicontazione==null && other.getRendicontazione()==null) || 
             (this.rendicontazione!=null &&
              java.util.Arrays.equals(this.rendicontazione, other.getRendicontazione()))) &&
            ((this.iuRegolamento==null && other.getIuRegolamento()==null) || 
             (this.iuRegolamento!=null &&
              this.iuRegolamento.equals(other.getIuRegolamento()))) &&
            ((this.numTotalePagamenti==null && other.getNumTotalePagamenti()==null) || 
             (this.numTotalePagamenti!=null &&
              this.numTotalePagamenti.equals(other.getNumTotalePagamenti()))) &&
            this.importoTotale == other.getImportoTotale() &&
            ((this.crsErrore==null && other.getCrsErrore()==null) || 
             (this.crsErrore!=null &&
              java.util.Arrays.equals(this.crsErrore, other.getCrsErrore())));
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
        if (getIdRendicontazione() != null) {
            _hashCode += getIdRendicontazione().hashCode();
        }
        if (getIdentificativoPsp() != null) {
            _hashCode += getIdentificativoPsp().hashCode();
        }
        if (getIdFlusso() != null) {
            _hashCode += getIdFlusso().hashCode();
        }
        if (getDataOraFlusso() != null) {
            _hashCode += getDataOraFlusso().hashCode();
        }
        _hashCode += (isAvvenutoScarico() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getDataScarico() != null) {
            _hashCode += getDataScarico().hashCode();
        }
        _hashCode += (isAvvenutoControllo() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getDataControllo() != null) {
            _hashCode += getDataControllo().hashCode();
        }
        if (getRendicontazione() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRendicontazione());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRendicontazione(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIuRegolamento() != null) {
            _hashCode += getIuRegolamento().hashCode();
        }
        if (getNumTotalePagamenti() != null) {
            _hashCode += getNumTotalePagamenti().hashCode();
        }
        _hashCode += new Float(getImportoTotale()).hashCode();
        if (getCrsErrore() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCrsErrore());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCrsErrore(), i);
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
        new org.apache.axis.description.TypeDesc(FlussoRendicontazione.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "flussoRendicontazione"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idRendicontazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idRendicontazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificativoPsp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "identificativoPsp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idFlusso");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idFlusso"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataOraFlusso");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataOraFlusso"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("avvenutoScarico");
        elemField.setXmlName(new javax.xml.namespace.QName("", "avvenutoScarico"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataScarico");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataScarico"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("avvenutoControllo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "avvenutoControllo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataControllo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataControllo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rendicontazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rendicontazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("iuRegolamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "iuRegolamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numTotalePagamenti");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numTotalePagamenti"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importoTotale");
        elemField.setXmlName(new javax.xml.namespace.QName("", "importoTotale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("crsErrore");
        elemField.setXmlName(new javax.xml.namespace.QName("", "crsErrore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
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
