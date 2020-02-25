package siap.siep.istanza.action;

import f3b.web.IWebConstants;

/**
 * <p>Title: ICostantiIstanza</p>
 * <p>Description: Classe di costanti di Istanza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public interface ICostantiIstanza
{
  public static final String CAMPO_ID_ISTANZA = "IdIstanza";
  public static final String CAMPO_COD_MOTIVO = "CodMotivo";
  public static final String CAMPO_NOTE = "Note";
  public static final String CAMPO_COGNOME_SOGGETTO_PRESENTANTE = "CognomeSoggettoPresentante";
  public static final String CAMPO_NOME_SOGGETTO_PRESENTANTE = "NomeSoggettoPresentante";
  public static final String CAMPO_GIORNO_DATA_PRESENTAZIONE = "GiornoDataPresentazione";
  public static final String CAMPO_MESE_DATA_PRESENTAZIONE = "MeseDataPresentazione";
  public static final String CAMPO_ANNO_DATA_PRESENTAZIONE = "AnnoDataPresentazione";
  public static final String CAMPO_COD_ESITO = "CodEsito";
  public static final String CAMPO_ANNO_REGISTRO = "AnnoRegistro";
  public static final String CAMPO_PROGR_REGISTRO = "ProgrRegistro";
  public static final String CAMPO_COD_TIPO_UFFICIO_DESTINATARIO = "CodTipoUfficioDestinatario";
  public static final String CAMPO_COD_LUOGO_DESTINATARIO = "CodLuogoDestinatario";
  public static final String CAMPO_COD_UFFICIO_DESTINATARIO = "CodUfficioDestinatario";
  public static final String CAMPO_COGNOME_AVVOCATO = "CognomeAvvocato";
  public static final String CAMPO_NOME_AVVOCATO = "NomeAvvocato";
  public static final String CAMPO_FORO_COMPETENZA = "ForoCompetenza";
  public static final String CAMPO_ANNO_SENTENZA = "AnnoSentenza";
  public static final String CAMPO_NUMERO_SENTENZA = "NumeroSentenza";
  public static final String CAMPO_GIORNO_DATA_SENTENZA = "GiornoDataSentenza";
  public static final String CAMPO_MESE_DATA_SENTENZA = "MeseDataSentenza";
  public static final String CAMPO_ANNO_DATA_SENTENZA = "AnnoDataSentenza";
  public static final String CAMPO_GIORNO_DATA_IRREVOCABILITA = "GiornoDataIrrevocabilita";
  public static final String CAMPO_MESE_DATA_IRREVOCABILITA = "MeseDataIrrevocabilita";
  public static final String CAMPO_ANNO_DATA_IRREVOCABILITA = "AnnoDataIrrevocabilita";
  public static final String CAMPO_COD_TIPO_AUTORITA_EMITTENTE = "CodTipoAutoritaEmittente";
  public static final String CAMPO_COD_LUOGO_EMITTENTE = "CodLuogoEmittente";
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
  public static final String CAMPO_SOG_ID_SOGGETTO = "SogIdSoggetto";
  public static final String CAMPO_FLAG_PRESENZA_FASCICOLO = "FlagPresenzaFascicolo";
  public static final String CAMPO_CAM_ID_CAMPO_NOTE = "CamIdCampoNote";


  public static final String TEMPLATE_TRASFERISCI_ISTANZA = "STXISBDI";

  public static final String PG_LOAD_RICERCAISTANZA = IWebConstants.ROOT_DIR + "files/siap/siep/istanza/LoadRicercaIstanza.jsp";
  public static final String PG_LOAD_DETTAGLIOISTANZA = IWebConstants.ROOT_DIR + "files/siap/siep/istanza/DettaglioIstanza.jsp";
  public static final String PG_RICERCAISTANZA = IWebConstants.ROOT_DIR + "files/siap/siep/istanza/RicercaIstanza.jsp";
  public static final String PG_LOAD_INSERISCIISTANZA = IWebConstants.ROOT_DIR + "files/siap/siep/istanza/LoadInserisciIstanza.jsp";
  public static final String PG_RICERCA_EVENTO_ISTANZA = IWebConstants.ROOT_DIR + "files/siap/siep/istanza/RicercaEventoIstanza.jsp";
  public static final String PG_LOAD_TRASFERISCI_ISTANZA_STESSA_BDI = IWebConstants.ROOT_DIR + "files/siap/siep/istanza/LoadTrasferisciIstanza.jsp";
  public static final String PG_RICERCAISTANZA_SOGGETTI = IWebConstants.ROOT_DIR + "files/siap/siep/istanza/RicercaIstanzaSoggetto.jsp";
  //public static final String PG_DETTAGLIOISTANZA_UNICOSOGGETTO = IWebConstants.ROOT_DIR + "files/siap/siep/istanza/DettaglioIstanzaSoggettoUnico.jsp";
  public static final String PG_LOAD_TRASFERISCI_ISTANZA = IWebConstants.ROOT_DIR + "files/siap/siep/istanza/LoadTrasferisciIstanza.jsp";
  public static final String PG_BUTTONS_ISTANZA = IWebConstants.ROOT_DIR + "files/siap/siep/istanza/buttonsIstanza.jsp";
  public static final String PG_BUTTONS_ISTANZA_TRASFERIMENTO = IWebConstants.ROOT_DIR + "files/siap/siep/istanza/buttosIstanzaTrasferimento.jsp";
  public static final String PG_ATTESA = IWebConstants.ROOT_DIR + "files/siap/siep/istanza/Attesa.jsp";

  public static final String PG_DETTAGLIO_MESSAGGIO_RICEVUTO = IWebConstants.ROOT_DIR + "files/siap/siep/istanza/DettaglioMessaggiIstanzaRicevuta.jsp";
  public static final String PG_DETTAGLIO_MESSAGGIO_TRASMESSO = IWebConstants.ROOT_DIR + "files/siap/siep/istanza/DettaglioIstanzaSpedita.jsp";
  public static final String PG_DETTAGLIO_TRASFERISCI_ISTANZA = IWebConstants.ROOT_DIR + "files/siap/siep/istanza/DettaglioTrasferisciIstanza.jsp";
  public static final String PG_LOAD_INSERISCIISTANZA_ANN_TRASMISSIONE = IWebConstants.ROOT_DIR + "files/siap/siep/istanza/LoadInserisciIstanzaAnnTrasmissione.jsp";
  public static final String PG_LOAD_DETTAGLIOISTANZA_ANN_TRASMISSIONE = IWebConstants.ROOT_DIR + "files/siap/siep/istanza/DettaglioIstanzaAnnTrasmissione.jsp";
  public static final String PG_INSERICI_MOTIVAZIONI_ISTANZA = IWebConstants.ROOT_DIR + "files/siap/siep/istanza/LoadAnnullaIstanza.jsp";


}