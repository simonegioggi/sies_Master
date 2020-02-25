package siap.siep.modulocumulo.dao;

/**
* <p>Title: PeriodoLibAntCumuloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella PeriodoLibAntCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;
import f3b.util.DateUtils;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

import siap.siep.modulocumulo.model.PeriodoLibAntCumuloModel;

public class PeriodoLibAntCumuloDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public PeriodoLibAntCumuloDAO (Connection con) {
    super(con);
    setTable("PERIODO_LIB_ANT_CUMULO");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_PERIODO_LIB_ANT_CUMULO","PERIODO_LIB_ANT_CUMULO_SEQ");

    //setField("ID_PERIODO_LIB_ANT_CUMULO", BIG_DECIMAL);
    setField("DATA_INIZIO"                 , DATE);
    setField("DATA_FINE"                   , DATE);

    setField("LIB_ID_LIB_ANTICIPATA_CUMULO", BIG_DECIMAL);
    setField("FLAG_STATO"                  , STRING);
    setField("MOTIVO_MODIFICA"             , STRING);
    setField("ID_PERIODO_LIBANT_ORIGINE"   , BIG_DECIMAL);
    
    setField("COD_OPERATORE_INSERIMENTO"   , STRING);
    setField("DATA_INSERIMENTO"            , DATE);
    setField("COD_UFFICIO_INSERIMENTO"     , STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO" , STRING);
    setField("DATA_AGGIORNAMENTO"          , DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO"   , STRING);
  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  BigDecimal  getIdPeriodoLibAntCumulo()      throws DAOException  { return getBigDecimal ("ID_PERIODO_LIB_ANT_CUMULO"   ); } 
  public  Date        getDataInizio()                 throws DAOException  { return getDate       ("DATA_INIZIO"                 ); } 
  public  Date        getDataFine()                   throws DAOException  { return getDate       ("DATA_FINE"                   ); } 
 
  public  BigDecimal  getLibIdLibAnticipataCumulo()   throws DAOException  { return getBigDecimal ("LIB_ID_LIB_ANTICIPATA_CUMULO"); } 
  public  String      getFlagStato()                  throws DAOException  { return getString     ("FLAG_STATO"                  ); } 
  public  String      getMotivoModifica()             throws DAOException  { return getString     ("MOTIVO_MODIFICA"             ); } 
  public  BigDecimal  getIdPeriodoLibantOrigine()     throws DAOException  { return getBigDecimal ("ID_PERIODO_LIBANT_ORIGINE"   ); } 
  
  public  String      getCodOperatoreInserimento()    throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"   ); } 
  public  Date        getDataInserimento()            throws DAOException  { return getDate       ("DATA_INSERIMENTO"            ); } 
  public  String      getCodUfficioInserimento()      throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"     ); } 
  public  String      getCodOperatoreAggiornamento()  throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO" ); } 
  public  Date        getDataAggiornamento()          throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"          ); } 
  public  String      getCodUfficioAggiornamento()    throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"   ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdPeriodoLibAntCumulo      (BigDecimal  aValore )   { setBigDecimal ("ID_PERIODO_LIB_ANT_CUMULO"   , aValore); } 
  public void  setDataInizio                 (Date        aValore )   { setDate       ("DATA_INIZIO"                 , aValore); } 
  public void  setDataFine                   (Date        aValore )   { setDate       ("DATA_FINE"                   , aValore); } 

  public void  setLibIdLibAnticipataCumulo   (BigDecimal  aValore )   { setBigDecimal ("LIB_ID_LIB_ANTICIPATA_CUMULO", aValore); } 
  public void  setFlagStato                  (String      aValore )   { setString     ("FLAG_STATO"                  , aValore); } 
  public void  setMotivoModifica             (String      aValore )   { setString     ("MOTIVO_MODIFICA"             , aValore); } 
  public void  setIdPeriodoLibantOrigine     (BigDecimal  aValore )   { setBigDecimal ("ID_PERIODO_LIBANT_ORIGINE"   , aValore); } 
  
  public void  setCodOperatoreInserimento    (String      aValore )   { setString     ("COD_OPERATORE_INSERIMENTO"   , aValore); } 
  public void  setDataInserimento            (Date        aValore )   { setDate       ("DATA_INSERIMENTO"            , aValore); } 
  public void  setCodUfficioInserimento      (String      aValore )   { setString     ("COD_UFFICIO_INSERIMENTO"     , aValore); } 
  public void  setCodOperatoreAggiornamento  (String      aValore )   { setString     ("COD_OPERATORE_AGGIORNAMENTO" , aValore); } 
  public void  setDataAggiornamento          (Date        aValore )   { setDate       ("DATA_AGGIORNAMENTO"          , aValore); } 
  public void  setCodUfficioAggiornamento    (String      aValore )   { setString     ("COD_UFFICIO_AGGIORNAMENTO"   , aValore); } 


  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new PeriodoLibAntCumuloModel(  
      getIdPeriodoLibAntCumulo() , 
      getDataInizio() , 
      getDataFine() , 
      getLibIdLibAnticipataCumulo() , 
      getFlagStato() , 
      getMotivoModifica() , 
      getIdPeriodoLibantOrigine() , 
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
  public void setDAOFromModel(PeriodoLibAntCumuloModel aModel) throws DAOException {
    setIdPeriodoLibAntCumulo      ( aModel.getIdPeriodoLibAntCumulo()     );  
    setDataInizio                 ( aModel.getDataInizio()                );  
    setDataFine                   ( aModel.getDataFine()                  );  

    setLibIdLibAnticipataCumulo   ( aModel.getLibIdLibAnticipataCumulo()  );  
    setFlagStato                  ( aModel.getFlagStato()                 );  
    setMotivoModifica             ( aModel.getMotivoModifica()            );  
    setIdPeriodoLibantOrigine     ( aModel.getIdPeriodoLibantOrigine()    );  
    
    setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
    setDataInserimento            ( aModel.getDataInserimento()           );  
    setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(PeriodoLibAntCumuloModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdPeriodoLibAntCumulo() != null ) { 
      lCondizioni += " and ID_PERIODO_LIB_ANT_CUMULO = " + aModel.getIdPeriodoLibAntCumulo() + ""; 
    } 
    if (aModel.getDataInizio() != null ) { 
      lCondizioni += " and to_char(DATA_INIZIO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInizio(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataFine() != null ) { 
      lCondizioni += " and to_char(DATA_FINE,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataFine(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getLibIdLibAnticipataCumulo() != null ) { 
      lCondizioni += " and LIB_ID_LIB_ANTICIPATA_CUMULO = " + aModel.getLibIdLibAnticipataCumulo() + ""; 
    } 
    if (aModel.getFlagStato() != null && aModel.getFlagStato().length() > 0) { 
      lCondizioni += " and FLAG_STATO = '" + aModel.getFlagStato() + "' "; 
    } 
    if (aModel.getMotivoModifica() != null && aModel.getMotivoModifica().length() > 0) { 
      lCondizioni += " and MOTIVO_MODIFICA = '" + aModel.getMotivoModifica() + "' "; 
    } 
    if (aModel.getIdPeriodoLibantOrigine() != null ) { 
      lCondizioni += " and ID_PERIODO_LIBANT_ORIGINE = " + aModel.getIdPeriodoLibantOrigine() + ""; 
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
  public void selCondizioneUpdate( BigDecimal aIdPeriodoLibAntCumulo) {
    String lCondizioni = new String();

    lCondizioni += " and ID_PERIODO_LIB_ANT_CUMULO = " + aIdPeriodoLibAntCumulo;
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni);
  }

  /***************************************************************************** 
   * Imposta la condizione di where per l'operazione di update/Delete con for.Key
   * 	'LIB_ID_LIB_ANTICIPATA_CUMULO' 
   * @param key 
   ****************************************************************************/ 
  public void selCondizioneLib_Id_LibAnt( BigDecimal aILibAnticipataCumulo) {
    String lCondizioni = new String();

    lCondizioni += " and LIB_ID_LIB_ANTICIPATA_CUMULO = " + aILibAnticipataCumulo;
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
