package siap.regesies.regesentenza.action;

import siap.regesies.action.ICostantiRegeSies;
import f3b.web.IWebConstants;

/**
 * <p>Title: ICostantiRegeSentenza</p>
 * <p>Description: Classe di costanti di RegeSentenza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public interface ICostantiRegeSentenza extends ICostantiRegeSies
{
  public static final String PROVVEDIMENTO_IN_SESSION = "provvedimentoRege";
  public static final String CAMPO_ID_FILE = "IdFile";
  public static final String CAMPO_COD_TIPO_PROVVEDIMENTO = "CodTipoProvvedimento";
  public static final String CAMPO_DESCR_TIPO_PROVVEDIMENTO = "DescrTipoProvvedimento";
  public static final String CAMPO_ANNO_REGE_PM = "AnnoRegePm";
  public static final String CAMPO_NUMERO_REGE_PM = "NumeroRegePm";
  public static final String CAMPO_GIORNO_DATA_ARRIVO_ATTO = "GiornoDataArrivoAtto";
  public static final String CAMPO_MESE_DATA_ARRIVO_ATTO = "MeseDataArrivoAtto";
  public static final String CAMPO_ANNO_DATA_ARRIVO_ATTO = "AnnoDataArrivoAtto";
  public static final String CAMPO_GIORNO_DATA_PROVVEDIMENTO = "GiornoDataProvvedimento";
  public static final String CAMPO_MESE_DATA_PROVVEDIMENTO = "MeseDataProvvedimento";
  public static final String CAMPO_ANNO_DATA_PROVVEDIMENTO = "AnnoDataProvvedimento";
  public static final String CAMPO_COD_TIPO_AUTORITA_EMITTENTE = "CodTipoAutoritaEmittente";
  public static final String CAMPO_DESCR_TIPO_AUTORITA_EMITTENTE = "DescrTipoAutoritaEmittente";

  public static final String CAMPO_COD_LUOGO_EMITTENTE = "CodLuogoEmittente";
  public static final String CAMPO_DESCR_LUOGO_EMITTENTE = "DescrLuogoEmittente";

  public static final String CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE = "NumSezioneAutoritaEmittente";
  public static final String CAMPO_ANNO_SENTENZA = "AnnoSentenza";
  public static final String CAMPO_NUMERO_SENTENZA = "NumeroSentenza";
  public static final String CAMPO_GIORNO_DATA_IRREVOCABILITA = "GiornoDataIrrevocabilita";
  public static final String CAMPO_MESE_DATA_IRREVOCABILITA = "MeseDataIrrevocabilita";
  public static final String CAMPO_ANNO_DATA_IRREVOCABILITA = "AnnoDataIrrevocabilita";
  public static final String CAMPO_FLAG_SENTENZA_APPLICAZ_PENA = "FlagSentenzaApplicazPena";
  public static final String CAMPO_COD_TIPO_PROVV_RIF = "CodTipoProvvRif";
  public static final String CAMPO_GIORNO_DATA_PROVV_RIF = "GiornoDataProvvRif";
  public static final String CAMPO_MESE_DATA_PROVV_RIF = "MeseDataProvvRif";
  public static final String CAMPO_ANNO_DATA_PROVV_RIF = "AnnoDataProvvRif";
  public static final String CAMPO_COD_TIPO_AUTORITA_PROVV_RIF = "CodTipoAutoritaProvvRif";
  public static final String CAMPO_ANNO_PROVV_RIF = "AnnoProvvRif";
  public static final String CAMPO_NUMERO_PROVV_RIF = "NumeroProvvRif";
  public static final String CAMPO_COD_LUOGO_PROVV_RIF = "CodLuogoProvvRif";
  public static final String CAMPO_NUM_SEZIONE_AUTORITA_PROVV_RIF = "NumSezioneAutoritaProvvRif";
  public static final String CAMPO_COD_TIPO_DECISIONE_CASSAZIONE = "CodTipoDecisioneCassazione";
  public static final String CAMPO_ANNO_SENTENZA_CASSAZIONE = "AnnoSentenzaCassazione";
  public static final String CAMPO_NUMERO_SENTENZA_CASSAZIONE = "NumeroSentenzaCassazione";
  public static final String CAMPO_ANNO_RACCOLTA_GENERALE = "AnnoRaccoltaGenerale";
  public static final String CAMPO_NUMERO_RACCOLTA_GENERALE = "NumeroRaccoltaGenerale";
  public static final String CAMPO_ANNO_REGISTRO_35 = "AnnoRegistro35";
  public static final String CAMPO_NUM_REGISTRO_35 = "NumRegistro35";
  public static final String CAMPO_NOTE = "Note";
  public static final String CAMPO_DESCR_NUM_CAMPIONE_PENALE = "DescrNumCampionePenale";
  public static final String CAMPO_ANNO_REGE_GIP = "AnnoRegeGip";
  public static final String CAMPO_NUMERO_REGE_GIP = "NumeroRegeGip";
  public static final String CAMPO_ANNO_REGE_DIB = "AnnoRegeDib";
  public static final String CAMPO_NUMERO_REGE_DIB = "NumeroRegeDib";
  public static final String CAMPO_ANNO_REGE_CAS = "AnnoRegeCas";
  public static final String CAMPO_NUMERO_REGE_CAS = "NumeroRegeCas";
  public static final String CAMPO_ANNO_REGE_CAP = "AnnoRegeCap";
  public static final String CAMPO_NUMERO_REGE_CAP = "NumeroRegeCap";
  public static final String CAMPO_ANNO_REGE_CASAP = "AnnoRegeCasap";
  public static final String CAMPO_NUMERO_REGE_CASAP = "NumeroRegeCasap";
  public static final String CAMPO_NOTA_DISPOSITIVO = "NotaDispositivo";
  public static final String CAMPO_COD_TIPO_RITO = "CodTipoRito";
  public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
  public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
  public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
  public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
  public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento";
  public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
  public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
  public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
  public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
  public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO = "CodUfficioAggiornamento";
  public static final String CAMPO_FLAG_GIUDIZIO_ABBREVIATO = "FlagGiudizioAbbreviato";
  public static final String CAMPO_COUNT_SOGGETTI = "CountSoggetti";
  public static final String CAMPO_PROGR_PROVVEDIMENTO = "ProgrProvvedimento";
  public static final String CAMPO_SOGGETTO_OMONIMO = "Omonimo";
  public static final String CAMPO_GIORNO_ISCRIZIONE_ATTI = "GiornoIscrizioneAtti";
  public static final String CAMPO_MESE_ISCRIZIONE_ATTI = "MeseIscrizioneAtti";
  public static final String CAMPO_ANNO_ISCRIZIONE_ATTI = "AnnoIscrizioneAtti";
  public static final String CAMPO_GIORNO_DATA_ISCRIZIONE = "GiornoDataIscrizione";
  public static final String CAMPO_MESE_DATA_ISCRIZIONE = "MeseDataIscrizione";
  public static final String CAMPO_ANNO_DATA_ISCRIZIONE = "AnnoDataIscrizione";
 
  public static final String CAMPO_CHECK_RESIDENZA = "CheckResidenza";
  public static final String CAMPO_CHECK_REATO = "CheckReato";
  public static final String CAMPO_CHECK_CIRCOSTANZA = "CheckCircostanza";
  public static final String CAMPO_CHECK_NOTIZIAREATO = "CheckNotiziaReato";
  public static final String CAMPO_CHECK_DISPOSITIVO = "CheckDispositivo";
  public static final String CAMPO_CHECK_DIFENSORI = "CheckDifensori";

  public static final String PG_LOAD_RICERCAREGESENTENZA = IWebConstants.ROOT_DIR + "files/siap/regesies/regesentenza/LoadRicercaRegeSentenzaPerEstremi.jsp";
  public static final String PG_LOAD_DETTAGLIO_PROVVEDIMENTO = IWebConstants.ROOT_DIR + "files/siap/regesies/regesentenza/DettaglioProvvedimento.jsp";
  public static final String PG_LOAD_DETTAGLIOREGESENTENZA = IWebConstants.ROOT_DIR + "files/siap/regesies/regesentenza/DettaglioRegeSentenza.jsp";
  public static final String PG_LOAD_DETTAGLIOREGE_DECRETO = IWebConstants.ROOT_DIR + "files/siap/regesies/regesentenza/DettaglioRegeDecreto.jsp";
  public static final String PG_ESITO_IMPORT = IWebConstants.ROOT_DIR + "files/siap/regesies/regesentenza/EsitoImport.jsp";
  public static final String PG_INSERISCIFASCICOLO = IWebConstants.ROOT_DIR + "files/siap/regesies/regesentenza/LoadInserimentoProcedimento.jsp";
  public static final String PG_ESITO_INTEGRAZIONE = IWebConstants.ROOT_DIR + "files/siap/regesies/regesentenza/EsitoIntegrazione.jsp";

  public static final String PG_RICERCAREGESENTENZA = IWebConstants.ROOT_DIR + "files/siap/regesies/regesentenza/RicercaRegeSentenza.jsp";
  public static final String PG_LOAD_MODIFICAREGESENTENZA = IWebConstants.ROOT_DIR + "files/siap/regesies/regesentenza/LoadModificaRegeSentenza.jsp";
  public static final String PG_LOAD_MODIFICAREGEDECRETO = IWebConstants.ROOT_DIR + "files/siap/regesies/regesentenza/LoadModificaRegeDecreto.jsp";
  public static final String PG_RIASSUNTO_INTEGRAZIONE = IWebConstants.ROOT_DIR + "files/siap/regesies/regesentenza/RiassuntoIntegrazione.jsp";

  public static final String PG_ELENCO_PROVVEDIMENTI = IWebConstants.ROOT_DIR + "files/siap/regesies/regesentenza/ElencoProvvedimenti.jsp";
  public static final String PAGE_BUTTONS_REGE_SENTENZA = IWebConstants.ROOT_DIR + "files/siap/regesies/regesentenza/buttonsRegeSentenza.jsp";
  public static final String PG_ELENCO_SOGGETTI = IWebConstants.ROOT_DIR + "files/siap/regesies/regesoggetto/ElencoRegeSoggetto.jsp";
  public static final String CAMPO_COD_TIPO_RITO_RIF = "CodTipoRitoRif";

  public static final String ESITO_POSITIVO = "0000";
  public static final String ESITO_DATO_PRESENTE = "1111";

  public static final String ESITO_ERRORE = "9999";


}