package siap.sius.ulterioreistanza.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sius.ulterioreistanza.model.UlterioreIstanzaModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: UlterioreIstanzaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella UlterioreIstanza</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class UlterioreIstanzaDAO extends TableDAO 
{
  /**
   * Costruttore di classe, con Connection come parametro.
   * <p>  
   * @param con Connection connessione al DBase.
   */
	public UlterioreIstanzaDAO (Connection aCon) 
	{
	   super(aCon);
	   setTable("ULTERIORE_ISTANZA");
     
     setFieldKey("ID_ULTERIORE_ISTANZA",BIG_DECIMAL);
     setSequenceField("ID_ULTERIORE_ISTANZA","ULT_IST_SEQ");

     setField("ID_ULTERIORE_ISTANZA", BIG_DECIMAL);
     setField("COD_OGGETTO_PROCEDIMENTO", STRING);
     setField("DATA_RICHIESTA", DATE);
     setField("DATA_ARRIVO_CANCELLERIA", DATE);
     setField("COD_TIPO_ATTO", STRING);
     setField("COD_TIPO_MITTENTE_ATTO", STRING);
     setField("SEDE_MITTENTE", STRING);
     setField("DESCR_MITTENTE", STRING);
     setField("NOTE", STRING);
     setField("COD_OPERATORE_INSERIMENTO", STRING);
     setField("DATA_INSERIMENTO", DATE);
     setField("COD_UFFICIO_INSERIMENTO", STRING);
     setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
     setField("DATA_AGGIORNAMENTO", DATE);
     setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
     setField("FAS_SIU_ID_FASCICOLO_SIUS", BIG_DECIMAL);
	}

  //
  // METODI GET()
  //

  public BigDecimal  getIdUlterioreIstanza() 		     throws DAOException	 { return getBigDecimal("ID_ULTERIORE_ISTANZA"); } 
  public String 		 getCodOggettoProcedimento() 	 throws DAOException	 { return getString("COD_OGGETTO_PROCEDIMENTO"); } 
  public Date 			 getDataRichiesta() 		         throws DAOException	 { return getDate("DATA_RICHIESTA"); } 
	public Date        getDataArrivoCancelleria() 		 throws DAOException	 { return getDate("DATA_ARRIVO_CANCELLERIA"); } 
	public String 		 getCodTipoAtto() 		           throws DAOException	 { return getString("COD_TIPO_ATTO"); } 
	public String 		 getCodTipoMittenteAtto()        throws DAOException	 { return getString("COD_TIPO_MITTENTE_ATTO"); } 
	public String 		 getSedeMittente() 		           throws DAOException	 { return getString("SEDE_MITTENTE"); } 
	public String 		 getDescrMittente() 		         throws DAOException	 { return getString("DESCR_MITTENTE"); } 
	public String 		 getNote() 		                   throws DAOException	 { return getString("NOTE"); } 
	public String 		 getCodOperatoreInserimento() 	 throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); } 
	public Date        getDataInserimento() 		       throws DAOException	 { return getDate("DATA_INSERIMENTO"); } 
	public String      getCodUfficioInserimento() 		 throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); } 
	public String 		 getCodOperatoreAggiornamento()  throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); } 
	public Date 		   getDataAggiornamento() 		     throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); } 
	public String 		 getCodUfficioAggiornamento()    throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); } 
	public BigDecimal  getFasSiuIdFascicoloSius() 		 throws DAOException	 { return getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"); } 


	//
	// METODI SET()
	//
  
	public void  	 setIdUlterioreIstanza( BigDecimal aValore ) 			   { setBigDecimal("ID_ULTERIORE_ISTANZA", aValore); } 
	public void  	 setCodOggettoProcedimento( String aValore ) 			 { setString("COD_OGGETTO_PROCEDIMENTO", aValore); } 
	public void  	 setDataRichiesta( Date aValore ) 			             { setDate("DATA_RICHIESTA", aValore); } 
	public void  	 setDataArrivoCancelleria( Date aValore ) 			     { setDate("DATA_ARRIVO_CANCELLERIA", aValore); } 
	public void  	 setCodTipoAtto( String aValore ) 			             { setString("COD_TIPO_ATTO", aValore); } 
	public void  	 setCodTipoMittenteAtto( String aValore ) 			     { setString("COD_TIPO_MITTENTE_ATTO", aValore); } 
	public void  	 setSedeMittente( String aValore ) 			             { setString("SEDE_MITTENTE", aValore); } 
	public void  	 setDescrMittente( String aValore ) 			           { setString("DESCR_MITTENTE", aValore); } 
	public void  	 setNote( String aValore ) 			                     { setString("NOTE", aValore); } 
	public void  	 setCodOperatoreInserimento( String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); } 
	public void  	 setDataInserimento( Date aValore ) 			           { setDate("DATA_INSERIMENTO", aValore); } 
	public void  	 setCodUfficioInserimento( String aValore ) 			   { setString("COD_UFFICIO_INSERIMENTO", aValore); } 
	public void  	 setCodOperatoreAggiornamento( String aValore ) 		 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); } 
	public void  	 setDataAggiornamento( Date aValore ) 			         { setDate("DATA_AGGIORNAMENTO", aValore); } 
	public void  	 setCodUfficioAggiornamento( String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); } 
	public void  	 setFasSiuIdFascicoloSius( BigDecimal aValore ) 		 { setBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS", aValore); } 

  /**
   * Ritorna l'istanza del Model, opportunamante popolato, con i valori passati
   * come parametro.
   */
  public GenericModel getModel() throws DAOException
  { 
    return new UlterioreIstanzaModel(  getIdUlterioreIstanza() , 
								                       getCodOggettoProcedimento() , 
								                       "",
								                       getDataRichiesta() , 
								                       getDataArrivoCancelleria() , 
								                       getCodTipoAtto() , 
								                       "",
								                       getCodTipoMittenteAtto() , 
								                       "",
								                       getSedeMittente() , 
								                       getDescrMittente() , 
								                       getNote() , 
								                       getCodOperatoreInserimento() , 
								                       getDataInserimento() , 
								                       getCodUfficioInserimento() , 
								                       "",
								                       getCodOperatoreAggiornamento() , 
								                       getDataAggiornamento() , 
								                       getCodUfficioAggiornamento() , 
								                       "",
								                       getFasSiuIdFascicoloSius()  
								                    );
		}


   /**
    * Imposta i valori del DAO, con i dati prelevati dal Model.
    * <p> 
    * @param aModel Model di dati.
    * @throws DAOException propaga errore di eccezione.
    */
	 public void 	setDAOFromModel(UlterioreIstanzaModel aModel) throws DAOException
   {
	   setIdUlterioreIstanza( aModel.getIdUlterioreIstanza() );  
	   setCodOggettoProcedimento( aModel.getCodOggettoProcedimento() );  
		 setDataRichiesta( aModel.getDataRichiesta() );  
		 setDataArrivoCancelleria( aModel.getDataArrivoCancelleria() );  
		 setCodTipoAtto( aModel.getCodTipoAtto() );  
		 setCodTipoMittenteAtto( aModel.getCodTipoMittenteAtto() );  
		 setSedeMittente( aModel.getSedeMittente() );  
		 setDescrMittente( aModel.getDescrMittente() );  
		 setNote( aModel.getNote() );  
		 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );  
		 setDataInserimento( aModel.getDataInserimento() );  
		 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );  
		 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
		 setDataAggiornamento( aModel.getDataAggiornamento() );  
		 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
		 setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );  
   }

   /**
    * Imposta i dati del DAO con i dati presenti nel Model.
    * <p>
    * @param aModel Dati del Model.
    * @throws DAOException propaga errore di eccezione.
    */
	 public void 	setDAOFromModelForUpdate(UlterioreIstanzaModel aModel) throws DAOException
   {
		 setIdUlterioreIstanza( aModel.getIdUlterioreIstanza() );  
		 setCodOggettoProcedimento( aModel.getCodOggettoProcedimento() );  
		 setDataRichiesta( aModel.getDataRichiesta() );  
		 setDataArrivoCancelleria( aModel.getDataArrivoCancelleria() );  
		 setCodTipoAtto( aModel.getCodTipoAtto() );  
		 setCodTipoMittenteAtto( aModel.getCodTipoMittenteAtto() );  
		 setSedeMittente( aModel.getSedeMittente() );  
		 setDescrMittente( aModel.getDescrMittente() );  
		 setNote( aModel.getNote() );  
		 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
		 setDataAggiornamento( aModel.getDataAggiornamento() );  
		 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
		 setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );  
		 setCondizioneUpdate(aModel.getIdUlterioreIstanza());
		}

   /**
    * Imposta le condizioni a partire dal quello valorizzato nel
    * model UlterioreIstanzaModel.
    * <p>
    * @param aModel UlterioreIstanzaModel.
    */
	 public void setCondizione(UlterioreIstanzaModel aModel)
   {
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false; 
 		 if ( lInserito ) setCondition(lCondizioni); 
   }

   
   /**
    * Metodo che imposta il filtro di condizione sulla chiave
    * per l'aggiornamento.
    * <p>
    * @param key chiave.
    */
	 public void setCondizioneUpdate(BigDecimal key)
 	 {
	   setCondition(" ID_ULTERIORE_ISTANZA = " + key ); 
 	 }
}