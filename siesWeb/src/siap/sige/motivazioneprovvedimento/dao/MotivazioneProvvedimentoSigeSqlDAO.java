package siap.sige.motivazioneprovvedimento.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sige.motivazioneprovvedimento.model.MotivazioneProvvedimentoSigeModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: MotivazioneProvvedimentoSigeSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella MOTIVAZIONE_PROVVED_SIGE</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: </p>
* @version 1.0
*/

public class MotivazioneProvvedimentoSigeSqlDAO extends SIAPSqlDAO
{
  /**
   * Costruttore di classe con la connessione come argomento.
   * <p>
   * @param con connessione al Dbase.
   */
  public MotivazioneProvvedimentoSigeSqlDAO (Connection con)
  {
    super(con);
  }

  //
  // METODO RICERCA()
  //

  /**
  * Ritorna la select SQL per estrazione dati dalla tabella Motivazione_Provved_Sige
  * senza join e senza traduzioni.
  * <p>
  * @return select sql.
  */
  protected String getSqlQueryPura()
  {
    String lStatement = new String("");
    lStatement += " SELECT ID_MOTIVAZIONE_PROVVED_SIGE, ";
    lStatement +=	" COD_TIPO_MOTIVAZIONE, ";
    lStatement +=	" DESCR_MOTIVAZIONE, ";
    lStatement +=	" ALTRA_MOTIVAZIONE, ";
    lStatement +=	" COD_OPERATORE_INSERIMENTO, ";
    lStatement +=	" DATA_INSERIMENTO, ";
    lStatement +=	" COD_UFFICIO_INSERIMENTO, ";
    lStatement +=	" COD_OPERATORE_AGGIORNAMENTO, ";
    lStatement +=	" DATA_AGGIORNAMENTO, ";
    lStatement +=	" COD_UFFICIO_AGGIORNAMENTO, ";
    lStatement +=	" PRO_SIG_ID_PROVVED_SIGE, ";
    lStatement += " PROGR_MOTIVAZIONE, ";
    lStatement += "EVE_ID_EVENTO ";
    lStatement += " FROM MOTIVAZIONE_PROVVED_SIGE ";

    return lStatement;
  }

  /**
  * Ritorna la select SQL per estrazione dati dalla tabella MOTIVAZIONE_PROVVED_SIGE
  * <p>
  * @return select sql.
  */
  protected String getSqlQuery()
  {
    String lStatement = new String("");
    lStatement += " SELECT ID_MOTIVAZIONE_PROVVED_SIGE, ";
    lStatement +=	" COD_TIPO_MOTIVAZIONE, ";
    lStatement += "REPLACE( REPLACE( TIPO_MOT.RV_MEANING, '<?>', MOT_PROVVED.DESCR_MOTIVAZIONE ), '<?1>', MOT_PROVVED.ALTRA_MOTIVAZIONE) DESCR_MOTIVAZIONE,";
    //lStatement +=	" DESCR_MOTIVAZIONE, ";
    lStatement +=	" ALTRA_MOTIVAZIONE, ";
    lStatement +=	" COD_OPERATORE_INSERIMENTO, ";
    lStatement +=	" DATA_INSERIMENTO, ";
    lStatement +=	" COD_UFFICIO_INSERIMENTO, ";
    lStatement +=	" COD_OPERATORE_AGGIORNAMENTO, ";
    lStatement +=	" DATA_AGGIORNAMENTO, ";
    lStatement +=	" COD_UFFICIO_AGGIORNAMENTO, ";
    lStatement +=	" PRO_SIG_ID_PROVVED_SIGE, ";
    lStatement += " PROGR_MOTIVAZIONE, ";
    lStatement += " EVE_ID_EVENTO ";
    lStatement += " FROM MOTIVAZIONE_PROVVED_SIGE MOT_PROVVED, CG_REF_CODES TIPO_MOT ";
    lStatement += " WHERE ";

    return lStatement;
  }

  //
  // METODO GETMODEL()
  //
  public GenericModel  getModel() throws DAOException
  {
    MotivazioneProvvedimentoSigeModel aModel = new  MotivazioneProvvedimentoSigeModel();

    //Inserire le opportune set delle descrizioni!
    aModel.setIdMotivazioneProvvedSige(getBigDecimal("ID_MOTIVAZIONE_PROVVED_SIGE") );
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
    aModel.setProSigIdProvvedSige(getBigDecimal("PRO_SIG_ID_PROVVED_SIGE") );
    aModel.setProgrMotivazione(getBigDecimal("PROGR_MOTIVAZIONE"));
    aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO") );
    return aModel;
  }

  /**
   * Prepara lo statement per eseguire la ricerca delle motivazioni provvedimento
   * <p>
   * @param aKey id provvedimento Sige.
   */

  public void ricercaMotivazioneProvvedSigeByIdProvv( BigDecimal aKey )
  {
    String lStatement = getSqlQueryPura();
    lStatement += " WHERE PRO_SIG_ID_PROVVED_SIGE = " + aKey;
    setStatement( lStatement );
  }


  /**
   * Prepara lo statement per eseguire la ricerca delle motivazioni provvedimento Sige
   * <p>
   * @param aKey id Evento.
   */

  public void ricercaMotivazioneProvvedSigeByEve( BigDecimal aKey )
  {
    String lStatement = getSqlQueryPura();
    lStatement += " WHERE EVE_ID_EVENTO = " + aKey;
    setStatement( lStatement );
  }

  /**
   * Prepara lo statement per eseguire la ricerca delle motivazioni decreto
   * per inammissibilita, per deposito decreto.
   * <p>
   * @param aKey id deposito decreto.
   */

  public void ricercaMotivazioneDecretoInammissibilitaByIdProvSige( BigDecimal aKey )
  {
    String lStatement = getSqlQuery();
    lStatement += " TIPO_MOT.RV_DOMAIN = 'MOTIVO_INAMMISSIBILITA_SIGE'";
    lStatement += " AND TIPO_MOT.RV_LOW_VALUE = MOT_PROVVED.COD_TIPO_MOTIVAZIONE";
    lStatement += " AND PRO_SIG_ID_PROVVED_SIGE = " + aKey;
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
    lStatement += " TIPO_MOT.RV_DOMAIN = 'MOTIVO_INAMMISSIBILITA_SIGE'";
    lStatement += " AND TIPO_MOT.RV_LOW_VALUE = MOT_PROVVED.COD_TIPO_MOTIVAZIONE";
    lStatement += " AND EVE_ID_EVENTO = " + aKey;
    setStatement( lStatement );
  }

  /**
   * Prepara lo statement per eseguire la ricerca delle motivazioni decreto
   * per incompetenza, per deposito decreto.
   * <p>
   * @param aKey id deposito decreto.
   */
  public void ricercaMotivazioneDecretoIncompetenzaByIdProv( BigDecimal aKey )
  {
    String lStatement = getSqlQuery();
    // N. B. sostituire MOTIVO_INAMMISSIBILITA con MOTIVO_INCOMPETENZA
    //    lStatement += " TIPO_MOT.RV_DOMAIN = 'MOTIVO_INCOMPETENZA'";
    lStatement += " TIPO_MOT.RV_DOMAIN = 'MOTIVO_INAMMISSIBILITA_SIGE'";

    lStatement += " AND TIPO_MOT.RV_LOW_VALUE = MOT_PROVVED.COD_TIPO_MOTIVAZIONE";
    lStatement += " AND PRO_SIG_ID_PROVVED_SIGE = " + aKey;
    setStatement( lStatement );
  }
  /**
   * Prepara lo statement per eseguire la ricerca delle motivazioni del provvedimento.
   * <p>
   * @param aKey id Provvedimento SIGE.
   */
  public void ricercaMotivazioneProvvedimentoByIdProvSige( BigDecimal aKey )
  {
    String lStatement = getSqlQuery();
    lStatement += " TIPO_MOT.RV_LOW_VALUE = MOT_PROVVED.COD_TIPO_MOTIVAZIONE";
    lStatement += " AND PRO_SIG_ID_PROVVED_SIGE = " + aKey;
    setStatement( lStatement );
  }
}
