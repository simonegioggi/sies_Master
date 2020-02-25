package siap.sius.motivazionedecreto.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sius.motivazionedecreto.model.MotivazioneDecretoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: MotivazioneDecretoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella MotivazioneDecreto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class MotivazioneDecretoDAO extends SIAPTableDAO
{
	public MotivazioneDecretoDAO (Connection con)
	{
    super(con);
    setTable("MOTIVAZIONE_DECRETO");
    setSequenceField("ID_MOTIVAZIONE_DECRETO", "MOT_DEC_SEQ");
    setFieldKey("ID_MOTIVAZIONE_DECRETO", BIG_DECIMAL);

    setField("ID_MOTIVAZIONE_DECRETO", BIG_DECIMAL);
    setField("COD_TIPO_MOTIVAZIONE", STRING);
    setField("DESCR_MOTIVAZIONE", STRING);
    setField("ALTRA_MOTIVAZIONE", STRING);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("DEP_DEC_ID_DEPOSITO_DECRETO", BIG_DECIMAL);
    setField("PROGR_MOTIVAZIONE", BIG_DECIMAL);
    setField("EVE_ID_EVENTO", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //
  public BigDecimal      getIdMotivazioneDecreto() 		    throws DAOException	 { return getBigDecimal("ID_MOTIVAZIONE_DECRETO"); }
  public String 				 getCodTipoMotivazione() 		      throws DAOException	 { return getString("COD_TIPO_MOTIVAZIONE"); }
  public String 				 getDescrMotivazione() 		        throws DAOException	 { return getString("DESCR_MOTIVAZIONE"); }
  public String 				 getAltraMotivazione() 		        throws DAOException	 { return getString("ALTRA_MOTIVAZIONE"); }
  public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 					 getDataInserimento() 		        throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String 				 getCodUfficioInserimento() 		  throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 				 getCodOperatoreAggiornamento() 	throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 					 getDataAggiornamento() 		      throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String 				 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal 		 getDepDecIdDepositoDecreto() 		throws DAOException	 { return getBigDecimal("DEP_DEC_ID_DEPOSITO_DECRETO"); }
  public BigDecimal      getProgrMotivazione()            throws DAOException  { return getBigDecimal("PROGR_MOTIVAZIONE"); }
  public BigDecimal 		 getEveIdEvento() 		throws DAOException	 { return getBigDecimal("EVE_ID_EVENTO"); }

  //
  // METODI SET()
  //
  public void  	 setIdMotivazioneDecreto(BigDecimal aValore )     { setBigDecimal("ID_MOTIVAZIONE_DECRETO", aValore); }
  public void  	 setCodTipoMotivazione(String aValore ) 			    { setString("COD_TIPO_MOTIVAZIONE", aValore); }
  public void  	 setDescrMotivazione(String aValore ) 			      { setString("DESCR_MOTIVAZIONE", aValore); }
  public void  	 setAltraMotivazione(String aValore ) 			      { setString("ALTRA_MOTIVAZIONE", aValore); }
  public void  	 setCodOperatoreInserimento(String aValore ) 		  { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void  	 setDataInserimento(Date aValore ) 			          { setDate("DATA_INSERIMENTO", aValore); }
  public void  	 setCodUfficioInserimento(String aValore ) 		    { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void  	 setCodOperatoreAggiornamento(String aValore ) 	  { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void  	 setDataAggiornamento(Date aValore ) 			        { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void  	 setCodUfficioAggiornamento(String aValore ) 		  { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void  	 setDepDecIdDepositoDecreto(BigDecimal aValore ) 	{ setBigDecimal("DEP_DEC_ID_DEPOSITO_DECRETO", aValore); }
  public void    setProgrMotivazione(BigDecimal aValore )         { setBigDecimal("PROGR_MOTIVAZIONE", aValore); }
  public void    setEveIdEvento(BigDecimal aValore ) 			 { setBigDecimal("EVE_ID_EVENTO", aValore); }
	/**
   * Ritorna il model corrispondente popolato di dati.
   * <p>
   * @return model popolato.
   * @throws DAOException propaga errore di eccezione.
   */
  public GenericModel getModel() throws DAOException
  {
    return new MotivazioneDecretoModel(
           getIdMotivazioneDecreto() ,
           getCodTipoMotivazione() ,
           getDescrMotivazione() ,
           getAltraMotivazione() ,
           getCodOperatoreInserimento() ,
           getDataInserimento() ,
           getCodUfficioInserimento() ,
           "",
           getCodOperatoreAggiornamento() ,
           getDataAggiornamento() ,
           getCodUfficioAggiornamento() ,
           "",
           getDepDecIdDepositoDecreto(),
           getProgrMotivazione(),
           getEveIdEvento()
          );
  }


	 /**
    * Popola il DAO con i dati contenuti nel model passato come
    * argomento.
    * <p>
    * @param aModel model con i dati da impostare nel DAO.
    * @throws DAOException propaga errore di eccezione.
    */
   public void 	setDAOFromModel(MotivazioneDecretoModel aModel) throws DAOException
   {
     setIdMotivazioneDecreto( aModel.getIdMotivazioneDecreto() );
     setCodTipoMotivazione( aModel.getCodTipoMotivazione() );
     setDescrMotivazione( aModel.getDescrMotivazione() );
     setAltraMotivazione( aModel.getAltraMotivazione() );
     setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
     setDataInserimento( aModel.getDataInserimento() );
     setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
     setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
     setDataAggiornamento( aModel.getDataAggiornamento() );
     setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
     setDepDecIdDepositoDecreto( aModel.getDepDecIdDepositoDecreto() );
     setProgrMotivazione( aModel.getProgrMotivazione() );
     setEveIdEvento( aModel.getEveIdEvento() );
  }


	 /**
    * Popola il DAO con i dati contenuti nel model passato come
    * argomento.
    * <p>
    * @param aModel model con i dati da impostare nel DAO.
    * @throws DAOException propaga errore di eccezione.
    */
   public void 	setDAOFromModelIncompetenza(MotivazioneDecretoModel aModel) throws DAOException
   {
     setCodTipoMotivazione( aModel.getCodTipoMotivazione() );
     setAltraMotivazione( aModel.getAltraMotivazione() );
     setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
     setDataInserimento( aModel.getDataInserimento() );
     setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
     setDepDecIdDepositoDecreto( aModel.getDepDecIdDepositoDecreto() );
     setProgrMotivazione( aModel.getProgrMotivazione() );
   }

    /**
     * Popola il DAO con i dati contenuti nel model passato come
     * argomento, per la fase di update.
     * <p>
     * @param aModel model con i dati da impostare nel DAO.
     * @throws DAOException propaga errore di eccezione.
     */
     public void setDAOFromModelForUpdate(MotivazioneDecretoModel aModel) throws DAOException
    {
      setIdMotivazioneDecreto( aModel.getIdMotivazioneDecreto() );
      setCodTipoMotivazione( aModel.getCodTipoMotivazione() );
      setDescrMotivazione( aModel.getDescrMotivazione() );
      setAltraMotivazione( aModel.getAltraMotivazione() );
      setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
      setDataAggiornamento( aModel.getDataAggiornamento() );
      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
      setDepDecIdDepositoDecreto( aModel.getDepDecIdDepositoDecreto() );
      setProgrMotivazione( aModel.getProgrMotivazione() );
      setEveIdEvento( aModel.getEveIdEvento() );
      setCondizioneUpdate(aModel.getIdMotivazioneDecreto());
		}

    /**
     * Imposta condizioni per l'update del record.
     * <p>
     * @param aKey id del record da updatare.
     */
	  public void setCondizioneUpdate(BigDecimal aKey)
    {
	    setCondition(" ID_MOTIVAZIONE_DECRETO = " + aKey );
    }

  /**
   * Imposta la condizione per l'id del deposito decreto.
   * <p>
   * @param aKey chiave del deposito ordinanza.
   */
  public void setCondizioneByDepDecreto( BigDecimal aKey )
  {
    setCondition( " DEP_DEC_ID_DEPOSITO_DECRETO = " + aKey );
  }

  /**
   * Imposta la condizione per l'id dell'Evento.
   * <p>
   * @param aKey chiave del Evento associato.
   */
  public void setCondizioneByEve( BigDecimal aKey )
  {
    setCondition( " EVE_ID_EVENTO = " + aKey );
  }


}
