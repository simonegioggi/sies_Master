package siap.siep.modulocumulo.dao;

/**
* <p>Title: PenaRideterminataCumuloSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella PenaRideterminataCumulo</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.modulocumulo.model.PenaRideterminataCumuloModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class PenaRideterminataCumuloSqlDAO extends SqlDAO {
  /***************************************************************************** 
   * Costruttore  
   * @param con 
   ****************************************************************************/ 
  public PenaRideterminataCumuloSqlDAO (Connection con) {
    super(con);
  }


  /***************************************************************************** 
   * Effettua la generica ricerca in base ai dati specificati nel model 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaPenaRideterminataCumulo( PenaRideterminataCumuloModel  aModel)  throws DAOException {
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
  public void ricercaPenaRideterminataCumuloByKey (BigDecimal aIdPenaRideterminataCumulo) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " WHERE " + setCondizioniByKey( aIdPenaRideterminataCumulo);

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }

  public void ricercaPenaRideterminataCumulByIdDatiFinali( BigDecimal aIdDatiFinali  ) {
    String lSql = getSqlQuery();

    lSql += " and DAT_ID_DATI_FINALI_CUMULO = " + aIdDatiFinali;
    lSql += " and FLAG_PENA_RESIDUA_CUMULO = 'N' ";

    setStatement(lSql);
  }
  
  public void ricercaPenaResiduaCumulByIdDatiFinali( BigDecimal aIdDatiFinali  ) {
    String lSql = getSqlQuery();

    lSql += " and DAT_ID_DATI_FINALI_CUMULO = " + aIdDatiFinali;
    lSql += " and FLAG_PENA_RESIDUA_CUMULO = 'S' ";

    setStatement(lSql);
  }
  
  public void ricercaPenaResiduaCumulByIdIstr ( BigDecimal aIdIstruttoria  ) {
    String lSql = getSqlQuery();

    lSql += " and ISTR_ID_ISTRUTTORIA_CUMULO = " + aIdIstruttoria;
    lSql += " and FLAG_PENA_RESIDUA_CUMULO = 'S' ";

    setStatement(lSql);
  }     
  
  //public void ricercaPenaRideterminataCumulByIdIstruttoria ( BigDecimal aIdDatiFinali  ) {
  public void ricercaPenaRideterminataCumulByIdIstruttoria ( BigDecimal aId  ) {	  
    String lSql = getSqlQuery();

    lSql += " and ISTR_ID_ISTRUTTORIA_CUMULO = " + aId;
    //lSql += " and FLAG_PENA_RESIDUA_CUMULO = 'N' ";

    setStatement(lSql);
  }
  
  /**
   * Ricerca le PENA_RIDETERMINATA_CUMULO sul FASCILO legate ad eventi validati
   * ordinate per data inserimento desc. Serve per recuperare l'ultima Pena Cumulo
   * , la più recente
   * @param aIdDatiFinali
   */
  public void ricercaPeneRideterminateCumuloByIdFascDataInsDesc ( BigDecimal aIdFascicoloSiep  ) {
    String lStatement = new String("");

    lStatement += " SELECT " +
                  "ID_PENA_RIDETERMINATA_CUMULO, "+  
                  "COD_TIPO_PENA_DETENTIVA, CG_REF_CODES.RV_MEANING descTipoPena, "+  
                  "NUM_ANNI_RECLUSIONE, NUM_MESI_RECLUSIONE, NUM_GIORNI_RECLUSIONE, "+  
                  "IMPORTO_MULTA, "+  
                  "NUM_ANNI_ARRESTO, NUM_MESI_ARRESTO, NUM_GIORNI_ARRESTO, "+  
                  "IMPORTO_AMMENDA, "+  
                  "NUM_ANNI_ISOLAMENTO_DIURNO, "+  
                  "NUM_MESI_ISOLAMENTO_DIURNO, "+  
                  "NUM_GIORNI_ISOLAMENTO_DIURNO, "+  
                  "NUMERO_GIORNI_LA, NUMERO_GIORNI_LS, NUMERO_GIORNI_LI, "+  
                  "NUMERO_GIORNI_RIDUZIONE, "+  
                  "NUMERO_GIORNI_SCOMPUTO, "+
                  
                  "FLAG_PENA_RESIDUA_CUMULO, "+  
                  "DATA_INIZIO, "+  
                  "DATA_FINE_RECLUSIONE, DATA_INIZIO_ARRESTO, "+  
                  "DATA_FINE_PRESUNTA, "+  
                  "DATA_FINE, "+
                  "IS_PENA_DA_RICALCOLARE, "+
                  
                  "DAT_ID_DATI_FINALI_CUMULO, "+  
                  "PENA_RIDETERMINATA_CUMULO.ISTR_ID_ISTRUTTORIA_CUMULO, "+  
                  "PENA_RIDETERMINATA_CUMULO.COD_OPERATORE_INSERIMENTO, "+  
                  "PENA_RIDETERMINATA_CUMULO.DATA_INSERIMENTO, "+  
                  "PENA_RIDETERMINATA_CUMULO.COD_UFFICIO_INSERIMENTO, "+  
                  "PENA_RIDETERMINATA_CUMULO.COD_OPERATORE_AGGIORNAMENTO, "+  
                  "PENA_RIDETERMINATA_CUMULO.DATA_AGGIORNAMENTO, "+  
                  "PENA_RIDETERMINATA_CUMULO.COD_UFFICIO_AGGIORNAMENTO "; 
    // aggiungere qui gli eventuali campi descrizioni 

    // Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
    lStatement += " FROM PENA_RIDETERMINATA_CUMULO LEFT OUTER JOIN CG_REF_CODES ON COD_TIPO_PENA_DETENTIVA = CG_REF_CODES.RV_LOW_VALUE AND CG_REF_CODES.RV_DOMAIN = 'TIPO_PENA_DETENTIVA' ";
    lStatement +=    " , ISTRUTTORIA_CUMULO, EVENTO " ;
    lStatement += " WHERE 1=1 ";
    
    
    lStatement += " AND PENA_RIDETERMINATA_CUMULO.ISTR_ID_ISTRUTTORIA_CUMULO = ISTRUTTORIA_CUMULO.ID_ISTRUTTORIA_CUMULO ";
    lStatement += " AND ISTRUTTORIA_CUMULO.EVE_ID_EVENTO_PROV = EVENTO.ID_EVENTO ";
    lStatement += " AND PENA_RIDETERMINATA_CUMULO.FLAG_PENA_RESIDUA_CUMULO = 'S' ";
    lStatement += " AND EVENTO.FLAG_DOCUMENTO_REGISTRATO = 'S' ";
    lStatement += " AND EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = "+aIdFascicoloSiep;
        
    lStatement += " order by EVENTO.DATA_INSERIMENTO desc ";

    setStatement(lStatement);
  }
  
  

  /***************************************************************************** 
   * Metodo per la costruzione della sql query 
   * @return 
   ****************************************************************************/ 
  protected String getSqlQuery() {
    String lStatement = new String("");

    lStatement += " SELECT " +
                  "ID_PENA_RIDETERMINATA_CUMULO, "+  
                  "COD_TIPO_PENA_DETENTIVA, CG_REF_CODES.RV_MEANING descTipoPena, "+  
                  "NUM_ANNI_RECLUSIONE, "+  
                  "NUM_MESI_RECLUSIONE, "+  
                  "NUM_GIORNI_RECLUSIONE, "+  
                  "IMPORTO_MULTA, "+  
                  "NUM_ANNI_ARRESTO, "+  
                  "NUM_MESI_ARRESTO, "+  
                  "NUM_GIORNI_ARRESTO, "+  
                  "IMPORTO_AMMENDA, "+  
                  "NUM_ANNI_ISOLAMENTO_DIURNO, "+  
                  "NUM_MESI_ISOLAMENTO_DIURNO, "+  
                  "NUM_GIORNI_ISOLAMENTO_DIURNO, "+  
                  "NUMERO_GIORNI_LA, "+  
                  "NUMERO_GIORNI_LS, "+  
                  "NUMERO_GIORNI_LI, "+  
                  "NUMERO_GIORNI_RIDUZIONE, "+  
                  "NUMERO_GIORNI_SCOMPUTO, "+
                  
                  "FLAG_PENA_RESIDUA_CUMULO, "+  
                  "DATA_INIZIO, "+  
                  "DATA_FINE_RECLUSIONE, DATA_INIZIO_ARRESTO, "+  
                  "DATA_FINE_PRESUNTA, "+  
                  "DATA_FINE, "+
                  "IS_PENA_DA_RICALCOLARE, "+
                  
                  "DAT_ID_DATI_FINALI_CUMULO, "+  
                  "ISTR_ID_ISTRUTTORIA_CUMULO, "+  
                  "COD_OPERATORE_INSERIMENTO, "+  
                  "DATA_INSERIMENTO, "+  
                  "COD_UFFICIO_INSERIMENTO, "+  
                  "COD_OPERATORE_AGGIORNAMENTO, "+  
                  "DATA_AGGIORNAMENTO, "+  
                  "COD_UFFICIO_AGGIORNAMENTO "; 
    // aggiungere qui gli eventuali campi descrizioni 

    // Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
    lStatement += " FROM PENA_RIDETERMINATA_CUMULO LEFT OUTER JOIN CG_REF_CODES ON COD_TIPO_PENA_DETENTIVA = CG_REF_CODES.RV_LOW_VALUE AND CG_REF_CODES.RV_DOMAIN = 'TIPO_PENA_DETENTIVA' ";
    lStatement += " WHERE 1=1 ";
    
    return lStatement;
  }


  /***************************************************************************** 
   * Metodo che carica il record del result set nel model 
   * @return 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel  getModel() throws DAOException {
     PenaRideterminataCumuloModel aModel = new  PenaRideterminataCumuloModel(); 

    //Inserire le opportune set delle descrizioni!
    aModel.setIdPenaRideterminataCumulo ( getBigDecimal ("ID_PENA_RIDETERMINATA_CUMULO") ); 
    aModel.setCodTipoPenaDetentiva      ( getString     ("COD_TIPO_PENA_DETENTIVA"     ) ); 
    aModel.setDescrTipoPenaDetentiva    ( getString     ("descTipoPena") );
    aModel.setNumAnniReclusione         ( getBigDecimal ("NUM_ANNI_RECLUSIONE"         ) ); 
    aModel.setNumMesiReclusione         ( getBigDecimal ("NUM_MESI_RECLUSIONE"         ) ); 
    aModel.setNumGiorniReclusione       ( getBigDecimal ("NUM_GIORNI_RECLUSIONE"       ) ); 
    aModel.setImportoMulta              ( getBigDecimal ("IMPORTO_MULTA"               ) ); 
    aModel.setNumAnniArresto            ( getBigDecimal ("NUM_ANNI_ARRESTO"            ) ); 
    aModel.setNumMesiArresto            ( getBigDecimal ("NUM_MESI_ARRESTO"            ) ); 
    aModel.setNumGiorniArresto          ( getBigDecimal ("NUM_GIORNI_ARRESTO"          ) ); 
    aModel.setImportoAmmenda            ( getBigDecimal ("IMPORTO_AMMENDA"             ) ); 
    aModel.setNumAnniIsolamentoDiurno   ( getBigDecimal ("NUM_ANNI_ISOLAMENTO_DIURNO"  ) ); 
    aModel.setNumMesiIsolamentoDiurno   ( getBigDecimal ("NUM_MESI_ISOLAMENTO_DIURNO"  ) ); 
    aModel.setNumGiorniIsolamentoDiurno ( getBigDecimal ("NUM_GIORNI_ISOLAMENTO_DIURNO") ); 
    aModel.setNumeroGiorniLA            ( getBigDecimal ("NUMERO_GIORNI_LA"            ) ); 
    aModel.setNumeroGiorniLS            ( getBigDecimal ("NUMERO_GIORNI_LS"            ) ); 
    aModel.setNumeroGiorniLI            ( getBigDecimal ("NUMERO_GIORNI_LI"            ) ); 
    aModel.setNumeroGiorniRiduzione     ( getBigDecimal ("NUMERO_GIORNI_RIDUZIONE"     ) );
    aModel.setNumeroGiorniScomputo      ( getBigDecimal ("NUMERO_GIORNI_SCOMPUTO"      ) );    
    
    aModel.setFlagPenaResiduaCumulo     ( getString     ("FLAG_PENA_RESIDUA_CUMULO"    ) ); 
    aModel.setDataInizio                ( getDate       ("DATA_INIZIO"                 ) ); 
    aModel.setDataFineReclusione        ( getDate       ("DATA_FINE_RECLUSIONE"        ) ); 
    aModel.setDataInizioArresto         ( getDate       ("DATA_INIZIO_ARRESTO"         ) ); 
    aModel.setDataFinePresunta          ( getDate       ("DATA_FINE_PRESUNTA"          ) ); 
    aModel.setDataFine                  ( getDate       ("DATA_FINE"                   ) ); 
    
    aModel.setIsPenaDaRicalcolare       ( getString     ("IS_PENA_DA_RICALCOLARE"      ) ); 
    
    aModel.setDatIdDatiFinaliCumulo     ( getBigDecimal ("DAT_ID_DATI_FINALI_CUMULO"   ) ); 
    aModel.setIstrIdIstruttoriaCumulo   ( getBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO"  ) ); 

    aModel.setCodOperatoreInserimento   ( getString     ("COD_OPERATORE_INSERIMENTO"   ) ); 
    aModel.setDataInserimento           ( getDate       ("DATA_INSERIMENTO"            ) ); 
    aModel.setCodUfficioInserimento     ( getString     ("COD_UFFICIO_INSERIMENTO"     ) ); 
    aModel.setCodOperatoreAggiornamento ( getString     ("COD_OPERATORE_AGGIORNAMENTO" ) ); 
    aModel.setDataAggiornamento         ( getDate       ("DATA_AGGIORNAMENTO"          ) ); 
    aModel.setCodUfficioAggiornamento   ( getString     ("COD_UFFICIO_AGGIORNAMENTO"   ) ); 

    return aModel;
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public String setCondizioni(PenaRideterminataCumuloModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdPenaRideterminataCumulo() != null ) { 
      lCondizioni += " and ID_PENA_RIDETERMINATA_CUMULO = " + aModel.getIdPenaRideterminataCumulo() + ""; 
    } 
    if (aModel.getCodTipoPenaDetentiva() != null && aModel.getCodTipoPenaDetentiva().length() > 0) { 
      lCondizioni += " and COD_TIPO_PENA_DETENTIVA = '" + aModel.getCodTipoPenaDetentiva() + "' "; 
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
    if (aModel.getNumAnniIsolamentoDiurno() != null ) { 
      lCondizioni += " and NUM_ANNI_ISOLAMENTO_DIURNO = " + aModel.getNumAnniIsolamentoDiurno() + ""; 
    } 
    if (aModel.getNumMesiIsolamentoDiurno() != null ) { 
      lCondizioni += " and NUM_MESI_ISOLAMENTO_DIURNO = " + aModel.getNumMesiIsolamentoDiurno() + ""; 
    } 
    if (aModel.getNumGiorniIsolamentoDiurno() != null ) { 
      lCondizioni += " and NUM_GIORNI_ISOLAMENTO_DIURNO = " + aModel.getNumGiorniIsolamentoDiurno() + ""; 
    } 
    if (aModel.getNumeroGiorniLA() != null ) { 
      lCondizioni += " and NUMERO_GIORNI_LA = " + aModel.getNumeroGiorniLA() + ""; 
    } 
    if (aModel.getNumeroGiorniLS() != null ) { 
      lCondizioni += " and NUMERO_GIORNI_LS = " + aModel.getNumeroGiorniLS() + ""; 
    } 
    if (aModel.getNumeroGiorniLI() != null ) { 
      lCondizioni += " and NUMERO_GIORNI_LI = " + aModel.getNumeroGiorniLI() + ""; 
    } 
    if (aModel.getNumeroGiorniRiduzione() != null ) { 
      lCondizioni += " and NUMERO_GIORNI_RIDUZIONE = " + aModel.getNumeroGiorniRiduzione() + ""; 
    } 
    if (aModel.getNumeroGiorniScomputo() != null ) { 
      lCondizioni += " and NUMERO_GIORNI_SCOMPUTO = " + aModel.getNumeroGiorniScomputo() + ""; 
    } 
    if (aModel.getFlagPenaResiduaCumulo() != null && aModel.getFlagPenaResiduaCumulo().length() > 0) { 
      lCondizioni += " and FLAG_PENA_RESIDUA_CUMULO = '" + aModel.getFlagPenaResiduaCumulo() + "' "; 
    } 
    if (aModel.getDataInizio() != null ) { 
      lCondizioni += " and to_char(DATA_INIZIO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInizio(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataFineReclusione() != null ) { 
      lCondizioni += " and to_char(DATA_FINE_RECLUSIONE,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataFineReclusione(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataInizioArresto() != null ) { 
      lCondizioni += " and to_char(DATA_INIZIO_ARRESTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInizioArresto(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataFinePresunta() != null ) { 
      lCondizioni += " and to_char(DATA_FINE_PRESUNTA,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataFinePresunta(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getDataFine() != null ) { 
      lCondizioni += " and to_char(DATA_FINE,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataFine(),"dd/MM/yyyy") + "' "; 
    }
    if (aModel.getIsPenaDaRicalcolare() != null && aModel.getIsPenaDaRicalcolare().length() > 0) { 
      lCondizioni += " and IS_PENA_DA_RICALCOLARE = '" + aModel.getIsPenaDaRicalcolare() + "' "; 
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
  public String setCondizioniByKey( BigDecimal aIdPenaRideterminataCumulo  ) {
    String lCondizioni = new String();

    lCondizioni += " and ID_PENA_RIDETERMINATA_CUMULO = " + aIdPenaRideterminataCumulo;

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
