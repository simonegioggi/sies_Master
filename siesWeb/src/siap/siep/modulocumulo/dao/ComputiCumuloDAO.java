package siap.siep.modulocumulo.dao;

/**
* <p>Title: ComputiCumuloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella ComputiCumulo</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;
import f3b.util.DateUtils;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

import siap.siep.modulocumulo.model.ComputiCumuloModel;

public class ComputiCumuloDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public ComputiCumuloDAO (Connection con) {
    super(con);
    setTable("COMPUTI_CUMULO");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_COMPUTI_CUMULO","COMPUTI_CUMULO_SEQ");

    //setField("ID_COMPUTI_CUMULO", BIG_DECIMAL);
    setField("COD_TIPO_ANNOTAZIONE"       , STRING);
    setField("COD_CAUSALE_COMPUTO"        , STRING);
    setField("FLAG_PIU_MENO"              , STRING);
    setField("DATA_RECLUSIONE_DA"         , DATE);
    setField("DATA_RECLUSIONE_A"          , DATE);
    setField("NUM_ANNI_RECLUSIONE"        , BIG_DECIMAL);
    setField("NUM_MESI_RECLUSIONE"        , BIG_DECIMAL);
    setField("NUM_GIORNI_RECLUSIONE"      , BIG_DECIMAL);
    setField("IMPORTO_MULTA"              , BIG_DECIMAL);
    setField("DATA_ARRESTO_DA"            , DATE);
    setField("DATA_ARRESTO_A"             , DATE);
    setField("NUM_ANNI_ARRESTO"           , BIG_DECIMAL);
    setField("NUM_MESI_ARRESTO"           , BIG_DECIMAL);
    setField("NUM_GIORNI_ARRESTO"         , BIG_DECIMAL);
    setField("IMPORTO_AMMENDA"            , BIG_DECIMAL);
    
    setField("NUM_GIORNI_MAP"             , BIG_DECIMAL);
    
    setField("COD_DPR"                    , STRING);
    setField("DATA_RICHIESTA"             , DATE);
    setField("NOTE"                       , STRING);
    
    setField("COD_TIPO_MISURA"               , STRING);
    setField("IST_DET_ID_ISTITUTO_DETENZIONE", STRING);
    setField("ALTRO_LUOGO_DETENZIONE"        , STRING);
    
    setField("FLAG_STATO"                 , STRING);
    setField("MOTIVO_MODIFICA"            , STRING);
    setField("TIT_ID_TITOLO_CUMULATO"     , BIG_DECIMAL);
    setField("DAT_ID_DATI_FINALI_CUMULO"  , BIG_DECIMAL);
    setField("ISTR_ID_ISTRUTTORIA_CUMULO" , BIG_DECIMAL);
    setField("STAT_ID_STATO_ESEC_TIT_CUM" , BIG_DECIMAL);
    
    setField("DATA_EMISSIONE_PROVV"       , DATE);
    setField("DATA_RICEZIONE_PROVV"       , DATE);
    setField("ANNO_PROVV"                 , BIG_DECIMAL);
    setField("PROGR_PROVV"                , BIG_DECIMAL);
    setField("COD_UFFICIO_EMITTENTE_PROVV" , STRING);
    setField("COD_LUOGO_UFFICIO_PROVV"    , STRING);
    setField("SEZIONE_PROVV"    		  , STRING);
    setField("COD_TIPO_PROVV"    		  , STRING);

    setField("REA_ID_REATO_CUM"           , BIG_DECIMAL);

    setField("COD_FONTE"				  , STRING);
    setField("ANNO_FONTE"		          , BIG_DECIMAL);
    setField("NUMERO_FONTE"	    		  , STRING);
    setField("COD_SOTTONUMERAZIONE"		  , STRING);
    setField("COMMA"		    		  , STRING);
    setField("LETTERA"		    		  , STRING);
    setField("NUMERO"		    		  , STRING);
    setField("ARTICOLO"		    		  , STRING);

    setField("COD_TIPO_REGISTRO_ORDINANZA", STRING);
    setField("DATA_SOSPENSIONE_INTERRUZIONE", DATE);
    setField("COD_OGGETTO_DECISIONE"	  , STRING);

    setField("PROTOCOLLO"				  , STRING);
    setField("ALTRA_AUTORITA"			  , STRING);
    setField("ALTRO_LUOGO"				  , STRING);

    setField("LUOGO_ESEC_MISURA"		  , STRING);
    setField("DATA_INIZIO_MISURA"         , DATE);
    setField("DATA_FINE_MISURA"           , DATE);
    setField("NUM_ANNI_MISURA"            , BIG_DECIMAL);
    setField("NUM_MESI_MISURA"            , BIG_DECIMAL);
    setField("NUM_GIORNI_MISURA"          , BIG_DECIMAL);

    setField("DATA_INIZIO_REVOCA"         , DATE);
    setField("NUM_ANNI_REVOCA_RECLUSIONE" , BIG_DECIMAL);
    setField("NUM_MESI_REVOCA_RECLUSIONE" , BIG_DECIMAL);
    setField("NUM_GIORNI_REVOCA_RECLUSIONE", BIG_DECIMAL);
    setField("NUM_ANNI_REVOCA_ARRESTO" 	  , BIG_DECIMAL);
    setField("NUM_MESI_REVOCA_ARRESTO" 	  , BIG_DECIMAL);
    setField("NUM_GIORNI_REVOCA_ARRESTO"  , BIG_DECIMAL);

    setField("DATA_INGRESSO_ISTITUTO"     , DATE);
    setField("DATA_SCARCERAZIONE"	      , DATE);
    setField("FLAG_DECISIONE_TRIBUNALE"   , STRING);
    setField("COD_TDS_COMPETENTE"		  , STRING);
    
    setField("COD_OPERATORE_INSERIMENTO"  , STRING);
    setField("DATA_INSERIMENTO"           , DATE);
    setField("COD_UFFICIO_INSERIMENTO"    , STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO"         , DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO"  , STRING);
 
// FUNGIBILITA
    setField("ANNO_PROC"                 , BIG_DECIMAL);
    setField("PROGR_PROC"                , BIG_DECIMAL);
    
    setField("ANNO_BDMC"                 , BIG_DECIMAL);
    setField("NUMERO_BDMC"               , STRING);
    setField("ANNO_REGE"                 , BIG_DECIMAL);
    setField("NUMERO_REGE"               , STRING);
    setField("TIPO_REGE"                 , STRING);
    setField("TIPO_AUT_REGE"			 , STRING);
    setField("COD_SEDE_REGE"    		 , STRING);
    setField("DATA_REGE"          		 , DATE);
 
    setField("ANNO_REGE_PM"              , BIG_DECIMAL);
    setField("NUMERO_REGE_PM"            , STRING);
    setField("COD_TIPO_UFFICIO_PM"		 , STRING);
    setField("COD_SEDE_UFFICIO_PM"    	 , STRING);
    
    setField("CHIAVE_ANNO_SIEP"          , BIG_DECIMAL);
    setField("CHIAVE_NUMERO_SIEP"        , BIG_DECIMAL);
    setField("CHIAVE_UFFICIO_SIEP"       , STRING);
    
    setField("ANNO_SENTENZA"             	, BIG_DECIMAL);
    setField("NUMERO_SENTENZA"           	, STRING);
    setField("DATA_SENTENZA"          	 	, DATE);
    setField("COD_TIPO_AUT_EMITT_SENTENZA" 	, STRING);
    setField("COD_LUOGO_EMITTENTE_SENTENZA" , STRING);
    
    // MEV_2025-48 - ALTRO – Benefici con anticipazione effetti
    setField("FLAG_APP_PROVVISORIA" , STRING);
    
  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  BigDecimal  getIdComputiCumulo()            throws DAOException  { return getBigDecimal ("ID_COMPUTI_CUMULO"          ); } 
  public  String      getCodTipoAnnotazione()         throws DAOException  { return getString     ("COD_TIPO_ANNOTAZIONE"       ); } 
  public  String      getCodCausaleComputo()          throws DAOException  { return getString     ("COD_CAUSALE_COMPUTO"        ); } 
  public  String      getFlagPiuMeno()                throws DAOException  { return getString     ("FLAG_PIU_MENO"              ); } 
  public  Date        getDataReclusioneDa()           throws DAOException  { return getDate       ("DATA_RECLUSIONE_DA"         ); } 
  public  Date        getDataReclusioneA()            throws DAOException  { return getDate       ("DATA_RECLUSIONE_A"          ); } 
  public  BigDecimal  getNumAnniReclusione()          throws DAOException  { return getBigDecimal ("NUM_ANNI_RECLUSIONE"        ); } 
  public  BigDecimal  getNumMesiReclusione()          throws DAOException  { return getBigDecimal ("NUM_MESI_RECLUSIONE"        ); } 
  public  BigDecimal  getNumGiorniReclusione()        throws DAOException  { return getBigDecimal ("NUM_GIORNI_RECLUSIONE"      ); } 
  public  BigDecimal  getImportoMulta()               throws DAOException  { return getBigDecimal ("IMPORTO_MULTA"              ); } 
  public  Date        getDataArrestoDa()              throws DAOException  { return getDate       ("DATA_ARRESTO_DA"            ); } 
  public  Date        getDataArrestoA()               throws DAOException  { return getDate       ("DATA_ARRESTO_A"             ); } 
  public  BigDecimal  getNumAnniArresto()             throws DAOException  { return getBigDecimal ("NUM_ANNI_ARRESTO"           ); } 
  public  BigDecimal  getNumMesiArresto()             throws DAOException  { return getBigDecimal ("NUM_MESI_ARRESTO"           ); } 
  public  BigDecimal  getNumGiorniArresto()           throws DAOException  { return getBigDecimal ("NUM_GIORNI_ARRESTO"         ); } 
  public  BigDecimal  getImportoAmmenda()             throws DAOException  { return getBigDecimal ("IMPORTO_AMMENDA"            ); } 
  
  public  BigDecimal  getNumGiorniMap()               throws DAOException  { return getBigDecimal ("NUM_GIORNI_MAP"            ); } 
  
  public  String      getCodDpr()                     throws DAOException  { return getString     ("COD_DPR"                    ); }
  public  Date        getDataRichiesta()              throws DAOException  { return getDate       ("DATA_RICHIESTA"             ); } 
  public  String      getNote()                       throws DAOException  { return getString     ("NOTE"                       ); } 
  
  public  String      getCodTipoMisura()              throws DAOException  { return getString     ("COD_TIPO_MISURA"               ); } 
  public  String      getIstDetIdIstitutoDetenzione() throws DAOException  { return getString     ("IST_DET_ID_ISTITUTO_DETENZIONE"); } 
  public  String      getAltroLuogoDetenzione()       throws DAOException  { return getString     ("ALTRO_LUOGO_DETENZIONE"        ); } 
  
  public  String      getFlagStato()                  throws DAOException  { return getString     ("FLAG_STATO"                 ); } 
  public  String      getMotivoModifica()             throws DAOException  { return getString     ("MOTIVO_MODIFICA"            ); } 
  
  public  BigDecimal  getTitIdTitoloCumulato()        throws DAOException  { return getBigDecimal ("TIT_ID_TITOLO_CUMULATO"     ); } 
  public  BigDecimal  getDatIdDatiFinaliCumulo()      throws DAOException  { return getBigDecimal ("DAT_ID_DATI_FINALI_CUMULO"  ); } 
  public  BigDecimal  getIstrIdIstruttoriaCumulo()    throws DAOException  { return getBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO" ); } 
  public  BigDecimal  getStatIdStatoEsecTitCum()      throws DAOException  { return getBigDecimal ("STAT_ID_STATO_ESEC_TIT_CUM" ); } 
  
  public  Date        getDataEmissioneProvv()         throws DAOException  { return getDate       ("DATA_EMISSIONE_PROVV"       ); } 
  public  Date        getDataRicezioneProvv()         throws DAOException  { return getDate       ("DATA_RICEZIONE_PROVV"       ); } 
  public  BigDecimal  getAnnoProvv()               	  throws DAOException  { return getBigDecimal ("ANNO_PROVV"                 ); } 
  public  BigDecimal  getProgrProvv()              	  throws DAOException  { return getBigDecimal ("PROGR_PROVV"                ); } 
  public  String      getCodUfficioEmittenteProvv()   throws DAOException  { return getString     ("COD_UFFICIO_EMITTENTE_PROVV" ); } 
  public  String      getCodLuogoUfficioProvv()       throws DAOException  { return getString     ("COD_LUOGO_UFFICIO_PROVV"    ); } 
  public  String      getSezioneProvv()       		  throws DAOException  { return getString     ("SEZIONE_PROVV"    			); } 
  public  String      getCodTipoProvv()       		  throws DAOException  { return getString     ("COD_TIPO_PROVV"    			); } 

  public  BigDecimal  getReaIdReatoCum()          	  throws DAOException  { return getBigDecimal ("REA_ID_REATO_CUM"           ); } 

  public String  	  getCodFonte()   				  throws DAOException  { return getString     ("COD_FONTE"	    			); }
  public BigDecimal   getAnnoFonte()         		  throws DAOException  { return getBigDecimal ("ANNO_FONTE"					); }
  public String  	  getNumeroFonte()				  throws DAOException  { return getString     ("NUMERO_FONTE"	    		); }
  public String  	  getCodSottonumerazione()		  throws DAOException  { return getString     ("COD_SOTTONUMERAZIONE"	    ); }
  public String  	  getComma()		   			  throws DAOException  { return getString     ("COMMA"	    				); }
  public String  	  getLettera()	   				  throws DAOException  { return getString     ("LETTERA"	    			); }
  public String  	  getNumero()	   				  throws DAOException  { return getString     ("NUMERO"	    				); }
  public String  	  getArticolo()	   				  throws DAOException  { return getString     ("ARTICOLO"	    			); }

  public String  	  getCodTipoRegistroOrdinanza()	  throws DAOException  { return getString     ("COD_TIPO_REGISTRO_ORDINANZA"); }
  public  Date        getDataSospensioneInterruzione()throws DAOException  { return getDate       ("DATA_SOSPENSIONE_INTERRUZIONE"); } 
  public String  	  getCodOggettoDecisione()		  throws DAOException  { return getString     ("COD_OGGETTO_DECISIONE"		); }

  public String  	  getProtocollo()				  throws DAOException  { return getString     ("PROTOCOLLO"					); }
  public String  	  getAltraAutorita()			  throws DAOException  { return getString     ("ALTRA_AUTORITA"				); }
  public String  	  getAltroLuogo()				  throws DAOException  { return getString     ("ALTRO_LUOGO"				); }

  public String	  	 getLuogoEsecMisura()		 	  throws DAOException  { return getString     ("LUOGO_ESEC_MISURA"			); }
  public Date        getDataInizioMisura()            throws DAOException  { return getDate       ("DATA_INIZIO_MISURA"         ); } 
  public Date        getDataFineMisura()	          throws DAOException  { return getDate       ("DATA_FINE_MISURA"	        ); } 
  public BigDecimal  getNumAnniMisura()            	  throws DAOException  { return getBigDecimal ("NUM_ANNI_MISURA"	        ); } 
  public BigDecimal  getNumMesiMisura()            	  throws DAOException  { return getBigDecimal ("NUM_MESI_MISURA"	        ); } 
  public BigDecimal  getNumGiorniMisura()          	  throws DAOException  { return getBigDecimal ("NUM_GIORNI_MISURA"	        ); } 

  public Date        getDataInizioRevoca()            throws DAOException  { return getDate       ("DATA_INIZIO_REVOCA"         ); } 
  public BigDecimal  getNumAnniRevocaReclusione()  	  throws DAOException  { return getBigDecimal ("NUM_ANNI_REVOCA_RECLUSIONE" ); } 
  public BigDecimal  getNumMesiRevocaReclusione()  	  throws DAOException  { return getBigDecimal ("NUM_MESI_REVOCA_RECLUSIONE" ); } 
  public BigDecimal  getNumGiorniRevocaReclusione()	  throws DAOException  { return getBigDecimal ("NUM_GIORNI_REVOCA_RECLUSIONE"); } 
  public BigDecimal  getNumAnniRevocaArresto()     	  throws DAOException  { return getBigDecimal ("NUM_ANNI_REVOCA_ARRESTO"    ); } 
  public BigDecimal  getNumMesiRevocaArresto()     	  throws DAOException  { return getBigDecimal ("NUM_MESI_REVOCA_ARRESTO"    ); } 
  public BigDecimal  getNumGiorniRevocaArresto()   	  throws DAOException  { return getBigDecimal ("NUM_GIORNI_REVOCA_ARRESTO"  ); } 
  public Date        getDataIngressoIstituto()        throws DAOException  { return getDate       ("DATA_INGRESSO_ISTITUTO"     ); } 
  public Date        getDataScarcerazione()			  throws DAOException  { return getDate       ("DATA_SCARCERAZIONE"     ); } 
  public String      getFlagDecisioneTribunale()      throws DAOException  { return getString     ("FLAG_DECISIONE_TRIBUNALE"  ); } 
  public String      getCodTDSCompetente()		      throws DAOException  { return getString     ("COD_TDS_COMPETENTE"  ); } 

  public  String      getCodOperatoreInserimento()    throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"  ); } 
  public  Date        getDataInserimento()            throws DAOException  { return getDate       ("DATA_INSERIMENTO"           ); } 
  public  String      getCodUfficioInserimento()      throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"    ); } 
  public  String      getCodOperatoreAggiornamento()  throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"); } 
  public  Date        getDataAggiornamento()          throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"         ); } 
  public  String      getCodUfficioAggiornamento()    throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"  ); } 
  
// FUNGIBILITA
  public  BigDecimal  getAnnoProc()               	  throws DAOException  { return getBigDecimal ("ANNO_PROC"                 ); } 
  public  BigDecimal  getProgrProc()              	  throws DAOException  { return getBigDecimal ("PROGR_PROC"                ); }
  
  public  BigDecimal  getAnnoBdmc()               	  throws DAOException  { return getBigDecimal ("ANNO_BDMC"          ); } 
  public  String	  getNumeroBdmc()              	  throws DAOException  { return getString	  ("NUMERO_BDMC"        ); } 
  public  BigDecimal  getAnnoRege()               	  throws DAOException  { return getBigDecimal ("ANNO_REGE"          ); } 
  public  String	  getNumeroRege()              	  throws DAOException  { return getString	  ("NUMERO_REGE"        ); } 
  public  String      getTipoRege()                 throws DAOException  { return getString     ("TIPO_REGE"          	); } 
  public  String      getCodTipoAutoritaRege()   	throws DAOException  { return getString     ("TIPO_AUT_REGE" 		); } 
  public  String      getCodLuogoAutoritaRege()     throws DAOException  { return getString     ("COD_SEDE_REGE"    	); } 
  public  Date        getDataRege()            		throws DAOException  { return getDate       ("DATA_REGE"          	); } 
  
  public  BigDecimal  getAnnoRegePm()               throws DAOException  { return getBigDecimal ("ANNO_REGE_PM"            ); } 
  public  String  	  getNumeroRegePm()             throws DAOException  { return getString 	("NUMERO_REGE_PM"          ); } 
  public  String      getCodTipoUfficioPm()   		throws DAOException  { return getString     ("COD_TIPO_UFFICIO_PM" 	 ); } 
  public  String      getCodSedeUfficioPm()       	throws DAOException  { return getString     ("COD_SEDE_UFFICIO_PM"     ); }
  
  public  BigDecimal  getChiaveAnnosiep()           throws DAOException  { return getBigDecimal ("CHIAVE_ANNO_SIEP"        ); } 
  public  BigDecimal  getChiaveNumeroSiep()         throws DAOException  { return getBigDecimal ("CHIAVE_NUMERO_SIEP"      ); } 
  public  String      getChiaveUfficoSiep()         throws DAOException  { return getString     ("CHIAVE_UFFICIO_SIEP"     ); } 
  
  public  BigDecimal  getAnnoSentenza()               	throws DAOException  { return getBigDecimal ("ANNO_SENTENZA"          	 	); } 
  public  String	  getNumeroSentenza()              	throws DAOException  { return getString 	("NUMERO_SENTENZA"        		); } 
  public  Date        getDataSentenza()            		throws DAOException  { return getDate       ("DATA_SENTENZA"          		); } 
  public  String      getCodTipoAutoritaEmittente()   	throws DAOException  { return getString     ("COD_TIPO_AUT_EMITT_SENTENZA" 	); } 
  public  String      getCodLuogoEmittente()       		throws DAOException  { return getString     ("COD_LUOGO_EMITTENTE_SENTENZA" ); }
  
  //MEV_2025-48 - ALTRO – Benefici con anticipazione effetti
  public  String      getFlagAppProvvisoria()           throws DAOException  { return getString     ("FLAG_APP_PROVVISORIA" ); }

  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdComputiCumulo            (BigDecimal  aValore )   { setBigDecimal ("ID_COMPUTI_CUMULO"          , aValore); } 
  public void  setCodTipoAnnotazione         (String      aValore )   { setString     ("COD_TIPO_ANNOTAZIONE"       , aValore); } 
  public void  setCodCausaleComputo          (String      aValore )   { setString     ("COD_CAUSALE_COMPUTO"        , aValore); } 
  public void  setFlagPiuMeno                (String      aValore )   { setString     ("FLAG_PIU_MENO"              , aValore); } 
  public void  setDataReclusioneDa           (Date        aValore )   { setDate       ("DATA_RECLUSIONE_DA"         , aValore); } 
  public void  setDataReclusioneA            (Date        aValore )   { setDate       ("DATA_RECLUSIONE_A"          , aValore); } 
  public void  setNumAnniReclusione          (BigDecimal  aValore )   { setBigDecimal ("NUM_ANNI_RECLUSIONE"        , aValore); } 
  public void  setNumMesiReclusione          (BigDecimal  aValore )   { setBigDecimal ("NUM_MESI_RECLUSIONE"        , aValore); } 
  public void  setNumGiorniReclusione        (BigDecimal  aValore )   { setBigDecimal ("NUM_GIORNI_RECLUSIONE"      , aValore); } 
  public void  setImportoMulta               (BigDecimal  aValore )   { setBigDecimal ("IMPORTO_MULTA"              , aValore); } 
  public void  setDataArrestoDa              (Date        aValore )   { setDate       ("DATA_ARRESTO_DA"            , aValore); } 
  public void  setDataArrestoA               (Date        aValore )   { setDate       ("DATA_ARRESTO_A"             , aValore); } 
  public void  setNumAnniArresto             (BigDecimal  aValore )   { setBigDecimal ("NUM_ANNI_ARRESTO"           , aValore); } 
  public void  setNumMesiArresto             (BigDecimal  aValore )   { setBigDecimal ("NUM_MESI_ARRESTO"           , aValore); } 
  public void  setNumGiorniArresto           (BigDecimal  aValore )   { setBigDecimal ("NUM_GIORNI_ARRESTO"         , aValore); } 
  public void  setImportoAmmenda             (BigDecimal  aValore )   { setBigDecimal ("IMPORTO_AMMENDA"            , aValore); } 
  
  public void  setNumGiorniMap               (BigDecimal  aValore )   { setBigDecimal ("NUM_GIORNI_MAP"            , aValore); } 
  
  public void  setCodDpr                     (String      aValore )   { setString     ("COD_DPR"                    , aValore); }
  public void  setDataRichiesta              (Date        aValore )   { setDate       ("DATA_RICHIESTA"             , aValore); } 
  public void  setNote                       (String      aValore )   { setString     ("NOTE"                       , aValore); } 
  
  public void  setCodTipoMisura               (String      aValore )   { setString     ("COD_TIPO_MISURA"               , aValore); } 
  public void  setIstDetIdIstitutoDetenzione  (String      aValore )   { setString     ("IST_DET_ID_ISTITUTO_DETENZIONE", aValore); } 
  public void  setAltroLuogoDetenzione        (String      aValore )   { setString     ("ALTRO_LUOGO_DETENZIONE"        , aValore); } 
  
  public void  setFlagStato                  (String      aValore )   { setString     ("FLAG_STATO"                 , aValore); } 
  public void  setMotivoModifica             (String      aValore )   { setString     ("MOTIVO_MODIFICA"            , aValore); } 
  
  public void  setTitIdTitoloCumulato        (BigDecimal  aValore )   { setBigDecimal ("TIT_ID_TITOLO_CUMULATO"     , aValore); } 
  public void  setDatIdDatiFinaliCumulo      (BigDecimal  aValore )   { setBigDecimal ("DAT_ID_DATI_FINALI_CUMULO"  , aValore); } 
  public void  setIstrIdIstruttoriaCumulo    (BigDecimal  aValore )   { setBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO" , aValore); } 
  public void  setStatIdStatoEsecTitCum      (BigDecimal  aValore )   { setBigDecimal ("STAT_ID_STATO_ESEC_TIT_CUM" , aValore); } 
  
  public void  setDataEmissioneProvv         (Date        aValore )   { setDate       ("DATA_EMISSIONE_PROVV"       , aValore); } 
  public void  setDataRicezioneProvv         (Date        aValore )   { setDate       ("DATA_RICEZIONE_PROVV"       , aValore); } 
  public void  setAnnoProvv         		 (BigDecimal  aValore )   { setBigDecimal ("ANNO_PROVV"       			, aValore); } 
  public void  setProgrProvv         		 (BigDecimal  aValore )   { setBigDecimal ("PROGR_PROVV"       			, aValore); } 
  public void  setCodUfficioEmittenteProvv   (String      aValore )   { setString     ("COD_UFFICIO_EMITTENTE_PROVV" , aValore); } 
  public void  setCodLuogoUfficioProvv       (String      aValore )   { setString     ("COD_LUOGO_UFFICIO_PROVV"    , aValore); } 
  public void  setSezioneProvv       		 (String      aValore )   { setString     ("SEZIONE_PROVV"    			, aValore); } 
  public void  setCodTipoProvv       		 (String      aValore )   { setString     ("COD_TIPO_PROVV"    			, aValore); } 

  public void  setReaIdReatoCum       		 (BigDecimal  aValore )   { setBigDecimal ("REA_ID_REATO_CUM"  			, aValore); } 

  public void  setCodFonte		       		 (String      aValore )   { setString     ("COD_FONTE"	    			, aValore); } 
  public void  setAnnoFonte		       		 (BigDecimal  aValore )   { setBigDecimal ("ANNO_FONTE"		  			, aValore); } 
  public void  setNumeroFonte	       		 (String      aValore )   { setString     ("NUMERO_FONTE"    			, aValore); } 
  public void  setCodSottonumerazione  		 (String      aValore )   { setString     ("COD_SOTTONUMERAZIONE"		, aValore); } 
  public void  setComma				  		 (String      aValore )   { setString     ("COMMA"						, aValore); } 
  public void  setLettera			  		 (String      aValore )   { setString     ("LETTERA"					, aValore); } 
  public void  setNumero			  		 (String      aValore )   { setString     ("NUMERO"						, aValore); } 
  public void  setArticolo			  		 (String      aValore )   { setString     ("ARTICOLO"					, aValore); } 

  public void  setCodTipoRegistroOrdinanza	 (String      aValore )   { setString     ("COD_TIPO_REGISTRO_ORDINANZA", aValore); }
  public void  setDataSospensioneInterruzione(Date        aValore )   { setDate       ("DATA_SOSPENSIONE_INTERRUZIONE", aValore); } 
  public void  setCodOggettoDecisione	 	 (String      aValore )   { setString     ("COD_OGGETTO_DECISIONE"		, aValore); }

  public void  setProtocollo			 	 (String      aValore )   { setString     ("PROTOCOLLO"					, aValore); }
  public void  setAltraAutorita			 	 (String      aValore )   { setString     ("ALTRA_AUTORITA"				, aValore); }
  public void  setAltroLuogo			 	 (String      aValore )   { setString     ("ALTRO_LUOGO"				, aValore); }

  public void  setLuogoEsecMisura		 	 (String	  aValore )   { setString     ("LUOGO_ESEC_MISURA"			, aValore); } 
  public void  setDataInizioMisura		 	 (Date		  aValore )   { setDate		  ("DATA_INIZIO_MISURA"			, aValore); } 
  public void  setDataFineMisura		 	 (Date		  aValore )   { setDate		  ("DATA_FINE_MISURA"			, aValore); } 
  public void  setNumAnniMisura        		 (BigDecimal  aValore )   { setBigDecimal ("NUM_ANNI_MISURA"   			, aValore); } 
  public void  setNumMesiMisura        		 (BigDecimal  aValore )   { setBigDecimal ("NUM_MESI_MISURA"   			, aValore); } 
  public void  setNumGiorniMisura      		 (BigDecimal  aValore )   { setBigDecimal ("NUM_GIORNI_MISURA" 			, aValore); } 

  public void  setDataInizioRevoca		 	 (Date		  aValore )   { setDate		  ("DATA_INIZIO_REVOCA"			, aValore); } 
  public void  setNumAnniRevocaReclusione	 (BigDecimal  aValore )   { setBigDecimal ("NUM_ANNI_REVOCA_RECLUSIONE" , aValore); } 
  public void  setNumMesiRevocaReclusione	 (BigDecimal  aValore )   { setBigDecimal ("NUM_MESI_REVOCA_RECLUSIONE" , aValore); } 
  public void  setNumGiorniRevocaReclusione	 (BigDecimal  aValore )   { setBigDecimal ("NUM_GIORNI_REVOCA_RECLUSIONE" , aValore); } 
  public void  setNumAnniRevocaArresto		 (BigDecimal  aValore )   { setBigDecimal ("NUM_ANNI_REVOCA_ARRESTO"	 , aValore); } 
  public void  setNumMesiRevocaArresto		 (BigDecimal  aValore )   { setBigDecimal ("NUM_MESI_REVOCA_ARRESTO"	 , aValore); } 
  public void  setNumGiorniRevocaArresto	 (BigDecimal  aValore )   { setBigDecimal ("NUM_GIORNI_REVOCA_ARRESTO"	 , aValore); } 
  
  public void  setDataIngressoIstituto	 	 (Date		  aValore )   { setDate		  ("DATA_INGRESSO_ISTITUTO"		, aValore); } 

  public void  setDataScarcerazione		 	 (Date		  aValore )   { setDate		  ("DATA_SCARCERAZIONE"			, aValore); } 
  public void  setFlagDecisioneTribunale     (String      aValore )   { setString     ("FLAG_DECISIONE_TRIBUNALE"   , aValore); } 
  public void  setCodTDSCompetente     		 (String      aValore )   { setString     ("COD_TDS_COMPETENTE"   		, aValore); } 

  public void  setCodOperatoreInserimento    (String      aValore )   { setString     ("COD_OPERATORE_INSERIMENTO"  , aValore); } 
  public void  setDataInserimento            (Date        aValore )   { setDate       ("DATA_INSERIMENTO"           , aValore); } 
  public void  setCodUfficioInserimento      (String      aValore )   { setString     ("COD_UFFICIO_INSERIMENTO"    , aValore); } 
  public void  setCodOperatoreAggiornamento  (String      aValore )   { setString     ("COD_OPERATORE_AGGIORNAMENTO", aValore); } 
  public void  setDataAggiornamento          (Date        aValore )   { setDate       ("DATA_AGGIORNAMENTO"         , aValore); } 
  public void  setCodUfficioAggiornamento    (String      aValore )   { setString     ("COD_UFFICIO_AGGIORNAMENTO"  , aValore); } 

// Fungibilita
  public void  setAnnoProc 	        		 (BigDecimal  aValore )   { setBigDecimal ("ANNO_PROC"       			, aValore); } 
  public void  setProgrProc         		 (BigDecimal  aValore )   { setBigDecimal ("PROGR_PROC"       			, aValore); } 
  
  public void  setChiaveAnnoSIEP         	(BigDecimal  aValore )   { setBigDecimal ("CHIAVE_ANNO_SIEP"    		 , aValore); } 
  public void  setChiaveNumeroSIEP         	(BigDecimal  aValore )   { setBigDecimal ("CHIAVE_NUMERO_SIEP"    		 , aValore); }
  public void  setChiaveUfficioSIEP         (String  	 aValore )   { setString 	 ("CHIAVE_UFFICIO_SIEP"    		 , aValore); }
  
  public void  setAnnoSentenza         		(BigDecimal  aValore )   { setBigDecimal ("ANNO_SENTENZA"    			 , aValore); } 
  public void  setNumeroSentenza         	(String  	 aValore )   { setString 	 ("NUMERO_SENTENZA"    			 , aValore); }
  public void  setDataSentenza         		(Date        aValore )   { setDate       ("DATA_SENTENZA"       		 , aValore); }
  public void  setCodTipoAutoritaEmittente  (String      aValore )   { setString     ("COD_TIPO_AUT_EMITT_SENTENZA"  , aValore); } 
  public void  setCodLuogoEmittente       	(String      aValore )   { setString     ("COD_LUOGO_EMITTENTE_SENTENZA" , aValore); } 
  
  public void  setAnnoRegePM         		(BigDecimal  aValore )   { setBigDecimal ("ANNO_REGE_PM"    			 , aValore); } 
  public void  setNumeroRegePM         		(String  	 aValore )   { setString 	 ("NUMERO_REGE_PM"    			 , aValore); }
  public void  setCodTipoUfficioPM  		(String      aValore )   { setString     ("COD_TIPO_UFFICIO_PM"  		 , aValore); } 
  public void  setCodSedeUfficioPM       	(String      aValore )   { setString     ("COD_SEDE_UFFICIO_PM" 		 , aValore); } 
  
  public void  setAnnoBDMC         			(BigDecimal  aValore )   { setBigDecimal ("ANNO_BDMC"    			 , aValore); } 
  public void  setNumeroBDMC         		(String  	 aValore )   { setString 	 ("NUMERO_BDMC"    			 , aValore); }
  public void  setAnnoRege         			(BigDecimal  aValore )   { setBigDecimal ("ANNO_REGE"    			 , aValore); } 
  public void  setNumeroRege         		(String  	 aValore )   { setString 	 ("NUMERO_REGE"    			 , aValore); }
  public void  setTipoRege     				(String      aValore )   { setString     ("TIPO_REGE"    	 	 , aValore); }
  public void  setCodTipoAutoritaRege  		(String      aValore )   { setString     ("TIPO_AUT_REGE" 		 , aValore); } 
  public void  setCodLuogoAutoritaRege      (String      aValore )   { setString     ("COD_SEDE_REGE" 		 , aValore); }
  public void  setDataRege         			(Date        aValore )   { setDate       ("DATA_REGE"       	 , aValore); }
  
  //MEV_2025-48 - ALTRO – Benefici con anticipazione effetti
  public void  setFlagAppProvvisoria        (String      aValore )   { setString     ("FLAG_APP_PROVVISORIA" , aValore); }
  
  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new ComputiCumuloModel(  
      getIdComputiCumulo() , 
      getCodTipoAnnotazione() , 
 "",
      getCodCausaleComputo() , 
 "",
      getFlagPiuMeno() , 
      getDataReclusioneDa() , 
      getDataReclusioneA() , 
      getNumAnniReclusione() , 
      getNumMesiReclusione() , 
      getNumGiorniReclusione() , 
      getImportoMulta() , 
      getDataArrestoDa() , 
      getDataArrestoA() , 
      getNumAnniArresto() , 
      getNumMesiArresto() , 
      getNumGiorniArresto() , 
      getImportoAmmenda() , 
      getNumGiorniMap(),
      getCodDpr(),
      getDataRichiesta(),
      getNote(),     
      getCodTipoMisura() , 
 "",
      getIstDetIdIstitutoDetenzione() , 
      getAltroLuogoDetenzione(),
      getFlagStato() , 
      getMotivoModifica() , 
      getTitIdTitoloCumulato() , 
      getDatIdDatiFinaliCumulo() , 
      getIstrIdIstruttoriaCumulo() , 
      getStatIdStatoEsecTitCum(),
      
      getDataEmissioneProvv(),
      getDataRicezioneProvv(),
      getAnnoProvv(),
      getProgrProvv(),
      getCodUfficioEmittenteProvv(),
 "",   // Descrizione  
      getCodLuogoUfficioProvv(),
 "",   // descrizione  
      getSezioneProvv(),
      getCodTipoProvv(),

      getReaIdReatoCum(),

      getCodFonte(),
      "",
      getAnnoFonte(),
      getNumeroFonte(),
      getCodSottonumerazione(),
      "",
      getComma(),
      getLettera(),
      getNumero(),
      getArticolo(),

      getCodTipoRegistroOrdinanza(),
      "",   // Descrizione  
	  getDataSospensioneInterruzione() , 
      getCodOggettoDecisione(),
      "",   // descrizione  
      
      getProtocollo() , 
      getAltraAutorita() , 
      getAltroLuogo(), 

      getLuogoEsecMisura(),
      getDataInizioMisura(),
      getDataFineMisura(),
      getNumAnniMisura(),
      getNumMesiMisura(),
      getNumGiorniMisura(),

      getDataInizioRevoca(),
      getNumAnniRevocaReclusione(),
      getNumMesiRevocaReclusione(),
      getNumGiorniRevocaReclusione(),
      getNumAnniRevocaArresto(),
      getNumMesiRevocaArresto(),
      getNumGiorniRevocaArresto(),

      getDataIngressoIstituto(),
      getDataScarcerazione(),
      getFlagDecisioneTribunale(),
      getCodTDSCompetente(),
      "",
      getCodOperatoreInserimento() , 
      getDataInserimento() , 
      getCodUfficioInserimento() , 
      getCodOperatoreAggiornamento() , 
      getDataAggiornamento() , 
      getCodUfficioAggiornamento(),
            
// Fungibilità
      getAnnoProc(),
      getProgrProc(),
      
      getAnnoBdmc(),
      getNumeroBdmc(),
      getAnnoRege(),
      getNumeroRege(),
      getTipoRege(),
      getCodTipoAutoritaRege(),
      "",
      getCodLuogoAutoritaRege(),
      "",
      getDataRege(),
      
      getAnnoRegePm(),
      getNumeroRegePm(),
      getCodTipoUfficioPm(),
      "",
      getCodSedeUfficioPm(),
      "",
      
      getChiaveAnnosiep(),
      getChiaveNumeroSiep(),
      getChiaveUfficoSiep(),
      
      getAnnoSentenza(),
      getNumeroSentenza(),
      getDataSentenza(),
      getCodTipoAutoritaEmittente(),
      "",
      getCodLuogoEmittente(),
      "",
      getFlagAppProvvisoria() // MEV_2025-48 - ALTRO – Benefici con anticipazione effetti
      
    );
  }


  /***************************************************************************** 
   * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void setDAOFromModel(ComputiCumuloModel aModel) throws DAOException {
    setIdComputiCumulo            ( aModel.getIdComputiCumulo()           );  
    setCodTipoAnnotazione         ( aModel.getCodTipoAnnotazione()        );  
    setCodCausaleComputo          ( aModel.getCodCausaleComputo()         );  
    setFlagPiuMeno                ( aModel.getFlagPiuMeno()               );  
    setDataReclusioneDa           ( aModel.getDataReclusioneDa()          );  
    setDataReclusioneA            ( aModel.getDataReclusioneA()           );  
    setNumAnniReclusione          ( aModel.getNumAnniReclusione()         );  
    setNumMesiReclusione          ( aModel.getNumMesiReclusione()         );  
    setNumGiorniReclusione        ( aModel.getNumGiorniReclusione()       );  
    setImportoMulta               ( aModel.getImportoMulta()              );  
    setDataArrestoDa              ( aModel.getDataArrestoDa()             );  
    setDataArrestoA               ( aModel.getDataArrestoA()              );  
    setNumAnniArresto             ( aModel.getNumAnniArresto()            );  
    setNumMesiArresto             ( aModel.getNumMesiArresto()            );  
    setNumGiorniArresto           ( aModel.getNumGiorniArresto()          );  
    setImportoAmmenda             ( aModel.getImportoAmmenda()            );
    setNumGiorniMap               ( aModel.getNumGiorniMap());
    
    setCodDpr                     ( aModel.getCodDpr()                    );
    setDataRichiesta              ( aModel.getDataRichiesta()             );
    setNote                       ( aModel.getNote()                      );  
    
    setCodTipoMisura               ( aModel.getCodTipoMisura()              );  
    setIstDetIdIstitutoDetenzione  ( aModel.getIstDetIdIstitutoDetenzione() );  
    setAltroLuogoDetenzione        ( aModel.getAltroLuogoDetenzione()       );  
    
    setFlagStato                  ( aModel.getFlagStato()                 );  
    setMotivoModifica             ( aModel.getMotivoModifica()            );  
    setTitIdTitoloCumulato        ( aModel.getTitIdTitoloCumulato()       );  
    setDatIdDatiFinaliCumulo      ( aModel.getDatIdDatiFinaliCumulo()     );  
    setIstrIdIstruttoriaCumulo    ( aModel.getIstrIdIstruttoriaCumulo()   );  
    setStatIdStatoEsecTitCum      ( aModel.getStatIdStatoEsecTitCum());

    setDataEmissioneProvv		  ( aModel.getDataEmissioneProvv() );
    setDataRicezioneProvv		  ( aModel.getDataRicezioneProvv() );
    setAnnoProvv		  		  ( aModel.getAnnoProvv() );
    setProgrProvv		  		  ( aModel.getProgrProvv() );
    setCodUfficioEmittenteProvv   ( aModel.getCodUfficioEmittenteProvv() );  
    setCodLuogoUfficioProvv       ( aModel.getCodLuogoUfficioProvv() );  
    setSezioneProvv       		  ( aModel.getSezioneProvv() );  
    setCodTipoProvv       		  ( aModel.getCodTipoProvv() );  

    setReaIdReatoCum	  		  ( aModel.getReaIdReatoCum() );

    setCodFonte		       		  ( aModel.getCodFonte() );  
    setAnnoFonte	       		  ( aModel.getAnnoFonte() );  
    setNumeroFonte	       		  ( aModel.getNumeroFonte() );  
    setCodSottonumerazione 		  ( aModel.getCodSottonumerazione() );  
    setComma		       		  ( aModel.getComma() );  
    setLettera		       		  ( aModel.getLettera() );  
    setNumero		       		  ( aModel.getNumero() );  
    setArticolo		       		  ( aModel.getArticolo() );  

    setCodTipoRegistroOrdinanza	  ( aModel.getCodTipoRegistroOrdinanza() );  
    setDataSospensioneInterruzione( aModel.getDataSospensioneInterruzione() );  
    setCodOggettoDecisione		  ( aModel.getCodOggettoDecisione() );  

    setProtocollo				  ( aModel.getProtocollo() );  
    setAltraAutorita			  ( aModel.getAltraAutorita() );  
    setAltroLuogo				  ( aModel.getAltroLuogo() );  
    
    setLuogoEsecMisura			  ( aModel.getLuogoEsecMisura() );  
    setDataInizioMisura			  ( aModel.getDataInizioMisura() );  
    setDataFineMisura			  ( aModel.getDataFineMisura() );  
    setNumAnniMisura			  ( aModel.getNumAnniMisura() );  
    setNumMesiMisura			  ( aModel.getNumMesiMisura() );  
    setNumGiorniMisura			  ( aModel.getNumGiorniMisura() );  

    setDataInizioRevoca			  ( aModel.getDataInizioRevoca() );  
    setNumAnniRevocaReclusione	  ( aModel.getNumAnniRevocaReclusione() );  
    setNumMesiRevocaReclusione	  ( aModel.getNumMesiRevocaReclusione() );  
    setNumGiorniRevocaReclusione  ( aModel.getNumGiorniRevocaReclusione() );  
    setNumAnniRevocaArresto		  ( aModel.getNumAnniRevocaArresto() );  
    setNumMesiRevocaArresto		  ( aModel.getNumMesiRevocaArresto() );  
    setNumGiorniRevocaArresto	  ( aModel.getNumGiorniRevocaArresto() );  

    setDataIngressoIstituto		  ( aModel.getDataIngressoIstituto() );  
    setDataScarcerazione		  ( aModel.getDataScarcerazione() );  
    setFlagDecisioneTribunale	  ( aModel.getFlagDecisioneTribunale()   );  
    setCodTDSCompetente			  ( aModel.getCodTDSCompetente()   );  
    
    setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
    setDataInserimento            ( aModel.getDataInserimento()           );  
    setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   ); 
    
// Fungibilita
   setAnnoProc					(aModel.getAnnoProc()	);
   setProgrProc					(aModel.getProgrProc() 	);
   
   setChiaveAnnoSIEP			(aModel.getChiaveAnnoSIEP() );
   setChiaveNumeroSIEP			(aModel.getChiaveNumeroSIEP() );
   setChiaveUfficioSIEP			(aModel.getChiaveUfficioSIEP() );
   
   setAnnoSentenza     			(aModel.getAnnoSentenza()  );
   setNumeroSentenza     		(aModel.getNumeroSentenza()  );
   setDataSentenza				(aModel.getDataSentenza() );	 
   setCodTipoAutoritaEmittente	(aModel.getCodTipoAutoritaEmittente() );
   setCodLuogoEmittente			(aModel.getCodLuogoEmittente()  );
   
   setAnnoRegePM				(aModel.getAnnoRegePM() );
   setNumeroRegePM				(aModel.getNumeroRegePM() );
   setCodTipoUfficioPM			(aModel.getCodTipoUfficioPM() );
   setCodSedeUfficioPM			(aModel.getCodSedeUfficioPM() );
    
   setAnnoBDMC					(aModel.getAnnoBDMC()	);
   setNumeroBDMC				(aModel.getNumeroBDMC()	);
   setAnnoRege					(aModel.getAnnoRege()	);
   setNumeroRege				(aModel.getNumeroRege()	);
   setTipoRege					(aModel.getTipoRege()	);
   setCodTipoAutoritaRege		(aModel.getCodTipoAutoritaRege()	);
   setCodLuogoAutoritaRege		(aModel.getCodLuogoAutoritaRege()	);
   setDataRege					(aModel.getDataEmissioneOrdRege()	);
   
   // MEV_2025-48 - ALTRO – Benefici con anticipazione effetti
   setFlagAppProvvisoria        (aModel.getFlagAppProvvisoria()   );
   
  }
  
  public void setDAOFromModelForUpdate(ComputiCumuloModel aModel) throws DAOException 
  {
    //setIdComputiCumulo            ( aModel.getIdComputiCumulo()           );  
    setCodTipoAnnotazione         ( aModel.getCodTipoAnnotazione()        );  
    setCodCausaleComputo          ( aModel.getCodCausaleComputo()         );  
    setFlagPiuMeno                ( aModel.getFlagPiuMeno()               );  
    
    setDataReclusioneDa           ( aModel.getDataReclusioneDa()          );  
    setDataReclusioneA            ( aModel.getDataReclusioneA()           );  
    setNumAnniReclusione          ( aModel.getNumAnniReclusione()         );  
    setNumMesiReclusione          ( aModel.getNumMesiReclusione()         );  
    setNumGiorniReclusione        ( aModel.getNumGiorniReclusione()       );  
    setImportoMulta               ( aModel.getImportoMulta()              );  
    
    setDataArrestoDa              ( aModel.getDataArrestoDa()             );  
    setDataArrestoA               ( aModel.getDataArrestoA()              );  
    setNumAnniArresto             ( aModel.getNumAnniArresto()            );  
    setNumMesiArresto             ( aModel.getNumMesiArresto()            );  
    setNumGiorniArresto           ( aModel.getNumGiorniArresto()          );  
    setImportoAmmenda             ( aModel.getImportoAmmenda()            );  

    setNumGiorniMap               ( aModel.getNumGiorniMap());

    setCodDpr                     ( aModel.getCodDpr()                    );
    setDataRichiesta              ( aModel.getDataRichiesta()             );
    
    setNote                       ( aModel.getNote()                      );  
   
    setCodTipoMisura               ( aModel.getCodTipoMisura()              );  
    setIstDetIdIstitutoDetenzione  ( aModel.getIstDetIdIstitutoDetenzione() );  
    setAltroLuogoDetenzione        ( aModel.getAltroLuogoDetenzione()       );  
    
    setFlagStato                  ( aModel.getFlagStato()                 );  
    setMotivoModifica             ( aModel.getMotivoModifica()            );  
    setTitIdTitoloCumulato        ( aModel.getTitIdTitoloCumulato()       );  
    setDatIdDatiFinaliCumulo      ( aModel.getDatIdDatiFinaliCumulo()     );  
    setIstrIdIstruttoriaCumulo    ( aModel.getIstrIdIstruttoriaCumulo()   );  
    setStatIdStatoEsecTitCum      ( aModel.getStatIdStatoEsecTitCum());

    setDataEmissioneProvv		  ( aModel.getDataEmissioneProvv() );
    setDataRicezioneProvv		  ( aModel.getDataRicezioneProvv() );
    setAnnoProvv		  		  ( aModel.getAnnoProvv() );
    setProgrProvv		  		  ( aModel.getProgrProvv() );
    setCodUfficioEmittenteProvv   ( aModel.getCodUfficioEmittenteProvv() );  
    setCodLuogoUfficioProvv       ( aModel.getCodLuogoUfficioProvv() );  
    setSezioneProvv       		  ( aModel.getSezioneProvv() );  
    setCodTipoProvv       		  ( aModel.getCodTipoProvv() );  

    setReaIdReatoCum       		  ( aModel.getReaIdReatoCum() );  

    setCodFonte		       		  ( aModel.getCodFonte() );  
    setAnnoFonte	       		  ( aModel.getAnnoFonte() );  
    setNumeroFonte	       		  ( aModel.getNumeroFonte() );  
    setCodSottonumerazione 		  ( aModel.getCodSottonumerazione() );  
    setComma		       		  ( aModel.getComma() );  
    setLettera		       		  ( aModel.getLettera() );  
    setNumero		       		  ( aModel.getNumero() );  
    setArticolo		       		  ( aModel.getArticolo() );  
    
    setCodTipoRegistroOrdinanza	  ( aModel.getCodTipoRegistroOrdinanza() );  
    setDataSospensioneInterruzione (aModel.getDataSospensioneInterruzione() );  
    setCodOggettoDecisione		  ( aModel.getCodOggettoDecisione() );  

    setProtocollo				  ( aModel.getProtocollo() );  
    setAltraAutorita			  ( aModel.getAltraAutorita() );  
    setAltroLuogo				  ( aModel.getAltroLuogo() );  

    setLuogoEsecMisura			  ( aModel.getLuogoEsecMisura() );  
    setDataInizioMisura			  ( aModel.getDataInizioMisura() );  
    setDataFineMisura			  ( aModel.getDataFineMisura() );  
    setNumAnniMisura			  ( aModel.getNumAnniMisura() );  
    setNumMesiMisura			  ( aModel.getNumMesiMisura() );  
    setNumGiorniMisura			  ( aModel.getNumGiorniMisura() );  

    setDataInizioRevoca			  ( aModel.getDataInizioRevoca() );  
    setNumAnniRevocaReclusione	  ( aModel.getNumAnniRevocaReclusione() );  
    setNumMesiRevocaReclusione	  ( aModel.getNumMesiRevocaReclusione() );  
    setNumGiorniRevocaReclusione  ( aModel.getNumGiorniRevocaReclusione() );  
    setNumAnniRevocaArresto		  ( aModel.getNumAnniRevocaArresto() );  
    setNumMesiRevocaArresto		  ( aModel.getNumMesiRevocaArresto() );  
    setNumGiorniRevocaArresto	  ( aModel.getNumGiorniRevocaArresto() );  

    setDataIngressoIstituto		  ( aModel.getDataIngressoIstituto() );  
    
    setDataScarcerazione		  ( aModel.getDataScarcerazione() );  
    setFlagDecisioneTribunale	  ( aModel.getFlagDecisioneTribunale()   );  
    setCodTDSCompetente			  ( aModel.getCodTDSCompetente()   );  

    //setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
    //setDataInserimento            ( aModel.getDataInserimento()           );  
    //setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );
    
 // Fungibilita
    setAnnoProc					(aModel.getAnnoProc()	);
    setProgrProc					(aModel.getProgrProc() 	);
    
    setChiaveAnnoSIEP			(aModel.getChiaveAnnoSIEP() );
    setChiaveNumeroSIEP			(aModel.getChiaveNumeroSIEP() );
    setChiaveUfficioSIEP			(aModel.getChiaveUfficioSIEP() );
    
    setAnnoSentenza     			(aModel.getAnnoSentenza()  );
    setNumeroSentenza     		(aModel.getNumeroSentenza()  );
    setDataSentenza				(aModel.getDataSentenza() );	 
    setCodTipoAutoritaEmittente	(aModel.getCodTipoAutoritaEmittente() );
    setCodLuogoEmittente			(aModel.getCodLuogoEmittente()  );
    
	
    setAnnoRegePM				(aModel.getAnnoRegePM() );
    setNumeroRegePM				(aModel.getNumeroRegePM() );
    setCodTipoUfficioPM			(aModel.getCodTipoUfficioPM() );
    setCodSedeUfficioPM			(aModel.getCodSedeUfficioPM() );
     
    setAnnoBDMC					(aModel.getAnnoBDMC()	);
    setNumeroBDMC				(aModel.getNumeroBDMC()	);
    setAnnoRege					(aModel.getAnnoRege()	);
    setNumeroRege				(aModel.getNumeroRege()	);
    setTipoRege					(aModel.getTipoRege()	);
    setCodTipoAutoritaRege		(aModel.getCodTipoAutoritaRege()	);
    setCodLuogoAutoritaRege		(aModel.getCodLuogoAutoritaRege()	);
    setDataRege					(aModel.getDataEmissioneOrdRege()	);    
    
    // MEV_2025-48 - ALTRO – Benefici con anticipazione effetti
    setFlagAppProvvisoria        (aModel.getFlagAppProvvisoria()   );
  }  


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(ComputiCumuloModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdComputiCumulo() != null ) { 
      lCondizioni += " and ID_COMPUTI_CUMULO = " + aModel.getIdComputiCumulo() + ""; 
    } 
    if (aModel.getCodTipoAnnotazione() != null && aModel.getCodTipoAnnotazione().length() > 0) { 
      lCondizioni += " and COD_TIPO_ANNOTAZIONE = '" + aModel.getCodTipoAnnotazione() + "' "; 
    } 
    if (aModel.getCodCausaleComputo() != null && aModel.getCodCausaleComputo().length() > 0) { 
      lCondizioni += " and COD_CAUSALE_COMPUTO = '" + aModel.getCodCausaleComputo() + "' "; 
    } 
    if (aModel.getFlagPiuMeno() != null && aModel.getFlagPiuMeno().length() > 0) { 
      lCondizioni += " and FLAG_PIU_MENO = '" + aModel.getFlagPiuMeno() + "' "; 
    } 
    if (aModel.getDataReclusioneDa() != null ) { 
      lCondizioni += " and to_char(DATA_RECLUSIONE_DA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataReclusioneDa(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataReclusioneA() != null ) { 
      lCondizioni += " and to_char(DATA_RECLUSIONE_A,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataReclusioneA(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getNumAnniReclusione() != null ) { 
      lCondizioni += " and NUM_ANNI_RECLUSIONE = " + aModel.getNumAnniReclusione() + ""; 
    } 
    if (aModel.getNumMesiReclusione() != null ) { 
      lCondizioni += " and NUM_MESI_RECLUSIONE = " + aModel.getNumMesiReclusione() + ""; 
    } 
    if (aModel.getNumGiorniReclusione() != null ) { 
      lCondizioni += " and NUM_GIORNI_RECLUSIONE = " + aModel.getNumGiorniReclusione() + ""; 
    } 
    if (aModel.getImportoMulta() != null ) { 
      lCondizioni += " and IMPORTO_MULTA = " + aModel.getImportoMulta() + ""; 
    } 
    if (aModel.getDataArrestoDa() != null ) { 
      lCondizioni += " and to_char(DATA_ARRESTO_DA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataArrestoDa(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataArrestoA() != null ) { 
      lCondizioni += " and to_char(DATA_ARRESTO_A,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataArrestoA(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getNumAnniArresto() != null ) { 
      lCondizioni += " and NUM_ANNI_ARRESTO = " + aModel.getNumAnniArresto() + ""; 
    } 
    if (aModel.getNumMesiArresto() != null ) { 
      lCondizioni += " and NUM_MESI_ARRESTO = " + aModel.getNumMesiArresto() + ""; 
    } 
    if (aModel.getNumGiorniArresto() != null ) { 
      lCondizioni += " and NUM_GIORNI_ARRESTO = " + aModel.getNumGiorniArresto() + ""; 
    } 
    if (aModel.getImportoAmmenda() != null ) { 
      lCondizioni += " and IMPORTO_AMMENDA = " + aModel.getImportoAmmenda() + ""; 
    }
    if (aModel.getCodDpr() != null && aModel.getCodDpr().length() > 0) { 
      lCondizioni += " and COD_DPR = '" + aModel.getCodDpr() + "' "; 
    }     
    if (aModel.getDataRichiesta() != null ) { 
      lCondizioni += " and DATA_RICHIESTA = '" + aModel.getDataRichiesta() + "' "; 
    }       
    if (aModel.getNote() != null && aModel.getNote().length() > 0) { 
      lCondizioni += " and NOTE = '" + aModel.getNote() + "' "; 
    } 
    if (aModel.getFlagStato() != null && aModel.getFlagStato().length() > 0) { 
      lCondizioni += " and FLAG_STATO = '" + aModel.getFlagStato() + "' "; 
    } 
    if (aModel.getMotivoModifica() != null && aModel.getMotivoModifica().length() > 0) { 
      lCondizioni += " and MOTIVO_MODIFICA = '" + aModel.getMotivoModifica() + "' "; 
    } 
    if (aModel.getTitIdTitoloCumulato() != null ) { 
      lCondizioni += " and TIT_ID_TITOLO_CUMULATO = " + aModel.getTitIdTitoloCumulato() + ""; 
    } 
    if (aModel.getDatIdDatiFinaliCumulo() != null ) { 
      lCondizioni += " and DAT_ID_DATI_FINALI_CUMULO = " + aModel.getDatIdDatiFinaliCumulo() + ""; 
    } 
    if (aModel.getIstrIdIstruttoriaCumulo() != null ) { 
      lCondizioni += " and ISTR_ID_ISTRUTTORIA_CUMULO = " + aModel.getIstrIdIstruttoriaCumulo() + ""; 
    } 

    if (aModel.getDataEmissioneProvv() != null ) { 
        lCondizioni += " and DATA_EMISSIONE_PROVV = " + aModel.getDataEmissioneProvv() + ""; 
    } 
    if (aModel.getDataRicezioneProvv() != null ) { 
        lCondizioni += " and DATA_EMISSIONE_PROVV = " + aModel.getDataRicezioneProvv() + ""; 
    } 
    if (aModel.getAnnoProvv() != null ) { 
        lCondizioni += " and ANNO_PROVV = " + aModel.getAnnoProvv() + ""; 
    } 
    if (aModel.getProgrProvv() != null ) { 
        lCondizioni += " and PROGR_PROVV = " + aModel.getProgrProvv() + ""; 
    } 
    if (aModel.getCodUfficioEmittenteProvv() != null ) { 
        lCondizioni += " and COD_UFFICIO_EMITTENTE_PROVV = " + aModel.getCodUfficioEmittenteProvv() + ""; 
    } 
    if (aModel.getCodLuogoUfficioProvv() != null ) { 
        lCondizioni += " and COD_LUOGO_UFFICIO_PROVV = " + aModel.getCodLuogoUfficioProvv() + ""; 
    } 
    if (aModel.getSezioneProvv() != null ) { 
        lCondizioni += " and SEZIONE_PROVV = " + aModel.getSezioneProvv() + ""; 
    } 
    if (aModel.getCodTipoProvv() != null ) { 
        lCondizioni += " and COD_TIPO_PROVV = " + aModel.getCodTipoProvv() + ""; 
    } 
    if (aModel.getReaIdReatoCum() != null ) { 
        lCondizioni += " and REA_ID_REATO_CUM = " + aModel.getReaIdReatoCum() + ""; 
    } 

    if (aModel.getCodFonte() != null ) { 
        lCondizioni += " and COD_FONTE = " + aModel.getCodFonte() + ""; 
    } 
    if (aModel.getAnnoFonte() != null ) { 
        lCondizioni += " and ANNO_FONTE = " + aModel.getAnnoFonte() + ""; 
    } 
    if (aModel.getNumeroFonte() != null ) { 
        lCondizioni += " and NUMERO_FONTE = " + aModel.getNumeroFonte() + ""; 
    } 
    if (aModel.getCodSottonumerazione() != null ) { 
        lCondizioni += " and COD_SOTTONUMERAZIONE = " + aModel.getCodSottonumerazione() + ""; 
    } 
    if (aModel.getComma() != null ) { 
        lCondizioni += " and COMMA = " + aModel.getComma() + ""; 
    } 
    if (aModel.getLettera() != null ) { 
        lCondizioni += " and LETTERA = " + aModel.getLettera() + ""; 
    } 
    if (aModel.getNumero() != null ) { 
        lCondizioni += " and NUMERO = " + aModel.getNumero() + ""; 
    } 
    if (aModel.getArticolo() != null ) { 
        lCondizioni += " and ARTICOLO = " + aModel.getArticolo() + ""; 
    }

    if (aModel.getCodTipoRegistroOrdinanza() != null ) { 
        lCondizioni += " and COD_TIPO_REGISTRO_ORDINANZA = " + aModel.getCodTipoRegistroOrdinanza() + ""; 
    }
    if (aModel.getDataSospensioneInterruzione() != null ) { 
        lCondizioni += " and to_char(DATA_SOSPENSIONE_INTERRUZIONE,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataSospensioneInterruzione(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getCodOggettoDecisione() != null ) { 
        lCondizioni += " and COD_OGGETTO_DECISIONE = " + aModel.getCodOggettoDecisione() + ""; 
    }

    if (aModel.getProtocollo() != null ) { 
        lCondizioni += " and PROTOCOLLO = " + aModel.getProtocollo() + ""; 
    }
    if (aModel.getAltraAutorita() != null ) { 
        lCondizioni += " and ALTRA_AUTORITA = " + aModel.getAltraAutorita() + ""; 
    }
    if (aModel.getAltroLuogo() != null ) { 
        lCondizioni += " and ALTRO_LUOGO = " + aModel.getAltroLuogo() + ""; 
    }

    if (aModel.getLuogoEsecMisura() != null ) { 
        lCondizioni += " and LUOGO_ESEC_MISURA = " + aModel.getLuogoEsecMisura() + ""; 
    }
    if (aModel.getDataInizioMisura() != null ) { 
        lCondizioni += " and DATA_INIZIO_MISURA = " + aModel.getDataInizioMisura() + ""; 
    }
    if (aModel.getDataFineMisura() != null ) { 
        lCondizioni += " and DATA_FINE_MISURA = " + aModel.getDataFineMisura() + ""; 
    }
    if (aModel.getNumAnniMisura() != null ) { 
        lCondizioni += " and NUM_ANNI_MISURA = " + aModel.getNumAnniMisura() + ""; 
    }
    if (aModel.getNumMesiMisura() != null ) { 
        lCondizioni += " and NUM_MESI_MISURA = " + aModel.getNumMesiMisura() + ""; 
    }
    if (aModel.getNumGiorniMisura() != null ) { 
        lCondizioni += " and NUM_GIORNI_MISURA = " + aModel.getNumGiorniMisura() + ""; 
    }

    if (aModel.getDataInizioRevoca() != null ) { 
        lCondizioni += " and DATA_INIZIO_REVOCA = " + aModel.getDataInizioRevoca() + ""; 
    }
    if (aModel.getNumAnniRevocaReclusione() != null ) { 
        lCondizioni += " and NUM_ANNI_REVOCA_RECLUSIONE = " + aModel.getNumAnniRevocaReclusione() + ""; 
    }
    if (aModel.getNumMesiRevocaReclusione() != null ) { 
        lCondizioni += " and NUM_MESI_REVOCA_RECLUSIONE = " + aModel.getNumMesiRevocaReclusione() + ""; 
    }
    if (aModel.getNumGiorniRevocaReclusione() != null ) { 
        lCondizioni += " and NUM_GIORNI_REVOCA_RECLUSIONE = " + aModel.getNumGiorniRevocaReclusione() + ""; 
    }
    if (aModel.getNumAnniRevocaArresto() != null ) { 
        lCondizioni += " and NUM_ANNI_REVOCA_ARRESTO = " + aModel.getNumAnniRevocaArresto() + ""; 
    }
    if (aModel.getNumMesiRevocaArresto() != null ) { 
        lCondizioni += " and NUM_MESI_REVOCA_ARRESTO = " + aModel.getNumMesiRevocaArresto() + ""; 
    }
    if (aModel.getNumGiorniRevocaArresto() != null ) { 
        lCondizioni += " and NUM_GIORNI_REVOCA_ARRESTO = " + aModel.getNumGiorniRevocaArresto() + ""; 
    }

    if (aModel.getDataIngressoIstituto() != null ) { 
        lCondizioni += " and DATA_INGRESSO_ISTITUTO = " + aModel.getDataIngressoIstituto() + ""; 
    }
    if (aModel.getDataScarcerazione() != null ) { 
        lCondizioni += " and DATA_SCARCERAZIONE = " + aModel.getDataScarcerazione() + ""; 
    }
    if (aModel.getFlagDecisioneTribunale() != null ) { 
        lCondizioni += " and FLAG_DECISIONE_TRIBUNALE = " + aModel.getFlagDecisioneTribunale() + ""; 
    }
    if (aModel.getCodTDSCompetente() != null ) { 
        lCondizioni += " and COD_TDS_COMPETENTE = " + aModel.getCodTDSCompetente() + ""; 
    }
    
    if (aModel.getCodOperatoreInserimento() != null && aModel.getCodOperatoreInserimento().length() > 0) { 
      lCondizioni += " and COD_OPERATORE_INSERIMENTO = '" + aModel.getCodOperatoreInserimento() + "' "; 
    } 
    if (aModel.getDataInserimento() != null ) { 
      lCondizioni += " and to_char(DATA_INSERIMENTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInserimento(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "' "; 
    } 
    if (aModel.getCodOperatoreAggiornamento() != null && aModel.getCodOperatoreAggiornamento().length() > 0) { 
      lCondizioni += " and COD_OPERATORE_AGGIORNAMENTO = '" + aModel.getCodOperatoreAggiornamento() + "' "; 
    } 
    if (aModel.getDataAggiornamento() != null ) { 
      lCondizioni += " and to_char(DATA_AGGIORNAMENTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataAggiornamento(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getCodUfficioAggiornamento() != null && aModel.getCodUfficioAggiornamento().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_AGGIORNAMENTO = '" + aModel.getCodUfficioAggiornamento() + "' "; 
    } 
    
    if (aModel.getNumGiorniMap() != null ) { 
      lCondizioni += " and NUM_GIORNI_MAP = " + aModel.getNumGiorniMap() + ""; 
    } 
    if (aModel.getStatIdStatoEsecTitCum() != null ) { 
      lCondizioni += " and STAT_ID_STATO_ESEC_TIT_CUM = " + aModel.getStatIdStatoEsecTitCum() + ""; 
    } 
    if (aModel.getCodTipoMisura() != null && aModel.getCodTipoMisura().length() > 0) { 
      lCondizioni += " and COD_TIPO_MISURA = '" + aModel.getCodTipoMisura() + "' "; 
    } 
    if (aModel.getIstDetIdIstitutoDetenzione() != null && aModel.getIstDetIdIstitutoDetenzione().length() > 0) { 
      lCondizioni += " and IST_DET_ID_ISTITUTO_DETENZIONE = '" + aModel.getIstDetIdIstitutoDetenzione() + "' "; 
    } 
    if (aModel.getAltroLuogoDetenzione() != null && aModel.getAltroLuogoDetenzione().length() > 0) { 
      lCondizioni += " and ALTRO_LUOGO_DETENZIONE = '" + aModel.getAltroLuogoDetenzione() + "' "; 
    } 
    
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni); 
  }


  /***************************************************************************** 
   * Imposta la condizione di where per l'operazione di update puntuale 
   * si entra sempre in chiave 
   * @param key 
   ****************************************************************************/ 
  public void selCondizioneUpdate( BigDecimal aIdComputiCumulo) {
    String lCondizioni = new String();

    lCondizioni += " and ID_COMPUTI_CUMULO = " + aIdComputiCumulo;
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni);
  }

  public void selCondizioneUpdateByIdStatEsec( BigDecimal aIdStatoEsecuzione) {
    String lCondizioni = new String();

    lCondizioni += " and STAT_ID_STATO_ESEC_TIT_CUM = " + aIdStatoEsecuzione;
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni);
  }

  /***************************************************************************** 
   * Imposta la condizione di order by per la ricerca  
   *  
   *****************************************************************************/ 
  public void setOrderBy() { 
    String orderBy = ""; 
    //======================================================================= 
    // Lasciare orderBy="" se non si vuole scegliere un ordinamento, 
    // altrimenti elencare i campi separati da virgola 
    // n.b. non inserire la clausola ORDER BY, viene aggiunta automaticamente 
    //======================================================================= 

    setOrder(orderBy); 
  } 

}
