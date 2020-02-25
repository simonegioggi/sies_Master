package siap.sico.security.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPSqlDAO;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import f3b.dao.DAOException;
import f3b.security.model.FunctionModel;
import f3b.security.model.ProfileModel;
import f3b.util.DateUtils;

public class SecuritySqlDAO extends SIAPSqlDAO
{
  public SecuritySqlDAO(Connection aCon)
  {
    super(aCon);
  }

  public void ricercaProfiloByCodiceUtente(String aCodUtente)
    throws DAOException
  {
    String lSysDateStr = DateUtils.getSysDate("dd/MM/yyyy");

    String lStatement = "";

    lStatement += "SELECT PRO.COD_PROFILO, PRO.DESCRIZIONE,";
    lStatement +=       " PRO.DATA_FINE_VALIDITA DT_FIN_PRO, UTE_PRO.DATA_FINE_VALIDITA DT_FIN_UTE_PRO";
    lStatement +=  " FROM PROFILO PRO, UTENTE_PROFILO UTE_PRO";
    lStatement +=  " WHERE (PRO.COD_PROFILO = UTE_PRO.PRF_COD_PROFILO)";
    lStatement +=    " AND (UTE_PRO.UTE_COD_UTENTE = '"+aCodUtente+"')";
    lStatement +=    " AND (UTE_PRO.DATA_INIZIO_VALIDITA <= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY'))";
    lStatement +=    " AND (PRO.DATA_FINE_VALIDITA is null OR PRO.DATA_FINE_VALIDITA >= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY'))";
    lStatement +=    " AND (UTE_PRO.DATA_FINE_VALIDITA is null OR UTE_PRO.DATA_FINE_VALIDITA >= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY'))";

    setStatement( lStatement );
  }

  public void ricercaFunzioneByAzioneCodiceProfilo(String aNomeAzione, BigDecimal aCodProfilo)
    throws DAOException
  {
    String lSysDateStr = DateUtils.getSysDate("dd/MM/yyyy");

    String lStatement = "";

    // seleziona la funzione
    lStatement += "SELECT FUN.ID_FUNZIONE, FUN.DESCRIZIONE,";
    lStatement +=       " FUN.COD_TIPO_FUNZIONE, FUN.AZIONE_CONTESTO_JAVA";
    lStatement +=  " FROM FUNZIONE FUN, FUNZIONE_PROFILO FUN_PRO";
    lStatement +=  " WHERE ((FUN.ID_FUNZIONE = FUN_PRO.FUN_ID_FUNZIONE)";
    // corrispondente all'azione
    lStatement +=    " AND (FUN.AZIONE_CONTESTO_JAVA = '"+aNomeAzione+"')";
    // associata al profilo
    lStatement +=    " AND (FUN_PRO.PRF_COD_PROFILO = "+aCodProfilo+")";
    lStatement +=    " AND (FUN_PRO.DATA_INIZIO_VALIDITA <= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY'))";
    lStatement +=    " AND (FUN_PRO.DATA_FINE_VALIDITA is null OR FUN_PRO.DATA_FINE_VALIDITA >= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY')) )";

    setStatement( lStatement );
  }

  public void ricercaFunzioniFiglieByCodiceProfilo(BigDecimal aCodProfilo, BigDecimal aIdFunzionePadre)
    throws DAOException
  {
    String lSysDateStr = DateUtils.getSysDate("dd/MM/yyyy");

    String lStatement = "";

    // seleziona le funzioni
    lStatement += "SELECT FUN.ID_FUNZIONE, FUN.DESCRIZIONE,";
    lStatement +=       " FUN.COD_TIPO_FUNZIONE, FUN.AZIONE_CONTESTO_JAVA,";
    lStatement +=       " REL_FUN.LABEL_FUNZIONE, REL_FUN.ORDINE_VISUALIZZAZIONE,";
    lStatement +=       " REL_FUN.COD_TIPO_VISUALIZZAZIONE, ";
    lStatement +=       " REL_FUN.IMMAGINE";
    lStatement +=  " FROM FUNZIONE FUN, RELAZIONE_FUNZIONE REL_FUN, FUNZIONE_PROFILO FUN_PRO";
    lStatement +=  " WHERE ((FUN.ID_FUNZIONE = FUN_PRO.FUN_ID_FUNZIONE)";
    // associate al profilo
    lStatement +=    " AND (FUN_PRO.PRF_COD_PROFILO = "+aCodProfilo+")";
    lStatement +=    " AND (FUN_PRO.DATA_INIZIO_VALIDITA <= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY'))";
    lStatement +=    " AND (FUN_PRO.DATA_FINE_VALIDITA is null OR FUN_PRO.DATA_FINE_VALIDITA >= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY'))";
    // e figlie della funzione padre
    lStatement +=    " AND (REL_FUN.FUN_ID_FUNZIONE = "+aIdFunzionePadre+")";
    lStatement +=    " AND (FUN.ID_FUNZIONE = REL_FUN.FUN_ID_FUNZIONE_FIGLIA))";
    lStatement +=    " ORDER BY REL_FUN.ORDINE_VISUALIZZAZIONE";

    setStatement( lStatement );
  }

  public void ricercaFunzioniByCodProfiloTipoVisual( BigDecimal aCodProfilo,
                                                     BigDecimal aIdFunzionePadre,
                                                     String aTipoVisual)
    throws DAOException
  {
    String lSysDateStr = DateUtils.getSysDate("dd/MM/yyyy");

    String lStatement = "";

    // seleziona le funzioni
    lStatement += "SELECT FUN.ID_FUNZIONE, FUN.DESCRIZIONE,";
    lStatement +=       " FUN.COD_TIPO_FUNZIONE, FUN.AZIONE_CONTESTO_JAVA,";
    lStatement +=       " REL_FUN.LABEL_FUNZIONE, REL_FUN.ORDINE_VISUALIZZAZIONE,";
    lStatement +=       " REL_FUN.COD_TIPO_VISUALIZZAZIONE, ";
    lStatement +=       " REL_FUN.IMMAGINE";
    lStatement +=  " FROM FUNZIONE FUN, RELAZIONE_FUNZIONE REL_FUN, FUNZIONE_PROFILO FUN_PRO";
    lStatement +=  " WHERE ((FUN.ID_FUNZIONE = FUN_PRO.FUN_ID_FUNZIONE)";
    // associate al profilo
    lStatement +=    " AND (FUN_PRO.PRF_COD_PROFILO = "+aCodProfilo+")";
    lStatement +=    " AND (FUN_PRO.DATA_INIZIO_VALIDITA <= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY'))";
    lStatement +=    " AND (FUN_PRO.DATA_FINE_VALIDITA is null OR FUN_PRO.DATA_FINE_VALIDITA >= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY'))";
    // figlie della funzione padre
    lStatement +=    " AND (REL_FUN.FUN_ID_FUNZIONE = "+aIdFunzionePadre+")";
    // relative ad un tipo di visualizzazione
    lStatement +=    " AND (REL_FUN.COD_TIPO_VISUALIZZAZIONE = '"+aTipoVisual+"')";
    lStatement +=    " AND (FUN.ID_FUNZIONE = REL_FUN.FUN_ID_FUNZIONE_FIGLIA))";
    lStatement +=    " ORDER BY REL_FUN.ORDINE_VISUALIZZAZIONE";

    setStatement( lStatement );
  }

/*
  public void ricercaUfficioByCodiceUtente(String aCodUtente)
    throws DAOException
  {
    String lSysDateStr = DateUtils.getSysDate("dd/MM/yyyy");

    String lStatement = "";

    lStatement += "SELECT UFF.COD_UFFICIO, UFF.DESCRIZIONE DESC_UFFICIO, UFF.COD_DISTRETTO,";
    lStatement +=  " UFF.COD_PROVINCIA, UFF.COD_COMUNE, UFF.UFF_COD_UFFICIO, COM.DESCRIZIONE DESC_COMUNE";
    lStatement +=  " FROM UFFICIO UFF, UTENTE_UFFICIO UTE_UFF, COMUNE COM";
    lStatement += " WHERE ((UTE_UFF.UFF_COD_UFFICIO = UFF.COD_UFFICIO)";
    lStatement +=   " AND (UTE_UFF.UTE_COD_UTENTE = '"+aCodUtente+"')";
    lStatement +=   " AND (UTE_UFF.DATA_INIZIO_VALIDITA <= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY'))";
    lStatement +=   " AND (UTE_UFF.DATA_FINE_VALIDITA is null OR UTE_UFF.DATA_FINE_VALIDITA >= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY')))";
    lStatement +=   " AND COM.COD_COMUNE = UFF.COD_COMUNE";

    setStatement( lStatement );
  }
*/

  public void ricercaUtenteValidoByCodiceUtenteUfficio( String aCodUtente,
                                                        String aCodTipoUfficio,
                                                        String aCodComune )
    throws DAOException
  {
    String lSysDateStr = DateUtils.getSysDate("dd/MM/yyyy");

    String lStatement = "";

    // seleziona l'utente
    lStatement += "SELECT UTE.NOME, UTE.TELEFONO UT_TEL, UTE.FAX UT_FAX, UTE.E_MAIL UT_E_MAIL, DATA_ORA_CONNESSIONE, UTE.PWD, UTE.DATA_INSERIMENTO, UTE.COGNOME,";
    lStatement +=       " UTE.COD_OPERATORE_AGGIORNAMENTO, UTE.COD_OPERATORE_INSERIMENTO, UTE.COD_UTENTE,";
    lStatement +=       " UTE.DATA_AGGIORNAMENTO, UTE.DATA_FINE_VALIDITA, UTE.DATA_ULTIMA_MODIFICA_PWD,UTE.IP, ";
    lStatement +=       " UTE.USERID_NSC, UTE.PWD_NSC ";
    lStatement +=  " FROM UTENTE UTE, UFFICIO UFF, UTENTE_UFFICIO UTE_UFF";
    lStatement +=  " WHERE (UTE.COD_UTENTE = UTE_UFF.UTE_COD_UTENTE)";
    lStatement +=    " AND (UTE_UFF.UFF_COD_UFFICIO = UFF.COD_UFFICIO)";
    // associato al codice utente
    lStatement +=    " AND (UTE_UFF.UTE_COD_UTENTE = '"+aCodUtente+"')";
    // valido rispetto alla data di validità
    lStatement +=    " AND ((UTE_UFF.DATA_INIZIO_VALIDITA <= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY'))";
    lStatement +=      " AND (UTE_UFF.DATA_FINE_VALIDITA is null OR UTE_UFF.DATA_FINE_VALIDITA >= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY')))";
    lStatement +=    " AND (UTE.DATA_FINE_VALIDITA is null OR UTE.DATA_FINE_VALIDITA >= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY'))";
    // dell'ufficio
    lStatement +=    " AND (UFF.COD_TIPO_UFFICIO = '"+aCodTipoUfficio+"')";
    // del comune
    lStatement +=    " AND (UFF.COD_COMUNE = '"+aCodComune+"')";

    setStatement( lStatement );
  }

 public void ricercaUtenteValidoByCodiceUtente( String aCodUtente)
    throws DAOException
  {
    String lSysDateStr = DateUtils.getSysDate("dd/MM/yyyy");

    String lStatement = "";

    // seleziona l'utente
    lStatement += "SELECT UTE.NOME, UTE.TELEFONO UT_TEL, UTE.FAX UT_FAX, UTE.E_MAIL UT_E_MAIL, DATA_ORA_CONNESSIONE, UTE.PWD, UTE.DATA_INSERIMENTO, UTE.COGNOME,";
    lStatement +=       " UTE.COD_OPERATORE_AGGIORNAMENTO, UTE.COD_OPERATORE_INSERIMENTO, UTE.COD_UTENTE,";
    lStatement +=       " UTE.DATA_AGGIORNAMENTO, UTE.DATA_FINE_VALIDITA, UTE.DATA_ULTIMA_MODIFICA_PWD,UTE.IP, ";
    lStatement +=       " UTE.USERID_NSC, UTE.PWD_NSC ";
    lStatement +=  " FROM UTENTE UTE, UFFICIO UFF, UTENTE_UFFICIO UTE_UFF";
    lStatement +=  " WHERE (UTE.COD_UTENTE = UTE_UFF.UTE_COD_UTENTE)";
    lStatement +=    " AND (UTE_UFF.UFF_COD_UFFICIO = UFF.COD_UFFICIO)";
    // associato al codice utente
    lStatement +=    " AND (UTE_UFF.UTE_COD_UTENTE = '"+aCodUtente+"')";
    // valido rispetto alla data di validità
    lStatement +=    " AND ((UTE_UFF.DATA_INIZIO_VALIDITA <= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY'))";
    lStatement +=      " AND (UTE_UFF.DATA_FINE_VALIDITA is null OR UTE_UFF.DATA_FINE_VALIDITA >= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY')))";
    lStatement +=    " AND (UTE.DATA_FINE_VALIDITA is null OR UTE.DATA_FINE_VALIDITA >= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY'))";

    setStatement( lStatement );
  }


  public void ricercaUfficioByCodiceUtente(String aCodUtente)
    throws DAOException
  {
    String lSysDateStr = DateUtils.getSysDate("dd/MM/yyyy");

    String lStatement = "";

    // seleziona l'ufficio
    lStatement += "SELECT UFF.COD_UFFICIO, UFF.COD_TIPO_UFFICIO, UFF.COD_DISTRETTO,";
    lStatement +=       " UFF.COD_PROVINCIA, UFF.COD_COMUNE,";
    lStatement +=       " UFF.DATA_CARICAMENTO_REGE, UFF.COD_UFFICIO_COMPETENTE,";
    lStatement +=       " UFF.INDIRIZZO, UFF.CAP CAP_UFFICIO, UFF.TELEFONO, UFF.FAX, UFF.E_MAIL,";
    lStatement +=       " COM.DESCRIZIONE DESCR_COMUNE,";
    lStatement +=       " DESCR_PROVINCIA.RV_MEANING DESCR_PROVINCIA,";
    lStatement +=       " DESCR_TIPO_UFFICIO.RV_MEANING DESCR_TIPO_UFFICIO";
    lStatement +=  " FROM UFFICIO UFF, UTENTE_UFFICIO UTE_UFF, COMUNE COM,";
    lStatement +=       " CG_REF_CODES DESCR_PROVINCIA, CG_REF_CODES DESCR_TIPO_UFFICIO";
    lStatement +=  " WHERE (UTE_UFF.UFF_COD_UFFICIO = UFF.COD_UFFICIO)";
    // associata all'utente
    lStatement +=    " AND (UTE_UFF.UTE_COD_UTENTE = '"+aCodUtente+"')";
    // valido rispetto alla data di validità
    lStatement +=    " AND ((UTE_UFF.DATA_INIZIO_VALIDITA <= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY'))";
    lStatement +=          " AND (UTE_UFF.DATA_FINE_VALIDITA is null OR UTE_UFF.DATA_FINE_VALIDITA >= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY')))";
    lStatement +=  " AND (COM.COD_COMUNE = UFF.COD_COMUNE)";
    lStatement +=  " AND (DESCR_TIPO_UFFICIO.RV_DOMAIN = 'TIPO_UFFICIO')";
    lStatement +=  " AND (DESCR_TIPO_UFFICIO.RV_LOW_VALUE = UFF.COD_TIPO_UFFICIO)";
    lStatement +=  " AND (DESCR_PROVINCIA.RV_DOMAIN = 'PROVINCIA')";
    lStatement +=  " AND (DESCR_PROVINCIA.RV_LOW_VALUE = UFF.COD_PROVINCIA)";

    setStatement( lStatement );
  }

  public ProfileModel getProfiloModel ()
    throws DAOException
  {
    Date lDataFineProfilo = getDate("DT_FIN_PRO");
    Date lDataFineUtenteProfilo = getDate("DT_FIN_UTE_PRO");

    Date lMinTraDate = null;
    if (lDataFineProfilo != null && lDataFineUtenteProfilo != null)
      lMinTraDate = DateUtils.getMinDate(lDataFineProfilo, lDataFineUtenteProfilo);
    else if (lDataFineProfilo != null)
      lMinTraDate = lDataFineProfilo;
    else if(lDataFineUtenteProfilo != null)
      lMinTraDate = lDataFineUtenteProfilo;

    ProfileModel aModel = new ProfileModel( getBigDecimal("COD_PROFILO"),
                                            getString("DESCRIZIONE"),
                                            lMinTraDate
                                           );

    return aModel;
  }

  public FunctionModel getFunzioneModel ()
    throws DAOException
  {
    FunctionModel aModel = new FunctionModel( getBigDecimal("ID_FUNZIONE"),
                                              getString("DESCRIZIONE"),
                                              getString("AZIONE_CONTESTO_JAVA"),
                                              getString("LABEL_FUNZIONE"),
                                              getString("COD_TIPO_FUNZIONE"),
                                              getBigDecimal("ORDINE_VISUALIZZAZIONE"),
                                              getString("COD_TIPO_VISUALIZZAZIONE"),
                                              getString("IMMAGINE")
                                            );

    return aModel;
  }

  public UtenteModel getUtenteModel ()
    throws DAOException
  {
    UtenteModel aModel = new UtenteModel( getString("COD_UTENTE"),
                                          getString("PWD"),
                                          getString("COGNOME"),
                                          getString("NOME"),
                                          getString("UT_TEL"),
                                          getString("UT_FAX"),
                                          getString("UT_E_MAIL"),
                                          getDate("DATA_FINE_VALIDITA"),
                                          getDate("DATA_ORA_CONNESSIONE"),
                                          getString("COD_OPERATORE_INSERIMENTO"),
                                          getDate("DATA_INSERIMENTO"),
                                          getString("COD_OPERATORE_AGGIORNAMENTO"),
                                          getDate("DATA_AGGIORNAMENTO"),
                                          getDate("DATA_ULTIMA_MODIFICA_PWD"),
                                          getString("IP"),
                                          getString("USERID_NSC"),
                                          getString("PWD_NSC") );

    return aModel;
  }

  public UfficioModel getUfficioModel()
    throws DAOException
  {
    UfficioModel aModel = new UfficioModel( getString("COD_UFFICIO"),
                                            getString("COD_TIPO_UFFICIO"),
                                            getString("DESCR_TIPO_UFFICIO"),
                                            getString("COD_DISTRETTO"),
                                            getString("COD_PROVINCIA"),
                                            getString("DESCR_PROVINCIA"),
                                            getString("COD_COMUNE"),
                                            getString("DESCR_COMUNE"),
                                            getDate("DATA_CARICAMENTO_REGE"),
                                            getString("COD_UFFICIO_COMPETENTE"),
                                            getString("INDIRIZZO"),
                                            getString("CAP_UFFICIO"),
                                            getString("TELEFONO"),
                                            getString("FAX"),
                                            getString("E_MAIL"),
                                            null);

    return aModel;
  }

  public BigDecimal getIdFunzione()
    throws DAOException
  {
    return getBigDecimal("ID_FUNZIONE");
  }

  public String getDescrFunzione()
    throws DAOException
  {
    return getString("DESCRIZIONE");
  }

  public String getCodTipoFunzione()
    throws DAOException
  {
    return getString("COD_TIPO_FUNZIONE");
  }

  public String getMenuFunzione()
    throws DAOException
  {
    return getString("MENU_FUNZIONE");
  }

  public String getAzioneContestoJava()
    throws DAOException
  {
    return getString("AZIONE_CONTESTO_JAVA");
  }


   /*---GDV per l'oscuramento di alcune funzioni,
   Si sta pensando ad una soluzionme più efficace...
public void ricercaFunzioniFiglieByCodiceProfiloConOscuramento(BigDecimal aCodProfilo,
BigDecimal aIdFunzionePadre, int aFirst, int aLast)
    throws DAOException
  {
    String lSysDateStr = DateUtils.getSysDate("dd/MM/yyyy");

    String lStatement = "";

    // seleziona le funzioni
    lStatement += "SELECT FUN.ID_FUNZIONE, FUN.DESCRIZIONE,";
    lStatement +=       " FUN.COD_TIPO_FUNZIONE, FUN.AZIONE_CONTESTO_JAVA,";
    lStatement +=       " REL_FUN.LABEL_FUNZIONE, REL_FUN.ORDINE_VISUALIZZAZIONE,";
    lStatement +=       " REL_FUN.COD_TIPO_VISUALIZZAZIONE";
    lStatement +=  " FROM FUNZIONE FUN, RELAZIONE_FUNZIONE REL_FUN, FUNZIONE_PROFILO FUN_PRO";
    lStatement +=  " WHERE ((FUN.ID_FUNZIONE = FUN_PRO.FUN_ID_FUNZIONE)";
    // associate al profilo
    lStatement +=    " AND (FUN_PRO.PRF_COD_PROFILO = "+aCodProfilo+")";
    lStatement +=    " AND (FUN_PRO.DATA_INIZIO_VALIDITA <= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY'))";
    lStatement +=    " AND (FUN_PRO.DATA_FINE_VALIDITA is null OR FUN_PRO.DATA_FINE_VALIDITA >= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY'))";
    // e figlie della funzione padre
    lStatement +=    " AND (REL_FUN.FUN_ID_FUNZIONE = "+aIdFunzionePadre+")";
    lStatement +=    " AND ((FUN.ID_FUNZIONE <= "+ aFirst + ")";
    lStatement +=    " or (FUN.ID_FUNZIONE >= "+ aLast + "))";
    lStatement +=    " AND (FUN.ID_FUNZIONE = REL_FUN.FUN_ID_FUNZIONE_FIGLIA))";
    lStatement +=    " ORDER BY REL_FUN.ORDINE_VISUALIZZAZIONE";

    setStatement( lStatement );
  }

public void ricercaFunzioniByCodProfiloTipoVisualConOscuramento( BigDecimal aCodProfilo,
                                                     BigDecimal aIdFunzionePadre,
                                                     String aTipoVisual,
                                                     int aFirst,
                                                     int aLast)
    throws DAOException
  {
    String lSysDateStr = DateUtils.getSysDate("dd/MM/yyyy");

    String lStatement = "";

    // seleziona le funzioni
    lStatement += "SELECT FUN.ID_FUNZIONE, FUN.DESCRIZIONE,";
    lStatement +=       " FUN.COD_TIPO_FUNZIONE, FUN.AZIONE_CONTESTO_JAVA,";
    lStatement +=       " REL_FUN.LABEL_FUNZIONE, REL_FUN.ORDINE_VISUALIZZAZIONE,";
    lStatement +=       " REL_FUN.COD_TIPO_VISUALIZZAZIONE";
    lStatement +=  " FROM FUNZIONE FUN, RELAZIONE_FUNZIONE REL_FUN, FUNZIONE_PROFILO FUN_PRO";
    lStatement +=  " WHERE ((FUN.ID_FUNZIONE = FUN_PRO.FUN_ID_FUNZIONE)";
    // associate al profilo
    lStatement +=    " AND (FUN_PRO.PRF_COD_PROFILO = "+aCodProfilo+")";
    lStatement +=    " AND (FUN_PRO.DATA_INIZIO_VALIDITA <= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY'))";
    lStatement +=    " AND (FUN_PRO.DATA_FINE_VALIDITA is null OR FUN_PRO.DATA_FINE_VALIDITA >= TO_DATE('"+lSysDateStr+"','DD/MM/YYYY'))";
    // figlie della funzione padre
    lStatement +=    " AND (REL_FUN.FUN_ID_FUNZIONE = "+aIdFunzionePadre+")";
    lStatement +=    " AND ((FUN.ID_FUNZIONE <= "+ aFirst + ")";
    lStatement +=    " or (FUN.ID_FUNZIONE >= "+ aLast + "))";
    // relative ad un tipo di visualizzazione
    lStatement +=    " AND (REL_FUN.COD_TIPO_VISUALIZZAZIONE = '"+aTipoVisual+"')";
    lStatement +=    " AND (FUN.ID_FUNZIONE = REL_FUN.FUN_ID_FUNZIONE_FIGLIA))";
    lStatement +=    " ORDER BY REL_FUN.ORDINE_VISUALIZZAZIONE";

    setStatement( lStatement );
  }
*/
}
