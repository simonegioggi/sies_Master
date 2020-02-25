package siap.siep.verbale.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiVerbale</p>
* <p>Description: Classe di costanti di Verbale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiVerbale
{
  public static final String CAMPO_ID_VERBALE = "IdVerbale";
  public static final String CAMPO_COD_TIPO_VERBALE = "CodTipoVerbale";
  public static final String CAMPO_GIORNO_DATA_EMISSIONE = "GiornoDataEmissione";
  public static final String CAMPO_MESE_DATA_EMISSIONE = "MeseDataEmissione";
  public static final String CAMPO_ANNO_DATA_EMISSIONE = "AnnoDataEmissione";
  public static final String CAMPO_GIORNO_DATA_PERVENIMENTO = "GiornoDataPervenimento";
  public static final String CAMPO_MESE_DATA_PERVENIMENTO = "MeseDataPervenimento";
  public static final String CAMPO_ANNO_DATA_PERVENIMENTO = "AnnoDataPervenimento";
  public static final String CAMPO_COD_TIPO_UFFICIO_FIRMATARIO = "CodTipoUfficioFirmatario";
  public static final String CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO = "CodLuogoUfficioFirmatario";
  public static final String CAMPO_NOTE = "Note";
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
  public static final String CAMPO_EVE_ID_EVENTO = "EveIdEvento";
  public static final String CSS_ID_CSSA = "IdCssa";
  public static final String IST_DET_ID_ISTITUTO_DETENZIONE = "IstDetIdIstitutoDetenzione";
  public static final String CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO_UG = "CodLuogoUfficioFirmatarioUg";
  public static final String CAMPO_GIORNO_DATA_SCADENZA = "GiornoDataScadenza";
  public static final String CAMPO_MESE_DATA_SCADENZA = "MeseDataScadenza";
  public static final String CAMPO_ANNO_DATA_SCADENZA = "AnnoDataScadenza";

  public static final String CAMPO_NUMERO_PROTOCOLLO = "NumeroProtocollo";

  public static final String CAMPO_NUM_ANNI_ESPULSIONE = "NumAnniEspulsione";
  public static final String CAMPO_NUM_MESI_ESPULSIONE = "NumMesiEspulsione";
  public static final String CAMPO_NUM_GIORNI_ESPULSIONE = "NumGiorniEspulsione"; 
  
  public static final String PG_LOAD_RICERCAVERBALE	= IWebConstants.ROOT_DIR + "files/siap/siep/verbale/LoadRicercaVerbale.jsp";
  public static final String PG_LOAD_DETTAGLIO_VERBALE_ARRESTO	= IWebConstants.ROOT_DIR + "files/siap/siep/verbale/DettaglioVerbaleArresto.jsp";
  public static final String PG_RICERCAVERBALE	= IWebConstants.ROOT_DIR + "files/siap/siep/verbale/RicercaVerbale.jsp";
  public static final String PG_LOAD_INSERISCIVERBALE_ARRESTO	= IWebConstants.ROOT_DIR + "files/siap/siep/verbale/LoadInserisciVerbaleArresto.jsp";
  public static final String PG_LOAD_INSERISCI_VERBALE_VANE_RICERCHE	= IWebConstants.ROOT_DIR + "files/siap/siep/verbale/LoadInserisciVerbaleVaneRicerche.jsp";
  public static final String PG_LOAD_DETTAGLIO_VERBALE_VANE_RICERCHE	= IWebConstants.ROOT_DIR + "files/siap/siep/verbale/DettaglioVerbaleVaneRicerche.jsp";
  public static final String PG_LOAD_DETTAGLIO_PENA_RESIDUA_VERBALE_ARRESTO	= IWebConstants.ROOT_DIR + "files/siap/siep/verbale/DettaglioPenaResiduaVerbaleArresto.jsp";
  public static final String PG_REGISTRA_PENA_RESIDUA_VERBALE_ARRESTO	= IWebConstants.ROOT_DIR + "files/siap/siep/verbale/RegistraPenaVerbaleArresto.jsp";
  public static final String PG_LOAD_INSERISCI_VERBALE_SOTTOSCRIZIONE	= IWebConstants.ROOT_DIR + "files/siap/siep/verbale/LoadInserisciVerbaleSottoscrizione.jsp";
  public static final String PG_LOAD_INSERISCI_VARIAZIONE_VERBALE_SOTTOSCRIZIONE	= IWebConstants.ROOT_DIR + "files/siap/siep/verbale/LoadInserisciVariazioneVerbaleSottoscrizione.jsp";
  public static final String PG_LOAD_DETTAGLIO_VERBALE_SOTTOSCRIZIONE	= IWebConstants.ROOT_DIR + "files/siap/siep/verbale/DettaglioVerbaleSottoscrizione.jsp";
  public static final String PG_LOAD_DETTAGLIO_PENA_RESIDUA_VERBALE_SOTTOSCRIZIONE	= IWebConstants.ROOT_DIR + "files/siap/siep/verbale/DettaglioPenaResiduaVerbaleSottoscrizione.jsp";
  public static final String PG_LOAD_DETTAGLIO_PENA_RESIDUA_VARIAZIONE_VERBALE_SOTTOSCRIZIONE	= IWebConstants.ROOT_DIR + "files/siap/siep/verbale/DettaglioPenaResiduaVariazioneVerbaleSottoscrizione.jsp";
  public static final String PG_REGISTRA_PENA_RESIDUA_VERBALE_SOTTOSCRIZIONE	= IWebConstants.ROOT_DIR + "files/siap/siep/verbale/RegistraPenaVerbaleSottoscrizione.jsp";
  public static final String PG_REGISTRA_PENA_RESIDUA_VARIAZIONE_VERBALE_SOTTOSCRIZIONE	= IWebConstants.ROOT_DIR + "files/siap/siep/verbale/RegistraPenaVariazioneVerbaleSottoscrizione.jsp";
  public static final String PG_LOAD_INSERISCINOTIFICA_CARCERE	= IWebConstants.ROOT_DIR + "files/siap/siep/verbale/LoadInserisciNotificaCarcere.jsp";
  public static final String PG_LOAD_DETTAGLIO_PENA_RESIDUA_NOTIFICA_CARCERE	= IWebConstants.ROOT_DIR + "files/siap/siep/verbale/DettaglioPenaResiduaNotificaCarcere.jsp";
}