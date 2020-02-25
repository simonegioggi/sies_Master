package siap.siep.beneficio.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.beneficio.model.BeneficioModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: BeneficioDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Beneficio</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class BeneficioDAO extends SIAPTableDAO
{
	public BeneficioDAO (Connection con)
	{
			 super(con);
			 setTable("BENEFICIO");

			 //Settare la Sequence e i campi chiave

			 setSequenceField("ID_BENEFICIO", "BEN_SEQ");
			 setField("COD_NATURA_BENEFICIO", STRING);
			 setField("COD_TIPO_BENEFICIO", STRING);
			 setField("COD_TIPO_SOSP_SUBORDINATA", STRING);
			 setField("NUM_ANNI_RECLUSIONE", BIG_DECIMAL);
			 setField("NUM_MESI_RECLUSIONE", BIG_DECIMAL);
			 setField("NUM_GIORNI_RECLUSIONE", BIG_DECIMAL);
			 setField("IMPORTO_MULTA", BIG_DECIMAL);
             setField("NUM_ANNI_ARRESTO", BIG_DECIMAL);
             setField("NUM_MESI_ARRESTO", BIG_DECIMAL);
             setField("NUM_GIORNI_ARRESTO", BIG_DECIMAL);
             setField("IMPORTO_AMMENDA", BIG_DECIMAL);
			 setField("COD_DPR", STRING);
			 setField("NOTE", STRING);
			 setField("INAPPLICABILITA_MIS_SICUREZZA", STRING);
			 setField("COD_OPERATORE_INSERIMENTO", STRING);
			 setField("DATA_INSERIMENTO", DATE);
			 setField("COD_UFFICIO_INSERIMENTO", STRING);
			 setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
			 setField("DATA_AGGIORNAMENTO", DATE);
			 setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
			 setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
			 setField("EVE_ID_EVENTO", BIG_DECIMAL);
			
			 
			 setField("COD_SOTTOTIPO_BENEFICIO", STRING);		
			 setField("NUM_ANNI_SOSPENSIONE", BIG_DECIMAL);
			 setField("NUM_MESI_PRESTAZIONE", BIG_DECIMAL);
			 setField("NUM_GIORNI_PRESTAZIONE", BIG_DECIMAL);
			 setField("NUM_ORE_SETTIMANALI", BIG_DECIMAL);
			 setField("FLAG_FREQUENZA_SETTIMANALE", STRING);
			 setField("RIF_ID_PROVVEDIMENTO", BIG_DECIMAL);
			 setField("RIF_COD_TIPO_PROVVEDIMENTO", STRING);
			 setField("RIF_DATA_PROVVEDIMENTO", DATE);
			 setField("RIF_COD_TIPO_AUTO_EMITTENTE", STRING);
			 setField("RIF_COD_LUOGO_EMITTENTE", STRING);
			 setField("RIF_NUM_SEZIONE_AUTO_EMITTENTE", STRING);
			 setField("RIF_ANNO_PROVVEDIMENTO", BIG_DECIMAL);
			 setField("RIF_NUMERO_PROVVEDIMENTO", STRING);
			 setField("BEN_ID_BENEFICIO",BIG_DECIMAL);
			 
			 setField("NUM_ANNI_ADEMPIMENTO", BIG_DECIMAL);
			 setField("NUM_MESI_ADEMPIMENTO", BIG_DECIMAL);
			 setField("NUM_GIORNI_ADEMPIMENTO", BIG_DECIMAL); 
			 
			 setField("RIF_DATA_IRREVOCABILITA", DATE);
			 
	}


  //
  // METODI GET()
  //

			public BigDecimal 		 getIdBeneficio() 		throws DAOException	 { return getBigDecimal("ID_BENEFICIO"); }
			public String 			 getCodNaturaBeneficio() 		throws DAOException	 { return getString("COD_NATURA_BENEFICIO"); }
			public String 			 getCodTipoBeneficio() 		throws DAOException	 { return getString("COD_TIPO_BENEFICIO"); }
			public String 			 getCodTipoSospSubordinata() 		throws DAOException	 { return getString("COD_TIPO_SOSP_SUBORDINATA"); }
            public BigDecimal 		 getNumAnniReclusione() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_RECLUSIONE"); }
			public BigDecimal 		 getNumMesiReclusione() 		throws DAOException	 { return getBigDecimal("NUM_MESI_RECLUSIONE"); }
			public BigDecimal 		 getNumGiorniReclusione() 	throws DAOException	 { return getBigDecimal("NUM_GIORNI_RECLUSIONE"); }
			public BigDecimal 		 getImportoMulta() 		throws DAOException	 { return getBigDecimal("IMPORTO_MULTA"); }
            public BigDecimal 		 getNumAnniArresto() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_ARRESTO"); }
            public BigDecimal 		 getNumMesiArresto() 		throws DAOException	 { return getBigDecimal("NUM_MESI_ARRESTO"); }
            public BigDecimal 		 getNumGiorniArresto() 	throws DAOException	 { return getBigDecimal("NUM_GIORNI_ARRESTO"); }
            public BigDecimal 		 getImportoAmmenda() 		throws DAOException	 { return getBigDecimal("IMPORTO_AMMENDA"); }
			public String 			 getCodDpr() 		throws DAOException	 { return getString("COD_DPR"); }
			public String 			 getNote() 		throws DAOException	 { return getString("NOTE"); }
			public String 			 getInapplicabilitaMisSicurezza() 		throws DAOException	 { return getString("INAPPLICABILITA_MIS_SICUREZZA"); }
			public String 			 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
			public Date 			 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
			public String 			 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
			public String 			 getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
			public Date 			 getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
			public String 			 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
			public BigDecimal 		 getFasSieIdFascicoloSiep() 		throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
			public BigDecimal 		 getEveIdEvento() 		throws DAOException	 { return getBigDecimal("EVE_ID_EVENTO"); }

            public String        getCodSottotipoBeneficio() throws DAOException   {return getString("COD_SOTTOTIPO_BENEFICIO");}
            public BigDecimal    getNumAnniSospensione() throws DAOException       {return getBigDecimal("NUM_ANNI_SOSPENSIONE");}       
     
            public BigDecimal    getNumGiorniPrestazione() throws DAOException  {return getBigDecimal("NUM_GIORNI_PRESTAZIONE");}       
            public BigDecimal    getNumMesiPrestazione() throws DAOException  {return getBigDecimal("NUM_MESI_PRESTAZIONE");}       

            public BigDecimal    getNumOreSettimanali() throws DAOException  {return getBigDecimal("NUM_ORE_SETTIMANALI");}            
            public String        getFlagFrequenzaSettimanale() throws DAOException  {return getString("FLAG_FREQUENZA_SETTIMANALE");}          
            public BigDecimal    getRifIdProvvedimento() throws DAOException  {return getBigDecimal("RIF_ID_PROVVEDIMENTO");}
    	    public String 		 getRifCodTipoProvvedimento() 		 throws DAOException  	 { return getString("RIF_COD_TIPO_PROVVEDIMENTO"); }
 		    public Date 		 getRifDataProvvedimento() 		 throws DAOException  	     { return getDate("RIF_DATA_PROVVEDIMENTO"); }
   	     	public String 		 getRifCodTipoAutoEmittente() 	 throws DAOException  	 { return getString("RIF_COD_TIPO_AUTO_EMITTENTE"); }
		    public String 		 getRifCodLuogoAutoEmittente() 		 throws DAOException  	 { return getString("RIF_COD_LUOGO_EMITTENTE"); }      
		    public String 		 getRifNumSezioneAutoEmittente()  throws DAOException  			 { return getString("RIF_NUM_SEZIONE_AUTO_EMITTENTE"); }
		    public BigDecimal    getRifAnnoProvvedimento() 	 throws DAOException  		 { return getBigDecimal("RIF_ANNO_PROVVEDIMENTO"); }
		    public String 		 getRifNumeroProvvedimento()  throws DAOException  			 { return getString("RIF_NUMERO_PROVVEDIMENTO"); }			
	
		    public BigDecimal    getBenIdBeneficio()  throws DAOException  {return getBigDecimal("BEN_ID_BENEFICIO");}
	        public BigDecimal    getNumAnniAdempimento() throws DAOException   {return getBigDecimal("NUM_ANNI_ADEMPIMENTO");}       
	        public BigDecimal    getNumMesiAdempimento() throws DAOException   {return getBigDecimal("NUM_MESI_ADEMPIMENTO");}       
	        public BigDecimal    getNumGiorniAdempimento() throws DAOException {return getBigDecimal("NUM_GIORNI_ADEMPIMENTO");}
	        
	        public Date 		 getRifDataIrrevocabilita() 		 throws DAOException  	     { return getDate("RIF_DATA_IRREVOCABILITA"); }
														
												
				

  //
  // METODI SET()
  //

			public void  	 setIdBeneficio(BigDecimal aValore ) 			 { setBigDecimal("ID_BENEFICIO", aValore); }
			public void  	 setCodNaturaBeneficio(String aValore ) 			 { setString("COD_NATURA_BENEFICIO", aValore); }
			public void  	 setCodTipoBeneficio(String aValore ) 			 { setString("COD_TIPO_BENEFICIO", aValore); }
			public void  	 setCodTipoSospSubordinata(String aValore ) 			 { setString("COD_TIPO_SOSP_SUBORDINATA", aValore); }
			public void  	 setNumAnniReclusione(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_RECLUSIONE", aValore); }
			public void  	 setNumMesiReclusione(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_RECLUSIONE", aValore); }
			public void  	 setNumGiorniReclusione(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_RECLUSIONE", aValore); }
			public void  	 setImportoMulta(BigDecimal aValore ) 			 { setBigDecimal("IMPORTO_MULTA", aValore); }
            public void  	 setNumAnniArresto(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_ARRESTO", aValore); }
            public void  	 setNumMesiArresto(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_ARRESTO", aValore); }
            public void  	 setNumGiorniArresto(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_ARRESTO", aValore); }
            public void  	 setImportoAmmenda(BigDecimal aValore ) 			 { setBigDecimal("IMPORTO_AMMENDA", aValore); }
			public void  	 setCodDpr(String aValore ) 			 { setString("COD_DPR", aValore); }
			public void  	 setNote(String aValore ) 			 { setString("NOTE", aValore); }
			public void  	 setInapplicabilitaMisSicurezza(String aValore ) 			 { setString("INAPPLICABILITA_MIS_SICUREZZA", aValore); }
			public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
			public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
			public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
			public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
			public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
			public void  	 setFasSieIdFascicoloSiep(BigDecimal aValore ) 			 { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
			public void  	 setEveIdEvento(BigDecimal aValore ) 			 { setBigDecimal("EVE_ID_EVENTO", aValore); }

            public void      setCodSottotipoBeneficio(String aValore)     { setString("COD_SOTTOTIPO_BENEFICIO", aValore);}
            public void      setNumAnniSospensione(BigDecimal aValore)         { setBigDecimal("NUM_ANNI_SOSPENSIONE", aValore);}       
            public void      setNumMesiPrestazione(BigDecimal aValore)    { setBigDecimal("NUM_MESI_PRESTAZIONE", aValore);}       
            public void      setNumGiorniPrestazione(BigDecimal aValore)    { setBigDecimal("NUM_GIORNI_PRESTAZIONE", aValore);}       
            public void      setNumOreSettimanali(BigDecimal aValore)    { setBigDecimal("NUM_ORE_SETTIMANALI", aValore);}            
            public void      setFlagFrequenzaSettimanale(String aValore)    { setString("FLAG_FREQUENZA_SETTIMANALE", aValore);}          
            public void      setRifIdProvvedimento(BigDecimal aValore)    { setBigDecimal("RIF_ID_PROVVEDIMENTO", aValore);}
    	    public void 	 setRifCodTipoProvvedimento(String aValore) 		 	 {  setString("RIF_COD_TIPO_PROVVEDIMENTO", aValore); }
		    public void 	 setRifDataProvvedimento(Date aValore) 		  	     {  setDate("RIF_DATA_PROVVEDIMENTO", aValore); }
   	        public void 	 setRifCodTipoAutoEmittente(String aValore) 	 	 {  setString("RIF_COD_TIPO_AUTO_EMITTENTE", aValore); }
		    public void 	 setRifCodLuogoAutoEmittente(String aValore) 		   	 {  setString("RIF_COD_LUOGO_EMITTENTE", aValore); }      
		    public void 	 setRifNumSezioneAutoEmittente(String aValore)   			 {  setString("RIF_NUM_SEZIONE_AUTO_EMITTENTE", aValore); }
		    public void      setRifAnnoProvvedimento(BigDecimal aValore) 	 		 {  setBigDecimal("RIF_ANNO_PROVVEDIMENTO", aValore); }
		    public void 	 setRifNumeroProvvedimento(String aValore)  			 {  setString("RIF_NUMERO_PROVVEDIMENTO", aValore); }			
	
			
	        public void      setNumAnniAdempimento(BigDecimal aValore)     { setBigDecimal("NUM_ANNI_ADEMPIMENTO", aValore);}       
	        public void      setNumMesiAdempimento(BigDecimal aValore)     { setBigDecimal("NUM_MESI_ADEMPIMENTO", aValore);}       
	        public void      setNumGiorniAdempimento(BigDecimal aValore)   { setBigDecimal("NUM_GIORNI_ADEMPIMENTO", aValore);}       
														
		    public void      setBenIdBeneficio(BigDecimal aValore)         {setBigDecimal("BEN_ID_BENEFICIO", aValore);}
		    public void 	 setRifDataIrrevocabilita(Date aValore) 		  	     {  setDate("RIF_DATA_IRREVOCABILITA", aValore); }
			

	public GenericModel getModel() throws DAOException
  			 {
 				 return new BeneficioModel(
								 getIdBeneficio() ,
								 getCodNaturaBeneficio() ,
								 "",
								 getCodTipoBeneficio() ,
								 "",
								 getCodTipoSospSubordinata() ,
								 "",
								 getNumAnniReclusione() ,
								 getNumMesiReclusione() ,
								 getNumGiorniReclusione() ,
								 getImportoMulta() ,
                                 getNumAnniArresto() ,
                                 getNumMesiArresto() ,
                                 getNumGiorniArresto() ,
                                 getImportoAmmenda() ,
								 getCodDpr() ,
								 "",
								 getNote() ,
								 getInapplicabilitaMisSicurezza() ,
								 getCodOperatoreInserimento() ,
								 getDataInserimento() ,
								 getCodUfficioInserimento() ,
								 "",
								 getCodOperatoreAggiornamento() ,
								 getDataAggiornamento() ,
								 getCodUfficioAggiornamento() ,
								 "",
								 getFasSieIdFascicoloSiep() ,
								 getEveIdEvento(),			 
					             getCodSottotipoBeneficio(),
					             "",     
					             getNumAnniSospensione(),           
					             getNumGiorniPrestazione(),
					             getNumMesiPrestazione(),      
					             getNumOreSettimanali(),          
					             getFlagFrequenzaSettimanale(),        
					             getRifIdProvvedimento(),
					    	     getRifCodTipoProvvedimento(),
					    	     "",
							     getRifDataProvvedimento(),
					   	         getRifCodTipoAutoEmittente(),
							     getRifCodLuogoAutoEmittente(),
							     "",
							     "",
							     getRifNumSezioneAutoEmittente(),
							     getRifAnnoProvvedimento(),
							     getRifNumeroProvvedimento(),			
							     getNumAnniAdempimento(),
							     getNumMesiAdempimento(),
							     getNumGiorniAdempimento(),
							     getBenIdBeneficio(),
							     getRifDataIrrevocabilita()
								);
		}


	 public void 	 setDAOFromModel(BeneficioModel aModel) throws DAOException
  		{
				 setIdBeneficio( aModel.getIdBeneficio() );
				 setCodNaturaBeneficio( aModel.getCodNaturaBeneficio() );
				 setCodTipoBeneficio( aModel.getCodTipoBeneficio() );
				 setCodTipoSospSubordinata( aModel.getCodTipoSospSubordinata() );
				 setNumAnniReclusione( aModel.getNumAnniReclusione() );
				 setNumMesiReclusione( aModel.getNumMesiReclusione() );
				 setNumGiorniReclusione( aModel.getNumGiorniReclusione() );
				 setImportoMulta( aModel.getImportoMulta() );
                 setNumAnniArresto( aModel.getNumAnniArresto() );
                 setNumMesiArresto( aModel.getNumMesiArresto() );
                 setNumGiorniArresto( aModel.getNumGiorniArresto() );
                 setImportoAmmenda( aModel.getImportoAmmenda() );
				 setCodDpr( aModel.getCodDpr() );
				 setNote( aModel.getNote() );
				 setInapplicabilitaMisSicurezza( aModel.getInapplicabilitaMisSicurezza() );
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
				 setEveIdEvento( aModel.getEveIdEvento() );
				 
	             setCodSottotipoBeneficio( aModel.getCodSottotipoBeneficio() );     
	             setNumAnniSospensione( aModel.getNumAnniSospensione() );     
	             setNumMesiPrestazione( aModel.getNumMesiPrestazione() );     
	             setNumGiorniPrestazione( aModel.getNumGiorniPrestazione() );    
	             setNumOreSettimanali( aModel.getNumOreSettimanali() );        
	             setFlagFrequenzaSettimanale( aModel.getFlagFrequenzaSettimanale() );  
	             setRifIdProvvedimento( aModel.getRifIdProvvedimento() );
	    	     setRifCodTipoProvvedimento( aModel.getRifCodTipoProvvedimento() );
			     setRifDataProvvedimento( aModel.getRifDataProvvedimento());
	   	         setRifCodTipoAutoEmittente( aModel.getRifCodTipoAutoEmittente() );
			     setRifCodLuogoAutoEmittente( aModel.getRifCodLuogoAutoEmittente() );   
			     setRifNumSezioneAutoEmittente( aModel.getRifNumSezioneAutoEmittente() );
			     setRifAnnoProvvedimento( aModel.getRifAnnoProvvedimento() );
			     setRifNumeroProvvedimento( aModel.getRifNumeroProvvedimento() );
			     setBenIdBeneficio(aModel.getBenIdBeneficio());
			     
	             setNumAnniAdempimento   ( aModel.getNumAnniAdempimento() );     
	             setNumMesiAdempimento   ( aModel.getNumMesiAdempimento() );     
	             setNumGiorniAdempimento ( aModel.getNumGiorniAdempimento() );
	             
	             setRifDataIrrevocabilita( aModel.getRifDataIrrevocabilita() );
		}


	 public void 	 setDAOFromModelForUpdate(BeneficioModel aModel) throws DAOException
  		{
				 setIdBeneficio( aModel.getIdBeneficio() );
				 setCodNaturaBeneficio( aModel.getCodNaturaBeneficio() );
				 setCodTipoBeneficio( aModel.getCodTipoBeneficio() );
				 setCodTipoSospSubordinata( aModel.getCodTipoSospSubordinata() );
                 setNumAnniReclusione( aModel.getNumAnniReclusione() );
                 setNumMesiReclusione( aModel.getNumMesiReclusione() );
                 setNumGiorniReclusione( aModel.getNumGiorniReclusione() );
                 setImportoMulta( aModel.getImportoMulta() );
                 setNumAnniArresto( aModel.getNumAnniArresto() );
                 setNumMesiArresto( aModel.getNumMesiArresto() );
                 setNumGiorniArresto( aModel.getNumGiorniArresto() );
                 setImportoAmmenda( aModel.getImportoAmmenda() );
				 setCodDpr( aModel.getCodDpr() );
				 setNote( aModel.getNote() );
				 setInapplicabilitaMisSicurezza( aModel.getInapplicabilitaMisSicurezza() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
				 setEveIdEvento( aModel.getEveIdEvento() );
			
	             setCodSottotipoBeneficio( aModel.getCodSottotipoBeneficio() );     
	             setNumAnniSospensione( aModel.getNumAnniSospensione() );     
	             setNumMesiPrestazione( aModel.getNumMesiPrestazione() );     
	             setNumGiorniPrestazione( aModel.getNumGiorniPrestazione() );    
	             setNumOreSettimanali( aModel.getNumOreSettimanali() );        
	             setFlagFrequenzaSettimanale( aModel.getFlagFrequenzaSettimanale() );  
	             setRifIdProvvedimento( aModel.getRifIdProvvedimento() );
	    	     setRifCodTipoProvvedimento( aModel.getRifCodTipoProvvedimento() );
			     setRifDataProvvedimento( aModel.getRifDataProvvedimento());
	   	         setRifCodTipoAutoEmittente( aModel.getRifCodTipoAutoEmittente() );
			     setRifCodLuogoAutoEmittente( aModel.getRifCodLuogoAutoEmittente() );   
			     setRifNumSezioneAutoEmittente( aModel.getRifNumSezioneAutoEmittente() );
			     setRifAnnoProvvedimento( aModel.getRifAnnoProvvedimento() );
			     setRifNumeroProvvedimento( aModel.getRifNumeroProvvedimento() );				 
	             setNumAnniAdempimento   ( aModel.getNumAnniAdempimento() );     
	             setNumMesiAdempimento   ( aModel.getNumMesiAdempimento() );     
	             setNumGiorniAdempimento ( aModel.getNumGiorniAdempimento() );   
			     setBenIdBeneficio(aModel.getBenIdBeneficio());		     
			     
				 setCondizioneUpdate(aModel.getIdBeneficio());
				 setRifDataIrrevocabilita( aModel.getRifDataIrrevocabilita());
		}


	public void setCondizione(BeneficioModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition(" ID_BENEFICIO = " + key );
		 }
	
	public void setCondizioneFascSiep(BigDecimal key)
 	{
      String lCondizioni = " FAS_SIE_ID_FASCICOLO_SIEP = " + key;

     setCondition(lCondizioni);
    }

	

}
