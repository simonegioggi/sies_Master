package siap.siep.competenza.dao;

/**
* <p>Title: CompetenzaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Competenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;
import f3b.util.DateUtils;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.dao.TableDAO;

import siap.siep.competenza.model.CompetenzaModel;

public class CompetenzaDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public CompetenzaDAO (Connection con) {
    super(con);
    setTable("COMPETENZA");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_COMPETENZA","COMP_SEQ");

    //setField("ID_COMPETENZA", BIG_DECIMAL);
    setField("COD_TIPO_PROVVEDIMENTO"        , STRING);
    setField("DATA_PROVVEDIMENTO"            , DATE);
    setField("COD_TIPO_AUTORITA_EMITTENTE"   , STRING);
    setField("COD_LUOGO_EMITTENTE"           , STRING);
    setField("NUM_SEZIONE_AUTORITA_EMITTENTE", STRING);
    setField("ANNO_SENTENZA"                 , BIG_DECIMAL);
    setField("NUMERO_SENTENZA"               , STRING);
    setField("DATA_IRREVOCABILITA"           , DATE);
    setField("SEN_ID_SENTENZA"               , BIG_DECIMAL);
    setField("FAS_SIE_ID_FASCICOLO_SIEP"     , BIG_DECIMAL);
    setField("EVE_ID_EVENTO"                 , BIG_DECIMAL);
    setField("CHIAVE_ANNO"                   , BIG_DECIMAL);
    setField("CHIAVE_UFFICIO"                , STRING);
    setField("CHIAVE_PROGR"                  , BIG_DECIMAL);
    setField("FLAG_ACCORPATO"                , STRING);
    setField("CHIAVE_UFFICIO_ORIGINE"        , STRING);
    setField("CHIAVE_PROGR_ORIGINE"          , BIG_DECIMAL);    
    
    setField("COD_OPERATORE_INSERIMENTO"     , STRING);
    setField("DATA_INSERIMENTO"              , DATE);
    setField("COD_UFFICIO_INSERIMENTO"       , STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO"   , STRING);
    setField("DATA_AGGIORNAMENTO"            , DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO"     , STRING);
    
    setField("COD_TIPO_AUTORITA_COMP", STRING);
    setField("COD_LUOGO_AUTORITA_COMP", STRING);
    setField("COD_UFFICIO_AUTORITA_COMP", STRING);
    
    setField("ID_MESSAGGIO_RICHIESTA", BIG_DECIMAL);
    
//	Solo per i Record Competenza associati a Messaggi di: TRASFERIMENTO / RIGETTO / RESTITUZIONE  Atti X Competenza 
//	Dati Relativi al titolo Richiesto 
    setField("COD_TIPO_PROVVEDIMENTO_RICH"	 , STRING);
    setField("DATA_PROVVEDIMENTO_RICH"       , DATE);
    setField("COD_TIPO_AUTOR_EMITTENTE_RICH" , STRING);
    setField("COD_LUOGO_EMITTENTE_RICH"      , STRING);
    setField("NUM_SEZIONE_AUTOR_EMITT_RICH"	 , STRING);
    setField("ANNO_SENTENZA_RICH"            , BIG_DECIMAL);
    setField("NUMERO_SENTENZA_RICH"          , STRING);
    setField("DATA_IRREVOCABILITA_RICH"      , DATE);
    setField("COGNOME_SOGGETTO_RICH"		 , STRING);
    setField("NOME_SOGGETTO_RICH"		 	 , STRING);
    setField("DATA_NASCITA_SOGGETTO_RICH"	 , DATE);
    setField("COD_STATO_NASC_SOGGETTO_RICH"  , STRING);
    setField("COD_COMUNE_NASC_SOGGETTO_RICH" , STRING);
    setField("CODICECUI_SOGGETTO_RICH"		 , STRING);
  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  BigDecimal  getIdCompetenza()                 throws DAOException  { return getBigDecimal ("ID_COMPETENZA"                 ); } 
  public  String      getCodTipoProvvedimento()         throws DAOException  { return getString     ("COD_TIPO_PROVVEDIMENTO"        ); } 
  public  Date        getDataProvvedimento()            throws DAOException  { return getDate       ("DATA_PROVVEDIMENTO"            ); } 
  public  String      getCodTipoAutoritaEmittente()     throws DAOException  { return getString     ("COD_TIPO_AUTORITA_EMITTENTE"   ); } 
  public  String      getCodLuogoEmittente()            throws DAOException  { return getString     ("COD_LUOGO_EMITTENTE"           ); } 
  public  String      getNumSezioneAutoritaEmittente()  throws DAOException  { return getString     ("NUM_SEZIONE_AUTORITA_EMITTENTE"); } 
  public  BigDecimal  getAnnoSentenza()                 throws DAOException  { return getBigDecimal ("ANNO_SENTENZA"                 ); } 
  public  String      getNumeroSentenza()               throws DAOException  { return getString     ("NUMERO_SENTENZA"               ); } 
  public  Date        getDataIrrevocabilita()           throws DAOException  { return getDate       ("DATA_IRREVOCABILITA"           ); } 
  public  BigDecimal  getSenIdSentenza()                throws DAOException  { return getBigDecimal ("SEN_ID_SENTENZA"               ); } 
  public  BigDecimal  getFasSieIdFascicoloSiep()        throws DAOException  { return getBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"     ); } 
  public  BigDecimal  getEveIdEvento()                  throws DAOException  { return getBigDecimal ("EVE_ID_EVENTO"                 ); } 
  public  BigDecimal  getChiaveAnno()                   throws DAOException  { return getBigDecimal ("CHIAVE_ANNO"                   ); } 
  public  String      getChiaveUfficio()                throws DAOException  { return getString     ("CHIAVE_UFFICIO"                ); } 
  public  BigDecimal  getChiaveProgr()                  throws DAOException  { return getBigDecimal ("CHIAVE_PROGR"                  ); } 
  public  String      getFlagAccorpato()                throws DAOException  { return getString     ("FLAG_ACCORPATO"                ); } 
  public  String      getChiaveUfficioOrigine()         throws DAOException  { return getString     ("CHIAVE_UFFICIO_ORIGINE"        ); } 
  public  BigDecimal  getChiaveProgrOrigine()           throws DAOException  { return getBigDecimal ("CHIAVE_PROGR_ORIGINE"          ); } 
  
  public  String      getCodOperatoreInserimento()      throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"     ); } 
  public  Date        getDataInserimento()              throws DAOException  { return getDate       ("DATA_INSERIMENTO"              ); } 
  public  String      getCodUfficioInserimento()        throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"       ); } 
  public  String      getCodOperatoreAggiornamento()    throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"   ); } 
  public  Date        getDataAggiornamento()            throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"            ); } 
  public  String      getCodUfficioAggiornamento()      throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"     ); } 

  public String  getCodTipoAutoritaComp()			throws DAOException  { return getString     ("COD_TIPO_AUTORITA_COMP"  ); }
  public String  getCodLuogoAutoritaComp()		throws DAOException  { return getString     	("COD_LUOGO_AUTORITA_COMP"  ); }
  public String  getCodUfficioAutoritaComp()		throws DAOException  { return getString 	("COD_UFFICIO_AUTORITA_COMP");}
  public BigDecimal getIdMessaggioRichiesta()	throws DAOException {return getBigDecimal	("ID_MESSAGGIO_RICHIESTA"); }

//	Dati Relativi al titolo Richiesto
  public  String      getCodTipoProvvedimento_Rich()         throws DAOException  { return getString     ("COD_TIPO_PROVVEDIMENTO_RICH"        ); } 
  public  Date        getDataProvvedimento_Rich()            throws DAOException  { return getDate       ("DATA_PROVVEDIMENTO_RICH"            ); } 
  public  String      getCodTipoAutoritaEmittente_Rich()     throws DAOException  { return getString     ("COD_TIPO_AUTOR_EMITTENTE_RICH"	   ); } 
  public  String      getCodLuogoEmittente_Rich()            throws DAOException  { return getString     ("COD_LUOGO_EMITTENTE_RICH"           ); } 
  public  String      getNumSezioneAutoritaEmittente_Rich()  throws DAOException  { return getString     ("NUM_SEZIONE_AUTOR_EMITT_RICH"	   ); } 
  public  BigDecimal  getAnnoSentenza_Rich()                 throws DAOException  { return getBigDecimal ("ANNO_SENTENZA_RICH"                 ); } 
  public  String      getNumeroSentenza_Rich()               throws DAOException  { return getString     ("NUMERO_SENTENZA_RICH"               ); } 
  public  Date        getDataIrrevocabilita_Rich()           throws DAOException  { return getDate       ("DATA_IRREVOCABILITA_RICH"           ); } 
  public  String	getCognome_Soggetto_Rich()				throws DAOException  { return getString		("COGNOME_SOGGETTO_RICH"				); }
  public  String	getNome_Soggetto_Rich()					throws DAOException  { return getString		("NOME_SOGGETTO_RICH" 					); }	
  public  Date		getDataNascita_Soggetto_Rich()			throws DAOException  { return getDate		("DATA_NASCITA_SOGGETTO_RICH"	 		); }
  public  String	getCodStatoNascita_Soggetto_Rich()		throws DAOException  { return getString		("COD_STATO_NASC_SOGGETTO_RICH"			); }
  public  String	getCodComuneNascita_Soggetto_Rich()		throws DAOException  { return getString		("COD_COMUNE_NASC_SOGGETTO_RICH"		); }
  public  String	getCodiceCui_Soggetto_Rich()			throws DAOException  { return getString		("CODICECUI_SOGGETTO_RICH"				); }
  
  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdCompetenza                 (BigDecimal  aValore )   { setBigDecimal ("ID_COMPETENZA"                 , aValore); } 
  public void  setCodTipoProvvedimento         (String      aValore )   { setString     ("COD_TIPO_PROVVEDIMENTO"        , aValore); } 
  public void  setDataProvvedimento            (Date        aValore )   { setDate       ("DATA_PROVVEDIMENTO"            , aValore); } 
  public void  setCodTipoAutoritaEmittente     (String      aValore )   { setString     ("COD_TIPO_AUTORITA_EMITTENTE"   , aValore); } 
  public void  setCodLuogoEmittente            (String      aValore )   { setString     ("COD_LUOGO_EMITTENTE"           , aValore); } 
  public void  setNumSezioneAutoritaEmittente  (String      aValore )   { setString     ("NUM_SEZIONE_AUTORITA_EMITTENTE", aValore); } 
  public void  setAnnoSentenza                 (BigDecimal  aValore )   { setBigDecimal ("ANNO_SENTENZA"                 , aValore); } 
  public void  setNumeroSentenza               (String      aValore )   { setString     ("NUMERO_SENTENZA"               , aValore); } 
  public void  setDataIrrevocabilita           (Date        aValore )   { setDate       ("DATA_IRREVOCABILITA"           , aValore); } 
  public void  setSenIdSentenza                (BigDecimal  aValore )   { setBigDecimal ("SEN_ID_SENTENZA"               , aValore); } 
  public void  setFasSieIdFascicoloSiep        (BigDecimal  aValore )   { setBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"     , aValore); } 
  public void  setEveIdEvento                  (BigDecimal  aValore )   { setBigDecimal ("EVE_ID_EVENTO"                 , aValore); } 
  
  public void  setChiaveAnno                   (BigDecimal  aValore )   { setBigDecimal ("CHIAVE_ANNO"                   , aValore); } 
  public void  setChiaveUfficio                (String      aValore )   { setString     ("CHIAVE_UFFICIO"                , aValore); } 
  public void  setChiaveProgr                  (BigDecimal  aValore )   { setBigDecimal ("CHIAVE_PROGR"                  , aValore); } 
  public void  setFlagAccorpato                (String      aValore )   { setString     ("FLAG_ACCORPATO"                , aValore); } 
  public void  setChiaveUfficioOrigine         (String      aValore )   { setString     ("CHIAVE_UFFICIO_ORIGINE"        , aValore); } 
  public void  setChiaveProgrOrigine           (BigDecimal  aValore )   { setBigDecimal ("CHIAVE_PROGR_ORIGINE"          , aValore); }   
  
  public void  setCodOperatoreInserimento      (String      aValore )   { setString     ("COD_OPERATORE_INSERIMENTO"     , aValore); } 
  public void  setDataInserimento              (Date        aValore )   { setDate       ("DATA_INSERIMENTO"              , aValore); } 
  public void  setCodUfficioInserimento        (String      aValore )   { setString     ("COD_UFFICIO_INSERIMENTO"       , aValore); } 
  public void  setCodOperatoreAggiornamento    (String      aValore )   { setString     ("COD_OPERATORE_AGGIORNAMENTO"   , aValore); } 
  public void  setDataAggiornamento            (Date        aValore )   { setDate       ("DATA_AGGIORNAMENTO"            , aValore); } 
  public void  setCodUfficioAggiornamento      (String      aValore )   { setString     ("COD_UFFICIO_AGGIORNAMENTO"     , aValore); } 
  public void  setDescrUfficioAggiornamento    (String      aValore )   { setString     ("DESCR_UFFICIO_AGGIORNAMENTO"   , aValore); }
  
  public void  setCodTipoAutoritaComp			(String     aValore )	{ setString     ("COD_TIPO_AUTORITA_COMP"   , aValore); }
  public void  setCodLuogoAutoritaComp			(String     aValore )	{ setString     ("COD_LUOGO_AUTORITA_COMP"   , aValore); }
  public void  setDescrTipoAutoritaComp			(String     aValore )	{ setString     ("DESCR_TIPO_AUTORITA_COMP"  , aValore); }
  public void  setDescrLuogoAutoritaComp		(String     aValore )	{ setString     ("DESCR_LUOGO_AUTORITA_COMP" , aValore); }
  public void  setCodUfficioAutoritaComp		(String 	aValore )	{ setString 	("COD_UFFICIO_AUTORITA_COMP", aValore);}
  public void  setIdMessaggiorichiesta		    (BigDecimal aValore)    {setBigDecimal ("ID_MESSAGGIO_RICHIESTA", aValore); }
  
//Dati Relativi al titolo Richiesto
  public void  setCodTipoProvvedimento_Rich         (String      aValore )   { setString     ("COD_TIPO_PROVVEDIMENTO_RICH"        	, aValore); } 
  public void  setDataProvvedimento_Rich            (Date        aValore )   { setDate       ("DATA_PROVVEDIMENTO_RICH"            	, aValore); } 
  public void  setCodTipoAutoritaEmittente_Rich     (String      aValore )   { setString     ("COD_TIPO_AUTOR_EMITTENTE_RICH"   	, aValore); } 
  public void  setCodLuogoEmittente_Rich            (String      aValore )   { setString     ("COD_LUOGO_EMITTENTE_RICH"           	, aValore); } 
  public void  setNumSezioneAutoritaEmittente_Rich  (String      aValore )   { setString     ("NUM_SEZIONE_AUTOR_EMITT_RICH"		, aValore); } 
  public void  setAnnoSentenza_Rich                 (BigDecimal  aValore )   { setBigDecimal ("ANNO_SENTENZA_RICH"                 	, aValore); } 
  public void  setNumeroSentenza_Rich               (String      aValore )   { setString     ("NUMERO_SENTENZA_RICH"               	, aValore); } 
  public void  setDataIrrevocabilita_Rich           (Date        aValore )   { setDate       ("DATA_IRREVOCABILITA_RICH"           	, aValore); }
  public void	setCognome_Soggetto_Rich			(String      aValore)	 { setString	 ("COGNOME_SOGGETTO_RICH"				, aValore); }
  public void	setNome_Soggetto_Rich				(String      aValore)	 { setString	 ("NOME_SOGGETTO_RICH" 					, aValore); }	
  public void	setDataNascita_Soggetto_Rich		(Date        aValore)	 { setDate		 ("DATA_NASCITA_SOGGETTO_RICH"	 		, aValore); }
  public void	setCodStatoNascita_Soggetto_Rich	(String      aValore)	 { setString	 ("COD_STATO_NASC_SOGGETTO_RICH"		, aValore); }
  public void	setCodComuneNascita_Soggetto_Rich	(String      aValore)	 { setString	 ("COD_COMUNE_NASC_SOGGETTO_RICH"		, aValore); }
  public void	setCodiceCui_Soggetto_Rich			(String      aValore)	 { setString	 ("CODICECUI_SOGGETTO_RICH"				, aValore); }

  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new CompetenzaModel(  
      getIdCompetenza() , 
      getCodTipoProvvedimento() , 
 "",
      getDataProvvedimento() , 
      getCodTipoAutoritaEmittente() , 
 "",
      getCodLuogoEmittente() , 
 "",
      getNumSezioneAutoritaEmittente() , 
      getAnnoSentenza() , 
      getNumeroSentenza() , 
      getDataIrrevocabilita() , 
      getSenIdSentenza() , 
      getFasSieIdFascicoloSiep() , 
      getEveIdEvento() , 
      getChiaveAnno() , 
      getChiaveUfficio() , 
      getChiaveProgr() , 
      getFlagAccorpato() , 
      getChiaveUfficioOrigine() , 
      getChiaveProgrOrigine() ,      
      getCodOperatoreInserimento() , 
      getDataInserimento() , 
      getCodUfficioInserimento() , 
 "",
      getCodOperatoreAggiornamento() , 
      getDataAggiornamento() , 
      getCodUfficioAggiornamento(),
      "",
      getCodTipoAutoritaComp(),
      getCodLuogoAutoritaComp(),
      "",
      "",
      getCodUfficioAutoritaComp(),
      getIdMessaggioRichiesta()
      ,

// Metodi 'get' sui dati relativi al TITOLO RICHIESTO
      getCodTipoProvvedimento_Rich() , 
      "",	// descrProvvedimento
      getDataProvvedimento_Rich() , 
      getCodTipoAutoritaEmittente_Rich() , 
      "",	// DescTipoAutorita
      getCodLuogoEmittente_Rich() , 
      "",	// DesctLuogoAutorita
      getNumSezioneAutoritaEmittente_Rich() , 
      getAnnoSentenza_Rich() , 
      getNumeroSentenza_Rich() , 
      getDataIrrevocabilita_Rich() , 
      getCognome_Soggetto_Rich(),
      getNome_Soggetto_Rich(),
      getDataNascita_Soggetto_Rich(),
      getCodStatoNascita_Soggetto_Rich(),
      getCodComuneNascita_Soggetto_Rich(),
      "",	// DescComuneNascita
      "",	// SiglaProvincia
      getCodiceCui_Soggetto_Rich()
      
    );
  }


  /***************************************************************************** 
   * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void setDAOFromModel(CompetenzaModel aModel) throws DAOException {
    setIdCompetenza                 ( aModel.getIdCompetenza()                );  
    setCodTipoProvvedimento         ( aModel.getCodTipoProvvedimento()        );  
    setDataProvvedimento            ( aModel.getDataProvvedimento()           );  
    setCodTipoAutoritaEmittente     ( aModel.getCodTipoAutoritaEmittente()    );  
    setCodLuogoEmittente            ( aModel.getCodLuogoEmittente()           );  
    setNumSezioneAutoritaEmittente  ( aModel.getNumSezioneAutoritaEmittente() );  
    setAnnoSentenza                 ( aModel.getAnnoSentenza()                );  
    setNumeroSentenza               ( aModel.getNumeroSentenza()              );  
    setDataIrrevocabilita           ( aModel.getDataIrrevocabilita()          );  
    setSenIdSentenza                ( aModel.getSenIdSentenza()               );  
    setFasSieIdFascicoloSiep        ( aModel.getFasSieIdFascicoloSiep()       );  
    setEveIdEvento                  ( aModel.getEveIdEvento()                 );  
    setChiaveAnno                   ( aModel.getChiaveAnno()                  );  
    setChiaveUfficio                ( aModel.getChiaveUfficio()               );  
    setChiaveProgr                  ( aModel.getChiaveProgr()                 );  
    setFlagAccorpato                ( aModel.getFlagAccorpato()               );  
    setChiaveUfficioOrigine         ( aModel.getChiaveUfficioOrigine()        );  
    setChiaveProgrOrigine           ( aModel.getChiaveProgrOrigine()          );    
    
    setCodOperatoreInserimento      ( aModel.getCodOperatoreInserimento()     );  
    setDataInserimento              ( aModel.getDataInserimento()             );  
    setCodUfficioInserimento        ( aModel.getCodUfficioInserimento()       );  
    setCodOperatoreAggiornamento    ( aModel.getCodOperatoreAggiornamento()   );  
    setDataAggiornamento            ( aModel.getDataAggiornamento()           );  
    setCodUfficioAggiornamento      ( aModel.getCodUfficioAggiornamento()     );  
    
    setCodTipoAutoritaComp			(aModel.getCodTipoAutoritaComp());
    setCodLuogoAutoritaComp			(aModel.getCodLuogoAutoritaComp());
    setCodUfficioAutoritaComp	(aModel.getCodUfficioAutoritaComp());
    setIdMessaggiorichiesta		(aModel.getIdMessaggioRichiesta());
   
  //Dati Relativi al titolo Richiesto
    setCodTipoProvvedimento_Rich         ( aModel.getCodTipoProvvedimento_Rich()        );  
    setDataProvvedimento_Rich            ( aModel.getDataProvvedimento_Rich()           );  
    setCodTipoAutoritaEmittente_Rich     ( aModel.getCodTipoAutoritaEmittente_Rich()    );  
    setCodLuogoEmittente_Rich            ( aModel.getCodLuogoEmittente_Rich()           );  
    setNumSezioneAutoritaEmittente_Rich  ( aModel.getNumSezioneAutoritaEmittente_Rich() );  
    setAnnoSentenza_Rich                 ( aModel.getAnnoSentenza_Rich()                );  
    setNumeroSentenza_Rich               ( aModel.getNumeroSentenza_Rich()              );  
    setDataIrrevocabilita_Rich           ( aModel.getDataIrrevocabilita_Rich()          );
    setCognome_Soggetto_Rich			(aModel.getCognome_Soggetto_Rich()				);
    setNome_Soggetto_Rich				(aModel.getNome_Soggetto_Rich()					);
    setDataNascita_Soggetto_Rich		(aModel.getDataNascita_Soggetto_Rich()			);
    setCodStatoNascita_Soggetto_Rich	(aModel.getCodStatoNascita_Soggetto_Rich()		);
    setCodComuneNascita_Soggetto_Rich	(aModel.getCodComuneNascita_Soggetto_Rich()		);
    setCodiceCui_Soggetto_Rich			(aModel.getCodiceCui_Soggetto_Rich()			);
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(CompetenzaModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdCompetenza() != null ) { 
      lCondizioni += " and ID_COMPETENZA = " + aModel.getIdCompetenza() + ""; 
    } 
    if (aModel.getCodTipoProvvedimento() != null && aModel.getCodTipoProvvedimento().length() > 0) { 
      lCondizioni += " and COD_TIPO_PROVVEDIMENTO = '" + aModel.getCodTipoProvvedimento() + "' "; 
    } 
    if (aModel.getDataProvvedimento() != null ) { 
      lCondizioni += " and to_char(DATA_PROVVEDIMENTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataProvvedimento(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getCodTipoAutoritaEmittente() != null && aModel.getCodTipoAutoritaEmittente().length() > 0) { 
      lCondizioni += " and COD_TIPO_AUTORITA_EMITTENTE = '" + aModel.getCodTipoAutoritaEmittente() + "' "; 
    } 
    if (aModel.getCodLuogoEmittente() != null && aModel.getCodLuogoEmittente().length() > 0) { 
      lCondizioni += " and COD_LUOGO_EMITTENTE = '" + aModel.getCodLuogoEmittente() + "' "; 
    } 
    if (aModel.getNumSezioneAutoritaEmittente() != null && aModel.getNumSezioneAutoritaEmittente().length() > 0) { 
      lCondizioni += " and NUM_SEZIONE_AUTORITA_EMITTENTE = '" + aModel.getNumSezioneAutoritaEmittente() + "' "; 
    } 
    if (aModel.getAnnoSentenza() != null ) { 
      lCondizioni += " and ANNO_SENTENZA = " + aModel.getAnnoSentenza() + ""; 
    } 
    if (aModel.getNumeroSentenza() != null && aModel.getNumeroSentenza().length() > 0) { 
      lCondizioni += " and NUMERO_SENTENZA = '" + aModel.getNumeroSentenza() + "' "; 
    } 
    if (aModel.getDataIrrevocabilita() != null ) { 
      lCondizioni += " and to_char(DATA_IRREVOCABILITA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataIrrevocabilita(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getSenIdSentenza() != null ) { 
      lCondizioni += " and SEN_ID_SENTENZA = " + aModel.getSenIdSentenza() + ""; 
    } 
    if (aModel.getFasSieIdFascicoloSiep() != null ) { 
      lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + ""; 
    } 
    if (aModel.getEveIdEvento() != null ) { 
      lCondizioni += " and EVE_ID_EVENTO = " + aModel.getEveIdEvento() + ""; 
    } 
    if (aModel.getChiaveAnno() != null ) { 
      lCondizioni += " and CHIAVE_ANNO = " + aModel.getChiaveAnno() + ""; 
    } 
    if (aModel.getChiaveUfficio() != null && aModel.getChiaveUfficio().length() > 0) { 
      lCondizioni += " and CHIAVE_UFFICIO = '" + aModel.getChiaveUfficio() + "' "; 
    } 
    if (aModel.getChiaveProgr() != null ) { 
      lCondizioni += " and CHIAVE_PROGR = " + aModel.getChiaveProgr() + ""; 
    } 
    if (aModel.getFlagAccorpato() != null && aModel.getFlagAccorpato().length() > 0) { 
      lCondizioni += " and FLAG_ACCORPATO = '" + aModel.getFlagAccorpato() + "' "; 
    } 
    if (aModel.getChiaveUfficioOrigine() != null && aModel.getChiaveUfficioOrigine().length() > 0) { 
      lCondizioni += " and CHIAVE_UFFICIO_ORIGINE = '" + aModel.getChiaveUfficioOrigine() + "' "; 
    } 
    if (aModel.getChiaveProgrOrigine() != null ) { 
      lCondizioni += " and CHIAVE_PROGR_ORIGINE = " + aModel.getChiaveProgrOrigine() + ""; 
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
    
    if (aModel.getCodTipoAutoritaComp() != null && aModel.getCodTipoAutoritaComp().length() > 0) { 
        lCondizioni += " and COD_TIPO_AUTORITA_COMP = '" + aModel.getCodTipoAutoritaComp() + "' "; 
      }
    if (aModel.getCodLuogoAutoritaComp() != null && aModel.getCodLuogoAutoritaComp().length() > 0) { 
        lCondizioni += " and COD_LUOGO_AUTORITA_COMP = '" + aModel.getCodLuogoAutoritaComp() + "' "; 
      } 
    if (aModel.getCodUfficioAutoritaComp() != null && aModel.getCodUfficioAutoritaComp().length() > 0) { 
        lCondizioni += " and COD_UFFICIO_AUTORITA_COMP = '" + aModel.getCodUfficioAutoritaComp() + "' "; 
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
  public void selCondizioneUpdate( BigDecimal aIdCompetenza) {
    String lCondizioni = new String();

    lCondizioni += " and ID_COMPETENZA = " + aIdCompetenza;
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
