package siap.siep.modulocumulo.dao;

/**
* <p>Title: PenaRideterminataCumuloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella PenaRideterminataCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.modulocumulo.model.PenaRideterminataCumuloModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class PenaRideterminataCumuloDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public PenaRideterminataCumuloDAO (Connection con) {
    super(con);
    setTable("PENA_RIDETERMINATA_CUMULO");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_PENA_RIDETERMINATA_CUMULO","PEN_RID_CUM_SEQ");

    //setField("ID_PENA_RIDETERMINATA_CUMULO", BIG_DECIMAL);
    setField("COD_TIPO_PENA_DETENTIVA"     , STRING);
    setField("NUM_ANNI_RECLUSIONE"         , BIG_DECIMAL);
    setField("NUM_MESI_RECLUSIONE"         , BIG_DECIMAL);
    setField("NUM_GIORNI_RECLUSIONE"       , BIG_DECIMAL);
    setField("IMPORTO_MULTA"               , BIG_DECIMAL);
    setField("NUM_ANNI_ARRESTO"            , BIG_DECIMAL);
    setField("NUM_MESI_ARRESTO"            , BIG_DECIMAL);
    setField("NUM_GIORNI_ARRESTO"          , BIG_DECIMAL);
    setField("IMPORTO_AMMENDA"             , BIG_DECIMAL);
    setField("NUM_ANNI_ISOLAMENTO_DIURNO"  , BIG_DECIMAL);
    setField("NUM_MESI_ISOLAMENTO_DIURNO"  , BIG_DECIMAL);
    setField("NUM_GIORNI_ISOLAMENTO_DIURNO", BIG_DECIMAL);
    setField("NUMERO_GIORNI_LA"            , BIG_DECIMAL);
    setField("NUMERO_GIORNI_LS"            , BIG_DECIMAL);
    setField("NUMERO_GIORNI_LI"            , BIG_DECIMAL);
    setField("NUMERO_GIORNI_RIDUZIONE"     , BIG_DECIMAL);
    setField("NUMERO_GIORNI_SCOMPUTO"      , BIG_DECIMAL);
    setField("FLAG_PENA_RESIDUA_CUMULO"    , STRING);
    setField("DATA_INIZIO"                 , DATE);
    setField("DATA_FINE_RECLUSIONE"        , DATE);
    setField("DATA_INIZIO_ARRESTO"         , DATE);
    setField("DATA_FINE_PRESUNTA"          , DATE);
    setField("DATA_FINE"                   , DATE);    
    setField("IS_PENA_DA_RICALCOLARE"      , STRING);
    setField("DAT_ID_DATI_FINALI_CUMULO"   , BIG_DECIMAL);
    setField("ISTR_ID_ISTRUTTORIA_CUMULO"  , BIG_DECIMAL);
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
  public  BigDecimal  getIdPenaRideterminataCumulo()  throws DAOException  { return getBigDecimal ("ID_PENA_RIDETERMINATA_CUMULO"); } 
  public  String      getCodTipoPenaDetentiva()       throws DAOException  { return getString     ("COD_TIPO_PENA_DETENTIVA"     ); } 
  public  BigDecimal  getNumAnniReclusione()          throws DAOException  { return getBigDecimal ("NUM_ANNI_RECLUSIONE"         ); } 
  public  BigDecimal  getNumMesiReclusione()          throws DAOException  { return getBigDecimal ("NUM_MESI_RECLUSIONE"         ); } 
  public  BigDecimal  getNumGiorniReclusione()        throws DAOException  { return getBigDecimal ("NUM_GIORNI_RECLUSIONE"       ); } 
  public  BigDecimal  getImportoMulta()               throws DAOException  { return getBigDecimal ("IMPORTO_MULTA"               ); } 
  public  BigDecimal  getNumAnniArresto()             throws DAOException  { return getBigDecimal ("NUM_ANNI_ARRESTO"            ); } 
  public  BigDecimal  getNumMesiArresto()             throws DAOException  { return getBigDecimal ("NUM_MESI_ARRESTO"            ); } 
  public  BigDecimal  getNumGiorniArresto()           throws DAOException  { return getBigDecimal ("NUM_GIORNI_ARRESTO"          ); } 
  public  BigDecimal  getImportoAmmenda()             throws DAOException  { return getBigDecimal ("IMPORTO_AMMENDA"             ); } 
  public  BigDecimal  getNumAnniIsolamentoDiurno()    throws DAOException  { return getBigDecimal ("NUM_ANNI_ISOLAMENTO_DIURNO"  ); } 
  public  BigDecimal  getNumMesiIsolamentoDiurno()    throws DAOException  { return getBigDecimal ("NUM_MESI_ISOLAMENTO_DIURNO"  ); } 
  public  BigDecimal  getNumGiorniIsolamentoDiurno()  throws DAOException  { return getBigDecimal ("NUM_GIORNI_ISOLAMENTO_DIURNO"); } 
  public  BigDecimal  getNumeroGiorniLA()             throws DAOException  { return getBigDecimal ("NUMERO_GIORNI_LA"            ); } 
  public  BigDecimal  getNumeroGiorniLS()             throws DAOException  { return getBigDecimal ("NUMERO_GIORNI_LS"            ); } 
  public  BigDecimal  getNumeroGiorniLI()             throws DAOException  { return getBigDecimal ("NUMERO_GIORNI_LI"            ); } 
  public  BigDecimal  getNumeroGiorniRiduzione()      throws DAOException  { return getBigDecimal ("NUMERO_GIORNI_RIDUZIONE"     ); } 
  public  BigDecimal  getNumeroGiorniScomputo()       throws DAOException  { return getBigDecimal ("NUMERO_GIORNI_SCOMPUTO"      ); } 
  public  String      getFlagPenaResiduaCumulo()      throws DAOException  { return getString     ("FLAG_PENA_RESIDUA_CUMULO"    ); } 
  public  Date        getDataInizio()                 throws DAOException  { return getDate       ("DATA_INIZIO"                 ); } 
  public  Date        getDataFineReclusione()         throws DAOException  { return getDate       ("DATA_FINE_RECLUSIONE"        ); } 
  public  Date        getDataInizioArresto()          throws DAOException  { return getDate       ("DATA_INIZIO_ARRESTO"         ); } 
  public  Date        getDataFinePresunta()           throws DAOException  { return getDate       ("DATA_FINE_PRESUNTA"          ); } 
  public  Date        getDataFine()                   throws DAOException  { return getDate       ("DATA_FINE"                   ); } 
  public  String      getIsPenaDaRicalcolare()        throws DAOException  { return getString     ("IS_PENA_DA_RICALCOLARE"      ); } 
  public  BigDecimal  getDatIdDatiFinaliCumulo()      throws DAOException  { return getBigDecimal ("DAT_ID_DATI_FINALI_CUMULO"   ); } 
  public  BigDecimal  getIstrIdIstruttoriaCumulo()    throws DAOException  { return getBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO"  ); } 
  public  String      getCodOperatoreInserimento()    throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"   ); } 
  public  Date        getDataInserimento()            throws DAOException  { return getDate       ("DATA_INSERIMENTO"            ); } 
  public  String      getCodUfficioInserimento()      throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"     ); } 
  public  String      getCodOperatoreAggiornamento()  throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO" ); } 
  public  Date        getDataAggiornamento()          throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"          ); } 
  public  String      getCodUfficioAggiornamento()    throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"   ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdPenaRideterminataCumulo  (BigDecimal  aValore )   { setBigDecimal ("ID_PENA_RIDETERMINATA_CUMULO", aValore); } 
  public void  setCodTipoPenaDetentiva       (String      aValore )   { setString     ("COD_TIPO_PENA_DETENTIVA"     , aValore); } 
  public void  setNumAnniReclusione          (BigDecimal  aValore )   { setBigDecimal ("NUM_ANNI_RECLUSIONE"         , aValore); } 
  public void  setNumMesiReclusione          (BigDecimal  aValore )   { setBigDecimal ("NUM_MESI_RECLUSIONE"         , aValore); } 
  public void  setNumGiorniReclusione        (BigDecimal  aValore )   { setBigDecimal ("NUM_GIORNI_RECLUSIONE"       , aValore); } 
  public void  setImportoMulta               (BigDecimal  aValore )   { setBigDecimal ("IMPORTO_MULTA"               , aValore); } 
  public void  setNumAnniArresto             (BigDecimal  aValore )   { setBigDecimal ("NUM_ANNI_ARRESTO"            , aValore); } 
  public void  setNumMesiArresto             (BigDecimal  aValore )   { setBigDecimal ("NUM_MESI_ARRESTO"            , aValore); } 
  public void  setNumGiorniArresto           (BigDecimal  aValore )   { setBigDecimal ("NUM_GIORNI_ARRESTO"          , aValore); } 
  public void  setImportoAmmenda             (BigDecimal  aValore )   { setBigDecimal ("IMPORTO_AMMENDA"             , aValore); } 
  public void  setNumAnniIsolamentoDiurno    (BigDecimal  aValore )   { setBigDecimal ("NUM_ANNI_ISOLAMENTO_DIURNO"  , aValore); } 
  public void  setNumMesiIsolamentoDiurno    (BigDecimal  aValore )   { setBigDecimal ("NUM_MESI_ISOLAMENTO_DIURNO"  , aValore); } 
  public void  setNumGiorniIsolamentoDiurno  (BigDecimal  aValore )   { setBigDecimal ("NUM_GIORNI_ISOLAMENTO_DIURNO", aValore); } 
  public void  setNumeroGiorniLA             (BigDecimal  aValore )   { setBigDecimal ("NUMERO_GIORNI_LA"            , aValore); } 
  public void  setNumeroGiorniLS             (BigDecimal  aValore )   { setBigDecimal ("NUMERO_GIORNI_LS"            , aValore); } 
  public void  setNumeroGiorniLI             (BigDecimal  aValore )   { setBigDecimal ("NUMERO_GIORNI_LI"            , aValore); } 
  public void  setNumeroGiorniRiduzione      (BigDecimal  aValore )   { setBigDecimal ("NUMERO_GIORNI_RIDUZIONE"     , aValore); } 
  public void  setNumeroGiorniScomputo       (BigDecimal  aValore )   { setBigDecimal ("NUMERO_GIORNI_SCOMPUTO"      , aValore); } 
  public void  setFlagPenaResiduaCumulo      (String      aValore )   { setString     ("FLAG_PENA_RESIDUA_CUMULO"    , aValore); } 
  public void  setDataInizio                 (Date        aValore )   { setDate       ("DATA_INIZIO"                 , aValore); } 
  public void  setDataFineReclusione         (Date        aValore )   { setDate       ("DATA_FINE_RECLUSIONE"        , aValore); } 
  public void  setDataInizioArresto          (Date        aValore )   { setDate       ("DATA_INIZIO_ARRESTO"         , aValore); } 
  public void  setDataFinePresunta           (Date        aValore )   { setDate       ("DATA_FINE_PRESUNTA"          , aValore); } 
  public void  setDataFine                   (Date        aValore )   { setDate       ("DATA_FINE"                   , aValore); }
  public void  setIsPenaDaRicalcolare        (String      aValore )   { setString     ("IS_PENA_DA_RICALCOLARE"      , aValore); } 
  public void  setDatIdDatiFinaliCumulo      (BigDecimal  aValore )   { setBigDecimal ("DAT_ID_DATI_FINALI_CUMULO"   , aValore); } 
  public void  setIstrIdIstruttoriaCumulo    (BigDecimal  aValore )   { setBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO"  , aValore); } 
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
    return new PenaRideterminataCumuloModel(  
      getIdPenaRideterminataCumulo() , 
      getCodTipoPenaDetentiva() , 
 "",
      getNumAnniReclusione() , 
      getNumMesiReclusione() , 
      getNumGiorniReclusione() , 
      getImportoMulta() , 
      getNumAnniArresto() , 
      getNumMesiArresto() , 
      getNumGiorniArresto() , 
      getImportoAmmenda() , 
      getNumAnniIsolamentoDiurno() , 
      getNumMesiIsolamentoDiurno() , 
      getNumGiorniIsolamentoDiurno() , 
      getNumeroGiorniLA() , 
      getNumeroGiorniLS() , 
      getNumeroGiorniLI() , 
      getNumeroGiorniRiduzione() , 
      getNumeroGiorniScomputo(),
      getFlagPenaResiduaCumulo() , 
      getDataInizio() , 
      getDataFineReclusione() , 
      getDataInizioArresto() , 
      getDataFinePresunta() , 
      getDataFine(),
      getIsPenaDaRicalcolare(),
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
  public void setDAOFromModel(PenaRideterminataCumuloModel aModel) throws DAOException {
    setIdPenaRideterminataCumulo  ( aModel.getIdPenaRideterminataCumulo() );  
    setCodTipoPenaDetentiva       ( aModel.getCodTipoPenaDetentiva()      );  
    setNumAnniReclusione          ( aModel.getNumAnniReclusione()         );  
    setNumMesiReclusione          ( aModel.getNumMesiReclusione()         );  
    setNumGiorniReclusione        ( aModel.getNumGiorniReclusione()       );  
    setImportoMulta               ( aModel.getImportoMulta()              );  
    setNumAnniArresto             ( aModel.getNumAnniArresto()            );  
    setNumMesiArresto             ( aModel.getNumMesiArresto()            );  
    setNumGiorniArresto           ( aModel.getNumGiorniArresto()          );  
    setImportoAmmenda             ( aModel.getImportoAmmenda()            );  
    setNumAnniIsolamentoDiurno    ( aModel.getNumAnniIsolamentoDiurno()   );  
    setNumMesiIsolamentoDiurno    ( aModel.getNumMesiIsolamentoDiurno()   );  
    setNumGiorniIsolamentoDiurno  ( aModel.getNumGiorniIsolamentoDiurno() );  
    setNumeroGiorniLA             ( aModel.getNumeroGiorniLA()            );  
    setNumeroGiorniLS             ( aModel.getNumeroGiorniLS()            );  
    setNumeroGiorniLI             ( aModel.getNumeroGiorniLI()            );  
    setNumeroGiorniRiduzione      ( aModel.getNumeroGiorniRiduzione()     );
    setNumeroGiorniScomputo       ( aModel.getNumeroGiorniScomputo()      );    
    setFlagPenaResiduaCumulo      ( aModel.getFlagPenaResiduaCumulo()     );  
    setDataInizio                 ( aModel.getDataInizio()                );  
    setDataFineReclusione         ( aModel.getDataFineReclusione()        );  
    setDataInizioArresto          ( aModel.getDataInizioArresto()         );  
    setDataFinePresunta           ( aModel.getDataFinePresunta()          );  
    setDataFine                   ( aModel.getDataFine()                  );
    setIsPenaDaRicalcolare        ( aModel.getIsPenaDaRicalcolare()       );     
    setDatIdDatiFinaliCumulo      ( aModel.getDatIdDatiFinaliCumulo()     );  
    setIstrIdIstruttoriaCumulo    ( aModel.getIstrIdIstruttoriaCumulo()   );  
    setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
    setDataInserimento            ( aModel.getDataInserimento()           );  
    setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
  }

  public void setDAOFromModelForUpdate (PenaRideterminataCumuloModel aModel) throws DAOException {
    //setIdPenaRideterminataCumulo  ( aModel.getIdPenaRideterminataCumulo() );  
    setCodTipoPenaDetentiva       ( aModel.getCodTipoPenaDetentiva()      );  
    
    setNumAnniReclusione          ( aModel.getNumAnniReclusione()         );  
    setNumMesiReclusione          ( aModel.getNumMesiReclusione()         );  
    setNumGiorniReclusione        ( aModel.getNumGiorniReclusione()       );  
    setImportoMulta               ( aModel.getImportoMulta()              );  
    setNumAnniArresto             ( aModel.getNumAnniArresto()            );  
    setNumMesiArresto             ( aModel.getNumMesiArresto()            );  
    setNumGiorniArresto           ( aModel.getNumGiorniArresto()          );  
    setImportoAmmenda             ( aModel.getImportoAmmenda()            );  
    
    setNumAnniIsolamentoDiurno    ( aModel.getNumAnniIsolamentoDiurno()   );  
    setNumMesiIsolamentoDiurno    ( aModel.getNumMesiIsolamentoDiurno()   );  
    setNumGiorniIsolamentoDiurno  ( aModel.getNumGiorniIsolamentoDiurno() );  
    
    setNumeroGiorniLA             ( aModel.getNumeroGiorniLA()            );  
    setNumeroGiorniLS             ( aModel.getNumeroGiorniLS()            );  
    setNumeroGiorniLI             ( aModel.getNumeroGiorniLI()            );  
    setNumeroGiorniRiduzione      ( aModel.getNumeroGiorniRiduzione()     );  
    setNumeroGiorniScomputo       ( aModel.getNumeroGiorniScomputo()      );    

    setFlagPenaResiduaCumulo      ( aModel.getFlagPenaResiduaCumulo()     );  
    setDataInizio                 ( aModel.getDataInizio()                );  
    setDataFineReclusione         ( aModel.getDataFineReclusione()        );  
    setDataInizioArresto          ( aModel.getDataInizioArresto()         );  
    setDataFinePresunta           ( aModel.getDataFinePresunta()          );  
    setDataFine                   ( aModel.getDataFine()                  ); 
    setIsPenaDaRicalcolare        ( aModel.getIsPenaDaRicalcolare()       ); 
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
  public void setCondizioni(PenaRideterminataCumuloModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdPenaRideterminataCumulo() != null ) { 
      lCondizioni += " and ID_PENA_RIDETERMINATA_CUMULO = " + aModel.getIdPenaRideterminataCumulo() + ""; 
    } 
    if (aModel.getCodTipoPenaDetentiva() != null && aModel.getCodTipoPenaDetentiva().length() > 0) { 
      lCondizioni += " and COD_TIPO_PENA_DETENTIVA = '" + aModel.getCodTipoPenaDetentiva() + "' "; 
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
    if (aModel.getNumAnniIsolamentoDiurno() != null ) { 
      lCondizioni += " and NUM_ANNI_ISOLAMENTO_DIURNO = " + aModel.getNumAnniIsolamentoDiurno() + ""; 
    } 
    if (aModel.getNumMesiIsolamentoDiurno() != null ) { 
      lCondizioni += " and NUM_MESI_ISOLAMENTO_DIURNO = " + aModel.getNumMesiIsolamentoDiurno() + ""; 
    } 
    if (aModel.getNumGiorniIsolamentoDiurno() != null ) { 
      lCondizioni += " and NUM_GIORNI_ISOLAMENTO_DIURNO = " + aModel.getNumGiorniIsolamentoDiurno() + ""; 
    } 
    if (aModel.getNumeroGiorniLA() != null ) { 
      lCondizioni += " and NUMERO_GIORNI_LA = " + aModel.getNumeroGiorniLA() + ""; 
    } 
    if (aModel.getNumeroGiorniLS() != null ) { 
      lCondizioni += " and NUMERO_GIORNI_LS = " + aModel.getNumeroGiorniLS() + ""; 
    } 
    if (aModel.getNumeroGiorniLI() != null ) { 
      lCondizioni += " and NUMERO_GIORNI_LI = " + aModel.getNumeroGiorniLI() + ""; 
    } 
    if (aModel.getNumeroGiorniRiduzione() != null ) { 
      lCondizioni += " and NUMERO_GIORNI_RIDUZIONE = " + aModel.getNumeroGiorniRiduzione() + ""; 
    } 
    if (aModel.getNumeroGiorniScomputo() != null ) { 
      lCondizioni += " and NUMERO_GIORNI_SCOMPUTO = " + aModel.getNumeroGiorniScomputo() + ""; 
    } 
    if (aModel.getFlagPenaResiduaCumulo() != null && aModel.getFlagPenaResiduaCumulo().length() > 0) { 
      lCondizioni += " and FLAG_PENA_RESIDUA_CUMULO = '" + aModel.getFlagPenaResiduaCumulo() + "' "; 
    } 
    if (aModel.getDataInizio() != null ) { 
      lCondizioni += " and to_char(DATA_INIZIO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInizio(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataFineReclusione() != null ) { 
      lCondizioni += " and to_char(DATA_FINE_RECLUSIONE,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataFineReclusione(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataInizioArresto() != null ) { 
      lCondizioni += " and to_char(DATA_INIZIO_ARRESTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInizioArresto(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataFinePresunta() != null ) { 
      lCondizioni += " and to_char(DATA_FINE_PRESUNTA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataFinePresunta(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataFine() != null ) { 
      lCondizioni += " and to_char(DATA_FINE,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataFine(),"dd/MM/yyyy") + "' "; 
    }
    if (aModel.getIsPenaDaRicalcolare() != null && aModel.getIsPenaDaRicalcolare().length() > 0) { 
      lCondizioni += " and IS_PENA_DA_RICALCOLARE = '" + aModel.getIsPenaDaRicalcolare() + "' "; 
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
  public void selCondizioneUpdate( BigDecimal aIdPenaRideterminataCumulo) {
    String lCondizioni = new String();

    lCondizioni += " and ID_PENA_RIDETERMINATA_CUMULO = " + aIdPenaRideterminataCumulo;
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
