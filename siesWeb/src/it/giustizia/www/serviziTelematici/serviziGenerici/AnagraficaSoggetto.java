/**
 * AnagraficaSoggetto.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class AnagraficaSoggetto  implements java.io.Serializable {
    private java.lang.String naturaGiuridica;

    private java.lang.String codiceIdentificativoUnivoco;

    private java.lang.String nominativo;

    private java.lang.String indirizzo;

    private java.lang.String civico;

    private java.lang.String cap;

    private java.lang.String localita;

    private java.lang.String provincia;

    private java.lang.String regione;

    private java.lang.String nazione;

    private java.lang.String email;

    public AnagraficaSoggetto() {
    }

    public AnagraficaSoggetto(
           java.lang.String naturaGiuridica,
           java.lang.String codiceIdentificativoUnivoco,
           java.lang.String nominativo,
           java.lang.String indirizzo,
           java.lang.String civico,
           java.lang.String cap,
           java.lang.String localita,
           java.lang.String provincia,
           java.lang.String regione,
           java.lang.String nazione,
           java.lang.String email) {
           this.naturaGiuridica = naturaGiuridica;
           this.codiceIdentificativoUnivoco = codiceIdentificativoUnivoco;
           this.nominativo = nominativo;
           this.indirizzo = indirizzo;
           this.civico = civico;
           this.cap = cap;
           this.localita = localita;
           this.provincia = provincia;
           this.regione = regione;
           this.nazione = nazione;
           this.email = email;
    }


    /**
     * Gets the naturaGiuridica value for this AnagraficaSoggetto.
     * 
     * @return naturaGiuridica
     */
    public java.lang.String getNaturaGiuridica() {
        return naturaGiuridica;
    }


    /**
     * Sets the naturaGiuridica value for this AnagraficaSoggetto.
     * 
     * @param naturaGiuridica
     */
    public void setNaturaGiuridica(java.lang.String naturaGiuridica) {
        this.naturaGiuridica = naturaGiuridica;
    }


    /**
     * Gets the codiceIdentificativoUnivoco value for this AnagraficaSoggetto.
     * 
     * @return codiceIdentificativoUnivoco
     */
    public java.lang.String getCodiceIdentificativoUnivoco() {
        return codiceIdentificativoUnivoco;
    }


    /**
     * Sets the codiceIdentificativoUnivoco value for this AnagraficaSoggetto.
     * 
     * @param codiceIdentificativoUnivoco
     */
    public void setCodiceIdentificativoUnivoco(java.lang.String codiceIdentificativoUnivoco) {
        this.codiceIdentificativoUnivoco = codiceIdentificativoUnivoco;
    }


    /**
     * Gets the nominativo value for this AnagraficaSoggetto.
     * 
     * @return nominativo
     */
    public java.lang.String getNominativo() {
        return nominativo;
    }


    /**
     * Sets the nominativo value for this AnagraficaSoggetto.
     * 
     * @param nominativo
     */
    public void setNominativo(java.lang.String nominativo) {
        this.nominativo = nominativo;
    }


    /**
     * Gets the indirizzo value for this AnagraficaSoggetto.
     * 
     * @return indirizzo
     */
    public java.lang.String getIndirizzo() {
        return indirizzo;
    }


    /**
     * Sets the indirizzo value for this AnagraficaSoggetto.
     * 
     * @param indirizzo
     */
    public void setIndirizzo(java.lang.String indirizzo) {
        this.indirizzo = indirizzo;
    }


    /**
     * Gets the civico value for this AnagraficaSoggetto.
     * 
     * @return civico
     */
    public java.lang.String getCivico() {
        return civico;
    }


    /**
     * Sets the civico value for this AnagraficaSoggetto.
     * 
     * @param civico
     */
    public void setCivico(java.lang.String civico) {
        this.civico = civico;
    }


    /**
     * Gets the cap value for this AnagraficaSoggetto.
     * 
     * @return cap
     */
    public java.lang.String getCap() {
        return cap;
    }


    /**
     * Sets the cap value for this AnagraficaSoggetto.
     * 
     * @param cap
     */
    public void setCap(java.lang.String cap) {
        this.cap = cap;
    }


    /**
     * Gets the localita value for this AnagraficaSoggetto.
     * 
     * @return localita
     */
    public java.lang.String getLocalita() {
        return localita;
    }


    /**
     * Sets the localita value for this AnagraficaSoggetto.
     * 
     * @param localita
     */
    public void setLocalita(java.lang.String localita) {
        this.localita = localita;
    }


    /**
     * Gets the provincia value for this AnagraficaSoggetto.
     * 
     * @return provincia
     */
    public java.lang.String getProvincia() {
        return provincia;
    }


    /**
     * Sets the provincia value for this AnagraficaSoggetto.
     * 
     * @param provincia
     */
    public void setProvincia(java.lang.String provincia) {
        this.provincia = provincia;
    }


    /**
     * Gets the regione value for this AnagraficaSoggetto.
     * 
     * @return regione
     */
    public java.lang.String getRegione() {
        return regione;
    }


    /**
     * Sets the regione value for this AnagraficaSoggetto.
     * 
     * @param regione
     */
    public void setRegione(java.lang.String regione) {
        this.regione = regione;
    }


    /**
     * Gets the nazione value for this AnagraficaSoggetto.
     * 
     * @return nazione
     */
    public java.lang.String getNazione() {
        return nazione;
    }


    /**
     * Sets the nazione value for this AnagraficaSoggetto.
     * 
     * @param nazione
     */
    public void setNazione(java.lang.String nazione) {
        this.nazione = nazione;
    }


    /**
     * Gets the email value for this AnagraficaSoggetto.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this AnagraficaSoggetto.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AnagraficaSoggetto)) return false;
        AnagraficaSoggetto other = (AnagraficaSoggetto) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.naturaGiuridica==null && other.getNaturaGiuridica()==null) || 
             (this.naturaGiuridica!=null &&
              this.naturaGiuridica.equals(other.getNaturaGiuridica()))) &&
            ((this.codiceIdentificativoUnivoco==null && other.getCodiceIdentificativoUnivoco()==null) || 
             (this.codiceIdentificativoUnivoco!=null &&
              this.codiceIdentificativoUnivoco.equals(other.getCodiceIdentificativoUnivoco()))) &&
            ((this.nominativo==null && other.getNominativo()==null) || 
             (this.nominativo!=null &&
              this.nominativo.equals(other.getNominativo()))) &&
            ((this.indirizzo==null && other.getIndirizzo()==null) || 
             (this.indirizzo!=null &&
              this.indirizzo.equals(other.getIndirizzo()))) &&
            ((this.civico==null && other.getCivico()==null) || 
             (this.civico!=null &&
              this.civico.equals(other.getCivico()))) &&
            ((this.cap==null && other.getCap()==null) || 
             (this.cap!=null &&
              this.cap.equals(other.getCap()))) &&
            ((this.localita==null && other.getLocalita()==null) || 
             (this.localita!=null &&
              this.localita.equals(other.getLocalita()))) &&
            ((this.provincia==null && other.getProvincia()==null) || 
             (this.provincia!=null &&
              this.provincia.equals(other.getProvincia()))) &&
            ((this.regione==null && other.getRegione()==null) || 
             (this.regione!=null &&
              this.regione.equals(other.getRegione()))) &&
            ((this.nazione==null && other.getNazione()==null) || 
             (this.nazione!=null &&
              this.nazione.equals(other.getNazione()))) &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail())));
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
        if (getNaturaGiuridica() != null) {
            _hashCode += getNaturaGiuridica().hashCode();
        }
        if (getCodiceIdentificativoUnivoco() != null) {
            _hashCode += getCodiceIdentificativoUnivoco().hashCode();
        }
        if (getNominativo() != null) {
            _hashCode += getNominativo().hashCode();
        }
        if (getIndirizzo() != null) {
            _hashCode += getIndirizzo().hashCode();
        }
        if (getCivico() != null) {
            _hashCode += getCivico().hashCode();
        }
        if (getCap() != null) {
            _hashCode += getCap().hashCode();
        }
        if (getLocalita() != null) {
            _hashCode += getLocalita().hashCode();
        }
        if (getProvincia() != null) {
            _hashCode += getProvincia().hashCode();
        }
        if (getRegione() != null) {
            _hashCode += getRegione().hashCode();
        }
        if (getNazione() != null) {
            _hashCode += getNazione().hashCode();
        }
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AnagraficaSoggetto.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "anagraficaSoggetto"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("naturaGiuridica");
        elemField.setXmlName(new javax.xml.namespace.QName("", "naturaGiuridica"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceIdentificativoUnivoco");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceIdentificativoUnivoco"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nominativo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nominativo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indirizzo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "indirizzo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("civico");
        elemField.setXmlName(new javax.xml.namespace.QName("", "civico"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cap");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cap"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("localita");
        elemField.setXmlName(new javax.xml.namespace.QName("", "localita"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("provincia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "provincia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("regione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "regione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("", "email"));
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
