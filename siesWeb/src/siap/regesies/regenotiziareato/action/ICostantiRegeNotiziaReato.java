package siap.regesies.regenotiziareato.action;

import siap.regesies.action.ICostantiRegeSies;
import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiRegeNotiziaReato</p>
* <p>Description: Classe di costanti di RegeNotiziaReato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiRegeNotiziaReato extends ICostantiRegeSies
{
		 public static final String CAMPO_ID_FILE = "IdFile";
		 public static final String CAMPO_PROGR_NOTIZIA = "ProgrNotizia";
		 public static final String CAMPO_GIORNO_DATA_PERVENIMENTO = "GiornoDataPervenimento";
		 public static final String CAMPO_MESE_DATA_PERVENIMENTO = "MeseDataPervenimento";
		 public static final String CAMPO_ANNO_DATA_PERVENIMENTO = "AnnoDataPervenimento";
		 public static final String CAMPO_ACQUISIZIONE_DIRETTA = "AcquisizioneDiretta";
		 public static final String CAMPO_GIORNO_DATA_FATTO = "GiornoDataFatto";
		 public static final String CAMPO_MESE_DATA_FATTO = "MeseDataFatto";
		 public static final String CAMPO_ANNO_DATA_FATTO = "AnnoDataFatto";
		 public static final String CAMPO_COD_FONTE = "CodFonte";
		 public static final String CAMPO_TIPO_FONTE = "TipoFonte";
		 public static final String CAMPO_COD_COMUNE_FONTE = "CodComuneFonte";
		 public static final String CAMPO_NUM_REG_AUTORITA = "NumRegAutorita";
		 public static final String CAMPO_LUOGO_PROVENIENZA = "LuogoProvenienza";
		 public static final String CAMPO_GIORNO_DATA_ACQUISIZIONE = "GiornoDataAcquisizione";
		 public static final String CAMPO_MESE_DATA_ACQUISIZIONE = "MeseDataAcquisizione";
		 public static final String CAMPO_ANNO_DATA_ACQUISIZIONE = "AnnoDataAcquisizione";
		 public static final String CAMPO_NUMERO_RICEVUTA = "NumeroRicevuta";
		 public static final String CAMPO_DESCRIZIONE_FONTE = "DescrizioneFonte";
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
		 public static final String PG_MODIFICAREGENOTIZIAREATO	= IWebConstants.ROOT_DIR + "files/siap/regesies/regenotiziareato/ModificaRegeNotiziaReato.jsp";
		 public static final String PG_DETTAGLIOREGENOTIZIAREATO	= IWebConstants.ROOT_DIR + "files/siap/regesies/regenotiziareato/DettaglioRegeNotiziaReato.jsp";
		 public static final String PG_RICERCAREGENOTIZIAREATO	= IWebConstants.ROOT_DIR + "files/siap/regesies/regenotiziareato/RicercaRegeNotiziaReato.jsp";
		 public static final String PG_LOAD_INSERISCIREGENOTIZIAREATO	= IWebConstants.ROOT_DIR + "files/siap/regesies/regenotiziareato/LoadInserisciRegeNotiziaReato.jsp";
}