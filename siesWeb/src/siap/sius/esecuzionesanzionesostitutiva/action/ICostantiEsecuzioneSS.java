package siap.sius.esecuzionesanzionesostitutiva.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiEsecuzioneSS</p>
* <p>Description: Classe di costanti di Esecuzione Sanzioni Sostitutive</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiEsecuzioneSS
{
  public static final String CAMPO_ID_ESECUZIONE_SS = "IdEsecuzioneSS";
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

  public static final String CAMPO_GIORNO_INIZIO_SANZIONE = "GiornoInizioSanzione";
  public static final String CAMPO_MESE_INIZIO_SANZIONE = "MeseInizioSanzione";
  public static final String CAMPO_ANNO_INIZIO_SANZIONE = "AnnoInizioSanzione";
  public static final String CAMPO_GIORNO_TERMINE_INIZIALE = "GiornoTermineIniziale";
  public static final String CAMPO_MESE_TERMINE_INIZIALE = "MeseTermineIniziale";
  public static final String CAMPO_ANNO_TERMINE_INIZIALE = "AnnoTermineIniziale";
  public static final String CAMPO_GIORNO_TERMINE_ATTUALE = "GiornoTermineAttuale";
  public static final String CAMPO_MESE_TERMINE_ATTUALE = "MeseTermineAttuale";
  public static final String CAMPO_ANNO_TERMINE_ATTUALE = "AnnoTermineAttuale";
  public static final String CAMPO_LUOGO_ESECUZIONE_SANZIONE = "LuogoEsecuzioneSanzione";

  public static final String PG_BUTTONS_ESS = IWebConstants.ROOT_DIR + "files/siap/sius/esecuzionesanzionesostitutiva/buttonsESS.jsp";
  public static final String PG_LOAD_RICERCA_ESECUZIONE_SS = IWebConstants.ROOT_DIR + "files/siap/sius/esecuzionesanzionesostitutiva/LoadRicercaEsecuzioneSS.jsp";
  public static final String PG_RICERCA_ESECUZIONE_SS = IWebConstants.ROOT_DIR + "files/siap/sius/esecuzionesanzionesostitutiva/RicercaEsecuzioneSS.jsp";
  public static final String PG_DETTAGLIO_ESECUZIONE_SS = IWebConstants.ROOT_DIR + "files/siap/sius/esecuzionesanzionesostitutiva/DettaglioEsecuzioneSS.jsp";
  public static final String PG_LOAD_MODIFICA_ESECUZIONE_SS = IWebConstants.ROOT_DIR + "files/siap/sius/esecuzionesanzionesostitutiva/LoadModificaEsecuzioneSS.jsp";
}
