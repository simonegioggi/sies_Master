/**
 * Referente.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.reginde.interrogazioniInt;

public class Referente  implements java.io.Serializable {
    private java.lang.String idReferente;

    private java.lang.String nome;

    private java.lang.String cognome;

    private java.util.Calendar dataInserimento;

    private java.util.Calendar dataUltimaModifica;

    private java.lang.String codiceFiscale;

    public Referente() {
    }

    public Referente(
           java.lang.String idReferente,
           java.lang.String nome,
           java.lang.String cognome,
           java.util.Calendar dataInserimento,
           java.util.Calendar dataUltimaModifica,
           java.lang.String codiceFiscale) {
           this.idReferente = idReferente;
           this.nome = nome;
           this.cognome = cognome;
           this.dataInserimento = dataInserimento;
           this.dataUltimaModifica = dataUltimaModifica;
           this.codiceFiscale = codiceFiscale;
    }


    /**
     * Gets the idReferente value for this Referente.
     * 
     * @return idReferente
     */
    public java.lang.String getIdReferente() {
        return idReferente;
    }


    /**
     * Sets the idReferente value for this Referente.
     * 
     * @param idReferente
     */
    public void setIdReferente(java.lang.String idReferente) {
        this.idReferente = idReferente;
    }


    /**
     * Gets the nome value for this Referente.
     * 
     * @return nome
     */
    public java.lang.String getNome() {
        return nome;
    }


    /**
     * Sets the nome value for this Referente.
     * 
     * @param nome
     */
    public void setNome(java.lang.String nome) {
        this.nome = nome;
    }


    /**
     * Gets the cognome value for this Referente.
     * 
     * @return cognome
     */
    public java.lang.String getCognome() {
        return cognome;
    }


    /**
     * Sets the cognome value for this Referente.
     * 
     * @param cognome
     */
    public void setCognome(java.lang.String cognome) {
        this.cognome = cognome;
    }


    /**
     * Gets the dataInserimento value for this Referente.
     * 
     * @return dataInserimento
     */
    public java.util.Calendar getDataInserimento() {
        return dataInserimento;
    }


    /**
     * Sets the dataInserimento value for this Referente.
     * 
     * @param dataInserimento
     */
    public void setDataInserimento(java.util.Calendar dataInserimento) {
        this.dataInserimento = dataInserimento;
    }


    /**
     * Gets the dataUltimaModifica value for this Referente.
     * 
     * @return dataUltimaModifica
     */
    public java.util.Calendar getDataUltimaModifica() {
        return dataUltimaModifica;
    }


    /**
     * Sets the dataUltimaModifica value for this Referente.
     * 
     * @param dataUltimaModifica
     */
    public void setDataUltimaModifica(java.util.Calendar dataUltimaModifica) {
        this.dataUltimaModifica = dataUltimaModifica;
    }


    /**
     * Gets the codiceFiscale value for this Referente.
     * 
     * @return codiceFiscale
     */
    public java.lang.String getCodiceFiscale() {
        return codiceFiscale;
    }


    /**
     * Sets the codiceFiscale value for this Referente.
     * 
     * @param codiceFiscale
     */
    public void setCodiceFiscale(java.lang.String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Referente)) return false;
        Referente other = (Referente) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idReferente==null && other.getIdReferente()==null) || 
             (this.idReferente!=null &&
              this.idReferente.equals(other.getIdReferente()))) &&
            ((this.nome==null && other.getNome()==null) || 
             (this.nome!=null &&
              this.nome.equals(other.getNome()))) &&
            ((this.cognome==null && other.getCognome()==null) || 
             (this.cognome!=null &&
              this.cognome.equals(other.getCognome()))) &&
            ((this.dataInserimento==null && other.getDataInserimento()==null) || 
             (this.dataInserimento!=null &&
              this.dataInserimento.equals(other.getDataInserimento()))) &&
            ((this.dataUltimaModifica==null && other.getDataUltimaModifica()==null) || 
             (this.dataUltimaModifica!=null &&
              this.dataUltimaModifica.equals(other.getDataUltimaModifica()))) &&
            ((this.codiceFiscale==null && other.getCodiceFiscale()==null) || 
             (this.codiceFiscale!=null &&
              this.codiceFiscale.equals(other.getCodiceFiscale())));
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
        if (getIdReferente() != null) {
            _hashCode += getIdReferente().hashCode();
        }
        if (getNome() != null) {
            _hashCode += getNome().hashCode();
        }
        if (getCognome() != null) {
            _hashCode += getCognome().hashCode();
        }
        if (getDataInserimento() != null) {
            _hashCode += getDataInserimento().hashCode();
        }
        if (getDataUltimaModifica() != null) {
            _hashCode += getDataUltimaModifica().hashCode();
        }
        if (getCodiceFiscale() != null) {
            _hashCode += getCodiceFiscale().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Referente.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "referente"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idReferente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idReferente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nome");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cognome");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cognome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataInserimento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataInserimento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataUltimaModifica");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataUltimaModifica"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
