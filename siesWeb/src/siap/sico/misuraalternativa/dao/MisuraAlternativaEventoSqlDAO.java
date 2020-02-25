package siap.sico.misuraalternativa.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sico.evento.model.EventoModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>Title: MisuraAlternativaSqlDAO</p>
 * <p>Description: Classe SqlDAO che rappresenta la tabella MisuraAlternativa</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class MisuraAlternativaEventoSqlDAO
    extends SqlDAO
{
  public MisuraAlternativaEventoSqlDAO(Connection con)
  {
    super(con);
  }

//
// METODO RICERCA()
//


// ricerca evento decreto sospensione Det Domiciliare
  public void ricercaEventoDecretoSospProvvDetDomByIdFascicolo(BigDecimal aKey) throws DAOException
  {
    String lStatement = getSqlQueryEvento();

    lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
    lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '02'";
    lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";
    lStatement += " AND UFF_EMI.COD_UFFICIO = EVENTO.COD_UFFICIO_EMITTENTE";
    lStatement += " AND UFF_EMI.COD_TIPO_UFFICIO = 'UDS'";
    lStatement += " AND EVENTO.COD_MOTIVO IN ('2149','2150','2151')";
    lStatement += setOrderEventoDesc();

    setStatement(lStatement);
  }

  // ricerca evento decreto sospensione Affidamento in prova
  public void ricercaEventoDecretoSospProvvAffProvByIdFascicolo(BigDecimal aKey) throws DAOException
  {
    String lStatement = getSqlQueryEvento();

    lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
    lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '02'";
    lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";
    lStatement += " AND UFF_EMI.COD_UFFICIO = EVENTO.COD_UFFICIO_EMITTENTE";
    lStatement += " AND UFF_EMI.COD_TIPO_UFFICIO = 'UDS'";
    lStatement += " AND EVENTO.COD_MOTIVO IN ('2145','2146','2147')";
    lStatement += setOrderEventoDesc();

    setStatement(lStatement);
  }

  // ricerca evento decreto sospensione sEMILIBERTA
  public void ricercaEventoDecretoSospProvvSemilByIdFascicolo(BigDecimal aKey) throws DAOException
  {
   String lStatement = getSqlQueryEvento();

   lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
   lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '02'";
   lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";
   lStatement += " AND UFF_EMI.COD_UFFICIO = EVENTO.COD_UFFICIO_EMITTENTE";
   lStatement += " AND UFF_EMI.COD_TIPO_UFFICIO = 'UDS'";
   lStatement += " AND EVENTO.COD_MOTIVO IN ('2148')";
   lStatement += setOrderEventoDesc();

   setStatement(lStatement);
  }

// ricerca evento ordinanza ripristino Affidamento in prova
  public void ricercaEventoOrdinanzaRiprAffProvByIdFascicolo(BigDecimal aKey) throws DAOException
  {
    String lStatement = getSqlQueryEvento();

    lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
    lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '03'";
    lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";
    lStatement += " AND UFF_EMI.COD_UFFICIO = EVENTO.COD_UFFICIO_EMITTENTE";
    lStatement += " AND UFF_EMI.COD_TIPO_UFFICIO = 'TDS'";
    lStatement += " AND EVENTO.COD_MOTIVO IN ('0014','0015','0086')";
    lStatement += setOrderEventoDesc();

    setStatement(lStatement);
  }

// ricerca ordinanza ripristino  Det Domiciliare
  public void ricercaEventoOrdRipriDetDomByIdFascicolo(BigDecimal aKey) throws DAOException
  {
    String lStatement = getSqlQueryEvento();

    lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
    lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '03'";
    lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";
    lStatement += " AND UFF_EMI.COD_UFFICIO = EVENTO.COD_UFFICIO_EMITTENTE";
    lStatement += " AND UFF_EMI.COD_TIPO_UFFICIO = 'TDS'";
    lStatement += " AND EVENTO.COD_MOTIVO IN ('0016','0087','0088','0089')";
    lStatement += setOrderEventoDesc();

    setStatement(lStatement);
  }

  // ricerca ordinanza ripristino sospensione SEMILIBERTA
 public void ricercaEventoOrdRipriSemilByIdFascicolo(BigDecimal aKey) throws DAOException
 {
   String lStatement = getSqlQueryEvento();

   lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
   lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '03'";
   lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";
   lStatement += " AND UFF_EMI.COD_UFFICIO = EVENTO.COD_UFFICIO_EMITTENTE";
   lStatement += " AND UFF_EMI.COD_TIPO_UFFICIO = 'TDS'";
   lStatement += " AND EVENTO.COD_MOTIVO IN ('0091')";
   lStatement += setOrderEventoDesc();

   setStatement(lStatement);
 }


 // ricerca ordinanza Det. Dom. Spe. Ammissione Affidamento
 public void ricercaEventoDetDomSpeAmmAffByIdFascicolo(BigDecimal aKey) throws DAOException
 {
   String lStatement = getSqlQueryEvento();

   lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
   lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '03'";
   lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";
   lStatement += " AND UFF_EMI.COD_UFFICIO = EVENTO.COD_UFFICIO_EMITTENTE";
   lStatement += " AND UFF_EMI.COD_TIPO_UFFICIO = 'TDS'";
   lStatement += " AND EVENTO.COD_MOTIVO IN ('0192')";
   lStatement += setOrderEventoDesc();

   setStatement(lStatement);
 }



  public void ricercaEventoOrdinanzaByIdFascicolo(BigDecimal aKey) throws DAOException
  {
    String lStatement = getSqlQueryEvento();

    lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
    lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '03'";
    lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";
    lStatement += " AND UFF_EMI.COD_UFFICIO = EVENTO.COD_UFFICIO_EMITTENTE";
    lStatement += " AND UFF_EMI.COD_TIPO_UFFICIO = 'TDS'";
    lStatement += " AND EVENTO.COD_MOTIVO IN ('0001','0002','0003','0004','0005','0010','0013')";
    lStatement += setOrderEventoDesc();

    setStatement(lStatement);
  }

  public void ricercaEventoRipristinoByIdFascicolo(BigDecimal aKey) throws DAOException
  {
    String lStatement = getSqlQueryEvento();

    lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
    lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '03'";
    lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";
    lStatement += " AND UFF_EMI.COD_UFFICIO = EVENTO.COD_UFFICIO_EMITTENTE";
    lStatement += " AND UFF_EMI.COD_TIPO_UFFICIO = 'TDS'";
    lStatement += " AND EVENTO.COD_MOTIVO IN ('0123','0124','0125','0126','0127','0128','0129')";
    lStatement += setOrderEventoDesc();

    setStatement(lStatement);
  }


  // ricerca evento Revoca Detenzione Dom
  public void ricercaEventoRevocaDetDomByIdFascicolo(BigDecimal aKey) throws DAOException
  {
    String lStatement = getSqlQueryEvento();
    lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
    lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '03'";
    lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";
    lStatement += " AND UFF_EMI.COD_UFFICIO = EVENTO.COD_UFFICIO_EMITTENTE";
    lStatement += " AND UFF_EMI.COD_TIPO_UFFICIO = 'TDS'";
    lStatement += " AND EVENTO.COD_MOTIVO IN ('0016','0087','0088','0089')";
    lStatement += setOrderEventoDesc();

    setStatement(lStatement);
  }

  // ricerca evento MA
  public void ricercaEventoMAByIdFascicoloTipUffMotProvTipEve(BigDecimal aKey, String aTipoUff,String[] aMotivo,String aTipoProv,String aTipoEve) throws DAOException
  {
    String lStatement = getSqlQueryEvento();
    lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
    lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '"+aTipoProv+"'";
    lStatement += " AND EVENTO.COD_TIPO_EVENTO = '"+aTipoEve+"'";
    lStatement += " AND UFF_EMI.COD_UFFICIO = EVENTO.COD_UFFICIO_EMITTENTE";
    lStatement += " AND UFF_EMI.COD_TIPO_UFFICIO = '"+aTipoUff+"'";

    if(aMotivo.length>0)
        {
          lStatement += " AND EVENTO.COD_MOTIVO IN (";
         for(int i=0;i<aMotivo.length;i++)
         {
           lStatement += "'" +aMotivo[i]+ "'";
           if(aMotivo.length>1 && i<aMotivo.length-1)
            lStatement += ",";

         }
           lStatement += ")";
        }

    lStatement += setOrderEventoDesc();

    setStatement(lStatement);
  }




  public GenericModel getModel() throws DAOException
  {
    EventoModel aModel = new EventoModel();
    aModel.setIdEvento(getBigDecimal("ID_EVENTO"));
    aModel.setCodTipoEvento(getString("COD_TIPO_EVENTO"));
    aModel.setDescrTipoEvento(getString("COD_EVE"));
    aModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
    aModel.setDescrTipoProvvedimento(getString("COD_PRO"));
    aModel.setCodMotivo(getString("COD_MOTIVO"));
    aModel.setDescrMotivo(getString("COD_MOV"));
    aModel.setCodUfficioEmittente(getString("COD_UFFICIO_EMITTENTE"));
    aModel.setDescrUfficioEmittente(getString("DESC_UFF_EMITTENTE"));
    aModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
    aModel.setDescrLuogoEmittente(getString("LUO_EMI"));
    aModel.setCognomeSoggettoPresentante(getString("COGNOME_SOGGETTO_PRESENTANTE"));
    aModel.setNomeSoggettoPresentante(getString("NOME_SOGGETTO_PRESENTANTE"));
    aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
    aModel.setCodEsito(getString("COD_ESITO"));
    aModel.setDescrEsito(getString("COD_ESI"));
    aModel.setFlagPiuMeno(getString("FLAG_PIU_MENO"));
    aModel.setDataTrasmissioneAtti(getDate("DATA_TRASMISSIONE_ATTI"));
    aModel.setDataRicezioneAtti(getDate("DATA_RICEZIONE_ATTI"));
    aModel.setCodUfficioDestinatario(getString("COD_UFFICIO_DESTINATARIO"));
    aModel.setCodLuogoDestinatario(getString("COD_LUOGO_DESTINATARIO"));
    aModel.setDescrLuogoDestinatario(getString("LUO_DES"));
    aModel.setAnnoProtocollo(getBigDecimal("ANNO_PROTOCOLLO"));
    aModel.setProgrProtocollo(getBigDecimal("PROGR_PROTOCOLLO"));
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
    aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
    aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
    aModel.setFlagDocumentoRegistrato(getString("FLAG_DOCUMENTO_REGISTRATO"));
    aModel.setCodMagistrato(getString("COD_MAGISTRATO"));
    aModel.setCodTipoUfficioDestinatario(getString("COD_TIPO_UFFICIO_DESTINATARIO"));
    aModel.setFasSiuIdFascicoloSiusDest(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS_DEST"));
    //GDV
    aModel.setLegge(getString("COD_ABBR"));


// Add 20030713 By paolo
    aModel.setDescrTipoUfficioDestinatario(getString("DESC_UFF_DESTINATARIO"));
    aModel.setTemIdTemplate(getString("TEM_ID_TEMPLATE"));
    aModel.setFlagStampaSiep(getString("FLAG_STAMPA_SIEP"));
    aModel.setFlagStampaSius(getString("FLAG_STAMPA_SIUS"));
    aModel.setFlagVideoSiep(getString("FLAG_VIDEO_SIEP"));
    aModel.setFlagVideoSius(getString("FLAG_VIDEO_SIUS"));

    return aModel;
  }



/*  public boolean eventoCoRiRe(BigDecimal aIdEvento) throws DAOException
  {
    String lStatement = new String();
    lStatement += "select count(*) as COUNT from EVENTO EVE, CG_REF_CODES ESITO, CG_REF_CODES MOTIVO ";
    lStatement += "WHERE EVE.ID_EVENTO = '" + aIdEvento + "'";
    lStatement += " AND (ESITO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO' AND EVE.COD_ESITO = ESITO.RV_LOW_VALUE) ";
    lStatement += " AND (MOTIVO.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND EVE.COD_MOTIVO = MOTIVO.RV_LOW_VALUE  AND MOTIVO.RV_HIGH_VALUE IN ('C001','C002','C003','C004','C008') )";

    setStatement(lStatement);

    this.start();

    BigDecimal lCount = null;

    if (this.next())
      lCount = this.getBigDecimal("COUNT");

    if (lCount.intValue() > 0)
      return true;
    else
      return false;
  }*/



  private String setOrderEventoDesc()
  {
    String lCondizioni = " ORDER BY DATA_EMISSIONE DESC, DATA_INSERIMENTO DESC, ID_EVENTO DESC";

    return lCondizioni;
  }

  protected String getSqlQueryEvento() throws DAOException
  {
    String lStatement = new String("");

    lStatement += "SELECT ID_EVENTO, ";
    lStatement += " COD_TIPO_EVENTO, CODEVE.RV_MEANING COD_EVE,";
    lStatement += " COD_TIPO_PROVVEDIMENTO, CODTIPPRO.RV_MEANING COD_PRO,";
     //GDV - 09/02/2006 - Selezione dell'attributo legge dell'evento
    lStatement += " CODMOV.RV_ABBREVIATION COD_ABBR,";

    lStatement += " COD_MOTIVO, CODMOV.RV_MEANING COD_MOV,";
    lStatement += " COD_UFFICIO_EMITTENTE, UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMITTENTE , ";
    lStatement += " COD_LUOGO_EMITTENTE, LUOEMI.DESCRIZIONE LUO_EMI, ";
    lStatement += " NOME_SOGGETTO_PRESENTANTE, ";
    lStatement += " COGNOME_SOGGETTO_PRESENTANTE, ";
    lStatement += " DATA_EMISSIONE, ";
    lStatement += " COD_ESITO, CODESI.RV_MEANING COD_ESI,";
    lStatement += " FLAG_PIU_MENO, ";
    lStatement += " DATA_TRASMISSIONE_ATTI, ";
    lStatement += " DATA_RICEZIONE_ATTI, ";
    lStatement += " COD_UFFICIO_DESTINATARIO, ";
    lStatement += " COD_LUOGO_DESTINATARIO, LUODES.DESCRIZIONE LUO_DES,";
    lStatement += " ANNO_PROTOCOLLO, ";
    lStatement += " PROGR_PROTOCOLLO, ";
    lStatement += " DOC_BLOB, ";
    lStatement += " COD_OPERATORE_INSERIMENTO, ";
    lStatement += " DATA_INSERIMENTO,";
    lStatement += " COD_UFFICIO_INSERIMENTO,";
    lStatement += " COD_OPERATORE_AGGIORNAMENTO, ";
    lStatement += " DATA_AGGIORNAMENTO,";
    lStatement += " COD_UFFICIO_AGGIORNAMENTO, ";
    lStatement += " FAS_SIE_ID_FASCICOLO_SIEP, ";
    lStatement += " FAS_SIU_ID_FASCICOLO_SIUS, ";
    lStatement += " UFF_TIPO_DES.RV_MEANING DESC_UFF_DESTINATARIO, ";
    lStatement += " FLAG_DOCUMENTO_REGISTRATO, ";
    lStatement += " COD_MAGISTRATO, COD_TIPO_UFFICIO_DESTINATARIO, ";
    lStatement += " FAS_SIU_ID_FASCICOLO_SIUS_DEST,  ";
    lStatement += " TEM_ID_TEMPLATE,  "; // Add By Paolo
    lStatement += " FLAG_STAMPA_SIEP,  ";
    lStatement += " FLAG_STAMPA_SIUS,  ";
    lStatement += " FLAG_VIDEO_SIEP,  ";
    lStatement += " FLAG_VIDEO_SIUS  ";
    lStatement += " FROM EVENTO, cg_ref_codes CODESI,cg_ref_codes CODMOV,UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI,";
    lStatement += " CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE, COMUNE LUOEMI,COMUNE LUODES, CG_REF_CODES UFF_TIPO_DES";
    lStatement += " WHERE";
    lStatement += " EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND";
    lStatement += " EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND";
    lStatement += " EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND";
    lStatement += " EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'  AND";
    lStatement += " EVENTO.COD_LUOGO_EMITTENTE =  LUOEMI.COD_COMUNE AND";
    lStatement += " EVENTO.COD_LUOGO_DESTINATARIO = LUODES.COD_COMUNE";
    lStatement += " AND UFF_EMI.COD_UFFICIO = COD_UFFICIO_EMITTENTE AND UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO' ";
    lStatement += " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";
    lStatement += " AND UFF_TIPO_DES.RV_LOW_VALUE = EVENTO.COD_TIPO_UFFICIO_DESTINATARIO AND UFF_TIPO_DES.RV_DOMAIN = 'TIPO_UFFICIO'";

    return lStatement;
  }
}
