package siap.siep.sospensione.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiSospensione</p>
* <p>Description: Classe di costanti di AnnotazioneManuale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiSospensione
{
  public static final String CAMPO_ID_SOSPENSIONE = "IdSospensione";
  public static final String CAMPO_GIORNO_DATA_INIZIO = "GiornoDataInizio";
  public static final String CAMPO_MESE_DATA_INIZIO = "MeseDataInizio";
  public static final String CAMPO_ANNO_DATA_INIZIO = "AnnoDataInizio";
  public static final String CAMPO_GIORNO_DATA_FINE = "GiornoDataFine";
  public static final String CAMPO_MESE_DATA_FINE = "MeseDataFine";
  public static final String CAMPO_ANNO_DATA_FINE = "AnnoDataFine";
  public static final String CAMPO_NUM_ANNI_RINVIO = "NumAnniRinvio";
  public static final String CAMPO_NUM_MESI_RINVIO = "NumMesiRinvio";
  public static final String CAMPO_NUM_GIORNI_RINVIO = "NumGiorniRinvio";
  public static final String CAMPO_PEN_RES_ID_PENA_RESIDUA = "PenResIdPenaResidua";
  public static final String CAMPO_NUM_ANNI_PENA_ESPIATA = "NumAnniPenaEspiata";
  public static final String CAMPO_NUM_MESI_PENA_ESPIATA = "NumMesiPenaEspiata";
  public static final String CAMPO_NUM_GIORNI_PENA_ESPIATA = "NumGiorniPenaEspiata";
  public static final String CAMPO_NUM_ANNI_PENA_RESIDUA_RECLUS = "NumAnniPenaResiduaReclus";
  public static final String CAMPO_NUM_MESI_PENA_RESIDUA_RECLUS = "NumMesiPenaResiduaReclus";
  public static final String CAMPO_NUM_GIORNI_PENA_RESIDUA_RECLUS = "NumGiorniPenaResiduaReclus";
  public static final String CAMPO_NUM_ANNI_PENA_RESIDUA_ARRES = "NumAnniPenaResiduaArres";
  public static final String CAMPO_NUM_MESI_PENA_RESIDUA_ARRES = "NumMesiPenaResiduaArres";
  public static final String CAMPO_NUM_GIORNI_PENA_RESIDUA_ARRES = "NumGiorniPenaResiduaArres";
  public static final String CAMPO_NUM_ANNI_INTERRUZIONE = "NumAnniInterruzione";
  public static final String CAMPO_NUM_MESI_INTERRUZIONE = "NumMesiInterruzione";
  public static final String CAMPO_NUM_GIORNI_INTERRUZIONE = "NumGiorniInterruzione";
  public static final String CAMPO_MULTA_ESPIATA = "MultaEspiata";
  public static final String CAMPO_AMMENDA_ESPIATA = "AmmendaEspiata";
  public static final String CAMPO_MULTA_RESIDUA = "MultaResidua";
  public static final String CAMPO_AMMENDA_RESIDUA = "AmmendaResidua";
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
  public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep";
  public static final String CAMPO_FLAG_INTERRUZIONE = "FlagInterruzione";
  public static final String CAMPO_CODICE_MAGISTRATO = "CodiceMagistrato";
  public static final String CAMPO_SEDE_TDS = "SedeTds";
  public static final String CAMPO_SEDE_UDS = "SedeUds";

  public static final String CAMPO_NUM_GIORNI_LIBANTICIPATA = "NumGiorniLibanticipata";

  public static final String CAMPO_COD_POLIZIA = "CodPolizia";
  public static final String CAMPO_SEDE_POLIZIA = "SedePolizia";
  public static final String CAMPO_NOTE_POLIZIA = "NotePolizia";

  public static final String CAMPO_COD_UGCONDANNATO = "CodUGCondannato";
  public static final String CAMPO_SEDE_UGCONDANNATO = "SedeUGCondannato";
  public static final String CAMPO_NOTE_UGCONDANNATO = "NoteUGCondannato";

  public static final String CAMPO_COD_TDS = "CodTDS";
  public static final String CAMPO_COD_GE = "CodGE";
  public static final String CAMPO_SEDE_GE = "SedeGE";

  public static final String CAMPO_GIORNO_DATA_ESPULSIONE = "GiornoDataEspulsione";
  public static final String CAMPO_MESE_DATA_ESPULSIONE = "MeseDataEspulsione";
  public static final String CAMPO_ANNO_DATA_ESPULSIONE = "AnnoDataEspulsione";



  public static final String CAMPO_RESTITUZIONE_OE = "Differimento_restituzione_oe";

  public static final String TIPO_DIFFERIMENTO = "TipoDifferimento";
  public static final String DIFFERIMENTO_PROV    = "DifferimentoProvv";
  public static final String DIFFERIMENTO_DEF     = "DifferimentoDef";
  public static final String DIFFERIMENTO_RIGETTO = "DifferimentoRigetto";
  public static final String DIFFERIMENTO_REVOCA  = "DifferimentoRevoca";

  //SOSPENSIONE
  public static final String PG_LOAD_INSERISCI_SOSPENSIONE = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadInserisciSospensione.jsp";
  public static final String PG_LOAD_INSERISCI_SOSPENSIONE_DECISIONI_SORVEGLIANZA = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadInserisciSospensioneDecisioniSorv.jsp";
  public static final String PG_LOAD_DETTAGLIO_SOSPENSIONE_DECISIONI_SORVEGLIANZA = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadDettaglioSospensioneDecisioniSorv.jsp";

  public static final String PG_LOAD_DETTAGLIOSOSPENSIONE = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/DettaglioSospensione.jsp";
  public static final String PG_LOAD_INSERISCI_SOSPENSIONE_ART47 = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadInserisciSospensioneArt47.jsp";
  public static final String PG_LOAD_DETTAGLIO_SOSPENSIONE_ART47 = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadDettaglioSospensioneArt47.jsp";
  public static final String PG_LOAD_DETTAGLIO_SOSPENSIONE_ART91 = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadDettaglioSospensioneArt91.jsp";

  public static final String PG_LOAD_INSERISCI_SOSPENSIONE_ART91 = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadInserisciSospensioneArt91.jsp";
  public static final String PG_LOAD_INSERISCI_SOSPENSIONE_PENA = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadInserisciSospensionePena.jsp";
  public static final String PG_LOAD_DETTAGLIO_SOSPENSIONE_PENA = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadDettaglioSospensionePena.jsp";

  public static final String PG_LOAD_INSERISCI_SOSPENSIONE_DIFFERIMENTO = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadInserisciSospensioneDifferimento.jsp";
  public static final String PG_LOAD_DETTAGLIOSOSPENSIONE_DIFFERIMENTO = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/DettaglioSospensioneDifferimento.jsp";
  public static final String PG_LOAD_INSERISCI_NOTIFICHE_DIFFERIMENTO = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadInserisciNotificheDifferimento.jsp";
  public static final String PG_LOAD_DETTAGLIO_NOTIFICHE_DIFFERIMENTO = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadDettaglioNotificheDifferimento.jsp";

  public static final String PG_LOAD_INSERISCI_SOSPENSIONE_INTERRUZIONE = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadInserisciInterruzione.jsp";
  public static final String PG_LOAD_DETTAGLIOSOSPENSIONE_INTERRUZIONE = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/DettaglioInterruzione.jsp";

  public static final String PG_LOAD_DETTAGLIO_INTERRUZIONE_STAMPE = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/DettaglioInterruzionePerStampe.jsp";
  public static final String PG_LOAD_INSERISCI_SOSPENSIONE_OE = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadInserisciSospensioneOE.jsp";

  public static final String PG_LOAD_DETTAGLIO_SOSPENSIONE_OE = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadDettaglioSospensioneOE.jsp";
  public static final String PG_LOAD_DETTAGLIO_SOSPENSIONE_OS = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadDettaglioSospensioneOS.jsp";

  public static final String PG_LOAD_INSERISCI_SOSPENSIONE_ESPULSIONE = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadInserisciEspulsione.jsp";
  public static final String PG_LOAD_INSERISCI_COMUNICAZIONE_ESPULSIONE = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadInserisciComunicazioneEspulsione.jsp";

  public static final String PG_LOAD_DETTAGLIOSOSPENSIONE_ESPULSIONE = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/DettaglioEspulsione.jsp";
  public static final String PG_LOAD_INSERISCI_NOTIFICHE_ESPULSIONE = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadInserisciNotificheEspulsione.jsp";
  public static final String PG_LOAD_DETTAGLIO_NOTIFICHE_ESPULSIONE = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadDettaglioNotificheEspulsione.jsp";

  public static final String PG_LOAD_GRIGLIA_ESPULSIONE  = IWebConstants.ROOT_DIR + "files/siap/siep/sospensione/LoadGrigliaEspulsione.jsp";
  public static final String PG_LOAD_INSERISCI_CONCESSIONE_ESPULSIONE = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadInserisciEspulsioneConcessione.jsp";
  public static final String PG_LOAD_DETTAGLIO_CONCESSIONE_ESPULSIONE = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/DettaglioConcessioneEspulsione.jsp";
  public static final String PG_LOAD_INSERISCI_RINUNCIA_OPPOSIZIONE_ESPULSIONE = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadInserisciRinunciaOpEspulsione.jsp";
  public static final String PG_LOAD_DETTAGLIO_RINUNCIA_OPPOSIZIONE_ESPULSIONE = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/DettaglioRinunciaOpEspulsione.jsp";
  public static final String PG_LOAD_INSERISCI_ACCOGLIMENTO_OPPOSIZIONE_ESPULSIONE = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadInserisciAccoglimentoOpEspulsione.jsp";
  public static final String PG_LOAD_DETTAGLIO_ACCOGLIMENTO_OPPOSIZIONE_ESPULSIONE = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/DettaglioAccoglimentoOpEspulsione.jsp";
  public static final String PG_LOAD_INSERISCI_RIGETTO_OPPOSIZIONE_ESPULSIONE = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadInserisciRigettoOpEspulsione.jsp";
  public static final String PG_LOAD_DETTAGLIO_RIGETTO_OPPOSIZIONE_ESPULSIONE = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/DettaglioRigettoOpEspulsione.jsp";

  public static final String PG_LOAD_INSERISCI_DECRETO_SOSPENSIONE = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadInserisciDecretoSospensione.jsp";
  public static final String PG_LOAD_DETTAGLIO_DECRETO_SOSPENSIONE = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadDettaglioDecretoSospensione.jsp";

  public static final String PG_LOAD_INSERISCI_SOSPENSIONE_ESEC_PM = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/LoadInserisciSospensioneEsecPenaDispPm.jsp";
  public static final String PG_LOAD_DETTAGLIO_SOSPENSIONE_ESEC_PM = IWebConstants.ROOT_DIR + "/files/siap/siep/sospensione/DettaglioSospensioneEsecPenaDispPm.jsp";

  public static final String PG_LOAD_TRASFERISCI_PROVVEDIMENTO_DS = IWebConstants.ROOT_DIR + "files/siap/siep/sospensione/LoadTrasferisciProvvedimentoDS.jsp";

}