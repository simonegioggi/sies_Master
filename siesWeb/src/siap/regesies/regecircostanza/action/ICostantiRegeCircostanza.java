package siap.regesies.regecircostanza.action;

import siap.regesies.action.ICostantiRegeSies;
import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiRegeCircostanza</p>
* <p>Description: Classe di costanti di RegeCircostanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
*/
public interface ICostantiRegeCircostanza extends ICostantiRegeSies
{
		 public static final String CAMPO_ID_FILE = "IdFile";
		 public static final String CAMPO_PROGR_CIRCOSTANZA = "ProgrCircostanza";
		 public static final String CAMPO_COD_TIPO_CIRCOSTANZA = "CodTipoCircostanza";
		 public static final String CAMPO_COD_FONTE = "CodFonte";
		 public static final String CAMPO_ANNO_FONTE = "AnnoFonte";
		 public static final String CAMPO_NUMERO_FONTE = "NumeroFonte";
		 public static final String CAMPO_COD_SOTTONUMERAZIONE = "CodSottonumerazione";
		 public static final String CAMPO_COMMA = "Comma";
		 public static final String CAMPO_LETTERA = "Lettera";
		 public static final String CAMPO_NUMERO = "Numero";
     public static final String CAMPO_ARTICOLO =   "Articolo";
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

     public static final String PG_DETTAGLIOREGECIRCOSTANZA	= IWebConstants.ROOT_DIR + "files/siap/regesies/regecircostanza/DettaglioRegeCircostanza.jsp";
		 public static final String PG_RICERCAREGECIRCOSTANZA	= IWebConstants.ROOT_DIR + "files/siap/regesies/regecircostanza/RicercaRegeCircostanza.jsp";
		 public static final String PG_MODIFICAREGECIRCOSTANZA	= IWebConstants.ROOT_DIR + "files/siap/regesies/regecircostanza/ModificaRegeCircostanza.jsp";
}