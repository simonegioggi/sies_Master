package siap.siep.scambiosanzione.dao;

/**
* <p>Title: ScambioSanzioneDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella ScambioSanzione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class ScambioSanzioneDAO extends TableDAO { 
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public ScambioSanzioneDAO (Connection con) {
    super(con);
    setTable("SCAMBIO_SANZIONE");
 
    setSequenceField("ID_SCAMBIO_SANZIONE","SCA_SAN_SEQ");
    setFieldKey("ID_SCAMBIO_SANZIONE", BIG_DECIMAL);
    
    setField("ID_SCAMBIO_SANZIONE"        , BIG_DECIMAL);
    setField("COD_TIPO_DECISIONE"         , STRING);
    setField("COD_NATURA_SANZIONE"        , STRING);
    setField("COD_TIPO_SANZIONE"          , STRING);
    setField("DATA_INIZIO"                , DATE);
    setField("DATA_FINE"                  , DATE);
    setField("NOTE"                       , STRING);
    setField("ANNO_REGISTRO"              , BIG_DECIMAL);
    setField("NUMERO_REGISTRO"            , BIG_DECIMAL);
    setField("CHIAVE_ANNO_FASCICOLO_SIUS" , BIG_DECIMAL);
    setField("CHIAVE_PROGR_FASCICOLO_SIUS", BIG_DECIMAL);
    setField("COD_UFFICIO_SORVEGLIANZA"   , STRING);
    setField("COD_UFFICIO_EMITTENTE"      , STRING);
    setField("COD_OPERATORE_INSERIMENTO"  , STRING);
    setField("DATA_INSERIMENTO"           , DATE);
    setField("COD_UFFICIO_INSERIMENTO"    , STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO"         , DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO"  , STRING);
    setField("DATA_EMISSIONE"             , DATE);
    setField("EVE_ID_EVENTO"              , BIG_DECIMAL);
    setField("FAS_SIE_ID_FASCICOLO_SIEP"  , BIG_DECIMAL);
//  conversione della sanzione sostitutiva paolo c. 3/3/2008
    setField("NUM_GIORNI_RECLUSIONE"  	  , BIG_DECIMAL);
    setField("NUM_MESI_RECLUSIONE"  	  , BIG_DECIMAL);
    setField("NUM_ANNI_RECLUSIONE"  	  , BIG_DECIMAL);
    setField("NUM_GIORNI_ARRESTO"  		  , BIG_DECIMAL);
    setField("NUM_MESI_ARRESTO"  		  , BIG_DECIMAL);
    setField("NUM_ANNI_ARRESTO"  		  , BIG_DECIMAL);
  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  BigDecimal  getIdScambioSanzione()          throws DAOException  { return getBigDecimal ("ID_SCAMBIO_SANZIONE"        ); } 
  public  String      getCodTipoDecisione()           throws DAOException  { return getString     ("COD_TIPO_DECISIONE"         ); } 
  public  String      getCodNaturaSanzione()          throws DAOException  { return getString     ("COD_NATURA_SANZIONE"        ); } 
  public  String      getCodTipoSanzione()            throws DAOException  { return getString     ("COD_TIPO_SANZIONE"          ); } 
  public  Date        getDataInizio()                 throws DAOException  { return getDate       ("DATA_INIZIO"                ); } 
  public  Date        getDataFine()                   throws DAOException  { return getDate       ("DATA_FINE"                  ); } 
  public  String      getNote()                       throws DAOException  { return getString     ("NOTE"                       ); } 
  public  BigDecimal  getAnnoRegistro()               throws DAOException  { return getBigDecimal ("ANNO_REGISTRO"              ); } 
  public  BigDecimal  getNumeroRegistro()             throws DAOException  { return getBigDecimal ("NUMERO_REGISTRO"            ); } 
  public  BigDecimal  getChiaveAnnoFascicoloSius()    throws DAOException  { return getBigDecimal ("CHIAVE_ANNO_FASCICOLO_SIUS" ); } 
  public  BigDecimal  getChiaveProgrFascicoloSius()   throws DAOException  { return getBigDecimal ("CHIAVE_PROGR_FASCICOLO_SIUS"); } 
  public  String      getCodUfficioSorveglianza()     throws DAOException  { return getString     ("COD_UFFICIO_SORVEGLIANZA"   ); } 
  public  String      getCodUfficioEmittente()        throws DAOException  { return getString     ("COD_UFFICIO_EMITTENTE"      ); } 
  public  String      getCodOperatoreInserimento()    throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"  ); } 
  public  Date        getDataInserimento()            throws DAOException  { return getDate       ("DATA_INSERIMENTO"           ); } 
  public  String      getCodUfficioInserimento()      throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"    ); } 
  public  String      getCodOperatoreAggiornamento()  throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"); } 
  public  Date        getDataAggiornamento()          throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"         ); } 
  public  String      getCodUfficioAggiornamento()    throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"  ); } 
  public  Date        getDataEmissione()              throws DAOException  { return getDate       ("DATA_EMISSIONE"             ); } 
  public  BigDecimal  getEveIdEvento()                throws DAOException  { return getBigDecimal ("EVE_ID_EVENTO"              ); } 
  public  BigDecimal  getFasSieIdFascicoloSiep()      throws DAOException  { return getBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"	); }
//conversione della sanzione sostitutiva paolo c. 3/3/2008
  public  BigDecimal  getNumGiorniReclusione()        throws DAOException  { return getBigDecimal 	("NUM_GIORNI_RECLUSIONE"	); }
  public  BigDecimal  getNumMesiReclusione()          throws DAOException  { return getBigDecimal 	("NUM_MESI_RECLUSIONE"		); }
  public  BigDecimal  getNumAnniReclusione()          throws DAOException  { return getBigDecimal 	("NUM_ANNI_RECLUSIONE"		); }
  public  BigDecimal  getNumGiorniArresto()        	  throws DAOException  { return getBigDecimal 	("NUM_GIORNI_ARRESTO"		); }
  public  BigDecimal  getNumMesiArresto()             throws DAOException  { return getBigDecimal 	("NUM_MESI_ARRESTO"			); }
  public  BigDecimal  getNumAnniArresto()             throws DAOException  { return getBigDecimal 	("NUM_ANNI_ARRESTO"			); }


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdScambioSanzione          (BigDecimal  aValore )   { setBigDecimal ("ID_SCAMBIO_SANZIONE"        , aValore); } 
  public void  setCodTipoDecisione           (String      aValore )   { setString     ("COD_TIPO_DECISIONE"         , aValore); } 
  public void  setCodNaturaSanzione          (String      aValore )   { setString     ("COD_NATURA_SANZIONE"        , aValore); } 
  public void  setCodTipoSanzione            (String      aValore )   { setString     ("COD_TIPO_SANZIONE"          , aValore); } 
  public void  setDataInizio                 (Date        aValore )   { setDate       ("DATA_INIZIO"                , aValore); } 
  public void  setDataFine                   (Date        aValore )   { setDate       ("DATA_FINE"                  , aValore); } 
  public void  setNote                       (String      aValore )   { setString     ("NOTE"                       , aValore); } 
  public void  setAnnoRegistro               (BigDecimal  aValore )   { setBigDecimal ("ANNO_REGISTRO"              , aValore); } 
  public void  setNumeroRegistro             (BigDecimal  aValore )   { setBigDecimal ("NUMERO_REGISTRO"            , aValore); } 
  public void  setChiaveAnnoFascicoloSius    (BigDecimal  aValore )   { setBigDecimal ("CHIAVE_ANNO_FASCICOLO_SIUS" , aValore); } 
  public void  setChiaveProgrFascicoloSius   (BigDecimal  aValore )   { setBigDecimal ("CHIAVE_PROGR_FASCICOLO_SIUS", aValore); } 
  public void  setCodUfficioSorveglianza     (String      aValore )   { setString     ("COD_UFFICIO_SORVEGLIANZA"   , aValore); } 
  public void  setCodUfficioEmittente        (String      aValore )   { setString     ("COD_UFFICIO_EMITTENTE"      , aValore); } 
  public void  setCodOperatoreInserimento    (String      aValore )   { setString     ("COD_OPERATORE_INSERIMENTO"  , aValore); } 
  public void  setDataInserimento            (Date        aValore )   { setDate       ("DATA_INSERIMENTO"           , aValore); } 
  public void  setCodUfficioInserimento      (String      aValore )   { setString     ("COD_UFFICIO_INSERIMENTO"    , aValore); } 
  public void  setCodOperatoreAggiornamento  (String      aValore )   { setString     ("COD_OPERATORE_AGGIORNAMENTO", aValore); } 
  public void  setDataAggiornamento          (Date        aValore )   { setDate       ("DATA_AGGIORNAMENTO"         , aValore); } 
  public void  setCodUfficioAggiornamento    (String      aValore )   { setString     ("COD_UFFICIO_AGGIORNAMENTO"  , aValore); } 
  public void  setDataEmissione              (Date        aValore )   { setDate       ("DATA_EMISSIONE"             , aValore); } 
  public void  setEveIdEvento                (BigDecimal  aValore )   { setBigDecimal ("EVE_ID_EVENTO"              , aValore); } 
  public void  setFasSieIdFascicoloSiep      (BigDecimal  aValore )	  { setBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"	, aValore); }
//conversione della sanzione sostitutiva paolo c. 3/3/2008
  public void  setNumGiorniReclusione      (BigDecimal  aValore ) { setBigDecimal   	("NUM_GIORNI_RECLUSIONE"	, aValore); }
  public void  setNumMesiReclusione        (BigDecimal  aValore ) { setBigDecimal		("NUM_MESI_RECLUSIONE"		, aValore); }
  public void  setNumAnniReclusione        (BigDecimal  aValore ) { setBigDecimal 		("NUM_ANNI_RECLUSIONE"		, aValore); }
  public void  setNumGiorniArresto         (BigDecimal  aValore ) { setBigDecimal 		("NUM_GIORNI_ARRESTO"		, aValore); }
  public void  setNumMesiArresto           (BigDecimal  aValore ) { setBigDecimal 		("NUM_MESI_ARRESTO"			, aValore); }
  public void  setNumAnniArresto           (BigDecimal  aValore ) { setBigDecimal 		("NUM_ANNI_ARRESTO"			, aValore); }

  
  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new ScambioSanzioneModel(  
      getIdScambioSanzione() , 
      getCodTipoDecisione() , 
 "",
      getCodNaturaSanzione() , 
 "",
      getCodTipoSanzione() , 
 "",
      getDataInizio() , 
      getDataFine() , 
      getNote() , 
      getAnnoRegistro() , 
      getNumeroRegistro() , 
      getChiaveAnnoFascicoloSius() , 
      getChiaveProgrFascicoloSius() , 
      getCodUfficioSorveglianza() , 
 "",
      getCodUfficioEmittente() , 
 "",
      getCodOperatoreInserimento() , 
      getDataInserimento() , 
      getCodUfficioInserimento() , 
 "",
      getCodOperatoreAggiornamento() , 
      getDataAggiornamento() , 
      getCodUfficioAggiornamento() , 
 "",
      getDataEmissione() , 
      getEveIdEvento(),
      getFasSieIdFascicoloSiep(),
//    conversione della sanzione sostitutiva paolo c. 3/3/2008
      getNumGiorniReclusione(),
      getNumMesiReclusione(),
      getNumAnniReclusione(),
      getNumGiorniArresto(),
      getNumMesiArresto(),
      getNumAnniArresto()
    );
  }


  /***************************************************************************** 
   * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void setDAOFromModel(ScambioSanzioneModel aModel) throws DAOException {
    setIdScambioSanzione          ( aModel.getIdScambioSanzione()         );  
    setCodTipoDecisione           ( aModel.getCodTipoDecisione()          );  
    setCodNaturaSanzione          ( aModel.getCodNaturaSanzione()         );  
    setCodTipoSanzione            ( aModel.getCodTipoSanzione()           );  
    setDataInizio                 ( aModel.getDataInizio()                );  
    setDataFine                   ( aModel.getDataFine()                  );  
    setNote                       ( aModel.getNote()                      );  
    setAnnoRegistro               ( aModel.getAnnoRegistro()              );  
    setNumeroRegistro             ( aModel.getNumeroRegistro()            );  
    setChiaveAnnoFascicoloSius    ( aModel.getChiaveAnnoFascicoloSius()   );  
    setChiaveProgrFascicoloSius   ( aModel.getChiaveProgrFascicoloSius()  );  
    setCodUfficioSorveglianza     ( aModel.getCodUfficioSorveglianza()    );  
    setCodUfficioEmittente        ( aModel.getCodUfficioEmittente()       );  
    setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
    setDataInserimento            ( aModel.getDataInserimento()           );  
    setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
    setDataEmissione              ( aModel.getDataEmissione()             );  
    setEveIdEvento                ( aModel.getEveIdEvento()               ); 
    setFasSieIdFascicoloSiep	  ( aModel.getFasSieIdFascicoloSiep()	  );
//  conversione della sanzione sostitutiva paolo c. 3/3/2008
    setNumGiorniReclusione        ( aModel.getNumGiorniReclusione()       );
    setNumMesiReclusione          ( aModel.getNumMesiReclusione()	      );		
    setNumAnniReclusione          ( aModel.getNumAnniReclusione()	      );		
    setNumGiorniArresto           ( aModel.getNumGiorniArresto()	      );		
    setNumMesiArresto             ( aModel.getNumMesiArresto()	          );		
    setNumAnniArresto             ( aModel.getNumAnniArresto()	          );		
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(ScambioSanzioneModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdScambioSanzione() != null ) { 
      lCondizioni += " and ID_SCAMBIO_SANZIONE = " + aModel.getIdScambioSanzione() + ""; 
    } 
    if (aModel.getCodTipoDecisione() != null && aModel.getCodTipoDecisione().length() > 0) { 
      lCondizioni += " and COD_TIPO_DECISIONE = '" + aModel.getCodTipoDecisione() + "' "; 
    } 
    if (aModel.getCodNaturaSanzione() != null && aModel.getCodNaturaSanzione().length() > 0) { 
      lCondizioni += " and COD_NATURA_SANZIONE = '" + aModel.getCodNaturaSanzione() + "' "; 
    } 
    if (aModel.getCodTipoSanzione() != null && aModel.getCodTipoSanzione().length() > 0) { 
      lCondizioni += " and COD_TIPO_SANZIONE = '" + aModel.getCodTipoSanzione() + "' "; 
    } 
    if (aModel.getDataInizio() != null ) { 
      lCondizioni += " and to_char(DATA_INIZIO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInizio(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataFine() != null ) { 
      lCondizioni += " and to_char(DATA_FINE,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataFine(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getNote() != null && aModel.getNote().length() > 0) { 
      lCondizioni += " and NOTE = '" + aModel.getNote() + "' "; 
    } 
    if (aModel.getAnnoRegistro() != null ) { 
      lCondizioni += " and ANNO_REGISTRO = " + aModel.getAnnoRegistro() + ""; 
    } 
    if (aModel.getNumeroRegistro() != null ) { 
      lCondizioni += " and NUMERO_REGISTRO = " + aModel.getNumeroRegistro() + ""; 
    } 
    if (aModel.getChiaveAnnoFascicoloSius() != null ) { 
      lCondizioni += " and CHIAVE_ANNO_FASCICOLO_SIUS = " + aModel.getChiaveAnnoFascicoloSius() + ""; 
    } 
    if (aModel.getChiaveProgrFascicoloSius() != null ) { 
      lCondizioni += " and CHIAVE_PROGR_FASCICOLO_SIUS = " + aModel.getChiaveProgrFascicoloSius() + ""; 
    } 
    if (aModel.getCodUfficioSorveglianza() != null && aModel.getCodUfficioSorveglianza().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_SORVEGLIANZA = '" + aModel.getCodUfficioSorveglianza() + "' "; 
    } 
    if (aModel.getCodUfficioEmittente() != null && aModel.getCodUfficioEmittente().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_EMITTENTE = '" + aModel.getCodUfficioEmittente() + "' "; 
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
    if (aModel.getDataEmissione() != null ) { 
      lCondizioni += " and to_char(DATA_EMISSIONE,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataEmissione(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getEveIdEvento() != null ) { 
      lCondizioni += " and EVE_ID_EVENTO = " + aModel.getEveIdEvento() + ""; 
    }
    if (aModel.getFasSieIdFascicoloSiep() != null ) { 
        lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + ""; 
      }
//  conversione della sanzione sostitutiva paolo c. 3/3/2008
    if (aModel.getNumGiorniReclusione() != null ) { 
        lCondizioni += " and NUM_GIORNI_RECLUSIONE = " + aModel.getNumGiorniReclusione() + ""; 
      }
    if (aModel.getNumMesiReclusione() != null ) { 
        lCondizioni += " and NUM_MESI_RECLUSIONE = " + aModel.getNumMesiReclusione() + ""; 
      }
    if (aModel.getNumAnniReclusione() != null ) { 
        lCondizioni += " and NUM_ANNI_RECLUSIONE = " + aModel.getNumAnniReclusione() + ""; 
      }
    if (aModel.getNumGiorniArresto() != null ) { 
        lCondizioni += " and NUM_GIORNI_ARRESTO = " + aModel.getNumGiorniArresto() + ""; 
      }
    if (aModel.getNumMesiArresto() != null ) { 
        lCondizioni += " and NUM_MESI_ARRESTO = " + aModel.getNumMesiArresto() + ""; 
      }
    if (aModel.getNumAnniArresto() != null ) { 
        lCondizioni += " and NUM_ANNI_ARRESTO = " + aModel.getNumAnniArresto() + ""; 
      }	

    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni); 
  }

  /** 
   * Imposta la condizione di where per l'operazione di update puntuale 
   * si entra sempre in chiave 
   * @param key 
   */ 
  public void selCondizioneUpdate( BigDecimal aIdScambioSanzione) {
    String lCondizioni = new String();

    lCondizioni += " and ID_SCAMBIO_SANZIONE = " + aIdScambioSanzione;
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni);
  }


  /** 
   * Imposta la condizione di order by per la ricerca  
   *  
   */ 
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
   * La funzione controlla se esiste almeno un record in tabelle 
   * SCAMBIO_SANZIONE legata all'Evento specificato dal suo ID.
   * @param aIdEvento
   * @return
   * @throws DAOException
   */
  
  public boolean esisteScambioSanzionePerEvento(BigDecimal aIdEvento) throws DAOException
  {
	  boolean lRet = false;
	  setCondition(" EVE_ID_EVENTO = " + aIdEvento );
	  start();
	  if (next() )
		  lRet = true;
	  
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Esiste Scambio Sanzione per evento -> " + aIdEvento + " ? " + lRet);
  
	  return lRet;
  }
 
  /** 
   * Imposta la condizione di where per l'operazione di delete 
   * @param key 
   */ 
  public void selCondizionebyEvento( BigDecimal aIdEvento) {
    String lCondizioni = new String();

    lCondizioni += " and EVE_ID_EVENTO = " + aIdEvento;
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni);
  }

}