/**
 * Pda.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.pstbe.gestione;

public class Pda  extends it.giustizia.www.serviziTelematici.pstbe.gestione.Anagrafica  implements java.io.Serializable {
    private java.lang.String id;

    private java.lang.String certificatoHash;

    private byte[] certificatoSSL;

    private java.lang.String codPagTel;

    private java.lang.String codiceAlbo;

    private java.util.Calendar dataAutorizzazione;

    private java.util.Calendar dataCessazione;

    private java.lang.String descEstesa;

    private java.lang.String descrizione;

    private java.lang.String flagPdaPagTel;

    private it.giustizia.www.serviziTelematici.pstbe.gestione.Fornitori fornitori;

    private java.lang.String legaleRappresentante;

    private java.lang.String linkManOp;

    private java.lang.String nomeCertificatoSSL;

    private byte[] note;

    private java.lang.String sedeLegale;

    private it.giustizia.www.serviziTelematici.pstbe.gestione.TipologiaPda tipologia;

    private java.lang.String url;

    private java.lang.String urlWebService;

    public Pda() {
    }

    public Pda(
           java.lang.String id,
           java.lang.String certificatoHash,
           byte[] certificatoSSL,
           java.lang.String codPagTel,
           java.lang.String codiceAlbo,
           java.util.Calendar dataAutorizzazione,
           java.util.Calendar dataCessazione,
           java.lang.String descEstesa,
           java.lang.String descrizione,
           java.lang.String flagPdaPagTel,
           it.giustizia.www.serviziTelematici.pstbe.gestione.Fornitori fornitori,
           java.lang.String legaleRappresentante,
           java.lang.String linkManOp,
           java.lang.String nomeCertificatoSSL,
           byte[] note,
           java.lang.String sedeLegale,
           it.giustizia.www.serviziTelematici.pstbe.gestione.TipologiaPda tipologia,
           java.lang.String url,
           java.lang.String urlWebService) {
        this.id = id;
        this.certificatoHash = certificatoHash;
        this.certificatoSSL = certificatoSSL;
        this.codPagTel = codPagTel;
        this.codiceAlbo = codiceAlbo;
        this.dataAutorizzazione = dataAutorizzazione;
        this.dataCessazione = dataCessazione;
        this.descEstesa = descEstesa;
        this.descrizione = descrizione;
        this.flagPdaPagTel = flagPdaPagTel;
        this.fornitori = fornitori;
        this.legaleRappresentante = legaleRappresentante;
        this.linkManOp = linkManOp;
        this.nomeCertificatoSSL = nomeCertificatoSSL;
        this.note = note;
        this.sedeLegale = sedeLegale;
        this.tipologia = tipologia;
        this.url = url;
        this.urlWebService = urlWebService;
    }


    /**
     * Gets the id value for this Pda.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this Pda.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the certificatoHash value for this Pda.
     * 
     * @return certificatoHash
     */
    public java.lang.String getCertificatoHash() {
        return certificatoHash;
    }


    /**
     * Sets the certificatoHash value for this Pda.
     * 
     * @param certificatoHash
     */
    public void setCertificatoHash(java.lang.String certificatoHash) {
        this.certificatoHash = certificatoHash;
    }


    /**
     * Gets the certificatoSSL value for this Pda.
     * 
     * @return certificatoSSL
     */
    public byte[] getCertificatoSSL() {
        return certificatoSSL;
    }


    /**
     * Sets the certificatoSSL value for this Pda.
     * 
     * @param certificatoSSL
     */
    public void setCertificatoSSL(byte[] certificatoSSL) {
        this.certificatoSSL = certificatoSSL;
    }


    /**
     * Gets the codPagTel value for this Pda.
     * 
     * @return codPagTel
     */
    public java.lang.String getCodPagTel() {
        return codPagTel;
    }


    /**
     * Sets the codPagTel value for this Pda.
     * 
     * @param codPagTel
     */
    public void setCodPagTel(java.lang.String codPagTel) {
        this.codPagTel = codPagTel;
    }


    /**
     * Gets the codiceAlbo value for this Pda.
     * 
     * @return codiceAlbo
     */
    public java.lang.String getCodiceAlbo() {
        return codiceAlbo;
    }


    /**
     * Sets the codiceAlbo value for this Pda.
     * 
     * @param codiceAlbo
     */
    public void setCodiceAlbo(java.lang.String codiceAlbo) {
        this.codiceAlbo = codiceAlbo;
    }


    /**
     * Gets the dataAutorizzazione value for this Pda.
     * 
     * @return dataAutorizzazione
     */
    public java.util.Calendar getDataAutorizzazione() {
        return dataAutorizzazione;
    }


    /**
     * Sets the dataAutorizzazione value for this Pda.
     * 
     * @param dataAutorizzazione
     */
    public void setDataAutorizzazione(java.util.Calendar dataAutorizzazione) {
        this.dataAutorizzazione = dataAutorizzazione;
    }


    /**
     * Gets the dataCessazione value for this Pda.
     * 
     * @return dataCessazione
     */
    public java.util.Calendar getDataCessazione() {
        return dataCessazione;
    }


    /**
     * Sets the dataCessazione value for this Pda.
     * 
     * @param dataCessazione
     */
    public void setDataCessazione(java.util.Calendar dataCessazione) {
        this.dataCessazione = dataCessazione;
    }


    /**
     * Gets the descEstesa value for this Pda.
     * 
     * @return descEstesa
     */
    public java.lang.String getDescEstesa() {
        return descEstesa;
    }


    /**
     * Sets the descEstesa value for this Pda.
     * 
     * @param descEstesa
     */
    public void setDescEstesa(java.lang.String descEstesa) {
        this.descEstesa = descEstesa;
    }


    /**
     * Gets the descrizione value for this Pda.
     * 
     * @return descrizione
     */
    public java.lang.String getDescrizione() {
        return descrizione;
    }


    /**
     * Sets the descrizione value for this Pda.
     * 
     * @param descrizione
     */
    public void setDescrizione(java.lang.String descrizione) {
        this.descrizione = descrizione;
    }


    /**
     * Gets the flagPdaPagTel value for this Pda.
     * 
     * @return flagPdaPagTel
     */
    public java.lang.String getFlagPdaPagTel() {
        return flagPdaPagTel;
    }


    /**
     * Sets the flagPdaPagTel value for this Pda.
     * 
     * @param flagPdaPagTel
     */
    public void setFlagPdaPagTel(java.lang.String flagPdaPagTel) {
        this.flagPdaPagTel = flagPdaPagTel;
    }


    /**
     * Gets the fornitori value for this Pda.
     * 
     * @return fornitori
     */
    public it.giustizia.www.serviziTelematici.pstbe.gestione.Fornitori getFornitori() {
        return fornitori;
    }


    /**
     * Sets the fornitori value for this Pda.
     * 
     * @param fornitori
     */
    public void setFornitori(it.giustizia.www.serviziTelematici.pstbe.gestione.Fornitori fornitori) {
        this.fornitori = fornitori;
    }


    /**
     * Gets the legaleRappresentante value for this Pda.
     * 
     * @return legaleRappresentante
     */
    public java.lang.String getLegaleRappresentante() {
        return legaleRappresentante;
    }


    /**
     * Sets the legaleRappresentante value for this Pda.
     * 
     * @param legaleRappresentante
     */
    public void setLegaleRappresentante(java.lang.String legaleRappresentante) {
        this.legaleRappresentante = legaleRappresentante;
    }


    /**
     * Gets the linkManOp value for this Pda.
     * 
     * @return linkManOp
     */
    public java.lang.String getLinkManOp() {
        return linkManOp;
    }


    /**
     * Sets the linkManOp value for this Pda.
     * 
     * @param linkManOp
     */
    public void setLinkManOp(java.lang.String linkManOp) {
        this.linkManOp = linkManOp;
    }


    /**
     * Gets the nomeCertificatoSSL value for this Pda.
     * 
     * @return nomeCertificatoSSL
     */
    public java.lang.String getNomeCertificatoSSL() {
        return nomeCertificatoSSL;
    }


    /**
     * Sets the nomeCertificatoSSL value for this Pda.
     * 
     * @param nomeCertificatoSSL
     */
    public void setNomeCertificatoSSL(java.lang.String nomeCertificatoSSL) {
        this.nomeCertificatoSSL = nomeCertificatoSSL;
    }


    /**
     * Gets the note value for this Pda.
     * 
     * @return note
     */
    public byte[] getNote() {
        return note;
    }


    /**
     * Sets the note value for this Pda.
     * 
     * @param note
     */
    public void setNote(byte[] note) {
        this.note = note;
    }


    /**
     * Gets the sedeLegale value for this Pda.
     * 
     * @return sedeLegale
     */
    public java.lang.String getSedeLegale() {
        return sedeLegale;
    }


    /**
     * Sets the sedeLegale value for this Pda.
     * 
     * @param sedeLegale
     */
    public void setSedeLegale(java.lang.String sedeLegale) {
        this.sedeLegale = sedeLegale;
    }


    /**
     * Gets the tipologia value for this Pda.
     * 
     * @return tipologia
     */
    public it.giustizia.www.serviziTelematici.pstbe.gestione.TipologiaPda getTipologia() {
        return tipologia;
    }


    /**
     * Sets the tipologia value for this Pda.
     * 
     * @param tipologia
     */
    public void setTipologia(it.giustizia.www.serviziTelematici.pstbe.gestione.TipologiaPda tipologia) {
        this.tipologia = tipologia;
    }


    /**
     * Gets the url value for this Pda.
     * 
     * @return url
     */
    public java.lang.String getUrl() {
        return url;
    }


    /**
     * Sets the url value for this Pda.
     * 
     * @param url
     */
    public void setUrl(java.lang.String url) {
        this.url = url;
    }


    /**
     * Gets the urlWebService value for this Pda.
     * 
     * @return urlWebService
     */
    public java.lang.String getUrlWebService() {
        return urlWebService;
    }


    /**
     * Sets the urlWebService value for this Pda.
     * 
     * @param urlWebService
     */
    public void setUrlWebService(java.lang.String urlWebService) {
        this.urlWebService = urlWebService;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Pda)) return false;
        Pda other = (Pda) obj;
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
            ((this.certificatoHash==null && other.getCertificatoHash()==null) || 
             (this.certificatoHash!=null &&
              this.certificatoHash.equals(other.getCertificatoHash()))) &&
            ((this.certificatoSSL==null && other.getCertificatoSSL()==null) || 
             (this.certificatoSSL!=null &&
              java.util.Arrays.equals(this.certificatoSSL, other.getCertificatoSSL()))) &&
            ((this.codPagTel==null && other.getCodPagTel()==null) || 
             (this.codPagTel!=null &&
              this.codPagTel.equals(other.getCodPagTel()))) &&
            ((this.codiceAlbo==null && other.getCodiceAlbo()==null) || 
             (this.codiceAlbo!=null &&
              this.codiceAlbo.equals(other.getCodiceAlbo()))) &&
            ((this.dataAutorizzazione==null && other.getDataAutorizzazione()==null) || 
             (this.dataAutorizzazione!=null &&
              this.dataAutorizzazione.equals(other.getDataAutorizzazione()))) &&
            ((this.dataCessazione==null && other.getDataCessazione()==null) || 
             (this.dataCessazione!=null &&
              this.dataCessazione.equals(other.getDataCessazione()))) &&
            ((this.descEstesa==null && other.getDescEstesa()==null) || 
             (this.descEstesa!=null &&
              this.descEstesa.equals(other.getDescEstesa()))) &&
            ((this.descrizione==null && other.getDescrizione()==null) || 
             (this.descrizione!=null &&
              this.descrizione.equals(other.getDescrizione()))) &&
            ((this.flagPdaPagTel==null && other.getFlagPdaPagTel()==null) || 
             (this.flagPdaPagTel!=null &&
              this.flagPdaPagTel.equals(other.getFlagPdaPagTel()))) &&
            ((this.fornitori==null && other.getFornitori()==null) || 
             (this.fornitori!=null &&
              this.fornitori.equals(other.getFornitori()))) &&
            ((this.legaleRappresentante==null && other.getLegaleRappresentante()==null) || 
             (this.legaleRappresentante!=null &&
              this.legaleRappresentante.equals(other.getLegaleRappresentante()))) &&
            ((this.linkManOp==null && other.getLinkManOp()==null) || 
             (this.linkManOp!=null &&
              this.linkManOp.equals(other.getLinkManOp()))) &&
            ((this.nomeCertificatoSSL==null && other.getNomeCertificatoSSL()==null) || 
             (this.nomeCertificatoSSL!=null &&
              this.nomeCertificatoSSL.equals(other.getNomeCertificatoSSL()))) &&
            ((this.note==null && other.getNote()==null) || 
             (this.note!=null &&
              java.util.Arrays.equals(this.note, other.getNote()))) &&
            ((this.sedeLegale==null && other.getSedeLegale()==null) || 
             (this.sedeLegale!=null &&
              this.sedeLegale.equals(other.getSedeLegale()))) &&
            ((this.tipologia==null && other.getTipologia()==null) || 
             (this.tipologia!=null &&
              this.tipologia.equals(other.getTipologia()))) &&
            ((this.url==null && other.getUrl()==null) || 
             (this.url!=null &&
              this.url.equals(other.getUrl()))) &&
            ((this.urlWebService==null && other.getUrlWebService()==null) || 
             (this.urlWebService!=null &&
              this.urlWebService.equals(other.getUrlWebService())));
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
        if (getCertificatoHash() != null) {
            _hashCode += getCertificatoHash().hashCode();
        }
        if (getCertificatoSSL() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCertificatoSSL());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCertificatoSSL(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCodPagTel() != null) {
            _hashCode += getCodPagTel().hashCode();
        }
        if (getCodiceAlbo() != null) {
            _hashCode += getCodiceAlbo().hashCode();
        }
        if (getDataAutorizzazione() != null) {
            _hashCode += getDataAutorizzazione().hashCode();
        }
        if (getDataCessazione() != null) {
            _hashCode += getDataCessazione().hashCode();
        }
        if (getDescEstesa() != null) {
            _hashCode += getDescEstesa().hashCode();
        }
        if (getDescrizione() != null) {
            _hashCode += getDescrizione().hashCode();
        }
        if (getFlagPdaPagTel() != null) {
            _hashCode += getFlagPdaPagTel().hashCode();
        }
        if (getFornitori() != null) {
            _hashCode += getFornitori().hashCode();
        }
        if (getLegaleRappresentante() != null) {
            _hashCode += getLegaleRappresentante().hashCode();
        }
        if (getLinkManOp() != null) {
            _hashCode += getLinkManOp().hashCode();
        }
        if (getNomeCertificatoSSL() != null) {
            _hashCode += getNomeCertificatoSSL().hashCode();
        }
        if (getNote() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getNote());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getNote(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSedeLegale() != null) {
            _hashCode += getSedeLegale().hashCode();
        }
        if (getTipologia() != null) {
            _hashCode += getTipologia().hashCode();
        }
        if (getUrl() != null) {
            _hashCode += getUrl().hashCode();
        }
        if (getUrlWebService() != null) {
            _hashCode += getUrlWebService().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Pda.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "pda"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("certificatoHash");
        elemField.setXmlName(new javax.xml.namespace.QName("", "certificatoHash"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("certificatoSSL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "certificatoSSL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codPagTel");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codPagTel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceAlbo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceAlbo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataAutorizzazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataAutorizzazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataCessazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataCessazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descEstesa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descEstesa"));
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
        elemField.setFieldName("flagPdaPagTel");
        elemField.setXmlName(new javax.xml.namespace.QName("", "flagPdaPagTel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fornitori");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fornitori"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "fornitori"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("legaleRappresentante");
        elemField.setXmlName(new javax.xml.namespace.QName("", "legaleRappresentante"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("linkManOp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "linkManOp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomeCertificatoSSL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nomeCertificatoSSL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("note");
        elemField.setXmlName(new javax.xml.namespace.QName("", "note"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sedeLegale");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sedeLegale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipologia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipologia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "tipologiaPda"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("url");
        elemField.setXmlName(new javax.xml.namespace.QName("", "url"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("urlWebService");
        elemField.setXmlName(new javax.xml.namespace.QName("", "urlWebService"));
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
