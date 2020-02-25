package siap.sige.sentenza.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.sentenza.model.FasSigeSentenzaModel;
import siap.sige.sentenza.model.SentenzaSigeModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: FasSigeSentenzaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella FasSigeSentenza</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: </p>
* @version 1.0
*/

public class FasSigeSentenzaDAO extends TableDAO 
{
	public FasSigeSentenzaDAO (Connection con) 
	{
		super(con);
		setTable("FAS_SIGE_SENTENZA");

		//Settare la Sequence e i campi chiave
		setSequenceField("ID_FAS_SIGE_SENTENZA", "FAS_SIGE_SEN_SEQ");
		setFieldKey("ID_FAS_SIGE_SENTENZA", BIG_DECIMAL);

		setField("ID_FAS_SIGE_SENTENZA", BIG_DECIMAL);
		setField("FAS_ID_FASCICOLO_SIGE", BIG_DECIMAL);
		setField("SEN_ID_SENTENZA", BIG_DECIMAL);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("DATA_IRREVOCABILITA", DATE);
		setField("FLAG_COMPETENZA", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

		public BigDecimal 	getIdFasSigeSentenza() throws DAOException	 { return getBigDecimal("ID_FAS_SIGE_SENTENZA"); } 
		public BigDecimal 	getFasIdFascicoloSige() throws DAOException	 { return getBigDecimal("FAS_ID_FASCICOLO_SIGE"); } 
		public BigDecimal 	getSenIdSentenza() 		throws DAOException	 { return getBigDecimal("SEN_ID_SENTENZA"); } 
		public Date 		getDataInserimento() 	throws DAOException	 { return getDate("DATA_INSERIMENTO"); } 
		public String 		getCodOperatoreInserimento() throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); } 
		public String 		getCodUfficioInserimento() 	 throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); } 
		public Date 		getDataIrrevocabilita() 		throws DAOException	 { return getDate("DATA_IRREVOCABILITA"); } 
		public String 		getFlagCompetenza() 		throws DAOException	 { return getString("FLAG_COMPETENZA"); } 
		public Date 		getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); } 
		public String 		getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); } 
		public String 		getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); } 
		public BigDecimal 	getFasSieIdFascicoloSiep() 		throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); } 


  //
  // METODI SET()
  //

		public void  	 setIdFasSigeSentenza(BigDecimal aValore ) 			 { setBigDecimal("ID_FAS_SIGE_SENTENZA", aValore); } 
		public void  	 setFasIdFascicoloSige(BigDecimal aValore ) 			 { setBigDecimal("FAS_ID_FASCICOLO_SIGE", aValore); } 
		public void  	 setSenIdSentenza(BigDecimal aValore ) 			 { setBigDecimal("SEN_ID_SENTENZA", aValore); } 
		public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); } 
		public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); } 
		public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); } 
		public void  	 setDataIrrevocabilita(Date aValore ) 			 { setDate("DATA_IRREVOCABILITA", aValore); } 
		public void  	 setFlagCompetenza(String aValore ) 			 { setString("FLAG_COMPETENZA", aValore); } 
		public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); } 
		public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); } 
		public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); } 
		public void  	 setFasSieIdFascicoloSiep(BigDecimal aValore ) 	{ setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); } 


	public GenericModel getModel() throws DAOException
  	{ 
		return new SentenzaSigeModel(  
								 getIdFasSigeSentenza(), 
								 getFasIdFascicoloSige(), 
								 getSenIdSentenza(), 
								 getDataInserimento(), 
								 getCodOperatoreInserimento(), 
								 getCodUfficioInserimento(),
								 "",
								 getDataIrrevocabilita(),
								 getFlagCompetenza(),
								 getDataAggiornamento(),
								 getCodOperatoreAggiornamento(),
								 getCodUfficioAggiornamento(),
								 "",
								 getFasSieIdFascicoloSiep());
	}


	public void  setDAOFromModel(SentenzaSigeModel aModel) throws DAOException
  	{
		setIdFasSigeSentenza( aModel.getIdFasSigeSentenza());  
		setFasIdFascicoloSige( aModel.getFasIdFascicoloSige());  
		setSenIdSentenza( aModel.getIdSentenza());  
		setDataInserimento( aModel.getDataInserimento() );  
		setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );  
		setCodUfficioInserimento( aModel.getCodUfficioInserimento() );  
		setDataIrrevocabilita( aModel.getDataIrrevocabilita() );  
		setFlagCompetenza( aModel.getFlagCompetenza() );  
		setDataAggiornamento( aModel.getDataAggiornamento() );  
		setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
		setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
		setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
  	}


	public void 	 setDAOFromModelForInsert(SentenzaSigeModel aModel) throws DAOException
  	{
		setFasIdFascicoloSige( aModel.getFasIdFascicoloSige());  
		setSenIdSentenza( aModel.getIdSentenza());  
		setDataInserimento( aModel.getDataInserimento() );  
		setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );  
		setCodUfficioInserimento( aModel.getCodUfficioInserimento() );  
		setDataIrrevocabilita( aModel.getDataIrrevocabilita() );  
		setFlagCompetenza( aModel.getFlagCompetenza() );  
		setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
	}

	/**
	 * Preparazione dell'Update.
	 * I campi aggiornabili sono: DATA_IRREVOCABILITA e FLAG_COMPETENZA.
	 * @param aModel
	 * @throws DAOException
	 */

	public void setDAOFromModelForUpdate(SentenzaSigeModel aModel) throws DAOException
  	{
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setDataIrrevocabilita( aModel.getDataIrrevocabilita() );  
		setFlagCompetenza( aModel.getFlagCompetenza() );  
	}

	/**
	 * Aggiorna il campo SEN_ID_SENTENZA.
	 * @param aModel
	 * @throws DAOException
	 */
	public void setDAOFromModelForUpdateIdSentenza(SentenzaModel aModel) throws DAOException
  	{
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setSenIdSentenza(aModel.getIdSentenza());
  	}
	
	/**
	 * La funzione aggiorna eventuale Titolo di Competenza per un FASCICOLO_SIGE
	 * resettando il FLAG_COMPETENZA da "S" ad "N";
	 * @param aModel
	 * @throws DAOException
	 */
	
	public void 	 annullaCompetenzaXFascicolo(SentenzaSigeModel aModel) throws DAOException
  	{	
		// Campi da aggiornare in tabella
		setDataAggiornamento( aModel.getDataAggiornamento() );  
		setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
		setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
		setFlagCompetenza( "N" );  
		
		// Preparazione della condizione di Update
		SentenzaSigeModel lCondizioni = new SentenzaSigeModel();
		lCondizioni.setFasIdFascicoloSige(aModel.getFasIdFascicoloSige());
		lCondizioni.setFlagCompetenza("S");
		setCondizione(lCondizioni);
		
		// Aggiornamento
		update();
	}
	
	
	
/**
 * Valorizza condizione di filtro in base al contenuto del FasSigeSentenzaModel.
 * I campi che possono definire il filtro sono:
 * ID_FAS_SIGE_SENTENZA, FAS_ID_FASCICOLO_SIGE, SEN_ID_SENTENZA.
 * @param aModel
 */
	public void setCondizione(SentenzaSigeModel aModel)
	{
		 String lCondizioni = new String(); 
		 String lAppoggio = new String(); 
		 boolean lInserito = false; 
		 
		 if (aModel.getIdFasSigeSentenza() != null)
		 {
			 lCondizioni = " ID_FAS_SIGE_SENTENZA = " + aModel.getIdFasSigeSentenza();
			 lInserito = true; 
		 }
		 if (aModel.getFasIdFascicoloSige() != null)
		 {
			 lAppoggio = " FAS_ID_FASCICOLO_SIGE = " + aModel.getFasIdFascicoloSige();
			 if (lInserito)
				 lCondizioni += " AND" + lAppoggio;
			 else
				 lCondizioni =  lAppoggio;
			 lInserito = true; 
		 }
		 if (aModel.getIdSentenza() != null)
		 {
			 lAppoggio = " SEN_ID_SENTENZA = " + aModel.getIdSentenza();
			 if (lInserito)
				 lCondizioni += " AND" + lAppoggio;
			 else
				 lCondizioni =  lAppoggio;
			 lInserito = true; 
		 }
		 if (aModel.getFlagCompetenza() != null && aModel.getFlagCompetenza().trim().length() > 0 )
		 {
			 lAppoggio = " FLAG_COMPETENZA = '" + aModel.getFlagCompetenza() + "'";
			 if (lInserito)
				 lCondizioni += " AND" + lAppoggio;
			 else
				 lCondizioni =  lAppoggio;
			 lInserito = true; 
		 }
		 if (aModel.getFasSieIdFascicoloSiep() != null)
		 {
			 lAppoggio = " FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep();
			 if (lInserito)
				 lCondizioni += " AND" + lAppoggio;
			 else
				 lCondizioni =  lAppoggio;
			 lInserito = true; 
		 }
		 
 		 if ( lInserito ) setCondition(lCondizioni); 
	}


	public void setCondizioneUpdate(BigDecimal key)
 	{
		setCondition(" ID_FAS_SIGE_SENTENZA = " + key ); 
	}
	
	/**
	 * Setta le condizioni per trovare un record per Fascicolo Sige,
	 * Sentenza con DATA_IRREVOCABILITA null.
	 * @param aModel
	 */
	public void setCondizioneDataIrrevocabilitaNull(SentenzaSigeModel aModel) throws DAOException
 	{
		if (aModel.getFasIdFascicoloSige() == null)
    		throw new DAOException("ID Fascicolo SIGE non definito!");
		if (aModel.getIdSentenza() == null)
    		throw new DAOException("ID Sentenza non definito!");
		
		setCondition(" FAS_ID_FASCICOLO_SIGE = " + aModel.getFasIdFascicoloSige() + " AND SEN_ID_SENTENZA = "  + aModel.getIdSentenza() + " AND DATA_IRREVOCABILITA IS NULL"); 
	}

	/** 20/01/2009
	 * Valorizza condizione di filtro in base al parametro FasSigeSentenzaModel.
	 * I campi che possono definire il filtro sono:
	 * ID_FAS_SIGE_SENTENZA, FAS_ID_FASCICOLO_SIGE, SEN_ID_SENTENZA.
	 * @param aModel
	 */
		public void setCondizione(FasSigeSentenzaModel aModel)
		{
			 String lCondizioni = new String(); 
			 String lAppoggio = new String(); 
			 boolean lInserito = false; 
			 
			 if (aModel.getIdFasSigeSentenza() != null)
			 {
				 lCondizioni = " ID_FAS_SIGE_SENTENZA = " + aModel.getIdFasSigeSentenza();
				 lInserito = true; 
			 }
			 if (aModel.getFasIdFascicoloSige() != null)
			 {
				 lAppoggio = " FAS_ID_FASCICOLO_SIGE = " + aModel.getFasIdFascicoloSige();
				 if (lInserito)
					 lCondizioni += " AND" + lAppoggio;
				 else
					 lCondizioni =  lAppoggio;
				 lInserito = true; 
			 }
			 if (aModel.getSenIdSentenza() != null)
			 {
				 lAppoggio = " SEN_ID_SENTENZA = " + aModel.getSenIdSentenza();
				 if (lInserito)
					 lCondizioni += " AND" + lAppoggio;
				 else
					 lCondizioni =  lAppoggio;
				 lInserito = true; 
			 }
			 
	 		 if ( lInserito ) setCondition(lCondizioni); 
		}

	  public void setDAOFromModelForAssegnaTitoloEsecutivo(FascicoloSigeEstesoModel aFascicoloSigeEsteso, FascicoloSiepModel lFasSiepMod) throws DAOException
	  {
	    setCodOperatoreAggiornamento   ( aFascicoloSigeEsteso.getFascicoloSige().getCodOperatoreAggiornamento() );
	    setCodUfficioAggiornamento     ( aFascicoloSigeEsteso.getFascicoloSige().getCodUfficioAggiornamento() );
	    setDataAggiornamento           ( aFascicoloSigeEsteso.getFascicoloSige().getDataAggiornamento() );
	    setSenIdSentenza               ( lFasSiepMod.getSenIdSentenza() );
	    setFasSieIdFascicoloSiep       ( lFasSiepMod.getIdFascicoloSiep() );
	    
	    setCondizioneAssegnaTitoloEsecUpdate(aFascicoloSigeEsteso.getFascicoloSige().getIdFascicoloSige());
	  }

	  public void setDAOFromModelForAssegnaTitoloEsecutivo(FascicoloSigeEstesoModel aFascicoloSigeEsteso) throws DAOException
	  {
	    setCodOperatoreAggiornamento   ( aFascicoloSigeEsteso.getFascicoloSige().getCodOperatoreAggiornamento() );
	    setCodUfficioAggiornamento     ( aFascicoloSigeEsteso.getFascicoloSige().getCodUfficioAggiornamento() );
	    setDataAggiornamento           ( aFascicoloSigeEsteso.getFascicoloSige().getDataAggiornamento() );
	    setSenIdSentenza               ( aFascicoloSigeEsteso.getFascicoloSiep().getSenIdSentenza() );
	    setFasSieIdFascicoloSiep       ( aFascicoloSigeEsteso.getFascicoloSiep().getIdFascicoloSiep() );
	    
	    setCondizioneAssegnaTitoloEsecUpdate(aFascicoloSigeEsteso.getFascicoloSige().getIdFascicoloSige());
	  }
	  
	  public void setCondizioneAssegnaTitoloEsecUpdate(BigDecimal key)
	  {
		  setCondition(" FAS_ID_FASCICOLO_SIGE = " + key + "AND FLAG_COMPETENZA = 'S' "); 
	  }

}
