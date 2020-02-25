package siap.siep.modulocumulo.dao;

/**
* <p>Title: DatiFinaliCumuloSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella DatiFinaliCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.modulocumulo.model.DatiFinaliCumuloModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class DatiFinaliCumuloSqlDAO extends SqlDAO {
  /***************************************************************************** 
   * Costruttore  
   * @param con 
   ****************************************************************************/ 
  public DatiFinaliCumuloSqlDAO (Connection con) {
    super(con);
  }


  /***************************************************************************** 
   * Restituisce il numero di record dell'operazione di ricerca costruendo 
   * la clausola where con lo stesso model utilizzato per la ricerca 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void getCountDatiFinaliCumulo(DatiFinaliCumuloModel aModel) throws DAOException {
    // Costruisce lo statement da eseguire 
    String lStatement ="SELECT COUNT(*) HowManyRecords FROM DATI_FINALI_CUMULO ";

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
  public void ricercaDatiFinaliCumuloPaged(DatiFinaliCumuloModel aModel, int aPage) throws DAOException { 
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
  public void ricercaDatiFinaliCumulo( DatiFinaliCumuloModel  aModel)  throws DAOException {
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
  public void ricercaDatiFinaliCumuloByKey( BigDecimal aIdDatiFinaliCumulo) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += setCondizioniByKey( aIdDatiFinaliCumulo);

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
                  "ID_DATI_FINALI_CUMULO, "+  
                  "TIPO_UFFICIO_EMISSIONE, "+  
                  "DATA_PROVVEDIMENTO, "+  
                    "COD_TIPO_PROVVEDIMENTO, tipo_provv.RV_MEANING descTipoProvv, "+  
                  "ANNO_PROVVEDIMENTO, "+  
                  "NUMERO_PROVVEDIMENTO, "+  
                    "COD_TIPO_UFFICIO_EMITTENTE, tipo_ufficio.RV_MEANING descTipoUfficio, "+  
                    "COD_LUOGO_UFFICIO_EMITTENTE, COMUNE.DESCRIZIONE descLuogoUfficio, "+  
                  "SEZIONE_UFFICIO_EMITTENTE, "+  
                  "FLAG_CREA_FASCICOLO_MS, FAS_SIE_ID_FASCICOLO_SIEP_MS, " +
                  "EVE_ID_EVENTO, "+  
                  "ISTR_ID_ISTRUTTORIA_CUMULO, "+ 
                  
                  "FLAG_PRIMO_CUMULO, "+
                  "COD_OPERATORE_INSERIMENTO, "+  
                  "DATA_INSERIMENTO, "+  
                  "COD_UFFICIO_INSERIMENTO, "+  
                  "COD_OPERATORE_AGGIORNAMENTO, "+  
                  "DATA_AGGIORNAMENTO, "+  
                  "COD_UFFICIO_AGGIORNAMENTO "; 
    // aggiungere qui gli eventuali campi descrizioni 

    // Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
    lStatement += " FROM DATI_FINALI_CUMULO LEFT OUTER JOIN CG_REF_CODES tipo_provv ON DATI_FINALI_CUMULO.COD_TIPO_PROVVEDIMENTO = tipo_provv.RV_LOW_VALUE AND tipo_provv.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' ";
    lStatement +=                         " LEFT OUTER JOIN CG_REF_CODES tipo_ufficio ON DATI_FINALI_CUMULO.COD_TIPO_UFFICIO_EMITTENTE  = tipo_ufficio.RV_LOW_VALUE AND tipo_ufficio.RV_DOMAIN = 'TIPO_UFFICIO' ";
    lStatement +=                         " LEFT OUTER JOIN COMUNE ON DATI_FINALI_CUMULO.COD_LUOGO_UFFICIO_EMITTENTE = COMUNE.COD_COMUNE ";
    lStatement += " WHERE 1=1 ";

    return lStatement;
  }


  /***************************************************************************** 
   * Metodo che carica il record del result set nel model 
   * @return 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel  getModel() throws DAOException {
     DatiFinaliCumuloModel aModel = new  DatiFinaliCumuloModel(); 

    //Inserire le opportune set delle descrizioni!
    aModel.setIdDatiFinaliCumulo        ( getBigDecimal ("ID_DATI_FINALI_CUMULO"      ) ); 
    aModel.setTipoUfficioEmissione      ( getString     ("TIPO_UFFICIO_EMISSIONE"     ) ); 
    aModel.setDataProvvedimento         ( getDate       ("DATA_PROVVEDIMENTO"         ) ); 
    aModel.setCodTipoProvvedimento      ( getString     ("COD_TIPO_PROVVEDIMENTO"     ) ); 
aModel.setDescrTipoProvvedimento(getString("descTipoProvv") );
    aModel.setAnnoProvvedimento         ( getBigDecimal ("ANNO_PROVVEDIMENTO"         ) ); 
    aModel.setNumeroProvvedimento       ( getBigDecimal ("NUMERO_PROVVEDIMENTO"       ) ); 
    aModel.setCodTipoUfficioEmittente   ( getString     ("COD_TIPO_UFFICIO_EMITTENTE" ) ); 
aModel.setDescrTipoUfficioEmittente(getString("descTipoUfficio") );
    aModel.setCodLuogoUfficioEmittente  ( getString     ("COD_LUOGO_UFFICIO_EMITTENTE") ); 
aModel.setDescrLuogoUfficioEmittente(getString("descLuogoUfficio") );
    aModel.setSezioneUfficioEmittente   ( getString     ("SEZIONE_UFFICIO_EMITTENTE"  ) ); 
    aModel.setFlagCreaFascicoloMs       ( getString     ("FLAG_CREA_FASCICOLO_MS"      ) ); 
    aModel.setFasSieIdFascicoloSiepMs   ( getBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP_MS") ); 
    aModel.setEveIdEvento               ( getBigDecimal ("EVE_ID_EVENTO"              ) ); 
    aModel.setIstrIdIstruttoriaCumulo   ( getBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO" ) ); 
    
    aModel.setFlagPrimoCumulo			( getString		("FLAG_PRIMO_CUMULO"	)	);
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
  public String setCondizioni(DatiFinaliCumuloModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdDatiFinaliCumulo() != null ) { 
      lCondizioni += " and ID_DATI_FINALI_CUMULO = " + aModel.getIdDatiFinaliCumulo() + ""; 
    } 
    if (aModel.getTipoUfficioEmissione() != null && aModel.getTipoUfficioEmissione().length() > 0) { 
      lCondizioni += " and TIPO_UFFICIO_EMISSIONE = '" + aModel.getTipoUfficioEmissione() + "' "; 
    } 
    if (aModel.getDataProvvedimento() != null ) { 
      lCondizioni += " and to_char(DATA_PROVVEDIMENTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataProvvedimento(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getCodTipoProvvedimento() != null && aModel.getCodTipoProvvedimento().length() > 0) { 
      lCondizioni += " and COD_TIPO_PROVVEDIMENTO = '" + aModel.getCodTipoProvvedimento() + "' "; 
    } 
    if (aModel.getAnnoProvvedimento() != null ) { 
      lCondizioni += " and ANNO_PROVVEDIMENTO = " + aModel.getAnnoProvvedimento() + ""; 
    } 
    if (aModel.getNumeroProvvedimento() != null ) { 
      lCondizioni += " and NUMERO_PROVVEDIMENTO = " + aModel.getNumeroProvvedimento() + ""; 
    } 
    if (aModel.getCodTipoUfficioEmittente() != null && aModel.getCodTipoUfficioEmittente().length() > 0) { 
      lCondizioni += " and COD_TIPO_UFFICIO_EMITTENTE = '" + aModel.getCodTipoUfficioEmittente() + "' "; 
    } 
    if (aModel.getCodLuogoUfficioEmittente() != null && aModel.getCodLuogoUfficioEmittente().length() > 0) { 
      lCondizioni += " and COD_LUOGO_UFFICIO_EMITTENTE = '" + aModel.getCodLuogoUfficioEmittente() + "' "; 
    } 
    if (aModel.getSezioneUfficioEmittente() != null && aModel.getSezioneUfficioEmittente().length() > 0) { 
      lCondizioni += " and SEZIONE_UFFICIO_EMITTENTE = '" + aModel.getSezioneUfficioEmittente() + "' "; 
    } 
    if (aModel.getFlagCreaFascicoloMs() != null && aModel.getFlagCreaFascicoloMs().length() > 0) { 
      lCondizioni += " and FLAG_CREA_FASCICOLO_MS = '" + aModel.getFlagCreaFascicoloMs() + "' "; 
    } 
    if (aModel.getFasSieIdFascicoloSiepMs() != null ) { 
      lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP_MS = " + aModel.getFasSieIdFascicoloSiepMs() + ""; 
    } 
    if (aModel.getEveIdEvento() != null ) { 
      lCondizioni += " and EVE_ID_EVENTO = " + aModel.getEveIdEvento() + ""; 
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

    return lCondizioni; 
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di select per chiave 
   * @param aKey 
   * @return 
   ****************************************************************************/ 
  public String setCondizioniByKey( BigDecimal aIdDatiFinaliCumulo  ) {
    String lCondizioni = new String();

    lCondizioni += " and ID_DATI_FINALI_CUMULO = " + aIdDatiFinaliCumulo;

    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    return lCondizioni;
  }

  public void ricercaDatiFinaliCumuloByIdIstruttoria( BigDecimal aIdIstruttoria  ) {
    String lSql = getSqlQuery();

    lSql += " and ISTR_ID_ISTRUTTORIA_CUMULO = " + aIdIstruttoria;

    setStatement(lSql);
  }
  
  public void ricercaDatiFinaliCumuloByIdEvento( BigDecimal aIdEvento  ) {
    String lSql = getSqlQuery();

    lSql += " and EVE_ID_EVENTO = " + aIdEvento;

    setStatement(lSql);
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
