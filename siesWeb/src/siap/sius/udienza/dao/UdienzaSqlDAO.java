package siap.sius.udienza.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.udienza.model.UdienzaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
* <p>Title: UdienzaSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella Udienza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class UdienzaSqlDAO extends SIAPSqlDAO
{
  /**
   * Costruttore di classe con argomento la Connection
   * <p>
   * @param con Connection Connessione al dbase.
   */
  public UdienzaSqlDAO (Connection con)
  {
    super(con);
  }

  //
  // METODO RICERCA()
  //
  
  /**
   * Metodo che inposta lo statement per la ricerca di una udienza
   * i parametri di filtro vengono impostati nel model passato come argomento
   * <p>
   * @param aModel UdienzaModel istanza dell'oggetto.
   * @throws DAOException Propaga errore di eccezione.
   */
  public void ricercaUdienza( UdienzaModel  aModel )
  throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " " + setCondizione(aModel);
    // 09/02/2006 lSql += " ORDER BY UDI.NUM_COLLEGIO, UDI.DATA_UDIENZA";
    // Recupera il tipo di ordinamento.
    lSql += getTipoOrdinamento( aModel );

    setStatement(lSql);
  }

  //
  // METODO ricercaUdienzaGenerale()
  //
  
  /**
   * Metodo che imposta lo statement per la ricerca Udienza
   * utilizzando come filtro i parametri passati come argomento,
   * opportunamente incapsulati nell'oggetto UdeinzaModel.
   * <p>
   * @param aModel UdienzaModel istanza udienza model.
   * @throws DAOException Propaga errore di eccezione.
   */
  public void ricercaUdienzaGenerale( UdienzaModel  aModel )
  throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " " + setCondizioneGenerale(aModel);
    // 09/02/2006 lSql += " ORDER BY UDI.NUM_COLLEGIO, UDI.DATA_UDIENZA";
    lSql += " ORDER BY UDI.DATA_UDIENZA, UDI.NUM_COLLEGIO";
    setStatement(lSql);
  }

  //
  // METODO ricercaUdienzaUDS()
  //
  /**
   * Metodo che imposta lo statement per la ricerca udienza per
   * l'UDS ( Ufficio di Sorveglianza ).
   * <p>
   * @param aModel UdienzaModel Istanza UdienzaModel
   * @throws DAOException Propaga errore di eccezione.
   */
  public void ricercaUdienzaUDS( UdienzaModel  aModel )
  throws DAOException
  {
    String lSql = getSqlQueryUDS();

    lSql += " " + setCondizioneUDS(aModel);
    // 09/02/2006 lSql += " ORDER BY UDI.NUM_COLLEGIO, UDI.DATA_UDIENZA";
    // Recupera il tipo di ordinamento.
    lSql += getTipoOrdinamento( aModel );

    setStatement(lSql);
  }

  //
  // METODO ricercaNuoveUdienze()
  //
  /**
   * Metodo che imposta lo statement per la ricerca di nuove udienze.
   * <p>
   * @param aModel UdienzaModel Istanza oggetto UdienzaModel
   * @param aGeneraleProcedimento GeneraleProcedimentoModel Istanza oggetto GeneraleProcedimentoModel
   * @throws DAOException Propaga errore di eccezione. 
   */
  public void ricercaNuoveUdienze( UdienzaModel  aModel, GeneraleProcedimentoModel aGeneraleProcedimento )
  throws DAOException
  {
    String lSql = getSqlQueryNuoveUdienze(aGeneraleProcedimento);
    lSql += " " + setCondizioneNuoveUdienze(aModel);
    lSql += " ORDER BY  UDI.DATA_UDIENZA ";
    setStatement(lSql);
  }

  //
  // METODO RICERCA()delle 30 udienze precedenti
  //
  /**
   * Metodo che imposta lo statement per la ricerca delle 30 udienze precedenti.
   * <p>
   * @param aModel UdienzaModel Istanza UdienzaModel.
   * @param aGeneraleProcedeimento GeneraleProcedimentoModel Istanza GeneraleProcedimentoModel.
   * @throws DAOException Propaga errore di eccezione. 
   */
  public void ricercaUdienzePrecedenti( UdienzaModel  aModel, GeneraleProcedimentoModel aGeneraleProcedimento )
  throws DAOException
  {
    String lSql = getSqlQueryUdienzePrecedenti(aModel, aGeneraleProcedimento);
    lSql += " ORDER BY  UDI.DATA_UDIENZA desc";
    setStatement(lSql);
  }

  /**
   * Metodod che imposta lo statement per la ricerca delle udienze per data
   * <p>
   * @param aKey data
   * @param aCodUfficio codice d'ufficio. 
   * @throws DAOException Propaga errore di eccezione.
   */
   public void ricercaUdienzaByDate( String aKey ,String aCodUfficio)
   throws DAOException
   {
      String lSelect = getSqlQuery();
      lSelect += " AND DATA_UDIENZA = TO_DATE(" + aKey +",'YYYYMMDD')";
      lSelect += " AND UDI.COD_UFFICIO_APPARTENENZA = '" + aCodUfficio + "'";
      setStatement(lSelect);
   }

  /**
   * Imposta lo statement per la ricerca della udienza, per il proprio ID
   * <p>
   * @param aKey Id chiave.
   * @throws DAOException Propaga errore di eccezione.
   */
  public void ricercaUdienzaByKey( BigDecimal aKey )
  throws DAOException
  {
    String lSql = getSqlQuery();
    lSql +=  setCondizioniByKey(aKey);
    setStatement(lSql);
  }

  /**
   * Metodo che ritorna la query SQL per le Nuove Udienze
   * <p>
   * @param aGeneraleProcedimento GeneraleProcedimentoModel
   * @return ritorna la stringa query sql. 
   */
  protected String getSqlQueryNuoveUdienze(GeneraleProcedimentoModel aGeneraleProcedimento)
  {
    String lSelect = new String("");
    lSelect += "SELECT distinct  UDI.ID_UDIENZA   ,";
    lSelect += " UDI.DATA_UDIENZA, ";
    lSelect += " UDI.COD_PRESIDENTE, ";
    lSelect += " ( PRES.COGNOME || '  ' || PRES.NOME ) AS DESC_PRES, ";
    lSelect += " UDI.COD_GIUDICE_1, ";
    lSelect += " ( GIU1.COGNOME || '  ' || GIU1.NOME ) AS DESC_GIUD_1, ";
    lSelect += " UDI.COD_GIUDICE_2, ";
    lSelect += " ( GIU2.COGNOME || '  ' || GIU2.NOME ) AS DESC_GIUD_2, ";
    lSelect += " UDI.COD_PG, ";
    lSelect += " UDI.COD_ID_ESPERTO_1, ";
    lSelect += " UDI.COD_ID_ESPERTO_2, ";
    lSelect += " UDI.COD_ID_ASSISTENTE, ";
    //lSelect += " UDI.FLAG_RINVIATA, ";
    lSelect += " UDI.NUMERO_MAX_FASCICOLI, ";
    lSelect += " UDI.COD_OPERATORE_INSERIMENTO, ";
    lSelect += " UDI.DATA_INSERIMENTO, ";
    lSelect += " UDI.COD_UFFICIO_INSERIMENTO, ";
    lSelect += " UDI.COD_OPERATORE_AGGIORNAMENTO, ";
    lSelect += " UDI.DATA_AGGIORNAMENTO, ";
    lSelect += " UDI.COD_UFFICIO_AGGIORNAMENTO, ";
    lSelect += " UDI.LUOGO_UDIENZA, ";
    lSelect += " UDI.COD_UFFICIO_APPARTENENZA, ";
    lSelect += " UDI.NUM_COLLEGIO, ";
    lSelect += " UDI.ORA_INIZIO, ";
    lSelect += " UDI.MIN_INIZIO, ";
    lSelect += " UDI.ORA_FINE, ";
    lSelect += " UDI.MIN_FINE, ";
    lSelect += " UDI.ORA_FINE_CC, ";
    lSelect += " UDI.MIN_FINE_CC ";
    lSelect += " FROM UDIENZA UDI, ";
    lSelect += " MAGISTRATO PRES, ";
    lSelect += " MAGISTRATO GIU1, ";
    lSelect += " MAGISTRATO GIU2 ";
    lSelect += " WHERE ";
    lSelect += " (UDI.COD_GIUDICE_1,UDI.COD_GIUDICE_2,UDI.COD_PRESIDENTE) IN (select UDI2.COD_GIUDICE_1,UDI2.COD_GIUDICE_2,UDI2.COD_PRESIDENTE ";
    lSelect += "    from UDIENZA UDI2, GENERALE_PROCEDIMENTO GP ";
    lSelect += "   where   GP.ID_GENERALE_PROCEDIMENTO ="+ aGeneraleProcedimento.getIdGeneraleProcedimento() ;
    lSelect += "     AND  UDI2.ID_UDIENZA  = GP.UDI_ID_UDIENZA";
    lSelect += "     AND  UDI.COD_GIUDICE_1= UDI2.COD_GIUDICE_1";
    lSelect += "     AND  UDI.COD_GIUDICE_2= UDI2.COD_GIUDICE_2";
    lSelect += "     AND  UDI.COD_PRESIDENTE= UDI2.COD_PRESIDENTE";
   // lSelect += "   AND  UDI.COD_ID_ESPERTO_1= UDI2.COD_ID_ESPERTO_1";
   // lSelect += "   AND  UDI.COD_ID_ESPERTO_2= UDI2.COD_ID_ESPERTO_2";
    lSelect += " ) ";
    lSelect += " AND UDI.COD_PRESIDENTE	    = PRES.COD_MAGISTRATO ";
    lSelect += " AND UDI.COD_GIUDICE_1      = GIU1.COD_MAGISTRATO ";
    lSelect += " AND UDI.COD_GIUDICE_2      = GIU2.COD_MAGISTRATO ";


   //lStatement += " WHERE ";
    return lSelect;
  }

  /**
   * Metodo che ritorna la query SQL.
   * <p>
   * @return ritorna la stringa query sql. 
   */
  protected String getSqlQuery()
  {
    String lSelect = new String("");

    lSelect += "SELECT DISTINCT UDI.ID_UDIENZA   ,";
    lSelect += " UDI.DATA_UDIENZA, ";
    lSelect += " UDI.COD_PRESIDENTE, ";
    lSelect += " ( PRES.COGNOME || '  ' || PRES.NOME ) AS DESC_PRES, ";
    lSelect += " UDI.COD_GIUDICE_1, ";
    lSelect += " ( GIU1.COGNOME || '  ' || GIU1.NOME ) AS DESC_GIUD_1, ";
    lSelect += " UDI.COD_GIUDICE_2, ";
    lSelect += " ( GIU2.COGNOME || '  ' || GIU2.NOME ) AS DESC_GIUD_2, ";
    lSelect += " UDI.COD_PG, ";
    lSelect += " ( PROC.COGNOME || '  ' || PROC.NOME ) AS DESC_PG, ";
    lSelect += " UDI.COD_ID_ESPERTO_1, ";
    lSelect += " ( ESP1.COGNOME || '  ' || ESP1.NOME )   AS DESC_ESP_1, ";
    lSelect += " UDI.COD_ID_ESPERTO_2, ";
    lSelect += " ( ESP2.COGNOME || '  ' || ESP2.NOME ) AS DESC_ESP_2, ";
    lSelect += " UDI.COD_ID_ASSISTENTE, ";
    lSelect += " ( ASS.COGNOME || '  ' || ASS.NOME )   AS DESC_ASS, ";
    //lSelect += " UDI.FLAG_RINVIATA, ";
    lSelect += " UDI.NUMERO_MAX_FASCICOLI, ";
    lSelect += " UDI.COD_OPERATORE_INSERIMENTO, ";
    lSelect += " UDI.DATA_INSERIMENTO, ";
    lSelect += " UDI.COD_UFFICIO_INSERIMENTO, ";
    lSelect += " UDI.COD_OPERATORE_AGGIORNAMENTO, ";
    lSelect += " UDI.DATA_AGGIORNAMENTO, ";
    lSelect += " UDI.COD_UFFICIO_AGGIORNAMENTO, ";
    lSelect += " UDI.LUOGO_UDIENZA, ";
    lSelect += " UDI.COD_UFFICIO_APPARTENENZA, ";
    lSelect += " UDI.NUM_COLLEGIO, ";
    lSelect += " UDI.ORA_INIZIO, ";
    lSelect += " UDI.MIN_INIZIO, ";
    lSelect += " UDI.ORA_FINE, ";
    lSelect += " UDI.MIN_FINE, ";
    lSelect += " UDI.ORA_FINE_CC, ";
    lSelect += " UDI.MIN_FINE_CC ";
    lSelect += " FROM UDIENZA UDI, ";
    lSelect += " MAGISTRATO PRES, ";
    lSelect += " MAGISTRATO GIU1, ";
    lSelect += " MAGISTRATO GIU2, ";
    lSelect += " MAGISTRATO PROC, ";
    lSelect += " ESPERTO ESP1, ";
    lSelect += " ESPERTO ESP2, ";
    lSelect += " ASSISTENTE_GIUDIZIARIO ASS ";
    lSelect += " WHERE ";
    // MEV10-s3: aggiunte outer join su campi non obbligatori
    lSelect += " UDI.COD_GIUDICE_1 = GIU1.COD_MAGISTRATO(+) ";
    lSelect += " AND UDI.COD_GIUDICE_2 = GIU2.COD_MAGISTRATO(+) ";
    lSelect += " AND UDI.COD_PRESIDENTE = PRES.COD_MAGISTRATO(+) ";
    lSelect += " AND PRES.COD_UFFICIO_APPARTENENZA(+) = UDI.COD_UFFICIO_APPARTENENZA ";
    lSelect += " AND UDI.COD_PG = PROC.COD_MAGISTRATO(+) ";
    lSelect += " AND UDI.COD_ID_ASSISTENTE = ASS.ID_ASSISTENTE_GIUDIZIARIO(+) ";
    lSelect += " AND UDI.COD_ID_ESPERTO_1 = ESP1.ID_ESPERTO ";
    lSelect += " AND UDI.COD_ID_ESPERTO_2 = ESP2.ID_ESPERTO ";

   //lStatement += " WHERE ";
    return lSelect;
  }

  /**
   * Metodo che ritorna la query SQL UDS.
   * <p>
   * @return ritorna la stringa query sql. 
   */
  protected String getSqlQueryUDS()
  {
    String lSelect = new String("");

    lSelect += "SELECT DISTINCT UDI.ID_UDIENZA,";
    lSelect += " UDI.DATA_UDIENZA,";
    lSelect += " UDI.COD_PRESIDENTE,";
    lSelect += " ( PRES.COGNOME || '  ' || PRES.NOME ) AS DESC_PRES,";
    lSelect += " UDI.COD_PG,";
    lSelect += " ( PROC.COGNOME || '  ' || PROC.NOME ) AS DESC_PG,";
    lSelect += " UDI.COD_ID_ASSISTENTE,";
    lSelect += " ( ASS.COGNOME || '  ' || ASS.NOME )   AS DESC_ASS,";
    //lSelect += " UDI.FLAG_RINVIATA, ";
    lSelect += " UDI.NUMERO_MAX_FASCICOLI,";
    lSelect += " UDI.COD_OPERATORE_INSERIMENTO,";
    lSelect += " UDI.DATA_INSERIMENTO,";
    lSelect += " UDI.COD_UFFICIO_INSERIMENTO,";
    lSelect += " UDI.COD_OPERATORE_AGGIORNAMENTO,";
    lSelect += " UDI.DATA_AGGIORNAMENTO,";
    lSelect += " UDI.COD_UFFICIO_AGGIORNAMENTO,";
    lSelect += " UDI.LUOGO_UDIENZA,";
    lSelect += " UDI.COD_UFFICIO_APPARTENENZA,";
    lSelect += " UDI.NUM_COLLEGIO,";
    lSelect += " UDI.ORA_INIZIO,";
    lSelect += " UDI.MIN_INIZIO,";
    lSelect += " UDI.ORA_FINE,";
    lSelect += " UDI.MIN_FINE";
    lSelect += " FROM UDIENZA UDI,";
    lSelect += " MAGISTRATO PRES,";
    lSelect += " MAGISTRATO PROC,";
    lSelect += " ASSISTENTE_GIUDIZIARIO ASS";
    lSelect += " WHERE";
    // MEV10-s3: aggiunte join condition sulla tabella "MAGISTRATO"
    lSelect += " UDI.COD_PRESIDENTE = PRES.COD_MAGISTRATO(+)";
    lSelect += " AND UDI.COD_PG = PROC.COD_MAGISTRATO";
    lSelect += " AND PRES.COD_UFFICIO_APPARTENENZA(+) = UDI.COD_UFFICIO_APPARTENENZA";
    lSelect += " AND UDI.COD_ID_ASSISTENTE = ASS.ID_ASSISTENTE_GIUDIZIARIO";


   //lStatement += " WHERE ";
    return lSelect;
  }

  /**
   * Metodo che ritorna la query SQL per  Udienze Precedenti.
   * <p>
   * @return ritorna la stringa query sql. 
   */
  protected String getSqlQueryUdienzePrecedenti(UdienzaModel  aModel, GeneraleProcedimentoModel aGeneraleProcedimento)
  {
    String lSelect = new String("");

    lSelect += "SELECT UDI.ID_UDIENZA   ,";
    lSelect += " UDI.DATA_UDIENZA ";
    lSelect += " FROM UDIENZA UDI, ";
    lSelect += " GENERALE_PROCEDIMENTO GP, ";
    lSelect += " UDIENZA_PROCEDIMENTO UP ";
    lSelect += " WHERE ";
    lSelect += " GP.ID_GENERALE_PROCEDIMENTO = "+ aGeneraleProcedimento.getIdGeneraleProcedimento();
    lSelect += " AND GP.ID_GENERALE_PROCEDIMENTO = UP.GEN_PRID_GENERALE_PROCEDIMENTO ";
    lSelect += " AND UP.UDI_ID_UDIENZA = UDI.ID_UDIENZA ";
    lSelect += " AND UDI.DATA_UDIENZA < to_date(" + DateUtils.getDateToString(aModel.getDataUdienza(), "yyyyMMdd" )+  ",'yyyyMMdd') ";
    lSelect += " AND rownum <= 30 ";

   //lStatement += " WHERE ";
    return lSelect;
  }

  //
  // METODO GETMODEL()
  //
  
  /**
   * Metodod che ritorna il model ( UdienzaModel ) con i dati prelevati dal
   * DBase.
   * <p>
   * @return GenericModel  Istanza di UdienzaModel 
   * @throws DAOException Propaga errore di eccezione.  
   */
  public GenericModel getModel() throws DAOException
  {
    UdienzaModel aModel = new  UdienzaModel();

    //Inserire le opportune set delle descrizioni!
    aModel.setIdUdienza(getBigDecimal("ID_UDIENZA") );
    aModel.setDataUdienza(getDate("DATA_UDIENZA") );
    aModel.setCodPresidente(getString("COD_PRESIDENTE") );
    aModel.setDescrPresidente(getString("DESC_PRES") );
    aModel.setCodGiudice1(getString("COD_GIUDICE_1") );
    aModel.setDescrGiudice1(getString("DESC_GIUD_1") );
    aModel.setCodGiudice2(getString("COD_GIUDICE_2") );
    aModel.setDescrGiudice2(getString("DESC_GIUD_2") );
    aModel.setCodPg(getString("COD_PG") );
    aModel.setDescrPg(getString("DESC_PG") );
    aModel.setCodIdEsperto1(getBigDecimal("COD_ID_ESPERTO_1") );
    aModel.setDescrIdEsperto1(getString("DESC_ESP_1") );
    aModel.setCodIdEsperto2(getBigDecimal("COD_ID_ESPERTO_2") );
    aModel.setDescrIdEsperto2(getString("DESC_ESP_2") );
    aModel.setCodIdAssistente(getBigDecimal("COD_ID_ASSISTENTE") );
    aModel.setDescrIdAssistente(getString("DESC_ASS") );
    //aModel.setFlagRinviata(getString("FLAG_RINVIATA") );
    aModel.setNumeroMaxFascicoli(getBigDecimal("NUMERO_MAX_FASCICOLI") );
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
    //aModel.setDescrUfficioInserimento(getString("") );
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
    //aModel.setDescrUfficioAggiornamento(getString("") );
    aModel.setLuogoUdienza(getString("LUOGO_UDIENZA") );
    aModel.setCodUfficioAppartenenza(getString("COD_UFFICIO_APPARTENENZA") );
    //aModel.setDescrUfficioAppartenenza(getString("") );
    aModel.setNumCollegio(getBigDecimal("NUM_COLLEGIO") );
    aModel.setOraInizio( getString("ORA_INIZIO"));
    aModel.setMinInizio( getString("MIN_INIZIO"));
    aModel.setOraFine( getString("ORA_FINE"));
    aModel.setMinFine( getString("MIN_FINE"));
    aModel.setOraFineCC( getString("ORA_FINE_CC"));
    aModel.setMinFineCC( getString("MIN_FINE_CC"));
    
    return aModel;
  }

  //
  //  nuovo metodo METODO GETMODEL() 09/12/2003
  //
  public GenericModel getModelByKeyUDS() throws DAOException
  {
    UdienzaModel aModel = new  UdienzaModel();

    aModel.setIdUdienza(getBigDecimal("ID_UDIENZA") );
    aModel.setDataUdienza(getDate("DATA_UDIENZA") );
    aModel.setCodPresidente(getString("COD_PRESIDENTE") );
    aModel.setDescrPresidente(getString("DESC_PRES") );
    // aModel.setCodGiudice1(getString("COD_GIUDICE_1") );
    // aModel.setDescrGiudice1(getString("DESC_GIUD_1") );
    // aModel.setCodGiudice2(getString("COD_GIUDICE_2") );
    // aModel.setDescrGiudice2(getString("DESC_GIUD_2") );
    aModel.setCodPg(getString("COD_PG") );
    aModel.setDescrPg(getString("DESC_PG") );
    // aModel.setCodIdEsperto1(getBigDecimal("COD_ID_ESPERTO_1") );
    // aModel.setDescrIdEsperto1(getString("DESC_ESP_1") );
    // aModel.setCodIdEsperto2(getBigDecimal("COD_ID_ESPERTO_2") );
    // aModel.setDescrIdEsperto2(getString("DESC_ESP_2") );
    aModel.setCodIdAssistente(getBigDecimal("COD_ID_ASSISTENTE") );
    aModel.setDescrIdAssistente(getString("DESC_ASS") );
    // aModel.setFlagRinviata(getString("FLAG_RINVIATA") );
    aModel.setNumeroMaxFascicoli(getBigDecimal("NUMERO_MAX_FASCICOLI") );
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
    // aModel.setDescrUfficioInserimento(getString("") );
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
    // aModel.setDescrUfficioAggiornamento(getString("") );
    aModel.setLuogoUdienza(getString("LUOGO_UDIENZA") );
    aModel.setCodUfficioAppartenenza(getString("COD_UFFICIO_APPARTENENZA") );
    // aModel.setDescrUfficioAppartenenza(getString("") );
    aModel.setNumCollegio(getBigDecimal("NUM_COLLEGIO") );
    aModel.setOraInizio( getString("ORA_INIZIO"));
    aModel.setMinInizio( getString("MIN_INIZIO"));
    aModel.setOraFine( getString("ORA_FINE"));
    aModel.setMinFine( getString("MIN_FINE"));

    return aModel;
  }

  //
  // METODO GETMODEL()
  //
  public GenericModel getModelNuoveUdienze() throws DAOException
  {
    UdienzaModel aModel = new  UdienzaModel();

    //Inserire le opportune set delle descrizioni!
    aModel.setIdUdienza(getBigDecimal("ID_UDIENZA") );
    aModel.setDataUdienza(getDate("DATA_UDIENZA") );
    aModel.setCodPresidente(getString("COD_PRESIDENTE") );
    aModel.setDescrPresidente(getString("DESC_PRES") );
    aModel.setCodGiudice1(getString("COD_GIUDICE_1") );
    aModel.setDescrGiudice1(getString("DESC_GIUD_1") );
    aModel.setCodGiudice2(getString("COD_GIUDICE_2") );
    aModel.setDescrGiudice2(getString("DESC_GIUD_2") );
    aModel.setCodPg(getString("COD_PG") );
    // aModel.setDescrPg(getString("DESC_PG") );
    aModel.setCodIdEsperto1(getBigDecimal("COD_ID_ESPERTO_1") );
    // aModel.setDescrIdEsperto1(getString("DESC_ESP_1") );
    aModel.setCodIdEsperto2(getBigDecimal("COD_ID_ESPERTO_2") );
    // aModel.setDescrIdEsperto2(getString("DESC_ESP_2") );
    aModel.setCodIdAssistente(getBigDecimal("COD_ID_ASSISTENTE") );
    // aModel.setDescrIdAssistente(getString("DESC_ASS") );
    // aModel.setFlagRinviata(getString("FLAG_RINVIATA") );
    aModel.setNumeroMaxFascicoli(getBigDecimal("NUMERO_MAX_FASCICOLI") );
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
    // aModel.setDescrUfficioInserimento(getString("") );
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO") );
    // aModel.setDescrUfficioAggiornamento(getString("") );
    aModel.setLuogoUdienza(getString("LUOGO_UDIENZA") );
    aModel.setCodUfficioAppartenenza(getString("COD_UFFICIO_APPARTENENZA") );
    // aModel.setDescrUfficioAppartenenza(getString("") );
    aModel.setNumCollegio(getBigDecimal("NUM_COLLEGIO") );

    aModel.setOraInizio( getString("ORA_INIZIO"));
    aModel.setMinInizio( getString("MIN_INIZIO"));
    aModel.setOraFine( getString("ORA_FINE"));
    aModel.setMinFine( getString("MIN_FINE"));

    
    return aModel;
  }

  //
  // METODO GETMODEL()
  //
  public GenericModel getModelUdienzePrecedenti() throws DAOException
  {
    UdienzaModel aModel = new  UdienzaModel();

    //Inserire le opportune set delle descrizioni!
    aModel.setIdUdienza(getBigDecimal("ID_UDIENZA") );
    aModel.setDataUdienza(getDate("DATA_UDIENZA") );

    return aModel;
  }

  /**
   * <p>
   * @param aKey
   * @throws DAOException
   */
  public void ricercaDattaglioUdienzaByKey( BigDecimal aKey )
  throws DAOException
  {
    String lSelect = new String();

    lSelect = getSqlQuery();
    lSelect += " AND UDI.ID_UDIENZA = " + aKey ;

    setStatement(lSelect);
  }

  /**
   * <p>
   * @param aKey
   * @throws DAOException
   */
  public void ricercaDattaglioUdienzaByKeyUDS( BigDecimal aKey )
  throws DAOException
  {
    String lSelect = new String();
    lSelect = getSqlQueryUDS();
    lSelect += " AND UDI.ID_UDIENZA = " + aKey;
    setStatement(lSelect);
  }

  /**
   *
   * @param aModel
   * @return
   */
  public String setCondizione(UdienzaModel aModel)
  {
    String lCondizioni = new String();

    // boolean lInserito = false;

    if (aModel.getNumCollegio() != null)
      lCondizioni += " AND UDI.NUM_COLLEGIO = " + aModel.getNumCollegio();
    if (aModel.getCodUfficioAppartenenza() != null)
      lCondizioni += " AND UDI.COD_UFFICIO_APPARTENENZA = '" + aModel.getCodUfficioAppartenenza() + "'";
    if (aModel.getDataUdienza() != null)
      lCondizioni += " AND UDI.DATA_UDIENZA >= TO_DATE(" + DateUtils.getDateToString( aModel.getDataUdienza(), "yyyyMMdd" ) + ",'YYYYMMDD' )";
    if (aModel.getDataUdienzaFine() != null)
      lCondizioni += " AND UDI.DATA_UDIENZA <= TO_DATE(" + DateUtils.getDateToString( aModel.getDataUdienzaFine(), "yyyyMMdd" ) + ",'YYYYMMDD' )";
 
    if (aModel.getCodPresidente() != null && aModel.getCodPresidente().length() > 1)
      lCondizioni += " AND UDI.COD_PRESIDENTE = '" + aModel.getCodPresidente() + "'";
    if (aModel.getCodGiudice1() != null && aModel.getCodGiudice1().length()> 1)
     lCondizioni += " AND UDI.COD_GIUDICE_1 = '" + aModel.getCodGiudice1()+ "'";
    if (aModel.getCodGiudice2() != null && aModel.getCodGiudice2().length() > 1)
     lCondizioni += " AND UDI.COD_GIUDICE_2 = '" + aModel.getCodGiudice2()+ "'";
    if (aModel.getCodPg() != null && aModel.getCodPg().length() > 1)
     lCondizioni += " AND UDI.COD_PG = '" + aModel.getCodPg() + "'";
 
    if (aModel.getCodIdEsperto1() != null && 
        aModel.getCodIdEsperto1().toString().length() > 4)
     lCondizioni += " AND UDI.COD_ID_ESPERTO_1 = " + aModel.getCodIdEsperto1();
    if (aModel.getCodIdEsperto2() != null && 
        aModel.getCodIdEsperto2().toString().length() > 4)
     lCondizioni += " AND UDI.COD_ID_ESPERTO_2 = " + aModel.getCodIdEsperto2();
    if (aModel.getCodIdAssistente() != null && 
        aModel.getCodIdAssistente().toString().length() > 4)
     lCondizioni += " AND UDI.COD_ID_ASSISTENTE = " + aModel.getCodIdAssistente();
     
    return lCondizioni;
  }

  /**
   *
   * @param aModel
   * @return
   */
  public String setCondizioneGenerale(UdienzaModel aModel)
  {
    String lCondizioni = new String();

    // boolean lInserito = false;
    if (aModel.getCodUfficioAppartenenza() != null)
      lCondizioni += " AND UDI.COD_UFFICIO_APPARTENENZA = '" + aModel.getCodUfficioAppartenenza() + "'";
    if (aModel.getDataUdienza() != null)
      lCondizioni += " AND UDI.DATA_UDIENZA >= TO_DATE(" + DateUtils.getDateToString( aModel.getDataUdienza(), "yyyyMMdd" ) + ",'YYYYMMDD' )";
     if (aModel.getDataUdienzaFine() != null)
      lCondizioni += " AND UDI.DATA_UDIENZA <= TO_DATE(" + DateUtils.getDateToString( aModel.getDataUdienzaFine(), "yyyyMMdd" ) + ",'YYYYMMDD' )";
    return lCondizioni;
  }

   /**
   *
   * @param aModel
   * @return
   */
  public String setCondizioneUDS(UdienzaModel aModel)
  {
    String lCondizioni = new String();

    // boolean lInserito = false;

    if (aModel.getNumCollegio() != null)
      lCondizioni += " AND UDI.NUM_COLLEGIO = " + aModel.getNumCollegio();
    if (aModel.getCodUfficioAppartenenza() != null)
      lCondizioni += " AND UDI.COD_UFFICIO_APPARTENENZA = '" + aModel.getCodUfficioAppartenenza() + "'";
    if (aModel.getDataUdienza() != null)
      lCondizioni += " AND UDI.DATA_UDIENZA >= TO_DATE(" + DateUtils.getDateToString( aModel.getDataUdienza(), "yyyyMMdd" ) + ",'YYYYMMDD' )";
    if (aModel.getDataUdienzaFine() != null)
      lCondizioni += " AND UDI.DATA_UDIENZA <= TO_DATE(" + DateUtils.getDateToString( aModel.getDataUdienzaFine(), "yyyyMMdd" ) + ",'YYYYMMDD' )";
    
    // Filtro aggiuntivo   
    if (aModel.getCodIdAssistente() != null && 
        aModel.getCodIdAssistente().toString().length() > 4)
      lCondizioni += " AND UDI.COD_ID_ASSISTENTE = " + aModel.getCodIdAssistente(); 
 
    if (aModel.getCodPresidente() != null && 
        aModel.getCodPresidente().length() > 1)
      lCondizioni += " AND UDI.COD_PRESIDENTE = '" + aModel.getCodPresidente() + "'";
 
    if (aModel.getCodPg() != null && 
        aModel.getCodPg().length() > 1)
      lCondizioni += " AND UDI.COD_PG = '" + aModel.getCodPg() + "'";
    
     
    return lCondizioni;
  }

  /**
   *
   * @param aModel
   * @return
   */
  public String setCondizioneNuoveUdienze(UdienzaModel aModel)
  {
    String lCondizioni = new String();

   // boolean lInserito = false;
    if (aModel.getDataUdienza() != null)
      lCondizioni += " AND TO_CHAR(UDI.DATA_UDIENZA,'yyyyMMdd') > "+ DateUtils.getDateToString( aModel.getDataUdienza(), "yyyyMMdd" ) ;
    if (aModel.getCodUfficioAppartenenza() != null)
      lCondizioni += " AND UDI.COD_UFFICIO_APPARTENENZA = '" + aModel.getCodUfficioAppartenenza() + "'";

    return lCondizioni;
  }

  /**
   * Imposta le condizioni per chiave.
   * <p>
   * @param aKey Parametro chiave ID
   * @return la stringa SQL di condizione.
   */
  public String setCondizioniByKey(BigDecimal aKey)
  {
    return " AND ID_UDIENZA = " + aKey;
  }

  /**
   * Metodo che ritorna il tipo di ordinamanto desiderato ossia
   * orderby per DATA-COLLEGIO ( Condizione di default ) oppure
   * COLLEGIO-DATA. Il Tipo di ordinamento viene impostato, utilizzando
   * il metodo <code> getMessage() </code> ereditato da GenericModel.
   * Quindi se il valore di getMessage non è valorizzato imposta la
   * condizione di ordinamanto di default (DATA-COLLEGIO)
   * <p>
   * Mappatura dei codici :
   * -> D-C ( Ordinamento per Data Udienza e Collegio ) default
   * -> C-D ( Ordinamento per Collegio e Data Udienza )
   * <p>
   * @param aModel UdienzaModel Istanza dell' oggetto UdienzaModel
   * @return String ritorna la stringa SQL ORDER BY desiderata.
   */
  protected String getTipoOrdinamento( UdienzaModel aModel )
  {
    String lSql = new String();

    // Condizione di default è l'orderby per DATA - COLLEGIO
    // Quindi al fine di ridurre l'impatto sulle altre funzionalità
    // che utilizzano tale condizione, si considera che, se il Message
    // non è valorizzato applica l'ordinamento di default.
    // Mappatura codici :
    // -> D-C ( Ordinamento per Data Udienza e Collegio ) default
    // -> C-D ( Ordinamento per Collegio e Data Udienza )
    if( aModel.getMessage() == null || aModel.getMessage().equalsIgnoreCase("D-C") )
      lSql += " ORDER BY UDI.DATA_UDIENZA, UDI.NUM_COLLEGIO"; // Default
    else if( aModel.getMessage().equalsIgnoreCase("C-D") )
      lSql += " ORDER BY UDI.NUM_COLLEGIO, UDI.DATA_UDIENZA";

    return lSql;
  }
  
  /**
   * Metodo che imposta lo statement, per recuperare la count
   * dei records legati al Magistrato aCodMagistrato
   * <p>
   * @param aCodMagistrato utilizzato per impostare le condizioni di filtro.
   */
  public void countMagUdiCodMagistrato( String aCodMagistrato )
  {
    String lStatement = "SELECT COUNT(*) AS COUNT FROM UDIENZA WHERE";
    lStatement += " COD_PRESIDENTE = '"+aCodMagistrato+"'";
    lStatement += " OR COD_GIUDICE_1 = '"+aCodMagistrato+"'";
    lStatement += " OR COD_GIUDICE_2 = '"+aCodMagistrato+"'";
    lStatement += " OR COD_PG = '"+aCodMagistrato+"'";
   
    setStatement( lStatement );
  }

  
  
  
  
  
  
  

}