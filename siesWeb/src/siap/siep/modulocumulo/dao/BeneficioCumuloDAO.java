package siap.siep.modulocumulo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
//import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.modulocumulo.model.BeneficioCumuloModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: BeneficioCumuloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Beneficio_Cumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class BeneficioCumuloDAO extends SIAPTableDAO
{
	public BeneficioCumuloDAO (Connection con)
	{
			 super(con);
			 setTable("BENEFICIO_CUMULO");

			 //Settare la Sequence e i campi chiave

			 setSequenceField("ID_BENEFICIO_CUMULO", "BEN_CUM_SEQ");
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
			 setField("COD_OPERATORE_INSERIMENTO", STRING);
			 setField("DATA_INSERIMENTO", DATE);
			 setField("COD_UFFICIO_INSERIMENTO", STRING);
			 setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
			 setField("DATA_AGGIORNAMENTO", DATE);
			 setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
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
			 setField("NUM_ANNI_ADEMPIMENTO", BIG_DECIMAL);
			 setField("NUM_MESI_ADEMPIMENTO", BIG_DECIMAL);
			 setField("NUM_GIORNI_ADEMPIMENTO", BIG_DECIMAL); 
			 setField("RIF_DATA_IRREVOCABILITA", DATE);
			 setField("BEN_ID_BENEFICIO_CUMULO",BIG_DECIMAL);
			 
			 setField("ID_BENEFICIO_ORIGINE", BIG_DECIMAL);
			 setField("BEN_ID_BENEFICIO_ORIG", BIG_DECIMAL);
			 setField("ENTE_INCARICATO", STRING);
			 
			 setField("FLAG_STATO", STRING);
			 setField("MOTIVO_MODIFICA", STRING);
			 setField("TIT_ID_TITOLO_CUMULATO",BIG_DECIMAL);
			 setField("TIT_ID_TITOLO_CUMULO_COLLEGATO",BIG_DECIMAL);
	}

  //
  // METODI GET()
  //

			public BigDecimal 		 getIdBeneficioCumulo() 		throws DAOException	 { return getBigDecimal("ID_BENEFICIO_CUMULO"); }
			public String 			 getCodNaturaBeneficio() 		throws DAOException	 { return getString("COD_NATURA_BENEFICIO"); }
			public String 			 getCodTipoBeneficio() 			throws DAOException	 { return getString("COD_TIPO_BENEFICIO"); }
			public String 			 getCodTipoSospSubordinata() 	throws DAOException	 { return getString("COD_TIPO_SOSP_SUBORDINATA"); }
            public BigDecimal 		 getNumAnniReclusione() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_RECLUSIONE"); }
			public BigDecimal 		 getNumMesiReclusione() 		throws DAOException	 { return getBigDecimal("NUM_MESI_RECLUSIONE"); }
			public BigDecimal 		 getNumGiorniReclusione() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_RECLUSIONE"); }
			public BigDecimal 		 getImportoMulta() 				throws DAOException	 { return getBigDecimal("IMPORTO_MULTA"); }
            public BigDecimal 		 getNumAnniArresto() 		throws DAOException	 { return getBigDecimal("NUM_ANNI_ARRESTO"); }
            public BigDecimal 		 getNumMesiArresto() 		throws DAOException	 { return getBigDecimal("NUM_MESI_ARRESTO"); }
            public BigDecimal 		 getNumGiorniArresto() 		throws DAOException	 { return getBigDecimal("NUM_GIORNI_ARRESTO"); }
            public BigDecimal 		 getImportoAmmenda() 		throws DAOException	 { return getBigDecimal("IMPORTO_AMMENDA"); }
			public String 			 getCodDpr() 				throws DAOException	 { return getString("COD_DPR"); }
			public String 			 getNote() 					throws DAOException	 { return getString("NOTE"); }
			public String 			 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
			public Date 			 getDataInserimento() 				throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
			public String 			 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
			public String 			 getCodOperatoreAggiornamento() 	throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
			public Date 			 getDataAggiornamento() 			throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
			public String 			 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
            public String        getCodSottotipoBeneficio() 	throws DAOException  {return getString("COD_SOTTOTIPO_BENEFICIO");}
            public BigDecimal    getNumAnniSospensione() 		throws DAOException  {return getBigDecimal("NUM_ANNI_SOSPENSIONE");}       
            public BigDecimal    getNumGiorniPrestazione() 		throws DAOException  {return getBigDecimal("NUM_GIORNI_PRESTAZIONE");}       
            public BigDecimal    getNumMesiPrestazione() 		throws DAOException  {return getBigDecimal("NUM_MESI_PRESTAZIONE");}       
            public BigDecimal    getNumOreSettimanali() 		throws DAOException  {return getBigDecimal("NUM_ORE_SETTIMANALI");}            
            public String        getFlagFrequenzaSettimanale() 	throws DAOException  {return getString("FLAG_FREQUENZA_SETTIMANALE");}          
            public BigDecimal    getRifIdProvvedimento() 		throws DAOException  {return getBigDecimal("RIF_ID_PROVVEDIMENTO");}
    	    public String 		 getRifCodTipoProvvedimento() 	throws DAOException  { return getString("RIF_COD_TIPO_PROVVEDIMENTO"); }
 		    public Date 		 getRifDataProvvedimento() 		throws DAOException  { return getDate("RIF_DATA_PROVVEDIMENTO"); }
   	     	public String 		 getRifCodTipoAutoEmittente() 	throws DAOException  { return getString("RIF_COD_TIPO_AUTO_EMITTENTE"); }
		    public String 		 getRifCodLuogoEmittente() 		throws DAOException	 { return getString("RIF_COD_LUOGO_EMITTENTE"); }      
		    public String 		 getRifNumSezioneAutoEmittente()  	throws DAOException  	{ return getString("RIF_NUM_SEZIONE_AUTO_EMITTENTE"); }
		    public BigDecimal    getRifAnnoProvvedimento() 	 		throws DAOException  	{ return getBigDecimal("RIF_ANNO_PROVVEDIMENTO"); }
		    public String 		 getRifNumeroProvvedimento()  		throws DAOException  	{ return getString("RIF_NUMERO_PROVVEDIMENTO"); }			
	        public BigDecimal    getNumAnniAdempimento() 		throws DAOException   	{return getBigDecimal("NUM_ANNI_ADEMPIMENTO");}       
	        public BigDecimal    getNumMesiAdempimento() 		throws DAOException   	{return getBigDecimal("NUM_MESI_ADEMPIMENTO");}       
	        public BigDecimal    getNumGiorniAdempimento() 		throws DAOException 	{return getBigDecimal("NUM_GIORNI_ADEMPIMENTO");}
	        public Date 		 getRifDataIrrevocabilita() 	throws DAOException  	{ return getDate("RIF_DATA_IRREVOCABILITA"); }
	        
	        public BigDecimal    getBenIdBeneficioCumulo()  	throws DAOException  {return getBigDecimal("BEN_ID_BENEFICIO_CUMULO");}
	        public BigDecimal    getIdBeneficioOrigine()  		throws DAOException  {return getBigDecimal("ID_BENEFICIO_ORIGINE");}
	        public BigDecimal    getBenIdBeneficioOrig()  	throws DAOException  	 {return getBigDecimal("BEN_ID_BENEFICIO_ORIG");}
	        
	        public String		getEnteIncaricato() 		throws DAOException	 { return getString("ENTE_INCARICATO"); }
	        public String 		getFlagStato() 				throws DAOException	 { return getString("FLAG_STATO"); }
	        public String 		getMotivoModifica() 		throws DAOException	 { return getString("MOTIVO_MODIFICA"); }
	        public BigDecimal 	getTitIdTitoloCumulato()	throws DAOException	 { return getBigDecimal("TIT_ID_TITOLO_CUMULATO"); }
	        public BigDecimal 	getTitIdTitoloCumulatoCollegato() throws DAOException	 { return getBigDecimal("TIT_ID_TITOLO_CUMULO_COLLEGATO"); }


  //
  // METODI SET()
  //

			public void  	 setIdBeneficioCumulo(BigDecimal aValore ) 			 { setBigDecimal("ID_BENEFICIO_CUMULO", aValore); }
			public void  	 setCodNaturaBeneficio(String aValore ) 			 { setString("COD_NATURA_BENEFICIO", aValore); }
			public void  	 setCodTipoBeneficio(String aValore ) 			 	 { setString("COD_TIPO_BENEFICIO", aValore); }
			public void  	 setCodTipoSospSubordinata(String aValore ) 		 { setString("COD_TIPO_SOSP_SUBORDINATA", aValore); }
			public void  	 setNumAnniReclusione(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_RECLUSIONE", aValore); }
			public void  	 setNumMesiReclusione(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_RECLUSIONE", aValore); }
			public void  	 setNumGiorniReclusione(BigDecimal aValore ) 		 { setBigDecimal("NUM_GIORNI_RECLUSIONE", aValore); }
			public void  	 setImportoMulta(BigDecimal aValore ) 			 	 { setBigDecimal("IMPORTO_MULTA", aValore); }
            public void  	 setNumAnniArresto(BigDecimal aValore ) 			 { setBigDecimal("NUM_ANNI_ARRESTO", aValore); }
            public void  	 setNumMesiArresto(BigDecimal aValore ) 			 { setBigDecimal("NUM_MESI_ARRESTO", aValore); }
            public void  	 setNumGiorniArresto(BigDecimal aValore ) 			 { setBigDecimal("NUM_GIORNI_ARRESTO", aValore); }
            public void  	 setImportoAmmenda(BigDecimal aValore ) 			 { setBigDecimal("IMPORTO_AMMENDA", aValore); }
			public void  	 setCodDpr(String aValore ) 			 			{ setString("COD_DPR", aValore); }
			public void  	 setNote(String aValore ) 			 				{ setString("NOTE", aValore); }
			public void  	 setCodOperatoreInserimento(String aValore )		{ setString("COD_OPERATORE_INSERIMENTO", aValore); }
			public void  	 setDataInserimento(Date aValore ) 			 		{ setDate("DATA_INSERIMENTO", aValore); }
			public void  	 setCodUfficioInserimento(String aValore ) 			{ setString("COD_UFFICIO_INSERIMENTO", aValore); }
			public void  	 setCodOperatoreAggiornamento(String aValore ) 		{ setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
			public void  	 setDataAggiornamento(Date aValore ) 			 	{ setDate("DATA_AGGIORNAMENTO", aValore); }
			public void  	 setCodUfficioAggiornamento(String aValore ) 		{ setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
            public void      setCodSottotipoBeneficio(String aValore)     		{ setString("COD_SOTTOTIPO_BENEFICIO", aValore);}
            public void      setNumAnniSospensione(BigDecimal aValore)         	{ setBigDecimal("NUM_ANNI_SOSPENSIONE", aValore);}       
            public void      setNumMesiPrestazione(BigDecimal aValore)    		{ setBigDecimal("NUM_MESI_PRESTAZIONE", aValore);}       
            public void      setNumGiorniPrestazione(BigDecimal aValore)    	{ setBigDecimal("NUM_GIORNI_PRESTAZIONE", aValore);}       
            public void      setNumOreSettimanali(BigDecimal aValore)    		{ setBigDecimal("NUM_ORE_SETTIMANALI", aValore);}            
            public void      setFlagFrequenzaSettimanale(String aValore)    	{ setString("FLAG_FREQUENZA_SETTIMANALE", aValore);}          
            public void      setRifIdProvvedimento(BigDecimal aValore)    		{ setBigDecimal("RIF_ID_PROVVEDIMENTO", aValore);}
    	    public void 	 setRifCodTipoProvvedimento(String aValore) 	 	{  setString("RIF_COD_TIPO_PROVVEDIMENTO", aValore); }
		    public void 	 setRifDataProvvedimento(Date aValore) 		  	    {  setDate("RIF_DATA_PROVVEDIMENTO", aValore); }
   	        public void 	 setRifCodTipoAutoEmittente(String aValore) 	 	{  setString("RIF_COD_TIPO_AUTO_EMITTENTE", aValore); }
		    public void 	 setRifCodLuogoEmittente(String aValore) 	  		{  setString("RIF_COD_LUOGO_EMITTENTE", aValore); }      
		    public void 	 setRifNumSezioneAutoEmittente(String aValore)  	{  setString("RIF_NUM_SEZIONE_AUTO_EMITTENTE", aValore); }
		    public void      setRifAnnoProvvedimento(BigDecimal aValore) 	 	{  setBigDecimal("RIF_ANNO_PROVVEDIMENTO", aValore); }
		    public void 	 setRifNumeroProvvedimento(String aValore)  		{  setString("RIF_NUMERO_PROVVEDIMENTO", aValore); }			
	        public void      setNumAnniAdempimento(BigDecimal aValore)     		{ setBigDecimal("NUM_ANNI_ADEMPIMENTO", aValore);}       
	        public void      setNumMesiAdempimento(BigDecimal aValore)     		{ setBigDecimal("NUM_MESI_ADEMPIMENTO", aValore);}       
	        public void      setNumGiorniAdempimento(BigDecimal aValore)   		{ setBigDecimal("NUM_GIORNI_ADEMPIMENTO", aValore);}       
	        public void 	 setRifDataIrrevocabilita(Date aValore)	     		{  setDate("RIF_DATA_IRREVOCABILITA", aValore); }
	        
		    public void		setBenIdBeneficioCumulo(BigDecimal aValore)   		{	setBigDecimal("BEN_ID_BENEFICIO_CUMULO", aValore); }
	        public void    	setIdBeneficioOrigine(BigDecimal aValore)  			{	setBigDecimal("ID_BENEFICIO_ORIGINE", aValore); }
	        public void    	setBenIdBeneficioOrig(BigDecimal aValore)  	 		{	setBigDecimal("BEN_ID_BENEFICIO_ORIG", aValore); }
	        
	        public void		setEnteIncaricato(String aValore) 			 		{  	setString("ENTE_INCARICATO", aValore); }
	        public void 	setFlagStato(String aValore) 					 	{  	setString("FLAG_STATO", aValore); }
	        public void 	setMotivoModifica(String aValore) 			 		{  	setString("MOTIVO_MODIFICA", aValore); }
	        public void 	setTitIdTitoloCumulato(BigDecimal aValore)			{  	setBigDecimal("TIT_ID_TITOLO_CUMULATO", aValore); }
	        public void 	setTitIdTitoloCumulatoCollegato(BigDecimal aValore)	{  	setBigDecimal("TIT_ID_TITOLO_CUMULO_COLLEGATO", aValore); }
			

	public GenericModel getModel() throws DAOException
  			 {
 				 return new BeneficioCumuloModel(
								 getIdBeneficioCumulo(),
								 getCodNaturaBeneficio(),
								 "",
								 getCodTipoBeneficio(),
								 "",
								 getCodTipoSospSubordinata(),
								 "",
								 getCodSottotipoBeneficio(),
					             "",     
								 getNumAnniReclusione(),
								 getNumMesiReclusione(),
								 getNumGiorniReclusione(),
								 getImportoMulta(),
                                 getNumAnniArresto(),
                                 getNumMesiArresto(),
                                 getNumGiorniArresto(),
                                 getImportoAmmenda(),
								 getCodDpr(),
								 "",
								 getNote(),
					             getNumAnniSospensione(),           
					             getNumMesiPrestazione(),
					             getNumGiorniPrestazione(),      
					             getNumOreSettimanali(),
							     getNumAnniAdempimento(),
							     getNumMesiAdempimento(),
							     getNumGiorniAdempimento(),
							     getEnteIncaricato(), 
							     getFlagFrequenzaSettimanale(),
					             getRifIdProvvedimento(),
					    	     getRifCodTipoProvvedimento(),
					    	     "",
							     getRifDataProvvedimento(),
							     getRifDataIrrevocabilita(),
					   	         getRifCodTipoAutoEmittente(),
							     getRifCodLuogoEmittente(),
							     "",
							     "",
							     getRifNumSezioneAutoEmittente(),
							     getRifAnnoProvvedimento(),
							     getRifNumeroProvvedimento(),
							     getTitIdTitoloCumulato(),
							     getFlagStato(), 	
							     getMotivoModifica(),
							     getIdBeneficioOrigine(),  
							     getBenIdBeneficioOrig(), 
							     getBenIdBeneficioCumulo(),
							     getTitIdTitoloCumulatoCollegato(),
								 getCodOperatoreInserimento(),
								 getDataInserimento(),
								 getCodUfficioInserimento(),
								 getCodOperatoreAggiornamento(),
								 getDataAggiornamento(),
								 getCodUfficioAggiornamento()
								);
		}


	 public void 	 setDAOFromModel(BeneficioCumuloModel aModel) throws DAOException
  		{
				 setIdBeneficioCumulo( aModel.getIdBeneficioCumulo() );
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
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
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
			     setRifCodLuogoEmittente( aModel.getRifCodLuogoEmittente() );   
			     setRifNumSezioneAutoEmittente( aModel.getRifNumSezioneAutoEmittente() );
			     setRifAnnoProvvedimento( aModel.getRifAnnoProvvedimento() );
			     setRifNumeroProvvedimento( aModel.getRifNumeroProvvedimento() );
			     
	             setNumAnniAdempimento   ( aModel.getNumAnniAdempimento() );     
	             setNumMesiAdempimento   ( aModel.getNumMesiAdempimento() );     
	             setNumGiorniAdempimento ( aModel.getNumGiorniAdempimento() );
	             setRifDataIrrevocabilita( aModel.getRifDataIrrevocabilita() );
	             
	             setBenIdBeneficioCumulo(aModel.getBenIdBeneficioCumulo());
	             setIdBeneficioOrigine(aModel.getIdBeneficioOrigine());
	             setBenIdBeneficioOrig(aModel.getBenIdBeneficioOrig());
	             
	             setEnteIncaricato(aModel.getEnteIncaricato());
	             setFlagStato(aModel.getFlagStato());
	             setMotivoModifica(aModel.getMotivoModifica());
	             setTitIdTitoloCumulato(aModel.getTitIdTitoloCumulato());
	             setTitIdTitoloCumulatoCollegato(aModel.getTitIdTitoloCumulatoCollegato());
		}


	 public void 	 setDAOFromModelForUpdate(BeneficioCumuloModel aModel) throws DAOException
  		{
				 setIdBeneficioCumulo( aModel.getIdBeneficioCumulo() );
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
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
			
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
			     setRifCodLuogoEmittente( aModel.getRifCodLuogoEmittente() );   
			     setRifNumSezioneAutoEmittente( aModel.getRifNumSezioneAutoEmittente() );
			     setRifAnnoProvvedimento( aModel.getRifAnnoProvvedimento() );
			     setRifNumeroProvvedimento( aModel.getRifNumeroProvvedimento() );				 
	             setNumAnniAdempimento   ( aModel.getNumAnniAdempimento() );     
	             setNumMesiAdempimento   ( aModel.getNumMesiAdempimento() );     
	             setNumGiorniAdempimento ( aModel.getNumGiorniAdempimento() );
	             
			     setBenIdBeneficioCumulo(aModel.getBenIdBeneficioCumulo());
			     setIdBeneficioOrigine(aModel.getIdBeneficioOrigine());
			     setBenIdBeneficioOrig(aModel.getBenIdBeneficioOrig());
			     
			     setEnteIncaricato(aModel.getEnteIncaricato());
			     setFlagStato(aModel.getFlagStato());
			     setMotivoModifica(aModel.getMotivoModifica());
			     setTitIdTitoloCumulato(aModel.getTitIdTitoloCumulato());
			     
				 setCondizioneUpdate(aModel.getIdBeneficioCumulo());
				 setRifDataIrrevocabilita( aModel.getRifDataIrrevocabilita());
				 
				 setTitIdTitoloCumulatoCollegato(aModel.getTitIdTitoloCumulatoCollegato());
		}

    public void selCondizioneUpdateByIdTitolo( BigDecimal aIdTitolo)
    {
       String lCondizioni = " TIT_ID_TITOLO_CUMULATO = " + aIdTitolo;
  
       setCondition(lCondizioni);
    }
	  
	public void setCondizione(BeneficioCumuloModel aModel)
	{
		String lCondizioni = new String();

		boolean lInserito = false;
		if ( lInserito ) setCondition(lCondizioni);
	}

	public void setCondizioneUpdate(BigDecimal key)
	{
		setCondition(" ID_BENEFICIO_CUMULO = " + key );
	}
	
}
