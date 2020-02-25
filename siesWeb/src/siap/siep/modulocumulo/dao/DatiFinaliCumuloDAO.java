package siap.siep.modulocumulo.dao;

/**
* <p>Title: DatiFinaliCumuloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella DatiFinaliCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.modulocumulo.model.DatiFinaliCumuloModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class DatiFinaliCumuloDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public DatiFinaliCumuloDAO (Connection con) {
    super(con);
    setTable("DATI_FINALI_CUMULO");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_DATI_FINALI_CUMULO","DAT_FIN_CUM_SEQ");

    //setField("ID_DATI_FINALI_CUMULO", BIG_DECIMAL);
    setField("TIPO_UFFICIO_EMISSIONE"     , STRING);
    setField("DATA_PROVVEDIMENTO"         , DATE);
    setField("COD_TIPO_PROVVEDIMENTO"     , STRING);
    setField("ANNO_PROVVEDIMENTO"         , BIG_DECIMAL);
    setField("NUMERO_PROVVEDIMENTO"       , BIG_DECIMAL);
    setField("COD_TIPO_UFFICIO_EMITTENTE" , STRING);
    setField("COD_LUOGO_UFFICIO_EMITTENTE", STRING);
    setField("SEZIONE_UFFICIO_EMITTENTE"  , STRING);
    setField("FLAG_CREA_FASCICOLO_MS"      , STRING);
    setField("FAS_SIE_ID_FASCICOLO_SIEP_MS", BIG_DECIMAL);
    setField("EVE_ID_EVENTO"              , BIG_DECIMAL);
    setField("ISTR_ID_ISTRUTTORIA_CUMULO" , BIG_DECIMAL);
    
    setField("FLAG_PRIMO_CUMULO"      , STRING);
    
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
  public  BigDecimal  getIdDatiFinaliCumulo()         throws DAOException  { return getBigDecimal ("ID_DATI_FINALI_CUMULO"      ); } 
  public  String      getTipoUfficioEmissione()       throws DAOException  { return getString     ("TIPO_UFFICIO_EMISSIONE"     ); } 
  public  Date        getDataProvvedimento()          throws DAOException  { return getDate       ("DATA_PROVVEDIMENTO"         ); } 
  public  String      getCodTipoProvvedimento()       throws DAOException  { return getString     ("COD_TIPO_PROVVEDIMENTO"     ); } 
  public  BigDecimal  getAnnoProvvedimento()          throws DAOException  { return getBigDecimal ("ANNO_PROVVEDIMENTO"         ); } 
  public  BigDecimal  getNumeroProvvedimento()        throws DAOException  { return getBigDecimal ("NUMERO_PROVVEDIMENTO"       ); } 
  public  String      getCodTipoUfficioEmittente()    throws DAOException  { return getString     ("COD_TIPO_UFFICIO_EMITTENTE" ); } 
  public  String      getCodLuogoUfficioEmittente()   throws DAOException  { return getString     ("COD_LUOGO_UFFICIO_EMITTENTE"); } 
  public  String      getSezioneUfficioEmittente()    throws DAOException  { return getString     ("SEZIONE_UFFICIO_EMITTENTE"  ); } 
  public  String      getFlagCreaFascicoloMs()        throws DAOException  { return getString     ("FLAG_CREA_FASCICOLO_MS"      ); } 
  public  BigDecimal  getFasSieIdFascicoloSiepMs()    throws DAOException  { return getBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP_MS"); }  
  public  BigDecimal  getEveIdEvento()                throws DAOException  { return getBigDecimal ("EVE_ID_EVENTO"              ); } 
  public  BigDecimal  getIstrIdIstruttoriaCumulo()    throws DAOException  { return getBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO" ); }
  
  public  String	  getFlagPrimoCumulo()			  throws DAOException  { return getString	  ("FLAG_PRIMO_CUMULO");	}
  
  public  String      getCodOperatoreInserimento()    throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"  ); } 
  public  Date        getDataInserimento()            throws DAOException  { return getDate       ("DATA_INSERIMENTO"           ); } 
  public  String      getCodUfficioInserimento()      throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"    ); } 
  public  String      getCodOperatoreAggiornamento()  throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"); } 
  public  Date        getDataAggiornamento()          throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"         ); } 
  public  String      getCodUfficioAggiornamento()    throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"  ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdDatiFinaliCumulo         (BigDecimal  aValore )   { setBigDecimal ("ID_DATI_FINALI_CUMULO"      , aValore); } 
  public void  setTipoUfficioEmissione       (String      aValore )   { setString     ("TIPO_UFFICIO_EMISSIONE"     , aValore); } 
  public void  setDataProvvedimento          (Date        aValore )   { setDate       ("DATA_PROVVEDIMENTO"         , aValore); } 
  public void  setCodTipoProvvedimento       (String      aValore )   { setString     ("COD_TIPO_PROVVEDIMENTO"     , aValore); } 
  public void  setAnnoProvvedimento          (BigDecimal  aValore )   { setBigDecimal ("ANNO_PROVVEDIMENTO"         , aValore); } 
  public void  setNumeroProvvedimento        (BigDecimal  aValore )   { setBigDecimal ("NUMERO_PROVVEDIMENTO"       , aValore); } 
  public void  setCodTipoUfficioEmittente    (String      aValore )   { setString     ("COD_TIPO_UFFICIO_EMITTENTE" , aValore); } 
  public void  setCodLuogoUfficioEmittente   (String      aValore )   { setString     ("COD_LUOGO_UFFICIO_EMITTENTE", aValore); } 
  public void  setSezioneUfficioEmittente    (String      aValore )   { setString     ("SEZIONE_UFFICIO_EMITTENTE"  , aValore); } 
  public void  setFlagCreaFascicoloMs        (String      aValore )   { setString     ("FLAG_CREA_FASCICOLO_MS"      , aValore); } 
  public void  setFasSieIdFascicoloSiepMs    (BigDecimal  aValore )   { setBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP_MS", aValore); } 
  public void  setEveIdEvento                (BigDecimal  aValore )   { setBigDecimal ("EVE_ID_EVENTO"              , aValore); } 
  public void  setIstrIdIstruttoriaCumulo    (BigDecimal  aValore )   { setBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO" , aValore); }
  
  public void setFlagPrimoCumulo			 (String aValore)		  { setString	  ("FLAG_PRIMO_CUMULO", aValore); }
  
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
    return new DatiFinaliCumuloModel(  
      getIdDatiFinaliCumulo() , 
      getTipoUfficioEmissione() , 
      getDataProvvedimento() , 
      getCodTipoProvvedimento() , 
 "",
      getAnnoProvvedimento() , 
      getNumeroProvvedimento() , 
      getCodTipoUfficioEmittente() , 
 "",
      getCodLuogoUfficioEmittente() , 
 "",
      getSezioneUfficioEmittente() , 
      getFlagCreaFascicoloMs() , 
      getFasSieIdFascicoloSiepMs(),
      getEveIdEvento() , 
      getIstrIdIstruttoriaCumulo() , 
      
      getFlagPrimoCumulo(),
      
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
  public void setDAOFromModel(DatiFinaliCumuloModel aModel) throws DAOException {
    setIdDatiFinaliCumulo         ( aModel.getIdDatiFinaliCumulo()        );  
    setTipoUfficioEmissione       ( aModel.getTipoUfficioEmissione()      );  
    setDataProvvedimento          ( aModel.getDataProvvedimento()         );  
    setCodTipoProvvedimento       ( aModel.getCodTipoProvvedimento()      );  
    setAnnoProvvedimento          ( aModel.getAnnoProvvedimento()         );  
    setNumeroProvvedimento        ( aModel.getNumeroProvvedimento()       );  
    setCodTipoUfficioEmittente    ( aModel.getCodTipoUfficioEmittente()   );  
    setCodLuogoUfficioEmittente   ( aModel.getCodLuogoUfficioEmittente()  );  
    setSezioneUfficioEmittente    ( aModel.getSezioneUfficioEmittente()   );  
    setFlagCreaFascicoloMs        ( aModel.getFlagCreaFascicoloMs()       );  
    setFasSieIdFascicoloSiepMs    ( aModel.getFasSieIdFascicoloSiepMs()   );    
    setEveIdEvento                ( aModel.getEveIdEvento()               );  
    setIstrIdIstruttoriaCumulo    ( aModel.getIstrIdIstruttoriaCumulo()   );
    
    //setFlagPrimoCumulo			  ( aModel.getFlagPrimoCumulo()		);	
    
    setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
    setDataInserimento            ( aModel.getDataInserimento()           );  
    setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
  }
  
  /**
   * Metodo specifico per effattuare l'aggiornamento dei solo campi aggiornabili
   * utilizzando il model
   * @param aModel
   * @throws DAOException
   */
  public void setDAOFromModelForUpdate(DatiFinaliCumuloModel aModel) throws DAOException {
    //setIdDatiFinaliCumulo         ( aModel.getIdDatiFinaliCumulo()        );  
    setTipoUfficioEmissione       ( aModel.getTipoUfficioEmissione()      );  
    setDataProvvedimento          ( aModel.getDataProvvedimento()         );  
    setCodTipoProvvedimento       ( aModel.getCodTipoProvvedimento()      );  
    setAnnoProvvedimento          ( aModel.getAnnoProvvedimento()         );  
    setNumeroProvvedimento        ( aModel.getNumeroProvvedimento()       );  
    setCodTipoUfficioEmittente    ( aModel.getCodTipoUfficioEmittente()   );  
    setCodLuogoUfficioEmittente   ( aModel.getCodLuogoUfficioEmittente()  );  
    setSezioneUfficioEmittente    ( aModel.getSezioneUfficioEmittente()   );  
    
    setFlagPrimoCumulo			  (aModel.getFlagPrimoCumulo()	);
    // n.b. Posizione Giuridica, Evento ed Istruttoria vanno modificate solo 
    //      utilizzando i metodi diretti del DAO setXXXX

//  setFlagCreaFascicoloMs        ( aModel.getFlagCreaFascicoloMs()       );  
//  setFasSieIdFascicoloSiepMs    ( aModel.getFasSieIdFascicoloSiepMs()   );  
    //setEveIdEvento                ( aModel.getEveIdEvento()               );  
    //setIstrIdIstruttoriaCumulo    ( aModel.getIstrIdIstruttoriaCumulo()   );  
    //setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
    //setDataInserimento            ( aModel.getDataInserimento()           );  
    //setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(DatiFinaliCumuloModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdDatiFinaliCumulo() != null ) { 
      lCondizioni += " and ID_DATI_FINALI_CUMULO = " + aModel.getIdDatiFinaliCumulo() + ""; 
    } 
    if (aModel.getTipoUfficioEmissione() != null && aModel.getTipoUfficioEmissione().length() > 0) { 
      lCondizioni += " and TIPO_UFFICIO_EMISSIONE = '" + aModel.getTipoUfficioEmissione() + "' "; 
    } 
    if (aModel.getDataProvvedimento() != null ) { 
      lCondizioni += " and to_char(DATA_PROVVEDIMENTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataProvvedimento(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getCodTipoProvvedimento() != null && aModel.getCodTipoProvvedimento().length() > 0) { 
      lCondizioni += " and COD_TIPO_PROVVEDIMENTO = '" + aModel.getCodTipoProvvedimento() + "' "; 
    } 
    if (aModel.getAnnoProvvedimento() != null ) { 
      lCondizioni += " and ANNO_PROVVEDIMENTO = " + aModel.getAnnoProvvedimento() + ""; 
    } 
    if (aModel.getNumeroProvvedimento() != null ) { 
      lCondizioni += " and NUMERO_PROVVEDIMENTO = " + aModel.getNumeroProvvedimento() + ""; 
    } 
    if (aModel.getCodTipoUfficioEmittente() != null && aModel.getCodTipoUfficioEmittente().length() > 0) { 
      lCondizioni += " and COD_TIPO_UFFICIO_EMITTENTE = '" + aModel.getCodTipoUfficioEmittente() + "' "; 
    } 
    if (aModel.getCodLuogoUfficioEmittente() != null && aModel.getCodLuogoUfficioEmittente().length() > 0) { 
      lCondizioni += " and COD_LUOGO_UFFICIO_EMITTENTE = '" + aModel.getCodLuogoUfficioEmittente() + "' "; 
    } 
    if (aModel.getSezioneUfficioEmittente() != null && aModel.getSezioneUfficioEmittente().length() > 0) { 
      lCondizioni += " and SEZIONE_UFFICIO_EMITTENTE = '" + aModel.getSezioneUfficioEmittente() + "' "; 
    } 
    if (aModel.getFlagCreaFascicoloMs() != null && aModel.getFlagCreaFascicoloMs().length() > 0) { 
      lCondizioni += " and FLAG_CREA_FASCICOLO_MS = '" + aModel.getFlagCreaFascicoloMs() + "' "; 
    } 
    if (aModel.getFasSieIdFascicoloSiepMs() != null ) { 
      lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP_MS = " + aModel.getFasSieIdFascicoloSiepMs() + ""; 
    } 
    if (aModel.getEveIdEvento() != null ) { 
      lCondizioni += " and EVE_ID_EVENTO = " + aModel.getEveIdEvento() + ""; 
    } 
    if (aModel.getIstrIdIstruttoriaCumulo() != null ) { 
      lCondizioni += " and ISTR_ID_ISTRUTTORIA_CUMULO = " + aModel.getIstrIdIstruttoriaCumulo() + ""; 
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
  public void selCondizioneUpdate( BigDecimal aIdDatiFinaliCumulo) {
    String lCondizioni = new String();

    lCondizioni += " and ID_DATI_FINALI_CUMULO = " + aIdDatiFinaliCumulo;
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
