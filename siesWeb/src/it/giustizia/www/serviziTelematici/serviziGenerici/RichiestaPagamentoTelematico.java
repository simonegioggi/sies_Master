/**
 * RichiestaPagamentoTelematico.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class RichiestaPagamentoTelematico  implements java.io.Serializable {
    private java.lang.String codiceDistretto;

    private java.lang.String codiceUfficio;

    private java.lang.String autenticazioneSoggetto;

    private it.giustizia.www.serviziTelematici.serviziGenerici.AnagraficaSoggetto soggettoPagatore;

    private it.giustizia.www.serviziTelematici.serviziGenerici.DatiVersamento datiVersamento;

    private java.util.Calendar dataScadenza;

    public RichiestaPagamentoTelematico() {
    }

    public RichiestaPagamentoTelematico(
           java.lang.String codiceDistretto,
           java.lang.String codiceUfficio,
           java.lang.String autenticazioneSoggetto,
           it.giustizia.www.serviziTelematici.serviziGenerici.AnagraficaSoggetto soggettoPagatore,
           it.giustizia.www.serviziTelematici.serviziGenerici.DatiVersamento datiVersamento,
           java.util.Calendar dataScadenza) {
           this.codiceDistretto = codiceDistretto;
           this.codiceUfficio = codiceUfficio;
           this.autenticazioneSoggetto = autenticazioneSoggetto;
           this.soggettoPagatore = soggettoPagatore;
           this.datiVersamento = datiVersamento;
           this.dataScadenza = dataScadenza;
    }


    /**
     * Gets the codiceDistretto value for this RichiestaPagamentoTelematico.
     * 
     * @return codiceDistretto
     */
    public java.lang.String getCodiceDistretto() {
        return codiceDistretto;
    }


    /**
     * Sets the codiceDistretto value for this RichiestaPagamentoTelematico.
     * 
     * @param codiceDistretto
     */
    public void setCodiceDistretto(java.lang.String codiceDistretto) {
        this.codiceDistretto = codiceDistretto;
    }


    /**
     * Gets the codiceUfficio value for this RichiestaPagamentoTelematico.
     * 
     * @return codiceUfficio
     */
    public java.lang.String getCodiceUfficio() {
        return codiceUfficio;
    }


    /**
     * Sets the codiceUfficio value for this RichiestaPagamentoTelematico.
     * 
     * @param codiceUfficio
     */
    public void setCodiceUfficio(java.lang.String codiceUfficio) {
        this.codiceUfficio = codiceUfficio;
    }


    /**
     * Gets the autenticazioneSoggetto value for this RichiestaPagamentoTelematico.
     * 
     * @return autenticazioneSoggetto
     */
    public java.lang.String getAutenticazioneSoggetto() {
        return autenticazioneSoggetto;
    }


    /**
     * Sets the autenticazioneSoggetto value for this RichiestaPagamentoTelematico.
     * 
     * @param autenticazioneSoggetto
     */
    public void setAutenticazioneSoggetto(java.lang.String autenticazioneSoggetto) {
        this.autenticazioneSoggetto = autenticazioneSoggetto;
    }


    /**
     * Gets the soggettoPagatore value for this RichiestaPagamentoTelematico.
     * 
     * @return soggettoPagatore
     */
    public it.giustizia.www.serviziTelematici.serviziGenerici.AnagraficaSoggetto getSoggettoPagatore() {
        return soggettoPagatore;
    }


    /**
     * Sets the soggettoPagatore value for this RichiestaPagamentoTelematico.
     * 
     * @param soggettoPagatore
     */
    public void setSoggettoPagatore(it.giustizia.www.serviziTelematici.serviziGenerici.AnagraficaSoggetto soggettoPagatore) {
        this.soggettoPagatore = soggettoPagatore;
    }


    /**
     * Gets the datiVersamento value for this RichiestaPagamentoTelematico.
     * 
     * @return datiVersamento
     */
    public it.giustizia.www.serviziTelematici.serviziGenerici.DatiVersamento getDatiVersamento() {
        return datiVersamento;
    }


    /**
     * Sets the datiVersamento value for this RichiestaPagamentoTelematico.
     * 
     * @param datiVersamento
     */
    public void setDatiVersamento(it.giustizia.www.serviziTelematici.serviziGenerici.DatiVersamento datiVersamento) {
        this.datiVersamento = datiVersamento;
    }


    /**
     * Gets the dataScadenza value for this RichiestaPagamentoTelematico.
     * 
     * @return dataScadenza
     */
    public java.util.Calendar getDataScadenza() {
        return dataScadenza;
    }


    /**
     * Sets the dataScadenza value for this RichiestaPagamentoTelematico.
     * 
     * @param dataScadenza
     */
    public void setDataScadenza(java.util.Calendar dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RichiestaPagamentoTelematico)) return false;
        RichiestaPagamentoTelematico other = (RichiestaPagamentoTelematico) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codiceDistretto==null && other.getCodiceDistretto()==null) || 
             (this.codiceDistretto!=null &&
              this.codiceDistretto.equals(other.getCodiceDistretto()))) &&
            ((this.codiceUfficio==null && other.getCodiceUfficio()==null) || 
             (this.codiceUfficio!=null &&
              this.codiceUfficio.equals(other.getCodiceUfficio()))) &&
            ((this.autenticazioneSoggetto==null && other.getAutenticazioneSoggetto()==null) || 
             (this.autenticazioneSoggetto!=null &&
              this.autenticazioneSoggetto.equals(other.getAutenticazioneSoggetto()))) &&
            ((this.soggettoPagatore==null && other.getSoggettoPagatore()==null) || 
             (this.soggettoPagatore!=null &&
              this.soggettoPagatore.equals(other.getSoggettoPagatore()))) &&
            ((this.datiVersamento==null && other.getDatiVersamento()==null) || 
             (this.datiVersamento!=null &&
              this.datiVersamento.equals(other.getDatiVersamento()))) &&
            ((this.dataScadenza==null && other.getDataScadenza()==null) || 
             (this.dataScadenza!=null &&
              this.dataScadenza.equals(other.getDataScadenza())));
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
        if (getCodiceDistretto() != null) {
            _hashCode += getCodiceDistretto().hashCode();
        }
        if (getCodiceUfficio() != null) {
            _hashCode += getCodiceUfficio().hashCode();
        }
        if (getAutenticazioneSoggetto() != null) {
            _hashCode += getAutenticazioneSoggetto().hashCode();
        }
        if (getSoggettoPagatore() != null) {
            _hashCode += getSoggettoPagatore().hashCode();
        }
        if (getDatiVersamento() != null) {
            _hashCode += getDatiVersamento().hashCode();
        }
        if (getDataScadenza() != null) {
            _hashCode += getDataScadenza().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RichiestaPagamentoTelematico.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "richiestaPagamentoTelematico"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
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
        elemField.setFieldName("autenticazioneSoggetto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "autenticazioneSoggetto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("soggettoPagatore");
        elemField.setXmlName(new javax.xml.namespace.QName("", "soggettoPagatore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "anagraficaSoggetto"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datiVersamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "datiVersamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "datiVersamento"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataScadenza");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataScadenza"));
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
