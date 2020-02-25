package siap.sius.motivazionedecreto.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sius.motivazionedecreto.model.MotivazioneDecretoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: MotivazioneDecretoSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella MotivazioneDecreto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class MotivazioneDecretoSqlDAO extends SIAPSqlDAO
{
  /**
   * Costruttore di classe con la connessione come argomento.
   * <p>
   * @param con connessione al Dbase.
   */
  public MotivazioneDecretoSqlDAO (Connection con)
  {
    super(con);
  }


  //
  // METODO RICERCA()
  //

  /**
  * Ritorna la select SQL per estrazione dati dalla tabella Motivazione_Decreto
  * senza join e senza traduzioni.
  * <p>
  * @return select sql.
  */
  protected String getSqlQueryPura()
  {
    String lStatement = new String("");
    lStatement += " SELECT ID_MOTIVAZIONE_DECRETO, ";
    lStatement +=	" COD_TIPO_MOTIVAZIONE, ";
    lStatement +=	" DESCR_MOTIVAZIONE, ";
    lStatement +=	" ALTRA_MOTIVAZIONE, ";
    lStatement +=	" COD_OPERATORE_INSERIMENTO, ";
    lStatement +=	" DATA_INSERIMENTO, ";
    lStatement +=	" COD_UFFICIO_INSERIMENTO, ";
    lStatement +=	" COD_OPERATORE_AGGIORNAMENTO, ";
    lStatement +=	" DATA_AGGIORNAMENTO, ";
    lStatement +=	" COD_UFFICIO_AGGIORNAMENTO, ";
    lStatement +=	" DEP_DEC_ID_DEPOSITO_DECRETO, ";
    lStatement += " PROGR_MOTIVAZIONE, ";
    lStatement += "EVE_ID_EVENTO ";
    lStatement += " FROM MOTIVAZIONE_DECRETO ";

    return lStatement;
  }




  /**
  * Ritorna la select SQL per estrazione dati dalla tabella Motivazione_Decreto
  * <p>
  * @return select sql.
  */
  protected String getSqlQuery()
  {
    String lStatement = new String("");
    lStatement += " SELECT ID_MOTIVAZIONE_DECRETO, ";
    lStatement +=	" COD_TIPO_MOTIVAZIONE, ";
    lStatement += "REPLACE( REPLACE( TIPO_MOT.RV_MEANING, '<?>', MOT_DECR.DESCR_MOTIVAZIONE ), '<?1>', MOT_DECR.ALTRA_MOTIVAZIONE) DESCR_MOTIVAZIONE,";
                //lStatement +=	" DESCR_MOTIVAZIONE, ";
                lStatement +=	" ALTRA_MOTIVAZIONE, ";
                lStatement +=	" COD_OPERATORE_INSERIMENTO, ";
                lStatement +=	" DATA_INSERIMENTO, ";
                lStatement +=	" COD_UFFICIO_INSERIMENTO, ";
                lStatement +=	" COD_OPERATORE_AGGIORNAMENTO, ";
                lStatement +=	" DATA_AGGIORNAMENTO, ";
                lStatement +=	" COD_UFFICIO_AGGIORNAMENTO, ";
                lStatement +=	" DEP_DEC_ID_DEPOSITO_DECRETO, ";
    lStatement += " PROGR_MOTIVAZIONE, ";
    lStatement += "EVE_ID_EVENTO ";
    lStatement += " FROM MOTIVAZIONE_DECRETO MOT_DECR, CG_REF_CODES TIPO_MOT ";
    lStatement += " WHERE ";

    return lStatement;
  }

  //
  // METODO GETMODEL()
  //
  public GenericModel  getModel() throws DAOException
  {
    MotivazioneDecretoModel aModel = new  MotivazioneDecretoModel();

    //Inserire le opportune set delle descrizioni!
    aModel.setIdMotivazioneDecreto(getBigDecimal("ID_MOTIVAZIONE_DECRETO") );
    aModel.setCodTipoMotivazione(getString("COD_TIPO_MOTIVAZIONE") );
    aModel.setDescrMotivazione(getString("DESCR_MOTIVAZIONE") );
    aModel.setAltraMotivazione(getString("ALTRA_MOTIVAZIONE") );
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
    //aModel.setDescrUfficioInserimento(getString("") );
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
    //aModel.setDescrUfficioAggiornamento(getString("") );
    aModel.setDepDecIdDepositoDecreto(getBigDecimal("DEP_DEC_ID_DEPOSITO_DECRETO") );
    aModel.setProgrMotivazione(getBigDecimal("PROGR_MOTIVAZIONE"));
    aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO") );
    return aModel;
  }

  /**
   * Prepara lo statement per eseguire la ricerca delle motivazioni decreto
   * <p>
   * @param aKey id deposito decreto.
   */

  public void ricercaMotivazioneDecretoByDepDec( BigDecimal aKey )
  {
    String lStatement = getSqlQueryPura();
    lStatement += " WHERE DEP_DEC_ID_DEPOSITO_DECRETO = " + aKey;
    setStatement( lStatement );
  }


  /**
   * Prepara lo statement per eseguire la ricerca delle motivazioni decreto
   * <p>
   * @param aKey id Evento.
   */

  public void ricercaMotivazioneDecretoByEve( BigDecimal aKey )
  {
    String lStatement = getSqlQueryPura();
    lStatement += " WHERE EVE_ID_EVENTO = " + aKey;
    setStatement( lStatement );
  }


  /**
   * Prepara lo statement per eseguire la ricerca delle motivazioni decreto
   * <p>
   * @param aKey id Fascicolo SIUS.
   */

  public void ricercaMotivazioneDecretoByFasSius( BigDecimal aKey )
  {
    String lStatement = getSqlQueryPura();
    lStatement += " JOIN EVENTO ON (MOTIVAZIONE_DECRETO.EVE_ID_EVENTO = EVENTO.ID_EVENTO AND EVENTO.FAS_SIU_ID_FASCICOLO_SIUS=";
    lStatement += aKey + ") ";
    lStatement += "ORDER BY DATA_INSERIMENTO DECR ";
    setStatement( lStatement );
  }



  /**
   * Prepara lo statement per eseguire la ricerca delle motivazioni decreto
   * per inammissibilita, per deposito decreto.
   * <p>
   * @param aKey id deposito decreto.
   */

  public void ricercaMotivazioneDecretoInammissibilitaByDepDec( BigDecimal aKey )
  {
    String lStatement = getSqlQuery();
    lStatement += " TIPO_MOT.RV_DOMAIN = 'MOTIVO_INAMMISSIBILITA'";
    lStatement += " AND TIPO_MOT.RV_LOW_VALUE = MOT_DECR.COD_TIPO_MOTIVAZIONE";
    lStatement += " AND DEP_DEC_ID_DEPOSITO_DECRETO = " + aKey;
    setStatement( lStatement );
  }

  /**
   * Prepara lo statement per eseguire la ricerca delle motivazioni decreto
   * per inammissibilita, per evento.
   * <p>
   * @param aKey id deposito decreto.
   */

  public void ricercaMotivazioneDecretoInammissibilitaByEve( BigDecimal aKey )
  {
    String lStatement = getSqlQuery();
    lStatement += " TIPO_MOT.RV_DOMAIN = 'MOTIVO_INAMMISSIBILITA'";
    lStatement += " AND TIPO_MOT.RV_LOW_VALUE = MOT_DECR.COD_TIPO_MOTIVAZIONE";
    lStatement += " AND EVE_ID_EVENTO = " + aKey;
    setStatement( lStatement );
  }


  /**
   * Prepara lo statement per eseguire la ricerca delle motivazioni decreto
   * per incompetenza, per deposito decreto.
   * <p>
   * @param aKey id deposito decreto.
   */
  public void ricercaMotivazioneDecretoIncompetenzaByDepDec( BigDecimal aKey )
  {
    String lStatement = getSqlQuery();
    // N. B. sostituire MOTIVO_INAMMISSIBILITA con MOTIVO_INCOMPETENZA
//    lStatement += " TIPO_MOT.RV_DOMAIN = 'MOTIVO_INCOMPETENZA'";
// Rimodificato il 17/12/03
    lStatement += " TIPO_MOT.RV_DOMAIN = 'MOTIVO_INAMMISSIBILITA'";

    lStatement += " AND TIPO_MOT.RV_LOW_VALUE = MOT_DECR.COD_TIPO_MOTIVAZIONE";
    lStatement += " AND DEP_DEC_ID_DEPOSITO_DECRETO = " + aKey;
    setStatement( lStatement );
  }
}
