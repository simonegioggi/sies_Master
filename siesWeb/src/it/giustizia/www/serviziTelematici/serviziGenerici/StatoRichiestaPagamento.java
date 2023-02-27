/**
 * StatoRichiestaPagamento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class StatoRichiestaPagamento  implements java.io.Serializable {
    private java.lang.String id;

    private java.lang.String codiceCRS;

    private java.lang.String codiceDistretto;

    private java.lang.String descrizioneDistretto;

    private java.lang.String pagatore;

    private java.lang.String denominazionePagatore;

    private java.lang.String versante;

    private java.lang.String denominazioneVersante;

    private java.util.Calendar dataRichiesta;

    private java.util.Calendar dataRicevuta;

    private java.lang.String stato;

    private float importo;

    private java.lang.String causale;

    private java.lang.String statoNodoPA;

    private java.lang.String tipologia;

    private java.lang.String errore;

    private java.lang.String ruolo;

    private java.lang.String destinazione;

    private java.lang.String descrizioneTipologia;

    private java.lang.String numeroAvviso;

    public StatoRichiestaPagamento() {
    }

    public StatoRichiestaPagamento(
           java.lang.String id,
           java.lang.String codiceCRS,
           java.lang.String codiceDistretto,
           java.lang.String descrizioneDistretto,
           java.lang.String pagatore,
           java.lang.String denominazionePagatore,
           java.lang.String versante,
           java.lang.String denominazioneVersante,
           java.util.Calendar dataRichiesta,
           java.util.Calendar dataRicevuta,
           java.lang.String stato,
           float importo,
           java.lang.String causale,
           java.lang.String statoNodoPA,
           java.lang.String tipologia,
           java.lang.String errore,
           java.lang.String ruolo,
           java.lang.String destinazione,
           java.lang.String descrizioneTipologia,
           java.lang.String numeroAvviso) {
           this.id = id;
           this.codiceCRS = codiceCRS;
           this.codiceDistretto = codiceDistretto;
           this.descrizioneDistretto = descrizioneDistretto;
           this.pagatore = pagatore;
           this.denominazionePagatore = denominazionePagatore;
           this.versante = versante;
           this.denominazioneVersante = denominazioneVersante;
           this.dataRichiesta = dataRichiesta;
           this.dataRicevuta = dataRicevuta;
           this.stato = stato;
           this.importo = importo;
           this.causale = causale;
           this.statoNodoPA = statoNodoPA;
           this.tipologia = tipologia;
           this.errore = errore;
           this.ruolo = ruolo;
           this.destinazione = destinazione;
           this.descrizioneTipologia = descrizioneTipologia;
           this.numeroAvviso = numeroAvviso;
    }


    /**
     * Gets the id value for this StatoRichiestaPagamento.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this StatoRichiestaPagamento.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the codiceCRS value for this StatoRichiestaPagamento.
     * 
     * @return codiceCRS
     */
    public java.lang.String getCodiceCRS() {
        return codiceCRS;
    }


    /**
     * Sets the codiceCRS value for this StatoRichiestaPagamento.
     * 
     * @param codiceCRS
     */
    public void setCodiceCRS(java.lang.String codiceCRS) {
        this.codiceCRS = codiceCRS;
    }


    /**
     * Gets the codiceDistretto value for this StatoRichiestaPagamento.
     * 
     * @return codiceDistretto
     */
    public java.lang.String getCodiceDistretto() {
        return codiceDistretto;
    }


    /**
     * Sets the codiceDistretto value for this StatoRichiestaPagamento.
     * 
     * @param codiceDistretto
     */
    public void setCodiceDistretto(java.lang.String codiceDistretto) {
        this.codiceDistretto = codiceDistretto;
    }


    /**
     * Gets the descrizioneDistretto value for this StatoRichiestaPagamento.
     * 
     * @return descrizioneDistretto
     */
    public java.lang.String getDescrizioneDistretto() {
        return descrizioneDistretto;
    }


    /**
     * Sets the descrizioneDistretto value for this StatoRichiestaPagamento.
     * 
     * @param descrizioneDistretto
     */
    public void setDescrizioneDistretto(java.lang.String descrizioneDistretto) {
        this.descrizioneDistretto = descrizioneDistretto;
    }


    /**
     * Gets the pagatore value for this StatoRichiestaPagamento.
     * 
     * @return pagatore
     */
    public java.lang.String getPagatore() {
        return pagatore;
    }


    /**
     * Sets the pagatore value for this StatoRichiestaPagamento.
     * 
     * @param pagatore
     */
    public void setPagatore(java.lang.String pagatore) {
        this.pagatore = pagatore;
    }


    /**
     * Gets the denominazionePagatore value for this StatoRichiestaPagamento.
     * 
     * @return denominazionePagatore
     */
    public java.lang.String getDenominazionePagatore() {
        return denominazionePagatore;
    }


    /**
     * Sets the denominazionePagatore value for this StatoRichiestaPagamento.
     * 
     * @param denominazionePagatore
     */
    public void setDenominazionePagatore(java.lang.String denominazionePagatore) {
        this.denominazionePagatore = denominazionePagatore;
    }


    /**
     * Gets the versante value for this StatoRichiestaPagamento.
     * 
     * @return versante
     */
    public java.lang.String getVersante() {
        return versante;
    }


    /**
     * Sets the versante value for this StatoRichiestaPagamento.
     * 
     * @param versante
     */
    public void setVersante(java.lang.String versante) {
        this.versante = versante;
    }


    /**
     * Gets the denominazioneVersante value for this StatoRichiestaPagamento.
     * 
     * @return denominazioneVersante
     */
    public java.lang.String getDenominazioneVersante() {
        return denominazioneVersante;
    }


    /**
     * Sets the denominazioneVersante value for this StatoRichiestaPagamento.
     * 
     * @param denominazioneVersante
     */
    public void setDenominazioneVersante(java.lang.String denominazioneVersante) {
        this.denominazioneVersante = denominazioneVersante;
    }


    /**
     * Gets the dataRichiesta value for this StatoRichiestaPagamento.
     * 
     * @return dataRichiesta
     */
    public java.util.Calendar getDataRichiesta() {
        return dataRichiesta;
    }


    /**
     * Sets the dataRichiesta value for this StatoRichiestaPagamento.
     * 
     * @param dataRichiesta
     */
    public void setDataRichiesta(java.util.Calendar dataRichiesta) {
        this.dataRichiesta = dataRichiesta;
    }


    /**
     * Gets the dataRicevuta value for this StatoRichiestaPagamento.
     * 
     * @return dataRicevuta
     */
    public java.util.Calendar getDataRicevuta() {
        return dataRicevuta;
    }


    /**
     * Sets the dataRicevuta value for this StatoRichiestaPagamento.
     * 
     * @param dataRicevuta
     */
    public void setDataRicevuta(java.util.Calendar dataRicevuta) {
        this.dataRicevuta = dataRicevuta;
    }


    /**
     * Gets the stato value for this StatoRichiestaPagamento.
     * 
     * @return stato
     */
    public java.lang.String getStato() {
        return stato;
    }


    /**
     * Sets the stato value for this StatoRichiestaPagamento.
     * 
     * @param stato
     */
    public void setStato(java.lang.String stato) {
        this.stato = stato;
    }


    /**
     * Gets the importo value for this StatoRichiestaPagamento.
     * 
     * @return importo
     */
    public float getImporto() {
        return importo;
    }


    /**
     * Sets the importo value for this StatoRichiestaPagamento.
     * 
     * @param importo
     */
    public void setImporto(float importo) {
        this.importo = importo;
    }


    /**
     * Gets the causale value for this StatoRichiestaPagamento.
     * 
     * @return causale
     */
    public java.lang.String getCausale() {
        return causale;
    }


    /**
     * Sets the causale value for this StatoRichiestaPagamento.
     * 
     * @param causale
     */
    public void setCausale(java.lang.String causale) {
        this.causale = causale;
    }


    /**
     * Gets the statoNodoPA value for this StatoRichiestaPagamento.
     * 
     * @return statoNodoPA
     */
    public java.lang.String getStatoNodoPA() {
        return statoNodoPA;
    }


    /**
     * Sets the statoNodoPA value for this StatoRichiestaPagamento.
     * 
     * @param statoNodoPA
     */
    public void setStatoNodoPA(java.lang.String statoNodoPA) {
        this.statoNodoPA = statoNodoPA;
    }


    /**
     * Gets the tipologia value for this StatoRichiestaPagamento.
     * 
     * @return tipologia
     */
    public java.lang.String getTipologia() {
        return tipologia;
    }


    /**
     * Sets the tipologia value for this StatoRichiestaPagamento.
     * 
     * @param tipologia
     */
    public void setTipologia(java.lang.String tipologia) {
        this.tipologia = tipologia;
    }


    /**
     * Gets the errore value for this StatoRichiestaPagamento.
     * 
     * @return errore
     */
    public java.lang.String getErrore() {
        return errore;
    }


    /**
     * Sets the errore value for this StatoRichiestaPagamento.
     * 
     * @param errore
     */
    public void setErrore(java.lang.String errore) {
        this.errore = errore;
    }


    /**
     * Gets the ruolo value for this StatoRichiestaPagamento.
     * 
     * @return ruolo
     */
    public java.lang.String getRuolo() {
        return ruolo;
    }


    /**
     * Sets the ruolo value for this StatoRichiestaPagamento.
     * 
     * @param ruolo
     */
    public void setRuolo(java.lang.String ruolo) {
        this.ruolo = ruolo;
    }


    /**
     * Gets the destinazione value for this StatoRichiestaPagamento.
     * 
     * @return destinazione
     */
    public java.lang.String getDestinazione() {
        return destinazione;
    }


    /**
     * Sets the destinazione value for this StatoRichiestaPagamento.
     * 
     * @param destinazione
     */
    public void setDestinazione(java.lang.String destinazione) {
        this.destinazione = destinazione;
    }


    /**
     * Gets the descrizioneTipologia value for this StatoRichiestaPagamento.
     * 
     * @return descrizioneTipologia
     */
    public java.lang.String getDescrizioneTipologia() {
        return descrizioneTipologia;
    }


    /**
     * Sets the descrizioneTipologia value for this StatoRichiestaPagamento.
     * 
     * @param descrizioneTipologia
     */
    public void setDescrizioneTipologia(java.lang.String descrizioneTipologia) {
        this.descrizioneTipologia = descrizioneTipologia;
    }


    /**
     * Gets the numeroAvviso value for this StatoRichiestaPagamento.
     * 
     * @return numeroAvviso
     */
    public java.lang.String getNumeroAvviso() {
        return numeroAvviso;
    }


    /**
     * Sets the numeroAvviso value for this StatoRichiestaPagamento.
     * 
     * @param numeroAvviso
     */
    public void setNumeroAvviso(java.lang.String numeroAvviso) {
        this.numeroAvviso = numeroAvviso;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StatoRichiestaPagamento)) return false;
        StatoRichiestaPagamento other = (StatoRichiestaPagamento) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.codiceCRS==null && other.getCodiceCRS()==null) || 
             (this.codiceCRS!=null &&
              this.codiceCRS.equals(other.getCodiceCRS()))) &&
            ((this.codiceDistretto==null && other.getCodiceDistretto()==null) || 
             (this.codiceDistretto!=null &&
              this.codiceDistretto.equals(other.getCodiceDistretto()))) &&
            ((this.descrizioneDistretto==null && other.getDescrizioneDistretto()==null) || 
             (this.descrizioneDistretto!=null &&
              this.descrizioneDistretto.equals(other.getDescrizioneDistretto()))) &&
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
            ((this.dataRichiesta==null && other.getDataRichiesta()==null) || 
             (this.dataRichiesta!=null &&
              this.dataRichiesta.equals(other.getDataRichiesta()))) &&
            ((this.dataRicevuta==null && other.getDataRicevuta()==null) || 
             (this.dataRicevuta!=null &&
              this.dataRicevuta.equals(other.getDataRicevuta()))) &&
            ((this.stato==null && other.getStato()==null) || 
             (this.stato!=null &&
              this.stato.equals(other.getStato()))) &&
            this.importo == other.getImporto() &&
            ((this.causale==null && other.getCausale()==null) || 
             (this.causale!=null &&
              this.causale.equals(other.getCausale()))) &&
            ((this.statoNodoPA==null && other.getStatoNodoPA()==null) || 
             (this.statoNodoPA!=null &&
              this.statoNodoPA.equals(other.getStatoNodoPA()))) &&
            ((this.tipologia==null && other.getTipologia()==null) || 
             (this.tipologia!=null &&
              this.tipologia.equals(other.getTipologia()))) &&
            ((this.errore==null && other.getErrore()==null) || 
             (this.errore!=null &&
              this.errore.equals(other.getErrore()))) &&
            ((this.ruolo==null && other.getRuolo()==null) || 
             (this.ruolo!=null &&
              this.ruolo.equals(other.getRuolo()))) &&
            ((this.destinazione==null && other.getDestinazione()==null) || 
             (this.destinazione!=null &&
              this.destinazione.equals(other.getDestinazione()))) &&
            ((this.descrizioneTipologia==null && other.getDescrizioneTipologia()==null) || 
             (this.descrizioneTipologia!=null &&
              this.descrizioneTipologia.equals(other.getDescrizioneTipologia()))) &&
            ((this.numeroAvviso==null && other.getNumeroAvviso()==null) || 
             (this.numeroAvviso!=null &&
              this.numeroAvviso.equals(other.getNumeroAvviso())));
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
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getCodiceCRS() != null) {
            _hashCode += getCodiceCRS().hashCode();
        }
        if (getCodiceDistretto() != null) {
            _hashCode += getCodiceDistretto().hashCode();
        }
        if (getDescrizioneDistretto() != null) {
            _hashCode += getDescrizioneDistretto().hashCode();
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
        if (getDataRichiesta() != null) {
            _hashCode += getDataRichiesta().hashCode();
        }
        if (getDataRicevuta() != null) {
            _hashCode += getDataRicevuta().hashCode();
        }
        if (getStato() != null) {
            _hashCode += getStato().hashCode();
        }
        _hashCode += new Float(getImporto()).hashCode();
        if (getCausale() != null) {
            _hashCode += getCausale().hashCode();
        }
        if (getStatoNodoPA() != null) {
            _hashCode += getStatoNodoPA().hashCode();
        }
        if (getTipologia() != null) {
            _hashCode += getTipologia().hashCode();
        }
        if (getErrore() != null) {
            _hashCode += getErrore().hashCode();
        }
        if (getRuolo() != null) {
            _hashCode += getRuolo().hashCode();
        }
        if (getDestinazione() != null) {
            _hashCode += getDestinazione().hashCode();
        }
        if (getDescrizioneTipologia() != null) {
            _hashCode += getDescrizioneTipologia().hashCode();
        }
        if (getNumeroAvviso() != null) {
            _hashCode += getNumeroAvviso().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(StatoRichiestaPagamento.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "statoRichiestaPagamento"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceCRS");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceCRS"));
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
        elemField.setFieldName("descrizioneDistretto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descrizioneDistretto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("dataRichiesta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataRichiesta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataRicevuta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataRicevuta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
        elemField.setFieldName("importo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "importo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
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
        elemField.setFieldName("statoNodoPA");
        elemField.setXmlName(new javax.xml.namespace.QName("", "statoNodoPA"));
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
        elemField.setFieldName("errore");
        elemField.setXmlName(new javax.xml.namespace.QName("", "errore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("destinazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "destinazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descrizioneTipologia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descrizioneTipologia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroAvviso");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroAvviso"));
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
