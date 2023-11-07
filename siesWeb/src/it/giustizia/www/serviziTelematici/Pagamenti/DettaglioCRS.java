/**
 * DettaglioCRS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.Pagamenti;

public class DettaglioCRS  implements java.io.Serializable {
    private java.lang.String CRS;

    private java.util.Calendar dataCreazione;

    private java.lang.String pagatore;

    private java.lang.String denominazionePagatore;

    private java.lang.String versante;

    private java.lang.String denominazioneVersante;

    private java.lang.String causale;

    private float importo;

    private int esitoPagamento;

    private java.lang.String tipoAllegato;

    private byte[] contenutoAllegato;

    private java.lang.String codiceSistemaRT;

    private it.giustizia.www.serviziTelematici.serviziGenerici.DatiContesto contesto;

    private java.lang.String idApa;

    public DettaglioCRS() {
    }

    public DettaglioCRS(
           java.lang.String CRS,
           java.util.Calendar dataCreazione,
           java.lang.String pagatore,
           java.lang.String denominazionePagatore,
           java.lang.String versante,
           java.lang.String denominazioneVersante,
           java.lang.String causale,
           float importo,
           int esitoPagamento,
           java.lang.String tipoAllegato,
           byte[] contenutoAllegato,
           java.lang.String codiceSistemaRT,
           it.giustizia.www.serviziTelematici.serviziGenerici.DatiContesto contesto,
           java.lang.String idApa) {
           this.CRS = CRS;
           this.dataCreazione = dataCreazione;
           this.pagatore = pagatore;
           this.denominazionePagatore = denominazionePagatore;
           this.versante = versante;
           this.denominazioneVersante = denominazioneVersante;
           this.causale = causale;
           this.importo = importo;
           this.esitoPagamento = esitoPagamento;
           this.tipoAllegato = tipoAllegato;
           this.contenutoAllegato = contenutoAllegato;
           this.codiceSistemaRT = codiceSistemaRT;
           this.contesto = contesto;
           this.idApa = idApa;
    }


    /**
     * Gets the CRS value for this DettaglioCRS.
     * 
     * @return CRS
     */
    public java.lang.String getCRS() {
        return CRS;
    }


    /**
     * Sets the CRS value for this DettaglioCRS.
     * 
     * @param CRS
     */
    public void setCRS(java.lang.String CRS) {
        this.CRS = CRS;
    }


    /**
     * Gets the dataCreazione value for this DettaglioCRS.
     * 
     * @return dataCreazione
     */
    public java.util.Calendar getDataCreazione() {
        return dataCreazione;
    }


    /**
     * Sets the dataCreazione value for this DettaglioCRS.
     * 
     * @param dataCreazione
     */
    public void setDataCreazione(java.util.Calendar dataCreazione) {
        this.dataCreazione = dataCreazione;
    }


    /**
     * Gets the pagatore value for this DettaglioCRS.
     * 
     * @return pagatore
     */
    public java.lang.String getPagatore() {
        return pagatore;
    }


    /**
     * Sets the pagatore value for this DettaglioCRS.
     * 
     * @param pagatore
     */
    public void setPagatore(java.lang.String pagatore) {
        this.pagatore = pagatore;
    }


    /**
     * Gets the denominazionePagatore value for this DettaglioCRS.
     * 
     * @return denominazionePagatore
     */
    public java.lang.String getDenominazionePagatore() {
        return denominazionePagatore;
    }


    /**
     * Sets the denominazionePagatore value for this DettaglioCRS.
     * 
     * @param denominazionePagatore
     */
    public void setDenominazionePagatore(java.lang.String denominazionePagatore) {
        this.denominazionePagatore = denominazionePagatore;
    }


    /**
     * Gets the versante value for this DettaglioCRS.
     * 
     * @return versante
     */
    public java.lang.String getVersante() {
        return versante;
    }


    /**
     * Sets the versante value for this DettaglioCRS.
     * 
     * @param versante
     */
    public void setVersante(java.lang.String versante) {
        this.versante = versante;
    }


    /**
     * Gets the denominazioneVersante value for this DettaglioCRS.
     * 
     * @return denominazioneVersante
     */
    public java.lang.String getDenominazioneVersante() {
        return denominazioneVersante;
    }


    /**
     * Sets the denominazioneVersante value for this DettaglioCRS.
     * 
     * @param denominazioneVersante
     */
    public void setDenominazioneVersante(java.lang.String denominazioneVersante) {
        this.denominazioneVersante = denominazioneVersante;
    }


    /**
     * Gets the causale value for this DettaglioCRS.
     * 
     * @return causale
     */
    public java.lang.String getCausale() {
        return causale;
    }


    /**
     * Sets the causale value for this DettaglioCRS.
     * 
     * @param causale
     */
    public void setCausale(java.lang.String causale) {
        this.causale = causale;
    }


    /**
     * Gets the importo value for this DettaglioCRS.
     * 
     * @return importo
     */
    public float getImporto() {
        return importo;
    }


    /**
     * Sets the importo value for this DettaglioCRS.
     * 
     * @param importo
     */
    public void setImporto(float importo) {
        this.importo = importo;
    }


    /**
     * Gets the esitoPagamento value for this DettaglioCRS.
     * 
     * @return esitoPagamento
     */
    public int getEsitoPagamento() {
        return esitoPagamento;
    }


    /**
     * Sets the esitoPagamento value for this DettaglioCRS.
     * 
     * @param esitoPagamento
     */
    public void setEsitoPagamento(int esitoPagamento) {
        this.esitoPagamento = esitoPagamento;
    }


    /**
     * Gets the tipoAllegato value for this DettaglioCRS.
     * 
     * @return tipoAllegato
     */
    public java.lang.String getTipoAllegato() {
        return tipoAllegato;
    }


    /**
     * Sets the tipoAllegato value for this DettaglioCRS.
     * 
     * @param tipoAllegato
     */
    public void setTipoAllegato(java.lang.String tipoAllegato) {
        this.tipoAllegato = tipoAllegato;
    }


    /**
     * Gets the contenutoAllegato value for this DettaglioCRS.
     * 
     * @return contenutoAllegato
     */
    public byte[] getContenutoAllegato() {
        return contenutoAllegato;
    }


    /**
     * Sets the contenutoAllegato value for this DettaglioCRS.
     * 
     * @param contenutoAllegato
     */
    public void setContenutoAllegato(byte[] contenutoAllegato) {
        this.contenutoAllegato = contenutoAllegato;
    }


    /**
     * Gets the codiceSistemaRT value for this DettaglioCRS.
     * 
     * @return codiceSistemaRT
     */
    public java.lang.String getCodiceSistemaRT() {
        return codiceSistemaRT;
    }


    /**
     * Sets the codiceSistemaRT value for this DettaglioCRS.
     * 
     * @param codiceSistemaRT
     */
    public void setCodiceSistemaRT(java.lang.String codiceSistemaRT) {
        this.codiceSistemaRT = codiceSistemaRT;
    }


    /**
     * Gets the contesto value for this DettaglioCRS.
     * 
     * @return contesto
     */
    public it.giustizia.www.serviziTelematici.serviziGenerici.DatiContesto getContesto() {
        return contesto;
    }


    /**
     * Sets the contesto value for this DettaglioCRS.
     * 
     * @param contesto
     */
    public void setContesto(it.giustizia.www.serviziTelematici.serviziGenerici.DatiContesto contesto) {
        this.contesto = contesto;
    }


    /**
     * Gets the idApa value for this DettaglioCRS.
     * 
     * @return idApa
     */
    public java.lang.String getIdApa() {
        return idApa;
    }


    /**
     * Sets the idApa value for this DettaglioCRS.
     * 
     * @param idApa
     */
    public void setIdApa(java.lang.String idApa) {
        this.idApa = idApa;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DettaglioCRS)) return false;
        DettaglioCRS other = (DettaglioCRS) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.CRS==null && other.getCRS()==null) || 
             (this.CRS!=null &&
              this.CRS.equals(other.getCRS()))) &&
            ((this.dataCreazione==null && other.getDataCreazione()==null) || 
             (this.dataCreazione!=null &&
              this.dataCreazione.equals(other.getDataCreazione()))) &&
            ((this.pagatore==null && other.getPagatore()==null) || 
             (this.pagatore!=null &&
              this.pagatore.equals(other.getPagatore()))) &&
            ((this.denominazionePagatore==null && other.getDenominazionePagatore()==null) || 
             (this.denominazionePagatore!=null &&
              this.denominazionePagatore.equals(other.getDenominazionePagatore()))) &&
            ((this.versante==null && other.getVersante()==null) || 
             (this.versante!=null &&
              this.versante.equals(other.getVersante()))) &&
            ((this.denominazioneVersante==null && other.getDenominazioneVersante()==null) || 
             (this.denominazioneVersante!=null &&
              this.denominazioneVersante.equals(other.getDenominazioneVersante()))) &&
            ((this.causale==null && other.getCausale()==null) || 
             (this.causale!=null &&
              this.causale.equals(other.getCausale()))) &&
            this.importo == other.getImporto() &&
            this.esitoPagamento == other.getEsitoPagamento() &&
            ((this.tipoAllegato==null && other.getTipoAllegato()==null) || 
             (this.tipoAllegato!=null &&
              this.tipoAllegato.equals(other.getTipoAllegato()))) &&
            ((this.contenutoAllegato==null && other.getContenutoAllegato()==null) || 
             (this.contenutoAllegato!=null &&
              java.util.Arrays.equals(this.contenutoAllegato, other.getContenutoAllegato()))) &&
            ((this.codiceSistemaRT==null && other.getCodiceSistemaRT()==null) || 
             (this.codiceSistemaRT!=null &&
              this.codiceSistemaRT.equals(other.getCodiceSistemaRT()))) &&
            ((this.contesto==null && other.getContesto()==null) || 
             (this.contesto!=null &&
              this.contesto.equals(other.getContesto()))) &&
            ((this.idApa==null && other.getIdApa()==null) || 
             (this.idApa!=null &&
              this.idApa.equals(other.getIdApa())));
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
        if (getCRS() != null) {
            _hashCode += getCRS().hashCode();
        }
        if (getDataCreazione() != null) {
            _hashCode += getDataCreazione().hashCode();
        }
        if (getPagatore() != null) {
            _hashCode += getPagatore().hashCode();
        }
        if (getDenominazionePagatore() != null) {
            _hashCode += getDenominazionePagatore().hashCode();
        }
        if (getVersante() != null) {
            _hashCode += getVersante().hashCode();
        }
        if (getDenominazioneVersante() != null) {
            _hashCode += getDenominazioneVersante().hashCode();
        }
        if (getCausale() != null) {
            _hashCode += getCausale().hashCode();
        }
        _hashCode += new Float(getImporto()).hashCode();
        _hashCode += getEsitoPagamento();
        if (getTipoAllegato() != null) {
            _hashCode += getTipoAllegato().hashCode();
        }
        if (getContenutoAllegato() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getContenutoAllegato());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getContenutoAllegato(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCodiceSistemaRT() != null) {
            _hashCode += getCodiceSistemaRT().hashCode();
        }
        if (getContesto() != null) {
            _hashCode += getContesto().hashCode();
        }
        if (getIdApa() != null) {
            _hashCode += getIdApa().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DettaglioCRS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/Pagamenti", "dettaglioCRS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CRS");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CRS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataCreazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataCreazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pagatore");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pagatore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("denominazionePagatore");
        elemField.setXmlName(new javax.xml.namespace.QName("", "denominazionePagatore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("versante");
        elemField.setXmlName(new javax.xml.namespace.QName("", "versante"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("denominazioneVersante");
        elemField.setXmlName(new javax.xml.namespace.QName("", "denominazioneVersante"));
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
        elemField.setFieldName("importo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "importo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("esitoPagamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "esitoPagamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoAllegato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoAllegato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contenutoAllegato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contenutoAllegato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceSistemaRT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceSistemaRT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contesto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contesto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "datiContesto"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idApa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idApa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
