package siap.sige.tenore.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.sentenza.model.SentenzaModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.tenore.model.TenoreSentenzaReatoModel;
import siap.sige.tenore.model.TenoreSigeModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: TenoreSentenzaReatoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella TenoreSentenzaReato</p>
* Tabella di relazione tra Oggetto SIGE (TENORE_SIGE), Sentenza e Reato.
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public class TenoreSentenzaReatoDAO extends TableDAO 
{
	public TenoreSentenzaReatoDAO (Connection con) 
	{
			 super(con);
			 setTable("TENORE_SENTENZA_REATO");

			//Settare la Sequence e i campi chiave
			setSequenceField("ID_TEN_SEN_REA", "TEN_SEN_REA_SEQ");
			
			setFieldKey("ID_TEN_SEN_REA", BIG_DECIMAL);
		
			setField("ID_TEN_SEN_REA", BIG_DECIMAL);
			setField("DATA_INSERIMENTO", DATE);
			setField("COD_OPERATORE_INSERIMENTO", STRING);
			setField("COD_UFFICIO_INSERIMENTO", STRING);
			setField("TEN_ID_TENORE_SIGE", BIG_DECIMAL);
			setField("SEN_ID_SENTENZA", BIG_DECIMAL);
			setField("REA_ID_REATO", BIG_DECIMAL);
			setField("COD_ESITO", STRING);
			setField("DATA_AGGIORNAMENTO", DATE);
			setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
			setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
			 
	}


  //
  // METODI GET()
  //

	public Date 			getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); } 
	public String 			getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); } 
	public String 			getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); } 
	public BigDecimal 		getIdTenSenRea() 		throws DAOException	 { return getBigDecimal("ID_TEN_SEN_REA"); } 
	public BigDecimal 		getTenIdTenoreSige() 		throws DAOException	 { return getBigDecimal("TEN_ID_TENORE_SIGE"); } 
	public BigDecimal 		getSenIdSentenza() 		throws DAOException	 { return getBigDecimal("SEN_ID_SENTENZA"); } 
	public BigDecimal 		getReaIdReato() 		throws DAOException	 { return getBigDecimal("REA_ID_REATO"); } 
	public String 			getCodEsito() 		throws DAOException	 { return getString("COD_ESITO"); } 
	public Date 			getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); } 
	public String 			getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); } 
	public String 			getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); } 


  //
  // METODI SET()
  //

	public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); } 
	public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); } 
	public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); } 
	public void  	 setIdTenSenRea(BigDecimal aValore ) 			 { setBigDecimal("ID_TEN_SEN_REA", aValore); } 
	public void  	 setTenIdTenoreSige(BigDecimal aValore ) 			 { setBigDecimal("TEN_ID_TENORE_SIGE", aValore); } 
	public void  	 setSenIdSentenza(BigDecimal aValore ) 			 { setBigDecimal("SEN_ID_SENTENZA", aValore); } 
	public void  	 setReaIdReato(BigDecimal aValore ) 			 { setBigDecimal("REA_ID_REATO", aValore); } 
	public void  	 setCodEsito(String aValore ) 			 { setString("COD_ESITO", aValore); } 
	public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); } 
	public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); } 
	public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); } 

	
	
	public GenericModel getModel() throws DAOException
  	{ 
		return new TenoreSentenzaReatoModel(  
								 getTenIdTenoreSige() , 
								 getSenIdSentenza() , 
								 getReaIdReato() , 
								 getDataInserimento() , 
								 getCodOperatoreInserimento() , 
								 getCodUfficioInserimento(), 
								 "",
								 getIdTenSenRea(),
								 getCodEsito(),
								 "",
								 getDataAggiornamento(),
								 getCodOperatoreAggiornamento(),
								 getCodUfficioAggiornamento(),
								 ""	);
	}


	public void 	 setDAOFromModel(TenoreSentenzaReatoModel aModel) throws DAOException
  	{
				 setTenIdTenoreSige( aModel.getTenIdTenoreSige() );  
				 setSenIdSentenza( aModel.getSenIdSentenza() );  
				 setReaIdReato( aModel.getReaIdReato() );  
				 setDataInserimento( aModel.getDataInserimento() );  
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );  
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );  
	}
	 
	 public void 	 setDAOFromTenoreModel(TenoreSigeModel aModel) throws DAOException
		{
				 setTenIdTenoreSige( aModel.getIdTenoreSige() );  
				 setSenIdSentenza( aModel.getIdSentenza() );  
				 setReaIdReato( aModel.getIdReato() );  
				 setDataInserimento( aModel.getDataInserimento() );  
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );  
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );  
		}
	 /**
	  * Viene modificato il campo SEN_ID_SENTENZA.
	  * @param aModel
	  * @throws DAOException
	  */
		public void setDAOFromModelForUpdateSentenza(SentenzaModel aModel) throws DAOException
		{
			setDataAggiornamento(aModel.getDataAggiornamento());
			setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
			setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
			setSenIdSentenza(aModel.getIdSentenza());
		}


	 public void 	 setDAOFromModelForUpdate(TenoreSentenzaReatoModel aModel) throws DAOException
  		{
				 setTenIdTenoreSige( aModel.getTenIdTenoreSige() );  
				 setSenIdSentenza( aModel.getSenIdSentenza() );  
				 setReaIdReato( aModel.getReaIdReato() );  
		// setCondizioneUpdate(aModel.getIdTenoreSentenzaReato());
		}


	public void setCondizione(TenoreSentenzaReatoModel aModel)
		 {
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false; 
 		 if ( lInserito ) setCondition(lCondizioni); 
		 }


	public void setCondizioneUpdate(BigDecimal key)
 	{
		setCondition(" ID_TEN_SEN_REA = " + key ); 
	}

	/**
	 * Predispone la condizione di filtro per cancellare tutti i record di relazione 
	 * individuati dalla chiave  TEN_ID_TENORE_SIGE.
	 */
	public void selCondizioneDelete(BigDecimal aIdTenoreSige)
	{
	    setCondition(" TEN_ID_TENORE_SIGE = " + aIdTenoreSige);
	}

	/**
	 * Predispone la condizione di filtro per cancellare l'associazione 
	 * Titolo Esecutivo/Oggetto.
	 */
	public void selCondizioneDeleteTitoloEsecutivo(BigDecimal aIdTenoreSige, BigDecimal aIdSentenza)
	{
	    setCondition(" TEN_ID_TENORE_SIGE = " + aIdTenoreSige + " AND SEN_ID_SENTENZA = " + aIdSentenza);
	}

	public void setDAOFromModelForUpdateEsiti(TenoreSentenzaReatoModel aModel) throws DAOException
	{
		setCodEsito(aModel.getCodEsito());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setCondizioneUpdate(aModel.getIdTenSenRea());
	}
	
	  public void selCondizioneDeleteProvvedimento(BigDecimal aIdProvSige)
	  {
	    setCondition(" TEN_ID_TENORE_SIGE IN ( SELECT ID_TENORE_SIGE FROM TENORE_SIGE WHERE PROV_ID_PROVVEDIMENTO_SIGE = " + aIdProvSige + " )");
	  }

	  public void selCondizioneSentenza(BigDecimal aIdSentenza)
	  {
	    setCondition(" SEN_ID_SENTENZA = " + aIdSentenza);
	  }
	
	  /**
	   * Seleziona i record legati ad un Fascicolo SIGE e ad una Sentenza specificata.
	   * @param aIdSentenza
	   * @param aIdFasSige
	   */
	  public void selCondizioneSentenzaFascicolo(BigDecimal aIdSentenza, BigDecimal aIdFasSige)
	  {
		String lCondizione = " SEN_ID_SENTENZA = " + aIdSentenza;
		lCondizione += " AND TEN_ID_TENORE_SIGE IN ( SELECT ID_TENORE_SIGE FROM TENORE_SIGE WHERE FAS_ID_FASCICOLO_SIGE = " + aIdFasSige + " )";
		
	    setCondition(lCondizione);
	  }
	  
	  public void setUpdateForDeleteProvvedimento(ProvvedimentoSigeModel aProvModel) throws DAOException
	  {
		if (aProvModel.getIdProvvedimentoSige() == null)
			throw new  DAOException("ID Provvedimento SIGE assente!");
		
		setCodEsito(null);
		setDataAggiornamento(aProvModel.getDataAggiornamento());
		setCodOperatoreAggiornamento(aProvModel.getCodOperatoreAggiornamento());
		setCodUfficioAggiornamento(aProvModel.getCodUfficioAggiornamento());
	    setCondition(" TEN_ID_TENORE_SIGE IN ( SELECT ID_TENORE_SIGE FROM TENORE_SIGE WHERE PROV_ID_PROVVEDIMENTO_SIGE = " + aProvModel.getIdProvvedimentoSige() + " )");
	  }

	  
}
