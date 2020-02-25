package siap.siep.istruttoriacumulo.dao;

/**
* <p>Title: EsitoArchiviazioniCumuloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella EsitoArchiviazioniCumulo</p>
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

import siap.siep.istruttoriacumulo.model.EsitoArchiviazioniCumuloModel;

public class EsitoArchiviazioniCumuloDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public EsitoArchiviazioniCumuloDAO (Connection con) {
    super(con);
    setTable("ESITO_ARCHIVIAZIONI_CUMULO");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_ESITO_ARCHIVIAZIONI_CUMULO","ES_ARC_CUM_SEQ");

    //setField("ID_ESITO_ARCHIVIAZIONI_CUMULO", BIG_DECIMAL);
    setField("FLAG_ARCHIVIATO"              , STRING);
    setField("DESCRIZIONE_ESITO"            , STRING);
    setField("CHIAVE_UFFICIO"               , STRING);
    setField("CHIAVE_ANNO_FASC_SIEP"        , BIG_DECIMAL);
    setField("CHIAVE_PROGR_FASC_SIEP"       , BIG_DECIMAL);
    setField("FAS_ID_FASCICOLO_SIEP"        , BIG_DECIMAL);
    setField("ISTR_ID_ISTRUTTORIA_CUMULO"   , BIG_DECIMAL);
    setField("EVE_ID_EVENTO"   				, BIG_DECIMAL);
    setField("COD_OPERATORE_INSERIMENTO"    , STRING);
    setField("DATA_INSERIMENTO"             , DATE);
    setField("COD_UFFICIO_INSERIMENTO"      , STRING);
  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  BigDecimal  getIdEsitoArchiviazioniCumulo()  throws DAOException  { return getBigDecimal ("ID_ESITO_ARCHIVIAZIONI_CUMULO"); } 
  public  String      getFlagArchiviato()              throws DAOException  { return getString     ("FLAG_ARCHIVIATO"              ); } 
  public  String      getDescrizioneEsito()            throws DAOException  { return getString     ("DESCRIZIONE_ESITO"            ); } 
  public  String      getChiaveUfficio()               throws DAOException  { return getString     ("CHIAVE_UFFICIO"               ); } 
  public  BigDecimal  getChiaveAnnoFascSiep()          throws DAOException  { return getBigDecimal ("CHIAVE_ANNO_FASC_SIEP"        ); } 
  public  BigDecimal  getChiaveProgrFascSiep()         throws DAOException  { return getBigDecimal ("CHIAVE_PROGR_FASC_SIEP"       ); } 
  public  BigDecimal  getFasIdFascicoloSiep()          throws DAOException  { return getBigDecimal ("FAS_ID_FASCICOLO_SIEP"        ); } 
  public  BigDecimal  getIstrIdIstruttoriaCumulo()     throws DAOException  { return getBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO"   ); } 
  public  BigDecimal  getEveIdEvento()		           throws DAOException  { return getBigDecimal ("EVE_ID_EVENTO"       			); }   
  public  String      getCodOperatoreInserimento()     throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"    ); } 
  public  Date        getDataInserimento()             throws DAOException  { return getDate       ("DATA_INSERIMENTO"             ); } 
  public  String      getCodUfficioInserimento()       throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"      ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdEsitoArchiviazioniCumulo  (BigDecimal  aValore )   { setBigDecimal ("ID_ESITO_ARCHIVIAZIONI_CUMULO", aValore); } 
  public void  setFlagArchiviato              (String      aValore )   { setString     ("FLAG_ARCHIVIATO"              , aValore); } 
  public void  setDescrizioneEsito            (String      aValore )   { setString     ("DESCRIZIONE_ESITO"            , aValore); } 
  public void  setChiaveUfficio               (String      aValore )   { setString     ("CHIAVE_UFFICIO"               , aValore); } 
  public void  setChiaveAnnoFascSiep          (BigDecimal  aValore )   { setBigDecimal ("CHIAVE_ANNO_FASC_SIEP"        , aValore); } 
  public void  setChiaveProgrFascSiep         (BigDecimal  aValore )   { setBigDecimal ("CHIAVE_PROGR_FASC_SIEP"       , aValore); } 
  public void  setFasIdFascicoloSiep          (BigDecimal  aValore )   { setBigDecimal ("FAS_ID_FASCICOLO_SIEP"        , aValore); } 
  public void  setIstrIdIstruttoriaCumulo     (BigDecimal  aValore )   { setBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO"   , aValore); } 
  public void  setEveIdEvento          		  (BigDecimal  aValore )   { setBigDecimal ("EVE_ID_EVENTO"       		   , aValore); }   
  public void  setCodOperatoreInserimento     (String      aValore )   { setString     ("COD_OPERATORE_INSERIMENTO"    , aValore); } 
  public void  setDataInserimento             (Date        aValore )   { setDate       ("DATA_INSERIMENTO"             , aValore); } 
  public void  setCodUfficioInserimento       (String      aValore )   { setString     ("COD_UFFICIO_INSERIMENTO"      , aValore); } 


  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new EsitoArchiviazioniCumuloModel(  
      getIdEsitoArchiviazioniCumulo() , 
      getFlagArchiviato() , 
      getDescrizioneEsito() , 
      getChiaveUfficio() , 
      getChiaveAnnoFascSiep() , 
      getChiaveProgrFascSiep() , 
      getFasIdFascicoloSiep() , 
      getIstrIdIstruttoriaCumulo() , 
      getEveIdEvento(),
      getCodOperatoreInserimento() , 
      getDataInserimento() , 
      getCodUfficioInserimento()
    );
  }


  /***************************************************************************** 
   * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void setDAOFromModel(EsitoArchiviazioniCumuloModel aModel) throws DAOException {
    setIdEsitoArchiviazioniCumulo  ( aModel.getIdEsitoArchiviazioniCumulo() );  
    setFlagArchiviato              ( aModel.getFlagArchiviato()             );  
    setDescrizioneEsito            ( aModel.getDescrizioneEsito()           );  
    setChiaveUfficio               ( aModel.getChiaveUfficio()              );  
    setChiaveAnnoFascSiep          ( aModel.getChiaveAnnoFascSiep()         );  
    setChiaveProgrFascSiep         ( aModel.getChiaveProgrFascSiep()        );  
    setFasIdFascicoloSiep          ( aModel.getFasIdFascicoloSiep()         );  
    setIstrIdIstruttoriaCumulo     ( aModel.getIstrIdIstruttoriaCumulo()    );  
    setEveIdEvento				   ( aModel.getEveIdEvento()				);
    setCodOperatoreInserimento     ( aModel.getCodOperatoreInserimento()    );  
    setDataInserimento             ( aModel.getDataInserimento()            );  
    setCodUfficioInserimento       ( aModel.getCodUfficioInserimento()      );  
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(EsitoArchiviazioniCumuloModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdEsitoArchiviazioniCumulo() != null ) { 
      lCondizioni += " and ID_ESITO_ARCHIVIAZIONI_CUMULO = " + aModel.getIdEsitoArchiviazioniCumulo() + ""; 
    } 
    if (aModel.getFlagArchiviato() != null && aModel.getFlagArchiviato().length() > 0) { 
      lCondizioni += " and FLAG_ARCHIVIATO = '" + aModel.getFlagArchiviato() + "' "; 
    } 
    if (aModel.getDescrizioneEsito() != null && aModel.getDescrizioneEsito().length() > 0) { 
      lCondizioni += " and DESCRIZIONE_ESITO = '" + aModel.getDescrizioneEsito() + "' "; 
    } 
    if (aModel.getChiaveUfficio() != null && aModel.getChiaveUfficio().length() > 0) { 
      lCondizioni += " and CHIAVE_UFFICIO = '" + aModel.getChiaveUfficio() + "' "; 
    } 
    if (aModel.getChiaveAnnoFascSiep() != null ) { 
      lCondizioni += " and CHIAVE_ANNO_FASC_SIEP = " + aModel.getChiaveAnnoFascSiep() + ""; 
    } 
    if (aModel.getChiaveProgrFascSiep() != null ) { 
      lCondizioni += " and CHIAVE_PROGR_FASC_SIEP = " + aModel.getChiaveProgrFascSiep() + ""; 
    } 
    if (aModel.getFasIdFascicoloSiep() != null ) { 
      lCondizioni += " and FAS_ID_FASCICOLO_SIEP = " + aModel.getFasIdFascicoloSiep() + ""; 
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
  public void selCondizioneUpdate( BigDecimal aIdEsitoArchiviazioniCumulo) {
    String lCondizioni = new String();

    lCondizioni += " and ID_ESITO_ARCHIVIAZIONI_CUMULO = " + aIdEsitoArchiviazioniCumulo;
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
