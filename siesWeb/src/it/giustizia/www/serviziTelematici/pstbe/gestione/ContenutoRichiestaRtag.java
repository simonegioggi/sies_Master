/**
 * ContenutoRichiestaRtag.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.pstbe.gestione;

public class ContenutoRichiestaRtag  extends it.giustizia.www.serviziTelematici.pstbe.gestione.Anagrafica  implements java.io.Serializable {
    private java.lang.String id;

    private int annullamento;

    private java.util.Calendar dataRegistrazione;

    private it.giustizia.www.serviziTelematici.serviziGenerici.DateConstraint dataRegistrazioneConstraint;

    private java.lang.String destinazione;

    private java.lang.String erroreServer;

    private java.lang.String hostInvio;

    private java.lang.String hostRicezione;

    private java.lang.String idMessaggio;

    private int progressivo;

    private it.giustizia.www.serviziTelematici.pstbe.gestione.RichiestaRtag richiesta;

    private java.lang.String sorgente;

    private java.lang.String tipologia;

    public ContenutoRichiestaRtag() {
    }

    public ContenutoRichiestaRtag(
           java.lang.String id,
           int annullamento,
           java.util.Calendar dataRegistrazione,
           it.giustizia.www.serviziTelematici.serviziGenerici.DateConstraint dataRegistrazioneConstraint,
           java.lang.String destinazione,
           java.lang.String erroreServer,
           java.lang.String hostInvio,
           java.lang.String hostRicezione,
           java.lang.String idMessaggio,
           int progressivo,
           it.giustizia.www.serviziTelematici.pstbe.gestione.RichiestaRtag richiesta,
           java.lang.String sorgente,
           java.lang.String tipologia) {
        this.id = id;
        this.annullamento = annullamento;
        this.dataRegistrazione = dataRegistrazione;
        this.dataRegistrazioneConstraint = dataRegistrazioneConstraint;
        this.destinazione = destinazione;
        this.erroreServer = erroreServer;
        this.hostInvio = hostInvio;
        this.hostRicezione = hostRicezione;
        this.idMessaggio = idMessaggio;
        this.progressivo = progressivo;
        this.richiesta = richiesta;
        this.sorgente = sorgente;
        this.tipologia = tipologia;
    }


    /**
     * Gets the id value for this ContenutoRichiestaRtag.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this ContenutoRichiestaRtag.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the annullamento value for this ContenutoRichiestaRtag.
     * 
     * @return annullamento
     */
    public int getAnnullamento() {
        return annullamento;
    }


    /**
     * Sets the annullamento value for this ContenutoRichiestaRtag.
     * 
     * @param annullamento
     */
    public void setAnnullamento(int annullamento) {
        this.annullamento = annullamento;
    }


    /**
     * Gets the dataRegistrazione value for this ContenutoRichiestaRtag.
     * 
     * @return dataRegistrazione
     */
    public java.util.Calendar getDataRegistrazione() {
        return dataRegistrazione;
    }


    /**
     * Sets the dataRegistrazione value for this ContenutoRichiestaRtag.
     * 
     * @param dataRegistrazione
     */
    public void setDataRegistrazione(java.util.Calendar dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }


    /**
     * Gets the dataRegistrazioneConstraint value for this ContenutoRichiestaRtag.
     * 
     * @return dataRegistrazioneConstraint
     */
    public it.giustizia.www.serviziTelematici.serviziGenerici.DateConstraint getDataRegistrazioneConstraint() {
        return dataRegistrazioneConstraint;
    }


    /**
     * Sets the dataRegistrazioneConstraint value for this ContenutoRichiestaRtag.
     * 
     * @param dataRegistrazioneConstraint
     */
    public void setDataRegistrazioneConstraint(it.giustizia.www.serviziTelematici.serviziGenerici.DateConstraint dataRegistrazioneConstraint) {
        this.dataRegistrazioneConstraint = dataRegistrazioneConstraint;
    }


    /**
     * Gets the destinazione value for this ContenutoRichiestaRtag.
     * 
     * @return destinazione
     */
    public java.lang.String getDestinazione() {
        return destinazione;
    }


    /**
     * Sets the destinazione value for this ContenutoRichiestaRtag.
     * 
     * @param destinazione
     */
    public void setDestinazione(java.lang.String destinazione) {
        this.destinazione = destinazione;
    }


    /**
     * Gets the erroreServer value for this ContenutoRichiestaRtag.
     * 
     * @return erroreServer
     */
    public java.lang.String getErroreServer() {
        return erroreServer;
    }


    /**
     * Sets the erroreServer value for this ContenutoRichiestaRtag.
     * 
     * @param erroreServer
     */
    public void setErroreServer(java.lang.String erroreServer) {
        this.erroreServer = erroreServer;
    }


    /**
     * Gets the hostInvio value for this ContenutoRichiestaRtag.
     * 
     * @return hostInvio
     */
    public java.lang.String getHostInvio() {
        return hostInvio;
    }


    /**
     * Sets the hostInvio value for this ContenutoRichiestaRtag.
     * 
     * @param hostInvio
     */
    public void setHostInvio(java.lang.String hostInvio) {
        this.hostInvio = hostInvio;
    }


    /**
     * Gets the hostRicezione value for this ContenutoRichiestaRtag.
     * 
     * @return hostRicezione
     */
    public java.lang.String getHostRicezione() {
        return hostRicezione;
    }


    /**
     * Sets the hostRicezione value for this ContenutoRichiestaRtag.
     * 
     * @param hostRicezione
     */
    public void setHostRicezione(java.lang.String hostRicezione) {
        this.hostRicezione = hostRicezione;
    }


    /**
     * Gets the idMessaggio value for this ContenutoRichiestaRtag.
     * 
     * @return idMessaggio
     */
    public java.lang.String getIdMessaggio() {
        return idMessaggio;
    }


    /**
     * Sets the idMessaggio value for this ContenutoRichiestaRtag.
     * 
     * @param idMessaggio
     */
    public void setIdMessaggio(java.lang.String idMessaggio) {
        this.idMessaggio = idMessaggio;
    }


    /**
     * Gets the progressivo value for this ContenutoRichiestaRtag.
     * 
     * @return progressivo
     */
    public int getProgressivo() {
        return progressivo;
    }


    /**
     * Sets the progressivo value for this ContenutoRichiestaRtag.
     * 
     * @param progressivo
     */
    public void setProgressivo(int progressivo) {
        this.progressivo = progressivo;
    }


    /**
     * Gets the richiesta value for this ContenutoRichiestaRtag.
     * 
     * @return richiesta
     */
    public it.giustizia.www.serviziTelematici.pstbe.gestione.RichiestaRtag getRichiesta() {
        return richiesta;
    }


    /**
     * Sets the richiesta value for this ContenutoRichiestaRtag.
     * 
     * @param richiesta
     */
    public void setRichiesta(it.giustizia.www.serviziTelematici.pstbe.gestione.RichiestaRtag richiesta) {
        this.richiesta = richiesta;
    }


    /**
     * Gets the sorgente value for this ContenutoRichiestaRtag.
     * 
     * @return sorgente
     */
    public java.lang.String getSorgente() {
        return sorgente;
    }


    /**
     * Sets the sorgente value for this ContenutoRichiestaRtag.
     * 
     * @param sorgente
     */
    public void setSorgente(java.lang.String sorgente) {
        this.sorgente = sorgente;
    }


    /**
     * Gets the tipologia value for this ContenutoRichiestaRtag.
     * 
     * @return tipologia
     */
    public java.lang.String getTipologia() {
        return tipologia;
    }


    /**
     * Sets the tipologia value for this ContenutoRichiestaRtag.
     * 
     * @param tipologia
     */
    public void setTipologia(java.lang.String tipologia) {
        this.tipologia = tipologia;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ContenutoRichiestaRtag)) return false;
        ContenutoRichiestaRtag other = (ContenutoRichiestaRtag) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            this.annullamento == other.getAnnullamento() &&
            ((this.dataRegistrazione==null && other.getDataRegistrazione()==null) || 
             (this.dataRegistrazione!=null &&
              this.dataRegistrazione.equals(other.getDataRegistrazione()))) &&
            ((this.dataRegistrazioneConstraint==null && other.getDataRegistrazioneConstraint()==null) || 
             (this.dataRegistrazioneConstraint!=null &&
              this.dataRegistrazioneConstraint.equals(other.getDataRegistrazioneConstraint()))) &&
            ((this.destinazione==null && other.getDestinazione()==null) || 
             (this.destinazione!=null &&
              this.destinazione.equals(other.getDestinazione()))) &&
            ((this.erroreServer==null && other.getErroreServer()==null) || 
             (this.erroreServer!=null &&
              this.erroreServer.equals(other.getErroreServer()))) &&
            ((this.hostInvio==null && other.getHostInvio()==null) || 
             (this.hostInvio!=null &&
              this.hostInvio.equals(other.getHostInvio()))) &&
            ((this.hostRicezione==null && other.getHostRicezione()==null) || 
             (this.hostRicezione!=null &&
              this.hostRicezione.equals(other.getHostRicezione()))) &&
            ((this.idMessaggio==null && other.getIdMessaggio()==null) || 
             (this.idMessaggio!=null &&
              this.idMessaggio.equals(other.getIdMessaggio()))) &&
            this.progressivo == other.getProgressivo() &&
            ((this.richiesta==null && other.getRichiesta()==null) || 
             (this.richiesta!=null &&
              this.richiesta.equals(other.getRichiesta()))) &&
            ((this.sorgente==null && other.getSorgente()==null) || 
             (this.sorgente!=null &&
              this.sorgente.equals(other.getSorgente()))) &&
            ((this.tipologia==null && other.getTipologia()==null) || 
             (this.tipologia!=null &&
              this.tipologia.equals(other.getTipologia())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        _hashCode += getAnnullamento();
        if (getDataRegistrazione() != null) {
            _hashCode += getDataRegistrazione().hashCode();
        }
        if (getDataRegistrazioneConstraint() != null) {
            _hashCode += getDataRegistrazioneConstraint().hashCode();
        }
        if (getDestinazione() != null) {
            _hashCode += getDestinazione().hashCode();
        }
        if (getErroreServer() != null) {
            _hashCode += getErroreServer().hashCode();
        }
        if (getHostInvio() != null) {
            _hashCode += getHostInvio().hashCode();
        }
        if (getHostRicezione() != null) {
            _hashCode += getHostRicezione().hashCode();
        }
        if (getIdMessaggio() != null) {
            _hashCode += getIdMessaggio().hashCode();
        }
        _hashCode += getProgressivo();
        if (getRichiesta() != null) {
            _hashCode += getRichiesta().hashCode();
        }
        if (getSorgente() != null) {
            _hashCode += getSorgente().hashCode();
        }
        if (getTipologia() != null) {
            _hashCode += getTipologia().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ContenutoRichiestaRtag.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "contenutoRichiestaRtag"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("annullamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "annullamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataRegistrazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataRegistrazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataRegistrazioneConstraint");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataRegistrazioneConstraint"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "dateConstraint"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "destinazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("erroreServer");
        elemField.setXmlName(new javax.xml.namespace.QName("", "erroreServer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostInvio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "hostInvio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hostRicezione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "hostRicezione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idMessaggio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idMessaggio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("progressivo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "progressivo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("richiesta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "richiesta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "richiestaRtag"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sorgente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sorgente"));
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
