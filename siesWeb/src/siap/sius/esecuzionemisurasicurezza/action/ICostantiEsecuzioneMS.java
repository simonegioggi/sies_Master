package siap.sius.esecuzionemisurasicurezza.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiEsecuzioneMS</p>
* <p>Description: Classe di costanti di Esecuzione Misure Sicurezza</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiEsecuzioneMS
{
  public static final String CAMPO_ID_ESECUZIONE_MS = "IdEsecuzioneMS";
  public static final String CAMPO_ANNO_S7 = "AnnoS7";
  public static final String CAMPO_PROGR_S7 = "ProgrS7";

  public static final String CAMPO_CHIAVE_ANNO = "ChiaveAnno";
  public static final String CAMPO_CHIAVE_PROGR = "ChiaveProgr";
  public static final String CAMPO_CHIAVE_ANNO_INIZIALE = "ChiaveAnnoDa";
  public static final String CAMPO_CHIAVE_PROGR_INIZIALE = "ChiaveProgrDa";
  public static final String CAMPO_CHIAVE_ANNO_FINALE = "ChiaveAnnoA";
  public static final String CAMPO_CHIAVE_PROGR_FINALE = "ChiaveProgrA";
  public static final String CAMPO_CHIAVE_UFFICIO = "ChiaveUfficio";
  public static final String CAMPO_ID_SOGGETTO = "IdSoggetto";
  public static final String CAMPO_ID_FASCICOLO_SIEP = "IdFascicoloSIEP";
  public static final String CAMPO_ID_FASCICOLO_SIUS = "IdFascicoloSius";

  public static final String CAMPO_GIORNO_INIZIO_MISURA = "GiornoInizioMisura";
  public static final String CAMPO_MESE_INIZIO_MISURA = "MeseInizioMisura";
  public static final String CAMPO_ANNO_INIZIO_MISURA = "AnnoInizioMisura";
  public static final String CAMPO_GIORNO_TERMINE_INIZIALE = "GiornoTermineIniziale";
  public static final String CAMPO_MESE_TERMINE_INIZIALE = "MeseTermineIniziale";
  public static final String CAMPO_ANNO_TERMINE_INIZIALE = "AnnoTermineIniziale";
  public static final String CAMPO_GIORNO_TERMINE_ATTUALE = "GiornoTermineAttuale";
  public static final String CAMPO_MESE_TERMINE_ATTUALE = "MeseTermineAttuale";
  public static final String CAMPO_ANNO_TERMINE_ATTUALE = "AnnoTermineAttuale";
  public static final String CAMPO_LUOGO_ESECUZIONE_MISURA = "LuogoEsecuzioneMisura";
  
  public static final String CAMPO_COD_TIPO_ESECUZIONE = "CodTipoEsecuzione";
  public static final String CAMPO_NUM_ANNI_ESECUZIONE = "NumAnniEsecuzione";
  public static final String CAMPO_NUM_MESI_ESECUZIONE = "NumMesiEsecuzione";
  public static final String CAMPO_NUM_GIORNI_ESECUZIONE = "NumGiorniEsecuzione";
  public static final String CAMPO_COD_TIPO_NUOVA_MISURA_ESECUZIONE   = "CodTipoNuovaMisuraeEsecuzione";
  public static final String CAMPO_NUM_ANNI_NUOVA_MISURA_ESECUZIONE   = "NumAnniNuovaMisuraEsecuzione";
  public static final String CAMPO_NUM_MESI_NUOVA_MISURA_ESECUZIONE   = "NumMesiNuovaMisuraEsecuzione";
  public static final String CAMPO_NUM_GIORNI_NUOVA_MISURA_ESECUZIONE = "NumGiorniNuovaMisuraEsecuzione";
  public static final String CAMPO_DESCR_TIPO_NUOVA_MISURA_ESECUZIONE = "DescrTipoNuovaMisuraeEsecuzione";

  public static final String CAMPO_GIORNO_DATA_TERMINE_MISURA = "GiornoDataTermineMisura";
  public static final String CAMPO_MESE_DATA_TERMINE_MISURA = "MeseDataTermineMisura";
  public static final String CAMPO_ANNO_DATA_TERMINE_MISURA = "AnnoDataTermineMisura";

  public static final String CAMPO_COD_NATURA_MISURA_RIDETERMINATA = "CodNaturaMisuraRideterminata";
  public static final String CAMPO_COD_TIPO_MISURA_RIDETERMINATA   = "CodTipoMisuraRideterminata";
  public static final String CAMPO_NUM_ANNI_MISURA_RIDETERMINATA   = "NumAnniMisuraRideterminata";
  public static final String CAMPO_NUM_MESI_MISURA_RIDETERMINATA   = "NumMesiMisuraRideterminata";
  public static final String CAMPO_NUM_GIORNI_MISURA_RIDETERMINATA = "NumGiorniMisuraRideterminata";
  
  public static final String CAMPO_COD_ESITO_TENORE= "CodEsitoTenore";

  public static final String CAMPO_COD_NATURA_MISURA_RIDETERMINATA_TWO = "CodNaturaMisuraRideterminataTwo";
  public static final String CAMPO_COD_TIPO_MISURA_RIDETERMINATA_TWO   = "CodTipoMisuraRideterminataTwo";
  public static final String CAMPO_NUM_ANNI_MISURA_RIDETERMINATA_TWO   = "NumAnniMisuraRideterminataTwo";
  public static final String CAMPO_NUM_MESI_MISURA_RIDETERMINATA_TWO   = "NumMesiMisuraRideterminataTwo";
  public static final String CAMPO_NUM_GIORNI_MISURA_RIDETERMINATA_TWO = "NumGiorniMisuraRideterminataTwo";

  public static final String PG_BUTTONS_EMS = IWebConstants.ROOT_DIR + "files/siap/sius/esecuzionemisurasicurezza/buttonsEMS.jsp";
  public static final String PG_LOAD_RICERCA_ESECUZIONE_MS = IWebConstants.ROOT_DIR + "files/siap/sius/esecuzionemisurasicurezza/LoadRicercaEsecuzioneMS.jsp";
  public static final String PG_RICERCA_ESECUZIONE_MS = IWebConstants.ROOT_DIR + "files/siap/sius/esecuzionemisurasicurezza/RicercaEsecuzioneMS.jsp";
  public static final String PG_DETTAGLIO_ESECUZIONE_MS = IWebConstants.ROOT_DIR + "files/siap/sius/esecuzionemisurasicurezza/DettaglioEsecuzioneMS.jsp";
  public static final String PG_ELENCO_ESECUZIONI_MS = IWebConstants.ROOT_DIR + "files/siap/sius/esecuzionemisurasicurezza/ElencoEsecuzioniMS.jsp";
  public static final String PG_LOAD_MODIFICA_ESECUZIONE_MS = IWebConstants.ROOT_DIR + "files/siap/sius/esecuzionemisurasicurezza/LoadModificaEsecuzioneMS.jsp";
  public static final String PG_ELENCO_ESEC_MS_RIDETERMINATE = IWebConstants.ROOT_DIR + "files/siap/sius/esecuzionemisurasicurezza/ElencoEsecuzioniMSRideterminate.jsp";
}
