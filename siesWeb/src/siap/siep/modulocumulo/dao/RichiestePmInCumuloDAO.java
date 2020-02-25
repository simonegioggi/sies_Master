package siap.siep.modulocumulo.dao;

/**
* <p>Title: RichiestePmInCumuloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella RichiestePmInCumulo</p>
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

import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;

public class RichiestePmInCumuloDAO extends TableDAO { 
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public RichiestePmInCumuloDAO (Connection con) {
    super(con);
    setTable("RICHIESTE_PM_IN_CUMULO");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_RICHIESTE_PM_IN_CUMULO","RICHIESTE_PM_IN_CUMULO_SEQ");

    //setField("ID_RICHIESTE_PM_IN_CUMULO", BIG_DECIMAL);
    setField("COD_TIPO_RICHIESTA"           , STRING);
    setField("COD_TIPO_ANNOTAZIONE"         , STRING);
    setField("DATA_EMISSIONE"               , DATE);
    setField("FLAG_PIU_MENO_R"              , STRING);
    setField("NUM_ANNI_RECLUSIONE_R"        , BIG_DECIMAL);
    setField("NUM_MESI_RECLUSIONE_R"        , BIG_DECIMAL);
    setField("NUM_GIORNI_RECLUSIONE_R"      , BIG_DECIMAL);
    setField("IMPORTO_MULTA_R"              , BIG_DECIMAL);
    setField("NUM_ANNI_ARRESTO_R"           , BIG_DECIMAL);
    setField("NUM_MESI_ARRESTO_R"           , BIG_DECIMAL);
    setField("NUM_GIORNI_ARRESTO_R"         , BIG_DECIMAL);
    setField("IMPORTO_AMMENDA_R"            , BIG_DECIMAL);
    setField("FLAG_APP_PROVVISORIA"         , STRING);
    setField("COD_TIPO_PENA_ACCESSORIA"     , STRING);
    setField("COD_TIPO_DURATA_PA"           , STRING);
    setField("NUM_ANNI_PA"                  , BIG_DECIMAL);
    setField("NUM_MESI_PA"                  , BIG_DECIMAL);
    setField("NUM_GIORNI_PA"                , BIG_DECIMAL);
    setField("COD_FONTE"                    , STRING);
    setField("ANNO_FONTE"                   , BIG_DECIMAL);
    setField("NUMERO_FONTE"                 , STRING);
    setField("ARTICOLO"                     , STRING);
    setField("COD_SOTTONUMERAZIONE"         , STRING);
    setField("COMMA"                        , STRING);
    setField("LETTERA"                      , STRING);
    setField("NUMERO"                       , STRING);
    setField("ANNO_CC"                      , BIG_DECIMAL);
    setField("NUMERO_CC"                    , STRING);
    setField("DATA_CC"                      , DATE);
    setField("COD_DPR"                      , STRING);
    setField("MOTIVAZIONI"                  , STRING);
    setField("NOTE_RECLUSIONE"              , STRING);
    
    setField("NUM_GIORNI_LA_REV"	        , BIG_DECIMAL);
    setField("NUM_GIORNI_LS_REV"          , BIG_DECIMAL);
    setField("NUM_GIORNI_LI_REV"          , BIG_DECIMAL);
    setField("COD_MOTIVO"					, STRING);
    setField("TIPO_ANNOTAZIONE_BENEFICIO"	, STRING);
    
    setField("ISTR_ID_ISTRUTTORIA_CUMULO"   , BIG_DECIMAL);
    setField("TIT_ID_TITOLO_CUMULATO"       , BIG_DECIMAL);
    setField("TIT_ID_TITOLO_CUMULATO_REF"   , BIG_DECIMAL);
    setField("RIC_ID_RICHIESTE_INVIATE_CUM" , BIG_DECIMAL);
    
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
  public  BigDecimal  getIdRichiestePmInCumulo()      throws DAOException  { return getBigDecimal ("ID_RICHIESTE_PM_IN_CUMULO"    ); } 
  public  String      getCodTipoRichiesta()           throws DAOException  { return getString     ("COD_TIPO_RICHIESTA"           ); } 
  public  String      getCodTipoAnnotazione()         throws DAOException  { return getString     ("COD_TIPO_ANNOTAZIONE"         ); } 
  public  Date        getDataEmissione()              throws DAOException  { return getDate       ("DATA_EMISSIONE"               ); } 
  public  String      getFlagPiuMenoR()               throws DAOException  { return getString     ("FLAG_PIU_MENO_R"              ); } 
  public  BigDecimal  getNumAnniReclusioneR()         throws DAOException  { return getBigDecimal ("NUM_ANNI_RECLUSIONE_R"        ); } 
  public  BigDecimal  getNumMesiReclusioneR()         throws DAOException  { return getBigDecimal ("NUM_MESI_RECLUSIONE_R"        ); } 
  public  BigDecimal  getNumGiorniReclusioneR()       throws DAOException  { return getBigDecimal ("NUM_GIORNI_RECLUSIONE_R"      ); } 
  public  BigDecimal  getImportoMultaR()              throws DAOException  { return getBigDecimal ("IMPORTO_MULTA_R"              ); } 
  public  BigDecimal  getNumAnniArrestoR()            throws DAOException  { return getBigDecimal ("NUM_ANNI_ARRESTO_R"           ); } 
  public  BigDecimal  getNumMesiArrestoR()            throws DAOException  { return getBigDecimal ("NUM_MESI_ARRESTO_R"           ); } 
  public  BigDecimal  getNumGiorniArrestoR()          throws DAOException  { return getBigDecimal ("NUM_GIORNI_ARRESTO_R"         ); } 
  public  BigDecimal  getImportoAmmendaR()            throws DAOException  { return getBigDecimal ("IMPORTO_AMMENDA_R"            ); } 
  public  String      getFlagAppProvvisoria()         throws DAOException  { return getString     ("FLAG_APP_PROVVISORIA"         ); } 
  public  String      getCodTipoPenaAccessoria()      throws DAOException  { return getString     ("COD_TIPO_PENA_ACCESSORIA"     ); } 
  public  String      getCodTipoDurataPa()            throws DAOException  { return getString     ("COD_TIPO_DURATA_PA"           ); } 
  public  BigDecimal  getNumAnniPa()                  throws DAOException  { return getBigDecimal ("NUM_ANNI_PA"                  ); } 
  public  BigDecimal  getNumMesiPa()                  throws DAOException  { return getBigDecimal ("NUM_MESI_PA"                  ); } 
  public  BigDecimal  getNumGiorniPa()                throws DAOException  { return getBigDecimal ("NUM_GIORNI_PA"                ); } 
  public  String      getCodFonte()                   throws DAOException  { return getString     ("COD_FONTE"                    ); } 
  public  BigDecimal  getAnnoFonte()                  throws DAOException  { return getBigDecimal ("ANNO_FONTE"                   ); } 
  public  String      getNumeroFonte()                throws DAOException  { return getString     ("NUMERO_FONTE"                 ); } 
  public  String      getArticolo()                   throws DAOException  { return getString     ("ARTICOLO"                     ); } 
  public  String      getCodSottonumerazione()        throws DAOException  { return getString     ("COD_SOTTONUMERAZIONE"         ); } 
  public  String      getComma()                      throws DAOException  { return getString     ("COMMA"                        ); } 
  public  String      getLettera()                    throws DAOException  { return getString     ("LETTERA"                      ); } 
  public  String      getNumero()                     throws DAOException  { return getString     ("NUMERO"                       ); } 
  public  BigDecimal  getAnnoCc()                     throws DAOException  { return getBigDecimal ("ANNO_CC"                      ); } 
  public  String      getNumeroCc()                   throws DAOException  { return getString     ("NUMERO_CC"                    ); } 
  public  Date        getDataCc()                     throws DAOException  { return getDate       ("DATA_CC"                      ); }
  
  public  String      getCodTipoBeneficio()           throws DAOException  { return getString     ("TIPO_ANNOTAZIONE_BENEFICIO"   ); }
  public  String      getCodDpr()                     throws DAOException  { return getString     ("COD_DPR"                      ); } 
  public  String      getMotivazioni()                throws DAOException  { return getString     ("MOTIVAZIONI"                  ); } 
  public  String      getNoteReclusione()             throws DAOException  { return getString     ("NOTE_RECLUSIONE"              ); } 
  
  public  String      getCodMotivo()               	  throws DAOException  { return getString     ("COD_MOTIVO"		              ); } 
  
  public  BigDecimal  getIstrIdIstruttoriaCumulo()    throws DAOException  { return getBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO"   ); } 
  public  BigDecimal  getTitIdTitoloCumulato()        throws DAOException  { return getBigDecimal ("TIT_ID_TITOLO_CUMULATO"       ); } 
  public  BigDecimal  getTitIdTitoloCumulatoRef()     throws DAOException  { return getBigDecimal ("TIT_ID_TITOLO_CUMULATO_REF"   ); } 

  public  String      getCodOperatoreInserimento()    throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"    ); } 
  public  Date        getDataInserimento()            throws DAOException  { return getDate       ("DATA_INSERIMENTO"             ); } 
  public  String      getCodUfficioInserimento()      throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"      ); } 
  public  String      getCodOperatoreAggiornamento()  throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"  ); } 
  public  Date        getDataAggiornamento()          throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"           ); } 
  public  String      getCodUfficioAggiornamento()    throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"    ); }

  public  BigDecimal  getRicIdRichiesteInviateCum()   throws DAOException  { return getBigDecimal ("RIC_ID_RICHIESTE_INVIATE_CUM" ); } 
  public  BigDecimal  getNumGiorniRevocaLA()   		  throws DAOException  { return getBigDecimal ("NUM_GIORNI_LA_REV" 			  ); } 
  public  BigDecimal  getNumGiorniRevocaLS()        throws DAOException  { return getBigDecimal ("NUM_GIORNI_LS_REV"        ); } 
  public  BigDecimal  getNumGiorniRevocaLI()        throws DAOException  { return getBigDecimal ("NUM_GIORNI_LI_REV"        ); } 


  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdRichiestePmInCumulo      (BigDecimal  aValore )   { setBigDecimal ("ID_RICHIESTE_PM_IN_CUMULO"    , aValore); } 
  public void  setCodTipoRichiesta           (String      aValore )   { setString     ("COD_TIPO_RICHIESTA"           , aValore); } 
  public void  setCodTipoAnnotazione         (String      aValore )   { setString     ("COD_TIPO_ANNOTAZIONE"         , aValore); } 
  public void  setDataEmissione              (Date        aValore )   { setDate       ("DATA_EMISSIONE"               , aValore); } 
  public void  setFlagPiuMenoR               (String      aValore )   { setString     ("FLAG_PIU_MENO_R"              , aValore); } 
  public void  setNumAnniReclusioneR         (BigDecimal  aValore )   { setBigDecimal ("NUM_ANNI_RECLUSIONE_R"        , aValore); } 
  public void  setNumMesiReclusioneR         (BigDecimal  aValore )   { setBigDecimal ("NUM_MESI_RECLUSIONE_R"        , aValore); } 
  public void  setNumGiorniReclusioneR       (BigDecimal  aValore )   { setBigDecimal ("NUM_GIORNI_RECLUSIONE_R"      , aValore); } 
  public void  setImportoMultaR              (BigDecimal  aValore )   { setBigDecimal ("IMPORTO_MULTA_R"              , aValore); } 
  public void  setNumAnniArrestoR            (BigDecimal  aValore )   { setBigDecimal ("NUM_ANNI_ARRESTO_R"           , aValore); } 
  public void  setNumMesiArrestoR            (BigDecimal  aValore )   { setBigDecimal ("NUM_MESI_ARRESTO_R"           , aValore); } 
  public void  setNumGiorniArrestoR          (BigDecimal  aValore )   { setBigDecimal ("NUM_GIORNI_ARRESTO_R"         , aValore); } 
  public void  setImportoAmmendaR            (BigDecimal  aValore )   { setBigDecimal ("IMPORTO_AMMENDA_R"            , aValore); } 
  public void  setFlagAppProvvisoria         (String      aValore )   { setString     ("FLAG_APP_PROVVISORIA"         , aValore); } 
  public void  setCodTipoPenaAccessoria      (String      aValore )   { setString     ("COD_TIPO_PENA_ACCESSORIA"     , aValore); } 
  public void  setCodTipoDurataPa            (String      aValore )   { setString     ("COD_TIPO_DURATA_PA"           , aValore); } 
  public void  setNumAnniPa                  (BigDecimal  aValore )   { setBigDecimal ("NUM_ANNI_PA"                  , aValore); } 
  public void  setNumMesiPa                  (BigDecimal  aValore )   { setBigDecimal ("NUM_MESI_PA"                  , aValore); } 
  public void  setNumGiorniPa                (BigDecimal  aValore )   { setBigDecimal ("NUM_GIORNI_PA"                , aValore); } 
  public void  setCodFonte                   (String      aValore )   { setString     ("COD_FONTE"                    , aValore); } 
  public void  setAnnoFonte                  (BigDecimal  aValore )   { setBigDecimal ("ANNO_FONTE"                   , aValore); } 
  public void  setNumeroFonte                (String      aValore )   { setString     ("NUMERO_FONTE"                 , aValore); } 
  public void  setArticolo                   (String      aValore )   { setString     ("ARTICOLO"                     , aValore); } 
  public void  setCodSottonumerazione        (String      aValore )   { setString     ("COD_SOTTONUMERAZIONE"         , aValore); } 
  public void  setComma                      (String      aValore )   { setString     ("COMMA"                        , aValore); } 
  public void  setLettera                    (String      aValore )   { setString     ("LETTERA"                      , aValore); } 
  public void  setNumero                     (String      aValore )   { setString     ("NUMERO"                       , aValore); } 
  public void  setAnnoCc                     (BigDecimal  aValore )   { setBigDecimal ("ANNO_CC"                      , aValore); } 
  public void  setNumeroCc                   (String      aValore )   { setString     ("NUMERO_CC"                    , aValore); } 
  public void  setDataCc                     (Date        aValore )   { setDate       ("DATA_CC"                      , aValore); } 
  
  public void  setCodTipoBeneficio			 (String	  aValore )	  { setString 	  ("TIPO_ANNOTAZIONE_BENEFICIO"	  , aValore); }	
  public void  setCodDpr                     (String      aValore )   { setString     ("COD_DPR"                      , aValore); } 
  public void  setMotivazioni                (String      aValore )   { setString     ("MOTIVAZIONI"                  , aValore); } 
  public void  setNoteReclusione             (String      aValore )   { setString     ("NOTE_RECLUSIONE"              , aValore); } 

  public void  setCodMotivo                  (String      aValore )   { setString     ("COD_MOTIVO"	                  , aValore); }
  
  public void  setIstrIdIstruttoriaCumulo    (BigDecimal  aValore )   { setBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO"   , aValore); } 
  public void  setTitIdTitoloCumulato        (BigDecimal  aValore )   { setBigDecimal ("TIT_ID_TITOLO_CUMULATO"       , aValore); } 
  public void  setTitIdTitoloCumulatoRef     (BigDecimal  aValore )   { setBigDecimal ("TIT_ID_TITOLO_CUMULATO_REF"   , aValore); } 
  
  public void  setCodOperatoreInserimento    (String      aValore )   { setString     ("COD_OPERATORE_INSERIMENTO"    , aValore); } 
  public void  setDataInserimento            (Date        aValore )   { setDate       ("DATA_INSERIMENTO"             , aValore); } 
  public void  setCodUfficioInserimento      (String      aValore )   { setString     ("COD_UFFICIO_INSERIMENTO"      , aValore); } 
  public void  setCodOperatoreAggiornamento  (String      aValore )   { setString     ("COD_OPERATORE_AGGIORNAMENTO"  , aValore); } 
  public void  setDataAggiornamento          (Date        aValore )   { setDate       ("DATA_AGGIORNAMENTO"           , aValore); } 
  public void  setCodUfficioAggiornamento    (String      aValore )   { setString     ("COD_UFFICIO_AGGIORNAMENTO"    , aValore); } 
  
  public void  setRicIdRichiesteInviateCum   (BigDecimal  aValore )	  { setBigDecimal ("RIC_ID_RICHIESTE_INVIATE_CUM" , aValore); }
  public void  setNumGiorniRevocaLA			 (BigDecimal  aValore )	  { setBigDecimal ("NUM_GIORNI_LA_REV" 			  , aValore); }
  public void  setNumGiorniRevocaLS      (BigDecimal  aValore )   { setBigDecimal ("NUM_GIORNI_LS_REV"        , aValore); }
  public void  setNumGiorniRevocaLI      (BigDecimal  aValore )   { setBigDecimal ("NUM_GIORNI_LI_REV"        , aValore); }

  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException { 
    return new RichiestePmInCumuloModel(  
      getIdRichiestePmInCumulo() , 
      getCodTipoRichiesta() , 
      "",
      getCodTipoAnnotazione() , 
      "",
      getDataEmissione() , 
      getFlagPiuMenoR() , 
      getNumAnniReclusioneR() , 
      getNumMesiReclusioneR() , 
      getNumGiorniReclusioneR() , 
      getImportoMultaR() , 
      getNumAnniArrestoR() , 
      getNumMesiArrestoR() , 
      getNumGiorniArrestoR() , 
      getImportoAmmendaR() , 
      getFlagAppProvvisoria() , 
      getCodTipoPenaAccessoria() , 
      "",
      getCodTipoDurataPa() , 
      "",
      getNumAnniPa() , 
      getNumMesiPa() , 
      getNumGiorniPa() , 
      getCodFonte() , 
      "",
      "",
      getAnnoFonte() , 
      getNumeroFonte() , 
      getArticolo() , 
      getCodSottonumerazione() , 
      "",
      getComma() , 
      getLettera() , 
      getNumero() , 
      getAnnoCc() , 
      getNumeroCc() , 
      getDataCc() ,
      
      getCodTipoBeneficio() ,
      "",
      getCodDpr() , 
      "",
      getMotivazioni() , 
      getNoteReclusione() , 

      getNumGiorniRevocaLA(),
      getNumGiorniRevocaLS(),
      getNumGiorniRevocaLI(),
      
      getCodMotivo(),
      
      getIstrIdIstruttoriaCumulo() , 
      getTitIdTitoloCumulato() , 
      getTitIdTitoloCumulatoRef() , 
      getCodOperatoreInserimento() , 
      getDataInserimento() , 
      getCodUfficioInserimento() , 
      getCodOperatoreAggiornamento() , 
      getDataAggiornamento() , 
      getCodUfficioAggiornamento(),
      getRicIdRichiesteInviateCum()
    );
  }


  /***************************************************************************** 
   *  Metodo specifico per effattuare l'aggiornamento dei solo campi aggiornabili
   * a partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void setDAOFromModelForUpdate(RichiestePmInCumuloModel aModel) throws DAOException {
 //   setIdRichiestePmInCumulo      ( aModel.getIdRichiestePmInCumulo()     );  
    setCodTipoRichiesta           ( aModel.getCodTipoRichiesta()          );  
    setCodTipoAnnotazione         ( aModel.getCodTipoAnnotazione()        );  
    setDataEmissione              ( aModel.getDataEmissione()             );  
    setFlagPiuMenoR               ( aModel.getFlagPiuMenoR()              );  
    setNumAnniReclusioneR         ( aModel.getNumAnniReclusioneR()        );  
    setNumMesiReclusioneR         ( aModel.getNumMesiReclusioneR()        );  
    setNumGiorniReclusioneR       ( aModel.getNumGiorniReclusioneR()      );  
    setImportoMultaR              ( aModel.getImportoMultaR()             );  
    setNumAnniArrestoR            ( aModel.getNumAnniArrestoR()           );  
    setNumMesiArrestoR            ( aModel.getNumMesiArrestoR()           );  
    setNumGiorniArrestoR          ( aModel.getNumGiorniArrestoR()         );  
    setImportoAmmendaR            ( aModel.getImportoAmmendaR()           );  
    setFlagAppProvvisoria         ( aModel.getFlagAppProvvisoria()        );  
    setCodTipoPenaAccessoria      ( aModel.getCodTipoPenaAccessoria()     );  
    setCodTipoDurataPa            ( aModel.getCodTipoDurataPa()           );  
    setNumAnniPa                  ( aModel.getNumAnniPa()                 );  
    setNumMesiPa                  ( aModel.getNumMesiPa()                 );  
    setNumGiorniPa                ( aModel.getNumGiorniPa()               );  
    setCodFonte                   ( aModel.getCodFonte()                  );  
    setAnnoFonte                  ( aModel.getAnnoFonte()                 );  
    setNumeroFonte                ( aModel.getNumeroFonte()               );  
    setArticolo                   ( aModel.getArticolo()                  );  
    setCodSottonumerazione        ( aModel.getCodSottonumerazione()       );  
    setComma                      ( aModel.getComma()                     );  
    setLettera                    ( aModel.getLettera()                   );  
    setNumero                     ( aModel.getNumero()                    );  
    setAnnoCc                     ( aModel.getAnnoCc()                    );  
    setNumeroCc                   ( aModel.getNumeroCc()                  );  
    setDataCc                     ( aModel.getDataCc()                    ); 
    setCodTipoBeneficio			  ( aModel.getCodTipoBeneficio()		  );
    setCodDpr                     ( aModel.getCodDpr()                    );  
    setMotivazioni                ( aModel.getMotivazioni()               );  
    setNoteReclusione             ( aModel.getNoteReclusione()            );  
   
    setRicIdRichiesteInviateCum	  ( aModel.getRicIdRichiesteInviateCum()  );
    setNumGiorniRevocaLA		  ( aModel.getNumGiorniRevocaLA() 		  );
    setNumGiorniRevocaLS      ( aModel.getNumGiorniRevocaLS()       );
    setNumGiorniRevocaLI      ( aModel.getNumGiorniRevocaLI()       );
    setCodMotivo				  ( aModel.getCodMotivo()				  );
    
//    setIstrIdIstruttoriaCumulo    ( aModel.getIstrIdIstruttoriaCumulo()   );  
    setTitIdTitoloCumulato        ( aModel.getTitIdTitoloCumulato()       );  
    	setTitIdTitoloCumulatoRef     ( aModel.getTitIdTitoloCumulatoRef()    );  
    
 //   setCodOperatoreInserimento    ( aModel.getCodOperatoreInserimento()   );  
 //   setDataInserimento            ( aModel.getDataInserimento()           );  
 //   setCodUfficioInserimento      ( aModel.getCodUfficioInserimento()     );  
    
    setCodOperatoreAggiornamento  ( aModel.getCodOperatoreAggiornamento() );  
    setDataAggiornamento          ( aModel.getDataAggiornamento()         );  
    setCodUfficioAggiornamento    ( aModel.getCodUfficioAggiornamento()   );  
  }
  
  /***************************************************************************** 
   * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void setDAOFromModel(RichiestePmInCumuloModel aModel) throws DAOException {
    setIdRichiestePmInCumulo      ( aModel.getIdRichiestePmInCumulo()     );  
    setCodTipoRichiesta           ( aModel.getCodTipoRichiesta()          );  
    setCodTipoAnnotazione         ( aModel.getCodTipoAnnotazione()        );  
    setDataEmissione              ( aModel.getDataEmissione()             );  
    setFlagPiuMenoR               ( aModel.getFlagPiuMenoR()              );  
    setNumAnniReclusioneR         ( aModel.getNumAnniReclusioneR()        );  
    setNumMesiReclusioneR         ( aModel.getNumMesiReclusioneR()        );  
    setNumGiorniReclusioneR       ( aModel.getNumGiorniReclusioneR()      );  
    setImportoMultaR              ( aModel.getImportoMultaR()             );  
    setNumAnniArrestoR            ( aModel.getNumAnniArrestoR()           );  
    setNumMesiArrestoR            ( aModel.getNumMesiArrestoR()           );  
    setNumGiorniArrestoR          ( aModel.getNumGiorniArrestoR()         );  
    setImportoAmmendaR            ( aModel.getImportoAmmendaR()           );  
    setFlagAppProvvisoria         ( aModel.getFlagAppProvvisoria()        );  
    setCodTipoPenaAccessoria      ( aModel.getCodTipoPenaAccessoria()     );  
    setCodTipoDurataPa            ( aModel.getCodTipoDurataPa()           );  
    setNumAnniPa                  ( aModel.getNumAnniPa()                 );  
    setNumMesiPa                  ( aModel.getNumMesiPa()                 );  
    setNumGiorniPa                ( aModel.getNumGiorniPa()               );  
    setCodFonte                   ( aModel.getCodFonte()                  );  
    setAnnoFonte                  ( aModel.getAnnoFonte()                 );  
    setNumeroFonte                ( aModel.getNumeroFonte()               );  
    setArticolo                   ( aModel.getArticolo()                  );  
    setCodSottonumerazione        ( aModel.getCodSottonumerazione()       );  
    setComma                      ( aModel.getComma()                     );  
    setLettera                    ( aModel.getLettera()                   );  
    setNumero                     ( aModel.getNumero()                    );  
    setAnnoCc                     ( aModel.getAnnoCc()                    );  
    setNumeroCc                   ( aModel.getNumeroCc()                  );  
    setDataCc                     ( aModel.getDataCc()                    );
    setCodTipoBeneficio			  ( aModel.getCodTipoBeneficio()		  );
    setCodDpr                     ( aModel.getCodDpr()                    );  
    setMotivazioni                ( aModel.getMotivazioni()               );  
    setNoteReclusione             ( aModel.getNoteReclusione()            );  

    setIstrIdIstruttoriaCumulo    ( aModel.getIstrIdIstruttoriaCumulo()   );  
    setTitIdTitoloCumulato        ( aModel.getTitIdTitoloCumulato()       );  
    setTitIdTitoloCumulatoRef     ( aModel.getTitIdTitoloCumulatoRef()    );  
    setRicIdRichiesteInviateCum	  ( aModel.getRicIdRichiesteInviateCum()  );
    setNumGiorniRevocaLA          ( aModel.getNumGiorniRevocaLA()   	  );
    setNumGiorniRevocaLS          ( aModel.getNumGiorniRevocaLS()       );
    setNumGiorniRevocaLI          ( aModel.getNumGiorniRevocaLI()       );
    setCodMotivo                  ( aModel.getCodMotivo()				  );
    
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
  public void setCondizioni(RichiestePmInCumuloModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdRichiestePmInCumulo() != null ) { 
      lCondizioni += " and ID_RICHIESTE_PM_IN_CUMULO = " + aModel.getIdRichiestePmInCumulo() + ""; 
    } 
    if (aModel.getCodTipoRichiesta() != null && aModel.getCodTipoRichiesta().length() > 0) { 
      lCondizioni += " and COD_TIPO_RICHIESTA = '" + aModel.getCodTipoRichiesta() + "' "; 
    } 
    if (aModel.getCodTipoAnnotazione() != null && aModel.getCodTipoAnnotazione().length() > 0) { 
      lCondizioni += " and COD_TIPO_ANNOTAZIONE = '" + aModel.getCodTipoAnnotazione() + "' "; 
    } 
    if (aModel.getDataEmissione() != null ) { 
      lCondizioni += " and to_char(DATA_EMISSIONE,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataEmissione(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getFlagPiuMenoR() != null && aModel.getFlagPiuMenoR().length() > 0) { 
      lCondizioni += " and FLAG_PIU_MENO_R = '" + aModel.getFlagPiuMenoR() + "' "; 
    } 
    if (aModel.getNumAnniReclusioneR() != null ) { 
      lCondizioni += " and NUM_ANNI_RECLUSIONE_R = " + aModel.getNumAnniReclusioneR() + ""; 
    } 
    if (aModel.getNumMesiReclusioneR() != null ) { 
      lCondizioni += " and NUM_MESI_RECLUSIONE_R = " + aModel.getNumMesiReclusioneR() + ""; 
    } 
    if (aModel.getNumGiorniReclusioneR() != null ) { 
      lCondizioni += " and NUM_GIORNI_RECLUSIONE_R = " + aModel.getNumGiorniReclusioneR() + ""; 
    } 
    if (aModel.getImportoMultaR() != null ) { 
      lCondizioni += " and IMPORTO_MULTA_R = " + aModel.getImportoMultaR() + ""; 
    } 
    if (aModel.getNumAnniArrestoR() != null ) { 
      lCondizioni += " and NUM_ANNI_ARRESTO_R = " + aModel.getNumAnniArrestoR() + ""; 
    } 
    if (aModel.getNumMesiArrestoR() != null ) { 
      lCondizioni += " and NUM_MESI_ARRESTO_R = " + aModel.getNumMesiArrestoR() + ""; 
    } 
    if (aModel.getNumGiorniArrestoR() != null ) { 
      lCondizioni += " and NUM_GIORNI_ARRESTO_R = " + aModel.getNumGiorniArrestoR() + ""; 
    } 
    if (aModel.getImportoAmmendaR() != null ) { 
      lCondizioni += " and IMPORTO_AMMENDA_R = " + aModel.getImportoAmmendaR() + ""; 
    } 
    if (aModel.getFlagAppProvvisoria() != null && aModel.getFlagAppProvvisoria().length() > 0) { 
      lCondizioni += " and FLAG_APP_PROVVISORIA = '" + aModel.getFlagAppProvvisoria() + "' "; 
    } 
    if (aModel.getCodTipoPenaAccessoria() != null && aModel.getCodTipoPenaAccessoria().length() > 0) { 
      lCondizioni += " and COD_TIPO_PENA_ACCESSORIA = '" + aModel.getCodTipoPenaAccessoria() + "' "; 
    } 
    if (aModel.getCodTipoDurataPa() != null && aModel.getCodTipoDurataPa().length() > 0) { 
      lCondizioni += " and COD_TIPO_DURATA_PA = '" + aModel.getCodTipoDurataPa() + "' "; 
    } 
    if (aModel.getNumAnniPa() != null ) { 
      lCondizioni += " and NUM_ANNI_PA = " + aModel.getNumAnniPa() + ""; 
    } 
    if (aModel.getNumMesiPa() != null ) { 
      lCondizioni += " and NUM_MESI_PA = " + aModel.getNumMesiPa() + ""; 
    } 
    if (aModel.getNumGiorniPa() != null ) { 
      lCondizioni += " and NUM_GIORNI_PA = " + aModel.getNumGiorniPa() + ""; 
    } 
    if (aModel.getCodFonte() != null && aModel.getCodFonte().length() > 0) { 
      lCondizioni += " and COD_FONTE = '" + aModel.getCodFonte() + "' "; 
    } 
    if (aModel.getAnnoFonte() != null ) { 
      lCondizioni += " and ANNO_FONTE = " + aModel.getAnnoFonte() + ""; 
    } 
    if (aModel.getNumeroFonte() != null && aModel.getNumeroFonte().length() > 0) { 
      lCondizioni += " and NUMERO_FONTE = '" + aModel.getNumeroFonte() + "' "; 
    } 
    if (aModel.getArticolo() != null && aModel.getArticolo().length() > 0) { 
      lCondizioni += " and ARTICOLO = '" + aModel.getArticolo() + "' "; 
    } 
    if (aModel.getCodSottonumerazione() != null && aModel.getCodSottonumerazione().length() > 0) { 
      lCondizioni += " and COD_SOTTONUMERAZIONE = '" + aModel.getCodSottonumerazione() + "' "; 
    } 
    if (aModel.getComma() != null && aModel.getComma().length() > 0) { 
      lCondizioni += " and COMMA = '" + aModel.getComma() + "' "; 
    } 
    if (aModel.getLettera() != null && aModel.getLettera().length() > 0) { 
      lCondizioni += " and LETTERA = '" + aModel.getLettera() + "' "; 
    } 
    if (aModel.getNumero() != null && aModel.getNumero().length() > 0) { 
      lCondizioni += " and NUMERO = '" + aModel.getNumero() + "' "; 
    } 
    if (aModel.getAnnoCc() != null ) { 
      lCondizioni += " and ANNO_CC = " + aModel.getAnnoCc() + ""; 
    } 
    if (aModel.getNumeroCc() != null && aModel.getNumeroCc().length() > 0) { 
      lCondizioni += " and NUMERO_CC = '" + aModel.getNumeroCc() + "' "; 
    } 
    if (aModel.getDataCc() != null ) { 
      lCondizioni += " and to_char(DATA_CC,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataCc(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getCodDpr() != null && aModel.getCodDpr().length() > 0) { 
      lCondizioni += " and COD_DPR = '" + aModel.getCodDpr() + "' "; 
    } 
    if (aModel.getMotivazioni() != null && aModel.getMotivazioni().length() > 0) { 
      lCondizioni += " and MOTIVAZIONI = '" + aModel.getMotivazioni() + "' "; 
    } 
    if (aModel.getNoteReclusione() != null && aModel.getNoteReclusione().length() > 0) { 
      lCondizioni += " and NOTE_RECLUSIONE = '" + aModel.getNoteReclusione() + "' "; 
    } 
    if (aModel.getIstrIdIstruttoriaCumulo() != null ) { 
      lCondizioni += " and ISTR_ID_ISTRUTTORIA_CUMULO = " + aModel.getIstrIdIstruttoriaCumulo() + ""; 
    } 
    if (aModel.getTitIdTitoloCumulato() != null ) { 
      lCondizioni += " and TIT_ID_TITOLO_CUMULATO = " + aModel.getTitIdTitoloCumulato() + ""; 
    } 
    if (aModel.getTitIdTitoloCumulatoRef() != null ) { 
      lCondizioni += " and TIT_ID_TITOLO_CUMULATO_REF = " + aModel.getTitIdTitoloCumulatoRef() + ""; 
    } 
    if (aModel.getNumGiorniRevocaLA() != null ) { 
        lCondizioni += " and NUM_GIORNI_LA_REV = " + aModel.getNumGiorniRevocaLA() + ""; 
    } 
    if (aModel.getNumGiorniRevocaLS() != null ) { 
      lCondizioni += " and NUM_GIORNI_LS_REV = " + aModel.getNumGiorniRevocaLS() + ""; 
    } 
    if (aModel.getNumGiorniRevocaLI() != null ) { 
      lCondizioni += " and NUM_GIORNI_LI_REV = " + aModel.getNumGiorniRevocaLI() + ""; 
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
  public void selCondizioneUpdate( BigDecimal aIdRichiestePmInCumulo) {
    String lCondizioni = new String();

    lCondizioni += " and ID_RICHIESTE_PM_IN_CUMULO = " + aIdRichiestePmInCumulo;
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
