package siap.sige.sezione.action;

import f3b.web.IWebConstants;

/**
* <p>Title: ICostantiSezione</p>
* <p>Description: Classe di costanti di Sezione</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
public interface ICostantiSezione {

	public static final String CAMPO_ID_SEZIONE = "IdSezione";
	public static final String CAMPO_CODICE = "Codice";
	public static final String CAMPO_DESCRIZIONE = "Descrizione";
	public static final String CAMPO_COD_UFFICIO_APPARTENENZA = "CodUfficioAppartenenza";
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

	public static final String PG_LOAD_RICERCASEZIONE = IWebConstants.ROOT_DIR + "files/siap/sige/sezione/LoadRicercaSezione.jsp";
	public static final String PG_LOAD_DETTAGLIOSEZIONE = IWebConstants.ROOT_DIR + "files/siap/sige/sezione/DettaglioSezione.jsp";
	public static final String PG_RICERCASEZIONE = IWebConstants.ROOT_DIR + "files/siap/sige/sezione/RicercaSezione.jsp";
	public static final String PG_LOAD_INSERISCISEZIONE = IWebConstants.ROOT_DIR + "files/siap/sige/sezione/LoadInserisciSezione.jsp";
	public static final String PG_LOAD_RICERCA_SEZIONE_LISTA = IWebConstants.ROOT_DIR + "files/siap/sige/sezione/LoadRicercaSezioneLista.jsp";
	public static final String PG_RICERCA_SEZIONE_LISTA = IWebConstants.ROOT_DIR + "files/siap/sige/sezione/RicercaSezioneLista.jsp";

}