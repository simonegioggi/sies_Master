package siap.siep.ordineesecuzione.dao;

/**
* <p>Title: OrdineEsecuzioneSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella Evento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.evento.model.EventoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.web.IWebConstants;

public class OrdineEsecuzioneSqlDao extends SIAPSqlDAO
{
  public OrdineEsecuzioneSqlDao (Connection con)
  {
    super(con);
  }

  //
  // METODO RICERCA()
  //
  public void ricercaEvento( EventoModel  aModel)	 throws DAOException
  {
    String lStatement = getSqlQuery();
    lStatement +=  " " + setCondizioni();

    setStatement(lStatement);
  }

  public void ricercaEventoByIdFascicolo( BigDecimal aFascicoloId)	 throws DAOException
  {
    String lStatement = getSqlQuery();

    lStatement +=  " " + setCondizioniByIdFascicolo(aFascicoloId);
    lStatement +=  " " + setCondizioni() + " " + setOrder();

    setStatement(lStatement);
  }

  public void ricercaTuttiEventiByIdFascicolo( BigDecimal aFascicoloId)	 throws DAOException
  {
    String lStatement = getSqlQuery();
    lStatement +=  " " + setCondizioniByIdFascicolo(aFascicoloId);
    lStatement +=  " " + setOrder();
    setStatement(lStatement);
  }


   public void ricercaTuttiEventiByIdFascicoloPaged( BigDecimal aFascicoloId,int aPage)	 throws DAOException
   {
     String lStatement = getSqlQuery();
     String lPaginedStatement=new String("");

     lStatement +=  " " + setCondizioniByIdFascicolo(aFascicoloId);
     lStatement +=  " " + setOrder();

     lPaginedStatement="SELECT * FROM (SELECT INNER.* , Rownum rn FROM ("+lStatement+"  ) INNER ) WHERE rn between  "+((aPage-1)*IWebConstants.RESULT_PER_PAGE+1)+ " AND "+ (aPage)*IWebConstants.RESULT_PER_PAGE;

     setStatement(lPaginedStatement);
   }


  public void getCountEventiStatoEsecuzione(BigDecimal aFascicoloId)  throws DAOException

  {
    String lStatement ="SELECT COUNT(*) HowManyRecords FROM EVENTO";
    lStatement +=  " WHERE FAS_SIE_ID_FASCICOLO_SIEP =" +aFascicoloId;
    lStatement +=  " AND EVENTO.COD_MOTIVO NOT IN ('0670') "; // MEV70 Esclusione Eventi con CodMotivo="0670"
    lStatement +=  " " + setOrder();

    setStatement(lStatement);
  }



/*
  public void ricercaTuttiEventiNOIstanzeByIdFascicolo( BigDecimal aFascicoloId) throws DAOException
  {
    String lStatement = getSqlQuery();

    lStatement +=  " " + setCondizioniByIdFascicolo(aFascicoloId);
    // Le ISTANZE vengono cercate in una select a parte
    lStatement +=  " AND COD_TIPO_EVENTO != '03'";
    lStatement +=  " " + setOrder();

    setStatement(lStatement);
  }

  public void ricercaTutteIstanzeByIdFascicolo( BigDecimal aFascicoloId) throws DAOException
  {
    String lStatement = getSqlQuery();

    lStatement +=  " " + setCondizioniByIdFascicolo(aFascicoloId);
    lStatement +=  " AND COD_TIPO_EVENTO = '03'";
    lStatement +=  " " + setOrder();

    setStatement(lStatement);
  }
*/

public void ricercaEventoNonRegistratoByFascicoloSiep(BigDecimal aIdFascicolo, String aTipEve, String aTipProv) throws DAOException
  {
    String lStatement = getSqlQuery();

    lStatement += " AND EVENTO.COD_TIPO_EVENTO = '"+aTipEve+"'";
    lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '"+aTipProv+"'";
    lStatement += " AND (EVENTO.FLAG_DOCUMENTO_REGISTRATO ='N' OR EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL)";
    lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;

    setStatement(lStatement);
  }


  public void ricercaEventoByFascicoloSiepXStampa (BigDecimal  aKey )	throws DAOException
  {
    String lStatement = getSqlQuery();

    lStatement +=  " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
    //lStatement +=  " AND COD_TIPO_EVENTO IN ('01','02','03','04')";
    lStatement +=  " AND COD_TIPO_EVENTO IN ('01','02','04')"; // Le ISTANZE vengono cercate in una select a parte
    lStatement +=  " AND FLAG_STAMPA_SIEP = 'S'";
    lStatement +=  " AND FLAG_DOCUMENTO_REGISTRATO = 'S'";

    lStatement += setOrder();

    setStatement(lStatement);
  }

  // Seleziona Eventi di tipo PROVVEDIMENTO, RICHIESTA, ISTANZA
  public void ricercaEventiPerCodiceTipoEventoByIdFascicolo( BigDecimal aFascicoloId)	 throws DAOException
  {
    String lStatement = getSqlQuery();

    lStatement += " " + setCondizioniByIdFascicolo(aFascicoloId);


    lStatement += " " + "AND COD_TIPO_EVENTO IN ('01', '02', '03')";
    lStatement += " " + setCondizioneEventiNonAnnullati();
    lStatement += " " + setOrderDesc();

    setStatement(lStatement);
  }

  public void ricercaEventoOERPNonRegistratoByFascicoloSiep(BigDecimal aIdFascicolo, String aTipEve, String aTipProv) throws DAOException
  {
    String lStatement = getSqlQuery();

    lStatement += " AND EVENTO.COD_TIPO_EVENTO = '"+aTipEve+"'";
    lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '"+aTipProv+"'";
    lStatement += " AND (EVENTO.FLAG_DOCUMENTO_REGISTRATO ='N' OR EVENTO.FLAG_DOCUMENTO_REGISTRATO IS NULL)";
    lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicolo;
    //richiesta modifica 25-02-04
    //lStatement += " AND COD_MOTIVO IN ('0130','0131','0132','0133','0134')";
    lStatement += " AND COD_MOTIVO IN ('0130','0131','0132','0000','0134')";

    setStatement(lStatement);
  }

 public void ricercaOrdineEsecuzioneByIdFascicolo(BigDecimal aIdFascicoloSiep) throws DAOException
  {
    String lStatement = getSqlQuery();

    lStatement += " AND (EVENTO.COD_TIPO_EVENTO = '01')";
    lStatement += " AND (EVENTO.FLAG_DOCUMENTO_REGISTRATO = 'S')";
    lStatement += " AND (EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep + ")";
    lStatement += " AND (CODMOV.RV_HIGH_VALUE LIKE 'OE%' OR CODMOV.RV_HIGH_VALUE LIKE 'LS%' " ;
    lStatement += "   OR CODMOV.RV_HIGH_VALUE LIKE 'RS%' OR CODMOV.RV_HIGH_VALUE LIKE 'LED%')";
    lStatement += setOrder();

    setStatement(lStatement);
  }

//modifica fatta il 01-02-05 luciana --dario per i codici 0222, 0223, 0224, 0277
  public void ricercaOrdineEsecuzionePeneConcorrentiByIdFascicolo(BigDecimal aIdFascicoloSiep) throws DAOException
   {
     String lStatement = getSqlQuery();

     lStatement += " AND (EVENTO.COD_TIPO_EVENTO = '01')";
     lStatement += " AND (EVENTO.FLAG_DOCUMENTO_REGISTRATO = 'S')";
     lStatement += " AND (EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep + ")";
     lStatement += " AND (CODMOV.RV_HIGH_VALUE LIKE 'OE%' OR CODMOV.RV_HIGH_VALUE LIKE 'LS%' " ;
     lStatement += "	 OR CODMOV.RV_HIGH_VALUE LIKE 'RS%' OR CODMOV.RV_HIGH_VALUE LIKE 'LED%' " ;
     lStatement += "   OR EVENTO.COD_MOTIVO = '0222' OR EVENTO.COD_MOTIVO = '0223' " ;
     lStatement += "   OR EVENTO.COD_MOTIVO = '0224' OR EVENTO.COD_MOTIVO = '0277')";
     lStatement += setOrder();

     setStatement(lStatement);
   }



/*
  // Seleziona Eventi di tipo ISTANZA
  public void ricercaEventiPerCodiceTipoEventoIstanzaByIdFascicolo( BigDecimal aFascicoloId)	 throws DAOException
  {
    String lStatement = getSqlQuery();

    lStatement +=  " " + setCondizioniByIdFascicolo(aFascicoloId);

    lStatement += " " + "AND COD_TIPO_EVENTO = '03'";
    lStatement +=  " " + setOrder();
    lStatement +=  " DESC ";

    setStatement(lStatement);
  }
*/

  public void ricercaEventoNonNotificatoByIdFascicolo( BigDecimal aFascicoloId)	 throws DAOException
  {
    String lStatement = "SELECT DISTINCT ID_EVENTO,  COD_TIPO_EVENTO, "
                      + " CODEVE.RV_MEANING COD_EVE, COD_TIPO_PROVVEDIMENTO, CODTIPPRO.RV_MEANING COD_PRO, COD_MOTIVO,"
                      + " CODMOV.RV_MEANING COD_MOV, COD_UFFICIO_EMITTENTE, UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMITTENTE ,  "
                      + " COD_LUOGO_EMITTENTE, LUOEMI.DESCRIZIONE LUO_EMI,  NOME_SOGGETTO_PRESENTANTE,  COGNOME_SOGGETTO_PRESENTANTE,  "
                      + " DATA_EMISSIONE, FLAG_STAMPA_SIEP, FLAG_VIDEO_SIEP, EVENTO.COD_ESITO, CODESI.RV_MEANING COD_ESI, FLAG_PIU_MENO, "
                      + " DATA_TRASMISSIONE_ATTI,  DATA_RICEZIONE_ATTI,  COD_UFFICIO_DESTINATARIO,  COD_LUOGO_DESTINATARIO, "
                      + " LUODES.DESCRIZIONE LUO_DES, ANNO_PROTOCOLLO,  PROGR_PROTOCOLLO,  EVENTO.COD_OPERATORE_INSERIMENTO,  "
                      + " EVENTO.DATA_INSERIMENTO, EVENTO.COD_UFFICIO_INSERIMENTO, COD_OPERATORE_AGGIORNAMENTO,  EVENTO.DATA_AGGIORNAMENTO,"
                      + " EVENTO.COD_UFFICIO_AGGIORNAMENTO,  FAS_SIE_ID_FASCICOLO_SIEP,  FAS_SIU_ID_FASCICOLO_SIUS,  "
                      + " UFF_TIPO_DES.RV_MEANING DESC_UFF_DESTINATARIO,  FLAG_DOCUMENTO_REGISTRATO,COD_MAGISTRATO, "
                      + " COD_TIPO_UFFICIO_DESTINATARIO,  FAS_SIU_ID_FASCICOLO_SIUS_DEST "
                      + " FROM EVENTO, "
                      + " NOTIFICA, "
                      + " CG_REF_CODES CODESI,CG_REF_CODES "
                      + " CODMOV,UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI, CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE, COMUNE LUOEMI,"
                      + " COMUNE LUODES, CG_REF_CODES UFF_TIPO_DES WHERE EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND "
                      + " CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND "
                      + " CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND "
                      + " CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND "
                      + " CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'  AND EVENTO.COD_LUOGO_EMITTENTE =  LUOEMI.COD_COMUNE AND "
                      + " EVENTO.COD_LUOGO_DESTINATARIO = LUODES.COD_COMUNE AND UFF_EMI.COD_UFFICIO = COD_UFFICIO_EMITTENTE "
                      + " AND UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO'  AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO "
                      + " AND UFF_TIPO_DES.RV_LOW_VALUE = EVENTO.COD_TIPO_UFFICIO_DESTINATARIO AND UFF_TIPO_DES.RV_DOMAIN = 'TIPO_UFFICIO'  "
                      + " AND NOTIFICA.EVE_ID_EVENTO = EVENTO.ID_EVENTO AND NOTIFICA.DATA_AVVENUTA_NOTIFICA IS NULL";
    lStatement += " " + setCondizioniByIdFascicolo(aFascicoloId);
    lStatement += " " + setCondizioni() + " " + setOrder();

    setStatement(lStatement);
  }

  protected String getSqlQuery() throws DAOException
  {
    String lStatement = new String("");

    lStatement +=  "SELECT DISTINCT ID_EVENTO, ";
    lStatement +=  " COD_TIPO_EVENTO, CODEVE.RV_MEANING COD_EVE,";
    lStatement +=  " COD_TIPO_PROVVEDIMENTO, CODTIPPRO.RV_MEANING COD_PRO,";
    lStatement +=  " COD_MOTIVO, CODMOV.RV_MEANING COD_MOV,";
    lStatement +=  " COD_UFFICIO_EMITTENTE, UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMITTENTE , ";
    lStatement +=  " COD_LUOGO_EMITTENTE, LUOEMI.DESCRIZIONE LUO_EMI, ";
    lStatement +=  " NOME_SOGGETTO_PRESENTANTE, ";
    lStatement +=  " COGNOME_SOGGETTO_PRESENTANTE, ";
    lStatement +=  " DATA_EMISSIONE, ";
    lStatement +=  " COD_ESITO, CODESI.RV_MEANING COD_ESI,";
    lStatement +=  " FLAG_PIU_MENO, ";
    lStatement +=  " DATA_TRASMISSIONE_ATTI, ";
    lStatement +=  " DATA_RICEZIONE_ATTI, ";
    lStatement +=  " COD_UFFICIO_DESTINATARIO, ";
    lStatement +=  " COD_LUOGO_DESTINATARIO, LUODES.DESCRIZIONE LUO_DES,";
    lStatement +=  " ANNO_PROTOCOLLO, ";
    lStatement +=  " PROGR_PROTOCOLLO, ";
    lStatement +=  " COD_OPERATORE_INSERIMENTO, ";
    lStatement +=  " DATA_INSERIMENTO,";
    lStatement +=  " COD_UFFICIO_INSERIMENTO,";
    lStatement +=  " COD_OPERATORE_AGGIORNAMENTO, ";
    lStatement +=  " DATA_AGGIORNAMENTO,";
    lStatement +=  " COD_UFFICIO_AGGIORNAMENTO, ";
    lStatement +=  " FAS_SIE_ID_FASCICOLO_SIEP, ";
    lStatement +=  " FAS_SIU_ID_FASCICOLO_SIUS, ";
    lStatement +=  " UFF_TIPO_DES.RV_MEANING DESC_UFF_DESTINATARIO, ";
    lStatement +=  " FLAG_DOCUMENTO_REGISTRATO, ";
    lStatement +=  " COD_MAGISTRATO, COD_TIPO_UFFICIO_DESTINATARIO, ";
    lStatement +=  " FAS_SIU_ID_FASCICOLO_SIUS_DEST,  ";
    lStatement +=  " FLAG_STAMPA_SIEP,  ";
    lStatement +=  " FLAG_VIDEO_SIEP ";
    lStatement +=	 " FROM EVENTO, cg_ref_codes CODESI,cg_ref_codes CODMOV,UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI,";
    lStatement +=  " CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE, COMUNE LUOEMI,COMUNE LUODES, CG_REF_CODES UFF_TIPO_DES";
    lStatement +=  " WHERE";
    lStatement +=  " EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND";
    lStatement +=  " EVENTO.COD_MOTIVO NOT IN ('0670') AND "; // MEV70 Esclusione Eventi con CodMotivo="0670"
    lStatement +=  " EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND";
    lStatement +=  " EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND (CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' OR CODMOV.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO') AND"; //****
    lStatement +=  " EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'  AND";
    lStatement +=  " EVENTO.COD_LUOGO_EMITTENTE =  LUOEMI.COD_COMUNE AND";
    lStatement +=  " EVENTO.COD_LUOGO_DESTINATARIO = LUODES.COD_COMUNE";
    lStatement +=  " AND UFF_EMI.COD_UFFICIO = COD_UFFICIO_EMITTENTE AND UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO' ";
    lStatement +=  " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";
    lStatement +=  " AND UFF_TIPO_DES.RV_LOW_VALUE = EVENTO.COD_TIPO_UFFICIO_DESTINATARIO AND UFF_TIPO_DES.RV_DOMAIN = 'TIPO_UFFICIO'";


    return lStatement;
  }

/*
  protected String getSqlQueryEventoIstanza()	 throws DAOException
  {
    String lStatement = new String("");

    lStatement +=  "SELECT ID_EVENTO, ";
    lStatement +=  " COD_TIPO_EVENTO, CODEVE.RV_MEANING COD_EVE,";
    lStatement +=  " COD_TIPO_PROVVEDIMENTO, CODTIPPRO.RV_MEANING COD_PRO,";
    lStatement +=  " COD_MOTIVO, OGGETTOPROCEDIMENTO.RV_MEANING COD_MOV,";
    lStatement +=  " COD_UFFICIO_EMITTENTE, UFF_TIPO_EMI.RV_MEANING DESC_UFF_EMITTENTE , ";
    lStatement +=  " COD_LUOGO_EMITTENTE, LUOEMI.DESCRIZIONE LUO_EMI, ";
    lStatement +=  " NOME_SOGGETTO_PRESENTANTE, ";
    lStatement +=  " COGNOME_SOGGETTO_PRESENTANTE, ";
    lStatement +=  " DATA_EMISSIONE, ";
    lStatement +=  " COD_ESITO, CODESI.RV_MEANING COD_ESI,";
    lStatement +=  " FLAG_PIU_MENO, ";
    lStatement +=  " DATA_TRASMISSIONE_ATTI, ";
    lStatement +=  " DATA_RICEZIONE_ATTI, ";
    lStatement +=  " COD_UFFICIO_DESTINATARIO, ";
    lStatement +=  " COD_LUOGO_DESTINATARIO, LUODES.DESCRIZIONE LUO_DES,";
    lStatement +=  " ANNO_PROTOCOLLO, ";
    lStatement +=  " PROGR_PROTOCOLLO, ";
    lStatement +=  " DOC_BLOB, ";
    lStatement +=  " COD_OPERATORE_INSERIMENTO, ";
    lStatement +=  " DATA_INSERIMENTO,";
    lStatement +=  " COD_UFFICIO_INSERIMENTO,";
    lStatement +=  " COD_OPERATORE_AGGIORNAMENTO, ";
    lStatement +=  " DATA_AGGIORNAMENTO,";
    lStatement +=  " COD_UFFICIO_AGGIORNAMENTO, ";
    lStatement +=  " FAS_SIE_ID_FASCICOLO_SIEP, ";
    lStatement +=  " FAS_SIU_ID_FASCICOLO_SIUS, ";
    lStatement +=  " UFF_TIPO_DES.RV_MEANING DESC_UFF_DESTINATARIO, ";
    lStatement +=  " FLAG_DOCUMENTO_REGISTRATO, ";
    lStatement +=  " COD_MAGISTRATO, COD_TIPO_UFFICIO_DESTINATARIO, ";
    lStatement +=  " FAS_SIU_ID_FASCICOLO_SIUS_DEST,  ";
    lStatement +=  " TEM_ID_TEMPLATE,  "; // Add By Paolo
    lStatement +=  " FLAG_STAMPA_SIEP,  ";
    lStatement +=  " FLAG_STAMPA_SIUS,  ";
    lStatement +=  " FLAG_VIDEO_SIEP,  ";
    lStatement +=  " FLAG_VIDEO_SIUS  ";
    lStatement +=	 " FROM EVENTO, cg_ref_codes CODESI,cg_ref_codes OGGETTOPROCEDIMENTO,UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI,";
    lStatement +=  " CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE, COMUNE LUOEMI,COMUNE LUODES, CG_REF_CODES UFF_TIPO_DES";
    lStatement +=  " WHERE";
    lStatement +=  " EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND";
    lStatement +=  " EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND";
    lStatement +=  " EVENTO.COD_MOTIVO = OGGETTOPROCEDIMENTO.RV_LOW_VALUE AND OGGETTOPROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' AND";
    lStatement +=  " EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'  AND";
    lStatement +=  " EVENTO.COD_LUOGO_EMITTENTE =  LUOEMI.COD_COMUNE AND";
    lStatement +=  " EVENTO.COD_LUOGO_DESTINATARIO = LUODES.COD_COMUNE";
    lStatement +=  " AND UFF_EMI.COD_UFFICIO = COD_UFFICIO_EMITTENTE AND UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO' ";
    lStatement +=  " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";
    lStatement +=  " AND UFF_TIPO_DES.RV_LOW_VALUE = EVENTO.COD_TIPO_UFFICIO_DESTINATARIO AND UFF_TIPO_DES.RV_DOMAIN = 'TIPO_UFFICIO'";

    return lStatement;
  }
*/
  //
  // METODO GETMODEL()
  //
  public GenericModel getModel() throws DAOException
  {
    EventoModel aModel = new  EventoModel();

    //Inserire le opportune set delle descrizioni!
    aModel.setIdEvento(getBigDecimal("ID_EVENTO") );
    aModel.setCodTipoEvento(getString("COD_TIPO_EVENTO") );
    aModel.setDescrTipoEvento(getString("COD_EVE") );
    aModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO") );
    aModel.setDescrTipoProvvedimento(getString("COD_PRO") );
    aModel.setCodMotivo(getString("COD_MOTIVO") );
    aModel.setDescrMotivo(getString("COD_MOV") );
    aModel.setCodUfficioEmittente(getString("COD_UFFICIO_EMITTENTE") );
    aModel.setDescrUfficioEmittente(getString("DESC_UFF_EMITTENTE") );
    aModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE") );
    aModel.setDescrLuogoEmittente(getString("LUO_EMI") );
    aModel.setCognomeSoggettoPresentante(getString("COGNOME_SOGGETTO_PRESENTANTE") );
    aModel.setNomeSoggettoPresentante(getString("NOME_SOGGETTO_PRESENTANTE") );
    aModel.setDataEmissione(getDate("DATA_EMISSIONE") );
    aModel.setCodEsito(getString("COD_ESITO") );
    aModel.setDescrEsito(getString("COD_ESI") );
    aModel.setFlagPiuMeno(getString("FLAG_PIU_MENO") );
    aModel.setDataTrasmissioneAtti(getDate("DATA_TRASMISSIONE_ATTI") );
    aModel.setDataRicezioneAtti(getDate("DATA_RICEZIONE_ATTI") );
    aModel.setCodUfficioDestinatario(getString("COD_UFFICIO_DESTINATARIO") );
    aModel.setCodLuogoDestinatario(getString("COD_LUOGO_DESTINATARIO") );
    aModel.setDescrLuogoDestinatario(getString("LUO_DES") );
    aModel.setAnnoProtocollo(getBigDecimal("ANNO_PROTOCOLLO") );
    aModel.setProgrProtocollo(getBigDecimal("PROGR_PROTOCOLLO") );
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
    aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP") );
    aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS") );
    aModel.setFlagDocumentoRegistrato(getString("FLAG_DOCUMENTO_REGISTRATO") );
    aModel.setCodMagistrato(getString("COD_MAGISTRATO") );
    aModel.setCodTipoUfficioDestinatario(getString("COD_TIPO_UFFICIO_DESTINATARIO") );
    aModel.setFasSiuIdFascicoloSiusDest(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS_DEST"));
    aModel.setDescrTipoUfficioDestinatario(getString("DESC_UFF_DESTINATARIO"));
    aModel.setFlagStampaSiep(getString("FLAG_STAMPA_SIEP"));
    aModel.setFlagVideoSiep(getString("FLAG_VIDEO_SIEP"));

    return aModel;
  }

  public String setCondizioni()
  {
    String lCondizioni = new String();

    // STUB 28/09/2005 lCondizioni +=" AND COD_MOTIVO IN ('0057','0058','0059','0060','0061','0062','0063')";
    lCondizioni +=" AND COD_MOTIVO IN ('0057','0217','0058','0059','0060','0061','0062','0063')";

    return lCondizioni;
  }

  public String setCondizioniByIdFascicolo(BigDecimal aFascicoloId)
  {
    String lCondizioni = new String();

    lCondizioni +=" AND FAS_SIE_ID_FASCICOLO_SIEP = " + aFascicoloId;

    return lCondizioni;
  }

  private String setOrder()
  {
    String lCondizioni = " ORDER BY DATA_EMISSIONE, DATA_INSERIMENTO,ID_EVENTO"; //, ANNO_PROTOCOLLO, PROGR_PROTOCOLLO ";

    return lCondizioni;
  }

  private String setOrderDesc()
  {
    String lCondizioni = " ORDER BY DATA_EMISSIONE DESC, DATA_INSERIMENTO DESC, ID_EVENTO DESC"; //, ANNO_PROTOCOLLO DESC, PROGR_PROTOCOLLO DESC";

    return lCondizioni;
  }

  private String setCondizioneEventiNonAnnullati()
{
  String lCondizioni = " AND FLAG_DOCUMENTO_REGISTRATO  <> 'A'"; //, ANNO_PROTOCOLLO DESC, PROGR_PROTOCOLLO DESC";

  return lCondizioni;
}

}