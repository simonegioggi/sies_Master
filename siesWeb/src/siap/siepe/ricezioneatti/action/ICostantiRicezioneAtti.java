package siap.siepe.ricezioneatti.action;

import f3b.web.IWebConstants;

/**
 * <p>Title: ICostantiRicezioneAtti</p>
 * <p>Description: Classe di costanti di Ricezione Atti</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public interface ICostantiRicezioneAtti
{
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
  public static final String CAMPO_DESCR_COMUNE_UFFICIO = "DescrComuneUfficio";
  public static final String CAMPO_COD_TIPO_UFFICIO = "CodTipoUfficio";
  public static final String CAMPO_STATO_RICEZIONE = "statoRicezione";
  
  public static final String CAMPO_CHIAVE_ANNO_SIEPE = "ChiaveAnnoSiepe";
  public static final String CAMPO_CHIAVE_PROGR_SIEPE = "ChiaveProgrSiepe";
  
  public static final String PG_TOOLBAR_HEADER_PROVVEDIMENTO                = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/toolbar_ProvvedimentoRicevuto.jsp";
  public static final String PG_TOOLBAR_HEADER_RICHIESTA_REL                = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/toolbar_RichiestaRelRicevuta.jsp";
  public static final String PG_TOOLBAR_HEADER_RICHIESTA_RICEVUTA           = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/toolbar_RichiestaRicevuta.jsp";
  public static final String PG_TOOLBAR_HEADER_RELAZIONE_RICHIESTA_RICEVUTA = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/toolbar_RelazioneRichiestaRicevuta.jsp";
  public static final String PG_TOOLBAR_HEADER_ATTIVITA_RICEVUTA            = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/toolbar_AttivitaRicevuta.jsp";
  public static final String PG_TOOLBAR_HEADER_RELAZIONE_ATTIVITA_RICEVUTA  = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/toolbar_RelazioneAttivitaRicevuta.jsp";
  public static final String PG_DETTAGLIO_PROVVEDIMENTO_RICEVUTO            = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/DettaglioProvvedimentoRicevuto.jsp";
  public static final String PG_DETTAGLIO_RICHIESTA_REL_RICEVUTA            = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/DettaglioRichiestaRelRicevuta.jsp";
  public static final String PG_DETTAGLIO_RICHIESTA_RICEVUTA                = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/DettaglioRichiestaRicevuta.jsp";
  public static final String PG_DETTAGLIO_RELAZIONE_RICHIESTA_RICEVUTA      = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/DettaglioRelazioneRichiestaRicevuta.jsp";
  public static final String PG_DETTAGLIO_ATTIVITA_RICEVUTA                 = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/DettaglioAttivitaRicevuta.jsp";
  public static final String PG_DETTAGLIO_RELAZIONE_ATTIVITA_RICEVUTA       = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/DettaglioRelazioneAttivitaRicevuta.jsp";
  
  public static final String PG_LOAD_RICERCAATTIPERTIPOEDATE                = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/LoadRicercaAttiPerTipoeDate.jsp";
  public static final String PG_LISTA_ATTI_RICEVUTI_PERTIPOEDATE            = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/ListaAttiRicevutiPerTipoeDate.jsp";
  public static final String PG_LOAD_RICERCAATTIPERSOGGETTO                 = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/LoadRicercaAttiPerSoggetto.jsp";
  public static final String PG_LISTA_ATTI_RICEVUTI_PERSOGGETTO             = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/ListaAttiRicevutiPerSoggetto.jsp";
  public static final String PG_LISTA_ATTI_RICEVUTI                         = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/ListaAttiRicevuti.jsp";
  public static final String PG_LOAD_RICERCAATTIPERSIEPESIUS                = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/LoadRicercaAttiPerSIEPeSIUS.jsp";
  public static final String PG_LOAD_RICERCAATTIUEPE                        = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/LoadRicercaAttiUEPE.jsp";
  public static final String PG_SINTESI_PROCEDIMENTO_SIUS_RICEVUTO          = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/SintesiProcedimentoSiusRicevuto.jsp";
  
  public static final String PG_CRITERI_DI_RICERCA                          = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/CriteriDiRicerca.jsp";
  public static final String PG_CRUSCOTTO                                   = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/Cruscotto.jsp";
  public static final String PG_RESTITUZIONE_ATTO_RICEVUTO                  = IWebConstants.ROOT_DIR + "files/siap/siepe/ricezioneatti/LoadRestituzioneAttoRicevuto.jsp";
}