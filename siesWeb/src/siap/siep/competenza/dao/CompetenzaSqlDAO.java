package siap.siep.competenza.dao;

/**
* <p>Title: CompetenzaSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella Competenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sico.web.ISICOCostantiWeb;
import siap.siep.competenza.model.CompetenzaModel;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class CompetenzaSqlDAO extends SqlDAO {
  /***************************************************************************** 
   * Costruttore  
   * @param con 
   ****************************************************************************/ 
  public CompetenzaSqlDAO (Connection con) {
    super(con);
  }


  /***************************************************************************** 
   * Restituisce il numero di record dell'operazione di ricerca costruendo 
   * la clausola where con lo stesso model utilizzato per la ricerca 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void getCountCompetenza(CompetenzaModel aModel) throws DAOException {
    // Costruisce lo statement da eseguire 
    String lStatement ="SELECT COUNT(*) HowManyRecords FROM COMPETENZA ";

    // Recupero la where condition in base al model 
    String lCondizioni = this.setCondizioni(aModel);

    if (!lCondizioni.trim().equals("")) 
      lStatement+=" AND " + lCondizioni;

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
  public void ricercaCompetenzaPaged(CompetenzaModel aModel, int aPage) throws DAOException { 
    String lStatement = new String(""); 

    lStatement += getSqlQuery(); 

    // Recupero la where condition in base al model 
    String lCondizioni = this.setCondizioni(aModel);

    if (!lCondizioni.trim().equals("")) 
      lStatement+=" AND " + lCondizioni;

    lStatement += " "+getOrderBy()+" "; 

    String lPaginedStatement = ""; 
    lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement + "  ) INNER ) WHERE rn between  " + ( (aPage - 1) * ISICOCostantiWeb.RESULT_PER_PAGE + 1) + 
          " AND " + (aPage) * ISICOCostantiWeb.RESULT_PER_PAGE; 

    setStatement(lPaginedStatement); 
  } 


  /***************************************************************************** 
   * Effettua la generica ricerca in base ai dati specificati nel model 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaCompetenza( CompetenzaModel  aModel)  throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Recupero la where condition in base al model 
    String lCondizioni = setCondizioni(aModel);

    if (!lCondizioni.trim().equals("")) 
      lSql+=" AND " + lCondizioni;

    lSql += " "+getOrderBy()+" "; 

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }


  /***************************************************************************** 
   * Metodo che imposta la statement di ricerca per chiave 
   * @param aKey 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaCompetenzaByKey( BigDecimal aIdCompetenza) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " AND " + setCondizioniByKey( aIdCompetenza);

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }

  
  /***************************************************************************** 
   * Metodo che imposta la statement di ricerca per id evento
   * @param aKey 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaCompetenzaByEveIdEvento( BigDecimal aIdEvento) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " AND " + setCondizioniByEveIdEvento( aIdEvento);

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }
  
  /***************************************************************************** 
   * Metodo che imposta la statement di ricerca per id messaggio
   * @param aKey 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaCompetenzaByIdMessaggioRichiesta( BigDecimal aIdMessaggio) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " AND " + setCondizioniByIdMessaggioRichiesta( aIdMessaggio);

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
                  "ID_COMPETENZA, "+  
                  "COD_TIPO_PROVVEDIMENTO, "+  
                  "DATA_PROVVEDIMENTO, "+  
                  "COD_TIPO_AUTORITA_EMITTENTE, "+  
                  "COD_LUOGO_EMITTENTE, "+  
                  "NUM_SEZIONE_AUTORITA_EMITTENTE, "+  
                  "ANNO_SENTENZA, "+  
                  "NUMERO_SENTENZA, "+  
                  "DATA_IRREVOCABILITA, "+  
                  "SEN_ID_SENTENZA, "+  
                  "FAS_SIE_ID_FASCICOLO_SIEP, "+  
                  "EVE_ID_EVENTO, "+  
                  "CHIAVE_ANNO, "+  
                  "CHIAVE_UFFICIO, "+  
                  "CHIAVE_PROGR, "+  
                  "FLAG_ACCORPATO, CHIAVE_UFFICIO_ORIGINE, CHIAVE_PROGR_ORIGINE, "+
                  		"ID_MESSAGGIO_RICHIESTA, " +
                  "COD_OPERATORE_INSERIMENTO, "+  
                  "DATA_INSERIMENTO, "+  
                  "COD_UFFICIO_INSERIMENTO, "+  
                  "COD_OPERATORE_AGGIORNAMENTO, "+  
                  "DATA_AGGIORNAMENTO, "+  
                  "COD_UFFICIO_AGGIORNAMENTO , " +
                  "COD_TIPO_AUTORITA_COMP , " +
                  "COD_UFFICIO_AUTORITA_COMP , " +
                  "COD_LUOGO_AUTORITA_COMP, " +
                  "LUOGOEMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE, " +
                  "CGCODAUTC.RV_MEANING DESCR_TIPO_AUTORITA_COMP, " +
                  "LUOGOCOMPETENTE.DESCRIZIONE DESCR_LUOGO_AUTORITA_COMP, " +
                  "CGCOD.RV_MEANING DESCR_TIPO_PROVVEDIMENTO, " + 
                  "CGCODAU.RV_MEANING DESCR_AUT_EMITTENTE, " +
 // Parte NUOVA con i campi relativi al TITOLO_RICHIESTO
			    "COD_TIPO_PROVVEDIMENTO_RICH, " +
			    "DATA_PROVVEDIMENTO_RICH, " +
			    "COD_TIPO_AUTOR_EMITTENTE_RICH, " +
			    "COD_LUOGO_EMITTENTE_RICH, " +
			    "NUM_SEZIONE_AUTOR_EMITT_RICH, " +
			    "ANNO_SENTENZA_RICH, " +
			    "NUMERO_SENTENZA_RICH, " +
			    "DATA_IRREVOCABILITA_RICH, " +
			    "LUO_EMITTENTE_RICH.DESCRIZIONE DESCR_LUOGO_EMITTENTE_RICH, " +
			    "CGCOD_RICH.RV_MEANING DESCR_TIPO_PROVVEDIMENTO_RICH, " +
			    "CGCOD_AUTO_RICH.RV_MEANING DESCR_AUT_EMITTENTE_RICH, " +
			    
			    "COGNOME_SOGGETTO_RICH, " +
			    "NOME_SOGGETTO_RICH, " +
			    "DATA_NASCITA_SOGGETTO_RICH, " +
			    "COD_STATO_NASC_SOGGETTO_RICH, " +
			    "COD_COMUNE_NASC_SOGGETTO_RICH, " +
			    "CODICECUI_SOGGETTO_RICH, " +
			    "COMUNE_NASC.DESCRIZIONE COMUNE_NASCITA, COMUNE_NASC.COD_PROVINCIA SIGLA_PROVINCIA";
    
    lStatement += " FROM COMPETENZA, CG_REF_CODES CGCOD, CG_REF_CODES CGCODAUTC, CG_REF_CODES CGCODAU, COMUNE LUOGOEMITTENTE, COMUNE LUOGOCOMPETENTE";
    		lStatement += ", CG_REF_CODES CGCOD_RICH, CG_REF_CODES CGCOD_AUTO_RICH, COMUNE LUO_EMITTENTE_RICH, COMUNE COMUNE_NASC";
    lStatement += " WHERE ";
    lStatement += " (COMPETENZA.COD_TIPO_PROVVEDIMENTO = CGCOD.RV_LOW_VALUE AND CGCOD.RV_DOMAIN = 'TIPO_PROVVEDIMENTO')";
    lStatement += " AND (COMPETENZA.COD_TIPO_AUTORITA_EMITTENTE = CGCODAU.RV_LOW_VALUE AND CGCODAU.RV_DOMAIN = 'TIPO_UFFICIO_EMITTENTE')"; 
    lStatement += " AND (COMPETENZA.COD_LUOGO_EMITTENTE = LUOGOEMITTENTE.COD_COMUNE)";
    lStatement += " AND (COMPETENZA.COD_LUOGO_AUTORITA_COMP = LUOGOCOMPETENTE.COD_COMUNE)";
    lStatement += " AND ( CGCODAUTC.RV_DOMAIN = 'TIPO_UFFICIO_PM'";
    lStatement += " AND CGCODAUTC.RV_LOW_VALUE = COMPETENZA.COD_TIPO_AUTORITA_COMP)";
    
    	lStatement += " AND ( NVL(COMPETENZA.COD_TIPO_PROVVEDIMENTO_RICH, '-') = CGCOD_RICH.RV_LOW_VALUE AND CGCOD_RICH.RV_DOMAIN = 'TIPO_PROVVEDIMENTO')";
    	lStatement += " AND ( NVL(COMPETENZA.COD_TIPO_AUTOR_EMITTENTE_RICH, '-') = CGCOD_AUTO_RICH.RV_LOW_VALUE AND CGCOD_AUTO_RICH.RV_DOMAIN = 'TIPO_UFFICIO_EMITTENTE')";
    	lStatement += " AND NVL(COMPETENZA.COD_LUOGO_EMITTENTE_RICH, '-') = LUO_EMITTENTE_RICH.COD_COMUNE";
    	lStatement += " AND NVL(COMPETENZA.COD_COMUNE_NASC_SOGGETTO_RICH, '-') = COMUNE_NASC.COD_COMUNE";


    
    return lStatement;
  }


  /***************************************************************************** 
   * Metodo che carica il record del result set nel model 
   * @return 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel  getModel() throws DAOException {
     CompetenzaModel aModel = new  CompetenzaModel(); 

    //Inserire le opportune set delle descrizioni!
    aModel.setIdCompetenza                ( getBigDecimal ("ID_COMPETENZA"                 ) ); 
    aModel.setCodTipoProvvedimento        ( getString     ("COD_TIPO_PROVVEDIMENTO"        ) ); 
 aModel.setDescrTipoProvvedimento(getString("DESCR_TIPO_PROVVEDIMENTO") );
    aModel.setDataProvvedimento           ( getDate       ("DATA_PROVVEDIMENTO"            ) ); 
    aModel.setCodTipoAutoritaEmittente    ( getString     ("COD_TIPO_AUTORITA_EMITTENTE"   ) ); 
 aModel.setDescrTipoAutoritaEmittente(getString("DESCR_AUT_EMITTENTE") );
    aModel.setCodLuogoEmittente           ( getString     ("COD_LUOGO_EMITTENTE"           ) ); 
 aModel.setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE") );
    aModel.setNumSezioneAutoritaEmittente ( getString     ("NUM_SEZIONE_AUTORITA_EMITTENTE") ); 
    aModel.setAnnoSentenza                ( getBigDecimal ("ANNO_SENTENZA"                 ) ); 
    aModel.setNumeroSentenza              ( getString     ("NUMERO_SENTENZA"               ) ); 
    aModel.setDataIrrevocabilita          ( getDate       ("DATA_IRREVOCABILITA"           ) ); 
    aModel.setSenIdSentenza               ( getBigDecimal ("SEN_ID_SENTENZA"               ) ); 
    aModel.setFasSieIdFascicoloSiep       ( getBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"     ) ); 
    aModel.setEveIdEvento                 ( getBigDecimal ("EVE_ID_EVENTO"                 ) ); 
    
    aModel.setChiaveAnno                  ( getBigDecimal ("CHIAVE_ANNO"                   ) ); 
    aModel.setChiaveUfficio               ( getString     ("CHIAVE_UFFICIO"                ) ); 
    aModel.setChiaveProgr                 ( getBigDecimal ("CHIAVE_PROGR"                  ) ); 
    aModel.setFlagAccorpato               ( getString     ("FLAG_ACCORPATO"                ) ); 
    aModel.setChiaveUfficioOrigine        ( getString     ("CHIAVE_UFFICIO_ORIGINE"        ) ); 
    aModel.setChiaveProgrOrigine          ( getBigDecimal ("CHIAVE_PROGR_ORIGINE"          ) ); 
    
    aModel.setIdMessaggiorichiesta		  ( getBigDecimal ("ID_MESSAGGIO_RICHIESTA") );
    
    aModel.setCodOperatoreInserimento     ( getString     ("COD_OPERATORE_INSERIMENTO"     ) ); 
    aModel.setDataInserimento             ( getDate       ("DATA_INSERIMENTO"              ) ); 
    aModel.setCodUfficioInserimento       ( getString     ("COD_UFFICIO_INSERIMENTO"       ) ); 
    aModel.setCodOperatoreAggiornamento   ( getString     ("COD_OPERATORE_AGGIORNAMENTO"   ) ); 
    aModel.setDataAggiornamento           ( getDate       ("DATA_AGGIORNAMENTO"            ) ); 
    aModel.setCodUfficioAggiornamento     ( getString     ("COD_UFFICIO_AGGIORNAMENTO"     ) ); 
    
    aModel.setCodTipoAutoritaComp		( getString   ("COD_TIPO_AUTORITA_COMP"	) ); 
    aModel.setCodUfficioAutoritaComp		( getString   ("COD_UFFICIO_AUTORITA_COMP"	) ); 

    aModel.setCodLuogoAutoritaComp	( getString   ("COD_LUOGO_AUTORITA_COMP"	) ); 
    aModel.setDescrTipoAutoritaComp	( getString   ("DESCR_TIPO_AUTORITA_COMP"	) ); 
    aModel.setDescrLuogoAutoritaComp	( getString   ("DESCR_LUOGO_AUTORITA_COMP"	) ); 
 // Parte NUOVA con i campi relativi al TITOLO_RICHIESTO
    aModel.setCodTipoProvvedimento_Rich        	( getString     ("COD_TIPO_PROVVEDIMENTO_RICH"        ) ); 
 aModel.setDescrTipoProvvedimento_Rich			( getString("DESCR_TIPO_PROVVEDIMENTO_RICH") );
    aModel.setDataProvvedimento_Rich           	( getDate       ("DATA_PROVVEDIMENTO_RICH"            ) ); 
    aModel.setCodTipoAutoritaEmittente_Rich    	( getString     ("COD_TIPO_AUTOR_EMITTENTE_RICH"   ) ); 
 aModel.setDescrTipoAutoritaEmittente_Rich		( getString("DESCR_AUT_EMITTENTE_RICH") );
    aModel.setCodLuogoEmittente_Rich           	( getString     ("COD_LUOGO_EMITTENTE_RICH"           ) ); 
 aModel.setDescrLuogoEmittente_Rich				( getString("DESCR_LUOGO_EMITTENTE_RICH") );
    aModel.setNumSezioneAutoritaEmittente_Rich 	( getString     ("NUM_SEZIONE_AUTOR_EMITT_RICH") ); 
    aModel.setAnnoSentenza_Rich                	( getBigDecimal ("ANNO_SENTENZA_RICH"                 ) ); 
    aModel.setNumeroSentenza_Rich              	( getString     ("NUMERO_SENTENZA_RICH"               ) ); 
    aModel.setDataIrrevocabilita_Rich          	( getDate       ("DATA_IRREVOCABILITA_RICH"           ) ); 
    
    aModel.setCognome_Soggetto_Rich				(getString     ("COGNOME_SOGGETTO_RICH"        		)	);
    aModel.setNome_Soggetto_Rich				(getString     ("NOME_SOGGETTO_RICH"        		)	);
    aModel.setDataNascita_Soggetto_Rich			(getDate       ("DATA_NASCITA_SOGGETTO_RICH"     	)	);
    aModel.setCodStatoNascita_Soggetto_Rich		(getString     ("COD_STATO_NASC_SOGGETTO_RICH"      )	);
    aModel.setCodComuneNascita_Soggetto_Rich	(getString     ("COD_COMUNE_NASC_SOGGETTO_RICH"     )	);
    aModel.setDescrComuneNascita_Soggetto_Rich	(getString     ("COMUNE_NASCITA"        	)	);
    aModel.setSigla_Provincia_Soggetto_Rich		(getString     ("SIGLA_PROVINCIA"        	)	);
    aModel.setCodiceCui_Soggetto_Rich			(getString     ("CODICECUI_SOGGETTO_RICH"        	)	);

    return aModel;
  }

  
  /**
   * MEV_39 
   * 
 * @return
 * @throws DAOException
 */
public GenericModel  getModelCompCumulo() throws DAOException {
	     CompetenzaModel aModel = new  CompetenzaModel(); 
	     
	     aModel.setIdCompetenza                ( getBigDecimal ("ID_COMPETENZA"                 ) ); 
	     aModel.setDataProvvedimento           ( getDate       ("DATA_PROVVEDIMENTO"            ) );
	     aModel.setChiaveAnno                  ( getBigDecimal ("CHIAVE_ANNO"                   ) ); 
	     aModel.setChiaveUfficio               ( getString     ("CHIAVE_UFFICIO"                ) ); 
	     aModel.setChiaveProgr                 ( getBigDecimal ("CHIAVE_PROGR"                  ) ); 
	     aModel.setCodTipoAutoritaComp		   ( getString     ("COD_TIPO_AUTORITA_COMP"	    ) ); 
	     aModel.setCodUfficioAutoritaComp	   ( getString     ("COD_UFFICIO_AUTORITA_COMP"	    ) ); 
	     aModel.setCodLuogoAutoritaComp	       ( getString     ("COD_LUOGO_AUTORITA_COMP"	    ) ); 
	     aModel.setDescrTipoAutoritaComp	   ( getString     ("DESCR_TIPO_AUTORITA_COMP"	    ) ); 
	     aModel.setDescrLuogoAutoritaComp	   ( getString     ("DESCR_LUOGO_AUTORITA_COMP"	    ) );
	     
	     
	     return aModel;
  }

  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public String setCondizioni(CompetenzaModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdCompetenza() != null ) { 
      lCondizioni += " and ID_COMPETENZA = " + aModel.getIdCompetenza() + ""; 
    } 
    if (aModel.getCodTipoProvvedimento() != null && aModel.getCodTipoProvvedimento().length() > 0) { 
      lCondizioni += " and COD_TIPO_PROVVEDIMENTO = '" + aModel.getCodTipoProvvedimento() + "' "; 
    } 
    if (aModel.getDataProvvedimento() != null ) { 
      lCondizioni += " and to_char(DATA_PROVVEDIMENTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataProvvedimento(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getCodTipoAutoritaEmittente() != null && aModel.getCodTipoAutoritaEmittente().length() > 0) { 
      lCondizioni += " and COD_TIPO_AUTORITA_EMITTENTE = '" + aModel.getCodTipoAutoritaEmittente() + "' "; 
    } 
    if (aModel.getCodLuogoEmittente() != null && aModel.getCodLuogoEmittente().length() > 0) { 
      lCondizioni += " and COD_LUOGO_EMITTENTE = '" + aModel.getCodLuogoEmittente() + "' "; 
    } 
    if (aModel.getNumSezioneAutoritaEmittente() != null && aModel.getNumSezioneAutoritaEmittente().length() > 0) { 
      lCondizioni += " and NUM_SEZIONE_AUTORITA_EMITTENTE = '" + aModel.getNumSezioneAutoritaEmittente() + "' "; 
    } 
    if (aModel.getAnnoSentenza() != null ) { 
      lCondizioni += " and ANNO_SENTENZA = " + aModel.getAnnoSentenza() + ""; 
    } 
    if (aModel.getNumeroSentenza() != null && aModel.getNumeroSentenza().length() > 0) { 
      lCondizioni += " and NUMERO_SENTENZA = '" + aModel.getNumeroSentenza() + "' "; 
    } 
    if (aModel.getDataIrrevocabilita() != null ) { 
      lCondizioni += " and to_char(DATA_IRREVOCABILITA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataIrrevocabilita(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getSenIdSentenza() != null ) { 
      lCondizioni += " and SEN_ID_SENTENZA = " + aModel.getSenIdSentenza() + ""; 
    } 
    if (aModel.getFasSieIdFascicoloSiep() != null ) { 
      lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + ""; 
    } 
    if (aModel.getEveIdEvento() != null ) { 
      lCondizioni += " and EVE_ID_EVENTO = " + aModel.getEveIdEvento() + ""; 
    } 
    if (aModel.getChiaveAnno() != null ) { 
      lCondizioni += " and CHIAVE_ANNO = " + aModel.getChiaveAnno() + ""; 
    } 
    if (aModel.getChiaveUfficio() != null && aModel.getChiaveUfficio().length() > 0) { 
      lCondizioni += " and CHIAVE_UFFICIO = '" + aModel.getChiaveUfficio() + "' "; 
    } 
    if (aModel.getChiaveProgr() != null ) { 
      lCondizioni += " and CHIAVE_PROGR = " + aModel.getChiaveProgr() + ""; 
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
    if (aModel.getCodTipoAutoritaComp() != null && aModel.getCodTipoAutoritaComp().length() > 0) { 
        lCondizioni += " and COD_TIPO_AUTORITA_COMP = '" + aModel.getCodTipoAutoritaComp() + "' "; 
      }
    if (aModel.getCodLuogoAutoritaComp() != null && aModel.getCodLuogoAutoritaComp().length() > 0) { 
        lCondizioni += " and COD_LUOGO_AUTORITA_COMP = '" + aModel.getCodLuogoAutoritaComp() + "' "; 
      }
    if (aModel.getCodUfficioAutoritaComp() != null && aModel.getCodUfficioAutoritaComp().length() > 0) { 
        lCondizioni += " and COD_UFFICIO_AUTORITA_COMP = '" + aModel.getCodUfficioAutoritaComp() + "' "; 
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
  public String setCondizioniByKey( BigDecimal aIdCompetenza  ) {
    String lCondizioni = new String();

    lCondizioni += " and ID_COMPETENZA = " + aIdCompetenza;

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
  public String setCondizioniByEveIdEvento( BigDecimal aIdEvento  ) {
    String lCondizioni = new String();

    lCondizioni += " and EVE_ID_EVENTO = " + aIdEvento;

    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    return lCondizioni;
  }
  
  public String setCondizioniByIdMessaggioRichiesta( BigDecimal aIdMessaggio  )
  {
	    String lCondizioni = new String();

	    lCondizioni += " and ID_MESSAGGIO_RICHIESTA = " + aIdMessaggio;

	    // Elimino il primo and 
	    if (lCondizioni.length() > 0) { 
	      lCondizioni = lCondizioni.substring(4); 
	    } 

	    return lCondizioni;
  }
  
  /**
   * Ricerca tutti i record competenza associati a un fascicolo, ovvero a eventi 
   * collegati al fascicolo.
   * n.b. il collegamento al fascicolo non è diretto. 
   *      Il campo COMPETENZA.FAS_SIE_ID_FASCICOLO_SIEP non indica il fascicolo di 
   *      appartenenza del record, ma l'eventuale id del fascicolo richiesto/trasmesso
   *      
   * 
   * @param aIdFascicolo
   * @throws DAOException
   */
  public void ricercaCompetenzaByKeyFasc (BigDecimal aIdFascicolo) throws DAOException {
    // Recupera la select...from 
    String lStatement = "";
    lStatement += " SELECT ID_COMPETENZA, "+  
                          "COMPETENZA.COD_TIPO_PROVVEDIMENTO, "+  
                          "COMPETENZA.DATA_PROVVEDIMENTO, "+  
                          "COMPETENZA.COD_TIPO_AUTORITA_EMITTENTE, "+  
                          "COMPETENZA.COD_LUOGO_EMITTENTE, "+  
                          "COMPETENZA.NUM_SEZIONE_AUTORITA_EMITTENTE, "+  
                          "COMPETENZA.ANNO_SENTENZA, "+  
                          "COMPETENZA.NUMERO_SENTENZA, "+  
                          "COMPETENZA.DATA_IRREVOCABILITA, "+  
                          "COMPETENZA.SEN_ID_SENTENZA, "+  
                          "COMPETENZA.FAS_SIE_ID_FASCICOLO_SIEP, "+  
                          "COMPETENZA.EVE_ID_EVENTO, "+  
                          "COMPETENZA.CHIAVE_ANNO, "+  
                          "COMPETENZA.CHIAVE_UFFICIO, "+  
                          "COMPETENZA.CHIAVE_PROGR, "+ 
                          "COMPETENZA.FLAG_ACCORPATO, "+ 
                          "COMPETENZA.CHIAVE_UFFICIO_ORIGINE, "+ 
                          "COMPETENZA.CHIAVE_PROGR_ORIGINE, "+ 
                          "COMPETENZA.COD_OPERATORE_INSERIMENTO, "+  
                          "COMPETENZA.DATA_INSERIMENTO, "+  
                          "COMPETENZA.COD_UFFICIO_INSERIMENTO, "+  
                          "COMPETENZA.COD_OPERATORE_AGGIORNAMENTO, "+  
                          "COMPETENZA.DATA_AGGIORNAMENTO, "+  
                          "COMPETENZA.COD_UFFICIO_AGGIORNAMENTO , " +
                          "COMPETENZA.COD_TIPO_AUTORITA_COMP , " +
                          "COMPETENZA.COD_UFFICIO_AUTORITA_COMP , " +
                          "COMPETENZA.COD_LUOGO_AUTORITA_COMP, " +
                          		"COMPETENZA.ID_MESSAGGIO_RICHIESTA, " +
                          "LUOGOEMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE, " +
                          "CGCODAUTC.RV_MEANING DESCR_TIPO_AUTORITA_COMP, " +
                          "LUOGOCOMPETENTE.DESCRIZIONE DESCR_LUOGO_AUTORITA_COMP, " +
                          "CGCOD.RV_MEANING DESCR_TIPO_PROVVEDIMENTO, " + 
                          "CGCODAU.RV_MEANING DESCR_AUT_EMITTENTE, " +
    // Parte NUOVA con i campi relativi al TITOLO_RICHIESTO
		    "COD_TIPO_PROVVEDIMENTO_RICH, " +
		    "DATA_PROVVEDIMENTO_RICH, " +
		    "COD_TIPO_AUTOR_EMITTENTE_RICH, " +
		    "COD_LUOGO_EMITTENTE_RICH, " +
		    "NUM_SEZIONE_AUTOR_EMITT_RICH, " +
		    "ANNO_SENTENZA_RICH, " +
		    "NUMERO_SENTENZA_RICH, " +
		    "DATA_IRREVOCABILITA_RICH, " +
		    
		    "(null) DESCR_LUOGO_EMITTENTE_RICH, " +
		    "(null) DESCR_TIPO_PROVVEDIMENTO_RICH, "+
		    "(null) DESCR_AUT_EMITTENTE_RICH, "+
		       
		    "COGNOME_SOGGETTO_RICH, " +
		    "NOME_SOGGETTO_RICH, " +
		    "DATA_NASCITA_SOGGETTO_RICH, " +
		    "COD_STATO_NASC_SOGGETTO_RICH, " +
		    "COD_COMUNE_NASC_SOGGETTO_RICH, " +
		    "CODICECUI_SOGGETTO_RICH, " +
		    
			"(null) COMUNE_NASCITA, " +
			"(null) SIGLA_PROVINCIA";

// aggiungere qui gli eventuali campi descrizioni 

    // Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
    lStatement += " FROM COMPETENZA, CG_REF_CODES CGCOD, CG_REF_CODES CGCODAUTC, CG_REF_CODES CGCODAU, COMUNE LUOGOEMITTENTE, COMUNE LUOGOCOMPETENTE";    
    lStatement +=    " , EVENTO, FASCICOLO_SIEP ";
    lStatement += " WHERE ";
    lStatement += "     (COMPETENZA.COD_TIPO_PROVVEDIMENTO = CGCOD.RV_LOW_VALUE AND CGCOD.RV_DOMAIN = 'TIPO_PROVVEDIMENTO')";
    lStatement += " AND (COMPETENZA.COD_TIPO_AUTORITA_EMITTENTE = CGCODAU.RV_LOW_VALUE AND CGCODAU.RV_DOMAIN = 'TIPO_UFFICIO_EMITTENTE')"; 
    lStatement += " AND (COMPETENZA.COD_LUOGO_EMITTENTE = LUOGOEMITTENTE.COD_COMUNE)";
    lStatement += " AND (COMPETENZA.COD_LUOGO_AUTORITA_COMP = LUOGOCOMPETENTE.COD_COMUNE)";
    lStatement += " AND ( CGCODAUTC.RV_DOMAIN = 'TIPO_UFFICIO_PM'";
    lStatement += " AND CGCODAUTC.RV_LOW_VALUE = COMPETENZA.COD_TIPO_AUTORITA_COMP)";

    lStatement += " AND COMPETENZA.EVE_ID_EVENTO = EVENTO.ID_EVENTO";
    lStatement += " AND EVENTO.FLAG_DOCUMENTO_REGISTRATO = 'S' "; // Solo le Competenze legate a Eventi validati
    
    lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = FASCICOLO_SIEP.ID_FASCICOLO_SIEP";
    lStatement += " AND FASCICOLO_SIEP.ID_FASCICOLO_SIEP = "+aIdFascicolo;
    
    lStatement += " order by COMPETENZA.ID_COMPETENZA ASC ";


    // Imposta lo statement da eseguire 
    setStatement(lStatement);
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
   * MEV_39
   * 
   * Ricerca tutti i record competenza associati a un fascicolo, ovvero a eventi 
   * collegati al fascicolo.
   * n.b. il collegamento al fascicolo non è diretto. 
   *      Il campo COMPETENZA.FAS_SIE_ID_FASCICOLO_SIEP non indica il fascicolo di 
   *      appartenenza del record, ma l'eventuale id del fascicolo richiesto/trasmesso
   *      
   * 
   * @param aIdFascicolo
   * @throws DAOException
   */
  public void ExRicercaCompetenzePerCumulo (BigDecimal aIdFascicolo, boolean stessoDistretto) throws DAOException {
	  
	  if(stessoDistretto){
	    String lStatement = "";
	    // @emma intervento post collaudo 11.3  (devo estrarre la data del provvedimento di cumulo e NON della sentenza (anomalia 7 del verbale))
	    lStatement += " SELECT C.ID_COMPETENZA, eCumulo.Data_Emissione as DATA_PROVVEDIMENTO, C.CHIAVE_ANNO , C.CHIAVE_UFFICIO , ";
	    lStatement += " C.CHIAVE_PROGR ,  C.COD_TIPO_AUTORITA_COMP, C.COD_UFFICIO_AUTORITA_COMP,";
	    lStatement += " C.COD_LUOGO_AUTORITA_COMP, CGCODAUTC.RV_MEANING DESCR_TIPO_AUTORITA_COMP,  LUOGOCOMPETENTE.DESCRIZIONE DESCR_LUOGO_AUTORITA_COMP";
	
	    // Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
	    // @emma intervento post collaudo 11.3  (aggiungo tabella FASCICOLO_SIEP AA )
	    lStatement += " FROM COMPETENZA C, CG_REF_CODES CGCODAUTC, COMUNE  LUOGOCOMPETENTE, FASCICOLO_SIEP  A, EVENTO  E, FASCICOLO_SIEP AA ";    
	    
	    lStatement += " , EVENTO eCumulo";
	    
	    lStatement += " WHERE ";
	    lStatement += " (C.COD_LUOGO_AUTORITA_COMP = LUOGOCOMPETENTE.COD_COMUNE) AND (CGCODAUTC.RV_DOMAIN = 'TIPO_UFFICIO_PM' ";
	    lStatement += " AND CGCODAUTC.RV_LOW_VALUE = C.COD_TIPO_AUTORITA_COMP) ";
	   
	    lStatement += " AND C.EVE_ID_EVENTO = E.ID_EVENTO";
	    lStatement += " AND A.ID_FASCICOLO_SIEP  = "+aIdFascicolo;
	    lStatement += " AND E.FAS_SIE_ID_FASCICOLO_SIEP = A.ID_FASCICOLO_SIEP AND E.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
	    // @emma intervento post collaudo 11.3 (aggiungo la condition che segue)
	    lStatement += " AND C.FAS_SIE_ID_FASCICOLO_SIEP  = AA.ID_FASCICOLO_SIEP(+)";
	    lStatement += " AND  eCumulo.Fas_Sie_Id_Fascicolo_Siep= AA.ID_FASCICOLO_SIEP  and eCumulo.Id_Evento = ( select max(evv.id_evento) "
	    		+ "           from EVENTO evv where evv.fas_sie_id_fascicolo_siep=AA.ID_FASCICOLO_SIEP "
	    		+ "  and evv.istr_id_istruttoria_cumulo is not null and evv.cod_motivo IN (select myr.rv_low_value  from cg_ref_codes myr"
	    		+ " where myr.rv_domain = 'MOTIVO_PROVVEDIMENTO'  and myr.rv_high_value='CUMULO_NEW')  )";
	    
	    lStatement += " AND E.ID_EVENTO =  (SELECT MAX(EE.ID_EVENTO) "
	    		   + " FROM EVENTO EE  WHERE EE.FAS_SIE_ID_FASCICOLO_SIEP = A.ID_FASCICOLO_SIEP"
	    		   + " AND EE.COD_MOTIVO IN ('0340', '0740', '5403'))";
	    
	    
	       // Imposta lo statement da eseguire 
    setStatement(lStatement);
	  }
	  else{	  
		  
		  String lStatement = "";
		    // @emma intervento post collaudo 11.3  (devo estrarre la data del provvedimento di cumulo e NON della sentenza (anomalia 7 del verbale))
		    lStatement += " SELECT C.ID_COMPETENZA, "
		    		+ " EW.DATA_ESITO as DATA_PROVVEDIMENTO, EW.CHIAVE_ANNO ,EW.CHIAVE_UFFICIO , ";
		    lStatement += " EW.CHIAVE_PROGR ,  C.COD_TIPO_AUTORITA_COMP, C.COD_UFFICIO_AUTORITA_COMP,";
		    lStatement += " C.COD_LUOGO_AUTORITA_COMP, CGCODAUTC.RV_MEANING DESCR_TIPO_AUTORITA_COMP,  LUOGOCOMPETENTE.DESCRIZIONE DESCR_LUOGO_AUTORITA_COMP";
	
		    // Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
		    // @emma intervento post collaudo 11.3  (aggiungo tabella FASCICOLO_SIEP AA )
		    lStatement += " FROM COMPETENZA C, CG_REF_CODES CGCODAUTC, COMUNE  LUOGOCOMPETENTE, FASCICOLO_SIEP  A, EVENTO  E,  ANNOTAZIONE_ESITO_TRASMISSIONE EW ";    
		  
		    lStatement += " WHERE ";
		    lStatement += " (C.COD_LUOGO_AUTORITA_COMP = LUOGOCOMPETENTE.COD_COMUNE) AND (CGCODAUTC.RV_DOMAIN = 'TIPO_UFFICIO_PM' ";
		    lStatement += " AND CGCODAUTC.RV_LOW_VALUE = C.COD_TIPO_AUTORITA_COMP) ";
		   
		    lStatement += " AND C.EVE_ID_EVENTO = E.ID_EVENTO";
		    lStatement += " AND A.ID_FASCICOLO_SIEP  = "+aIdFascicolo;
		    lStatement += " AND E.FAS_SIE_ID_FASCICOLO_SIEP = A.ID_FASCICOLO_SIEP AND E.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
		    // @emma intervento post collaudo 11.3 (aggiungo la condition che segue)
		    lStatement += "     AND EW.FAS_SIE_ID_FASCICOLO_SIEP = "+aIdFascicolo; 
		    lStatement += " AND E.ID_EVENTO =  (SELECT MAX(EE.ID_EVENTO) "
		    		   + " FROM EVENTO EE  WHERE EE.FAS_SIE_ID_FASCICOLO_SIEP = A.ID_FASCICOLO_SIEP"
		    		   + " AND EE.COD_MOTIVO IN ('0340', '0740', '5403'))";
		    
		    
		       // Imposta lo statement da eseguire 
		    setStatement(lStatement);
	  }
  }
}
