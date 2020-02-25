package siap.sius.curatore.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.dao.SIAPTableDAO;
import siap.sius.curatore.model.CuratoreSiusModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;

/**
* <p>Title: CuratoreSiusDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella CuratoreSius</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class CuratoreSiusDAO extends SIAPTableDAO
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public CuratoreSiusDAO (Connection con)
	{
    super(con);
    setTable("CURATORE_SIUS");
    setField("DATA_INIZIO", DATE);
    setField("DATA_FINE", DATE);
    setField("FLAG_TIPO", STRING);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("CUR_ID_CURATORE", BIG_DECIMAL);
    setField("FAS_SIU_ID_FASCICOLO_SIUS", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

  public Date 					 getDataInizio() 		            throws DAOException	 { return getDate("DATA_INIZIO"); }
  public Date 					 getDataFine() 		              throws DAOException	 { return getDate("DATA_FINE"); }
  public String 				 getFlagTipo()					 		    throws DAOException	 { return getString("FLAG_TIPO"); }
  public String 				 getCodOperatoreInserimento()   throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 					 getDataInserimento() 		      throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 				 getCodOperatoreAggiornamento() throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 					 getDataAggiornamento() 		    throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String 				 getCodUfficioAggiornamento() 	throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal			 getCurIdCuratore() 		      	throws DAOException	 { return getBigDecimal("CUR_ID_CURATORE"); }
  public BigDecimal 		 getFasSiuIdFascicoloSius() 		throws DAOException	 { return getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"); }

  //
  // METODI SET()
  //
  public void  	 setDataInizio(Date aValore ) 			            { setDate("DATA_INIZIO", aValore); }
  public void  	 setDataFine(Date aValore ) 			              { setDate("DATA_FINE", aValore); }
  public void  	 setFlagTipo(String aValore )						        { setString("FLAG_TIPO", aValore); }
  public void  	 setCodOperatoreInserimento(String aValore ) 	  { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void  	 setDataInserimento(Date aValore ) 			        { setDate("DATA_INSERIMENTO", aValore); }
  public void  	 setCodUfficioInserimento(String aValore ) 		  { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void  	 setCodOperatoreAggiornamento(String aValore ) 	{ setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void  	 setDataAggiornamento(Date aValore ) 			      { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void  	 setCodUfficioAggiornamento(String aValore ) 	  { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void  	 setCurIdCuratore(BigDecimal aValore )		 			{ setBigDecimal("CUR_ID_CURATORE", aValore); }
  public void  	 setFasSiuIdFascicoloSius(BigDecimal aValore ) 	{ setBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS", aValore); }

	public GenericModel getModel() throws DAOException
  {
    return new CuratoreSiusModel(
								 getDataInizio() ,
								 getDataFine() ,
								 getFlagTipo() ,
								 "",
								 getCodOperatoreInserimento() ,
								 getDataInserimento() ,
								 getCodUfficioInserimento() ,
								 "",
								 getCodOperatoreAggiornamento() ,
								 getDataAggiornamento() ,
								 getCodUfficioAggiornamento() ,
								 "",
								 getCurIdCuratore() ,
								 getFasSiuIdFascicoloSius()
								);
		}


  /**
   * Imposta i dati del model nel dao.
   * <p>
   * @param aModel dati da impostare nel dao.
   * @throws DAOException propaga errore di eccezione.
   */
  public void setDAOFromModel(CuratoreSiusModel aModel) throws DAOException
  {
  	if (aModel.getDataInizio()!=null)
    	setDataInizio( aModel.getDataInizio() );
  	if (aModel.getDataFine()!=null)
      setDataFine( aModel.getDataFine() );
  	if (aModel.getFlagTipo()!=null)
      setFlagTipo( aModel.getFlagTipo() );
  	if (aModel.getCodOperatoreInserimento()!=null)
      setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
  	if (aModel.getDataInserimento()!=null)
      setDataInserimento( aModel.getDataInserimento() );
  	if (aModel.getCodUfficioInserimento()!=null)
      setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
  	if (aModel.getCodOperatoreAggiornamento()!=null)
      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
  	if (aModel.getDataAggiornamento()!=null)
      setDataAggiornamento( aModel.getDataAggiornamento() );
  	if (aModel.getCodUfficioAggiornamento()!=null)
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
  	if (aModel.getCurIdCuratore()!=null)
      setCurIdCuratore( aModel.getCurIdCuratore() );
  	if (aModel.getFasSiuIdFascicoloSius()!=null)
      setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );
  }

  /**
   * Imposta i dati del model nel dao, per update.
   * <p>
   * @param aModel dati da impostare nel dao.
   * @throws DAOException propaga errore di eccezione.
   */
  public void setDAOFromModelForUpdate(CuratoreSiusModel aModel) throws DAOException
  {
     setDataInizio( aModel.getDataInizio() );
     setDataFine( aModel.getDataFine() );
     setFlagTipo( aModel.getFlagTipo() );
     setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
     setDataAggiornamento( aModel.getDataAggiornamento() );
     setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
     setCurIdCuratore( aModel.getCurIdCuratore() );
     setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );

     // Imposta condizione di update.
     setCondizioneUpdate(aModel.getCurIdCuratore(),aModel.getFasSiuIdFascicoloSius() );
  }

  /**
   * Imposta i dati del model nel dao, per update (apertura).
   * <p>
   * @param aModel dati da impostare nel dao.
   * @throws DAOException propaga errore di eccezione.
   */
  public void setDAOFromModelForApertura(CuratoreSiusModel aModel) throws DAOException
  {
     setDataInizio( aModel.getDataInizio() );
     setDataFine( aModel.getDataFine() );
     setFlagTipo( aModel.getFlagTipo() );
     setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
     setDataAggiornamento( aModel.getDataAggiornamento() );
     setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
     setCurIdCuratore( aModel.getCurIdCuratore() );
     setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );

     // Imposta condizione di update.
     setCondizioneApertura(aModel.getCurIdCuratore(),aModel.getFasSiuIdFascicoloSius() );
  }
  
	/**
   * Imposta condizione SQL per fase di update record.
   * <p>
   * @param aIdFascicolo l'id del fascicolo SIUS.
   * @param aIdCur Id Curatore.
   */
  public void setCondizioneUpdate( BigDecimal aIdCur, BigDecimal aIdFascicolo )
  {
	  String lSql = new String("");
    lSql += " FAS_SIU_ID_FASCICOLO_SIUS = '" + aIdFascicolo +"'" ;
    lSql += " AND DATA_FINE IS null " ;
    lSql += " AND (CUR_ID_CURATORE = '" + aIdCur + "')" ;

    setCondition( lSql );
  }

	/**
   * Imposta condizione SQL per fase di ripristino con update record.
   * <p>
   * @param aIdFascicolo l'id del fascicolo SIUS.
   * @param aIdCur Id Curatore.
   */
  public void setCondizioneApertura( BigDecimal aIdCur, BigDecimal aIdFascicolo )
  {
	  String lSql = new String("");
    lSql += " FAS_SIU_ID_FASCICOLO_SIUS = '" + aIdFascicolo +"'" ;
    lSql += " AND DATA_FINE IS not null " ;
    lSql += " AND (CUR_ID_CURATORE = '" + aIdCur + "')" ;

    setCondition( lSql );
  }
  
  /**
   * Imposta la condizione di Update sul curatore correntemente assegnatario
   * del fascicolo passato in input.
   * n.b. il curatore corrente del fascicolo è quello senza data
   *      fine
   * @param idFascicoloSius - id del fascicolo
   */
  public void setCondizioneUpdateCuratoreSiusCorrente(BigDecimal idFascicoloSius )
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

	/**
   * Imposta condizione SQL per individuare il curatore attivo per un fascicolo.
   * <p>
   * @param aIdFascicolo l'id del fascicolo SIUS.
   */
  public void setCondizioneAttivo(BigDecimal aIdFascicolo)
  {
	String lSql = new String("");
    lSql += " FAS_SIU_ID_FASCICOLO_SIUS = " + aIdFascicolo ;
    lSql += " AND DATA_FINE IS null " ;

    setCondition( lSql );
  }
  
	/**
	 * Prepara lo statement per chiudere il curatore attuale.
	 * @param aModel
	 * @throws DAOException
	 */
	 public void setDAOFromModelForChiusura(CuratoreSiusModel aModel) throws DAOException
		{
				 setDataFine( aModel.getDataFine() );  
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
				 setDataAggiornamento( aModel.getDataAggiornamento() );  
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
				 setCondizioneAttivo(aModel.getFasSiuIdFascicoloSius());
		}
  
}