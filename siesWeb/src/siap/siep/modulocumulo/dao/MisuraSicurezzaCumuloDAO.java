package siap.siep.modulocumulo.dao;

/**
* <p>Title: MisuraSicurezzaCumuloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella MisuraSicurezzaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class MisuraSicurezzaCumuloDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public MisuraSicurezzaCumuloDAO (Connection con) {
    super(con);
    setTable("MISURA_SICUREZZA_CUMULO");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_MISURA_SICUREZZA_CUMULO","MIS_SIC_CUM_SEQ");

    //setField("ID_MISURA_SICUREZZA_CUMULO", BIG_DECIMAL);
    setField("COD_NATURA"                 , STRING);
    setField("COD_TIPO"                   , STRING);
    setField("NUM_ANNI"                   , BIG_DECIMAL);
    setField("NUM_MESI"                   , BIG_DECIMAL);
    setField("NUM_GIORNI"                 , BIG_DECIMAL);
    setField("ANNO_REG_38"                , BIG_DECIMAL);
    setField("NUM_REG_38"                 , BIG_DECIMAL);
    setField("MOTIVO_MODIFICA"            , STRING);
    setField("FLAG_STATO"                 , STRING);
    setField("TIT_ID_TITOLO_CUMULATO"     , BIG_DECIMAL);
    setField("ID_MISURA_SICUREZZA_ORIGINE", BIG_DECIMAL);
    
    setField("FLAG_ANNULLA_MISURA"			, STRING);
    setField("DATA_FINE_VALIDITA"			, DATE);
    setField("MIS_ID_MISURA_SICUREZZA_CUMULO"	, BIG_DECIMAL);
    setField("MIS_ID_MISURA_SICUREZZA_ORIG"		, BIG_DECIMAL);
    setField("IST_DET_ID_ISTITUTO_DETENZIONE"	, STRING);
    setField("LUOGO_ESECUZIONE_MISURA"			, STRING);
    
    setField("ANNO_FASCICOLO_CLASSE_IV"         , BIG_DECIMAL);
    setField("NUMERO_FASCICOLO_CLASSE_IV"       , BIG_DECIMAL);
    setField("COD_AUTORITA_EMITT_CLASSE_IV"     , STRING);
    setField("LUOGO_AUTORITA_EMITT_CLASSE_IV"   , STRING);
    setField("FLAG_STATO_MISURA"                , STRING);
    
    setField("FLAG_DATI_FINALI"              , STRING);

    setField("COD_OPERATORE_INSERIMENTO"  , STRING);
    setField("DATA_INSERIMENTO"           , DATE);
    setField("COD_UFFICIO_INSERIMENTO"    , STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO"         , DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO"  , STRING);
  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  BigDecimal  getIdMisuraSicurezzaCumulo()    throws DAOException  { return getBigDecimal ("ID_MISURA_SICUREZZA_CUMULO" ); } 
  public  String      getCodNatura()                  throws DAOException  { return getString     ("COD_NATURA"                 ); } 
  public  String      getCodTipo()                    throws DAOException  { return getString     ("COD_TIPO"                   ); } 
  public  BigDecimal  getNumAnni()                    throws DAOException  { return getBigDecimal ("NUM_ANNI"                   ); } 
  public  BigDecimal  getNumMesi()                    throws DAOException  { return getBigDecimal ("NUM_MESI"                   ); } 
  public  BigDecimal  getNumGiorni()                  throws DAOException  { return getBigDecimal ("NUM_GIORNI"                 ); } 
  public  BigDecimal  getAnnoReg38()                  throws DAOException  { return getBigDecimal ("ANNO_REG_38"                ); } 
  public  BigDecimal  getNumReg38()                   throws DAOException  { return getBigDecimal ("NUM_REG_38"                 ); }
  
  public String		 getFlagAnnullaMisura()				throws DAOException  { return getString("FLAG_ANNULLA_MISURA"); }
  public Date		 getDataFineValidita()				throws DAOException  { return getDate("DATA_FINE_VALIDITA"); }
  public BigDecimal	 getMisIdMisuraSicurezzaCumulo()	throws DAOException  { return getBigDecimal("MIS_ID_MISURA_SICUREZZA_CUMULO"); }
  public BigDecimal	 getMisIdMisuraSicurezzaOrigine()	throws DAOException  { return getBigDecimal("MIS_ID_MISURA_SICUREZZA_ORIG"); }
  public String 	getIstDetIdIstitutoDetenzione()		throws DAOException	{ return getString("IST_DET_ID_ISTITUTO_DETENZIONE");}
  public String		getLuogoEsecuzioneMisura()			throws DAOException	{ return getString("LUOGO_ESECUZIONE_MISURA"); }
  
  public  BigDecimal  getAnnoFascicoloSiepIV()          throws DAOException  { return getBigDecimal ("ANNO_FASCICOLO_CLASSE_IV"    ); }  
  public  BigDecimal  getNumeroFascicoloSiepIV()        throws DAOException  { return getBigDecimal ("NUMERO_FASCICOLO_CLASSE_IV"  ); }
  public  String      getCodAutoritaEmittenteIV()      	throws DAOException  { return getString     ("COD_AUTORITA_EMITT_CLASSE_IV" ); } 
  //public String      getDescrAutoritaEmittenteIV() 		throws DAOException  { return getString  }
  public  String      getCodLuogoEmittenteIV()      	throws DAOException  { return getString     ("LUOGO_AUTORITA_EMITT_CLASSE_IV" ); } 
  //public String      getDescrLuogoEmittenteIV()  		throws DAOException  { return getString  }
  public  String      getFlagStatoMisura()              throws DAOException  { return getString     ("FLAG_STATO_MISURA"            ); } 
 // public String      getDescrFlagStatoMisura()    		throws DAOException  { return getString  }
  
  public  String      getMotivoModifica()             throws DAOException  { return getString     ("MOTIVO_MODIFICA"            ); }
  public  String      getFlagStato()                  throws DAOException  { return getString     ("FLAG_STATO"                 ); } 
  public  BigDecimal  getTitIdTitoloCumulato()        throws DAOException  { return getBigDecimal ("TIT_ID_TITOLO_CUMULATO"     ); } 
  public  BigDecimal  getIdMisuraSicurezzaOrigine()   throws DAOException  { return getBigDecimal ("ID_MISURA_SICUREZZA_ORIGINE"); } 
  public  String      getFlagDatiFinali()             throws DAOException  { return getString     ("FLAG_DATI_FINALI"              ); } 
 
  public  String      getCodOperatoreInserimento()    throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"  ); } 
  public  Date        getDataInserimento()            throws DAOException  { return getDate       ("DATA_INSERIMENTO"           ); } 
  public  String      getCodUfficioInserimento()      throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"    ); } 
  public  String      getCodOperatoreAggiornamento()  throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"); } 
  public  Date        getDataAggiornamento()          throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"         ); } 
  public  String      getCodUfficioAggiornamento()    throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"  ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdMisuraSicurezzaCumulo    (BigDecimal  aValore )   { setBigDecimal ("ID_MISURA_SICUREZZA_CUMULO" , aValore); } 
  public void  setCodNatura                  (String      aValore )   { setString     ("COD_NATURA"                 , aValore); } 
  public void  setCodTipo                    (String      aValore )   { setString     ("COD_TIPO"                   , aValore); } 
  public void  setNumAnni                    (BigDecimal  aValore )   { setBigDecimal ("NUM_ANNI"                   , aValore); } 
  public void  setNumMesi                    (BigDecimal  aValore )   { setBigDecimal ("NUM_MESI"                   , aValore); } 
  public void  setNumGiorni                  (BigDecimal  aValore )   { setBigDecimal ("NUM_GIORNI"                 , aValore); } 
  public void  setAnnoReg38                  (BigDecimal  aValore )   { setBigDecimal ("ANNO_REG_38"                , aValore); } 
  public void  setNumReg38                   (BigDecimal  aValore )   { setBigDecimal ("NUM_REG_38"                 , aValore); }
  
  public void setFlagAnnullaMisura			(String aValore)		{setString("FLAG_ANNULLA_MISURA"				, aValore); }
  public void setDataFineValidita			(Date aValore)			{setDate("DATA_FINE_VALIDITA"					, aValore); }
  public void setMisIdMisuraSicurezzaCumulo	(BigDecimal aValore)	{setBigDecimal("MIS_ID_MISURA_SICUREZZA_CUMULO"	, aValore); }
  public void setMisIdMisuraSicurezzaOrigine(BigDecimal aValore)	{setBigDecimal("MIS_ID_MISURA_SICUREZZA_ORIG"	, aValore); }
  public void setIstDetIdIstitutoDetenzione	(String aValore)		{setString("IST_DET_ID_ISTITUTO_DETENZIONE"		, aValore); }
  public void setLuogoEsecuzioneMisura		(String aValore)		{setString("LUOGO_ESECUZIONE_MISURA"			, aValore); }
  
  public void  setAnnoFascicoloSiepIV     	(BigDecimal  aValore )   { setBigDecimal ("ANNO_FASCICOLO_CLASSE_IV"    , aValore); } 
  public void  setNumeroFascicoloSiepIV     (BigDecimal  aValore )   { setBigDecimal ("NUMERO_FASCICOLO_CLASSE_IV"  , aValore); } 
  public void  setCodAutoritaEmittenteIV    (String      aValore )   { setString     ("COD_AUTORITA_EMITT_CLASSE_IV" , aValore); } 
  public void  setCodLuogoEmittenteIV    	(String      aValore )   { setString     ("LUOGO_AUTORITA_EMITT_CLASSE_IV" , aValore); } 
  public void  setFlagStatoMisura           (String      aValore )   { setString     ("FLAG_STATO_MISURA"            , aValore); } 

  public void  setMotivoModifica             (String      aValore )   { setString     ("MOTIVO_MODIFICA"            , aValore); }
  public void  setFlagStato                  (String      aValore )   { setString     ("FLAG_STATO"                 , aValore); } 
  public void  setTitIdTitoloCumulato        (BigDecimal  aValore )   { setBigDecimal ("TIT_ID_TITOLO_CUMULATO"     , aValore); } 
  public void  setIdMisuraSicurezzaOrigine   (BigDecimal  aValore )   { setBigDecimal ("ID_MISURA_SICUREZZA_ORIGINE", aValore); } 
  public void  setFlagDatiFinali             (String      aValore )   { setString     ("FLAG_DATI_FINALI"              , aValore); } 
  
  public void  setCodOperatoreInserimento    (String      aValore )   { setString     ("COD_OPERATORE_INSERIMENTO"  , aValore); } 
  public void  setDataInserimento            (Date        aValore )   { setDate       ("DATA_INSERIMENTO"           , aValore); } 
  public void  setCodUfficioInserimento      (String      aValore )   { setString     ("COD_UFFICIO_INSERIMENTO"    , aValore); } 
  public void  setCodOperatoreAggiornamento  (String      aValore )   { setString     ("COD_OPERATORE_AGGIORNAMENTO", aValore); } 
  public void  setDataAggiornamento          (Date        aValore )   { setDate       ("DATA_AGGIORNAMENTO"         , aValore); } 
  public void  setCodUfficioAggiornamento    (String      aValore )   { setString     ("COD_UFFICIO_AGGIORNAMENTO"  , aValore); } 


  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new MisuraSicurezzaCumuloModel(  
      getIdMisuraSicurezzaCumulo() , 
      getCodNatura() ,  "",
      getCodTipo() ,  "",
      getNumAnni() , 
      getNumMesi() , 
      getNumGiorni() , 
      getAnnoReg38() , 
      getNumReg38() , 
      getFlagAnnullaMisura() ,
      getDataFineValidita() ,
      getMisIdMisuraSicurezzaCumulo() ,
      getMisIdMisuraSicurezzaOrigine() ,
      getIstDetIdIstitutoDetenzione() ,
      getLuogoEsecuzioneMisura() ,
      
      getAnnoFascicoloSiepIV(),
      getNumeroFascicoloSiepIV(),
      getCodAutoritaEmittenteIV(), "",
      getCodLuogoEmittenteIV(), "",
      getFlagStatoMisura(), "",
      
      getMotivoModifica(),
      getFlagStato() , 
      getTitIdTitoloCumulato() , 
      getIdMisuraSicurezzaOrigine() , 
      
      getFlagDatiFinali() , 
      
      getCodOperatoreInserimento() , 
      getDataInserimento() , 
      getCodUfficioInserimento() , 
      getCodOperatoreAggiornamento() , 
      getDataAggiornamento() , 
      getCodUfficioAggiornamento()  
    );
  }


  /***************************************************************************** 
   * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void setDAOFromModel(MisuraSicurezzaCumuloModel aModel) throws DAOException {
    setIdMisuraSicurezzaCumulo    ( aModel.getIdMisuraSicurezzaCumulo()   );  
    setCodNatura                  ( aModel.getCodNatura()                 );  
    setCodTipo                    ( aModel.getCodTipo()                   );  
    setNumAnni                    ( aModel.getNumAnni()                   );  
    setNumMesi                    ( aModel.getNumMesi()                   );  
    setNumGiorni                  ( aModel.getNumGiorni()                 );  
    setAnnoReg38                  ( aModel.getAnnoReg38()                 );  
    setNumReg38                   ( aModel.getNumReg38()                  );
    setFlagAnnullaMisura		  ( aModel.getFlagAnnullaMisura()	);
    setDataFineValidita			  ( aModel.getDataFineValidita()	);
    setMisIdMisuraSicurezzaCumulo (aModel.getMisIdMisuraSicurezzaCumulo()  );
    setMisIdMisuraSicurezzaOrigine(aModel.getMisIdMisuraSicurezzaOrigine() );
    setIstDetIdIstitutoDetenzione (aModel.getIstDetIdIstitutoDetenzione()  );
    setLuogoEsecuzioneMisura	  (aModel.getLuogoEsecuzioneMisura() );
    
    setAnnoFascicoloSiepIV		(aModel.getAnnoFascicoloSiepIV());
    setNumeroFascicoloSiepIV	(aModel.getNumeroFascicoloSiepIV());
    setCodAutoritaEmittenteIV	(aModel.getCodAutoritaEmittenteIV());
    setCodLuogoEmittenteIV		(aModel.getCodLuogoEmittenteIV());
    setFlagStatoMisura			(aModel.getFlagStatoMisura());
    
    setMotivoModifica             ( aModel.getMotivoModifica()            );  
    setFlagStato                  ( aModel.getFlagStato()                 );  
    setTitIdTitoloCumulato        ( aModel.getTitIdTitoloCumulato()       );  
    setIdMisuraSicurezzaOrigine   ( aModel.getIdMisuraSicurezzaOrigine()  );  
    setFlagDatiFinali              ( aModel.getFlagDatiFinali()             );  
    
    setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
    setDataInserimento            ( aModel.getDataInserimento()           );  
    setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
  }

  /**
   * Definisce le condizioni di set per i soli campi aggiornabili
   * @param aModel
   * @throws DAOException
   */
  public void setDAOFromModelForUpdate(MisuraSicurezzaCumuloModel aModel) throws DAOException
  {
    setCodNatura                  ( aModel.getCodNatura()                 );  
    setCodTipo                    ( aModel.getCodTipo()                   );  
    setNumAnni                    ( aModel.getNumAnni()                   );  
    setNumMesi                    ( aModel.getNumMesi()                   );  
    setNumGiorni                  ( aModel.getNumGiorni()                 );
    
    setDataFineValidita			  ( aModel.getDataFineValidita()		  );
    setMisIdMisuraSicurezzaCumulo ( aModel.getMisIdMisuraSicurezzaCumulo() );
    
    setMotivoModifica             ( aModel.getMotivoModifica()            );  
    setFlagStato                  ( aModel.getFlagStato()                 ); 
    
    setAnnoFascicoloSiepIV		(aModel.getAnnoFascicoloSiepIV()	);
    setNumeroFascicoloSiepIV	(aModel.getNumeroFascicoloSiepIV()	);
    setCodAutoritaEmittenteIV	(aModel.getCodAutoritaEmittenteIV()	);
    setCodLuogoEmittenteIV		(aModel.getCodLuogoEmittenteIV()	);
    setFlagStatoMisura			  (aModel.getFlagStatoMisura()		);

    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
  }

  /**
   * Imposta le condizioni per la cancellazione logica del dato.
   * @param aModel
   * @throws DAOException
   */
  public void setDAOFromModelForDelete(MisuraSicurezzaCumuloModel aModel) throws DAOException
  {
    setMotivoModifica             ( aModel.getMotivoModifica()            );  
    setFlagStato                  ( aModel.getFlagStato()                 );  

    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
  }

  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(MisuraSicurezzaCumuloModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdMisuraSicurezzaCumulo() != null ) { 
      lCondizioni += " and ID_MISURA_SICUREZZA_CUMULO = " + aModel.getIdMisuraSicurezzaCumulo() + ""; 
    } 
    if (aModel.getCodNatura() != null && aModel.getCodNatura().length() > 0) { 
      lCondizioni += " and COD_NATURA = '" + aModel.getCodNatura() + "' "; 
    } 
    if (aModel.getCodTipo() != null && aModel.getCodTipo().length() > 0) { 
      lCondizioni += " and COD_TIPO = '" + aModel.getCodTipo() + "' "; 
    } 
    if (aModel.getNumAnni() != null ) { 
      lCondizioni += " and NUM_ANNI = " + aModel.getNumAnni() + ""; 
    } 
    if (aModel.getNumMesi() != null ) { 
      lCondizioni += " and NUM_MESI = " + aModel.getNumMesi() + ""; 
    } 
    if (aModel.getNumGiorni() != null ) { 
      lCondizioni += " and NUM_GIORNI = " + aModel.getNumGiorni() + ""; 
    } 
    if (aModel.getAnnoReg38() != null ) { 
      lCondizioni += " and ANNO_REG_38 = " + aModel.getAnnoReg38() + ""; 
    } 
    if (aModel.getNumReg38() != null ) { 
      lCondizioni += " and NUM_REG_38 = " + aModel.getNumReg38() + ""; 
    } 
    if (aModel.getMotivoModifica() != null && aModel.getMotivoModifica().length() > 0) { 
      lCondizioni += " and MOTIVO_MODIFICA = '" + aModel.getMotivoModifica() + "' "; 
    } 
    if (aModel.getFlagStato() != null && aModel.getFlagStato().length() > 0) { 
      lCondizioni += " and FLAG_STATO = '" + aModel.getFlagStato() + "' "; 
    } 
    if (aModel.getTitIdTitoloCumulato() != null ) { 
      lCondizioni += " and TIT_ID_TITOLO_CUMULATO = " + aModel.getTitIdTitoloCumulato() + ""; 
    } 
    if (aModel.getIdMisuraSicurezzaOrigine()!= null ) { 
      lCondizioni += " and ID_MISURA_SICUREZZA_ORIGINE = " + aModel.getIdMisuraSicurezzaOrigine() + ""; 
    } 
    if (aModel.getFlagDatiFinali() != null && aModel.getFlagDatiFinali().length() > 0) { 
      lCondizioni += " and FLAG_DATI_FINALI = '" + aModel.getFlagDatiFinali() + "' "; 
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
  public void selCondizioneUpdate( BigDecimal aIdMisuraSicurezzaCumulo) {
    String lCondizioni = new String();

    lCondizioni += " and ID_MISURA_SICUREZZA_CUMULO = " + aIdMisuraSicurezzaCumulo;
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni);
  }

  public void selCondizioneUpdateByIdTitolo ( BigDecimal aIdTitolo) {
    String lCondizioni = new String();

    lCondizioni += " and TIT_ID_TITOLO_CUMULATO = " + aIdTitolo;
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni);
  }
  
  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_MISURA_SICUREZZA_CUMULO = " + key );
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
