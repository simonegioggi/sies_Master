package siap.sius.udienza.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiUdienza</p>
* <p>Description: Classe di costanti di Udienza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiUdienza
{
  public static final String CAMPO_ID_UDIENZA                   = "IdUdienza";
  public static final String CAMPO_GIORNO_DATA_UDIENZA          = "GiornoDataUdienza";
  public static final String CAMPO_MESE_DATA_UDIENZA            = "MeseDataUdienza";
  public static final String CAMPO_ANNO_DATA_UDIENZA            = "AnnoDataUdienza";

  public static final String CAMPO_GIORNO_DATA_UDIENZA_FINE     = "GiornoDataUdienzaFine";
  public static final String CAMPO_MESE_DATA_UDIENZA_FINE       = "MeseDataUdienzaFine";
  public static final String CAMPO_ANNO_DATA_UDIENZA_FINE       = "AnnoDataUdienzaFine";

  public static final String CAMPO_DATA_UDIENZA                 = "DataUdienza";   /* Luigi */
  public static final String CAMPO_COD_PRESIDENTE               = "CodPresidente";
  public static final String CAMPO_COD_GIUDICE_1                = "CodGiudice1";
  public static final String CAMPO_COD_GIUDICE_2                = "CodGiudice2";
  public static final String CAMPO_COD_PG                       = "CodPg";
  public static final String CAMPO_COD_ID_ESPERTO_1             = "CodIdEsperto1";
  public static final String CAMPO_COD_ID_ESPERTO_2             = "CodIdEsperto2";
  public static final String CAMPO_COD_ID_ASSISTENTE            = "CodIdAssistente";
 // public static final String CAMPO_FLAG_RINVIATA              = "FlagRinviata";
  public static final String CAMPO_NUMERO_MAX_FASCICOLI         = "NumeroMaxFascicoli";
  public static final String CAMPO_COD_OPERATORE_INSERIMENTO    = "CodOperatoreInserimento";
  public static final String CAMPO_GIORNO_DATA_INSERIMENTO      = "GiornoDataInserimento";
  public static final String CAMPO_MESE_DATA_INSERIMENTO        = "MeseDataInserimento";
  public static final String CAMPO_ANNO_DATA_INSERIMENTO        = "AnnoDataInserimento";
  public static final String CAMPO_DATA_INSERIMENTO             = "DataInserimento";   /* Luigi */
  public static final String CAMPO_COD_UFFICIO_INSERIMENTO      = "CodUfficioInserimento";
  public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO  = "CodOperatoreAggiornamento";
  public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO    = "GiornoDataAggiornamento";
  public static final String CAMPO_MESE_DATA_AGGIORNAMENTO      = "MeseDataAggiornamento";
  public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO      = "AnnoDataAggiornamento";
  public static final String CAMPO_DATA_AGGIORNAMENTO           = "DataAggiornamento";   /* Luigi */
  public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO    = "CodUfficioAggiornamento";
  public static final String CAMPO_LUOGO_UDIENZA                = "LuogoUdienza";
  public static final String CAMPO_COD_UFFICIO_APPARTENENZA     = "CodUfficioAppartenenza";
  public static final String CAMPO_NUM_COLLEGIO                 = "NumCollegio";
  
  public static final String CAMPO_ORA_INIZIO = "OraInizio";
  public static final String CAMPO_MIN_INIZIO = "MinInizio";
  public static final String CAMPO_ORA_FINE = "OraFine";
  public static final String CAMPO_MIN_FINE = "MinFine";
  // Orario di fine Camera Consiglio.
  public static final String CAMPO_ORA_FINE_CC = "OraFineCC";
  public static final String CAMPO_MIN_FINE_CC = "MinFineCC";
  
  public static final String CAMPO_CHECK_RUOLO  = "CheckRuolo"; // 18/06/2004

  public static final String CAMPO_COD_LUOGO_DETENZIONE = "CodLuogoDetenzione";
  public static final String CAMPO_COD_IST_DETENZIONE = "CodIstDetenzione";
  public static final String CAMPO_COD_AVVOCATO = "CodAvvocato";

  public static final String CAMPO_INTERESSATO        = "Interessato";
  public static final String CAMPO_DIFENSORE          = "Difensore";
  public static final String CAMPO_PROCURA_GENERALE   = "ProcuraGenerale";
  public static final String CAMPO_TIPONOTIFICA       = "TipoNotifica";

  public static final String CAMPO_TIPOORDINAMENTO    = "TipoOrdinamento";

  public static final String CAMPO_CHECK_INS_FISS_UDIENZA  = "CheckInsFissUdienza";
  
  public static final String CODTIPONOTIFICACOMUNICAZIONE  = "C";
  public static final String CODTIPONOTIFICA  = "N";


  public static final String PG_LOAD_RICERCAUDIENZA	  = IWebConstants.ROOT_DIR + "files/siap/sius/udienza/LoadRicercaUdienza.jsp";
  public static final String PG_LOAD_RICERCAUDIENZA_UDS	  = IWebConstants.ROOT_DIR + "files/siap/sius/udienza/LoadRicercaUdienzaUDS.jsp";

  public static final String PG_LOAD_DETTAGLIOUDIENZA	= IWebConstants.ROOT_DIR + "files/siap/sius/udienza/DettaglioUdienza.jsp";
  public static final String PG_LOAD_DETTAGLIOUDIENZA_UDS = IWebConstants.ROOT_DIR + "files/siap/sius/udienza/DettaglioUdienzaUDS.jsp";
  public static final String PG_RICERCAUDIENZA	= IWebConstants.ROOT_DIR + "files/siap/sius/udienza/RicercaUdienza.jsp";
  public static final String PG_RICERCAUDIENZA_UDS	= IWebConstants.ROOT_DIR + "files/siap/sius/udienza/RicercaUdienzaUDS.jsp";

  public static final String PG_ELENCOUDIENZE  = IWebConstants.ROOT_DIR + "files/siap/sius/udienza/ElencoUdienze.jsp";
  public static final String PG_LOAD_INSERISCIUDIENZA	 = IWebConstants.ROOT_DIR + "files/siap/sius/udienza/LoadInserisciUdienza.jsp";
  public static final String PG_LOAD_INSERISCIUDIENZA_UDS  = IWebConstants.ROOT_DIR + "files/siap/sius/udienza/LoadInserisciUdienzaUDS.jsp";

  public static final String PG_LOAD_INSERISCICOPIAUDIENZA_UDS = IWebConstants.ROOT_DIR + "files/siap/sius/udienza/LoadInserisciCopiaUdienzaUDS.jsp";
  public static final String PG_LOAD_INSERISCIFISSAZIONEUDIENZA	= IWebConstants.ROOT_DIR + "files/siap/sius/udienza/LoadInserisciFissazioneUdienza.jsp";
  public static final String PG_LOAD_DETTAGLIOFISSAZIONEUDIENZA	= IWebConstants.ROOT_DIR + "files/siap/sius/udienza/DettaglioFissazioneUdienza.jsp";
  public static final String PG_LOAD_RICERCAUDIENZAXPROCEDIMENTI = IWebConstants.ROOT_DIR + "files/siap/sius/udienza/LoadRicercaUdienzaXProcedimenti.jsp";
  public static final String PG_LOAD_RICERCAUDIENZAXPROCEDIMENTI_DFP = IWebConstants.ROOT_DIR + "files/siap/sius/udienza/LoadRicercaUdienzaXProcedimentiDFP.jsp";
  public static final String PG_CONFERMA_PREFISSAZIONE = IWebConstants.ROOT_DIR + "files/siap/sius/udienza/ConfermaPreF.jsp";
  
  public static final String PG_LOAD_RINVIOUDIENZA	= IWebConstants.ROOT_DIR + "files/siap/sius/udienza/LoadRinvioUdienza.jsp";
  public static final String PG_DETTAGLIO_RINVIOUDIENZA	= IWebConstants.ROOT_DIR + "files/siap/sius/udienza/DettaglioRinvioUdienza.jsp";

  public static final String PG_LOAD_INSERISCIVERBALEUDIENZA	= IWebConstants.ROOT_DIR + "files/siap/sius/udienza/LoadInserisciVerbaleUdienza.jsp";
  public static final String PG_LOAD_DETTAGLIOVERBALEUDIENZA	= IWebConstants.ROOT_DIR + "files/siap/sius/udienza/DettaglioVerbaleUdienza.jsp";
  public static final String PG_LOAD_DESTINATARI  = IWebConstants.ROOT_DIR + "files/siap/sius/udienza/Destinatari.jsp";

  public static final String PG_LOAD_INSERISCIORDINANZARINVIOUDIENZA	= IWebConstants.ROOT_DIR + "files/siap/sius/udienza/LoadInserisciOrdinanzaRinvioUdienza.jsp";

  public static final String PG_LOAD_INSERISCISENTENZARINVIOUDIENZA	= IWebConstants.ROOT_DIR + "files/siap/sius/udienza/LoadInserisciSentenzaRinvioUdienza.jsp";
  
  // genny 01/03/2004
  public static final String PG_DETTAGLIO_VERBALERINVIOUDIENZA	= IWebConstants.ROOT_DIR + "files/siap/sius/udienza/DettaglioVerbaleRinvioUdienza.jsp";

  // Bottoni nel dettaglio Fissazione Udienza
  public static final String PG_BUTTONS	= IWebConstants.ROOT_DIR + "files/siap/sius/udienza/BottoniFissUdienza.jsp";

  // ID dei modelli di stampa 
  public static final String ID_TEMPLATE_VERBALE_UDIENZA = "SIUS_VE_001";
  public static final String ID_TEMPLATE_VERBALE_UDIENZA_UDS = "SIUS_VE_003";
  
}