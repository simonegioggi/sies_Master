package siap.siep.modulocumulo.dao;

/**
* <p>Title: LibAnticipataCumuloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella LibAnticipataCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;
import f3b.util.DateUtils;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

import siap.siep.modulocumulo.model.LibAnticipataCumuloModel;

public class LibAnticipataCumuloDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public LibAnticipataCumuloDAO (Connection con) {
    super(con);
    setTable("LIB_ANTICIPATA_CUMULO");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_LIB_ANTICIPATA_CUMULO","LIB_ANTICIPATA_CUMULO_SEQ");

    //setField("ID_LIB_ANTICIPATA_CUMULO", BIG_DECIMAL);
    setField("COD_TIPO_LICENZA"           , STRING);
    setField("NUMERO_GIORNI"              , BIG_DECIMAL);
    setField("SOMMA_RISARC_DANNI"         , BIG_DECIMAL);
    setField("FLAG_CONCESSO"              , STRING);
    setField("TIPO_LA"                    , STRING);
    setField("FLAG_ELABORATO"             , STRING);
    
    setField("FLAG_STATO"                 , STRING);
    setField("MOTIVO_MODIFICA"            , STRING);
    setField("TIT_ID_TITOLO_CUMULATO"     , BIG_DECIMAL);
    setField("ID_LIBANTICIPATA_ORIGINE"   , BIG_DECIMAL);
    setField("STAT_ID_STATO_ESEC_TIT_CUM" , BIG_DECIMAL);
    
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
  public  BigDecimal  getIdLibAnticipataCumulo()      throws DAOException  { return getBigDecimal ("ID_LIB_ANTICIPATA_CUMULO"   ); } 
  public  String      getCodTipoLicenza()             throws DAOException  { return getString     ("COD_TIPO_LICENZA"           ); } 
  public  BigDecimal  getNumeroGiorni()               throws DAOException  { return getBigDecimal ("NUMERO_GIORNI"              ); } 
  public  BigDecimal  getSommaRisarcDanni()           throws DAOException  { return getBigDecimal ("SOMMA_RISARC_DANNI"         ); } 
  public  String      getFlagConcesso()               throws DAOException  { return getString     ("FLAG_CONCESSO"              ); } 
  public  String      getTipoLa()                     throws DAOException  { return getString     ("TIPO_LA"                    ); } 
  public  String      getFlagElaborato()              throws DAOException  { return getString     ("FLAG_ELABORATO"             ); } 
  
  public  String      getFlagStato()                  throws DAOException  { return getString     ("FLAG_STATO"                 ); } 
  public  String      getMotivoModifica()             throws DAOException  { return getString     ("MOTIVO_MODIFICA"            ); } 
  public  BigDecimal  getTitIdTitoloCumulato()        throws DAOException  { return getBigDecimal ("TIT_ID_TITOLO_CUMULATO"     ); } 
  public  BigDecimal  getIdLibanticipataOrigine()     throws DAOException  { return getBigDecimal ("ID_LIBANTICIPATA_ORIGINE"   ); }
  public  BigDecimal  getStatIdStatoEsecTitoloCum()   throws DAOException  { return getBigDecimal ("STAT_ID_STATO_ESEC_TIT_CUM" ); } 
  
  public  String      getCodOperatoreInserimento()    throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"  ); } 
  public  Date        getDataInserimento()            throws DAOException  { return getDate       ("DATA_INSERIMENTO"           ); } 
  public  String      getCodUfficioInserimento()      throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"    ); } 
  public  String      getCodOperatoreAggiornamento()  throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"); } 
  public  Date        getDataAggiornamento()          throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"         ); } 
  public  String      getCodUfficioAggiornamento()    throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"  ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdLibAnticipataCumulo      (BigDecimal  aValore )   { setBigDecimal ("ID_LIB_ANTICIPATA_CUMULO"   , aValore); } 
  public void  setCodTipoLicenza             (String      aValore )   { setString     ("COD_TIPO_LICENZA"           , aValore); } 
  public void  setNumeroGiorni               (BigDecimal  aValore )   { setBigDecimal ("NUMERO_GIORNI"              , aValore); } 
  public void  setSommaRisarcDanni           (BigDecimal  aValore )   { setBigDecimal ("SOMMA_RISARC_DANNI"         , aValore); } 
  public void  setFlagConcesso               (String      aValore )   { setString     ("FLAG_CONCESSO"              , aValore); } 
  public void  setTipoLa                     (String      aValore )   { setString     ("TIPO_LA"                    , aValore); } 
  public void  setFlagElaborato              (String      aValore )   { setString     ("FLAG_ELABORATO"             , aValore); } 
  
  public void  setFlagStato                  (String      aValore )   { setString     ("FLAG_STATO"                 , aValore); } 
  public void  setMotivoModifica             (String      aValore )   { setString     ("MOTIVO_MODIFICA"            , aValore); } 
  public void  setTitIdTitoloCumulato        (BigDecimal  aValore )   { setBigDecimal ("TIT_ID_TITOLO_CUMULATO"     , aValore); } 
  public void  setIdLibanticipataOrigine     (BigDecimal  aValore )   { setBigDecimal ("ID_LIBANTICIPATA_ORIGINE"   , aValore); }
  public void  setStatIdStatoEsecTitoloCum   (BigDecimal  aValore )   { setBigDecimal ("STAT_ID_STATO_ESEC_TIT_CUM" , aValore); }
  
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
    return new LibAnticipataCumuloModel(  
      getIdLibAnticipataCumulo() , 
      getCodTipoLicenza() , "",
      getNumeroGiorni() , 
      getSommaRisarcDanni() , 
      getFlagConcesso() , 
      getTipoLa() , 
      getFlagElaborato() , 
      getFlagStato() , 
      getMotivoModifica() , 
      getTitIdTitoloCumulato() , 
      getIdLibanticipataOrigine() , 
      getStatIdStatoEsecTitoloCum(),
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
  public void setDAOFromModel(LibAnticipataCumuloModel aModel) throws DAOException {
    setIdLibAnticipataCumulo      ( aModel.getIdLibAnticipataCumulo()     );  
    setCodTipoLicenza             ( aModel.getCodTipoLicenza()            );  
    setNumeroGiorni               ( aModel.getNumeroGiorni()              );  
    setSommaRisarcDanni           ( aModel.getSommaRisarcDanni()          );  
    setFlagConcesso               ( aModel.getFlagConcesso()              );  
    setTipoLa                     ( aModel.getTipoLa()                    );  
    setFlagElaborato              ( aModel.getFlagElaborato()             );  
    setFlagStato                  ( aModel.getFlagStato()                 );  
    setMotivoModifica             ( aModel.getMotivoModifica()            );  
    setTitIdTitoloCumulato        ( aModel.getTitIdTitoloCumulato()       );  
    setIdLibanticipataOrigine     ( aModel.getIdLibanticipataOrigine()    ); 
    setStatIdStatoEsecTitoloCum	  ( aModel.getStatIdStatoEsecTitoloCum()  );
    setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
    setDataInserimento            ( aModel.getDataInserimento()           );  
    setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
  }

  /***************************************************************************** 
   * Metodo che imposta i campi delle operazioni di Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void setDAOFromModelForUpdate(LibAnticipataCumuloModel aModel) throws DAOException {
    setIdLibAnticipataCumulo      ( aModel.getIdLibAnticipataCumulo()     );  
    setCodTipoLicenza             ( aModel.getCodTipoLicenza()            );  
    setNumeroGiorni               ( aModel.getNumeroGiorni()              );  
    setSommaRisarcDanni           ( aModel.getSommaRisarcDanni()          );  
    setFlagConcesso               ( aModel.getFlagConcesso()              );  
    setTipoLa                     ( aModel.getTipoLa()                    );  
    setFlagElaborato              ( aModel.getFlagElaborato()             );  
    setFlagStato                  ( aModel.getFlagStato()                 );  
    setMotivoModifica             ( aModel.getMotivoModifica()            );  
    setTitIdTitoloCumulato        ( aModel.getTitIdTitoloCumulato()       );  
    setIdLibanticipataOrigine     ( aModel.getIdLibanticipataOrigine()    ); 
    setStatIdStatoEsecTitoloCum	  ( aModel.getStatIdStatoEsecTitoloCum()  );
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
  public void setCondizioni(LibAnticipataCumuloModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdLibAnticipataCumulo() != null ) { 
      lCondizioni += " and ID_LIB_ANTICIPATA_CUMULO = " + aModel.getIdLibAnticipataCumulo() + ""; 
    } 
    if (aModel.getCodTipoLicenza() != null && aModel.getCodTipoLicenza().length() > 0) { 
      lCondizioni += " and COD_TIPO_LICENZA = '" + aModel.getCodTipoLicenza() + "' "; 
    } 
    if (aModel.getNumeroGiorni() != null ) { 
      lCondizioni += " and NUMERO_GIORNI = " + aModel.getNumeroGiorni() + ""; 
    } 
    if (aModel.getSommaRisarcDanni() != null ) { 
      lCondizioni += " and SOMMA_RISARC_DANNI = " + aModel.getSommaRisarcDanni() + ""; 
    } 
    if (aModel.getFlagConcesso() != null && aModel.getFlagConcesso().length() > 0) { 
      lCondizioni += " and FLAG_CONCESSO = '" + aModel.getFlagConcesso() + "' "; 
    } 
    if (aModel.getTipoLa() != null && aModel.getTipoLa().length() > 0) { 
      lCondizioni += " and TIPO_LA = '" + aModel.getTipoLa() + "' "; 
    } 
    if (aModel.getFlagElaborato() != null && aModel.getFlagElaborato().length() > 0) { 
      lCondizioni += " and FLAG_ELABORATO = '" + aModel.getFlagElaborato() + "' "; 
    } 
    if (aModel.getFlagStato() != null && aModel.getFlagStato().length() > 0) { 
      lCondizioni += " and FLAG_STATO = '" + aModel.getFlagStato() + "' "; 
    } 
    if (aModel.getMotivoModifica() != null && aModel.getMotivoModifica().length() > 0) { 
      lCondizioni += " and MOTIVO_MODIFICA = '" + aModel.getMotivoModifica() + "' "; 
    } 
    if (aModel.getTitIdTitoloCumulato() != null ) { 
      lCondizioni += " and TIT_ID_TITOLO_CUMULATO = " + aModel.getTitIdTitoloCumulato() + ""; 
    } 
    if (aModel.getIdLibanticipataOrigine() != null ) { 
      lCondizioni += " and ID_LIBANTICIPATA_ORIGINE = " + aModel.getIdLibanticipataOrigine() + ""; 
    } 
    if (aModel.getStatIdStatoEsecTitoloCum() != null ) { 
        lCondizioni += " and STAT_ID_STATO_ESEC_TIT_CUM = " + aModel.getStatIdStatoEsecTitoloCum() + ""; 
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
  public void selCondizioneUpdate( BigDecimal aIdLibAnticipataCumulo) {
    String lCondizioni = new String();

    lCondizioni += " and ID_LIB_ANTICIPATA_CUMULO = " + aIdLibAnticipataCumulo;
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni);
  }
  
  public void selCondizioneUpdateByIdStatEsec( BigDecimal aIdStatoEsecuzione) 
  {
	    String lCondizioni = new String();

	    lCondizioni += " and STAT_ID_STATO_ESEC_TIT_CUM = " + aIdStatoEsecuzione;
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
