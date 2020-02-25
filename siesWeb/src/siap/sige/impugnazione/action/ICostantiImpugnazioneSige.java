package siap.sige.impugnazione.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiImpugnazione</p>
* <p>Description: Classe di costanti di Impugnazione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiImpugnazioneSige
{
  public static final String CAMPO_COD_TIPO_UFFICIO             = "CodTipoUfficio";
  public static final String CAMPO_RICHIESTA_STAMPA             = "RichiestaStampa";
  public static final String CAMPO_DESCR_COMUNE_UFFICIO         = "DescrComuneUfficio";
  public static final String CAMPO_NOTE                         = "Note";
  public static final String CAMPO_COD_TEMPLATE                 = "CodTemplate";

  public static final String COD_EVENTO_PROVVEDIMENTO           = "01";
  public static final String COD_ORDINANZA                      = "03";
  public static final String COD_DECRETO                        = "02";
  public static final String COD_TIPO_PROVVEDIMENTO             = "15";
  public static final String COD_TIPO_SCADENZARIO               = "90";		// Scadenzario Irrevocabilità provvedimento SIGE
  public static final String COD_TIPO_OPPOSIZIONE               = "04";
  public static final String COD_TIPO_RICORSO                   = "01";
  public static final String CAMPO_FLAG_AGGIORNAMENTO			= "FlagAggiornamento";
  
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
  public static final String CAMPO_ID_OPPOSIZIONE_SORGENTE     = "opposizioneSorgente";
  public static final String CAMPO_SHOW_COMBO_TEMPLATE = "ShowComboTemplate";
  public static final String CAMPO_COD_DESTINATARIO_PUBBLICO_MINISTERO="CodDestinatarioPubblicoMinistero";
  public static final String CAMPO_COD_SEDE_DESTINATARIO_PUBBLICO_MINISTERO="CodSedeDestinatarioPubblicoMinistero";

  public static final String CAMPO_COD_DESTINATARIO_UFFICIO_RECUPERO_CREDITI="CodDestinatarioUfficioRecuperoCrediti";
  public static final String CAMPO_COD_SEDE_DESTINATARIO_UFFICIO_RECUPERO_CREDITI="CodSedeDestinatarioUfficioRecuperoCrediti";
  
  // Campi Motivo Annullamento
  public static final String CAMPO_MOTIVO_ANNULLAMENTO = "MotivoAnnullamento";

  public static final String PG_LOAD_DETTAGLIOPROCEDIMENTOSIGE  = IWebConstants.ROOT_DIR + "files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp";
  public static final String PG_DETTAGLIOIMPUGNAZIONESIGE       = IWebConstants.ROOT_DIR + "files/siap/sige/impugnazione/DettaglioImpugnazioneSige.jsp";
  public static final String PG_ELENCOPROVVEDIMENTISIGEXIMPUGNAZIONE = IWebConstants.ROOT_DIR + "files/siap/sige/impugnazione/ElencoProvvedimentiSigeXImpugnazione.jsp";
  public static final String PG_GRIGLIA_GESTIONE_IMPUGNAZIONI = IWebConstants.ROOT_DIR + "files/siap/sige/impugnazione/GrigliaGestioneImpugnazioni.jsp";
  public static final String PG_BUTTONS_RICERCA_PROVVEDIMENTI_SIGE = IWebConstants.ROOT_DIR + "files/siap/sige/impugnazione/buttonsProvvedimentoSige.jsp";
  public static final String PG_BUTTONS_ELENCOIMPUGNAZIONISIGE  = IWebConstants.ROOT_DIR + "files/siap/sige/impugnazione/buttonsElencoImpugnazioniSige.jsp"; 
  public static final String PG_LOAD_INSERISCIIMPUGNAZIONESIGE  = IWebConstants.ROOT_DIR + "files/siap/sige/impugnazione/LoadInserisciImpugnazioneSige.jsp";
  public static final String PG_LOAD_INSERISCI_ESITO_IMPUGNAZIONESIGE  = IWebConstants.ROOT_DIR + "files/siap/sige/impugnazione/LoadInserisciEsitoImpugnazioneSige.jsp";
  public static final String PG_LOAD_MODIFICAIMPUGNAZIONESIGE   = IWebConstants.ROOT_DIR + "files/siap/sige/impugnazione/LoadModificaImpugnazioneSige.jsp";
  public static final String PG_LOAD_ANNULLAMENTO_IMPUGNAZIONESIGE	= IWebConstants.ROOT_DIR + "files/siap/sige/impugnazione/LoadAnnullamentoImpugnazioneSige.jsp";
  public static final String PG_LISTA_ANNULLATE	                = IWebConstants.ROOT_DIR + "files/siap/sige/impugnazione/ListaAnnullate.jsp";
  public static final String PG_ELENCOIMPUGNAZIONISIGE			= IWebConstants.ROOT_DIR + "files/siap/sige/impugnazione/ElencoImpugnazioniSige.jsp";
  public static final String PG_ELENCOIMPUGNAZIONISIGEPROVVEDIMENTO = IWebConstants.ROOT_DIR + "files/siap/sige/impugnazione/ElencoImpugnazioniSigeProvvedimento.jsp";
  public static final String PG_LOAD_TRASMISSIONE_IMPUGNAZIONE= IWebConstants.ROOT_DIR + "files/siap/sige/impugnazione/LoadTrasmissioneImpugnazioneSige.jsp";
  public static final String PG_LOAD_RICORSO_CONVERTITO = IWebConstants.ROOT_DIR + "files/siap/sige/impugnazione/LoadModificaRicorsoConvertito.jsp";
  public static final String PG_LOAD_DESTINATARI = IWebConstants.ROOT_DIR + "files/siap/sige/impugnazione/Destinatari.jsp";
  public static final String PG_LOAD_DESTINATARI_OPPOSIZIONE = IWebConstants.ROOT_DIR + "files/siap/sige/impugnazione/DestinatariOpposizione.jsp";
  
  public static final String COD_ESITO_ANNULLA_SENZA_RINVIO="01";
  public static final String COD_ESITO_ANNULLA_CON_RINVIO="02";
  public static final String COD_ESITO_ANNULLA_PARZIALMENTE="03";
  public static final String COD_ESITO_DICHIARA_INAMISSIBILE_IL_RICORSO="04";
  public static final String COD_ESITO_RIGETTA="05";
  public static final String COD_ESITO_RETTIFICA="06";
  public static final String COD_ESITO_ACCOGLIE="07";
  public static final String COD_ESITO_DICHIARA_NDP_NLP="08";
  public static final String COD_ESITO_DICHIARA_INCOPETENZA="09";
  public static final String COD_ESITO_ACCOGLIE_FISSA_UDIENZA="10";
  public static final String COD_ESITO_CONVERTE_IN_RICORSO_IN_CASSAZIONE="11";
  public static final String COD_ESITO_CONVERTE_RICORSO_IN_OPPOSIZIONE="12";
  public static final String COD_ESITO_DICHIARA_INAMISSIBILE="13";
  
  
  public static final short SHORT_COD_ESITO_ANNULLA_SENZA_RINVIO=1;
  public static final short SHORT_COD_ESITO_ANNULLA_CON_RINVIO=2;
  public static final short SHORT_COD_ESITO_ANNULLA_PARZIALMENTE=3;
  public static final short SHORT_COD_ESITO_DICHIARA_INAMISSIBILE_IL_RICORSO=4;
  public static final short SHORT_COD_ESITO_RIGETTA=5;
  public static final short SHORT_COD_ESITO_RETTIFICA=6;
  public static final short SHORT_COD_ESITO_ACCOGLIE=7;
  public static final short SHORT_COD_ESITO_DICHIARA_NDP_NLP=8;
  public static final short SHORT_COD_ESITO_DICHIARA_INCOPETENZA=9;
  public static final short SHORT_COD_ESITO_ACCOGLIE_FISSA_UDIENZA=10;
  public static final short SHORT_COD_ESITO_CONVERTE_IN_RICORSO_IN_CASSAZIONE=11;
  public static final short SHORT_COD_ESITO_CONVERTE_RICORSO_IN_OPPOSIZIONE=12;
  public static final short SHORT_COD_ESITO_DICHIARA_INAMISSIBILE=13;
}
