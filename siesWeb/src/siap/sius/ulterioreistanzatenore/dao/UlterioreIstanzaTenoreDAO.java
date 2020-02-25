package siap.sius.ulterioreistanzatenore.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sius.ulterioreistanzatenore.model.UlterioreIstanzaTenoreModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: UlterioreIstanzaTenoreDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella UlterioreIstanzaTenore</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class UlterioreIstanzaTenoreDAO extends TableDAO 
{
  /**
   * Costruttore di classe, con Connection come parametro.
   * <p>  
   * @param con Connection connessione al DBase.
   */
	public UlterioreIstanzaTenoreDAO (Connection con) 
	{
	  super(con);
	  setTable("ULTERIORE_ISTANZA_TENORE");

		//Settare la Sequence e i campi chiave
    setFieldKey("ID_ULTERIORE_ISTANZA_TENORE",BIG_DECIMAL);
    setSequenceField("ID_ULTERIORE_ISTANZA_TENORE","ULT_IST_TEN_SEQ");
     
		setField("ID_ULTERIORE_ISTANZA_TENORE", BIG_DECIMAL);
		setField("COD_OGGETTO_TENORE", STRING);
		setField("DATA", DATE);
		setField("COD_DETTAGLIO_OGGETTO", STRING);
		setField("COD_MAGISTRATO", STRING);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("ULT_IST_ID_ULTERIORE_ISTANZA", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //
	public BigDecimal 		 getIdUlterioreIstanzaTenore() 		throws DAOException	 { return getBigDecimal("ID_ULTERIORE_ISTANZA_TENORE"); } 
	public String 				 getCodOggettoTenore() 		        throws DAOException	 { return getString("COD_OGGETTO_TENORE"); } 
	public Date 					 getData() 		                    throws DAOException	 { return getDate("DATA"); } 
	public String 				 getCodDettaglioOggetto() 		    throws DAOException	 { return getString("COD_DETTAGLIO_OGGETTO"); } 
	public String 				 getCodMagistrato() 		          throws DAOException	 { return getString("COD_MAGISTRATO"); } 
	public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); } 
	public Date 					 getDataInserimento() 		        throws DAOException	 { return getDate("DATA_INSERIMENTO"); } 
	public String 				 getCodUfficioInserimento() 		  throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); } 
	public String 				 getCodOperatoreAggiornamento() 	throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); } 
	public Date 					 getDataAggiornamento() 		      throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); } 
	public String 				 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); } 
	public BigDecimal 		 getUltIstIdUlterioreIstanza() 		throws DAOException	 { return getBigDecimal("ULT_IST_ID_ULTERIORE_ISTANZA"); } 

  //
  // METODI SET()
  //
	public void  	 setIdUlterioreIstanzaTenore(BigDecimal aValore ) { setBigDecimal("ID_ULTERIORE_ISTANZA_TENORE", aValore); } 
	public void  	 setCodOggettoTenore(String aValore ) 			      { setString("COD_OGGETTO_TENORE", aValore); } 
	public void  	 setData(Date aValore ) 			                    { setDate("DATA", aValore); } 
	public void  	 setCodDettaglioOggetto(String aValore ) 			    { setString("COD_DETTAGLIO_OGGETTO", aValore); } 
	public void  	 setCodMagistrato(String aValore ) 			          { setString("COD_MAGISTRATO", aValore); } 
	public void  	 setCodOperatoreInserimento(String aValore ) 			{ setString("COD_OPERATORE_INSERIMENTO", aValore); } 
	public void  	 setDataInserimento(Date aValore ) 			          { setDate("DATA_INSERIMENTO", aValore); } 
	public void  	 setCodUfficioInserimento(String aValore ) 			  { setString("COD_UFFICIO_INSERIMENTO", aValore); } 
	public void  	 setCodOperatoreAggiornamento(String aValore ) 	  { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); } 
	public void  	 setDataAggiornamento(Date aValore ) 			        { setDate("DATA_AGGIORNAMENTO", aValore); } 
	public void  	 setCodUfficioAggiornamento(String aValore ) 			{ setString("COD_UFFICIO_AGGIORNAMENTO", aValore); } 
	public void  	 setUltIstIdUlterioreIstanza(BigDecimal aValore ) { setBigDecimal("ULT_IST_ID_ULTERIORE_ISTANZA", aValore); } 

	/**
   * Metodo che ritorna il model popolato.
   * <p>
   * @return ritorna il model opportunamente popolato. 
	 */
	public GenericModel getModel() throws DAOException
  { 
 				 return new UlterioreIstanzaTenoreModel(  
								 getIdUlterioreIstanzaTenore() , 
								 getCodOggettoTenore() , 
								 "",
								 getData() , 
								 getCodDettaglioOggetto() , 
								 "",
								 getCodMagistrato() , 
								 "",
								 getCodOperatoreInserimento() , 
								 getDataInserimento() , 
								 getCodUfficioInserimento() , 
								 "",
								 getCodOperatoreAggiornamento() , 
								 getDataAggiornamento() , 
								 getCodUfficioAggiornamento() , 
								 "",
								 getUltIstIdUlterioreIstanza()  
								);
		}

  /**
   * Imposta i valori del DAO, con i dati prelevati dal Model.
   * <p> 
   * @param aModel Model di dati.
   * @throws DAOException propaga errore di eccezione.
   */
	 public void 	 setDAOFromModel(UlterioreIstanzaTenoreModel aModel) throws DAOException
   {
	   setIdUlterioreIstanzaTenore( aModel.getIdUlterioreIstanzaTenore() );  
	   setCodOggettoTenore( aModel.getCodOggettoTenore() );  
	   setData( aModel.getData() );  
	   setCodDettaglioOggetto( aModel.getCodDettaglioOggetto() );  
	   setCodMagistrato( aModel.getCodMagistrato() );  
	   setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );  
	   setDataInserimento( aModel.getDataInserimento() );  
	   setCodUfficioInserimento( aModel.getCodUfficioInserimento() );  
	   setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
	   setDataAggiornamento( aModel.getDataAggiornamento() );  
	   setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
	   setUltIstIdUlterioreIstanza( aModel.getUltIstIdUlterioreIstanza() );  
   }

   /**
    * Imposta i dati del DAO con i dati presenti nel Model.
    * <p>
    * @param aModel Dati del Model.
    * @throws DAOException propaga errore di eccezione.
    */
	 public void 	 setDAOFromModelForUpdate(UlterioreIstanzaTenoreModel aModel) throws DAOException
	 {
	   setIdUlterioreIstanzaTenore( aModel.getIdUlterioreIstanzaTenore() );  
	   setCodOggettoTenore( aModel.getCodOggettoTenore() );  
	   setData( aModel.getData() );  
	   setCodDettaglioOggetto( aModel.getCodDettaglioOggetto() );  
	   setCodMagistrato( aModel.getCodMagistrato() );  
	   setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
	   setDataAggiornamento( aModel.getDataAggiornamento() );  
	   setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
     setUltIstIdUlterioreIstanza( aModel.getUltIstIdUlterioreIstanza() );  
		 setCondizioneUpdate(aModel.getIdUlterioreIstanzaTenore());
		}

   /**
    * Imposta le condizioni a partire dal quello valorizzato nel
    * model UlterioreIstanzaTenoreModel.
    * <p>
    * @param aModel UlterioreIstanzaTenoreModel.
    */
	 public void setCondizione(UlterioreIstanzaTenoreModel aModel)
	 {
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false;
     
      if( aModel.getUltIstIdUlterioreIstanza() != null )
      {
        lCondizioni += ( lInserito ? " AND " : "" );
        lCondizioni += " ULT_IST_ID_ULTERIORE_ISTANZA = " + aModel.getUltIstIdUlterioreIstanza();
        lInserito = true;
      }   
      setCondition(lCondizioni); 
	}

	/**
   * Imposta condizione di update per id
   * <p> 
   * @param key id dell'ulteriore istanza tenore
	 */
	public void setCondizioneUpdate(BigDecimal key)
	{
	  setCondition(" ID_ULTERIORE_ISTANZA_TENORE = " + key ); 
	}

  /**
   * Imposta condizione di update per id ulteriore istanza.
   * <p> 
   * @param key id dell'ulteriore istanza.
   */
  public void setCondizioneUpdateByIdUltIst(BigDecimal key)
  {
    setCondition(" ULT_IST_ID_ULTERIORE_ISTANZA = " + key ); 
  }
}
