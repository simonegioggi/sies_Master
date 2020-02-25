package siap.siep.modulocumulo.dao;

/**
* <p>Title: PosizioneGiuridicaCumuloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella PosizioneGiuridicaCumulo</p>
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

import siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel;

public class PosizioneGiuridicaCumuloDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public PosizioneGiuridicaCumuloDAO (Connection con) {
    super(con);
    setTable("POSIZIONE_GIURIDICA_CUMULO");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_POSIZIONE_GIURIDICA_CUM","POS_GIU_CUM_SEQ");

    //setField("ID_POSIZIONE_GIURIDICA_CUM", BIG_DECIMAL);
    setField("COD_POSIZIONE_GIURIDICA"       , STRING);
    setField("DATA_INIZIO"                   , DATE);
    setField("IST_DET_ID_ISTITUTO_DETENZIONE", STRING);
    setField("ALTRO_LUOGO"                   , STRING);
    setField("CHIAVE_ANNO_FAS_SIUS"          , BIG_DECIMAL);
    setField("CHIAVE_PROGR_FAS_SIUS"         , BIG_DECIMAL);
    setField("CHIAVE_UFF_FAS_SIUS"           , STRING);
    setField("ANNO_REGISTRO"                 , BIG_DECIMAL);
    setField("NUMERO_REGISTRO"               , BIG_DECIMAL);
    setField("COD_TIPO_PROVVEDIMENTO"        , STRING);    
    setField("DATA_EMISSIONE_PROVV"          , DATE);
    setField("NUM_ANNI_MISURA"               , BIG_DECIMAL);
    setField("NUM_MESI_MISURA"               , BIG_DECIMAL);
    setField("NUM_GIORNI_MISURA"             , BIG_DECIMAL);
    setField("DATA_FINE_MISURA"              , DATE);
    setField("FLAG_DECISIONE_TRIBUNALE"		 , STRING);	
    
    setField("FLAG_DIFF_DET_DOM"			 , STRING);	
    setField("DATA_INIZIO_MISURA"            , DATE);
    
    setField("DAT_ID_DATI_FINALI_CUMULO"     , BIG_DECIMAL);
    setField("ISTR_ID_ISTRUTTORIA_CUMULO"    , BIG_DECIMAL);
    setField("TIT_ID_TITOLO_CUMULATO"	     , BIG_DECIMAL);
    
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
  public  BigDecimal  getIdPosizioneGiuridicaCum()     throws DAOException  { return getBigDecimal ("ID_POSIZIONE_GIURIDICA_CUM"    ); } 
  public  String      getCodPosizioneGiuridica()       throws DAOException  { return getString     ("COD_POSIZIONE_GIURIDICA"       ); } 
  public  Date        getDataInizio()                  throws DAOException  { return getDate       ("DATA_INIZIO"                   ); } 
  public  String      getIstDetIdIstitutoDetenzione()  throws DAOException  { return getString     ("IST_DET_ID_ISTITUTO_DETENZIONE"); } 
  public  String      getAltroLuogo()                  throws DAOException  { return getString     ("ALTRO_LUOGO"                   ); } 
  public  BigDecimal  getChiaveAnnoFasSius()           throws DAOException  { return getBigDecimal ("CHIAVE_ANNO_FAS_SIUS"          ); } 
  public  BigDecimal  getChiaveProgrFasSius()          throws DAOException  { return getBigDecimal ("CHIAVE_PROGR_FAS_SIUS"         ); } 
  public  String      getChiaveUffFasSius()            throws DAOException  { return getString     ("CHIAVE_UFF_FAS_SIUS"           ); } 
  public  BigDecimal  getAnnoRegistro()                throws DAOException  { return getBigDecimal ("ANNO_REGISTRO"                 ); } 
  public  BigDecimal  getNumeroRegistro()              throws DAOException  { return getBigDecimal ("NUMERO_REGISTRO"               ); }
  public  String      getCodTipoProvvedimento()        throws DAOException  { return getString     ("COD_TIPO_PROVVEDIMENTO"        ); }   
  public  Date        getDataEmissioneProvv()          throws DAOException  { return getDate       ("DATA_EMISSIONE_PROVV"          ); } 
  public  BigDecimal  getNumAnniMisura()               throws DAOException  { return getBigDecimal ("NUM_ANNI_MISURA"               ); } 
  public  BigDecimal  getNumMesiMisura()               throws DAOException  { return getBigDecimal ("NUM_MESI_MISURA"               ); } 
  public  BigDecimal  getNumGiorniMisura()             throws DAOException  { return getBigDecimal ("NUM_GIORNI_MISURA"             ); } 
  public  Date        getDataFineMisura()              throws DAOException  { return getDate       ("DATA_FINE_MISURA"              ); } 
  
  public  String	  getFlagDecisioneTDS()			   throws DAOException	{ return getString		("FLAG_DECISIONE_TRIBUNALE"		); }
  public  String	  getFlagDifferimentoDetDom()	   throws DAOException	{ return getString		("FLAG_DIFF_DET_DOM"			); }
  public  Date        getDataInizioMisura()            throws DAOException  { return getDate        ("DATA_INIZIO_MISURA"           ); }
  
  public  BigDecimal  getDatIdDatiFinaliCumulo()       throws DAOException  { return getBigDecimal ("DAT_ID_DATI_FINALI_CUMULO"     ); }
  public  BigDecimal  getIstrIdIstruttoriaCumulo()     throws DAOException  { return getBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO"    ); } 
  public  BigDecimal  getTitIdTitoloCumulato()         throws DAOException  { return getBigDecimal ("TIT_ID_TITOLO_CUMULATO"	    ); }
  public  String      getCodOperatoreInserimento()     throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"     ); } 
  public  Date        getDataInserimento()             throws DAOException  { return getDate       ("DATA_INSERIMENTO"              ); } 
  public  String      getCodUfficioInserimento()       throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"       ); } 
  public  String      getCodOperatoreAggiornamento()   throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"   ); } 
  public  Date        getDataAggiornamento()           throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"            ); } 
  public  String      getCodUfficioAggiornamento()     throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"     ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdPosizioneGiuridicaCum     (BigDecimal  aValore )   { setBigDecimal ("ID_POSIZIONE_GIURIDICA_CUM"    , aValore); } 
  public void  setCodPosizioneGiuridica       (String      aValore )   { setString     ("COD_POSIZIONE_GIURIDICA"       , aValore); } 
  public void  setDataInizio                  (Date        aValore )   { setDate       ("DATA_INIZIO"                   , aValore); } 
  public void  setIstDetIdIstitutoDetenzione  (String      aValore )   { setString     ("IST_DET_ID_ISTITUTO_DETENZIONE", aValore); } 
  public void  setAltroLuogo                  (String      aValore )   { setString     ("ALTRO_LUOGO"                   , aValore); } 
  public void  setChiaveAnnoFasSius           (BigDecimal  aValore )   { setBigDecimal ("CHIAVE_ANNO_FAS_SIUS"          , aValore); } 
  public void  setChiaveProgrFasSius          (BigDecimal  aValore )   { setBigDecimal ("CHIAVE_PROGR_FAS_SIUS"         , aValore); } 
  public void  setChiaveUffFasSius            (String      aValore )   { setString     ("CHIAVE_UFF_FAS_SIUS"           , aValore); } 
  public void  setAnnoRegistro                (BigDecimal  aValore )   { setBigDecimal ("ANNO_REGISTRO"                 , aValore); } 
  public void  setNumeroRegistro              (BigDecimal  aValore )   { setBigDecimal ("NUMERO_REGISTRO"               , aValore); }
  public void  setCodTipoProvvedimento        (String      aValore )   { setString     ("COD_TIPO_PROVVEDIMENTO"        , aValore); } 
  public void  setDataEmissioneProvv          (Date        aValore )   { setDate       ("DATA_EMISSIONE_PROVV"          , aValore); }
  public void  setNumAnniMisura               (BigDecimal  aValore )   { setBigDecimal ("NUM_ANNI_MISURA"               , aValore); } 
  public void  setNumMesiMisura               (BigDecimal  aValore )   { setBigDecimal ("NUM_MESI_MISURA"               , aValore); } 
  public void  setNumGiorniMisura             (BigDecimal  aValore )   { setBigDecimal ("NUM_GIORNI_MISURA"             , aValore); } 
  public void  setDataFineMisura              (Date        aValore )   { setDate       ("DATA_FINE_MISURA"              , aValore); } 
  
  public void  setFlagDecisioneTDS				(String 	aValore)	{ setString		("FLAG_DECISIONE_TRIBUNALE"		, aValore); }
  public void  setFlagDifferimentoDetDom		(String 	aValore)	{ setString		("FLAG_DIFF_DET_DOM"			, aValore); }
  public void  setDataInizioMisura				(Date       aValore )   { setDate       ("DATA_INIZIO_MISURA"           , aValore); } 
  
  public void  setDatIdDatiFinaliCumulo       (BigDecimal  aValore )   { setBigDecimal ("DAT_ID_DATI_FINALI_CUMULO"     , aValore); }
  public void  setIstrIdIstruttoriaCumulo     (BigDecimal  aValore )   { setBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO"    , aValore); } 
  public void  setTitIdTitoloCumulato	      (BigDecimal  aValore )   { setBigDecimal ("TIT_ID_TITOLO_CUMULATO"	    , aValore); }
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
    return new PosizioneGiuridicaCumuloModel(  
      getIdPosizioneGiuridicaCum() , 
      getCodPosizioneGiuridica() , 
 "",
      getDataInizio() , 
      getIstDetIdIstitutoDetenzione() , 
      getAltroLuogo() , 
      getChiaveAnnoFasSius() , 
      getChiaveProgrFasSius() , 
      getChiaveUffFasSius() , 
      getAnnoRegistro() , 
      getNumeroRegistro() , 
      getCodTipoProvvedimento() , 
 "",      
      getDataEmissioneProvv() , 
      getNumAnniMisura() , 
      getNumMesiMisura() , 
      getNumGiorniMisura() , 
      getDataFineMisura() , 
      
      getFlagDecisioneTDS(),
      
      getFlagDifferimentoDetDom(),
      getDataInizioMisura(),
      
      getDatIdDatiFinaliCumulo() , 
      getIstrIdIstruttoriaCumulo() , 
      getTitIdTitoloCumulato() , 
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
  public void setDAOFromModel(PosizioneGiuridicaCumuloModel aModel) throws DAOException {
    setIdPosizioneGiuridicaCum     ( aModel.getIdPosizioneGiuridicaCum()    );  
    setCodPosizioneGiuridica       ( aModel.getCodPosizioneGiuridica()      );  
    setDataInizio                  ( aModel.getDataInizio()                 );  
    setIstDetIdIstitutoDetenzione  ( aModel.getIstDetIdIstitutoDetenzione() );  
    setAltroLuogo                  ( aModel.getAltroLuogo()                 );  
    setChiaveAnnoFasSius           ( aModel.getChiaveAnnoFasSius()          );  
    setChiaveProgrFasSius          ( aModel.getChiaveProgrFasSius()         );  
    setChiaveUffFasSius            ( aModel.getChiaveUffFasSius()           );  
    setAnnoRegistro                ( aModel.getAnnoRegistro()               );  
    setNumeroRegistro              ( aModel.getNumeroRegistro()             ); 
    setCodTipoProvvedimento        ( aModel.getCodTipoProvvedimento()       );  
    setDataEmissioneProvv          ( aModel.getDataEmissioneProvv()         );
    setNumAnniMisura               ( aModel.getNumAnniMisura()              );  
    setNumMesiMisura               ( aModel.getNumMesiMisura()              );  
    setNumGiorniMisura             ( aModel.getNumGiorniMisura()            );  
    setDataFineMisura              ( aModel.getDataFineMisura()             );
    
    setFlagDecisioneTDS				(aModel.getFlagDecisioneTDS()			);
    setFlagDifferimentoDetDom		(aModel.getFlagDifferimentoDetDom()		);
    setDataInizioMisura				(aModel.getDataInizioMisura()			);
    
    setDatIdDatiFinaliCumulo       ( aModel.getDatIdDatiFinaliCumulo()      ); 
    setIstrIdIstruttoriaCumulo     ( aModel.getIstrIdIstruttoriaCumulo()    );  
    setTitIdTitoloCumulato	       ( aModel.getTitIdTitoloCumulato()	    ); 
    setCodOperatoreInserimento     ( aModel.getCodOperatoreInserimento()    );  
    setDataInserimento             ( aModel.getDataInserimento()            );  
    setCodUfficioInserimento       ( aModel.getCodUfficioInserimento()      );  
    setCodOperatoreAggiornamento   ( aModel.getCodOperatoreAggiornamento()  );  
    setDataAggiornamento           ( aModel.getDataAggiornamento()          );  
    setCodUfficioAggiornamento     ( aModel.getCodUfficioAggiornamento()    );  
  }

  public void setDAOFromModelForUpdate (PosizioneGiuridicaCumuloModel aModel) throws DAOException {
    //setIdPosizioneGiuridicaCum     ( aModel.getIdPosizioneGiuridicaCum()    );  
    setCodPosizioneGiuridica       ( aModel.getCodPosizioneGiuridica()      );  
    setDataInizio                  ( aModel.getDataInizio()                 );  
    setIstDetIdIstitutoDetenzione  ( aModel.getIstDetIdIstitutoDetenzione() );  
    setAltroLuogo                  ( aModel.getAltroLuogo()                 );  
    setChiaveAnnoFasSius           ( aModel.getChiaveAnnoFasSius()          );  
    setChiaveProgrFasSius          ( aModel.getChiaveProgrFasSius()         );  
    setChiaveUffFasSius            ( aModel.getChiaveUffFasSius()           );  
    setAnnoRegistro                ( aModel.getAnnoRegistro()               );  
    setNumeroRegistro              ( aModel.getNumeroRegistro()             ); 
    setCodTipoProvvedimento        ( aModel.getCodTipoProvvedimento()       );  
    setDataEmissioneProvv          ( aModel.getDataEmissioneProvv()         );
    setNumAnniMisura               ( aModel.getNumAnniMisura()              );  
    setNumMesiMisura               ( aModel.getNumMesiMisura()              );  
    setNumGiorniMisura             ( aModel.getNumGiorniMisura()            );  
    setDataFineMisura              ( aModel.getDataFineMisura()             ); 
    
    setFlagDecisioneTDS				(aModel.getFlagDecisioneTDS()			);
    setFlagDifferimentoDetDom		(aModel.getFlagDifferimentoDetDom()		);
    setDataInizioMisura				(aModel.getDataInizioMisura()			);
    
    //setDatIdDatiFinaliCumulo       ( aModel.getDatIdDatiFinaliCumulo()      ); 
    //setIstrIdIstruttoriaCumulo     ( aModel.getIstrIdIstruttoriaCumulo()    );  
    //setTitIdTitoloCumulato		(aModel.getTitIdTitoloCumulato()	      ); 
    //setCodOperatoreInserimento     ( aModel.getCodOperatoreInserimento()    );  
    //setDataInserimento             ( aModel.getDataInserimento()            );  
    //setCodUfficioInserimento       ( aModel.getCodUfficioInserimento()      );  
    setCodOperatoreAggiornamento   ( aModel.getCodOperatoreAggiornamento()  );  
    setDataAggiornamento           ( aModel.getDataAggiornamento()          );  
    setCodUfficioAggiornamento     ( aModel.getCodUfficioAggiornamento()    );  
  }
  
  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(PosizioneGiuridicaCumuloModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdPosizioneGiuridicaCum() != null ) { 
      lCondizioni += " and ID_POSIZIONE_GIURIDICA_CUM = " + aModel.getIdPosizioneGiuridicaCum() + ""; 
    } 
    if (aModel.getCodPosizioneGiuridica() != null && aModel.getCodPosizioneGiuridica().length() > 0) { 
      lCondizioni += " and COD_POSIZIONE_GIURIDICA = '" + aModel.getCodPosizioneGiuridica() + "' "; 
    } 
    if (aModel.getDataInizio() != null ) { 
      lCondizioni += " and to_char(DATA_INIZIO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInizio(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getIstDetIdIstitutoDetenzione() != null && aModel.getIstDetIdIstitutoDetenzione().length() > 0) { 
      lCondizioni += " and IST_DET_ID_ISTITUTO_DETENZIONE = '" + aModel.getIstDetIdIstitutoDetenzione() + "' "; 
    } 
    if (aModel.getAltroLuogo() != null && aModel.getAltroLuogo().length() > 0) { 
      lCondizioni += " and ALTRO_LUOGO = '" + aModel.getAltroLuogo() + "' "; 
    } 
    if (aModel.getChiaveAnnoFasSius() != null ) { 
      lCondizioni += " and CHIAVE_ANNO_FAS_SIUS = " + aModel.getChiaveAnnoFasSius() + ""; 
    } 
    if (aModel.getChiaveProgrFasSius() != null ) { 
      lCondizioni += " and CHIAVE_PROGR_FAS_SIUS = " + aModel.getChiaveProgrFasSius() + ""; 
    } 
    if (aModel.getChiaveUffFasSius() != null && aModel.getChiaveUffFasSius().length() > 0) { 
      lCondizioni += " and CHIAVE_UFF_FAS_SIUS = '" + aModel.getChiaveUffFasSius() + "' "; 
    } 
    if (aModel.getAnnoRegistro() != null ) { 
      lCondizioni += " and ANNO_REGISTRO = " + aModel.getAnnoRegistro() + ""; 
    } 
    if (aModel.getNumeroRegistro() != null ) { 
      lCondizioni += " and NUMERO_REGISTRO = " + aModel.getNumeroRegistro() + ""; 
    } 
    if (aModel.getCodTipoProvvedimento() != null && aModel.getCodTipoProvvedimento().length() > 0) { 
      lCondizioni += " and COD_TIPO_PROVVEDIMENTO = '" + aModel.getCodTipoProvvedimento() + "' "; 
    }     
    if (aModel.getDataEmissioneProvv() != null ) { 
      lCondizioni += " and to_char(DATA_EMISSIONE_PROVV,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataEmissioneProvv(),"dd/MM/yyyy") + "' "; 
    }
    if (aModel.getNumAnniMisura() != null ) { 
      lCondizioni += " and NUM_ANNI_MISURA = " + aModel.getNumAnniMisura() + ""; 
    } 
    if (aModel.getNumMesiMisura() != null ) { 
      lCondizioni += " and NUM_MESI_MISURA = " + aModel.getNumMesiMisura() + ""; 
    } 
    if (aModel.getNumGiorniMisura() != null ) { 
      lCondizioni += " and NUM_GIORNI_MISURA = " + aModel.getNumGiorniMisura() + ""; 
    } 
    if (aModel.getDataFineMisura() != null ) { 
      lCondizioni += " and to_char(DATA_FINE_MISURA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataFineMisura(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDatIdDatiFinaliCumulo() != null ) { 
      lCondizioni += " and DAT_ID_DATI_FINALI_CUMULO = " + aModel.getDatIdDatiFinaliCumulo() + ""; 
    }
    if (aModel.getIstrIdIstruttoriaCumulo() != null ) { 
      lCondizioni += " and ISTR_ID_ISTRUTTORIA_CUMULO = " + aModel.getIstrIdIstruttoriaCumulo() + ""; 
    } 
    if (aModel.getTitIdTitoloCumulato() != null ) { 
        lCondizioni += " and TIT_ID_TITOLO_CUMULATO = " + aModel.getTitIdTitoloCumulato() + ""; 
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
  public void selCondizioneUpdate( BigDecimal aIdPosizioneGiuridicaCum) {
    String lCondizioni = new String();

    lCondizioni += " and ID_POSIZIONE_GIURIDICA_CUM = " + aIdPosizioneGiuridicaCum;
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni);
  }

  public void selCondizioneByIdTitolo( BigDecimal aIdTitolo) {
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
