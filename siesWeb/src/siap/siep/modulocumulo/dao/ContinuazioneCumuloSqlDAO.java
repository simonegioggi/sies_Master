package siap.siep.modulocumulo.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.siep.modulocumulo.model.ContinuazioneCumuloModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: ContinuazioneCumuloSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella ContinuazioneCumulo
 * </p>
 * <p>
 * in ambito Cumulo (Continuazione_Cumulo)
 * </p>
*/
public class ContinuazioneCumuloSqlDAO extends SIAPSqlDAO {

	public ContinuazioneCumuloSqlDAO(Connection con) {
    super(con);
  }

  //
  // METODO RICERCA()
  //

	public void ricercaContinuazione(ContinuazioneCumuloModel aModel) throws DAOException {
    String lSql = getSqlQuery();

    lSql += " " + setCondizione(aModel);
    lSql += " " + setOrderByProgressivo();

    setStatement(lSql);
  }

	public void ricercaContinuazioneByKey(BigDecimal aKey) throws DAOException {
    String lSql = getSqlQuery();

    lSql += " " + setCondizioniByKey(aKey);

    setStatement(lSql);
  }

	public void ricercaContinuazioneByIdPenaComplessivaCum(BigDecimal aKey) throws DAOException {
    String lSql = getSqlQuery();

    lSql += " " + setCondizioniByIdPenaComplessiva(aKey);
    lSql += " " + setOrderByProgressivo();

    setStatement(lSql);
  }

	public void ricercaContinuazioneByIdTitolo(BigDecimal aIdTitolo) throws DAOException {
    String lSql = getSqlQuery();

    lSql += " AND TIT_ID_TITOLO_CUMULATO = " + aIdTitolo;
    lSql += " " + setOrderByProgressivo();

    setStatement(lSql);
  }
  
  public void ricercaContinuazioneByIdTitoloCont (BigDecimal aIdTitolo)   throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " AND TIT_ID_TITOLO_CUMULATO_CONT = " + aIdTitolo;
    lSql += " " + setOrderByProgressivo();

    setStatement(lSql);
  }
  
  public void ricercaContinuazioneByIdIstruttoria (BigDecimal aIdIstruttoria)   throws DAOException
  {
    String lSql = getSqlQueryByIdIstruttoria (aIdIstruttoria);

    lSql += " " + setOrderByProgressivo();

    setStatement(lSql);
  }
  
	protected String getSqlQuery() {
    String lStatement = new String("");

		lStatement += " SELECT " + "ID_CONTINUAZIONE_CUM, " + "PROGR_CONTINUAZIONE, "
				+ "COD_TIPO_CONTINUAZIONE, TIPO_CONTINUAZIONE.RV_MEANING DESCR_TIPO_CONT, "
				+ "COD_TIPO_AUTORITA, TIPO_AUTORITA.RV_MEANING DESCR_TIPO_AUT, "
				+ "COD_LUOGO_AUTORITA, LUOGO_AUT.DESCRIZIONE DESCR_LUOGO_AUT, " + "DATA_SENTENZA, "
				+ "ANNO_SENTENZA, " + "NUM_SENTENZA, " +
                  
				"ANNO_REGE_PM, " + "NUM_REGE_PM, " +
                  
				"ANNO_REG_GEN, " + "NUMERO_REG_GEN, " + "TIPO_REG_GEN, " +
                 
				"PC_ID_PENA_COMPLESSIVA_CUM, " + "TIT_ID_TITOLO_CUMULATO_CONT, " +
                  
				"FLAG_STATO, " + "MOTIVO_MODIFICA, " + "TIT_ID_TITOLO_CUMULATO, "
				+ "ID_CONTINUAZIONE_ORIGINE, " +
    
				"COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO ";
    
		lStatement += " FROM CONTINUAZIONE_CUMULO LEFT OUTER JOIN CG_REF_CODES TIPO_CONTINUAZIONE "
				+ " ON TIPO_CONTINUAZIONE.RV_DOMAIN = 'TIPO_CONTINUAZIONE' "
				+ " AND TIPO_CONTINUAZIONE.RV_LOW_VALUE = CONTINUAZIONE_CUMULO.COD_TIPO_CONTINUAZIONE ";
		lStatement += " LEFT OUTER JOIN CG_REF_CODES TIPO_AUTORITA "
				+ " ON TIPO_AUTORITA.RV_DOMAIN = 'TIPO_UFFICIO' "
				+ " AND TIPO_AUTORITA.RV_LOW_VALUE = CONTINUAZIONE_CUMULO.COD_TIPO_AUTORITA";
		lStatement += " LEFT OUTER JOIN COMUNE LUOGO_AUT "
				+ " ON LUOGO_AUT.COD_COMUNE = CONTINUAZIONE_CUMULO.COD_LUOGO_AUTORITA ";

    lStatement += " WHERE 1=1 ";
    
    return lStatement;
  }

  
  /**
   * 
   * @return
   */
  protected String getSqlQueryByIdIstruttoria(BigDecimal aIdIstruttoria)
  {
    String lStatement = new String("");

    lStatement += " SELECT " +
                  "ID_CONTINUAZIONE_CUM, "+
                  "PROGR_CONTINUAZIONE, "+
                  "COD_TIPO_CONTINUAZIONE, TIPO_CONTINUAZIONE.RV_MEANING DESCR_TIPO_CONT, "+
                  "COD_TIPO_AUTORITA, TIPO_AUTORITA.RV_MEANING DESCR_TIPO_AUT, "+
                  "COD_LUOGO_AUTORITA, LUOGO_AUT.DESCRIZIONE DESCR_LUOGO_AUT, "+
                  "CONTINUAZIONE_CUMULO.DATA_SENTENZA, "+
                  "CONTINUAZIONE_CUMULO.ANNO_SENTENZA, "+
                  "CONTINUAZIONE_CUMULO.NUM_SENTENZA, "+
                  
                  "CONTINUAZIONE_CUMULO.ANNO_REGE_PM, "+
                  "CONTINUAZIONE_CUMULO.NUM_REGE_PM, "+
                  
                  "CONTINUAZIONE_CUMULO.ANNO_REG_GEN, "+
                  "CONTINUAZIONE_CUMULO.NUMERO_REG_GEN, "+
                  "CONTINUAZIONE_CUMULO.TIPO_REG_GEN, "+
                 
                  "PC_ID_PENA_COMPLESSIVA_CUM, "+
                  "TIT_ID_TITOLO_CUMULATO_CONT, "+
                  
                  "CONTINUAZIONE_CUMULO.FLAG_STATO, "+
                  "CONTINUAZIONE_CUMULO.MOTIVO_MODIFICA, "+
                  "TIT_ID_TITOLO_CUMULATO, "+
                  "ID_CONTINUAZIONE_ORIGINE, "+
    
                  "CONTINUAZIONE_CUMULO.COD_OPERATORE_INSERIMENTO, "+
                  "CONTINUAZIONE_CUMULO.DATA_INSERIMENTO, "+
                  "CONTINUAZIONE_CUMULO.COD_UFFICIO_INSERIMENTO, "+
                  "CONTINUAZIONE_CUMULO.COD_OPERATORE_AGGIORNAMENTO, "+
                  "CONTINUAZIONE_CUMULO.DATA_AGGIORNAMENTO, "+
                  "CONTINUAZIONE_CUMULO.COD_UFFICIO_AGGIORNAMENTO ";
    
    lStatement += " FROM CONTINUAZIONE_CUMULO LEFT OUTER JOIN CG_REF_CODES TIPO_CONTINUAZIONE " +
                                         " ON TIPO_CONTINUAZIONE.RV_DOMAIN = 'TIPO_CONTINUAZIONE' "+
                                        " AND TIPO_CONTINUAZIONE.RV_LOW_VALUE = CONTINUAZIONE_CUMULO.COD_TIPO_CONTINUAZIONE ";
    lStatement +=                           " LEFT OUTER JOIN CG_REF_CODES TIPO_AUTORITA " +
                                         " ON TIPO_AUTORITA.RV_DOMAIN = 'TIPO_UFFICIO' "+
                                        " AND TIPO_AUTORITA.RV_LOW_VALUE = CONTINUAZIONE_CUMULO.COD_TIPO_AUTORITA";
    lStatement +=                           " LEFT OUTER JOIN COMUNE LUOGO_AUT " +
                                         " ON LUOGO_AUT.COD_COMUNE = CONTINUAZIONE_CUMULO.COD_LUOGO_AUTORITA ";
    lStatement += ", TITOLO_CUMULATO ";
    lStatement += " WHERE 1=1 ";
    lStatement +=   " AND CONTINUAZIONE_CUMULO.TIT_ID_TITOLO_CUMULATO = TITOLO_CUMULATO.ID_TITOLO_CUMULATO ";
    lStatement +=   " AND TITOLO_CUMULATO.ISTR_ID_ISTRUTTORIA_CUMULO = "+aIdIstruttoria;
    
    return lStatement;
  }

  //
  // METODO GETMODEL()
  //

	public GenericModel getModel() throws DAOException {
    ContinuazioneCumuloModel aModel = new ContinuazioneCumuloModel();

    //Inserire le opportune set delle descrizioni!
    aModel.setIdContinuazioneCum (getBigDecimal("ID_CONTINUAZIONE_CUM") );
    aModel.setProgrContinuazione (getBigDecimal("PROGR_CONTINUAZIONE") );
    aModel.setCodTipoContinuazione   (getString("COD_TIPO_CONTINUAZIONE") );
    aModel.setDescrTipoContinuazione (getString("DESCR_TIPO_CONT") );
    
    aModel.setCodTipoAutorita    (getString("COD_TIPO_AUTORITA") );
    aModel.setDescrTipoAutorita  (getString("DESCR_TIPO_AUT") );
    aModel.setCodLuogoAutorita   (getString("COD_LUOGO_AUTORITA") );
    aModel.setDescrLuogoAutorita (getString("DESCR_LUOGO_AUT") );
    
    aModel.setDataSentenza  (getDate("DATA_SENTENZA") );
    aModel.setAnnoSentenza  (getBigDecimal("ANNO_SENTENZA") );
    aModel.setNumSentenza   (getString("NUM_SENTENZA") );
    
    aModel.setAnnoRegePm (getBigDecimal("ANNO_REGE_PM") );
    aModel.setNumRegePm  (getString("NUM_REGE_PM") );
    
    aModel.setAnnoRegGen   (getBigDecimal("ANNO_REG_GEN") );
    aModel.setNumeroRegGen (getString("NUMERO_REG_GEN") );
    aModel.setTipoRegGen   (getString("TIPO_REG_GEN") );
    
    aModel.setPcIdPenaComplessivaCum  (getBigDecimal("PC_ID_PENA_COMPLESSIVA_CUM") );
    aModel.setTitIdTitoloCumulatoCont (getBigDecimal("TIT_ID_TITOLO_CUMULATO_CONT") );
    
    aModel.setFlagStato              (getString("FLAG_STATO") );
    aModel.setMotivoModifica         (getString("MOTIVO_MODIFICA") );
    aModel.setTitIdTitoloCumulato    (getBigDecimal("TIT_ID_TITOLO_CUMULATO") );    
    aModel.setIdContinuazioneOrigine (getBigDecimal("ID_CONTINUAZIONE_ORIGINE") );    

    aModel.setCodOperatoreInserimento   (getString("COD_OPERATORE_INSERIMENTO") );
    aModel.setDataInserimento           (getDate("DATA_INSERIMENTO") );
    aModel.setCodUfficioInserimento     (getString("COD_UFFICIO_INSERIMENTO") );
    aModel.setCodOperatoreAggiornamento (getString("COD_OPERATORE_AGGIORNAMENTO") );
    aModel.setDataAggiornamento         (getDate("DATA_AGGIORNAMENTO") );
    aModel.setCodUfficioAggiornamento   (getString("COD_UFFICIO_AGGIORNAMENTO") );

    return aModel;
  }

	public String setCondizione(ContinuazioneCumuloModel aModel) {
    String lCondizioni = new String();

		// boolean lInserito = false;

    return lCondizioni;
  }

	public String setCondizioniByKey(BigDecimal aKey) {
    return " AND ID_CONTINUAZIONE_CUM = " + aKey;
  }

	public String setCondizioniByIdPenaComplessiva(BigDecimal aKeyPena) {
    return " AND PC_ID_PENA_COMPLESSIVA_CUM = " + aKeyPena;
  }

	public String setOrderByProgressivo() {
    return " ORDER BY PROGR_CONTINUAZIONE";
  }

	public BigDecimal getProgressivoContinuazione(BigDecimal aKeyPenaCompl) throws DAOException {
    String lStatement = new String();

    lStatement += "SELECT MAX(PROGR_CONTINUAZIONE) aMAX";
    lStatement += " FROM CONTINUAZIONE_CUMULO";
    lStatement += " WHERE PC_ID_PENA_COMPLESSIVA_CUM = " + aKeyPenaCompl;

    setStatement(lStatement);

    this.start();

    BigDecimal lProgressivo = null;
    if (this.next() && (this.getBigDecimal("aMAX") != null) )
      lProgressivo = this.getBigDecimal("aMAX");

    this.stop();

    if (lProgressivo == null)
      lProgressivo = new BigDecimal(0);

    return lProgressivo;
  }

}