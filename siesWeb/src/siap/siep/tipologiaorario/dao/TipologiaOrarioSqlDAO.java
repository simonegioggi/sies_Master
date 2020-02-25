package siap.siep.tipologiaorario.dao;

/**
* <p>Title: TipologiaOrarioSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella TipologiaOrario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.tipologiaorario.model.TipologiaOrarioModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class TipologiaOrarioSqlDAO extends SqlDAO {
  /***************************************************************************** 
   * Costruttore  
   * @param con 
   ****************************************************************************/ 
  public TipologiaOrarioSqlDAO (Connection con) {
    super(con);
  }


  /***************************************************************************** 
   * Restituisce il numero di record dell'operazione di ricerca costruendo 
   * la clausola where con lo stesso model utilizzato per la ricerca 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void getCountTipologiaOrario(TipologiaOrarioModel aModel) throws DAOException {
    // Costruisce lo statement da eseguire 
    String lStatement ="SELECT COUNT(*) HowManyRecords FROM TIPOLOGIA_ORARIO ";

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
  public void ricercaTipologiaOrarioPaged(TipologiaOrarioModel aModel, int aPage) throws DAOException { 
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
  } 


  /***************************************************************************** 
   * Effettua la generica ricerca in base ai dati specificati nel model 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaTipologiaOrario( TipologiaOrarioModel  aModel)  throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Recupero la where condition in base al model 
    String lCondizioni = setCondizioni(aModel);

    if (!lCondizioni.trim().equals("")) 
      lSql+=" WHERE " + lCondizioni;

    lSql += " "+getOrderBy()+" "; 

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }


  /***************************************************************************** 
   * Metodo che imposta la statement di ricerca per chiave 
   * @param aKey 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaTipologiaOrarioByKey( BigDecimal aIdTipologiaOrario) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " WHERE " + setCondizioniByKey( aIdTipologiaOrario);

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }

  
  /***************************************************************************** 
   * Metodo che imposta la statement di ricerca per id beneficio 
   * @param aIdBeneficio 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaTipologiaOrarioByIdBeneficio( BigDecimal aIdBeneficio) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql +=  setCondizioniByIdBeneficio( aIdBeneficio);

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }
  
  /**
   * MEV262 Cumulo - Recupera gli orari di esecuzione della SS LPU by id
   * @param aIdUlterioreSanzione
   * @throws DAOException
   */
  public void ricercaTipologiaOrarioByIdUltSanz( BigDecimal aIdUlterioreSanzione) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " and DAT_FIN_CUM_ULT_SANZIONI = " + aIdUlterioreSanzione;


    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }

  public void ricercaTipologiaOrarioByIdBeneficioCumulo( BigDecimal aIdBeneficioCumulo) throws DAOException 
  {
	    // Recupera la select...from 
	    String lSql = getSqlQuery();

	    // Aggiunge le where condition per chiave BEN_ID_BENEFICIO_CUMULO
	    lSql += " and BEN_ID_BENEFICIO_CUMULO = "+ aIdBeneficioCumulo;

	    // Imposta lo statement da eseguire 
	    setStatement(lSql);
  }

  /***************************************************************************** 
   * Metodo per la costruzione della sql query 
   * @return 
   ****************************************************************************/ 
  protected String getSqlQuery() {
    String lStatement = new String("");

    lStatement += " SELECT " +
                  "ID_TIPOLOGIA_ORARIO, "+  
                  "COD_NUM_GIORNO, "+  
                  "NUMGIORNO.RV_MEANING DESCR_NUM_GIORNO, "+  
                  "DALLE_ORE, "+  
                  "ALLE_ORE, "+  
                  "ENTE_INCARICATO, "+  
                  "BEN_ID_BENEFICIO, "+  
                  "DAT_FIN_CUM_ULT_SANZIONI, "+                    
                  "COD_OPERATORE_INSERIMENTO, "+  
                  "DATA_INSERIMENTO, "+  
                  "COD_UFFICIO_INSERIMENTO, "+  
                  "COD_OPERATORE_AGGIORNAMENTO, "+  
                  "DATA_AGGIORNAMENTO, "+  
                  "COD_UFFICIO_AGGIORNAMENTO "; 
    lStatement += " FROM TIPOLOGIA_ORARIO,CG_REF_CODES NUMGIORNO ";
    lStatement += " WHERE NUMGIORNO.RV_DOMAIN = 'NUM_GIORNO' AND COD_NUM_GIORNO = NUMGIORNO.RV_LOW_VALUE";

    return lStatement;
  }


  /***************************************************************************** 
   * Metodo che carica il record del result set nel model 
   * @return 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel  getModel() throws DAOException {
     TipologiaOrarioModel aModel = new  TipologiaOrarioModel(); 

    //Inserire le opportune set delle descrizioni!
    aModel.setIdTipologiaOrario         ( getBigDecimal ("ID_TIPOLOGIA_ORARIO"        ) ); 
    aModel.setCodNumGiorno              ( getString     ("COD_NUM_GIORNO"             ) ); 
    aModel.setDescrNumGiorno            ( getString     ("DESCR_NUM_GIORNO") );
    aModel.setDalleOre                  ( getString     ("DALLE_ORE"                  ) ); 
    aModel.setAlleOre                   ( getString     ("ALLE_ORE"                   ) ); 
    aModel.setEnteIncaricato            ( getString     ("ENTE_INCARICATO"            ) ); 
    aModel.setBenIdBeneficio            ( getBigDecimal ("BEN_ID_BENEFICIO"           ) ); 
    aModel.setCodOperatoreInserimento   ( getString     ("COD_OPERATORE_INSERIMENTO"  ) ); 
    aModel.setDataInserimento           ( getDate       ("DATA_INSERIMENTO"           ) ); 
    aModel.setCodUfficioInserimento     ( getString     ("COD_UFFICIO_INSERIMENTO"    ) ); 
// aModel.setDescrUfficioInserimento(getString("") );
    aModel.setCodOperatoreAggiornamento ( getString     ("COD_OPERATORE_AGGIORNAMENTO") ); 
    aModel.setDataAggiornamento         ( getDate       ("DATA_AGGIORNAMENTO"         ) ); 
    aModel.setCodUfficioAggiornamento   ( getString     ("COD_UFFICIO_AGGIORNAMENTO"  ) ); 
// aModel.setDescrUfficioAggiornamento(getString("") );

    return aModel;
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public String setCondizioni(TipologiaOrarioModel aModel) {
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


    return lCondizioni; 
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di select per chiave 
   * @param aKey 
   * @return 
   ****************************************************************************/ 
  public String setCondizioniByKey( BigDecimal aIdTipologiaOrario ) 
  {
    String lCondizioni = new String();
    lCondizioni += " and ID_TIPOLOGIA_ORARIO = " + aIdTipologiaOrario + ""; 


    return lCondizioni;
  }

  
  /***************************************************************************** 
   * Metodo che imposta le condizioni di select per chiave 
   * @param aKey 
   * @return 
   ****************************************************************************/ 
  public String setCondizioniByIdBeneficio( BigDecimal aIdBeneficio  ) {
    String lCondizioni = new String();

    lCondizioni += " and BEN_ID_BENEFICIO = " + aIdBeneficio;

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
