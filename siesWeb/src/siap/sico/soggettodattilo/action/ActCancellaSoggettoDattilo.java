package siap.sico.soggettodattilo.action;

import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggettodattilo.controller.ISoggettoDattilo;
import siap.sico.soggettodattilo.model.SoggettoDattiloModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
* <p>Title: ActCancellaSoggettoDattilo</p>
* <p>Description: Classe Action per la cancellazione del SoggettoDattilo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActCancellaSoggettoDattilo extends ActionSiap implements ICostantiSoggettoDattilo {

	/**
	* Azione di Cancellazione del SoggettoDattilo
	* @return Nome della pagina JSP da visualizzare
	* al termine dell'elaborazione
	* @throws F3BException
	*/
	public String processRequest() throws Exception {
		String lPage = IWebConstants.PG_MESSAGE;

	    UfficioModel lUfficio = this.getUfficioUtenteConnesso();
	    String codUfficio = lUfficio.getCodUfficio();
	    
		// imposta il model
		SoggettoDattiloModel lSogMod = new SoggettoDattiloModel();
		if (!isRequestParameterNullObj(CAMPO_ID_DATTILO)) {
			lSogMod.setIdDattilo(getRequestBigDecimalParameter(CAMPO_ID_DATTILO));
		}

		ISoggettoDattilo lCtrl = SICOLookupRemote.getSoggettoDattiloRemote();
		
		// Ricerca il Soggetto Dattilo
		lSogMod = lCtrl.ExRicercaSoggettoDattiloByKey(lSogMod.getIdDattilo());
		// L'utente potrà cancellare solo l'allegato inserito dal proprio ufficio.
		if(lSogMod.getCodUfficioInserimento() != null && !lSogMod.getCodUfficioInserimento().equals("")
		   && !lSogMod.getCodUfficioInserimento().equals(codUfficio)){
			throw new F3BException(F3BException.USER_MESSAGE, "Non è possibile procedere con la cancellazione. Allegato inserito da altro ufficio.");
		}
		
		// Si procede con la cancellazione del soggetto dattilo
		lCtrl.ExCancellaSoggettoDattilo(lSogMod);

		// Prepara la pagina di destinazione
		// String lRitorno = goToRitorno();
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Riferimento Dattiloscopico cancellato");

		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.sico.soggetto.action.ActLoadDettaglioSoggetto");
		lRedirigi.setParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO, getRequestStringParameter(CAMPO_COD_SOGGETTO));
		// TipoOperazione = 'A' corrispondente al pulsante "Allegare Documento"
		// presente nella pagina del Dettaglio Soggetto (visibile solo agli utenti Siep)
		// lRedirigi.setParameter("TipoOperazione", "A");
		
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		// return ritornoDopoCancellazione("Riferimento Dattiloscopico cancellato", lRitorno);
		return lPage;
	}

}