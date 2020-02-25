package siap.siep.sanzionesostitutiva.dao;

/**
* <p>Title: SanzioneSostResiduaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella SanzioneSostResidua</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class SanzioneSostResiduaDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public SanzioneSostResiduaDAO (Connection con) {
    super(con);
    setTable("SANZIONE_SOST_RESIDUA");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_SANZIONE_SOST_RESIDUA","SAN_SOS_RES_SEQ");

    //setField("ID_SANZIONE_SOST_RESIDUA", BIG_DECIMAL);
    setField("FAS_SIE_ID_FASCICOLO_SIEP"  , BIG_DECIMAL);
    setField("EVE_ID_EVENTO"              , BIG_DECIMAL);
    setField("PEN_RES_ID_PENA_RESIDUA"    , BIG_DECIMAL);
    setField("COD_TIPO_SANZIONE"          , STRING);
    setField("DATA_INIZIO"                , DATE);
    setField("DATA_FINE_PRESUNTA"         , DATE);
    setField("DATA_FINE"                  , DATE);
    setField("NUM_ANNI"                   , BIG_DECIMAL);
    setField("NUM_MESI"                   , BIG_DECIMAL);
    setField("NUM_GIORNI"                 , BIG_DECIMAL);
    setField("SANZIONE_PECUNIARIA_MULTA"  , BIG_DECIMAL);
    setField("SANZIONE_PECUNIARIA_AMMENDA", BIG_DECIMAL);
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
  public  BigDecimal  getIdSanzioneSostResidua()      throws DAOException  { return getBigDecimal ("ID_SANZIONE_SOST_RESIDUA"   ); } 
  public  BigDecimal  getFasSieIdFascicoloSiep()      throws DAOException  { return getBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"  ); } 
  public  BigDecimal  getEveIdEvento()                throws DAOException  { return getBigDecimal ("EVE_ID_EVENTO"              ); } 
  public  BigDecimal  getPenResIdPenaResidua()        throws DAOException  { return getBigDecimal ("PEN_RES_ID_PENA_RESIDUA"    ); } 
  public  String      getCodTipoSanzione()            throws DAOException  { return getString     ("COD_TIPO_SANZIONE"          ); } 
  public  Date        getDataInizio()                 throws DAOException  { return getDate       ("DATA_INIZIO"                ); } 
  public  Date        getDataFinePresunta()           throws DAOException  { return getDate       ("DATA_FINE_PRESUNTA"         ); } 
  public  Date        getDataFine()                   throws DAOException  { return getDate       ("DATA_FINE"                  ); } 
  public  BigDecimal  getNumAnni()                    throws DAOException  { return getBigDecimal ("NUM_ANNI"                   ); } 
  public  BigDecimal  getNumMesi()                    throws DAOException  { return getBigDecimal ("NUM_MESI"                   ); } 
  public  BigDecimal  getNumGiorni()                  throws DAOException  { return getBigDecimal ("NUM_GIORNI"                 ); } 
  public  BigDecimal  getSanzionePecuniariaMulta()    throws DAOException  { return getBigDecimal ("SANZIONE_PECUNIARIA_MULTA"  ); } 
  public  BigDecimal  getSanzionePecuniariaAmmenda()  throws DAOException  { return getBigDecimal ("SANZIONE_PECUNIARIA_AMMENDA"); } 
  public  String      getCodOperatoreInserimento()    throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"  ); } 
  public  Date        getDataInserimento()            throws DAOException  { return getDate       ("DATA_INSERIMENTO"           ); } 
  public  String      getCodUfficioInserimento()      throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"    ); } 
  public  String      getCodOperatoreAggiornamento()  throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"); } 
  public  Date        getDataAggiornamento()          throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"         ); } 
  public  String      getCodUfficioAggiornamento()    throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"  ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdSanzioneSostResidua      (BigDecimal  aValore )   { setBigDecimal ("ID_SANZIONE_SOST_RESIDUA"   , aValore); } 
  public void  setFasSieIdFascicoloSiep      (BigDecimal  aValore )   { setBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"  , aValore); } 
  public void  setEveIdEvento                (BigDecimal  aValore )   { setBigDecimal ("EVE_ID_EVENTO"              , aValore); } 
  public void  setPenResIdPenaResidua        (BigDecimal  aValore )   { setBigDecimal ("PEN_RES_ID_PENA_RESIDUA"    , aValore); } 
  public void  setCodTipoSanzione            (String      aValore )   { setString     ("COD_TIPO_SANZIONE"          , aValore); } 
  public void  setDataInizio                 (Date        aValore )   { setDate       ("DATA_INIZIO"                , aValore); } 
  public void  setDataFinePresunta           (Date        aValore )   { setDate       ("DATA_FINE_PRESUNTA"         , aValore); } 
  public void  setDataFine                   (Date        aValore )   { setDate       ("DATA_FINE"                  , aValore); } 
  public void  setNumAnni                    (BigDecimal  aValore )   { setBigDecimal ("NUM_ANNI"                   , aValore); } 
  public void  setNumMesi                    (BigDecimal  aValore )   { setBigDecimal ("NUM_MESI"                   , aValore); } 
  public void  setNumGiorni                  (BigDecimal  aValore )   { setBigDecimal ("NUM_GIORNI"                 , aValore); } 
  public void  setSanzionePecuniariaMulta    (BigDecimal  aValore )   { setBigDecimal ("SANZIONE_PECUNIARIA_MULTA"  , aValore); } 
  public void  setSanzionePecuniariaAmmenda  (BigDecimal  aValore )   { setBigDecimal ("SANZIONE_PECUNIARIA_AMMENDA", aValore); } 
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
    return new SanzioneSostResiduaModel(  
      getIdSanzioneSostResidua() , 
      getFasSieIdFascicoloSiep() , 
      getEveIdEvento() , 
      getPenResIdPenaResidua() , 
      getCodTipoSanzione() , 
 "",
      getDataInizio() , 
      getDataFinePresunta() , 
      getDataFine() , 
      getNumAnni() , 
      getNumMesi() , 
      getNumGiorni() , 
      getSanzionePecuniariaMulta() , 
      getSanzionePecuniariaAmmenda() , 
 "",      
      getCodOperatoreInserimento() , 
      getDataInserimento() , 
      getCodUfficioInserimento() , 
 "",
      getCodOperatoreAggiornamento() , 
      getDataAggiornamento() , 
      getCodUfficioAggiornamento(),
 ""
    );
  }


  /***************************************************************************** 
   * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void setDAOFromModel(SanzioneSostResiduaModel aModel) throws DAOException {
    setIdSanzioneSostResidua      ( aModel.getIdSanzioneSostResidua()     );  
    setFasSieIdFascicoloSiep      ( aModel.getFasSieIdFascicoloSiep()     );  
    setEveIdEvento                ( aModel.getEveIdEvento()               );  
    setPenResIdPenaResidua        ( aModel.getPenResIdPenaResidua()       );  
    setCodTipoSanzione            ( aModel.getCodTipoSanzione()           );  
    setDataInizio                 ( aModel.getDataInizio()                );  
    setDataFinePresunta           ( aModel.getDataFinePresunta()          );  
    setDataFine                   ( aModel.getDataFine()                  );  
    setNumAnni                    ( aModel.getNumAnni()                   );  
    setNumMesi                    ( aModel.getNumMesi()                   );  
    setNumGiorni                  ( aModel.getNumGiorni()                 );  
    setSanzionePecuniariaMulta    ( aModel.getSanzionePecuniariaMulta()   );  
    setSanzionePecuniariaAmmenda  ( aModel.getSanzionePecuniariaAmmenda() );  
    setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
    setDataInserimento            ( aModel.getDataInserimento()           );  
    setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
  }

  public void setDAOFromModelForUpdate(SanzioneSostResiduaModel aModel) throws DAOException
  {
    //setIdSanzioneSostitutiva( aModel.getIdSanzioneSostitutiva() );
    setCodTipoSanzione( aModel.getCodTipoSanzione() );
    setNumAnni   ( aModel.getNumAnni() );
    setNumMesi   ( aModel.getNumMesi() );
    setNumGiorni ( aModel.getNumGiorni() );
    setSanzionePecuniariaMulta   ( aModel.getSanzionePecuniariaMulta() );
    setSanzionePecuniariaAmmenda ( aModel.getSanzionePecuniariaAmmenda());
    
    
    setCodOperatoreAggiornamento ( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento         ( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento   ( aModel.getCodUfficioAggiornamento() );

    setCondizioneUpdate(aModel.getIdSanzioneSostResidua());
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(SanzioneSostResiduaModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdSanzioneSostResidua() != null ) { 
      lCondizioni += " and ID_SANZIONE_SOST_RESIDUA = " + aModel.getIdSanzioneSostResidua() + ""; 
    } 
    if (aModel.getFasSieIdFascicoloSiep() != null ) { 
      lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + ""; 
    } 
    if (aModel.getEveIdEvento() != null ) { 
      lCondizioni += " and EVE_ID_EVENTO = " + aModel.getEveIdEvento() + ""; 
    } 
    if (aModel.getPenResIdPenaResidua() != null ) { 
      lCondizioni += " and PEN_RES_ID_PENA_RESIDUA = " + aModel.getPenResIdPenaResidua() + ""; 
    } 
    if (aModel.getCodTipoSanzione() != null && aModel.getCodTipoSanzione().length() > 0) { 
      lCondizioni += " and COD_TIPO_SANZIONE = '" + aModel.getCodTipoSanzione() + "' "; 
    } 
    if (aModel.getDataInizio() != null ) { 
      lCondizioni += " and to_char(DATA_INIZIO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInizio(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataFinePresunta() != null ) { 
      lCondizioni += " and to_char(DATA_FINE_PRESUNTA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataFinePresunta(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataFine() != null ) { 
      lCondizioni += " and to_char(DATA_FINE,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataFine(),"dd/MM/yyyy") + "' "; 
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
    if (aModel.getSanzionePecuniariaMulta() != null ) { 
      lCondizioni += " and SANZIONE_PECUNIARIA_MULTA = " + aModel.getSanzionePecuniariaMulta() + ""; 
    } 
    if (aModel.getSanzionePecuniariaAmmenda() != null ) { 
      lCondizioni += " and SANZIONE_PECUNIARIA_AMMENDA = " + aModel.getSanzionePecuniariaAmmenda() + ""; 
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
  public void setCondizioneUpdate( BigDecimal aIdSanzioneSostResidua) {
    String lCondizioni = new String();

    lCondizioni += " ID_SANZIONE_SOST_RESIDUA = " + aIdSanzioneSostResidua;

    setCondition(lCondizioni);
  }

  public void setCondizioneUpdateByIdPenRes( BigDecimal aIdPenaResidua) {
    String lCondizioni = new String();

    lCondizioni += " PEN_RES_ID_PENA_RESIDUA = " + aIdPenaResidua;

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
