package siap.siep.fascicolo.dao;

/**
 * <p>Title: FascicoloSiepSqlDAO</p>
 * <p>Description: Realizza Sql DAo del Fascicolo Siep
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepPadreModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

public class FascicoloSiepPadreSqlDAO extends SIAPSqlDAO
{
  public FascicoloSiepPadreSqlDAO(Connection aCon)
  {
    super(aCon);
  }

  protected String getFascicoloSqlQuery()
  {
    String lStatement = new String();

    lStatement += "SELECT FASC.ANNO_FASCICOLO_UNIONE, FASC.CHIAVE_ANNO, FASC.CHIAVE_PROGR,";
    lStatement +=       " FASC.CHIAVE_UFFICIO, DESCR_TIPO_UFF.RV_MEANING DESCR_TIPO_UFFICIO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO,";
    lStatement +=       " DESCR_TIPO_UFF.RV_LOW_VALUE  COD_TIPO_UFFICIO,";
    lStatement +=       " FASC.COD_MOTIVO_ARCHIVIAZIONE, MOTIVO_ARCHIVIAZIONE.RV_MEANING DESCR_MOTIVO_ARCHIVIAZIONE,";
    lStatement +=       " FASC.COD_OPERATORE_AGGIORNAMENTO, FASC.COD_OPERATORE_INSERIMENTO,";
    lStatement +=       " FASC.COD_STATO_FASCICOLO, STATO_FASCICOLO.RV_MEANING DESCR_STATO_FASCICOLO,";
    lStatement +=       " FASC.COD_TIPO_POS_LIBERO, TIPO_POS_LIBERO.RV_MEANING DESCR_TIPO_POS_LIBERO, ";
    lStatement +=       " FASC.COD_UFFICIO_AGGIORNAMENTO, FASC.DATA_AGGIORNAMENTO,";
    lStatement +=       " FASC.DATA_ARCHIVIAZIONE, FASC.DATA_INSERIMENTO,";
    lStatement +=       " FASC.DATA_ISCRIZIONE, FASC.DATA_UNIONE, FASC.FAS_SIE_ID_FASCICOLO_SIEP,";
    lStatement +=       " FASC.FLAG_VALIDATO, FASC.ID_FASCICOLO_SIEP, FASC.LETTERA_FASCICOLO,";
    lStatement +=       " FASC.NOTE NOTE_FASCICOLO, FASC.NUM_FASCICOLO_UNIONE, FASC.SEN_ID_SENTENZA, FASC.SOG_ID_SOGGETTO,";
    lStatement +=       " FASC.FLAG_ALTRA_CAUSA, FASC.DATA_IRREVOCABILITA, ";
    lStatement +=       " FASC.FLAG_CUMULANTE, ";
    lStatement +=       " FASC.FLAG_CUMULATO, ";
    //lStatement +=       " FASC.COD_UFFICIO_UNIONE, DESCR_TIPO_UFFUNIONE.RV_MEANING DESCR_TIPO_UFFICIO_UNIONE, DESCR_COM_UFFUNIONE.DESCRIZIONE DESCR_COMUNE_UFFICIO_UNIONE ";
    lStatement +=       " FASC.COD_UFFICIO_UNIONE, DESCR_TIPO_UFFUNIONE.RV_MEANING DESCR_TIPO_UFFICIO_UNIONE, DESCR_COM_UFFUNIONE.DESCRIZIONE DESCR_COMUNE_UFFICIO_UNIONE, FASC.KEY_PROVV_NSC, ";

    //Modifica Accorpamento Uffici  
    lStatement +=       " FASC.CHIAVE_PROGR_ORIG, ";
    lStatement +=       " FASC.COD_UFFICIO_INSERIMENTO, UFFINSERIMENTO.COD_TIPO_UFFICIO COD_TIPO_UFFICIO_INS, DESCR_TIPO_UFFINSERIMENTO.RV_MEANING DESCR_TIPO_UFFICIO_INS, DESCR_COM_UFFINSERIMENTO.DESCRIZIONE DESCR_COMUNE_UFFICIO_INS,";
    lStatement +=       " UFFINSERIMENTO.FLAG_ACCORP FLAG_UFFICIO_ACCORPATO";
    
    lStatement +=       " FROM FASCICOLO_SIEP FASC, CG_REF_CODES MOTIVO_ARCHIVIAZIONE,";
    lStatement +=       " CG_REF_CODES STATO_FASCICOLO, CG_REF_CODES TIPO_POS_LIBERO, ";
    lStatement +=       " UFFICIO UFF, CG_REF_CODES DESCR_TIPO_UFF, COMUNE DESCR_COM_UFF, ";
    lStatement +=       " UFFICIO UFFUNIONE, CG_REF_CODES DESCR_TIPO_UFFUNIONE, COMUNE DESCR_COM_UFFUNIONE, ";
    //Modifica Accorpamento Uffici
    lStatement +=       " UFFICIO UFFINSERIMENTO, ";
    lStatement +=       " CG_REF_CODES DESCR_TIPO_UFFINSERIMENTO, ";
    lStatement +=       " COMUNE DESCR_COM_UFFINSERIMENTO ";
    
    lStatement +=       " WHERE (MOTIVO_ARCHIVIAZIONE.RV_DOMAIN = 'MOTIVO_ARCHIVIAZIONE' AND MOTIVO_ARCHIVIAZIONE.RV_LOW_VALUE = FASC.COD_MOTIVO_ARCHIVIAZIONE)";
    lStatement +=       " AND (STATO_FASCICOLO.RV_DOMAIN = 'STATO_FASCICOLO' AND STATO_FASCICOLO.RV_LOW_VALUE = FASC.COD_STATO_FASCICOLO)";
    lStatement +=       " AND (DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' AND UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE)";
    lStatement +=       " AND (FASC.CHIAVE_UFFICIO = UFF.COD_UFFICIO)";
    lStatement +=       " AND (UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE)";

    lStatement +=       " AND (DESCR_TIPO_UFFUNIONE.RV_DOMAIN = 'TIPO_UFFICIO' AND UFFUNIONE.COD_TIPO_UFFICIO = DESCR_TIPO_UFFUNIONE.RV_LOW_VALUE)";
    lStatement +=       " AND (FASC.COD_UFFICIO_UNIONE = UFFUNIONE.COD_UFFICIO)";
    lStatement +=       " AND (UFFUNIONE.COD_COMUNE = DESCR_COM_UFFUNIONE.COD_COMUNE)";

    lStatement +=       " AND (TIPO_POS_LIBERO.RV_DOMAIN = 'TIPO_POS_LIBERO' AND TIPO_POS_LIBERO.RV_LOW_VALUE = FASC.COD_TIPO_POS_LIBERO)";

    lStatement +=       " AND (FASC.Cod_Ufficio_Inserimento = UFFINSERIMENTO.COD_UFFICIO )";
    lStatement +=       " AND (UFFINSERIMENTO.COD_TIPO_UFFICIO = DESCR_TIPO_UFFINSERIMENTO.RV_LOW_VALUE AND DESCR_TIPO_UFFINSERIMENTO.RV_DOMAIN = 'TIPO_UFFICIO')";
    lStatement +=       " AND (UFFINSERIMENTO.COD_COMUNE = DESCR_COM_UFFINSERIMENTO.COD_COMUNE)";
    
    return lStatement;
  }

 /**
  * Esegue la ricerca di un fascicolo
  * @param aModel
  * @throws DAOException
  */
  public void ricercaFascicolo(FascicoloSiepModel aModel)
    throws DAOException
  {
    String lStatement = getFascicoloSqlQuery();
    lStatement += " " + setCondizione(aModel);
    lStatement += " " + setOrder();

    setStatement( lStatement );
  }


/**
  * Esegue la ricerca di un fascicolo tramite Chiave
  * @param aModel
  * @throws DAOException
  */
  public void ricercaFascicoloByKey(BigDecimal aKey)
    throws DAOException
  {
    String lStatement = getFascicoloSqlQuery();

    lStatement += " AND ID_FASCICOLO_SIEP = " + aKey;

    setStatement( lStatement );
  }


  /**
   * Setta le condizioni per la Ricerca
   * @param aModel
   * @return
   */
  private String setCondizione(FascicoloSiepModel aModel)
  {
    String lCondizioni = new String();

    if ( (aModel.getSenIdSentenza() != null) && (aModel.getSenIdSentenza().intValue() > 0) )
    {
      lCondizioni = " AND SEN_ID_SENTENZA = " + aModel.getSenIdSentenza() + "";
    }
    if  (aModel.getSogIdSoggetto() != null)
    {
      lCondizioni += " AND SOG_ID_SOGGETTO = " + aModel.getSogIdSoggetto() + "";
    }
    if ( (aModel.getIdFascicoloSiep() != null) && (aModel.getIdFascicoloSiep().intValue() > 0) )
    {
      lCondizioni += " AND ID_FASCICOLO_SIEP = " + aModel.getIdFascicoloSiep() + "";
    }
    // Cerca i fascicoli a partire da una coppia Progressivo/Anno
    if ( (aModel.getChiaveAnnoIniziale() != null ) && (aModel.getChiaveAnnoIniziale().intValue() > 0)
         && (aModel.getChiaveProgrIniziale() != null ) && (aModel.getChiaveProgrIniziale().intValue() > 0) )
    {
      lCondizioni += " AND ( (CHIAVE_ANNO > "+aModel.getChiaveAnnoIniziale()+")";
      lCondizioni +=      " OR (CHIAVE_ANNO = "+aModel.getChiaveAnnoIniziale()+" AND CHIAVE_PROGR >= "+aModel.getChiaveProgrIniziale()+"))";
    }
   // Cerca i fascicoli fino ad una coppia Progressivo/Anno
   if ( (aModel.getChiaveAnnoFinale() != null ) && (aModel.getChiaveAnnoFinale().intValue() > 0)
         && (aModel.getChiaveProgrFinale() != null ) && (aModel.getChiaveProgrFinale().intValue() > 0) )
    {
      // Nel caso non venga specificata la coppia di ricerca iniziale,
      // vengono cercati i fascicoli
      // a partire dal primo fascicolo dell'anno finale specificato
      if ( (aModel.getChiaveAnnoIniziale() == null ) || (aModel.getChiaveAnnoIniziale().intValue() <= 0)
          && (aModel.getChiaveProgrIniziale() == null ) || (aModel.getChiaveProgrIniziale().intValue() <= 0) )
      {
        lCondizioni += " AND ( (CHIAVE_ANNO > "+aModel.getChiaveAnnoFinale()+")";
        lCondizioni +=      " OR (CHIAVE_ANNO = "+aModel.getChiaveAnnoFinale()+" AND CHIAVE_PROGR >= 1))";
      }

      lCondizioni += " AND ( (CHIAVE_ANNO < "+aModel.getChiaveAnnoFinale()+")";
      lCondizioni += " OR (CHIAVE_ANNO = "+aModel.getChiaveAnnoFinale()+" AND CHIAVE_PROGR <= "+aModel.getChiaveProgrFinale()+"))";
    }


    return lCondizioni;
  }

  private String setOrder()
  {
    String lOrder = new String();

    lOrder = " ORDER BY 2, 3 ";

    return lOrder;
  }

 /**
  *
  * @return Il Model dei dati selezionati
  * @throws DAOException
  */
  public GenericModel getModel() throws DAOException
  {
    FascicoloSiepPadreModel lFascicolo = new  FascicoloSiepPadreModel();

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
    
    lFascicolo.setKeyProvvNsc(getBigDecimal("KEY_PROVV_NSC") );

    //Modifica Accorpamento Uffici   
    lFascicolo.setChiaveProgrOrig(getBigDecimal("CHIAVE_PROGR_ORIG") );
    lFascicolo.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
    lFascicolo.setCodTipoUfficioInserimento(getString("COD_TIPO_UFFICIO_INS") );
    lFascicolo.setDescrTipoUfficioInserimento(getString("DESCR_TIPO_UFFICIO_INS") );
    lFascicolo.setDescrComuneUfficioInserimento(getString("DESCR_COMUNE_UFFICIO_INS") );
    lFascicolo.setFlagUfficioAccorpato(getString("FLAG_UFFICIO_ACCORPATO") );

    return lFascicolo;
  }

}