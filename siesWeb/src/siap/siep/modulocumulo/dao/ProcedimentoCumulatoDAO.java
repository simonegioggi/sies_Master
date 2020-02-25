package siap.siep.modulocumulo.dao;

/**
* <p>Title: ProcedimentoCumulatoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella ProcedimentoCumulato</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;
import f3b.util.DateUtils;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;


public class ProcedimentoCumulatoDAO extends TableDAO { 

  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public ProcedimentoCumulatoDAO (Connection con) {
    super(con);
    setTable("PROCEDIMENTO_CUMULATO");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_PROCEDIMENTO_CUMULATO","PROCEDIMENTO_CUMULATO_SEQ");

    //setField("ID_PROCEDIMENTO_CUMULATO", BIG_DECIMAL);
    setField("CHIAVE_ANNO_FAS_CUMULATO"      , BIG_DECIMAL);
    setField("CHIAVE_PROGR_FAS_CUMULATO"     , BIG_DECIMAL);
    setField("COD_TIPO_UFFICIO_FAS_CUMULATO" , STRING);
    setField("COD_LUOGO_UFFICIO_FAS_CUMULATO", STRING);
    setField("COD_UFFICIO_FAS_CUMULATO"      , STRING);
    setField("KEY_PROVV_NSC"                 , BIG_DECIMAL);    

    setField("DATA_RICHIESTA_FASCICOLO"      , DATE);
    setField("DATA_PERVENIMENTO_FASCICOLO"   , DATE);
    setField("NOTE"                          , STRING);
    
    setField("FLAG_ACCORPATO"                , STRING);
    setField("CHIAVE_UFFICIO_ORIGINE"        , STRING);
    setField("CHIAVE_PROGR_ORIGINE"          , BIG_DECIMAL);    
    
    setField("TIT_ID_TITOLO_CUMULATO"        , BIG_DECIMAL);
    setField("FLAG_STATO"                    , STRING);
    setField("MOTIVO_MODIFICA"               , STRING);
    setField("ID_FASCICOLO_SIEP_ORIGINE"     , BIG_DECIMAL);
    setField("EVE_ID_EVENTO" 			     , BIG_DECIMAL);
    
    setField("COD_OPERATORE_INSERIMENTO"     , STRING);
    setField("DATA_INSERIMENTO"              , DATE);
    setField("COD_UFFICIO_INSERIMENTO"       , STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO"   , STRING);
    setField("DATA_AGGIORNAMENTO"            , DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO"     , STRING);
  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  BigDecimal  getIdProcedimentoCumulato()      throws DAOException  { return getBigDecimal ("ID_PROCEDIMENTO_CUMULATO"      ); } 
  public  BigDecimal  getChiaveAnnoFasCumulato()       throws DAOException  { return getBigDecimal ("CHIAVE_ANNO_FAS_CUMULATO"      ); } 
  public  BigDecimal  getChiaveProgrFasCumulato()      throws DAOException  { return getBigDecimal ("CHIAVE_PROGR_FAS_CUMULATO"     ); } 
  public  String      getCodTipoUfficioFasCumulato()   throws DAOException  { return getString     ("COD_TIPO_UFFICIO_FAS_CUMULATO" ); } 
  public  String      getCodLuogoUfficioFasCumulato()  throws DAOException  { return getString     ("COD_LUOGO_UFFICIO_FAS_CUMULATO"); } 
  public  String      getCodUfficioFasCumulato()       throws DAOException  { return getString     ("COD_UFFICIO_FAS_CUMULATO"      ); } 
  public  BigDecimal  getKeyProvvNsc()                 throws DAOException  { return getBigDecimal ("KEY_PROVV_NSC"                 ); } 
  
  public  Date        getDataRichiestaFascicolo()      throws DAOException  { return getDate       ("DATA_RICHIESTA_FASCICOLO"      ); } 
  public  Date        getDataPervenimentoFascicolo()   throws DAOException  { return getDate       ("DATA_PERVENIMENTO_FASCICOLO"   ); } 
  public  String      getNote()                        throws DAOException  { return getString     ("NOTE"                          ); } 

  public  String      getFlagAccorpato()               throws DAOException  { return getString     ("FLAG_ACCORPATO"                ); } 
  public  String      getChiaveUfficioOrigine()        throws DAOException  { return getString     ("CHIAVE_UFFICIO_ORIGINE"        ); } 
  public  BigDecimal  getChiaveProgrOrigine()          throws DAOException  { return getBigDecimal ("CHIAVE_PROGR_ORIGINE"          ); } 
  
  public  BigDecimal  getTitIdTitoloCumulato()         throws DAOException  { return getBigDecimal ("TIT_ID_TITOLO_CUMULATO"        ); } 
  public  String      getFlagStato()                   throws DAOException  { return getString     ("FLAG_STATO"                    ); } 
  public  String      getMotivoModifica()              throws DAOException  { return getString     ("MOTIVO_MODIFICA"               ); } 
  public  BigDecimal  getIdFascicoloSiepOrigine()      throws DAOException  { return getBigDecimal ("ID_FASCICOLO_SIEP_ORIGINE"     ); } 
  public  BigDecimal  getEveIdEvento()				   throws DAOException  { return getBigDecimal ("EVE_ID_EVENTO"     ); } 

  public  String      getCodOperatoreInserimento()     throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"     ); } 
  public  Date        getDataInserimento()             throws DAOException  { return getDate       ("DATA_INSERIMENTO"              ); } 
  public  String      getCodUfficioInserimento()       throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"       ); } 
  public  String      getCodOperatoreAggiornamento()   throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"   ); } 
  public  Date        getDataAggiornamento()           throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"            ); } 
  public  String      getCodUfficioAggiornamento()     throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"     ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdProcedimentoCumulato      (BigDecimal  aValore )   { setBigDecimal ("ID_PROCEDIMENTO_CUMULATO"      , aValore); } 
  public void  setChiaveAnnoFasCumulato       (BigDecimal  aValore )   { setBigDecimal ("CHIAVE_ANNO_FAS_CUMULATO"      , aValore); } 
  public void  setChiaveProgrFasCumulato      (BigDecimal  aValore )   { setBigDecimal ("CHIAVE_PROGR_FAS_CUMULATO"     , aValore); } 
  public void  setCodTipoUfficioFasCumulato   (String      aValore )   { setString     ("COD_TIPO_UFFICIO_FAS_CUMULATO" , aValore); } 
  public void  setCodLuogoUfficioFasCumulato  (String      aValore )   { setString     ("COD_LUOGO_UFFICIO_FAS_CUMULATO", aValore); } 
  public void  setCodUfficioFasCumulato       (String      aValore )   { setString     ("COD_UFFICIO_FAS_CUMULATO"      , aValore); } 
  public void  setKeyProvvNsc                 (BigDecimal  aValore )   { setBigDecimal ("KEY_PROVV_NSC           "      , aValore); } 

  public void  setDataRichiestaFascicolo      (Date        aValore )   { setDate       ("DATA_RICHIESTA_FASCICOLO"      , aValore); } 
  public void  setDataPervenimentoFascicolo   (Date        aValore )   { setDate       ("DATA_PERVENIMENTO_FASCICOLO"   , aValore); } 
  public void  setNote                        (String      aValore )   { setString     ("NOTE"                          , aValore); } 
  
  public void  setFlagAccorpato               (String      aValore )   { setString     ("FLAG_ACCORPATO"                , aValore); } 
  public void  setChiaveUfficioOrigine        (String      aValore )   { setString     ("CHIAVE_UFFICIO_ORIGINE"        , aValore); } 
  public void  setChiaveProgrOrigine          (BigDecimal  aValore )   { setBigDecimal ("CHIAVE_PROGR_ORIGINE"          , aValore); } 
  
  public void  setTitIdTitoloCumulato         (BigDecimal  aValore )   { setBigDecimal ("TIT_ID_TITOLO_CUMULATO"        , aValore); } 
  public void  setFlagStato                   (String      aValore )   { setString     ("FLAG_STATO"                    , aValore); } 
  public void  setMotivoModifica              (String      aValore )   { setString     ("MOTIVO_MODIFICA"               , aValore); } 
  public void  setIdFascicoloSiepOrigine      (BigDecimal  aValore )   { setBigDecimal ("ID_FASCICOLO_SIEP_ORIGINE"     , aValore); } 
  public void  setEveIdEvento			      (BigDecimal  aValore )   { setBigDecimal ("EVE_ID_EVENTO"    				 , aValore); } 
  
  public void  setCodOperatoreInserimento     (String      aValore )   { setString     ("COD_OPERATORE_INSERIMENTO"     , aValore); } 
  public void  setDataInserimento             (Date        aValore )   { setDate       ("DATA_INSERIMENTO"              , aValore); } 
  public void  setCodUfficioInserimento       (String      aValore )   { setString     ("COD_UFFICIO_INSERIMENTO"       , aValore); } 
  public void  setCodOperatoreAggiornamento   (String      aValore )   { setString     ("COD_OPERATORE_AGGIORNAMENTO"   , aValore); } 
  public void  setDataAggiornamento           (Date        aValore )   { setDate       ("DATA_AGGIORNAMENTO"            , aValore); } 
  public void  setCodUfficioAggiornamento     (String      aValore )   { setString     ("COD_UFFICIO_AGGIORNAMENTO"     , aValore); } 


  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new ProcedimentoCumulatoModel(  
      getIdProcedimentoCumulato() , 
      getChiaveAnnoFasCumulato() , 
      getChiaveProgrFasCumulato() , 
      getCodTipoUfficioFasCumulato() , 
 "",
      getCodLuogoUfficioFasCumulato() , 
 "",
      getCodUfficioFasCumulato() , 
 "",
      getKeyProvvNsc(),

      getDataRichiestaFascicolo() , 
      getDataPervenimentoFascicolo() , 
      getNote() , 
      getIdFascicoloSiepOrigine() , 
      getEveIdEvento(),
      
      getFlagAccorpato() , 
      getChiaveUfficioOrigine() , 
      getChiaveProgrOrigine() ,

      getTitIdTitoloCumulato() , 
      getFlagStato() , 
      getMotivoModifica() , 
      
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
  public void setDAOFromModel(ProcedimentoCumulatoModel aModel) throws DAOException {
    setIdProcedimentoCumulato      ( aModel.getIdProcedimentoCumulato()     );  
    setChiaveAnnoFasCumulato       ( aModel.getChiaveAnnoFasCumulato()      );  
    setChiaveProgrFasCumulato      ( aModel.getChiaveProgrFasCumulato()     );  
    setCodTipoUfficioFasCumulato   ( aModel.getCodTipoUfficioFasCumulato()  );  
    setCodLuogoUfficioFasCumulato  ( aModel.getCodLuogoUfficioFasCumulato() );  
    setCodUfficioFasCumulato       ( aModel.getCodUfficioFasCumulato()      );  
    setKeyProvvNsc                 ( aModel.getKeyProvvNsc()                );
    
    setDataRichiestaFascicolo      ( aModel.getDataRichiestaFascicolo()     );  
    setDataPervenimentoFascicolo   ( aModel.getDataPervenimentoFascicolo()  );  
    setNote                        ( aModel.getNote()                       );  
    
    setFlagAccorpato               ( aModel.getFlagAccorpato()              );  
    setChiaveUfficioOrigine        ( aModel.getChiaveUfficioOrigine()       );  
    setChiaveProgrOrigine          ( aModel.getChiaveProgrOrigine()         );      

    setTitIdTitoloCumulato         ( aModel.getTitIdTitoloCumulato()        );  
    setFlagStato                   ( aModel.getFlagStato()                  );  
    setMotivoModifica              ( aModel.getMotivoModifica()             );  
    setIdFascicoloSiepOrigine      ( aModel.getIdFascicoloSiepOrigine()     );  

    setCodOperatoreInserimento     ( aModel.getCodOperatoreInserimento()    );  
    setDataInserimento             ( aModel.getDataInserimento()            );  
    setCodUfficioInserimento       ( aModel.getCodUfficioInserimento()      );  
    setCodOperatoreAggiornamento   ( aModel.getCodOperatoreAggiornamento()  );  
    setDataAggiornamento           ( aModel.getDataAggiornamento()          );  
    setCodUfficioAggiornamento     ( aModel.getCodUfficioAggiornamento()    );  
  }

  /**
   * 
   * @param aModel
   * @throws DAOException
   */
  public void setDAOFromModelForUpdate (ProcedimentoCumulatoModel aModel) throws DAOException {
    //setIdProcedimentoCumulato      ( aModel.getIdProcedimentoCumulato()     );  
    setChiaveAnnoFasCumulato       ( aModel.getChiaveAnnoFasCumulato()      );  
    setChiaveProgrFasCumulato      ( aModel.getChiaveProgrFasCumulato()     );  
    setCodTipoUfficioFasCumulato   ( aModel.getCodTipoUfficioFasCumulato()  );  
    setCodLuogoUfficioFasCumulato  ( aModel.getCodLuogoUfficioFasCumulato() );  
    setCodUfficioFasCumulato       ( aModel.getCodUfficioFasCumulato()      );  
    //setKeyProvvNsc                 ( aModel.getKeyProvvNsc()                );
    
    setDataRichiestaFascicolo      ( aModel.getDataRichiestaFascicolo()     );  
    setDataPervenimentoFascicolo   ( aModel.getDataPervenimentoFascicolo()  );  
    setNote                        ( aModel.getNote()                       );  
    
    setFlagAccorpato               ( aModel.getFlagAccorpato()              );  
    setChiaveUfficioOrigine        ( aModel.getChiaveUfficioOrigine()       );  
    setChiaveProgrOrigine          ( aModel.getChiaveProgrOrigine()         );      

    //setTitIdTitoloCumulato         ( aModel.getTitIdTitoloCumulato()        );  
    setFlagStato                   ( aModel.getFlagStato()                  );  
    setMotivoModifica              ( aModel.getMotivoModifica()             );  
    //setIdFascicoloSiepOrigine      ( aModel.getIdFascicoloSiepOrigine()     ); 
    setEveIdEvento				   ( aModel.getEveIdEvento() 	);

//    setCodOperatoreInserimento     ( aModel.getCodOperatoreInserimento()    );  
//    setDataInserimento             ( aModel.getDataInserimento()            );  
//    setCodUfficioInserimento       ( aModel.getCodUfficioInserimento()      );  
    setCodOperatoreAggiornamento   ( aModel.getCodOperatoreAggiornamento()  );  
    setDataAggiornamento           ( aModel.getDataAggiornamento()          );  
    setCodUfficioAggiornamento     ( aModel.getCodUfficioAggiornamento()    );  
  }

  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(ProcedimentoCumulatoModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdProcedimentoCumulato() != null ) { 
      lCondizioni += " and ID_PROCEDIMENTO_CUMULATO = " + aModel.getIdProcedimentoCumulato() + ""; 
    } 
    if (aModel.getChiaveAnnoFasCumulato() != null ) { 
      lCondizioni += " and CHIAVE_ANNO_FAS_CUMULATO = " + aModel.getChiaveAnnoFasCumulato() + ""; 
    } 
    if (aModel.getChiaveProgrFasCumulato() != null ) { 
      lCondizioni += " and CHIAVE_PROGR_FAS_CUMULATO = " + aModel.getChiaveProgrFasCumulato() + ""; 
    } 
    if (aModel.getCodTipoUfficioFasCumulato() != null && aModel.getCodTipoUfficioFasCumulato().length() > 0) { 
      lCondizioni += " and COD_TIPO_UFFICIO_FAS_CUMULATO = '" + aModel.getCodTipoUfficioFasCumulato() + "' "; 
    } 
    if (aModel.getCodLuogoUfficioFasCumulato() != null && aModel.getCodLuogoUfficioFasCumulato().length() > 0) { 
      lCondizioni += " and COD_LUOGO_UFFICIO_FAS_CUMULATO = '" + aModel.getCodLuogoUfficioFasCumulato() + "' "; 
    } 
    if (aModel.getCodUfficioFasCumulato() != null && aModel.getCodUfficioFasCumulato().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_FAS_CUMULATO = '" + aModel.getCodUfficioFasCumulato() + "' "; 
    }
    if (aModel.getKeyProvvNsc() != null ) { 
      lCondizioni += " and KEY_PROVV_NSC = " + aModel.getKeyProvvNsc() + ""; 
    }     
    
    
    if (aModel.getDataRichiestaFascicolo() != null ) { 
      lCondizioni += " and to_char(DATA_RICHIESTA_FASCICOLO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataRichiestaFascicolo(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataPervenimentoFascicolo() != null ) { 
      lCondizioni += " and to_char(DATA_PERVENIMENTO_FASCICOLO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataPervenimentoFascicolo(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getNote() != null && aModel.getNote().length() > 0) { 
      lCondizioni += " and NOTE = '" + aModel.getNote() + "' "; 
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
    
    
    if (aModel.getTitIdTitoloCumulato() != null ) { 
      lCondizioni += " and TIT_ID_TITOLO_CUMULATO = " + aModel.getTitIdTitoloCumulato() + ""; 
    } 
    if (aModel.getFlagStato() != null && aModel.getFlagStato().length() > 0) { 
      lCondizioni += " and FLAG_STATO = '" + aModel.getFlagStato() + "' "; 
    } 
    if (aModel.getMotivoModifica() != null && aModel.getMotivoModifica().length() > 0) { 
      lCondizioni += " and MOTIVO_MODIFICA = '" + aModel.getMotivoModifica() + "' "; 
    } 
    if (aModel.getIdFascicoloSiepOrigine() != null ) { 
      lCondizioni += " and ID_FASCICOLO_SIEP_ORIGINE = " + aModel.getIdFascicoloSiepOrigine() + ""; 
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
  public void selCondizioneUpdate( BigDecimal aIdProcedimentoCumulato) {
    String lCondizioni = new String();

    lCondizioni += " and ID_PROCEDIMENTO_CUMULATO = " + aIdProcedimentoCumulato;
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni);
  }
  
  public void selCondizioneUpdateByIdTitolo( BigDecimal aIdTitolo) {
    String lCondizioni = new String();

    lCondizioni += " and TIT_ID_TITOLO_CUMULATO = " + aIdTitolo;
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
