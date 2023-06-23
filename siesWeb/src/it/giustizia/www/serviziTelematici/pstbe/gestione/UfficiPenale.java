/**
 * UfficiPenale.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.pstbe.gestione;

public class UfficiPenale  extends it.giustizia.www.serviziTelematici.pstbe.gestione.Anagrafica  implements java.io.Serializable {
    private java.lang.String id;

    private it.giustizia.www.serviziTelematici.pstbe.gestione.AttiDepositabiliUfficio[] attiDepositabiliUfficio;

    private byte[] certificatoCifra;

    private java.lang.String citta;

    private java.lang.String descrizione;

    private it.giustizia.www.serviziTelematici.pstbe.gestione.GestoreLocale gestoreLocale;

    private java.lang.String indirizzoPec;

    private java.lang.String nomeCertificatoCifra;

    private java.lang.String provincia;

    private it.giustizia.www.serviziTelematici.pstbe.gestione.Regioni regione;

    private it.giustizia.www.serviziTelematici.pstbe.gestione.TipiAttoDepositabile tipiAttoDepositabile;

    private it.giustizia.www.serviziTelematici.pstbe.gestione.TipiUfficio tipoUfficio;

    public UfficiPenale() {
    }

    public UfficiPenale(
           java.lang.String id,
           it.giustizia.www.serviziTelematici.pstbe.gestione.AttiDepositabiliUfficio[] attiDepositabiliUfficio,
           byte[] certificatoCifra,
           java.lang.String citta,
           java.lang.String descrizione,
           it.giustizia.www.serviziTelematici.pstbe.gestione.GestoreLocale gestoreLocale,
           java.lang.String indirizzoPec,
           java.lang.String nomeCertificatoCifra,
           java.lang.String provincia,
           it.giustizia.www.serviziTelematici.pstbe.gestione.Regioni regione,
           it.giustizia.www.serviziTelematici.pstbe.gestione.TipiAttoDepositabile tipiAttoDepositabile,
           it.giustizia.www.serviziTelematici.pstbe.gestione.TipiUfficio tipoUfficio) {
        this.id = id;
        this.attiDepositabiliUfficio = attiDepositabiliUfficio;
        this.certificatoCifra = certificatoCifra;
        this.citta = citta;
        this.descrizione = descrizione;
        this.gestoreLocale = gestoreLocale;
        this.indirizzoPec = indirizzoPec;
        this.nomeCertificatoCifra = nomeCertificatoCifra;
        this.provincia = provincia;
        this.regione = regione;
        this.tipiAttoDepositabile = tipiAttoDepositabile;
        this.tipoUfficio = tipoUfficio;
    }


    /**
     * Gets the id value for this UfficiPenale.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this UfficiPenale.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the attiDepositabiliUfficio value for this UfficiPenale.
     * 
     * @return attiDepositabiliUfficio
     */
    public it.giustizia.www.serviziTelematici.pstbe.gestione.AttiDepositabiliUfficio[] getAttiDepositabiliUfficio() {
        return attiDepositabiliUfficio;
    }


    /**
     * Sets the attiDepositabiliUfficio value for this UfficiPenale.
     * 
     * @param attiDepositabiliUfficio
     */
    public void setAttiDepositabiliUfficio(it.giustizia.www.serviziTelematici.pstbe.gestione.AttiDepositabiliUfficio[] attiDepositabiliUfficio) {
        this.attiDepositabiliUfficio = attiDepositabiliUfficio;
    }

    public it.giustizia.www.serviziTelematici.pstbe.gestione.AttiDepositabiliUfficio getAttiDepositabiliUfficio(int i) {
        return this.attiDepositabiliUfficio[i];
    }

    public void setAttiDepositabiliUfficio(int i, it.giustizia.www.serviziTelematici.pstbe.gestione.AttiDepositabiliUfficio _value) {
        this.attiDepositabiliUfficio[i] = _value;
    }


    /**
     * Gets the certificatoCifra value for this UfficiPenale.
     * 
     * @return certificatoCifra
     */
    public byte[] getCertificatoCifra() {
        return certificatoCifra;
    }


    /**
     * Sets the certificatoCifra value for this UfficiPenale.
     * 
     * @param certificatoCifra
     */
    public void setCertificatoCifra(byte[] certificatoCifra) {
        this.certificatoCifra = certificatoCifra;
    }


    /**
     * Gets the citta value for this UfficiPenale.
     * 
     * @return citta
     */
    public java.lang.String getCitta() {
        return citta;
    }


    /**
     * Sets the citta value for this UfficiPenale.
     * 
     * @param citta
     */
    public void setCitta(java.lang.String citta) {
        this.citta = citta;
    }


    /**
     * Gets the descrizione value for this UfficiPenale.
     * 
     * @return descrizione
     */
    public java.lang.String getDescrizione() {
        return descrizione;
    }


    /**
     * Sets the descrizione value for this UfficiPenale.
     * 
     * @param descrizione
     */
    public void setDescrizione(java.lang.String descrizione) {
        this.descrizione = descrizione;
    }


    /**
     * Gets the gestoreLocale value for this UfficiPenale.
     * 
     * @return gestoreLocale
     */
    public it.giustizia.www.serviziTelematici.pstbe.gestione.GestoreLocale getGestoreLocale() {
        return gestoreLocale;
    }


    /**
     * Sets the gestoreLocale value for this UfficiPenale.
     * 
     * @param gestoreLocale
     */
    public void setGestoreLocale(it.giustizia.www.serviziTelematici.pstbe.gestione.GestoreLocale gestoreLocale) {
        this.gestoreLocale = gestoreLocale;
    }


    /**
     * Gets the indirizzoPec value for this UfficiPenale.
     * 
     * @return indirizzoPec
     */
    public java.lang.String getIndirizzoPec() {
        return indirizzoPec;
    }


    /**
     * Sets the indirizzoPec value for this UfficiPenale.
     * 
     * @param indirizzoPec
     */
    public void setIndirizzoPec(java.lang.String indirizzoPec) {
        this.indirizzoPec = indirizzoPec;
    }


    /**
     * Gets the nomeCertificatoCifra value for this UfficiPenale.
     * 
     * @return nomeCertificatoCifra
     */
    public java.lang.String getNomeCertificatoCifra() {
        return nomeCertificatoCifra;
    }


    /**
     * Sets the nomeCertificatoCifra value for this UfficiPenale.
     * 
     * @param nomeCertificatoCifra
     */
    public void setNomeCertificatoCifra(java.lang.String nomeCertificatoCifra) {
        this.nomeCertificatoCifra = nomeCertificatoCifra;
    }


    /**
     * Gets the provincia value for this UfficiPenale.
     * 
     * @return provincia
     */
    public java.lang.String getProvincia() {
        return provincia;
    }


    /**
     * Sets the provincia value for this UfficiPenale.
     * 
     * @param provincia
     */
    public void setProvincia(java.lang.String provincia) {
        this.provincia = provincia;
    }


    /**
     * Gets the regione value for this UfficiPenale.
     * 
     * @return regione
     */
    public it.giustizia.www.serviziTelematici.pstbe.gestione.Regioni getRegione() {
        return regione;
    }


    /**
     * Sets the regione value for this UfficiPenale.
     * 
     * @param regione
     */
    public void setRegione(it.giustizia.www.serviziTelematici.pstbe.gestione.Regioni regione) {
        this.regione = regione;
    }


    /**
     * Gets the tipiAttoDepositabile value for this UfficiPenale.
     * 
     * @return tipiAttoDepositabile
     */
    public it.giustizia.www.serviziTelematici.pstbe.gestione.TipiAttoDepositabile getTipiAttoDepositabile() {
        return tipiAttoDepositabile;
    }


    /**
     * Sets the tipiAttoDepositabile value for this UfficiPenale.
     * 
     * @param tipiAttoDepositabile
     */
    public void setTipiAttoDepositabile(it.giustizia.www.serviziTelematici.pstbe.gestione.TipiAttoDepositabile tipiAttoDepositabile) {
        this.tipiAttoDepositabile = tipiAttoDepositabile;
    }


    /**
     * Gets the tipoUfficio value for this UfficiPenale.
     * 
     * @return tipoUfficio
     */
    public it.giustizia.www.serviziTelematici.pstbe.gestione.TipiUfficio getTipoUfficio() {
        return tipoUfficio;
    }


    /**
     * Sets the tipoUfficio value for this UfficiPenale.
     * 
     * @param tipoUfficio
     */
    public void setTipoUfficio(it.giustizia.www.serviziTelematici.pstbe.gestione.TipiUfficio tipoUfficio) {
        this.tipoUfficio = tipoUfficio;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UfficiPenale)) return false;
        UfficiPenale other = (UfficiPenale) obj;
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
            ((this.attiDepositabiliUfficio==null && other.getAttiDepositabiliUfficio()==null) || 
             (this.attiDepositabiliUfficio!=null &&
              java.util.Arrays.equals(this.attiDepositabiliUfficio, other.getAttiDepositabiliUfficio()))) &&
            ((this.certificatoCifra==null && other.getCertificatoCifra()==null) || 
             (this.certificatoCifra!=null &&
              java.util.Arrays.equals(this.certificatoCifra, other.getCertificatoCifra()))) &&
            ((this.citta==null && other.getCitta()==null) || 
             (this.citta!=null &&
              this.citta.equals(other.getCitta()))) &&
            ((this.descrizione==null && other.getDescrizione()==null) || 
             (this.descrizione!=null &&
              this.descrizione.equals(other.getDescrizione()))) &&
            ((this.gestoreLocale==null && other.getGestoreLocale()==null) || 
             (this.gestoreLocale!=null &&
              this.gestoreLocale.equals(other.getGestoreLocale()))) &&
            ((this.indirizzoPec==null && other.getIndirizzoPec()==null) || 
             (this.indirizzoPec!=null &&
              this.indirizzoPec.equals(other.getIndirizzoPec()))) &&
            ((this.nomeCertificatoCifra==null && other.getNomeCertificatoCifra()==null) || 
             (this.nomeCertificatoCifra!=null &&
              this.nomeCertificatoCifra.equals(other.getNomeCertificatoCifra()))) &&
            ((this.provincia==null && other.getProvincia()==null) || 
             (this.provincia!=null &&
              this.provincia.equals(other.getProvincia()))) &&
            ((this.regione==null && other.getRegione()==null) || 
             (this.regione!=null &&
              this.regione.equals(other.getRegione()))) &&
            ((this.tipiAttoDepositabile==null && other.getTipiAttoDepositabile()==null) || 
             (this.tipiAttoDepositabile!=null &&
              this.tipiAttoDepositabile.equals(other.getTipiAttoDepositabile()))) &&
            ((this.tipoUfficio==null && other.getTipoUfficio()==null) || 
             (this.tipoUfficio!=null &&
              this.tipoUfficio.equals(other.getTipoUfficio())));
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
        if (getAttiDepositabiliUfficio() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAttiDepositabiliUfficio());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAttiDepositabiliUfficio(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCertificatoCifra() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCertificatoCifra());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCertificatoCifra(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCitta() != null) {
            _hashCode += getCitta().hashCode();
        }
        if (getDescrizione() != null) {
            _hashCode += getDescrizione().hashCode();
        }
        if (getGestoreLocale() != null) {
            _hashCode += getGestoreLocale().hashCode();
        }
        if (getIndirizzoPec() != null) {
            _hashCode += getIndirizzoPec().hashCode();
        }
        if (getNomeCertificatoCifra() != null) {
            _hashCode += getNomeCertificatoCifra().hashCode();
        }
        if (getProvincia() != null) {
            _hashCode += getProvincia().hashCode();
        }
        if (getRegione() != null) {
            _hashCode += getRegione().hashCode();
        }
        if (getTipiAttoDepositabile() != null) {
            _hashCode += getTipiAttoDepositabile().hashCode();
        }
        if (getTipoUfficio() != null) {
            _hashCode += getTipoUfficio().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UfficiPenale.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "ufficiPenale"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attiDepositabiliUfficio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attiDepositabiliUfficio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "attiDepositabiliUfficio"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("certificatoCifra");
        elemField.setXmlName(new javax.xml.namespace.QName("", "certificatoCifra"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("citta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "citta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descrizione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descrizione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gestoreLocale");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gestoreLocale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "gestoreLocale"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indirizzoPec");
        elemField.setXmlName(new javax.xml.namespace.QName("", "indirizzoPec"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomeCertificatoCifra");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nomeCertificatoCifra"));
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
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "regioni"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipiAttoDepositabile");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipiAttoDepositabile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "tipiAttoDepositabile"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoUfficio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoUfficio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "tipiUfficio"));
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
