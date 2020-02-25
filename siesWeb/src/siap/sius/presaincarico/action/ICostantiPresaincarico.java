package siap.sius.presaincarico.action;

import f3b.web.IWebConstants;

/**
 * <p>Title: ICostantiPresaincarico</p>
 * <p>Description: Classe di costanti di Presaincarico</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public interface ICostantiPresaincarico
{
  public static final String CAMPO_ID_MESSAGGIO = "IdMessaggio";
  public static final String CAMPO_COD_UFFICIO_MITTENTE = "CodUfficioMittente";
  public static final String CAMPO_COD_UFFICIO_DESTINATARIO = "CodUfficioDestinatario";
  public static final String CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI = "GiornoDataTrasmissioneAtti";
  public static final String CAMPO_MESE_DATA_TRASMISSIONE_ATTI = "MeseDataTrasmissioneAtti";
  public static final String CAMPO_ANNO_DATA_TRASMISSIONE_ATTI = "AnnoDataTrasmissioneAtti";
  public static final String CAMPO_GIORNO_DATA_RICEZIONE_ATTI = "GiornoDataRicezioneAtti";
  public static final String CAMPO_MESE_DATA_RICEZIONE_ATTI = "MeseDataRicezioneAtti";
  public static final String CAMPO_ANNO_DATA_RICEZIONE_ATTI = "AnnoDataRicezioneAtti";
  public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
  public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
  public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
  public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
  public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
  public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
  public static final String CAMPO_ANNO_FASCICOLO_SIEP = "AnnoFascicoloSiep";
  public static final String CAMPO_PROGR_FASCICOLO_SIEP = "ProgrFascicoloSiep";
  public static final String CAMPO_UFFICIO_FASCICOLO_SIEP = "UfficioFascicoloSiep";
  public static final String CAMPO_ANNO_FASCICOLO_SIUS = "AnnoFascicoloSius";
  public static final String CAMPO_PROGR_FASCICOLO_SIUS = "ProgrFascicoloSius";
  public static final String CAMPO_UFFICIO_FASCICOLO_SIUS = "UfficioFascicoloSius";
  public static final String CAMPO_EVE_ID_EVENTO = "EveIdEvento";
  public static final String CAMPO_ANNO_FASCICOLO_SIEPE = "AnnoFascicoloSiepe";
  public static final String CAMPO_PROGR_FASCICOLO_SIEPE = "ProgrFascicoloSiepe";
  public static final String CAMPO_UFFICIO_FASCICOLO_SIEPE = "UfficioFascicoloSiepe";

  public static final String CAMPO_DESCR_COMUNE_UFFICIO = "DescrComuneUfficio";
  public static final String CAMPO_COD_TIPO_UFFICIO = "CodTipoUfficio";
  public static final String CAMPO_INCLUDE_INCARICO = "IncludeInCarico";

  public static final String PG_LOAD_RICERCAATTISIEP                      = IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/LoadRicercaAttiSiep.jsp";
  public static final String PG_LISTA_ATTISIEP_RICEVUTI                   = IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/DettaglioMessaggiAttiRicevuti.jsp";
  public static final String PG_DETTAGLIO_ATTOSIEP_RICEVUTO               = IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/DettaglioMessaggiAttiRicevuti.jsp";
  public static final String PG_DETTAGLIO_ORDINANZA_RICEVUTA              = IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/DettaglioOrdinanzaRicevuta.jsp";
  public static final String PG_DETTAGLIO_DECRETO_RICEVUTO                = IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/DettaglioDecretoRicevuto.jsp";
  public static final String PG_LOAD_RICERCAATTISIUS                      = IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/LoadRicercaAttiSius.jsp";
  public static final String PG_LOAD_RICERCAATTIPERDATE                   = IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/LoadRicercaAttiPerDate.jsp";
  public static final String PG_DETTAGLIO_RICORSO_RICEVUTO                = IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/DettaglioRicorsoRicevuto.jsp";
  public static final String PG_LOAD_RICERCA_PROVV_SIUS                   = IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/LoadRicercaProvvedimentiSIUS.jsp";
  public static final String PG_DETTAGLIO_ATTIVITA_RICEVUTA               = IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/DettaglioAttivitaRicevuta.jsp";
  public static final String PG_DETTAGLIO_RICHIESTA_SIEPE_RICEVUTA        = IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/DettaglioRichiestaSiepeRicevuta.jsp";
  public static final String PG_LOAD_RICERCAATTISIEPE                     = IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/LoadRicercaAttiSiepe.jsp";
  public static final String PG_DETTAGLIO_RELAZIONE_ATTIVITA_RICEVUTA     = IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/DettaglioRelazioneDiAttivitaRicevuta.jsp";
  public static final String PG_DETTAGLIO_RELAZIONE_RICHIESTA_RICEVUTA    = IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/DettaglioRelazioneRichiestaSiepeRicevuta.jsp";
  public static final String PG_DETTAGLIO_TRASMISSIONE_COMPETENZA_RICEVUTA= IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/DettaglioTrasmissioneCompetenzaRicevuta.jsp";
  public static final String PG_DETTAGLIO_CONFERMA_PRESAICARICO_COMPETENZA= IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/DettaglioPresaincaricoCompetenzaRicevuta.jsp";
  public static final String PG_DETTAGLIO_REST_ATTI_COMPETENZA			  = IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/LoadRestituisciAtti.jsp";
  
  //public static final String PG_LOAD_RICERCAATTISIUS     = IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/LoadRicercaAttiSius.jsp";
  //public static final String PG_RICERCAATTI              = IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/RicercaAtti.jsp";
  //public static final String PG_RICERCAATTISIUS          = IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/RicercaAttiSius.jsp";

  //public static final String PG_LOAD_RICERCAATTIPERDATE  = IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/LoadRicercaAttiPerDate.jsp";
  //public static final String PG_RICERCAATTIPERDATE	 = IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/RicercaAttiPerDate.jsp";

  //public static final String PG_LOAD_DETTAGLIOATTI	 = IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/LoadRicercaAtti.jsp";
  //public static final String PG_LOAD_INSERISCIATTI	 = IWebConstants.ROOT_DIR + "files/siap/sius/presaincarico/LoadInserisciAtti.jsp";

}