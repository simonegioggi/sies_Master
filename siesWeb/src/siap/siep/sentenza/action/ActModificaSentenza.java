package siap.siep.sentenza.action;


/**
 * <p>Title: ActModificaSentenza</p>
 * <p>Description: Classe Action per la modifica di Sentenza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
import f3b.util.F3BException;

public class ActModificaSentenza extends ActInserisciSentenza implements ICostantiSentenza
{
	/**
	 * Azione di Modifica del Sentenza
	 * @return Nome della pagina JSP da visualizzare
	 * al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception
	{
		// Setta il flag che segnala operazione di Modifica
		mModifica = true;
		
		return super.processRequest();
	}
}