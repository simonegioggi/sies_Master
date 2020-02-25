package siap.siep.modulocumulo.dao;

/**
* <p>Title: DatiFinaliUlterioriSanzioniSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella DatiFinaliUlterioriSanzioni</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.modulocumulo.model.DatiFinaliUlterioriSanzioniModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class DatiFinaliUlterioriSanzioniSqlDAO extends SqlDAO {
  /***************************************************************************** 
   * Costruttore  
   * @param con 
   ****************************************************************************/ 
  public DatiFinaliUlterioriSanzioniSqlDAO (Connection con) {
    super(con);
  }


  /***************************************************************************** 
   * Effettua la generica ricerca in base ai dati specificati nel model 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaDatiFinaliUlterioriSanzioni( DatiFinaliUlterioriSanzioniModel  aModel)  throws DAOException {
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
  public void ricercaDatiFinaliUlterioriSanzioniByKey( BigDecimal aIdDatiFinaliUlterioriSanz) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " WHERE " + setCondizioniByKey( aIdDatiFinaliUlterioriSanz);

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }

  public void ricercaDatiFinaliUlterioriSanzioniByIdDatiFinali ( BigDecimal aIdDatiFinaliCumulo) throws DAOException {
    String lSql = getSqlQuery();

    lSql += " and DAT_ID_DATI_FINALI_CUMULO = " + aIdDatiFinaliCumulo;

    setStatement(lSql);
  }

  public void ricercaDatiFinaliUlterioriSanzioniByIdIstruttoria ( BigDecimal aIdIstruttoriaCumulo) throws DAOException {
    String lSql = getSqlQuery();

    lSql += " and ISTR_ID_ISTRUTTORIA_CUMULO = " + aIdIstruttoriaCumulo;

    setStatement(lSql);
  }

  /***************************************************************************** 
   * Metodo per la costruzione della sql query 
   * @return 
   ****************************************************************************/ 
  protected String getSqlQuery() {
    String lStatement = new String("");

    lStatement += " SELECT " +
                  "ID_DATI_FINALI_ULTERIORI_SANZ, "+  
                  "COD_TIPO_ULTERIORE_SANZIONE, CGTipoUltSanz.RV_MEANING descTipoUlterioreSanzione,"+  
                  "CGTipoUltSanz.RV_ALT2_VALUE descTipoUlterioreSanzXStampe, "+
                  "NUM_ANNI, "+  
                  "NUM_MESI, "+  
                  "NUM_GIORNI, "+  
                  "MULTA, "+  
                  "AMMENDA, "+  
                  "FLAG_ESPUL_PERP, "+  
                  "COD_TIPO_LPU, CGTipoLPU.RV_MEANING descTipoLPU, "+  
                  "NUM_ORE_TOT, "+  
                  "NUM_ORE_SETT, "+  
                  "COD_FREQ_SETT, "+  
                  "DAT_ID_DATI_FINALI_CUMULO, "+  
                  "ISTR_ID_ISTRUTTORIA_CUMULO, "+  
                  "COD_OPERATORE_INSERIMENTO, "+  
                  "DATA_INSERIMENTO, "+  
                  "COD_UFFICIO_INSERIMENTO, "+  
                  "COD_OPERATORE_AGGIORNAMENTO, "+  
                  "DATA_AGGIORNAMENTO, "+  
                  "COD_UFFICIO_AGGIORNAMENTO "; 

    lStatement += " FROM DATI_FINALI_ULTERIORI_SANZIONI LEFT OUTER JOIN CG_REF_CODES CGTipoUltSanz ON COD_TIPO_ULTERIORE_SANZIONE = CGTipoUltSanz.RV_LOW_VALUE AND CGTipoUltSanz.RV_DOMAIN = 'TIPO_ULTERIORE_SANZIONE' ";
    lStatement +=                                     " LEFT OUTER JOIN CG_REF_CODES CGTipoLPU ON COD_TIPO_LPU = CGTipoLPU.RV_LOW_VALUE AND CGTipoLPU.RV_DOMAIN = 'TIPO_SANZIONE_SOSTITUTIVA_LPU' ";
    lStatement += " WHERE 1=1 ";
    
    return lStatement;
  }


  /***************************************************************************** 
   * Metodo che carica il record del result set nel model 
   * @return 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel  getModel() throws DAOException {
     DatiFinaliUlterioriSanzioniModel aModel = new  DatiFinaliUlterioriSanzioniModel(); 

    //Inserire le opportune set delle descrizioni!
    aModel.setIdDatiFinaliUlterioriSanz ( getBigDecimal ("ID_DATI_FINALI_ULTERIORI_SANZ") ); 
    aModel.setCodTipoUlterioreSanzione  ( getString     ("COD_TIPO_ULTERIORE_SANZIONE"  ) ); 
aModel.setDescrTipoUlterioreSanzione(getString("descTipoUlterioreSanzione") );
aModel.setDescrTipoUlterioreSanzioneXStampa (getString("descTipoUlterioreSanzXStampe") );
    aModel.setNumAnni                   ( getBigDecimal ("NUM_ANNI"                     ) ); 
    aModel.setNumMesi                   ( getBigDecimal ("NUM_MESI"                     ) ); 
    aModel.setNumGiorni                 ( getBigDecimal ("NUM_GIORNI"                   ) ); 
    aModel.setMulta                     ( getBigDecimal ("MULTA"                        ) ); 
    aModel.setAmmenda                   ( getBigDecimal ("AMMENDA"                      ) ); 
    aModel.setFlagEspulPerp             ( getString     ("FLAG_ESPUL_PERP"              ) ); 
    aModel.setCodTipoLpu                ( getString     ("COD_TIPO_LPU"                 ) ); 
aModel.setDescrTipoLpu(getString("descTipoLPU") );
    aModel.setNumOreTot                 ( getBigDecimal ("NUM_ORE_TOT"                  ) ); 
    aModel.setNumOreSett                ( getBigDecimal ("NUM_ORE_SETT"                 ) ); 
    aModel.setCodFreqSett               ( getBigDecimal ("COD_FREQ_SETT"                ) ); 
    aModel.setDatIdDatiFinaliCumulo     ( getBigDecimal ("DAT_ID_DATI_FINALI_CUMULO"    ) ); 
    aModel.setIstrIdIstruttoriaCumulo   ( getBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO"   ) ); 
    
    aModel.setCodOperatoreInserimento   ( getString     ("COD_OPERATORE_INSERIMENTO"    ) ); 
    aModel.setDataInserimento           ( getDate       ("DATA_INSERIMENTO"             ) ); 
    aModel.setCodUfficioInserimento     ( getString     ("COD_UFFICIO_INSERIMENTO"      ) ); 
    aModel.setCodOperatoreAggiornamento ( getString     ("COD_OPERATORE_AGGIORNAMENTO"  ) ); 
    aModel.setDataAggiornamento         ( getDate       ("DATA_AGGIORNAMENTO"           ) ); 
    aModel.setCodUfficioAggiornamento   ( getString     ("COD_UFFICIO_AGGIORNAMENTO"    ) ); 

    return aModel;
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public String setCondizioni(DatiFinaliUlterioriSanzioniModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdDatiFinaliUlterioriSanz() != null ) { 
      lCondizioni += " and ID_DATI_FINALI_ULTERIORI_SANZ = " + aModel.getIdDatiFinaliUlterioriSanz() + ""; 
    } 
    if (aModel.getCodTipoUlterioreSanzione() != null && aModel.getCodTipoUlterioreSanzione().length() > 0) { 
      lCondizioni += " and COD_TIPO_ULTERIORE_SANZIONE = '" + aModel.getCodTipoUlterioreSanzione() + "' "; 
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
    if (aModel.getMulta() != null ) { 
      lCondizioni += " and MULTA = " + aModel.getMulta() + ""; 
    } 
    if (aModel.getAmmenda() != null ) { 
      lCondizioni += " and AMMENDA = " + aModel.getAmmenda() + ""; 
    } 
    if (aModel.getFlagEspulPerp() != null && aModel.getFlagEspulPerp().length() > 0) { 
      lCondizioni += " and FLAG_ESPUL_PERP = '" + aModel.getFlagEspulPerp() + "' "; 
    } 
    if (aModel.getCodTipoLpu() != null && aModel.getCodTipoLpu().length() > 0) { 
      lCondizioni += " and COD_TIPO_LPU = '" + aModel.getCodTipoLpu() + "' "; 
    } 
    if (aModel.getNumOreTot() != null ) { 
      lCondizioni += " and NUM_ORE_TOT = " + aModel.getNumOreTot() + ""; 
    } 
    if (aModel.getNumOreSett() != null ) { 
      lCondizioni += " and NUM_ORE_SETT = " + aModel.getNumOreSett() + ""; 
    } 
    if (aModel.getCodFreqSett() != null ) { 
      lCondizioni += " and COD_FREQ_SETT = " + aModel.getCodFreqSett() + ""; 
    } 
    if (aModel.getDatIdDatiFinaliCumulo() != null ) { 
      lCondizioni += " and DAT_ID_DATI_FINALI_CUMULO = " + aModel.getDatIdDatiFinaliCumulo() + ""; 
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
  public String setCondizioniByKey( BigDecimal aIdDatiFinaliUlterioriSanz  ) {
    String lCondizioni = new String();

    lCondizioni += " and ID_DATI_FINALI_ULTERIORI_SANZ = " + aIdDatiFinaliUlterioriSanz;

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
