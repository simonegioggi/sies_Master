package siap.siep.sollecitoesitotrasmissione.dao;

/**
* <p>Title: SollecitoEsitoTrasmissioneSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella SollecitoEsitoTrasmissione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.siep.sollecitoesitotrasmissione.model.SollecitoEsitoTrasmissioneModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class SollecitoEsitoTrasmissioneSqlDAO extends SqlDAO {
  Logger logger = Logger.getLogger("sqldaoLogger");
  /***************************************************************************** 
   * Costruttore  
   * @param con 
   ****************************************************************************/ 
  public SollecitoEsitoTrasmissioneSqlDAO (Connection con) {
    super(con);
  }


  /***************************************************************************** 
   * Restituisce il numero di record dell'operazione di ricerca costruendo 
   * la clausola where con lo stesso model utilizzato per la ricerca 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void getCountSollecitoEsitoTrasmissione(SollecitoEsitoTrasmissioneModel aModel) throws DAOException {
    // Costruisce lo statement da eseguire 
    String lStatement ="SELECT COUNT(*) HowManyRecords FROM SOLLECITO_ESITO_TRASMISSIONE ";

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
  public void ricercaSollecitoEsitoTrasmissionePaged(SollecitoEsitoTrasmissioneModel aModel, int aPage) throws DAOException { 
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
    logger.info("lPaginedStatement = "+lPaginedStatement); 
  } 


  /***************************************************************************** 
   * Effettua la generica ricerca in base ai dati specificati nel model 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaSollecitoEsitoTrasmissione( SollecitoEsitoTrasmissioneModel  aModel)  throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Recupero la where condition in base al model 
    String lCondizioni = setCondizioni(aModel);

    if (!lCondizioni.trim().equals("")) 
      lSql+=" WHERE " + lCondizioni;

    lSql += " "+getOrderBy()+" "; 

    // Imposta lo statement da eseguire 
    setStatement(lSql);
    logger.info("lSql = "+lSql); 
  }


  /***************************************************************************** 
   * Metodo che imposta la statement di ricerca per chiave 
   * @param aKey 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaSollecitoEsitoTrasmissioneByKey( BigDecimal aIdSollecito) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " WHERE " + setCondizioniByKey( aIdSollecito);

    // Imposta lo statement da eseguire 
    setStatement(lSql);
    logger.info("lSql = "+lSql); 
  }

  /***************************************************************************** 
   * Metodo che imposta la statement di ricerca per chiave 
   * @param aKey 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaSollecitoEsitoTrasmissioneByIdEvento( BigDecimal aIdEvento) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " WHERE EVE_ID_EVENTO = " +  aIdEvento;

    // Imposta lo statement da eseguire 
    setStatement(lSql);
    logger.info("lSql = "+lSql); 
  }

  /***************************************************************************** 
   * Metodo per la costruzione della sql query 
   * @return 
   ****************************************************************************/ 
  protected String getSqlQuery() {
    String lStatement = new String("");

    lStatement += " SELECT " +
                  "ID_SOLLECITO, "+  
                  "COD_UFF_SOLLECITATO, "+  
                  "OGGETTO_MS_SOLLECITO, "+  
                  "OGGETTO_MS_SOLLECITATO, "+  
                  "DATA_INVIO_MS_SOLLECITATO, "+  
                  "MES_ID_MESSAGGIO_SOLLECITATO, "+  
                  "COD_UFF_INOLTRANTE, "+  
                  "DATA_INOLTRO, "+  
                  "MES_ID_MESSAGGIO_INOLTRO, "+  
                  "EVE_ID_EVENTO, "+  
                  "FAS_SIE_ID_FASCICOLO_SIEP, "+  
                  "COD_OPERATORE_INSERIMENTO, "+  
                  "DATA_INSERIMENTO, "+  
                  "COD_UFFICIO_INSERIMENTO, "+  
                  "COD_OPERATORE_AGGIORNAMENTO, "+  
                  "DATA_AGGIORNAMENTO, "+  
                  "COD_UFFICIO_AGGIORNAMENTO "; 
    // aggiungere qui gli eventuali campi descrizioni 

    // Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
    lStatement += " FROM SOLLECITO_ESITO_TRASMISSIONE ";
//    lStatement +=    " , UFFICIO uff_sollecitato ";
//    lStatement +=    " , UFFICIO uff_inoltrante ";
//    lStatement +=    " , CG_REF_CODES oggetto_sollecito ";
//    lStatement +=    " , CG_REF_CODES oggetto_sollecitato ";
    
    
//    lStatement += " (     nvl(SOLLECITO_ESITO_TRASMISSIONE.COD_UFF_SOLLECITATO,'-')  = UFF_SOLLECITATO.RV_LOW_VALUE AND CODUFFSOLLECITATO.RV_DOMAIN = 'UFF_SOLLECITATO' ) " ;
//    lStatement += " (     nvl(SOLLECITO_ESITO_TRASMISSIONE.COD_UFF_INOLTRANTE,'-')   = UFF_INOLTRANTE.RV_LOW_VALUE AND CODUFFINOLTRANTE.RV_DOMAIN = 'UFF_INOLTRANTE' ) " ;
//    lStatement += " (     nvl(SOLLECITO_ESITO_TRASMISSIONE.OGGETTO_MS_SOLLECITO,'-') = CODUFFINOLTRANTE.RV_LOW_VALUE AND CODUFFINOLTRANTE.RV_DOMAIN = 'UFF_INOLTRANTE' ) " ;
//    lStatement += " (     nvl(SOLLECITO_ESITO_TRASMISSIONE.OGGETTO_MS_SOLLECITATO,'-') = CODUFFINOLTRANTE.RV_LOW_VALUE AND CODUFFINOLTRANTE.RV_DOMAIN = 'UFF_INOLTRANTE' ) " ;

    return lStatement;
  }


  /***************************************************************************** 
   * Metodo che carica il record del result set nel model 
   * @return 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel  getModel() throws DAOException {
     SollecitoEsitoTrasmissioneModel aModel = new  SollecitoEsitoTrasmissioneModel(); 

    //Inserire le opportune set delle descrizioni!
    aModel.setIdSollecito               ( getBigDecimal ("ID_SOLLECITO"                ) ); 
    aModel.setCodUffSollecitato         ( getString     ("COD_UFF_SOLLECITATO"         ) ); 
//aModel.setDescrUffSollecitato(getString("") );
    aModel.setOggettoMsSollecito        ( getString     ("OGGETTO_MS_SOLLECITO"        ) ); 
    aModel.setOggettoMsSollecitato      ( getString     ("OGGETTO_MS_SOLLECITATO"      ) ); 
    aModel.setDataInvioMsSollecitato    ( getDate       ("DATA_INVIO_MS_SOLLECITATO"   ) ); 
    aModel.setMesIdMessaggioSollecitato ( getBigDecimal ("MES_ID_MESSAGGIO_SOLLECITATO") ); 
    aModel.setCodUffInoltrante          ( getString     ("COD_UFF_INOLTRANTE"          ) ); 
//aModel.setDescrUffInoltrante(getString("") );
    aModel.setDataInoltro               ( getDate       ("DATA_INOLTRO"                ) ); 
    aModel.setMesIdMessaggioInoltro     ( getBigDecimal ("MES_ID_MESSAGGIO_INOLTRO"    ) ); 
    aModel.setEveIdEvento               ( getBigDecimal ("EVE_ID_EVENTO"               ) ); 
    aModel.setFasSieIdFascicoloSiep     ( getBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"   ) ); 
    aModel.setCodOperatoreInserimento   ( getString     ("COD_OPERATORE_INSERIMENTO"   ) ); 
    aModel.setDataInserimento           ( getDate       ("DATA_INSERIMENTO"            ) ); 
    aModel.setCodUfficioInserimento     ( getString     ("COD_UFFICIO_INSERIMENTO"     ) ); 
//aModel.setDescrUfficioInserimento(getString("") );
    aModel.setCodOperatoreAggiornamento ( getString     ("COD_OPERATORE_AGGIORNAMENTO" ) ); 
    aModel.setDataAggiornamento         ( getDate       ("DATA_AGGIORNAMENTO"          ) ); 
    aModel.setCodUfficioAggiornamento   ( getString     ("COD_UFFICIO_AGGIORNAMENTO"   ) ); 
//aModel.setDescrUfficioAggiornamento(getString("") );

    return aModel;
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public String setCondizioni(SollecitoEsitoTrasmissioneModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdSollecito() != null ) { 
      lCondizioni += " and ID_SOLLECITO = " + aModel.getIdSollecito() + ""; 
    } 
    if (aModel.getCodUffSollecitato() != null && aModel.getCodUffSollecitato().length() > 0) { 
      lCondizioni += " and COD_UFF_SOLLECITATO = '" + aModel.getCodUffSollecitato() + "' "; 
    } 
    if (aModel.getOggettoMsSollecito() != null && aModel.getOggettoMsSollecito().length() > 0) { 
      lCondizioni += " and OGGETTO_MS_SOLLECITO = '" + aModel.getOggettoMsSollecito() + "' "; 
    } 
    if (aModel.getOggettoMsSollecitato() != null && aModel.getOggettoMsSollecitato().length() > 0) { 
      lCondizioni += " and OGGETTO_MS_SOLLECITATO = '" + aModel.getOggettoMsSollecitato() + "' "; 
    } 
    if (aModel.getDataInvioMsSollecitato() != null ) { 
      lCondizioni += " and to_char(DATA_INVIO_MS_SOLLECITATO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInvioMsSollecitato(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getMesIdMessaggioSollecitato() != null ) { 
      lCondizioni += " and MES_ID_MESSAGGIO_SOLLECITATO = " + aModel.getMesIdMessaggioSollecitato() + ""; 
    } 
    if (aModel.getCodUffInoltrante() != null && aModel.getCodUffInoltrante().length() > 0) { 
      lCondizioni += " and COD_UFF_INOLTRANTE = '" + aModel.getCodUffInoltrante() + "' "; 
    } 
    if (aModel.getDataInoltro() != null ) { 
      lCondizioni += " and to_char(DATA_INOLTRO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInoltro(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getMesIdMessaggioInoltro() != null ) { 
      lCondizioni += " and MES_ID_MESSAGGIO_INOLTRO = " + aModel.getMesIdMessaggioInoltro() + ""; 
    } 
    if (aModel.getEveIdEvento() != null ) { 
      lCondizioni += " and EVE_ID_EVENTO = " + aModel.getEveIdEvento() + ""; 
    } 
    if (aModel.getFasSieIdFascicoloSiep() != null ) { 
      lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + ""; 
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

    logger.info("lCondizioni = "+lCondizioni); 
    return lCondizioni; 
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di select per chiave 
   * @param aKey 
   * @return 
   ****************************************************************************/ 
  public String setCondizioniByKey( BigDecimal aIdSollecito  ) {
    String lCondizioni = new String();

    lCondizioni += " and ID_SOLLECITO = " + aIdSollecito;

    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    logger.info("lCondizioni = "+lCondizioni); 

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
