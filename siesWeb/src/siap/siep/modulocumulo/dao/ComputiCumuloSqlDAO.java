package siap.siep.modulocumulo.dao;

/**
* <p>Title: ComputiCumuloSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella ComputiCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.siep.modulocumulo.model.ComputiCumuloModel;

public class ComputiCumuloSqlDAO extends SqlDAO {
  /***************************************************************************** 
   * Costruttore  
   * @param con 
   ****************************************************************************/ 
  public ComputiCumuloSqlDAO (Connection con) {
    super(con);
  }


  /***************************************************************************** 
   * Restituisce il numero di record dell'operazione di ricerca costruendo 
   * la clausola where con lo stesso model utilizzato per la ricerca 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void getCountComputiCumulo(ComputiCumuloModel aModel) throws DAOException {
    // Costruisce lo statement da eseguire 
    String lStatement ="SELECT COUNT(*) HowManyRecords FROM COMPUTI_CUMULO ";

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
  public void ricercaComputiCumuloPaged(ComputiCumuloModel aModel, int aPage) throws DAOException { 
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
  public void ricercaComputiCumulo( ComputiCumuloModel  aModel)  throws DAOException {
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
  public void ricercaComputiCumuloByKey( BigDecimal aIdComputiCumulo) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " AND " + setCondizioniByKey( aIdComputiCumulo);

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }

  public void ricercaComputiCumuloByIdIstruttoria ( BigDecimal aIdIstruttoria) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " AND ISTR_ID_ISTRUTTORIA_CUMULO = " + aIdIstruttoria;

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }
  
  public void ricercaComputiCumuloByIdIstruttoriaDatiFinali ( BigDecimal aIdIstruttoria, BigDecimal aIdDatiFinali) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " AND ISTR_ID_ISTRUTTORIA_CUMULO = " + aIdIstruttoria;
    lSql += " AND DAT_ID_DATI_FINALI_CUMULO = " + aIdDatiFinali;

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }  
  
  public void ricercaComputiCumuloByIdStatoEsec ( BigDecimal aIdStatoEsec) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " AND STAT_ID_STATO_ESEC_TIT_CUM = " + aIdStatoEsec;
    
    lSql += " ORDER BY DATA_RECLUSIONE_DA ASC";

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }
  
  public void ricercaComputiCumuloByIdTitoloCum ( BigDecimal aIdTitolo) throws DAOException {
	    // Recupera la select...from 
	    String lSql = getSqlQuery();
	    
	    // Aggiunge condizione con COD_MOTIVO (in Computi_Cumulo = COD_OGGETTO_DECISIONE) 
	    lSql += " AND COD_OGGETTO_DECISIONE IN  ( '0002', '0003', '0001', '2005', '2007', '2008', '2006', '0025', '0361', '0013', '0005','0010', '0362', '0195', '0012', '0011', '0610', '2630', '0004', '2245' )" ; 
	    // Aggiunge condizione su ESITO (in Computi_Cumulo = COD_TIPO_MISURA)
	    lSql += " AND COD_TIPO_MISURA IN  ( 'CO','ED','AC','PP') "; 
	    
	    // Aggiunge le where condition per chiave 
	    lSql += " AND TIT_ID_TITOLO_CUMULATO = " + aIdTitolo;
	    lSql += " ORDER BY DATA_INSERIMENTO DESC";

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
                  "ID_COMPUTI_CUMULO, "+  
                  "COD_TIPO_ANNOTAZIONE, TIPOANNOTAZIONE.RV_MEANING DESC_TIPO_ANNOTAZIONE, "+  
                  "COD_CAUSALE_COMPUTO, CAUSALECOMPUTO.RV_MEANING DESC_CAUSALE_COMPUTO, "+  
                  "FLAG_PIU_MENO, "+  
                  
                  "DATA_RECLUSIONE_DA, DATA_RECLUSIONE_A, "+  
                  "NUM_ANNI_RECLUSIONE, NUM_MESI_RECLUSIONE, NUM_GIORNI_RECLUSIONE, "+  
                  "IMPORTO_MULTA, "+  
                  
                  "DATA_ARRESTO_DA, DATA_ARRESTO_A, "+  
                  "NUM_ANNI_ARRESTO, NUM_MESI_ARRESTO, NUM_GIORNI_ARRESTO, "+  
                  "IMPORTO_AMMENDA, "+
                  
                  "NUM_GIORNI_MAP, "+
                  
                  "COD_DPR, DPR.RV_MEANING DESC_DPR, "+
                  "DATA_RICHIESTA, "+
                  "NOTE, "+
                  
                  "COD_TIPO_MISURA, CODTIPOMISURA.RV_MEANING DESC_TIPO_MISURA, "+
                  "IST_DET_ID_ISTITUTO_DETENZIONE, "+
                  "ALTRO_LUOGO_DETENZIONE, "+

                  "FLAG_STATO, "+  
                  "MOTIVO_MODIFICA, "+  
                  "TIT_ID_TITOLO_CUMULATO, "+  
                  "DAT_ID_DATI_FINALI_CUMULO, "+  
                  "ISTR_ID_ISTRUTTORIA_CUMULO, "+  
                  "STAT_ID_STATO_ESEC_TIT_CUM, "+  

                  "DATA_EMISSIONE_PROVV, "+  
                  "DATA_RICEZIONE_PROVV, "+  
                  "ANNO_PROVV, "+  
                  "PROGR_PROVV, "+ 

                  "COD_UFFICIO_EMITTENTE_PROVV, TIPO_UFF.RV_MEANING DescUfficioEmittenteProvv, "+
                  "COD_LUOGO_UFFICIO_PROVV, COMUNE.DESCRIZIONE DescLuogoUfficioEmittenteProvv, "+
                  "SEZIONE_PROVV, "+  
                  "COD_TIPO_PROVV, "+ 

                  "REA_ID_REATO_CUM, "+  

                  "COD_FONTE, "+  
                  "ANNO_FONTE, "+  
                  "NUMERO_FONTE, "+  
                  "COD_SOTTONUMERAZIONE, "+  
                  "COMMA, "+  
                  "LETTERA, "+  
                  "NUMERO, "+  
                  "ARTICOLO, "+  

                  "COD_TIPO_REGISTRO_ORDINANZA, CODREGORD.RV_LOW_VALUE||' ('|| CODREGORD.RV_MEANING||')' descTipoRegistroOrdinanza, "+
                  "DATA_SOSPENSIONE_INTERRUZIONE, "+
                  "COD_OGGETTO_DECISIONE, CODOGGETTODEC.RV_MEANING descOggettoDecisione, "+

                  "PROTOCOLLO, "+
                  "ALTRA_AUTORITA, "+
                  "ALTRO_LUOGO, "+
                  
                  "LUOGO_ESEC_MISURA, "+
                  "DATA_INIZIO_MISURA, "+
                  "DATA_FINE_MISURA, "+
                  "NUM_ANNI_MISURA, "+
                  "NUM_MESI_MISURA, "+
                  "NUM_GIORNI_MISURA, "+
                  
                  "DATA_INIZIO_REVOCA, "+
                  "NUM_ANNI_REVOCA_RECLUSIONE, "+
                  "NUM_MESI_REVOCA_RECLUSIONE, "+
                  "NUM_GIORNI_REVOCA_RECLUSIONE, "+
                  "NUM_ANNI_REVOCA_ARRESTO, "+
                  "NUM_MESI_REVOCA_ARRESTO, "+
                  "NUM_GIORNI_REVOCA_ARRESTO, "+

                  "DATA_INGRESSO_ISTITUTO, "+
                  "DATA_SCARCERAZIONE, "+
                  "FLAG_DECISIONE_TRIBUNALE, "+
                  "COD_TDS_COMPETENTE, "+
                  
                  "COD_OPERATORE_INSERIMENTO, "+  
                  "DATA_INSERIMENTO, "+  
                  "COD_UFFICIO_INSERIMENTO, "+  
                  "COD_OPERATORE_AGGIORNAMENTO, "+  
                  "DATA_AGGIORNAMENTO, "+  
                  "COD_UFFICIO_AGGIORNAMENTO, "+  
    
                  "ANNO_PROC, PROGR_PROC, "+   // -- (Vanno insieme ai campi _PROVV) 
                  
                  "ANNO_REGE_PM, NUMERO_REGE_PM, "+           //   -- Rege_PM
                  "COD_TIPO_UFFICIO_PM, TIPO_UFF_PM.RV_MEANING DescrTipoUfficioPM, "+
                  "COD_SEDE_UFFICIO_PM, SEDE_UFF_PM.DESCRIZIONE DescrSedeUfficioPM, "+
                  
                  "ANNO_BDMC, NUMERO_BDMC, ANNO_REGE, NUMERO_REGE, TIPO_REGE, "+ //    -- BDMC e Rege
                  "TIPO_AUT_REGE, TIPAUTO_REGE.RV_MEANING DescrTipoAutoritaRege, "+
                  "COD_SEDE_REGE, SEDEAUTO_REGE.DESCRIZIONE DescrLuogoAutoritaRege, DATA_REGE, "+
                  
                   // MEV_2025-48 - ALTRO – Benefici con anticipazione effetti                  
                   "FLAG_APP_PROVVISORIA, "+  
                  
                  
                  "CHIAVE_ANNO_SIEP, CHIAVE_NUMERO_SIEP, CHIAVE_UFFICIO_SIEP, ANNO_SENTENZA, NUMERO_SENTENZA, DATA_SENTENZA, "+    // Campi sentenza
                  "COD_TIPO_AUT_EMITT_SENTENZA, TIPO_AUTO.RV_MEANING DescrTipoAutoritaEmittente, "+
                  "COD_LUOGO_EMITTENTE_SENTENZA, LUOGO_AUTO.DESCRIZIONE DescrLuogoEmittente "; 


    // Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
    lStatement += " FROM COMPUTI_CUMULO ";
    lStatement +=     "LEFT OUTER JOIN CG_REF_CODES TIPOANNOTAZIONE ON TIPOANNOTAZIONE.RV_LOW_VALUE = COD_TIPO_ANNOTAZIONE "+
                                                                    " AND TIPOANNOTAZIONE.RV_DOMAIN = 'TIPO_ANNOTAZIONE' ";

    lStatement +=     "LEFT OUTER JOIN CG_REF_CODES CAUSALECOMPUTO ON CAUSALECOMPUTO.RV_LOW_VALUE = COD_CAUSALE_COMPUTO "+
                                                                " AND CAUSALECOMPUTO.RV_DOMAIN = 'CAUSALE_COMPUTO' ";
    
    lStatement +=     "LEFT OUTER JOIN CG_REF_CODES DPR ON DPR.RV_LOW_VALUE = COD_DPR "+
                                                     " AND DPR.RV_DOMAIN = 'DPR' ";
    
    lStatement +=     "LEFT OUTER JOIN CG_REF_CODES CODTIPOMISURA ON CODTIPOMISURA.RV_LOW_VALUE = COD_TIPO_MISURA "+
                                                               " AND CODTIPOMISURA.RV_DOMAIN = 'TIPO_MISURA_CAUTELARE' ";

    // n.b. nel campo è momorizzato l'RV_ABBREVIATION e non RV_LOW_VALUE per cui nella join va aggiunta la condizione
    //      su RV_HIGH_VALUE perchè non si lavora in chiave e si porebbero ottenere più record
    lStatement +=     "LEFT OUTER JOIN CG_REF_CODES CODOGGETTODEC ON CODOGGETTODEC.RV_ABBREVIATION = COD_OGGETTO_DECISIONE "+
                                                               " AND CODOGGETTODEC.RV_HIGH_VALUE = COD_TIPO_REGISTRO_ORDINANZA "+
                                                               " AND CODOGGETTODEC.RV_DOMAIN = 'OGGETTO_SOSPENSIONI' ";
    lStatement +=     "LEFT OUTER JOIN CG_REF_CODES CODREGORD ON CODREGORD.RV_HIGH_VALUE = COD_TIPO_REGISTRO_ORDINANZA "+
                                                           " AND CODREGORD.RV_DOMAIN = 'TIPO_REGISTRO_ORDINANZA' ";

    
    // Fungibilita
   // -- Campi PTOVV
    lStatement += " LEFT OUTER JOIN UFFICIO ON UFFICIO.COD_UFFICIO = COD_UFFICIO_EMITTENTE_PROVV "+
                  " LEFT OUTER JOIN CG_REF_CODES TIPO_UFF ON TIPO_UFF.RV_LOW_VALUE = UFFICIO.COD_TIPO_UFFICIO "+
                    " AND TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' "+
                  " LEFT OUTER JOIN COMUNE ON COMUNE.COD_COMUNE = COD_LUOGO_UFFICIO_PROVV ";
    //-- Campi SENTENZA
    lStatement += " LEFT OUTER JOIN CG_REF_CODES TIPO_AUTO ON TIPO_AUTO.RV_LOW_VALUE = COD_TIPO_AUT_EMITT_SENTENZA "+ 
                                                       " AND TIPO_AUTO.RV_DOMAIN = 'TIPO_UFFICIO' "+
                  " LEFT OUTER JOIN COMUNE LUOGO_AUTO ON LUOGO_AUTO.COD_COMUNE = COD_LUOGO_EMITTENTE_SENTENZA ";
    //-- Campi REGE_PM
    lStatement += " LEFT OUTER JOIN CG_REF_CODES TIPO_UFF_PM ON TIPO_UFF_PM.RV_LOW_VALUE = COD_TIPO_UFFICIO_PM "+
                                                          " AND TIPO_UFF_PM.RV_DOMAIN = 'TIPO_UFFICIO' "+
                  " LEFT OUTER JOIN COMUNE SEDE_UFF_PM ON SEDE_UFF_PM.COD_COMUNE = COD_SEDE_UFFICIO_PM ";

    //-- Campi BDMC e Rege
    lStatement += " LEFT OUTER JOIN CG_REF_CODES TIPAUTO_REGE ON TIPAUTO_REGE.RV_LOW_VALUE = TIPO_AUT_REGE "+
                                                           " AND TIPAUTO_REGE.RV_DOMAIN = 'TIPO_UFFICIO' "+
                  " LEFT OUTER JOIN COMUNE SEDEAUTO_REGE ON SEDEAUTO_REGE.COD_COMUNE = COD_SEDE_REGE ";
    
    lStatement += " WHERE 1=1 ";
    return lStatement;
  }


  /***************************************************************************** 
   * Metodo che carica il record del result set nel model 
   * @return 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel  getModel() throws DAOException {
    ComputiCumuloModel aModel = new  ComputiCumuloModel(); 

    //Inserire le opportune set delle descrizioni!
    aModel.setIdComputiCumulo           ( getBigDecimal ("ID_COMPUTI_CUMULO"          ) ); 
    aModel.setCodTipoAnnotazione        ( getString     ("COD_TIPO_ANNOTAZIONE"       ) ); 
    aModel.setDescrTipoAnnotazione      ( getString     ("DESC_TIPO_ANNOTAZIONE") );
    aModel.setCodCausaleComputo         ( getString     ("COD_CAUSALE_COMPUTO"        ) ); 
    aModel.setDescrCausaleComputo       ( getString     ("DESC_CAUSALE_COMPUTO") );
    aModel.setFlagPiuMeno               ( getString     ("FLAG_PIU_MENO"              ) ); 
    
    aModel.setDataReclusioneDa          ( getDate       ("DATA_RECLUSIONE_DA"         ) ); 
    aModel.setDataReclusioneA           ( getDate       ("DATA_RECLUSIONE_A"          ) ); 
    aModel.setNumAnniReclusione         ( getBigDecimal ("NUM_ANNI_RECLUSIONE"        ) ); 
    aModel.setNumMesiReclusione         ( getBigDecimal ("NUM_MESI_RECLUSIONE"        ) ); 
    aModel.setNumGiorniReclusione       ( getBigDecimal ("NUM_GIORNI_RECLUSIONE"      ) ); 
    aModel.setImportoMulta              ( getBigDecimal ("IMPORTO_MULTA"              ) ); 
    
    aModel.setDataArrestoDa             ( getDate       ("DATA_ARRESTO_DA"            ) ); 
    aModel.setDataArrestoA              ( getDate       ("DATA_ARRESTO_A"             ) ); 
    aModel.setNumAnniArresto            ( getBigDecimal ("NUM_ANNI_ARRESTO"           ) ); 
    aModel.setNumMesiArresto            ( getBigDecimal ("NUM_MESI_ARRESTO"           ) ); 
    aModel.setNumGiorniArresto          ( getBigDecimal ("NUM_GIORNI_ARRESTO"         ) ); 
    aModel.setImportoAmmenda            ( getBigDecimal ("IMPORTO_AMMENDA"            ) ); 
    
    
    aModel.setCodDpr            ( getString     ("COD_DPR") ); 
    aModel.setDescDpr           ( getString     ("DESC_DPR") ); 
    aModel.setDataRichiesta     ( getDate       ("DATA_RICHIESTA"));
    
    aModel.setNote                      ( getString     ("NOTE"                       ) ); 

    aModel.setNumGiorniMap               ( getBigDecimal ("NUM_GIORNI_MAP"                ) ); 
    aModel.setCodTipoMisura              ( getString     ("COD_TIPO_MISURA"               ) ); 
    aModel.setDescrTipoMisura            ( getString("DESC_TIPO_MISURA") );
    aModel.setIstDetIdIstitutoDetenzione ( getString     ("IST_DET_ID_ISTITUTO_DETENZIONE") ); 
    aModel.setAltroLuogoDetenzione       ( getString     ("ALTRO_LUOGO_DETENZIONE"        ) ); 
    
    
    aModel.setFlagStato                 ( getString     ("FLAG_STATO"                 ) ); 
    aModel.setMotivoModifica            ( getString     ("MOTIVO_MODIFICA"            ) ); 
    aModel.setTitIdTitoloCumulato       ( getBigDecimal ("TIT_ID_TITOLO_CUMULATO"     ) ); 
    aModel.setDatIdDatiFinaliCumulo     ( getBigDecimal ("DAT_ID_DATI_FINALI_CUMULO"  ) ); 
    aModel.setIstrIdIstruttoriaCumulo   ( getBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO" ) ); 
    aModel.setStatIdStatoEsecTitCum     ( getBigDecimal ("STAT_ID_STATO_ESEC_TIT_CUM" ) ); 

    aModel.setDataEmissioneProvv        ( getDate       ("DATA_EMISSIONE_PROVV"   ) );
    aModel.setDataRicezioneProvv        ( getDate       ("DATA_RICEZIONE_PROVV"   ) );
    aModel.setAnnoProvv               	( getBigDecimal ("ANNO_PROVV"             ) ); 
    aModel.setProgrProvv               	( getBigDecimal ("PROGR_PROVV"            ) ); 
    aModel.setCodUfficioEmittenteProvv 	( getString 	("COD_UFFICIO_EMITTENTE_PROVV" ) ); 
    aModel.setCodLuogoUfficioProvv     	( getString 	("COD_LUOGO_UFFICIO_PROVV") ); 
    aModel.setDescLuogoUfficioEmittenteProvv ( getString("DescLuogoUfficioEmittenteProvv") ); 
    
    aModel.setSezioneProvv     			( getString 	("SEZIONE_PROVV") ); 
    aModel.setCodTipoProvv   			( getString 	("COD_TIPO_PROVV") ); 

    aModel.setReaIdReatoCum            	( getBigDecimal ("REA_ID_REATO_CUM"           ) ); 

    aModel.setCodFonte		       		( getString 	("COD_FONTE"   ) );  
    aModel.setDescrFonte	       		( DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoFonteReato(),aModel.getCodFonte()));  
    aModel.setAnnoFonte	       		  	( getBigDecimal ("ANNO_FONTE"   ) );  
    aModel.setNumeroFonte	       		( getString 	("NUMERO_FONTE"   ) );  
    aModel.setCodSottonumerazione 		( getString 	("COD_SOTTONUMERAZIONE"   ) );  
    aModel.setDescrSottonumerazione		( DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getSottonumerazione(),aModel.getCodSottonumerazione()));  
    aModel.setComma		       		  	( getString 	("COMMA"   ) );  
    aModel.setLettera		       		( getString 	("LETTERA"   ) );  
    aModel.setNumero		       		( getString 	("NUMERO"   ) );  
    aModel.setArticolo		       		( getString 	("ARTICOLO"   ) );  

    aModel.setCodTipoRegistroOrdinanza	 ( getString 	("COD_TIPO_REGISTRO_ORDINANZA") );  
    aModel.setDescrTipoRegistroOrdinanza ( getString  ("descTipoRegistroOrdinanza") );      
    aModel.setDataSospensioneInterruzione( getDate    ("DATA_SOSPENSIONE_INTERRUZIONE") ); 
    aModel.setCodOggettoDecisione		     ( getString 	("COD_OGGETTO_DECISIONE") );  
    aModel.setDescrOggettoDecisione      ( getString  ("descOggettoDecisione") );  

    aModel.setProtocollo				( getString 	("PROTOCOLLO") );  
    aModel.setAltraAutorita				( getString 	("ALTRA_AUTORITA") );  
    aModel.setAltroLuogo				( getString 	("ALTRO_LUOGO") );  

    aModel.setLuogoEsecMisura			( getString 	("LUOGO_ESEC_MISURA") );  
    aModel.setDataInizioMisura			( getDate       ("DATA_INIZIO_MISURA" ) );  
    aModel.setDataFineMisura			( getDate       ("DATA_FINE_MISURA" ) );  
    aModel.setNumAnniMisura            	( getBigDecimal ("NUM_ANNI_MISURA"  ) ); 
    aModel.setNumMesiMisura            	( getBigDecimal ("NUM_MESI_MISURA"  ) ); 
    aModel.setNumGiorniMisura          	( getBigDecimal ("NUM_GIORNI_MISURA"  ) ); 

    aModel.setDataInizioRevoca			( getDate       ("DATA_INIZIO_REVOCA" ) ) ;  
    aModel.setNumAnniRevocaReclusione	( getBigDecimal ("NUM_ANNI_REVOCA_RECLUSIONE"  ) );  
    aModel.setNumMesiRevocaReclusione	( getBigDecimal ("NUM_MESI_REVOCA_RECLUSIONE"  ) );  
    aModel.setNumGiorniRevocaReclusione	( getBigDecimal ("NUM_GIORNI_REVOCA_RECLUSIONE"  ) );  
    aModel.setNumAnniRevocaArresto		( getBigDecimal ("NUM_ANNI_REVOCA_ARRESTO"  ) );  
    aModel.setNumMesiRevocaArresto		( getBigDecimal ("NUM_MESI_REVOCA_ARRESTO"  ) );  
    aModel.setNumGiorniRevocaArresto	( getBigDecimal ("NUM_GIORNI_REVOCA_ARRESTO"  ) );  
    
    aModel.setDataIngressoIstituto		( getDate       ("DATA_INGRESSO_ISTITUTO" ) ) ;  
    aModel.setDataScarcerazione			( getDate       ("DATA_SCARCERAZIONE" ) ) ;  
    aModel.setFlagDecisioneTribunale	( getString     ("FLAG_DECISIONE_TRIBUNALE"  ) ); 
    aModel.setCodTDSCompetente			( getString     ("COD_TDS_COMPETENTE"  ) ); 
    aModel.setDescrTDSCompetente		( getString     ("COD_TDS_COMPETENTE"  ) ); 

    aModel.setCodOperatoreInserimento   ( getString     ("COD_OPERATORE_INSERIMENTO"  ) ); 
    aModel.setDataInserimento           ( getDate       ("DATA_INSERIMENTO"           ) ); 
    aModel.setCodUfficioInserimento     ( getString     ("COD_UFFICIO_INSERIMENTO"    ) ); 
    aModel.setCodOperatoreAggiornamento ( getString     ("COD_OPERATORE_AGGIORNAMENTO") ); 
    aModel.setDataAggiornamento         ( getDate       ("DATA_AGGIORNAMENTO"         ) ); 
    aModel.setCodUfficioAggiornamento   ( getString     ("COD_UFFICIO_AGGIORNAMENTO"  ) ); 
    
    // Fungibilita
    aModel.setDescUfficioEmittenteProvv (getString		("DescUfficioEmittenteProvv")	);
    aModel.setDescLuogoUfficioEmittenteProvv(getString  ("DescLuogoUfficioEmittenteProvv") );
    
    aModel.setAnnoProc               	( getBigDecimal ("ANNO_PROC"             ) ); 
    aModel.setProgrProc               	( getBigDecimal ("PROGR_PROC"            ) ); 
    
    aModel.setAnnoBDMC					(getBigDecimal	("ANNO_BDMC"));
    aModel.setNumeroBDMC				(getString		("NUMERO_BDMC"));
    aModel.setAnnoRege					(getBigDecimal	("ANNO_REGE"));
    aModel.setNumeroRege				(getString		("NUMERO_REGE"));
    aModel.setTipoRege					(getString		("TIPO_REGE"));
    aModel.setCodTipoAutoritaRege		(getString		("TIPO_AUT_REGE"));
    aModel.setDescrTipoAutoritaRege		(getString		("DescrTipoAutoritaRege"));
    aModel.setCodLuogoAutoritaRege		(getString		("COD_SEDE_REGE")	);
    aModel.setDescrLuogoAutoritaRege	(getString		("DescrLuogoAutoritaRege"));
    aModel.setDataEmissioneOrdRege		(getDate		("DATA_REGE")	);
    
    aModel.setAnnoRegePM				(getBigDecimal	("ANNO_REGE_PM"));
    aModel.setNumeroRegePM				(getString		("NUMERO_REGE_PM"));
    aModel.setCodTipoUfficioPM			(getString		("COD_TIPO_UFFICIO_PM"));
    aModel.setDescrTipoUfficioPM		(getString		("DescrTipoUfficioPM"));
    aModel.setCodSedeUfficioPM			(getString		("COD_SEDE_UFFICIO_PM"));
    aModel.setDescrSedeUfficioPM		(getString		("DescrSedeUfficioPM"));
    
    aModel.setAnnoSentenza				(getBigDecimal	("ANNO_SENTENZA"));
    aModel.setNumeroSentenza			(getString		("NUMERO_SENTENZA"));
    aModel.setDataSentenza				(getDate		("DATA_SENTENZA"));
    aModel.setCodTipoAutoritaEmittente	(getString		("COD_TIPO_AUT_EMITT_SENTENZA"));
    aModel.setDescrTipoAutoritaEmittente(getString		("DescrTipoAutoritaEmittente"));
    aModel.setCodLuogoEmittente			(getString		("COD_LUOGO_EMITTENTE_SENTENZA"));
    aModel.setDescrLuogoEmittente		(getString		("DescrLuogoEmittente"));
    
    aModel.setChiaveAnnoSIEP			(getBigDecimal	("CHIAVE_ANNO_SIEP"));
    aModel.setChiaveNumeroSIEP			(getBigDecimal	("CHIAVE_NUMERO_SIEP"));
    aModel.setChiaveUfficioSIEP			(getString		("CHIAVE_UFFICIO_SIEP"));
    
    // MEV_2025-48 - ALTRO – Benefici con anticipazione effetti
    aModel.setFlagAppProvvisoria        (getString      ("FLAG_APP_PROVVISORIA"));

    return aModel;
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public String setCondizioni(ComputiCumuloModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdComputiCumulo() != null ) { 
      lCondizioni += " and ID_COMPUTI_CUMULO = " + aModel.getIdComputiCumulo() + ""; 
    } 
    if (aModel.getCodTipoAnnotazione() != null && aModel.getCodTipoAnnotazione().length() > 0) { 
      lCondizioni += " and COD_TIPO_ANNOTAZIONE = '" + aModel.getCodTipoAnnotazione() + "' "; 
    } 
    if (aModel.getCodCausaleComputo() != null && aModel.getCodCausaleComputo().length() > 0) { 
      lCondizioni += " and COD_CAUSALE_COMPUTO = '" + aModel.getCodCausaleComputo() + "' "; 
    } 
    if (aModel.getFlagPiuMeno() != null && aModel.getFlagPiuMeno().length() > 0) { 
      lCondizioni += " and FLAG_PIU_MENO = '" + aModel.getFlagPiuMeno() + "' "; 
    } 
    if (aModel.getDataReclusioneDa() != null ) { 
      lCondizioni += " and to_char(DATA_RECLUSIONE_DA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataReclusioneDa(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataReclusioneA() != null ) { 
      lCondizioni += " and to_char(DATA_RECLUSIONE_A,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataReclusioneA(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getNumAnniReclusione() != null ) { 
      lCondizioni += " and NUM_ANNI_RECLUSIONE = " + aModel.getNumAnniReclusione() + ""; 
    } 
    if (aModel.getNumMesiReclusione() != null ) { 
      lCondizioni += " and NUM_MESI_RECLUSIONE = " + aModel.getNumMesiReclusione() + ""; 
    } 
    if (aModel.getNumGiorniReclusione() != null ) { 
      lCondizioni += " and NUM_GIORNI_RECLUSIONE = " + aModel.getNumGiorniReclusione() + ""; 
    } 
    if (aModel.getImportoMulta() != null ) { 
      lCondizioni += " and IMPORTO_MULTA = " + aModel.getImportoMulta() + ""; 
    } 
    if (aModel.getDataArrestoDa() != null ) { 
      lCondizioni += " and to_char(DATA_ARRESTO_DA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataArrestoDa(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataArrestoA() != null ) { 
      lCondizioni += " and to_char(DATA_ARRESTO_A,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataArrestoA(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getNumAnniArresto() != null ) { 
      lCondizioni += " and NUM_ANNI_ARRESTO = " + aModel.getNumAnniArresto() + ""; 
    } 
    if (aModel.getNumMesiArresto() != null ) { 
      lCondizioni += " and NUM_MESI_ARRESTO = " + aModel.getNumMesiArresto() + ""; 
    } 
    if (aModel.getNumGiorniArresto() != null ) { 
      lCondizioni += " and NUM_GIORNI_ARRESTO = " + aModel.getNumGiorniArresto() + ""; 
    } 
    if (aModel.getImportoAmmenda() != null ) { 
      lCondizioni += " and IMPORTO_AMMENDA = " + aModel.getImportoAmmenda() + ""; 
    }
    if (aModel.getCodDpr() != null && aModel.getCodDpr().length() > 0) { 
      lCondizioni += " and COD_DPR = '" + aModel.getCodDpr() + "' "; 
    }
    if (aModel.getDataRichiesta() != null ) { 
      lCondizioni += " and DATA_RICHIESTA = '" + aModel.getDataRichiesta() + "' "; 
    }     
    if (aModel.getNote() != null && aModel.getNote().length() > 0) { 
      lCondizioni += " and NOTE = '" + aModel.getNote() + "' "; 
    }     
    if (aModel.getFlagStato() != null && aModel.getFlagStato().length() > 0) { 
      lCondizioni += " and FLAG_STATO = '" + aModel.getFlagStato() + "' "; 
    } 
    if (aModel.getMotivoModifica() != null && aModel.getMotivoModifica().length() > 0) { 
      lCondizioni += " and MOTIVO_MODIFICA = '" + aModel.getMotivoModifica() + "' "; 
    } 
    if (aModel.getTitIdTitoloCumulato() != null ) { 
      lCondizioni += " and TIT_ID_TITOLO_CUMULATO = " + aModel.getTitIdTitoloCumulato() + ""; 
    } 
    if (aModel.getDatIdDatiFinaliCumulo() != null ) { 
      lCondizioni += " and DAT_ID_DATI_FINALI_CUMULO = " + aModel.getDatIdDatiFinaliCumulo() + ""; 
    } 
    if (aModel.getIstrIdIstruttoriaCumulo() != null ) { 
      lCondizioni += " and ISTR_ID_ISTRUTTORIA_CUMULO = " + aModel.getIstrIdIstruttoriaCumulo() + ""; 
    } 
    if (aModel.getDataEmissioneProvv() != null ) { 
        lCondizioni += " and DATA_EMISSIONE_PROVV = " + aModel.getDataEmissioneProvv() + ""; 
    } 
    if (aModel.getDataRicezioneProvv() != null ) { 
        lCondizioni += " and DATA_RICEZIONE_PROVV = " + aModel.getDataRicezioneProvv() + ""; 
    } 
    if (aModel.getAnnoProvv() != null ) { 
        lCondizioni += " and ANNO_PROVV = " + aModel.getAnnoProvv() + ""; 
    } 
    if (aModel.getProgrProvv() != null ) { 
        lCondizioni += " and PROGR_PROVV = " + aModel.getProgrProvv() + ""; 
    } 
    if (aModel.getCodUfficioEmittenteProvv() != null ) { 
        lCondizioni += " and COD_UFFICIO_EMITTENTE_PROVV = " + aModel.getCodUfficioEmittenteProvv() + ""; 
    } 
    if (aModel.getCodLuogoUfficioProvv() != null ) { 
        lCondizioni += " and COD_LUOGO_UFFICIO_PROVV = " + aModel.getCodLuogoUfficioProvv() + ""; 
    } 
    if (aModel.getSezioneProvv() != null ) { 
        lCondizioni += " and SEZIONE_PROVV = " + aModel.getSezioneProvv() + ""; 
    } 
    if (aModel.getCodTipoProvv() != null ) { 
        lCondizioni += " and COD_TIPO_PROVV = " + aModel.getCodTipoProvv() + ""; 
    } 
    if (aModel.getReaIdReatoCum() != null ) { 
        lCondizioni += " and REA_ID_REATO_CUM = " + aModel.getReaIdReatoCum() + ""; 
    } 

    if (aModel.getCodFonte() != null ) { 
        lCondizioni += " and COD_FONTE = " + aModel.getCodFonte() + ""; 
    } 
    if (aModel.getAnnoFonte() != null ) { 
        lCondizioni += " and ANNO_FONTE = " + aModel.getAnnoFonte() + ""; 
    } 
    if (aModel.getNumeroFonte() != null ) { 
        lCondizioni += " and NUMERO_FONTE = " + aModel.getNumeroFonte() + ""; 
    } 
    if (aModel.getCodSottonumerazione() != null ) { 
        lCondizioni += " and COD_SOTTONUMERAZIONE = " + aModel.getCodSottonumerazione() + ""; 
    } 
    if (aModel.getComma() != null ) { 
        lCondizioni += " and COMMA = " + aModel.getComma() + ""; 
    } 
    if (aModel.getLettera() != null ) { 
        lCondizioni += " and LETTERA = " + aModel.getLettera() + ""; 
    } 
    if (aModel.getNumero() != null ) { 
        lCondizioni += " and NUMERO = " + aModel.getNumero() + ""; 
    } 
    if (aModel.getArticolo() != null ) { 
        lCondizioni += " and ARTICOLO = " + aModel.getArticolo() + ""; 
    }

    if (aModel.getCodTipoRegistroOrdinanza() != null ) { 
        lCondizioni += " and COD_TIPO_REGISTRO_ORDINANZA = " + aModel.getCodTipoRegistroOrdinanza() + ""; 
    }
    if (aModel.getDataSospensioneInterruzione() != null ) { 
        lCondizioni += " and to_char(DATA_SOSPENSIONE_INTERRUZIONE,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataSospensioneInterruzione(),"dd/MM/yyyy") + "' "; 
      } 
    if (aModel.getCodOggettoDecisione() != null ) { 
        lCondizioni += " and COD_OGGETTO_DECISIONE = " + aModel.getCodOggettoDecisione() + ""; 
    }

    if (aModel.getProtocollo() != null ) { 
        lCondizioni += " and PROTOCOLLO = " + aModel.getProtocollo() + ""; 
    }
    if (aModel.getAltraAutorita() != null ) { 
        lCondizioni += " and ALTRA_AUTORITA = " + aModel.getAltraAutorita() + ""; 
    }
    if (aModel.getAltroLuogo() != null ) { 
        lCondizioni += " and ALTRO_LUOGO = " + aModel.getAltroLuogo() + ""; 
    }
    
    if (aModel.getLuogoEsecMisura() != null ) { 
        lCondizioni += " and LUOGO_ESEC_MISURA = " + aModel.getLuogoEsecMisura() + ""; 
    }
    if (aModel.getDataInizioMisura() != null ) { 
        lCondizioni += " and DATA_INIZIO_MISURA = " + aModel.getDataInizioMisura() + ""; 
    }
    if (aModel.getDataFineMisura() != null ) { 
        lCondizioni += " and DATA_FINE_MISURA = " + aModel.getDataFineMisura() + ""; 
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
    
    if (aModel.getDataInizioRevoca() != null ) { 
        lCondizioni += " and DATA_INIZIO_REVOCA = " + aModel.getDataInizioRevoca() + ""; 
    }
    if (aModel.getNumAnniRevocaReclusione() != null ) { 
        lCondizioni += " and NUM_ANNI_REVOCA_RECLUSIONE = " + aModel.getNumAnniRevocaReclusione() + ""; 
    }
    if (aModel.getNumMesiRevocaReclusione() != null ) { 
        lCondizioni += " and NUM_MESI_REVOCA_RECLUSIONE = " + aModel.getNumMesiRevocaReclusione() + ""; 
    }
    if (aModel.getNumGiorniRevocaReclusione() != null ) { 
        lCondizioni += " and NUM_GIORNI_REVOCA_RECLUSIONE = " + aModel.getNumGiorniRevocaReclusione() + ""; 
    }
    if (aModel.getNumAnniRevocaArresto() != null ) { 
        lCondizioni += " and NUM_ANNI_REVOCA_ARRESTO = " + aModel.getNumAnniRevocaArresto() + ""; 
    }
    if (aModel.getNumMesiRevocaArresto() != null ) { 
        lCondizioni += " and NUM_MESI_REVOCA_ARRESTO = " + aModel.getNumMesiRevocaArresto() + ""; 
    }
    if (aModel.getNumGiorniRevocaArresto() != null ) { 
        lCondizioni += " and NUM_GIORNI_REVOCA_ARRESTO = " + aModel.getNumGiorniRevocaArresto() + ""; 
    }

    if (aModel.getDataIngressoIstituto() != null ) { 
        lCondizioni += " and DATA_INGRESSO_ISTITUTO = " + aModel.getDataIngressoIstituto() + ""; 
    }
    if (aModel.getDataScarcerazione() != null ) { 
        lCondizioni += " and DATA_SCARCERAZIONE = " + aModel.getDataScarcerazione() + ""; 
    }
    if (aModel.getFlagDecisioneTribunale() != null ) { 
        lCondizioni += " and FLAG_DECISIONE_TRIBUNALE = " + aModel.getFlagDecisioneTribunale() + ""; 
    }
    if (aModel.getCodTDSCompetente() != null ) { 
        lCondizioni += " and COD_TDS_COMPETENTE = " + aModel.getCodTDSCompetente() + ""; 
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
    if (aModel.getNumGiorniMap() != null ) { 
      lCondizioni += " and NUM_GIORNI_MAP = " + aModel.getNumGiorniMap() + ""; 
    } 
    if (aModel.getStatIdStatoEsecTitCum() != null ) { 
      lCondizioni += " and STAT_ID_STATO_ESEC_TIT_CUM = " + aModel.getStatIdStatoEsecTitCum() + ""; 
    } 
    if (aModel.getCodTipoMisura() != null && aModel.getCodTipoMisura().length() > 0) { 
      lCondizioni += " and COD_TIPO_MISURA = '" + aModel.getCodTipoMisura() + "' "; 
    } 
    if (aModel.getIstDetIdIstitutoDetenzione() != null && aModel.getIstDetIdIstitutoDetenzione().length() > 0) { 
      lCondizioni += " and IST_DET_ID_ISTITUTO_DETENZIONE = '" + aModel.getIstDetIdIstitutoDetenzione() + "' "; 
    } 
    if (aModel.getAltroLuogoDetenzione() != null && aModel.getAltroLuogoDetenzione().length() > 0) { 
      lCondizioni += " and ALTRO_LUOGO_DETENZIONE = '" + aModel.getAltroLuogoDetenzione() + "' "; 
    }    
    // MEV_2025-48 - ALTRO – Benefici con anticipazione effetti
    if (aModel.getFlagAppProvvisoria() != null && aModel.getFlagAppProvvisoria().length() > 0) { 
        lCondizioni += " and FLAG_APP_PROVVISORIA = '" + aModel.getFlagAppProvvisoria() + "' "; 
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
  public String setCondizioniByKey( BigDecimal aIdComputiCumulo  ) {
    String lCondizioni = new String();

    lCondizioni += " and ID_COMPUTI_CUMULO = " + aIdComputiCumulo;

    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

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
