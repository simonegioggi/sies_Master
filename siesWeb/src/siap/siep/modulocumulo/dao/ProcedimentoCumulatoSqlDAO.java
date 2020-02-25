package siap.siep.modulocumulo.dao;

/**
* <p>Title: ProcedimentoCumulatoSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella ProcedimentoCumulato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;

import org.apache.log4j.Logger;

public class ProcedimentoCumulatoSqlDAO extends SqlDAO {
  /***************************************************************************** 
   * Costruttore  
   * @param con 
   ****************************************************************************/ 
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
  public ProcedimentoCumulatoSqlDAO (Connection con) {
    super(con);
  }


  /***************************************************************************** 
   * Restituisce il numero di record dell'operazione di ricerca costruendo 
   * la clausola where con lo stesso model utilizzato per la ricerca 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void getCountProcedimentoCumulato(ProcedimentoCumulatoModel aModel) throws DAOException {
    // Costruisce lo statement da eseguire 
    String lStatement ="SELECT COUNT(*) HowManyRecords FROM PROCEDIMENTO_CUMULATO ";

    // Recupero la where condition in base al model 
    String lCondizioni = this.setCondizioni(aModel);
    if (!lCondizioni.trim().equals("")) 
      lStatement += " WHERE "+lCondizioni;

    // Imposta lo statement da eseguire 
    setStatement(lStatement);
    siesLogger.debug("lStatement = "+lStatement); 
  }

  /***************************************************************************** 
   * Effettua la ricerca e restituisce solo i risultati nel range di record che 
   * vanno inseriti nella pagfina passata in input 
   * @param aModel 
   * @param aPage 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaProcedimentoCumulatoPaged(ProcedimentoCumulatoModel aModel, int aPage) throws DAOException { 
    String lStatement = new String(""); 

    lStatement += getSqlQuery(); 

    // Recupero la where condition in base al model 
    String lCondizioni = this.setCondizioni(aModel);

    lStatement += lCondizioni;

    lStatement += " "+getOrderBy()+" "; 

    String lPaginedStatement = ""; 
    lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement + "  ) INNER ) WHERE rn between  " + ( (aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1) + 
          " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE; 

    setStatement(lPaginedStatement); 
    siesLogger.debug("lPaginedStatement = "+lPaginedStatement); 
  } 


  /***************************************************************************** 
   * Effettua la generica ricerca in base ai dati specificati nel model 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaProcedimentoCumulato( ProcedimentoCumulatoModel  aModel, BigDecimal aIdIstruttoria)  throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();
    
    // Recupero la where condition in base al model 
    String lCondizioni = setCondizioni(aModel);

    if (!lCondizioni.trim().equals("")) 
      lSql+= lCondizioni;

    if (aIdIstruttoria!=null)
      lSql+= " AND (TIT_ID_TITOLO_CUMULATO in (SELECT ID_TITOLO_CUMULATO FROM TITOLO_CUMULATO where ISTR_ID_ISTRUTTORIA_CUMULO = "+aIdIstruttoria+" ))";
    
    
    // Imposta lo statement da eseguire 
    setStatement(lSql);
    siesLogger.debug("lSql = "+lSql); 
  }
  
  /***************************************************************************** 
   * Effettua la generica ricerca in base ai dati specificati nel model;
   *  ( ChiaveAnnoFasCumulato, ChiaveProgrFasCumulato, CodUfficioFasCumulato ) 
   *  TIT_ID_TITOLO_CUMULATO deve far parte di una ISTRUTTORIA data.
   * @param aModel, IdIstruttoria 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaProcedimentoCumulatoInIstruttoria( ProcedimentoCumulatoModel  aModel, BigDecimal aIdIstruttoria)  throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Recupero la where condition in base al model 
    String lCondizioni = setCondizioni(aModel);
    
    // Condizioni by Istruttoria
    lCondizioni += setCondizioniTitoloInIstruttoria(aIdIstruttoria);

    if (!lCondizioni.trim().equals("")) 
      lSql+= lCondizioni;

    // Imposta lo statement da eseguire 
    setStatement(lSql);
    siesLogger.debug("lSql = "+lSql); 
  }


  /***************************************************************************** 
   * Metodo che imposta la statement di ricerca per chiave 
   * @param aKey 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaProcedimentoCumulatoByKey( BigDecimal aIdProcedimentoCumulato) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += setCondizioniByKey ( aIdProcedimentoCumulato);

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }
  
  /********************************************************************************** 
   * Metodo che imposta la statement di ricerca per chiave Fascicolo_Siep di origine 
   * @param aKey 
   * @throws DAOException 
   *********************************************************************************/ 
  public void ricercaProcedimentoCumulatoByIdFascicoloSiepOrig( BigDecimal aIdFascicoloSiep) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += setCondizioniByIdFascicoloSiepOrig ( aIdFascicoloSiep);

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }
  
  public void ricercaProcedimentoCumulatoByIdFascicoloSiepOrigEIstruttoria( BigDecimal aIdFascicoloSiep, BigDecimal aIdIstruttoria) throws DAOException 
  {
	  // Recupera la select...from 
	  String lSql = getSqlQuery();

	  // Aggiunge le where condition per chiave 
	  lSql += setCondizioniByIdFascicoloSiepOrig ( aIdFascicoloSiep);
	  lSql += setCondizioniTitoloInIstruttoria(aIdIstruttoria);

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
                  "ID_PROCEDIMENTO_CUMULATO, "+  
                  "ID_FASCICOLO_SIEP_ORIGINE, "+  
                  "CHIAVE_ANNO_FAS_CUMULATO, "+  
                  "CHIAVE_PROGR_FAS_CUMULATO, "+  
               "COD_TIPO_UFFICIO_FAS_CUMULATO, CODTIPOUFFICIOFASCUMULATO.RV_MEANING DESC_TIPO_UFF_FAS_CUMULATO, "+  
               "COD_LUOGO_UFFICIO_FAS_CUMULATO, CODLUOGOUFFICIOFASCUMULATO.DESCRIZIONE DESC_LUOGO_UFF_FAS_CUMULATO, "+  
               "COD_UFFICIO_FAS_CUMULATO, "+  
                  "DATA_RICHIESTA_FASCICOLO, "+  
                  "DATA_PERVENIMENTO_FASCICOLO, "+  
                  "NOTE, "+  
                  "FLAG_ACCORPATO, CHIAVE_UFFICIO_ORIGINE, CHIAVE_PROGR_ORIGINE, " +
                  "EVE_ID_EVENTO, "+
                 // "TIPO_UFF_ORIG.RV_MEANING DESC_UFF_ORIGINE, "+
                 // "COMUNE_UFF_ORIG.DESCRIZIONE DESC_SEDE_UFF_ORIGINE, "+                  
                  "TIT_ID_TITOLO_CUMULATO, "+  
                  "FLAG_STATO, "+  
                  "MOTIVO_MODIFICA, "+  
                  "COD_OPERATORE_INSERIMENTO, "+  
                  "DATA_INSERIMENTO, "+  
                  "COD_UFFICIO_INSERIMENTO, "+  
                  "COD_OPERATORE_AGGIORNAMENTO, "+  
                  "DATA_AGGIORNAMENTO, "+  
                  "COD_UFFICIO_AGGIORNAMENTO "; 
    // aggiungere qui gli eventuali campi descrizioni 

    // Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
    lStatement += " FROM PROCEDIMENTO_CUMULATO ";
    lStatement +=    " , CG_REF_CODES CODTIPOUFFICIOFASCUMULATO ";
    lStatement +=    " , COMUNE CODLUOGOUFFICIOFASCUMULATO ";    
    lStatement +=    " , UFFICIO UFFFASCUM "; 
    //
//    lStatement +=    " , UFFICIO UFFFASORIG ";    
//    lStatement +=    " , CG_REF_CODES TIPO_UFF_ORIG ";    
//    lStatement +=    " , COMUNE COMUNE_UFF_ORIG ";    
    //
    lStatement += " WHERE 1=1 ";     
    lStatement +=   " AND ( nvl(PROCEDIMENTO_CUMULATO.COD_TIPO_UFFICIO_FAS_CUMULATO,'-') = CODTIPOUFFICIOFASCUMULATO.RV_LOW_VALUE AND CODTIPOUFFICIOFASCUMULATO.RV_DOMAIN = 'TIPO_UFFICIO' ) "; 
    lStatement +=   " AND ( nvl(PROCEDIMENTO_CUMULATO.COD_LUOGO_UFFICIO_FAS_CUMULATO,'-') = CODLUOGOUFFICIOFASCUMULATO.COD_COMUNE ) "; 
    lStatement +=   " AND ( nvl(PROCEDIMENTO_CUMULATO.COD_UFFICIO_FAS_CUMULATO,'-') = UFFFASCUM.COD_UFFICIO ) "; 
    //
//    lStatement +=   " AND PROCEDIMENTO_CUMULATO.CHIAVE_UFFICIO_ORIGINE = UFFFASORIG.COD_UFFICIO (+) ";
//    lStatement +=   " AND (NVL (UFFFASORIG.COD_TIPO_UFFICIO, '-') = TIPO_UFF_ORIG.RV_LOW_VALUE AND TIPO_UFF_ORIG.RV_DOMAIN = 'TIPO_UFFICIO') ";
//    lStatement +=   " AND (NVL (UFFFASORIG.COD_COMUNE, '-') = COMUNE_UFF_ORIG.COD_COMUNE) ";

    //siesLogger.debug("lStatement = "+lStatement); 
    return lStatement;
  }


  /***************************************************************************** 
   * Metodo che carica il record del result set nel model 
   * @return 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel  getModel() throws DAOException {
     ProcedimentoCumulatoModel aModel = new  ProcedimentoCumulatoModel(); 

    //Inserire le opportune set delle descrizioni!
    aModel.setIdProcedimentoCumulato     ( getBigDecimal ("ID_PROCEDIMENTO_CUMULATO"      ) ); 
    aModel.setIdFascicoloSiepOrigine     ( getBigDecimal ("ID_FASCICOLO_SIEP_ORIGINE"     ) ); 
    aModel.setChiaveAnnoFasCumulato      ( getBigDecimal ("CHIAVE_ANNO_FAS_CUMULATO"      ) ); 
    aModel.setChiaveProgrFasCumulato     ( getBigDecimal ("CHIAVE_PROGR_FAS_CUMULATO"     ) ); 
    aModel.setCodTipoUfficioFasCumulato  ( getString     ("COD_TIPO_UFFICIO_FAS_CUMULATO" ) ); 
aModel.setDescrTipoUfficioFasCumulato (getString("DESC_TIPO_UFF_FAS_CUMULATO") );
    aModel.setCodLuogoUfficioFasCumulato ( getString     ("COD_LUOGO_UFFICIO_FAS_CUMULATO") ); 
aModel.setDescrLuogoUfficioFasCumulato (getString("DESC_LUOGO_UFF_FAS_CUMULATO") );
    aModel.setCodUfficioFasCumulato      ( getString     ("COD_UFFICIO_FAS_CUMULATO"      ) ); 
//aModel.setDescrUfficioFasCumulato(getString("") );
    aModel.setDataRichiestaFascicolo     ( getDate       ("DATA_RICHIESTA_FASCICOLO"      ) ); 
    aModel.setDataPervenimentoFascicolo  ( getDate       ("DATA_PERVENIMENTO_FASCICOLO"   ) ); 
    aModel.setNote                       ( getString     ("NOTE"                          ) ); 
    aModel.setEveIdEvento				 ( getBigDecimal ("EVE_ID_EVENTO"	)	);
    
    aModel.setFlagAccorpato              ( getString     ("FLAG_ACCORPATO"                ) ); 
    aModel.setChiaveUfficioOrigine       ( getString     ("CHIAVE_UFFICIO_ORIGINE"        ) ); 
    aModel.setChiaveProgrOrigine         ( getBigDecimal ("CHIAVE_PROGR_ORIGINE"          ) ); 
    
    aModel.setTitIdTitoloCumulato        ( getBigDecimal ("TIT_ID_TITOLO_CUMULATO"        ) ); 
    aModel.setFlagStato                  ( getString     ("FLAG_STATO"                    ) ); 
    aModel.setMotivoModifica             ( getString     ("MOTIVO_MODIFICA"               ) ); 

    aModel.setCodOperatoreInserimento    ( getString     ("COD_OPERATORE_INSERIMENTO"     ) ); 
    aModel.setDataInserimento            ( getDate       ("DATA_INSERIMENTO"              ) ); 
    aModel.setCodUfficioInserimento      ( getString     ("COD_UFFICIO_INSERIMENTO"       ) ); 
    aModel.setCodOperatoreAggiornamento  ( getString     ("COD_OPERATORE_AGGIORNAMENTO"   ) ); 
    aModel.setDataAggiornamento          ( getDate       ("DATA_AGGIORNAMENTO"            ) ); 
    aModel.setCodUfficioAggiornamento    ( getString     ("COD_UFFICIO_AGGIORNAMENTO"     ) ); 

    return aModel;
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public String setCondizioni(ProcedimentoCumulatoModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdProcedimentoCumulato() != null ) { 
      lCondizioni += " and ID_PROCEDIMENTO_CUMULATO = " + aModel.getIdProcedimentoCumulato() + ""; 
    } 
    if (aModel.getIdFascicoloSiepOrigine() != null ) { 
      lCondizioni += " and ID_FASCICOLO_SIEP_ORIGINE = " + aModel.getIdFascicoloSiepOrigine() + ""; 
    } 
    if (aModel.getChiaveAnnoFasCumulato() != null ) { 
      lCondizioni += " and CHIAVE_ANNO_FAS_CUMULATO = " + aModel.getChiaveAnnoFasCumulato() + ""; 
    } 
    if (aModel.getChiaveProgrFasCumulato() != null ) { 
      lCondizioni += " and CHIAVE_PROGR_FAS_CUMULATO = " + aModel.getChiaveProgrFasCumulato() + ""; 
    } 
    if (aModel.getCodTipoUfficioFasCumulato() != null && aModel.getCodTipoUfficioFasCumulato().length() > 0) { 
      lCondizioni += " and COD_TIPO_UFFICIO_FAS_CUMULATO = '" + aModel.getCodTipoUfficioFasCumulato() + "' "; 
    } 
    if (aModel.getCodLuogoUfficioFasCumulato() != null && aModel.getCodLuogoUfficioFasCumulato().length() > 0) { 
      lCondizioni += " and COD_LUOGO_UFFICIO_FAS_CUMULATO = '" + aModel.getCodLuogoUfficioFasCumulato() + "' "; 
    } 
    if (aModel.getCodUfficioFasCumulato() != null && aModel.getCodUfficioFasCumulato().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_FAS_CUMULATO = '" + aModel.getCodUfficioFasCumulato() + "' "; 
    } 
    if (aModel.getDataRichiestaFascicolo() != null ) { 
      lCondizioni += " and to_char(DATA_RICHIESTA_FASCICOLO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataRichiestaFascicolo(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataPervenimentoFascicolo() != null ) { 
      lCondizioni += " and to_char(DATA_PERVENIMENTO_FASCICOLO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataPervenimentoFascicolo(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getNote() != null && aModel.getNote().length() > 0) { 
      lCondizioni += " and NOTE = '" + aModel.getNote() + "' "; 
    } 
    
    if (aModel.getFlagAccorpato() != null && aModel.getFlagAccorpato().length() > 0) { 
      lCondizioni += " and FLAG_ACCORPATO = '" + aModel.getFlagAccorpato() + "' "; 
    } 
    if (aModel.getChiaveUfficioOrigine() != null && aModel.getChiaveUfficioOrigine().length() > 0) { 
      lCondizioni += " and CHIAVE_UFFICIO_ORIGINE = '" + aModel.getChiaveUfficioOrigine() + "' "; 
    } 
    if (aModel.getChiaveProgrOrigine() != null ) { 
      lCondizioni += " and CHIAVE_PROGR_ORIGINE = " + aModel.getChiaveProgrOrigine() + ""; 
    } 
    
    if (aModel.getTitIdTitoloCumulato() != null ) { 
      lCondizioni += " and TIT_ID_TITOLO_CUMULATO = " + aModel.getTitIdTitoloCumulato() + ""; 
    } 
    if (aModel.getFlagStato() != null && aModel.getFlagStato().length() > 0) { 
      lCondizioni += " and FLAG_STATO = '" + aModel.getFlagStato() + "' "; 
    } 
    if (aModel.getMotivoModifica() != null && aModel.getMotivoModifica().length() > 0) { 
      lCondizioni += " and MOTIVO_MODIFICA = '" + aModel.getMotivoModifica() + "' "; 
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
  public String setCondizioniByKey( BigDecimal aIdProcedimentoCumulato  ) {
    String lCondizioni = new String();

    lCondizioni += " and ID_PROCEDIMENTO_CUMULATO = " + aIdProcedimentoCumulato;

    return lCondizioni;
  }
  
  /******************************************************************************* 
   * Metodo che imposta le condizioni di select per chiave Fascicolo Siep Origine 
   * @param aKey 
   * @return 
   ******************************************************************************/ 
  public String setCondizioniByIdFascicoloSiepOrig( BigDecimal aIdFascicoloSiep  ) {
    String lCondizioni = new String();

    lCondizioni += " and ID_FASCICOLO_SIEP_ORIGINE = " + aIdFascicoloSiep;

    return lCondizioni;
  }
  
  /*****************************************************************************
   *  Metodo che imposta le condizioni di select TITOLO_CUMULATO
   * @param aIdIstruttoria
   * @throws DAOException
   *****************************************************************************/
  public String setCondizioniTitoloInIstruttoria( BigDecimal aIdIstruttoria) throws DAOException 
  {
	  String lCondizioni = new String();
	  lCondizioni += " AND TIT_ID_TITOLO_CUMULATO IN ";
	  lCondizioni += " (select TC.ID_TITOLO_CUMULATO from TITOLO_CUMULATO TC ";
	  lCondizioni += " 	where  TC.ISTR_ID_ISTRUTTORIA_CUMULO = "+aIdIstruttoria+" )";

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

  /**
   * 
   * @param aIdTitoloCumulato
   * @throws DAOException
   */
  public void ricercaProcedimentoCumulatoByIdTitolo( BigDecimal aIdTitoloCumulato) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " AND TIT_ID_TITOLO_CUMULATO = "+aIdTitoloCumulato;

    // Imposta lo statement da eseguire 
    setStatement(lSql);
    
    //siesLogger.debug("lSql = "+lSql); 
  }  
  

  
  
  public void ricercaProcedimentiClasseIVPerRibaltamentoByIdIstru ( BigDecimal aIdIstruttoria, String aChiaveUfficio) throws DAOException {
    String lStatement = new String("");

    lStatement += " SELECT ANNO_FASCICOLO_CLASSE_IV as CHIAVE_ANNO, NUMERO_FASCICOLO_CLASSE_IV as CHIAVE_PROGR, COD_AUTORITA_EMITT_CLASSE_IV as CHIAVE_UFFICIO "+
                        ", FASCICOLO_SIEP.ID_FASCICOLO_SIEP " +         
                    " FROM MISURA_SICUREZZA_CUMULO, TITOLO_CUMULATO, FASCICOLO_SIEP "+
                   " WHERE MISURA_SICUREZZA_CUMULO.TIT_ID_TITOLO_CUMULATO = TITOLO_CUMULATO.ID_TITOLO_CUMULATO "+
                     " AND TITOLO_CUMULATO.ISTR_ID_ISTRUTTORIA_CUMULO = " +aIdIstruttoria +
                     " AND MISURA_SICUREZZA_CUMULO.FLAG_STATO <> 'C' "+
                     " AND MISURA_SICUREZZA_CUMULO.FLAG_STATO_MISURA = 'V' "+
                     " AND MISURA_SICUREZZA_CUMULO.COD_AUTORITA_EMITT_CLASSE_IV = '"+aChiaveUfficio+"' "+
                     " AND FASCICOLO_SIEP.CHIAVE_ANNO = ANNO_FASCICOLO_CLASSE_IV "+
                     " AND FASCICOLO_SIEP.CHIAVE_PROGR = NUMERO_FASCICOLO_CLASSE_IV "+
                     " AND FASCICOLO_SIEP.CHIAVE_UFFICIO = COD_AUTORITA_EMITT_CLASSE_IV "+
                     " AND NUMERO_FASCICOLO_CLASSE_IV > 40000 AND NUMERO_FASCICOLO_CLASSE_IV <= 50000 "+
               " UNION "+
                  " SELECT CHIAVE_ANNO_FAS_CUMULATO as CHIAVE_ANNO, CHIAVE_PROGR_FAS_CUMULATO as CHIAVE_PROGR, COD_UFFICIO_FAS_CUMULATO as CHIAVE_UFFICIO"+
                        ", FASCICOLO_SIEP.ID_FASCICOLO_SIEP " +         
                    " FROM TITOLO_CUMULATO, PROCEDIMENTO_CUMULATO, FASCICOLO_SIEP "+
                   " WHERE PROCEDIMENTO_CUMULATO.TIT_ID_TITOLO_CUMULATO = TITOLO_CUMULATO.ID_TITOLO_CUMULATO "+
                     " AND CHIAVE_PROGR_FAS_CUMULATO>40000 AND CHIAVE_PROGR_FAS_CUMULATO<=50000 "+
                     " AND FASCICOLO_SIEP.CHIAVE_ANNO = CHIAVE_ANNO_FAS_CUMULATO "+
                     " AND FASCICOLO_SIEP.CHIAVE_PROGR = CHIAVE_PROGR_FAS_CUMULATO "+
                     " AND FASCICOLO_SIEP.CHIAVE_UFFICIO = COD_UFFICIO_FAS_CUMULATO "+
                     " AND TITOLO_CUMULATO.ISTR_ID_ISTRUTTORIA_CUMULO = "+aIdIstruttoria+
                     " AND PROCEDIMENTO_CUMULATO.COD_UFFICIO_FAS_CUMULATO = '"+aChiaveUfficio+"' ";


    // Imposta lo statement da eseguire 
    setStatement (lStatement);
  }
  
  
  public GenericModel getModelPerRibaltamento() throws DAOException {
    ProcedimentoCumulatoModel aModel = new  ProcedimentoCumulatoModel(); 

   //Inserire le opportune set delle descrizioni!
   aModel.setChiaveAnnoFasCumulato      ( getBigDecimal ("CHIAVE_ANNO"   ) ); 
   aModel.setChiaveProgrFasCumulato     ( getBigDecimal ("CHIAVE_PROGR"  ) ); 
   aModel.setCodUfficioFasCumulato      ( getString     ("CHIAVE_UFFICIO") ); 
   
   aModel.setIdFascicoloSiepOrigine     ( getBigDecimal ("ID_FASCICOLO_SIEP"   ) ); 
 
   return aModel;
  } 
}
