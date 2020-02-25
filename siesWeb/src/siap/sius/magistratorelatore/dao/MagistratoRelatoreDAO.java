package siap.sius.magistratorelatore.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.dao.SIAPTableDAO;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;

/**
* <p>Title: MagistratoRelatoreDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella MagistratoRelatore</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class MagistratoRelatoreDAO extends SIAPTableDAO
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public MagistratoRelatoreDAO (Connection con)
	{
    super(con);
    setTable("MAGISTRATO_RELATORE");
    setField("DATA_INIZIO", DATE);
    setField("DATA_FINE", DATE);
    setField("COD_RUOLO_MAGISTRATO", STRING);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("MAG_COD_MAGISTRATO", STRING);
    setField("FAS_SIU_ID_FASCICOLO_SIUS", BIG_DECIMAL);
    setField("ESP_ID_ESPERTO", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

  public Date 					 getDataInizio() 		            throws DAOException	 { return getDate("DATA_INIZIO"); }
  public Date 					 getDataFine() 		              throws DAOException	 { return getDate("DATA_FINE"); }
  public String 				 getCodRuoloMagistrato() 		    throws DAOException	 { return getString("COD_RUOLO_MAGISTRATO"); }
  public String 				 getCodOperatoreInserimento()   throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 					 getDataInserimento() 		      throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 				 getCodOperatoreAggiornamento() throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 					 getDataAggiornamento() 		    throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String 				 getCodUfficioAggiornamento() 	throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public String 				 getMagCodMagistrato() 		      throws DAOException	 { return getString("MAG_COD_MAGISTRATO"); }
  public BigDecimal 		 getFasSiuIdFascicoloSius() 		throws DAOException	 { return getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"); }
  public BigDecimal 		 getEspIdEsperto() 		          throws DAOException	 { return getBigDecimal("ESP_ID_ESPERTO"); }


  //
  // METODI SET()
  //
  public void  	 setDataInizio(Date aValore ) 			            { setDate("DATA_INIZIO", aValore); }
  public void  	 setDataFine(Date aValore ) 			              { setDate("DATA_FINE", aValore); }
  public void  	 setCodRuoloMagistrato(String aValore )         { setString("COD_RUOLO_MAGISTRATO", aValore); }
  public void  	 setCodOperatoreInserimento(String aValore ) 	  { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void  	 setDataInserimento(Date aValore ) 			        { setDate("DATA_INSERIMENTO", aValore); }
  public void  	 setCodUfficioInserimento(String aValore ) 		  { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void  	 setCodOperatoreAggiornamento(String aValore ) 	{ setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void  	 setDataAggiornamento(Date aValore ) 			      { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void  	 setCodUfficioAggiornamento(String aValore ) 	  { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void  	 setMagCodMagistrato(String aValore ) 			    { setString("MAG_COD_MAGISTRATO", aValore); }
  public void  	 setFasSiuIdFascicoloSius(BigDecimal aValore ) 	{ setBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS", aValore); }
  public void  	 setEspIdEsperto(BigDecimal aValore ) 			    { setBigDecimal("ESP_ID_ESPERTO", aValore); }


	public GenericModel getModel() throws DAOException
  {
    return new MagistratoRelatoreModel(
								 getDataInizio() ,
								 getDataFine() ,
								 getCodRuoloMagistrato() ,
								 "",
								 getCodOperatoreInserimento() ,
								 getDataInserimento() ,
								 getCodUfficioInserimento() ,
								 "",
								 getCodOperatoreAggiornamento() ,
								 getDataAggiornamento() ,
								 getCodUfficioAggiornamento() ,
								 "",
								 getMagCodMagistrato() ,
								 getFasSiuIdFascicoloSius() ,
								 getEspIdEsperto()
								);
		}


  /**
   * Imposta i dati del model nel dao.
   * <p>
   * @param aModel dati da impostare nel dao.
   * @throws DAOException propaga errore di eccezione.
   */
  public void setDAOFromModel(MagistratoRelatoreModel aModel) throws DAOException
  {
    setDataInizio( aModel.getDataInizio() );
    setDataFine( aModel.getDataFine() );
    setCodRuoloMagistrato( aModel.getCodRuoloMagistrato() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setMagCodMagistrato( aModel.getMagCodMagistrato() );
    setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );
    setEspIdEsperto( aModel.getEspIdEsperto() );
  }

  /**
   * Imposta i dati del model nel dao, per update.
   * <p>
   * @param aModel dati da impostare nel dao.
   * @throws DAOException propaga errore di eccezione.
   */
  public void setDAOFromModelForUpdate(MagistratoRelatoreModel aModel) throws DAOException
  {
     setDataInizio( aModel.getDataInizio() );
     setDataFine( aModel.getDataFine() );
     setCodRuoloMagistrato( aModel.getCodRuoloMagistrato() );
     setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
     setDataAggiornamento( aModel.getDataAggiornamento() );
     setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
     setMagCodMagistrato( aModel.getMagCodMagistrato() );
     setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );
     setEspIdEsperto( aModel.getEspIdEsperto() );
     // Imposta condizione di update.
     setCondizioneUpdate(aModel.getFasSiuIdFascicoloSius(),
                         aModel.getMagCodMagistrato(),
                         aModel.getEspIdEsperto() );
  }

	/**
   * Imposta condizione SQL per fase di update record.
   * <p>
   * @param aIdFascicolo l'id del fascicolo SIUS.
   * @param aCodMag codice del magistrato.
   */
  public void setCondizioneUpdate(BigDecimal aIdFascicolo, String aCodMag, BigDecimal aIdEsperto)
  {
	  String lSql = new String("");
    lSql += " FAS_SIU_ID_FASCICOLO_SIUS = " + aIdFascicolo ;
    lSql += " AND DATA_FINE IS null " ;
    lSql += " AND (MAG_COD_MAGISTRATO = '" + aCodMag + "' OR ESP_ID_ESPERTO = " + aIdEsperto + ")";

    setCondition( lSql );
  }
  
  /**
   * Imposta la condizione di Update sul magistrato correntemente assegnatario
   * del fascicolo passato in input.
   * n.b. il magistrato correntemente relatore del fascicolo è quello senza data
   *      fine
   * @param idFascicoloSius - id del fascicolo
   */
  public void setCondizioneUpdateMagistratoSorveglianzaCorrente(BigDecimal idFascicoloSius )
  {
    String lCondizioni = null;

    lCondizioni = " FAS_SIU_ID_FASCICOLO_SIUS = " + idFascicoloSius ;
    lCondizioni += " AND DATA_FINE is null ";
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lCondizioni = "+lCondizioni);
    
    setCondition(lCondizioni);
  }
	/**
   * Imposta condizione SQL per fase di update/delete record per IdFascicoloSius.
   * <p>
   * @param aIdFascicolo l'id del fascicolo SIUS.
   */

  public void setCondizionePerFascicoloSius(BigDecimal aIdFascicolo)
  {
	  String lSql = new String("");
    lSql += " FAS_SIU_ID_FASCICOLO_SIUS = " + aIdFascicolo ;

    setCondition( lSql );
  }

}