package siap.siepe.fascicolo.action;

import f3b.web.IWebConstants;

/**
* <p>Title: ICostantiFascicoloSiepe</p>
* <p>Description: Classe di costanti di FascicoloSiepe</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiFascicoloSiepe
{
  public static final String CAMPO_ID_FASCICOLO_SIEPE  = "IdFascicoloSiepe";
  public static final String CAMPO_CHIAVE_ANNO = "ChiaveAnno";
  public static final String CAMPO_CHIAVE_PROGR = "ChiaveProgr";
  public static final String CAMPO_CHIAVE_UFFICIO = "ChiaveUfficio";
  public static final String CAMPO_NUM_UEPE = "NumUepe";
  public static final String CAMPO_ANNO_UEPE = "AnnoUepe";
  public static final String CAMPO_PROGR_UEPE = "ProgrUepe";
  public static final String CAMPO_COD_STATO_FASCICOLO = "CodStatoFascicolo";
  public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
  public static final String CAMPO_GIORNO_DATA_ISCRIZIONE = "GiornoDataIscrizione";
  public static final String CAMPO_MESE_DATA_ISCRIZIONE = "MeseDataIscrizione";
  public static final String CAMPO_ANNO_DATA_ISCRIZIONE = "AnnoDataIscrizione";
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
  public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP = "FasSieIdFascicoloSiep";
  public static final String CAMPO_FAS_SIU_ID_FASCICOLO_SIUS = "FasSiuIdFascicoloSius";
  public static final String CAMPO_COD_INCARICO = "CodIncarico";
  public static final String CAMPO_NOTE = "Note";
  public static final String CAMPO_COD_UFFICIO_MITTENTE = "CodUfficioMittente";
  public static final String CAMPO_EVE_ID_EVENTO = "EveIdEvento";
  public static final String CAMPO_COD_ATTIVITA = "CodAttivita";
  public static final String CAMPO_DESCR_ATTIVITA = "DescrizioneAttivita";



  // Costanti x Ricezione Atti
  public static final String CAMPO_GIORNO_DATA_RICEZIONE_INIZIO = "GiornoDataRicezioneInizio";
  public static final String CAMPO_MESE_DATA_RICEZIONE_INIZIO = "MeseDataRicezioneInizio";
  public static final String CAMPO_ANNO_DATA_RICEZIONE_INIZIO = "AnnoDataRicezioneInizio";
  public static final String CAMPO_GIORNO_DATA_RICEZIONE_FINE = "GiornoDataRicezioneFine";
  public static final String CAMPO_MESE_DATA_RICEZIONE_FINE = "MeseDataRicezioneFine";
  public static final String CAMPO_ANNO_DATA_RICEZIONE_FINE = "AnnoDataRicezioneFine";
  public static final String CAMPO_COD_TIPO_ATTO = "CodTipoAtto";
  public static final String CAMPO_INCLUDE_VISTO = "IncludeVisto";
  public static final String CAMPO_ANNO_FASCICOLO_SIEP = "AnnoFascicoloSiep";
  public static final String CAMPO_PROGR_FASCICOLO_SIEP = "ProgrFascicoloSiep";
  public static final String CAMPO_ANNO_FASCICOLO_SIUS = "AnnoFascicoloSius";
  public static final String CAMPO_PROGR_FASCICOLO_SIUS = "ProgrFascicoloSius";
  public static final String CAMPO_COD_TIPO_UFFICIO = "CodTipoUfficio";

  //  Costanti x Ricerca Avanzata fascicolo SIEPE.
  public static final String CAMPO_DESCR_COMUNE_UFFICIO       = "DescrComuneUfficio";
  public static final String CAMPO_CHIAVE_PROGR_INIZIALE      = "CampoChiaveProgrIniziale";
  public static final String CAMPO_CHIAVE_ANNO_INIZIALE       = "CampoChiaveAnnoIniziale";
  public static final String CAMPO_CHIAVE_PROGR_FINALE        = "CampoChiaveProgrFinale";
  public static final String CAMPO_CHIAVE_ANNO_FINALE         = "CampoChiaveAnnoFinale";
  public static final String CAMPO_GIORNO_ISCRIZIONE          = "GiornoIscrizione";
  public static final String CAMPO_MESE_ISCRIZIONE            = "MeseIscrizione";
  public static final String CAMPO_ANNO_ISCRIZIONE            = "AnnoIscrizione";
  public static final String CAMPO_GIORNO_ISCRIZIONE_INIZIALE = "GiornoIscrizioneIniziale";
  public static final String CAMPO_MESE_ISCRIZIONE_INIZIALE   = "MeseIscrizioneIniziale";
  public static final String CAMPO_ANNO_ISCRIZIONE_INIZIALE   = "AnnoIscrizioneIniziale";
  public static final String CAMPO_GIORNO_ISCRIZIONE_FINALE   = "GiornoIscrizioneFinale";
  public static final String CAMPO_MESE_ISCRIZIONE_FINALE     = "MeseIscrizioneFinale";
  public static final String CAMPO_ANNO_ISCRIZIONE_FINALE     = "AnnoIscrizioneFinale";
  public static final String CAMPO_CHIAVE_UFFICIO2            = "ChiaveUfficio2";
  public static final String CAMPO_DESCR_COMUNE_UFFICIO2      = "CampoDescrComuneUfficio2";
  public static final String CAMPO_INCLUDE_UFFICIO            = "CampoIncludeUfficio";
  public static final String CAMPO_INCLUDE_ARCHIVIATI         = "CampoIncludeArchiviati";

  //DATA DEFINIZIONE
  public static final String CAMPO_GIORNO_DATA_DEFINIZIONE = "GiornoDataDefinizione";
  public static final String CAMPO_MESE_DATA_DEFINIZIONE = "MeseDataDefinizione";
  public static final String CAMPO_ANNO_DATA_DEFINIZIONE = "AnnoDataDefinizione";
  // Campi DEFINIZIONE
  public static final String CAMPO_TIPO_DEFINIZIONE = "CampoTipoDefinizione";
  public static final String CAMPO_DESCR_DEFINIZIONE = "CampoDescrDefinizione";
  
  public static final String PG_LOAD_RICERCASOGGETTICONPROCSIEPE   = IWebConstants.ROOT_DIR + "files/siap/siepe/fascicolo/LoadRicercaSoggettiConProcSiepe.jsp";
  public static final String PG_RICERCASOGGETTICONPROCSIEPE        = IWebConstants.ROOT_DIR + "files/siap/siepe/fascicolo/RicercaSoggettiConProcSiepe.jsp";
  public static final String PG_RICERCAPROCSIEPEDELSOGGETTO        = IWebConstants.ROOT_DIR + "files/siap/siepe/fascicolo/RicercaProcSiepeDelSoggetto.jsp";
  public static final String PG_LOAD_RICERCAFASCICOLOSIEPE         = IWebConstants.ROOT_DIR + "files/siap/siepe/fascicolo/LoadRicercaFascicoloSiepe.jsp";
  public static final String PG_LOAD_DETTAGLIOFASCICOLOSIEPE       = IWebConstants.ROOT_DIR + "files/siap/siepe/fascicolo/DettaglioFascicoloSiepe.jsp";
  public static final String PG_RICERCAFASCICOLOSIEPE              = IWebConstants.ROOT_DIR + "files/siap/siepe/fascicolo/RicercaFascicoloSiepe.jsp";
  public static final String PG_LOAD_RICERCAFASSIEPEPUNTUALE       = IWebConstants.ROOT_DIR + "files/siap/siepe/fascicolo/LoadRicercaFasSiepePuntuale.jsp";
  public static final String PG_LOAD_INSERISCIFASCICOLOSIEPE       = IWebConstants.ROOT_DIR + "files/siap/siepe/fascicolo/LoadInserisciFascicoloSiepe.jsp";
  public static final String PG_LOAD_MODIFICA_FASCICOLO_SIEPE      = IWebConstants.ROOT_DIR + "files/siap/siepe/fascicolo/LoadModificaFascicoloSiepe.jsp";
  public static final String PG_LOAD_RICERCAATTIVISTIPERTIPO       = IWebConstants.ROOT_DIR + "files/siap/siepe/fascicolo/LoadRicercaAttiVistiPerTipo.jsp";
  public static final String PG_LOAD_RICERCAATTIVISTIPERSOGGETTO   = IWebConstants.ROOT_DIR + "files/siap/siepe/fascicolo/LoadRicercaAttiVistiPerSoggetto.jsp";
  public static final String PG_LOAD_DEFINIZIONE_PROCEDIMENTO      = IWebConstants.ROOT_DIR + "files/siap/siepe/fascicolo/LoadDefinizioneProcedimento.jsp";
  
  public static final String PG_LISTA_ATTI_RICEVUTI                = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/ListaAttiRicevuti.jsp";
  public static final String PG_CRITERI_DI_RICERCA                 = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/CriteriDiRicerca.jsp";
  public static final String PG_BUTTONS_PROCEDIMENTI               = IWebConstants.ROOT_DIR + "files/siap/siepe/fascicolo/ButtonsProcedimentiDelSoggetto.jsp";
  public static final String PG_SINTESI_SOGG_FASCICOLI             = IWebConstants.ROOT_DIR + "files/siap/siepe/fascicolo/SintesiProcedimentiCollegati.jsp";

  //CODIFICA STATO FASCICOLO
  public static final String COD_DEFINITO = "01";
  public static final String COD_ISCRITTO = "02";

  // Provvisorio, da cambiare path e naming e allocazione jsp ....
  public static final String PG_RICERCA_PROCEDIMENTI_SIEPE_PER_FAS_SIEP   = IWebConstants.ROOT_DIR + "files/siap/siepe/fascicolo/RicercaProcedimentiSiepePerFasSiep.jsp";

}
