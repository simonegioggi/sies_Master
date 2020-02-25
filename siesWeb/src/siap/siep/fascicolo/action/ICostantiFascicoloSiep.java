package siap.siep.fascicolo.action;

import siap.siep.web.ISIEPCostantiWeb;
import f3b.web.IWebConstants;

public interface ICostantiFascicoloSiep extends ISIEPCostantiWeb
{
  public static final String CAMPO_ID_FASCICOLO_SIEP = "ChiaveFascicolo";
  public static final String CAMPO_CHIAVE_ANNO = "ChiaveSiepAnno";
  public static final String CAMPO_CHIAVE_UFFICIO = "ChiaveSiepUfficio";
  public static final String CAMPO_CHIAVE_ACCORPATO = "ChiaveSiepAccorpato";
  public static final String CAMPO_CHIAVE_PROGR = "ChiaveSiepProgressivo";
  public static final String CAMPO_CHIAVE_PROGR_ORIGIN = "ChiaveSiepProgressivoOrigin";
  public static final String CAMPO_COD_STATO_FASCICOLO = "CodiceStatoFascicoloOB";

  /* inizio modifica marzo 2010 */
  public static final String CAMPO_GIORNO_ARRIVO_ATTO = "GiornoArrivoAtto";
  public static final String CAMPO_MESE_ARRIVO_ATTO = "MeseArrivoAtto";
  public static final String CAMPO_ANNO_ARRIVO_ATTO = "AnnoArrivoAtto";
  /* fine modifica marzo 2010 */

  public static final String CAMPO_DATA_ARRIVO_SENTENZA = "DataArrivoSentenza";

  public static final String CAMPO_GIORNO_ARRIVO_SENTENZA = "GiornoArrivoSentenza";
  public static final String CAMPO_MESE_ARRIVO_SENTENZA = "MeseArrivoSentenza";
  public static final String CAMPO_ANNO_ARRIVO_SENTENZA = "AnnoArrivoSentenza";

  public static final String CAMPO_DATA_ISCRIZIONE_ATTI = "DataIscrizione";

  public static final String CAMPO_GIORNO_ISCRIZIONE_ATTI = "GiornoIscrizioneAtti";
  public static final String CAMPO_MESE_ISCRIZIONE_ATTI = "MeseIscrizioneAtti";
  public static final String CAMPO_ANNO_ISCRIZIONE_ATTI = "AnnoIscrizioneAtti";

  public static final String CAMPO_DATA_ARCHIVIAZIONE = "DataArchiviazione";

  public static final String CAMPO_GIORNO_ARCHIVIAZIONE = "GiornoArchiviazione";
  public static final String CAMPO_MESE_ARCHIVIAZIONE = "MeseArchiviazione";
  public static final String CAMPO_ANNO_ARCHIVIAZIONE = "AnnoArchiviazione";

  public static final String CAMPO_GIORNO_ISCRIZIONE = "GiornoIscrizione";
  public static final String CAMPO_MESE_ISCRIZIONE = "MeseIscrizione";
  public static final String CAMPO_ANNO_ISCRIZIONE = "AnnoIscrizione";

  public static final String CAMPO_GIORNO_ISCRIZIONE_INIZIALE = "GiornoIscrizioneIniziale";
  public static final String CAMPO_MESE_ISCRIZIONE_INIZIALE = "MeseIscrizioneIniziale";
  public static final String CAMPO_ANNO_ISCRIZIONE_INIZIALE = "AnnoIscrizioneIniziale";

  public static final String CAMPO_GIORNO_ISCRIZIONE_FINALE = "GiornoIscrizioneFinale";
  public static final String CAMPO_MESE_ISCRIZIONE_FINALE = "MeseIscrizioneFinale";
  public static final String CAMPO_ANNO_ISCRIZIONE_FINALE = "AnnoIscrizioneFinale";

  public static final String CAMPO_LETTERA_FASCICOLO = "LetteraFascicolo";
  public static final String CAMPO_ANNO_FASCICOLO_UNIONE = "AnnoFascicoloUnione";
  public static final String CAMPO_NUM_FASCICOLO_UNIONE = "NumeroFascicoloUnione";

  public static final String CAMPO_DATA_UNIONE = "DataUnioneFascicolo";

  public static final String CAMPO_GIORNO_UNIONE = "GiornoUnioneFascicolo";
  public static final String CAMPO_MESE_UNIONE = "MeseUnioneFascicolo";
  public static final String CAMPO_ANNO_UNIONE = "AnnoUnioneFascicolo";

  public static final String CAMPO_NOTE = "NoteFascicolo";
  public static final String CAMPO_TESTO_AGGIUNTIVO = "TestoAggiuntivoFascicolo";

  public static final String CAMPO_FLAG_VALIDATO = "FlagValidato";
  public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodiceOperatoreInserimento";
  public static final String CAMPO_DATA_INSERIMENTO = "DataInserimento";
  public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodiceOperatoreAggiornamento";
  public static final String CAMPO_DATA_AGGIORNAMENTO = "DataAggiornamento";
  public static final String CAMPO_SOG_ID_SOGGETTO = "SogIdSoggetto";
  public static final String CAMPO_SEN_ID_SENTENZA = "SenIdSentenza";
  public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieChiaveFascicolo";

  public static final String CAMPO_CHIAVE_PROGR_INIZIALE = "CampoChiaveProgrIniziale";
  public static final String CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE= "CampoChiaveProgrOriginIniziale";
  public static final String CAMPO_CHIAVE_ANNO_INIZIALE = "CampoChiaveAnnoIniziale";

  public static final String CAMPO_CHIAVE_PROGR_FINALE = "CampoChiaveProgrFinale";
  public static final String CAMPO_CHIAVE_PROGR_ORIGIN_FINALE = "CampoChiaveProgrOriginFinale";
  public static final String CAMPO_CHIAVE_ANNO_FINALE = "CampoChiaveAnnoFinale";

  public static final String CAMPO_DESCR_COMUNE_UFFICIO = "CampoDescrComuneUfficio";
  public static final String CAMPO_GIORNO_DATA_IRREVOCABILITA= "DataIrrevocabilitaGiorni";
  public static final String CAMPO_MESE_DATA_IRREVOCABILITA = "DataIrrevocabilitaMesi";
  public static final String CAMPO_ANNO_DATA_IRREVOCABILITA = "DataIrrevocabilitaAnni";

  public static final String CAMPO_COD_TIPO_POS_LIBERO = "CampoTipoPosLibero";

  public static final String CAMPO_FLAG_ALTRA_CAUSA = "FlagAltraCausa";
  public static final String CAMPO_FLAG_CUMULO = "FlagCumulo";
  public static final String CAMPO_FLAG_CUMULANTE = "FlagCumulante";
  public static final String CAMPO_COD_UFFICIO_UNIONE = "CodUfficioUnione";
  public static final String CAMPO_SEDE_UFFICIO_UNIONE = "SedeUfficioUnione";
  public static final String CAMPO_KEY_PROVV_NSC = "KeyProvvNSC";

  public static final String CAMPO_TIPO_INT_SOSP = "TipoIntSosp";
  public static final String CAMPO_MOTIVO_INT_SOSP = "MotivoIntSosp";  
  
  public static final String CAMPO_COD_UFFICIO_UNIONE_STESSA = "CodUfficioUnioneStessa";
  public static final String CAMPO_SEDE_UFFICIO_UNIONE_STESSA = "SedeUfficioUnioneStessa";

  public static final String CAMPO_AZIONE_CHIAMANTE = "AzioneChiamante";
  public static final String FASCICOLO_RICERCATO = "FascicoloRicercato";

  public static final String REDIRECT_FASCICOLO_RICERCATO = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
    "=siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&Cumulo=0&" +
    ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=";

  public static final String REDIRECT_FASCICOLO_CUMULANTE = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
    "=siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&Cumulo=1&" +
    ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=";
  
  public static final String PG_LOAD_RICERCAFASCICOLO_SIEP = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/LoadRicercaFascicoloSiep.jsp";
  public static final String PG_LOAD_RICERCA_PER_VALIDAZIONE = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/LoadRicercaFascicoloPerValidazione.jsp";
  public static final String PG_LOAD_RICERCA_PER_SVALIDAZIONE = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/LoadRicercaFascicoloPerAnnullamentoValidazione.jsp";
  public static final String PG_RICERCAFASCICOLO_SIEP = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/RicercaFascicoloSiep.jsp";
  public static final String PG_LOAD_INSERISCIFASCICOLO_SIEP = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/LoadInserisciFascicoloSiep.jsp";
  public static final String PG_DETTAGLIO_FASCICOLO_SIEP = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/DettaglioFascicoloSiep.jsp";
  public static final String PG_DETTAGLIO_SOGGETTO_SENTENZA = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp";
  public static final String PG_LOAD_RICERCA_UNIVOCO = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/LoadRicercaFascicoloUnivoco.jsp";
  public static final String PG_LOAD_RICERCA_PRESAINCARICO_COMPETENZA = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/LoadRicercaFascicoloPresaincaricoCompetenza.jsp";
  
  public static final String PG_RICERCA_FASCICOLI_SIEP_SOSPESI_INTERROTTI = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/RicercaSospesiInterrotti.jsp";
  
  public static final String PG_GRIGLIA_RICERCHE_SIEP = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/GrigliaRicerche.jsp";

  public static final String PG_LOAD_RICERCAFASCICOLO_SIEP_PER_SOGGETTO = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/LoadRicercaFascicoloSiepPerSoggetto.jsp";
  public static final String PG_LOAD_RICERCAFASCICOLOSOGGETTI_SIEP_PER_SOGGETTO = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/RicercaSoggettiConProcedimentiSiep.jsp";
  public static final String PG_LOAD_RICERCAFASCICOLOSOGGETTI_SIEP_PER_SOGGETTO_COD_CUI = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/RicercaSoggettiConProcedimentiSiepCodCui.jsp";
  public static final String PG_RICERCAFASCICOLO_SIEP_PER_SOGGETTO = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/RicercaFascicoloSiepPerSoggetto.jsp";
  public static final String PG_LOAD_RICERCAFASCICOLOSOGGETTI_SIEP_PER_SOGGETTO_PER_ALIAS = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/RicercaSoggettiAliasConProcedimentiSiep.jsp";

  public static final String PG_LOAD_INSERISCRESIDENZAFASCICOLO = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/LoadInserisciResidenzaFascicolo.jsp";
  public static final String PG_LOAD_INSERISCIDOMICILIOFASCICOLO = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/LoadInserisciDomicilioFascicolo.jsp";
  public static final String PG_RICERCA_LOAD_RICERCA_FASCICOLI_NON_VALIDATI = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/LoadRicercaProcedimentiNonValidati.jsp";
  public static final String PG_RICERCA_FASCICOLI_NON_VALIDATI = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/ElencoProcedimentiNonValidati.jsp";

  public static final String PG_DETTAGLIO_NOTE_PROCEDIMENTO = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/DettaglioNoteProcedimento.jsp";
  public static final String PG_MODIFICA_NOTE_PROCEDIMENTO = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/ModificaNoteProcedimento.jsp";
  public static final String PG_LOAD_RICERCA_SOSPESI_INTERROTTI = f3b.web.IWebConstants.ROOT_DIR + "files/siap/siep/fascicolo/LoadRicercaSospesiInterrotti.jsp";

  public static final String PG_RIDEFINIZIONESENTENZA            = ROOT_DIR + "files/siap/siep/fascicolo/RidefinizioneSentenza.jsp";
}