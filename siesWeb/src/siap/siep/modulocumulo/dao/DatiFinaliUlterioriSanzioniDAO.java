package siap.siep.modulocumulo.dao;

/**
* <p>Title: DatiFinaliUlterioriSanzioniDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella DatiFinaliUlterioriSanzioni</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.modulocumulo.model.DatiFinaliUlterioriSanzioniModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class DatiFinaliUlterioriSanzioniDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public DatiFinaliUlterioriSanzioniDAO (Connection con) {
    super(con);
    setTable("DATI_FINALI_ULTERIORI_SANZIONI");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_DATI_FINALI_ULTERIORI_SANZ","DAT_FIN_ULT_SAN_SEQ");

    //setField("ID_DATI_FINALI_ULTERIORI_SANZ", BIG_DECIMAL);
    setField("COD_TIPO_ULTERIORE_SANZIONE"  , STRING);
    setField("NUM_ANNI"                     , BIG_DECIMAL);
    setField("NUM_MESI"                     , BIG_DECIMAL);
    setField("NUM_GIORNI"                   , BIG_DECIMAL);
    setField("MULTA"                        , BIG_DECIMAL);
    setField("AMMENDA"                      , BIG_DECIMAL);
    setField("FLAG_ESPUL_PERP"              , STRING);
    setField("COD_TIPO_LPU"                 , STRING);
    setField("NUM_ORE_TOT"                  , BIG_DECIMAL);
    setField("NUM_ORE_SETT"                 , BIG_DECIMAL);
    setField("COD_FREQ_SETT"                , BIG_DECIMAL);
    setField("DAT_ID_DATI_FINALI_CUMULO"    , BIG_DECIMAL);
    setField("ISTR_ID_ISTRUTTORIA_CUMULO"   , BIG_DECIMAL);
    setField("COD_OPERATORE_INSERIMENTO"    , STRING);
    setField("DATA_INSERIMENTO"             , DATE);
    setField("COD_UFFICIO_INSERIMENTO"      , STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO"  , STRING);
    setField("DATA_AGGIORNAMENTO"           , DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO"    , STRING);
  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  BigDecimal  getIdDatiFinaliUlterioriSanz()  throws DAOException  { return getBigDecimal ("ID_DATI_FINALI_ULTERIORI_SANZ"); } 
  public  String      getCodTipoUlterioreSanzione()   throws DAOException  { return getString     ("COD_TIPO_ULTERIORE_SANZIONE"  ); } 
  public  BigDecimal  getNumAnni()                    throws DAOException  { return getBigDecimal ("NUM_ANNI"                     ); } 
  public  BigDecimal  getNumMesi()                    throws DAOException  { return getBigDecimal ("NUM_MESI"                     ); } 
  public  BigDecimal  getNumGiorni()                  throws DAOException  { return getBigDecimal ("NUM_GIORNI"                   ); } 
  public  BigDecimal  getMulta()                      throws DAOException  { return getBigDecimal ("MULTA"                        ); } 
  public  BigDecimal  getAmmenda()                    throws DAOException  { return getBigDecimal ("AMMENDA"                      ); } 
  public  String      getFlagEspulPerp()              throws DAOException  { return getString     ("FLAG_ESPUL_PERP"              ); } 
  public  String      getCodTipoLpu()                 throws DAOException  { return getString     ("COD_TIPO_LPU"                 ); } 
  public  BigDecimal  getNumOreTot()                  throws DAOException  { return getBigDecimal ("NUM_ORE_TOT"                  ); } 
  public  BigDecimal  getNumOreSett()                 throws DAOException  { return getBigDecimal ("NUM_ORE_SETT"                 ); } 
  public  BigDecimal  getCodFreqSett()                throws DAOException  { return getBigDecimal ("COD_FREQ_SETT"                ); } 
  public  BigDecimal  getDatIdDatiFinaliCumulo()      throws DAOException  { return getBigDecimal ("DAT_ID_DATI_FINALI_CUMULO"    ); } 
  public  BigDecimal  getIstrIdIstruttoriaCumulo()    throws DAOException  { return getBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO"   ); } 
  public  String      getCodOperatoreInserimento()    throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"    ); } 
  public  Date        getDataInserimento()            throws DAOException  { return getDate       ("DATA_INSERIMENTO"             ); } 
  public  String      getCodUfficioInserimento()      throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"      ); } 
  public  String      getCodOperatoreAggiornamento()  throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"  ); } 
  public  Date        getDataAggiornamento()          throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"           ); } 
  public  String      getCodUfficioAggiornamento()    throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"    ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdDatiFinaliUlterioriSanz  (BigDecimal  aValore )   { setBigDecimal ("ID_DATI_FINALI_ULTERIORI_SANZ", aValore); } 
  public void  setCodTipoUlterioreSanzione   (String      aValore )   { setString     ("COD_TIPO_ULTERIORE_SANZIONE"  , aValore); } 
  public void  setNumAnni                    (BigDecimal  aValore )   { setBigDecimal ("NUM_ANNI"                     , aValore); } 
  public void  setNumMesi                    (BigDecimal  aValore )   { setBigDecimal ("NUM_MESI"                     , aValore); } 
  public void  setNumGiorni                  (BigDecimal  aValore )   { setBigDecimal ("NUM_GIORNI"                   , aValore); } 
  public void  setMulta                      (BigDecimal  aValore )   { setBigDecimal ("MULTA"                        , aValore); } 
  public void  setAmmenda                    (BigDecimal  aValore )   { setBigDecimal ("AMMENDA"                      , aValore); } 
  public void  setFlagEspulPerp              (String      aValore )   { setString     ("FLAG_ESPUL_PERP"              , aValore); } 
  public void  setCodTipoLpu                 (String      aValore )   { setString     ("COD_TIPO_LPU"                 , aValore); } 
  public void  setNumOreTot                  (BigDecimal  aValore )   { setBigDecimal ("NUM_ORE_TOT"                  , aValore); } 
  public void  setNumOreSett                 (BigDecimal  aValore )   { setBigDecimal ("NUM_ORE_SETT"                 , aValore); } 
  public void  setCodFreqSett                (BigDecimal  aValore )   { setBigDecimal ("COD_FREQ_SETT"                , aValore); } 
  public void  setDatIdDatiFinaliCumulo      (BigDecimal  aValore )   { setBigDecimal ("DAT_ID_DATI_FINALI_CUMULO"    , aValore); } 
  public void  setIstrIdIstruttoriaCumulo    (BigDecimal  aValore )   { setBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO"   , aValore); } 
  public void  setCodOperatoreInserimento    (String      aValore )   { setString     ("COD_OPERATORE_INSERIMENTO"    , aValore); } 
  public void  setDataInserimento            (Date        aValore )   { setDate       ("DATA_INSERIMENTO"             , aValore); } 
  public void  setCodUfficioInserimento      (String      aValore )   { setString     ("COD_UFFICIO_INSERIMENTO"      , aValore); } 
  public void  setCodOperatoreAggiornamento  (String      aValore )   { setString     ("COD_OPERATORE_AGGIORNAMENTO"  , aValore); } 
  public void  setDataAggiornamento          (Date        aValore )   { setDate       ("DATA_AGGIORNAMENTO"           , aValore); } 
  public void  setCodUfficioAggiornamento    (String      aValore )   { setString     ("COD_UFFICIO_AGGIORNAMENTO"    , aValore); } 


  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new DatiFinaliUlterioriSanzioniModel(  
      getIdDatiFinaliUlterioriSanz() , 
      getCodTipoUlterioreSanzione() , 
 "","",
      getNumAnni() , 
      getNumMesi() , 
      getNumGiorni() , 
      getMulta() , 
      getAmmenda() , 
      getFlagEspulPerp() , 
      getCodTipoLpu() , 
 "",
      getNumOreTot() , 
      getNumOreSett() , 
      getCodFreqSett() , 
      getDatIdDatiFinaliCumulo() , 
      getIstrIdIstruttoriaCumulo() , 
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
  public void setDAOFromModel(DatiFinaliUlterioriSanzioniModel aModel) throws DAOException {
    setIdDatiFinaliUlterioriSanz  ( aModel.getIdDatiFinaliUlterioriSanz() );  
    setCodTipoUlterioreSanzione   ( aModel.getCodTipoUlterioreSanzione()  );  
    setNumAnni                    ( aModel.getNumAnni()                   );  
    setNumMesi                    ( aModel.getNumMesi()                   );  
    setNumGiorni                  ( aModel.getNumGiorni()                 );  
    setMulta                      ( aModel.getMulta()                     );  
    setAmmenda                    ( aModel.getAmmenda()                   );  
    setFlagEspulPerp              ( aModel.getFlagEspulPerp()             );  
    setCodTipoLpu                 ( aModel.getCodTipoLpu()                );  
    setNumOreTot                  ( aModel.getNumOreTot()                 );  
    setNumOreSett                 ( aModel.getNumOreSett()                );  
    setCodFreqSett                ( aModel.getCodFreqSett()               );  
    setDatIdDatiFinaliCumulo      ( aModel.getDatIdDatiFinaliCumulo()     );  
    setIstrIdIstruttoriaCumulo    ( aModel.getIstrIdIstruttoriaCumulo()   );  
    setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
    setDataInserimento            ( aModel.getDataInserimento()           );  
    setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
  }

  public void setDAOFromModelForUpdate (DatiFinaliUlterioriSanzioniModel aModel) throws DAOException {
    //setIdDatiFinaliUlterioriSanz  ( aModel.getIdDatiFinaliUlterioriSanz() );  
    setCodTipoUlterioreSanzione   ( aModel.getCodTipoUlterioreSanzione()  );  
    setNumAnni                    ( aModel.getNumAnni()                   );  
    setNumMesi                    ( aModel.getNumMesi()                   );  
    setNumGiorni                  ( aModel.getNumGiorni()                 );  
    setMulta                      ( aModel.getMulta()                     );  
    setAmmenda                    ( aModel.getAmmenda()                   );  
    setFlagEspulPerp              ( aModel.getFlagEspulPerp()             );  
    setCodTipoLpu                 ( aModel.getCodTipoLpu()                );  
    setNumOreTot                  ( aModel.getNumOreTot()                 );  
    setNumOreSett                 ( aModel.getNumOreSett()                );  
    setCodFreqSett                ( aModel.getCodFreqSett()               );  
//    setDatIdDatiFinaliCumulo      ( aModel.getDatIdDatiFinaliCumulo()     );  
//    setIstrIdIstruttoriaCumulo    ( aModel.getIstrIdIstruttoriaCumulo()   );  
//    setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
//    setDataInserimento            ( aModel.getDataInserimento()           );  
//    setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
  }
  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(DatiFinaliUlterioriSanzioniModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdDatiFinaliUlterioriSanz() != null ) { 
      lCondizioni += " and ID_DATI_FINALI_ULTERIORI_SANZ = " + aModel.getIdDatiFinaliUlterioriSanz() + ""; 
    } 
    if (aModel.getCodTipoUlterioreSanzione() != null && aModel.getCodTipoUlterioreSanzione().length() > 0) { 
      lCondizioni += " and COD_TIPO_ULTERIORE_SANZIONE = '" + aModel.getCodTipoUlterioreSanzione() + "' "; 
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
    if (aModel.getMulta() != null ) { 
      lCondizioni += " and MULTA = " + aModel.getMulta() + ""; 
    } 
    if (aModel.getAmmenda() != null ) { 
      lCondizioni += " and AMMENDA = " + aModel.getAmmenda() + ""; 
    } 
    if (aModel.getFlagEspulPerp() != null && aModel.getFlagEspulPerp().length() > 0) { 
      lCondizioni += " and FLAG_ESPUL_PERP = '" + aModel.getFlagEspulPerp() + "' "; 
    } 
    if (aModel.getCodTipoLpu() != null && aModel.getCodTipoLpu().length() > 0) { 
      lCondizioni += " and COD_TIPO_LPU = '" + aModel.getCodTipoLpu() + "' "; 
    } 
    if (aModel.getNumOreTot() != null ) { 
      lCondizioni += " and NUM_ORE_TOT = " + aModel.getNumOreTot() + ""; 
    } 
    if (aModel.getNumOreSett() != null ) { 
      lCondizioni += " and NUM_ORE_SETT = " + aModel.getNumOreSett() + ""; 
    } 
    if (aModel.getCodFreqSett() != null ) { 
      lCondizioni += " and COD_FREQ_SETT = " + aModel.getCodFreqSett() + ""; 
    } 
    if (aModel.getDatIdDatiFinaliCumulo() != null ) { 
      lCondizioni += " and DAT_ID_DATI_FINALI_CUMULO = " + aModel.getDatIdDatiFinaliCumulo() + ""; 
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
  public void selCondizioneUpdate( BigDecimal aIdDatiFinaliUlterioriSanz) {
    String lCondizioni = new String();

    lCondizioni += " and ID_DATI_FINALI_ULTERIORI_SANZ = " + aIdDatiFinaliUlterioriSanz;
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
