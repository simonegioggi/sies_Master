package siap.siep.annotazionemanuale.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.annotazionemanuale.model.UltimaAnnotazioneModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>Title: UltimaAnnotazioneManualeSqlDAO </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class UltimaAnnotazioneManualeSqlDAO extends SqlDAO
{
  public UltimaAnnotazioneManualeSqlDAO(Connection con)
  {
    super(con);
  }

 public void ricercaEventoOrdinanza(BigDecimal aKey) throws DAOException
  {
    String lStatement = getSqlQuery();
    lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
    lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '03'";
    lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";
    lStatement += " AND EVENTO.COD_MOTIVO IN ('0122','0212','0213','0210','0211','0121','0284','0285','0286')";

    lStatement += setOrderEventoDesc();

    setStatement(lStatement);
  }

 public void ricercaEventoProvvedimento(BigDecimal aKey) throws DAOException
  {
    String lStatement = getSqlQuery();
    lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;

    lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";
    // 31/07/2006 Sostituzione del controllo di COD_TIPO_PROVVEDIMENTO: l'Indulto crea un EVENTO con COD_TIPO_PROVVEDIMENTO = 26.
    //lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '04'";
    lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO IN ('04', '26')";
    lStatement += " AND EVENTO.COD_MOTIVO IN ('0122','0210','0211','0212','0213','0121','0284','0285','0286')";
/*
    lStatement += " AND ( (EVENTO.COD_TIPO_PROVVEDIMENTO = '04'";
    lStatement += " AND EVENTO.COD_MOTIVO IN ('0122','0210','0211'))";
    lStatement += " OR ( EVENTO.COD_TIPO_PROVVEDIMENTO = '02'";
    lStatement += " AND EVENTO.COD_MOTIVO IN ('0212','0213','0121')))";
*/
    lStatement += setOrderEventoDesc();

    setStatement(lStatement);
  }

/*
  public void ricercaEventoRichiesta(BigDecimal aKey) throws DAOException
  {
    String lStatement = getSqlQuery();
    lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey;
    lStatement += " AND EVENTO.COD_TIPO_PROVVEDIMENTO = '04'";
    lStatement += " AND EVENTO.COD_TIPO_EVENTO = '01'";
    lStatement += " AND EVENTO.COD_MOTIVO IN ('0122','0212','0213','0210','0211','0121')";

    lStatement += setOrderEventoDesc();

    setStatement(lStatement);
  }
*/

  protected String getSqlQuery() throws DAOException
  {
    String lStatement = new String("");

    lStatement += " SELECT ID_EVENTO, ";
    lStatement += " COD_TIPO_EVENTO, CODEVE.RV_MEANING COD_EVE,";
    lStatement += " COD_TIPO_PROVVEDIMENTO, CODTIPPRO.RV_MEANING COD_PRO,";
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
   // lStatement += " DOC_BLOB, ";
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
    lStatement += " TEM_ID_TEMPLATE, "; // Add By Paolo
    lStatement += " FLAG_STAMPA_SIEP, ";
    lStatement += " FLAG_STAMPA_SIUS, ";
    lStatement += " FLAG_VIDEO_SIEP, ";
    lStatement += " FLAG_VIDEO_SIUS, ";
    lStatement += " EVE_ID_EVENTO, ";
    lStatement += " ANN_ID_ANNOTAZIONE_MANUALE, ";
    lStatement += " PEN_ID_PENA_RESIDUA ";
    lStatement += " FROM EVENTO, cg_ref_codes CODESI,cg_ref_codes CODMOV,UFFICIO UFF_EMI, CG_REF_CODES UFF_TIPO_EMI,";
    lStatement += " CG_REF_CODES CODTIPPRO,CG_REF_CODES CODEVE, COMUNE LUOEMI,COMUNE LUODES, CG_REF_CODES UFF_TIPO_DES";
    lStatement += " WHERE";
    lStatement += " EVENTO.COD_TIPO_EVENTO = CODEVE.RV_LOW_VALUE AND CODEVE.RV_DOMAIN = 'TIPO_EVENTO' AND";
    lStatement += " EVENTO.COD_TIPO_PROVVEDIMENTO = CODTIPPRO.RV_LOW_VALUE AND CODTIPPRO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND";
    lStatement += " EVENTO.COD_MOTIVO = CODMOV.RV_LOW_VALUE AND (CODMOV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' OR CODMOV.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO') AND"; //****
    lStatement += " EVENTO.COD_ESITO = CODESI.RV_LOW_VALUE AND CODESI.RV_DOMAIN = 'ESITO_PROVVEDIMENTO'  AND";
    lStatement += " EVENTO.COD_LUOGO_EMITTENTE =  LUOEMI.COD_COMUNE AND";
    lStatement += " EVENTO.COD_LUOGO_DESTINATARIO = LUODES.COD_COMUNE";
    lStatement += " AND UFF_EMI.COD_UFFICIO = COD_UFFICIO_EMITTENTE AND UFF_TIPO_EMI.RV_DOMAIN = 'TIPO_UFFICIO' ";
    lStatement += " AND UFF_TIPO_EMI.RV_LOW_VALUE = UFF_EMI.COD_TIPO_UFFICIO";
    lStatement += " AND UFF_TIPO_DES.RV_LOW_VALUE = EVENTO.COD_TIPO_UFFICIO_DESTINATARIO AND UFF_TIPO_DES.RV_DOMAIN = 'TIPO_UFFICIO'";

    return lStatement;
  }

  public GenericModel getModel() throws DAOException
  {
    UltimaAnnotazioneModel aModel = new UltimaAnnotazioneModel();

//Inserire le opportune set delle descrizioni!
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
    aModel.setDescrTipoUfficioDestinatario(getString("DESC_UFF_DESTINATARIO"));
    aModel.setDescrTipoUfficioDestinatario(getString("DESC_UFF_DESTINATARIO"));
    aModel.setTemIdTemplate(getString("TEM_ID_TEMPLATE"));
    aModel.setFlagStampaSiep(getString("FLAG_STAMPA_SIEP"));
    aModel.setFlagStampaSius(getString("FLAG_STAMPA_SIUS"));
    aModel.setFlagVideoSiep(getString("FLAG_VIDEO_SIEP"));
    aModel.setFlagVideoSius(getString("FLAG_VIDEO_SIUS"));
    aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
    aModel.setAnnIdAnnotazioneManuale(getBigDecimal("ANN_ID_ANNOTAZIONE_MANUALE"));
    aModel.setPenIdPenaResidua(getBigDecimal("PEN_ID_PENA_RESIDUA"));

    aModel.setLegge(getString("COD_ABBR"));

    return aModel;
  }

  private String setOrderEventoDesc()
  {
    String lCondizioni = " ORDER BY DATA_INSERIMENTO DESC";

    return lCondizioni;
  }
}
