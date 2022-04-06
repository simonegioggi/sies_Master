/**
 * Ruoloente.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.reginde.interrogazioniExt;

public class Ruoloente  implements java.io.Serializable {
    private java.lang.String classe;

    private java.lang.String codiceFiscale;

    private java.lang.String codice;

    private java.lang.String descrizione;

    private java.lang.String id;

    private it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.IndAbilitato[] indirizziAbilitati;

    private boolean pubblicaAmministrazione;

    private java.lang.String pec;

    private java.lang.String partitaIVA;

    private it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.Referente referente;

    private java.lang.String ruolo;

    private java.lang.String stato;

    private it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.TipologieEnti tipologia;

    private boolean visibile;

    public Ruoloente() {
    }

    public Ruoloente(
           java.lang.String classe,
           java.lang.String codiceFiscale,
           java.lang.String codice,
           java.lang.String descrizione,
           java.lang.String id,
           it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.IndAbilitato[] indirizziAbilitati,
           boolean pubblicaAmministrazione,
           java.lang.String pec,
           java.lang.String partitaIVA,
           it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.Referente referente,
           java.lang.String ruolo,
           java.lang.String stato,
           it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.TipologieEnti tipologia,
           boolean visibile) {
           this.classe = classe;
           this.codiceFiscale = codiceFiscale;
           this.codice = codice;
           this.descrizione = descrizione;
           this.id = id;
           this.indirizziAbilitati = indirizziAbilitati;
           this.pubblicaAmministrazione = pubblicaAmministrazione;
           this.pec = pec;
           this.partitaIVA = partitaIVA;
           this.referente = referente;
           this.ruolo = ruolo;
           this.stato = stato;
           this.tipologia = tipologia;
           this.visibile = visibile;
    }


    /**
     * Gets the classe value for this Ruoloente.
     * 
     * @return classe
     */
    public java.lang.String getClasse() {
        return classe;
    }


    /**
     * Sets the classe value for this Ruoloente.
     * 
     * @param classe
     */
    public void setClasse(java.lang.String classe) {
        this.classe = classe;
    }


    /**
     * Gets the codiceFiscale value for this Ruoloente.
     * 
     * @return codiceFiscale
     */
    public java.lang.String getCodiceFiscale() {
        return codiceFiscale;
    }


    /**
     * Sets the codiceFiscale value for this Ruoloente.
     * 
     * @param codiceFiscale
     */
    public void setCodiceFiscale(java.lang.String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }


    /**
     * Gets the codice value for this Ruoloente.
     * 
     * @return codice
     */
    public java.lang.String getCodice() {
        return codice;
    }


    /**
     * Sets the codice value for this Ruoloente.
     * 
     * @param codice
     */
    public void setCodice(java.lang.String codice) {
        this.codice = codice;
    }


    /**
     * Gets the descrizione value for this Ruoloente.
     * 
     * @return descrizione
     */
    public java.lang.String getDescrizione() {
        return descrizione;
    }


    /**
     * Sets the descrizione value for this Ruoloente.
     * 
     * @param descrizione
     */
    public void setDescrizione(java.lang.String descrizione) {
        this.descrizione = descrizione;
    }


    /**
     * Gets the id value for this Ruoloente.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this Ruoloente.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the indirizziAbilitati value for this Ruoloente.
     * 
     * @return indirizziAbilitati
     */
    public it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.IndAbilitato[] getIndirizziAbilitati() {
        return indirizziAbilitati;
    }


    /**
     * Sets the indirizziAbilitati value for this Ruoloente.
     * 
     * @param indirizziAbilitati
     */
    public void setIndirizziAbilitati(it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.IndAbilitato[] indirizziAbilitati) {
        this.indirizziAbilitati = indirizziAbilitati;
    }

    public it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.IndAbilitato getIndirizziAbilitati(int i) {
        return this.indirizziAbilitati[i];
    }

    public void setIndirizziAbilitati(int i, it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.IndAbilitato _value) {
        this.indirizziAbilitati[i] = _value;
    }


    /**
     * Gets the pubblicaAmministrazione value for this Ruoloente.
     * 
     * @return pubblicaAmministrazione
     */
    public boolean isPubblicaAmministrazione() {
        return pubblicaAmministrazione;
    }


    /**
     * Sets the pubblicaAmministrazione value for this Ruoloente.
     * 
     * @param pubblicaAmministrazione
     */
    public void setPubblicaAmministrazione(boolean pubblicaAmministrazione) {
        this.pubblicaAmministrazione = pubblicaAmministrazione;
    }


    /**
     * Gets the pec value for this Ruoloente.
     * 
     * @return pec
     */
    public java.lang.String getPec() {
        return pec;
    }


    /**
     * Sets the pec value for this Ruoloente.
     * 
     * @param pec
     */
    public void setPec(java.lang.String pec) {
        this.pec = pec;
    }


    /**
     * Gets the partitaIVA value for this Ruoloente.
     * 
     * @return partitaIVA
     */
    public java.lang.String getPartitaIVA() {
        return partitaIVA;
    }


    /**
     * Sets the partitaIVA value for this Ruoloente.
     * 
     * @param partitaIVA
     */
    public void setPartitaIVA(java.lang.String partitaIVA) {
        this.partitaIVA = partitaIVA;
    }


    /**
     * Gets the referente value for this Ruoloente.
     * 
     * @return referente
     */
    public it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.Referente getReferente() {
        return referente;
    }


    /**
     * Sets the referente value for this Ruoloente.
     * 
     * @param referente
     */
    public void setReferente(it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.Referente referente) {
        this.referente = referente;
    }


    /**
     * Gets the ruolo value for this Ruoloente.
     * 
     * @return ruolo
     */
    public java.lang.String getRuolo() {
        return ruolo;
    }


    /**
     * Sets the ruolo value for this Ruoloente.
     * 
     * @param ruolo
     */
    public void setRuolo(java.lang.String ruolo) {
        this.ruolo = ruolo;
    }


    /**
     * Gets the stato value for this Ruoloente.
     * 
     * @return stato
     */
    public java.lang.String getStato() {
        return stato;
    }


    /**
     * Sets the stato value for this Ruoloente.
     * 
     * @param stato
     */
    public void setStato(java.lang.String stato) {
        this.stato = stato;
    }


    /**
     * Gets the tipologia value for this Ruoloente.
     * 
     * @return tipologia
     */
    public it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.TipologieEnti getTipologia() {
        return tipologia;
    }


    /**
     * Sets the tipologia value for this Ruoloente.
     * 
     * @param tipologia
     */
    public void setTipologia(it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.TipologieEnti tipologia) {
        this.tipologia = tipologia;
    }


    /**
     * Gets the visibile value for this Ruoloente.
     * 
     * @return visibile
     */
    public boolean isVisibile() {
        return visibile;
    }


    /**
     * Sets the visibile value for this Ruoloente.
     * 
     * @param visibile
     */
    public void setVisibile(boolean visibile) {
        this.visibile = visibile;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Ruoloente)) return false;
        Ruoloente other = (Ruoloente) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.classe==null && other.getClasse()==null) || 
             (this.classe!=null &&
              this.classe.equals(other.getClasse()))) &&
            ((this.codiceFiscale==null && other.getCodiceFiscale()==null) || 
             (this.codiceFiscale!=null &&
              this.codiceFiscale.equals(other.getCodiceFiscale()))) &&
            ((this.codice==null && other.getCodice()==null) || 
             (this.codice!=null &&
              this.codice.equals(other.getCodice()))) &&
            ((this.descrizione==null && other.getDescrizione()==null) || 
             (this.descrizione!=null &&
              this.descrizione.equals(other.getDescrizione()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.indirizziAbilitati==null && other.getIndirizziAbilitati()==null) || 
             (this.indirizziAbilitati!=null &&
              java.util.Arrays.equals(this.indirizziAbilitati, other.getIndirizziAbilitati()))) &&
            this.pubblicaAmministrazione == other.isPubblicaAmministrazione() &&
            ((this.pec==null && other.getPec()==null) || 
             (this.pec!=null &&
              this.pec.equals(other.getPec()))) &&
            ((this.partitaIVA==null && other.getPartitaIVA()==null) || 
             (this.partitaIVA!=null &&
              this.partitaIVA.equals(other.getPartitaIVA()))) &&
            ((this.referente==null && other.getReferente()==null) || 
             (this.referente!=null &&
              this.referente.equals(other.getReferente()))) &&
            ((this.ruolo==null && other.getRuolo()==null) || 
             (this.ruolo!=null &&
              this.ruolo.equals(other.getRuolo()))) &&
            ((this.stato==null && other.getStato()==null) || 
             (this.stato!=null &&
              this.stato.equals(other.getStato()))) &&
            ((this.tipologia==null && other.getTipologia()==null) || 
             (this.tipologia!=null &&
              this.tipologia.equals(other.getTipologia()))) &&
            this.visibile == other.isVisibile();
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
        if (getClasse() != null) {
            _hashCode += getClasse().hashCode();
        }
        if (getCodiceFiscale() != null) {
            _hashCode += getCodiceFiscale().hashCode();
        }
        if (getCodice() != null) {
            _hashCode += getCodice().hashCode();
        }
        if (getDescrizione() != null) {
            _hashCode += getDescrizione().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getIndirizziAbilitati() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIndirizziAbilitati());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIndirizziAbilitati(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += (isPubblicaAmministrazione() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getPec() != null) {
            _hashCode += getPec().hashCode();
        }
        if (getPartitaIVA() != null) {
            _hashCode += getPartitaIVA().hashCode();
        }
        if (getReferente() != null) {
            _hashCode += getReferente().hashCode();
        }
        if (getRuolo() != null) {
            _hashCode += getRuolo().hashCode();
        }
        if (getStato() != null) {
            _hashCode += getStato().hashCode();
        }
        if (getTipologia() != null) {
            _hashCode += getTipologia().hashCode();
        }
        _hashCode += (isVisibile() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Ruoloente.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniExt", "ruoloente"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("classe");
        elemField.setXmlName(new javax.xml.namespace.QName("", "classe"));
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
        elemField.setFieldName("codice");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codice"));
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
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indirizziAbilitati");
        elemField.setXmlName(new javax.xml.namespace.QName("", "indirizziAbilitati"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "indAbilitato"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pubblicaAmministrazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pubblicaAmministrazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pec");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pec"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("partitaIVA");
        elemField.setXmlName(new javax.xml.namespace.QName("", "partitaIVA"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "referente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "referente"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ruolo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ruolo"));
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
        elemField.setFieldName("tipologia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipologia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "tipologieEnti"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("visibile");
        elemField.setXmlName(new javax.xml.namespace.QName("", "visibile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
