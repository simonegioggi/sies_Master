package siap.siep.annotazioneesitotrasmissione.dao;

/**
* <p>Title: AnnotazioneEsitoTrasmissioneSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella AnnotazioneEsitoTrasmissione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class AnnotazioneEsitoTrasmissioneSqlDAO extends SqlDAO {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  
  /***************************************************************************** 
   * Costruttore  
   * @param con 
   ****************************************************************************/ 
  public AnnotazioneEsitoTrasmissioneSqlDAO (Connection con) {
    super(con);
  }


  /***************************************************************************** 
   * Restituisce il numero di record dell'operazione di ricerca costruendo 
   * la clausola where con lo stesso model utilizzato per la ricerca 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void getCountAnnotazioneEsitoTrasmissione(AnnotazioneEsitoTrasmissioneModel aModel) throws DAOException {
    // Costruisce lo statement da eseguire 
    String lStatement ="SELECT COUNT(*) HowManyRecords FROM ANNOTAZIONE_ESITO_TRASMISSIONE ";

    // Recupero la where condition in base al model 
    String lCondizioni = this.setCondizioni(aModel);

    if (!lCondizioni.trim().equals("")) 
      lStatement+=" WHERE " + lCondizioni;

    // Imposta lo statement da eseguire 
    setStatement(lStatement);
  }

  /***************************************************************************** 
   * Effettua la ricerca e restituisce solo i risultati nel range di record che 
   * vanno inseriti nella pagfina passata in input 
   * @param aModel 
   * @param aPage 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaAnnotazioneEsitoTrasmissionePaged(AnnotazioneEsitoTrasmissioneModel aModel, int aPage) throws DAOException { 
    String lStatement = new String(""); 

    lStatement += getSqlQuery(); 

    // Recupero la where condition in base al model 
    String lCondizioni = this.setCondizioni(aModel);

    if (!lCondizioni.trim().equals("")) 
      lStatement+=" WHERE " + lCondizioni;

    lStatement += " "+getOrderBy()+" "; 

    String lPaginedStatement = ""; 
    lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement + "  ) INNER ) WHERE rn between  " + ( (aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1) + 
          " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE; 

    setStatement(lPaginedStatement); 
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lPaginedStatement = "+lPaginedStatement); 
  } 


  /***************************************************************************** 
   * Effettua la generica ricerca in base ai dati specificati nel model 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaAnnotazioneEsitoTrasmissione( AnnotazioneEsitoTrasmissioneModel  aModel)  throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Recupero la where condition in base al model 
    String lCondizioni = setCondizioni(aModel);

    if (!lCondizioni.trim().equals("")) 
      lSql+=" AND " + lCondizioni;

    lSql += " "+getOrderBy()+" "; 

    // Imposta lo statement da eseguire 
    setStatement(lSql);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lSql = "+lSql); 
  }


  /***************************************************************************** 
   * Metodo che imposta la statement di ricerca per chiave 
   * @param aKey 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaAnnotazioneEsitoTrasmissioneByKey( BigDecimal aIdEsitoTrasmissione) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += setCondizioniByKey( aIdEsitoTrasmissione);

    // Imposta lo statement da eseguire 
    setStatement(lSql);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lSql = "+lSql); 
  }

  /**
   * 
   * @param aIdEvento
   * @throws DAOException
   */
  public void ricercaAnnotazioneEsitoTrasmissioneByIdEvento( BigDecimal aIdEvento) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // 
    lSql += " AND EVE_ID_EVENTO =  " + aIdEvento;

    // Imposta lo statement da eseguire 
    setStatement(lSql);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lSql = "+lSql); 
  }
  /***************************************************************************** 
   * Metodo per la costruzione della sql query 
   * @return 
   ****************************************************************************/ 
  protected String getSqlQuery() {
    String lStatement = new String("");

    lStatement += " SELECT " +
                  "ID_ESITO_TRASMISSIONE, "+  
                  "DATA_TRASMISSIONE, "+  
                  "OGGETTO_TRASMISSIONE, "+  
                  "COD_UFFICIO_DESTINATARIO, "+  
                  "COD_UFFICIO_INOLTRANTE, "+  
                  "COD_UFFICIO_INOLTRO, "+  
                  "COD_UFFICIO_ESITO, "+  
                  "DATA_ESITO, "+  
                  "COD_ESITO, "+  
                  "NOTE_ESITO, "+  
                  "CHIAVE_ANNO, "+  
                  "CHIAVE_PROGR, "+  
                  "CHIAVE_UFFICIO, "+  
                  "EVE_ID_EVENTO, "+  
                  "FAS_SIE_ID_FASCICOLO_SIEP, "+  
                  "MES_ID_MESSAGGIO_RICHIESTA, "+  
                  "MES_ID_MESSAGGIO_ESITO, "+  
                  "COD_OPERATORE_INSERIMENTO, "+  
                  "DATA_INSERIMENTO, "+  
                  "COD_UFFICIO_INSERIMENTO, "+  
                  "COD_OPERATORE_AGGIORNAMENTO, "+  
                  "DATA_AGGIORNAMENTO, "+  
                  "COD_UFFICIO_AGGIORNAMENTO "; 
    // aggiungere qui gli eventuali campi descrizioni 
    lStatement += " , oggettoTrasm.DESCRIZIONE descrOggetto ";
    lStatement += " , esitoTrasm.DESCRIZIONE descrEsito ";
    lStatement += " , cgDest.RV_MEANING   descrTipoUfficioDest      , comuneDest.DESCRIZIONE   descrComuneUfficioDest ";
    lStatement += " , cgInoltrante.RV_MEANING descrTipoUfficioInoltrante, comuneInoltrante.DESCRIZIONE descrComuneUfficioInoltrante ";
    lStatement += " , cgInoltro.RV_MEANING descrTipoUfficioInoltro, comuneInoltro.DESCRIZIONE descrComuneUfficioInoltro ";
    lStatement += " , cgEsito.RV_MEANING  descrTipoUfficioEsito     , comuneEsito.DESCRIZIONE  descrComuneUfficioEsito ";
    lStatement += " , cgChiave.RV_MEANING descrTipoUfficioChiave    , comuneChiave.DESCRIZIONE descrComuneUfficioChiave ";    
    // Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
    lStatement += " FROM ANNOTAZIONE_ESITO_TRASMISSIONE";
    lStatement += "      , JMS_CODE oggettoTrasm "; // Oggetto Trasmissine JMS_CODE.DOMINIO = 'TIPO_OPERAZIONE'
    lStatement += "      , JMS_CODE esitoTrasm ";   // Esito Trasmissine JMS_CODE.DOMINIO = 'CODICE_ESITO'  
    lStatement += "      , UFFICIO ufficioDest      , COMUNE comuneDest      , CG_REF_CODES cgDest ";
    lStatement += "      , UFFICIO ufficioInoltrante, COMUNE comuneInoltrante, CG_REF_CODES cgInoltrante ";
    lStatement += "      , UFFICIO ufficioInoltro   , COMUNE comuneInoltro   , CG_REF_CODES cgInoltro ";
    lStatement += "      , UFFICIO ufficioEsito     , COMUNE comuneEsito     , CG_REF_CODES cgEsito ";
    lStatement += "      , UFFICIO ufficioChiave    , COMUNE comuneChiave    , CG_REF_CODES cgChiave  ";
    lStatement += " WHERE 1=1 ";
    lStatement += "  AND (     nvl(ANNOTAZIONE_ESITO_TRASMISSIONE.OGGETTO_TRASMISSIONE,'-') = oggettoTrasm.CODICE AND oggettoTrasm.DOMINIO = 'TIPO_OPERAZIONE' ) " ;
    lStatement += "  AND (     nvl(ANNOTAZIONE_ESITO_TRASMISSIONE.COD_ESITO,'-') = esitoTrasm.CODICE AND esitoTrasm.DOMINIO = 'CODICE_ESITO') " ;
    //================= Ufficio Destinatario ======
    lStatement += "  AND (     ufficioDest.COD_UFFICIO = nvl(ANNOTAZIONE_ESITO_TRASMISSIONE.COD_UFFICIO_DESTINATARIO,'-') ) " ;
    lStatement += "  AND (     ufficioDest.COD_COMUNE = comuneDest.COD_COMUNE ) " ;
    lStatement += "  AND (     ufficioDest.COD_TIPO_UFFICIO = cgDest.RV_LOW_VALUE ) " ;
    lStatement += "  AND (     cgDest.RV_DOMAIN ='TIPO_UFFICIO' )" ;    
    //================= Ufficio Inoltrante ======
    lStatement += "  AND (     ufficioInoltrante.COD_UFFICIO = nvl(ANNOTAZIONE_ESITO_TRASMISSIONE.COD_UFFICIO_INOLTRANTE,'-')  ) " ;
    lStatement += "  AND (     ufficioInoltrante.COD_COMUNE = comuneInoltrante.COD_COMUNE  ) " ;
    lStatement += "  AND (     ufficioInoltrante.COD_TIPO_UFFICIO = cgInoltrante.RV_LOW_VALUE  ) " ;
    lStatement += "  AND (     cgInoltrante.RV_DOMAIN = 'TIPO_UFFICIO'  ) " ;
    //================= Ufficio Inoltro ======
    lStatement += "  AND (     ufficioInoltro.COD_UFFICIO = nvl(ANNOTAZIONE_ESITO_TRASMISSIONE.COD_UFFICIO_INOLTRO,'-')  ) " ;
    lStatement += "  AND (     ufficioInoltro.COD_COMUNE = comuneInoltro.COD_COMUNE  ) " ;
    lStatement += "  AND (     ufficioInoltro.COD_TIPO_UFFICIO = cgInoltro.RV_LOW_VALUE  ) " ;
    lStatement += "  AND (     cgInoltro.RV_DOMAIN = 'TIPO_UFFICIO'  ) " ;
    //================= Ufficio Esito ======
    lStatement += "  AND (     ufficioEsito.COD_UFFICIO = nvl(ANNOTAZIONE_ESITO_TRASMISSIONE.COD_UFFICIO_ESITO,'-')  ) " ;
    lStatement += "  AND (     ufficioEsito.COD_COMUNE = comuneEsito.COD_COMUNE  ) " ;
    lStatement += "  AND (     ufficioEsito.COD_TIPO_UFFICIO = cgEsito.RV_LOW_VALUE  ) " ;
    lStatement += "  AND (     cgEsito.RV_DOMAIN = 'TIPO_UFFICIO'  ) " ;    
    //================= Ufficio Chiave ======
    lStatement += "  AND (     ufficioChiave.COD_UFFICIO = nvl(ANNOTAZIONE_ESITO_TRASMISSIONE.CHIAVE_UFFICIO,'-') ) " ;
    lStatement += "  AND (     ufficioChiave.COD_COMUNE = comuneChiave.COD_COMUNE  ) " ;
    lStatement += "  AND (     ufficioChiave.COD_TIPO_UFFICIO = cgChiave.RV_LOW_VALUE  ) " ;
    lStatement += "  AND (     cgChiave.RV_DOMAIN = 'TIPO_UFFICIO'  ) " ;     
    
    return lStatement;
  }


  /***************************************************************************** 
   * Metodo che carica il record del result set nel model 
   * @return 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel  getModel() throws DAOException {
     AnnotazioneEsitoTrasmissioneModel aModel = new  AnnotazioneEsitoTrasmissioneModel(); 

    //Inserire le opportune set delle descrizioni!
    aModel.setIdEsitoTrasmissione       ( getBigDecimal ("ID_ESITO_TRASMISSIONE"      ) ); 
    aModel.setDataTrasmissione          ( getDate       ("DATA_TRASMISSIONE"          ) ); 
    aModel.setOggettoTrasmissione       ( getString     ("OGGETTO_TRASMISSIONE"       ) ); 
    aModel.setDescrOggettoTrasmissione  ( getString     ("descrOggetto"));
    aModel.setCodUfficioDestinatario    ( getString     ("COD_UFFICIO_DESTINATARIO"   ) ); 
    aModel.setDescrUfficioDestinatario  ( getString     ("descrTipoUfficioDest") );
    aModel.setCodUfficioInoltrante      ( getString     ("COD_UFFICIO_INOLTRANTE"     ) ); 
    aModel.setDescrUfficioInoltrante    ( getString     ("descrTipoUfficioInoltrante") );
    aModel.setCodUfficioInoltro         ( getString     ("COD_UFFICIO_INOLTRO"     ) ); 
    aModel.setDescrTipoUfficioInoltro   ( getString     ("descrTipoUfficioInoltro") );
    aModel.setDescrComuneUfficioInoltro ( getString     ("descrComuneUfficioInoltro") );    
    aModel.setCodUfficioEsito           ( getString     ("COD_UFFICIO_ESITO"          ) ); 
    aModel.setDescrTipoUfficioEsito     ( getString     ("descrTipoUfficioEsito") );
    aModel.setDescrComuneUfficioEsito   ( getString     ("descrComuneUfficioEsito") );
    aModel.setDataEsito                 ( getDate       ("DATA_ESITO"                 ) ); 
    aModel.setCodEsito                  ( getString     ("COD_ESITO"                  ) ); 
    aModel.setDescrEsito                ( getString     ("descrEsito") );
    aModel.setNoteEsito                 ( getString     ("NOTE_ESITO"                 ) ); 
    aModel.setChiaveAnno                ( getBigDecimal ("CHIAVE_ANNO"                ) ); 
    aModel.setChiaveProgr               ( getBigDecimal ("CHIAVE_PROGR"               ) ); 
    aModel.setChiaveUfficio             ( getString     ("CHIAVE_UFFICIO"             ) ); 
    aModel.setDescrChiaveUfficio        ( getString     ("descrTipoUfficioChiave") );        
    aModel.setEveIdEvento               ( getBigDecimal ("EVE_ID_EVENTO"              ) ); 
    aModel.setFasSieIdFascicoloSiep     ( getBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"  ) ); 
    aModel.setMesIdMessaggioRichiesta   ( getBigDecimal ("MES_ID_MESSAGGIO_RICHIESTA" ) ); 
    aModel.setMesIdMessaggioEsito       ( getBigDecimal ("MES_ID_MESSAGGIO_ESITO"     ) ); 
    aModel.setCodOperatoreInserimento   ( getString     ("COD_OPERATORE_INSERIMENTO"  ) ); 
    aModel.setDataInserimento           ( getDate       ("DATA_INSERIMENTO"           ) ); 
    aModel.setCodUfficioInserimento     ( getString     ("COD_UFFICIO_INSERIMENTO"    ) ); 
    aModel.setCodOperatoreAggiornamento ( getString     ("COD_OPERATORE_AGGIORNAMENTO") ); 
    aModel.setDataAggiornamento         ( getDate       ("DATA_AGGIORNAMENTO"         ) ); 
    aModel.setCodUfficioAggiornamento   ( getString     ("COD_UFFICIO_AGGIORNAMENTO"  ) ); 

    return aModel;
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public String setCondizioni(AnnotazioneEsitoTrasmissioneModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdEsitoTrasmissione() != null ) { 
      lCondizioni += " and ID_ESITO_TRASMISSIONE = " + aModel.getIdEsitoTrasmissione() + ""; 
    } 
    if (aModel.getDataTrasmissione() != null ) { 
      lCondizioni += " and to_char(DATA_TRASMISSIONE,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataTrasmissione(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getOggettoTrasmissione() != null && aModel.getOggettoTrasmissione().length() > 0) { 
      lCondizioni += " and OGGETTO_TRASMISSIONE = '" + aModel.getOggettoTrasmissione() + "' "; 
    } 
    if (aModel.getCodUfficioDestinatario() != null && aModel.getCodUfficioDestinatario().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_DESTINATARIO = '" + aModel.getCodUfficioDestinatario() + "' "; 
    } 
    if (aModel.getCodUfficioInoltrante() != null && aModel.getCodUfficioInoltrante().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_INOLTRANTE = '" + aModel.getCodUfficioInoltrante() + "' "; 
    } 
    if (aModel.getCodUfficioInoltro() != null && aModel.getCodUfficioInoltro().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_INOLTRO = '" + aModel.getCodUfficioInoltro() + "' "; 
    } 
    if (aModel.getCodUfficioEsito() != null && aModel.getCodUfficioEsito().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_ESITO = '" + aModel.getCodUfficioEsito() + "' "; 
    } 
    if (aModel.getDataEsito() != null ) { 
      lCondizioni += " and to_char(DATA_ESITO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataEsito(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getCodEsito() != null && aModel.getCodEsito().length() > 0) { 
      lCondizioni += " and COD_ESITO = '" + aModel.getCodEsito() + "' "; 
    } 
    if (aModel.getNoteEsito() != null && aModel.getNoteEsito().length() > 0) { 
      lCondizioni += " and NOTE_ESITO = '" + aModel.getNoteEsito() + "' "; 
    } 
    if (aModel.getChiaveAnno() != null ) { 
      lCondizioni += " and CHIAVE_ANNO = " + aModel.getChiaveAnno() + ""; 
    } 
    if (aModel.getChiaveProgr() != null ) { 
      lCondizioni += " and CHIAVE_PROGR = " + aModel.getChiaveProgr() + ""; 
    } 
    if (aModel.getChiaveUfficio() != null && aModel.getChiaveUfficio().length() > 0) { 
      lCondizioni += " and CHIAVE_UFFICIO = '" + aModel.getChiaveUfficio() + "' "; 
    } 
    if (aModel.getEveIdEvento() != null ) { 
      lCondizioni += " and EVE_ID_EVENTO = " + aModel.getEveIdEvento() + ""; 
    } 
    if (aModel.getFasSieIdFascicoloSiep() != null ) { 
      lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + ""; 
    } 
    if (aModel.getMesIdMessaggioRichiesta() != null ) { 
      lCondizioni += " and MES_ID_MESSAGGIO_RICHIESTA = " + aModel.getMesIdMessaggioRichiesta() + ""; 
    } 
    if (aModel.getMesIdMessaggioEsito() != null ) { 
      lCondizioni += " and MES_ID_MESSAGGIO_ESITO = " + aModel.getMesIdMessaggioEsito() + ""; 
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

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lCondizioni = "+lCondizioni); 
    return lCondizioni; 
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di select per chiave 
   * @param aKey 
   * @return 
   ****************************************************************************/ 
  public String setCondizioniByKey( BigDecimal aIdEsitoTrasmissione  ) {
    String lCondizioni = new String();

    lCondizioni += " and ID_ESITO_TRASMISSIONE = " + aIdEsitoTrasmissione;
    
    siesLogger.debug("lCondizioni = "+lCondizioni); 

    return lCondizioni;
  }


  /***************************************************************************** 
   * Metodo per la costruzione della sezione order by 
   * @return 
   ****************************************************************************/ 
  protected String getOrderBy() { 
    String orderBy = new String(""); 
    //orderBy = " ORDER BY "; 
    return orderBy; 
  } 
}