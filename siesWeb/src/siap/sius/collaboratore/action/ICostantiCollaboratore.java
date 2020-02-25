package siap.sius.collaboratore.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiCollaboratore</p>
* <p>Description: Classe di costanti di Collaboratore</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 3.0
*/

public interface ICostantiCollaboratore
{

	public static final String CHK_DATA_INIZIO_NULLABLE = "ChkDataInizioNullable";
	public static final String CAMPO_GIORNO_DATA_INIZIO = "GiornoDataInizio";
	public static final String CAMPO_MESE_DATA_INIZIO = "MeseDataInizio";
	public static final String CAMPO_ANNO_DATA_INIZIO = "AnnoDataInizio";
	public static final String CAMPO_GIORNO_DATA_FINE = "GiornoDataFine";
	public static final String CAMPO_MESE_DATA_FINE = "MeseDataFine";
	public static final String CAMPO_ANNO_DATA_FINE = "AnnoDataFine";
	public static final String CAMPO_ID_COLLABORATORE = "IdCollaboratore";
	public static final String CAMPO_ID_FASCICOLO_SIUS = "IdFascicoloSius";
	public static final String CAMPO_COD_UFFICIO = "codUfficio";
	public static final String CAMPO_COD_UFFICIO_INS = "codUfficioIns";
	public static final String CAMPO_COD_UFFICIO_AGG = "codUfficioAgg";
	public static final String CAMPO_COD_OPERATORE_INS = "codOperatoreIns";
	public static final String CAMPO_COD_OPERATORE_AGG = "codOperatoreAgg";
	
	// bottone collegato a funzioni LINK (inserimento)
	public static final String PG_BUTTONS_LINK         = IWebConstants.ROOT_DIR + "files/siap/sius/collaboratore/BottoniLink.jsp";
	// Dettaglio Storico del Collaboratore
	public static final String PG_LOAD_DETTAGLIOCOLLABORATORE	= IWebConstants.ROOT_DIR + "files/siap/sius/collaboratore/DettaglioCollaboratore.jsp";
	// Form di Input o Modifica
	public static final String PG_LOAD_MODIFICACOLLABORATORE	= IWebConstants.ROOT_DIR + "files/siap/sius/collaboratore/LoadInserisciCollaboratore.jsp";

}