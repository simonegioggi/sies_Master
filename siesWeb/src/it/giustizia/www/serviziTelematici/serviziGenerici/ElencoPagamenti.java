/**
 * ElencoPagamenti.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class ElencoPagamenti  implements java.io.Serializable {
    private java.lang.String codiceCRS;

    private java.lang.String tipologia;

    private java.lang.String codiceFiscale;

    private java.lang.String codiceDistretto;

    private java.lang.String causale;

    private java.lang.String stato;

    private java.util.Calendar dataRichiestaDa;

    private java.util.Calendar dataRichiestaA;

    private int dimensionePagina;

    private int numeroPagina;

    private java.util.Calendar dataRicevutaDa;

    private java.util.Calendar dataRicevutaA;

    public ElencoPagamenti() {
    }

    public ElencoPagamenti(
           java.lang.String codiceCRS,
           java.lang.String tipologia,
           java.lang.String codiceFiscale,
           java.lang.String codiceDistretto,
           java.lang.String causale,
           java.lang.String stato,
           java.util.Calendar dataRichiestaDa,
           java.util.Calendar dataRichiestaA,
           int dimensionePagina,
           int numeroPagina,
           java.util.Calendar dataRicevutaDa,
           java.util.Calendar dataRicevutaA) {
           this.codiceCRS = codiceCRS;
           this.tipologia = tipologia;
           this.codiceFiscale = codiceFiscale;
           this.codiceDistretto = codiceDistretto;
           this.causale = causale;
           this.stato = stato;
           this.dataRichiestaDa = dataRichiestaDa;
           this.dataRichiestaA = dataRichiestaA;
           this.dimensionePagina = dimensionePagina;
           this.numeroPagina = numeroPagina;
           this.dataRicevutaDa = dataRicevutaDa;
           this.dataRicevutaA = dataRicevutaA;
    }


    /**
     * Gets the codiceCRS value for this ElencoPagamenti.
     * 
     * @return codiceCRS
     */
    public java.lang.String getCodiceCRS() {
        return codiceCRS;
    }


    /**
     * Sets the codiceCRS value for this ElencoPagamenti.
     * 
     * @param codiceCRS
     */
    public void setCodiceCRS(java.lang.String codiceCRS) {
        this.codiceCRS = codiceCRS;
    }


    /**
     * Gets the tipologia value for this ElencoPagamenti.
     * 
     * @return tipologia
     */
    public java.lang.String getTipologia() {
        return tipologia;
    }


    /**
     * Sets the tipologia value for this ElencoPagamenti.
     * 
     * @param tipologia
     */
    public void setTipologia(java.lang.String tipologia) {
        this.tipologia = tipologia;
    }


    /**
     * Gets the codiceFiscale value for this ElencoPagamenti.
     * 
     * @return codiceFiscale
     */
    public java.lang.String getCodiceFiscale() {
        return codiceFiscale;
    }


    /**
     * Sets the codiceFiscale value for this ElencoPagamenti.
     * 
     * @param codiceFiscale
     */
    public void setCodiceFiscale(java.lang.String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }


    /**
     * Gets the codiceDistretto value for this ElencoPagamenti.
     * 
     * @return codiceDistretto
     */
    public java.lang.String getCodiceDistretto() {
        return codiceDistretto;
    }


    /**
     * Sets the codiceDistretto value for this ElencoPagamenti.
     * 
     * @param codiceDistretto
     */
    public void setCodiceDistretto(java.lang.String codiceDistretto) {
        this.codiceDistretto = codiceDistretto;
    }


    /**
     * Gets the causale value for this ElencoPagamenti.
     * 
     * @return causale
     */
    public java.lang.String getCausale() {
        return causale;
    }


    /**
     * Sets the causale value for this ElencoPagamenti.
     * 
     * @param causale
     */
    public void setCausale(java.lang.String causale) {
        this.causale = causale;
    }


    /**
     * Gets the stato value for this ElencoPagamenti.
     * 
     * @return stato
     */
    public java.lang.String getStato() {
        return stato;
    }


    /**
     * Sets the stato value for this ElencoPagamenti.
     * 
     * @param stato
     */
    public void setStato(java.lang.String stato) {
        this.stato = stato;
    }


    /**
     * Gets the dataRichiestaDa value for this ElencoPagamenti.
     * 
     * @return dataRichiestaDa
     */
    public java.util.Calendar getDataRichiestaDa() {
        return dataRichiestaDa;
    }


    /**
     * Sets the dataRichiestaDa value for this ElencoPagamenti.
     * 
     * @param dataRichiestaDa
     */
    public void setDataRichiestaDa(java.util.Calendar dataRichiestaDa) {
        this.dataRichiestaDa = dataRichiestaDa;
    }


    /**
     * Gets the dataRichiestaA value for this ElencoPagamenti.
     * 
     * @return dataRichiestaA
     */
    public java.util.Calendar getDataRichiestaA() {
        return dataRichiestaA;
    }


    /**
     * Sets the dataRichiestaA value for this ElencoPagamenti.
     * 
     * @param dataRichiestaA
     */
    public void setDataRichiestaA(java.util.Calendar dataRichiestaA) {
        this.dataRichiestaA = dataRichiestaA;
    }


    /**
     * Gets the dimensionePagina value for this ElencoPagamenti.
     * 
     * @return dimensionePagina
     */
    public int getDimensionePagina() {
        return dimensionePagina;
    }


    /**
     * Sets the dimensionePagina value for this ElencoPagamenti.
     * 
     * @param dimensionePagina
     */
    public void setDimensionePagina(int dimensionePagina) {
        this.dimensionePagina = dimensionePagina;
    }


    /**
     * Gets the numeroPagina value for this ElencoPagamenti.
     * 
     * @return numeroPagina
     */
    public int getNumeroPagina() {
        return numeroPagina;
    }


    /**
     * Sets the numeroPagina value for this ElencoPagamenti.
     * 
     * @param numeroPagina
     */
    public void setNumeroPagina(int numeroPagina) {
        this.numeroPagina = numeroPagina;
    }


    /**
     * Gets the dataRicevutaDa value for this ElencoPagamenti.
     * 
     * @return dataRicevutaDa
     */
    public java.util.Calendar getDataRicevutaDa() {
        return dataRicevutaDa;
    }


    /**
     * Sets the dataRicevutaDa value for this ElencoPagamenti.
     * 
     * @param dataRicevutaDa
     */
    public void setDataRicevutaDa(java.util.Calendar dataRicevutaDa) {
        this.dataRicevutaDa = dataRicevutaDa;
    }


    /**
     * Gets the dataRicevutaA value for this ElencoPagamenti.
     * 
     * @return dataRicevutaA
     */
    public java.util.Calendar getDataRicevutaA() {
        return dataRicevutaA;
    }


    /**
     * Sets the dataRicevutaA value for this ElencoPagamenti.
     * 
     * @param dataRicevutaA
     */
    public void setDataRicevutaA(java.util.Calendar dataRicevutaA) {
        this.dataRicevutaA = dataRicevutaA;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ElencoPagamenti)) return false;
        ElencoPagamenti other = (ElencoPagamenti) obj;
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
            ((this.tipologia==null && other.getTipologia()==null) || 
             (this.tipologia!=null &&
              this.tipologia.equals(other.getTipologia()))) &&
            ((this.codiceFiscale==null && other.getCodiceFiscale()==null) || 
             (this.codiceFiscale!=null &&
              this.codiceFiscale.equals(other.getCodiceFiscale()))) &&
            ((this.codiceDistretto==null && other.getCodiceDistretto()==null) || 
             (this.codiceDistretto!=null &&
              this.codiceDistretto.equals(other.getCodiceDistretto()))) &&
            ((this.causale==null && other.getCausale()==null) || 
             (this.causale!=null &&
              this.causale.equals(other.getCausale()))) &&
            ((this.stato==null && other.getStato()==null) || 
             (this.stato!=null &&
              this.stato.equals(other.getStato()))) &&
            ((this.dataRichiestaDa==null && other.getDataRichiestaDa()==null) || 
             (this.dataRichiestaDa!=null &&
              this.dataRichiestaDa.equals(other.getDataRichiestaDa()))) &&
            ((this.dataRichiestaA==null && other.getDataRichiestaA()==null) || 
             (this.dataRichiestaA!=null &&
              this.dataRichiestaA.equals(other.getDataRichiestaA()))) &&
            this.dimensionePagina == other.getDimensionePagina() &&
            this.numeroPagina == other.getNumeroPagina() &&
            ((this.dataRicevutaDa==null && other.getDataRicevutaDa()==null) || 
             (this.dataRicevutaDa!=null &&
              this.dataRicevutaDa.equals(other.getDataRicevutaDa()))) &&
            ((this.dataRicevutaA==null && other.getDataRicevutaA()==null) || 
             (this.dataRicevutaA!=null &&
              this.dataRicevutaA.equals(other.getDataRicevutaA())));
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
        if (getTipologia() != null) {
            _hashCode += getTipologia().hashCode();
        }
        if (getCodiceFiscale() != null) {
            _hashCode += getCodiceFiscale().hashCode();
        }
        if (getCodiceDistretto() != null) {
            _hashCode += getCodiceDistretto().hashCode();
        }
        if (getCausale() != null) {
            _hashCode += getCausale().hashCode();
        }
        if (getStato() != null) {
            _hashCode += getStato().hashCode();
        }
        if (getDataRichiestaDa() != null) {
            _hashCode += getDataRichiestaDa().hashCode();
        }
        if (getDataRichiestaA() != null) {
            _hashCode += getDataRichiestaA().hashCode();
        }
        _hashCode += getDimensionePagina();
        _hashCode += getNumeroPagina();
        if (getDataRicevutaDa() != null) {
            _hashCode += getDataRicevutaDa().hashCode();
        }
        if (getDataRicevutaA() != null) {
            _hashCode += getDataRicevutaA().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ElencoPagamenti.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "elencoPagamenti"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceCRS");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceCRS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipologia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipologia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceFiscale");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceFiscale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("causale");
        elemField.setXmlName(new javax.xml.namespace.QName("", "causale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "stato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataRicevutaDa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataRicevutaDa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataRicevutaA");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataRicevutaA"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
