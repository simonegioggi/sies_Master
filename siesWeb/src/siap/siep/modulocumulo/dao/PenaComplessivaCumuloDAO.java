package siap.siep.modulocumulo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.modulocumulo.model.PenaComplessivaCumuloModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: PenaComplessivaCumuloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella PenaComplessiva</p>
* <p>		in ambito Cumulo (Pena_complessiva_Cumulo) </p>
* @version
*/

public class PenaComplessivaCumuloDAO extends SIAPTableDAO
{
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public PenaComplessivaCumuloDAO (Connection con) {
    super(con);
    setTable("PENA_COMPLESSIVA_CUMULO");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_PENA_COMPLESSIVA_CUM","PEN_COM_CUM_SEQ");

    //setField("ID_PENA_COMPLESSIVA_CUM", BIG_DECIMAL);
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
    setField("DATA_PRESCRIZIONE"           , DATE);
    setField("FLAG_PENA_IN_CONTINUAZIONE"  , STRING);
    setField("FLAG_STATO"                  , STRING);
    setField("MOTIVO_MODIFICA"             , STRING);
    setField("TIT_ID_TITOLO_CUMULATO"      , BIG_DECIMAL);
    setField("ID_PENA_COMPLESSIVA_ORIGINE" , BIG_DECIMAL);
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
  public  BigDecimal  getIdPenaComplessivaCum()       throws DAOException  { return getBigDecimal ("ID_PENA_COMPLESSIVA_CUM"     ); } 
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
  public  Date        getDataPrescrizione()           throws DAOException  { return getDate       ("DATA_PRESCRIZIONE"           ); } 
  public  String      getFlagPenaInContinuazione()    throws DAOException  { return getString     ("FLAG_PENA_IN_CONTINUAZIONE"  ); } 
  public  String      getFlagStato()                  throws DAOException  { return getString     ("FLAG_STATO"                  ); } 
  public  String      getMotivoModifica()             throws DAOException  { return getString     ("MOTIVO_MODIFICA"             ); } 
  public  BigDecimal  getTitIdTitoloCumulato()        throws DAOException  { return getBigDecimal ("TIT_ID_TITOLO_CUMULATO"      ); } 
  public  BigDecimal  getIdPenaComplessivaOrigine()   throws DAOException  { return getBigDecimal ("ID_PENA_COMPLESSIVA_ORIGINE" ); } 
  public  String      getCodOperatoreInserimento()    throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"   ); } 
  public  Date        getDataInserimento()            throws DAOException  { return getDate       ("DATA_INSERIMENTO"            ); } 
  public  String      getCodUfficioInserimento()      throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"     ); } 
  public  String      getCodOperatoreAggiornamento()  throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO" ); } 
  public  Date        getDataAggiornamento()          throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"          ); } 
  public  String      getCodUfficioAggiornamento()    throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"   ); } 

  
  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdPenaComplessivaCum       (BigDecimal  aValore )   { setBigDecimal ("ID_PENA_COMPLESSIVA_CUM"     , aValore); } 
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
  public void  setDataPrescrizione           (Date        aValore )   { setDate       ("DATA_PRESCRIZIONE"           , aValore); } 
  public void  setFlagPenaInContinuazione    (String      aValore )   { setString     ("FLAG_PENA_IN_CONTINUAZIONE"  , aValore); } 
  public void  setFlagStato                  (String      aValore )   { setString     ("FLAG_STATO"                  , aValore); } 
  public void  setMotivoModifica             (String      aValore )   { setString     ("MOTIVO_MODIFICA"             , aValore); } 
  public void  setTitIdTitoloCumulato        (BigDecimal  aValore )   { setBigDecimal ("TIT_ID_TITOLO_CUMULATO"      , aValore); } 
  public void  setIdPenaComplessivaOrigine   (BigDecimal  aValore )   { setBigDecimal ("ID_PENA_COMPLESSIVA_ORIGINE" , aValore); } 
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
    return new PenaComplessivaCumuloModel(  
      getIdPenaComplessivaCum() , 
      getCodTipoPenaDetentiva() , "",
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
      getDataPrescrizione() , 
      
      getFlagPenaInContinuazione() , 

      getFlagStato() , 
      getMotivoModifica() , 
      getTitIdTitoloCumulato() , 
      getIdPenaComplessivaOrigine() ,

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
  public void setDAOFromModel(PenaComplessivaCumuloModel aModel) throws DAOException {
    setIdPenaComplessivaCum       ( aModel.getIdPenaComplessivaCum()      );  
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
    setDataPrescrizione           ( aModel.getDataPrescrizione()          );  
    setFlagPenaInContinuazione    ( aModel.getFlagPenaInContinuazione()   );  
    setFlagStato                  ( aModel.getFlagStato()                 );  
    setMotivoModifica             ( aModel.getMotivoModifica()            );  
    setTitIdTitoloCumulato        ( aModel.getTitIdTitoloCumulato()       );  
    setIdPenaComplessivaOrigine   ( aModel.getIdPenaComplessivaOrigine()  );  
    setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
    setDataInserimento            ( aModel.getDataInserimento()           );  
    setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
  }

  public void setDAOFromModelForUpdate(PenaComplessivaCumuloModel aModel) throws DAOException
  {
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
    setDataPrescrizione           ( aModel.getDataPrescrizione()          );  
    setFlagPenaInContinuazione    ( aModel.getFlagPenaInContinuazione()   );  
    setFlagStato                  ( aModel.getFlagStato()                 );  
    setMotivoModifica             ( aModel.getMotivoModifica()            );  
    //setTitIdTitoloCumulato        ( aModel.getTitIdTitoloCumulato()       );  
    //setIdPenaComplessivaOrigine   ( aModel.getIdPenaComplessivaOrigine()  );  
    
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  


    // Imposta la condizione di aggiornamento in chiave
    setCondizioneUpdate (aModel.getIdPenaComplessivaCum());
  }

  public void setCondizione(PenaComplessivaCumuloModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

  public void setCondizioneUpdate(BigDecimal key)
  {
   setCondition(" ID_PENA_COMPLESSIVA_CUM = " + key );
  }
}
