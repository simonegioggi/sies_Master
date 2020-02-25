package siap.siep.modulocumulo.dao;

/**
* <p>Title: MisuraCautelareCumuloSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella MisuraCautelareCumulo</p>
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

import siap.siep.modulocumulo.model.MisuraCautelareCumuloModel;

public class MisuraCautelareCumuloSqlDAO extends SqlDAO {
  /***************************************************************************** 
   * Costruttore  
   * @param con 
   ****************************************************************************/ 
  public MisuraCautelareCumuloSqlDAO (Connection con) {
    super(con);
  }


  /***************************************************************************** 
   * Restituisce il numero di record dell'operazione di ricerca costruendo 
   * la clausola where con lo stesso model utilizzato per la ricerca 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void getCountMisuraCautelareCumulo(MisuraCautelareCumuloModel aModel) throws DAOException {
    // Costruisce lo statement da eseguire 
    String lStatement ="SELECT COUNT(*) HowManyRecords FROM MISURA_CAUTELARE_CUMULO ";

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
  public void ricercaMisuraCautelareCumuloPaged(MisuraCautelareCumuloModel aModel, int aPage) throws DAOException { 
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
  public void ricercaMisuraCautelareCumulo( MisuraCautelareCumuloModel  aModel)  throws DAOException {
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
  public void ricercaMisuraCautelareCumuloByKey( BigDecimal aIdMisuraCautelareCumulo) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql +=  " and ID_MISURA_CAUTELARE_CUMULO = " + aIdMisuraCautelareCumulo;


    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }

  
  
  /* 
	 * ISSUE MAC : aggiunto ricercaMisuraCautelareCumuloByIdTitolo con un parametro in più (visCanLog) e modificato quello vecchio.
	 *  Se il parametro visCanLog è true la query tira fuori anche le misure cautelari
	 *  cancellate logicamente, altrimenti se è false le misure cancellate logicamente vengono escluse
	 * Numero MAC : 20200110017
	 * Autore    : monica
	 * Data      : 14/gen/2020
	 * Branch    : 11.2.5
	 */
  /**
   * 
   * @param aIdTitolo, visCancLog
   * @throws DAOException
   */
  public void ricercaMisuraCautelareCumuloByIdTitolo( BigDecimal aIdTitolo, boolean visCancLog) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    String condizioneCancLogica = "";
    if(!visCancLog){
    	condizioneCancLogica = " AND MCC.FLAG_STATO <> 'C' ";
    }
    
    lSql += " AND TIT_ID_TITOLO_CUMULATO = " +  aIdTitolo;
    lSql += condizioneCancLogica;
    lSql += " ORDER BY DATA_INIZIO ";
    
    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }
  
  
  /**
   * 
   * @param aIdTitolo
   * @throws DAOException
   */
  public void ricercaMisuraCautelareCumuloByIdTitolo( BigDecimal aIdTitolo) throws DAOException {
    // Recupera la select...from 
	  this.ricercaMisuraCautelareCumuloByIdTitolo(aIdTitolo, true);
  }
//***** FINE INTERVENTO 20200110017 *****//
  
  /**
   * Recupera tutte le MC legate a una istruttoria
   * @param aIdIstruttoria
   * @throws DAOException
   */
  public void ricercaMisuraCautelareCumuloByIdIstruttoria (BigDecimal aIdIstruttoria) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    lSql += " AND TITOLO_CUMULATO.ISTR_ID_ISTRUTTORIA_CUMULO = " +  aIdIstruttoria;
    lSql += " ORDER BY DATA_INIZIO ";
    
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
                  "MCC.ID_MISURA_CAUTELARE_CUMULO, "+  
                  "MCC.COD_TIPO_MISURA, CODTIPOMISURA.RV_MEANING DESC_TIPO_MISURA, "+  
                  "MCC.DATA_INIZIO,  MCC.DATA_FINE, "+  
                  "MCC.NUM_ANNI, MCC.NUM_MESI, MCC.NUM_GIORNI, MCC.GIORNI, "+  
                  "MCC.FLAG_MODIFICA_MANUALE, " + 
                  "MCC.IST_DET_ID_ISTITUTO_DETENZIONE, "+  
                  "MCC.ALTRO_LUOGO_DETENZIONE, "+  
                  "MCC.AUTORITA_COMPETENTE, COD_AUT_COMP.RV_MEANING DESC_AUT_COMP, "+   // Dominio TIPO_AUTORITA
                  "MCC.AUTORITA_COMPETENTE_SEDE, LUOGO_AUT_COMP.DESCRIZIONE DESC_AUT_COMP_SEDE, "+  
                  "MCC.AUTORITA_COMPETENTE_INDIRIZZO, "+
                  "MCC.TIT_ID_TITOLO_CUMULATO, "+  
                  "MCC.FLAG_STATO, "+  
                  "MCC.MOTIVO_MODIFICA, "+  
                  "MCC.ID_MISURA_CAUTELARE_ORIGINE, "+  
                  "MCC.COD_OPERATORE_INSERIMENTO, MCC.DATA_INSERIMENTO, MCC.COD_UFFICIO_INSERIMENTO, "+  
                  "MCC.COD_OPERATORE_AGGIORNAMENTO, MCC.DATA_AGGIORNAMENTO, MCC.COD_UFFICIO_AGGIORNAMENTO "; 
    // aggiungere qui gli eventuali campi descrizioni 

    // Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
    lStatement += " FROM MISURA_CAUTELARE_CUMULO MCC ";
    lStatement += 		"LEFT OUTER JOIN CG_REF_CODES CODTIPOMISURA ON CODTIPOMISURA.RV_LOW_VALUE = COD_TIPO_MISURA "+
                                                               " AND CODTIPOMISURA.RV_DOMAIN = 'TIPO_MISURA_CAUTELARE' ";
    // Istituto
    // Autorità Competente
    lStatement +=     "LEFT OUTER JOIN CG_REF_CODES COD_AUT_COMP ON COD_AUT_COMP.RV_LOW_VALUE = AUTORITA_COMPETENTE "+
                                                              " AND COD_AUT_COMP.RV_DOMAIN = 'TIPO_AUTORITA' ";
    
    // Autorità Competente Luogo
    lStatement +=     " LEFT OUTER JOIN COMUNE LUOGO_AUT_COMP ON LUOGO_AUT_COMP.COD_COMUNE = AUTORITA_COMPETENTE_SEDE ";

    lStatement +=     " LEFT OUTER JOIN TITOLO_CUMULATO ON TITOLO_CUMULATO.ID_TITOLO_CUMULATO = MCC.TIT_ID_TITOLO_CUMULATO ";

    
    lStatement += " WHERE 1=1 ";
    
    return lStatement;
  }


  /***************************************************************************** 
   * Metodo che carica il record del result set nel model 
   * @return 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel  getModel() throws DAOException {
     MisuraCautelareCumuloModel aModel = new  MisuraCautelareCumuloModel(); 

    //Inserire le opportune set delle descrizioni!
    aModel.setIdMisuraCautelareCumulo     ( getBigDecimal ("ID_MISURA_CAUTELARE_CUMULO"    ) ); 
    aModel.setCodTipoMisura               ( getString     ("COD_TIPO_MISURA"               ) ); 
aModel.setDescrTipoMisura             ( getString ("DESC_TIPO_MISURA") );
    aModel.setDataInizio                  ( getDate       ("DATA_INIZIO"                   ) ); 
    aModel.setDataFine                    ( getDate       ("DATA_FINE"                     ) ); 
    aModel.setNumAnni                     ( getBigDecimal ("NUM_ANNI"                      ) ); 
    aModel.setNumMesi                     ( getBigDecimal ("NUM_MESI"                      ) ); 
    aModel.setNumGiorni                   ( getBigDecimal ("NUM_GIORNI"                    ) ); 
    aModel.setGiorni                      ( getBigDecimal ("GIORNI"                        ) ); 
    aModel.setFlagModificaManuale         ( getString     ("FLAG_MODIFICA_MANUALE"         ) ); 

    aModel.setIstDetIdIstitutoDetenzione  ( getString     ("IST_DET_ID_ISTITUTO_DETENZIONE") ); 
    aModel.setAltroLuogoDetenzione        ( getString     ("ALTRO_LUOGO_DETENZIONE"        ) ); 
    aModel.setAutoritaCompetente          ( getString     ("AUTORITA_COMPETENTE"           ) ); 
aModel.setDescrAutoritaCompetente          ( getString     ("DESC_AUT_COMP"           ) ); 
    aModel.setAutoritaCompetenteSede      ( getString     ("AUTORITA_COMPETENTE_SEDE"      ) ); 
aModel.setDescrAutoritaCompetenteSede      ( getString     ("DESC_AUT_COMP_SEDE"      ) ); 
    aModel.setAutoritaCompetenteIndirizzo ( getString     ("AUTORITA_COMPETENTE_INDIRIZZO" ) ); 
    /*
    aModel.setFlagComputabile             ( getString     ("FLAG_COMPUTABILE"              ) ); 
    aModel.setCodMotivoNonComputabile     ( getString     ("COD_MOTIVO_NON_COMPUTABILE"    ) ); 
aModel.setDescrMotivoNonComputabile(getString("DESC_NON_COMPUTABILE") );
    aModel.setCodTipoUfficioRifer         ( getString     ("COD_TIPO_UFFICIO_RIFER"        ) ); 
aModel.setDescrTipoUfficioRifer(getString("DESC_UFF_RIFER") );
    aModel.setCodLuogoUfficioRifer        ( getString     ("COD_LUOGO_UFFICIO_RIFER"       ) ); 
aModel.setDescrLuogoUfficioRifer(getString("DESC_LUOGO_UFF_RIFER") );
    aModel.setDataFungibilita             ( getDate       ("DATA_FUNGIBILITA"              ) ); 
    aModel.setAnnoRifer                   ( getBigDecimal ("ANNO_RIFER"                    ) ); 
    aModel.setNumRifer                    ( getString     ("NUM_RIFER"                     ) ); 
    
    aModel.setNote                        ( getString     ("NOTE"                          ) ); 
    
    aModel.setAnnoFascBdmc                ( getBigDecimal ("ANNO_FASC_BDMC"                ) ); 
    aModel.setNumeFascBdmc                ( getBigDecimal ("NUME_FASC_BDMC"                ) ); 
    aModel.setCodiceUfficioPmSede         ( getString     ("CODICE_UFFICIO_PM_SEDE"        ) ); 
aModel.setDescrUfficioPmSede (getString("DESC_UFFICIO_PM") );
aModel.setDescrComunePmSede  (getString("DESC_COMUNE_UFF_PM") );
    aModel.setAnnoRgnr                    ( getBigDecimal ("ANNO_RGNR"                     ) ); 
    aModel.setNumeroRgnr                  ( getBigDecimal ("NUMERO_RGNR"                   ) ); 
    aModel.setAnnoRegGen                  ( getBigDecimal ("ANNO_REG_GEN"                  ) ); 
    aModel.setNumeroRegGen                ( getBigDecimal ("NUMERO_REG_GEN"                ) ); 
    aModel.setTipoUfficioRegGen           ( getString     ("TIPO_UFFICIO_REG_GEN"          ) ); 
aModel.setDescrTipoUfficioRegGen  ( getString     ("DESC_UFF_REG_GEN"          ) ); 
    aModel.setAutoritaEmittente           ( getString     ("AUTORITA_EMITTENTE"            ) ); 
aModel.setDescrAutoritaEmittente           ( getString     ("DESC_AUTORITA_EMITTENTE"            ) ); 
    aModel.setAutoritaEmittenteLuogo      ( getString     ("AUTORITA_EMITTENTE_LUOGO"      ) ); 
aModel.setDescrAutoritaEmittenteLuogo      ( getString     ("DESC_AUTORITA_EMITTENTE_LUOGO"      ) ); 
    aModel.setDataEmissioneOrdinanza      ( getDate       ("DATA_EMISSIONE_ORDINANZA"      ) );
*/
    aModel.setTitIdTitoloCumulato         ( getBigDecimal ("TIT_ID_TITOLO_CUMULATO"        ) ); 
    aModel.setFlagStato                   ( getString     ("FLAG_STATO"                    ) ); 
    aModel.setMotivoModifica              ( getString     ("MOTIVO_MODIFICA"               ) ); 
    aModel.setIdMisuraCautelareOrigine    ( getBigDecimal ("ID_MISURA_CAUTELARE_ORIGINE"   ) ); 
    
    aModel.setCodOperatoreInserimento     ( getString     ("COD_OPERATORE_INSERIMENTO"     ) ); 
    aModel.setDataInserimento             ( getDate       ("DATA_INSERIMENTO"              ) ); 
    aModel.setCodUfficioInserimento       ( getString     ("COD_UFFICIO_INSERIMENTO"       ) ); 
    aModel.setCodOperatoreAggiornamento   ( getString     ("COD_OPERATORE_AGGIORNAMENTO"   ) ); 
    aModel.setDataAggiornamento           ( getDate       ("DATA_AGGIORNAMENTO"            ) ); 
    aModel.setCodUfficioAggiornamento     ( getString     ("COD_UFFICIO_AGGIORNAMENTO"     ) ); 

    return aModel;
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public String setCondizioni(MisuraCautelareCumuloModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdMisuraCautelareCumulo() != null ) { 
      lCondizioni += " and ID_MISURA_CAUTELARE_CUMULO = " + aModel.getIdMisuraCautelareCumulo() + ""; 
    } 
    if (aModel.getCodTipoMisura() != null && aModel.getCodTipoMisura().length() > 0) { 
      lCondizioni += " and COD_TIPO_MISURA = '" + aModel.getCodTipoMisura() + "' "; 
    } 
    if (aModel.getDataInizio() != null ) { 
      lCondizioni += " and to_char(DATA_INIZIO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInizio(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataFine() != null ) { 
      lCondizioni += " and to_char(DATA_FINE,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataFine(),"dd/MM/yyyy") + "' "; 
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
    if (aModel.getGiorni() != null ) { 
      lCondizioni += " and GIORNI = " + aModel.getGiorni() + ""; 
    } 
    if (aModel.getFlagModificaManuale() != null ) { 
      lCondizioni += " and FLAG_MODIFICA_MANUALE = " + aModel.getFlagModificaManuale() + ""; 
    } 
    if (aModel.getIstDetIdIstitutoDetenzione() != null && aModel.getIstDetIdIstitutoDetenzione().length() > 0) { 
      lCondizioni += " and IST_DET_ID_ISTITUTO_DETENZIONE = '" + aModel.getIstDetIdIstitutoDetenzione() + "' "; 
    } 
    if (aModel.getAltroLuogoDetenzione() != null && aModel.getAltroLuogoDetenzione().length() > 0) { 
      lCondizioni += " and ALTRO_LUOGO_DETENZIONE = '" + aModel.getAltroLuogoDetenzione() + "' "; 
    } 
    if (aModel.getAutoritaCompetente() != null && aModel.getAutoritaCompetente().length() > 0) { 
      lCondizioni += " and AUTORITA_COMPETENTE = '" + aModel.getAutoritaCompetente() + "' "; 
    } 
    if (aModel.getAutoritaCompetenteSede() != null && aModel.getAutoritaCompetenteSede().length() > 0) { 
      lCondizioni += " and AUTORITA_COMPETENTE_SEDE = '" + aModel.getAutoritaCompetenteSede() + "' "; 
    } 
    if (aModel.getAutoritaCompetenteIndirizzo() != null && aModel.getAutoritaCompetenteIndirizzo().length() > 0) { 
      lCondizioni += " and AUTORITA_COMPETENTE_INDIRIZZO = '" + aModel.getAutoritaCompetenteIndirizzo() + "' "; 
    } 
    /*
    if (aModel.getFlagComputabile() != null && aModel.getFlagComputabile().length() > 0) { 
      lCondizioni += " and FLAG_COMPUTABILE = '" + aModel.getFlagComputabile() + "' "; 
    } 
    if (aModel.getCodMotivoNonComputabile() != null && aModel.getCodMotivoNonComputabile().length() > 0) { 
      lCondizioni += " and COD_MOTIVO_NON_COMPUTABILE = '" + aModel.getCodMotivoNonComputabile() + "' "; 
    } 
    if (aModel.getCodTipoUfficioRifer() != null && aModel.getCodTipoUfficioRifer().length() > 0) { 
      lCondizioni += " and COD_TIPO_UFFICIO_RIFER = '" + aModel.getCodTipoUfficioRifer() + "' "; 
    } 
    if (aModel.getCodLuogoUfficioRifer() != null && aModel.getCodLuogoUfficioRifer().length() > 0) { 
      lCondizioni += " and COD_LUOGO_UFFICIO_RIFER = '" + aModel.getCodLuogoUfficioRifer() + "' "; 
    } 
    if (aModel.getDataFungibilita() != null ) { 
      lCondizioni += " and to_char(DATA_FUNGIBILITA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataFungibilita(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getAnnoRifer() != null ) { 
      lCondizioni += " and ANNO_RIFER = " + aModel.getAnnoRifer() + ""; 
    } 
    if (aModel.getNumRifer() != null && aModel.getNumRifer().length() > 0) { 
      lCondizioni += " and NUM_RIFER = '" + aModel.getNumRifer() + "' "; 
    } 
    if (aModel.getNote() != null && aModel.getNote().length() > 0) { 
      lCondizioni += " and NOTE = '" + aModel.getNote() + "' "; 
    } 
    if (aModel.getAnnoFascBdmc() != null ) { 
      lCondizioni += " and ANNO_FASC_BDMC = " + aModel.getAnnoFascBdmc() + ""; 
    } 
    if (aModel.getNumeFascBdmc() != null ) { 
      lCondizioni += " and NUME_FASC_BDMC = " + aModel.getNumeFascBdmc() + ""; 
    } 
    if (aModel.getCodiceUfficioPmSede() != null && aModel.getCodiceUfficioPmSede().length() > 0) { 
      lCondizioni += " and CODICE_UFFICIO_PM_SEDE = '" + aModel.getCodiceUfficioPmSede() + "' "; 
    } 
    if (aModel.getAnnoRgnr() != null ) { 
      lCondizioni += " and ANNO_RGNR = " + aModel.getAnnoRgnr() + ""; 
    } 
    if (aModel.getNumeroRgnr() != null ) { 
      lCondizioni += " and NUMERO_RGNR = " + aModel.getNumeroRgnr() + ""; 
    } 
    if (aModel.getAnnoRegGen() != null ) { 
      lCondizioni += " and ANNO_REG_GEN = " + aModel.getAnnoRegGen() + ""; 
    } 
    if (aModel.getNumeroRegGen() != null ) { 
      lCondizioni += " and NUMERO_REG_GEN = " + aModel.getNumeroRegGen() + ""; 
    } 
    if (aModel.getTipoUfficioRegGen() != null && aModel.getTipoUfficioRegGen().length() > 0) { 
      lCondizioni += " and TIPO_UFFICIO_REG_GEN = '" + aModel.getTipoUfficioRegGen() + "' "; 
    } 
    if (aModel.getAutoritaEmittente() != null && aModel.getAutoritaEmittente().length() > 0) { 
      lCondizioni += " and AUTORITA_EMITTENTE = '" + aModel.getAutoritaEmittente() + "' "; 
    } 
    if (aModel.getAutoritaEmittenteLuogo() != null && aModel.getAutoritaEmittenteLuogo().length() > 0) { 
      lCondizioni += " and AUTORITA_EMITTENTE_LUOGO = '" + aModel.getAutoritaEmittenteLuogo() + "' "; 
    } 
    if (aModel.getDataEmissioneOrdinanza() != null ) { 
      lCondizioni += " and to_char(DATA_EMISSIONE_ORDINANZA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataEmissioneOrdinanza(),"dd/MM/yyyy") + "' "; 
    }   
    */  
    if (aModel.getTitIdTitoloCumulato() != null ) { 
      lCondizioni += " and TIT_ID_TITOLO_CUMULATO = " + aModel.getTitIdTitoloCumulato() + ""; 
    } 
    if (aModel.getFlagStato() != null && aModel.getFlagStato().length() > 0) { 
      lCondizioni += " and FLAG_STATO = '" + aModel.getFlagStato() + "' "; 
    } 
    if (aModel.getMotivoModifica() != null && aModel.getMotivoModifica().length() > 0) { 
      lCondizioni += " and MOTIVO_MODIFICA = '" + aModel.getMotivoModifica() + "' "; 
    } 
    if (aModel.getIdMisuraCautelareOrigine() != null ) { 
      lCondizioni += " and ID_MISURA_CAUTELARE_ORIGINE = " + aModel.getIdMisuraCautelareOrigine() + ""; 
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
  public String setCondizioniByKey( BigDecimal aIdMisuraCautelareCumulo  ) {
    String lCondizioni = new String();

    lCondizioni += " and ID_MISURA_CAUTELARE_CUMULO = " + aIdMisuraCautelareCumulo;

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
  
  protected String setOrderByDataInizio() { 
    String orderBy = new String(""); 
    orderBy = " ORDER BY DATA_INIZIO "; 
    return orderBy; 
  }
}
