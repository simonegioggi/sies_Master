package siap.sius.impugnazione.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiImpugnazione</p>
* <p>Description: Classe di costanti di Impugnazione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiImpugnazione
{
  public static final String CAMPO_COD_TIPO_UFFICIO             = "CodTipoUfficio";
  public static final String CAMPO_RICHIESTA_STAMPA             = "RichiestaStampa";
  public static final String CAMPO_DESCR_COMUNE_UFFICIO         = "DescrComuneUfficio";
  public static final String CAMPO_NOTE                         = "Note";
  public static final String CAMPO_COD_TEMPLATE                 = "CodTemplate";

  public static final String COD_EVENTO_PROVVEDIMENTO           = "01";
  public static final String COD_ORDINANZA                      = "03";
  public static final String COD_DECRETO                        = "02";
  public static final String COD_TIPO_SCADENZARIO               = "50";
  public static final String COD_TIPO_PROVVEDIMENTO             = "15";

  public static final String CAMPO_ID_IMPUGNAZIONE = "IdImpugnazione";
  public static final String CAMPO_ANNO_S7 = "AnnoS7";
  public static final String CAMPO_PROGR_S7 = "ProgrS7";
  public static final String CAMPO_COD_TIPO_IMPUGNAZIONE = "CodTipoImpugnazione";
  public static final String CAMPO_SOGGETTO_IMPUGNANTE = "SoggettoImpugnante";
  public static final String CAMPO_GIORNO_DATA_RICORSO = "GiornoDataRicorso";
  public static final String CAMPO_MESE_DATA_RICORSO = "MeseDataRicorso";
  public static final String CAMPO_ANNO_DATA_RICORSO = "AnnoDataRicorso";
  public static final String CAMPO_GIORNO_DATA_ANNOTAZIONE = "GiornoDataAnnotazione";
  public static final String CAMPO_MESE_DATA_ANNOTAZIONE = "MeseDataAnnotazione";
  public static final String CAMPO_ANNO_DATA_ANNOTAZIONE = "AnnoDataAnnotazione";
  public static final String CAMPO_ANNOTAZIONE = "Annotazione";
  public static final String CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA = "GiornoDataArrivoCancelleria";
  public static final String CAMPO_MESE_DATA_ARRIVO_CANCELLERIA = "MeseDataArrivoCancelleria";
  public static final String CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA = "AnnoDataArrivoCancelleria";
  public static final String CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI = "GiornoDataTrasmissioneAtti";
  public static final String CAMPO_MESE_DATA_TRASMISSIONE_ATTI = "MeseDataTrasmissioneAtti";
  public static final String CAMPO_ANNO_DATA_TRASMISSIONE_ATTI = "AnnoDataTrasmissioneAtti";
  public static final String CAMPO_COD_AUTORITA_DESTINATARIA = "CodAutoritaDestinataria";
  public static final String CAMPO_GIORNO_DATA_DECISIONE = "GiornoDataDecisione";
  public static final String CAMPO_MESE_DATA_DECISIONE = "MeseDataDecisione";
  public static final String CAMPO_ANNO_DATA_DECISIONE = "AnnoDataDecisione";
  public static final String CAMPO_COD_TENORE_DECISIONE = "CodTenoreDecisione";
  public static final String CAMPO_GIORNO_DATA_RESTITUZIONE_ATTI = "GiornoDataRestituzioneAtti";
  public static final String CAMPO_MESE_DATA_RESTITUZIONE_ATTI = "MeseDataRestituzioneAtti";
  public static final String CAMPO_ANNO_DATA_RESTITUZIONE_ATTI = "AnnoDataRestituzioneAtti";
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
  public static final String CAMPO_DEP_OPID_DEPOSITO_ORDINANZA_PC = "DepOpidDepositoOrdinanzaPc";
  public static final String CAMPO_DEP_DEC_ID_DEPOSITO_DECRETO = "DepDecIdDepositoDecreto";
  public static final String CAMPO_FLAG_SOSP_ESEC              = "FlagSospEsecuzione";  // 07/05/2007
  public static final String CAMPO_NUMERO_IMPUGNAZIONI         = "numeroImpugnazioni";  // 11/10/2007

  // Campi Motivo Annullamento
  public static final String CAMPO_MOTIVO_ANNULLAMENTO = "MotivoAnnullamento";

  public static final String CAMPO_DESCRIZIONE_ALTRO = "DescrizioneAltro";

  public static final String PG_LOAD_IMPUGNAZIONE               = IWebConstants.ROOT_DIR + "files/siap/sius/impugnazione/LoadImpugnazione.jsp";
  public static final String PG_LOAD_DETTAGLIOPROCEDIMENTOSIUS  = IWebConstants.ROOT_DIR + "files/siap/sius/fascicolo/SintesiProcedimentoSius.jsp";
  public static final String PG_DETTAGLIOIMPUGNAZIONE           = IWebConstants.ROOT_DIR + "files/siap/sius/impugnazione/DettaglioImpugnazione.jsp";
  public static final String PG_ELENCOPROVVEDIMENTI             = IWebConstants.ROOT_DIR + "files/siap/sius/impugnazione/ElencoProvvedimenti.jsp";
  public static final String PG_BUTTONS_RICERCA                 = IWebConstants.ROOT_DIR + "files/siap/sius/impugnazione/buttonsProvvedimento.jsp";
  public static final String PG_BUTTONS_ELENCOIMPUGNAZIONI      = IWebConstants.ROOT_DIR + "files/siap/sius/impugnazione/buttonsElencoImpugnazioni.jsp"; // 16/11/2007

  public static final String PG_LOAD_RICERCAIMPUGNAZIONE	     = IWebConstants.ROOT_DIR + "files/siap/sius/impugnazione/LoadRicercaImpugnazione.jsp";
  public static final String PG_LOAD_DETTAGLIOIMPUGNAZIONE	    = IWebConstants.ROOT_DIR + "files/siap/sius/impugnazione/DettaglioImpugnazione.jsp";
  public static final String PG_RICERCAIMPUGNAZIONE	            = IWebConstants.ROOT_DIR + "files/siap/sius/impugnazione/RicercaImpugnazione.jsp";
  public static final String PG_LOAD_INSERISCIIMPUGNAZIONE	    = IWebConstants.ROOT_DIR + "files/siap/sius/impugnazione/LoadInserisciImpugnazione.jsp";
  public static final String PG_LOAD_MODIFICAIMPUGNAZIONE 	    = IWebConstants.ROOT_DIR + "files/siap/sius/impugnazione/LoadModificaImpugnazione.jsp";
  public static final String PG_LOAD_ANNULLAMENTO_IMPUGNAZIONE	= IWebConstants.ROOT_DIR + "files/siap/sius/impugnazione/LoadAnnullamentoImpugnazione.jsp";
  public static final String PG_LISTA_ANNULLATE	                = IWebConstants.ROOT_DIR + "files/siap/sius/impugnazione/ListaAnnullate.jsp";
  public static final String PG_ELENCOIMPUGNAZIONI				= IWebConstants.ROOT_DIR + "files/siap/sius/impugnazione/ElencoImpugnazioni.jsp";  // 12/10/2007
  public static final String PG_LOAD_ANNULLAMENTO_IMPUGNAZIONE_FC	= IWebConstants.ROOT_DIR + "files/siap/siep/fogliocomplementare/LoadAnnullamentoImpugnazioneFc.jsp";

  // OPPOSIZIONE - since 06/2014
  public static final String PG_ELENCO_PROVVEDIMENTI_OPP        = IWebConstants.ROOT_DIR + "files/siap/sius/impugnazione/ElencoProvvedimentiOpposizione.jsp";
  public static final String PG_BUTTONS_OPPOSIZIONE             = IWebConstants.ROOT_DIR + "files/siap/sius/impugnazione/buttonsProvvedimentoOpposizione.jsp";
  public static final String PG_ELENCO_OPPOSIZIONI              = IWebConstants.ROOT_DIR + "files/siap/sius/impugnazione/ElencoOpposizioni.jsp";
  public static final String PG_BUTTONS_ELENCO_OPPOSIZIONI      = IWebConstants.ROOT_DIR + "files/siap/sius/impugnazione/buttonsElencoOpposizioni.jsp";

  public static final String PG_LOAD_INSERISCI_OPPOSIZIONE      = IWebConstants.ROOT_DIR + "files/siap/sius/impugnazione/LoadInserisciOpposizione.jsp";
  public static final String PG_LOAD_DETTAGLIO_OPPOSIZIONE      = IWebConstants.ROOT_DIR + "files/siap/sius/impugnazione/DettaglioOpposizione.jsp";
  public static final String PG_LOAD_MODIFICA_OPPOSIZIONE       = IWebConstants.ROOT_DIR + "files/siap/sius/impugnazione/LoadModificaOpposizione.jsp";
  public static final String PG_LOAD_ANNULLAMENTO_OPPOSIZIONE   = IWebConstants.ROOT_DIR + "files/siap/sius/impugnazione/LoadAnnullamentoOpposizione.jsp";

}
