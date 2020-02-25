package siap.sige.tenore.action;

import java.util.StringTokenizer;

/**
* <p>Title: ActModificaOggetto</p>
* <p>Description: Classe Action per l'aggiornamento degli Oggetti Sige in sessione.</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
public class ActModificaOggetto extends ActAggiornaTenoriSigeInSessione implements ICostantiTenoreSige {

	public String processRequest() throws Exception {
		
		//[EC] Lettura del codice contenuto
        String codContenutoSelected = getRequestStringParameter(COD_CONTENUTO_SEL);
		// Lettura Id degli Oggetti selezionati
		StringTokenizer lCodOggetto = new StringTokenizer(getRequestStringParameter(CAMPO_COD_OGGETTO_SIGE) ,"|");
		
		// Lettura Id dei Titoli Esecutivi selezionati
		StringTokenizer lIdSentenza = new StringTokenizer(getRequestStringParameter(CAMPO_SEN_ID_SENTENZA) ,"|");

		// Lettura Id dei Reati selezionati
	    StringTokenizer lIdReati = new StringTokenizer(getRequestStringParameter(CAMPO_REA_ID_REATO) ,"|");

	    removeSessionAttribute("tenori");
	    
	    // Modifica Tenori Sige
	    // MERGE v10 COLLAUDO: modifica no inserimento
	    caricaTenori(lCodOggetto, lIdSentenza, lIdReati, "M", codContenutoSelected);

		// Ritorno al punto di partenza
		String lRetPage = ritornoDopoCancellazione("Oggetto modificato", null);
		return lRetPage;
	}

}
