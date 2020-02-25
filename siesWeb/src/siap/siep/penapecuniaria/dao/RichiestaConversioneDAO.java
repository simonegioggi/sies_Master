package siap.siep.penapecuniaria.dao;

/**
* <p>Title: RichiestaConversioneDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella RichiestaConversione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class RichiestaConversioneDAO extends SIAPTableDAO 
{ 
  
  /*****************************************************************************
   * Costruttore della classe: imposta il nome della tabella e i campi
   * @param con connessione
   ****************************************************************************/
  public RichiestaConversioneDAO (Connection con) 
  {
    super(con);
    setTable("RICHIESTA_CONVERSIONE");

    //Settare la Sequence e i campi chiave e commentare il setField del campo chiave
    setSequenceField("ID_RICHIESTA_CONVERSIONE","RIC_CON_SEQ");

    //setField("ID_RICHIESTA_CONVERSIONE", BIG_DECIMAL);
    setField("ANNO_PARTITA"                 , BIG_DECIMAL);
    setField("NUM_PARTITA"                  , BIG_DECIMAL);
    setField("NUM_EX_CAMPIONE"              , STRING);
    setField("PROT_CIRCOSRIZIONE_DOGANALE"  , STRING);
    setField("COD_TIPO_AUTORITA_EMITTENTE"  , STRING);
    setField("COD_LUOGO_EMITTENTE"          , STRING);
    setField("DATA_RICEZIONE_ATTO"          , DATE);
    setField("DATA_ISCRIZIONE_ATTO"         , DATE);
    setField("DATA_ESAZIONE"                , DATE);
    setField("IMPORTO_MULTA"                , BIG_DECIMAL);
    setField("DATA_PRESCRIZIONE_MULTA"      , DATE);
    setField("FLAG_IMPRESCRITTIBILE_MULTA"  , STRING);
    setField("IMPORTO_AMMENDA"              , BIG_DECIMAL);
    setField("DATA_PRESCRIZIONE_AMMENDA"    , DATE);
    setField("FLAG_IMPRESCRITTIBILE_AMMENDA", STRING);
    setField("FAS_SIE_ID_FASCICOLO_SIEP"    , BIG_DECIMAL);
    setField("EVE_ID_EVENTO"                , BIG_DECIMAL);
    setField("COD_OPERATORE_INSERIMENTO"    , STRING);
    setField("DATA_INSERIMENTO"             , DATE);
    setField("COD_UFFICIO_INSERIMENTO"      , STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO"  , STRING);
    setField("DATA_AGGIORNAMENTO"           , DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO"    , STRING);
    setField("FAS_SIU_ID_FASCICOLO_SIUS"    , BIG_DECIMAL);
    setField("DURATA_ESITO_ANNI"    				, BIG_DECIMAL);
    setField("DURATA_ESITO_MESI"    				, BIG_DECIMAL);
    setField("DURATA_ESITO_GIORNI"   				, BIG_DECIMAL);
    setField("NUMERO_RATE"			    				, BIG_DECIMAL);
    setField("VALORE_RATA"			    				, BIG_DECIMAL);
    setField("VALORE_ULTIMA_RATA"    				, BIG_DECIMAL);
    setField("DATA_ANNULLAMENTO"            , DATE);
    setField("COD_TIPO_SANZIONE"    				, STRING);
    setField("NOTE"    											, STRING);
    setField("DATA_DEPOSITO"			          , DATE);
    setField("DATA_INIZIO_PAGAMENTO"        , DATE);
    setField("NUMERO_GIORNI_INIZIO_PAGAMENTO"  , BIG_DECIMAL);

  }


  //============================================================================ 
  // Metodi get utilizzati per recuperare i dati dal result set 
  //============================================================================ 
  public  BigDecimal  getIdRichiestaConversione()       throws DAOException  { return getBigDecimal ("ID_RICHIESTA_CONVERSIONE"     ); } 
  public  BigDecimal  getAnnoPartita()                  throws DAOException  { return getBigDecimal ("ANNO_PARTITA"                 ); } 
  public  BigDecimal  getNumPartita()                   throws DAOException  { return getBigDecimal ("NUM_PARTITA"                  ); } 
  public  String      getNumExCampione()                throws DAOException  { return getString     ("NUM_EX_CAMPIONE"              ); } 
  public  String      getProtCircosrizioneDoganale()    throws DAOException  { return getString     ("PROT_CIRCOSRIZIONE_DOGANALE"  ); } 
  public  String      getCodTipoAutoritaEmittente()     throws DAOException  { return getString     ("COD_TIPO_AUTORITA_EMITTENTE"  ); } 
  public  String      getCodLuogoEmittente()            throws DAOException  { return getString     ("COD_LUOGO_EMITTENTE"          ); } 
  public  Date        getDataRicezioneAtto()            throws DAOException  { return getDate       ("DATA_RICEZIONE_ATTO"          ); } 
  public  Date        getDataIscrizioneAtto()           throws DAOException  { return getDate       ("DATA_ISCRIZIONE_ATTO"         ); } 
  public  Date        getDataEsazione()                 throws DAOException  { return getDate       ("DATA_ESAZIONE"                ); } 
  public  BigDecimal  getImportoMulta()                 throws DAOException  { return getBigDecimal ("IMPORTO_MULTA"                ); } 
  public  Date        getDataPrescrizioneMulta()        throws DAOException  { return getDate       ("DATA_PRESCRIZIONE_MULTA"      ); } 
  public  String      getFlagImprescrittibileMulta()    throws DAOException  { return getString     ("FLAG_IMPRESCRITTIBILE_MULTA"  ); } 
  public  BigDecimal  getImportoAmmenda()               throws DAOException  { return getBigDecimal ("IMPORTO_AMMENDA"              ); } 
  public  Date        getDataPrescrizioneAmmenda()      throws DAOException  { return getDate       ("DATA_PRESCRIZIONE_AMMENDA"    ); } 
  public  String      getFlagImprescrittibileAmmenda()  throws DAOException  { return getString     ("FLAG_IMPRESCRITTIBILE_AMMENDA"); } 
  public  BigDecimal  getFasSieIdFascicoloSiep()        throws DAOException  { return getBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"    ); } 
  public  BigDecimal  getEveIdEvento()                  throws DAOException  { return getBigDecimal ("EVE_ID_EVENTO"                ); } 
  public  String      getCodOperatoreInserimento()      throws DAOException  { return getString     ("COD_OPERATORE_INSERIMENTO"    ); } 
  public  Date        getDataInserimento()              throws DAOException  { return getDate       ("DATA_INSERIMENTO"             ); } 
  public  String      getCodUfficioInserimento()        throws DAOException  { return getString     ("COD_UFFICIO_INSERIMENTO"      ); } 
  public  String      getCodOperatoreAggiornamento()    throws DAOException  { return getString     ("COD_OPERATORE_AGGIORNAMENTO"  ); } 
  public  Date        getDataAggiornamento()            throws DAOException  { return getDate       ("DATA_AGGIORNAMENTO"           ); } 
  public  String      getCodUfficioAggiornamento()      throws DAOException  { return getString     ("COD_UFFICIO_AGGIORNAMENTO"    ); } 
  public  BigDecimal  getFasSiuIdFascicoloSius()        throws DAOException  { return getBigDecimal ("FAS_SIU_ID_FASCICOLO_SIUS"    ); } 
  public  BigDecimal  getDurataEsitoAnni()			        throws DAOException  { return getBigDecimal ("DURATA_ESITO_ANNI"    				); } 
  public  BigDecimal  getDurataEsitoMesi()			        throws DAOException  { return getBigDecimal ("DURATA_ESITO_MESI"    				); } 
  public  BigDecimal  getDurataEsitoGiorni()		        throws DAOException  { return getBigDecimal ("DURATA_ESITO_GIORNI"   				); } 
  public  BigDecimal  getNumeroRate()						        throws DAOException  { return getBigDecimal ("NUMERO_RATE"				   				); } 
  public  BigDecimal  getValoreRata()						        throws DAOException  { return getBigDecimal ("VALORE_RATA"				   				); } 
  public  BigDecimal  getValoreUltimaRata()			        throws DAOException  { return getBigDecimal ("VALORE_ULTIMA_RATA"	   				); } 
  public  Date        getDataAnnullamento()			        throws DAOException  { return getDate 			("DATA_ANNULLAMENTO"	   				); } 
  public  String	    getCodTipoSanzione()			        throws DAOException  { return getString			("COD_TIPO_SANZIONE"	   				); } 
  public  String	    getNote()			        						throws DAOException  { return getString			("NOTE"								   				); } 
  public  Date        getDataDeposito()					        throws DAOException  { return getDate 			("DATA_DEPOSITO"			   				); } 
  public  Date        getDataInizioPagamento()	        throws DAOException  { return getDate 			("DATA_INIZIO_PAGAMENTO" 				); } 
  public  BigDecimal  getNumGGInizioPagamento()	        throws DAOException  { return getBigDecimal ("NUMERO_GIORNI_INIZIO_PAGAMENTO"	); } 

  //============================================================================ 
  // Metodi set utilizzati per impostare i campi delle query  
  //============================================================================ 
  public void  setIdRichiestaConversione       (BigDecimal  aValore )   { setBigDecimal ("ID_RICHIESTA_CONVERSIONE"     , aValore); } 
  public void  setAnnoPartita                  (BigDecimal  aValore )   { setBigDecimal ("ANNO_PARTITA"                 , aValore); } 
  public void  setNumPartita                   (BigDecimal  aValore )   { setBigDecimal ("NUM_PARTITA"                  , aValore); } 
  public void  setNumExCampione                (String      aValore )   { setString     ("NUM_EX_CAMPIONE"              , aValore); } 
  public void  setProtCircosrizioneDoganale    (String      aValore )   { setString     ("PROT_CIRCOSRIZIONE_DOGANALE"  , aValore); } 
  public void  setCodTipoAutoritaEmittente     (String      aValore )   { setString     ("COD_TIPO_AUTORITA_EMITTENTE"  , aValore); } 
  public void  setCodLuogoEmittente            (String      aValore )   { setString     ("COD_LUOGO_EMITTENTE"          , aValore); } 
  public void  setDataRicezioneAtto            (Date        aValore )   { setDate       ("DATA_RICEZIONE_ATTO"          , aValore); } 
  public void  setDataIscrizioneAtto           (Date        aValore )   { setDate       ("DATA_ISCRIZIONE_ATTO"         , aValore); } 
  public void  setDataEsazione                 (Date        aValore )   { setDate       ("DATA_ESAZIONE"                , aValore); } 
  public void  setImportoMulta                 (BigDecimal  aValore )   { setBigDecimal ("IMPORTO_MULTA"                , aValore); } 
  public void  setDataPrescrizioneMulta        (Date        aValore )   { setDate       ("DATA_PRESCRIZIONE_MULTA"      , aValore); } 
  public void  setFlagImprescrittibileMulta    (String      aValore )   { setString     ("FLAG_IMPRESCRITTIBILE_MULTA"  , aValore); } 
  public void  setImportoAmmenda               (BigDecimal  aValore )   { setBigDecimal ("IMPORTO_AMMENDA"              , aValore); } 
  public void  setDataPrescrizioneAmmenda      (Date        aValore )   { setDate       ("DATA_PRESCRIZIONE_AMMENDA"    , aValore); } 
  public void  setFlagImprescrittibileAmmenda  (String      aValore )   { setString     ("FLAG_IMPRESCRITTIBILE_AMMENDA", aValore); } 
  public void  setFasSieIdFascicoloSiep        (BigDecimal  aValore )   { setBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"    , aValore); } 
  public void  setEveIdEvento                  (BigDecimal  aValore )   { setBigDecimal ("EVE_ID_EVENTO"                , aValore); } 
  public void  setCodOperatoreInserimento      (String      aValore )   { setString     ("COD_OPERATORE_INSERIMENTO"    , aValore); } 
  public void  setDataInserimento              (Date        aValore )   { setDate       ("DATA_INSERIMENTO"             , aValore); } 
  public void  setCodUfficioInserimento        (String      aValore )   { setString     ("COD_UFFICIO_INSERIMENTO"      , aValore); } 
  public void  setCodOperatoreAggiornamento    (String      aValore )   { setString     ("COD_OPERATORE_AGGIORNAMENTO"  , aValore); } 
  public void  setDataAggiornamento            (Date        aValore )   { setDate       ("DATA_AGGIORNAMENTO"           , aValore); } 
  public void  setCodUfficioAggiornamento      (String      aValore )   { setString     ("COD_UFFICIO_AGGIORNAMENTO"    , aValore); } 
  public void  setFasSiuIdFascicoloSius				 (BigDecimal  aValore )		{setBigDecimal 	("FAS_SIU_ID_FASCICOLO_SIUS"    , aValore); }
  public void  setDurataEsitoAnni							 (BigDecimal  aValore )		{setBigDecimal 	("DURATA_ESITO_ANNI"    				, aValore); }
  public void  setDurataEsitoMesi							 (BigDecimal  aValore )		{setBigDecimal 	("DURATA_ESITO_MESI"    				, aValore); }
  public void  setDurataEsitoGiorni						 (BigDecimal  aValore )		{setBigDecimal 	("DURATA_ESITO_GIORNI"   				, aValore); }
  public void  setNumeroRate									 (BigDecimal  aValore )		{setBigDecimal 	("NUMERO_RATE"				   				, aValore); }
  public void  setValoreRata									 (BigDecimal  aValore )		{setBigDecimal 	("VALORE_RATA"				   				, aValore); }
  public void  setValoreUltimaRata						 (BigDecimal  aValore )		{setBigDecimal 	("VALORE_ULTIMA_RATA"	   				, aValore); }
  public void  setDataAnnullamento						 (Date 				aValore )		{setDate 				("DATA_ANNULLAMENTO"	   				, aValore); }
  public void  setCodTipoSanzione							 (String			aValore )		{setString			("COD_TIPO_SANZIONE"	   				, aValore); }
  public void  setNote							 					 (String			aValore )		{setString			("NOTE"	   											, aValore); }
  public void  setDataDeposito			 					 (Date 				aValore )		{setDate				("DATA_DEPOSITO"								, aValore); }
  public void  setDataInizioPagamento					 (Date 				aValore )		{setDate				("DATA_INIZIO_PAGAMENTO"				, aValore); }
  public void  setNumGGInizioPagamento				 (BigDecimal	aValore )		{setBigDecimal	("NUMERO_GIORNI_INIZIO_PAGAMENTO"	, aValore); }
  
  /***************************************************************************** 
   * Metodo che recupera i dati della select e carica il model in output 
   * @return il model 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel getModel() throws DAOException 
  { 
    return new RichiestaConversioneModel(  
      getIdRichiestaConversione() , 
      getAnnoPartita() , 
      getNumPartita() , 
      getNumExCampione() , 
      getProtCircosrizioneDoganale() , 
      getCodTipoAutoritaEmittente() , 
      "",
      getCodLuogoEmittente() , 
      "",
      getDataRicezioneAtto() , 
      getDataIscrizioneAtto() , 
      getDataEsazione() , 
      getImportoMulta() , 
      getDataPrescrizioneMulta() , 
      getFlagImprescrittibileMulta() , 
      getImportoAmmenda() , 
      getDataPrescrizioneAmmenda() , 
      getFlagImprescrittibileAmmenda() , 
      getFasSieIdFascicoloSiep() , 
      getEveIdEvento() , 
      getCodOperatoreInserimento() , 
      getDataInserimento() , 
      getCodUfficioInserimento() , 
      "",
      getCodOperatoreAggiornamento() , 
      getDataAggiornamento() , 
      getCodUfficioAggiornamento(),
      getFasSiuIdFascicoloSius(),
      getDurataEsitoAnni(),
      getDurataEsitoMesi(),
      getDurataEsitoGiorni(),
      getNumeroRate(),
      getValoreRata(),
      getValoreUltimaRata(),
      getDataAnnullamento(),
      getCodTipoSanzione(),
      "",
      getNote(),
      getDataDeposito(),
      getDataInizioPagamento(),
      getNumGGInizioPagamento()
    );
  }


  /** 
   * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a 
   * partire dal contenuto del model passato in input. 
   * @param aModel 
   * @throws DAOException 
   */ 
  public void setDAOFromModel(RichiestaConversioneModel aModel) throws DAOException {
    setIdRichiestaConversione       ( aModel.getIdRichiestaConversione()      );  
    setAnnoPartita                  ( aModel.getAnnoPartita()                 );  
    setNumPartita                   ( aModel.getNumPartita()                  );  
    setNumExCampione                ( aModel.getNumExCampione()               );  
    setProtCircosrizioneDoganale    ( aModel.getProtCircosrizioneDoganale()   );  
    setCodTipoAutoritaEmittente     ( aModel.getCodTipoAutoritaEmittente()    );  
    setCodLuogoEmittente            ( aModel.getCodLuogoEmittente()           );  
    setDataRicezioneAtto            ( aModel.getDataRicezioneAtto()           );  
    setDataIscrizioneAtto           ( aModel.getDataIscrizioneAtto()          );  
    setDataEsazione                 ( aModel.getDataEsazione()                );  
    setImportoMulta                 ( aModel.getImportoMulta()                );  
    setDataPrescrizioneMulta        ( aModel.getDataPrescrizioneMulta()       );  
    setFlagImprescrittibileMulta    ( aModel.getFlagImprescrittibileMulta()   );  
    setImportoAmmenda               ( aModel.getImportoAmmenda()              );  
    setDataPrescrizioneAmmenda      ( aModel.getDataPrescrizioneAmmenda()     );  
    setFlagImprescrittibileAmmenda  ( aModel.getFlagImprescrittibileAmmenda() );  
    setFasSieIdFascicoloSiep        ( aModel.getFasSieIdFascicoloSiep()       );  
    setEveIdEvento                  ( aModel.getEveIdEvento()                 );  
    setCodOperatoreInserimento      ( aModel.getCodOperatoreInserimento()     );  
    setDataInserimento              ( aModel.getDataInserimento()             );  
    setCodUfficioInserimento        ( aModel.getCodUfficioInserimento()       );  
    setCodOperatoreAggiornamento    ( aModel.getCodOperatoreAggiornamento()   );  
    setDataAggiornamento            ( aModel.getDataAggiornamento()           );  
    setCodUfficioAggiornamento      ( aModel.getCodUfficioAggiornamento()     );
    setFasSiuIdFascicoloSius        ( aModel.getFasSiuIdFascicoloSius()       );  
    setDurataEsitoAnni			        ( aModel.getDurataEsitoAnni()				      );  
    setDurataEsitoMesi			        ( aModel.getDurataEsitoMesi()				      );  
    setDurataEsitoGiorni		        ( aModel.getDurataEsitoGiorni()			      );  
    setNumeroRate						        ( aModel.getNumeroRate()						      );  
    setValoreRata						        ( aModel.getValoreRata()						      );  
    setValoreUltimaRata			        ( aModel.getValoreUltimaRata()			      );  
    setDataAnnullamento            	( aModel.getDataAnnullamento()	          );  
    setCodTipoSanzione            	( aModel.getCodTipoSanzione() 	          );  
    setNote						            	( aModel.getNote()						 	          );  
    setDataDeposito		            	( aModel.getDataDeposito()		 	          );  
    setDataInizioPagamento         	( aModel.getDataInizioPagamento()         );  
    setNumGGInizioPagamento					( aModel.getNumeroGiorniInizioPagamento() );  

  }

  /** 
   * Metodo che imposta i campi delle operazioni di Aggiornamento x Emissione Ordinanza 
   * @param aModel 
   * @throws DAOException 
   */ 
  public void setDAOFromModelForUpdate(RichiestaConversioneModel aModel) throws DAOException {
  	if ( aModel.getIdRichiestaConversione() != null  )
  		setIdRichiestaConversione       ( aModel.getIdRichiestaConversione()      );  
  	if ( aModel.getEveIdEvento() != null  )
  		setEveIdEvento                  ( aModel.getEveIdEvento()                 );  
  	if ( aModel.getCodOperatoreAggiornamento() != null  && aModel.getCodOperatoreAggiornamento().length() > 1  )
  		setCodOperatoreAggiornamento    ( aModel.getCodOperatoreAggiornamento()   );  
  	if ( aModel.getDataAggiornamento() != null  )
  		setDataAggiornamento            ( aModel.getDataAggiornamento()           );  
   	if ( aModel.getCodUfficioAggiornamento() != null  && aModel.getCodUfficioAggiornamento().length() > 1  )
  		setCodUfficioAggiornamento      ( aModel.getCodUfficioAggiornamento()     );
  	if ( aModel.getDurataEsitoAnni() != null  )
  		setDurataEsitoAnni			        ( aModel.getDurataEsitoAnni()				      );  
  	if ( aModel.getDurataEsitoMesi() != null  )
  		setDurataEsitoMesi			        ( aModel.getDurataEsitoMesi()				      );  
  	if ( aModel.getDurataEsitoGiorni() != null  )
  		setDurataEsitoGiorni		        ( aModel.getDurataEsitoGiorni()			      );  
  	if ( aModel.getNumeroRate() != null  )
  		setNumeroRate						        ( aModel.getNumeroRate()						      );  
  	if ( aModel.getValoreRata() != null  )
  		setValoreRata						        ( aModel.getValoreRata()						      );  
  	if ( aModel.getValoreUltimaRata() != null  )
  		setValoreUltimaRata			        ( aModel.getValoreUltimaRata()			      );  
   	if ( aModel.getCodTipoSanzione() != null  && aModel.getCodTipoSanzione().length() > 1  )
  		setCodTipoSanzione            	( aModel.getCodTipoSanzione() 	          );  
  	if ( aModel.getDataDeposito() != null  )
  		setDataDeposito		            	( aModel.getDataDeposito()		 	          );  
  	if ( aModel.getDataInizioPagamento() != null  )
  		setDataInizioPagamento         	( aModel.getDataInizioPagamento()         );  
  	if ( aModel.getNumeroGiorniInizioPagamento() != null  )
  		setNumGGInizioPagamento					( aModel.getNumeroGiorniInizioPagamento() );  
  }
  
  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public void setCondizioni(RichiestaConversioneModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdRichiestaConversione() != null ) { 
      lCondizioni += " and ID_RICHIESTA_CONVERSIONE = " + aModel.getIdRichiestaConversione() + ""; 
    } 
    if (aModel.getAnnoPartita() != null ) { 
      lCondizioni += " and ANNO_PARTITA = " + aModel.getAnnoPartita() + ""; 
    } 
    if (aModel.getNumPartita() != null ) { 
      lCondizioni += " and NUM_PARTITA = " + aModel.getNumPartita() + ""; 
    } 
    if (aModel.getNumExCampione() != null && aModel.getNumExCampione().length() > 0) { 
      lCondizioni += " and NUM_EX_CAMPIONE = '" + aModel.getNumExCampione() + "' "; 
    } 
    if (aModel.getProtCircosrizioneDoganale() != null && aModel.getProtCircosrizioneDoganale().length() > 0) { 
      lCondizioni += " and PROT_CIRCOSRIZIONE_DOGANALE = '" + aModel.getProtCircosrizioneDoganale() + "' "; 
    } 
    if (aModel.getCodTipoAutoritaEmittente() != null && aModel.getCodTipoAutoritaEmittente().length() > 0) { 
      lCondizioni += " and COD_TIPO_AUTORITA_EMITTENTE = '" + aModel.getCodTipoAutoritaEmittente() + "' "; 
    } 
    if (aModel.getCodLuogoEmittente() != null && aModel.getCodLuogoEmittente().length() > 0) { 
      lCondizioni += " and COD_LUOGO_EMITTENTE = '" + aModel.getCodLuogoEmittente() + "' "; 
    } 
    if (aModel.getDataRicezioneAtto() != null ) { 
      lCondizioni += " and to_char(DATA_RICEZIONE_ATTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataRicezioneAtto(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataIscrizioneAtto() != null ) { 
      lCondizioni += " and to_char(DATA_ISCRIZIONE_ATTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataIscrizioneAtto(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataEsazione() != null ) { 
      lCondizioni += " and to_char(DATA_ESAZIONE,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataEsazione(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getImportoMulta() != null ) { 
      lCondizioni += " and IMPORTO_MULTA = " + aModel.getImportoMulta() + ""; 
    } 
    if (aModel.getDataPrescrizioneMulta() != null ) { 
      lCondizioni += " and to_char(DATA_PRESCRIZIONE_MULTA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataPrescrizioneMulta(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getFlagImprescrittibileMulta() != null && aModel.getFlagImprescrittibileMulta().length() > 0) { 
      lCondizioni += " and FLAG_IMPRESCRITTIBILE_MULTA = '" + aModel.getFlagImprescrittibileMulta() + "' "; 
    } 
    if (aModel.getImportoAmmenda() != null ) { 
      lCondizioni += " and IMPORTO_AMMENDA = " + aModel.getImportoAmmenda() + ""; 
    } 
    if (aModel.getDataPrescrizioneAmmenda() != null ) { 
      lCondizioni += " and to_char(DATA_PRESCRIZIONE_AMMENDA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataPrescrizioneAmmenda(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getFlagImprescrittibileAmmenda() != null && aModel.getFlagImprescrittibileAmmenda().length() > 0) { 
      lCondizioni += " and FLAG_IMPRESCRITTIBILE_AMMENDA = '" + aModel.getFlagImprescrittibileAmmenda() + "' "; 
    } 
    if (aModel.getFasSieIdFascicoloSiep() != null ) { 
      lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + ""; 
    } 
    if (aModel.getEveIdEvento() != null ) { 
      lCondizioni += " and EVE_ID_EVENTO = " + aModel.getEveIdEvento() + ""; 
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
    if (aModel.getFasSiuIdFascicoloSius() != null ) { 
      lCondizioni += " and FAS_SIU_ID_FASCICOLO_SIUS = " + aModel.getFasSiuIdFascicoloSius() + ""; 
    } 
    if (aModel.getDurataEsitoAnni() != null ) { 
      lCondizioni += " and DURATA_ESITO_ANNI = " + aModel.getDurataEsitoAnni() + ""; 
    } 
    if (aModel.getDurataEsitoMesi() != null ) { 
      lCondizioni += " and DURATA_ESITO_MESI = " + aModel.getDurataEsitoMesi() + ""; 
    } 
    if (aModel.getDurataEsitoGiorni() != null ) { 
      lCondizioni += " and DURATA_ESITO_GIORNI = " + aModel.getDurataEsitoGiorni() + ""; 
    } 
    if (aModel.getNumeroRate() != null ) { 
      lCondizioni += " and NUMERO_RATE = " + aModel.getNumeroRate() + ""; 
    } 
    if (aModel.getValoreRata() != null ) { 
      lCondizioni += " and VALORE_RATA = " + aModel.getValoreRata() + ""; 
    } 
    if (aModel.getValoreUltimaRata() != null ) { 
      lCondizioni += " and VALORE_ULTIMA_RATA = " + aModel.getValoreUltimaRata() + ""; 
    } 
    if (aModel.getDataAnnullamento() != null ) { 
      lCondizioni += " and to_char(DATA_ANNULLAMENTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataAnnullamento(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getCodTipoSanzione() != null && aModel.getCodTipoSanzione().length() > 1) { 
      lCondizioni += " and COD_TIPO_SANZIONE = '" + aModel.getCodTipoSanzione() + "' "; 
    } 

    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    setCondition(lCondizioni); 
  }


  /** 
   * Imposta la condizione di where per l'operazione di update puntuale 
   * si entra sempre in chiave 
   * @param key 
   */ 
  public void selCondizioneUpdate( BigDecimal aIdRichiestaConversione) {
    String lCondizioni = new String();

    lCondizioni += " and ID_RICHIESTA_CONVERSIONE = " + aIdRichiestaConversione;
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 
    setCondition(lCondizioni);
  }

  /** 
   * Imposta la condizione where per FAS_SIU_ID_FASCICOLO_SIUS. 
   * @param aIdFasSius 
   */ 
  public void selCondizioneByIdFasSius( BigDecimal aIdFasSius) {
    String lCondizioni = new String();

    lCondizioni += " FAS_SIU_ID_FASCICOLO_SIUS = " + aIdFasSius;

    setCondition(lCondizioni);
  }

  /** 
   * Imposta la condizione where per FAS_SIE_ID_FASCICOLO_SIEP. 
   * @param aIdFasSius 
   */ 
  public void selCondizioneByIdFasSiep( BigDecimal aIdFasSiep) {
    String lCondizioni = new String();

    lCondizioni += " FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFasSiep;

    setCondition(lCondizioni);
  }

  /** 
   * Imposta la condizione where per FAS_SIU_ID_FASCICOLO_SIUS ed EVE_ID_EVENTO. 
   * @param aIdFasSius 
   * @param aIdEvento 
   */ 
  public void selCondizioneByIdFasSiusIdEvento( BigDecimal aIdFasSius, BigDecimal aIdEvento) {
    String lCondizioni = new String();

    lCondizioni += " FAS_SIU_ID_FASCICOLO_SIUS = " + aIdFasSius;
    lCondizioni += " and EVE_ID_EVENTO = " + aIdEvento;

    setCondition(lCondizioni);
  }
  
  /** 
   * Imposta la condizione where per EVE_ID_EVENTO. 
   * @param aIdFasSius 
   */ 
  public void selCondizioneByIdEvento( BigDecimal aIdEvento) {
    String lCondizioni = new String();

    lCondizioni += " EVE_ID_EVENTO = " + aIdEvento;

    setCondition(lCondizioni);
  }

  /**
   * Il metodo prepara l'Update da effettuare a seguito di Cancellazione Ordinanza di Conversione Pena Pecuniaria.
   * @param aModel
   * @throws DAOException
   */
  public void setDAOFromModelForCancOrdinanzaCPP(RichiestaConversioneModel aModel) throws DAOException
  {
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    // MEV27 Richiesta_conversione non va sganciato dall'evento
    //setEveIdEvento(null);
    setDurataEsitoAnni(null);
    setDurataEsitoMesi(null);
    setDurataEsitoGiorni(null);
    setNumeroRate(null);
    setValoreRata(null);
    setValoreUltimaRata(null);
    setDataAnnullamento(null);
    setCodTipoSanzione("-");
    setDataDeposito(null);
    setDataInizioPagamento(null);
    setNumGGInizioPagamento(null);
    
  }
  
  /**
   * Il metodo prepara l'Update da effettuare a seguito di Deposito Ordinanza di Conversione Pena Pecuniaria.
   * @param aModel
   * @throws DAOException
   */
  public void setDAOFromModelForDepOrdinanzaCPP(RichiestaConversioneModel aModel) throws DAOException
  {
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataDeposito(aModel.getDataDeposito());
  }

  /**
   * Il metodo prepara l'Update da effettuare a seguito di Deposito Decreto di NDP/NLP in contesti di Conversione Pena Pecuniaria.
   * @param aModel
   * @throws DAOException
   */
  public void setDAOFromModelForDepDecretoCPP(RichiestaConversioneModel aModel) throws DAOException
  {
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataDeposito(aModel.getDataDeposito());
    setEveIdEvento(aModel.getEveIdEvento());
  }
  
  /** 
   * Imposta la condizione di order by per la ricerca  
   *  
   */ 
  public void setOrderBy() 
  { 
    String orderBy = ""; 
    //======================================================================= 
    // Lasciare orderBy="" se non si vuole scegliere un ordinamento, 
    // altrimenti elencare i campi separati da virgola 
    // n.b. non inserire la clausola ORDER BY, viene aggiunta automaticamente 
    //======================================================================= 

    setOrder(orderBy); 
  } 
}
