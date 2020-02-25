package siap.regesies.regereato.action;

import siap.regesies.action.ICostantiRegeSies;
import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiRegeReato</p>
* <p>Description: Classe di costanti di RegeReato</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiRegeReato extends ICostantiRegeSies
{
		 public static final String CAMPO_ID_FILE = "IdFile";
		 public static final String CAMPO_COD_TIPO_REATO = "CodTipoReato";
		 public static final String CAMPO_GIORNO_DATA_REATO = "GiornoDataReato";
		 public static final String CAMPO_MESE_DATA_REATO = "MeseDataReato";
		 public static final String CAMPO_ANNO_DATA_REATO = "AnnoDataReato";
		 public static final String CAMPO_PROGR_NUMERO_MANUALE = "ProgrNumeroManuale";
		 public static final String CAMPO_PROGR_REATO = "ProgrReato";
		 public static final String CAMPO_PROGR_CIRCOSTANZA = "ProgrCircostanza";
		 public static final String CAMPO_GIORNO_DATA_INIZIO = "GiornoDataInizio";
		 public static final String CAMPO_MESE_DATA_INIZIO = "MeseDataInizio";
		 public static final String CAMPO_ANNO_DATA_INIZIO = "AnnoDataInizio";
		 public static final String CAMPO_ANNO_INIZIO = "AnnoInizio";
		 public static final String CAMPO_MESE_INIZIO = "MeseInizio";
		 public static final String CAMPO_GIORNO_INIZIO = "GiornoInizio";
		 public static final String CAMPO_GIORNO_DATA_FINE = "GiornoDataFine";
		 public static final String CAMPO_MESE_DATA_FINE = "MeseDataFine";
		 public static final String CAMPO_ANNO_DATA_FINE = "AnnoDataFine";
		 public static final String CAMPO_ANNO_FINE = "AnnoFine";
		 public static final String CAMPO_MESE_FINE = "MeseFine";
		 public static final String CAMPO_GIORNO_FINE = "GiornoFine";
		 public static final String CAMPO_COD_PERIODO_CONSUMAZIONE = "CodPeriodoConsumazione";
		 public static final String CAMPO_DESC_LUOGO = "DescLuogo";
		 public static final String CAMPO_COD_FONTE = "CodFonte";
		 public static final String CAMPO_ANNO_FONTE = "AnnoFonte";
		 public static final String CAMPO_NUMERO_FONTE = "NumeroFonte";
		 public static final String CAMPO_COD_SOTTONUMERAZIONE = "CodSottonumerazione";
		 public static final String CAMPO_COMMA = "Comma";
		 public static final String CAMPO_LETTERA = "Lettera";
		 public static final String CAMPO_NUMERO = "Numero";
		 public static final String CAMPO_ARTICOLO = "Articolo";
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
		 public static final String PG_LOAD_RICERCAREGEREATO	= IWebConstants.ROOT_DIR + "files/siap/regesies/regereato/LoadRicercaRegeReato.jsp";
		 public static final String PG_LOAD_DETTAGLIOREGEREATO	= IWebConstants.ROOT_DIR + "files/siap/regesies/regereato/DettaglioRegeReato.jsp";
		 public static final String PG_RICERCAREGEREATO	= IWebConstants.ROOT_DIR + "files/siap/regesies/regereato/RicercaRegeReato.jsp";
		 public static final String PG_LOAD_INSERISCIREGEREATO	= IWebConstants.ROOT_DIR + "files/siap/regesies/regereato/ModificaRegeReato.jsp";

}