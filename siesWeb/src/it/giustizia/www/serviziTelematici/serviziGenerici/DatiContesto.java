/**
 * DatiContesto.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class DatiContesto  implements java.io.Serializable {
    private java.lang.String codiceServizio;

    private java.lang.String datiServizio;

    private java.lang.String dominio;

    private java.lang.String gruppo;

    private java.lang.String stato;

    private java.util.Calendar dataModifica;

    private java.util.Calendar dataInvalidazione;

    private java.lang.String utilizzatore;

    public DatiContesto() {
    }

    public DatiContesto(
           java.lang.String codiceServizio,
           java.lang.String datiServizio,
           java.lang.String dominio,
           java.lang.String gruppo,
           java.lang.String stato,
           java.util.Calendar dataModifica,
           java.util.Calendar dataInvalidazione,
           java.lang.String utilizzatore) {
           this.codiceServizio = codiceServizio;
           this.datiServizio = datiServizio;
           this.dominio = dominio;
           this.gruppo = gruppo;
           this.stato = stato;
           this.dataModifica = dataModifica;
           this.dataInvalidazione = dataInvalidazione;
           this.utilizzatore = utilizzatore;
    }


    /**
     * Gets the codiceServizio value for this DatiContesto.
     * 
     * @return codiceServizio
     */
    public java.lang.String getCodiceServizio() {
        return codiceServizio;
    }


    /**
     * Sets the codiceServizio value for this DatiContesto.
     * 
     * @param codiceServizio
     */
    public void setCodiceServizio(java.lang.String codiceServizio) {
        this.codiceServizio = codiceServizio;
    }


    /**
     * Gets the datiServizio value for this DatiContesto.
     * 
     * @return datiServizio
     */
    public java.lang.String getDatiServizio() {
        return datiServizio;
    }


    /**
     * Sets the datiServizio value for this DatiContesto.
     * 
     * @param datiServizio
     */
    public void setDatiServizio(java.lang.String datiServizio) {
        this.datiServizio = datiServizio;
    }


    /**
     * Gets the dominio value for this DatiContesto.
     * 
     * @return dominio
     */
    public java.lang.String getDominio() {
        return dominio;
    }


    /**
     * Sets the dominio value for this DatiContesto.
     * 
     * @param dominio
     */
    public void setDominio(java.lang.String dominio) {
        this.dominio = dominio;
    }


    /**
     * Gets the gruppo value for this DatiContesto.
     * 
     * @return gruppo
     */
    public java.lang.String getGruppo() {
        return gruppo;
    }


    /**
     * Sets the gruppo value for this DatiContesto.
     * 
     * @param gruppo
     */
    public void setGruppo(java.lang.String gruppo) {
        this.gruppo = gruppo;
    }


    /**
     * Gets the stato value for this DatiContesto.
     * 
     * @return stato
     */
    public java.lang.String getStato() {
        return stato;
    }


    /**
     * Sets the stato value for this DatiContesto.
     * 
     * @param stato
     */
    public void setStato(java.lang.String stato) {
        this.stato = stato;
    }


    /**
     * Gets the dataModifica value for this DatiContesto.
     * 
     * @return dataModifica
     */
    public java.util.Calendar getDataModifica() {
        return dataModifica;
    }


    /**
     * Sets the dataModifica value for this DatiContesto.
     * 
     * @param dataModifica
     */
    public void setDataModifica(java.util.Calendar dataModifica) {
        this.dataModifica = dataModifica;
    }


    /**
     * Gets the dataInvalidazione value for this DatiContesto.
     * 
     * @return dataInvalidazione
     */
    public java.util.Calendar getDataInvalidazione() {
        return dataInvalidazione;
    }


    /**
     * Sets the dataInvalidazione value for this DatiContesto.
     * 
     * @param dataInvalidazione
     */
    public void setDataInvalidazione(java.util.Calendar dataInvalidazione) {
        this.dataInvalidazione = dataInvalidazione;
    }


    /**
     * Gets the utilizzatore value for this DatiContesto.
     * 
     * @return utilizzatore
     */
    public java.lang.String getUtilizzatore() {
        return utilizzatore;
    }


    /**
     * Sets the utilizzatore value for this DatiContesto.
     * 
     * @param utilizzatore
     */
    public void setUtilizzatore(java.lang.String utilizzatore) {
        this.utilizzatore = utilizzatore;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatiContesto)) return false;
        DatiContesto other = (DatiContesto) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codiceServizio==null && other.getCodiceServizio()==null) || 
             (this.codiceServizio!=null &&
              this.codiceServizio.equals(other.getCodiceServizio()))) &&
            ((this.datiServizio==null && other.getDatiServizio()==null) || 
             (this.datiServizio!=null &&
              this.datiServizio.equals(other.getDatiServizio()))) &&
            ((this.dominio==null && other.getDominio()==null) || 
             (this.dominio!=null &&
              this.dominio.equals(other.getDominio()))) &&
            ((this.gruppo==null && other.getGruppo()==null) || 
             (this.gruppo!=null &&
              this.gruppo.equals(other.getGruppo()))) &&
            ((this.stato==null && other.getStato()==null) || 
             (this.stato!=null &&
              this.stato.equals(other.getStato()))) &&
            ((this.dataModifica==null && other.getDataModifica()==null) || 
             (this.dataModifica!=null &&
              this.dataModifica.equals(other.getDataModifica()))) &&
            ((this.dataInvalidazione==null && other.getDataInvalidazione()==null) || 
             (this.dataInvalidazione!=null &&
              this.dataInvalidazione.equals(other.getDataInvalidazione()))) &&
            ((this.utilizzatore==null && other.getUtilizzatore()==null) || 
             (this.utilizzatore!=null &&
              this.utilizzatore.equals(other.getUtilizzatore())));
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
        if (getCodiceServizio() != null) {
            _hashCode += getCodiceServizio().hashCode();
        }
        if (getDatiServizio() != null) {
            _hashCode += getDatiServizio().hashCode();
        }
        if (getDominio() != null) {
            _hashCode += getDominio().hashCode();
        }
        if (getGruppo() != null) {
            _hashCode += getGruppo().hashCode();
        }
        if (getStato() != null) {
            _hashCode += getStato().hashCode();
        }
        if (getDataModifica() != null) {
            _hashCode += getDataModifica().hashCode();
        }
        if (getDataInvalidazione() != null) {
            _hashCode += getDataInvalidazione().hashCode();
        }
        if (getUtilizzatore() != null) {
            _hashCode += getUtilizzatore().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatiContesto.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "datiContesto"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceServizio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceServizio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datiServizio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "datiServizio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dominio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dominio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gruppo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gruppo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("dataModifica");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataModifica"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataInvalidazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataInvalidazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("utilizzatore");
        elemField.setXmlName(new javax.xml.namespace.QName("", "utilizzatore"));
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
