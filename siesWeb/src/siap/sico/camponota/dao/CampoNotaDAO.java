package siap.sico.camponota.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sico.camponota.model.CampoNotaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: CampoNotaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella CampoNota</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class CampoNotaDAO extends SIAPTableDAO
{
	/**
   * Costruttore di classe con argomento.
   * <p>
   * @param aConn connessione al dbase.
   */
  public CampoNotaDAO (Connection aConn)
	{
    super(aConn);
    setTable("CAMPO_NOTA");
    setSequenceField("ID_CAMPO_NOTA", "CAM_NOT_SEQ");
    setFieldKey("ID_CAMPO_NOTA", BIG_DECIMAL);
    setField("ID_CAMPO_NOTA", BIG_DECIMAL);
    setField("PROGRESSIVO", BIG_DECIMAL);
    setField("DESCR", STRING);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("EVE_ID_EVENTO", BIG_DECIMAL);
    setField("OGGETTO_NOTA_RES", STRING);  // STUB 31/01/2006
    setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);  // STUB 31/01/2006
	}


  //
  // METODI GET()
  //

  public BigDecimal 		 getIdCampoNota() 		          throws DAOException	 { return getBigDecimal("ID_CAMPO_NOTA"); }
  public BigDecimal 		 getProgressivo() 		          throws DAOException	 { return getBigDecimal("PROGRESSIVO"); }
  public String 				 getDescr() 		                throws DAOException	 { return getString("DESCR"); }
  public String 				 getCodOperatoreInserimento()   throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 					 getDataInserimento() 		      throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String 				 getCodUfficioInserimento() 	  throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 				 getCodOperatoreAggiornamento() throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 					 getDataAggiornamento() 		    throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String 				 getCodUfficioAggiornamento()   throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal 		 getEveIdEvento() 		          throws DAOException	 { return getBigDecimal("EVE_ID_EVENTO"); }
  public String 				 getOggettoNotaRes()            throws DAOException	 { return getString("OGGETTO_NOTA_RES"); }  // STUB 31/01/2006
  public BigDecimal 		 getFasSieIdFascicoloSiep()     throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }  // STUB 31/01/2006

  //
  // METODI SET()
  //

  public void  	 setIdCampoNota(BigDecimal aValore ) 			 { setBigDecimal("ID_CAMPO_NOTA", aValore); }
  public void  	 setProgressivo(BigDecimal aValore ) 			 { setBigDecimal("PROGRESSIVO", aValore); }
  public void  	 setDescr(String aValore ) 			 { setString("DESCR", aValore); }
  public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
  public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void  	 setEveIdEvento(BigDecimal aValore ) 			         { setBigDecimal("EVE_ID_EVENTO", aValore); }
  public void  	 setOggettoNotaRes(String aValore ) 			 { setString("OGGETTO_NOTA_RES", aValore); }  // STUB 31/01/2006
  public void  	 setFasSieIdFascicoloSiep(BigDecimal aValore ) 		 { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore);}   // STUB 31/01/2006}

  /**
   * Ritorna il model popoalato con il dati prelevati dal Dbase.
   * <p>
   * @return model <code>GenericModel</code> popolato.
   * @throws DAOException propaga l'errore di eccezione.
   */
	public GenericModel getModel() throws DAOException
  {
    return new CampoNotaModel(
      getIdCampoNota() ,
      getProgressivo() ,
      getDescr() ,
      getCodOperatoreInserimento() ,
      getDataInserimento() ,
      getCodUfficioInserimento() ,
      "",
      getCodOperatoreAggiornamento() ,
      getDataAggiornamento() ,
      getCodUfficioAggiornamento() ,
      "",
      getEveIdEvento() ,
      getOggettoNotaRes() ,
      getFasSieIdFascicoloSiep());
  }

  /**
   * Imposta i dati del model con quelli passati come argomento.
   * <p>
   * @param aModel model con i dati utili per impostare il model stesso.
   * @throws DAOException propage l'errore di eccezione.
   */
  public void setDAOFromModel(CampoNotaModel aModel) throws DAOException
  {
     setIdCampoNota( aModel.getIdCampoNota() );
     setProgressivo( aModel.getProgressivo() );
     setDescr( aModel.getDescr() );
     setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
     setDataInserimento( aModel.getDataInserimento() );
     setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
     setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
     setDataAggiornamento( aModel.getDataAggiornamento() );
     setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
     setEveIdEvento( aModel.getEveIdEvento() );
     setOggettoNotaRes( aModel.getOggettoNotaRes() );
     setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
  }

  /**
   * Imposta i dati del model con quelli passati come argomento.
   * per fase di update.
   * <p>
   * @param aModel model con i dati utili per impostare il model stesso.
   * @throws DAOException propage l'errore di eccezione.
   */
  public void 	setDAOFromModelForUpdate(CampoNotaModel aModel) throws DAOException
  {
    setIdCampoNota( aModel.getIdCampoNota() );
    setProgressivo( aModel.getProgressivo() );
    setDescr( aModel.getDescr() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setEveIdEvento( aModel.getEveIdEvento() );
    setOggettoNotaRes( aModel.getOggettoNotaRes() );  // STUB 31/01/2006
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );  // STUB 31/01/2006
    setCondizioneUpdate(aModel.getIdCampoNota());
  }

  public void setCondizione(CampoNotaModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

  /**
   * Imposta la condizione per l'update dei dati.
   * <p>
   * @param key chaive id del record da aggiornare.
   */
   public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_CAMPO_NOTA = " + key );
  }

  /**
   * Imposta la condizione al ID EVENTO.
   * <p>
   * @param key chaive id Evento.
   */
   public void setCondizioneEvento(BigDecimal aIdEvento)
  {
    setCondition(" EVE_ID_EVENTO = " + aIdEvento );
  }

}
