package siap.siep.posizionematerialefasc.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>Title: PosizioneMaterialeFascSqlDAO</p>
 * <p>Description: Classe SqlDAO che rappresenta la tabella PosizioneMaterialeFasc</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class PosizioneMaterialeFascSqlDAO extends SqlDAO
{
  public PosizioneMaterialeFascSqlDAO(Connection con)
  {
    super(con);
  }

  //
  // METODO RICERCA()
  //

  public void ricercaPosizioneMaterialeFasc(PosizioneMaterialeFascModel aModel) 
  throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " " + setCondizione(aModel);
    lSql += " " + setOrder();

    setStatement(lSql);
  }

  public void ricercaPosizioneMaterialeFascAttivaXFas(BigDecimal aIdFascicolo) 
  throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += "  WHERE FAS_SIE_ID_FASCICOLO_SIEP= " + aIdFascicolo;
    lSql += " AND DATA_FINE IS NULL";

    setStatement(lSql);
  }

  public void ricercaPosizioneMaterialeFascicolo( String aCodPosizioneMateriale,
                                                  String aCodUfficio) 
    throws DAOException
  {
    String lSql = getSqlQueryFascicoli();

    lSql += " AND PF.COD_POSIZIONE_MATERIALE = '" + aCodPosizioneMateriale + "'";
    lSql += " AND PF.COD_UFFICIO = '" + aCodUfficio + "'";;
    lSql += " AND PF.DATA_FINE IS NULL";

    setStatement(lSql);
  }

  public void ricercaPosizioneMaterialeFascicoloSius( String aCodPosizioneMateriale,
                                                      String aCodUfficio) 
    throws DAOException
  {
    String lSql = getSqlQueryFascicoliSius();

    lSql += " AND PF.COD_POSIZIONE_MATERIALE = '" + aCodPosizioneMateriale + "'";
    lSql += " AND PF.COD_UFFICIO = '" + aCodUfficio + "'";;
    lSql += " AND PF.DATA_FINE IS NULL";

    setStatement(lSql);
  }
  
  protected String getSqlQuery()
  {
    String lStatement = new String("");

    lStatement += " SELECT " +
    "PF.COD_POSIZIONE_MATERIALE, " +
    "PF.COD_UFFICIO, " +
    "PF.FAS_SIE_ID_FASCICOLO_SIEP, " +
    "PF.COD_STATO_PROCEDIMENTO, " +
    "PF.DESCR_STATO_PROCEDIMENTO, " +
    "PF.DATA_INIZIO, " +
    "PF.DATA_FINE, " +
    "PF.COD_OPERATORE_INSERIMENTO, " +
    "PF.DATA_INSERIMENTO, " +
    "PF.COD_UFFICIO_INSERIMENTO, " +
    "PF.COD_OPERATORE_AGGIORNAMENTO, " +
    "PF.DATA_AGGIORNAMENTO, " +
    "PF.COD_UFFICIO_AGGIORNAMENTO, "+
    "P.DESC_POSIZIONE_MATERIALE";

    lStatement += " FROM POSIZIONE_MATERIALE_FASC PF";
    lStatement += " JOIN POSIZIONE_MATERIALE P ON P.COD_UFFICIO = PF.COD_UFFICIO";
    lStatement += " AND P.COD_POSIZIONE_MATERIALE = PF.COD_POSIZIONE_MATERIALE";

    //lStatement += " WHERE ";
    return lStatement;
  }

  protected String getSqlQueryFascicoli()
  {
    String lStatement = new String("");

    lStatement += "SELECT FASC.ANNO_FASCICOLO_UNIONE, FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR,";
    lStatement +=       " FASC.CHIAVE_UFFICIO, DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
    lStatement +=       " DESCR_TIPO_UFF.RV_LOW_VALUE  COD_TIPO_UFFICIO,";
    lStatement +=       " FASC.COD_MOTIVO_ARCHIVIAZIONE, MOTIVO_ARCHIVIAZIONE.RV_MEANING DESCR_MOTIVO_ARCHIVIAZIONE,";
    lStatement +=       " FASC.COD_OPERATORE_AGGIORNAMENTO, FASC.COD_OPERATORE_INSERIMENTO,";
    lStatement +=       " FASC.COD_STATO_FASCICOLO, STATO_FASCICOLO.RV_MEANING DESCR_STATO_FASCICOLO,";
    lStatement +=       " FASC.COD_TIPO_POS_LIBERO, TIPO_POS_LIBERO.RV_MEANING DESCR_TIPO_POS_LIBERO, ";
    lStatement +=       " FASC.COD_UFFICIO_AGGIORNAMENTO, FASC.COD_UFFICIO_INSERIMENTO, FASC.DATA_AGGIORNAMENTO,";
    lStatement +=       " FASC.DATA_ARCHIVIAZIONE, FASC.DATA_INSERIMENTO,";
    lStatement +=       " FASC.DATA_ISCRIZIONE, FASC.DATA_UNIONE, FASC.FAS_SIE_ID_FASCICOLO_SIEP,";
    lStatement +=       " FASC.FLAG_VALIDATO, FASC.ID_FASCICOLO_SIEP, FASC.LETTERA_FASCICOLO,";
    lStatement +=       " FASC.NOTE NOTE_FASCICOLO, FASC.NUM_FASCICOLO_UNIONE, FASC.SEN_ID_SENTENZA, FASC.SOG_ID_SOGGETTO,";
    lStatement +=       " FASC.FLAG_ALTRA_CAUSA, FASC.DATA_IRREVOCABILITA, ";
    lStatement +=       " FASC.FLAG_CUMULANTE, ";
    lStatement +=       " FASC.FLAG_CUMULATO, ";
    lStatement +=       " FASC.COD_UFFICIO_UNIONE, DESCR_TIPO_UFFUNIONE.RV_MEANING DESCR_TIPO_UFFICIO_UNIONE, DESCR_COM_UFFUNIONE.DESCRIZIONE DESCR_COMUNE_UFFICIO_UNIONE, ";
    lStatement +=       " SOGG.NOME NOME_SOGGETTO, SOGG.COGNOME COGNOME_SOGGETTO, ";
    lStatement +=       " PF.DATA_INIZIO DIPM ";
    lStatement +=  " FROM FASCICOLO_SIEP FASC, CG_REF_CODES MOTIVO_ARCHIVIAZIONE,";
    lStatement +=       " CG_REF_CODES STATO_FASCICOLO, CG_REF_CODES TIPO_POS_LIBERO, ";
    lStatement +=       " UFFICIO UFF, CG_REF_CODES DESCR_TIPO_UFF, COMUNE DESCR_COM_UFF, ";
    lStatement +=       " UFFICIO UFFUNIONE, CG_REF_CODES DESCR_TIPO_UFFUNIONE, COMUNE DESCR_COM_UFFUNIONE,";
    lStatement +=       " POSIZIONE_MATERIALE_FASC PF, SOGGETTO SOGG";
    lStatement +=  " WHERE (MOTIVO_ARCHIVIAZIONE.RV_DOMAIN = 'MOTIVO_ARCHIVIAZIONE' AND MOTIVO_ARCHIVIAZIONE.RV_LOW_VALUE = FASC.COD_MOTIVO_ARCHIVIAZIONE)";
    lStatement +=       " AND (STATO_FASCICOLO.RV_DOMAIN = 'STATO_FASCICOLO' AND STATO_FASCICOLO.RV_LOW_VALUE = FASC.COD_STATO_FASCICOLO)";
    lStatement +=       " AND (DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' AND UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE)";
    lStatement +=       " AND (FASC.CHIAVE_UFFICIO = UFF.COD_UFFICIO)";
    lStatement +=       " AND (UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE)";
    lStatement +=       " AND (DESCR_TIPO_UFFUNIONE.RV_DOMAIN = 'TIPO_UFFICIO' AND UFFUNIONE.COD_TIPO_UFFICIO = DESCR_TIPO_UFFUNIONE.RV_LOW_VALUE)";
    lStatement +=       " AND (FASC.COD_UFFICIO_UNIONE = UFFUNIONE.COD_UFFICIO)";
    lStatement +=       " AND (UFFUNIONE.COD_COMUNE = DESCR_COM_UFFUNIONE.COD_COMUNE)";
    lStatement +=       " AND (TIPO_POS_LIBERO.RV_DOMAIN = 'TIPO_POS_LIBERO' AND TIPO_POS_LIBERO.RV_LOW_VALUE = FASC.COD_TIPO_POS_LIBERO)";
    lStatement +=       " AND FASC.ID_FASCICOLO_SIEP = PF.FAS_SIE_ID_FASCICOLO_SIEP";
    lStatement +=       " AND FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";

    return lStatement;
  }

  protected String getSqlQueryFascicoliSius()
  {
    String lStatement = new String("");
    
    lStatement += "SELECT FASC.ID_FASCICOLO_SIUS, FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR, FASC.CHIAVE_UFFICIO, ";
    lStatement +=       " DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, ";
    lStatement +=       " FASC.COD_STATO_FASCICOLO, STATO_FASCICOLO.RV_MEANING DESCR_STATO_FASCICOLO, ";
    lStatement +=       " FASC.COD_OPERATORE_INSERIMENTO, FASC.COD_OPERATORE_AGGIORNAMENTO, ";
    lStatement +=       " FASC.COD_UFFICIO_INSERIMENTO, FASC.COD_UFFICIO_AGGIORNAMENTO, ";
    lStatement +=       " FASC.DATA_INSERIMENTO, FASC.DATA_ISCRIZIONE DATA_ISCRIZIONE, FASC.DATA_AGGIORNAMENTO, ";
    lStatement +=       " FASC.FAS_SIE_ID_FASCICOLO_SIEP, FASC.SOG_ID_SOGGETTO, FASC.FAS_SIU_ID_FASCICOLO_SIUS, FASC.ID_FASCICOLO_SIUS_ORIGINE, FASC.DATA_DEFINIZIONE, FASC.NUMERO_FASCICOLI_UNIFICATI, ";
    lStatement +=       " GP.ID_GENERALE_PROCEDIMENTO, GP.ANNO_S1, GP.PROGR_S1, GP.COD_OGGETTO_PROCEDIMENTO, GP.COD_TIPO_REGISTRO, TIPO_REGISTRO.RV_MEANING DESCR_TIPO_REGISTRO, ";
    lStatement +=       " OGGETTO_PROCEDIMENTO.RV_MEANING DESCR_OGGETTO_PROCEDIMENTO, GP.DATA_RICHIESTA, GP.UDI_ID_UDIENZA, ";
    lStatement +=       " GP.DATA_ARRIVO_CANCELLERIA, GP.DATA_CAMERA_CONSIGLIO, GP.COD_TIPO_ATTO, TIPO_ATTO.RV_MEANING DESCR_TIPO_ATTO, ";
    lStatement +=       " GP.DATA_DEFINIZIONE, GP.TIPO_DEFINIZIONE, GP.DESCR_DEFINIZIONE, ";
    lStatement +=       " GP.COD_TIPO_MITTENTE_ATTO, MITTENTE_ATTO.RV_MEANING DESCR_TIPO_MITTENTE, NVL(UD.DATA_UDIENZA, GP.DATA_CAMERA_CONSIGLIO ) DATA_UDIENZA, ";
    lStatement +=       " GP.COD_SEDE_MITTENTE, DESCR_COM_MIT.DESCRIZIONE DESCR_SEDE_MITTENTE, GP.ANNOTAZIONE, ";
    lStatement +=       " GP.COD_OPERATORE_AGGIORNAMENTO GP_COD_OP_AGG, GP.COD_UFFICIO_AGGIORNAMENTO GP_COD_UFF_AGG, GP.DATA_AGGIORNAMENTO GP_D_AGG, ";
    lStatement +=       " GP.SEZIONE SEZIONE, GP.DATA_FINE_PENA DATA_FINE_PENA, GP.COD_POSIZIONE_GIURIDICA COD_POSIZIONE_GIURIDICA, NVL(POS_GIURIDICA.RV_MEANING, '') DESCR_POSIZIONE_GIURIDICA, NVL(GP.COD_AUTORITA_DELEGATA, '-') COD_AUTORITA_DELEGATA, NVL(GP.DESCR_MITTENTE, '-') DESCR_MITTENTE, ";
    lStatement +=       " null CHIAVE_ANNO_SIEP, null CHIAVE_PROGR_SIEP , '-' CHIAVE_UFFICIO_SIEP ";
    lStatement +=       " ,DESCR_TIPO_UFF.RV_LOW_VALUE COD_TIPO_UFFICIO, PF.DATA_INIZIO DIPM ";
    lStatement +=   " FROM FASCICOLO_SIUS FASC, GENERALE_PROCEDIMENTO GP, CG_REF_CODES STATO_FASCICOLO, UDIENZA UD, ";
    lStatement +=       " CG_REF_CODES MITTENTE_ATTO, CG_REF_CODES DESCR_TIPO_UFF, CG_REF_CODES TIPO_ATTO, CG_REF_CODES OGGETTO_PROCEDIMENTO, CG_REF_CODES TIPO_REGISTRO,";
    lStatement +=       " CG_REF_CODES POS_GIURIDICA, UFFICIO UFF, COMUNE DESCR_COM_UFF, COMUNE DESCR_COM_MIT, ";
    lStatement +=       " POSIZIONE_MATERIALE_FASC_SIUS PF ";
    lStatement +=   " WHERE (STATO_FASCICOLO.RV_DOMAIN  = 'STATO_FASCICOLO' AND NVL(STATO_FASCICOLO.RV_LOW_VALUE,'-') = FASC.COD_STATO_FASCICOLO)";
    lStatement +=       " AND (DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO'    AND NVL(UFF.COD_TIPO_UFFICIO,'-') = DESCR_TIPO_UFF.RV_LOW_VALUE)";
    lStatement +=       " AND (MITTENTE_ATTO.RV_DOMAIN  = 'MITTENTE_ATTO'   AND NVL(GP.COD_TIPO_MITTENTE_ATTO,'-') = MITTENTE_ATTO.RV_LOW_VALUE)";
    lStatement +=       " AND (TIPO_REGISTRO.RV_DOMAIN  = 'TIPO_REGISTRO'   AND NVL(GP.COD_TIPO_REGISTRO,'-') = TIPO_REGISTRO.RV_LOW_VALUE)";
    lStatement +=       " AND (TIPO_ATTO.RV_DOMAIN      = 'TIPO_ATTO'       AND NVL(GP.COD_TIPO_ATTO,'-') = TIPO_ATTO.RV_LOW_VALUE)";
    lStatement +=       " AND (POS_GIURIDICA.RV_DOMAIN        = 'POSIZIONE_GIURIDICA'  AND NVL(GP.COD_POSIZIONE_GIURIDICA,'-') = POS_GIURIDICA.RV_LOW_VALUE)";
    lStatement +=       " AND (OGGETTO_PROCEDIMENTO.RV_DOMAIN = 'OGGETTO_PROCEDIMENTO' AND NVL(GP.COD_OGGETTO_PROCEDIMENTO,'-') = OGGETTO_PROCEDIMENTO.RV_LOW_VALUE)";
    lStatement +=       " AND (FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS)";
    lStatement +=       " AND (UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE AND FASC.CHIAVE_UFFICIO = UFF.COD_UFFICIO)";
    lStatement +=       " AND (GP.COD_SEDE_MITTENTE = DESCR_COM_MIT.COD_COMUNE)";
    lStatement +=       " AND (GP.UDI_ID_UDIENZA = UD.ID_UDIENZA (+) )";
    lStatement +=       " AND FASC.ID_FASCICOLO_SIUS = PF.FAS_SIUS_ID_FASCICOLO_SIUS";

    return lStatement;
  }

  //
  // METODO GETMODEL()
  //

  public GenericModel getModel() throws DAOException
  {
    PosizioneMaterialeFascModel aModel = new PosizioneMaterialeFascModel();

    //Inserire le opportune set delle descrizioni!
    aModel.setCodPosizioneMateriale(getString("COD_POSIZIONE_MATERIALE"));
    aModel.setDescrPosizioneMateriale(getString("DESC_POSIZIONE_MATERIALE"));
    aModel.setCodUfficio(getString("COD_UFFICIO"));
    //aModel.setDescrUfficio(getString(""));
    aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
    aModel.setCodStatoProcedimento(getString("COD_STATO_PROCEDIMENTO"));
    aModel.setDescrStatoProcedimento(getString("DESCR_STATO_PROCEDIMENTO"));
    if (aModel.getDescrStatoProcedimento() == null || aModel.getDescrStatoProcedimento().trim().length() < 1)
      aModel.setDescrStatoProcedimento(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getStatoProcedimento(), aModel.getCodStatoProcedimento() ));
    aModel.setDataInizio(getDate("DATA_INIZIO"));
    aModel.setDataFine(getDate("DATA_FINE"));
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
    //aModel.setDescrUfficioInserimento(getString(""));
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
    //aModel.setDescrUfficioAggiornamento(getString(""));

    return aModel;
  }

  public GenericModel getModelFascicoloPosizione() throws DAOException
  {
    FascicoloSiepModel lFascicolo = new  FascicoloSiepModel();

    lFascicolo.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP") );
    lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO") );
    lFascicolo.setChiaveUfficio(getString("CHIAVE_UFFICIO") );
    lFascicolo.setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO") );
    lFascicolo.setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO") );
    lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PROGR") );
    lFascicolo.setCodStatoFascicolo(getString("COD_STATO_FASCICOLO") );
    lFascicolo.setDescrStatoFascicolo(getString("DESCR_STATO_FASCICOLO") );
    lFascicolo.setDataIscrizione(getDate("DATA_ISCRIZIONE") );
    lFascicolo.setDataArchiviazione(getDate("DATA_ARCHIVIAZIONE") );
    lFascicolo.setCodMotivoArchiviazione(getString("COD_MOTIVO_ARCHIVIAZIONE") );
    lFascicolo.setDescrMotivoArchiviazione(getString("DESCR_MOTIVO_ARCHIVIAZIONE") );
    lFascicolo.setLetteraFascicolo(getString("LETTERA_FASCICOLO") );
    lFascicolo.setAnnoFascicoloUnione(getString("ANNO_FASCICOLO_UNIONE") );
    lFascicolo.setNumFascicoloUnione(getString("NUM_FASCICOLO_UNIONE") );
    lFascicolo.setDataUnione(getDate("DATA_UNIONE") );
    lFascicolo.setNote(getString("NOTE_FASCICOLO") );
    lFascicolo.setCodTipoPosLibero(getString("COD_TIPO_POS_LIBERO") );
    lFascicolo.setDescrTipoPosLibero(getString("DESCR_TIPO_POS_LIBERO") );
    lFascicolo.setFlagValidato(getString("FLAG_VALIDATO") );
    lFascicolo.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
    lFascicolo.setDataInserimento(getDate("DATA_INSERIMENTO") );
    lFascicolo.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
    lFascicolo.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
    lFascicolo.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
    lFascicolo.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
    lFascicolo.setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO") );
    lFascicolo.setSenIdSentenza(getBigDecimal("SEN_ID_SENTENZA") );
    lFascicolo.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP") );
    lFascicolo.setFlagAltraCausa(getString("FLAG_ALTRA_CAUSA") );
    lFascicolo.setCodTipoUfficio(getString("COD_TIPO_UFFICIO") );
    lFascicolo.setDataIrrevocabilita(getDate("DATA_IRREVOCABILITA") );
    lFascicolo.setFlagCumulante(getString("FLAG_CUMULANTE") );
    lFascicolo.setFlagCumulato(getString("FLAG_CUMULATO") );
    lFascicolo.setCodUfficioUnione(getString("COD_UFFICIO_UNIONE") );
    lFascicolo.setDescrTipoUfficioUnione(getString("DESCR_TIPO_UFFICIO_UNIONE") );
    lFascicolo.setDescrComuneUfficioUnione(getString("DESCR_COMUNE_UFFICIO_UNIONE") );
    lFascicolo.setDataInizioPosizioneMateriale(getDate("DIPM"));
    
    SoggettoModel lSoggetto = new SoggettoModel();

    lSoggetto.setNome(getString("NOME_SOGGETTO"));
    lSoggetto.setCognome(getString("COGNOME_SOGGETTO"));

    lFascicolo.setSoggetto(lSoggetto);

    return lFascicolo;
  }

  public GenericModel getModelFascicoloSiusPosizione() throws DAOException
  {
    FascicoloGPModel aModel = new  FascicoloGPModel();

    //Inserire le opportune set delle descrizioni!
    aModel.getFascicoloSiusModel().setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS") );
    aModel.getFascicoloSiusModel().setChiaveAnno(getBigDecimal("CHIAVE_ANNO") );
    aModel.getFascicoloSiusModel().setChiaveUfficio(getString("CHIAVE_UFFICIO") );
    aModel.getFascicoloSiusModel().setChiaveProgr(getBigDecimal("CHIAVE_PROGR") );
    aModel.getFascicoloSiusModel().setCodStatoFascicolo(getString("COD_STATO_FASCICOLO") );
    aModel.getFascicoloSiusModel().setDescrStatoFascicolo(getString("DESCR_STATO_FASCICOLO") );
    aModel.getFascicoloSiusModel().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
    aModel.getFascicoloSiusModel().setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
    aModel.getFascicoloSiusModel().setDataInserimento(getDate("DATA_INSERIMENTO") );
    aModel.getFascicoloSiusModel().setDataIscrizione(getDate("DATA_ISCRIZIONE") );
    aModel.getFascicoloSiusModel().setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
    aModel.getFascicoloSiusModel().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
    aModel.getFascicoloSiusModel().setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO") );
    aModel.getFascicoloSiusModel().setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP") );
    aModel.getFascicoloSiusModel().setChiaveAnnoSIEP(getBigDecimal("CHIAVE_ANNO_SIEP") );
    aModel.getFascicoloSiusModel().setChiaveProgrSIEP(getBigDecimal("CHIAVE_PROGR_SIEP") );
    aModel.getFascicoloSiusModel().setChiaveUfficioSIEP(getString("CHIAVE_UFFICIO_SIEP") );
    aModel.getFascicoloSiusModel().setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS") );
    aModel.getFascicoloSiusModel().setIdFascicoloSiusOrigine(getBigDecimal("ID_FASCICOLO_SIUS_ORIGINE") ); // 15/01/2004
    aModel.getFascicoloSiusModel().setDataDefinizione(getDate("DATA_DEFINIZIONE") );
    aModel.getFascicoloSiusModel().setNumeroFascicoliUnificati(getBigDecimal("NUMERO_FASCICOLI_UNIFICATI") ); // STUB 29/04/2004
    aModel.getFascicoloSiusModel().setDescrTipoUfficio(getString("DESCR_TIPO_UFFICIO") );
    aModel.getFascicoloSiusModel().setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO") );
    aModel.getFascicoloSiusModel().setCodTipoUfficio(getString("COD_TIPO_UFFICIO"));    
    aModel.getFascicoloSiusModel().setDataInizioPosizioneMateriale(getDate("DIPM"));
    
    aModel.getGeneraleProcedimentoModel().setIdGeneraleProcedimento(getBigDecimal("ID_GENERALE_PROCEDIMENTO") );
    aModel.getGeneraleProcedimentoModel().setAnnoS1(getBigDecimal("ANNO_S1") );
    aModel.getGeneraleProcedimentoModel().setProgrS1(getBigDecimal("PROGR_S1") );
    aModel.getGeneraleProcedimentoModel().setCodTipoRegistro(getString("COD_TIPO_REGISTRO") ) ;
    aModel.getGeneraleProcedimentoModel().setDescrTipoRegistro(getString("DESCR_TIPO_REGISTRO") ) ;
    aModel.getGeneraleProcedimentoModel().setDataCameraConsiglio(getDate("DATA_CAMERA_CONSIGLIO"));
    aModel.getGeneraleProcedimentoModel().setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO") );
    aModel.getGeneraleProcedimentoModel().setDescrOggettoProcedimento(getString("DESCR_OGGETTO_PROCEDIMENTO") );
    aModel.getGeneraleProcedimentoModel().setDataRichiesta(getDate("DATA_RICHIESTA") );
    aModel.getGeneraleProcedimentoModel().setDataArrivoCancelleria(getDate("DATA_ARRIVO_CANCELLERIA") );
    aModel.getGeneraleProcedimentoModel().setCodTipoAtto(getString("COD_TIPO_ATTO") );
    aModel.getGeneraleProcedimentoModel().setDescrTipoAtto(getString("DESCR_TIPO_ATTO") );
    aModel.getGeneraleProcedimentoModel().setCodTipoMittenteAtto(getString("COD_TIPO_MITTENTE_ATTO") );
    aModel.getGeneraleProcedimentoModel().setDescrTipoMittenteAtto(getString("DESCR_TIPO_MITTENTE") );
    aModel.getGeneraleProcedimentoModel().setCodSedeMittente(getString("COD_SEDE_MITTENTE") );
    aModel.getGeneraleProcedimentoModel().setDescrSedeMittente(getString("DESCR_SEDE_MITTENTE") );
    aModel.getGeneraleProcedimentoModel().setAnnotazione(getString("ANNOTAZIONE") );
    aModel.getGeneraleProcedimentoModel().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
    aModel.getGeneraleProcedimentoModel().setDataInserimento(getDate("DATA_INSERIMENTO") );
    aModel.getGeneraleProcedimentoModel().setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
    aModel.getGeneraleProcedimentoModel().setDescrUfficioInserimento("") ;
    aModel.getGeneraleProcedimentoModel().setCodOperatoreAggiornamento(getString("GP_COD_OP_AGG") );
    aModel.getGeneraleProcedimentoModel().setDataAggiornamento(getDate("GP_D_AGG") );
    aModel.getGeneraleProcedimentoModel().setCodUfficioAggiornamento(getString("GP_COD_UFF_AGG") );
    aModel.getGeneraleProcedimentoModel().setDescrUfficioAggiornamento("") ;
    aModel.getGeneraleProcedimentoModel().setFasSiuIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS") );
    aModel.getGeneraleProcedimentoModel().setSezione(getString("SEZIONE") );
    aModel.getGeneraleProcedimentoModel().setDataFinePena(getDate("DATA_FINE_PENA") );
    aModel.getGeneraleProcedimentoModel().setCodPosGiuridica(getString("COD_POSIZIONE_GIURIDICA") );
    aModel.getGeneraleProcedimentoModel().setDescrPosGiuridica(getString("DESCR_POSIZIONE_GIURIDICA") );
    aModel.getGeneraleProcedimentoModel().setDataCameraConsiglio(getDate("DATA_UDIENZA") );
    aModel.getGeneraleProcedimentoModel().setUdiIdUdienza(getBigDecimal("UDI_ID_UDIENZA") );
    aModel.getGeneraleProcedimentoModel().setCodAutoritaDelegata(getString("COD_AUTORITA_DELEGATA") );
    aModel.getGeneraleProcedimentoModel().setDescrMittente(getString("DESCR_MITTENTE") );

    aModel.getGeneraleProcedimentoModel().setTipoDefinizione(getString("TIPO_DEFINIZIONE"));
    aModel.getGeneraleProcedimentoModel().setDescrDefinizione(getString("DESCR_DEFINIZIONE"));
    aModel.getGeneraleProcedimentoModel().setDataDefinizione(getDate("DATA_DEFINIZIONE"));

    return aModel;
  }

  public String setCondizione(PosizioneMaterialeFascModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;


    if (aModel.getCodPosizioneMateriale() != null && aModel.getCodPosizioneMateriale().trim().length() > 0)
    {
      lInserito = true;
      lCondizioni = " COD_POSIZIONE_MATERIALE = '" + aModel.getCodPosizioneMateriale().trim() + "'"; ;
    }
    if (aModel.getCodUfficio() != null && aModel.getCodUfficio().trim().length() > 0)
    {
      if (lInserito)
        lCondizioni += " AND";

      lInserito = true;
      lCondizioni += " COD_UFFICIO = '" + aModel.getCodUfficio().trim() + "'"; ;
    }

    if (aModel.getFasSieIdFascicoloSiep() != null )
    {
      if (lInserito)
        lCondizioni += " AND";

      lInserito = true;
      lCondizioni += " FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep();
    }

    // Aggiungere le altre condizioni ....

    if (lInserito)
      lCondizioni = " WHERE " + lCondizioni;

    return lCondizioni;
  }

//Setta l'ordinamento per DATA DI INIZIO dalla più recente alla meno recente
  public String setOrder()
  {
    String lOrdering = " ORDER BY DATA_AGGIORNAMENTO DESC" ;

    return lOrdering;
  }
}