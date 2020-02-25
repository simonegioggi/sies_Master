package siap.siep.modulocumulo.dao;

/**
* <p>Title: RichiestePmInCumuloSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella RichiestePmInCumulo</p>
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

import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;

public class RichiestePmInCumuloSqlDAO extends SqlDAO {
  /***************************************************************************** 
   * Costruttore  
   * @param con 
   ****************************************************************************/ 
  public RichiestePmInCumuloSqlDAO (Connection con) {
    super(con);
  }


  /***************************************************************************** 
   * Restituisce il numero di record dell'operazione di ricerca costruendo 
   * la clausola where con lo stesso model utilizzato per la ricerca 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void getCountRichiestePmInCumulo(RichiestePmInCumuloModel aModel) throws DAOException {
    // Costruisce lo statement da eseguire 
    String lStatement ="SELECT COUNT(*) HowManyRecords FROM RICHIESTE_PM_IN_CUMULO ";

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
  public void ricercaRichiestePmInCumuloPaged(RichiestePmInCumuloModel aModel, int aPage) throws DAOException { 
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
  public void ricercaRichiestePmInCumulo( RichiestePmInCumuloModel  aModel)  throws DAOException {
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
  
  public void ricercaRichiestePmInCumuloByIdIstruttoria( BigDecimal  aIdIstru)  throws DAOException 
  {
	  // Recupera la select...from 
	  String lSql = getSqlQuery();
	  
	  lSql += setCondizioniByIdIstru( aIdIstru);
	  lSql += " "+getOrderBy()+" "; 

	  // Imposta lo statement da eseguire 
	  setStatement(lSql);
  }
  
  public void ricercaRichiestePmInCumuloByIdIstruttoria( BigDecimal  aIdIstru, String aCodTipoRic)  throws DAOException 
  {
	  // Recupera la select...from 
	  String lSql = getSqlQuery();
	  
	  lSql += setCondizioniByIdIstru( aIdIstru);
	  lSql += " and COD_TIPO_RICHIESTA = "+aCodTipoRic;
	  lSql += " ORDER BY RICHIESTE_PM_IN_CUMULO.DATA_EMISSIONE DESC"; 

	  
	  // Imposta lo statement da eseguire 
	  setStatement(lSql);
  }

  /**
   * 
   * @param aIdTitolo
   * @param aCodTipoRic
   * @param aTipoAnn
   * @throws DAOException
   */
  public void ricercaRichiestePmInCumuloByIdTitoloTipoRich( BigDecimal  aIdTitolo, String aCodTipoRic, String aTipoAnn)  throws DAOException 
  {
    // Recupera la select...from 
    String lSql = getSqlQuery();
    
    //lSql += setCondizioniByIdIstru( aIdIstru);
    lSql += " and TIT_ID_TITOLO_CUMULATO = "+aIdTitolo;
    lSql += " and COD_TIPO_RICHIESTA = '"+aCodTipoRic+"'";
    lSql += " and COD_TIPO_ANNOTAZIONE = '"+aTipoAnn+"'";
    lSql += " ORDER BY RICHIESTE_PM_IN_CUMULO.DATA_EMISSIONE DESC"; 
   
    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }
  
  
  /**
   * Richerca le richieste per istruttoria, tipo richiesta (01=GE, 02=Sorv),
   * tipoANnotazione
   * @param aIdIstruttoria
   * @param aCodTipoRic
   * @param aTipoAnn
   * @throws DAOException
   */
  public void ricercaRichiestePmInCumuloByIdIstruttoriaTipoRich (BigDecimal  aIdIstruttoria, String aCodTipoRic, String aTipoAnn)  throws DAOException 
  {
    // Recupera la select...from 
    String lSql = getSqlQuery();
    
    lSql += " and ISTR_ID_ISTRUTTORIA_CUMULO = "+aIdIstruttoria;
    
    if (aCodTipoRic!=null) 
      lSql += " and COD_TIPO_RICHIESTA = '"+aCodTipoRic+"'";
    
    if (aTipoAnn!=null) 
      lSql += " and COD_TIPO_ANNOTAZIONE = '"+aTipoAnn+"'";
    
    lSql += " ORDER BY RICHIESTE_PM_IN_CUMULO.DATA_EMISSIONE DESC"; 
   
    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }
  
  
  // ====================================================================
  // richieste ANCORA DA INVIARE ( Ric_Id_Richieste_Inviate_Cum = null)
  //=====================================================================
  public void ricercaRichiestePmInCumuloDaInviareByIdIstruttoria( BigDecimal  aIdIstru)  throws DAOException 
  {
	  // Recupera la select...from 
	  String lSql = getSqlQuery();
	  
	  lSql += setCondizioniByIdIstru( aIdIstru);
	  lSql += " and RIC_ID_RICHIESTE_INVIATE_CUM is null ";
	  lSql += " "+getOrderBy()+" "; 

	  // Imposta lo statement da eseguire 
	  setStatement(lSql);
  }

  // =========================================================================================
  // richieste ANCORA DA INVIARE per COD_TIPO_RICHIESTA ( Ric_Id_Richieste_Inviate_Cum = null)
  //==========================================================================================
  public void ricercaRichiestePmInCumuloDaInviareByIdIstruttoria( BigDecimal  aIdIstru, String aCodTipoRic)  throws DAOException 
  {
	  // Recupera la select...from 
	  String lSql = getSqlQuery();
	  
	  lSql += setCondizioniByIdIstru( aIdIstru);
	  lSql += " and RIC_ID_RICHIESTE_INVIATE_CUM is null ";
	  lSql += " and COD_TIPO_RICHIESTA = "+aCodTipoRic;
	 
	  // lSql += " "+getOrderBy()+" "; 
	  lSql += " order by RICHIESTE_PM_IN_CUMULO.DATA_EMISSIONE DESC";
	  
	  // Imposta lo statement da eseguire 
	  setStatement(lSql);
  }
  
  /***************************************************************************** 
   * Metodo che imposta la statement di ricerca per chiave 
   * @param aKey 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaRichiestePmInCumuloByKey( BigDecimal aIdRichiestePmInCumulo) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    lSql += setCondizioniByKey( aIdRichiestePmInCumulo);

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }

  /***************************************************************************************************** 
   * Metodo che imposta lo statement di ricerca RICHIESTE_PM_IN_CUMULO By Ric_Id_Richieste_Inviate_Cum 
   * @param For.Key Ric_Id_Richieste_Inviate_Cum
   * @throws DAOException 
   *****************************************************************************************************/
  public void ricercaRichiestePmInCumuloByIdRichInviateCum( BigDecimal  aIdRichInviate)  throws DAOException 
  {
	  // Recupera la select...from 
	  String lSql = getSqlQuery();
	  
	  lSql += " and RIC_ID_RICHIESTE_INVIATE_CUM = "+aIdRichInviate;
	  lSql += " "+getOrderBy()+" "; 

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
                  "ID_RICHIESTE_PM_IN_CUMULO, "+  
                  "COD_TIPO_RICHIESTA, CODTIPORICHIESTA.RV_MEANING DESC_RICHIESTA,"+  
                  "COD_TIPO_ANNOTAZIONE, CODTIPOANNOTAZIONE.RV_MEANING DESC_BENEFICIO, "+  
                  "RICHIESTE_PM_IN_CUMULO.DATA_EMISSIONE, "+  
                  "FLAG_PIU_MENO_R, "+  
                  "NUM_ANNI_RECLUSIONE_R, "+  
                  "NUM_MESI_RECLUSIONE_R, "+  
                  "NUM_GIORNI_RECLUSIONE_R, "+  
                  "IMPORTO_MULTA_R, "+  
                  "NUM_ANNI_ARRESTO_R, "+  
                  "NUM_MESI_ARRESTO_R, "+  
                  "NUM_GIORNI_ARRESTO_R, "+  
                  "IMPORTO_AMMENDA_R, "+  
                  "FLAG_APP_PROVVISORIA, "+  
                  	"COD_TIPO_PENA_ACCESSORIA, PENAACCESSORIA.RV_MEANING DESCR_PENA_ACC, "+  
                  	"COD_TIPO_DURATA_PA, TIPODURATA.RV_MEANING DESC_TIPO_DURATA, "+  
                  "NUM_ANNI_PA, "+  
                  "NUM_MESI_PA, "+  
                  "NUM_GIORNI_PA, "+  
                  "COD_FONTE, FONTELEG.RV_HIGH_VALUE FONTE_LEGISLATIVA, FONTELEG.RV_MEANING FONTE_LEGISLATIVA_SIGLA, "+   
                  "ANNO_FONTE, "+  
                  "NUMERO_FONTE, "+  
                  "ARTICOLO, "+  
                  "COD_SOTTONUMERAZIONE, BISTER.RV_MEANING DESCR_BISTER, "+  
                  "COMMA, "+  
                  "LETTERA, "+  
                  "NUMERO, "+  
                  "ANNO_CC, "+  
                  "NUMERO_CC, "+  
                  "DATA_CC, "+  
                  "TIPO_ANNOTAZIONE_BENEFICIO, TIPOBENEFICIODPR.RV_MEANING DESC_BENEFICIO_DPR, "+
                  "COD_DPR, CODTIPODPR.RV_MEANING DESC_DECRETO, "+  
                  "MOTIVAZIONI, "+  
                  "NOTE_RECLUSIONE, "+  
                  "COD_MOTIVO, "+
                  		
                  "NUM_GIORNI_LA_REV, "+  
                  "NUM_GIORNI_LS_REV, "+  
                  "NUM_GIORNI_LI_REV, "+  
                  
                  "ISTR_ID_ISTRUTTORIA_CUMULO, "+  
                  "TIT_ID_TITOLO_CUMULATO, "+  
                  "TIT_ID_TITOLO_CUMULATO_REF, "+  
                  "COD_OPERATORE_INSERIMENTO, "+  
                  "DATA_INSERIMENTO, "+  
                  "COD_UFFICIO_INSERIMENTO, "+  
                  "COD_OPERATORE_AGGIORNAMENTO, "+  
                  "DATA_AGGIORNAMENTO, "+  
                  "COD_UFFICIO_AGGIORNAMENTO, " + 
    			  "RIC_ID_RICHIESTE_INVIATE_CUM ";	

    lStatement += " FROM RICHIESTE_PM_IN_CUMULO";
    lStatement +=    " , CG_REF_CODES CODTIPORICHIESTA ";
    lStatement +=    " , CG_REF_CODES CODTIPOANNOTAZIONE ";
    lStatement +=    " , CG_REF_CODES CODTIPODPR ";
    lStatement +=    " , CG_REF_CODES TIPOBENEFICIODPR ";
    lStatement +=    " , CG_REF_CODES FONTELEG ";
    lStatement +=    " , CG_REF_CODES BISTER ";
    lStatement +=    " , CG_REF_CODES TIPODURATA ";
    lStatement +=    " , CG_REF_CODES PENAACCESSORIA ";
    
    lStatement += " WHERE 1=1 ";
    lStatement += " and (     nvl(RICHIESTE_PM_IN_CUMULO.COD_TIPO_RICHIESTA,'-') = CODTIPORICHIESTA.RV_LOW_VALUE AND CODTIPORICHIESTA.RV_DOMAIN = 'TIPO_RICHIESTA_CUMULO' ) "; 
    lStatement += " and (     nvl(RICHIESTE_PM_IN_CUMULO.COD_TIPO_ANNOTAZIONE,'-') = CODTIPOANNOTAZIONE.RV_LOW_VALUE AND CODTIPOANNOTAZIONE.RV_DOMAIN = 'TIPO_ANNOTAZIONE' ) "; 
    lStatement += " and (     nvl(RICHIESTE_PM_IN_CUMULO.COD_DPR,'-') = CODTIPODPR.RV_LOW_VALUE AND CODTIPODPR.RV_DOMAIN = 'DPR' ) "; 
    lStatement += " and (     nvl(RICHIESTE_PM_IN_CUMULO.TIPO_ANNOTAZIONE_BENEFICIO,'-') = TIPOBENEFICIODPR.RV_LOW_VALUE AND TIPOBENEFICIODPR.RV_DOMAIN = 'TIPO_ANNOTAZIONE' ) ";
    
    lStatement += " and (     nvl(RICHIESTE_PM_IN_CUMULO.COD_FONTE,'-') = FONTELEG.RV_LOW_VALUE AND FONTELEG.RV_DOMAIN = 'FONTE' ) "; 
    lStatement += " and (     nvl(RICHIESTE_PM_IN_CUMULO.COD_SOTTONUMERAZIONE,'-') = BISTER.RV_LOW_VALUE AND BISTER.RV_DOMAIN = 'SOTTONUMERAZIONE' ) "; 
    
    lStatement += " and ( nvl(RICHIESTE_PM_IN_CUMULO.COD_TIPO_DURATA_PA,'-') = TIPODURATA.RV_LOW_VALUE AND TIPODURATA.RV_DOMAIN = 'TIPO_DURATA' ) ";
    lStatement += " and ( nvl(RICHIESTE_PM_IN_CUMULO.COD_TIPO_PENA_ACCESSORIA, '-') = PENAACCESSORIA.RV_LOW_VALUE AND PENAACCESSORIA.RV_DOMAIN = 'TIPO_PENA_ACCESSORIA' ) ";


    return lStatement;
  }


  /***************************************************************************** 
   * Metodo che carica il record del result set nel model 
   * @return 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel  getModel() throws DAOException {
     RichiestePmInCumuloModel aModel = new  RichiestePmInCumuloModel(); 

    //Inserire le opportune set delle descrizioni!
    aModel.setIdRichiestePmInCumulo     ( getBigDecimal ("ID_RICHIESTE_PM_IN_CUMULO"    ) ); 
    aModel.setCodTipoRichiesta          ( getString     ("COD_TIPO_RICHIESTA"           ) ); 
aModel.setDescrTipoRichiesta(getString("DESC_RICHIESTA") );
    aModel.setCodTipoAnnotazione        ( getString     ("COD_TIPO_ANNOTAZIONE"         ) ); 
aModel.setDescrTipoAnnotazione(getString("DESC_BENEFICIO") );
	aModel.setDataEmissione             ( getDate       ("DATA_EMISSIONE"               ) ); 
    aModel.setFlagPiuMenoR              ( getString     ("FLAG_PIU_MENO_R"              ) ); 
    aModel.setNumAnniReclusioneR        ( getBigDecimal ("NUM_ANNI_RECLUSIONE_R"        ) ); 
    aModel.setNumMesiReclusioneR        ( getBigDecimal ("NUM_MESI_RECLUSIONE_R"        ) ); 
    aModel.setNumGiorniReclusioneR      ( getBigDecimal ("NUM_GIORNI_RECLUSIONE_R"      ) ); 
    aModel.setImportoMultaR             ( getBigDecimal ("IMPORTO_MULTA_R"              ) ); 
    aModel.setNumAnniArrestoR           ( getBigDecimal ("NUM_ANNI_ARRESTO_R"           ) ); 
    aModel.setNumMesiArrestoR           ( getBigDecimal ("NUM_MESI_ARRESTO_R"           ) ); 
    aModel.setNumGiorniArrestoR         ( getBigDecimal ("NUM_GIORNI_ARRESTO_R"         ) ); 
    aModel.setImportoAmmendaR           ( getBigDecimal ("IMPORTO_AMMENDA_R"            ) ); 
    aModel.setFlagAppProvvisoria        ( getString     ("FLAG_APP_PROVVISORIA"         ) ); 
    aModel.setCodTipoPenaAccessoria     ( getString     ("COD_TIPO_PENA_ACCESSORIA"     ) ); 
aModel.setDescrTipoPenaAccessoria(getString("DESCR_PENA_ACC") );
    aModel.setCodTipoDurataPa           ( getString     ("COD_TIPO_DURATA_PA"           ) ); 
aModel.setDescrTipoDurataPa(getString("DESC_TIPO_DURATA") );
    aModel.setNumAnniPa                 ( getBigDecimal ("NUM_ANNI_PA"                  ) ); 
    aModel.setNumMesiPa                 ( getBigDecimal ("NUM_MESI_PA"                  ) ); 
    aModel.setNumGiorniPa               ( getBigDecimal ("NUM_GIORNI_PA"                ) ); 
    aModel.setCodFonte                  ( getString     ("COD_FONTE"                    ) ); 
aModel.setDescrFonte(getString("FONTE_LEGISLATIVA") );
aModel.setDescrFonteSigla (getString("FONTE_LEGISLATIVA_SIGLA") );
    aModel.setAnnoFonte                 ( getBigDecimal ("ANNO_FONTE"                   ) ); 
    aModel.setNumeroFonte               ( getString     ("NUMERO_FONTE"                 ) ); 
    aModel.setArticolo                  ( getString     ("ARTICOLO"                     ) ); 
    aModel.setCodSottonumerazione       ( getString     ("COD_SOTTONUMERAZIONE"         ) ); 
aModel.setDescrSottonumerazione(getString("DESCR_BISTER") );
    aModel.setComma                     ( getString     ("COMMA"                        ) ); 
    aModel.setLettera                   ( getString     ("LETTERA"                      ) ); 
    aModel.setNumero                    ( getString     ("NUMERO"                       ) ); 
    aModel.setAnnoCc                    ( getBigDecimal ("ANNO_CC"                      ) ); 
    aModel.setNumeroCc                  ( getString     ("NUMERO_CC"                    ) ); 
    aModel.setDataCc                    ( getDate       ("DATA_CC"                      ) );
    
    aModel.setCodTipoBeneficio			( getString 	("TIPO_ANNOTAZIONE_BENEFICIO"	) );
aModel.setDescrBeneficio(getString("DESC_BENEFICIO_DPR") );    
    aModel.setCodDpr                    ( getString     ("COD_DPR"                      ) ); 
    aModel.setDescrDpr					( getString		("DESC_DECRETO") );
    aModel.setMotivazioni               ( getString     ("MOTIVAZIONI"                  ) ); 
    aModel.setNoteReclusione            ( getString     ("NOTE_RECLUSIONE"              ) ); 

    aModel.setNumGiorniRevocaLA			( getBigDecimal ("NUM_GIORNI_LA_REV"         	) ); 
    aModel.setNumGiorniRevocaLS     ( getBigDecimal ("NUM_GIORNI_LS_REV"          ) ); 
    aModel.setNumGiorniRevocaLI     ( getBigDecimal ("NUM_GIORNI_LI_REV"          ) ); 
    aModel.setCodMotivo					( getString		("COD_MOTIVO"					) );
    
    aModel.setIstrIdIstruttoriaCumulo   ( getBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO"   ) ); 
    aModel.setTitIdTitoloCumulato       ( getBigDecimal ("TIT_ID_TITOLO_CUMULATO"       ) ); 
    aModel.setTitIdTitoloCumulatoRef    ( getBigDecimal ("TIT_ID_TITOLO_CUMULATO_REF"   ) ); 
    aModel.setCodOperatoreInserimento   ( getString     ("COD_OPERATORE_INSERIMENTO"    ) ); 
    aModel.setDataInserimento           ( getDate       ("DATA_INSERIMENTO"             ) ); 
    aModel.setCodUfficioInserimento     ( getString     ("COD_UFFICIO_INSERIMENTO"      ) ); 
    aModel.setCodOperatoreAggiornamento ( getString     ("COD_OPERATORE_AGGIORNAMENTO"  ) ); 
    aModel.setDataAggiornamento         ( getDate       ("DATA_AGGIORNAMENTO"           ) ); 
    aModel.setCodUfficioAggiornamento   ( getString     ("COD_UFFICIO_AGGIORNAMENTO"    ) ); 
    aModel.setRicIdRichiesteInviateCum	( getBigDecimal	("RIC_ID_RICHIESTE_INVIATE_CUM" ) );
    return aModel;
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public String setCondizioni(RichiestePmInCumuloModel aModel) {
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
    if (aModel.getNumGiorniRevocaLA() != null ) { 
      lCondizioni += " and NUM_GIORNI_LA_REV = " + aModel.getNumGiorniRevocaLA() + ""; 
    }
    if (aModel.getNumGiorniRevocaLS() != null ) { 
      lCondizioni += " and NUM_GIORNI_LS_REV = " + aModel.getNumGiorniRevocaLS() + ""; 
    } 
    if (aModel.getNumGiorniRevocaLI() != null ) { 
      lCondizioni += " and NUM_GIORNI_LI_REV = " + aModel.getNumGiorniRevocaLI() + ""; 
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
  public String setCondizioniByKey( BigDecimal aIdRichiestePmInCumulo  ) {
    String lCondizioni = new String();

    lCondizioni += " and ID_RICHIESTE_PM_IN_CUMULO = " + aIdRichiestePmInCumulo;

    // Elimino il primo and 
  //  if (lCondizioni.length() > 0) { 
  //    lCondizioni = lCondizioni.substring(4); 
  //  } 

    return lCondizioni;
  }
  
  public String setCondizioniByIdIstru( BigDecimal aIdIstruttoria  ) 
  {
	  String lCondizioni = new String();
	  lCondizioni += " and ISTR_ID_ISTRUTTORIA_CUMULO = " + aIdIstruttoria;

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
