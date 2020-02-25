package siap.sige.fascicolo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: FascicoloSigeDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella FascicoloSige</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 5.0
*/

public class FascicoloSigeDAO extends TableDAO 
{
	public FascicoloSigeDAO (Connection con) 
	{
		super(con);
		setTable("FASCICOLO_SIGE");

		//Settare la Sequence e i campi chiave
		setSequenceField("ID_FASCICOLO_SIGE", "FAS_SIGE_SEQ");
		setFieldKey("ID_FASCICOLO_SIGE", BIG_DECIMAL);
    	
		setField("ID_FASCICOLO_SIGE", BIG_DECIMAL);
		setField("SOG_ID_SOGGETTO", BIG_DECIMAL);
		setField("CHIAVE_ANNO", BIG_DECIMAL);
		setField("CHIAVE_UFFICIO", STRING);
		setField("CHIAVE_PROGR", BIG_DECIMAL);
		setField("SEN_ID_SENTENZA_CUMULO", BIG_DECIMAL);
		setField("SEZ_ID_SEZIONE", BIG_DECIMAL);
		setField("COD_STATO_FASCICOLO", STRING);
		setField("COD_TIPO_GIUDIZIO", STRING);
		setField("DATA_ISCRIZIONE", DATE);
		setField("DATA_DEFINIZIONE", DATE);
		setField("RIC_ID_RICHIESTA_SIGE", BIG_DECIMAL);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("NOTE", STRING);
		setField("COD_POSIZIONE_GIURIDICA", STRING);
		setField("DATA_FINE_PENA", DATE);
		setField("COD_TIPO_DEFINIZIONE", STRING);
		setField("DESCR_DEFINIZIONE", STRING);
		setField("FAS_SIG_ID_FASCICOLO_SIGE", BIG_DECIMAL);	// 26/07/2010
		setField("NUMERO_FASCICOLI_UNIFICATI", BIG_DECIMAL);	// 26/07/2010
		//Modifica Accorpamento Uffici
		setField("CHIAVE_PROGR_ORIG", BIG_DECIMAL);
		setField("ID_FASCICOLO_SIGE_ORIGINE", BIG_DECIMAL);
		//Modifica 13/12/2016
		setField("ID_EVENTO_PROVV_CUMULO", BIG_DECIMAL);
		
	}

  //
  // METODI GET()
  //

	public BigDecimal getIdFascicoloSige() 			 throws DAOException	 { return getBigDecimal("ID_FASCICOLO_SIGE"); } 
	public BigDecimal getSogIdSoggetto() 			 throws DAOException	 { return getBigDecimal("SOG_ID_SOGGETTO"); } 
	public BigDecimal getChiaveAnno() 				 throws DAOException	 { return getBigDecimal("CHIAVE_ANNO"); } 
	public String 	  getChiaveUfficio() 			 throws DAOException	 { return getString("CHIAVE_UFFICIO"); } 
	public BigDecimal getChiaveProgr() 				 throws DAOException	 { return getBigDecimal("CHIAVE_PROGR"); } 
	public BigDecimal getSenIdSentenzaCumulo() 		 throws DAOException	 { return getBigDecimal("SEN_ID_SENTENZA_CUMULO"); }
	public BigDecimal getIdSezione() 				 throws DAOException	 { return getBigDecimal("SEZ_ID_SEZIONE"); } 
	public String 	  getCodStatoFascicolo() 		 throws DAOException	 { return getString("COD_STATO_FASCICOLO"); } 
	public String 	  getCodTipoGiudizio() 			 throws DAOException	 { return getString("COD_TIPO_GIUDIZIO"); } 
	public Date 	  getDataIscrizione() 			 throws DAOException	 { return getDate("DATA_ISCRIZIONE"); } 
	public Date 	  getDataDefinizione() 			 throws DAOException	 { return getDate("DATA_DEFINIZIONE"); } 
	public BigDecimal getRicIdRichiestaSige() 		 throws DAOException	 { return getBigDecimal("RIC_ID_RICHIESTA_SIGE"); } 
	public String 	  getCodOperatoreInserimento()	 throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); } 
	public String 	  getCodUfficioInserimento() 	 throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); } 
	public Date 	  getDataInserimento() 			 throws DAOException	 { return getDate("DATA_INSERIMENTO"); } 
	public String 	  getCodOperatoreAggiornamento() throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); } 
	public String 	  getCodUfficioAggiornamento() 	 throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); } 
	public Date 	  getDataAggiornamento() 		 throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); } 
	public String 	  getNote() 					 throws DAOException	 { return getString("NOTE"); } 
	public String 	  getCodPosizioneGiuridica() 	 throws DAOException	 { return getString("COD_POSIZIONE_GIURIDICA"); } 
	public Date 	  getDataFinePena() 			 throws DAOException	 { return getDate("DATA_FINE_PENA"); } 
	public String 	  getCodTipoDefinizione() 		 throws DAOException	 { return getString("COD_TIPO_DEFINIZIONE"); } 
	public String 	  getDescrDefinizione() 		 throws DAOException	 { return getString("DESCR_DEFINIZIONE"); } 
	public BigDecimal getFasSigIdFascicoloSige()	 throws DAOException	 { return getBigDecimal("FAS_SIG_ID_FASCICOLO_SIGE"); }  // 26/07/2010 
	public BigDecimal getNumeroFascicoliUnificati()	 throws DAOException	 { return getBigDecimal("NUMERO_FASCICOLI_UNIFICATI"); }  // 26/07/2010
	public BigDecimal getChiaveProgrOrig()	         throws DAOException	 { return getBigDecimal("CHIAVE_PROGR_ORIG"); }
	public BigDecimal getIdFascicoloSigeOrigine() 	 throws DAOException	 { return getBigDecimal("ID_FASCICOLO_SIGE_ORIGINE"); }
	public BigDecimal getIdEventoProvvCumulo() 		 throws DAOException	 { return getBigDecimal("ID_EVENTO_PROVV_CUMULO"); }
	
  //
  // METODI SET()
  //

	public void  	 setIdFascicoloSige(BigDecimal aValore ) 			{ setBigDecimal("ID_FASCICOLO_SIGE", aValore); } 
	public void  	 setSogIdSoggetto(BigDecimal aValore ) 				{ setBigDecimal("SOG_ID_SOGGETTO", aValore); } 
	public void  	 setChiaveAnno(BigDecimal aValore ) 				{ setBigDecimal("CHIAVE_ANNO", aValore); } 
	public void  	 setChiaveUfficio(String aValore ) 					{ setString("CHIAVE_UFFICIO", aValore); } 
	public void  	 setChiaveProgr(BigDecimal aValore ) 				{ setBigDecimal("CHIAVE_PROGR", aValore); } 
	public void  	 setSenIdSentenzaCumulo(BigDecimal aValore ) 		{ setBigDecimal("SEN_ID_SENTENZA_CUMULO", aValore); } 
	public void  	 setIdSezione(BigDecimal aValore ) 					{ setBigDecimal("SEZ_ID_SEZIONE", aValore); } 
	public void  	 setCodStatoFascicolo(String aValore ) 				{ setString("COD_STATO_FASCICOLO", aValore); } 
	public void  	 setCodTipoGiudizio(String aValore ) 				{ setString("COD_TIPO_GIUDIZIO", aValore); } 
	public void  	 setDataIscrizione(Date aValore ) 					{ setDate("DATA_ISCRIZIONE", aValore); } 
	public void  	 setDataDefinizione(Date aValore ) 					{ setDate("DATA_DEFINIZIONE", aValore); } 
	public void  	 setRicIdRichiestaSige(BigDecimal aValore ) 		{ setBigDecimal("RIC_ID_RICHIESTA_SIGE", aValore); } 
	public void  	 setCodOperatoreInserimento(String aValore )	    { setString("COD_OPERATORE_INSERIMENTO", aValore); } 
	public void  	 setCodUfficioInserimento(String aValore ) 		    { setString("COD_UFFICIO_INSERIMENTO", aValore); } 
	public void  	 setDataInserimento(Date aValore ) 			 	    { setDate("DATA_INSERIMENTO", aValore); } 
	public void  	 setCodOperatoreAggiornamento(String aValore ) 	    { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); } 
	public void  	 setCodUfficioAggiornamento(String aValore ) 	    { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); } 
	public void  	 setDataAggiornamento(Date aValore ) 			    { setDate("DATA_AGGIORNAMENTO", aValore); } 
	public void  	 setNote(String aValore ) 						    { setString("NOTE", aValore); } 
	public void  	 setCodPosizioneGiuridica(String aValore ) 			{ setString("COD_POSIZIONE_GIURIDICA", aValore); } 
	public void  	 setDataFinePena(Date aValore ) 			        { setDate("DATA_FINE_PENA", aValore); } 
	public void  	 setCodTipoDefinizione(String aValore ) 	        { setString("COD_TIPO_DEFINIZIONE", aValore); } 
	public void  	 setDescrDefinizione(String aValore ) 		        { setString("DESCR_DEFINIZIONE", aValore); } 
	public void  	 setFasSigIdFascicoloSige(BigDecimal aValore ) 		{ setBigDecimal("FAS_SIG_ID_FASCICOLO_SIGE", aValore); }   // 26/07/2010
	public void  	 setNumeroFascicoliUnificati(BigDecimal aValore )	{ setBigDecimal("NUMERO_FASCICOLI_UNIFICATI", aValore); }   // 26/07/2010
	public void  	 setChiaveProgrOrig(BigDecimal aValore )	        { setBigDecimal("CHIAVE_PROGR_ORIG", aValore); }
	public void  	 setIdFascicoloSigeOrigine(BigDecimal aValore ) 	{ setBigDecimal("ID_FASCICOLO_SIGE_ORIGINE", aValore); }
	public void  	 setIdEventoProvvCumulo(BigDecimal aValore ) 		{ setBigDecimal("ID_EVENTO_PROVV_CUMULO", aValore); }
	
	public GenericModel getModel() throws DAOException
  	{ 
		return new FascicoloSigeModel(  
								 getIdFascicoloSige() , 
								 getSogIdSoggetto() , 
								 getChiaveAnno() , 
								 null,
								 null,
								 getChiaveUfficio() , 
								 "",
								 getChiaveProgr() , 
								 null,
								 null,
								 getSenIdSentenzaCumulo(),
								 getIdSezione() , 
								 "",
								 getCodStatoFascicolo() , 
								 "",
								 getCodTipoGiudizio() , 
								 "",
								 getDataIscrizione() ,
								 null,
								 null,
								 getDataDefinizione() , 
								 getRicIdRichiestaSige() , 
								 getCodOperatoreInserimento() , 
								 getCodUfficioInserimento() , 
								 "",
								 getDataInserimento() , 
								 getCodOperatoreAggiornamento() , 
								 getCodUfficioAggiornamento() , 
								 "",
								 getDataAggiornamento(),
								 getNote(),
								 getCodPosizioneGiuridica() , 
								 "",
								 getDataFinePena(),
								 getCodTipoDefinizione(),
								 "",
								 getDescrDefinizione(),
								 "",
								 "",
								 getFasSigIdFascicoloSige(),
								 getNumeroFascicoliUnificati(),
								 getChiaveProgrOrig(),
								 getIdFascicoloSigeOrigine(),
								 getIdEventoProvvCumulo(),
								 "",
								 ""
								);
	}

	public void  setDAOFromModel(FascicoloSigeModel aModel) throws DAOException
  {
		setIdFascicoloSige( aModel.getIdFascicoloSige() );  
		setSogIdSoggetto( aModel.getSogIdSoggetto() );  
		setChiaveAnno( aModel.getChiaveAnno() );  
		setChiaveUfficio( aModel.getChiaveUfficio() );  
		setChiaveProgr( aModel.getChiaveProgr() );  
		setSenIdSentenzaCumulo(aModel.getSenIdSentenzaCumulo());
		setIdSezione( aModel.getIdSezione() );  
		setCodStatoFascicolo( aModel.getCodStatoFascicolo() );  
		setCodTipoGiudizio( aModel.getCodTipoGiudizio() );  
		setDataIscrizione( aModel.getDataIscrizione() );  
		setDataDefinizione( aModel.getDataDefinizione() );  
		setRicIdRichiestaSige( aModel.getRicIdRichiestaSige() );  
		setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );  
		setCodUfficioInserimento( aModel.getCodUfficioInserimento() );  
		setDataInserimento( aModel.getDataInserimento() );  
		setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
		setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
		setDataAggiornamento( aModel.getDataAggiornamento() );  
		setNote( aModel.getNote() );  
		setCodPosizioneGiuridica( aModel.getCodPosizioneGiuridica() );  
		setDataFinePena( aModel.getDataFinePena() );  
		setCodTipoDefinizione(aModel.getCodTipoDefinizione()); 
		setDescrDefinizione(aModel.getDescrDefinizione() ); 
		setFasSigIdFascicoloSige( aModel.getFasSigIdFascicoloSige() );  // 26/07/2010
		setNumeroFascicoliUnificati( aModel.getNumeroFascicoliUnificati() );  // 26/07/2010
		setChiaveProgrOrig( aModel.getChiaveProgrOrig() );
		setIdFascicoloSigeOrigine(aModel.getIdFascicoloSigeOrigine() );
		setIdEventoProvvCumulo(aModel.getIdEventoProvvCumulo() );
  }


	public void setDAOFromModelForUpdate(FascicoloSigeModel aModel) throws DAOException
  {
		setIdSezione( aModel.getIdSezione() );  
		//setCodStatoFascicolo( aModel.getCodStatoFascicolo() );  
		setCodTipoGiudizio( aModel.getCodTipoGiudizio() );  
		setSenIdSentenzaCumulo(aModel.getSenIdSentenzaCumulo());
		setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
		setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
		setDataAggiornamento( aModel.getDataAggiornamento() );  
		setNote( aModel.getNote() );  
		setCodPosizioneGiuridica( aModel.getCodPosizioneGiuridica() );  
		setDataFinePena( aModel.getDataFinePena() );
		setCodStatoFascicolo(aModel.getCodStatoFascicolo());
		setIdEventoProvvCumulo(aModel.getIdEventoProvvCumulo());
		//@emma 26072018 intervento post COLLAUDO 11.2 
		if(aModel.getDataDefinizione()!= null)
			setDataDefinizione(aModel.getDataDefinizione());
		setCondizioneUpdate(aModel.getIdFascicoloSige());
		
	}
	
	public void setDAOFromModelForUpdateTipoGiudizio(FascicoloSigeModel aModel) throws DAOException
	  {
		setCodTipoGiudizio( aModel.getCodTipoGiudizio() );  
		setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
		setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
		setDataAggiornamento( aModel.getDataAggiornamento() );  

		setCondizioneUpdate(aModel.getIdFascicoloSige());
			
		}


	public void setCondizione(FascicoloSigeModel aModel)
	{
		String lCondizioni = new String(); 
    	 boolean lInserito = false; 
    	 
    	 if (aModel.getChiaveAnno() != null)
    	 {
    		 lCondizioni = " CHIAVE_ANNO = " + aModel.getChiaveAnno();
    		 lInserito = true; 
    	 }
    	 if (aModel.getChiaveProgr() != null)
    	 {
    		 if (lInserito)
     			 lCondizioni += " AND";
    		 else
    			 lInserito = true; 
    		 
    		 lCondizioni += " CHIAVE_PROGR = " + aModel.getChiaveProgr();
    	 }
    	 if (aModel.getChiaveUfficio() != null && aModel.getChiaveUfficio().trim().length() > 0 )
    	 {
    		 if (lInserito)
     			 lCondizioni += " AND";
    		 else
    			 lInserito = true; 
    		 
    		 lCondizioni += " CHIAVE_UFFICIO = '" + aModel.getChiaveUfficio() + "'";
    	 }
    	 if(aModel.getSogIdSoggetto() != null)
    	{
    		if (lInserito)
     			 lCondizioni += " AND";
    		 else
    			 lInserito = true; 
    		
    		lCondizioni = " SOG_ID_SOGGETTO = " + aModel.getSogIdSoggetto(); 
    	}
	 
		 if ( lInserito ) setCondition(lCondizioni); 
	}


	public void setCondizioneUpdate(BigDecimal key)
 	{
		setCondition(" ID_FASCICOLO_SIGE = " + key ); 
	}
	
	/**
	 * Condizione di ricerca dei FASCICOLI SIGE legati ad una sentenza.
	 * @param aIdSentenza
	 */
	public void setCondizioneByIdSentenza(BigDecimal aIdSentenza)
 	{
		setCondition(" ID_FASCICOLO_SIGE IN (SELECT DISTINCT FAS_ID_FASCICOLO_SIGE FROM FAS_SIGE_SENTENZA WHERE SEN_ID_SENTENZA =  " + aIdSentenza + " ) " ); 
	}

	/**
	 * Condizione di ricerca dei FASCICOLI SIGE legati ad un Fascicolo SIEP.
	 * @param aIdSentenza
	 */
	public void setCondizioneByIdFasSiepCodUfficio(BigDecimal aIdFasSiep, String aCodUfficio)
 	{
		String condUfficio = "";
		if (aCodUfficio.length()>1)
			condUfficio = " AND FASCICOLO_SIGE.COD_UFFICIO_INSERIMENTO = "+ aCodUfficio;
		
		setCondition(" FAS_SIE_ID_FASCICOLO_SIEP  =  " + aIdFasSiep + " AND RIC_ID_RICHIESTA_SIGE = ID_RICHIESTA_SIGE " + condUfficio);

	}
	
  public void setCondizioneUpdateStatoFascicolo(BigDecimal key, String aCodStato)
  {
    setCondition(" ID_FASCICOLO_SIGE = " + key + " AND COD_STATO_FASCICOLO = '" + aCodStato + "'");
  }
	
  public void setDAOFromModelForAssegnaTitoloEsecutivo(FascicoloSigeEstesoModel aFascicoloSigeEsteso) throws DAOException
  {
    setSogIdSoggetto               ( aFascicoloSigeEsteso.getFascicoloSige().getSogIdSoggetto() );
    setCodOperatoreAggiornamento   ( aFascicoloSigeEsteso.getFascicoloSige().getCodOperatoreAggiornamento() );
    setCodUfficioAggiornamento     ( aFascicoloSigeEsteso.getFascicoloSige().getCodUfficioAggiornamento() );
    setDataAggiornamento           ( aFascicoloSigeEsteso.getFascicoloSige().getDataAggiornamento() );

    setCondizioneUpdate(aFascicoloSigeEsteso.getFascicoloSige().getIdFascicoloSige());
  }

  public void setDAOFromModelForUpdateCodStato(FascicoloSigeModel aModel) throws DAOException
  {
	setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
	setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
	setDataAggiornamento( aModel.getDataAggiornamento() );  
	setCodStatoFascicolo( aModel.getCodStatoFascicolo() );  

	setCondizioneUpdate(aModel.getIdFascicoloSige());
  }
  
  public void setDAOFromModelForUpdateDataDefinizione(FascicoloSigeModel aModel) throws DAOException
  {
	  setDataDefinizione( aModel.getDataDefinizione() );  
	  setCondizioneUpdate(aModel.getIdFascicoloSige());
  }
  
  public void setDAOFromModelForAggiornaIdFascicoloOrigine(FascicoloSigeModel aModel) throws DAOException
  {
	  setIdFascicoloSigeOrigine      ( aModel.getIdFascicoloSigeOrigine() );
	  setCodOperatoreAggiornamento   ( aModel.getCodOperatoreAggiornamento() );
	  setCodUfficioAggiornamento     ( aModel.getCodUfficioAggiornamento() );
	  setDataAggiornamento           ( aModel.getDataAggiornamento() );

	  setCondizioneUpdate(aModel.getIdFascicoloSige());
  }
  
  public void setDAOFromModelForAggiornaStatoDataDefinizioneFascicoloOrigine(FascicoloSigeModel aModel) throws DAOException
  {
      setCondizioneUpdate(aModel.getIdFascicoloSige());
  }


}