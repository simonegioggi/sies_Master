package siap.siep.tipologiaorario.dao;

/**
* <p>Title: TipologiaOrarioDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella TipologiaOrario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.tipologiaorario.model.TipologiaOrarioModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class TipologiaOrarioDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public TipologiaOrarioDAO (Connection con) {
    super(con);
    setTable("TIPOLOGIA_ORARIO");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_TIPOLOGIA_ORARIO","TIP_ORA_SEQ");
    this.setFieldKey("ID_TIPOLOGIA_ORARIO", BIG_DECIMAL);
    
    setField("ID_TIPOLOGIA_ORARIO"        , BIG_DECIMAL);
    setField("COD_NUM_GIORNO"             , STRING);
    setField("DALLE_ORE"                  , STRING);
    setField("ALLE_ORE"                   , STRING);
    setField("ENTE_INCARICATO"            , STRING);
    setField("BEN_ID_BENEFICIO"           , BIG_DECIMAL);
    setField("COD_OPERATORE_INSERIMENTO"  , STRING);
    setField("DATA_INSERIMENTO"           , DATE);
    setField("COD_UFFICIO_INSERIMENTO"    , STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO"         , DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO"  , STRING);
    
    //MEV26 - Cumulo
    setField("DAT_FIN_CUM_ULT_SANZIONI"   , BIG_DECIMAL);
    setField("BEN_ID_BENEFICIO_CUMULO"    , BIG_DECIMAL);
  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  BigDecimal  getIdTipologiaOrario()          throws DAOException  { return getBigDecimal ("ID_TIPOLOGIA_ORARIO"        ); } 
  public  String      getCodNumGiorno()               throws DAOException  { return getString     ("COD_NUM_GIORNO"             ); } 
  public  String      getDalleOre()                   throws DAOException  { return getString     ("DALLE_ORE"                  ); } 
  public  String      getAlleOre()                    throws DAOException  { return getString     ("ALLE_ORE"                   ); } 
  public  String      getEnteIncaricato()             throws DAOException  { return getString     ("ENTE_INCARICATO"            ); } 
  public  BigDecimal  getBenIdBeneficio()             throws DAOException  { return getBigDecimal ("BEN_ID_BENEFICIO"           ); } 
  public  String      getCodOperatoreInserimento()    throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"  ); } 
  public  Date        getDataInserimento()            throws DAOException  { return getDate       ("DATA_INSERIMENTO"           ); } 
  public  String      getCodUfficioInserimento()      throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"    ); } 
  public  String      getCodOperatoreAggiornamento()  throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"); } 
  public  Date        getDataAggiornamento()          throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"         ); } 
  public  String      getCodUfficioAggiornamento()    throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"  ); } 
  public BigDecimal   getDatFinCumUltSanzioni()       throws DAOException  { return getBigDecimal ("DAT_FIN_CUM_ULT_SANZIONI"   ); } 
  public  BigDecimal  getBenIdBeneficioCumulo()       throws DAOException  { return getBigDecimal ("BEN_ID_BENEFICIO_CUMULO"    ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdTipologiaOrario          (BigDecimal  aValore )   { setBigDecimal ("ID_TIPOLOGIA_ORARIO"        , aValore); } 
  public void  setCodNumGiorno               (String      aValore )   { setString     ("COD_NUM_GIORNO"             , aValore); } 
  public void  setDalleOre                   (String      aValore )   { setString     ("DALLE_ORE"                  , aValore); } 
  public void  setAlleOre                    (String      aValore )   { setString     ("ALLE_ORE"                   , aValore); } 
  public void  setEnteIncaricato             (String      aValore )   { setString     ("ENTE_INCARICATO"            , aValore); } 
  public void  setBenIdBeneficio             (BigDecimal  aValore )   { setBigDecimal ("BEN_ID_BENEFICIO"           , aValore); } 
  public void  setCodOperatoreInserimento    (String      aValore )   { setString     ("COD_OPERATORE_INSERIMENTO"  , aValore); } 
  public void  setDataInserimento            (Date        aValore )   { setDate       ("DATA_INSERIMENTO"           , aValore); } 
  public void  setCodUfficioInserimento      (String      aValore )   { setString     ("COD_UFFICIO_INSERIMENTO"    , aValore); } 
  public void  setCodOperatoreAggiornamento  (String      aValore )   { setString     ("COD_OPERATORE_AGGIORNAMENTO", aValore); } 
  public void  setDataAggiornamento          (Date        aValore )   { setDate       ("DATA_AGGIORNAMENTO"         , aValore); } 
  public void  setCodUfficioAggiornamento    (String      aValore )   { setString     ("COD_UFFICIO_AGGIORNAMENTO"  , aValore); } 
  public void  setDatFinCumUltSanzioni       (BigDecimal  aValore )   { setBigDecimal ("DAT_FIN_CUM_ULT_SANZIONI"   , aValore); }
  public void  setBenIdBeneficioCumulo       (BigDecimal  aValore )   { setBigDecimal ("BEN_ID_BENEFICIO_CUMULO"    , aValore); } 


  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new TipologiaOrarioModel(  
      getIdTipologiaOrario() , 
      getCodNumGiorno() , 
 "",
      getDalleOre() , 
      getAlleOre() , 
      getEnteIncaricato() , 
      getBenIdBeneficio() , 
      getCodOperatoreInserimento() , 
      getDataInserimento() , 
      getCodUfficioInserimento() , 
 "",
      getCodOperatoreAggiornamento() , 
      getDataAggiornamento() , 
      getCodUfficioAggiornamento(),
  "",
     getDatFinCumUltSanzioni(),
     getBenIdBeneficioCumulo()
    );
  }


  /***************************************************************************** 
   * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void setDAOFromModel(TipologiaOrarioModel aModel) throws DAOException {
    setIdTipologiaOrario          ( aModel.getIdTipologiaOrario()         );  
    setCodNumGiorno               ( aModel.getCodNumGiorno()              );  
    setDalleOre                   ( aModel.getDalleOre()                  );  
    setAlleOre                    ( aModel.getAlleOre()                   );  
    setEnteIncaricato             ( aModel.getEnteIncaricato()            );  
    setBenIdBeneficio             ( aModel.getBenIdBeneficio()            );  
    
    setDatFinCumUltSanzioni       ( aModel.getDatFinCumUltSanzioni()      ); 
    setBenIdBeneficioCumulo       ( aModel.getBenIdBeneficioCumulo()      ); 
     
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
  public void setCondizioni(TipologiaOrarioModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdTipologiaOrario() != null ) { 
      lCondizioni += " and ID_TIPOLOGIA_ORARIO = " + aModel.getIdTipologiaOrario() + ""; 
    } 
    if (aModel.getCodNumGiorno() != null && aModel.getCodNumGiorno().length() > 0) { 
      lCondizioni += " and COD_NUM_GIORNO = '" + aModel.getCodNumGiorno() + "' "; 
    } 
    if (aModel.getDalleOre() != null && aModel.getDalleOre().length() > 0) { 
      lCondizioni += " and DALLE_ORE = '" + aModel.getDalleOre() + "' "; 
    } 
    if (aModel.getAlleOre() != null && aModel.getAlleOre().length() > 0) { 
      lCondizioni += " and ALLE_ORE = '" + aModel.getAlleOre() + "' "; 
    } 
    if (aModel.getEnteIncaricato() != null && aModel.getEnteIncaricato().length() > 0) { 
      lCondizioni += " and ENTE_INCARICATO = '" + aModel.getEnteIncaricato() + "' "; 
    } 
    if (aModel.getBenIdBeneficio() != null ) { 
      lCondizioni += " and BEN_ID_BENEFICIO = " + aModel.getBenIdBeneficio() + ""; 
    } 
    
    
    if (aModel.getDatFinCumUltSanzioni()!= null ) { 
      lCondizioni += " and DAT_FIN_CUM_ULT_SANZIONI = " + aModel.getDatFinCumUltSanzioni() + ""; 
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
  public void selCondizioneUpdate( BigDecimal aIdTipologiaOrario) {
    String lCondizioni = new String();

    lCondizioni += " and ID_TIPOLOGIA_ORARIO = " + aIdTipologiaOrario;
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni);
  }

  public void selCondizioneDeleteByIdBeneficio( BigDecimal aIdBeneficio) {
	    String lCondizioni = new String();

	    lCondizioni += " and BEN_ID_BENEFICIO = " + aIdBeneficio;
	    // Elimino il primo and 
	    if (lCondizioni.length() > 0) { 
	      lCondizioni = lCondizioni.substring(4); 
	    } 

	    setCondition(lCondizioni);
	  }
  
  /**
   * MEV26 - Cumulo - condizione per delete TipologiaOrario legate alla
   * sanzione LPU definita in cumulo
   * @param aIdDatiFinaliUlterioriSanzioni
   */
  public void selCondizioneDeleteByIdDatFinUltSanz( BigDecimal aIdDatiFinaliUlterioriSanzioni) {
    String lCondizioni = new String();

    lCondizioni += " and DAT_FIN_CUM_ULT_SANZIONI = " + aIdDatiFinaliUlterioriSanzioni;
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni);
  }
  
  public void selCondizioneDeleteByIdBeneficioCumulo( BigDecimal aIdBeneficioCumulo)
  {
	    String lCondizioni = new String();

	    lCondizioni += " and BEN_ID_BENEFICIO_CUMULO = " + aIdBeneficioCumulo;
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
