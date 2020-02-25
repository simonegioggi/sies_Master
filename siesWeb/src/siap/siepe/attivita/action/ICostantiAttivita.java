package siap.siepe.attivita.action;

import f3b.web.IWebConstants;

/**
* <p>Title: ICostantiAttivita</p>
* <p>Description: Classe di costanti di Attivita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public interface ICostantiAttivita
{
  public static final String CAMPO_ID_ATTIVITA = "IdAttivita";
  public static final String CAMPO_GIORNO_DATA_INIZIO = "GiornoDataInizio";
  public static final String CAMPO_MESE_DATA_INIZIO = "MeseDataInizio";
  public static final String CAMPO_ANNO_DATA_INIZIO = "AnnoDataInizio";
  public static final String CAMPO_GIORNO_DATA_CHIUSURA = "GiornoDataChiusura";
  public static final String CAMPO_MESE_DATA_CHIUSURA = "MeseDataChiusura";
  public static final String CAMPO_ANNO_DATA_CHIUSURA = "AnnoDataChiusura";
  public static final String CAMPO_COD_TIPO_ATTIVITA = "CodTipoAttivita";
  public static final String CAMPO_NOTE = "Note";
  public static final String CAMPO_DOC_BLOB = "DocBlob";
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
  public static final String CAMPO_FAS_SIE_ID_FAS_SIEPE = "FasSieIdFasSiepe";
  public static final String CAMPO_ASS_SOC_ID_ASS_SOCIALE = "AssSocIdAssSociale";
  public static final String CAMPO_COD_ESITO = "CodEsitoAttivita";
  public static final String CAMPO_NOTA_CHIUSURA = "NotaChiusura";

  public static final String PG_LOAD_RICERCAATTIVITA	= IWebConstants.ROOT_DIR + "files/siap/siepe/attivita/LoadRicercaAttivita.jsp";
  public static final String PG_LOAD_DETTAGLIOATTIVITA	= IWebConstants.ROOT_DIR + "files/siap/siepe/attivita/DettaglioAttivita.jsp";
  public static final String PG_RICERCAATTIVITA	= IWebConstants.ROOT_DIR + "files/siap/siepe/attivita/RicercaAttivita.jsp";
  public static final String PG_LOAD_INSERISCIATTIVITA	= IWebConstants.ROOT_DIR + "files/siap/siepe/attivita/InserisciAttivita.jsp";
  public static final String PG_ELENCO_ATTIIVITA	= IWebConstants.ROOT_DIR + "files/siap/siepe/attivita/ElencoAttivita.jsp";
  public static final String PG_LOAD_TRASFERISCIATTIVITA	= IWebConstants.ROOT_DIR + "files/siap/siepe/attivita/TrasferisciAttivita.jsp";
  public static final String PG_LOAD_CHIUSURA_ATTIVITA	= IWebConstants.ROOT_DIR + "files/siap/siepe/attivita/ChiusuraAttivita.jsp";
  public static final String PG_LOAD_GESTIONE_ESPERTI	= IWebConstants.ROOT_DIR + "files/siap/siepe/espertoattivita/ElencoEspertiXAttivita.jsp";
  public static final String PG_ELENCO_ASSISTENTI_SOCIALI	= IWebConstants.ROOT_DIR + "files/siap/siepe/assistentesocialeattivita/ElencoAssistentiSocXAttivita.jsp";

}
