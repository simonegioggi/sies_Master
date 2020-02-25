package siap.siepe.espertoattivita.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiEspertoAttivita</p>
* <p>Description: Classe di costanti di EspertoAttivita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiEspertoAttivita
{
		 public static final String CAMPO_GIORNO_DATA_INIZIO = "GiornoDataInizio";
		 public static final String CAMPO_MESE_DATA_INIZIO = "MeseDataInizio";
		 public static final String CAMPO_ANNO_DATA_INIZIO = "AnnoDataInizio";
		 public static final String CAMPO_GIORNO_DATA_FINE = "GiornoDataFine";
		 public static final String CAMPO_MESE_DATA_FINE = "MeseDataFine";
		 public static final String CAMPO_ANNO_DATA_FINE = "AnnoDataFine";
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
		 public static final String CAMPO_ESP_ID_ESPERTO = "EspIdEsperto";
		 public static final String CAMPO_ATT_ID_ATTIVITA = "AttIdAttivita";
		 public static final String PG_LOAD_RICERCAESPERTOATTIVITA	= IWebConstants.ROOT_DIR + "files/siap/siepe/espertoattivita/LoadRicercaEspertoAttivita.jsp";
		 public static final String PG_LOAD_DETTAGLIOESPERTOATTIVITA	= IWebConstants.ROOT_DIR + "files/siap/siepe/espertoattivita/LoadRicercaEspertoAttivita.jsp";
		 public static final String PG_RICERCAESPERTOATTIVITA	= IWebConstants.ROOT_DIR + "files/siap/siepe/espertoattivita/RicercaEspertoAttivita.jsp";
		 public static final String PG_LOAD_INSERISCIESPERTOATTIVITA	= IWebConstants.ROOT_DIR + "files/siap/siepe/espertoattivita/LoadInserisciEspertoAttivita.jsp";
             public static final String PG_ELENCO_ESPERTI_ATTIVI	= IWebConstants.ROOT_DIR + "files/siap/siepe/espertoattivita/ElencoEspertiAttivi.jsp";
             public static final String JS_ESPERTO_ATTIVITA		    = IWebConstants.JS_DIR   + "EspertoAttivitaControlli.js";

}