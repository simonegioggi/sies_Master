package siap.siep.modulocumulo.dao;

/**
* <p>Title: MisuraSicurezzaCumuloSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella MisuraSicurezzaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.lang.String;
import f3b.util.DateUtils;
import java.sql.Connection;

import f3b.web.IWebConstants; 
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;

public class MisuraSicurezzaCumuloSqlDAO extends SqlDAO {
  /***************************************************************************** 
   * Costruttore  
   * @param con 
   ****************************************************************************/ 
  public MisuraSicurezzaCumuloSqlDAO (Connection con) {
    super(con);
  }


  /***************************************************************************** 
   * Restituisce il numero di record dell'operazione di ricerca costruendo 
   * la clausola where con lo stesso model utilizzato per la ricerca 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void getCountMisuraSicurezzaCumulo(MisuraSicurezzaCumuloModel aModel) throws DAOException {
    // Costruisce lo statement da eseguire 
    String lStatement ="SELECT COUNT(*) HowManyRecords FROM MISURA_SICUREZZA_CUMULO ";

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
  public void ricercaMisuraSicurezzaCumuloPaged(MisuraSicurezzaCumuloModel aModel, int aPage) throws DAOException { 
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
  public void ricercaMisuraSicurezzaCumulo( MisuraSicurezzaCumuloModel  aModel)  throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    lSql += " and " + setCondizioni(aModel);
    lSql += getOrderBy();
    
    setStatement(lSql);
  }


  /***************************************************************************** 
   * Metodo che imposta la statement di ricerca per chiave 
   * @param aKey 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaMisuraSicurezzaCumuloByKey( BigDecimal aIdMisuraSicurezzaCumulo) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += setCondizioniByKey( aIdMisuraSicurezzaCumulo);
    lSql += getOrderBy();
    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }
  
  /***************************************************************************** 
   * Metodo che imposta la statement di ricerca per Id_Misura_Sicurezza_Origine 
   * ***************************************************************************/
  public void ricercaMisuraSicurezzaCumuloByIdMisuraOrigine(BigDecimal aMisIdMisuraSicurezza, BigDecimal aIdTitoloCumulo) throws DAOException {

    // Recupera la select...from 
      String lSql = getSqlQuery();

      // Aggiunge le where condition per chiave 
      lSql += " and ID_MISURA_SICUREZZA_ORIGINE = " + aMisIdMisuraSicurezza + ""; 
      lSql += " and TIT_ID_TITOLO_CUMULATO = " + aIdTitoloCumulo + ""; 

      // Imposta lo statement da eseguire 
      setStatement(lSql);
  }
  
  /***************************************************************************** 
   * Metodo che imposta la statement di ricerca per MIS_Id_Misura_Sicurezza_Origine 
   * ***************************************************************************/
  public void ricercaMisuraSicurezzaCumuloByMisIdMisuraOrigine(BigDecimal aMisIdMisuraSicurezza, BigDecimal aIdTitoloCumulo) throws DAOException {

    // Recupera la select...from 
      String lSql = getSqlQuery();

      // Aggiunge le where condition per chiave 
      lSql += " and MIS_ID_MISURA_SICUREZZA_ORIG = " + aMisIdMisuraSicurezza + ""; 
      lSql += " and TIT_ID_TITOLO_CUMULATO = " + aIdTitoloCumulo + ""; 

      // Imposta lo statement da eseguire 
      setStatement(lSql);
  }

  /**
   * 
   * @param aIdIstruttoriaCumulo
   * @param aFlagDatiFinali - true recupera solo le MS selezionete in Datifinali
   * @throws DAOException
   */
  public void ricercaMisureSicurezzaCumuloByIdIstruttoria( BigDecimal aIdIstrittoriaCumulo, boolean aFlagDatiFinali) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQueryJoinTitoloCumulato();

    // Aggiunge le where condition per chiave 
    lSql += " AND TITOLO_CUMULATO.ISTR_ID_ISTRUTTORIA_CUMULO = "+aIdIstrittoriaCumulo;
    lSql += " AND MISURA_SICUREZZA_CUMULO.FLAG_STATO <> 'C'";
    
    if (aFlagDatiFinali){      
      lSql += " AND FLAG_DATI_FINALI= 'S' ";
    }
    
    // Ordinate per Data Irrevocabilità del Titolo, titolo e...
    lSql += " ORDER BY TITOLO_CUMULATO.DATA_IRREVOCABILITA ASC, TITOLO_CUMULATO.ID_TITOLO_CUMULATO ASC ";
    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }
  
  // Ricerca per Tit_Id_Titolo_Cumulato
  public void ricercaMisuraSicurezzaCumuloByIdTitoloCum( BigDecimal aIdTitolo) throws DAOException 
  {
	    String lSql = getSqlQuery();

	    lSql += setCondizioniByTitIdTitolo( aIdTitolo);
	    //lSql += getOrderBy();

	    setStatement(lSql);
  }
  
  // Ricerca per Tit_Id_Titolo_Cumulato in Join con RICHPM_MISSICUR_CUM
  public void ricercaMisuraSicurezzaCumuloByIdTitoloCumRichGE( BigDecimal aIdTitolo, BigDecimal aIdRichGE) throws DAOException 
  {
	    String lSql = getSqlQueryJoinRichiestaGE();

	    lSql += setCondizioniByTitIdTitolo(aIdTitolo);
	    // Aggiunge le where condition per chiave 
	    
	    lSql += " AND RICHPM_MISSICUR_CUM.RIC_ID_RICHIESTE_PM_IN_CUMULO = "+aIdRichGE;
	    lSql += " AND RICHPM_MISSICUR_CUM.MIS_ID_MISSICUR_CUMULO = ID_MISURA_SICUREZZA_CUMULO ";
	    //lSql += getOrderBy();

	    setStatement(lSql);
  }
  
  /***************************************************************************** 
   * Metodo per la costruzione della sql query 
   * @return 
   ****************************************************************************/ 
  protected String getSqlQuery() {
    String lStatement = new String("");

    lStatement += " SELECT ID_MISURA_SICUREZZA_CUMULO,  "+  
                         " COD_NATURA,  codnatura.RV_MEANING DESCR_NATURA, " +
                         " COD_TIPO, codtipo.RV_MEANING DESCR_TIPO, " +
                         " NUM_ANNI, NUM_MESI, NUM_GIORNI, "+  
                         " ANNO_REG_38, NUM_REG_38, "+  
                         " MOTIVO_MODIFICA, "+  
                         " FLAG_STATO, "+  
                         " TIT_ID_TITOLO_CUMULATO, ID_MISURA_SICUREZZA_ORIGINE, "+
                         " FLAG_ANNULLA_MISURA, DATA_FINE_VALIDITA, IST_DET_ID_ISTITUTO_DETENZIONE, "+
                         " LUOGO_ESECUZIONE_MISURA, MIS_ID_MISURA_SICUREZZA_CUMULO, MIS_ID_MISURA_SICUREZZA_ORIG, "+
                         " FLAG_DATI_FINALI, "+
                         
                         " ANNO_FASCICOLO_CLASSE_IV, NUMERO_FASCICOLO_CLASSE_IV, "+
                         " FLAG_STATO_MISURA, statoMisura.RV_MEANING STATO_MISURA, "+
                         " COD_AUTORITA_EMITT_CLASSE_IV, codAutEmi.RV_MEANING AUT_EMI_CLASSE_IV, "+
                         " LUOGO_AUTORITA_EMITT_CLASSE_IV, COMUNE.DESCRIZIONE LUO_EMI_CLASSE_IV, "+
                         
                         " COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, COD_UFFICIO_INSERIMENTO, "+  
                         " COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO "; 
    lStatement += " FROM MISURA_SICUREZZA_CUMULO, CG_REF_CODES codnatura, CG_REF_CODES codtipo, ";
    lStatement += "  CG_REF_CODES STATOMISURA, CG_REF_CODES CODAUTEMI, UFFICIO Uff, COMUNE ";
    lStatement += " WHERE (nvl(MISURA_SICUREZZA_CUMULO.COD_NATURA,'-') = CODNATURA.RV_LOW_VALUE AND CODNATURA.RV_DOMAIN = 'NATURA_MISURA_SICUREZZA' ) " ;
    lStatement += "   AND (nvl(MISURA_SICUREZZA_CUMULO.COD_TIPO,'-') = CODTIPO.RV_LOW_VALUE AND CODTIPO.RV_DOMAIN = 'TIPO_MISURA_SICUREZZA' ) " ;
    lStatement += " AND MISURA_SICUREZZA_CUMULO.FLAG_STATO_MISURA = STATOMISURA.RV_LOW_VALUE AND STATOMISURA.RV_DOMAIN = 'STATO_MISURA_CUMULO' ";
    lStatement += " AND (nvl (MISURA_SICUREZZA_CUMULO.COD_AUTORITA_EMITT_CLASSE_IV, '-') = Uff.COD_UFFICIO  ";
    lStatement += " AND Uff.COD_TIPO_UFFICIO = CODAUTEMI.RV_LOW_VALUE AND CODAUTEMI.RV_DOMAIN = 'TIPO_UFFICIO') ";
    lStatement += " AND (nvl (MISURA_SICUREZZA_CUMULO.LUOGO_AUTORITA_EMITT_CLASSE_IV, '-') =  COMUNE.COD_COMUNE )";
   
    return lStatement;
  }


  /***************************************************************************** 
   * Metodo che carica il record del result set nel model 
   * @return 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel  getModel() throws DAOException {
     MisuraSicurezzaCumuloModel aModel = new  MisuraSicurezzaCumuloModel(); 

    //Inserire le opportune set delle descrizioni!
    aModel.setIdMisuraSicurezzaCumulo   ( getBigDecimal ("ID_MISURA_SICUREZZA_CUMULO" ) ); 
    aModel.setCodNatura                 ( getString     ("COD_NATURA"                 ) ); 
    aModel.setDescrNatura(getString("DESCR_NATURA") );
    aModel.setCodTipo                   ( getString     ("COD_TIPO"                   ) ); 
    aModel.setDescrTipo(getString("DESCR_TIPO") );
    aModel.setNumAnni                   ( getBigDecimal ("NUM_ANNI"                   ) ); 
    aModel.setNumMesi                   ( getBigDecimal ("NUM_MESI"                   ) ); 
    aModel.setNumGiorni                 ( getBigDecimal ("NUM_GIORNI"                 ) ); 
    aModel.setAnnoReg38                 ( getBigDecimal ("ANNO_REG_38"                ) ); 
    aModel.setNumReg38                  ( getBigDecimal ("NUM_REG_38"                 ) );
    
    aModel.setFlagAnnullaMisura       (getString    ("FLAG_ANNULLA_MISURA")); 
    aModel.setDataFineValidita        (getDate    ("DATA_FINE_VALIDITA")); 
    aModel.setMisIdMisuraSicurezzaCumulo  (getBigDecimal  ("MIS_ID_MISURA_SICUREZZA_CUMULO"));
    aModel.setMisIdMisuraSicurezzaOrigine (getBigDecimal  ("MIS_ID_MISURA_SICUREZZA_ORIG") ); 
    aModel.setIstDetIdIstitutoDetenzione  (getString    ("IST_DET_ID_ISTITUTO_DETENZIONE") ); 
    aModel.setLuogoEsecuzioneMisura     (getString    ("LUOGO_ESECUZIONE_MISURA") ); 
    
    aModel.setAnnoFascicoloSiepIV       (getBigDecimal  ("ANNO_FASCICOLO_CLASSE_IV"));
    aModel.setNumeroFascicoloSiepIV     (getBigDecimal  ("NUMERO_FASCICOLO_CLASSE_IV"));
    aModel.setCodAutoritaEmittenteIV    (getString      ("COD_AUTORITA_EMITT_CLASSE_IV"));
    aModel.setDescrAutoritaEmittenteIV  (getString      ("AUT_EMI_CLASSE_IV"));
    aModel.setCodLuogoEmittenteIV       (getString      ("LUOGO_AUTORITA_EMITT_CLASSE_IV"));
    aModel.setDescrluogoEmittenteIV     (getString      ("LUO_EMI_CLASSE_IV"));
    aModel.setFlagStatoMisura           (getString      ("FLAG_STATO_MISURA"));
    aModel.setDescrFlagStatoMisura      (getString      ("STATO_MISURA"));
    
    aModel.setMotivoModifica            ( getString     ("MOTIVO_MODIFICA"            ) ); 
    aModel.setFlagStato                 ( getString     ("FLAG_STATO"                 ) ); 
    aModel.setTitIdTitoloCumulato       ( getBigDecimal ("TIT_ID_TITOLO_CUMULATO"     ) ); 
    aModel.setIdMisuraSicurezzaOrigine  ( getBigDecimal ("ID_MISURA_SICUREZZA_ORIGINE") ); 
    aModel.setFlagDatiFinali            ( getString     ("FLAG_DATI_FINALI"           ) ); 
    
    aModel.setCodOperatoreInserimento   ( getString     ("COD_OPERATORE_INSERIMENTO"  ) ); 
    aModel.setDataInserimento           ( getDate       ("DATA_INSERIMENTO"           ) ); 
    aModel.setCodUfficioInserimento     ( getString     ("COD_UFFICIO_INSERIMENTO"    ) ); 
    aModel.setCodOperatoreAggiornamento ( getString     ("COD_OPERATORE_AGGIORNAMENTO") ); 
    aModel.setDataAggiornamento         ( getDate       ("DATA_AGGIORNAMENTO"         ) ); 
    aModel.setCodUfficioAggiornamento   ( getString     ("COD_UFFICIO_AGGIORNAMENTO"  ) ); 

    return aModel;
  }


  /**
   * Aggiunge la condizione di join con la tabella TITOLO_COMULATO 
   * @return
   */
  protected String getSqlQueryJoinTitoloCumulato() {
    String lStatement = new String("");

    lStatement += " SELECT ID_MISURA_SICUREZZA_CUMULO,  "+  
                         " COD_NATURA,  codnatura.RV_MEANING DESCR_NATURA, " +
                         " COD_TIPO, codtipo.RV_MEANING DESCR_TIPO, " +
                         " NUM_ANNI, NUM_MESI, NUM_GIORNI, "+  
                         " ANNO_REG_38, NUM_REG_38, "+  
                         " MISURA_SICUREZZA_CUMULO.MOTIVO_MODIFICA, "+  
                         " MISURA_SICUREZZA_CUMULO.FLAG_STATO, "+  
                         " TIT_ID_TITOLO_CUMULATO, ID_MISURA_SICUREZZA_ORIGINE, "+
                         " FLAG_ANNULLA_MISURA, DATA_FINE_VALIDITA, IST_DET_ID_ISTITUTO_DETENZIONE, "+
                         " LUOGO_ESECUZIONE_MISURA, MIS_ID_MISURA_SICUREZZA_CUMULO, MIS_ID_MISURA_SICUREZZA_ORIG, "+
                         " FLAG_DATI_FINALI, "+
                         
                         " ANNO_FASCICOLO_CLASSE_IV, NUMERO_FASCICOLO_CLASSE_IV, "+
                         " FLAG_STATO_MISURA, statoMisura.RV_MEANING STATO_MISURA, "+
                         " COD_AUTORITA_EMITT_CLASSE_IV, codAutEmi.RV_MEANING AUT_EMI_CLASSE_IV, "+
                         " LUOGO_AUTORITA_EMITT_CLASSE_IV, COMUNE.DESCRIZIONE LUO_EMI_CLASSE_IV, "+                         
                         
                         " MISURA_SICUREZZA_CUMULO.COD_OPERATORE_INSERIMENTO, MISURA_SICUREZZA_CUMULO.DATA_INSERIMENTO, MISURA_SICUREZZA_CUMULO.COD_UFFICIO_INSERIMENTO, "+  
                         " MISURA_SICUREZZA_CUMULO.COD_OPERATORE_AGGIORNAMENTO, MISURA_SICUREZZA_CUMULO.DATA_AGGIORNAMENTO, MISURA_SICUREZZA_CUMULO.COD_UFFICIO_AGGIORNAMENTO "; 
    lStatement += "  FROM MISURA_SICUREZZA_CUMULO, CG_REF_CODES codnatura, CG_REF_CODES codtipo ";
    lStatement += "     , TITOLO_CUMULATO ";
    lStatement += "     , CG_REF_CODES STATOMISURA, CG_REF_CODES CODAUTEMI, UFFICIO Uff, COMUNE ";
    
    lStatement += " WHERE (nvl(MISURA_SICUREZZA_CUMULO.COD_NATURA,'-') = CODNATURA.RV_LOW_VALUE AND CODNATURA.RV_DOMAIN = 'NATURA_MISURA_SICUREZZA' ) " ;
    lStatement +=   " AND (nvl(MISURA_SICUREZZA_CUMULO.COD_TIPO,'-') = CODTIPO.RV_LOW_VALUE AND CODTIPO.RV_DOMAIN = 'TIPO_MISURA_SICUREZZA' ) " ;
    lStatement +=   " AND (TIT_ID_TITOLO_CUMULATO = TITOLO_CUMULATO.ID_TITOLO_CUMULATO) " ;

    lStatement +=   " AND (MISURA_SICUREZZA_CUMULO.FLAG_STATO_MISURA = STATOMISURA.RV_LOW_VALUE AND STATOMISURA.RV_DOMAIN = 'STATO_MISURA_CUMULO' ) ";
    
    lStatement +=   " AND (nvl (MISURA_SICUREZZA_CUMULO.COD_AUTORITA_EMITT_CLASSE_IV, '-') = Uff.COD_UFFICIO  ";
    lStatement +=   " AND Uff.COD_TIPO_UFFICIO = CODAUTEMI.RV_LOW_VALUE AND CODAUTEMI.RV_DOMAIN = 'TIPO_UFFICIO') ";
    
    lStatement +=   " AND (nvl (MISURA_SICUREZZA_CUMULO.LUOGO_AUTORITA_EMITT_CLASSE_IV, '-') =  COMUNE.COD_COMUNE )";
    
    
    
    return lStatement;
  }

  /**
   * Aggiunge la condizione di join con la tabella RICHPM_MISSICUR_CUM 
   * @return
   */
  protected String getSqlQueryJoinRichiestaGE() 
  {
	  String lStatement = new String("");

	  lStatement += " SELECT ID_MISURA_SICUREZZA_CUMULO,  "+  
			  " COD_NATURA,  codnatura.RV_MEANING DESCR_NATURA, " +
			  " COD_TIPO, codtipo.RV_MEANING DESCR_TIPO, " +
			  " NUM_ANNI, NUM_MESI, NUM_GIORNI, "+  
			  " ANNO_REG_38, NUM_REG_38, "+  
			  " MOTIVO_MODIFICA, "+  
			  " FLAG_STATO, "+  
			  " TIT_ID_TITOLO_CUMULATO, ID_MISURA_SICUREZZA_ORIGINE, "+
			  " FLAG_ANNULLA_MISURA, DATA_FINE_VALIDITA, IST_DET_ID_ISTITUTO_DETENZIONE, "+
			  " LUOGO_ESECUZIONE_MISURA, MIS_ID_MISURA_SICUREZZA_CUMULO, MIS_ID_MISURA_SICUREZZA_ORIG, "+
			  " FLAG_DATI_FINALI, "+
	                         
	          " ANNO_FASCICOLO_CLASSE_IV, NUMERO_FASCICOLO_CLASSE_IV, "+
	          " FLAG_STATO_MISURA, statoMisura.RV_MEANING STATO_MISURA, "+
	          " COD_AUTORITA_EMITT_CLASSE_IV, codAutEmi.RV_MEANING AUT_EMI_CLASSE_IV, "+
	          " LUOGO_AUTORITA_EMITT_CLASSE_IV, COMUNE.DESCRIZIONE LUO_EMI_CLASSE_IV, "+
	                         
	          " COD_OPERATORE_INSERIMENTO, DATA_INSERIMENTO, COD_UFFICIO_INSERIMENTO, "+  
	          " COD_OPERATORE_AGGIORNAMENTO, DATA_AGGIORNAMENTO, COD_UFFICIO_AGGIORNAMENTO "; 

	    lStatement += " FROM MISURA_SICUREZZA_CUMULO, RICHPM_MISSICUR_CUM,";
	    lStatement += " CG_REF_CODES codnatura, CG_REF_CODES codtipo, ";
	    lStatement += 	"  CG_REF_CODES STATOMISURA, CG_REF_CODES CODAUTEMI, UFFICIO Uff, COMUNE ";
	    lStatement += " WHERE (nvl(MISURA_SICUREZZA_CUMULO.COD_NATURA,'-') = CODNATURA.RV_LOW_VALUE AND CODNATURA.RV_DOMAIN = 'NATURA_MISURA_SICUREZZA' ) " ;
	    lStatement += 	"  AND (nvl(MISURA_SICUREZZA_CUMULO.COD_TIPO,'-') = CODTIPO.RV_LOW_VALUE AND CODTIPO.RV_DOMAIN = 'TIPO_MISURA_SICUREZZA' ) " ;
	    lStatement += " AND MISURA_SICUREZZA_CUMULO.FLAG_STATO_MISURA = STATOMISURA.RV_LOW_VALUE AND STATOMISURA.RV_DOMAIN = 'STATO_MISURA_CUMULO' ";
	    lStatement += " AND (nvl (MISURA_SICUREZZA_CUMULO.COD_AUTORITA_EMITT_CLASSE_IV, '-') = Uff.COD_UFFICIO  ";
	    lStatement += " AND Uff.COD_TIPO_UFFICIO = CODAUTEMI.RV_LOW_VALUE AND CODAUTEMI.RV_DOMAIN = 'TIPO_UFFICIO') ";
	    lStatement += " AND (nvl (MISURA_SICUREZZA_CUMULO.LUOGO_AUTORITA_EMITT_CLASSE_IV, '-') =  COMUNE.COD_COMUNE )";
	   
	    return lStatement;
	  }
  
  
  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public String setCondizioni(MisuraSicurezzaCumuloModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdMisuraSicurezzaCumulo() != null ) { 
      lCondizioni += " and ID_MISURA_SICUREZZA_CUMULO = " + aModel.getIdMisuraSicurezzaCumulo() + ""; 
    } 
    if (aModel.getCodNatura() != null && aModel.getCodNatura().length() > 0) { 
      lCondizioni += " and COD_NATURA = '" + aModel.getCodNatura() + "' "; 
    } 
    if (aModel.getCodTipo() != null && aModel.getCodTipo().length() > 0) { 
      lCondizioni += " and COD_TIPO = '" + aModel.getCodTipo() + "' "; 
    } 
    if (aModel.getNumAnni() != null ) { 
      lCondizioni += " and NUM_ANNI = " + aModel.getNumAnni() + ""; 
    } 
    if (aModel.getNumMesi() != null ) { 
      lCondizioni += " and NUM_MESI = " + aModel.getNumMesi() + ""; 
    } 
    if (aModel.getNumGiorni() != null ) { 
      lCondizioni += " and NUM_GIORNI = " + aModel.getNumGiorni() + ""; 
    } 
    if (aModel.getAnnoReg38() != null ) { 
      lCondizioni += " and ANNO_REG_38 = " + aModel.getAnnoReg38() + ""; 
    } 
    if (aModel.getNumReg38() != null ) { 
      lCondizioni += " and NUM_REG_38 = " + aModel.getNumReg38() + ""; 
    } 
    if (aModel.getMotivoModifica() != null && aModel.getMotivoModifica().length() > 0) { 
      lCondizioni += " and MOTIVO_MODIFICA = '" + aModel.getMotivoModifica() + "' "; 
    } 
    if (aModel.getFlagStato() != null && aModel.getFlagStato().length() > 0) { 
      lCondizioni += " and FLAG_STATO = '" + aModel.getFlagStato() + "' "; 
    }
    if (aModel.getTitIdTitoloCumulato() != null ) { 
      lCondizioni += " and TIT_ID_TITOLO_CUMULATO = " + aModel.getTitIdTitoloCumulato() + ""; 
    } 
    if (aModel.getIdMisuraSicurezzaOrigine()!= null ) { 
      lCondizioni += " and ID_MISURA_SICUREZZA_ORIGINE = " + aModel.getIdMisuraSicurezzaOrigine() + ""; 
    } 
    if (aModel.getFlagDatiFinali() != null && aModel.getFlagDatiFinali().length() > 0) { 
      lCondizioni += " and FLAG_DATI_FINALI = '" + aModel.getFlagDatiFinali() + "' "; 
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
  public String setCondizioniByKey( BigDecimal aIdMisuraSicurezzaCumulo  ) {
    String lCondizioni = new String();

    lCondizioni += " and ID_MISURA_SICUREZZA_CUMULO = " + aIdMisuraSicurezzaCumulo;

    return lCondizioni;
  }


  /***************************************************************************** 
   * Metodo per la costruzione della sezione order by 
   * @return 
   ****************************************************************************/ 
  protected String getOrderBy() { 
    String orderBy = new String(""); 
    orderBy = " ORDER BY ID_MISURA_SICUREZZA_CUMULO ASC "; 
    return orderBy; 
  }
  
  // Imposta la condizione per ForeignKey Tit_Id_Titolo_Cumulato
  public String setCondizioniByTitIdTitolo( BigDecimal aIdTitolo  )
  {
	    String lCondizioni = new String();
	    lCondizioni += " and TIT_ID_TITOLO_CUMULATO = " + aIdTitolo;
	    return lCondizioni;
  }
  
}	// Chiude il SqlDao

