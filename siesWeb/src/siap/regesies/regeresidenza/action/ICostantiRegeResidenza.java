package siap.regesies.regeresidenza.action;

import siap.regesies.action.ICostantiRegeSies;
import f3b.web.IWebConstants;

/**
* <p>Title: ICostantiRegeResidenza</p>
* <p>Description: Classe di costanti di RegeResidenza</p>
*/
public interface ICostantiRegeResidenza extends ICostantiRegeSies
{
		 public static final String CAMPO_ID_FILE = "IdFile";
		 public static final String CAMPO_COD_STATO = "CodStato";
		 public static final String CAMPO_COD_PROVINCIA = "CodProvincia";
		 public static final String CAMPO_COD_COMUNE = "CodComune";
     public static final String CAMPO_DESCR_COMUNE = "DescrComune";
		 public static final String CAMPO_CAP = "Cap";
		 public static final String CAMPO_INDIRIZZO = "Indirizzo";
		 public static final String CAMPO_COD_TIPO_RESIDENZA = "CodTipoResidenza";
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
		 public static final String CAMPO_DESC_COMUNE_ESTERO = "DescComuneEstero";
		 public static final String PG_DETTAGLIO_REGERESIDENZA	= IWebConstants.ROOT_DIR + "files/siap/regesies/regeresidenza/DettaglioRegeResidenza.jsp";
		 public static final String PG_LOAD_DETTAGLIOREGERESIDENZA	= IWebConstants.ROOT_DIR + "files/siap/regesies/regeresidenza/LoadRicercaRegeResidenza.jsp";
		 public static final String PG_RICERCAREGERESIDENZA	= IWebConstants.ROOT_DIR + "files/siap/regesies/regeresidenza/RicercaRegeResidenza.jsp";
		 public static final String PG_LOAD_INSERISCIREGERESIDENZA	= IWebConstants.ROOT_DIR + "files/siap/regesies/regeresidenza/LoadInserisciRegeResidenza.jsp";
     public static final String PG_LOAD_MODIFICA_REGERESIDENZA	= IWebConstants.ROOT_DIR + "files/siap/regesies/regeresidenza/LoadModificaRegeResidenza.jsp";

}